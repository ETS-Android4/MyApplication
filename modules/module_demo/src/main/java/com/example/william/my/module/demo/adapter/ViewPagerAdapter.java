package com.example.william.my.module.demo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.william.my.module.demo.R;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    private final List<String> mData;

    public ViewPagerAdapter(List<String> data) {
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
    public Object instantiateItem(ViewGroup container, int position) {
        View mView = LayoutInflater.from(container.getContext()).inflate(R.layout.demo_page, container, false);
        ((TextView) mView.findViewById(R.id.page_response)).setText(mData.get(position));
        container.addView(mView);
        return mView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
