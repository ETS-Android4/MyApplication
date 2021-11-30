package com.example.william.my.bean.repo;

import com.example.william.my.bean.data.ArticleDetailBean;

import java.util.List;

public interface DataSource {

    interface LoadArticleCallback {

        void showLoading();

        void onFailure(String msg);

        void onArticleLoaded(List<ArticleDetailBean> articles);
    }

    void getArticleResponse(int page, LoadArticleCallback callback);

}
