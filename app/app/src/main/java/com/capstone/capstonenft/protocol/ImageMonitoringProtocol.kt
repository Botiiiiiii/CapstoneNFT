package com.capstone.capstonenft.protocol

import com.capstone.capstonenft.dto.ImageMonitoringRequest
import com.capstone.capstonenft.system.net.AbstractHttpProtocol
import com.capstone.capstonenft.system.net.HttpConst

class ImageMonitoringProtocol: AbstractHttpProtocol<ImageMonitoringRequest>() {
    var PATH = "112.187.174.215:11962/scoring"

    override fun getUrl() = PATH

    override fun getMethod(): Int = HttpConst.HTTP_GET

}
