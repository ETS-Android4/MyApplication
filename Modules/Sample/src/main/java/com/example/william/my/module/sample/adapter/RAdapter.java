package com.example.william.my.module.sample.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * RAdapter
 * {@link #getItemViewType(int)} 返回item类型
 * {@link #createViewHolder(ViewGroup, int)} 创建ViewHolder
 * {@link #onBindViewHolder(ViewHolder, int)} 绑定ViewHolder
 * {@link #getItemCount()} 回返item数目
 */
public abstract class RAdapter<T> extends RecyclerView.Adapter<RAdapter.ViewHolder> {


    private List<T> mData;
    private final int[] mLayoutResID;

    private int mItemViewType;//item类型

    protected RAdapter(List<T> mData, int... mLayoutResID) {
        this.mData = mData;
        this.mLayoutResID = mLayoutResID;
    }

    /**
     * 返回item类型，返回值为布局ID
     */
    @Override
    public int getItemViewType(int position) {
        mItemViewType = setItemViewType(position, mData.get(position));
        //如果默认为0，返回第一个mLayoutResID，否则返回mItemViewType
        mItemViewType = mItemViewType == 0 ? mLayoutResID[0] : mItemViewType;
        return mItemViewType;
    }

    /**
     * 返回ItemViewType，判断加载哪个布局
     */
    protected int getItemViewType() {
        return mItemViewType;
    }

    /**
     * 创建ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.onCreateViewHolder(parent.getContext(), parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

    }

    /**
     * 绑定ViewHolder
     */
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull List<Object> payloads) {
        //设置监听
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(holder.getAdapterPosition());
            }
        });
        //抽象类中设置数据
        convert(holder, mData.get(position), position, payloads.isEmpty());
    }

    public abstract int setItemViewType(int position, T data);

    /**
     * 绑定ViewHolder抽象类
     */
    public abstract void convert(ViewHolder holder, T data, int position, boolean payloadsIsEmpty);

    /**
     * 回返item数目
     */
    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    /**
     * ViewHolder
     */
    protected static class ViewHolder extends RecyclerView.ViewHolder {

        private final View itemView;
        private final SparseArray<View> itemViews;

        private ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.itemViews = new SparseArray<>();
        }

        /**
         * 创建ViewHolder
         */
        private static ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int viewType) {
            //根据viewType返回不同的ViewHolder，viewType返回值为mLayoutResID
            return new ViewHolder(LayoutInflater.from(context).inflate(viewType, parent, false));
        }

        @SuppressWarnings("unchecked")
        public <T extends View> T findView(int id) {
            View view = itemViews.get(id);
            if (view == null) {
                view = itemView.findViewById(id);
                itemViews.put(id, view);
            }
            return (T) view;
        }
    }

    /**
     * 设置数据
     */
    public void setData(List<T> mData) {
        this.mData = mData;
    }

    /**
     * 点击监听接口
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
