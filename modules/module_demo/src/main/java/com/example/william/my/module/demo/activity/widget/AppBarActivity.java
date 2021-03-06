package com.example.william.my.module.demo.activity.widget;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.demo.R;
import com.example.william.my.module.demo.adapter.ViewPagerFragmentAdapter;
import com.example.william.my.module.fragment.PrimaryFragment;
import com.example.william.my.module.fragment.PrimaryVarFragment;
import com.example.william.my.module.router.ARouterPath;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.Arrays;

@Route(path = ARouterPath.Demo.Demo_AppBar)
public class AppBarActivity extends BaseActivity {

    private ViewPager app_bar_viewPager;

    private final String[] mTitles = new String[]{"Tab 1", "Tab 2", "Tab 3"};

    private final Fragment[] mFragments = new Fragment[]{
            new PrimaryFragment(),
            new PrimaryVarFragment(),
            new PrimaryVarFragment()};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity_app_bar);

        Toolbar toolbar = findViewById(R.id.appbar_toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = findViewById(R.id.appbar_toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        TabLayout app_bar_tabs = findViewById(R.id.appbar_tabs);
        app_bar_viewPager = findViewById(R.id.appbar_viewPager);
        app_bar_viewPager.setAdapter(new ViewPagerFragmentAdapter(getSupportFragmentManager(), Arrays.asList(mFragments), Arrays.asList(mTitles), true));

        //设置TabLayout可滚动，保证Tab数量过多时也可正常显示
        app_bar_tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        //设置TabLayout选中Tab下划线颜色
        app_bar_tabs.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        //两个参数分别对应Tab未选中的文字颜色和选中的文字颜色
        app_bar_tabs.setTabTextColors(ContextCompat.getColor(this, R.color.colorPrimary), ContextCompat.getColor(this, R.color.colorPrimaryDark));
        //绑定ViewPager
        app_bar_tabs.setupWithViewPager(app_bar_viewPager);
        //设置TabLayout的布局方式（GRAVITY_FILL、GRAVITY_CENTER）
        app_bar_tabs.setTabMode(TabLayout.MODE_FIXED);
        app_bar_tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        //设置TabLayout的选择监听
        app_bar_tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                app_bar_viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            /*
             * 重复点击Tab时回调
             */
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}