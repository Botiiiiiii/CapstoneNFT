package com.capstone.capstonenft.protocol

import com.capstone.capstonenft.dto.Message
import com.capstone.capstonenft.dto.TokenRegisterResponse
import com.capstone.capstonenft.system.net.AbstractHttpProtocol
import com.capstone.capstonenft.system.net.HttpConst

class TokenRegisterProtocol: AbstractHttpProtocol<Message>() {
    var PATH = "token/{token_id}/regist"

    override fun getUrl() = getDomain() + PATH

    override fun getMethod(): Int = HttpConst.HTTP_POST
}