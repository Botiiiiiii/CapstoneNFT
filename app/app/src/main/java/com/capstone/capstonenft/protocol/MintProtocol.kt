package com.capstone.capstonenft.protocol

import com.capstone.capstonenft.dto.MintResponse
import com.capstone.capstonenft.system.net.AbstractHttpProtocol
import com.capstone.capstonenft.system.net.HttpConst

class MintProtocol: AbstractHttpProtocol<MintResponse>() {
    var PATH = "token/mint"

    override fun getUrl() = getDomain() + PATH

    override fun getMethod(): Int = HttpConst.HTTP_POST
}