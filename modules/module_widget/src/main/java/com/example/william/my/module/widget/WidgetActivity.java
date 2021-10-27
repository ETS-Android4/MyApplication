package com.example.william.my.module.widget;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseListActivity;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.Widget.Widget)
public class WidgetActivity extends BaseListActivity {

    @Override
    protected void initData() {
        super.initData();
        mMap.put("AppBarActivity", ARouterPath.Widget.Widget_AppBar);
        mMap.put("DialogActivity", ARouterPath.Widget.Widget_Dialog);
        mMap.put("FlexBoxActivity", ARouterPath.Widget.Widget_FlexBox);
        mMap.put("FragmentActivity", ARouterPath.Widget.Widget_Fragment);
        mMap.put("FragmentGroupActivity", ARouterPath.Widget.Widget_FragmentGroup);
        mMap.put("FragmentTabHostActivity", ARouterPath.Widget.Widget_FragmentTabHost);
        mMap.put("FragmentViewPagerActivity", ARouterPath.Widget.Widget_FragmentViewPager);
        mMap.put("NotificationActivity", ARouterPath.Widget.Widget_Notification);
        mMap.put("RecyclerViewActivity", ARouterPath.Widget.Widget_RecyclerView);
        mMap.put("RecyclerView2Activity", ARouterPath.Widget.Widget_RecyclerViewNested);
        mMap.put("ViewFlipperActivity", ARouterPath.Widget.Widget_ViewFlipper);
        mMap.put("ViewPagerActivity", ARouterPath.Widget.Widget_ViewPager);
        mMap.put("ViewPager2Activity", ARouterPath.Widget.Widget_ViewPager2);
        mMap.put("WebViewActivity", ARouterPath.Widget.Widget_WebView);
    }
}