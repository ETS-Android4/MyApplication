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

    private static final String TAG = "ProgressResponseBody";

    private final String url;
    private final ResponseBody body;
    private final ResponseProgressListener listener;

    private BufferedSource bufferedSource;

    public ResponseProgressBody(String url, ResponseBody body, ResponseProgressListener listener) {
        this.url = url;
        this.body = body;
        this.listener = listener;
    }

    @Override
    public MediaType contentType() {
        return body.contentType();
    }

    @Override
    public long contentLength() {
        return body.contentLength();
    }

    @NonNull
    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(body.source()));
        }
        return bufferedSource;
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
            long contentLength = body.contentLength();
            if (count == -1) { // this source is exhausted
                bytesRead = contentLength;
            } else {
                bytesRead += count;
            }
            if (listener != null) {
                listener.onResponseProgress(url, bytesRead, contentLength);
            } else {
                Log.e(TAG, "url: " + url + "bytesRead: " + bytesRead + " , totalBytesCount: " + contentLength);
            }
            return count;
        }
    }
}