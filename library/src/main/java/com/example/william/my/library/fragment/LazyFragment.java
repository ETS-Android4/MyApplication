package com.example.william.my.library.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public abstract class LazyFragment extends Fragment {

    /**
     * 是否已加载
     */
    private boolean isLoaded = false;

    /**
     * 视图是否可见
     */
    private boolean isVisibleToUser = false;
    /**
     * 视图是否可交互
     */
    private boolean isCallOnResume = false;

    /**
     * 是否调用了setUserVisibleHint()方法
     * add show hide 不调用此方法，不执行懒加载
     */
    private boolean isCallUserVisibleHint = false;

    @Override
    public void onResume() {
        super.onResume();
        this.isCallOnResume = true;

        // 如未执行setUserVisibleHint，为add show hide模式
        // 使用isHidden()方法为isVisibleToUser赋值
        if (!isCallUserVisibleHint) {
            isVisibleToUser = !isHidden();
        }
        judgeLazyInit();
    }

    /**
     * add show hide
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.isVisibleToUser = !hidden;
        judgeLazyInit();
    }

    /**
     * ViewPager
     */
    @SuppressWarnings("deprecation")
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        this.isCallUserVisibleHint = true;
        judgeLazyInit();
    }

    private void judgeLazyInit() {
        if (!isLoaded && isVisibleToUser && isCallOnResume) {
            isLoaded = true;
            lazyInit();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getLayout() != 0) {
            return inflater.inflate(getLayout(), container, false);
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    /**
     * 在此方法内初始化控件
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view, savedInstanceState);
    }

    /**
     * 布局 id
     *
     * @return
     */
    protected abstract int getLayout();

    /**
     * 初始化布局
     *
     * @param view
     * @param state
     */
    protected abstract void initView(View view, Bundle state);

    /**
     * 懒加载初始化
     */
    protected abstract void lazyInit();

}
