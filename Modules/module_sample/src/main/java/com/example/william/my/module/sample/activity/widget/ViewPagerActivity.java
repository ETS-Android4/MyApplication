package com.example.william.my.module.sample.activity.widget;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.fragment.PrimaryDarkFragment;
import com.example.william.my.module.fragment.PrimaryFragment;
import com.example.william.my.module.fragment.PrimaryLightFragment;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.sample.R;
import com.example.william.my.module.sample.adapter.ViewPagerAdapter;
import com.example.william.my.module.sample.adapter.ViewPagerFragmentAdapter;

import java.util.Arrays;

@Route(path = ARouterPath.Sample.Sample_ViewPager)
public class ViewPagerActivity extends BaseActivity {

    private final String[] mData = new String[]{"fragment_primary", "fragment_primary_dark", "fragment_primary_light"};
    private final Fragment[] mFragments = new Fragment[]{new PrimaryFragment(), new PrimaryDarkFragment(), new PrimaryLightFragment()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_viewpager);

        ViewPager mPageView = findViewById(R.id.page_view);
        ViewPager mPageFragment = findViewById(R.id.page_fragment);

        mPageView.setAdapter(new ViewPagerAdapter(Arrays.asList(mData)));
        mPageFragment.setAdapter(new ViewPagerFragmentAdapter(getSupportFragmentManager(), Arrays.asList(mFragments)));
    }
}
