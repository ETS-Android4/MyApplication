package com.example.william.my.module.kotlin.bind.viewbinding

import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.william.my.module.kotlin.bind.method.CreateMethod
import com.example.william.my.module.kotlin.bind.cache.ViewBindingCache
import com.example.william.my.module.kotlin.bind.property.DialogViewBindingProperty
import com.example.william.my.module.kotlin.bind.property.FragmentViewBindingProperty
import com.example.william.my.module.kotlin.bind.property.ViewBindingProperty

/**
 * Create new [ViewBinding] associated with the [Fragment]
 *
 * @param T Class of expected [ViewBinding] result class
 */
@JvmName("viewBindingFragment")
public inline fun <reified T : ViewBinding> Fragment.viewBinding(
    createMethod: CreateMethod = CreateMethod.BIND
): ViewBindingProperty<Fragment, T> {
    return viewBinding(T::class.java, createMethod)
}

/**
 * Create new [ViewBinding] associated with the [Fragment]
 *
 * @param vbFactory Function that create new instance of [ViewBinding]. `MyViewBinding::bind` can be used
 * @param viewProvider Provide a [View] from the Fragment. By default call [Fragment.requireView]
 */
@JvmName("viewBindingFragment")
public inline fun <F : Fragment, T : ViewBinding> Fragment.viewBinding(
    crossinline vbFactory: (View) -> T,
    crossinline viewProvider: (F) -> View = Fragment::requireView
): ViewBindingProperty<F, T> {
    return viewBinding { fragment: F -> vbFactory(viewProvider(fragment)) }
}

/**
 * Create new [ViewBinding] associated with the [Fragment]
 */
@Suppress("UNCHECKED_CAST")
@JvmName("viewBindingFragment")
public fun <F : Fragment, T : ViewBinding> Fragment.viewBinding(
    viewBinder: (F) -> T
): ViewBindingProperty<F, T> {
    return when (this) {
        is DialogFragment -> DialogViewBindingProperty(viewBinder) as ViewBindingProperty<F, T>
        else -> FragmentViewBindingProperty(viewBinder)
    }
}

/**
 * Create new [ViewBinding] associated with the [Fragment]
 *
 * @param viewBindingClass Class of expected [ViewBinding] result class
 */
@JvmName("viewBindingFragment")
public fun <T : ViewBinding> Fragment.viewBinding(
    viewBindingClass: Class<T>,
    createMethod: CreateMethod = CreateMethod.BIND
): ViewBindingProperty<Fragment, T> = when (createMethod) {
    CreateMethod.BIND -> viewBinding {
        ViewBindingCache.getBind(viewBindingClass).bind(requireView())
    }
    CreateMethod.INFLATE -> viewBinding {
        ViewBindingCache.getInflateWithLayoutInflater(viewBindingClass)
            .inflate(layoutInflater, null, false)
    }
}
