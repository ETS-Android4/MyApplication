package com.example.william.my.kotlin

import android.util.Log
import com.example.william.my.module.bean.LoginBean
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class LoginResponseParser {

    fun parse(input: InputStream): LoginBean {
        val msg = StringBuilder()
        val reader = BufferedReader(InputStreamReader(input))
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            msg.append(line)
        }
        reader.close()
        val response = msg.toString()
        Log.e("LoginResponseParser", response)
        return Gson().fromJson(response, LoginBean::class.java)
    }
}