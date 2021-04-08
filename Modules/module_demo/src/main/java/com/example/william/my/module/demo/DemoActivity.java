package com.example.william.my.module.demo;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseListActivity;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.Demo.Demo)
public class DemoActivity extends BaseListActivity {

    @Override
    protected void initData() {
        super.initData();
        mMap.put("AnimatorActivity", ARouterPath.Demo.Demo_Animator);
        mMap.put("AsyncTaskActivity", ARouterPath.Demo.Demo_AsyncTask);
        mMap.put("BehaviorActivity", ARouterPath.Demo.Demo_Behavior);
        mMap.put("BroadcastActivity", ARouterPath.Demo.Demo_Broadcast);
        mMap.put("FloatWindowActivity", ARouterPath.Demo.Demo_FloatWindow);
        mMap.put("JobSchedulerActivity", ARouterPath.Demo.Demo_JobScheduler);
        mMap.put("MessengerActivity", ARouterPath.Demo.Demo_Messenger);
        mMap.put("NetworkStatusActivity", ARouterPath.Demo.Demo_NetworkStatus);
        mMap.put("PermissionActivity", ARouterPath.Demo.Demo_Permission);
        mMap.put("PicCropActivity", ARouterPath.Demo.Demo_PicCrop);
        mMap.put("ServiceActivity", ARouterPath.Demo.Demo_Service);
        mMap.put("TransitionActivity", ARouterPath.Demo.Demo_Transition);
        mMap.put("TurntableActivity", ARouterPath.Demo.Demo_Turntable);
        mMap.put("TypefaceActivity", ARouterPath.Demo.Demo_Typeface);
    }
}