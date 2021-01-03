package com.example.william.my.network.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.network.volley.VolleyUtils;
import com.example.william.my.module.activity.ResponseActivity;
import com.example.william.my.module.bean.LoginBean;
import com.example.william.my.module.router.ARouterPath;
import com.google.gson.Gson;

import static com.example.william.my.module.base.Urls.login;

/**
 * https://developer.android.google.cn/training/volley/index.html
 */
@Route(path = ARouterPath.NetWork.NetWork_Volley)
public class VolleyActivity extends ResponseActivity {

    @Override
    public void setOnClick() {
        super.setOnClick();
        login();
    }

    private void login() {
        VolleyUtils.<LoginBean>builder()
                .url(login)
                .addParams("username", "17778060027")
                .addParams("password", "wW123456")
                .clazz(LoginBean.class)
                .build(VolleyActivity.this)
                .enqueue(new VolleyUtils.VolleyListener<LoginBean>() {
                    @Override
                    public void onMySuccess(LoginBean result) {
                        String net_success = "Success: " + new Gson().toJson(result);
                        showResponse(net_success);
                    }

                    @Override
                    public void onMyError(String error) {
                        String net_error = "Error: " + error;
                        showResponse(net_error);
                    }
                });
    }
}
