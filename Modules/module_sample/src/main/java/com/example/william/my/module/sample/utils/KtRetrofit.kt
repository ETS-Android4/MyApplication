package com.example.william.my.module.sample.utils

import android.annotation.SuppressLint
import com.example.william.my.core.network.retrofit.converter.RetrofitConverterFactory
import com.example.william.my.module.sample.utils.callback.KtRetrofitFlowCallback
import com.example.william.my.module.sample.utils.exception.KtExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.*

object KtRetrofit {

    var retrofit: Retrofit.Builder = Retrofit.Builder()

    private fun okHttpClientBuilder(): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        //Log
        builder.addInterceptor(logging)
        //Cookie
        builder.cookieJar(cookieJar())

        ////忽略https证书
        builder.hostnameVerifier(ignoreHostnameVerifier())
            .sslSocketFactory(ignoreSSLSocketFactory(), trustManager())
        return builder
    }

    private fun cookieJar(): CookieJar {
        return object : CookieJar {
            private val cookieStore = HashMap<String, List<Cookie>>()
            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                cookieStore[url.host] = cookies
            }

            override fun loadForRequest(url: HttpUrl): List<Cookie> {
                val cookies = cookieStore[url.host]
                return cookies ?: ArrayList()
            }
        }
    }

    /**
     * 获取忽略证书的HostnameVerifier
     * 与{@link #getIgnoreSSLSocketFactory()}同时配置使用
     */
    private fun ignoreHostnameVerifier(): HostnameVerifier {
        return HostnameVerifier { _, _ -> true }
    }

    /**
     * 获取忽略证书的SSLSocketFactory
     * 与[.getIgnoreHostnameVerifier]同时配置使用
     */
    private fun ignoreSSLSocketFactory(): SSLSocketFactory {
        return try {
            val sslContext = SSLContext.getInstance("TLS")
            val trustManager = trustManager()
            sslContext.init(null, arrayOf<TrustManager>(trustManager), null)
            sslContext.socketFactory
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun trustManager(): X509TrustManager {
        return object : X509TrustManager {
            @SuppressLint("TrustAllX509TrustManager")
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @SuppressLint("TrustAllX509TrustManager")
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }
    }

    private fun retrofitBuilder(): Retrofit.Builder {
        return retrofit
            .client(okHttpClientBuilder().build())
            .baseUrl("https://www.wanandroid.com/")
            .addConverterFactory(ScalarsConverterFactory.create()) //标准类型转换器，防止上传图文的时候带引号
            .addConverterFactory(RetrofitConverterFactory.create())//解析工厂类
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
    }

    fun <T> buildApi(api: Class<T>): T {
        return retrofitBuilder()
            .build()
            .create(api)
    }

    suspend fun <T> buildFlow(flow: Flow<T>, flowCallback: KtRetrofitFlowCallback<T>) {
        withContext(Dispatchers.Main) {
            flow
                .onStart {

                }
                .catch { exception ->
                    flowCallback.onFailure(KtExceptionHandler.handleException(exception))
                }
                .collect { response ->
                    flowCallback.onResponse(response)
                }
        }
    }
}