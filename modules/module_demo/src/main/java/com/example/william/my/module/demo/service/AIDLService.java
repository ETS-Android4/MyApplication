package com.example.william.my.module.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.william.my.module.demo.IMyAidlInterface;
import com.example.william.my.module.utils.T;

public class AIDLService extends Service {

    @SuppressWarnings("FieldCanBeLocal")
    private XBinder xBinder;

    @SuppressWarnings("FieldCanBeLocal")
    private AIDLBinder aidlBinder;

    private static final String TAG = "AIDLService";


    @Override
    public IBinder onBind(Intent intent) {
        return xBinder;
        //return aidlBinder;
    }

    /**
     * 首次创建服务时调用，如果服务已在运行，则不会调用此方法。该方法只被调用一次。
     * 在调用 onStartCommand() 或 onBind() 之前
     */
    @Override
    public void onCreate() {
        super.onCreate();
        xBinder = new XBinder();
        aidlBinder = new AIDLBinder();
    }

    public static class XBinder extends Binder {

        public void showToast(String s) {
            T.show(s);
        }
    }

    public static class AIDLBinder extends IMyAidlInterface.Stub {

        @Override
        public void showToast(String s) throws RemoteException {
            T.show(s);
        }
    }
}
