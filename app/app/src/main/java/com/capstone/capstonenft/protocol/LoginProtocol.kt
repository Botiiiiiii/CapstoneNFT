package com.capstone.capstonenft.protocol

import com.capstone.capstonenft.dto.LoginResponse
import com.capstone.capstonenft.system.net.AbstractHttpProtocol
import com.capstone.capstonenft.system.net.HttpConst

class LoginProtocol: AbstractHttpProtocol<LoginResponse>() {
    var PATH = "user/login"

    override fun getUrl() = getDomain() + PATH

    override fun getMethod(): Int = HttpConst.HTTP_POST
}