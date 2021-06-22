package com.example.william.my.core.retrofit.callback;

import com.example.william.my.core.retrofit.status.State;

/**
 * 携带状态 {@link State} 的 Observer
 */
public abstract class ObserverCallback<Bean> implements RetrofitResponseCallback<Bean> {

    public ObserverCallback() {
        onLoading();
    }

    @Override
    public void onLoading() {

    }
}
