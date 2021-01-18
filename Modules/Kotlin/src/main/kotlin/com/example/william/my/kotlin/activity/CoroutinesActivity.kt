package com.example.william.my.kotlin.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.kotlin.LoginRepository
import com.example.william.my.kotlin.LoginResponseParser
import com.example.william.my.kotlin.LoginViewModel
import com.example.william.my.kotlin.databinding.KotlinActivityKotlinBinding
import com.example.william.my.module.bean.LoginBean
import com.example.william.my.module.router.ARouterPath
import com.google.gson.Gson

/**
 * https://codelabs.developers.google.com/codelabs/kotlin-coroutines#0
 * launch、async：启动一个新协程
 * withContext：不启动新协程，在原来的协程中切换线程，需要传入一个CoroutineContext对象
 */
@Route(path = ARouterPath.Kotlin.Kotlin_Coroutines)
class CoroutinesActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = KotlinActivityKotlinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = LoginViewModel(LoginRepository(LoginResponseParser()))

        viewModel.loginBean.observe(this, Observer {
            Log.e("TAG", Gson().toJson(it))
        })

        binding.kotlinTextView.setOnClickListener {
            viewModel.login("17778060027", "ww123456")
        }
    }
}