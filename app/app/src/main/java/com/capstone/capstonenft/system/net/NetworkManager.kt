package com.capstone.capstonenft.system.net

import com.capstone.capstonenft.system.utils.Trace

import android.os.Handler
import android.os.Looper
import com.capstone.capstonenft.system.config.Config
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.*
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.security.SecureRandom
import java.security.cert.CertPathValidatorException
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*


class NetworkManager private constructor() {
    private val mOkHttpClient: OkHttpClient = OkHttpClient()
    private val mInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)  // HEADERS)
    private var mTransactionCallback: HttpTransactionCallback? = null
    private lateinit var mDelayHandler: Handler
    private lateinit var mRunnable: Runnable

    companion object {
        private var mInstance: NetworkManager? = null

        @JvmStatic
        fun getInstance(): NetworkManager = mInstance ?: synchronized(this) {
            mInstance ?: NetworkManager().also {
                mInstance = it
            }
        }
    }

    fun init() {
    }

    fun registerTransactionCallback(callback: HttpTransactionCallback) {
        mTransactionCallback = callback
    }

    private fun getCallByMethod(
        retrofitInterface: RetrofitInterface,
        protocol: HttpRequestable
    ): Call<ResponseBody>? {
        val strUrl = protocol.getRedirection() ?: protocol.getUrl()

        return when (protocol.getMethod()) {
            HttpConst.HTTP_GET -> {
                retrofitInterface.requestGet(strUrl, protocol.getRequestHeaderMap())
            }

            HttpConst.HTTP_POST -> {
                val reqBody: RequestBody = if (protocol.getJsonRequestBody() != null) {
                    Trace.debug(">> Request.getJsonRequestBody() = \n${protocol.getJsonRequestBody()}")
                    (protocol.getJsonRequestBody() ?: "{}").toRequestBody(
                        protocol.getContentType().toMediaType()
                    )
                } else {
                    Trace.debug(
                        ">> Request.getRequestBody() = \n${
                            protocol.getRequestBody().toString()
                        }"
                    )
                    protocol.getRequestBody().toString().toRequestBody(
                        protocol.getContentType().toMediaType()
                    )
                }

                retrofitInterface.requestPost(strUrl, protocol.getRequestHeaderMap(), reqBody);
            }

            HttpConst.HTTP_FILE_UPLOAD -> {
                val file: File = protocol.getRequestBody() as File
                val reqBody: RequestBody =
                    file.asRequestBody(HttpConst.HTTP_MIME_TYPE_JPEG.toMediaType())
                val filePart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    HttpConst.HTTP_MULTIPART_FILE,
                    file.name,
                    reqBody
                )
                retrofitInterface.uploadFile(strUrl, protocol.getRequestHeaderMap(), filePart)
            }

            HttpConst.HTTP_MULTIPART -> {
                val multipart: Map<String, Any> = protocol.getRequestBody() as Map<String, Any>
                val json: String = multipart.get(HttpConst.HTTP_MULTIPART_JSON) as String
                val jsonPart: RequestBody =
                    json.toRequestBody(HttpConst.HTTP_MIME_TYPE_JSON.toMediaType())
                val file: File = multipart.get(HttpConst.HTTP_MULTIPART_FILE) as File

                if (file != null) {
                    val fileBody = file.asRequestBody(HttpConst.HTTP_MIME_TYPE_JPEG.toMediaType())
                    val filePart: MultipartBody.Part = MultipartBody.Part.createFormData(
                        HttpConst.HTTP_MULTIPART_FILE,
                        file.name,
                        fileBody
                    )
                    retrofitInterface.uploadMultiPart(
                        strUrl,
                        protocol.getRequestHeaderMap(),
                        jsonPart,
                        filePart
                    )
                } else {
                    retrofitInterface.uploadMultiPart(
                        strUrl,
                        protocol.getRequestHeaderMap(),
                        jsonPart,
                        null
                    )
                }
            }

            HttpConst.HTTP_DELETE -> {
                retrofitInterface.requestDelete(strUrl, protocol.getRequestHeaderMap())
            }

            HttpConst.HTTP_PUT -> {
                val reqBody: RequestBody = if (protocol.getJsonRequestBody() != null) {
                    Trace.debug(">> Request.getJsonRequestBody() = \n${protocol.getJsonRequestBody()}")
                    (protocol.getJsonRequestBody() ?: "{}").toRequestBody(
                        protocol.getContentType().toMediaType()
                    )
                } else {
                    Trace.debug(
                        ">> Request.getRequestBody() = \n${
                            protocol.getRequestBody().toString()
                        }"
                    )
                    protocol.getRequestBody().toString().toRequestBody(
                        protocol.getContentType().toMediaType()
                    )
                }

                retrofitInterface.requestPut(strUrl, protocol.getRequestHeaderMap(), reqBody);
            }

            else -> {
                Trace.debug("-- getCallByMethod() not supported method.");
                null
            }
        }
    }

    fun delayRequest(protocol: HttpRequestable, millisec: Int) {
        Trace.debug("++ delayRequest()")

        mRunnable = Runnable { asyncRequest(protocol) }
        mDelayHandler = Handler(Looper.getMainLooper())
        mDelayHandler.postDelayed(mRunnable, millisec.toLong())
    }

    fun cancelRequest(protocol: HttpRequestable) {
        Trace.debug("++ cancelRequest()")

        if (::mDelayHandler.isInitialized && ::mRunnable.isInitialized) {
            mDelayHandler.removeCallbacks(mRunnable, protocol)
        }
    }

    fun asyncRequest(protocol: HttpRequestable) {
        Trace.debug("++ asyncRequest()")

        mTransactionCallback?.transactionBegin(protocol)

        val okHttpClient = mOkHttpClient.newBuilder()
            .followRedirects(false)
            .connectTimeout(protocol.getConnectTimeout().toLong(), TimeUnit.SECONDS)
            .readTimeout(protocol.getReadTimeout().toLong(), TimeUnit.SECONDS)
            .apply {
                if (Config.SUPPORT_DEBUG) addInterceptor(mInterceptor)
                if (HttpConst.HTTP_FEATURE_HTTPS_ALL && isSecure(protocol.getUrl())) trustAllCert(
                    this
                )
//                    if (HttpConst.HTTP_FEATURE_IMAGE_CACHE) cache()   / /TODO
            }
            .build()

        val retrofit = Retrofit.Builder().baseUrl(protocol.getDomain()).client(okHttpClient).build()
        val retrofitInterface = retrofit.create(RetrofitInterface::class.java)
        val call = getCallByMethod(retrofitInterface, protocol) ?: return

        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                Trace.debug(">> asyncRequest() onResponse = $response")

                when (response.code()) {
                    HttpConst.Status.HTTP_200.getCode() -> {
                        if (response.body() == null) {
                            protocol.requestFailed(response.code(), response.message())
                            mTransactionCallback?.transactionEnd(protocol)
                            return
                        }
                    }

                    HttpConst.Status.HTTP_301.getCode(),
                    HttpConst.Status.HTTP_302.getCode() -> {
                        Trace.debug(">> redirection url = ${response.headers()["location"]}")
                        protocol.setRedirection(response.headers()["location"])
                        asyncRequest(protocol)
                        return
                    }

                    else -> {
                        protocol.requestFailed(response.code(), response.message())
                        mTransactionCallback?.transactionEnd(protocol)
                        return
                    }
                }

                if (HttpConst.HTTP_FEATURE_COROUTINE) {
                    CoroutineScope(Dispatchers.Main).launch {
                        processResponse(protocol, response)
                    }
                } else {
                    processResponse(protocol, response)
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, throwable: Throwable) {
                Trace.debug("++ onFailure() url = ${protocol.getUrl()} msg = ${throwable.message}")
                mTransactionCallback?.transactionEnd(protocol)

                when (throwable) {
                    is SSLHandshakeException,
                    is CertPathValidatorException -> {
                    }

                    is SocketTimeoutException -> {
                        protocol.requestFailed(
                            HttpConst.Error.HTTP_TIME_OUT.getCode(),
                            HttpConst.Error.HTTP_TIME_OUT.getMessage()
                        )
                    }

                    is ConnectException -> {
                        protocol.requestFailed(
                            HttpConst.Error.HTTP_NOT_CONNECTED.getCode(),
                            HttpConst.Error.HTTP_NOT_CONNECTED.getMessage()
                        )
                    }

                    is SocketException -> {
                        protocol.requestFailed(
                            HttpConst.Error.HTTP_SOCKET_DISCONNECTED.getCode(),
                            HttpConst.Error.HTTP_SOCKET_DISCONNECTED.getMessage()
                        )
                    }

                    is UnknownHostException -> {
                        protocol.requestFailed(
                            HttpConst.Error.HTTP_UNKNOWN_HOST.getCode(),
                            HttpConst.Error.HTTP_UNKNOWN_HOST.getMessage()
                        )
                    }

                    is IOException -> {
                        protocol.requestFailed(
                            HttpConst.Error.HTTP_IO_EXCEPTION.getCode(),
                            HttpConst.Error.HTTP_IO_EXCEPTION.getMessage()
                        )
                    }
                }
            }
        })
    }

    fun processResponse(protocol: HttpRequestable, response: Response<ResponseBody?>) {
        protocol.setResponseHeaderMap(response.headers().toMultimap())

        when (protocol.getResponseHeader(HttpConst.HTTP_HEADER_CONTENT_TYPE, 0)) {
            HttpConst.HTTP_MIME_TYPE_JPEG,
            HttpConst.HTTP_MIME_TYPE_PNG,
            HttpConst.HTTP_MIME_TYPE_GIF,
            HttpConst.HTTP_MIME_TYPE_JAR,
            HttpConst.HTTP_MIME_TYPE_APK,
            HttpConst.HTTP_MIME_TYPE_STREAM -> {
                val inputStream: InputStream? = response.body()?.byteStream()
                protocol.parse(inputStream)
            }

            else -> {
                var strResult: String? = "";

                try {
                    strResult = response.body()?.string()
                } catch (e: IOException) {
                    e.printStackTrace()
                    protocol.requestFailed(
                        HttpConst.Error.HTTP_DISCONNECTED.getCode(),
                        e.message ?: HttpConst.Error.HTTP_DISCONNECTED.getMessage()
                    )
                }

                protocol.parse(strResult)

            }
        }

        mTransactionCallback?.transactionEnd(protocol)
    }

    private fun isSecure(strUrl: String?): Boolean {
        return strUrl != null && strUrl.startsWith("https://")
    }


    private fun trustAllCert(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        Trace.debug("++ trustAllCert()")

        try {
            val trustManagers = arrayOf<TrustManager>(object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            })

            val context = SSLContext.getInstance("TLS")
            context.init(null, trustManagers, SecureRandom())
            builder.sslSocketFactory(context.socketFactory, trustManagers[0] as X509TrustManager)
            builder.hostnameVerifier { hostname, session -> true }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return builder
    }

    interface RetrofitInterface {
        @GET
        fun requestGet(
            @Url strUrl: String,
            @HeaderMap headers: Map<String, String>,
        ): Call<ResponseBody>

        @POST
        fun requestPost(
            @Url strUrl: String,
            @HeaderMap headers: Map<String, String>,
            @Body jsonPart: RequestBody,
        ): Call<ResponseBody>

        @POST
        @Multipart
        fun uploadFile(
            @Url strUrl: String,
            @HeaderMap headers: Map<String, String>,
            @Part filePart: MultipartBody.Part,
        ): Call<ResponseBody>

        @POST
        @Multipart
        fun uploadMultiPart(
            @Url strUrl: String,
            @HeaderMap headers: Map<String, String>,
            @Part(HttpConst.HTTP_MULTIPART_JSON) jsonPart: RequestBody,
            @Part filePart: MultipartBody.Part?,
        ): Call<ResponseBody>

        @DELETE
        fun requestDelete(
            @Url strUrl: String,
            @HeaderMap headers: Map<String, String>,
        ): Call<ResponseBody>

        @PUT
        fun requestPut(
            @Url strUrl: String,
            @HeaderMap headers: Map<String, String>,
            @Body jsonPart: RequestBody,
        ): Call<ResponseBody>
    }
}