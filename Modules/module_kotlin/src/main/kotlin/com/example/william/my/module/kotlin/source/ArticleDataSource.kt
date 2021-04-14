package com.example.william.my.module.kotlin.source

import com.example.william.my.core.network.retrofit.utils.RetrofitUtils
import com.example.william.my.module.bean.ArticleBean
import com.example.william.my.module.kotlin.api.KotlinApi
import com.example.william.my.module.kotlin.utils.ThreadUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ArticleDataSource {

    val getArticle: Flow<ArticleBean> = flow {
        //打印线程
        ThreadUtils.isMainThread("ArticleDataSource getArticle")

        val api = RetrofitUtils.buildApi(KotlinApi::class.java)

        val article = api.getArticle(0)
        emit(article) // Emits the result of the request to the flow 向数据流发送请求结果
        //delay(3000) // Suspends the coroutine for some time 挂起一段时间
    }
}


