package com.example.william.my.module.jetpack.activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.network.retrofit.loading.LoadingTip;
import com.example.william.my.core.network.retrofit.observer.WithLoadingTipObserver;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.bean.BannerDetailData;
import com.example.william.my.module.jetpack.model.LoginViewModel;
import com.example.william.my.module.router.ARouterPath;
import com.google.gson.Gson;

import java.util.List;

/**
 * https://developer.android.google.cn/topic/libraries/architecture/livedata
 * https://developer.android.google.cn/topic/libraries/architecture/viewmodel
 */
@Route(path = ARouterPath.JetPack.JetPack_LiveData)
public class LiveDataActivity extends BaseResponseActivity implements LoadingTip.LoadingTipListener {

    private LoadingTip mLoadingTip;
    private LoginViewModel mViewModel;

    @Override
    public void initView() {
        super.initView();

        initViewModel();
    }

    private void initViewModel() {
        mLoadingTip = LoadingTip.addLoadingTipWithTopBar(this);
        mLoadingTip.setOnReloadListener(this);

        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // List<MovieBean>
        //mViewModel.getBannersBean().observe(this, new WithLoadingTipObserver<List<BannerBean>>(mLoadingTip) {
        //    @Override
        //    protected void onResponse(List<BannerBean> response) {
        //        showResponse(new Gson().toJson(response));
        //    }
        //});

        // List<MovieData>
        mViewModel.getBannersData().observe(this, new WithLoadingTipObserver<List<BannerDetailData>>(mLoadingTip) {
            @Override
            protected void onResponse(@NonNull List<BannerDetailData> response) {
                showResponse(new Gson().toJson(response));
            }
        });
    }

    @Override
    public void reload() {
        mViewModel.request();
    }
}
