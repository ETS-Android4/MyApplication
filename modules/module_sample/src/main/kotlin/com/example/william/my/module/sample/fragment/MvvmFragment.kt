package com.example.william.my.module.sample.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.william.my.bean.data.ArticleDataBean
import com.example.william.my.bean.data.ArticleDetailBean
import com.example.william.my.core.retrofit.response.RetrofitResponse
import com.example.william.my.library.base.BaseRecyclerFragment
import com.example.william.my.module.sample.adapter.ArticleAdapter
import com.example.william.my.module.sample.model.ArticleViewModel
import com.example.william.my.module.sample.model.LiveDataViewModel

class MvvmFragment : BaseRecyclerFragment<ArticleDetailBean?>() {

    private val mArticleViewModel: LiveDataViewModel by viewModels()

    override fun getAdapter(): BaseQuickAdapter<ArticleDetailBean?, BaseViewHolder> {
        return ArticleAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mArticleViewModel.articleResponse.observe(viewLifecycleOwner, Observer { articles ->
            onDataSuccess(articles?.data?.datas)
        })
        queryData()
    }

    override fun queryData() {
        super.queryData()
        mArticleViewModel.request()
    }
}