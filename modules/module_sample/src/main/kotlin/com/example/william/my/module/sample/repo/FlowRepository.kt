package com.example.william.my.module.sample.repo

import com.example.william.my.bean.api.NetworkService
import com.example.william.my.bean.data.ArticleDataBean
import com.example.william.my.bean.data.LoginData
import com.example.william.my.core.retrofit.response.RetrofitResponse
import com.example.william.my.core.retrofit.utils.RetrofitUtils
import com.example.william.my.module.sample.utils.ThreadUtils
import com.example.william.my.module.utils.L
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class FlowRepository {

    private fun loginFlow(username: String, password: String): Flow<LoginData> {
        return flow {
            //打印线程
            ThreadUtils.isMainThread("ArticleRepository getArticle")

            val api = RetrofitUtils.buildApi(NetworkService::class.java)
            val loginData = api.login(username, password)
            emit(loginData) // Emits the result of the request to the flow 向数据流发送请求结果
        }
    }

    /**
     * 返回流上的数据转换。
     * Returns the data transformations on the flow.
     * 这些操作为懒汉式，不会触发流，只在发送数据时转换
     * These operations are lazy and don't trigger the flow. They just transform
     * the current value emitted by the flow at that point in time.
     */
    fun login(username: String, password: String): Flow<LoginData> {
        return loginFlow(username, password)
            // 中间运算符 map 转换数据
            .map { loginData ->
                loginData
            }
            // flowOn 只影响上游
            // flowOn affects the upstream flow ↑
            .flowOn(Dispatchers.IO)
            //下游不受影响
            //the downstream flow ↓ is not affected
            .catch { exception ->
                L.e("TAG", "exception : " + exception.message.toString())
            }
    }
}