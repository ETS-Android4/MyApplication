package com.example.william.my.kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.kotlin.databinding.KotlinActivityKotlinBinding
import com.example.william.my.kotlin.utils.Singleton
import com.example.william.my.module.router.ARouterPath

/**
 * https://developer.android.google.cn/kotlin/ktx
 */
@Route(path = ARouterPath.Kotlin.Kotlin)
class KotlinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = KotlinActivityKotlinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.kotlin_activity_kotlin)

        binding.kotlinTextView.setOnClickListener {
            Singleton.getInstance(this).showToast()
        }
    }
}