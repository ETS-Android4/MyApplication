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
        View mRootView = inflater.inflate(createView(), container, false);
        initView(mRootView);
        return mRootView;
    }

    protected abstract int createView();

    protected abstract void initView(View view);

    protected abstract void lazyInit();

}