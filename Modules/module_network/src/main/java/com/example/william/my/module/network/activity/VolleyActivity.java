package com.example.william.my.module.network.activity;

import static com.example.william.my.module.base.Urls.URL_LOGIN;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.bean.LoginBean;
import com.example.william.my.module.network.volley.VolleyUtils;
import com.example.william.my.module.router.ARouterPath;
import com.google.gson.Gson;

/**
 * https://developer.android.google.cn/training/volley/index.html
 */
@Route(path = ARouterPath.NetWork.NetWork_Volley)
public class VolleyActivity extends BaseResponseActivity {

    @Override
    public void setOnClick() {
        super.setOnClick();
        login();
    }

    private void login() {
        VolleyUtils.<LoginBean>builder()
                .url(URL_LOGIN)
                .addParams("username", "17778060027")
                .addParams("password", "wW123456")
                .clazz(LoginBean.class)
                .build(VolleyActivity.this)
                .enqueue(new VolleyUtils.VolleyListener<LoginBean>() {
                    @Override
                    public void onMySuccess(@NonNull LoginBean result) {
                        String net_success = "Success: " + new Gson().toJson(result);
                        showResponse(net_success);
                    }

                    @Override
                    public void onMyError(@NonNull String error) {
                        String net_error = "Error: " + error;
                        showResponse(net_error);
                    }
                });
    }
}
