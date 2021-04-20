package com.example.william.my.module.sample.utils

import com.example.william.my.core.network.retrofit.converter.RetrofitConverterFactory
import com.example.william.my.module.sample.utils.callback.KtRetrofitCallback
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object KtRetrofit {

    var retrofit: Retrofit.Builder = Retrofit.Builder()

    private fun okHttpClientBuilder(): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        return builder
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

    suspend fun <T> buildFlow(flow: Flow<T>, callback: KtRetrofitCallback<T>) {
//        withContext(Dispatchers.Main) {
//
//        }
    }

    suspend fun <T> buildFlow(api: Class<T>, callback: KtRetrofitCallback<T>) {

    }

}