package com.example.william.my.module.kotlin.bind.property

import androidx.annotation.MainThread
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty

interface ViewBindingProperty<in R : Any, out T : ViewBinding> : ReadOnlyProperty<R, T> {
    @MainThread
    fun clear()
}