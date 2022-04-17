package com.example.william.my.core.eventbus.flow.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.example.william.my.core.eventbus.flow.FlowEventBus

object FlowEventBusProvider : ViewModelStoreOwner {

    private val store: ViewModelStore = ViewModelStore()

    override fun getViewModelStore(): ViewModelStore {
        return store
    }

    private val mFlowEventBusProvider: ViewModelProvider by lazy {
        ViewModelProvider(
            FlowEventBusProvider,
            ViewModelProvider.AndroidViewModelFactory.getInstance(FlowEventBus.application)
        )
    }

    operator fun <T : ViewModel> get(modelClass: Class<T>): T {
        return mFlowEventBusProvider[modelClass]
    }
}