package com.example.william.my.sample.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.sample.service.MessageService;

import java.lang.ref.WeakReference;

@Route(path = ARouterPath.Sample.Sample_Messenger)
public class MessengerActivity extends BaseResponseActivity {

    private Messenger mClientMessenger;

    private ServiceConnection mServiceConnection;

    private static class ClientHandler extends Handler {

        private final WeakReference<MessengerActivity> softReference;

        public ClientHandler(@NonNull Looper looper, MessengerActivity activity) {
            super(looper);
            this.softReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            softReference.get().mResponse.setText(msg.getData().getString("KEY"));
        }
    }

    @Override
    public void initView() {
        super.initView();

        //1.引用Handler创造一个信使对象
        ClientHandler mClientHandler = new ClientHandler(Looper.getMainLooper(), this);
        mClientMessenger = new Messenger(mClientHandler);
    }

    @Override
    public void setOnClick() {
        super.setOnClick();
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

                Message mMessage = Message.obtain();

                Bundle bundle = new Bundle();
                bundle.putString("KEY", "发送消息到Service");

                //2.发送消息给服务端
                mMessage.setData(bundle);

                //3.传递Messenger给服务端，以便服务端向客户端发送消息
                mMessage.replyTo = mClientMessenger;

                try {
                    //4.发送Message到服务端
                    Messenger mMessenger = new Messenger(iBinder);
                    mMessenger.send(mMessage);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        bindService(new Intent(MessengerActivity.this, MessageService.class), mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }
}
