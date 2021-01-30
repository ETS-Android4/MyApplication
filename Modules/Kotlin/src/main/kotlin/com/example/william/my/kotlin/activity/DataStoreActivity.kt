package com.example.william.my.kotlin.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.kotlin.databinding.KotlinActivityKotlinBinding
import com.example.william.my.kotlin.datastore.ExamplePreferenceDataStore
import com.example.william.my.kotlin.datastore.ExampleProtoDataStore
import com.example.william.my.kotlin.utils.DataStoreUtils
import com.example.william.my.module.router.ARouterPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Preferences DataStore：不需要预先定义，但是不支持类型安全
 * Proto DataStore：需要预先使用protocol buffers定义数据，但是类型安全
 * https://developer.android.google.cn/topic/libraries/architecture/datastore
 */
@Route(path = ARouterPath.Kotlin.Kotlin_DataStore)
class DataStoreActivity : AppCompatActivity() {

    private val preferenceDataStore = ExamplePreferenceDataStore(this)

    private val protoDataStore = ExampleProtoDataStore(this)

    private lateinit var binding: KotlinActivityKotlinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = KotlinActivityKotlinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initCounter()

        binding.kotlinTextView.setOnClickListener {
            incrementCounter()
        }
    }

    /**
     * 从 DataStore 读取内容
     */
    private fun initCounter() {
        GlobalScope.launch(Dispatchers.Main) {
            preferenceDataStore.getCounter()
                .collect {
                    binding.kotlinTextView.text = it.toString()
                }
            protoDataStore.getCounter()
                .collect {
                    binding.kotlinTextView.text = it.toString()
                }
        }
    }

    /**
     * 将内容写入 DataStore
     */
    private fun incrementCounter() {
        GlobalScope.launch(Dispatchers.Main) {
            preferenceDataStore.incrementCounter()
            protoDataStore.incrementCounter()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        DataStoreUtils.clearSync()
        
        runBlocking {
            protoDataStore.clear()
        }
    }
}


