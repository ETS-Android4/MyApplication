package com.example.william.my.core.network.retrofit.exception;

/**
 * 服务器返回自定义异常
 */
public class ResultException extends RuntimeException {

    private final int code;
    private final String message;

    public ResultException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message == null ? "" : message;
    }
}