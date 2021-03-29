package com.example.william.my.module.sample.activity.function;

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
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.sample.service.MessageService;

import java.lang.ref.WeakReference;

@Route(path = ARouterPath.Sample.Sample_Messenger)
public class MessengerActivity extends BaseResponseActivity {

    public static final int MSG_CODE_SEND_TO_SERVICE = 1;
    public static final int MSG_CODE_SEND_TO_ACTIVITY = 2;
    public static final String MSG_SEND_KEY = "MSG_SEND_KEY";

    //serviceMessenger表示的是Service端的Messenger，其内部指向了MyService的ServiceHandler实例
    //可以用serviceMessenger向MyService发送消息
    private Messenger mServiceMessenger;

    //clientMessenger是客户端自身的Messenger，内部指向了ClientHandler的实例
    //MyService可以通过Message的replyTo得到clientMessenger，从而MyService可以向客户端发送消息，
    //并由ClientHandler接收并处理来自于Service的消息
    private Messenger mClientMessenger;

    private ServiceConnection mServiceConnection;

    private int mMessageId = 0;

    private static class ClientHandler extends Handler {

        private final WeakReference<MessengerActivity> weakReference;

        public ClientHandler(@NonNull Looper looper, MessengerActivity activity) {
            super(looper);
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            MessengerActivity mActivity = this.weakReference.get();
            if (mActivity == null) {
                return;
            }
            if (msg.what == MSG_CODE_SEND_TO_ACTIVITY) {
                String value = msg.getData().getString(MSG_SEND_KEY);
                mActivity.showResponse(value);
            }
        }
    }

    @Override
    public void initView() {
        super.initView();
        ClientHandler clientHandler = new ClientHandler(Looper.getMainLooper(), this);
        mClientMessenger = new Messenger(clientHandler);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        sendMessage(MSG_CODE_SEND_TO_SERVICE, MSG_SEND_KEY, "点击发送消息 : " + (mMessageId++), mClientMessenger);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                //我们可以通过从Service的onBind方法中返回的IBinder初始化一个指向Service端的Messenger
                mServiceMessenger = new Messenger(iBinder);

                sendMessage(MSG_CODE_SEND_TO_SERVICE, MSG_SEND_KEY, "你好，MyService，我是client", mClientMessenger);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                //客户端与Service失去连接
                mServiceMessenger = null;
            }
        };
        bindService(new Intent(MessengerActivity.this, MessageService.class), mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    private void sendMessage(int messageID, String key, String params, Messenger messenger) {
        if (mServiceMessenger == null) {
            return;
        }
        Message message = Message.obtain();
        message.what = messageID;

        //此处跨进程Message通信不能将msg.obj设置为non-Parcelable的对象，应该使用Bundle
        //message.obj = params;
        Bundle bundle = new Bundle();
        bundle.putString(key, params);
        message.setData(bundle);

        //需要将Message的replyTo设置为客户端的clientMessenger，
        //以便Service可以通过它向客户端发送消息
        message.replyTo = messenger;
        try {
            mServiceMessenger.send(message);
        } catch (RemoteException e) {
            Log.e(TAG, "Error passing service object back to activity.");
        }
    }
}
