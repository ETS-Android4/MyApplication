package com.example.william.my.core.network.retrofit.response;

import com.example.william.my.core.network.base.BaseBean;
import com.example.william.my.core.network.retrofit.status.State;
import com.google.gson.annotations.SerializedName;

public class RetrofitResponse<T> extends BaseBean {

    @SerializedName("errorCode")
    private final int code;// 状态码

    @SerializedName("errorMsg")
    private String message;// 描述信息

    @SerializedName("data")
    private T data;//数据对象

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

