package com.example.william.my.kotlin.repository

import android.util.Log
import com.example.william.my.kotlin.source.ArticlesDataSource
import com.example.william.my.module.bean.ArticlesBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class ArticlesRepository {

    /**
     * 返回流上转换的最喜爱的数据
     * Returns the favorite latest news applying transformations on the flow.
     * 这些操作为懒汉式，不会触发流，只在发送数据时转换
     * These operations are lazy and don't trigger the flow. They just transform
     * the current value emitted by the flow at that point in time.
     */
    val getArticles: Flow<ArticlesBean> =
        ArticlesDataSource().latestNews
            // Intermediate operation to filter the list of favorite topics
            .map { news ->
                news
            }
            // Intermediate operation to save the latest news in the cache
            .onEach {

            }
            // flowOn 只影响上游
            // flowOn affects the upstream flow ↑
            .flowOn(Dispatchers.IO)
            // 下游不受影响
            // the downstream flow ↓ is not affected
            .catch { exception ->
                Log.e("TAG", exception.message.toString())
            }
}