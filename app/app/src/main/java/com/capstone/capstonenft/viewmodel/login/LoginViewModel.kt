package com.capstone.capstonenft.viewmodel.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.capstonenft.NFT
import com.capstone.capstonenft.dto.Klay
import com.capstone.capstonenft.dto.LoginRequest
import com.capstone.capstonenft.dto.LoginResponse
import com.capstone.capstonenft.dto.RegisterResponse
import com.capstone.capstonenft.protocol.KlayProtocol
import com.capstone.capstonenft.protocol.LoginProtocol
import com.capstone.capstonenft.protocol.RegisterProtocol
import com.capstone.capstonenft.system.net.HttpResponsable
import com.capstone.capstonenft.system.net.NetworkManager
import com.capstone.capstonenft.system.net.ProtocolFactory
import com.capstone.capstonenft.system.utils.Trace
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import java.io.File

class LoginViewModel : ViewModel() {
    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> get() = _loginResponse

    private val _loginFailure = MutableLiveData<Boolean>()
    val loginFailure: LiveData<Boolean> get() = _loginFailure

    private val _register = MutableLiveData<RegisterResponse>()
    val register: LiveData<RegisterResponse> get() = _register

    fun login(id: String, pw: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            val token = task.result
            val login = LoginRequest(id, pw, token)

            val protocol: LoginProtocol = ProtocolFactory.create(LoginProtocol::class.java)

            protocol.setJsonRequestBody(login)
            protocol.setHttpResponsable(object : HttpResponsable<LoginResponse> {
                override fun onResponse(res: LoginResponse) {
                    Trace.error("onResponse = $res")
                    NFT.instance.loginResponse = res
                    getKlay()
                }

                override fun onFailure(nError: Int, strMsg: String) {
                    Trace.error("onFailure = $nError $strMsg")
                    _loginFailure.postValue(true)
                    super.onFailure(nError, strMsg)
                }
            })
            NetworkManager.getInstance().asyncRequest(protocol)
        })
    }

    fun register(id: String, pw: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            val token = task.result
            val login = LoginRequest(id, pw, token)

            val protocol: RegisterProtocol = ProtocolFactory.create(RegisterProtocol::class.java)

            protocol.setJsonRequestBody(login)
            protocol.setHttpResponsable(object : HttpResponsable<RegisterResponse> {
                override fun onResponse(res: RegisterResponse) {
                    Trace.error("onResponse = $res")
                    login(id, pw)
                }

                override fun onFailure(nError: Int, strMsg: String) {
                    Trace.error("onFailure = $nError $strMsg")
                    super.onFailure(nError, strMsg)
                }
            })
            NetworkManager.getInstance().asyncRequest(protocol)
        })
    }

    fun getKlay() {
        val protocol: KlayProtocol = ProtocolFactory.create(KlayProtocol::class.java)
        protocol.PATH = "user/${NFT.instance.loginResponse.name}/balance"

        protocol.setHttpResponsable(object : HttpResponsable<Klay> {
            override fun onResponse(res: Klay) {
                NFT.instance.klay = res
                _loginResponse.postValue(NFT.instance.loginResponse)
            }

            override fun onFailure(nError: Int, strMsg: String) {
                Trace.error("onFailure = $nError $strMsg")
                super.onFailure(nError, strMsg)
            }
        })
        NetworkManager.getInstance().asyncRequest(protocol)
    }

    fun uploadFile(file: File) {
//        val protocol: UploadProtocol = ProtocolFactory.create(UploadProtocol::class.java)
//
//        protocol.setJsonRequestBody(file)
//        Trace.error("header = ${protocol.getRequestHeaderMap()}")
//        Trace.error("data = ${protocol.getJsonRequestBody()}")
//        protocol.setHttpResponsable(object : HttpResponsable<BaseResponse> {
//            override fun onResponse(res: BaseResponse) {
//                Trace.error("onResponse = $res")
//            }
//
//            override fun onFailure(nError: Int, strMsg: String) {
//                Trace.error("onFailure = $nError $strMsg")
//                super.onFailure(nError, strMsg)
//            }
//        })
//        NetworkManager.getInstance().asyncRequest(protocol)
    }
}