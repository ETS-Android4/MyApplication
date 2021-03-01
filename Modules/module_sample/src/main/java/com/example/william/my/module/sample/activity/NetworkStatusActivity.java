package com.example.william.my.module.sample.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.helper.NetworkChangeHelper;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;

/**
 * 5.0之前版本我们是通过系统发送的广播来监听的，即ConnectivityManager.CONNECTIVITY_ACTION
 * 5.0及之后版本我们可以通过ConnectivityManager.NetworkCallback这个类来监听
 * 7.0及以上静态注册广播不能收到"ConnectivityManager.CONNECTIVITY_ACTION"这个广播了
 */
@Route(path = ARouterPath.Sample.Sample_NetworkStatus)
public class NetworkStatusActivity extends BaseResponseActivity {


    @Override
    public void initView() {
        super.initView();
        NetworkChangeHelper.getInstance().register(this, new NetworkChangeHelper.NetworkChangeListener() {
            @Override
            public void onNetworkChange(boolean isAvailable) {
                showResponse("网络是否连接 ： " + isAvailable);
            }
        });
    }
}