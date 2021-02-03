package com.example.william.my.module.kotlin.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.rxjava3.cachedIn
import androidx.paging.rxjava3.flowable
import com.example.william.my.module.kotlin.source.ExamplePagingSource
import com.example.william.my.module.kotlin.source.ExampleRxPagingSource

class PagingViewModel : ViewModel() {

    /**
     * FLow
     * pageSize 一次加载的数目
     */
    val articlesFlow = Pager(PagingConfig(pageSize = 20)) {
        ExamplePagingSource()
    }.flow
        .cachedIn(viewModelScope)

    /**
     * RxJava
     * pageSize 一次加载的数目
     */
    val articlesFlowable = Pager(PagingConfig(pageSize = 20)) {
        ExampleRxPagingSource()
    }.flowable
        .cachedIn(viewModelScope)
}