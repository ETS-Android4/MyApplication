package com.example.william.my.kotlin.repository

import android.util.Log
import com.example.william.my.kotlin.source.ArticlesDataSource
import com.example.william.my.module.bean.ArticlesBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

/**
 * 修改数据流
 */
class ArticlesRepository {

    /**
     * 返回流上的数据转换。
     * Returns the data transformations on the flow.
     * 这些操作为懒汉式，不会触发流，只在发送数据时转换
     * These operations are lazy and don't trigger the flow. They just transform
     * the current value emitted by the flow at that point in time.
     */
    val getArticles: Flow<ArticlesBean> =
        ArticlesDataSource().getArticles
            // 中间运算符 map 转换数据
            .map { articles ->
                articlesTakeOne(articles)
            }
            // Intermediate operation to save the latest news in the cache
            .onEach {
                //Log.e("TAG", Gson().toJson(it))
            }
            // flowOn 只影响上游
            // flowOn affects the upstream flow ↑
            .flowOn(Dispatchers.IO)
            // 下游不受影响
            // the downstream flow ↓ is not affected
            .catch { exception ->
                Log.e("TAG", "exception : " + exception.message.toString())
            }

    private fun articlesTakeOne(articles: ArticlesBean): ArticlesBean {
        articles.data.datas = articles.data.datas.take(1)
        return articles
    }
}