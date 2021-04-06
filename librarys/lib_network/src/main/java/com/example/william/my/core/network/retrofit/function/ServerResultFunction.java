package com.example.william.my.core.network.retrofit.function;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.william.my.core.network.retrofit.response.RetrofitResponse;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import io.reactivex.rxjava3.functions.Function;

/**
 * 服务器返回结果，泛型转换(JsonElement -> 泛型)
 */
public class ServerResultFunction<T> implements Function<RetrofitResponse<JsonElement>, RetrofitResponse<T>> {

    @SuppressWarnings("unchecked")
    @Override
    public RetrofitResponse<T> apply(@NonNull RetrofitResponse<JsonElement> response) throws Exception {
        //抛出服务器返回自定义异常
        if (!response.isSuccess()) {
            Log.e("ServerResult", new Gson().toJson(response));
            //throw new ServerResultException(response.getStatus(), response.getMessage());
        }
        return (RetrofitResponse<T>) response;
    }
}

