package com.example.william.my.module.sample.repo;

import com.example.william.my.module.bean.ArticleDetailBean;

import java.util.List;

public interface ArticleDataSource {

    interface LoadArticleCallback {

        void onArticleLoaded(List<ArticleDetailBean> articles);

        void onDataNotAvailable();

        void onFailure(String msg);
    }

    void getArticleList(int page, LoadArticleCallback callback);

}
