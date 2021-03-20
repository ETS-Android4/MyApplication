package com.example.william.my.module.kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.module.kotlin.databinding.KActivityKotlinBinding
import com.example.william.my.module.kotlin.utils.Singleton
import com.example.william.my.module.router.ARouterPath


/**
 * https://developer.android.google.cn/kotlin/ktx
 */
@Route(path = ARouterPath.Kotlin.Kotlin)
class KotlinActivity : AppCompatActivity() {

    private var binding: KActivityKotlinBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = KActivityKotlinBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        //setContentView(R.layout.k_activity_kotlin)

        binding!!.kotlinTextView.setOnClickListener {
            Singleton.getInstance(application).showToast("Singleton")
        }
    }
}

