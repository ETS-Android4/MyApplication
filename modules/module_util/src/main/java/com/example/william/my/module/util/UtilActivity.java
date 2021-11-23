package com.example.william.my.module.util;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseFragmentActivity;
import com.example.william.my.module.router.fragment.RouterRecyclerFragment;
import com.example.william.my.module.router.item.RouterItem;
import com.example.william.my.module.router.ARouterPath;

import java.util.ArrayList;

/**
 * https://github.com/Tencent/QMUI_Android
 * https://github.com/Blankj/AndroidUtilCode
 */
@Route(path = ARouterPath.Util.Util)
public class UtilActivity extends BaseFragmentActivity {

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
        routerItems.add(new RouterItem("AdaptScreenActivity", ARouterPath.Util.Util_AdaptScreen));
        routerItems.add(new RouterItem("BusUtilsActivity", ARouterPath.Util.Util_BusUtils));
        routerItems.add(new RouterItem("CacheActivity", ARouterPath.Util.Util_Cache));
        routerItems.add(new RouterItem("CrashUtilsActivity", ARouterPath.Util.Util_Crash));
        routerItems.add(new RouterItem("ImageUtilsActivity", ARouterPath.Util.Util_ImageUtils));
        routerItems.add(new RouterItem("LanguageActivity", ARouterPath.Util.Util_Language));
        routerItems.add(new RouterItem("PermissionActivity", ARouterPath.Util.Util_Permission));
        return routerItems;
    }
}