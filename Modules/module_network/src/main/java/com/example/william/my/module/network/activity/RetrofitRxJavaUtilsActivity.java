package com.example.william.my.module.network.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.retrofit.callback.ObserverCallback;
import com.example.william.my.core.retrofit.exception.ApiException;
import com.example.william.my.core.retrofit.response.RetrofitResponse;
import com.example.william.my.core.retrofit.utils.RetrofitUtils;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.api.NetworkService;
import com.example.william.my.module.bean.BannerBean;
import com.example.william.my.module.bean.BannerDetailBean;
import com.example.william.my.module.router.ARouterPath;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * @see RetrofitUtils
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
        RetrofitUtils.buildObservable(service.getBanner(), new ObserverCallback<BannerBean>() {

            @Override
            public void onLoading() {
                showResponse("Loading");
            }

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
        RetrofitUtils.buildObservable(service.getBannerList(), new ObserverCallback<RetrofitResponse<List<BannerDetailBean>>>() {

            @Override
            public void onLoading() {
                showResponse("Loading");
            }

            @Override
            public void onResponse(@NonNull RetrofitResponse<List<BannerDetailBean>> response) {
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