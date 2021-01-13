package com.example.william.my.jet;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.network.base.RxRetrofitConfig;
import com.example.william.my.module.activity.ARouterActivity;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.JetPack.JetPack)
public class JetPackActivity extends ARouterActivity {

    @Override
    protected void initData() {
        super.initData();
        RxRetrofitConfig.init(getApplication());
        mMap.put("BindActivity", ARouterPath.JetPack.JetPack_Bind);
        mMap.put("ViewModelActivity", ARouterPath.JetPack.JetPack_ViewModel);
        mMap.put("PagingActivity", ARouterPath.JetPack.JetPack_Paging);
        mMap.put("RoomActivity", ARouterPath.JetPack.JetPack_Room);
        mMap.put("NavigationActivity", ARouterPath.JetPack.JetPack_Navigation);
        mMap.put("WorkManagerActivity", ARouterPath.JetPack.JetPack_WorkManager);
    }
}