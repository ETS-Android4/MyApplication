package com.example.william.my.module.opensource.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.opensource.R;
import com.example.william.my.module.opensource.adapter.BRVAHAdapter;
import com.example.william.my.module.opensource.adapter.BRVAHProviderAdapter;
import com.example.william.my.module.router.ARouterPath;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

/**
 * https://github.com/scwang90/SmartRefreshLayout
 */
@Route(path = ARouterPath.OpenSource.OpenSource_SmartRefresh)
public class SmartRefreshActivity extends BaseActivity implements OnItemClickListener, OnItemChildClickListener {

    private int i = 0;

    private BRVAHAdapter mRefreshAdapter;
    private BRVAHProviderAdapter mBRVAHMultiAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_activity_smart_refresh);

        RecyclerView mRefreshRecyclerView = findViewById(R.id.refresh_recyclerView);
        RefreshLayout mRefreshRefreshLayout = findViewById(R.id.refresh_refreshLayout);

        //mRefreshRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
        //mRefreshRefreshLayout.setRefreshFooter(new ClassicsFooter(this));

        mRefreshAdapter = new BRVAHAdapter();
        mRefreshRecyclerView.setAdapter(mRefreshAdapter);

        //mBRVAHMultiAdapter = new BRVAHProviderAdapter();
        //mRefreshRecyclerView.setAdapter(mBRVAHMultiAdapter);

        //设置布局管理器
        mRefreshRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRefreshRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshlayout) {
                i = 0;
                mRefreshAdapter.setNewInstance(new ArrayList<>());
                //mBRVAHMultiAdapter.setNewInstance(new ArrayList<>());
                refreshlayout.finishRefresh(1000);//传入false表示刷新失败
            }
        });
        mRefreshRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshlayout) {
                for (int j = 0; j < 10; j++) {
                    i = i + 1;
                    mRefreshAdapter.addData("item : " + i);
                    //mBRVAHMultiAdapter.addData("item : " + i);
                }
                refreshlayout.finishLoadMore(1000);//传入false表示加载失败
            }
        });
        mRefreshAdapter.setOnItemClickListener(this);
        mRefreshAdapter.setOnItemChildClickListener(this);
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        mRefreshAdapter.notifyItemChanged(position, "payload");
    }

    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {

    }
}
