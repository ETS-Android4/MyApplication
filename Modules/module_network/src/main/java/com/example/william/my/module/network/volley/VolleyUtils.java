package com.example.william.my.module.network.volley;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Volley请求工具类
 */
public class VolleyUtils {

    private static final String TAG = "Volley";

    public static <T> PostFormBuilder<T> builder() {
        return new PostFormBuilder<>();
    }

    public static class PostFormBuilder<T> {

        private String url;
        private Map<String, String> params;

        private Class<T> clazz;

        public PostFormBuilder<T> url(String url) {
            this.url = url;
            return this;
        }

        public PostFormBuilder<T> addParams(String key, String val) {
            if (this.params == null) {
                params = new LinkedHashMap<>();
            }
            params.put(key, val);
            return this;
        }

        public PostFormBuilder<T> clazz(Class<T> clazz) {
            this.clazz = clazz;
            return this;
        }

        public VolleyRequest<T> build(Context context) {
            return new VolleyRequest<>(url, params, clazz, context);
        }
    }

    public static class VolleyRequest<T> {

        private final String url;
        private final Map<String, String> params;

        private final Class<T> clazz;
        private final Context context;
        private RequestQueue volleyQueue;

        private SharedPreferences sharedPreferences;

        public VolleyRequest(String url, Map<String, String> params, Class<T> clazz, Context context) {
            this.url = url;
            this.params = params;
            this.clazz = clazz;
            this.context = context;
            if (volleyQueue == null)
                volleyQueue = Volley.newRequestQueue(context);
            if (sharedPreferences == null)
                sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
        }

        public void enqueue(@NonNull VolleyListener<T> listener) {
            volleyQueue.add(new JsonRequest<T>(Request.Method.POST, url, clazz, context,
                    listener.responseListener(), listener.errorListener()) {
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

    public static class JsonRequest<T> extends Request<T> {

        private final Class<T> mClazz;
        private final Response.Listener<T> mListener;

        private SharedPreferences sharedPreferences;

        private JsonRequest(int method, String url, Class<T> mClazz, Context mContext,
                            Response.Listener<T> mListener, Response.ErrorListener mErrorListener) {
            super(method, url, mErrorListener);
            this.mClazz = mClazz;
            this.mListener = mListener;
            if (sharedPreferences == null)
                sharedPreferences = mContext.getSharedPreferences("cookie", Context.MODE_PRIVATE);
        }

        protected void deliverResponse(T response) {
            if (this.mListener != null) {
                this.mListener.onResponse(response);
            }
        }

        protected Response<T> parseNetworkResponse(@NonNull NetworkResponse response) {
            try {
                String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                String temp_cookie = response.headers.get("Set-Cookie");
                if (temp_cookie != null) {
                    String cookie = temp_cookie.split(";")[0];
                    sharedPreferences.edit().putString("cookie", cookie).apply();
                }
                Log.e(TAG, json);
                return Response.success(new Gson().fromJson(json, mClazz), HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException | JsonSyntaxException e) {
                return Response.error(new ParseError(e));
            }
        }
    }

    public abstract static class VolleyListener<T> {

        Response.Listener<T> mListener;

        Response.ErrorListener mErrorListener;

        // 请求成功时的回调函数
        public abstract void onMySuccess(@NonNull T result);

        // 请求失败时的回调函数
        public abstract void onMyError(@NonNull String error);

        // 创建请求的事件监听
        Response.Listener<T> responseListener() {
            mListener = new Response.Listener<T>() {
                @Override
                public void onResponse(T t) {
                    onMySuccess(t);
                }
            };
            return mListener;
        }

        // 创建请求失败的事件监听
        Response.ErrorListener errorListener() {
            mErrorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    onMyError(volleyError.getMessage());
                }
            };
            return mErrorListener;
        }
    }
}
