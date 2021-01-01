package com.example.william.my.jet.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.william.my.jet.R;
import com.example.william.my.module.bean.ArticlesBean;

import org.jetbrains.annotations.NotNull;

public class PagingAdapter extends PagingDataAdapter<ArticlesBean.DataBean.ArticleBean, RecyclerView.ViewHolder> {


    public PagingAdapter(@NotNull DiffUtil.ItemCallback<ArticlesBean.DataBean.ArticleBean> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.jet_item_recycle, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mTextView = itemView.findViewById(R.id.item_recycle_textView);
        }
    }
}
