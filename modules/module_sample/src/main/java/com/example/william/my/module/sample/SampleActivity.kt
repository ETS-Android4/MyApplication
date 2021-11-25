package com.example.william.my.module.sample

import com.example.william.my.module.router.ARouterPath
import com.example.william.my.library.base.BaseFragmentActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.module.sample.utils.Singleton
import com.example.william.my.module.router.fragment.RouterRecyclerFragment
import com.example.william.my.module.router.item.RouterItem
import java.util.ArrayList

/**
 * https://developer.android.google.cn/sample/guide
 * https://developer.android.google.cn/topic/libraries/architecture/index.html
 */
@Route(path = ARouterPath.Sample.Sample)
class SampleActivity : BaseFragmentActivity() {

    override fun setFragment(): Fragment {
        val bundle = Bundle()
        bundle.putParcelableArrayList("router", buildRouter())
        val fragment = RouterRecyclerFragment()
        fragment.arguments = bundle
        return fragment
    }

    private fun buildRouter(): ArrayList<RouterItem> {
        val routerItems = ArrayList<RouterItem>()
        routerItems.add(RouterItem("BindActivity", ARouterPath.Sample.Sample_Bind))
        routerItems.add(RouterItem("LiveDataActivity", ARouterPath.Sample.Sample_LiveData))
        routerItems.add(RouterItem("PagingActivity", ARouterPath.Sample.Sample_Paging))
        routerItems.add(RouterItem("WorkManagerActivity", ARouterPath.Sample.Sample_WorkManager))
        routerItems.add(RouterItem("NavigationActivity", ARouterPath.Sample.Sample_Navigation))
        routerItems.add(RouterItem("RoomActivity", ARouterPath.Sample.Sample_Room))
        return routerItems
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
        Singleton.getInstance(application).showToast("showToast")
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