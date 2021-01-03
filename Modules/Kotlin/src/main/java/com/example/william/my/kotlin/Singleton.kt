package com.example.william.my.kotlin

import android.content.Context

class Singleton private constructor(context: Context) {

    companion object {
        @Volatile
        private var instance: Singleton? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: Singleton(context).also { instance = it }
            }
    }

}
