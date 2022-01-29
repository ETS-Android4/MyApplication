package com.example.william.my.module.sample.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.william.my.bean.data.ArticleDetailBean
import com.example.william.my.library.base.BaseRecyclerFragment
import com.example.william.my.module.sample.adapter.ArticleAdapter
import com.example.william.my.module.sample.model.ArticleVMFactory
import com.example.william.my.module.sample.model.LiveDataViewModel

class MvvmFragment : BaseRecyclerFragment<ArticleDetailBean?>() {

    private val mLiveDataViewModel: LiveDataViewModel by viewModels {
        ArticleVMFactory
    }

    override fun getAdapter(): BaseQuickAdapter<ArticleDetailBean?, BaseViewHolder> {
        return ArticleAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        queryData()
    }

    private fun observeViewModel() {
        mLiveDataViewModel.article.observe(viewLifecycleOwner, Observer { articles ->
            onDataSuccess(articles?.data?.datas)
        })
    }

    override fun queryData() {
        super.queryData()
        mLiveDataViewModel.queryArticle(mPage)
    }
}