package com.example.william.my.core.okhttp.body;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.william.my.core.okhttp.listener.ResponseProgressListener;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 下载进度
 */
public class ResponseProgressBody extends ResponseBody {

    private final String TAG = this.getClass().getSimpleName();

    private final String mUrl;
    private final ResponseBody mResponseBody;
    private final ResponseProgressListener mResponseProgressListener;

    private BufferedSource mBufferedSource;

    public ResponseProgressBody(String url, ResponseBody responseBody, ResponseProgressListener listener) {
        this.mUrl = url;
        this.mResponseBody = responseBody;
        this.mResponseProgressListener = listener;
    }

    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @NonNull
    @Override
    public BufferedSource source() {
        if (mBufferedSource == null) {
            mBufferedSource = Okio.buffer(source(mResponseBody.source()));
        }
        return mBufferedSource;
    }

    private Source source(Source source) {
        return new ProgressSource(source);
    }

    private class ProgressSource extends ForwardingSource {

        long bytesRead = 0L;

        public ProgressSource(Source delegate) {
            super(delegate);
        }

        @Override
        public long read(@NonNull Buffer sink, long byteCount) throws IOException {
            long count = super.read(sink, byteCount);
            long contentLength = mResponseBody.contentLength();
            if (count == -1) { // this source is exhausted
                bytesRead = contentLength;
            } else {
                bytesRead += count;
            }
            if (mResponseProgressListener != null) {
                mResponseProgressListener.onResponseProgress(mUrl, bytesRead, contentLength);
            } else {
                Log.d(TAG, "url: " + mUrl + "bytesRead: " + bytesRead + " , totalBytesCount: " + contentLength);
            }
            return count;
        }
    }
}