package com.example.william.my.jet;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.ARouterActivity;
import com.example.william.my.module.router.ARouterPath;

/**
 * https://developer.android.google.cn/jetpack/guide
 * https://developer.android.google.cn/topic/libraries/architecture/index.html
 */
@Route(path = ARouterPath.JetPack.JetPack)
public class JetPackActivity extends ARouterActivity {

    @Override
    protected void initData() {
        super.initData();
        mMap.put("BindActivity", ARouterPath.JetPack.JetPack_Bind);
        mMap.put("NavigationActivity", ARouterPath.JetPack.JetPack_Navigation);
        mMap.put("PagingActivity", ARouterPath.JetPack.JetPack_Paging);
        mMap.put("RoomActivity", ARouterPath.JetPack.JetPack_Room);
        mMap.put("ViewModelActivity", ARouterPath.JetPack.JetPack_ViewModel);
        mMap.put("WorkManagerActivity", ARouterPath.JetPack.JetPack_WorkManager);
    }
}