package com.example.william.my.module.libraries;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseRecyclerActivity;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.router.item.RouterItem;

import java.util.ArrayList;

@Route(path = ARouterPath.Lib.Lib)
public class LibrariesActivity extends BaseRecyclerActivity {

    protected ArrayList<RouterItem> buildRouter() {
        ArrayList<RouterItem> routerItems = new ArrayList<>();
        routerItems.add(new RouterItem("BannerActivity", ARouterPath.Lib.Lib_Banner));
        routerItems.add(new RouterItem("InfiniteImageActivity", ARouterPath.Lib.Lib_InfiniteImage));
        routerItems.add(new RouterItem("NinePatchActivity", ARouterPath.Lib.Lib_NinePatch));
        routerItems.add(new RouterItem("VerifyCodeActivity", ARouterPath.Lib.Lib_VerifyCode));
        routerItems.add(new RouterItem("SphereCollisionActivity", ARouterPath.Lib.Lib_SphereCollision));
        return routerItems;
    }
}