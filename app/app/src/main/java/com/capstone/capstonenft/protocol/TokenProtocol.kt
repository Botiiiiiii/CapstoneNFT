package com.capstone.capstonenft.protocol

import com.capstone.capstonenft.dto.LoginResponse
import com.capstone.capstonenft.dto.Token
import com.capstone.capstonenft.dto.TokenResponse
import com.capstone.capstonenft.system.net.AbstractHttpProtocol
import com.capstone.capstonenft.system.net.HttpConst

class TokenProtocol: AbstractHttpProtocol<TokenResponse>() {
    var PATH = ""

    override fun getUrl() = getDomain() + PATH

    override fun getMethod(): Int = HttpConst.HTTP_GET
}