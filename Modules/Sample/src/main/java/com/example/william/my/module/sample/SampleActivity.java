package com.example.william.my.module.sample;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseListActivity;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.Sample.Sample)
public class SampleActivity extends BaseListActivity {

    @Override
    protected void initData() {
        super.initData();

        mMap.put("AppBarActivity", ARouterPath.Sample.Sample_AppBar);
        mMap.put("DialogActivity", ARouterPath.Sample.Sample_Dialog);
        mMap.put("FragmentActivity", ARouterPath.Sample.Sample_Fragment);
        mMap.put("FragmentGroupActivity", ARouterPath.Sample.Sample_FragmentGroup);
        mMap.put("FragmentTabHostActivity", ARouterPath.Sample.Sample_FragmentTabHost);
        mMap.put("FragmentViewPagerActivity", ARouterPath.Sample.Sample_FragmentViewPager);
        mMap.put("NotificationActivity", ARouterPath.Sample.Sample_Notification);
        mMap.put("RecyclerViewActivity", ARouterPath.Sample.Sample_RecyclerView);
        mMap.put("ViewPagerActivity", ARouterPath.Sample.Sample_ViewPager);
        mMap.put("ViewPager2Activity", ARouterPath.Sample.Sample_ViewPager2);
        mMap.put("WebViewActivity", ARouterPath.Sample.Sample_WebView);

        mMap.put("AnimatorActivity", ARouterPath.Sample.Sample_Animator);
        mMap.put("AsyncTaskActivity", ARouterPath.Sample.Sample_AsyncTask);
        mMap.put("BroadcastActivity", ARouterPath.Sample.Sample_Broadcast);
        mMap.put("MessengerActivity", ARouterPath.Sample.Sample_Messenger);
        mMap.put("NetworkStatusActivity", ARouterPath.Sample.Sample_NetworkStatus);
        mMap.put("PermissionActivity", ARouterPath.Sample.Sample_Permission);
        mMap.put("PicCropActivity", ARouterPath.Sample.Sample_PicCrop);
        mMap.put("ServiceActivity", ARouterPath.Sample.Sample_Service);
        mMap.put("TransitionActivity", ARouterPath.Sample.Sample_Transition);
        mMap.put("TurntableActivity", ARouterPath.Sample.Sample_Turntable);
        mMap.put("TypefaceActivity", ARouterPath.Sample.Sample_Typeface);
    }
}