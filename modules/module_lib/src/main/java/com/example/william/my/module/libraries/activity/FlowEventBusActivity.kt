package com.example.william.my.module.libraries.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.core.eventbus.flow.FlowEventBus
import com.example.william.my.module.libraries.databinding.LibActivityEventBusBinding
import com.example.william.my.module.libraries.event.ActivityEvent
import com.example.william.my.module.libraries.event.GlobalEvent
import com.example.william.my.module.router.ARouterPath
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Route(path = ARouterPath.Lib.Lib_FlowEventBus)
class FlowEventBusActivity : AppCompatActivity() {

    private lateinit var mBinding: LibActivityEventBusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = LibActivityEventBusBinding.inflate(layoutInflater);
        setContentView(mBinding.root)

        observeEvent()

        postEvent()
    }

    private fun observeEvent() {
        //自定义事件 跨页面
        FlowEventBus.observeEvent<GlobalEvent> {
            mBinding.response.text = "received GlobalEvent 1: ${it.message}"
        }
        FlowEventBus.observeEvent<GlobalEvent>(coroutineScope = CoroutineScope(Dispatchers.Main)) {
            mBinding.response.text = "received GlobalEvent 2: ${it.message}"
        }
        FlowEventBus.observeEvent<GlobalEvent>(owner = this, coroutineScope = CoroutineScope(Dispatchers.Main)) {

        }
        //自定义事件 本页面页
        FlowEventBus.observeEvent<ActivityEvent>(scope = this) {
            mBinding.response.text = "received ActivityEvent: ${it.message}"
        }
        //自定义事件 切换线程 + 指定最小生命周期
        FlowEventBus.observeEvent<ActivityEvent>(dispatcher = Dispatchers.IO, minActiveState = Lifecycle.State.DESTROYED) {
            mBinding.response.text = "received ActivityEvent: ${it.message} " + Thread.currentThread().name
        }
    }

    private fun postEvent() {
        mBinding.global.setOnClickListener {
            FlowEventBus.postEvent(GlobalEvent("send GlobalEvent by Activity"))
        }
        mBinding.activity.setOnClickListener {
            FlowEventBus.postEvent(this, ActivityEvent("send ActivityEvent by Activity"))
        }
    }
}