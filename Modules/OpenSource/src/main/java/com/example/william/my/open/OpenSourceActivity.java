package com.example.william.my.open;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseListActivity;
import com.example.william.my.module.router.ARouterPath;

/**
 * https://github.com/WVector/AppUpdate
 * https://github.com/AlexLiuSheng/CheckVersionLib
 * <p>
 * https://github.com/zxing/zxing
 * <p>
 * 在线选座
 * https://github.com/qifengdeqingchen/SeatTable
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
        mMap.put("BackgroundActivity", ARouterPath.OpenSource.OpenSource_Background);
        mMap.put("BadgeViewActivity", ARouterPath.OpenSource.OpenSource_BadgeView);
        mMap.put("BannerActivity", ARouterPath.OpenSource.OpenSource_Banner);
        mMap.put("CityPickerActivity", ARouterPath.OpenSource.OpenSource_CityPicker);
        mMap.put("CountdownActivity", ARouterPath.OpenSource.OpenSource_Countdown);
        mMap.put("FlycoTabLayoutActivity", ARouterPath.OpenSource.OpenSource_FlycoTabLayout);
        mMap.put("GSYPlayerActivity", ARouterPath.OpenSource.OpenSource_GSYPlayer);
        mMap.put("ImageSelectorActivity", ARouterPath.OpenSource.OpenSource_ImageSelector);
        mMap.put("PhotoViewActivity", ARouterPath.OpenSource.OpenSource_PhotoView);
        mMap.put("PickerViewActivity", ARouterPath.OpenSource.OpenSource_PickerView);
        mMap.put("PopWindowActivity", ARouterPath.OpenSource.OpenSource_PopWindow);
        mMap.put("SmartRefreshActivity", ARouterPath.OpenSource.OpenSource_SmartRefresh);
        mMap.put("SwipeLayoutActivity", ARouterPath.OpenSource.OpenSource_SwipeLayout);
        mMap.put("SwipeToLoadActivity", ARouterPath.OpenSource.OpenSource_SwipeToLoad);
        mMap.put("TangramActivity", ARouterPath.OpenSource.OpenSource_Tangram);
    }
}