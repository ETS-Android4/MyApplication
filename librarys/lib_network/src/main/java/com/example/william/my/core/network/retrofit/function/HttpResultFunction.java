package com.example.william.my.core.network.retrofit.function;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.william.my.core.network.retrofit.exception.ExceptionHandler;

import java.util.Objects;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;

/**
 * 统一异常管理
 */
public class HttpResultFunction<T> implements Function<Throwable, ObservableSource<T>> {

    @Override
    public ObservableSource<T> apply(@NonNull Throwable throwable) throws Exception {
        Log.e("HttpResult", Objects.requireNonNull(throwable.getMessage()));
        return Observable.error(ExceptionHandler.handleException(throwable));
    }
}
