package com.example.william.my.module.sample.frame.fragment

import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.william.my.bean.data.ArticleDetailBean
import com.example.william.my.library.base.BaseRecyclerFragment
import com.example.william.my.module.sample.adapter.ArticleAdapter
import com.example.william.my.module.sample.frame.contract.ArticleContract
import com.example.william.my.module.sample.frame.data.source.ArticleRepository
import com.example.william.my.module.sample.frame.data.source.remote.ArticleRemoteDataSource
import com.example.william.my.module.sample.frame.presenter.ArticlePresenter

/**
 * MVP：Model-View-Presenter
 */
class MvpFragment : BaseRecyclerFragment<ArticleDetailBean?>(), ArticleContract.View {

    private lateinit var articlePresenter: ArticlePresenter

    override fun getAdapter(): BaseQuickAdapter<ArticleDetailBean?, BaseViewHolder> {
        return ArticleAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create the presenter
        articlePresenter = ArticlePresenter(ArticleRepository.getInstance(ArticleRemoteDataSource), this)

        queryData()
    }

    override fun queryData() {
        super.queryData()
        articlePresenter.loadArticle(mPage)
    }

    override fun showArticle(article: List<ArticleDetailBean>) {
        onDataSuccess(article)
    }

    override fun showNoArticle() {
        onDataFail()
    }
}