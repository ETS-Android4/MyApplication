package com.example.william.my.module.opensource.tangram.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Keep;

import com.bumptech.glide.Glide;
import com.example.william.my.module.opensource.R;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle;

/**
 * 自定义 view
 */
@Keep
public class CustomCellView extends FrameLayout implements ITangramViewLifeCycle {

    private TextView mTextView;
    private ImageView mImageView;

    public CustomCellView(Context context) {
        super(context);
        init();
    }

    public CustomCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomCellView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.open_item_tangram, this);
        mTextView = findViewById(R.id.open_item_test_title);
        mImageView = findViewById(R.id.open_item_test_image);
    }

    @Override
    public void cellInited(BaseCell cell) {
        setOnClickListener(cell);
    }

    @Override
    public void postBindView(BaseCell cell) {
        setBackground(null);
    }

    @Override
    public void postUnBindView(BaseCell cell) {

    }

    public void setTextView(String text) {
        mTextView.setText(text);
    }

    public void setImageView(String url) {
        mImageView.setVisibility(VISIBLE);
        Glide.with(this).load(url).into(mImageView);
    }
}
