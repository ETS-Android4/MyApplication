package com.example.william.my.core.eventbus.flow

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.example.william.my.core.eventbus.flow.store.FlowEventBusProvider
import com.example.william.my.core.eventbus.flow.viewmodel.FlowEventBusModel
import kotlinx.coroutines.*

object FlowEventBus {

    lateinit var application: Application

    fun init(application: Application) {
        FlowEventBus.application = application
    }

//_______________________________________
//          observe event
//_______________________________________

    /**
     * 监听App Scope 事件
     */
    @MainThread
    inline fun <reified T> observeEvent(
        dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        isSticky: Boolean = false,
        noinline onReceived: (T) -> Unit
    ): Job {
        return FlowEventBusProvider[FlowEventBusModel::class.java]
            .observeEvent(ProcessLifecycleOwner.get(), T::class.java.name, minActiveState, dispatcher, isSticky, onReceived)
    }

    /**
     * 监听Activity Scope 事件
     */
    @MainThread
    inline fun <reified T> observeEvent(
        scope: ComponentActivity, dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        isSticky: Boolean = false,
        noinline onReceived: (T) -> Unit
    ): Job {
        return ViewModelProvider(scope)[FlowEventBusModel::class.java]
            .observeEvent(scope, T::class.java.name, minActiveState, dispatcher, isSticky, onReceived)
    }

    /**
     * 监听Fragment Scope 事件
     */
    @MainThread
    inline fun <reified T> observeEvent(
        scope: Fragment, dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        isSticky: Boolean = false,
        noinline onReceived: (T) -> Unit
    ): Job {
        return ViewModelProvider(scope)[FlowEventBusModel::class.java]
            .observeEvent(scope, T::class.java.name, minActiveState, dispatcher, isSticky, onReceived)
    }

    @MainThread
    inline fun <reified T> observeEvent(
        coroutineScope: CoroutineScope,
        isSticky: Boolean = false,
        noinline onReceived: (T) -> Unit
    ): Job {
        return coroutineScope.launch {
            FlowEventBusProvider[FlowEventBusModel::class.java]
                .observeWithoutLifecycle(T::class.java.name, isSticky, onReceived)
        }
    }

    @MainThread
    inline fun <reified T> observeEvent(
        owner: ViewModelStoreOwner, coroutineScope: CoroutineScope,
        isSticky: Boolean = false,
        noinline onReceived: (T) -> Unit
    ): Job {
        return coroutineScope.launch {
            ViewModelProvider(owner)[FlowEventBusModel::class.java]
                .observeWithoutLifecycle(T::class.java.name, isSticky, onReceived)
        }
    }

//_______________________________________
//          post event
//_______________________________________

    /**
     * Application范围的事件
     */
    inline fun <reified T> postEvent(event: T, timeMillis: Long = 0L) {
        FlowEventBusProvider[FlowEventBusModel::class.java]
            .postEvent(T::class.java.name, event!!, timeMillis)
    }


    /**
     * 限定范围的事件
     */
    inline fun <reified T> postEvent(scope: ViewModelStoreOwner, event: T, timeMillis: Long = 0L) {
        ViewModelProvider(scope)[FlowEventBusModel::class.java]
            .postEvent(T::class.java.name, event!!, timeMillis)
    }

//_______________________________________
//          get event
//_______________________________________

    //获取事件
    inline fun <reified T> getEventObserverCount(event: Class<T>): Int {
        return FlowEventBusProvider[FlowEventBusModel::class.java]
            .getEventObserverCount(event.name)
    }

    inline fun <reified T> getEventObserverCount(scope: ViewModelStoreOwner, event: Class<T>): Int {
        return ViewModelProvider(scope)[FlowEventBusModel::class.java]
            .getEventObserverCount(event.name)
    }

    //移除事件
    inline fun <reified T> removeStickyEvent(event: Class<T>) {
        FlowEventBusProvider[FlowEventBusModel::class.java]
            .removeStickEvent(event.name)
    }

    inline fun <reified T> removeStickyEvent(scope: ViewModelStoreOwner, event: Class<T>) {
        ViewModelProvider(scope)[FlowEventBusModel::class.java]
            .removeStickEvent(event.name)
    }

    // 清除事件缓存
    @ExperimentalCoroutinesApi
    inline fun <reified T> clearStickyEvent(event: Class<T>) {
        FlowEventBusProvider[FlowEventBusModel::class.java]
            .clearStickEvent(event.name)
    }

    @ExperimentalCoroutinesApi
    inline fun <reified T> clearStickyEvent(scope: ViewModelStoreOwner, event: Class<T>) {
        ViewModelProvider(scope)[FlowEventBusModel::class.java]
            .clearStickEvent(event.name)
    }

    fun <T> LifecycleOwner.launchWhenStateAtLeast(
        minState: Lifecycle.State,
        block: suspend CoroutineScope.() -> T
    ): Job {
        return lifecycleScope.launch {
            lifecycle.whenStateAtLeast(minState, block)
        }
    }
}