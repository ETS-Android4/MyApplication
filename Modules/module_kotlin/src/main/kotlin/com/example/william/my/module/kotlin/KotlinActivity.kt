package com.example.william.my.module.kotlin

import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.library.base.BaseActivity
import com.example.william.my.module.kotlin.databinding.KActivityKotlinBinding
import com.example.william.my.module.kotlin.utils.Singleton
import com.example.william.my.module.router.ARouterPath

/**
 * https://developer.android.google.cn/kotlin/ktx
 */
@Route(path = ARouterPath.Kotlin.Kotlin)
class KotlinActivity : BaseActivity() {

    private lateinit var binding: KActivityKotlinBinding

    var string: String? = null
    var arrayList: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = KActivityKotlinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.k_activity_kotlin)

        string.isNullOrEmpty()
        arrayList.isNullOrEmpty()

        scope()

        binding.kotlinTextView.setOnClickListener {
            Singleton.getInstance(application).showToast("Singleton")
        }
    }

    /**
     * 作用域函数
     */
    private fun scope() {
        // it
        val e = "hello ".let {
            Log.e(TAG, it)
        }
        // this
        val a = "hello ".run {
            this + "run 函数"
        }
        Log.e(TAG, a)
        val b = with("hello ") {
            Log.e(TAG, this)
            "with 函数"
        }
        Log.e(TAG, b)

        val c = "hello ".apply {
            Log.e(TAG, this)
            "with 函数"
        }
        Log.e(TAG, c)

        val d = "hello ".also {
            Log.e(TAG, it)
        }
        Log.e(TAG, d)
    }

    private fun <T1, T2> ifNotNull(value1: T1?, value2: T2?, bothNotNull: (T1, T2) -> (Unit)) {
        if (value1 != null && value2 != null) {
            bothNotNull(value1, value2)
        }
    }

    /**
     * ?:
     */
    fun getUserName(): String {
        return string ?: "Anonymous"
    }
}

