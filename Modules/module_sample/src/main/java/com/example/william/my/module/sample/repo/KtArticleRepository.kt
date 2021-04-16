package com.example.william.my.module.sample.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.william.my.core.network.retrofit.utils.RetrofitUtils
import com.example.william.my.module.bean.ArticleBean
import com.example.william.my.module.bean.ArticleDetailBean
import com.example.william.my.module.sample.api.KtArticleService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
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

    private val _articleList = MutableLiveData<List<ArticleDetailBean>>()
    override val articleList: LiveData<List<ArticleDetailBean>> = _articleList

    suspend fun fetchNewDataByFlow() {
        counter = 0
        getArticleFLow(counter)
            .collect {
                _articleList.value = it.data.datas
            }
    }

    // 移动到IO调度程序以使其成为安全的
    // move the execution to an IO dispatcher to make it main-safe
    private suspend fun getArticle(counter: Int): ArticleBean = withContext(Dispatchers.IO) {
        val api = RetrofitUtils.buildApi(KtArticleService::class.java)
        api.getArticle(counter)
    }

    private fun getArticleFLow(counter: Int): Flow<ArticleBean> = flow {
        val api = RetrofitUtils.buildApi(KtArticleService::class.java)
        emit(api.getArticle(counter))
    }
}
