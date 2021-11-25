package com.example.william.my.module.sample.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.william.my.library.base.BaseActivity
import com.example.william.my.module.sample.databinding.KtLayoutResponseBinding

/**
 * ActivityResultContracts
 */
class ResultSecondActivity : BaseActivity() {

    lateinit var binding: KtLayoutResponseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = KtLayoutResponseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent?.getStringExtra("input")
        binding.contentTextView.text = data

        binding.contentTextView.setOnClickListener {
            val intent = Intent().apply {
                putExtra("result", "Hello，我是回传的数据！")
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

}