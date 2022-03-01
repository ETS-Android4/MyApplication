package com.example.william.my.module.sample.frame.model

import androidx.lifecycle.*
import com.example.william.my.bean.data.ArticleDataBean
import com.example.william.my.bean.repo.DataRepository
import com.example.william.my.core.retrofit.response.RetrofitResponse

class ArticleViewModel(private val dataSource: DataRepository) : ViewModel() {

    private val mMutableLiveData = MutableLiveData<Int>()

    val article: LiveData<RetrofitResponse<ArticleDataBean>>
        get() = Transformations.switchMap(mMutableLiveData) { input ->
            dataSource.loadArticle(input)
        }

    fun queryArticle(page: Int) {
        mMutableLiveData.postValue(page)
    }
}

/**
 * 自定义实例，多参构造
 * Factory for [ArticleViewModel].
 */
object ArticleVMFactory : ViewModelProvider.Factory {

    private val dataSource = DataRepository.getInstance()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ArticleViewModel(dataSource) as T
    }
}
