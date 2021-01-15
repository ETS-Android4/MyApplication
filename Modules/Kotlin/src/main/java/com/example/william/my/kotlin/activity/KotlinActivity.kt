package com.example.william.my.kotlin.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.kotlin.R
import com.example.william.my.kotlin.Singleton
import com.example.william.my.module.router.ARouterPath

@Route(path = ARouterPath.Kotlin.Kotlin)
class KotlinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kotlin_activity_kotlin)

        Singleton.getInstance(this).showToast()
    }

}