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

        private final WeakReference<OnReceiveMessageHandler> weakReference;

        /**
         * 使用必读：推荐在Activity或者Activity内部持有类中实现该接口，不要使用匿名类，可能会被GC
         *
         * @param handler 收到消息回调接口
         */
        @SuppressWarnings("deprecation")
        public HandlerHolder(OnReceiveMessageHandler handler) {
            weakReference = new WeakReference<>(handler);
        }

        public HandlerHolder(@NonNull Looper looper, OnReceiveMessageHandler handler) {
            super(looper);
            weakReference = new WeakReference<>(handler);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (weakReference.get() != null) {
                weakReference.get().handlerMessage(msg);
            }
        }
    }

    /**
     * 收到消息回调接口
     */
    public interface OnReceiveMessageHandler {
        /**
         * 处理消息
         *
         * @param msg
         */
        void handlerMessage(Message msg);
    }
}

