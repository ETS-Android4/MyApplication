package com.example.william.my.module.network.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.bean.base.Urls;
import com.example.william.my.bean.data.LoginBean;
import com.example.william.my.core.retrofit.RxRetrofit;
import com.example.william.my.core.retrofit.exception.ApiException;
import com.example.william.my.core.retrofit.observer.RetrofitObserver;
import com.example.william.my.core.retrofit.response.RetrofitResponse;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

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
                .<LoginBean>Builder()
                .api(Urls.Url_Login)
                .addParams("username", "17778060027")
                .addParams("password", "ww123456")
                .post()
                .build()
                .createSingle()
                .subscribe(new RetrofitObserver<RetrofitResponse<LoginBean>>() {

                    @Override
                    public void onLoading() {
                        super.onLoading();
                        showResponse("onLoading");
                    }

                    @Override
                    public void onResponse(RetrofitResponse<LoginBean> response) {
                        String netSuccess = "Success: " + new Gson().toJson(response);
                        showResponse(netSuccess);
                    }

                    @Override
                    public void onFailure(@NotNull ApiException e) {
                        String netError = "Error: " + e.getMessage();
                        showResponse(netError);
                    }
                });
    }
}