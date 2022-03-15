package com.example.william.my.core.retrofit.callback

import com.example.william.my.core.retrofit.base.RetrofitCallback
import com.example.william.my.core.retrofit.exception.ApiException
import com.example.william.my.core.retrofit.exception.ExceptionHandler
import com.example.william.my.core.retrofit.response.RetrofitResponse
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable

/**
 * 处理基本逻辑
 */
open class RetrofitResponseCallback<T> : SingleObserver<RetrofitResponse<T>>, RetrofitCallback<T> {

    private var disposable: Disposable? = null

    override fun onSubscribe(d: Disposable) {
        disposable = d
    }

    override fun onSuccess(t: RetrofitResponse<T>) {
        onResponse(t.data)
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

    }

    override fun onResponse(response: T?) {

    }

    override fun onFailure(e: ApiException) {

    }
}