package com.example.william.my.module.kotlin.bind.cache

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RestrictTo
import androidx.viewbinding.ViewBinding
import java.lang.reflect.Method

object ViewBindingCache {

    private val inflateCache =
        mutableMapOf<Class<out ViewBinding>, InflateViewBinding<ViewBinding>>()
    private val bindCache = mutableMapOf<Class<out ViewBinding>, BindViewBinding<ViewBinding>>()

    @Suppress("UNCHECKED_CAST")
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    internal fun <T : ViewBinding> getInflateWithLayoutInflater(viewBindingClass: Class<T>): InflateViewBinding<T> {
        return inflateCache.getOrPut(viewBindingClass) { InflateViewBinding(viewBindingClass) } as InflateViewBinding<T>
    }

    @Suppress("UNCHECKED_CAST")
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    internal fun <T : ViewBinding> getBind(viewBindingClass: Class<T>): BindViewBinding<T> {
        return bindCache.getOrPut(viewBindingClass) { BindViewBinding(viewBindingClass) } as BindViewBinding<T>
    }

    /**
     * Reset all cached data
     */
    fun clear() {
        inflateCache.clear()
        bindCache.clear()
    }
}

/**
 * Wrapper of ViewBinding.inflate(LayoutInflater, ViewGroup, Boolean)
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal abstract class InflateViewBinding<out V : ViewBinding>(private val inflateViewBinding: Method) {

    @Suppress("UNCHECKED_CAST")
    abstract fun inflate(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ): V
}


@RestrictTo(RestrictTo.Scope.LIBRARY)
@Suppress("FunctionName")
internal fun <V : ViewBinding> InflateViewBinding(viewBindingClass: Class<V>): InflateViewBinding<V> {
    return try {
        val method = viewBindingClass.getMethod(
            "inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java
        )
        FullInflateViewBinding(method)
    } catch (e: NoSuchMethodException) {
        val method =
            viewBindingClass.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java)
        MergeInflateViewBinding(method)
    }
}

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class FullInflateViewBinding<out V : ViewBinding>(
    private val inflateViewBinding: Method
) : InflateViewBinding<V>(inflateViewBinding) {

    @Suppress("UNCHECKED_CAST")
    override fun inflate(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ): V {
        return inflateViewBinding(null, layoutInflater, parent, attachToParent) as V
    }
}

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class MergeInflateViewBinding<out V : ViewBinding>(
    private val inflateViewBinding: Method
) : InflateViewBinding<V>(inflateViewBinding) {

    @Suppress("UNCHECKED_CAST")
    override fun inflate(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ): V {
        require(attachToParent) {
            "${InflateViewBinding::class.java.simpleName} supports inflate only with attachToParent=true"
        }
        return inflateViewBinding(null, layoutInflater, parent) as V
    }
}

/**
 * Wrapper of ViewBinding.bind(View)
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class BindViewBinding<out V : ViewBinding>(viewBindingClass: Class<V>) {

    private val bindViewBinding = viewBindingClass.getMethod("bind", View::class.java)

    @Suppress("UNCHECKED_CAST")
    fun bind(view: View): V {
        return bindViewBinding(null, view) as V
    }
}
