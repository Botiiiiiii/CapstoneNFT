package com.capstone.capstonenft.viewmodel.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.capstonenft.dto.BaseResponse
import com.capstone.capstonenft.dto.LoginRequest
import com.capstone.capstonenft.dto.LoginResponse
import com.capstone.capstonenft.dto.RegisterResponse
import com.capstone.capstonenft.protocol.LoginProtocol
import com.capstone.capstonenft.protocol.RegisterProtocol
import com.capstone.capstonenft.protocol.Upload
import com.capstone.capstonenft.protocol.UploadProtocol
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
                    _loginResponse.value = res
                }

                override fun onFailure(nError: Int, strMsg: String) {
                    Trace.error("onFailure = $nError $strMsg")
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
                    _register.value = res
                }

                override fun onFailure(nError: Int, strMsg: String) {
                    Trace.error("onFailure = $nError $strMsg")
                    super.onFailure(nError, strMsg)
                }
            })
            NetworkManager.getInstance().asyncRequest(protocol)
        })
    }

    fun uploadFile(file: File) {
        val protocol: UploadProtocol = ProtocolFactory.create(UploadProtocol::class.java)
        var upload:Map<String, File> = mapOf(Pair("file", file))

        protocol.setJsonRequestBody(file)
        protocol.setHttpResponsable(object : HttpResponsable<BaseResponse> {
            override fun onResponse(res: BaseResponse) {
                Trace.error("onResponse = $res")
            }

            override fun onFailure(nError: Int, strMsg: String) {
                Trace.error("onFailure = $nError $strMsg")
                super.onFailure(nError, strMsg)
            }
        })
        NetworkManager.getInstance().asyncRequest(protocol)
    }
}