package com.example.william.my.kotlin

import com.example.william.my.module.base.Urls
import com.example.william.my.module.bean.LoginBean
import java.net.HttpURLConnection
import java.net.URL

class LoginRepository(private val responseParser: LoginResponseParser) {

    // 发出网络请求，阻塞当前线程
    // Function that makes the network request, blocking the current thread
    fun makeLoginRequest(jsonBody: String): Result<LoginBean> {
        val url = URL(loginUrl)
        (url.openConnection() as? HttpURLConnection)?.run {
            requestMethod = "POST"
            setRequestProperty("Content-Type", "application/json; utf-8")
            setRequestProperty("Accept", "application/json")
            doOutput = true
            outputStream.write(jsonBody.toByteArray())
            return Result.Success(responseParser.parse(inputStream))
        }
        return Result.Error(Exception("Cannot open HttpURLConnection"))
    }

    companion object {
        private const val loginUrl = Urls.login
    }
}