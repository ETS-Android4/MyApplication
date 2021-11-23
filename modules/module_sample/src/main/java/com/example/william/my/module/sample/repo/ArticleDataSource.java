package com.example.william.my.module.sample.repo;

import com.example.william.my.bean.data.ArticleDetailBean;

import java.util.List;

public interface ArticleDataSource {

    interface LoadArticleCallback {

        void showLoading();

        void onArticleLoaded(List<ArticleDetailBean> articles);

        void onDataNotAvailable();

        void onFailure(String msg);
    }

    void getArticleList(int page, LoadArticleCallback callback);

}
