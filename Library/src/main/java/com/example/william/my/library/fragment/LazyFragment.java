package com.example.william.my.library.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class LazyFragment extends Fragment {

    private boolean isLoaded = false; // 是否已加载

    private boolean isVisibleToUser = false; // 视图是否可见
    private boolean isCallOnResume = false; // 视图是否可交互

    /**
     * 是否调用了setUserVisibleHint()方法
     * add show hide 不调用此方法，不执行懒加载
     */
    private boolean isCallUserVisibleHint = false;// add show hide 不调用此方法

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(createView(), container, false);
        initView(mRootView);
        return mRootView;
    }

    protected abstract int createView();

    protected abstract void initView(View view);

    protected abstract void lazyInit();

}
