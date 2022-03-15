package com.example.william.my.core.retrofit.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.william.my.core.retrofit.callback.RetrofitLiveDataCallback
import com.example.william.my.core.retrofit.callback.RetrofitResponseCallback
import com.example.william.my.core.retrofit.exception.ApiException
import com.example.william.my.core.retrofit.function.HttpResultFunction
import com.example.william.my.core.retrofit.helper.RetrofitHelper
import com.example.william.my.core.retrofit.response.RetrofitResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

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

    /**
     * RetrofitResponse<T> --> onResponse(T)
     */
    @JvmStatic
    fun <T> buildSingle(single: Single<RetrofitResponse<T>>, callback: RetrofitResponseCallback<T>) {
        single
            .onErrorResumeNext(HttpResultFunction())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : RetrofitResponseCallback<T>() {
                override fun onResponse(response: T?) {
                    callback.onResponse(response)
                }

                override fun onFailure(e: ApiException) {
                    callback.onFailure(e)
                }
            })
    }

    /**
     * RetrofitResponse<T> --> MutableLiveData<RetrofitResponse<T>>
     */
    @JvmStatic
    fun <T> buildLiveData(single: Single<RetrofitResponse<T>>): LiveData<RetrofitResponse<T>> {
        val liveData: MutableLiveData<RetrofitResponse<T>> = MutableLiveData()
        single
            .onErrorResumeNext(HttpResultFunction())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : RetrofitLiveDataCallback<T>() {
                override fun onPostValue(value: RetrofitResponse<T>) {
                    liveData.postValue(value)
                }
            })
        return liveData
    }
}