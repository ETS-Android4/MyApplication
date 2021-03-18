package com.example.william.my.module.kotlin.bind.method

import androidx.viewbinding.ViewBinding

/**
 * Method that will be used to create [ViewBinding].
 */
enum class CreateMethod {
    /**
     * Use `ViewBinding.bind(View)`
     */
    BIND,

    /**
     * Use `ViewBinding.inflate(LayoutInflater, ViewGroup, boolean)`
     */
    INFLATE
}
