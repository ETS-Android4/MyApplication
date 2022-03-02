package com.example.william.my.core.retrofit.callback

import com.example.william.my.core.retrofit.base.BaseRetrofitCallback
import com.example.william.my.core.retrofit.exception.ApiException
import com.example.william.my.core.retrofit.exception.ExceptionHandler
import com.example.william.my.core.retrofit.response.RetrofitResponse
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable

/**
 * 处理基本逻辑
 *
 * io.reactivex.rxjava3.core.SingleObserver
 */
open class RetrofitLiveDataCallback<T> : SingleObserver<RetrofitResponse<T>>,
    BaseRetrofitCallback<RetrofitResponse<T>> {

    private var disposable: Disposable? = null

    override fun onSubscribe(d: Disposable) {
        disposable = d
    }

    override fun onSuccess(t: RetrofitResponse<T>) {
        onResponse(t)
        dispose()
    }

    override fun onError(e: Throwable) {
        if (e is ApiException) {
            onFailure(e)
        } else {
            onFailure(ApiException(e, ExceptionHandler.ERROR.UNKNOWN))
        }
        dispose()
    }

    private fun dispose() {
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }

    override fun onLoading() {
        onPostValue(RetrofitResponse.loading())
    }

    override fun onResponse(response: RetrofitResponse<T>?) {
        try {
            response?.let {
                onPostValue(it)
            }
        } catch (e: Exception) {
            onPostValue(RetrofitResponse.error("数据异常"))
        }
    }

    override fun onFailure(e: ApiException) {
        onPostValue(RetrofitResponse.error(e.message))
    }

    open fun onPostValue(value: RetrofitResponse<T>) {

    }
}