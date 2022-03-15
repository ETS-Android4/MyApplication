package com.example.william.my.module.network.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.bean.base.Urls;
import com.example.william.my.bean.data.LoginBean;
import com.example.william.my.core.volley.VolleyUtils;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;
import com.google.gson.Gson;

/**
 * https://developer.android.google.cn/training/volley/index.html
 */
@Route(path = ARouterPath.NetWork.NetWork_VolleyUtils)
public class VolleyUtilsActivity extends BaseResponseActivity {

    @Override
    public void setOnClick() {
        super.setOnClick();
        volleyUtils();
    }

    private void volleyUtils() {
        VolleyUtils.<LoginBean>builder()
                .url(Urls.Url_Login)
                .clazz(LoginBean.class)
                .addParams("username", "17778060027")
                .addParams("password", "wW123456")
                .post()
                .build(this)
                .enqueue(new VolleyUtils.ResponseListener<LoginBean>() {
                    @Override
                    public void onSuccess(LoginBean response) {
                        String net_success = "Success: " + new Gson().toJson(response);
                        showResponse(net_success);
                    }

                    @Override
                    public void onFailure(String error) {
                        String net_error = "Error: " + error;
                        showResponse(net_error);
                    }
                });
    }
}
