package com.capstone.capstonenft.protocol

import com.capstone.capstonenft.dto.LoginResponse
import com.capstone.capstonenft.dto.RegisterResponse
import com.capstone.capstonenft.system.net.AbstractHttpProtocol
import com.capstone.capstonenft.system.net.HttpConst

class RegisterProtocol: AbstractHttpProtocol<RegisterResponse>() {
    var PATH = "user/regist"

    override fun getUrl() = getDomain() + PATH

    override fun getMethod(): Int = HttpConst.HTTP_POST
}