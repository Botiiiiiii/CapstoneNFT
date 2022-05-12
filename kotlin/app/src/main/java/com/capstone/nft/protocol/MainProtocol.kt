package com.capstone.nft.protocol

import com.capstone.nft.model.dto.Gallery
import com.capstone.nft.system.net.AbstractHttpProtocol
import com.capstone.nft.system.net.HttpConst

class MainProtocol: AbstractHttpProtocol<Gallery>() {
    var PATH = "list"

    override fun getUrl() = getDomain() + PATH

    override fun getMethod(): Int = HttpConst.HTTP_POST
}