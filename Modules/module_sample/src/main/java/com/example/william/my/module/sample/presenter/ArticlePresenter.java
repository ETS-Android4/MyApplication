package com.example.william.my.module.sample.presenter;

import com.example.william.my.module.bean.ArticleDetailBean;
import com.example.william.my.module.sample.contract.ArticleContract;
import com.example.william.my.module.sample.repo.ArticleDataSource;
import com.example.william.my.module.sample.repo.ArticleRepository;

import java.util.List;

public class ArticlePresenter implements ArticleContract.Presenter {

    private int mPage;

    private final ArticleRepository mArticleRepository;

    private final ArticleContract.View mArticleView;

    public ArticlePresenter(ArticleRepository articleRepository, ArticleContract.View view) {
        mArticleRepository = articleRepository;
        mArticleView = view;

        this.mArticleView.setPresenter(this);
    }

    @Override
    public void onRefreshArticleList() {
        mPage = 0;
        queryArticleList(mPage);
    }

    @Override
    public void onLoadMoreArticleList() {
        mPage++;
        queryArticleList(mPage);
    }

    private void queryArticleList(int page) {
        mArticleRepository.getArticleList(page, new ArticleDataSource.LoadArticleCallback() {
            @Override
            public void onArticleLoaded(List<ArticleDetailBean> articles) {
                mArticleView.showArticles(mPage == 0, articles);
            }

            @Override
            public void onDataNotAvailable() {
                if (mPage == 0) {
                    mArticleView.showEmptyView();
                } else {
                    mArticleView.onDataNoMore();
                }
            }

            @Override
            public void onFailure(String msg) {
                if (mPage == 0) {
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
