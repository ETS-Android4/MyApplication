package com.example.william.my.module.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.william.my.library.fragment.LazyFragment;

/**
 * add show hide
 * 当使用旧方式适配时，Fragment通过onHiddenChanged()方法
 * 当使用新方式适配时，Fragment通过onHiddenChanged()方法或者onResume()方法()
 * ViewPager
 * 当使用旧方式适配时，Fragment通过setUserVisibleHint()方法
 * 当使用新方式适配时，Fragment通过setUserVisibleHint()方法
 * ViewPager2
 * 当使用旧方式适配时，Fragment通过onResume()，onPause()方法
 * 当使用新方式适配时，Fragment通过onResume()，onPause()方法
 */
public class BaseFragment extends LazyFragment {

    public final String TAG = this.getClass().getSimpleName();

    @Override
    protected int createView() {
        return 0;
    }

    /**
     * 在此方法内初始化控件
     */
    @Override
    protected void initView(View view, Bundle state) {

    }

    /**
     * 懒加载，只执行一次
     */
    @Override
    protected void lazyInit() {
        Log.e(TAG, "lazyInit");
    }

    /**
     * add show hide
     * 当使用旧方式适配时，执行此方法
     * 当使用新方式适配时，也执行此方法
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e(TAG, "onHiddenChanged : hidden : " + hidden);
    }

    /**
     * ViewPager
     * 执行此方法
     * ViewPager2
     * 不执行此方法
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e(TAG, "setUserVisibleHint : isVisibleToUser : " + isVisibleToUser);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
    }
}
