package com.capstone.capstonenft.protocol

import com.capstone.capstonenft.dto.BurnResponse
import com.capstone.capstonenft.system.net.AbstractHttpProtocol
import com.capstone.capstonenft.system.net.HttpConst

class BurnProtocol: AbstractHttpProtocol<BurnResponse>() {
    var PATH = "token/{token_id}/burn"

    override fun getUrl() = getDomain() + PATH

    override fun getMethod(): Int = HttpConst.HTTP_DELETE
}