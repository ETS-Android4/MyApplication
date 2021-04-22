package com.example.william.my.module.sample.retrofit

sealed class KtRetrofitResult<out R> {

    // By using Nothing as T, Loading is a subtype of all NetworkResult<T>
    object Loading : KtRetrofitResult<Nothing>()

    // Successful results are stored in data
    data class Success<out T>(val data: T) : KtRetrofitResult<T>()

    // By using Nothing as T, all NetworkError instances are a subtypes of all NetworkResults<T>
    data class Error(val exception: Throwable) : KtRetrofitResult<Nothing>()
}