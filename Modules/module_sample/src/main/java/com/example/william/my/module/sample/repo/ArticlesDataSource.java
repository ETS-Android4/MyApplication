package com.example.william.my.module.sample.repo;

import com.example.william.my.module.sample.bean.ArticleDetailBean;

import java.util.List;

public interface ArticlesDataSource {

    interface LoadArticleCallback {

        void onArticleLoaded(List<ArticleDetailBean> articles);

        void onDataNotAvailable();

        void onFailure(String msg);
    }

    void getArticleList(int page, LoadArticleCallback callback);

}
