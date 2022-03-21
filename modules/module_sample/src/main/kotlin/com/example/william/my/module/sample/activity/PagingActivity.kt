package com.example.william.my.module.sample.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import autodispose2.AutoDispose
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.bean.data.ArticleDetailBean
import com.example.william.my.library.base.BaseActivity
import com.example.william.my.module.router.ARouterPath
import com.example.william.my.module.sample.databinding.SampleActivityPagingBinding
import com.example.william.my.module.sample.paging.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Paging
 * https://developer.android.google.cn/topic/libraries/architecture/paging/v3-overview
 */
@Route(path = ARouterPath.Sample.Sample_Paging)
class PagingActivity : BaseActivity() {

    private val mViewModel by viewModels<ArticlePagingViewModel>()

    private val mRxViewModel by viewModels<ArticleRxPagingViewModel>()

    lateinit var mBinding: SampleActivityPagingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = SampleActivityPagingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val pagingAdapter = PagingAdapter(PagingComparator())
        mBinding.pagingRecycleView.adapter = pagingAdapter

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
        mBinding.pagingRecycleView.adapter = pagingAdapter.withLoadStateHeaderAndFooter(
            header = PagingStateAdapter(pagingAdapter::retry),
            footer = PagingStateAdapter(pagingAdapter::retry)
        )
    }

    /**
     * Paging Coroutines -> LiveData
     */
    private fun initArticleLiveData(viewModel: ArticlePagingViewModel, adapter: PagingAdapter) {
        // activity 可以使用 lifecycleScope。fragment 需要使用 viewLifecycleOwner.lifecycleScope
        // Activities can use lifecycleScope directly, but Fragments should instead use viewLifecycleOwner.lifecycleScope.
        lifecycleScope.launch {
            viewModel.articleFlow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }

    /**
     * Paging RxJava -> Flowable
     */
    @ExperimentalCoroutinesApi
    private fun initArticleFlowable(viewModel: ArticlePagingViewModel, adapter: PagingAdapter) {
        viewModel.articleFlowable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
            .subscribe(Consumer<PagingData<ArticleDetailBean>> {
                adapter.submitData(lifecycle, it)
            })
    }

    /**
     * Paging RxJava -> LiveData
     */
    private fun initArticleLiveData(viewModel: ArticleRxPagingViewModel, adapter: PagingAdapter) {
        viewModel.articleLiveData.observe(this@PagingActivity, Observer { pagingData ->
            lifecycleScope.launch {
                withContext(Dispatchers.Main) {
                    adapter.submitData(pagingData)
                }
            }
        })
    }

    /**
     * Paging RxJava -> Flowable
     */
    private fun initArticleFlowable(viewModel: ArticleRxPagingViewModel, adapter: PagingAdapter) {
        viewModel.articleFlowable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
            .subscribe(Consumer<PagingData<ArticleDetailBean>> {
                adapter.submitData(lifecycle, it)
            })
    }
}