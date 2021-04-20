package com.example.william.my.module.sample.utils.callback

import com.example.william.my.module.sample.utils.exception.KtApiException

interface KtRetrofitCallback<T> {
    fun onResponse(response: T)
    fun onFailure(e: KtApiException)
}