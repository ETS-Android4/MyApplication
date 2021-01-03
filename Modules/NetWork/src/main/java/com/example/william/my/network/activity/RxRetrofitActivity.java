package com.example.william.my.network.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.network.retrofit.exception.ApiException;
import com.example.william.my.core.network.retrofit.observer.RetrofitObserver;
import com.example.william.my.core.network.retrofit.response.RetrofitResponse;
import com.example.william.my.core.network.retrofit.utils.RxRetrofit;
import com.example.william.my.module.activity.ResponseActivity;
import com.example.william.my.module.base.Urls;
import com.example.william.my.module.bean.BannerBean;
import com.example.william.my.module.router.ARouterPath;
import com.google.gson.Gson;

import java.util.List;

@Route(path = ARouterPath.NetWork.NetWork_RxRetrofit)
public class RxRetrofitActivity extends ResponseActivity {

    @Override
    public void setOnClick() {
        super.setOnClick();
        getBanner();
    }

    private void getBanner() {
        RxRetrofit
                .<List<BannerBean>>Builder()
                .api(Urls.banner)
                .get()
                .build()
                .createObservable()
                .subscribe(new RetrofitObserver<RetrofitResponse<List<BannerBean>>>() {
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