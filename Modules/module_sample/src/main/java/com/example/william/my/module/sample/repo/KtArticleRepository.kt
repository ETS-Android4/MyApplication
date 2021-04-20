package com.example.william.my.module.sample.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.william.my.core.network.retrofit.utils.RetrofitUtils
import com.example.william.my.module.bean.ArticleBean
import com.example.william.my.module.bean.ArticleDataBean
import com.example.william.my.module.sample.api.KtArticleService
import com.example.william.my.module.sample.utils.KtRetrofit
import com.example.william.my.module.sample.utils.callback.KtLiveDataCallback
import com.example.william.my.module.sample.utils.callback.KtRetrofitFlowCallback
import com.example.william.my.module.sample.utils.exception.KtApiException
import com.example.william.my.module.sample.utils.exception.KtExceptionHandler
import com.example.william.my.module.sample.utils.response.KtRetrofitResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class KtArticleRepository : KtArticleDataSource {

    private var counter = 0

    private val _article = MutableLiveData<ArticleBean>()
    override val article: LiveData<ArticleBean> = _article

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

    // 移动到IO调度程序以使其成为安全的
    // move the execution to an IO dispatcher to make it main-safe
    private suspend fun getArticle(counter: Int): ArticleBean =
        withContext(Dispatchers.IO) {
            val api = RetrofitUtils.buildApi(KtArticleService::class.java)
            api.getArticle(counter)
        }

    private val _articleData = MutableLiveData<KtRetrofitResponse<ArticleDataBean>>()
    override val articleData: LiveData<KtRetrofitResponse<ArticleDataBean>> = _articleData

    private suspend fun getArticleResponse(counter: Int): KtRetrofitResponse<ArticleDataBean> =
        withContext(Dispatchers.IO) {
            val api = KtRetrofit.buildApi(KtArticleService::class.java)
            api.getArticleResponse(counter)
        }

    private fun getArticleResponseFlow(counter: Int): Flow<KtRetrofitResponse<ArticleDataBean>> =
        flow {
            val articleResponse = getArticleResponse(counter)
            emit(articleResponse)
        }

    override suspend fun fetchNewDataResponse() {

        //fetchNewDataBase()

        //fetchNewDataFlowCallback()

        fetchNewDataLiveDataCallback()
    }

    private suspend fun fetchNewDataBase() {
        withContext(Dispatchers.Main) {
            counter = 0
            getArticleResponseFlow(counter)
                .onStart {
                    //_articleData.postValue(KtRetrofitResponse.loading())
                }
                .catch { exception ->
                    // 捕获上游出现的异常
                    val e: KtApiException = KtExceptionHandler.handleException(exception)
                    _articleData.postValue(KtRetrofitResponse.error(e.message))
                }
                .collect { article ->
                    // 更新视图
                    // Update View with the latest favorite news
                    _articleData.postValue(article)
                }
        }
    }

    private suspend fun fetchNewDataFlowCallback() {
        KtRetrofit.buildFlow(
            getArticleResponseFlow(counter),
            object : KtRetrofitFlowCallback<KtRetrofitResponse<ArticleDataBean>> {
                override fun onLoading() {
                    _articleData.postValue(KtRetrofitResponse.loading())
                }

                override fun onResponse(response: KtRetrofitResponse<ArticleDataBean>) {
                    _articleData.postValue(response)
                }

                override fun onFailure(e: KtApiException) {
                    _articleData.postValue(KtRetrofitResponse.error(e.message))
                }
            })
    }

    private suspend fun fetchNewDataLiveDataCallback() {
        KtRetrofit.buildFlow(
            getArticleResponseFlow(counter), KtLiveDataCallback(_articleData)
        )
    }
}
