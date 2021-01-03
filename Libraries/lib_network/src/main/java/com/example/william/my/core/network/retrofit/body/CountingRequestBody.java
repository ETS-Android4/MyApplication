package com.example.william.my.core.network.retrofit.body;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.william.my.core.network.retrofit.listener.RetrofitRequestListener;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * 上传进度
 */
public class CountingRequestBody extends RequestBody {

    private static final String TAG = "CountingRequestBody";

    private final RequestBody body;
    private final RetrofitRequestListener listener;

    public CountingRequestBody(RequestBody body, RetrofitRequestListener listener) {
        this.body = body;
        this.listener = listener;
    }

    /**
     * 重写调用实际的响应体的contentType
     *
     * @return MediaType
     */
    @Override
    public MediaType contentType() {
        return body.contentType();
    }

    /**
     * 重写调用实际的响应体的contentLength
     *
     * @return contentLength
     * @throws IOException 异常
     */
    @Override
    public long contentLength() throws IOException {
        return body.contentLength();
    }

    /**
     * 重写writeTo
     *
     * @param sink BufferedSink
     * @throws IOException 异常
     */
    @Override
    public void writeTo(@NonNull BufferedSink sink) throws IOException {
        BufferedSink bufferedSink = Okio.buffer(sink(sink));
        body.writeTo(bufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();
    }

    /**
     * 写入，回调进度接口
     *
     * @param sink Sink
     * @return Sink
     */
    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            //当前写入字节数
            long bytesWritten = 0L;
            //总字节长度，避免多次调用contentLength()方法
            long totalBytesCount = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                //Log.e(TAG, source.readString(contentType().charset()));
                //增加当前写入的字节数
                bytesWritten += byteCount;
                //获得contentLength的值，后续不再调用
                if (totalBytesCount == 0) {
                    totalBytesCount = contentLength();
                }
                if (listener != null) {
                    listener.onProgress(bytesWritten, totalBytesCount);
                } else {
                    Log.e(TAG, "bytesWritten: " + bytesWritten + " , totalBytesCount: " + totalBytesCount);
                }
            }
        };
    }
}
