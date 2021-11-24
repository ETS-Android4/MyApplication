package com.example.william.my.module.network.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.bean.api.NetworkService;
import com.example.william.my.bean.data.ArticleBean;
import com.example.william.my.bean.data.ArticleDataBean;
import com.example.william.my.core.retrofit.callback.ObserverCallback;
import com.example.william.my.core.retrofit.exception.ApiException;
import com.example.william.my.core.retrofit.response.RetrofitResponse;
import com.example.william.my.core.retrofit.utils.RetrofitUtils;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;
import com.google.gson.Gson;

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
     * GsonConverterFactory Gson 解析 -> ArticleBean
     */
    private void getBanner() {
        RetrofitUtils.buildSingle(
                service.getArticleList(0),
                new ObserverCallback<ArticleBean>() {

                    @Override
                    public void onLoading() {
                        showResponse("Loading");
                    }

                    @Override
                    public void onResponse(@NonNull ArticleBean response) {
                        String netSuccess = "Article List: " + new Gson().toJson(response);
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
     * RetrofitConverterFactory 自定义解析 -> RetrofitResponse<ArticleDataBean>
     */
    private void getBannerList() {
        RetrofitUtils.buildSingle(
                service.getArticleDateList(0),
                new ObserverCallback<RetrofitResponse<ArticleDataBean>>() {

                    @Override
                    public void onLoading() {
                        showResponse("Loading");
                    }

                    @Override
                    public void onResponse(@NonNull RetrofitResponse<ArticleDataBean> response) {
                        String netSuccess = "Article List: " + new Gson().toJson(response);
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