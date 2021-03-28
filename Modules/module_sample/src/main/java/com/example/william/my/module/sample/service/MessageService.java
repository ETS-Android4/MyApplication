package com.example.william.my.module.sample.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

public class MessageService extends Service {

    private Messenger mServiceMessenger;//Service自身的Messenger

    private Messenger mClientMessenger;//Activity客户端的Messenger，通过客户端的Message的replyTo属性获得

    /**
     * 处理从客户端收到的消息
     */
    private static class ServiceHandler extends Handler {

        private final WeakReference<MessageService> softReference;

        ServiceHandler(@NonNull Looper looper, MessageService service) {
            super(looper);
            this.softReference = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            Message message = Message.obtain();

            Bundle bundle = new Bundle();
            String value = msg.getData().getString("KEY");
            bundle.putString("KEY", "收到客户端信息：" + value);

            //发送消息到客户端
            message.setData(bundle);

            //接收从客户端Messenger，以便服务端向客户端发送消息
            softReference.get().mClientMessenger = msg.replyTo;

            try {
                //向客户端发送消息
                softReference.get().mClientMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mServiceMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceHandler mServiceHandler = new ServiceHandler(Looper.getMainLooper(), this);
        mServiceMessenger = new Messenger(mServiceHandler);
    }

}
