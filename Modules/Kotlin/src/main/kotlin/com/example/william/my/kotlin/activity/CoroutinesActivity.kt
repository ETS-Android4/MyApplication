package com.example.william.my.kotlin.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.kotlin.databinding.KotlinActivityKotlinBinding
import com.example.william.my.kotlin.model.ExampleViewModel
import com.example.william.my.module.router.ARouterPath

/**
 * 协程
 * https://developer.android.google.cn/kotlin/coroutines
 * 数据流
 * https://developer.android.google.cn/kotlin/flow
 *
 * launch、async：启动一个新协程
 * withContext：不启动新协程，在原来的协程中切换线程，需要传入一个CoroutineContext对象
 */
@Route(path = ARouterPath.Kotlin.Kotlin_Coroutines)
class CoroutinesActivity : AppCompatActivity() {

    private lateinit var viewModel: ExampleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = KotlinActivityKotlinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ExampleViewModel()

        viewModel.login.observe(this, Observer {
            binding.kotlinTextView.text = it
        })

        binding.kotlinTextView.setOnClickListener {
            viewModel.login("17778060027", "ww123456")
        }
    }
}