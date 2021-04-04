package com.example.william.my.module.demo.contract;

import com.example.william.my.library.presenter.BasePresenter;
import com.example.william.my.library.view.BaseView;
import com.example.william.my.module.demo.bean.ArticleDetailBean;

import java.util.List;

public interface ArticleContract {

    interface View extends BaseView<Presenter> {

        void showEmptyView();

        void showArticles(boolean isFirst, List<ArticleDetailBean> article);

    }

    interface Presenter extends BasePresenter {

        void queryArticleList();

        void loadArticleList();
    }
}
