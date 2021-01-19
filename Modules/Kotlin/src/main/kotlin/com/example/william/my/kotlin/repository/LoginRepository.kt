package com.example.william.my.kotlin.repository

import com.example.william.my.kotlin.bean.LoginData
import com.example.william.my.kotlin.utils.NetworkResult
import com.example.william.my.module.base.Urls
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class LoginRepository {

    // 将 协程执行到 I/O 调度
    // Move the execution of the coroutine to the I/O dispatcher
    suspend fun login(jsonBody: String): NetworkResult<LoginData> = withContext(Dispatchers.IO) {
        // 阻塞网络请求
        // Blocking network request code
        makeLoginRequest(jsonBody)
    }

    // 发出网络请求，阻塞当前线程
    // Function that makes the network request, blocking the current thread
    private fun makeLoginRequest(jsonBody: String): NetworkResult<LoginData> {
        val url = URL(loginUrl)
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

    companion object {
        private const val loginUrl = Urls.login
    }
}