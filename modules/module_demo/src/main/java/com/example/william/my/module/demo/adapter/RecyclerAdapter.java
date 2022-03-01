package com.example.william.my.module.demo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.william.my.module.demo.R;
import com.example.william.my.module.demo.cache.RecyclerCacheExtension;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> mData;
    private final RecyclerCacheExtension mCaches;

    public RecyclerAdapter(List<String> data) {
        this.mData = data;
        this.mCaches = new RecyclerCacheExtension();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.demo_item_recycler, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        mCaches.addCache(position, holder.itemView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(RecyclerAdapter.this, v, holder.getBindingAdapterPosition());
            }
        });
        ((ViewHolder) holder).textView.setText(mData.get(position));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            //会执行不带payloads参数的onBindViewHolder
            super.onBindViewHolder(holder, position, payloads);
        } else {
            String payload = (String) payloads.get(0);
            ((ViewHolder) holder).textView.setText(payload);
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    /**
     * 刷新闪烁
     * setHasStableId(true)
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;

        private ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_primary_textView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerAdapter adapter, View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
