package com.netease.audioroom.demo.widget.loadsir.core;

import com.netease.audioroom.demo.widget.loadsir.callback.BaseCallback;

public interface Convertor<T> {
    Class<? extends BaseCallback> map(T t);
}
