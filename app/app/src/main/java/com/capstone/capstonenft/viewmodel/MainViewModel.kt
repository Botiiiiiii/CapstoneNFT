package com.capstone.capstonenft.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.capstonenft.dto.Gallery
import com.capstone.capstonenft.protocol.MainProtocol
import com.capstone.capstonenft.system.net.HttpResponsable
import com.capstone.capstonenft.system.net.NetworkManager
import com.capstone.capstonenft.system.net.ProtocolFactory
import com.capstone.capstonenft.system.utils.Trace

class MainViewModel: ViewModel() {
    val mldGallery = MutableLiveData<Gallery>()

    init {
        getMainItem()
    }

    fun getMainItem(){
        val protocol: MainProtocol = ProtocolFactory.create(MainProtocol::class.java)

        protocol.setHttpResponsable(object : HttpResponsable<Gallery> {
            override fun onResponse(res: Gallery) {
                mldGallery.value = res
            }

            override fun onFailure(nError: Int, strMsg: String) {
                super.onFailure(nError, strMsg)
                Trace.debug("++ Fail = $nError, $strMsg")
            }
        })
        NetworkManager.getInstance().asyncRequest(protocol)
    }
}