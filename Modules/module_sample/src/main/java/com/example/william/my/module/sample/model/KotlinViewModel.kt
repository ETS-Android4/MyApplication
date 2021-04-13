package com.example.william.my.module.sample.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.william.my.module.bean.ArticleBean
import com.example.william.my.module.sample.repo.KtArticleRepository
import kotlinx.coroutines.launch

/**
 * Showcases different patterns using the liveData coroutines builder.
 */
class LiveDataViewModel(private val articleDataSource: KtArticleRepository) : ViewModel() {

    // Exposed cached value in the data source that can be updated later on
    val article: LiveData<ArticleBean> = articleDataSource.article

    // Called when the user clicks on the "FETCH NEW DATA" button. Updates value in data source.
    fun onRefresh() {
        // Launch a coroutine that reads from a remote data source and updates cache
        viewModelScope.launch {
            articleDataSource.fetchNewData()
        }
    }

    fun onLoadMore() {

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
