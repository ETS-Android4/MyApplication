package com.example.william.my.module.kotlin.bind.viewbinding

import android.app.Activity
import android.view.View
import androidx.activity.ComponentActivity
import androidx.annotation.IdRes
import androidx.viewbinding.ViewBinding
import com.example.william.my.module.kotlin.bind.cache.ViewBindingCache
import com.example.william.my.module.kotlin.bind.method.CreateMethod
import com.example.william.my.module.kotlin.bind.property.ActivityViewBindingProperty
import com.example.william.my.module.kotlin.bind.property.ViewBindingProperty
import com.example.william.my.module.kotlin.bind.utils.findRootView
import com.example.william.my.module.kotlin.bind.utils.requireViewByIdCompat

/**
 * Create new [ViewBinding] associated with the [Activity][ComponentActivity] and allow customize how
 * a [View] will be bounded to the view binding.
 */
@JvmName("viewBindingActivity")
 fun <A : ComponentActivity, T : ViewBinding> ComponentActivity.viewBinding(
    viewBinder: (A) -> T
): ViewBindingProperty<A, T> {
    return ActivityViewBindingProperty(viewBinder)
}

/**
 * Create new [ViewBinding] associated with the [Activity][ComponentActivity] and allow customize how
 * a [View] will be bounded to the view binding.
 */
@JvmName("viewBindingActivity")
 inline fun <A : ComponentActivity, T : ViewBinding> ComponentActivity.viewBinding(
    crossinline vbFactory: (View) -> T,
    crossinline viewProvider: (A) -> View = ::findRootView
): ViewBindingProperty<A, T> {
    return viewBinding { activity -> vbFactory(viewProvider(activity)) }
}

/**
 * Create new [ViewBinding] associated with the [Activity][this] and allow customize how
 * a [View] will be bounded to the view binding.
 *
 * @param vbFactory Function that create new instance of [ViewBinding]. `MyViewBinding::bind` can be used
 * @param viewBindingRootId Root view's id that will be used as root for the view binding
 */
@Suppress("unused")
@JvmName("viewBindingActivity")
 inline fun <T : ViewBinding> ComponentActivity.viewBinding(
    crossinline vbFactory: (View) -> T,
    @IdRes viewBindingRootId: Int
): ViewBindingProperty<ComponentActivity, T> {
    return viewBinding { activity -> vbFactory(activity.requireViewByIdCompat(viewBindingRootId)) }
}

/**
 * Create new [ViewBinding] associated with the [Activity][ComponentActivity].
 * You need to set [ViewBinding.getRoot] as content view using [Activity.setContentView].
 *
 * @param T Class of expected [ViewBinding] result class
 */
@JvmName("inflateViewBindingActivity")
 inline fun <reified T : ViewBinding> ComponentActivity.viewBinding(
    createMethod: CreateMethod = CreateMethod.BIND
) = viewBinding(T::class.java, createMethod)

@JvmName("inflateViewBindingActivity")
 fun <T : ViewBinding> ComponentActivity.viewBinding(
    viewBindingClass: Class<T>,
    createMethod: CreateMethod = CreateMethod.BIND
): ViewBindingProperty<ComponentActivity, T> = when (createMethod) {
    CreateMethod.BIND -> viewBinding(viewBindingClass, ::findRootView)
    CreateMethod.INFLATE -> viewBinding {
        ViewBindingCache.getInflate(viewBindingClass)
            .inflate(layoutInflater, null, false)
    }
}

/**
 * Create new [ViewBinding] associated with the [Activity]
 *
 * @param viewBindingClass Class of expected [ViewBinding] result class
 * @param rootViewProvider Provider of root view for the [ViewBinding] from the [Activity][this]
 */
@JvmName("viewBindingActivity")
 fun <T : ViewBinding> ComponentActivity.viewBinding(
    viewBindingClass: Class<T>,
    rootViewProvider: (ComponentActivity) -> View
): ViewBindingProperty<ComponentActivity, T> {
    return viewBinding { activity ->
        ViewBindingCache.getBind(viewBindingClass).bind(rootViewProvider(activity))
    }
}
