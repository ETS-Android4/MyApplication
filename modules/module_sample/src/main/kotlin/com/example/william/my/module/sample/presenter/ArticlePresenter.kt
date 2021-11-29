package com.example.william.my.module.sample.presenter

import com.example.william.my.bean.data.ArticleDetailBean
import com.example.william.my.bean.repo.DataRepository
import com.example.william.my.bean.repo.DataSource.LoadArticleCallback
import com.example.william.my.module.sample.contract.ArticleContract

class ArticlePresenter(private val mDataRepository: DataRepository, private val mArticleView: ArticleContract.View) : ArticleContract.Presenter {

    init {
        mArticleView.setPresenter(this)
    }

    override fun queryArticleList(page: Int) {
        mDataRepository.queryArticleList(page, object : LoadArticleCallback {
            override fun showLoading() {
                mArticleView.showLoading()
            }

            override fun onArticleLoaded(articles: List<ArticleDetailBean>) {
                mArticleView.showArticles(page, articles)
            }

            override fun onFailure(msg: String) {
                if (page == 0) {
                    mArticleView.showEmptyView()
                }
                mArticleView.showToast(msg)
            }
        })
    }

    override fun start() {

    }

    override fun clear() {

    }
}