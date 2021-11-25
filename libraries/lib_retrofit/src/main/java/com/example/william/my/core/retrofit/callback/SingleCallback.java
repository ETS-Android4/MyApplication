package com.example.william.my.core.retrofit.callback;

public abstract class SingleCallback<Bean> implements ResponseCallback<Bean> {

    public SingleCallback() {
        onLoading();
    }

    @Override
    public void onLoading() {

    }
}
