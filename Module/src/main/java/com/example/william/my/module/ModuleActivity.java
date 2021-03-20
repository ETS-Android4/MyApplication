package com.example.william.my.module;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseListActivity;
import com.example.william.my.module.event.DefaultSmartEventBus;
import com.example.william.my.module.event.MessageEvent;
import com.example.william.my.module.event.SmartMessageEvent;
import com.example.william.my.module.router.ARouterPath;
import com.jeremyliao.liveeventbus.LiveEventBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 文件模板：
 * File => Settings => Editor => File and Code Templates
 * 开发规范：
 * Java文件命名：驼峰，资源命名：下划线
 * 命名规则：
 * 1. layout 文件的命名方
 * Activity 的 layout 以 module_activity 开头
 * Fragment 的 layout 以 module_fragment 开头
 * Dialog 的 layout 以 module_dialog 开头
 * include 的 layout 以 module_include 开头
 * ListView 的行 layout 以 module_list_item 开头
 * RecyclerView 的 item layout 以 module_recycle_item 开头
 * GridView 的行 layout 以 module_grid_item 开头
 * <p>
 * 2. drawable
 * 模块名_业务功能描述_控件描述_控件状态限定词 ： module_login_btn_pressed
 * <p>
 * 3. anim
 * 模块名_逻辑名称_[方向|序号] ：module_fade_in , module_push_down_in (动画+方向)
 * 模块名_功能命名_序号 ： module_loading_grey_001.
 * <p>
 * 4. color
 * 模块名_逻辑名称_颜色 : module_btn_bg_color
 * <p>
 * 5. dimen
 * 模块名_描述信息 : module_horizontal_line_height
 * <p>
 * 6. string
 * 模块名_逻辑名称 : module_login_tips
 * <p>
 * 7. Id 资源
 * (页面名称_控件名称_描述信息 ： login_btn_login)
 * <p>
 * 8. ApiService 实现方法 : find / getList
 * <p>
 * 9. Activity 内api调用方法以 Api 结尾 ：loadingAppListApi
 * <p>
 * mipmap会自动选择更加合适的图片仅在launcher中有效
 * <p>
 * Typo: In word -> application-level
 */
@Route(path = ARouterPath.Module)
public class ModuleActivity extends BaseListActivity {

    @Autowired(name = "param_key")
    public String mModuleParam;

    @Override
    protected void initData() {
        super.initData();
        mMap.put("AndroidUtilActivity", ARouterPath.Util.Util);

        mMap.put("NetWorkActivity", ARouterPath.NetWork.NetWork);
        mMap.put("CustomViewActivity", ARouterPath.CustomView.CustomView);

        mMap.put("JetPackActivity", ARouterPath.JetPack.JetPack);
        mMap.put("OpenSourceActivity", ARouterPath.OpenSource.OpenSource);
        mMap.put("SampleActivity", ARouterPath.Sample.Sample);

        //Kotlin
        //mMap.put("kotlin", "kotlin");
        mMap.put("KotlinActivity", ARouterPath.Kotlin.Kotlin);
        mMap.put("KotlinFlowActivity", ARouterPath.Kotlin.Kotlin_FLow);
        mMap.put("KotlinDataStoreActivity", ARouterPath.Kotlin.Kotlin_DataStore);
        mMap.put("KotlinPagingActivity", ARouterPath.Kotlin.Kotlin_Paging);

        //Flutter
        //mMap.put("Flutter", "Flutter");
        //mMap.put("FlutterActivity", ARouterPath.Flutter.Flutter);

        LiveEventBus
                .get("some_key", String.class)
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        Toast.makeText(ModuleActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });

        DefaultSmartEventBus
                .event1()
                .observe(this, new Observer<SmartMessageEvent>() {
                    @Override
                    public void onChanged(SmartMessageEvent event) {
                        Toast.makeText(ModuleActivity.this, event.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Toast.makeText(ModuleActivity.this, event.getMessage(), Toast.LENGTH_SHORT).show();
    }
}