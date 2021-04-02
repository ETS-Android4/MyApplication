package com.example.william.my.core.network.retrofit.interceptor;

import android.util.Log;

import androidx.annotation.NonNull;

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
public class RetrofitInterceptorLogging implements Interceptor {

    private final String TAG;

    public RetrofitInterceptorLogging(String tag) {
        TAG = tag;
    }

    private Level level = Level.BASIC;

    public enum Level {
        BASIC,
        BODY
    }

    public RetrofitInterceptorLogging setLevel(Level level) {
        if (level == null) throw new NullPointerException("level == null. Use Level.NONE instead.");
        this.level = level;
        return this;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Connection connection = chain.connection();
        Log.i(TAG, "--> "
                + request.method()
                + ' ' + request.url()
                + (connection != null ? " " + connection.protocol() : ""));
        //========================================
        Request.Builder builder = request.newBuilder();
        RequestBody requestBody = request.body();
        if (level == Level.BODY) {
            if (requestBody != null) {
                if (requestBody.contentType() != null) {
                    Log.i(TAG, "Content-Type: " + requestBody.contentType() + "-byte)");
                }
                if (requestBody.contentLength() != -1) {
                    Log.i(TAG, "Content-Length: " + requestBody.contentLength());
                }
                Log.i(TAG, "--> END " + request.method() + " (" + requestBody.contentLength() + "-byte body)");
            }
        }
        //========================================
        Headers headers_request = request.headers();
        if (level == Level.BODY) {
            for (int i = 0, count = headers_request.size(); i < count; i++) {
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equalsIgnoreCase(headers_request.name(i)) && !"Content-Length".equalsIgnoreCase(headers_request.name(i))) {
                    Log.i(TAG, headers_request.name(i) + ": " + headers_request.value(i));
                }
            }
        }
        //========================================
        Response response = chain.proceed(builder.build());
        long startNs = System.nanoTime();
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        Log.i(TAG, "<-- "
                + response.code()
                + (response.message().isEmpty() ? "" : ' ' + response.message())
                + ' ' + response.request().url()
                + " (" + tookMs + "ms" + ')');
        //========================================
        Headers headers_response = response.headers();
        if (level == Level.BODY) {
            for (int i = 0, count = headers_response.size(); i < count; i++) {
                Log.i(TAG, headers_response.name(i) + ": " + headers_response.value(i));
            }
        }
        //========================================
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.getBuffer();
            //========================================
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                Charset charset = contentType.charset(StandardCharsets.UTF_8);
                if (charset != null) {
                    Log.i(TAG, buffer.clone().readString(charset));
                }
            }
            if (level == Level.BODY) {
                Log.i(TAG, "<-- END HTTP (" + buffer.size() + "-byte body)");
            }
        }

        return response;
    }
}
