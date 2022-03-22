package com.example.william.my.module.sample.frame.fragment

import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.william.my.bean.data.ArticleDetailBean
import com.example.william.my.library.base.BaseRecyclerFragment
import com.example.william.my.module.sample.adapter.ArticleAdapter
import com.example.william.my.module.sample.frame.utils.obtainViewModel
import com.example.william.my.module.sample.frame.viewmodel.ArticleLiveDataViewModel

/**
 * Model-View-ViewModel
 * 通过 ViewModel 将数据（Model）和 UI（View）隔离，再通过 LiveData 将数据和 UI 的绑定，实现数据驱动 UI，只要 LiveData 的数据修改 UI 能自动响应更新。
 */
class MvvmFragment : BaseRecyclerFragment<ArticleDetailBean?>() {

    private lateinit var viewModel: ArticleLiveDataViewModel

    override fun getAdapter(): BaseQuickAdapter<ArticleDetailBean?, BaseViewHolder> {
        return ArticleAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel = obtainViewModel()

        viewModel.items.observe(viewLifecycleOwner) { article ->
            onDataSuccess(article)
        }
    }

    override fun queryData() {
        super.queryData()
        viewModel.loadArticle(mPage)
    }

    private fun obtainViewModel(): ArticleLiveDataViewModel = obtainViewModel(ArticleLiveDataViewModel::class.java)
}