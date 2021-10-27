package com.example.william.my.library.base;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;

import com.example.william.my.library.presenter.IBasePresenter;
import com.example.william.my.library.view.IBaseView;
import com.trello.lifecycle4.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle4.LifecycleProvider;

import java.lang.reflect.Constructor;

public abstract class BaseMvpActivity<T extends IBasePresenter, V extends IBaseView<T>> extends BaseActivity {

    private final String TAG = this.getClass().getSimpleName();

    protected T mPresenter;

    private LifecycleProvider<Lifecycle.Event> provider =
            AndroidLifecycle.createLifecycleProvider(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
    }

    /**
     * 返回逻辑处理的具体类型.
     */
    protected abstract Class<T> getPresenterClass();

    /**
     * 返回View层的接口类.
     */
    protected abstract Class<V> getViewClass();

    /**
     * 初始化Presenter
     */
    protected void initPresenter() {
        try {
            Constructor<T> constructor = getPresenterClass().getConstructor(getViewClass());
            mPresenter = constructor.newInstance(this);
        } catch (Exception e) {
            Log.e(TAG, "Init presenter throw an error : [" + e.getMessage() + "]");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.clear();
        }
    }
}
