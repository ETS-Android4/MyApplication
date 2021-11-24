package com.example.william.my.module.demo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseFragmentActivity;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.router.fragment.RouterRecyclerFragment;
import com.example.william.my.module.router.item.RouterItem;

import java.util.ArrayList;

@Route(path = ARouterPath.Demo.Demo)
public class DemoActivity extends BaseFragmentActivity {

    @Override
    public Fragment setFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("router", buildRouter());
        RouterRecyclerFragment fragment = new RouterRecyclerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private ArrayList<RouterItem> buildRouter() {
        ArrayList<RouterItem> routerItems = new ArrayList<>();
        //
        routerItems.add(new RouterItem("AppBarActivity", ARouterPath.Demo.Demo_AppBar));
        routerItems.add(new RouterItem("DialogActivity", ARouterPath.Demo.Demo_Dialog));
        routerItems.add(new RouterItem("FlexBoxActivity", ARouterPath.Demo.Demo_FlexBox));
        routerItems.add(new RouterItem("FragmentActivity", ARouterPath.Demo.Demo_Fragment));
        routerItems.add(new RouterItem("FragmentGroupActivity", ARouterPath.Demo.Demo_FragmentGroup));
        routerItems.add(new RouterItem("FragmentTabHostActivity", ARouterPath.Demo.Demo_FragmentTabHost));
        routerItems.add(new RouterItem("FragmentViewPagerActivity", ARouterPath.Demo.Demo_FragmentViewPager));
        routerItems.add(new RouterItem("NotificationActivity", ARouterPath.Demo.Demo_Notification));
        routerItems.add(new RouterItem("RecyclerViewActivity", ARouterPath.Demo.Demo_RecyclerView));
        routerItems.add(new RouterItem("RecyclerView2Activity", ARouterPath.Demo.Demo_RecyclerViewNested));
        routerItems.add(new RouterItem("ViewFlipperActivity", ARouterPath.Demo.Demo_ViewFlipper));
        routerItems.add(new RouterItem("ViewPagerActivity", ARouterPath.Demo.Demo_ViewPager));
        routerItems.add(new RouterItem("ViewPager2Activity", ARouterPath.Demo.Demo_ViewPager2));
        routerItems.add(new RouterItem("WebViewActivity", ARouterPath.Demo.Demo_WebView));
        //
        routerItems.add(new RouterItem("BlurViewActivity", ARouterPath.Demo.Demo_BlurView));
        routerItems.add(new RouterItem("IosDialogActivity", ARouterPath.Demo.Demo_IosDialog));
        routerItems.add(new RouterItem("MarqueeViewActivity", ARouterPath.Demo.Demo_MarqueeView));
        routerItems.add(new RouterItem("SpinnerActivity", ARouterPath.Demo.Demo_Spinner));
        routerItems.add(new RouterItem("TitleBarActivity", ARouterPath.Demo.Demo_TitleBar));
        //
        routerItems.add(new RouterItem("AnimatorActivity", ARouterPath.Demo.Demo_Animator));
        routerItems.add(new RouterItem("AsyncTaskActivity", ARouterPath.Demo.Demo_AsyncTask));
        routerItems.add(new RouterItem("BroadcastActivity", ARouterPath.Demo.Demo_Broadcast));
        routerItems.add(new RouterItem("FloatWindowActivity", ARouterPath.Demo.Demo_FloatWindow));
        routerItems.add(new RouterItem("HookActivity", ARouterPath.Demo.Demo_Hook));
        routerItems.add(new RouterItem("JobSchedulerActivity", ARouterPath.Demo.Demo_JobScheduler));
        routerItems.add(new RouterItem("MessengerActivity", ARouterPath.Demo.Demo_Messenger));
        routerItems.add(new RouterItem("NetworkStatusActivity", ARouterPath.Demo.Demo_NetworkStatus));
        routerItems.add(new RouterItem("PermissionActivity", ARouterPath.Demo.Demo_Permission));
        routerItems.add(new RouterItem("PicCropActivity", ARouterPath.Demo.Demo_PicCrop));
        routerItems.add(new RouterItem("ServiceActivity", ARouterPath.Demo.Demo_Service));
        routerItems.add(new RouterItem("TouchEventActivity", ARouterPath.Demo.Demo_TouchEvent));
        routerItems.add(new RouterItem("TransitionActivity", ARouterPath.Demo.Demo_Transition));
        routerItems.add(new RouterItem("TransparentActivity", ARouterPath.Demo.Demo_Transparent));
        routerItems.add(new RouterItem("TurntableActivity", ARouterPath.Demo.Demo_Turntable));
        routerItems.add(new RouterItem("TypefaceActivity", ARouterPath.Demo.Demo_Typeface));
        return routerItems;
    }
}