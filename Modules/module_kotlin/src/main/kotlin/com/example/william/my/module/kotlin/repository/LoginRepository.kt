package com.example.william.my.module.kotlin.repository

import android.util.Log
import com.example.william.my.module.base.Urls
import com.example.william.my.module.kotlin.data.LoginData
import com.example.william.my.module.kotlin.result.NetworkResult
import com.example.william.my.module.kotlin.utils.ThreadUtils
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class LoginRepository {

    // 将 协程 切换到 I/O 调度，确保主线程安全
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
            setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            //setRequestProperty("Content-Type", "application/json; utf-8")
            //setRequestProperty("Accept", "application/json")
            doOutput = true
            outputStream.write(jsonBody.toByteArray())
            return NetworkResult.NetworkSuccess(parse(inputStream))
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
        Log.e("LoginRepository", response)
        return Gson().fromJson(response, LoginData::class.java)
    }
}