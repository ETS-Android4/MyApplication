package com.example.william.my.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.william.my.sample.R;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    private final Context mContext;
    private final List<String> mData;

    public ViewPagerAdapter(Context mContext, List<String> data) {
        this.mContext = mContext;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.sample_page, container, false);
        ((TextView) mView.findViewById(R.id.page_response)).setText(mData.get(position));
        container.addView(mView);
        return mView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
