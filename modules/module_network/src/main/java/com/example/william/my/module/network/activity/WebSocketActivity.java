package com.example.william.my.module.network.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.websocket.RxWebSocketObserver;
import com.example.william.my.core.websocket.RxWebSocketUtils;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;
import com.google.gson.Gson;

import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

@Route(path = ARouterPath.NetWork.NetWork_WebSocket)
public class WebSocketActivity extends BaseResponseActivity {

    private final String url = "wss://echo.websocket.org";

    private OkHttpClient mOkHttpClient;

    @Override
    public void initView() {
        super.initView();

        mOkHttpClient = new OkHttpClient();
    }

    @Override
    public void setOnClick() {
        super.setOnClick();
        webSocketUtils();
    }

    private void webSocket() {
        Request request = new Request.Builder()
                .url(url)
                .build();
        mOkHttpClient.newWebSocket(request, new WebSocketListener() {
            /**
             * 和远程建立连接时回调
             */
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                showResponse("onOpen：" + response.code());
            }

            /**
             * 接收到消息时回调
             */
            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                webSocket.send("heart");
                showResponse("onMessageString：" + text);
            }

            /**
             * 接收到消息时回调
             */
            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
                showResponse("onMessageByteString：" + bytes.toString());
            }

            /**
             * 准备关闭时回调
             */
            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                showResponse("onClosing:" + "code:" + code + "reason:" + reason);
            }

            /**
             * 连接关闭时回调
             */
            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                showResponse("onClosed:" + "code:" + code + "reason:" + reason);
            }

            /**
             * 失败时被回调
             */
            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                StringBuilder builder = new StringBuilder("onFailure:");
                if (t.getMessage() != null) {
                    builder.append("Throwable:").append(t.getMessage());
                }
                if (response != null) {
                    builder.append("onFailure：").append(response.code());
                    builder.append("onFailure：").append(new Gson().toJson(response.body()));
                }
                showResponse(builder.toString());
            }
        });
    }

    private void webSocketUtils() {
        RxWebSocketUtils
                .getInstance()
                .createWebSocket(url)
                .subscribe(new RxWebSocketObserver() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        super.onSubscribe(disposable);
                    }

                    @Override
                    protected void onOpen(WebSocket webSocket, Response response) {
                        super.onOpen(webSocket, response);
                        showResponse("onOpen：" + response.code());
                    }

                    @Override
                    protected void onMessage(WebSocket webSocket, String text) {
                        super.onMessage(webSocket, text);
                        webSocket.send("heart");
                        showResponse("onMessageString：" + text);
                    }

                    @Override
                    protected void onMessage(WebSocket webSocket, ByteString bytes) {
                        super.onMessage(webSocket, bytes);
                        showResponse("onMessageByteString：" + bytes.toString());
                    }

                    @Override
                    protected void onReconnect() {
                        super.onReconnect();
                    }

                    @Override
                    protected void onClosed() {
                        super.onClosed();
                    }
                });
    }
}
