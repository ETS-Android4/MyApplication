package com.example.william.my.module.sample.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.william.my.module.sample.databinding.JetItemRecycleBinding;

import java.util.List;

public class ViewBindAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<String> mData;

    public ViewBindAdapter(List<String> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        JetItemRecycleBinding bind = JetItemRecycleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(bind);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).itemBind.itemRecycleTextView.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        public JetItemRecycleBinding itemBind;

        public ViewHolder(JetItemRecycleBinding bind) {
            super(bind.getRoot());
            this.itemBind = bind;
        }
    }
}
