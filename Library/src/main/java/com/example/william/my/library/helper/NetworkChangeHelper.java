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

import androidx.annotation.NonNull;
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

    @SuppressWarnings("deprecation")
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

    @SuppressWarnings("deprecation")
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
                            Log.e(TAG, "CONNECTIVITY_ACTION: 网络类型为WIFI");//执行两次
                        } else if (ConnectivityManager.TYPE_MOBILE == info.getType()) {
                            Log.e(TAG, "CONNECTIVITY_ACTION: 网络类型为移动数据");
                        }
                        mNetworkChangeListener.onNetworkChange(true);
                    } else {
                        mNetworkChangeListener.onNetworkChange(false);
                    }
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static class NetworkCallbackImpl extends ConnectivityManager.NetworkCallback {

        private static final String TAG = "NetworkCallbackImpl";

        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
            mNetworkChangeListener.onNetworkChange(true);
        }

        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
            mNetworkChangeListener.onNetworkChange(false);
        }

        @Override
        public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities);
            //网络变化时，这个方法会回调多次
            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.e(TAG, "onCapabilitiesChanged: 网络类型为wifi");
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.e(TAG, "onCapabilitiesChanged: 网络类型为移动数据");
                } else {
                    Log.e(TAG, "onCapabilitiesChanged: 其他网络");
                }
            }
        }
    }

    private static NetworkChangeListener mNetworkChangeListener;

    public interface NetworkChangeListener {
        void onNetworkChange(boolean isAvailable);
    }
}
