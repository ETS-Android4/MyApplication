package com.example.william.my.sample.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.fragment.PrimaryDarkFragment;
import com.example.william.my.module.fragment.PrimaryFragment;
import com.example.william.my.module.fragment.PrimaryLightFragment;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.sample.R;
import com.example.william.my.sample.adapter.ViewPagerFragmentAdapter;

import java.util.Arrays;

@Route(path = ARouterPath.Sample.Sample_FragmentViewPager)
public class FragmentViewPagerActivity extends AppCompatActivity {

    private final String[] mTitle = new String[]{"首页", "列表", "消息"};
    private final int[] mButtons = new int[]{R.id.fragment_button1, R.id.fragment_button2, R.id.fragment_button3};
    private final Fragment[] mFragments = new Fragment[]{new PrimaryFragment(), new PrimaryDarkFragment(), new PrimaryLightFragment()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_fragment_view_pager);

        initFragment();
    }

    private void initFragment() {
        ViewPager mPageFragment = findViewById(R.id.fragment_viewPager);
        mPageFragment.setOffscreenPageLimit(4);
        mPageFragment.setAdapter(new ViewPagerFragmentAdapter(getSupportFragmentManager(), Arrays.asList(mFragments)));
    }
}