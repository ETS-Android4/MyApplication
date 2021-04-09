package com.example.william.my.module.kotlin.contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.activity.result.contract.ActivityResultContract
import com.example.william.my.module.kotlin.activity.ResultSecondActivity

class ResultContract : ActivityResultContract<String, String?>() {

    override fun createIntent(context: Context, input: String?): Intent {
        return Intent(context, ResultSecondActivity::class.java).apply {
            putExtra("input", input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        val data = intent?.getStringExtra("result")
        return if (resultCode == Activity.RESULT_OK && !TextUtils.isEmpty(data)) data else "null"
    }
}