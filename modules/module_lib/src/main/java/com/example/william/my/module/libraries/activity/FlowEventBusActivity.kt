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
import kotlinx.coroutines.Dispatchers

@Route(path = ARouterPath.Lib.Lib_EventBus)
class FlowEventBusActivity : AppCompatActivity() {

    lateinit var mBinding: LibActivityEventBusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = LibActivityEventBusBinding.inflate(layoutInflater);
        setContentView(mBinding.root)

        observeEvent()

        postEvent()
    }

    private fun observeEvent() {
        //自定义事件
        FlowEventBus.observeEvent<GlobalEvent> {
            mBinding.textView.text = it.message
        }

        //自定义事件
        FlowEventBus.observeEvent<ActivityEvent>(this) {
            mBinding.textView.text = "received ActivityEvent: ${it.message}"
        }

        //自定义事件 切换线程
        FlowEventBus.observeEvent<ActivityEvent>(Dispatchers.IO) {
            mBinding.textView.text = "received ActivityEvent:${it.message} " + Thread.currentThread().name
        }

        //自定义事件 指定最小生命周期
        FlowEventBus.observeEvent<ActivityEvent>(minActiveState = Lifecycle.State.DESTROYED) {
            mBinding.textView.text = "received ActivityEvent:${it.message}   >  DESTROYED"
        }

        //自定义事件 切换线程 + 指定最小生命周期
        FlowEventBus.observeEvent<ActivityEvent>(Dispatchers.IO, Lifecycle.State.DESTROYED) {
            mBinding.textView.text = it.message
        }
    }

    private fun postEvent() {
        mBinding.textView.setOnClickListener {
            FlowEventBus.postEvent(GlobalEvent("send GlobalEvent by Activity"))
            FlowEventBus.postEvent(this, ActivityEvent("send ActivityEvent by Activity"))
        }
    }
}