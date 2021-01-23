package com.example.william.my.module.fragment;

import android.util.Log;
import android.view.View;

import com.example.william.my.library.fragment.LazyFragment;

public class BaseFragment extends LazyFragment {

    public final String TAG = this.getClass().getSimpleName();

    @Override
    protected int createView() {
        return 0;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void lazyInit() {
        Log.e(TAG, "lazyInit");
    }

    @Override
    public void onResume() {
        super.onResume();
        // 使用新版本适配时，当 Fragment 显示执行 onResume()
        Log.e(TAG, "onResume");
    }
}
