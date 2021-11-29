package com.example.william.my.module.sample.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.william.my.library.base.BaseActivity
import com.example.william.my.module.sample.databinding.SampleLayoutResponseBinding

/**
 * ActivityResultContracts
 */
class ResultSecondActivity : BaseActivity() {

    lateinit var mBinding: SampleLayoutResponseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = SampleLayoutResponseBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val data = intent?.getStringExtra("input")
        mBinding.textView.text = data

        mBinding.textView.setOnClickListener {
            val intent = Intent().apply {
                putExtra("result", "Hello，我是回传的数据！")
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

}