package com.example.william.my.module.sample.utils.callback

import com.example.william.my.module.sample.utils.exception.KtApiException
import io.reactivex.rxjava3.annotations.NonNull

interface KtRetrofitCallback<T> {
    fun onResponse(response: @NonNull T?)
    fun onFailure(e: @NonNull KtApiException?)
}