package com.example.william.my.module.sample.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.william.my.library.base.BaseRecyclerDialogFragment;
import com.example.william.my.module.bean.ArticleDetailBean;
import com.example.william.my.module.sample.adapter.ArticleAdapter;
import com.example.william.my.module.sample.contract.ArticleContract;
import com.example.william.my.module.sample.presenter.ArticlePresenter;
import com.example.william.my.module.sample.repo.ArticleRepository;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.List;

public class RecyclerDialogFragment extends BaseRecyclerDialogFragment<ArticleDetailBean> implements ArticleContract.View {

    private ArticleContract.Presenter mPresenter;

    @Override
    public BaseQuickAdapter<ArticleDetailBean, BaseViewHolder> setAdapter() {
        return new ArticleAdapter();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter = new ArticlePresenter(ArticleRepository.getInstance(), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onRefreshArticleList();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        super.onRefresh(refreshLayout);
        mPresenter.onRefreshArticleList();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        super.onLoadMore(refreshLayout);
        mPresenter.onLoadMoreArticleList();
    }

    @Override
    public void setPresenter(ArticleContract.Presenter presenter) {

    }

    @Override
    public void showLoading() {
        ToastUtils.showLong("正在请求数据…");
    }

    @Override
    public void closeLoading() {

    }

    @Override
    public void showToast(String message) {
        onDataFail(message);
    }

    @Override
    public void showArticles(boolean isFirst, List<ArticleDetailBean> article) {
        onDataSuccess(isFirst, article);
    }

    @Override
    public void showEmptyView() {
        onEmptyView();
    }

    @Override
    public void showArticlesNoMore() {
        onDataNoMore();
    }

    @Override
    protected void setAttributes(@NonNull WindowManager.LayoutParams params) {
        super.setAttributes(params);
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
    }
}
