package com.example.william.my.module.jetpack.activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.bean.data.ArticleDataBean;
import com.example.william.my.core.retrofit.loading.LoadingTip;
import com.example.william.my.core.retrofit.observer.WithLoadingTipObserver;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.jetpack.model.ArticleViewModel;
import com.example.william.my.module.router.ARouterPath;
import com.google.gson.Gson;

/**
 * https://developer.android.google.cn/topic/libraries/architecture/livedata
 * https://developer.android.google.cn/topic/libraries/architecture/viewmodel
 */
@Route(path = ARouterPath.JetPack.JetPack_LiveData)
public class LiveDataActivity extends BaseResponseActivity implements LoadingTip.LoadingTipListener {

    private LoadingTip mLoadingTip;
    private ArticleViewModel mViewModel;

    @Override
    public void initView() {
        super.initView();

        initViewModel();
    }

    private void initViewModel() {
        mLoadingTip = LoadingTip.addLoadingTipWithTopBar(this);
        mLoadingTip.setOnReloadListener(this);

        mViewModel = new ViewModelProvider(this).get(ArticleViewModel.class);
        mViewModel.getArticle().observe(this, new WithLoadingTipObserver<ArticleDataBean>(mLoadingTip, "getArticle") {
            @Override
            protected void onResponse(@NonNull ArticleDataBean response) {
                showResponse(new Gson().toJson(response));
            }
        });
    }

    @Override
    public void reload() {
        mViewModel.request();
    }
}
