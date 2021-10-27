package com.example.william.my.core.retrofit.callback;

public abstract class ObserverCallback<Bean> implements RetrofitResponseCallback<Bean> {

    public ObserverCallback() {
        onLoading();
    }

    @Override
    public void onLoading() {

    }
}
