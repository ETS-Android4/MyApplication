package com.example.william.my.module.kotlin.result

sealed class NetworkResult<out R> {

    // By using Nothing as T, Loading is a subtype of all NetworkResult<T>
    object Loading : NetworkResult<Nothing>()

    // Successful results are stored in data
    data class Success<out T>(val data: T) : NetworkResult<T>()

    // By using Nothing as T, all NetworkError instances are a subtypes of all NetworkResults<T>
    data class Error(val exception: Throwable) : NetworkResult<Nothing>()
}