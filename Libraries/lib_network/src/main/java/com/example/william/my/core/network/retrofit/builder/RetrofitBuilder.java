package com.example.william.my.core.network.retrofit.builder;

import androidx.lifecycle.LifecycleOwner;

import com.example.william.my.core.network.retrofit.method.Method;
import com.example.william.my.core.network.retrofit.response.RetrofitResponse;
import com.example.william.my.core.network.retrofit.utils.RxRetrofit;
import com.trello.lifecycle4.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle4.LifecycleTransformer;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.MultipartBody;

public class RetrofitBuilder<T> {

    private String api;
    private Method method;
    private Map<String, Object> header = new LinkedHashMap<>();
    private Map<String, Object> parameter = new LinkedHashMap<>();

    private boolean hasBody;

    private boolean isJson = true;
    private String bodyString;

    //public Map<String, File> fileMap;
    private MultipartBody.Builder bodyForm;

    private LifecycleTransformer<RetrofitResponse<T>> transformer;

    public String getApi() {
        return api;
    }

    public Method getMethod() {
        return method;
    }

    public Map<String, Object> getHeader() {
        return header;
    }

    public Map<String, Object> getParameter() {
        return parameter;
    }

    public boolean hasBody() {
        return hasBody;
    }

    public boolean isJson() {
        return isJson;
    }

    public String getBodyString() {
        return bodyString;
    }

    public MultipartBody.Builder getBodyForm() {
        return bodyForm;
    }

    public LifecycleTransformer<RetrofitResponse<T>> getTransformer() {
        return transformer;
    }

    public RetrofitBuilder<T> api(String api) {
        this.api = api;
        return this;
    }

    public RetrofitBuilder<T> get() {
        this.method = Method.GET;
        return this;
    }

    public RetrofitBuilder<T> post() {
        this.method = Method.POST;
        return this;
    }

    public RetrofitBuilder<T> delete() {
        this.method = Method.DELETE;
        return this;
    }

    public RetrofitBuilder<T> put() {
        this.method = Method.PUT;
        return this;
    }

    public RetrofitBuilder<T> addHeader(String key, Object value) {
        if (this.header == null) {
            this.header = new LinkedHashMap<>();
        }
        this.header.put(key, value);
        return this;
    }

    public RetrofitBuilder<T> addParams(String key, Object value) {
        if (this.parameter == null) {
            this.parameter = new LinkedHashMap<>();
        }
        this.parameter.put(key, value);
        return this;
    }

    public RetrofitBuilder<T> setRequestBody(String value, boolean isJson) {
        this.hasBody = true;
        this.isJson = isJson;
        this.bodyString = value;
        return this;
    }

    public RetrofitBuilder<T> addRequestBody(String key, String value) {
        if (this.bodyForm == null) {
            this.bodyForm = new MultipartBody.Builder().setType(MultipartBody.FORM);
        }
        this.hasBody = true;
        this.bodyForm = bodyForm.addFormDataPart(key, value);
        return this;
    }

    //public RetrofitBuilder<T> addFile(String key, File file) {
    //    //this.bodyPart = MultipartBody.Part.createFormData(
    //    //        key,
    //    //        file.getName(),
    //    //        RequestBody.create(MediaType.parse("multipart/form-data"), file)
    //    //);
    //    //this.bodyPart = MultipartBody.Part.createFormData(
    //    //        key,
    //    //        file.getName(),
    //    //        new CountingRequestBody(RequestBody.create(MediaType.parse("multipart/form-data"), file), listener));
    //    if (this.fileMap == null) {
    //        fileMap = new IdentityHashMap<>();
    //    }
    //    this.fileMap.put(key, file);
    //    return this;
    //}

    public RetrofitBuilder<T> setProvider(LifecycleOwner owner) {
        this.transformer = AndroidLifecycle.createLifecycleProvider(owner).bindToLifecycle();
        return this;
    }

    public RxRetrofit<T> build() {
        return new RxRetrofit<>(this);
    }
}