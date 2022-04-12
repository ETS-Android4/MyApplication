package com.example.william.my.module.libraries.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.core.eventbus.livedata.LiveEventBus
import com.example.william.my.module.libraries.databinding.LibActivityEventBusBinding
import com.example.william.my.module.libraries.event.GlobalEvent
import com.example.william.my.module.libraries.event.StickyEvent
import com.example.william.my.module.router.ARouterPath

@Route(path = ARouterPath.Lib.Lib_LiveEventBus)
class LiveEventBusActivity : AppCompatActivity() {

    private lateinit var mBinding: LibActivityEventBusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = LibActivityEventBusBinding.inflate(layoutInflater);
        setContentView(mBinding.root)

        postStickyEvent()

        observeEvent()

        postEvent()
    }

    private fun postStickyEvent() {
        LiveEventBus.postEvent(this, StickyEvent("send StickyEvent by Activity"))
    }

    private fun observeEvent() {
        LiveEventBus.observeEvent<GlobalEvent>(this) {
            mBinding.global.text = it.message
        }
        LiveEventBus.observeEvent<StickyEvent>(this, isSticky = true) {
            mBinding.sticky.text = it.message
        }
    }

    private fun postEvent() {
        LiveEventBus.postEvent(this, GlobalEvent("send GlobalEvent by Activity"))
    }
}