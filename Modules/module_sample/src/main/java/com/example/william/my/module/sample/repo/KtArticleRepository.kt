package com.example.william.my.module.sample.repo

import androidx.lifecycle.LiveData
import com.example.william.my.core.network.retrofit.utils.RetrofitUtils
import com.example.william.my.module.bean.ArticleBean
import com.example.william.my.module.sample.api.KtArticleService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class KtArticleRepository : KtArticleDataSource {

    override fun getArticleBean(): LiveData<ArticleBean> {
        TODO("Not yet implemented")
    }

    override val article: LiveData<ArticleBean>
        get() = TODO("Not yet implemented")

    override suspend fun fetchNewData() {
        TODO("Not yet implemented")
    }

    private var counter = 0

    private suspend fun getArticle(): Flow<ArticleBean> = flow {

        val api = RetrofitUtils.buildApi(KtArticleService::class.java)

        val article = api.getArticle(counter)
        counter++

        emit(article) // Emits the result of the request to the flow 向数据流发送请求结果
        delay(3000) // Suspends the coroutine for some time 挂起一段时间
    }
}
