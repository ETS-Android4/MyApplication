package com.example.william.my.module.kotlin.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.module.kotlin.contract.ResultContract
import com.example.william.my.module.kotlin.databinding.KLayoutResponseBinding
import com.example.william.my.module.router.ARouterPath

/**
 * ActivityResultContracts
 */
@Route(path = ARouterPath.Kotlin.Kotlin_Result)
class ResultActivity : ComponentActivity() {

    lateinit var binding: KLayoutResponseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = KLayoutResponseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.contentTextView.setOnClickListener {
            //myActivityLauncher.launch("input")
            val intent = Intent(this, SecondActivity::class.java).apply {
                putExtra("input", "input")
            }
            startActivityForResult.launch(intent)
        }
    }

    private val myActivityLauncher =
        registerForActivityResult(ResultContract()) {
            binding.contentTextView.text = "result value is :${it}"
        }

    private val startActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val result = it.data?.getStringExtra("result")
                binding.contentTextView.text = "result value is :${result}"
            }
        }

    private val takePicturePreview =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            binding.contentTextView.background = BitmapDrawable(resources, it)
        }

    private var requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {// 同意
            } else {// 拒绝
            }
        }

    private var requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it[Manifest.permission.CAMERA]!!) {// 同意
            } else {// 拒绝
            }
        }
}