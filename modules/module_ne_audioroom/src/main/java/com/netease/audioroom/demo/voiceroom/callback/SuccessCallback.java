package com.netease.audioroom.demo.voiceroom.callback;

import com.netease.nimlib.sdk.RequestCallback;

public abstract class SuccessCallback<T> implements RequestCallback<T> {

    @Override
    public void onFailed(int code) {

    }

    @Override
    public void onException(Throwable exception) {

    }
}
