package com.example.william.my.module.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.william.my.library.base.BaseRecyclerFragment;
import com.example.william.my.module.R;
import com.example.william.my.module.router.ARouterPath;

import java.util.ArrayList;
import java.util.List;

@Route(path = ARouterPath.Fragment.FragmentBasicRecycler)
public class BasicRecyclerFragment extends BaseRecyclerFragment<String> {

    @Override
    protected void initRecyclerData(Bundle savedInstanceState) {
        super.initRecyclerData(savedInstanceState);
        List<String> mData = new ArrayList<>();
        for (int i = 1; i < 61; i++) {
            mData.add("POSITION " + i);
        }
        mAdapter.setNewInstance(mData);
    }

    @Override
    protected boolean canRefresh() {
        return false;
    }

    @Override
    public BaseQuickAdapter<String, BaseViewHolder> setAdapter() {
        return new RecyclerAdapter();
    }

    public static class RecyclerAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public RecyclerAdapter() {
            super(R.layout.basics_item_recycler);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, String item) {
            helper.setText(R.id.item_textView, item);
        }
    }
}
