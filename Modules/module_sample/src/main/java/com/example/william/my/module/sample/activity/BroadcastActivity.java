package com.example.william.my.module.sample.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;

import java.lang.ref.WeakReference;

/**
 * BroadcastReceiver
 */
@Route(path = ARouterPath.Sample.Sample_Broadcast)
public class BroadcastActivity extends BaseResponseActivity {

    private MessageReceiver mMessageReceiver;

    /**
     * 消息监听器
     */
    public static class MessageReceiver extends BroadcastReceiver {

        private final WeakReference<BroadcastActivity> softReference;

        //声明一个操作常量字符串
        public static final String ACTION_UPDATE = "com.example.broadcast";

        public MessageReceiver(BroadcastActivity activity) {
            this.softReference = new WeakReference<>(activity);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            softReference.get().mResponse.setText(intent.getStringExtra("message"));
        }
    }

    @Override
    public void setOnClick() {
        super.setOnClick();

        Bundle bundle = new Bundle();
        bundle.putString("message", MessageReceiver.ACTION_UPDATE);

        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setAction(MessageReceiver.ACTION_UPDATE);
        sendBroadcast(intent);

        //LocalBroadcastManager.getInstance(BroadcastActivity.this).sendBroadcast(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mMessageReceiver = new MessageReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MessageReceiver.ACTION_UPDATE);
        registerReceiver(mMessageReceiver, intentFilter);

        //LocalBroadcastManager.getInstance(BroadcastActivity.this).registerReceiver(mMessageReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMessageReceiver);

        //LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }
}
