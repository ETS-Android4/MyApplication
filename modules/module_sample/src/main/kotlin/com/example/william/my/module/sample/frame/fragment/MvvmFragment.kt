package com.example.william.my.module.sample.frame.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.william.my.bean.data.ArticleDetailBean
import com.example.william.my.library.base.BaseRecyclerFragment
import com.example.william.my.module.sample.adapter.ArticleAdapter
import com.example.william.my.module.sample.frame.model.ArticleVMFactory
import com.example.william.my.module.sample.frame.model.ArticleViewModel

/**
 * Model-View-ViewModel
 * 通过 ViewModel 将数据（Model）和 UI（View）隔离，再通过 LiveData 将数据和 UI 的绑定，实现数据驱动 UI，只要L iveData 的数据修改 UI 能自动响应更新。
 */
class MvvmFragment : BaseRecyclerFragment<ArticleDetailBean?>() {

    private val mArticleViewModel: ArticleViewModel by viewModels {
        ArticleVMFactory
    }

    override fun getAdapter(): BaseQuickAdapter<ArticleDetailBean?, BaseViewHolder> {
        return ArticleAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
    }

    private fun observeViewModel() {
        mArticleViewModel.article.observe(viewLifecycleOwner) { articles ->
            onDataSuccess(articles?.data?.datas)
        }
        queryData()
    }

    override fun queryData() {
        super.queryData()
        mArticleViewModel.queryArticle(mPage)
    }
}