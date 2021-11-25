package com.example.william.my.module.opensource.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.opensource.R;
import com.example.william.my.module.opensource.adapter.BRVAHAdapter;
import com.example.william.my.module.opensource.adapter.BRVAHAdapter2;
import com.example.william.my.module.opensource.bean.SectionBean;
import com.example.william.my.module.router.ARouterPath;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/scwang90/SmartRefreshLayout
 */
@Route(path = ARouterPath.OpenSource.OpenSource_SmartRefresh)
public class SmartRefreshActivity extends BaseActivity {

    private int i = 0;

    private BRVAHAdapter mRefreshAdapter;

    private List<SectionBean> mRefreshData2;
    private BRVAHAdapter2 mRefreshAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_activity_smart_refresh);

        RecyclerView mRefreshRecyclerView = findViewById(R.id.refresh_recyclerView);
        RefreshLayout mRefreshRefreshLayout = findViewById(R.id.refresh_refreshLayout);

        //mRefreshRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
        //mRefreshRefreshLayout.setRefreshFooter(new ClassicsFooter(this));

        mRefreshAdapter = new BRVAHAdapter();
        mRefreshRecyclerView.setAdapter(mRefreshAdapter);

        //设置布局管理器
        mRefreshRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //分组功能
        //mRefreshData2 = new ArrayList<>();
        //for (int i = 0; i <= 30; i++) {
        //    mRefreshData2.add(new Section(i));
        //}
        //mRefreshAdapter2 = new BRVAHAdapter2(mRefreshData2);
        //mRefreshRecyclerView.setAdapter(mRefreshAdapter2);

        mRefreshRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshlayout) {
                i = 0;
                //mRefreshData.clear();
                mRefreshAdapter.setNewInstance(new ArrayList<>());
                mRefreshAdapter.notifyDataSetChanged();
                refreshlayout.finishRefresh(1000);//传入false表示刷新失败
            }
        });
        mRefreshRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshlayout) {
                i++;
                //mRefreshData.add("item : " + i);
                mRefreshAdapter.addData("item : " + i);
                mRefreshAdapter.notifyDataSetChanged();
                refreshlayout.finishLoadMore(1000);//传入false表示加载失败
            }
        });
    }
}
