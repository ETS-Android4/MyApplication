package com.example.william.my.module.sample.utils.exception

import com.google.gson.Gson
import com.google.gson.JsonParseException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.text.ParseException
import javax.net.ssl.SSLHandshakeException

/**
 * ApiException
 */
object KtExceptionHandler {

    private const val UNAUTHORIZED = 401 // 没有权限
    private const val FORBIDDEN = 403 // 禁止访问
    private const val NOT_FOUND = 404 // 找不到资源
    private const val REQUEST_TIMEOUT = 408 // 请求超时
    private const val INTERNAL_SERVER_ERROR = 500 // 服务器错误
    private const val BAD_GATEWAY = 502 // 错误网关
    private const val SERVICE_UNAVAILABLE = 503 // 服务不可用
    private const val GATEWAY_TIMEOUT = 504 // 网关超时

    fun handleException(e: Throwable): KtApiException {
        val ex: KtApiException
        when (e) {
            is HttpException -> {
                ex = KtApiException(e, ERROR.HTTP_ERROR)
                ex.code = e.code()
                try {
                    val body = e.response()!!.errorBody()
                    if (body != null) {
                        val error = Gson().fromJson(body.string(), ErrorBean::class.java)
                        ex.message = error.message
                    } else {
                        ex.message = "请求网络失败，请检查您的网络设置或稍后重试！"
                    }
                } catch (e1: Exception) {
                    ex.message = "请求网络失败，请检查您的网络设置或稍后重试！"
                }
            }
            is KtResultException -> {
                ex = KtApiException(e, e.code)
                ex.message =
                    if (!e.message.isNullOrEmpty()) e.message!! else "请求网络失败，请检查您的网络设置或稍后重试！"
            }
            is JsonParseException, is JSONException, is ParseException -> {
                ex = KtApiException(e, ERROR.PARSE_ERROR)
                ex.message = "解析错误，请稍后再试"
            }
            is ConnectException -> {
                ex = KtApiException(e, ERROR.NETWORK_ERROR)
                ex.message = "连接失败，请稍后再试"
            }
            is SocketTimeoutException -> {
                ex = KtApiException(e, ERROR.CONNECT_ERROR)
                ex.message = "连接超时，请稍后再试"
            }
            is SSLHandshakeException -> {
                ex = KtApiException(e, ERROR.SSL_ERROR)
                ex.message = "证书验证失败，请稍后再试"
            }
            else -> {
                ex = KtApiException(e, ERROR.UNKNOWN)
                ex.message = "未知错误，请稍后再试"
            }
        }
        return ex
    }

    object ERROR {
        /**
         * 未知错误
         */
        const val UNKNOWN = 1000

        /**
         * 协议出错
         */
        const val HTTP_ERROR = 1001

        /**
         * 解析错误
         */
        const val PARSE_ERROR = 1002

        /**
         * 网络错误
         */
        const val NETWORK_ERROR = 1003

        /**
         * 连接超时
         */
        const val CONNECT_ERROR = 1004

        /**
         * 证书出错
         */
        const val SSL_ERROR = 1005
    }

    data class ErrorBean(var message: String)
}