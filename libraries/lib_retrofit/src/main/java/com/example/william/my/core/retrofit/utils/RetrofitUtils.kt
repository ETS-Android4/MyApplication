package com.example.william.my.core.retrofit.utils

import com.example.william.my.core.retrofit.callback.RetrofitResponseCallback
import com.example.william.my.core.retrofit.exception.ApiException
import com.example.william.my.core.retrofit.exception.ExceptionHandler
import com.example.william.my.core.retrofit.function.HttpResultFunction
import com.example.william.my.core.retrofit.helper.RetrofitHelper
import com.example.william.my.core.retrofit.observer.RetrofitObserver
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

object RetrofitUtils {

    @JvmStatic
    fun getBaseUrl(url: String): String {
        var tempUrl = url
        var head = ""
        var index = tempUrl.indexOf("://")
        if (index != -1) {
            head = tempUrl.substring(0, index + 3)
            tempUrl = tempUrl.substring(index + 3)
        }
        index = tempUrl.indexOf("/")
        if (index != -1) {
            tempUrl = tempUrl.substring(0, index + 1)
        }
        return head + tempUrl
    }

    @JvmStatic
    fun <T> buildApi(api: Class<T>): T {
        return RetrofitHelper
            .getInstance()
            .baseUrl("https://www.wanandroid.com/")
            .build()
            .create(api)
    }

    @JvmStatic
    fun <T> buildApi(baseUrl: String, api: Class<T>): T {
        return RetrofitHelper
            .getInstance()
            .baseUrl(baseUrl)
            .build()
            .create(api)
    }

    @JvmStatic
    fun <T> buildSingle(
        observable: Single<T>,
        callback: RetrofitResponseCallback<T>
    ) {
        observable
            .onErrorResumeNext(HttpResultFunction())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : RetrofitObserver<T>() {
                override fun onResponse(response: T) {
                    callback.onResponse(response)
                }

                override fun onFailure(e: ApiException) {
                    callback.onFailure(e)
                }
            })
    }

    @JvmStatic
    fun <T> buildLiveData(
        observable: Single<T>,
        callback: RetrofitResponseCallback<T>
    ) {
        observable
            .onErrorResumeNext(HttpResultFunction())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : RetrofitObserver<T>() {
                override fun onResponse(response: T) {
                    callback.onResponse(response)
                }

                override fun onFailure(e: ApiException) {
                    callback.onFailure(e)
                }
            })
    }

    suspend fun <T> buildFlow(
        flow: Flow<T>,
        callback: RetrofitResponseCallback<T>
    ) {
        withContext(Dispatchers.Main) {
            flow
                .onStart {
                    callback.onLoading()
                }
                .catch { exception ->
                    callback.onFailure(ExceptionHandler.handleException(exception))
                }
                .collect { response ->
                    callback.onResponse(response)
                }
        }
    }
}