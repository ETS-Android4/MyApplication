package com.example.william.my.core.network.webSocket;

import android.os.SystemClock;
import android.util.Log;

import com.google.gson.Gson;

import java.io.EOFException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class RxWebSocketUtils {

    private static final String TAG = "RxWebSocketUtils";

    private final Map<String, WebSocket> webSocketMap;
    private final Map<String, Observable<RxWebSocketInfo>> observableMap;

    private static RxWebSocketUtils instance;

    public static RxWebSocketUtils getInstance() {
        if (instance == null) {
            synchronized (RxWebSocketUtils.class) {
                if (instance == null) {
                    instance = new RxWebSocketUtils();
                }
            }
        }
        return instance;
    }

    private RxWebSocketUtils() {
        webSocketMap = new ConcurrentHashMap<>();
        observableMap = new ConcurrentHashMap<>();
    }

    public Observable<RxWebSocketInfo> createWebSocket(final String url) {
        return createWebSocket(url, new Request.Builder().get().url(url).build(), new OkHttpClient());
    }

    public Observable<RxWebSocketInfo> createWebSocket(Request request) {
        if (request != null) {
            return createWebSocket(request.url().toString(), request, new OkHttpClient());
        } else {
            return null;
        }
    }

    @NonNull
    private Observable<RxWebSocketInfo> createWebSocket(String url, Request request) {
        return createWebSocket(url, request, new OkHttpClient());
    }

    public Observable<RxWebSocketInfo> createWebSocket(String url, OkHttpClient okHttpClient) {
        return createWebSocket(url, new Request.Builder().get().url(url).build(), okHttpClient);
    }

    public Observable<RxWebSocketInfo> createWebSocket(Request request, OkHttpClient okHttpClient) {
        if (request != null) {
            return createWebSocket(request.url().toString(), request, okHttpClient);
        } else {
            return null;
        }
    }

    @NonNull
    private Observable<RxWebSocketInfo> createWebSocket(final String url, final Request request, OkHttpClient okHttpClient) {
        Observable<RxWebSocketInfo> observable = observableMap.get(url);
        if (observable == null) {
            observable = Observable.create(new WebSocketOnSubscribe(url, request, okHttpClient))
                    .retry(new Predicate<Throwable>() {
                        @Override
                        public boolean test(Throwable throwable) throws Exception {
                            return throwable instanceof EOFException || throwable instanceof TimeoutException;
                        }
                    })
                    .doOnDispose(new Action() {
                        @Override
                        public void run() throws Exception {
                            observableMap.remove(url);
                            webSocketMap.remove(url);
                        }
                    })
                    .doOnNext(new Consumer<RxWebSocketInfo>() {
                        @Override
                        public void accept(RxWebSocketInfo webSocketInfo) throws Exception {
                            if (webSocketInfo.isOnOpen()) {
                                webSocketMap.put(url, webSocketInfo.getWebSocket());
                            }
                        }
                    })
                    .share()//共享，保证执行在同一个Observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            ;
            observableMap.put(request.url().toString(), observable);
        } else {
            WebSocket webSocket = webSocketMap.get(request.url().toString());
            if (webSocket != null) {
                observable = observable.startWithItem(new RxWebSocketInfo(webSocket, null, true));
            }
        }
        return observable.observeOn(AndroidSchedulers.mainThread());
    }

    public void send(String url, final String message) {
        send(url, new Request.Builder().get().url(url).build(), message);
    }

    public void send(Request request, final String message) {
        if (request != null) {
            send(request.url().toString(), request, message);
        }
    }

    private void send(String url, Request request, final String message) {
        createWebSocket(url, request)
                .subscribe(new RxWebSocketObserver() {
                    @Override
                    protected void onMessage(WebSocket webSocket, String text) {
                        super.onMessage(webSocket, text);
                        webSocket.send(message);
                    }
                });
    }

    public void cancel(String url) {
        cancel(url, new Request.Builder().get().url(url).build());
    }

    public void cancel(Request request) {
        if (request != null) {
            cancel(request.url().toString(), request);
        }
    }

    private void cancel(final String url, Request request) {
        WebSocket webSocket = webSocketMap.get(url);
        if (webSocket != null) {
            webSocket.cancel();
        }
        getDisposable(url, request).dispose();
    }

    public Disposable getDisposable(String url) {
        return createWebSocket(url).subscribe();
    }

    public Disposable getDisposable(Request request) {
        return createWebSocket(request).subscribe();
    }

    private Disposable getDisposable(final String url, Request request) {
        return createWebSocket(url, request).subscribe();
    }

    private class WebSocketOnSubscribe implements ObservableOnSubscribe<RxWebSocketInfo> {

        private final String url;
        private final Request request;
        private final OkHttpClient okHttpClient;

        private WebSocket webSocket;

        public WebSocketOnSubscribe(String url, Request request, OkHttpClient okHttpClient) {
            this.url = url;
            this.request = request;
            this.okHttpClient = okHttpClient;
        }

        @Override
        public void subscribe(@NonNull final ObservableEmitter<RxWebSocketInfo> emitter) throws Exception {
            if (webSocket != null) {
                //降低重连频率
                if (!"main".equals(Thread.currentThread().getName())) {
                    SystemClock.sleep(3000);
                    emitter.onNext(new RxWebSocketInfo(true));
                }
            }
            webSocket = okHttpClient.newWebSocket(request, new WebSocketListener() {
                @Override
                public void onOpen(final @NonNull WebSocket webSocket, @NonNull Response response) {
                    webSocketMap.put(url, webSocket);
                    if (!emitter.isDisposed()) {
                        emitter.onNext(new RxWebSocketInfo(webSocket, null, true));
                    }
                    Log.i(TAG, "onOpen：" + url);
                }

                @Override
                public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                    super.onMessage(webSocket, text);
                    if (!emitter.isDisposed()) {
                        emitter.onNext(new RxWebSocketInfo(webSocket, text));
                    }
                    Log.i(TAG, "onMessageString：" + text);
                }

                @Override
                public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
                    super.onMessage(webSocket, bytes);
                    if (!emitter.isDisposed()) {
                        emitter.onNext(new RxWebSocketInfo(webSocket, bytes));
                    }
                    Log.i(TAG, "onMessageByteString：" + bytes.toString());
                }


                @Override
                public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                    super.onClosing(webSocket, code, reason);
                    Log.i(TAG, "onClosing:" + "code:" + code + "reason:" + reason);
                }

                @Override
                public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                    super.onClosed(webSocket, code, reason);
                    emitter.onNext(new RxWebSocketInfo(false, true));
                    Log.i(TAG, "onClosed:" + "code:" + code + "reason:" + reason);
                }

                @Override
                public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, Response response) {
                    super.onFailure(webSocket, t, response);
                    if (!emitter.isDisposed()) {
                        emitter.onError(t);
                    }
                    if (response != null) {
                        Log.e(TAG, "onFailure：" + response.code());
                        Log.e(TAG, "onFailure：" + new Gson().toJson(response.body()));
                    }
                    Log.e(TAG, "Throwable:" + t.toString());
                }
            });
        }
    }
}
