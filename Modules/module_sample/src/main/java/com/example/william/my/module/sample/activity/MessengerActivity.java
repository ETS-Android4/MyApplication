package com.example.william.my.module.sample.activity;

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
import com.example.william.my.module.sample.service.MessageService;

import java.lang.ref.WeakReference;

@Route(path = ARouterPath.Sample.Sample_Messenger)
public class MessengerActivity extends BaseResponseActivity {

    //serviceMessenger表示的是Service端的Messenger，其内部指向了MyService的ServiceHandler实例
    //可以用serviceMessenger向MyService发送消息
    private Messenger mServiceMessenger;

    //clientMessenger是客户端自身的Messenger，内部指向了ClientHandler的实例
    //MyService可以通过Message的replyTo得到clientMessenger，从而MyService可以向客户端发送消息，
    //并由ClientHandler接收并处理来自于Service的消息
    private Messenger mClientMessenger;

    private ServiceConnection mServiceConnection;

    private static class ClientHandler extends Handler {

        private final WeakReference<MessengerActivity> mActivity;

        public ClientHandler(@NonNull Looper looper, MessengerActivity activity) {
            super(looper);
            this.mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            MessengerActivity msgActivity = this.mActivity.get();
            if (msgActivity == null) {
                return;
            }
            msgActivity.showResponse(msg.getData().getString("KEY"));
        }
    }

    @Override
    public void initView() {
        super.initView();
        ClientHandler clientHandler = new ClientHandler(Looper.getMainLooper(), this);
        mClientMessenger = new Messenger(clientHandler);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                //我们可以通过从Service的onBind方法中返回的IBinder初始化一个指向Service端的Messenger
                mServiceMessenger = new Messenger(iBinder);
                if (mServiceMessenger != null) {
                    Message mMessage = Message.obtain();

                    //此处跨进程Message通信不能将msg.obj设置为non-Parcelable的对象，应该使用Bundle
                    Bundle bundle = new Bundle();
                    bundle.putString("KEY", "发送消息到Service");

                    //发送消息给服务端
                    mMessage.setData(bundle);

                    //需要将Message的replyTo设置为客户端的clientMessenger，
                    //以便Service可以通过它向客户端发送消息
                    mMessage.replyTo = mClientMessenger;

                    try {
                        mServiceMessenger.send(mMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
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
}
