package com.example.william.my.network;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.network.base.RxRetrofitConfig;
import com.example.william.my.module.activity.ARouterActivity;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.NetWork.NetWork)
public class NetWorkActivity extends ARouterActivity {

    @Override
    public void initData() {
        super.initData();
        RxRetrofitConfig.init(getApplication());
        mMap.put("GlideActivity", ARouterPath.NetWork.NetWork_Glide);
        mMap.put("HttpURLActivity", ARouterPath.NetWork.NetWork_HttpURL);
        mMap.put("VolleyActivity", ARouterPath.NetWork.NetWork_Volley);
        mMap.put("OkHttpActivity", ARouterPath.NetWork.NetWork_OkHttp);
        mMap.put("RetrofitActivity", ARouterPath.NetWork.NetWork_Retrofit);
        mMap.put("RetrofitRxJavaActivity", ARouterPath.NetWork.NetWork_RetrofitRxJava);
        mMap.put("RetrofitUtilsActivity", ARouterPath.NetWork.NetWork_RetrofitUtils);
        mMap.put("RetrofitDownloadActivity", ARouterPath.NetWork.NetWork_RetrofitDownload);
        mMap.put("RxRetrofitActivity", ARouterPath.NetWork.NetWork_RxRetrofit);
        mMap.put("WebSocketActivity", ARouterPath.NetWork.NetWork_WebSocket);
        mMap.put("NanoHttpDActivity", ARouterPath.NetWork.NetWork_NanoHttpD);
        mMap.put("WebServerActivity", ARouterPath.NetWork.NetWork_WebServer);
    }
}