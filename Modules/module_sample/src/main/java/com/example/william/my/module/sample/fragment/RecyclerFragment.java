package com.example.william.my.module.sample.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.william.my.library.base.BaseRecyclerFragment;
import com.example.william.my.module.sample.adapter.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecyclerFragment extends BaseRecyclerFragment<String> {

    @Override
    public BaseQuickAdapter<String, BaseViewHolder> setAdapter() {
        return new RecyclerAdapter();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<String> mData = new ArrayList<>();
        for (int i = 0; i <= 20; i++) {
            mData.add("POSITION " + i);
        }
        onDataSuccess(mData);
    }
}
