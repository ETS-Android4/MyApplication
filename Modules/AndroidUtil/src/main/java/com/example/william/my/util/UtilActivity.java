package com.example.william.my.util;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.ARouterActivity;
import com.example.william.my.module.router.ARouterPath;

/**
 * https://github.com/Tencent/QMUI_Android
 * https://github.com/Blankj/AndroidUtilCode
 */
@Route(path = ARouterPath.Util.Util)
public class UtilActivity extends ARouterActivity {

    @Override
    protected void initData() {
        super.initData();
        mMap.put("AdaptScreenActivity", ARouterPath.Util.Util_AdaptScreen);
        mMap.put("BusUtilsActivity", ARouterPath.Util.Util_BusUtils);
        mMap.put("CacheActivity", ARouterPath.Util.Util_Cache);
        mMap.put("CrashUtilsActivity", ARouterPath.Util.Util_Crash);
        mMap.put("ImageUtilsActivity", ARouterPath.Util.Util_ImageUtils);
        mMap.put("LanguageActivity", ARouterPath.Util.Util_Language);
        mMap.put("PermissionActivity", ARouterPath.Util.Util_Permission);
    }
}