package com.example.william.my.library.base;

import android.os.Bundle;
import android.view.View;

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

import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BaseRecyclerFragment<T> extends BaseFragment
        implements OnItemClickListener, OnItemChildClickListener, OnRefreshLoadMoreListener {
    
    private SmartRefreshLayout mSmartRefreshLayout;

    private BaseQuickAdapter<T, BaseViewHolder> mAdapter;

    private int mPage = 1;

    @Override
    protected int getLayout() {
        return R.layout.basics_fragment_recycler;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    private void initView(View view) {
        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerView);
        mSmartRefreshLayout = view.findViewById(R.id.smartRefreshLayout);

        if (mRecyclerView != null) {
            //取消recyclerview单独的滑动效果
            mRecyclerView.setNestedScrollingEnabled(true);
            mRecyclerView.setLayoutManager(setLayoutManager());

            mAdapter = setAdapter();
            mRecyclerView.setAdapter(mAdapter);

            mAdapter.setOnItemClickListener(this);
            mAdapter.setOnItemChildClickListener(this);
        }

        if (mSmartRefreshLayout != null) {
            mSmartRefreshLayout.setOnRefreshListener(this);
            mSmartRefreshLayout.setOnLoadMoreListener(this);
            mSmartRefreshLayout.setOnRefreshLoadMoreListener(this);

            mSmartRefreshLayout.setEnableRefresh(canLoadMore());
            mSmartRefreshLayout.setEnableLoadMore(canRefresh());
        }
    }

    private boolean canRefresh() {
        return true;
    }

    private boolean canLoadMore() {
        return false;
    }

    public RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    public abstract BaseQuickAdapter<T, BaseViewHolder> setAdapter();

    public void onDataSuccess(List<T> list) {
        if (list != null && !list.isEmpty()) {
            if (mPage == 1) {
                mAdapter.setNewInstance(list);
            } else {
                mAdapter.addData(list);
            }
        } else {
            mSmartRefreshLayout.setEnableLoadMore(false);
        }
    }

    public void onDataFail() {
        mSmartRefreshLayout.setEnableLoadMore(false);
    }

    @Override
    public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
        mPage = 1;
        mSmartRefreshLayout.autoRefresh();
    }

    @Override
    public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
        mPage++;
        mSmartRefreshLayout.autoLoadMore();
    }

    @Override
    public void onItemClick(@NonNull @NotNull BaseQuickAdapter adapter, @NonNull @NotNull View view, int position) {

    }

    @Override
    public void onItemChildClick(@NonNull @NotNull BaseQuickAdapter adapter, @NonNull @NotNull View view, int position) {

    }

}
