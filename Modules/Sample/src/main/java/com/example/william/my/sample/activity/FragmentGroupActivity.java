package com.example.william.my.sample.activity;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.fragment.PrimaryDarkFragment;
import com.example.william.my.module.fragment.PrimaryFragment;
import com.example.william.my.module.fragment.PrimaryLightFragment;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.sample.R;

/**
 * add show hide
 * 1. onAttach -> 1. onCreate -> 2. onAttach -> 2. onCreate -> 3. onAttach -> 3. onCreate
 * 1. onActivityCreated -> 2. onActivityCreated -> 3. onActivityCreated
 * 2. onHiddenChanged -> 3. onHiddenChanged
 * 1. onStart -> 2. onStart -> 3. onStart
 * 1. onResume -> 2. onResume -> 3. onResume
 * <p>
 * 之后的切换只执行 onHiddenChanged方法
 */
@Route(path = ARouterPath.Sample.Sample_FragmentGroup)
public class FragmentGroupActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private FragmentTransaction mTransaction;
    private final String[] mTitle = new String[]{"首页", "列表", "消息"};
    private final int[] mButtons = new int[]{R.id.fragment_button1, R.id.fragment_button2, R.id.fragment_button3};
    private final Fragment[] mFragments = new Fragment[]{new PrimaryFragment(), new PrimaryDarkFragment(), new PrimaryLightFragment()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_fragment_group);

        initFragment(savedInstanceState);

        RadioGroup mRadioGroup = findViewById(R.id.fragment_navigate);
        mRadioGroup.setOnCheckedChangeListener(this);
        //mRadioGroup.check(R.id.fragment_button1);

        RadioButton mRadioButton = findViewById(mButtons[0]);
        mRadioButton.setChecked(true);

        for (int i = 0; i < mButtons.length; i++) {
            RadioButton radioButton = findViewById(mButtons[i]);
            radioButton.setText(mTitle[i]);
        }
        //initRadioButtons();
    }

    private void initFragment(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            for (int i = 0; i < mFragments.length; i++) {
                mFragments[i] = getSupportFragmentManager().findFragmentByTag(mTitle[i]);
            }
        } else {
            removeAllFragments();

            mTransaction = getSupportFragmentManager().beginTransaction();
            for (int i = 0; i < mFragments.length; i++) {
                mTransaction.add(R.id.fragment_frameLayout, mFragments[i], mTitle[i]);
                if (i == 0) {
                    mTransaction.setMaxLifecycle(mFragments[i], Lifecycle.State.RESUMED);
                } else {
                    mTransaction.setMaxLifecycle(mFragments[i], Lifecycle.State.STARTED);
                }
            }
            mTransaction.commit();
        }
    }

    private void removeAllFragments() {

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
        mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.show(mFragments[position]);
        mTransaction.setMaxLifecycle(mFragments[position], Lifecycle.State.RESUMED);

        for (Fragment fragment : mFragments) {
            if (fragment != mFragments[position]) {
                mTransaction.hide(fragment);
                mTransaction.setMaxLifecycle(fragment, Lifecycle.State.STARTED);
            }
        }

        mTransaction.commitAllowingStateLoss();
    }

    private void initRadioButtons() {
        for (int radioButton : mButtons) {
            RadioButton mRadioButton = findViewById(radioButton);
            //获得每个drawable对象
            Drawable[] drawables = mRadioButton.getCompoundDrawables();
            // 获得一个矩阵大小，图片宽高的 1/4
            Rect rect = new Rect(0, 0, drawables[1].getMinimumWidth() / 5, drawables[1].getMinimumHeight() / 5);
            //给图片设置矩阵大小
            drawables[1].setBounds(rect);
            // 设置给按钮的图片
            mRadioButton.setCompoundDrawables(null, drawables[1], null, null);
        }
    }
}
