package com.capstone.capstonenft.protocol

import com.capstone.capstonenft.dto.ResponseUpload
import com.capstone.capstonenft.system.net.AbstractHttpProtocol
import com.capstone.capstonenft.system.net.HttpConst

class DownloadUriProtocol: AbstractHttpProtocol<ResponseUpload>() {
    var PATH = "upload/uri"

    override fun getUrl() = getDomain() + PATH

    override fun getMethod(): Int = HttpConst.HTTP_POST
}