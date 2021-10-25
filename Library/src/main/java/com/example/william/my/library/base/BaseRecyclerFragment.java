package com.example.william.my.library.base;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.william.my.library.R;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

public abstract class BaseRecyclerFragment<T> extends BaseFragment
        implements OnItemClickListener, OnItemChildClickListener, OnRefreshLoadMoreListener {

    private int mPage;

    private SmartRefreshLayout mPullRefreshLayout;

    private BaseQuickAdapter<T, BaseViewHolder> mAdapter;

    private IEmptyView mEmptyView;

    @Override
    protected int getLayout() {
        return R.layout.basics_fragment_recycler;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView(view);

        initRecyclerData(savedInstanceState);
    }

    private void initRecyclerView(@NonNull View view) {
        mPullRefreshLayout = view.findViewById(R.id.smartRefresh);
        if (mPullRefreshLayout != null) {
            mPullRefreshLayout.setEnableRefresh(canRefresh());
            mPullRefreshLayout.setEnableLoadMore(canLoadMore());
            mPullRefreshLayout.setOnRefreshLoadMoreListener(this);
        }

        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerView);
        if (mRecyclerView != null) {
            //取消recyclerview单独的滑动效果
            mRecyclerView.setNestedScrollingEnabled(true);

            RecyclerView.OnScrollListener onScrollListener = getOnScrollListener();
            if (onScrollListener != null) {
                mRecyclerView.addOnScrollListener(onScrollListener);
            }
            RecyclerView.ItemDecoration itemDecoration = getItemDecoration();
            if (itemDecoration != null) {
                mRecyclerView.addItemDecoration(itemDecoration);
            }

            mAdapter = getAdapter();

            RecyclerView.LayoutManager layoutManager = getLayoutManager();
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(mAdapter);

            mAdapter.setOnItemClickListener(this);
            mAdapter.setOnItemChildClickListener(this);

            mEmptyView = getEmptyView();
            if (mEmptyView != null) {
                mEmptyView.showEmptyView();
                mEmptyView.setOnClickListener(new IEmptyView.OnEmptyClickListener() {
                    @Override
                    public void onRefresh() {

                    }
                });
                mAdapter.setEmptyView(mEmptyView.getRootView());
            }
        }
    }

    protected void initRecyclerData(Bundle savedInstanceState) {

    }

    protected boolean canRefresh() {
        return true;
    }

    protected boolean canLoadMore() {
        return false;
    }

    protected RecyclerView.OnScrollListener getOnScrollListener() {
        return null;
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }

    protected abstract BaseQuickAdapter<T, BaseViewHolder> getAdapter();

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    public IEmptyView getEmptyView() {
        return null;
    }

    protected void queryData() {
        mPage = 1;
    }

    protected void onDataFail(String message) {
        mPullRefreshLayout.setEnableLoadMore(false);
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    protected void onFetchDataSuccess(List<T> list) {
        if (list != null && !list.isEmpty()) {
            mAdapter.setNewInstance(list);
            mPullRefreshLayout.setEnableLoadMore(true);
        } else {
            mPullRefreshLayout.setEnableLoadMore(false);
        }
    }

    protected void onLoadDataSuccess(List<T> list) {
        if (list != null && !list.isEmpty()) {
            mAdapter.addData(list);
            mPullRefreshLayout.setEnableLoadMore(true);
        } else {
            mPullRefreshLayout.setEnableLoadMore(false);
        }
    }

    protected void onEmptyView() {
        TextView textView = new TextView(getActivity());
        textView.setGravity(Gravity.CENTER);
        textView.setText("无数据");
        mAdapter.setEmptyView(textView);
        mPullRefreshLayout.setEnableLoadMore(false);
    }

    protected void onDataNoMore() {
        mPullRefreshLayout.setEnableLoadMore(false);
        Toast.makeText(getContext(), "无更多数据", Toast.LENGTH_SHORT).show();
    }

    protected void onDataSuccess(List<T> list) {
        if (mPage == 0) {
            onFetchDataSuccess(list);
            if (mAdapter.getData().isEmpty()) {
                showEmptyView();
            }
        } else {
            onLoadDataSuccess(list);
        }
    }

    protected void onDataFail() {
        if (mPage == 0) {
            if (mAdapter.getData().isEmpty()) {
                showEmptyView();
            }
        }
        mPullRefreshLayout.setEnableLoadMore(false);
    }

    protected void showEmptyView() {
        if (mEmptyView != null) {
            mEmptyView.showEmptyView();
        }
    }

    protected void hideEmptyView() {
        if (mEmptyView != null) {
            mEmptyView.hide();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPage = 0;
        mPullRefreshLayout.finishRefresh(1000);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mPage++;
        mPullRefreshLayout.finishLoadMore(1000);
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {

    }

    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {

    }
}
