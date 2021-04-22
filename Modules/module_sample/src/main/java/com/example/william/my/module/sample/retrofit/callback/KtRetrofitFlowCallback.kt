package com.example.william.my.module.sample.retrofit.callback

import com.example.william.my.module.sample.retrofit.exception.KtApiException

interface KtRetrofitFlowCallback<T> {
    fun onLoading()
    fun onResponse(response: T)
    fun onFailure(e: KtApiException)
}