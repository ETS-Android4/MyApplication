package com.example.william.my.module.sample.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.william.my.bean.api.NetworkService
import com.example.william.my.bean.data.ArticleDataBean
import com.example.william.my.core.retrofit.callback.LiveDataCallback
import com.example.william.my.core.retrofit.exception.ApiException
import com.example.william.my.core.retrofit.exception.ExceptionHandler
import com.example.william.my.core.retrofit.response.RetrofitResponse
import com.example.william.my.core.retrofit.utils.RetrofitUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class ArticleRepository {

    private val _article = MutableLiveData<RetrofitResponse<ArticleDataBean>>()
    val article: LiveData<RetrofitResponse<ArticleDataBean>> = _article

    private suspend fun getArticle(counter: Int): RetrofitResponse<ArticleDataBean> =
        withContext(Dispatchers.IO) {
            val api = RetrofitUtils.buildApi(NetworkService::class.java)
            api.getArticleResponseSuspend(counter)
        }

    private fun buildArticleFLow(counter: Int): Flow<RetrofitResponse<ArticleDataBean>> =
        flow {
            val articleResponse = getArticle(counter)
            emit(articleResponse)
        }

    suspend fun fetchNewData(page: Int) {
        RetrofitUtils.buildFlow(
            buildArticleFLow(page), LiveDataCallback(_article)
        )
    }

    private suspend fun fetchNewDataFLow(page: Int) {
        withContext(Dispatchers.Main) {
            buildArticleFLow(page)
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
