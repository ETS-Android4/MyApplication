package com.example.william.my.library.helper;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

/**
 * 5.0之前版本我们是通过系统发送的广播来监听的，即ConnectivityManager.CONNECTIVITY_ACTION
 * 5.0及之后版本我们可以通过ConnectivityManager.NetworkCallback这个类来监听
 * <p>
 * 7.0及以上静态注册广播不能收到"ConnectivityManager.CONNECTIVITY_ACTION"这个广播了
 */
@SuppressLint("MissingPermission")
public class NetworkChangeHelper {

    private static NetworkChangeHelper instance;

    public static NetworkChangeHelper getInstance() {
        if (instance == null) {
            synchronized (NetworkChangeHelper.class) {
                if (instance == null) {
                    instance = new NetworkChangeHelper();
                }
            }
        }
        return instance;
    }

    private NetworkChangeHelper() {

    }

    public void register(Context context, NetworkChangeListener networkChangeListener) {

        mNetworkChangeListener = networkChangeListener;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            /*
             * 7.0以下使用广播方式监听，即ConnectivityManager.CONNECTIVITY_ACTION
             */
            NetworkChangeReceiver receiver = new NetworkChangeReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            context.registerReceiver(receiver, filter);
        } else {
            /*
             * 7.0以上使用ConnectivityManager.NetworkCallback
             */
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                connectivityManager.requestNetwork(new NetworkRequest.Builder().build(), new NetworkCallbackImpl());
            }
        }
    }

    public static class NetworkChangeReceiver extends BroadcastReceiver {

        private static final String TAG = "NetworkChangeReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = connectivityManager.getActiveNetworkInfo();
                if (info != null) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        if (ConnectivityManager.TYPE_WIFI == info.getType()) {
                            Log.i(TAG, "CONNECTIVITY_ACTION: 网络类型为WIFI");//执行两次
                        } else if (ConnectivityManager.TYPE_MOBILE == info.getType()) {
                            Log.i(TAG, "CONNECTIVITY_ACTION: 网络类型为移动数据");
                        }
                        mNetworkChangeListener.onNetworkStatusChange(true);
                    } else {
                        mNetworkChangeListener.onNetworkStatusChange(false);
                    }
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static class NetworkCallbackImpl extends ConnectivityManager.NetworkCallback {

        private static final String TAG = "NetworkCallbackImpl";

        @Override
        public void onAvailable(Network network) {
            super.onAvailable(network);
            mNetworkChangeListener.onNetworkStatusChange(true);
        }

        @Override
        public void onLost(Network network) {
            super.onLost(network);
            mNetworkChangeListener.onNetworkStatusChange(false);
        }

        @Override
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities);
            //网络变化时，这个方法会回调多次
            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i(TAG, "onCapabilitiesChanged: 网络类型为wifi");
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i(TAG, "onCapabilitiesChanged: 网络类型为移动数据");
                } else {
                    Log.i(TAG, "onCapabilitiesChanged: 其他网络");
                }
            }
        }
    }

    private static NetworkChangeListener mNetworkChangeListener;

    public interface NetworkChangeListener {
        /**
         * 网络状态改变
         *
         * @param isAvailable 是否可用
         */
        void onNetworkStatusChange(boolean isAvailable);
    }
}
