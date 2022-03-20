package com.example.william.my.module.sample.frame.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.william.my.bean.data.ArticleDetailBean
import com.example.william.my.library.base.BaseRecyclerFragment
import com.example.william.my.module.sample.adapter.ArticleAdapter
import com.example.william.my.module.sample.frame.model.StateFlowVMFactory
import com.example.william.my.module.sample.frame.model.StateFlowViewModel
import com.example.william.my.module.sample.frame.state.ArticleUiState
import kotlinx.coroutines.launch

/**
 * MVI：Model-View-Intent
 * 1. 将 LiveData 组件改成了 StateFlow
 * 2. ViewModel 传递给 View 的数据限制为 View 的 UIState
 */
class MviFragment : BaseRecyclerFragment<ArticleDetailBean?>() {

    private val mStateFlowViewModel: StateFlowViewModel by viewModels {
        StateFlowVMFactory
    }

    override fun getAdapter(): BaseQuickAdapter<ArticleDetailBean?, BaseViewHolder> {
        return ArticleAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            mStateFlowViewModel.state.collect {
                when (it) {
                    ArticleUiState.Idle -> {

                    }
                    is ArticleUiState.Loading -> {

                    }
                    is ArticleUiState.Users -> {
                        onDataSuccess(it.list)
                    }
                    is ArticleUiState.Error -> {
                        showToast(it.error)
                    }
                }
            }
        }
        queryData()
    }

    override fun queryData() {
        super.queryData()
        mStateFlowViewModel.getArticle(mPage)
    }
}