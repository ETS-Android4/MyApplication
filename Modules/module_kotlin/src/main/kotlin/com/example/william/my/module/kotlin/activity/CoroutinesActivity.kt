package com.example.william.my.module.kotlin.activity

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.william.my.library.base.BaseActivity
import com.example.william.my.module.kotlin.databinding.KLayoutResponseBinding
import com.example.william.my.module.kotlin.utils.ThreadUtils
import kotlinx.coroutines.*

/**
 * 协程
 * https://developer.android.google.cn/kotlin/coroutines
 *
 * launch、async：启动一个新协程。async支持并发
 * withContext：不启动新协程，在原来的协程中切换线程，需要传入一个CoroutineContext对象
 */
class CoroutinesActivity : BaseActivity() {

    lateinit var binding: KLayoutResponseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = KLayoutResponseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launchWhenCreated {
            Log.e(TAG, "launchWhenCreated")
        }

        lifecycleScope.launchWhenStarted {
            Log.e(TAG, "launchWhenStarted")
        }

        lifecycleScope.launchWhenResumed {
            Log.e(TAG, "launchWhenResumed")
        }
    }

    private fun launch() {
        ThreadUtils.isMainThread("启动协程")
        val job = GlobalScope.launch {
            delay()
        }
        ThreadUtils.isMainThread("主线程执行结束")
        if (job.isCompleted) {
            ThreadUtils.isMainThread("协程执行完成")
            job.cancel()
        }
    }

    private fun blocking() {
        ThreadUtils.isMainThread("启动阻塞")
        runBlocking {
            delay()
        }
        ThreadUtils.isMainThread("主线程执行结束")
    }

    private fun await() {
        GlobalScope.launch {
            val async1 = async(3000, 1)
            val async2 = async(5000, 2)
            val result = async1.await() + async2.await()
            Log.e("TAG", "result = $result")
        }
    }

    private fun async(timeMillis: Long, result: Int): Deferred<Int> {
        ThreadUtils.isMainThread("启动协程")
        val async = GlobalScope.async {
            delayResult(timeMillis, result)
        }
        ThreadUtils.isMainThread("主线程执行结束")
        return async
    }

    private suspend fun delay() {
        delay(3000)
        ThreadUtils.isMainThread("协程执行结束")
    }

    private suspend fun delayResult(timeMillis: Long, result: Int): Int {
        delay(timeMillis)
        ThreadUtils.isMainThread("协程执行结束")
        return result
    }
}