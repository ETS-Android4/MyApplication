package com.example.william.my.module.opensource;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseRecyclerActivity;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.router.item.RouterItem;

import java.util.ArrayList;

/**
 * https://github.com/zxing/zxing
 * <p>
 * https://github.com/lzyzsd/JsBridge
 * <p>
 * https://github.com/vinc3m1/RoundedImageView
 * https://github.com/KingJA/LoadSir
 */
@Route(path = ARouterPath.OpenSource.OpenSource)
public class OpenSourceActivity extends BaseRecyclerActivity {

    protected ArrayList<RouterItem> buildRouter() {
        ArrayList<RouterItem> routerItems = new ArrayList<>();
        routerItems.add(new RouterItem("EventBusActivity", ARouterPath.OpenSource.OpenSource_EventBus));
        //
        routerItems.add(new RouterItem("MMKVActivity", ARouterPath.OpenSource.OpenSource_MMKV));
        routerItems.add(new RouterItem("GreenDaoActivity", ARouterPath.OpenSource.OpenSource_GreenDao));
        //
        routerItems.add(new RouterItem("BackgroundActivity", ARouterPath.OpenSource.OpenSource_Background));
        routerItems.add(new RouterItem("BadgeViewActivity", ARouterPath.OpenSource.OpenSource_BadgeView));
        routerItems.add(new RouterItem("BannerActivity", ARouterPath.OpenSource.OpenSource_Banner));
        routerItems.add(new RouterItem("CityPickerActivity", ARouterPath.OpenSource.OpenSource_CityPicker));
        routerItems.add(new RouterItem("CountdownActivity", ARouterPath.OpenSource.OpenSource_Countdown));
        routerItems.add(new RouterItem("FloatWindowActivity", ARouterPath.OpenSource.OpenSource_FloatWindow));
        routerItems.add(new RouterItem("FlycoTabLayoutActivity", ARouterPath.OpenSource.OpenSource_FlycoTabLayout));
        routerItems.add(new RouterItem("GSYPlayerActivity", ARouterPath.OpenSource.OpenSource_GSYPlayer));
        routerItems.add(new RouterItem("ImageSelectorActivity", ARouterPath.OpenSource.OpenSource_ImageSelector));
        routerItems.add(new RouterItem("ImmersionBarActivity", ARouterPath.OpenSource.OpenSource_ImmersionBar));
        routerItems.add(new RouterItem("LottieActivity", ARouterPath.OpenSource.OpenSource_Lottie));
        routerItems.add(new RouterItem("PhotoViewActivity", ARouterPath.OpenSource.OpenSource_PhotoView));
        routerItems.add(new RouterItem("PickerViewActivity", ARouterPath.OpenSource.OpenSource_PickerView));
        routerItems.add(new RouterItem("PopWindowActivity", ARouterPath.OpenSource.OpenSource_PopWindow));
        routerItems.add(new RouterItem("ShadowLayoutActivity", ARouterPath.OpenSource.OpenSource_ShadowLayout));
        routerItems.add(new RouterItem("SmartRefreshActivity", ARouterPath.OpenSource.OpenSource_SmartRefresh));
        routerItems.add(new RouterItem("SwipeLayoutActivity", ARouterPath.OpenSource.OpenSource_SwipeLayout));
        routerItems.add(new RouterItem("SvagPlayerActivity", ARouterPath.OpenSource.OpenSource_SvagPlayer));
        routerItems.add(new RouterItem("SwipeToLoadActivity", ARouterPath.OpenSource.OpenSource_SwipeToLoad));
        routerItems.add(new RouterItem("TangramActivity", ARouterPath.OpenSource.OpenSource_Tangram));
        routerItems.add(new RouterItem("X5Activity", ARouterPath.OpenSource.OpenSource_X5));
        return routerItems;
    }
}