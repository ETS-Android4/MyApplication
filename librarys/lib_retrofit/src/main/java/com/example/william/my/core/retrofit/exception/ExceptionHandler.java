package com.example.william.my.core.retrofit.exception;

import android.net.ParseException;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import javax.net.ssl.SSLHandshakeException;

import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * ApiException
 */
public class ExceptionHandler {

    @NonNull
    public static ApiException handleException(Throwable e) {
        ApiException ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ApiException(e, ERROR.HTTP_ERROR);
            ex.setCode(httpException.code());
            try {
                Response<?> response = ((HttpException) e).response();
                if (response != null) {
                    ResponseBody body = response.errorBody();
                    if (body != null) {
                        HttpException exception = new Gson().fromJson(body.string(), HttpException.class);
                        ex.setMessage(exception.getMessage() != null ?
                                exception.getMessage() : "请求网络失败，请检查您的网络设置或稍后重试！");
                    } else {
                        ex.setMessage("请求网络失败，请检查您的网络设置或稍后重试！");
                    }
                } else {
                    ex.setMessage("请求网络失败，请检查您的网络设置或稍后重试！");
                }
            } catch (Exception e1) {
                ex.setMessage("请求网络失败，请检查您的网络设置或稍后重试！");
            }
            return ex;
        } else if (e instanceof ServerResultException) {
            ServerResultException exception = (ServerResultException) e;
            ex = new ApiException(exception, exception.getCode());
            ex.setMessage(exception.getMessage());
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ApiException(e, ERROR.PARSE_ERROR);
            ex.setMessage("解析错误，请稍后再试");
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ApiException(e, ERROR.NETWORK_ERROR);
            ex.setMessage("连接失败，请稍后再试");
            return ex;
        } else if (e instanceof SocketTimeoutException) {
            ex = new ApiException(e, ERROR.CONNECT_ERROR);
            ex.setMessage("连接超时，请稍后再试");
            return ex;
        } else if (e instanceof SSLHandshakeException) {
            ex = new ApiException(e, ERROR.SSL_ERROR);
            ex.setMessage("证书验证失败，请稍后再试");
            return ex;
        } else {
            ex = new ApiException(e, ERROR.UNKNOWN);
            ex.setMessage("未知错误，请稍后再试");
            return ex;
        }
    }

    public static class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;

        /**
         * 协议出错
         */
        private static final int HTTP_ERROR = 1001;

        /**
         * 解析错误
         */
        private static final int PARSE_ERROR = 1002;

        /**
         * 网络错误
         */
        private static final int NETWORK_ERROR = 1003;

        /**
         * 连接超时
         */
        private static final int CONNECT_ERROR = 1004;

        /**
         * 证书出错
         */
        private static final int SSL_ERROR = 1005;
    }
}


