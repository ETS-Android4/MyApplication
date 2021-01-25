package com.example.william.my.kotlin.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.kotlin.adapter.ArticlesAdapter
import com.example.william.my.kotlin.comparator.ArticleComparator
import com.example.william.my.kotlin.databinding.KotlinActivityPagingBinding
import com.example.william.my.kotlin.hodder.ExampleLoadStateAdapter
import com.example.william.my.kotlin.model.ExampleViewModel
import com.example.william.my.module.router.ARouterPath
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


/**
 * https://developer.android.google.cn/topic/libraries/architecture/paging/v3-overview
 */
@Route(path = ARouterPath.Kotlin.Kotlin_Paging)
class PagingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = KotlinActivityPagingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val viewModel by viewModels<ExampleViewModel>()
        val viewModel = ViewModelProvider(this).get(ExampleViewModel::class.java)

        val pagingAdapter = ArticlesAdapter(ArticleComparator())
        val recycleView = binding.pagingRecycleView
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.adapter = pagingAdapter

        // Activity 可以直接使用 lifecycleScope
        // Fragment 需要使用 viewLifecycleOwner.lifecycleScope
        // Activities can use lifecycleScope directly, but Fragments should instead use
        // viewLifecycleOwner.lifecycleScope.
        lifecycleScope.launch {
            viewModel.articlesFlow.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }

        //获取加载状态
        pagingAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.NotLoading -> Log.e("TAG", "is NotLoading")
                is LoadState.Loading -> Log.e("TAG", "is Loading")
                is LoadState.Error -> Log.e("TAG", "is Error")
            }
        }

        //呈现加载状态
        pagingAdapter.withLoadStateHeaderAndFooter(
            header = ExampleLoadStateAdapter(pagingAdapter::retry),
            footer = ExampleLoadStateAdapter(pagingAdapter::retry)
        )
    }
}