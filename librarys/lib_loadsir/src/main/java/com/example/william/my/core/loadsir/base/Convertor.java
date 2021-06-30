package com.example.william.my.core.loadsir.base;

import com.example.william.my.core.loadsir.callback.BaseCallback;

public interface Convertor<T> {
    Class<? extends BaseCallback> map(T t);
}
