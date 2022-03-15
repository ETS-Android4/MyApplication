package com.example.william.my.module.sample.frame.fragment

import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.william.my.bean.data.ArticleDetailBean
import com.example.william.my.bean.repo.ArticleRepository
import com.example.william.my.library.base.BaseRecyclerFragment
import com.example.william.my.module.sample.adapter.ArticleAdapter
import com.example.william.my.module.sample.contract.ArticleContract
import com.example.william.my.module.sample.frame.presenter.ArticlePresenter

/**
 * MVP：Model-View-Presenter
 */
class MvpFragment : BaseRecyclerFragment<ArticleDetailBean?>(), ArticleContract.View {

    lateinit var mPresenter: ArticlePresenter

    override fun getAdapter(): BaseQuickAdapter<ArticleDetailBean?, BaseViewHolder> {
        return ArticleAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPresenter = ArticlePresenter(ArticleRepository.getInstance(), this)
        queryData()
    }

    override fun queryData() {
        super.queryData()
        mPresenter.queryArticle(mPage)
    }

    override fun showArticles(article: List<ArticleDetailBean?>) {
        onDataSuccess(article)
    }
}