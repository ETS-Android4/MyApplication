package com.example.william.my.module.jetpack.activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.bean.data.BannerDetailBean;
import com.example.william.my.bean.data.BannerDetailData;
import com.example.william.my.core.retrofit.loading.LoadingTip;
import com.example.william.my.core.retrofit.observer.WithLoadingTipObserver;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.jetpack.model.BannerViewModel;
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
    private BannerViewModel mViewModel;

    @Override
    public void initView() {
        super.initView();

        initViewModel();
    }

    private void initViewModel() {
        mLoadingTip = LoadingTip.addLoadingTipWithTopBar(this);
        mLoadingTip.setOnReloadListener(this);

        mViewModel = new ViewModelProvider(this).get(BannerViewModel.class);

        //initBannerBean();

        initBannerData();
    }

    /**
     * List<BannerDetailBean>
     */
    private void initBannerBean() {
        mViewModel.getBannerBean().observe(this, new WithLoadingTipObserver<List<BannerDetailBean>>(mLoadingTip) {
            @Override
            protected void onResponse(@NonNull List<BannerDetailBean> response) {
                showResponse(new Gson().toJson(response));
            }
        });
    }

    /**
     * List<BannerDetailData>
     */
    private void initBannerData() {
        mViewModel.getBannerData().observe(this, new WithLoadingTipObserver<List<BannerDetailData>>(mLoadingTip, "获取Banner数据") {
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
