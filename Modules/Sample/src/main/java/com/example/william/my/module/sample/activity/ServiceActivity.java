package com.example.william.my.module.sample.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.sample.IMyAidlInterface;
import com.example.william.my.module.sample.service.AIDLService;
import com.example.william.my.module.sample.service.ForegroundService;

/**
 * Service
 */
@Route(path = ARouterPath.Sample.Sample_Service)
public class ServiceActivity extends BaseResponseActivity {

    private ServiceConnection mServiceConnection;

    @Override
    public void setOnClick() {
        super.setOnClick();
        startService(new Intent(this, ForegroundService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

                //BindService
                try {
                    AIDLService.XBinder xBinder = (AIDLService.XBinder) iBinder;
                    xBinder.showToast("服务已启动");
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }

                //AIDLService
                IMyAidlInterface serviceInterface = IMyAidlInterface.Stub.asInterface(iBinder);
                try {
                    serviceInterface.showToast("服务已启动");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            /**
             * 与服务的连接意外中断时调用，取消绑定时不会调用该方法
             */
            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        bindService(new Intent(ServiceActivity.this, AIDLService.class), mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }
}