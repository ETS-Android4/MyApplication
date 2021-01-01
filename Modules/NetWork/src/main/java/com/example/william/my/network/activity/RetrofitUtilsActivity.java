package com.example.william.my.network.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.network.retrofit.exception.ApiException;
import com.example.william.my.core.network.retrofit.observer.RetrofitObserver;
import com.example.william.my.core.network.retrofit.response.RetrofitResponse;
import com.example.william.my.core.network.retrofit.utils.RetrofitUtils;
import com.example.william.my.module.activity.ResponseActivity;
import com.example.william.my.module.bean.BannerBean;
import com.example.william.my.module.bean.BannersBean;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.service.NetworkService;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

/**
 * {@link RetrofitUtils}
 */
@Route(path = ARouterPath.NetWork.NetWork_RetrofitUtils)
public class RetrofitUtilsActivity extends ResponseActivity {

    private boolean b;

    private final NetworkService service = RetrofitUtils.buildApi(NetworkService.class);

    @Override
    public void setOnClick() {
        super.setOnClick();
        b = !b;
        if (b) {
            getBanners();
        } else {
            getBannerList();
        }
    }

    private void getBanners() {
        Observable<BannersBean> obs = RetrofitUtils.buildObs(service.getBanners());

        obs.subscribe(new RetrofitObserver<BannersBean>() {
            @Override
            public void onResponse(BannersBean response) {
                String net_success = "Success: " + new Gson().toJson(response);
                showResponse(net_success);
            }

            @Override
            public void onFailure(ApiException e) {
                String net_error = "Error: " + e.getMessage();
                showResponse(net_error);
            }
        });
    }

    private void getBannerList() {
        Observable<RetrofitResponse<List<BannerBean>>> responseObs = RetrofitUtils.buildObs(service.getBannersResponse());

        responseObs.subscribe(new RetrofitObserver<RetrofitResponse<List<BannerBean>>>() {
            @Override
            public void onResponse(RetrofitResponse<List<BannerBean>> response) {
                String net_success = "Success: " + new Gson().toJson(response);
                showResponse(net_success);
            }

            @Override
            public void onFailure(ApiException e) {
                String net_error = "Error: " + e.getMessage();
                showResponse(net_error);
            }
        });
    }
}