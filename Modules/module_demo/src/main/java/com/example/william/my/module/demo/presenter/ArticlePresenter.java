package com.example.william.my.module.demo.presenter;

import com.example.william.my.module.demo.bean.ArticleDetailBean;
import com.example.william.my.module.demo.contract.ArticleContract;
import com.example.william.my.module.demo.data.ArticleDataSource;
import com.example.william.my.module.demo.data.ArticleRepository;
import com.example.william.my.module.demo.utils.CheckUtils;

import java.util.List;

public class ArticlePresenter implements ArticleContract.Presenter {

    private final ArticleRepository mArticleRepository;

    private final ArticleContract.View mArticleView;

    private int mPage;

    public ArticlePresenter(ArticleRepository articleRepository, ArticleContract.View view) {
        mArticleRepository =
                CheckUtils.checkNotNull(articleRepository, "ArticleRepository cannot be null!");
        mArticleView =
                CheckUtils.checkNotNull(view, "ArticleView cannot be null!");

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
        mArticleRepository.getArticleList(page, new ArticleDataSource.LoadArticleCallback() {
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
