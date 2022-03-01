package com.example.william.my.module.opensource.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.fragment.PrimaryFragment;
import com.example.william.my.module.fragment.PrimaryVarFragment;
import com.example.william.my.module.opensource.R;
import com.example.william.my.module.router.ARouterPath;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

/**
 * https://github.com/H07000223/FlycoTabLayout
 */
@Route(path = ARouterPath.OpenSource.OpenSource_FlycoTabLayout)
public class FlycoTabLayoutActivity extends BaseActivity implements OnTabSelectListener {

    private final String[] mTitles = {"热门", "iOS", "Android"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_activity_flyco_tab_layout);

        SlidingTabLayout mTabLayout = findViewById(R.id.sliding_tab);
        ViewPager mViewPager = findViewById(R.id.sliding_viewPager);

        ArrayList<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(new PrimaryFragment());
        mFragmentList.add(new PrimaryVarFragment());
        mFragmentList.add(new PrimaryVarFragment());

        mTabLayout.setViewPager(mViewPager, mTitles, this, mFragmentList);

        mTabLayout.setOnTabSelectListener(this);
    }

    @Override
    public void onTabSelect(int position) {

    }

    @Override
    public void onTabReselect(int position) {

    }
}