package com.example.william.my.module.libraries.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.william.my.core.eventbus.flow.FlowEventBus
import com.example.william.my.library.base.BaseFragment
import com.example.william.my.module.libraries.databinding.LibActivityEventBusBinding
import com.example.william.my.module.libraries.event.ActivityEvent
import com.example.william.my.module.libraries.event.FragmentEvent
import com.example.william.my.module.libraries.event.GlobalEvent

class FlowEventBusFragment : BaseFragment() {

    lateinit var mBinding: LibActivityEventBusBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = LibActivityEventBusBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.textView.text = "FlowEventBusFragment"

        observeEvent()

        postEvent()
    }

    private fun observeEvent() {
        FlowEventBus.observeEvent<GlobalEvent> {

        }
        FlowEventBus.observeEvent<ActivityEvent> {

        }
        FlowEventBus.observeEvent<FragmentEvent> {

        }
    }

    private fun postEvent() {
        mBinding.textView.setOnClickListener {
            FlowEventBus.postEvent(GlobalEvent("send GlobalEvent by Fragment"))
            FlowEventBus.postEvent(requireActivity(), ActivityEvent("send ActivityEvent by Fragment"))
            FlowEventBus.postEvent(this, FragmentEvent("send FragmentEvent by Fragment"))
        }
    }
}