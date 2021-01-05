package com.example.william.my.kotlin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.william.my.kotlin.R
import com.example.william.my.kotlin.Singleton

class KotlinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)

        Singleton.getInstance(this).showToast()
    }

}