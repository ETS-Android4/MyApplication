package com.example.william.my.module.open;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseListActivity;
import com.example.william.my.module.router.ARouterPath;

/**
 * https://github.com/zxing/zxing
 * <p>
 * https://github.com/lzyzsd/JsBridge
 * <p>
 * https://github.com/vinc3m1/RoundedImageView
 * https://github.com/KingJA/LoadSir
 */
@Route(path = ARouterPath.OpenSource.OpenSource)
public class OpenSourceActivity extends BaseListActivity {

    @Override
    protected void initData() {
        super.initData();
        mMap.put("EventBusActivity", ARouterPath.OpenSource.OpenSource_EventBus);
        mMap.put("LiveEventBusActivity", ARouterPath.OpenSource.OpenSource_LiveEventBus);
        mMap.put("SmartEventBusActivity", ARouterPath.OpenSource.OpenSource_SmartEventBus);
        //
        mMap.put("MMKVActivity", ARouterPath.OpenSource.OpenSource_MMKV);
        mMap.put("GreenDaoActivity", ARouterPath.OpenSource.OpenSource_GreenDao);
        //
        mMap.put("BackgroundActivity", ARouterPath.OpenSource.OpenSource_Background);
        mMap.put("BadgeViewActivity", ARouterPath.OpenSource.OpenSource_BadgeView);
        mMap.put("BannerActivity", ARouterPath.OpenSource.OpenSource_Banner);
        mMap.put("CityPickerActivity", ARouterPath.OpenSource.OpenSource_CityPicker);
        mMap.put("CountdownActivity", ARouterPath.OpenSource.OpenSource_Countdown);
        mMap.put("FloatWindowActivity", ARouterPath.OpenSource.OpenSource_FloatWindow);
        mMap.put("FlycoTabLayoutActivity", ARouterPath.OpenSource.OpenSource_FlycoTabLayout);
        mMap.put("GSYPlayerActivity", ARouterPath.OpenSource.OpenSource_GSYPlayer);
        mMap.put("ImageSelectorActivity", ARouterPath.OpenSource.OpenSource_ImageSelector);
        mMap.put("ImmersionBarActivity", ARouterPath.OpenSource.OpenSource_ImmersionBar);
        mMap.put("PhotoViewActivity", ARouterPath.OpenSource.OpenSource_PhotoView);
        mMap.put("PickerViewActivity", ARouterPath.OpenSource.OpenSource_PickerView);
        mMap.put("PopWindowActivity", ARouterPath.OpenSource.OpenSource_PopWindow);
        mMap.put("SmartRefreshActivity", ARouterPath.OpenSource.OpenSource_SmartRefresh);
        mMap.put("SwipeLayoutActivity", ARouterPath.OpenSource.OpenSource_SwipeLayout);
        mMap.put("SvagPlayerActivity", ARouterPath.OpenSource.OpenSource_SvagPlayer);
        mMap.put("SwipeToLoadActivity", ARouterPath.OpenSource.OpenSource_SwipeToLoad);
        mMap.put("TangramActivity", ARouterPath.OpenSource.OpenSource_Tangram);
        mMap.put("X5Activity", ARouterPath.OpenSource.OpenSource_X5);
    }
}