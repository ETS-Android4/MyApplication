package com.example.william.my.module.sample.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.module.router.ARouterPath
import com.example.william.my.module.sample.databinding.SampleLayoutResponseBinding

/**
 * ActivityResultContracts
 */
@Route(path = ARouterPath.Sample.Sample_Result)
class ResultActivity : ComponentActivity() {

    lateinit var binding: SampleLayoutResponseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SampleLayoutResponseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textView.setOnClickListener {
            //launcher.launch("input")
            val intent = Intent(this, ResultSecondActivity::class.java).apply {
                putExtra("input", "input")
            }
            startActivityForResult.launch(intent)
        }
    }

    //自定义ActivityResultContract
    private val launcher =
        registerForActivityResult(CustomResultContract()) {
            binding.textView.text = "result value is :${it}"
        }

    // Create ActivityResultLauncher
    private val startActivityForResult =
        registerForActivityResult(StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val result = it.data?.getStringExtra("result")
                binding.textView.text = "result value is :${result}"
            }
        }

    //打开相机拍照
    private val takePicturePreview =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            binding.textView.background = BitmapDrawable(resources, it)
        }

    //获取单个权限请求
    private var requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {// 同意
                Toast.makeText(this, "您同意了权限申请", Toast.LENGTH_SHORT).show()
            } else {// 拒绝
                Toast.makeText(this, "您拒绝了权限申请", Toast.LENGTH_SHORT).show()
            }
        }

    //获取多个权限请求
    private var requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it[Manifest.permission.CAMERA]!!) {// 同意
                Toast.makeText(this, "您同意了权限申请", Toast.LENGTH_SHORT).show()
            } else {// 拒绝
                Toast.makeText(this, "您拒绝了权限申请", Toast.LENGTH_SHORT).show()
            }
        }

    /**
     * 自定义ActivityResultContract
     */
    class CustomResultContract : ActivityResultContract<String, String?>() {

        override fun createIntent(context: Context, input: String): Intent {
            return Intent(context, ResultSecondActivity::class.java).apply {
                putExtra("input", input)
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): String? {
            val data = intent?.getStringExtra("result")
            return if (resultCode == Activity.RESULT_OK && !TextUtils.isEmpty(data)) data else "null"
        }
    }
}