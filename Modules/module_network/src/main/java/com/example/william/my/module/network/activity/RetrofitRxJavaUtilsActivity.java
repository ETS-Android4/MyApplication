package com.example.william.my.module.network.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.retrofit.exception.ApiException;
import com.example.william.my.core.retrofit.observer.RetrofitObserver;
import com.example.william.my.core.retrofit.utils.RetrofitUtils;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.api.NetworkService;
import com.example.william.my.module.bean.BannerBean;
import com.example.william.my.module.bean.BannerDetailBean;
import com.example.william.my.module.router.ARouterPath;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;

/**
 * {@link RetrofitUtils}
 */
@Route(path = ARouterPath.NetWork.NetWork_RetrofitRxJavaUtils)
public class RetrofitRxJavaUtilsActivity extends BaseResponseActivity {

    private boolean b;

    private NetworkService service;

    @Override
    public void initView() {
        super.initView();
        service = RetrofitUtils.buildApi(NetworkService.class);
    }

    @Override
    public void setOnClick() {
        super.setOnClick();
        b = !b;
        if (b) {
            getBanner();
        } else {
            getBannerList();
        }
    }

    /**
     * GsonConverterFactory Gson 解析 -> BannerBean
     */
    private void getBanner() {
        Observable<BannerBean> obs = RetrofitUtils.buildObservable(service.getBanner());

        obs.subscribe(new RetrofitObserver<BannerBean>() {
            @Override
            public void onResponse(@NonNull BannerBean response) {
                String netSuccess = "getBanner: " + new Gson().toJson(response);
                showResponse(netSuccess);
            }

            @Override
            public void onFailure(@NonNull ApiException e) {
                String netError = "Error: " + e.getMessage();
                showResponse(netError);
            }
        });
    }

    /**
     * RetrofitConverterFactory 自定义解析 -> List<BannerDetailBean>
     */
    private void getBannerList() {
        Observable<com.example.william.my.core.retrofit.response.RetrofitResponse> responseObs = RetrofitUtils.buildObservable(service.getBannerList());

        responseObs.subscribe(new RetrofitObserver<RetrofitObserver<List<BannerDetailBean>>>() {
            @Override
            public void onResponse(@NonNull com.example.william.my.core.retrofit.response.RetrofitResponse response) {
                String netSuccess = "getBannerList: " + new Gson().toJson(response);
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