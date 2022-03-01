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

    protected int mPage;

    private SmartRefreshLayout mSmartRefresh;

    private RecyclerView mRecyclerView;

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

    private void initRecyclerView(View view) {
        mSmartRefresh = view.findViewById(R.id.smartRefresh);
        mRecyclerView = view.findViewById(R.id.recyclerView);

        if (mSmartRefresh != null) {
            mSmartRefresh.setEnableLoadMore(false);
            mSmartRefresh.setEnableRefresh(canRefresh());
            mSmartRefresh.setOnRefreshLoadMoreListener(this);
        }

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

            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(getLayoutManager());

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

    public void queryData() {

    }

    public void scrollToTop() {
        if (mRecyclerView != null) {
            mRecyclerView.scrollToPosition(0);
        }
    }

    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void onDataFail() {
        if (mAdapter.getData().isEmpty()) {
            onEmptyView();
        }
        mSmartRefresh.setEnableLoadMore(false);
    }

    private void onEmptyView() {
        if (mAdapter.getData().isEmpty()) {
            TextView textView = new TextView(getActivity());
            textView.setGravity(Gravity.CENTER);
            textView.setText("无数据");
            mAdapter.setEmptyView(textView);
            mSmartRefresh.setEnableLoadMore(false);
        }
    }

    private void onFetchDataSuccess(List<T> list, boolean canLoadMore) {
        if (list != null && !list.isEmpty()) {
            mAdapter.setNewInstance(list);
            mSmartRefresh.setEnableLoadMore(canLoadMore);

            if (mAdapter.getData().isEmpty()) {
                onEmptyView();
            }
        } else {
            mSmartRefresh.setEnableLoadMore(false);
        }
    }

    private void onLoadDataSuccess(List<T> list) {
        if (list != null && !list.isEmpty()) {
            mAdapter.addData(list);
            mSmartRefresh.setEnableLoadMore(true);
        } else {
            mSmartRefresh.setEnableLoadMore(false);
        }
    }

    public void onDataSuccess(List<T> list) {
        onDataSuccess(list, true);
    }

    public void onDataSuccess(List<T> list, boolean canLoadMore) {
        if (mPage == 0) {
            onFetchDataSuccess(list, canLoadMore);
            onEmptyView();
        } else {
            onLoadDataSuccess(list);
        }
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
        queryData();
        mSmartRefresh.finishRefresh(1000);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mPage++;
        queryData();
        mSmartRefresh.finishLoadMore(1000);
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {

    }

    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {

    }
}
