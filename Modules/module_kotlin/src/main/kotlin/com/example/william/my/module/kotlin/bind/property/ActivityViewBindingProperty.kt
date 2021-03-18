package com.example.william.my.module.kotlin.bind.property

import androidx.activity.ComponentActivity
import androidx.annotation.RestrictTo
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding

@RestrictTo(RestrictTo.Scope.LIBRARY)
class ActivityViewBindingProperty<in A : ComponentActivity, out T : ViewBinding>(
    viewBinder: (A) -> T
) : LifecycleViewBindingProperty<A, T>(viewBinder) {
    override fun getLifecycleOwner(thisRef: A): LifecycleOwner {
        return thisRef
    }
}