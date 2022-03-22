package com.example.william.my.module.sample.paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.rxjava3.cachedIn
import androidx.paging.rxjava3.flowable
import com.example.william.my.bean.data.ArticleDetailBean
import com.example.william.my.module.sample.frame.data.source.ArticleDataSource
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

/**
 * 设置 PagingData 流
 * FLow , Flowable
 */
class ArticlePagingViewModel(private val dataSource: ArticleDataSource) : ViewModel() {

    /**
     * Paging
     * .flow{}
     */
    val articleFlow: Flow<PagingData<ArticleDetailBean>> =
        Pager(PagingConfig(pageSize = 20)) {
            ArticlePagingSource(dataSource)
        }.flow
            .cachedIn(viewModelScope)

    /**
     * Paging
     * .flowable{}
     */
    @ExperimentalCoroutinesApi
    val articleFlowable: Flowable<PagingData<ArticleDetailBean>> =
        Pager(PagingConfig(pageSize = 20)) {
            ArticleRxPagingSource(dataSource)
        }.flowable
            .cachedIn(viewModelScope)
}