package com.example.william.my.module.sample.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.module.router.ARouterPath
import com.example.william.my.module.sample.R
import com.example.william.my.module.sample.adapter.ArticleBindAdapter
import com.example.william.my.module.sample.databinding.SampleActivityDataBindingBinding
import com.example.william.my.module.sample.model.DataBindingVMFactory
import com.example.william.my.module.sample.model.DataBindingViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

/**
 * https://developer.android.google.cn/topic/libraries/data-binding/binding-adapters
 */
@Route(path = ARouterPath.Sample.Sample_BindingAdapter)
class BindingAdapterActivity : AppCompatActivity(), OnRefreshLoadMoreListener {

    // Obtain ViewModel
    private val mViewModel: DataBindingViewModel by viewModels {
        DataBindingVMFactory
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

        initView()

        subscribeToModel()
    }

    private fun initView() {
        val mAdapter = ArticleBindAdapter()
        mBinding.recycleView.adapter = mAdapter

        mBinding.smartRefresh.setOnRefreshLoadMoreListener(this)
    }

    private fun subscribeToModel() {
        mViewModel.fetchNewData(0)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mPage = 0
        mViewModel.fetchNewData(mPage)
        mBinding.smartRefresh.finishRefresh(1000)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mPage++
        mViewModel.fetchNewData(mPage)
        mBinding.smartRefresh.finishLoadMore(1000)
    }
}