package com.example.william.my.module.kotlin

import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.core.keyvalue.KeyValue
import com.example.william.my.module.activity.BaseListActivity
import com.example.william.my.module.kotlin.utils.Singleton
import com.example.william.my.module.router.ARouterPath

/**
 * https://developer.android.google.cn/kotlin/ktx
 */
@Route(path = ARouterPath.Kotlin.Kotlin)
class KotlinActivity : BaseListActivity() {

    override fun initData() {
        super.initData()
        mMap["CoilActivity"] = ARouterPath.Kotlin.Kotlin_Coil
        mMap["FLowActivity"] = ARouterPath.Kotlin.Kotlin_FLow
        mMap["PagingActivity"] = ARouterPath.Kotlin.Kotlin_Paging
        mMap["DataStoreActivity"] = ARouterPath.Kotlin.Kotlin_DataStore
        mMap["ResultActivity"] = ARouterPath.Kotlin.Kotlin_Result

        setFuncTest()

        KeyValue.INSTANCE.init(this)
        KeyValue.INSTANCE.putString("key", "value")
        Log.e("TAG", KeyValue.INSTANCE.getString("key", "???") + "")

        Singleton.getInstance(application).showToast("showToast")
    }

    /**
     * 高阶函数
     * 如果一个函数接收另一个函数作为参数，或者返回值的类型是另一个函数，那么该函数称为高阶函数。
     */
    private fun setFuncTest() {
        setListener1(javaClass.simpleName) { str ->
            Log.e("TAG", str)
        }

        setListener2(javaClass.simpleName) { str ->
            Log.e("TAG", str)
        }

        setListener3(javaClass.simpleName) { str, callback ->
            Log.e("TAG", str)
            callback.invoke()
        }
    }

    private fun setListener1(str: String, callback: (str: String) -> Unit) {
        Log.e("TAG", str)
        callback.invoke("setListener")
    }

    private fun setListener2(str: String, callback: ((toast: String) -> Unit) = {}) {
        Log.e("TAG", str)
        callback.invoke("toast")
    }

    private fun setListener3(str: String, callback: (str: String, callback: () -> Unit) -> Unit) {
        Log.e("TAG", str)
        callback.invoke("setListener") {
            Log.e("TAG", "Callback")
        }
    }
}

