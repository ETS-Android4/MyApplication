package com.example.william.my.module.kotlin.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.rxjava3.cachedIn
import androidx.paging.rxjava3.flowable
import com.example.william.my.module.kotlin.source.ArticlePagingSource
import com.example.william.my.module.kotlin.source.ArticleRxPagingSource

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
    val articleFlowable = Pager(PagingConfig(pageSize = 20)) {
        ArticleRxPagingSource()
    }.flowable
        .cachedIn(viewModelScope)
}