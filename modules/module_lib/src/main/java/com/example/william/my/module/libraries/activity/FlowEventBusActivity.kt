package com.example.william.my.module.libraries.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.core.eventbus.flow.FlowEventBus
import com.example.william.my.module.libraries.databinding.LibActivityEventBusBinding
import com.example.william.my.module.libraries.event.ActivityEvent
import com.example.william.my.module.libraries.event.FragmentEvent
import com.example.william.my.module.libraries.event.GlobalEvent
import com.example.william.my.module.libraries.event.StickyEvent
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

        postStickyEvent()

        observeEvent()

        postEvent()
    }

    private fun postStickyEvent() {
        FlowEventBus.postEvent(StickyEvent("send StickyEvent by Activity"))
    }

    private fun observeEvent() {
        FlowEventBus.observeEvent<GlobalEvent> {
            mBinding.global.text = it.message
        }
        FlowEventBus.observeEvent<StickyEvent>(isSticky = true) {
            mBinding.sticky.text = it.message
        }
    }

    private fun postEvent() {
        FlowEventBus.postEvent(GlobalEvent("send GlobalEvent by Activity"))
    }

    private fun coroutineScope() {
        //监听 App CoroutineScope 事件
        FlowEventBus.observeEvent<ActivityEvent>(coroutineScope = CoroutineScope(Dispatchers.Main)) {

        }

        //监听 ViewModelStoreOwner CoroutineScope 事件
        FlowEventBus.observeEvent<ActivityEvent>(owner = this, coroutineScope = CoroutineScope(Dispatchers.Main)) {

        }
    }
}