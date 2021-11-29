package com.example.william.my.module.sample.repo

import com.example.william.my.bean.api.NetworkService
import com.example.william.my.bean.base.Urls
import com.example.william.my.bean.data.LoginData
import com.example.william.my.core.retrofit.utils.RetrofitUtils
import com.example.william.my.module.sample.result.NetworkResult
import com.example.william.my.module.sample.utils.ThreadUtils
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * withContext(Dispatchers.IO) {
 * } 是一个suspend 函数,
 * suspend函数需要在携程或者另一个suspend函数中调用
 */
class SuspendRepository {

    suspend fun loginByRetrofit(username: String, password: String): NetworkResult<LoginData> {

        return withContext(Dispatchers.IO) {
            //打印线程
            ThreadUtils.isMainThread("FlowRepository loginByRetrofit")

            val api = RetrofitUtils.buildApi(NetworkService::class.java)
            val loginData = api.login(username, password)
            NetworkResult.Success(loginData)
        }
    }

    // 2. 使用协程确保主线程安全
    // 将 协程 切换到 I/O 调度，确保主线程安全
    // Move the execution of the coroutine to the I/O dispatcher
    suspend fun loginByHttpURL(username: String, password: String): NetworkResult<LoginData> {

        return withContext(Dispatchers.IO) {
            //打印线程
            ThreadUtils.isMainThread("FlowRepository loginByHttpURL")

            // 阻塞网络请求
            // Blocking network request code
            makeLoginRequest(username, password)
        }
    }

    // 1. 在后台线程中执行
    // 发出网络请求，阻塞当前线程
    // Function that makes the network request, blocking the current thread
    private fun makeLoginRequest(username: String, password: String): NetworkResult<LoginData> {
        //打印线程
        ThreadUtils.isMainThread("LoginRepository makeLoginRequest")

        val url = URL(Urls.Url_Login)
        (url.openConnection() as? HttpURLConnection)?.run {
            requestMethod = "POST"
            setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            //setRequestProperty("Content-Type", "application/json; utf-8")
            //setRequestProperty("Accept", "application/json")
            doOutput = true
            val jsonBody = "username=$username&password=$password"
            outputStream.write(jsonBody.toByteArray())
            return NetworkResult.Success(parse(inputStream))
        }
        return NetworkResult.Error(Exception("Cannot open HttpURLConnection"))
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