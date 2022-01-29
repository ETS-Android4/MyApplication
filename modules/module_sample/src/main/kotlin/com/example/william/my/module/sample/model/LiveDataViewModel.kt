package com.example.william.my.module.sample.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.william.my.bean.repo.DataRepository

class LiveDataViewModel(private val dataSource: DataRepository) : ViewModel() {

    private val mMutableLiveData = MutableLiveData<Int>()

    val article = Transformations.switchMap(mMutableLiveData) { input ->
        dataSource.getArticle(input)
    }

    fun queryArticle(page: Int) {
        mMutableLiveData.postValue(page)
    }
}

/**
 * 自定义实例，多参构造
 * Factory for [DataBindingViewModel].
 */
object ArticleVMFactory : ViewModelProvider.Factory {

    private val dataSource = DataRepository.getInstance()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return LiveDataViewModel(dataSource) as T
    }
}
