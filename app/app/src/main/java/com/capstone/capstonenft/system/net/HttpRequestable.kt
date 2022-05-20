package com.capstone.capstonenft.system.net

interface HttpRequestable
{
    fun getMethod(): Int

    fun getDomain(): String

    fun getUrl(): String

    fun setRedirection(url: String?)

    fun getRedirection(): String?

    fun getConnectTimeout(): Int

    fun getReadTimeout(): Int

    fun getRequestHeaderMap(): Map<String, String>

    fun getRequestQueryMap(): Map<String, String>

    fun getContentType(): String

    fun getRequestBody(): Any?

    fun getJsonRequestBody(): String?

    fun setResponseHeaderMap(responseHeaders: Map<String, List<String>>?)

    fun getResponseHeader(strKey: String, nSubIndex: Int): String

    fun parse(responseBody: Any?)

    fun requestFailed(nError: Int, strMsg: String)

    fun hasOfflineJob(): Boolean

    fun processOffline()

    fun setRetryCount(nCount: Int)

    fun retry(): Boolean

    fun isValidate(): Boolean


    fun addRequestHeader(strKey: String, strVal: String)
}