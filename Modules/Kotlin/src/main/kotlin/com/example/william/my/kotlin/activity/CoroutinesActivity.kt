package com.example.william.my.kotlin.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.kotlin.LoginRepository
import com.example.william.my.kotlin.LoginResponseParser
import com.example.william.my.kotlin.LoginViewModel
import com.example.william.my.kotlin.databinding.KotlinActivityKotlinBinding
import com.example.william.my.module.router.ARouterPath

@Route(path = ARouterPath.Kotlin.Kotlin_Coroutines)
class CoroutinesActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = KotlinActivityKotlinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModel = LoginViewModel(LoginRepository(LoginResponseParser()));

        binding.kotlinTextView.setOnClickListener {
            viewModel.login("17778060027", "ww123456")
        }
    }
}