package com.example.william.my.module.sample.contract

import com.example.william.my.bean.data.ArticleDetailBean
import com.example.william.my.library.presenter.IBasePresenter
import com.example.william.my.library.view.IBaseView

interface ArticleContract {
    interface View : IBaseView<Presenter?> {
        fun showArticles(page: Int, article: List<ArticleDetailBean?>)
        fun showEmptyView()
        fun showArticlesNoMore()
    }

    interface Presenter : IBasePresenter {
        fun queryArticleList(page: Int)
    }
}