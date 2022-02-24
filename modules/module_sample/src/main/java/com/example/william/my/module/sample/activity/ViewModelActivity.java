package com.example.william.my.module.sample.activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.bean.data.ArticleDataBean;
import com.example.william.my.core.retrofit.loading.LoadingTip;
import com.example.william.my.core.retrofit.observer.WithLoadingTipObserver;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.sample.model.ViewModelViewModel;
import com.google.gson.Gson;

/**
 * https://developer.android.google.cn/topic/libraries/architecture/livedata
 * https://developer.android.google.cn/topic/libraries/architecture/viewmodel
 */
@Route(path = ARouterPath.Sample.Sample_ViewModel)
public class ViewModelActivity extends BaseResponseActivity implements LoadingTip.LoadingTipListener {

    private LoadingTip mLoadingTip;
    private ViewModelViewModel mViewModelViewModel;

    @Override
    public void initView() {
        super.initView();

        observeViewModel();
    }

    private void observeViewModel() {
        mLoadingTip = LoadingTip.addLoadingTipWithTopBar(this);
        mLoadingTip.setOnReloadListener(this);

        mViewModelViewModel = new ViewModelProvider(this).get(ViewModelViewModel.class);
        mViewModelViewModel.getArticle().observe(this, new WithLoadingTipObserver<ArticleDataBean>(mLoadingTip, "getArticle") {
            @Override
            protected void onResponse(@NonNull ArticleDataBean response) {
                showResponse(new Gson().toJson(response));
            }
        });
        reload();
    }

    @Override
    public void reload() {
        mViewModelViewModel.request();
    }
}
