package com.example.william.my.library.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

/**
 * Handler弱引用工具类
 */
public class HandlerUtils {

    private HandlerUtils() {
        throw new UnsupportedOperationException("Guy, r u crazy? u can NOT instantiate me...");
    }

    public static class HandlerHolder extends Handler {

        private final WeakReference<OnReceiveMessageListener> mListenerWeakReference;

        /**
         * 使用必读：推荐在Activity或者Activity内部持有类中实现该接口，不要使用匿名类，可能会被GC
         *
         * @param listener 收到消息回调接口
         */
        @SuppressWarnings("deprecation")
        public HandlerHolder(OnReceiveMessageListener listener) {
            mListenerWeakReference = new WeakReference<>(listener);
        }

        public HandlerHolder(@NonNull Looper looper, OnReceiveMessageListener listener) {
            super(looper);
            mListenerWeakReference = new WeakReference<>(listener);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (mListenerWeakReference.get() != null) {
                mListenerWeakReference.get().handlerMessage(msg);
            }
        }
    }

    /**
     * 收到消息回调接口
     */
    public interface OnReceiveMessageListener {
        void handlerMessage(Message msg);
    }
}

