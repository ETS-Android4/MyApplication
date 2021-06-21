package com.example.william.my.library.base;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

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

    private int mPage = 1;

    private SmartRefreshLayout mSmartRefreshLayout;

    protected BaseQuickAdapter<T, BaseViewHolder> mAdapter;

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

    private void initRecyclerView(@NotNull View view) {
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

            mSmartRefreshLayout.setEnableRefresh(canRefresh());
            mSmartRefreshLayout.setEnableLoadMore(canLoadMore());

            mSmartRefreshLayout.setOnRefreshLoadMoreListener(this);
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

    protected RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    protected abstract BaseQuickAdapter<T, BaseViewHolder> setAdapter();

    protected void onDataSuccess(List<T> list) {
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

    protected void onDataNoMore() {
        mSmartRefreshLayout.setEnableLoadMore(false);
    }

    protected void onEmptyView() {
        TextView textView = new TextView(getActivity());
        textView.setGravity(Gravity.CENTER);
        textView.setText("无数据");
        mAdapter.setEmptyView(textView);
        mAdapter.notifyDataSetChanged();
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
