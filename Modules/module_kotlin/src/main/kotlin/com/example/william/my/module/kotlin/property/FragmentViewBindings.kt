package com.example.william.my.module.kotlin.property

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

class FragmentViewBindings {
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
        return FragmentViewBindingProperty(viewBinder)
    }
}