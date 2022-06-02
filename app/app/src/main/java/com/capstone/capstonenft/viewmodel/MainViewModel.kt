package com.capstone.capstonenft.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.capstonenft.dto.Gallery
import com.capstone.capstonenft.dto.GalleryList
import com.capstone.capstonenft.protocol.MainProtocol
import com.capstone.capstonenft.system.net.HttpResponsable
import com.capstone.capstonenft.system.net.NetworkManager
import com.capstone.capstonenft.system.net.ProtocolFactory
import com.capstone.capstonenft.system.utils.Trace

class MainViewModel: ViewModel() {
    val mldGallery = MutableLiveData<GalleryList>()

    init {
        getMainItem()
    }

    fun getMainItem(){
//        val protocol: MainProtocol = ProtocolFactory.create(MainProtocol::class.java)
//
//        protocol.setHttpResponsable(object : HttpResponsable<GalleryList> {
//            override fun onResponse(res: GalleryList) {
//                Trace.error("onResponse = $res")
//                mldGallery.value = res
//            }
//
//            override fun onFailure(nError: Int, strMsg: String) {
//                super.onFailure(nError, strMsg)
//                Trace.debug("++ Fail = $nError, $strMsg")
//            }
//        })
//        NetworkManager.getInstance().asyncRequest(protocol)
    }
}