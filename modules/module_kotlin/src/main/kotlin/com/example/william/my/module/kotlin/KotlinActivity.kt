package com.example.william.my.module.kotlin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.library.base.BaseFragmentActivity
import com.example.william.my.module.router.fragment.RouterRecyclerFragment
import com.example.william.my.module.router.item.RouterItem
import com.example.william.my.module.kotlin.utils.Singleton
import com.example.william.my.module.router.ARouterPath
import java.util.*

/**
 * https://developer.android.google.cn/kotlin/ktx
 */
@Route(path = ARouterPath.Kotlin.Kotlin)
class KotlinActivity : BaseFragmentActivity() {

    override fun setFragment(): Fragment {
        val bundle = Bundle()
        bundle.putParcelableArrayList("router", buildRouter())
        val fragment =
            RouterRecyclerFragment()
        fragment.arguments = bundle
        return fragment
    }

    private fun buildRouter(): ArrayList<RouterItem> {
        val routerItems = ArrayList<RouterItem>()
        routerItems.add(RouterItem("CoilActivity", ARouterPath.Kotlin.Kotlin_Coil))
        routerItems.add(RouterItem("FLowActivity", ARouterPath.Kotlin.Kotlin_FLow))
        routerItems.add(RouterItem("PagingActivity", ARouterPath.Kotlin.Kotlin_Paging))
        routerItems.add(RouterItem("DataStoreActivity", ARouterPath.Kotlin.Kotlin_DataStore))
        routerItems.add(RouterItem("ResultActivity", ARouterPath.Kotlin.Kotlin_Result))
        return routerItems
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFuncTest()

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

