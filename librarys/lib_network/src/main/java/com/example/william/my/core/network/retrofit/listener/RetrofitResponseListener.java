package com.example.william.my.core.network.retrofit.listener;

/**
 * 下载进度接口
 */
public interface RetrofitResponseListener {

    void onProgress(String url, long bytesRead, long contentLength);

}
