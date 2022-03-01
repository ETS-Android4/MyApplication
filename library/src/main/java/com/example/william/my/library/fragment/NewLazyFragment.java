package com.example.william.my.library.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

/**
 * FragmentTransaction.setMaxLifecycle()
 */
public abstract class NewLazyFragment extends Fragment {

    private boolean isLoaded = false;

    @Override
    public void onResume() {
        super.onResume();
        //增加了Fragment是否可见的判断
        if (!isLoaded && !isHidden()) {
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