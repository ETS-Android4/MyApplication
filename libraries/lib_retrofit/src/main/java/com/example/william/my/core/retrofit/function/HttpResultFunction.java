package com.example.william.my.core.retrofit.function;

import androidx.annotation.NonNull;

import com.example.william.my.core.okhttp.utils.OkHttpLog;
import com.example.william.my.core.retrofit.exception.ExceptionHandler;

import java.util.Objects;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.rxjava3.functions.Function;

/**
 * 统一异常管理
 */
public class HttpResultFunction<T> implements Function<Throwable, SingleSource<T>> {

    private final String TAG = this.getClass().getSimpleName();

    @Override
    public SingleSource<T> apply(@NonNull Throwable throwable) throws Exception {
        OkHttpLog.e(TAG, Objects.requireNonNull(throwable.getMessage()));
        return Single.error(ExceptionHandler.handleException(throwable));
    }
}
