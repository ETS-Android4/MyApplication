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
package com.example.william.my.module.sample.frame.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.william.my.bean.data.ArticleDataBean
import com.example.william.my.bean.data.ArticleDetailBean
import com.example.william.my.module.sample.frame.data.source.ArticleDataSource

/**
 * Exposes the data to be used in the task list screen.
 */
class ArticleLiveDataViewModel(private val dataSource: ArticleDataSource) : ViewModel() {

    private val _items = MutableLiveData<List<ArticleDetailBean>>()
    val items: LiveData<List<ArticleDetailBean>>
        get() = _items

    init {
        loadArticle(0)
    }

    fun loadArticle(page: Int) {
        dataSource.getArticle(page, object : ArticleDataSource.LoadArticleCallback {
            override fun onArticleLoaded(article: ArticleDataBean) {
                _items.postValue(article.datas)
            }

            override fun onDataNotAvailable() {
                _items.postValue(emptyList())
            }
        })
    }
}
