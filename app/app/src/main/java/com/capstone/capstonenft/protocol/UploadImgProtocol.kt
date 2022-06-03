package com.capstone.capstonenft.protocol

import com.capstone.capstonenft.dto.*
import com.capstone.capstonenft.system.net.AbstractHttpProtocol
import com.capstone.capstonenft.system.net.HttpConst

class UploadImgProtocol: AbstractHttpProtocol<Upload>() {
    var PATH = "upload/image"

    override fun getUrl() = getDomain() + PATH

    override fun getMethod(): Int = HttpConst.HTTP_FILE_UPLOAD
}