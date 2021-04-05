package com.example.william.my.module.widget.service;

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

import com.example.william.my.module.widget.activity.function.MessengerActivity;

import java.lang.ref.WeakReference;

public class MessageService extends Service {

    //clientMessenger表示的是客户端的Messenger，可以通过来自于客户端的Message的replyTo属性获得，
    //其内部指向了客户端的ClientHandler实例，可以用clientMessenger向客户端发送消息
    private Messenger mClientMessenger;

    //serviceMessenger是Service自身的Messenger，其内部指向了ServiceHandler的实例
    //客户端可以通过IBinder构建Service端的Messenger，从而向Service发送消息，
    //并由ServiceHandler接收并处理来自于客户端的消息
    private Messenger mServiceMessenger;

    /**
     * 接收并处理从客户端收到的消息
     */
    private static class ServiceHandler extends Handler {

        private final WeakReference<MessageService> weakReference;

        ServiceHandler(@NonNull Looper looper, MessageService service) {
            super(looper);
            this.weakReference = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            MessageService mService = weakReference.get();
            if (mService == null) {
                return;
            }
            if (msg.what == MessengerActivity.MSG_CODE_SEND_TO_SERVICE) {
                //通过Message的replyTo获取到客户端自身的Messenger，
                //Service可以通过它向客户端发送消息
                weakReference.get().mClientMessenger = msg.replyTo;

                String value = msg.getData().getString(MessengerActivity.MSG_SEND_KEY);

                sendMessage(MessengerActivity.MSG_CODE_SEND_TO_ACTIVITY, MessengerActivity.MSG_SEND_KEY, value);
            }
        }

        private void sendMessage(int messageID, String key, String params) {
            if (weakReference.get().mClientMessenger == null) {
                return;
            }
            Message message = Message.obtain();
            message.what = messageID;

            //此处跨进程Message通信不能将msg.obj设置为non-Parcelable的对象，应该使用Bundle
            //message.obj = params;
            Bundle bundle = new Bundle();
            bundle.putString(key, params);
            message.setData(bundle);

            try {
                //向客户端发送消息
                weakReference.get().mClientMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //获取Service自身Messenger所对应的IBinder，并将其发送共享给所有客户端
        return mServiceMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceHandler serviceHandler = new ServiceHandler(Looper.getMainLooper(), this);
        mServiceMessenger = new Messenger(serviceHandler);
    }
}
