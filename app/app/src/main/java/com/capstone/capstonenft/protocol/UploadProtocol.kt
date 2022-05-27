package com.capstone.capstonenft.protocol

import com.capstone.capstonenft.dto.BaseResponse
import com.capstone.capstonenft.dto.Gallery
import com.capstone.capstonenft.dto.GalleryList
import com.capstone.capstonenft.system.net.AbstractHttpProtocol
import com.capstone.capstonenft.system.net.HttpConst

class UploadProtocol: AbstractHttpProtocol<BaseResponse>() {
    var PATH = "test/upload"

    override fun getUrl() = getDomain() + PATH

    override fun getMethod(): Int = HttpConst.HTTP_FILE_UPLOAD
}