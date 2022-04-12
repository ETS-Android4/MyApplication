package com.example.william.my.core.eventbus.livedata

import android.app.Application
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.william.my.core.eventbus.livedata.store.LiveEventBusProvider
import com.example.william.my.core.eventbus.livedata.viewmodel.LiveEventBusModel
import kotlinx.coroutines.Job

object LiveEventBus {

    lateinit var application: Application

    fun init(application: Application) {
        LiveEventBus.application = application
    }

//_______________________________________
//          observe event
//_______________________________________

//    /**
//     * 监听 App Scope 事件
//     */
//    @MainThread
//    inline fun <reified T> observeEvent(
//        dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
//        minState: Lifecycle.State = Lifecycle.State.STARTED,
//        isSticky: Boolean = false,
//        noinline onReceived: (T) -> Unit
//    ): Job {
//        return LiveEventBusProvider[FlowEventBusModel::class.java]
//            .observeEvent(ProcessLifecycleOwner.get(), minState, dispatcher, T::class.java.name, isSticky, onReceived)
//    }

    /**
     * 监听 Activity Scope 事件
     */
    @MainThread
    inline fun <reified T> observeEvent(
        owner: FragmentActivity,
        isSticky: Boolean = false,
        noinline onReceived: (T) -> Unit
    ): Job {
        return ViewModelProvider(owner)[LiveEventBusModel::class.java]
            .observeEvent(owner, T::class.java.name, isSticky, onReceived)
    }

    /**
     * 监听 Fragment Scope 事件
     */
    @MainThread
    inline fun <reified T> observeEvent(
        owner: Fragment,

        isSticky: Boolean = false,
        noinline onReceived: (T) -> Unit
    ): Job {
        return ViewModelProvider(owner)[LiveEventBusModel::class.java]
            .observeEvent(owner, T::class.java.name, isSticky, onReceived)
    }

//_______________________________________
//          post event
//_______________________________________

//    /**
//     * Application范围的事件
//     */
//    inline fun <reified T> postEvent(event: T) {
//        LiveEventBusProvider[LiveEventBusModel::class.java]
//            .postEvent(T::class.java.name, event!!)
//    }

    /**
     * 限定范围的事件
     */
    inline fun <reified T> postEvent(scope: ViewModelStoreOwner, event: T) {
        ViewModelProvider(scope)[LiveEventBusModel::class.java]
            .postEvent(T::class.java.name, event!!)
    }

//_______________________________________
//          get event
//_______________________________________

    //移除事件
    inline fun <reified T> removeStickyEvent(event: Class<T>) {
        LiveEventBusProvider[LiveEventBusModel::class.java]
            .removeStickEvent(event.name)
    }

    inline fun <reified T> removeStickyEvent(scope: ViewModelStoreOwner, event: Class<T>) {
        ViewModelProvider(scope)[LiveEventBusModel::class.java]
            .removeStickEvent(event.name)
    }
}