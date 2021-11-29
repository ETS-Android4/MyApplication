package com.example.william.my.module.sample.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.rxjava3.cachedIn
import androidx.paging.rxjava3.flowable
import com.example.william.my.module.sample.source.ArticlePagingSource
import com.example.william.my.module.sample.source.ArticleRxPagingSource
import kotlinx.coroutines.ExperimentalCoroutinesApi

class PagingViewModel : ViewModel() {

    /**
     * FLow
     * pageSize 一次加载的数目
     */
    val articleFlow = Pager(PagingConfig(pageSize = 20)) {
        ArticlePagingSource()
    }.flow
        .cachedIn(viewModelScope)

    /**
     * RxJava
     * pageSize 一次加载的数目
     */
    @ExperimentalCoroutinesApi
    val articleFlowable = Pager(PagingConfig(pageSize = 20)) {
        ArticleRxPagingSource()
    }.flowable
        .cachedIn(viewModelScope)
}