/*
 * Copyright (C) 2019 The Android Open Source Project
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
package com.example.william.my.module.sample.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.william.my.module.base.Urls
import com.example.william.my.module.sample.api.KtArticleService
import com.example.william.my.module.sample.bean.ArticleBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class KtArticleRepository : KtArticleDataSource {

    /**
     * LiveData builder generating a value that will be transformed.
     */
    fun getCurrentTime(): LiveData<Long> = liveData {
        while (true) {
            emit(System.currentTimeMillis())
            delay(1000)
        }
    }

    val getArticle: Flow<com.example.william.my.module.bean.ArticleBean> = flow {
        val article = buildApi().getArticle(0)
        emit(article) // Emits the result of the request to the flow 向数据流发送请求结果
        delay(3000) // Suspends the coroutine for some time 挂起一段时间
    }

    private fun buildApi(): KtArticleService {
        val retrofit = Retrofit.Builder()
            .baseUrl(Urls.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(KtArticleService::class.java)
    }

    override fun getArticleBean(): Flow<ArticleBean> {
        TODO("Not yet implemented")
    }

    /**
     * Cache + Remote data source.
     */

    // Cache of a data point that is exposed to VM
    private val _cachedData = MutableLiveData("This is old data")
    override val cachedData: LiveData<String> = _cachedData

    // Called when the cache needs to be refreshed. Must be called from coroutine.
    override suspend fun fetchNewData() {
        // Force Main thread
        withContext(Dispatchers.Main) {
            _cachedData.value = "Fetching new data..."
            _cachedData.value = simulateNetworkDataFetch()
        }
    }

    // Fetches new data in the background. Must be called from coroutine so it's scoped correctly.
    private var counter = 0

    // Using ioDispatcher because the function simulates a long and expensive operation.
    private suspend fun simulateNetworkDataFetch(): String = withContext(Dispatchers.IO) {
        delay(3000)
        counter++
        "New data from request #$counter"
    }
}
