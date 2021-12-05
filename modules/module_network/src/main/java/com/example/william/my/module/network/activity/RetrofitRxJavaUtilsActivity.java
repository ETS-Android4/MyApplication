package com.example.william.my.module.network.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.bean.api.NetworkService;
import com.example.william.my.bean.data.ArticleBean;
import com.example.william.my.bean.data.ArticleDataBean;
import com.example.william.my.core.retrofit.callback.RetrofitCallback;
import com.example.william.my.core.retrofit.callback.RetrofitResponseCallback;
import com.example.william.my.core.retrofit.exception.ApiException;
import com.example.william.my.core.retrofit.utils.RetrofitUtils;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

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
            getArticle();
        } else {
            getArticleResponse();
        }
    }

    /**
     * RetrofitCallback
     */
    private void getArticle() {
        RetrofitUtils.buildSingle(
                service.getArticle(0),
                new RetrofitCallback<ArticleBean>() {

                    @Override
                    public void onLoading() {
                        super.onLoading();
                    }

                    @Override
                    public void onResponse(ArticleBean response) {
                        String netSuccess = "RetrofitCallback: " + new Gson().toJson(response);
                        showResponse(netSuccess);
                    }

                    @Override
                    public void onFailure(@NotNull ApiException e) {
                        String netError = "onFailure: " + e.getMessage();
                        showResponse(netError);
                    }
                });
    }

    /**
     * RetrofitResponseCallback
     */
    private void getArticleResponse() {
        RetrofitUtils.buildResponseSingle(
                service.getArticleResponse(0),
                new RetrofitResponseCallback<ArticleDataBean>() {
                    @Override
                    public void onLoading() {
                        super.onLoading();
                    }

                    @Override
                    public void onResponse(ArticleDataBean response) {
                        String netSuccess = "RetrofitResponseCallback: " + new Gson().toJson(response);
                        showResponse(netSuccess);
                    }

                    @Override
                    public void onFailure(@NotNull ApiException e) {
                        String netError = "onFailure: " + e.getMessage();
                        showResponse(netError);
                    }
                });
    }
}