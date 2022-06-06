package com.capstone.capstonenft.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.capstonenft.NFT
import com.capstone.capstonenft.dto.*
import com.capstone.capstonenft.protocol.BuyProtocol
import com.capstone.capstonenft.protocol.OwnerProtocol
import com.capstone.capstonenft.protocol.ProfileProtocol
import com.capstone.capstonenft.protocol.TokenRegisterProtocol
import com.capstone.capstonenft.system.net.HttpResponsable
import com.capstone.capstonenft.system.net.NetworkManager
import com.capstone.capstonenft.system.net.ProtocolFactory
import com.capstone.capstonenft.system.utils.Trace

class DetailViewModel : ViewModel() {
    private val _message = MutableLiveData<Message>()
    val message:LiveData<Message> get() = _message

    private val _owner = MutableLiveData<Owner>()
    val owner:LiveData<Owner> get() = _owner

    fun setProfile(src:String) {
        val protocol: ProfileProtocol =
            ProtocolFactory.create(ProfileProtocol::class.java)
        protocol.PATH = "user/${NFT.instance.loginResponse.name}/profile"

        val request = Profile(src)
        protocol.setJsonRequestBody(request)
        protocol.setHttpResponsable(object : HttpResponsable<Message> {
            override fun onResponse(res: Message) {
                Trace.error("onResponse = $res")
                NFT.instance.loginResponse.profile_img = src
            }

            override fun onFailure(nError: Int, strMsg: String) {
                Trace.error("onFailure = $nError $strMsg")
                super.onFailure(nError, strMsg)
            }
        })
        NetworkManager.getInstance().asyncRequest(protocol)
    }

    fun soldItem(tokenId: Int, price: Float) {
        val protocol: TokenRegisterProtocol =
            ProtocolFactory.create(TokenRegisterProtocol::class.java)
        protocol.PATH = "token/$tokenId/regist"
        val request = TokenRegisteRquest(NFT.instance.loginResponse.address, price.toString())

        protocol.setJsonRequestBody(request)
        protocol.setHttpResponsable(object : HttpResponsable<Message> {
            override fun onResponse(res: Message) {
                Trace.error("onResponse = $res")
                _message.postValue(res)
            }

            override fun onFailure(nError: Int, strMsg: String) {
                Trace.error("onFailure = $nError $strMsg")
                super.onFailure(nError, strMsg)
            }
        })
        NetworkManager.getInstance().asyncRequest(protocol)
    }

    fun buyItem(tokenId:String) {
        val protocol: BuyProtocol =
            ProtocolFactory.create(BuyProtocol::class.java)
        protocol.PATH = "token/$tokenId/buy"
        val request = BuyRequest(NFT.instance.loginResponse.address)

        protocol.setJsonRequestBody(request)
        protocol.setHttpResponsable(object : HttpResponsable<Message> {
            override fun onResponse(res: Message) {
                Trace.error("onResponse = $res")
                _message.postValue(res)
            }

            override fun onFailure(nError: Int, strMsg: String) {
                Trace.error("onFailure = $nError $strMsg")
                super.onFailure(nError, strMsg)
            }
        })
        NetworkManager.getInstance().asyncRequest(protocol)
    }

    fun getOwnerItem(owner:String){
        val protocol: OwnerProtocol =
            ProtocolFactory.create(OwnerProtocol::class.java)
        protocol.PATH = "user/$owner/info"

        protocol.setHttpResponsable(object : HttpResponsable<Owner> {
            override fun onResponse(res: Owner) {
                Trace.error("onResponse = $res")
                _owner.postValue(res)
            }

            override fun onFailure(nError: Int, strMsg: String) {
                Trace.error("onFailure = $nError $strMsg")
                super.onFailure(nError, strMsg)
            }
        })
        NetworkManager.getInstance().asyncRequest(protocol)
    }
}