package com.example.william.my.core.okhttp.interceptor;

import com.example.william.my.core.okhttp.utils.OkHttpLog;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 日志拦截器
 */
public class InterceptorLogging implements Interceptor {

    private final String TAG;

    public InterceptorLogging(String tag) {
        TAG = tag;
    }

    private Level level = Level.BASIC;

    public enum Level {
        /**
         * 请求基本信息
         */
        BASIC,
        /**
         * 请求响应主体
         */
        BODY
    }

    public InterceptorLogging setLevel(Level level) {
        if (level == null) {
            throw new NullPointerException("level == null. Use Level.NONE instead.");
        }
        this.level = level;
        return this;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Connection connection = chain.connection();
        showLog(TAG, "--> "
                + request.method()
                + ' ' + request.url()
                + (connection != null ? " " + connection.protocol() : ""));
        //========================================
        Request.Builder builder = request.newBuilder();
        RequestBody requestBody = request.body();
        if (level == Level.BODY) {
            if (requestBody != null) {
                if (requestBody.contentType() != null) {
                    showLog(TAG, "Content-Type: " + requestBody.contentType() + "-byte)");
                }
                if (requestBody.contentLength() != -1) {
                    showLog(TAG, "Content-Length: " + requestBody.contentLength());
                }
                showLog(TAG, "--> END " + request.method() + " (" + requestBody.contentLength() + "-byte body)");
            }
        }
        //========================================
        Headers headersRequest = request.headers();
        if (level == Level.BODY) {
            for (int i = 0, count = headersRequest.size(); i < count; i++) {
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equalsIgnoreCase(headersRequest.name(i)) &&
                        !"Content-Length".equalsIgnoreCase(headersRequest.name(i))) {
                    showLog(TAG, headersRequest.name(i) + ": " + headersRequest.value(i));
                }
            }
        }
        //========================================
        Response response = chain.proceed(builder.build());
        long startNs = System.nanoTime();
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        showLog(TAG, "<-- "
                + response.code()
                + (response.message().isEmpty() ? "" : ' ' + response.message())
                + ' ' + response.request().url()
                + " (" + tookMs + "ms" + ')');
        //========================================
        Headers headersResponse = response.headers();
        if (level == Level.BODY) {
            for (int i = 0, count = headersResponse.size(); i < count; i++) {
                showLog(TAG, headersResponse.name(i) + ": " + headersResponse.value(i));
            }
        }
        //========================================
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            BufferedSource source = responseBody.source();
            // Buffer the entire body.
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.getBuffer();
            //========================================
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                Charset charset = contentType.charset(StandardCharsets.UTF_8);
                if (charset != null) {
                    showLog(TAG, buffer.clone().readString(charset));
                }
            }
            if (level == Level.BODY) {
                showLog(TAG, "<-- END HTTP (" + buffer.size() + "-byte body)");
            }
        }

        return response;
    }

    private static void showLog(String tag, String msg) {
        int maxLength = 2 * 1024;
        while (msg.length() > maxLength) {
            OkHttpLog.i(tag, msg.substring(0, maxLength));
            msg = msg.substring(maxLength);
        }
        OkHttpLog.i(tag, msg);
    }
}
