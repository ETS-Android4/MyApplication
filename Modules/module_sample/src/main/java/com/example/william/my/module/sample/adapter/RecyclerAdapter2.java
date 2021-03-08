package com.example.william.my.module.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.william.my.module.sample.R;

import java.util.List;

public class RecyclerAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<String> mData;

    public RecyclerAdapter2(List<String> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_item_recycler, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        ((RecyclerAdapter2.ViewHolder) holder).recyclerView.setLayoutManager(mLinearLayoutManager);
        ((RecyclerAdapter2.ViewHolder) holder).recyclerView.setAdapter(new RecyclerAdapter(mData));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        private final RecyclerView recyclerView;

        private ViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.item_recycleView);
        }
    }

    public void setData(List<String> data) {
        this.mData = data;
    }
}
