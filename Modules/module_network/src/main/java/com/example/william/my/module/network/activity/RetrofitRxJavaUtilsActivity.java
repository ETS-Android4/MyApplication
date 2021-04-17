package com.example.william.my.module.network.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.network.retrofit.exception.ApiException;
import com.example.william.my.core.network.retrofit.observer.RetrofitObserver;
import com.example.william.my.core.network.retrofit.response.RetrofitResponse;
import com.example.william.my.core.network.retrofit.utils.RetrofitUtils;
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
        Observable<BannerBean> obs = RetrofitUtils.buildObs(service.getBanner());

        obs.subscribe(new RetrofitObserver<BannerBean>() {
            @Override
            public void onResponse(@NonNull BannerBean response) {
                String net_success = "Success: " + new Gson().toJson(response);
                showResponse(net_success);
            }

            @Override
            public void onFailure(@NonNull ApiException e) {
                String net_error = "Error: " + e.getMessage();
                showResponse(net_error);
            }
        });
    }

    /**
     * RetrofitConverterFactory 自定义解析 -> List<BannerDetailBean>
     */
    private void getBannerList() {
        Observable<RetrofitResponse<List<BannerDetailBean>>> responseObs = RetrofitUtils.buildObs(service.getBannerResponse());

        responseObs.subscribe(new RetrofitObserver<RetrofitResponse<List<BannerDetailBean>>>() {
            @Override
            public void onResponse(@NonNull RetrofitResponse<List<BannerDetailBean>> response) {
                String net_success = "Success: " + new Gson().toJson(response);
                showResponse(net_success);
            }

            @Override
            public void onFailure(@NonNull ApiException e) {
                String net_error = "Error: " + e.getMessage();
                showResponse(net_error);
            }
        });
    }
}