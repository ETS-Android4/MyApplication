package com.example.william.my.module.sample.app;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.william.my.core.network.base.RxRetrofitConfig;
import com.example.william.my.library.interfaces.IComponentApplication;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;

import org.jetbrains.annotations.NotNull;

public class SampleApp implements IComponentApplication {

    @Override
    public void init(Application application) {
        initRxRetrofit(application);
        initSmartRefreshLayout();
    }

    @Override
    public void initAsync(Application application) {

    }

    private void initRxRetrofit(Application application) {
        RxRetrofitConfig.init(application);
    }

    private void initSmartRefreshLayout() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NotNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicsHeader(context);
            }
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NotNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicsFooter(context);
            }
        });
    }
}
