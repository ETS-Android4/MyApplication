package com.example.william.my.module.sample.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.william.my.module.bean.ArticleBean
import com.example.william.my.module.sample.repo.KtArticleRepository
import kotlinx.coroutines.launch

/**
 * ViewModel 应创建协程
 */
class LiveDataViewModel(private val articleDataSource: KtArticleRepository) : ViewModel() {

    val article: LiveData<ArticleBean> = articleDataSource.article

    fun onRefresh() {
        viewModelScope.launch {
            articleDataSource.fetchNewData()
        }
    }

    fun onLoadMore() {
        viewModelScope.launch {
            articleDataSource.loadMoreData()
        }
    }
}

/**
 * 自定义实例，多参构造
 * Factory for [LiveDataViewModel].
 */
object LiveDataVMFactory : ViewModelProvider.Factory {

    private val dataSource = KtArticleRepository()

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return LiveDataViewModel(dataSource) as T
    }
}
