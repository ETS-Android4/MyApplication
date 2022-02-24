package com.example.william.my.module.opensource.activity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeLoadMoreFooterLayout;
import com.aspsine.swipetoloadlayout.SwipeRefreshHeaderLayout;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.opensource.R;
import com.example.william.my.module.router.ARouterPath;

/**
 * https://github.com/Aspsine/SwipeToLoadLayout
 */
@Route(path = ARouterPath.OpenSource.OpenSource_SwipeToLoad)
public class SwipeToLoadActivity extends BaseActivity {

    private SwipeToLoadLayout mSwipeToLoadLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_activity_swipe_to_load);

        mSwipeToLoadLayout = findViewById(R.id.swipeToLoad);

        SwipeRefreshHeaderLayout mSwipeRefreshHeaderLayout = findViewById(R.id.swipe_refresh_header);
        SwipeLoadMoreFooterLayout mSwipeLoadMoreFooterLayout = findViewById(R.id.swipe_load_more_footer);

        mSwipeToLoadLayout.setRefreshHeaderView(mSwipeRefreshHeaderLayout);
        mSwipeToLoadLayout.setLoadMoreFooterView(mSwipeLoadMoreFooterLayout);

        mSwipeToLoadLayout.setRefreshCompleteDelayDuration(2000);
        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeToLoadLayout.setRefreshing(false);
            }
        });
        mSwipeToLoadLayout.setLoadMoreEnabled(true);
        mSwipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mSwipeToLoadLayout.setLoadingMore(false);
            }
        });
    }
}