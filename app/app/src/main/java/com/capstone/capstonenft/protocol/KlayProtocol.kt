package com.capstone.capstonenft.protocol

import com.capstone.capstonenft.dto.Klay
import com.capstone.capstonenft.dto.LoginResponse
import com.capstone.capstonenft.system.net.AbstractHttpProtocol
import com.capstone.capstonenft.system.net.HttpConst

class KlayProtocol: AbstractHttpProtocol<Klay>() {
    var PATH = "user/{nickname}/balance"

    override fun getUrl() = getDomain() + PATH

    override fun getMethod(): Int = HttpConst.HTTP_GET
}