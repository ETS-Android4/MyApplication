package com.example.william.my.module.network;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseRecyclerActivity;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.router.item.RouterItem;

import java.util.ArrayList;

@Route(path = ARouterPath.NetWork.NetWork)
public class NetWorkActivity extends BaseRecyclerActivity {

    protected ArrayList<RouterItem> buildRouter() {
        ArrayList<RouterItem> routerItems = new ArrayList<>();
        routerItems.add(new RouterItem("GlideActivity", ARouterPath.NetWork.NetWork_Glide));
        routerItems.add(new RouterItem("HttpURLActivity", ARouterPath.NetWork.NetWork_HttpURL));
        routerItems.add(new RouterItem("VolleyActivity", ARouterPath.NetWork.NetWork_Volley));
        routerItems.add(new RouterItem("VolleyUtilsActivity", ARouterPath.NetWork.NetWork_VolleyUtils));
        routerItems.add(new RouterItem("OkHttpActivity", ARouterPath.NetWork.NetWork_OkHttp));
        routerItems.add(new RouterItem("RetrofitActivity", ARouterPath.NetWork.NetWork_Retrofit));
        routerItems.add(new RouterItem("RetrofitRxJavaActivity", ARouterPath.NetWork.NetWork_RetrofitRxJava));
        routerItems.add(new RouterItem("RetrofitRxJavaUtilsActivity", ARouterPath.NetWork.NetWork_RetrofitRxJavaUtils));
        routerItems.add(new RouterItem("RxRetrofitActivity", ARouterPath.NetWork.NetWork_RxRetrofit));
        routerItems.add(new RouterItem("WebSocketActivity", ARouterPath.NetWork.NetWork_WebSocket));
        routerItems.add(new RouterItem("NanoHttpDActivity", ARouterPath.NetWork.NetWork_NanoHttpD));
        routerItems.add(new RouterItem("WebServerActivity", ARouterPath.NetWork.NetWork_WebServer));
        routerItems.add(new RouterItem("NettyActivity", ARouterPath.NetWork.NetWork_Netty));
        return routerItems;
    }
}