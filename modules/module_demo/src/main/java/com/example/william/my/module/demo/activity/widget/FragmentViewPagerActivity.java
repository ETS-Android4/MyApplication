package com.example.william.my.module.demo.activity.widget;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.demo.R;
import com.example.william.my.module.demo.adapter.ViewPagerFragmentAdapter;
import com.example.william.my.module.fragment.PrimaryFragment;
import com.example.william.my.module.fragment.PrimaryVarFragment;
import com.example.william.my.module.router.ARouterPath;

import java.util.Arrays;

@Route(path = ARouterPath.Demo.Demo_FragmentViewPager)
public class FragmentViewPagerActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private ViewPager mViewPager;
    private final String[] mTitle = new String[]{"首页", "列表", "消息"};
    private final int[] mButtons = new int[]{R.id.fragment_button1, R.id.fragment_button2, R.id.fragment_button3};
    private final Fragment[] mFragments = new Fragment[]{
            new PrimaryFragment(),
            new PrimaryVarFragment(),
            new PrimaryVarFragment()};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity_fragment_view_pager);

        initFragment();

        initRadioButtons();
    }

    private void initFragment() {
        mViewPager = findViewById(R.id.fragment_viewPager);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(new ViewPagerFragmentAdapter(getSupportFragmentManager(), Arrays.asList(mFragments), true));
    }

    private void initRadioButtons() {
        RadioGroup mRadioGroup = findViewById(R.id.fragment_navigate);
        mRadioGroup.setOnCheckedChangeListener(this);
        mRadioGroup.check(mButtons[0]);

        for (int i = 0; i < mButtons.length; i++) {
            RadioButton radioButton = findViewById(mButtons[i]);
            radioButton.setText(mTitle[i]);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i = 0; i < mFragments.length; i++) {
            if (mButtons[i] == checkedId) {
                switchTo(i);
            }
        }
    }

    private void switchTo(int position) {
        mViewPager.setCurrentItem(position);
    }
}