package com.example.william.my.module.sample.utils.exception

/**
 * 服务器返回自定义异常
 */
data class KtResultException(val code: Int, override val message: String?) : RuntimeException()