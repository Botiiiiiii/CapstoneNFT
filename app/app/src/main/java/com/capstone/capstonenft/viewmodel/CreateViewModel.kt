package com.capstone.capstonenft.viewmodel

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.capstonenft.NFT
import com.capstone.capstonenft.dto.*
import com.capstone.capstonenft.protocol.*
import com.capstone.capstonenft.system.net.HttpResponsable
import com.capstone.capstonenft.system.net.NetworkManager
import com.capstone.capstonenft.system.net.ProtocolFactory
import com.capstone.capstonenft.system.utils.Trace
import kotlinx.coroutines.delay
import java.io.File

class CreateViewModel : ViewModel() {
    private val _token = MutableLiveData<Token>()
    val token:LiveData<Token> get() = _token
    fun uploadImage(file: File, title: String, desc: String, creator: String, price: Int) {
        val imgProtocol: UploadImgProtocol = ProtocolFactory.create(UploadImgProtocol::class.java)

        imgProtocol.setJsonRequestBody(file)
        Trace.error("header = ${imgProtocol.getRequestHeaderMap()}")
        Trace.error("data = ${imgProtocol.getJsonRequestBody()}")
        imgProtocol.setHttpResponsable(object : HttpResponsable<Upload> {
            override fun onResponse(res: Upload) {
                Trace.error("onResponse = $res")
                downloadUri(res.imageUrl, title, desc, creator, price)
            }

            override fun onFailure(nError: Int, strMsg: String) {
                Trace.error("onFailure = $nError $strMsg")
                super.onFailure(nError, strMsg)
            }
        })
        NetworkManager.getInstance().asyncRequest(imgProtocol)
    }

    fun downloadUri(image: String, title: String, desc: String, creator: String, price: Int) {
        val protocol: DownloadUriProtocol = ProtocolFactory.create(DownloadUriProtocol::class.java)
        val requestUpload = RequestUpload(image, title, desc, creator)

        protocol.setJsonRequestBody(requestUpload)
        protocol.setHttpResponsable(object : HttpResponsable<ResponseUpload> {
            override fun onResponse(res: ResponseUpload) {
                Trace.error("onResponse = $res")
                mintingUri(image, title, desc, res.uri, price)
            }

            override fun onFailure(nError: Int, strMsg: String) {
                Trace.error("onFailure = $nError $strMsg")
                super.onFailure(nError, strMsg)
            }
        })
        NetworkManager.getInstance().asyncRequest(protocol)
    }

    fun mintingUri(image: String, title: String, desc: String, uri: String, price: Int) {
        val protocol: MintProtocol = ProtocolFactory.create(MintProtocol::class.java)
        val requestMint = MintRequest(NFT.instance.loginResponse.address, image, title, desc, uri)

        protocol.setJsonRequestBody(requestMint)
        protocol.setHttpResponsable(object : HttpResponsable<MintResponse> {
            override fun onResponse(res: MintResponse) {
                Trace.error("onResponse = $res")
                getTokenInfo(res.tokenid)
            }

            override fun onFailure(nError: Int, strMsg: String) {
                Trace.error("onFailure = $nError $strMsg")
                super.onFailure(nError, strMsg)
            }
        })
        NetworkManager.getInstance().asyncRequest(protocol)
    }

    fun getTokenInfo(tokenId:String) {
        val protocol: TokenProtocol = ProtocolFactory.create(TokenProtocol::class.java)
        protocol.PATH = "token/$tokenId/info"

        protocol.setHttpResponsable(object : HttpResponsable<TokenResponse> {
            override fun onResponse(res: TokenResponse) {
                Trace.error("onResponse = $res")
                NFT.instance.loginResponse.token_list.add(res.token)
                _token.postValue(res.token)
            }

            override fun onFailure(nError: Int, strMsg: String) {
                Trace.error("onFailure = $nError $strMsg")
                super.onFailure(nError, strMsg)
            }
        })
        NetworkManager.getInstance().asyncRequest(protocol)
    }
}