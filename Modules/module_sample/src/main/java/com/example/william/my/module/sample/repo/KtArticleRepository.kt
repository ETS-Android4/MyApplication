package com.example.william.my.module.sample.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.william.my.core.network.retrofit.utils.RetrofitUtils
import com.example.william.my.module.bean.ArticleBean
import com.example.william.my.module.sample.api.KtArticleService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class KtArticleRepository : KtArticleDataSource {

    private val _article = MutableLiveData<ArticleBean>()
    override val article: LiveData<ArticleBean> = _article

    override suspend fun fetchNewData() {
        withContext(Dispatchers.Main) {
            _article.value = getArticle()
        }
    }

    private var counter = 0

    private suspend fun getArticle(): ArticleBean = withContext(Dispatchers.IO) {
        val api = RetrofitUtils.buildApi(KtArticleService::class.java)
        api.getArticle(counter)
    }
}
