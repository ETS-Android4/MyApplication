package com.example.william.my.module.custom;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseListActivity;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.CustomView.CustomView)
public class CustomViewActivity extends BaseListActivity {

    @Override
    protected void initData() {
        super.initData();
        mMap.put("BlurViewActivity", ARouterPath.CustomView.CustomView_BlurView);
        mMap.put("GalleryActivity", ARouterPath.CustomView.CustomView_Gallery);
        mMap.put("GestureLockActivity", ARouterPath.CustomView.CustomView_GestureLock);
        mMap.put("IosDialogActivity", ARouterPath.CustomView.CustomView_IosDialog);
        mMap.put("MarqueeViewActivity", ARouterPath.CustomView.CustomView_MarqueeView);
        mMap.put("ScrollPageActivity", ARouterPath.CustomView.CustomView_ScrollPage);
        mMap.put("SpinnerActivity", ARouterPath.CustomView.CustomView_Spinner);
        mMap.put("TitleBarActivity", ARouterPath.CustomView.CustomView_TitleBar);
    }
}