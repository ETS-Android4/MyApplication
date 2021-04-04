package com.example.william.my.module.demo.data;

import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.example.william.my.core.network.retrofit.exception.ApiException;
import com.example.william.my.core.network.retrofit.observer.RetrofitObserver;
import com.example.william.my.core.network.retrofit.response.RetrofitResponse;
import com.example.william.my.core.network.retrofit.utils.RetrofitUtils;
import com.example.william.my.module.demo.bean.ArticleBean;
import com.example.william.my.module.demo.service.ArticleService;
import com.example.william.my.module.demo.utils.CheckUtils;

public class ArticleRepository implements ArticleDataSource {

    private final ArticleService service;

    private static ArticleRepository INSTANCE;

    public static ArticleRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ArticleRepository();
        }
        return INSTANCE;
    }

    private ArticleRepository() {
        service = RetrofitUtils.buildApi(ArticleService.class);
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getArticleList(int page, LoadArticleCallback callback) {

        CheckUtils.checkNotNull(callback);

        RetrofitUtils.buildObs(
                service.getArticleList(page))
                .subscribe(new RetrofitObserver<RetrofitResponse<ArticleBean>>() {
                    @Override
                    public void onResponse(RetrofitResponse<ArticleBean> response) {
                        if (ObjectUtils.isNotEmpty(response.getData()) &&
                                CollectionUtils.isNotEmpty(response.getData().getDatas())) {
                            callback.onArticleLoaded(response.getData().getDatas());
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }

                    @Override
                    public void onFailure(ApiException e) {
                        callback.onFailure(e.getMessage());
                    }
                });
    }
}
