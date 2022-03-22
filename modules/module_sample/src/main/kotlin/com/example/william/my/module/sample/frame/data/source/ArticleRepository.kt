/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.william.my.module.sample.frame.data.source

import com.example.william.my.bean.data.ArticleDataBean
import com.example.william.my.core.retrofit.response.RetrofitResponse
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow

/**
 * Concrete implementation to load tasks from the data sources into a cache.
 *
 *
 * For simplicity, this implements a dumb synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database doesn't
 * exist or is empty.
 */
class ArticleRepository private constructor(private val articleRemoteDataSource: ArticleDataSource) : ArticleDataSource {

    /**
     * Gets tasks from cache, local data source (SQLite) or remote data source, whichever is
     * available first.
     *
     * Note: [ArticleDataSource.LoadArticleCallback.onDataNotAvailable] is fired if all data sources fail to
     * get the data.
     */
    override fun getArticle(page: Int, callback: ArticleDataSource.LoadArticleCallback) {
        // Query the local storage if available. If not, query the network.
        articleRemoteDataSource.getArticle(page, object : ArticleDataSource.LoadArticleCallback {
            override fun onArticleLoaded(article: ArticleDataBean) {
                callback.onArticleLoaded(article)
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    override fun getArticleSingle(page: Int): Single<RetrofitResponse<ArticleDataBean>> {
        return articleRemoteDataSource.getArticleSingle(page)
    }

    override suspend fun getArticleSuspend(page: Int): RetrofitResponse<ArticleDataBean> {
        return articleRemoteDataSource.getArticleSuspend(page)
    }

    override fun getArticleFlow(page: Int): Flow<RetrofitResponse<ArticleDataBean>> {
        return articleRemoteDataSource.getArticleFlow(page)
    }

    companion object {

        private var INSTANCE: ArticleRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.
         * @param articleRemoteDataSource the backend data source
         * *
         * @param tasksLocalDataSource  the device storage data source
         * *
         * @return the [ArticleRepository] instance
         */
        @JvmStatic
        fun getInstance(articleRemoteDataSource: ArticleDataSource): ArticleRepository {
            return INSTANCE ?: ArticleRepository(articleRemoteDataSource)
                .apply {
                    INSTANCE = this
                }
        }

        /**
         * Used to force [getInstance] to create a new instance
         * next time it's called.
         */
        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}