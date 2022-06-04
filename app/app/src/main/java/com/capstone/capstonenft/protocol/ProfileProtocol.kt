package com.capstone.capstonenft.protocol

import com.capstone.capstonenft.dto.Klay
import com.capstone.capstonenft.dto.LoginResponse
import com.capstone.capstonenft.dto.Message
import com.capstone.capstonenft.system.net.AbstractHttpProtocol
import com.capstone.capstonenft.system.net.HttpConst

class ProfileProtocol: AbstractHttpProtocol<Message>() {
    var PATH = ""

    override fun getUrl() = getDomain() + PATH

    override fun getMethod(): Int = HttpConst.HTTP_POST
}