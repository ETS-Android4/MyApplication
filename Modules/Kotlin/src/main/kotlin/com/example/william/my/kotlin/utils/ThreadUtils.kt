package com.example.william.my.kotlin.utils

import android.os.Looper
import android.util.Log

class ThreadUtils {

    companion object {
        fun isMainThread(name: String) {
            val isMainThread = Looper.myLooper() == Looper.getMainLooper()
            Log.e("TAG", "$name isMainThread : $isMainThread")
        }
    }
}