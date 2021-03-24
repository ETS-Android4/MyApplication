package com.example.william.my.module.sample.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.william.my.core.widget.utils.SizeUtils;
import com.example.william.my.module.sample.R;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> mData;

    public RecyclerAdapter(List<String> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_item_recycler, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).textView.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;

        private ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_primary_textView);
        }
    }

    public void setData(List<String> data) {
        this.mData = data;
    }
}
