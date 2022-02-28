package com.example.william.my.module.sample.frame.presenter

import com.example.william.my.bean.data.ArticleDetailBean
import com.example.william.my.bean.repo.ArticleRepository
import com.example.william.my.bean.repo.ArticleDataSource.LoadArticleCallback
import com.example.william.my.module.sample.frame.contract.ArticleContract

class ArticlePresenter(
    private val mArticleRepository: ArticleRepository,
    private val mArticleView: ArticleContract.View
) : ArticleContract.Presenter {

    override fun queryArticle(page: Int) {
        mArticleRepository.getArticle(page, object : LoadArticleCallback {
            override fun showLoading() {

            }

            override fun onFailure(msg: String) {
                mArticleView.onDataFail()
                mArticleView.showToast(msg)
            }

            override fun onArticleLoaded(articles: List<ArticleDetailBean>) {
                mArticleView.showArticles(articles)
            }
        })
    }

    override fun start() {

    }

    override fun clear() {

    }
}