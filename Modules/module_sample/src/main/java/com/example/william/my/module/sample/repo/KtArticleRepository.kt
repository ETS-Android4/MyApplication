package com.example.william.my.module.sample.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.william.my.core.network.retrofit.utils.RetrofitUtils
import com.example.william.my.module.bean.ArticleBean
import com.example.william.my.module.sample.api.KtArticleService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class KtArticleRepository : KtArticleDataSource {

    private var counter = 0
    
    private val _article = MutableLiveData<ArticleBean>()
    override val article: LiveData<ArticleBean> = _article

    override suspend fun fetchNewData() {

    }

    override suspend fun loadMoreData() {

    }

    // 移动到IO调度程序以使其成为安全的
    // move the execution to an IO dispatcher to make it main-safe
    private suspend fun getArticle(counter: Int): ArticleBean = withContext(Dispatchers.IO) {
        val api = RetrofitUtils.buildApi(KtArticleService::class.java)
        api.getArticle(counter)
    }
}
