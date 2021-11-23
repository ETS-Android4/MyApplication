package com.example.william.my.module.network.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.bean.base.Urls;
import com.example.william.my.bean.data.BannerDetailBean;
import com.example.william.my.core.retrofit.RxRetrofit;
import com.example.william.my.core.retrofit.exception.ApiException;
import com.example.william.my.core.retrofit.observer.RetrofitObserver;
import com.example.william.my.core.retrofit.response.RetrofitResponse;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * @see RxRetrofit
 */
@Route(path = ARouterPath.NetWork.NetWork_RxRetrofit)
public class RxRetrofitActivity extends BaseResponseActivity {

    @Override
    public void setOnClick() {
        super.setOnClick();
        getBanner();
    }

    private void getBanner() {
        RxRetrofit
                .<List<BannerDetailBean>>Builder()
                .api(Urls.URL_BANNER)
                .get()
                .build()
                .createObservable()
                .subscribe(new RetrofitObserver<RetrofitResponse<List<BannerDetailBean>>>() {

                    @Override
                    public void onLoading() {
                        super.onLoading();
                        showResponse("Loading");
                    }

                    @Override
                    public void onResponse(@NonNull RetrofitResponse<List<BannerDetailBean>> response) {
                        String netSuccess = "Success: " + new Gson().toJson(response);
                        showResponse(netSuccess);
                    }

                    @Override
                    public void onFailure(@NonNull ApiException e) {
                        String netError = "Error: " + e.getMessage();
                        showResponse(netError);
                    }
                });
    }
}