package com.example.william.my.core.network.webSocket;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;

public abstract class RxWebSocketObserver implements Observer<RxWebSocketInfo> {

    private boolean hasOpened;

    private Disposable disposable;

    @Override
    public void onNext(@NonNull RxWebSocketInfo webSocketInfo) {
        if (webSocketInfo.isOnOpen()) {
            hasOpened = true;
            onOpen(webSocketInfo.getWebSocket(), webSocketInfo.getResponse());
        } else if (webSocketInfo.getString() != null) {
            onMessage(webSocketInfo.getWebSocket(), webSocketInfo.getString());
        } else if (webSocketInfo.getBytes() != null) {
            onMessage(webSocketInfo.getWebSocket(), webSocketInfo.getBytes());
        } else if (webSocketInfo.isOnReconnect()) {
            onReconnect();
        } else if (webSocketInfo.isOnClosed()) {
            onClosed();
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {
        if (hasOpened) {
            if (disposable != null) {
                disposable.dispose();
            }
        }
    }

    @Override
    public void onSubscribe(@NonNull Disposable disposable) {
        this.disposable = disposable;
    }

    protected void onOpen(WebSocket webSocket, Response response) {
    }

    protected void onMessage(WebSocket webSocket, String text) {
    }

    protected void onMessage(WebSocket webSocket, ByteString bytes) {
    }

    protected void onReconnect() {
    }

    protected void onClosed() {
    }
}
