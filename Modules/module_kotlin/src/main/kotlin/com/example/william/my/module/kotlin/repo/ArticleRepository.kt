package com.example.william.my.module.kotlin.repo

import com.example.william.my.module.bean.ArticleBean
import com.example.william.my.module.kotlin.source.ArticleDataSource
import com.example.william.my.module.kotlin.utils.ThreadUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class ArticleRepository {

    /**
     * 返回流上的数据转换。
     * Returns the data transformations on the flow.
     * 这些操作为懒汉式，不会触发流，只在发送数据时转换
     * These operations are lazy and don't trigger the flow. They just transform
     * the current value emitted by the flow at that point in time.
     */
    val article: Flow<ArticleBean> =
        ArticleDataSource().getArticle
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

    private fun articleTakeOne(article: ArticleBean): ArticleBean {
        //打印线程
        ThreadUtils.isMainThread("ArticleRepository articleTakeOne")

        article.data.datas = article.data.datas.take(1)
        return article
    }
}