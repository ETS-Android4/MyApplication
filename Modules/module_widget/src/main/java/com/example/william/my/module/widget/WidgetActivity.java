package com.example.william.my.module.widget;

import android.os.Looper;
import android.os.MessageQueue;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseListActivity;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.Widget.Widget)
public class WidgetActivity extends BaseListActivity {

    @Override
    protected void initData() {
        super.initData();

        mMap.put("AnimatorActivity", ARouterPath.Widget.Widget_Animator);
        mMap.put("AppBarActivity", ARouterPath.Widget.Widget_AppBar);
        mMap.put("BehaviorActivity", ARouterPath.Widget.Widget_Behavior);
        mMap.put("DialogActivity", ARouterPath.Widget.Widget_Dialog);
        mMap.put("FlexBoxActivity", ARouterPath.Widget.Widget_FlexBox);
        mMap.put("FragmentActivity", ARouterPath.Widget.Widget_Fragment);
        mMap.put("FragmentGroupActivity", ARouterPath.Widget.Widget_FragmentGroup);
        mMap.put("FragmentTabHostActivity", ARouterPath.Widget.Widget_FragmentTabHost);
        mMap.put("FragmentViewPagerActivity", ARouterPath.Widget.Widget_FragmentViewPager);
        mMap.put("NotificationActivity", ARouterPath.Widget.Widget_Notification);
        mMap.put("RecyclerViewActivity", ARouterPath.Widget.Widget_RecyclerView);
        mMap.put("RecyclerView2Activity", ARouterPath.Widget.Widget_RecyclerView2);
        mMap.put("ViewFlipperActivity", ARouterPath.Widget.Widget_ViewFlipper);
        mMap.put("ViewPagerActivity", ARouterPath.Widget.Widget_ViewPager);
        mMap.put("ViewPager2Activity", ARouterPath.Widget.Widget_ViewPager2);
        mMap.put("WebViewActivity", ARouterPath.Widget.Widget_WebView);

        mMap.put(" ", " ");
        mMap.put("AsyncTaskActivity", ARouterPath.Widget.Widget_AsyncTask);
        mMap.put("BroadcastActivity", ARouterPath.Widget.Widget_Broadcast);
        mMap.put("FloatWindowActivity", ARouterPath.Widget.Widget_FloatWindow);
        mMap.put("JobSchedulerActivity", ARouterPath.Widget.Widget_JobScheduler);
        mMap.put("MessengerActivity", ARouterPath.Widget.Widget_Messenger);
        mMap.put("NetworkStatusActivity", ARouterPath.Widget.Widget_NetworkStatus);
        mMap.put("PermissionActivity", ARouterPath.Widget.Widget_Permission);
        mMap.put("PicCropActivity", ARouterPath.Widget.Widget_PicCrop);
        mMap.put("ServiceActivity", ARouterPath.Widget.Widget_Service);
        mMap.put("TransitionActivity", ARouterPath.Widget.Widget_Transition);
        mMap.put("TurntableActivity", ARouterPath.Widget.Widget_Turntable);
        mMap.put("TypefaceActivity", ARouterPath.Widget.Widget_Typeface);

        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                Log.e(TAG, "addIdleHandler: queueIdle " + Thread.currentThread().getName());
                return false;
            }
        });
    }
}