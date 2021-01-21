package com.example.william.my.kotlin.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.kotlin.databinding.KotlinActivityKotlinBinding
import com.example.william.my.kotlin.model.ArticlesViewModel
import com.example.william.my.kotlin.repository.ArticlesRepository
import com.example.william.my.module.router.ARouterPath

/**
 * 数据流
 * https://developer.android.google.cn/kotlin/flow
 */
@Route(path = ARouterPath.Kotlin.Kotlin_Flow)
class FlowActivity : AppCompatActivity() {

    private lateinit var articlesViewModel: ArticlesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = KotlinActivityKotlinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        articlesViewModel = ArticlesViewModel(ArticlesRepository())

        articlesViewModel.articles.observe(this, Observer { articles ->
            binding.kotlinTextView.text = articles
        })

        binding.kotlinTextView.setOnClickListener {
            articlesViewModel.getArticles()
        }

        //articlesViewModel.getArticles2().observe(this, Observer { articles ->
        //    binding.kotlinTextView.text = articles
        //})
    }
}