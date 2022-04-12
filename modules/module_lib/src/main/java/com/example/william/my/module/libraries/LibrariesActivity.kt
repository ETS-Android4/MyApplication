package com.example.william.my.module.libraries

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.core.eventbus.flow.FlowEventBus
import com.example.william.my.core.eventbus.rxjava.RxBus
import com.example.william.my.module.activity.BaseRecyclerActivity
import com.example.william.my.module.libraries.event.GlobalEvent
import com.example.william.my.module.router.ARouterPath
import com.example.william.my.module.router.item.RouterItem
import com.example.william.my.module.utils.T

@Route(path = ARouterPath.Lib.Lib)
class LibrariesActivity : BaseRecyclerActivity() {

    override fun buildRouter(): ArrayList<RouterItem> {
        val routerItems = ArrayList<RouterItem>()
        routerItems.add(RouterItem("RxBusActivity", ARouterPath.Lib.Lib_RxBus))
        routerItems.add(RouterItem("LiveEventBusActivity", ARouterPath.Lib.Lib_LiveEventBus))
        routerItems.add(RouterItem("FlowEventBusActivity", ARouterPath.Lib.Lib_FlowEventBus))
        routerItems.add(RouterItem("BannerActivity", ARouterPath.Lib.Lib_Banner))
        routerItems.add(RouterItem("InfiniteImageActivity", ARouterPath.Lib.Lib_InfiniteImage))
        routerItems.add(RouterItem("NinePatchActivity", ARouterPath.Lib.Lib_NinePatch))
        routerItems.add(RouterItem("VerifyCodeActivity", ARouterPath.Lib.Lib_VerifyCode))
        routerItems.add(RouterItem("SphereCollisionActivity", ARouterPath.Lib.Lib_SphereCollision))
        return routerItems
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxBus
            .observeEvent(GlobalEvent::class.java)
            .subscribe {
                T.show(it.message)
            }
//        LiveEventBus
//            .observeEvent<GlobalEvent> {
//                T.show(it.message)
//            }
        FlowEventBus
            .observeEvent<GlobalEvent> {
                T.show(it.message)
            }
    }
}