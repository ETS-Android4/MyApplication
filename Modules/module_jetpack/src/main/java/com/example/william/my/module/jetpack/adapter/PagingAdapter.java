package com.example.william.my.module.jetpack.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.william.my.module.bean.ArticleDetailBean;
import com.example.william.my.module.jetpack.R;

import org.jetbrains.annotations.NotNull;

public class PagingAdapter extends PagingDataAdapter<ArticleDetailBean, RecyclerView.ViewHolder> {

    public PagingAdapter(@NotNull DiffUtil.ItemCallback<ArticleDetailBean> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.jet_item_recycle, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ArticleDetailBean articleDetailBean = getItem(position);
        ((ViewHolder) holder).mTextView.setText(articleDetailBean == null ? "" : position + ". " + articleDetailBean.getTitle());
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mTextView = itemView.findViewById(R.id.item_recycle_textView);
        }
    }
}
