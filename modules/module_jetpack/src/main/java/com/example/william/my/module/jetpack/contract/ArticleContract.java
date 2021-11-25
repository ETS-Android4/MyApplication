package com.example.william.my.module.jetpack.contract;

import com.example.william.my.bean.data.ArticleDetailBean;
import com.example.william.my.library.presenter.IBasePresenter;
import com.example.william.my.library.view.IBaseView;

import java.util.List;

public interface ArticleContract {

    interface View extends IBaseView<Presenter> {

        void showArticles(int page, List<ArticleDetailBean> article);

        void showEmptyView();

        void showArticlesNoMore();

    }

    interface Presenter extends IBasePresenter {

        void queryArticleList(int page);
    }
}
