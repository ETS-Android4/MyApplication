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
import com.example.william.my.core.network.retrofit.utils.RetrofitUtils
import com.example.william.my.module.bean.ArticleBean
import com.example.william.my.module.sample.api.KtArticleService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class KtArticleRepository : KtArticleDataSource {

    override fun getArticleBean(): LiveData<ArticleBean> {
        TODO("Not yet implemented")
    }

    override val article: LiveData<ArticleBean>
        get() = TODO("Not yet implemented")

    override suspend fun fetchNewData() {
        TODO("Not yet implemented")
    }

    private var counter = 0

    private suspend fun getArticle(): Flow<ArticleBean> = flow {

        val api = RetrofitUtils.buildApi(KtArticleService::class.java)

        val article = api.getArticle(counter)
        counter++

        emit(article) // Emits the result of the request to the flow 向数据流发送请求结果
        delay(3000) // Suspends the coroutine for some time 挂起一段时间
    }
}
