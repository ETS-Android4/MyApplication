package com.example.william.my.module.util;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseRecyclerActivity;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.router.item.RouterItem;

import java.util.ArrayList;

/**
 * https://github.com/Tencent/QMUI_Android
 * https://github.com/Blankj/AndroidUtilCode
 */
@Route(path = ARouterPath.Util.Util)
public class UtilActivity extends BaseRecyclerActivity {

    protected ArrayList<RouterItem> buildRouter() {
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