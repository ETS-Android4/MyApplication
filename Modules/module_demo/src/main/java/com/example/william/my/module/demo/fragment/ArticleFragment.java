package com.example.william.my.module.demo.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.example.william.my.module.demo.R;
import com.example.william.my.module.demo.adapter.ArticleAdapter;
import com.example.william.my.module.demo.bean.ArticleDetailBean;
import com.example.william.my.module.demo.contract.ArticleContract;
import com.example.william.my.module.demo.utils.CheckUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

public class ArticleFragment extends Fragment implements ArticleContract.View, OnRefreshLoadMoreListener {

    private ArticleAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mSmartRefreshLayout;

    private ArticleContract.Presenter mPresenter;

    public static ArticleFragment newInstance() {
        ArticleFragment fragment = new ArticleFragment();
        Bundle arguments = new Bundle();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.demo_layout_recycler, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recycleView);
        mSmartRefreshLayout = view.findViewById(R.id.smartRefreshLayout);

        mAdapter = new ArticleAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSmartRefreshLayout.setOnRefreshLoadMoreListener(this);
    }

    @Override
    public void setPresenter(@NonNull ArticleContract.Presenter presenter) {
        mPresenter = CheckUtils.checkNotNull(presenter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.queryArticleList();
    }

    @Override
    public void showToast(String message) {
        ToastUtils.showShort(message);
    }

    @Override
    public void showEmptyView() {
        TextView textView = new TextView(getActivity());
        textView.setGravity(Gravity.CENTER);
        textView.setText("无数据");
        mAdapter.setEmptyView(textView);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showArticles(boolean isFirst, List<ArticleDetailBean> article) {
        if (isFirst) {
            mAdapter.setNewInstance(article);
        } else {
            mAdapter.addData(article);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mPresenter.loadArticleList();
        mSmartRefreshLayout.finishLoadMore(1000);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPresenter.queryArticleList();
        mSmartRefreshLayout.finishRefresh(1000);
    }
}