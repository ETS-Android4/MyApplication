package com.example.william.my.kotlin.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.kotlin.databinding.KotlinActivityKotlinBinding
import com.example.william.my.kotlin.datastore.ExamplePreferenceDataStore
import com.example.william.my.module.router.ARouterPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Preferences DataStore：不需要预先定义，但是不支持类型安全
 * Proto DataStore：需要预先使用protocol buffers定义数据，但是类型安全
 * https://developer.android.google.cn/topic/libraries/architecture/datastore
 */
@Route(path = ARouterPath.Kotlin.Kotlin_DataStore)
class DataStoreActivity : AppCompatActivity() {

    private val dataStore = ExamplePreferenceDataStore(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = KotlinActivityKotlinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GlobalScope.launch(Dispatchers.Main) {
            dataStore.getUsername()
                .collect {
                    binding.kotlinTextView.text = it.toString()
                }
        }

        binding.kotlinTextView.setOnClickListener {
            incrementCounter()
        }
    }

    private fun incrementCounter() {
        GlobalScope.launch(Dispatchers.Main) {
            dataStore.incrementCounter()
        }
    }
}


