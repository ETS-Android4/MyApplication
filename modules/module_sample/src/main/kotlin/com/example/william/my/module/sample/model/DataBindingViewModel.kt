package com.example.william.my.module.sample.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.william.my.bean.data.ArticleDataBean
import com.example.william.my.core.retrofit.response.RetrofitResponse
import com.example.william.my.module.sample.repo.DataBindingRepository
import kotlinx.coroutines.launch

/**
 * ViewModel 应创建协程
 */
class DataBindingViewModel(private val dataSource: DataBindingRepository) : ViewModel() {

    val article: LiveData<RetrofitResponse<ArticleDataBean>> = dataSource.article

    fun fetchNewData(page: Int) {
        viewModelScope.launch {
            dataSource.fetchNewDataByUtils(page)
        }
    }
}

/**
 * 自定义实例，多参构造
 * Factory for [DataBindingViewModel].
 */
object DataBindingVMFactory : ViewModelProvider.Factory {

    private val dataSource = DataBindingRepository()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return DataBindingViewModel(dataSource) as T
    }
}
