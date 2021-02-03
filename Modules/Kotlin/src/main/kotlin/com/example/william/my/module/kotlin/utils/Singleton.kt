package com.example.william.my.module.kotlin.utils

import android.content.Context
import android.widget.Toast

class Singleton private constructor(private var context: Context) {

    companion object {

        @Volatile
        private var instance: Singleton? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: Singleton(context).also {
                    instance = it
                }
            }
    }

    fun showToast() {
        Toast.makeText(context, "Singleton", Toast.LENGTH_SHORT).show()
    }
}
