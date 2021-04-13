package com.example.william.my.module.sample.contract;

import com.example.william.my.library.presenter.BasePresenter;
import com.example.william.my.library.view.BaseView;
import com.example.william.my.module.bean.ArticleDetailBean;

import java.util.List;

public interface ArticleContract {

    interface View extends BaseView<Presenter> {

        void showEmptyView();

        void showArticles(boolean isFirst, List<ArticleDetailBean> article);

        void onDataNoMore();

    }

    interface Presenter extends BasePresenter {

        void onRefreshArticleList();

        void onLoadMoreArticleList();
    }
}
