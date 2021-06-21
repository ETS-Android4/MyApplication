package com.example.william.my.module.sample.contract;

import com.example.william.my.library.presenter.IBasePresenter;
import com.example.william.my.library.view.IBaseView;
import com.example.william.my.module.bean.ArticleDetailBean;

import java.util.List;

public interface ArticleContract {

    interface View extends IBaseView<Presenter> {

        void showEmptyView();

        void showArticles(boolean isFirst, List<ArticleDetailBean> article);

        void onDataNoMore();

    }

    interface Presenter extends IBasePresenter {

        void onRefreshArticleList();

        void onLoadMoreArticleList();
    }
}
