package com.example.william.my.jet.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.network.retrofit.loading.LoadingTip;
import com.example.william.my.core.network.retrofit.observer.WithLoadingTipObserver;
import com.example.william.my.jet.viewmodel.LoginViewModel;
import com.example.william.my.library.utils.dataBus.ActivityDataBus;
import com.example.william.my.module.activity.ResponseActivity;
import com.example.william.my.module.bean.BannerBean;
import com.example.william.my.module.bean.BannerData;
import com.example.william.my.module.router.ARouterPath;
import com.google.gson.Gson;

import java.util.List;

/**
 * https://developer.android.google.cn/topic/libraries/architecture/livedata
 * https://developer.android.google.cn/topic/libraries/architecture/viewmodel
 */
@Route(path = ARouterPath.JetPack.JetPack_ViewModel)
public class ViewModelActivity extends ResponseActivity implements LoadingTip.LoadingTipListener {

    private LoadingTip mLoadingTip;
    private LoginViewModel mViewModel;

    @Override
    public void initView() {
        super.initView();

        initViewModel();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.banners();
    }

    private void initViewModel() {
        mLoadingTip = LoadingTip.addLoadingTipWithTopBar(this);
        mLoadingTip.setOnReloadListener(this);

        mViewModel = ActivityDataBus.getData(this, LoginViewModel.class);

        // List<MovieBean>
        mViewModel.getBannersBean().observe(this, new WithLoadingTipObserver<List<BannerBean>>(mLoadingTip) {
            @Override
            protected void callback(List<BannerBean> response) {
                showResponse(new Gson().toJson(response));
            }
        });
        // List<MovieData>
        mViewModel.getBannersData().observe(this, new WithLoadingTipObserver<List<BannerData>>(mLoadingTip) {
            @Override
            protected void callback(List<BannerData> response) {
                showResponse(new Gson().toJson(response));
            }
        });
    }

    @Override
    public void reload() {
        mViewModel.banners();
    }
}
