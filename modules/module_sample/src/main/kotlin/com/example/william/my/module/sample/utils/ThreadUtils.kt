package com.example.william.my.module.sample.utils

import android.os.Looper
import com.example.william.my.module.utils.L

class ThreadUtils {

    companion object {
        fun isMainThread(name: String) {
            val isMainThread = Looper.myLooper() == Looper.getMainLooper()
            L.e("TAG", "$name isMainThread : $isMainThread")
        }
    }
}