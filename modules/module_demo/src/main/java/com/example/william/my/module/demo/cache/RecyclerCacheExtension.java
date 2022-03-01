package com.example.william.my.module.demo.cache;

import android.util.SparseArray;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerCacheExtension extends RecyclerView.ViewCacheExtension {

    private final SparseArray<View> mViewCache;

    public RecyclerCacheExtension() {
        mViewCache = new SparseArray<>(4);
    }


    @Override
    public View getViewForPositionAndType(@NonNull RecyclerView.Recycler recycler, int position, int type) {
        if (mViewCache.size() > position) {
            return mViewCache.get(position);
        }
        return null;
    }

    public void addCache(int position, View view) {
        if (mViewCache.get(position) != view) {
            mViewCache.put(position, view);
        }
    }

    public void clear() {
        mViewCache.clear();
    }
}
