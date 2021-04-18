package com.example.william.my.module.sample.utils.exception

/**
 * 统一异常管理
 */
data class KtApiException(val throwable: Throwable, var code: Int) : Exception(throwable) {

    override var message: String? = null

}