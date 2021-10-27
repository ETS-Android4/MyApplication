package com.example.william.my.module.sample.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.william.my.core.retrofit.callback.LiveDataCallback
import com.example.william.my.core.retrofit.exception.ApiException
import com.example.william.my.core.retrofit.exception.ExceptionHandler
import com.example.william.my.core.retrofit.response.RetrofitResponse
import com.example.william.my.core.retrofit.utils.RetrofitUtils
import com.example.william.my.retrofit.ArticleBean
import com.example.william.my.retrofit.ArticleDataBean
import com.example.william.my.module.sample.api.KtArticleService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class KtArticleRepository : KtArticleDataSource {

    private var counter = 0

    private val _article = MutableLiveData<com.example.william.my.retrofit.ArticleBean>()
    override val article: LiveData<com.example.william.my.retrofit.ArticleBean> = _article

    // 移动到IO调度程序以使其成为安全的
    // move the execution to an IO dispatcher to make it main-safe
    private suspend fun getArticle(counter: Int): com.example.william.my.retrofit.ArticleBean =
        withContext(Dispatchers.IO) {
            val api = RetrofitUtils.buildApi(KtArticleService::class.java)
            api.getArticle(counter)
        }

    override suspend fun fetchNewData() {
        withContext(Dispatchers.Main) {
            counter = 0
            _article.value = getArticle(counter)
        }
    }

    override suspend fun loadMoreData() {
        withContext(Dispatchers.Main) {
            counter++
            _article.value = getArticle(counter)
        }
    }

    private val _articleResponse = MutableLiveData<RetrofitResponse<com.example.william.my.retrofit.ArticleDataBean>>()
    override val articleResponse: LiveData<RetrofitResponse<com.example.william.my.retrofit.ArticleDataBean>> =
        _articleResponse

    private fun buildArticleResponseFLow(counter: Int): Flow<RetrofitResponse<com.example.william.my.retrofit.ArticleDataBean>> =
        flow {
            val articleResponse = getArticleResponse(counter)
            emit(articleResponse)
        }

    private suspend fun getArticleResponse(counter: Int): RetrofitResponse<com.example.william.my.retrofit.ArticleDataBean> =
        withContext(Dispatchers.IO) {
            val api = RetrofitUtils.buildApi(KtArticleService::class.java)
            api.getArticleResponse(counter)
        }

    override suspend fun fetchNewDataResponse() {
        counter = 0
        RetrofitUtils.buildFlow(
            buildArticleResponseFLow(counter), LiveDataCallback(_articleResponse)
        )
    }

    override suspend fun loadMoreDataResponse() {
        counter++
        RetrofitUtils.buildFlow(
            buildArticleResponseFLow(counter), LiveDataCallback(_articleResponse)
        )
    }

    private suspend fun fetchNewDataFLow() {
        withContext(Dispatchers.Main) {
            counter = 0
            buildArticleResponseFLow(counter)
                .onStart {
                    _articleResponse.postValue(RetrofitResponse.loading())
                }
                .catch { exception ->
                    val e: ApiException = ExceptionHandler.handleException(exception)
                    _articleResponse.postValue(RetrofitResponse.error(e.message))
                }
                .collect { article ->
                    _articleResponse.postValue(article)
                }
        }
    }
}
