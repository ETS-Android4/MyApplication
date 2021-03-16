package com.example.william.my.library.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(createView(), container, false);
    }

    /**
     * 在此方法内初始化控件
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view, savedInstanceState);
    }

    protected abstract int createView();

    protected abstract void initView(View view, Bundle state);

    protected abstract void lazyInit();

}