package com.example.william.my.module.jetpack;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseFragmentActivity;
import com.example.william.my.module.router.fragment.RouterRecyclerFragment;
import com.example.william.my.module.router.item.RouterItem;
import com.example.william.my.module.router.ARouterPath;

import java.util.ArrayList;

/**
 * https://developer.android.google.cn/jetpack/guide
 * https://developer.android.google.cn/topic/libraries/architecture/index.html
 */
@Route(path = ARouterPath.JetPack.JetPack)
public class JetPackActivity extends BaseFragmentActivity {

    @Override
    public Fragment setFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("router", buildRouter());
        RouterRecyclerFragment fragment = new RouterRecyclerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private ArrayList<RouterItem> buildRouter() {
        ArrayList<RouterItem> routerItems = new ArrayList<>();
        routerItems.add(new RouterItem("BindActivity", ARouterPath.JetPack.JetPack_Bind));
        routerItems.add(new RouterItem("LiveDataActivity", ARouterPath.JetPack.JetPack_LiveData));
        routerItems.add(new RouterItem("PagingActivity", ARouterPath.JetPack.JetPack_Paging));
        routerItems.add(new RouterItem("WorkManagerActivity", ARouterPath.JetPack.JetPack_WorkManager));
        routerItems.add(new RouterItem("NavigationActivity", ARouterPath.JetPack.JetPack_Navigation));
        routerItems.add(new RouterItem("RoomActivity", ARouterPath.JetPack.JetPack_Room));
        return routerItems;
    }
}