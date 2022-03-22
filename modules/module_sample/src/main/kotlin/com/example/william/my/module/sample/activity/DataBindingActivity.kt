package com.example.william.my.module.sample.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.module.router.ARouterPath
import com.example.william.my.module.sample.R
import com.example.william.my.module.sample.adapter.ArticleBindAdapter
import com.example.william.my.module.sample.databinding.ArticleDataBindingVMFactory
import com.example.william.my.module.sample.databinding.ArticleDataBindingViewModel
import com.example.william.my.module.sample.databinding.SampleActivityDataBindingBinding
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

/**
 * DataBinding
 * https://developer.android.google.cn/topic/libraries/data-binding
 */
@Route(path = ARouterPath.Sample.Sample_BindingAdapter)
class DataBindingActivity : AppCompatActivity(), OnRefreshLoadMoreListener {

    // Obtain ViewModel
    private val mViewModel: ArticleDataBindingViewModel by viewModels {
        ArticleDataBindingVMFactory
    }

    private var mPage = 0

    lateinit var mBinding: SampleActivityDataBindingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.sample_activity_data_binding)

        // Obtain binding object using the Data Binding library
        mBinding = DataBindingUtil.setContentView(this, R.layout.sample_activity_data_binding)

        // Set the LifecycleOwner to be able to observe LiveData objects
        mBinding.lifecycleOwner = this

        // Bind ViewModel
        mBinding.viewModel = mViewModel

        mBinding.recycleView.adapter = ArticleBindAdapter()

        mBinding.smartRefresh.setOnRefreshLoadMoreListener(this)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mPage = 0
        mViewModel.loadArticle(mPage)
        mBinding.smartRefresh.finishRefresh(1000)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mPage++
        mViewModel.loadArticle(mPage)
        mBinding.smartRefresh.finishLoadMore(1000)
    }
}