package com.example.william.my.core.network.webSocket;

import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;

public class RxWebSocketInfo {

    private WebSocket mWebSocket;

    private Response mResponse;

    private String mString;
    private ByteString mBytes;

    private boolean onOpen;
    private boolean onReconnect;
    private boolean onClosed;

    public RxWebSocketInfo(boolean onReconnect) {
        this.onReconnect = onReconnect;
    }

    public RxWebSocketInfo(boolean onReconnect, boolean onClosed) {
        this.onReconnect = onReconnect;
        this.onClosed = onClosed;
    }

    public RxWebSocketInfo(WebSocket mWebSocket, Response mResponse, boolean onOpen) {
        this.mWebSocket = mWebSocket;
        this.mResponse = mResponse;
        this.onOpen = onOpen;
    }

    public RxWebSocketInfo(WebSocket mWebSocket, String mString) {
        this.mWebSocket = mWebSocket;
        this.mString = mString;
    }

    public RxWebSocketInfo(WebSocket mWebSocket, ByteString mBytes) {
        this.mWebSocket = mWebSocket;
        this.mBytes = mBytes;
    }

    public WebSocket getWebSocket() {
        return mWebSocket;
    }

    public Response getResponse() {
        return mResponse;
    }

    public String getString() {
        return mString;
    }

    public ByteString getBytes() {
        return mBytes;
    }

    public boolean isOnOpen() {
        return onOpen;
    }

    public boolean isOnReconnect() {
        return onReconnect;
    }

    public boolean isOnClosed() {
        return onClosed;
    }
}
