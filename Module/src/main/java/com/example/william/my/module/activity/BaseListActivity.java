package com.example.william.my.module.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BaseListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    public ListView mListView;

    public final List<String> mData = new ArrayList<>();
    public final Map<String, String> mMap = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basics_layout_router);

        findView();
        initData();
        initView();
    }

    private void findView() {
        mListView = findViewById(R.id.router_listView);
        mListView.setOnItemClickListener(this);
    }

    protected void initData() {

    }

    private void initView() {
        for (Map.Entry<String, String> entry : mMap.entrySet()) {
            mData.add(entry.getKey());
        }
        mListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mData));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ARouter.getInstance().build(mMap.get(mData.get(i))).navigation();
    }
}