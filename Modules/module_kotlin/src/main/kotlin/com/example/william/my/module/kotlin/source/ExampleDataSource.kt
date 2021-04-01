package com.example.william.my.module.kotlin.source

import com.example.william.my.module.base.Urls
import com.example.william.my.module.bean.ArticlesBean
import com.example.william.my.module.kotlin.api.KotlinApi
import com.example.william.my.module.kotlin.utils.ThreadUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ExampleDataSource {

    val getArticles: Flow<ArticlesBean> = flow {
        //打印线程
        ThreadUtils.isMainThread("ExampleDataSource getArticles")

        val articles = buildApi().getArticles(0)
        emit(articles) // Emits the result of the request to the flow 向数据流发送请求结果
        delay(3000) // Suspends the coroutine for some time 挂起一段时间
    }

    private fun buildApi(): KotlinApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(Urls.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(KotlinApi::class.java)
    }
}


