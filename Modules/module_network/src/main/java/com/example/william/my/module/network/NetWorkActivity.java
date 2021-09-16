package com.example.william.my.module.network;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseListActivity;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.NetWork.NetWork)
public class NetWorkActivity extends BaseListActivity {

    @Override
    public void initData() {
        super.initData();
        mMap.put("ImageLoaderActivity", ARouterPath.NetWork.NetWork_ImageLoader);
        mMap.put("HttpURLActivity", ARouterPath.NetWork.NetWork_HttpURL);
        mMap.put("VolleyActivity", ARouterPath.NetWork.NetWork_Volley);
        mMap.put("OkHttpActivity", ARouterPath.NetWork.NetWork_OkHttp);
        mMap.put("RetrofitActivity", ARouterPath.NetWork.NetWork_Retrofit);
        mMap.put("RetrofitRxJavaActivity", ARouterPath.NetWork.NetWork_RetrofitRxJava);
        mMap.put("RetrofitRxJavaUtilsActivity", ARouterPath.NetWork.NetWork_RetrofitRxJavaUtils);
        mMap.put("RxRetrofitActivity", ARouterPath.NetWork.NetWork_RxRetrofit);
        mMap.put("RetrofitDownloadActivity", ARouterPath.NetWork.NetWork_Download);
        mMap.put("WebSocketActivity", ARouterPath.NetWork.NetWork_WebSocket);
        mMap.put("NanoHttpDActivity", ARouterPath.NetWork.NetWork_NanoHttpD);
        mMap.put("WebServerActivity", ARouterPath.NetWork.NetWork_WebServer);
        mMap.put("NettyActivity", ARouterPath.NetWork.NetWork_Netty);
    }
}