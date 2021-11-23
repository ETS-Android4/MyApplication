package com.example.william.my.core.okhttp.body;

import com.example.william.my.core.okhttp.listener.RequestProgressListener;
import com.example.william.my.core.okhttp.utils.OkHttpLog;

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
public class RequestProgressBody extends RequestBody {

    private final String TAG = this.getClass().getSimpleName();

    private final RequestBody mRequestBody;
    private final RequestProgressListener mRequestProgressListener;

    public RequestProgressBody(RequestBody requestBody, RequestProgressListener listener) {
        this.mRequestBody = requestBody;
        this.mRequestProgressListener = listener;
    }

    /**
     * 重写调用实际的响应体的contentType
     *
     * @return MediaType
     */
    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    /**
     * 重写调用实际的响应体的contentLength
     *
     * @return contentLength
     * @throws IOException 异常
     */
    @Override
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }

    /**
     * 重写writeTo
     *
     * @param sink BufferedSink
     * @throws IOException 异常
     */
    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        BufferedSink bufferedSink = Okio.buffer(sink(sink));
        mRequestBody.writeTo(bufferedSink);
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
                //增加当前写入的字节数
                bytesWritten += byteCount;
                //获得contentLength的值，后续不再调用
                if (totalBytesCount == 0) {
                    totalBytesCount = contentLength();
                }
                if (mRequestProgressListener != null) {
                    mRequestProgressListener.onProgress(bytesWritten, totalBytesCount);
                } else {
                    OkHttpLog.d(TAG, "bytesWritten: " + bytesWritten + " , totalBytesCount: " + totalBytesCount);
                }
            }
        };
    }
}
