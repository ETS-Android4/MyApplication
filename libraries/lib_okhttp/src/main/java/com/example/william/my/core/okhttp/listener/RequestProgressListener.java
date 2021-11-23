package com.example.william.my.core.okhttp.listener;

/**
 * 上传进度接口
 */
public interface RequestProgressListener {

    /**
     * 上传进度
     *
     * @param bytesWritten
     * @param contentLength
     */
    void onProgress(long bytesWritten, long contentLength);
}
