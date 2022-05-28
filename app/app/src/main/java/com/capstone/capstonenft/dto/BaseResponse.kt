package com.capstone.capstonenft.dto


open class BaseResponse
{
//    "timestamp": "2021-05-12T01:21:48.939275",
//    "status": 200,
//    "error": "0000",
//    "message": "성공",
//    "path": "/api/schedule/search",

    val timestamp: String = ""
    val status: String = ""
    val error: String = ""
    val errCode: String = ""
    val message: String = ""
    val path: String = ""

    fun isSuccess() = (error == "0000")
//    fun isSuccess() = (errCode == "err_success")

    override fun toString(): String {
        var strBuf: String = "\n"

        strBuf += "BaseResponse.timestamp = $timestamp\n"
        strBuf += "BaseResponse.status = $status\n"
        strBuf += "BaseResponse.error = $error\n"
        strBuf += "BaseResponse.message = $message\n"
        strBuf += "BaseResponse.path = $path\n"

        return strBuf
    }
}