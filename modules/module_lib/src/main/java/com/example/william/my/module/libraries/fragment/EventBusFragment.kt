package com.example.william.my.module.libraries.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.william.my.core.eventbus.flow.FlowEventBus
import com.example.william.my.library.base.BaseFragment
import com.example.william.my.module.libraries.databinding.LibFragmentEventBusBinding
import com.example.william.my.module.libraries.event.ActivityEvent
import com.example.william.my.module.libraries.event.FragmentEvent
import com.example.william.my.module.libraries.event.GlobalEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class EventBusFragment : BaseFragment() {

    private lateinit var mBinding: LibFragmentEventBusBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = LibFragmentEventBusBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeEvent()

        postEvent()
    }

    private fun observeEvent() {
        //监听全局事件
        FlowEventBus.observeEvent<GlobalEvent> {
            //mBinding.response.text = it.message
        }

        //监听 Activity Scope 事件
        FlowEventBus.observeEvent<ActivityEvent>(owner = requireActivity()) {
            mBinding.response.text = it.message
        }

        //监听 Fragment Scope 事件
        FlowEventBus.observeEvent<FragmentEvent>(owner = this) {
            mBinding.response.text = it.message
        }

        //监听 App CoroutineScope 事件
        FlowEventBus.observeEvent<GlobalEvent>(coroutineScope = CoroutineScope(Dispatchers.Main)) {
            //mBinding.response.text = it.message
        }

        //监听 ViewModelStoreOwner CoroutineScope 事件
        FlowEventBus.observeEvent<ActivityEvent>(owner = requireActivity(), coroutineScope = CoroutineScope(Dispatchers.Main)) {
            mBinding.response.text = it.message
        }
        FlowEventBus.observeEvent<FragmentEvent>(owner = this, coroutineScope = CoroutineScope(Dispatchers.Main)) {
            mBinding.response.text = it.message
        }
    }

    private fun postEvent() {

    }
}