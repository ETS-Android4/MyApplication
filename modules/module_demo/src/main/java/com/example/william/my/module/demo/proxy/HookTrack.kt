package com.example.william.my.module.demo.proxy

import android.app.Application
import com.example.william.my.module.utils.L

object HookTrack {

    private var activityLifeCycleRegister = false

    fun init(application: Application?) {
        if (application == null) {
            L.e("TAG", "Please init with the param \"Application\"/")
            throw RuntimeException()
        }
        if (!activityLifeCycleRegister) {
            application.registerActivityLifecycleCallbacks(HookActivityLifecycleCallbacks())
            activityLifeCycleRegister = true
        }
    }
}