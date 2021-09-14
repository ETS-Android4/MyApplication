package com.example.william.my.module.kotlin.activity

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.library.base.BaseActivity
import com.example.william.my.module.kotlin.databinding.KtLayoutResponseBinding
import com.example.william.my.module.kotlin.datastore.ExamplePreferenceDataStore
import com.example.william.my.module.kotlin.datastore.ExampleProtoDataStore
import com.example.william.my.module.router.ARouterPath
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

/**
 * DataStore
 * https://developer.android.google.cn/topic/libraries/architecture/datastore
 *
 * Preferences DataStore：不需要预先定义，但是不支持类型安全
 * Proto DataStore：需要预先使用protocol buffers定义数据，但是类型安全
 */
@DelicateCoroutinesApi
@Route(path = ARouterPath.Kotlin.Kotlin_DataStore)
class DataStoreActivity : BaseActivity() {

    private val preDataStore = ExamplePreferenceDataStore(this)

    private val protoDataStore = ExampleProtoDataStore(this)

    private lateinit var binding: KtLayoutResponseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = KtLayoutResponseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initCounter()

        binding.contentTextView.setOnClickListener {
            incrementCounter()
        }
    }

    /**
     * 从 DataStore 读取内容
     */
    private fun initCounter() {
        GlobalScope.launch(Dispatchers.Main) {
            preDataStore.getCounter()
                .collect {
                    binding.contentTextView.text = it.toString()
                }
            protoDataStore.getCounter()
                .collect {
                    binding.contentTextView.text = it.toString()
                }
        }
    }

    /**
     * 将内容写入 DataStore
     */
    private fun incrementCounter() {
        GlobalScope.launch(Dispatchers.Main) {
            preDataStore.incrementCounter()
            protoDataStore.incrementCounter()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        runBlocking {
            preDataStore.clear()
            protoDataStore.clear()
        }
    }
}


