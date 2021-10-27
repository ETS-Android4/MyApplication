package com.example.william.my.module.sample.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.module.router.ARouterPath
import com.example.william.my.module.sample.R
import com.example.william.my.module.sample.adapter.ArticleDataBindAdapter
import com.example.william.my.module.sample.databinding.SampleLayoutRecyclerBindBinding
import com.example.william.my.module.sample.model.KtArticleViewModel
import com.example.william.my.module.sample.model.LiveDataVMFactory
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

@Route(path = ARouterPath.Sample.Sample_Kotlin_Bind)
class KtBindActivity : AppCompatActivity(), OnRefreshLoadMoreListener {

    // Obtain ViewModel
    private val mViewModel: KtArticleViewModel by viewModels {
        LiveDataVMFactory
    }

    private lateinit var mBinding: SampleLayoutRecyclerBindBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.sample_layout_recycler)

        // Obtain binding object using the Data Binding library
        mBinding = DataBindingUtil.setContentView<SampleLayoutRecyclerBindBinding>(
            this, R.layout.sample_layout_recycler_bind
        )

        // Set the LifecycleOwner to be able to observe LiveData objects
        mBinding.lifecycleOwner = this

        // Bind ViewModel
        mBinding.viewModel = mViewModel

        initView()

        subscribeToModel()
    }

    private fun initView() {
        val mAdapter = ArticleDataBindAdapter()
        mBinding.recycleView.adapter = mAdapter

        mBinding.smartRefresh.setOnRefreshLoadMoreListener(this)
    }

    private fun subscribeToModel() {
        mViewModel.onRefresh()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mViewModel.onRefresh()
        mBinding.smartRefresh.finishRefresh(1000)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mViewModel.onLoadMore()
        mBinding.smartRefresh.finishLoadMore(1000)
    }
}