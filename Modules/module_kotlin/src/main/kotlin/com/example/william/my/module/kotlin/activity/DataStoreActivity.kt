package com.example.william.my.module.kotlin.activity

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.library.base.BaseActivity
import com.example.william.my.module.kotlin.databinding.KLayoutResponseBinding
import com.example.william.my.module.kotlin.datastore.ExamplePreferenceDataStore
import com.example.william.my.module.kotlin.datastore.ExampleProtoDataStore
import com.example.william.my.module.kotlin.utils.DataStoreUtils
import com.example.william.my.module.router.ARouterPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * DataStore
 * https://developer.android.google.cn/topic/libraries/architecture/datastore
 *
 * Preferences DataStore：不需要预先定义，但是不支持类型安全
 * Proto DataStore：需要预先使用protocol buffers定义数据，但是类型安全
 */
@Route(path = ARouterPath.Kotlin.Kotlin_DataStore)
class DataStoreActivity : BaseActivity() {

    private val preferenceDataStore = ExamplePreferenceDataStore(this)

    private val protoDataStore = ExampleProtoDataStore(this)

    private lateinit var binding: KLayoutResponseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = KLayoutResponseBinding.inflate(layoutInflater)
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
            DataStoreUtils.getData("String", "default")
                .collect {
                    binding.contentTextView.text = it
                }
            preferenceDataStore.getCounter()
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
            DataStoreUtils.putData("String", "String")
            preferenceDataStore.incrementCounter()
            protoDataStore.incrementCounter()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        DataStoreUtils.clearSync()

        runBlocking {
            preferenceDataStore.clear()
            protoDataStore.clear()
        }
    }
}


