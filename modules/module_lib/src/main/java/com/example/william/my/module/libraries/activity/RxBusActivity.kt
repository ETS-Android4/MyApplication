package com.example.william.my.module.libraries.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.core.eventbus.rxjava.RxBus
import com.example.william.my.module.libraries.databinding.LibActivityEventBusBinding
import com.example.william.my.module.libraries.event.GlobalEvent
import com.example.william.my.module.libraries.event.StickyEvent
import com.example.william.my.module.router.ARouterPath


@Route(path = ARouterPath.Lib.Lib_RxBus)
class RxBusActivity : AppCompatActivity() {

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
        RxBus.postSticky(StickyEvent("send StickyEvent by Activity"))
    }

    private fun observeEvent() {
        RxBus
            .observeEvent(GlobalEvent::class.java)
            .subscribe {
                mBinding.global.text = it.message
            }
        RxBus
            .observeEvent(StickyEvent::class.java)
            .subscribe {
                mBinding.sticky.text = it.message
            }
    }

    private fun postEvent() {
        RxBus.post(GlobalEvent("send GlobalEvent by Activity"))
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.removeAllStickyEvents()
    }
}