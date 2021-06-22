package com.example.william.my.module.kotlin.activity

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.william.my.library.base.BaseActivity
import com.example.william.my.module.kotlin.databinding.KtLayoutResponseBinding
import com.example.william.my.module.kotlin.utils.ThreadUtils
import kotlinx.coroutines.*

/**
 * 协程
 * https://developer.android.google.cn/kotlin/coroutines
 *
 * launch、async：启动一个新协程。async支持并发，可以通过await获取返回值
 * withContext：不启动新协程，在原来的协程中切换线程，需要传入一个CoroutineContext对象
 */
class CoroutinesActivity : BaseActivity() {

    lateinit var binding: KtLayoutResponseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = KtLayoutResponseBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun lifecycleScope() {
        //当 activity 处于create的时候执行协程体
        lifecycleScope.launchWhenCreated {
            Log.e(TAG, "launchWhenCreated")
        }

        //当 activity 处于start的时候执行协程体
        lifecycleScope.launchWhenStarted {
            Log.e(TAG, "launchWhenStarted")
        }

        //当 activity 处于resume的时候执行协程体
        lifecycleScope.launchWhenResumed {
            Log.e(TAG, "launchWhenResumed")
        }
    }

    /**
     * 1.启动一个新的线程，在新线程上创建运行协程，不堵塞当前线程
     */
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

    /**
     * 2.启动一个新的线程，在新线程上创建运行协程，不堵塞当前线程。支持通过await获取返回值
     */
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

    /**
     * 3.创建新的协程，运行在当前线程上，所以会堵塞当前线程，直到协程体结束
     */
    private fun blocking() {
        ThreadUtils.isMainThread("启动阻塞")
        runBlocking {
            delay()
        }
        ThreadUtils.isMainThread("主线程执行结束")
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