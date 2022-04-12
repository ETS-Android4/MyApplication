package com.example.william.my.core.eventbus.livedata.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.example.william.my.core.eventbus.livedata.LiveEventBus

object LiveEventBusProvider : ViewModelStoreOwner {

    private val store: ViewModelStore = ViewModelStore()

    override fun getViewModelStore(): ViewModelStore {
        return store
    }

    private val mLiveEventBusProvider: ViewModelProvider by lazy {
        ViewModelProvider(
            LiveEventBusProvider,
            ViewModelProvider.AndroidViewModelFactory.getInstance(LiveEventBus.application)
        )
    }

    operator fun <T : ViewModel> get(modelClass: Class<T>): T {
        return mLiveEventBusProvider[modelClass]
    }
}