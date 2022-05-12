package com.capstone.nft.system.net

import android.graphics.BitmapFactory
import com.capstone.nft.system.config.Config
import com.capstone.nft.system.utils.Trace
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.*
import java.net.URLEncoder

abstract class AbstractHttpProtocol<RES> : HttpRequestable
{
    private var mnMethod: Int = HttpConst.HTTP_GET
    private var mRequestHeaderMap: MutableMap<String, String> = mutableMapOf()
    private var mQueryMap: MutableMap<String, String> = mutableMapOf()
    private var mResponseHeaderMap: Map<String, List<String>>? = null
    var mBodyObject: Any? = "{}"
    private var mJsonBody: String? = null
    private var mResponsable: HttpResponsable<RES>? = null
    private var mResponseData: Class<*>? = null
    private var mRedirectionUrl: String? = null
    private var mnRetyCountMax: Int = HttpConst.HTTP_RETRY_COUNT_MAX
    private var mnRetyCount: Int = 0

    fun setMethod(nMethod: Int) {
        mnMethod = nMethod
    }

    override fun getMethod(): Int = mnMethod

    override fun getDomain(): String {
      return Config.getAppServer().getDomain()
    }

    open fun getPath(): String {
        return Config.getAppServer().getPath()
    }

    abstract override fun getUrl(): String

    override fun setRedirection(url: String?) {
        mRedirectionUrl = url
    }

    override fun getRedirection() = mRedirectionUrl

    override fun getConnectTimeout(): Int = HttpConst.HTTP_CONNECT_TIMEOUT

    override fun getReadTimeout(): Int = HttpConst.HTTP_READ_TIMEOUT

    override fun getRequestHeaderMap(): Map<String, String> = mRequestHeaderMap

    override fun addRequestHeader(strKey: String, strVal: String) {
        mRequestHeaderMap[strKey] = strVal
    }

    override fun getRequestQueryMap(): Map<String, String> = mQueryMap

    fun addQuery(strKey: String, strVal: String) {
        mQueryMap[strKey] = strVal
    }

    fun getQuery(strKey: String) : String? = mQueryMap[strKey]

    fun getQueries(): String {
        var strQuery = ""

        if (mQueryMap.isNotEmpty()) {
            strQuery += "?"

            for ((k, v) in mQueryMap) {
                strQuery += String.format("%s=%s&", k, URLEncoder.encode(v, "UTF-8"))
            }

            strQuery = strQuery.substring(0 until strQuery.length - 1)
        }

        return strQuery
    }

    override fun getContentType(): String = HttpConst.HTTP_MIME_TYPE_JSON

    override fun getRequestBody(): Any? = mBodyObject

    fun setRequestBody(obj: Any) {
        mBodyObject = obj
    }

    override fun getJsonRequestBody(): String? = mJsonBody

    fun setJsonRequestBody(obj: Any) {
        setJsonRequestBody(obj, false)
    }

    fun setJsonRequestBody(obj: Any, bExcludeExpose: Boolean) {
        mBodyObject = obj

        var gson: Gson = if (bExcludeExpose) {
            GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        } else {
            Gson()
        }

        mJsonBody = gson.toJson(mBodyObject)
    }

    override fun setResponseHeaderMap(responseHeaders: Map<String, List<String>>?) {
        mResponseHeaderMap = responseHeaders
    }

    override fun getResponseHeader(strKey: String, nSubIndex: Int): String {
        Trace.debug("++ getResponseHeader()")   // mResponseHeaderMap = $mResponseHeaderMap")

        var strVal: String? = null
        val values: List<String>? = mResponseHeaderMap?.get(strKey)
        values?: return ""

        strVal = values[0]

        if (nSubIndex < 0) {
            return strVal
        }

        val nIndex = strVal.indexOf(";")

        if (nIndex <= 0) {
            return strVal
        }

        val strVals = strVal.split(";")

        return if (nSubIndex < strVals.size) {
            strVals[nSubIndex]
        } else strVal
    }

    fun setHttpResponsable(responsable: HttpResponsable<RES>) {
        var strClassName: String = responsable.javaClass.genericInterfaces[0].toString()
        strClassName = strClassName.substring(strClassName.indexOf("<") + 1, strClassName.indexOf(">"))
        Trace.debug(">> setHttpResponsable() strClassName = $strClassName")

        mResponseData = Class.forName(strClassName)
        mResponsable = responsable
    }

    @Suppress("UNCHECKED_CAST")
    override fun parse(responseBody: Any?) {
        Trace.debug("++ parse()")
        var parsedObject: RES? = null

        if (responseBody == null) {
            mResponsable?.onFailure(
                HttpConst.Error.HTTP_DATA_NOT_EXIST.getCode(),
                    HttpConst.Error.HTTP_DATA_NOT_EXIST.getMessage())
            return
        }

        if (mResponsable == null || mResponseData == null) {
            Trace.debug("-- parse() mResponsable or mResponseData is null")
            return
        }

        when (getResponseHeader(HttpConst.HTTP_HEADER_CONTENT_TYPE, 0)) {
            HttpConst.HTTP_MIME_TYPE_HTML -> {
                requestFailed(
                    HttpConst.Error.HTTP_NOT_SUPPORTED_CONTENT_TYPE.getCode(),
                        HttpConst.Error.HTTP_NOT_SUPPORTED_CONTENT_TYPE.getMessage())

                return
            }

            HttpConst.HTTP_MIME_TYPE_JSON -> {
                if (mResponseData!!.isAssignableFrom(android.graphics.Bitmap::class.java) ||
                    mResponseData!!.isAssignableFrom(java.io.File::class.java))
                {
                    mResponsable?.onFailure(
                        HttpConst.Error.HTTP_DATA_NOT_EXIST.getCode(),
                            HttpConst.Error.HTTP_DATA_NOT_EXIST.getMessage())
                    return
                }

                val gson: Gson = Gson()
                parsedObject = gson.fromJson(responseBody as String, mResponseData) as RES
            }

            HttpConst.HTTP_MIME_TYPE_JPEG,
            HttpConst.HTTP_MIME_TYPE_PNG,
            HttpConst.HTTP_MIME_TYPE_GIF -> {
                parsedObject = BitmapFactory.decodeStream(responseBody as InputStream) as RES
            }

            HttpConst.HTTP_MIME_TYPE_OBJECT -> {
                parsedObject = responseBody as RES
            }

            else -> {
                requestFailed(
                    HttpConst.Error.HTTP_NOT_SUPPORTED_CONTENT_TYPE.getCode(),
                        HttpConst.Error.HTTP_NOT_SUPPORTED_CONTENT_TYPE.getMessage())
                return
            }
        }

        if (mResponsable == null) {
            Trace.debug("-- parse() mResponsable is null")
        } else {
            mResponsable?.onResponse(parsedObject)
        }
    }

    override fun requestFailed(nError: Int, strMsg: String) {
        mResponsable?.onFailure(nError, strMsg)
    }

    override fun hasOfflineJob(): Boolean = false

    override fun processOffline() {
    }

    override fun setRetryCount(nCount: Int) {
        mnRetyCountMax = nCount
    }
    override fun retry(): Boolean {
        mnRetyCount++

        if (mnRetyCount >= mnRetyCountMax) return false

        NetworkManager.getInstance().asyncRequest(this)
        return true
    }

    override fun isValidate() = true
}