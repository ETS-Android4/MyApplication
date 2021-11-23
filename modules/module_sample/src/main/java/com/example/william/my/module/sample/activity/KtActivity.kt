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
import com.example.william.my.bean.data.ArticleDetailBean
import com.example.william.my.core.retrofit.status.State

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
        mBinding.smartRefresh.setOnRefreshLoadMoreListener(this)
    }

    private fun subscribeToModel() {
        getArticleByObserver()
        //getArticleByWithLoadingTipObserver()
    }

    private fun getArticleByObserver() {
        mViewModel.articleResponse.observe(this, Observer {
            if (it.code == State.LOADING) {
                showLoading()
            } else if (it.code == State.SUCCESS) {
                if (!it.data.datas.isNullOrEmpty()) {
                    showArticles(it.data.curPage, it.data.datas)
                } else {
                    onDataNotAvailable(it.data.curPage == 0)
                }
            } else if (it.code == State.ERROR) {
                showToast(it.message)
            }
        })
    }

    private fun getArticleByWithLoadingTipObserver() {

    }

    override fun onResume() {
        super.onResume()
        mViewModel.onRefresh()
    }

    private fun showLoading() {
        ToastUtils.showShort("正在请求数据…")
    }

    private fun showToast(message: String?) {
        ToastUtils.showShort(message)
        mBinding.smartRefresh.setEnableLoadMore(false)
    }

    private fun showArticles(
        page: Int,
        articles: MutableList<ArticleDetailBean>
    ) {
        if (page == 0) {
            mAdapter.setNewInstance(articles)
        } else {
            mAdapter.addData(articles)
        }
        mBinding.smartRefresh.setEnableLoadMore(true)
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
        mBinding.smartRefresh.setEnableLoadMore(false)
    }

    private fun onDataNoMore() {
        ToastUtils.showShort("无更多数据")
        mBinding.smartRefresh.setEnableLoadMore(false)
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