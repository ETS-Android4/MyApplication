package com.example.william.my.module.sample.presenter

import com.example.william.my.bean.data.ArticleDetailBean
import com.example.william.my.bean.repo.DataRepository
import com.example.william.my.bean.repo.DataSource.LoadArticleCallback
import com.example.william.my.module.sample.contract.ArticleContract

class ArticlePresenter(private val mDataRepository: DataRepository, private val mArticleView: ArticleContract.View) : ArticleContract.Presenter {

    override fun queryArticleList(page: Int) {
        mDataRepository.queryArticleList(page, object : LoadArticleCallback {
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