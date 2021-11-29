package com.example.william.my.module.sample.activity

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.bean.data.ArticleDetailBean
import com.example.william.my.library.base.BaseActivity
import com.example.william.my.module.router.ARouterPath
import com.example.william.my.module.sample.adapter.PagingAdapter
import com.example.william.my.module.sample.adapter.PagingLoadStateAdapter
import com.example.william.my.module.sample.comparator.PagingComparator
import com.example.william.my.module.sample.databinding.SampleActivityPagingBinding
import com.example.william.my.module.sample.model.PagingViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Paging
 * https://developer.android.google.cn/topic/libraries/architecture/paging/v3-overview
 */
@Route(path = ARouterPath.Sample.Sample_Paging)
class PagingActivity : BaseActivity() {

    private val mDisposable = CompositeDisposable()

    lateinit var mViewModel: PagingViewModel

    lateinit var mBinding: SampleActivityPagingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = SampleActivityPagingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val pagingAdapter = PagingAdapter(PagingComparator())
        val recycleView = mBinding.pagingRecycleView
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.adapter = pagingAdapter

        mViewModel = ViewModelProvider(this)[PagingViewModel::class.java]
        initArticleLiveData(mViewModel, pagingAdapter)

        //获取加载状态
        pagingAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.NotLoading -> Log.d("TAG", "is NotLoading")
                is LoadState.Loading -> Log.d("TAG", "is Loading")
                is LoadState.Error -> Log.d("TAG", "is Error")
            }
        }

        //呈现加载状态
        //需要把withLoadStateFooter返回的adapter设置给recyclerview
        recycleView.adapter = pagingAdapter.withLoadStateHeaderAndFooter(
            header = PagingLoadStateAdapter(pagingAdapter::retry),
            footer = PagingLoadStateAdapter(pagingAdapter::retry)
        )
    }

    /**
     * Paging Coroutines -> LiveData
     */
    private fun initArticleLiveData(
        viewModel: PagingViewModel,
        pagingPagingAdapter: PagingAdapter
    ) {
        // activity 可以使用 lifecycleScope。fragment 需要使用 viewLifecycleOwner.lifecycleScope
        // Activities can use lifecycleScope directly, but Fragments should instead use viewLifecycleOwner.lifecycleScope.
        lifecycleScope.launch {
            viewModel.articleFlow.collectLatest { pagingData ->
                pagingPagingAdapter.submitData(pagingData)
            }
        }
    }

    /**
     * Paging RxJava -> Flowable
     */
    @ExperimentalCoroutinesApi
    private fun initArticleFlowable(
        viewModel: PagingViewModel,
        pagingPagingAdapter: PagingAdapter
    ) {
        mDisposable.add(
            viewModel.articleFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer<PagingData<ArticleDetailBean>> {
                    pagingPagingAdapter.submitData(lifecycle, it)
                })
        )
    }

    override fun onStop() {
        super.onStop()
        // clear all the subscriptions
        mDisposable.clear()
    }
}