package com.example.william.my.module.sample.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.william.my.module.bean.ArticleBean
import com.example.william.my.module.bean.ArticleDataBean
import com.example.william.my.module.sample.repo.KtArticleRepository
import com.example.william.my.module.sample.retrofit.response.KtRetrofitResponse
import kotlinx.coroutines.launch

/**
 * ViewModel 应创建协程
 */
class KtArticleViewModel(private val articleDataSource: KtArticleRepository) : ViewModel() {

    val article: LiveData<ArticleBean> = articleDataSource.article

    val articleData: LiveData<KtRetrofitResponse<ArticleDataBean>> = articleDataSource.articleData

    fun onRefresh() {
        viewModelScope.launch {
            //articleDataSource.fetchNewData()
            articleDataSource.fetchNewDataResponse()
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
 * Factory for [KtArticleViewModel].
 */
object LiveDataVMFactory : ViewModelProvider.Factory {

    private val dataSource = KtArticleRepository()

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return KtArticleViewModel(dataSource) as T
    }
}
