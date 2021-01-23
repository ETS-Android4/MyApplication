package com.example.william.my.module.fragment;

import android.util.Log;
import android.view.View;

import com.example.william.my.library.fragment.LazyFragment;

/**
 * 当使用旧方式适配时，Fragment显示通过setUserVisibleHint()方法
 * 当使用新方式适配时，Fragment显示通过onResume()方法
 */
public class BaseFragment extends LazyFragment {

    public final String TAG = this.getClass().getSimpleName();

    @Override
    protected int createView() {
        return 0;
    }

    @Override
    protected void initView(View view) {

    }

    /**
     * 懒加载，只执行一次
     */
    @Override
    protected void lazyInit() {
        Log.e(TAG, "lazyInit");
    }

    /**
     * 当使用新方式适配时，不执行此方法
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.e(TAG, "setUserVisibleHint");
        }
    }

    /**
     * 当使用新方式适配时，只有当前Fragment执行onResume()，其他Fragment声明周期限制在onStart()
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
    }
}
