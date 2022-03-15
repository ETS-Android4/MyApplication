package com.example.william.my.module.sample.repo

import com.example.william.my.bean.api.NetworkService
import com.example.william.my.bean.data.LoginBean
import com.example.william.my.core.retrofit.utils.RetrofitUtils
import com.example.william.my.module.sample.utils.ThreadUtils
import com.example.william.my.module.utils.L
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*

/**
 * Android 上的 Kotlin 数据流
 * https://developer.android.google.cn/kotlin/flow
 */
class FlowRepository(private val defaultDispatcher: CoroutineDispatcher) {

    /**
     * 创建数据流
     */
    private fun createFlow(username: String, password: String): Flow<LoginBean> {
        return flow {
            //打印线程
            ThreadUtils.isMainThread("FlowRepository login")

            val api = RetrofitUtils.buildApi(NetworkService::class.java)
            val loginBean = api.login(username, password)
            emit(loginBean)// Emits the result of the request to the flow
        }
            // Executes on the IO dispatcher
            .flowOn(defaultDispatcher)
    }

    /**
     * 修改数据流
     */
    private fun transformFlow(flow: Flow<LoginBean>): Flow<LoginBean> {
        return flow
            // 在默认调度程序上执行
            // Executes on the default dispatcher
            .map { news ->
                news
            }
            // 在默认调度程序上执行
            // Executes on the default dispatcher
            .onEach {

            }
            // flowOn 影响上游的 flow
            // flowOn affects the upstream flow ↑
            .flowOn(defaultDispatcher)
            // 下游的 flow 不受影响
            // the downstream flow ↓ is not affected
            .catch { exception -> // Executes in the consumer's context
                L.e("TAG", "exception : " + exception.message.toString())
            }
    }

    /**
     * 这些操作是惰性的，不会触发流。它们只是转换流在该时间点发出的当前值。
     * These operations are lazy and don't trigger the flow.
     * They just transform the current value emitted by the flow at that point in time.
     */
    fun login(username: String, password: String): Flow<LoginBean> {
        val loginBean = createFlow(username, password)
        return transformFlow(loginBean)
    }
}