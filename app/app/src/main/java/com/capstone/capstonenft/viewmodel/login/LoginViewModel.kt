package com.capstone.capstonenft.viewmodel.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.capstonenft.dto.LoginRequest
import com.capstone.capstonenft.dto.LoginResponse
import com.capstone.capstonenft.protocol.LoginProtocol
import com.capstone.capstonenft.system.net.HttpResponsable
import com.capstone.capstonenft.system.net.NetworkManager
import com.capstone.capstonenft.system.net.ProtocolFactory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class LoginViewModel: ViewModel() {
    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse : LiveData<LoginResponse> get() = _loginResponse

    fun login(id:String, pw: String){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            val token = task.result
            val login = LoginRequest(id,pw,token)

            val protocol: LoginProtocol = ProtocolFactory.create(LoginProtocol::class.java)

            protocol.setJsonRequestBody(login)
            protocol.setHttpResponsable(object : HttpResponsable<LoginResponse> {
                override fun onResponse(res: LoginResponse) {
                    _loginResponse.value = res
                }
                override fun onFailure(nError: Int, strMsg: String) {
                    super.onFailure(nError, strMsg)
                }
            })
            NetworkManager.getInstance().asyncRequest(protocol)
        })
    }
}