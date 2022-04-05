package com.example.william.my.module.libraries.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.example.william.my.core.eventbus.flow.FlowEventBus
import com.example.william.my.library.base.BaseFragment
import com.example.william.my.module.libraries.databinding.LibFragmentEventBusBinding
import com.example.william.my.module.libraries.event.ActivityEvent
import com.example.william.my.module.libraries.event.FragmentEvent
import com.example.william.my.module.libraries.event.GlobalEvent
import kotlinx.coroutines.Dispatchers

class FlowEventBusFragment : BaseFragment() {

    lateinit var mBinding: LibFragmentEventBusBinding

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
            mBinding.response.text = "received GlobalEvent 1: ${it.message}"
        }
        FlowEventBus.observeEvent<GlobalEvent>(isSticky = true) {
            mBinding.response.text = "received GlobalEvent 2: ${it.message}"
        }
        FlowEventBus.observeEvent<GlobalEvent>(dispatcher = Dispatchers.IO) {
            mBinding.response.text = "received GlobalEvent 3: ${it.message}"
        }

        //监听Activity事件
        FlowEventBus.observeEvent<ActivityEvent>(scope = requireActivity(), isSticky = true) {
            mBinding.response.text = "received ActivityEvent: ${it.message}"
        }
        FlowEventBus.observeEvent<ActivityEvent>(scope = requireActivity(), dispatcher = Dispatchers.IO, minActiveState = Lifecycle.State.STARTED) {
            mBinding.response.text = "received ActivityEvent: ${it.message}"
        }

        //监听自己Scope事件
        FlowEventBus.observeEvent<FragmentEvent>(scope = this, isSticky = true) {
            mBinding.response.text = "received ActivityEvent: ${it.message}"
        }
        FlowEventBus.observeEvent<FragmentEvent>(scope = this, dispatcher = Dispatchers.IO, minActiveState = Lifecycle.State.STARTED) {
            mBinding.response.text = "received FragmentEvent: ${it.message}"
        }
    }

    private fun postEvent() {
        mBinding.global.setOnClickListener {
            FlowEventBus.postEvent(GlobalEvent("send GlobalEvent by Fragment"))
        }
        mBinding.activity.setOnClickListener {
            FlowEventBus.postEvent(requireActivity(), ActivityEvent("send ActivityEvent by Fragment"))
        }
        mBinding.fragment.setOnClickListener {
            FlowEventBus.postEvent(this, FragmentEvent("send FragmentEvent by Fragment"))
        }
    }
}