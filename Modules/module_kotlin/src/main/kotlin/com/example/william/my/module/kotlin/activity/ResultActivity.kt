package com.example.william.my.module.kotlin.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.module.kotlin.contract.ResultContract
import com.example.william.my.module.kotlin.databinding.KLayoutResponseBinding
import com.example.william.my.module.router.ARouterPath

@Route(path = ARouterPath.Kotlin.Kotlin_Result)
class ResultActivity : ComponentActivity() {

    var binding: KLayoutResponseBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = KLayoutResponseBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding!!.contentTextView.setOnClickListener {
            myActivityLauncher.launch("input")
        }
    }

    private val myActivityLauncher = registerForActivityResult(ResultContract()) {
        binding!!.contentTextView.text = "result value is :${it}"
    }
}