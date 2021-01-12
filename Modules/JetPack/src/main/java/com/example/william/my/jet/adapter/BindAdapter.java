package com.example.william.my.jet.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.william.my.jet.databinding.JetItemBindBinding;

import java.util.List;

public class BindAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<String> mData;

    public BindAdapter(List<String> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        JetItemBindBinding bind = JetItemBindBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(bind);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).itemBind.bindItemTextView.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        public JetItemBindBinding itemBind;

        public ViewHolder(JetItemBindBinding bind) {
            super(bind.getRoot());
            this.itemBind = bind;
        }
    }
}
