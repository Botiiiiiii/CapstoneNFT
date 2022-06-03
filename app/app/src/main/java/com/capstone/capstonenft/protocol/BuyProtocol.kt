package com.capstone.capstonenft.protocol

import com.capstone.capstonenft.dto.BuyResponse
import com.capstone.capstonenft.dto.Message
import com.capstone.capstonenft.system.net.AbstractHttpProtocol
import com.capstone.capstonenft.system.net.HttpConst

class BuyProtocol: AbstractHttpProtocol<Message>() {
    var PATH = "token/{token_id}/buy"

    override fun getUrl() = getDomain() + PATH

    override fun getMethod(): Int = HttpConst.HTTP_POST
}