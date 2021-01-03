package com.example.william.my.module.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.william.my.module.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ARouterActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public final String TAG = this.getClass().getSimpleName();

    public ListView mListView;

    public final List<String> mData = new ArrayList<>();
    public final Map<String, String> mMap = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, getClass().getSimpleName());
        setContentView(R.layout.basics_layout_router);

        ARouter.getInstance()
                .inject(this);//BaseActivity

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