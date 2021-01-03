package com.example.william.my.open.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.open.R;
import com.example.william.my.open.adapter.BRVAHAdapter;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/scwang90/SmartRefreshLayout
 */
@Route(path = ARouterPath.OpenSource.OpenSource_SmartRefresh)
public class SmartRefreshActivity extends AppCompatActivity {

    private int i = 0;

    private List<String> mRefreshData;
    private BRVAHAdapter mRefreshAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_activity_smart_refresh);

        RecyclerView mRefreshRecyclerView = findViewById(R.id.refresh_recyclerView);
        RefreshLayout mRefreshRefreshLayout = findViewById(R.id.refresh_refreshLayout);

        //mRefreshRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
        //mRefreshRefreshLayout.setRefreshFooter(new ClassicsFooter(this));

        //设置布局管理器
        mRefreshRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRefreshData = new ArrayList<>();
        mRefreshAdapter = new BRVAHAdapter(mRefreshData);
        mRefreshRecyclerView.setAdapter(mRefreshAdapter);

        mRefreshRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshlayout) {
                i = 0;
                mRefreshData.clear();
                mRefreshAdapter.notifyDataSetChanged();
                refreshlayout.finishRefresh(1000);//传入false表示刷新失败
            }
        });
        mRefreshRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshlayout) {
                i++;
                mRefreshData.add("item : " + i);
                mRefreshAdapter.notifyDataSetChanged();
                refreshlayout.finishLoadMore(1000);//传入false表示加载失败
            }
        });
    }
}
