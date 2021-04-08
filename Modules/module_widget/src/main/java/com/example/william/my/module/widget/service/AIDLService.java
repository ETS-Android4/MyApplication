package com.example.william.my.module.widget.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.william.my.module.widget.IMyAidlInterface;

public class AIDLService extends Service {

    private XBinder xBinder;
    private AIDLBinder aidlBinder;

    private static final String TAG = "AIDLService";

    @Nullable
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

    public class XBinder extends Binder {

        public void showToast(String s) {
            Log.e(TAG, "XBinder: " + s);
            Toast.makeText(AIDLService.this, s, Toast.LENGTH_SHORT).show();
        }
    }

    public class AIDLBinder extends IMyAidlInterface.Stub {

        @Override
        public void showToast(String s) throws RemoteException {
            Log.e(TAG, "AIDLBinder: " + s);
            Toast.makeText(AIDLService.this, s, Toast.LENGTH_SHORT).show();
        }
    }
}
