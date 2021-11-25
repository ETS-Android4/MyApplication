package com.example.william.my.module.jetpack.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.william.my.bean.data.ArticleBean
import com.example.william.my.bean.data.ArticleDataBean
import com.example.william.my.core.retrofit.response.RetrofitResponse
import com.example.william.my.module.jetpack.repo.KtArticleRepository
import kotlinx.coroutines.launch

/**
 * ViewModel 应创建协程
 */
class KtArticleViewModel(private val articleDataSource: KtArticleRepository) : ViewModel() {

    val article: LiveData<ArticleBean> = articleDataSource.article

    val articleResponse: LiveData<RetrofitResponse<ArticleDataBean>> = articleDataSource.articleResponse

    fun onRefresh() {
        viewModelScope.launch {
            //articleDataSource.fetchNewData()
            articleDataSource.fetchNewDataResponse()
        }
    }

    fun onLoadMore() {
        viewModelScope.launch {
            //articleDataSource.loadMoreData()
            articleDataSource.loadMoreDataResponse()
        }
    }
}

/**
 * 自定义实例，多参构造
 * Factory for [ArticleViewModel].
 */
object LiveDataVMFactory : ViewModelProvider.Factory {

    private val dataSource = KtArticleRepository()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return KtArticleViewModel(dataSource) as T
    }
}
