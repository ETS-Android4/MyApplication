package com.example.william.my.module.sample.activity

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.library.base.BaseActivity
import com.example.william.my.module.router.ARouterPath
import com.example.william.my.module.sample.databinding.SampleLayoutResponseBinding
import com.example.william.my.module.sample.datastore.ExamplePreferenceDataStore
import com.example.william.my.module.sample.datastore.ExampleProtoDataStore
import kotlinx.coroutines.*

/**
 * DataStore
 * https://developer.android.google.cn/topic/libraries/architecture/datastore
 *
 * Preferences DataStore：不需要预先定义，但是不支持类型安全
 * Proto DataStore：需要预先使用protocol buffers定义数据，但是类型安全
 */
@DelicateCoroutinesApi
@Route(path = ARouterPath.Sample.Sample_DataStore)
class DataStoreActivity : BaseActivity() {

    private val preDataStore = ExamplePreferenceDataStore(this)

    private val protoDataStore = ExampleProtoDataStore(this)

    lateinit var mBinding: SampleLayoutResponseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = SampleLayoutResponseBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initCounter()

        mBinding.textView.setOnClickListener {
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
                    mBinding.textView.text = it.toString()
                }
            protoDataStore.getCounter()
                .collect {
                    mBinding.textView.text = it.toString()
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


