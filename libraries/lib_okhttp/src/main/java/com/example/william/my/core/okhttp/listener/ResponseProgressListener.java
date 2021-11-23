package com.example.william.my.core.okhttp.listener;

/**
 * 下载进度接口
 */
public interface ResponseProgressListener {

    /**
     * 下载进度
     *
     * @param url
     * @param bytesRead
     * @param contentLength
     */
    void onResponseProgress(String url, long bytesRead, long contentLength);
}
