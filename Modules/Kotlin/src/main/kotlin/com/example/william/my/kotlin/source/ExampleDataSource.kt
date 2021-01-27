package com.example.william.my.kotlin.source

import com.example.william.my.kotlin.bean.LoginData
import com.example.william.my.kotlin.result.NetworkResult
import com.example.william.my.kotlin.service.KotlinApi
import com.example.william.my.kotlin.utils.ThreadUtils
import com.example.william.my.module.base.Urls
import com.example.william.my.module.bean.ArticlesBean
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ExampleDataSource {

    val getArticles: Flow<ArticlesBean> = flow {
        //打印线程
        ThreadUtils.isMainThread("flow")

        val articles = buildApi().getArticles(0)
        emit(articles) // Emits the result of the request to the flow 向数据流发送请求结果
        delay(3000) // Suspends the coroutine for some time 挂起一段时间
    }

    private fun buildApi(): KotlinApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(Urls.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(KotlinApi::class.java)
    }

    // 将 协程 切换到 I/O 调度
    // Move the execution of the coroutine to the I/O dispatcher
    suspend fun login(jsonBody: String): NetworkResult<LoginData> = withContext(Dispatchers.IO) {
        //打印线程
        ThreadUtils.isMainThread("login")
        // 阻塞网络请求
        // Blocking network request code
        makeLoginRequest(jsonBody)
    }

    // 发出网络请求，阻塞当前线程
    // Function that makes the network request, blocking the current thread
    private fun makeLoginRequest(jsonBody: String): NetworkResult<LoginData> {
        //打印线程
        ThreadUtils.isMainThread("makeLoginRequest")

        val url = URL(Urls.login)
        (url.openConnection() as? HttpURLConnection)?.run {
            requestMethod = "POST"
            setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //setRequestProperty("Content-Type", "application/json; utf-8")
            //setRequestProperty("Accept", "application/json")
            doOutput = true
            outputStream.write(jsonBody.toByteArray())
            return NetworkResult.OK(parse(inputStream))
        }
        return NetworkResult.NetworkError(Exception("Cannot open HttpURLConnection"))
    }

    private fun parse(input: InputStream): LoginData {
        val msg = StringBuilder()
        val reader = BufferedReader(InputStreamReader(input))
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            msg.append(line)
        }
        reader.close()
        val response = msg.toString()
        return Gson().fromJson(response, LoginData::class.java)
    }
}


