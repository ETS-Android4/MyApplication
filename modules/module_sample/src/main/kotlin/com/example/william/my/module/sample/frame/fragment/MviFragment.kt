package com.example.william.my.module.sample.frame.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.william.my.bean.data.ArticleDetailBean
import com.example.william.my.library.base.BaseRecyclerFragment
import com.example.william.my.module.sample.adapter.ArticleAdapter
import com.example.william.my.module.sample.frame.intent.ArticleIntent
import com.example.william.my.module.sample.frame.intent.ArticleViewState
import com.example.william.my.module.sample.frame.utils.obtainViewModel
import com.example.william.my.module.sample.frame.viewmodel.ArticleStateFlowViewModel
import kotlinx.coroutines.launch

/**
 * MVI：Model-View-Intent
 * 1. 将 LiveData 组件改成了 StateFlow
 * 2. ViewModel 传递给 View 的数据限制为 View 的 UIState
 */
class MviFragment : BaseRecyclerFragment<ArticleDetailBean?>() {

    private lateinit var viewModel: ArticleStateFlowViewModel

    override fun getAdapter(): BaseQuickAdapter<ArticleDetailBean?, BaseViewHolder> {
        return ArticleAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel = obtainViewModel()

        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    when (it) {
                        is ArticleViewState.Loading -> {

                        }
                        is ArticleViewState.Success -> {
                            onDataSuccess(it.news)
                        }
                        is ArticleViewState.Error -> {
                            showToast(it.error)
                        }
                    }
                }
            }
        }
    }

    override fun queryData() {
        super.queryData()
        lifecycleScope.launch {
            viewModel.intent.send(ArticleIntent.LoadArticleIntent(mPage))
        }
    }

    private fun obtainViewModel(): ArticleStateFlowViewModel = obtainViewModel(ArticleStateFlowViewModel::class.java)
}