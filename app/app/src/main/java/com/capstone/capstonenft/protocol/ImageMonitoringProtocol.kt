package com.capstone.capstonenft.protocol

import com.capstone.capstonenft.dto.ImageMonitoring
import com.capstone.capstonenft.system.net.AbstractHttpProtocol
import com.capstone.capstonenft.system.net.HttpConst

class ImageMonitoringProtocol: AbstractHttpProtocol<ImageMonitoring>() {
    var PATH = "upload/image"

    override fun getUrl() = PATH

    override fun getMethod(): Int = HttpConst.HTTP_POST

}
