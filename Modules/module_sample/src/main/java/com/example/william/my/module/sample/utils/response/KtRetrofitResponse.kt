package com.example.william.my.module.sample.utils.response

import com.example.william.my.module.sample.utils.state.KtState

data class KtRetrofitResponse<T>(var code: Int, var message: String, val data: T?) {

    constructor(code: Int) : this(code, "", null)

    constructor(code: Int, data: T?) : this(code, "", data)

    constructor(code: Int, message: String) : this(code, message, null)

    /**
     * 是否成功(这里约定0)
     */
    val isSuccess: Boolean
        get() = code == KtState.SUCCESS

    companion object {
        fun <T> loading(): KtRetrofitResponse<T> {
            return KtRetrofitResponse(KtState.LOADING)
        }

        fun <T> error(message: String): KtRetrofitResponse<T> {
            return KtRetrofitResponse(KtState.ERROR, message)
        }

        fun <T> success(data: T): KtRetrofitResponse<T> {
            return KtRetrofitResponse(KtState.SUCCESS, data)
        }
    }
}