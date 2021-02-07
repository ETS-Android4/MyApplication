package com.example.william.my.module.kotlin.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.library.base.BaseActivity
import com.example.william.my.module.bean.ArticlesBean
import com.example.william.my.module.kotlin.adapter.ArticlesAdapter
import com.example.william.my.module.kotlin.comparator.ArticleComparator
import com.example.william.my.module.kotlin.databinding.KotlinActivityPagingBinding
import com.example.william.my.module.kotlin.holder.ExampleLoadStateAdapter
import com.example.william.my.module.kotlin.model.PagingViewModel
import com.example.william.my.module.router.ARouterPath
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * https://developer.android.google.cn/topic/libraries/architecture/paging/v3-overview
 */
@Route(path = ARouterPath.Kotlin.Kotlin_Paging)
class PagingActivity : BaseActivity() {

    private val mDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = KotlinActivityPagingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val viewModel by viewModels<ExampleViewModel>()
        val viewModel = ViewModelProvider(this).get(PagingViewModel::class.java)

        val pagingAdapter = ArticlesAdapter(ArticleComparator())
        val recycleView = binding.pagingRecycleView
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.adapter = pagingAdapter

        // activity_ktx 可以使用 lifecycleScope
        // fragment_ktx 需要使用 viewLifecycleOwner.lifecycleScope
        // Activities can use lifecycleScope directly, but Fragments should instead use
        // viewLifecycleOwner.lifecycleScope.
        //lifecycleScope.launch {
        //    viewModel.articlesFlow.collectLatest { pagingData ->
        //        pagingAdapter.submitData(pagingData)
        //    }
        //}

        mDisposable.add(
            viewModel.articlesFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer<PagingData<ArticlesBean.DataBean.ArticleBean>> {
                    pagingAdapter.submitData(lifecycle, it)
                })
        )

        //获取加载状态
        pagingAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.NotLoading -> Log.e("TAG", "is NotLoading")
                is LoadState.Loading -> Log.e("TAG", "is Loading")
                is LoadState.Error -> Log.e("TAG", "is Error")
            }
        }

        //呈现加载状态
        //需要把withLoadStateFooter返回的adapter设置给recyclerview
        recycleView.adapter = pagingAdapter.withLoadStateHeaderAndFooter(
            header = ExampleLoadStateAdapter(pagingAdapter::retry),
            footer = ExampleLoadStateAdapter(pagingAdapter::retry)
        )
    }

    override fun onStop() {
        super.onStop()
        // clear all the subscriptions
        mDisposable.clear()
    }
}