package com.capstone.capstonenft.protocol

import com.capstone.capstonenft.dto.Gallery
import com.capstone.capstonenft.dto.GalleryList
import com.capstone.capstonenft.system.net.AbstractHttpProtocol
import com.capstone.capstonenft.system.net.HttpConst

class MainProtocol: AbstractHttpProtocol<GalleryList>() {
    var PATH = "list"

    override fun getUrl() = getDomain() + PATH

    override fun getMethod(): Int = HttpConst.HTTP_GET
}