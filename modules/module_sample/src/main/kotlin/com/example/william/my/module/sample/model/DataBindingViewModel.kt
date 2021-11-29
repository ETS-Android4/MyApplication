package com.example.william.my.module.sample.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.william.my.bean.data.ArticleDataBean
import com.example.william.my.core.retrofit.response.RetrofitResponse
import com.example.william.my.module.sample.repo.ArticleRepository
import kotlinx.coroutines.launch

/**
 * ViewModel 应创建协程
 */
class DataBindingViewModel(private val articleDataSource: ArticleRepository) : ViewModel() {

    val article: LiveData<RetrofitResponse<ArticleDataBean>> = articleDataSource.article

    fun fetchNewData(page: Int) {
        viewModelScope.launch {
            articleDataSource.fetchNewData(page)
        }
    }
}

/**
 * 自定义实例，多参构造
 * Factory for [LiveDataViewModel].
 */
object DataBindingVMFactory : ViewModelProvider.Factory {

    private val dataSource = ArticleRepository()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return DataBindingViewModel(dataSource) as T
    }
}
