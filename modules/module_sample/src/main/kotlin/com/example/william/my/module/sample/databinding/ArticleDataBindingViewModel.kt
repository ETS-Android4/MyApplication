package com.example.william.my.module.sample.databinding

import androidx.lifecycle.*
import com.example.william.my.bean.data.ArticleDataBean
import com.example.william.my.core.retrofit.exception.ApiException
import com.example.william.my.core.retrofit.exception.ExceptionHandler
import com.example.william.my.core.retrofit.response.RetrofitResponse
import com.example.william.my.module.sample.frame.data.source.ArticleDataSource
import com.example.william.my.module.sample.frame.data.source.ArticleRepository
import com.example.william.my.module.sample.frame.data.source.remote.ArticleRemoteDataSource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ArticleDataBindingViewModel(private val dataSource: ArticleDataSource) : ViewModel() {

    private val _article = MutableLiveData<RetrofitResponse<ArticleDataBean>>()
    val article: LiveData<RetrofitResponse<ArticleDataBean>> = _article

    init {
        loadArticle(0)
    }

    fun loadArticle(page: Int) {
        viewModelScope.launch {
            dataSource.getArticleFlow(page)
                .onStart {
                    _article.postValue(RetrofitResponse.loading())
                }
                .catch { exception ->
                    val e: ApiException = ExceptionHandler.handleException(exception)
                    _article.postValue(RetrofitResponse.error(e.message))
                }
                .collect { article ->
                    _article.postValue(article)
                }
        }
    }
}

/**
 * 自定义实例，多参构造
 * Factory for [ArticleDataBindingViewModel].
 */
object ArticleDataBindingVMFactory : ViewModelProvider.Factory {

    private val dataSource = ArticleRepository.getInstance(ArticleRemoteDataSource)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ArticleDataBindingViewModel(dataSource) as T
    }
}
