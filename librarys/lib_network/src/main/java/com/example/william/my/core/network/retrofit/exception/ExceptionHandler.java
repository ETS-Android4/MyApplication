package com.example.william.my.core.network.retrofit.exception;

import android.net.ParseException;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.william.my.core.network.base.BaseBean;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Objects;

import javax.net.ssl.SSLHandshakeException;

import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * ApiException
 */
public class ExceptionHandler {

    private static final int UNAUTHORIZED = 401;// 没有权限
    private static final int FORBIDDEN = 403;// 禁止访问
    private static final int NOT_FOUND = 404;// 找不到资源
    private static final int REQUEST_TIMEOUT = 408;// 请求超时
    private static final int INTERNAL_SERVER_ERROR = 500; // 服务器错误
    private static final int BAD_GATEWAY = 502; // 错误网关
    private static final int SERVICE_UNAVAILABLE = 503;// 服务不可用
    private static final int GATEWAY_TIMEOUT = 504;// 网关超时

    @NonNull
    public static ApiException handleException(Throwable e) {
        ApiException ex;
        if (e instanceof HttpException) {//HTTP错误
            HttpException httpException = (HttpException) e;
            ex = new ApiException(e, ERROR.HTTP_ERROR);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ex.setCode(httpException.code());
                    ex.setMessage("网络出错，请稍后再试");
                    break;
            }

            ResponseBody body = Objects.requireNonNull(((HttpException) e).response()).errorBody();
            try {
                if (body != null) {
                    ErrorBean error = new Gson().fromJson(body.string(), ErrorBean.class);
                    ex.setMessage(error.getMessage() != null ? error.getMessage() : "请求网络失败，请检查您的网络设置或稍后重试！");
                }
            } catch (Exception e1) {
                Log.e("HttpException", ((HttpException) e).message());
                ex.setMessage("请求网络失败，请检查您的网络设置或稍后重试！");
            }
            return ex;
        } else if (e instanceof ServerResultException) {
            ServerResultException resultException = (ServerResultException) e;
            ex = new ApiException(resultException, resultException.getCode());
            ex.setMessage(resultException.getMessage());
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


    public static class ErrorBean extends BaseBean {

        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}


