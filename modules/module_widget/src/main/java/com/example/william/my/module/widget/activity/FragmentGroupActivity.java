package com.example.william.my.module.widget.activity;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.fragment.BasicRecyclerFragment;
import com.example.william.my.module.fragment.PrimaryFragment;
import com.example.william.my.module.fragment.PrimaryVarFragment;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.widget.R;

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
@Route(path = ARouterPath.Widget.Widget_FragmentGroup)
public class FragmentGroupActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private FragmentTransaction mTransaction;
    private final String[] mTitle = new String[]{"首页", "列表", "消息"};
    private final int[] mButtons = new int[]{R.id.fragment_button1, R.id.fragment_button2, R.id.fragment_button3};
    private final Fragment[] mFragments = new Fragment[]{
            new PrimaryFragment(),
            new PrimaryVarFragment(),
            new BasicRecyclerFragment()};

    private final boolean isNew = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_activity_fragment_group);

        initFragment(savedInstanceState);

        initRadioButtons();
    }

    /**
     * setMaxLifecycle 限制生命周期
     */
    private void initFragment(Bundle savedInstanceState) {
        mTransaction = getSupportFragmentManager().beginTransaction();

        if (savedInstanceState != null) {
            for (int i = 0; i < mFragments.length; i++) {
                mFragments[i] = getSupportFragmentManager().findFragmentByTag(mTitle[i]);
            }
        } else {
            removeAllFragments();
            for (int i = 0; i < mFragments.length; i++) {
                mTransaction.add(R.id.fragment_frameLayout, mFragments[i], mTitle[i]);
                if (isNew) {
                    if (i == 0) {
                        mTransaction.setMaxLifecycle(mFragments[i], Lifecycle.State.RESUMED);
                    } else {
                        mTransaction.setMaxLifecycle(mFragments[i], Lifecycle.State.STARTED);
                    }
                }
            }
            mTransaction.commit();
        }
    }

    private void removeAllFragments() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            mTransaction.remove(fragment).commit();
        }
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

    /**
     * setMaxLifecycle 限制生命周期
     */
    private void switchTo(int position) {
        mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.show(mFragments[position]);
        if (isNew) {
            mTransaction.setMaxLifecycle(mFragments[position], Lifecycle.State.RESUMED);
        }

        for (Fragment fragment : mFragments) {
            if (fragment != mFragments[position]) {
                mTransaction.hide(fragment);
                if (isNew) {
                    mTransaction.setMaxLifecycle(fragment, Lifecycle.State.STARTED);
                }
            }
        }
        mTransaction.commitAllowingStateLoss();
    }
}
