package com.example.william.my.core.retrofit.function;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.william.my.core.retrofit.exception.ServerResultException;
import com.example.william.my.core.retrofit.response.RetrofitResponse;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import io.reactivex.rxjava3.functions.Function;

/**
 * 服务器返回结果，泛型转换(JsonElement -> 泛型)
 */
public class ServerResultFunction<T> implements Function<RetrofitResponse<JsonElement>, RetrofitResponse<T>> {

    private final String TAG = getClass().getSimpleName();

    @SuppressWarnings("unchecked")
    @Override
    public RetrofitResponse<T> apply(@NonNull RetrofitResponse<JsonElement> response) throws Exception {
        //抛出服务器返回自定义异常
        if (!response.isSuccess()) {
            Log.e(TAG, new Gson().toJson(response));
            throw new ServerResultException(response.getCode(), response.getMessage());
        }
        return (RetrofitResponse<T>) response;
    }
}

