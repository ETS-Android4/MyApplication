package com.example.william.my.module.jetpack.presenter;

import com.example.william.my.bean.data.ArticleDetailBean;
import com.example.william.my.bean.repo.DataRepository;
import com.example.william.my.bean.repo.DataSource;
import com.example.william.my.module.jetpack.contract.ArticleContract;

import java.util.List;

public class ArticlePresenter implements ArticleContract.Presenter {

    private final DataRepository mArticleRepository;

    private final ArticleContract.View mArticleView;

    public ArticlePresenter(DataRepository articleRepository, ArticleContract.View view) {
        mArticleRepository = articleRepository;
        mArticleView = view;

        this.mArticleView.setPresenter(this);
    }

    @Override
    public void queryArticleList(int page) {
        mArticleRepository.queryArticleList(page, new DataSource.LoadArticleCallback() {
            @Override
            public void showLoading() {
                mArticleView.showLoading();
            }

            @Override
            public void onArticleLoaded(List<ArticleDetailBean> articles) {
                mArticleView.showArticles(page, articles);
            }

            @Override
            public void onFailure(String msg) {
                if (page == 0) {
                    mArticleView.showEmptyView();
                }
                mArticleView.showToast(msg);
            }
        });
    }


    @Override
    public void start() {

    }

    @Override
    public void clear() {

    }
}
