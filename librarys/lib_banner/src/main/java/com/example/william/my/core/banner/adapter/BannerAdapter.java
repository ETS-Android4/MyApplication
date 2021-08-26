package com.example.william.my.core.banner.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.william.my.core.banner.holder.IViewHolder;
import com.example.william.my.core.banner.utils.BannerUtils;

import java.util.ArrayList;
import java.util.List;


public abstract class BannerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements IViewHolder<VH> {

    protected List<Object> mDatas = new ArrayList<>();

    private VH mViewHolder;
    private int mIncreaseCount = 2;//画廊模式，首尾增加个数

    public BannerAdapter(List<Object> datas) {
        setDatas(datas);
    }

    /**
     * 设置实体集合（可以在自己的adapter自定义，不一定非要使用）
     *
     * @param datas
     */
    public void setDatas(List<Object> datas) {
        if (datas != null) {
            mDatas = datas;
            notifyDataSetChanged();
        }
    }

    /**
     * 获取指定的实体（可以在自己的adapter自定义，不一定非要使用）
     *
     * @param position 真实的position
     * @return
     */
    public Object getData(int position) {
        return mDatas.get(position);
    }

    /**
     * 获取指定的实体（可以在自己的adapter自定义，不一定非要使用）
     *
     * @param position 这里传的position不是真实的，获取时转换了一次
     * @return
     */
    public Object getRealData(int position) {
        return mDatas.get(getRealPosition(position));
    }


    @Override
    public final void onBindViewHolder(@NonNull VH holder, int position) {
        mViewHolder = holder;
        int real = getRealPosition(position);
        Object data = mDatas.get(real);
        onBindView(holder, mDatas.get(real), real, getRealCount());
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return onCreateHolder(parent, viewType);
    }

    @Override
    public int getItemCount() {
        return getRealCount() > 1 ? getRealCount() + mIncreaseCount : getRealCount();
    }

    public int getRealCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public int getRealPosition(int position) {
        return BannerUtils.getRealPosition(mIncreaseCount == 2, position, getRealCount());
    }

    public VH getViewHolder() {
        return mViewHolder;
    }

    public void setIncreaseCount(int increaseCount) {
        this.mIncreaseCount = increaseCount;
    }
}