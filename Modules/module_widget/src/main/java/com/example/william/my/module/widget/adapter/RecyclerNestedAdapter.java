package com.example.william.my.module.widget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.william.my.core.widget.recyclerView.NestedRecyclerView;
import com.example.william.my.module.widget.R;

import java.util.List;

/**
 * RecyclerView 嵌套 RecyclerView
 */
public class RecyclerNestedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<String> mData;

    public RecyclerNestedAdapter(List<String> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_item_recycler_nested, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        ((RecyclerNestedAdapter.ViewHolder) holder).recyclerView.setLayoutManager(mLinearLayoutManager);
        ((RecyclerNestedAdapter.ViewHolder) holder).recyclerView.setAdapter(new RecyclerAdapter(mData));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        private final NestedRecyclerView recyclerView;

        private ViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.item_recycleView);
        }
    }

    public void setData(List<String> data) {
        this.mData = data;
    }
}
