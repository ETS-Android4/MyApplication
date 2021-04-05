package com.example.william.my.module.demo.presenter;

import com.example.william.my.module.demo.bean.ArticleDetailBean;
import com.example.william.my.module.demo.contract.ArticleContract;
import com.example.william.my.module.demo.repo.ArticlesDataSource;
import com.example.william.my.module.demo.repo.ArticlesRepository;

import java.util.List;

public class ArticlesPresenter implements ArticleContract.Presenter {

    private int mPage;

    private final ArticlesRepository mArticleRepository;

    private final ArticleContract.View mArticleView;

    public ArticlesPresenter(ArticlesRepository articleRepository, ArticleContract.View view) {
        mArticleRepository = articleRepository;
        mArticleView = view;

        this.mArticleView.setPresenter(this);
    }

    @Override
    public void queryArticleList() {
        mPage = 0;
        queryArticleList(mPage);
    }

    @Override
    public void loadArticleList() {
        mPage++;
        queryArticleList(mPage);
    }

    private void queryArticleList(int page) {
        mArticleRepository.getArticleList(page, new ArticlesDataSource.LoadArticleCallback() {
            @Override
            public void onArticleLoaded(List<ArticleDetailBean> articles) {
                mArticleView.showArticles(mPage == 0, articles);
            }

            @Override
            public void onDataNotAvailable() {
                if (mPage == 0) {
                    mArticleView.showEmptyView();
                }
            }

            @Override
            public void onFailure(String msg) {
                mArticleView.showToast(msg);
            }
        });
    }


    @Override
    public void start() {

    }
}
