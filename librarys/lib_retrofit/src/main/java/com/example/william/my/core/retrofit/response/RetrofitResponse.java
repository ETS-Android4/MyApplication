package com.example.william.my.core.retrofit.response;

import com.example.william.my.core.retrofit.base.BaseBean;
import com.example.william.my.core.retrofit.status.State;
import com.google.gson.annotations.SerializedName;

/**
 * RetrofitResponse
 */
public class RetrofitResponse<T> extends BaseBean {

    /**
     * 状态码
     */
    @SerializedName("errorCode")
    private final int code;

    /**
     * 描述信息
     */
    @SerializedName("errorMsg")
    private String message;

    /**
     * 数据对象
     */
    @SerializedName("data")
    private T data;

    public RetrofitResponse(int code) {
        this.code = code;
    }

    private RetrofitResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private RetrofitResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public static <T> RetrofitResponse<T> loading() {
        return new RetrofitResponse<>(State.LOADING);
    }

    public static <T> RetrofitResponse<T> success(T data) {
        return new RetrofitResponse<>(State.SUCCESS, data);
    }

    public static <T> RetrofitResponse<T> error(String message) {
        return new RetrofitResponse<>(State.ERROR, message);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message == null ? "" : message;
    }

    public T getData() {
        return data;
    }

    /**
     * 是否成功(这里约定0)
     */
    public boolean isSuccess() {
        return code == State.SUCCESS;
    }
}

