package com.example.william.my.module.opensource.tangram.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.william.my.module.opensource.R;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle;

import java.util.Locale;

/**
 * 接口方式，实现 ITangramViewLifeCycle 接口
 */
@Keep
public class CustomInterfaceView extends FrameLayout implements ITangramViewLifeCycle {

    private TextView mTextView;

    public CustomInterfaceView(@NonNull Context context) {
        super(context);
        init();
    }

    public CustomInterfaceView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomInterfaceView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.open_item_tangram, this);
        mTextView = findViewById(R.id.open_item_test_title);
    }

    @Override
    public void cellInited(BaseCell cell) {
        setOnClickListener(cell);
    }

    @Override
    public void postBindView(BaseCell cell) {
        if (cell.pos == 0) {
            setBackgroundColor(0xff6200EE);
        } else {
            setBackgroundColor(0xff03DAC5);
        }
        if (cell.pos <= 1) {
            mTextView.setTextSize(14);
        } else {
            mTextView.setTextSize(12);
        }
        mTextView.setText(String.format(Locale.CHINA, "%s %d: %s", getClass().getSimpleName(), cell.pos, cell.optParam("text")));
    }

    @Override
    public void postUnBindView(BaseCell cell) {

    }
}
