package com.example.william.my.module.sample;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseRecyclerActivity;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.router.item.RouterItem;

import java.util.ArrayList;

/**
 * https://developer.android.google.cn/sample/guide
 * https://developer.android.google.cn/topic/libraries/architecture/index.html
 */
@Route(path = ARouterPath.Sample.Sample)
public class SampleActivity extends BaseRecyclerActivity {

    protected ArrayList<RouterItem> buildRouter() {
        ArrayList<RouterItem> routerItems = new ArrayList<>();
        routerItems.add(new RouterItem("BindActivity", ARouterPath.Sample.Sample_Bind));
        routerItems.add(new RouterItem("NavigationActivity", ARouterPath.Sample.Sample_Navigation));
        routerItems.add(new RouterItem("RoomActivity", ARouterPath.Sample.Sample_Room));
        routerItems.add(new RouterItem("ViewModelActivity", ARouterPath.Sample.Sample_ViewModel));
        routerItems.add(new RouterItem("WorkManagerActivity", ARouterPath.Sample.Sample_WorkManager));
        routerItems.add(new RouterItem(" ", " "));
        routerItems.add(new RouterItem("DataStoreActivity", ARouterPath.Sample.Sample_DataStore));
        routerItems.add(new RouterItem("PagingActivity", ARouterPath.Sample.Sample_Paging));
        routerItems.add(new RouterItem("ResultActivity", ARouterPath.Sample.Sample_Result));
        routerItems.add(new RouterItem(" ", " "));
        routerItems.add(new RouterItem("CoilActivity", ARouterPath.Sample.Sample_Coil));
        routerItems.add(new RouterItem("CoroutinesActivity", ARouterPath.Sample.Sample_Coroutines));
        routerItems.add(new RouterItem("FLowActivity", ARouterPath.Sample.Sample_StateFlow));
        routerItems.add(new RouterItem("LiveDataActivity", ARouterPath.Sample.Sample_LiveData));
        routerItems.add(new RouterItem(" ", " "));
        routerItems.add(new RouterItem("MVPActivity", ARouterPath.Sample.Sample_MVP));
        routerItems.add(new RouterItem("MVVMActivity", ARouterPath.Sample.Sample_MVVM));
        routerItems.add(new RouterItem("MVIActivity", ARouterPath.Sample.Sample_MVI));
        routerItems.add(new RouterItem("DataBingActivity", ARouterPath.Sample.Sample_BindingAdapter));
        return routerItems;
    }
}