package com.example.william.my.module.sample.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.william.my.bean.data.ArticleDetailBean
import com.example.william.my.library.base.BaseRecyclerFragment
import com.example.william.my.module.sample.adapter.ArticleAdapter
import com.example.william.my.module.sample.model.StateVMFactory
import com.example.william.my.module.sample.model.StateFlowViewModel
import com.example.william.my.module.sample.state.ArticleUiState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MviFragment : BaseRecyclerFragment<ArticleDetailBean?>() {

    private val mStateFlowViewModel: StateFlowViewModel by viewModels {
        StateVMFactory
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
    }

    override fun queryData() {
        super.queryData()
        mStateFlowViewModel.getArticle(mPage)
    }
}