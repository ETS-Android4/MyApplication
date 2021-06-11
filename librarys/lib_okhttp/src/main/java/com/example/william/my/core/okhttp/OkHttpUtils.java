package com.example.william.my.core.okhttp;

import com.example.william.my.core.okhttp.body.RequestProgressBody;
import com.example.william.my.core.okhttp.listener.RequestProgressListener;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class OkHttpUtils {

    /**
     * 构建携带进度的RequestBody
     *
     * @param file
     * @param listener
     * @return
     */
    public static RequestBody buildRequestProgressBody(String key, File file, RequestProgressListener listener) {
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody multipartBody = multipartBuilder
                .addFormDataPart(
                        key,
                        file.getName(),
                        RequestBody.Companion.create(file, MediaType.parse("multipart/form-data")))
                .build();
        return new RequestProgressBody(multipartBody, listener);
    }
}
