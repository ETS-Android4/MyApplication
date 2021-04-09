package com.example.william.my.module.kotlin

import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.module.activity.BaseListActivity
import com.example.william.my.module.router.ARouterPath

/**
 * https://developer.android.google.cn/kotlin/ktx
 */
@Route(path = ARouterPath.Kotlin.Kotlin)
class KotlinActivity : BaseListActivity() {

    override fun initData() {
        super.initData()
        mMap["FLowActivity"] = ARouterPath.Kotlin.Kotlin_FLow
        mMap["PagingActivity"] = ARouterPath.Kotlin.Kotlin_Paging
        mMap["DataStoreActivity"] = ARouterPath.Kotlin.Kotlin_DataStore
        mMap["ResultActivity"] = ARouterPath.Kotlin.Kotlin_Result
    }
}

