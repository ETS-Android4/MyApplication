package com.example.william.my.module.sample.activity

import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ToastUtils
import com.example.william.my.module.bean.ArticleDetailBean
import com.example.william.my.module.router.ARouterPath
import com.example.william.my.module.sample.adapter.ArticleAdapter
import com.example.william.my.module.sample.databinding.SampleLayoutRecyclerBinding
import com.example.william.my.module.sample.model.KtArticleViewModel
import com.example.william.my.module.sample.model.LiveDataVMFactory
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

@Route(path = ARouterPath.Sample.Sample_Kotlin)
class KtActivity : AppCompatActivity(), OnRefreshLoadMoreListener {

    // Obtain ViewModel
    private val mViewModel: KtArticleViewModel by viewModels {
        LiveDataVMFactory
    }

    private lateinit var mBinding: SampleLayoutRecyclerBinding

    private lateinit var mAdapter: ArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.sample_layout_recycler)

        mBinding = SampleLayoutRecyclerBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initView()

        subscribeToModel()
    }

    private fun initView() {
        mAdapter = ArticleAdapter()
        mBinding.recycleView.adapter = mAdapter
        mBinding.recycleView.layoutManager = LinearLayoutManager(this)
        mBinding.smartRefreshLayout.setOnRefreshLoadMoreListener(this)
    }

    private fun subscribeToModel() {
        mViewModel.articleData.observe(this, Observer {
            if (it.data?.datas.isNullOrEmpty()) {
                onDataNotAvailable(it.data?.curPage == 1)
            } else {
                showArticles(it.data?.curPage == 1, it.data!!.datas)
            }
        })
        mViewModel.onRefresh()
    }

    private fun onDataNotAvailable(isFirst: Boolean) {
        if (isFirst) {
            showEmptyView()
        } else {
            onDataNoMore()
        }
    }

    private fun showEmptyView() {
        val textView = TextView(this)
        textView.gravity = Gravity.CENTER
        textView.text = "无数据"
        mAdapter.setEmptyView(textView)
        mAdapter.notifyDataSetChanged()
        mBinding.smartRefreshLayout.setEnableLoadMore(false)
    }

    private fun onDataNoMore() {
        ToastUtils.showShort("无更多数据")
    }

    private fun showArticles(isFirst: Boolean, articles: MutableList<ArticleDetailBean>) {
        if (isFirst) {
            mAdapter.setNewInstance(articles)
        } else {
            mAdapter.addData(articles)
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mViewModel.onRefresh()
        mBinding.smartRefreshLayout.finishRefresh(1000)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mViewModel.onLoadMore()
        mBinding.smartRefreshLayout.finishLoadMore(1000)
    }
}