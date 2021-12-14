package com.example.william.my.module.network.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.bean.base.Urls;
import com.example.william.my.core.retrofit.RxRetrofit;
import com.example.william.my.core.retrofit.callback.RetrofitResponseCallback;
import com.example.william.my.core.retrofit.exception.ApiException;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

/**
 * @see RxRetrofit
 */
@Route(path = ARouterPath.NetWork.NetWork_RxRetrofit)
public class RxRetrofitActivity extends BaseResponseActivity {

    @Override
    public void setOnClick() {
        super.setOnClick();
        login();
    }

    private void login() {
        RxRetrofit
                .<JsonElement>Builder()
                .api(Urls.Url_Login)
                .addParams("username", "17778060027")
                .addParams("password", "ww123456")
                .post()
                .build()
                .createResponse()
//                .subscribe(new RetrofitCallback<RetrofitResponse<LoginData>>() {
//                    @Override
//                    public void onResponse(RetrofitResponse<LoginData> response) {
//                        String netSuccess = "onResponse: " + new Gson().toJson(response);
//                        showResponse(netSuccess);
//                    }
//
//                    @Override
//                    public void onFailure(ApiException e) {
//                        String netError = "onFailure: " + e.getMessage();
//                        showResponse(netError);
//                    }
//                });
                .subscribe(new RetrofitResponseCallback<JsonElement>() {
                    @Override
                    public void onResponse(JsonElement response) {
                        String netSuccess = "onResponse: " + new Gson().toJson(response);
                        showResponse(netSuccess);
                    }

                    @Override
                    public void onFailure(ApiException e) {
                        String netError = "onFailure: " + e.getMessage();
                        showResponse(netError);
                    }
                });
    }
}