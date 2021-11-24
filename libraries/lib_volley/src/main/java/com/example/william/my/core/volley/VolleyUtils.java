package com.example.william.my.core.volley;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Volley请求工具类
 */
public class VolleyUtils {

    private static RequestQueue volleyQueue;
    private static SharedPreferences sharedPreferences;

    public static <T> PostFormBuilder<T> builder() {
        return new PostFormBuilder<>();
    }

    public static class PostFormBuilder<T> {

        private int method;
        private String url;
        private Class<T> clazz;
        private Map<String, String> headers;
        private Map<String, String> params;

        public PostFormBuilder<T> url(String url) {
            this.url = url;
            return this;
        }

        public PostFormBuilder<T> clazz(Class<T> clazz) {
            this.clazz = clazz;
            return this;
        }

        public PostFormBuilder<T> addHeaders(String key, String val) {
            if (this.headers == null) {
                headers = new HashMap<>();
            }
            headers.put(key, val);
            return this;
        }

        public PostFormBuilder<T> addParams(String key, String val) {
            if (this.params == null) {
                params = new HashMap<>();
            }
            params.put(key, val);
            return this;
        }

        public PostFormBuilder<T> get() {
            this.method = Method.GET;
            return this;
        }

        public PostFormBuilder<T> post() {
            this.method = Method.POST;
            return this;
        }

        public VolleyRequest<T> build(Context context) {
            if (volleyQueue == null) {
                volleyQueue = VolleySingleton.Companion.getInstance(context).getRequestQueue();
            }
            if (sharedPreferences == null) {
                sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
            }
            return new VolleyRequest<>(method, url, clazz, headers, params);
        }
    }

    public static class VolleyRequest<T> {

        private final int method;
        private final String url;
        private final Class<T> clazz;
        private final Map<String, String> headers;
        private final Map<String, String> params;

        public VolleyRequest(int method, String url, Class<T> clazz, Map<String, String> headers, Map<String, String> params) {
            this.method = method;
            this.url = url;
            this.clazz = clazz;
            this.headers = headers;
            this.params = params;
        }

        public void enqueue(VolleyUtils.ResponseListener<T> responseListener) {
            volleyQueue.add(new GsonRequest<T>(method, url, clazz, headers, params,
                    responseListener.mListener, responseListener.mErrorListener) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    String cookie = sharedPreferences.getString("cookie", "");
                    if (!TextUtils.isEmpty(cookie)) {
                        HashMap<String, String> headers = new HashMap<>();
                        headers.put("Cookie", cookie);
                        return headers;
                    }
                    return super.getHeaders();
                }

                @Override
                protected Map<String, String> getParams() {
                    return params;
                }
            });
        }
    }

    public static class GsonRequest<T> extends Request<T> {

        private final Gson gson = new Gson();
        private final Class<T> clazz;
        private final Map<String, String> headers;
        private final Map<String, String> params;
        private final Response.Listener<T> listener;


        public GsonRequest(int method, String url, Class<T> clazz, Map<String, String> headers, Map<String, String> params,
                           Response.Listener<T> listener, Response.ErrorListener errorListener) {
            super(method, url, errorListener);
            this.clazz = clazz;
            this.headers = headers;
            this.params = params;
            this.listener = listener;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            return headers != null ? headers : super.getHeaders();
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return params != null ? params : super.getParams();
        }

        @Override
        protected void deliverResponse(T response) {
            listener.onResponse(response);
        }

        @Override
        protected Response<T> parseNetworkResponse(NetworkResponse response) {
            try {
                Map<String, String> headers = response.headers;
                if (headers != null) {
                    String tempCookie = headers.get("Set-Cookie");
                    if (tempCookie != null) {
                        String cookie = tempCookie.split(";")[0];
                        sharedPreferences.edit().putString("cookie", cookie).apply();
                    }
                }
                String json = new String(response.data, HttpHeaderParser.parseCharset(headers));
                return Response.success(gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException | JsonSyntaxException e) {
                return Response.error(new ParseError(e));
            }
        }
    }

    public static abstract class ResponseListener<T> implements ResponseCallback<T> {

        /**
         * 创建请求的事件监听
         */
        public Response.Listener<T> mListener = new Response.Listener<T>() {
            @Override
            public void onResponse(T t) {
                onSuccess(t);
            }
        };

        /**
         * 创建请求失败的事件监听
         */
        public Response.ErrorListener mErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() != null) {
                    onFailure(error.getMessage());
                } else {
                    onFailure("请求网络失败，请检查您的网络设置或稍后重试！");
                }
            }
        };
    }

    public interface ResponseCallback<T> {

        /**
         * onResponse
         *
         * @param response
         */
        void onSuccess(T response);

        /**
         * onFailure
         *
         * @param error
         */
        void onFailure(String error);
    }

}
