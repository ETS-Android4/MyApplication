package com.example.william.my.module.widget.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * ListViewAdapter
 * {@link #getCount()}
 * {@link #getItem(int)}
 * {@link #getItemId(int)}
 * {@link #getView(int, View, ViewGroup)}
 */
public abstract class LAdapter<T> extends BaseAdapter {

    private List<T> mData;
    private final int mLayoutResID;

    protected LAdapter(List<T> mData, int mLayoutResID) {
        this.mData = mData;
        this.mLayoutResID = mLayoutResID;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.getView(parent.getContext(), convertView, parent, mLayoutResID);
        convert(viewHolder, mData.get(position));
        return viewHolder.getConvertView();
    }

    public abstract void convert(ViewHolder holder, T data);

    protected static class ViewHolder {

        private final View itemView;
        private final SparseArray<View> itemViews;

        private ViewHolder(Context context, ViewGroup parent, int mLayoutResID) {
            itemView = LayoutInflater.from(context).inflate(mLayoutResID, parent, false);
            itemView.setTag(this);

            itemViews = new SparseArray<>();
        }

        private static ViewHolder getView(Context context, View convertView, ViewGroup parent, int mLayoutResID) {
            if (convertView == null) {
                return new ViewHolder(context, parent, mLayoutResID);
            }
            return (ViewHolder) convertView.getTag();
        }

        private View getConvertView() {
            return itemView;
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

    public void setData(List<T> mData) {
        this.mData = mData;
    }
}
