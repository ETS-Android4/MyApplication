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
package com.example.william.my.module.sample.frame.data.source.remote

import com.example.william.my.bean.api.NetworkService
import com.example.william.my.bean.data.ArticleDataBean
import com.example.william.my.core.retrofit.callback.RetrofitResponseCallback
import com.example.william.my.core.retrofit.exception.ApiException
import com.example.william.my.core.retrofit.response.RetrofitResponse
import com.example.william.my.core.retrofit.utils.RetrofitUtils.buildApi
import com.example.william.my.core.retrofit.utils.RetrofitUtils.buildSingle
import com.example.william.my.module.sample.frame.data.source.ArticleDataSource
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Implementation of the data source that adds a latency simulating network.
 */
object ArticleRemoteDataSource : ArticleDataSource {

    private var service: NetworkService = buildApi(NetworkService::class.java)

    /**
     * Note: [ArticleDataSource.LoadArticleCallback.onDataNotAvailable] is never fired. In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    override fun getArticle(page: Int, callback: ArticleDataSource.LoadArticleCallback) {
        buildSingle(
            service.getArticleResponse(page),
            object : RetrofitResponseCallback<ArticleDataBean>() {
                override fun onFailure(e: ApiException) {
                    callback.onDataNotAvailable()
                }

                override fun onResponse(response: ArticleDataBean) {
                    callback.onArticleLoaded(response)
                }
            }
        )
    }

    override fun getArticleSingle(page: Int): Single<RetrofitResponse<ArticleDataBean>> {
        return service.getArticleResponse(page)
    }

    override suspend fun getArticleSuspend(page: Int): RetrofitResponse<ArticleDataBean> {
        return service.getArticleSuspend(page)
    }

    override fun getArticleFlow(page: Int): Flow<RetrofitResponse<ArticleDataBean>> {
        return flow {
            emit(service.getArticleSuspend(page))
        }
    }
}