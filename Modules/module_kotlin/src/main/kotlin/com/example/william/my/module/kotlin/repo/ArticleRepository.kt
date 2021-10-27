package com.example.william.my.module.kotlin.repo

import com.example.william.my.core.retrofit.utils.RetrofitUtils
import com.example.william.my.retrofit.ArticleBean
import com.example.william.my.module.kotlin.api.KotlinApi
import com.example.william.my.module.kotlin.utils.ThreadUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/**
 *  flow {
 *  }
 */
class ArticleRepository {

    private val getArticleFlow: Flow<com.example.william.my.retrofit.ArticleBean> =
        flow {
            //打印线程
            ThreadUtils.isMainThread("ArticleRepository getArticle")

            val api = RetrofitUtils.buildApi(KotlinApi::class.java)

            val article = api.getArticleSuspend(0)
            emit(article) // Emits the result of the request to the flow 向数据流发送请求结果
            delay(3000) // Suspends the coroutine for some time 挂起一段时间
        }

    /**
     * 返回流上的数据转换。
     * Returns the data transformations on the flow.
     * 这些操作为懒汉式，不会触发流，只在发送数据时转换
     * These operations are lazy and don't trigger the flow. They just transform
     * the current value emitted by the flow at that point in time.
     */
    val article: Flow<com.example.william.my.retrofit.ArticleBean> =
        getArticleFlow
            // 中间运算符 map 转换数据
            .map { article ->
                articleTakeOne(article)
            }
            // flowOn 只影响上游
            // flowOn affects the upstream flow ↑
            .flowOn(Dispatchers.IO)
    // 下游不受影响
    // the downstream flow ↓ is not affected
    //.catch { exception ->
    //    Log.e("TAG", "exception : " + exception.message.toString())
    //}

    private fun articleTakeOne(article: com.example.william.my.retrofit.ArticleBean): com.example.william.my.retrofit.ArticleBean {
        //打印线程
        ThreadUtils.isMainThread("ArticleRepository articleTakeOne")

        article.data.datas = article.data.datas.take(1)
        return article
    }
}