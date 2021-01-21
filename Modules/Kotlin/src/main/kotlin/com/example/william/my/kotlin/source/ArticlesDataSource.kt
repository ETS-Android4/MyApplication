package com.example.william.my.kotlin.source

import com.example.william.my.kotlin.service.KotlinApi
import com.example.william.my.module.base.Urls
import com.example.william.my.module.bean.ArticlesBean
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 创建数据流
 */
class ArticlesDataSource(private val refreshIntervalMs: Long = 3000) {

    val getArticles: Flow<ArticlesBean> = flow {
        emit(buildApi().getArticles(0)) // Emits the result of the request to the flow 向数据流发送请求结果
        delay(refreshIntervalMs) // Suspends the coroutine for some time 挂起一段时间
    }

    private fun buildApi(): KotlinApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(Urls.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(KotlinApi::class.java)
    }
}


