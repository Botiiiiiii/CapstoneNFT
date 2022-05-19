package com.capstone.capstonenft.protocol

import com.capstone.capstonenft.dto.Gallery
import com.capstone.capstonenft.system.net.AbstractHttpProtocol
import com.capstone.capstonenft.system.net.HttpConst

class MainProtocol: AbstractHttpProtocol<Gallery>() {
    var PATH = "list"

    override fun getUrl() = getDomain() + PATH

    override fun getMethod(): Int = HttpConst.HTTP_POST
}