package com.example.william.my.core.network.retrofit.listener;

/**
 * 上传进度接口
 */
public interface RetrofitRequestListener {

    void onProgress(long bytesWritten, long contentLength);

}
