package com.example.william.my.open.tangram.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.william.my.open.R;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.structure.CellRender;

import java.util.Locale;

/**
 * 使用注解方式
 * 必须添加下面三个方法，以@CellRender注解
 * public void cellInited(BaseCell cell)；
 * public void postBindView(BaseCell cell)；
 * public void postUnBindView(BaseCell cell)；
 */
@Keep
public class CustomAnnotationView extends FrameLayout {

    private TextView mTextView;

    public CustomAnnotationView(@NonNull Context context) {
        super(context);
        init();
    }

    public CustomAnnotationView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomAnnotationView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        inflate(getContext(), R.layout.open_item_tangram, this);
        mTextView = findViewById(R.id.open_item_test_title);
    }

    @CellRender
    public void cellInited(BaseCell<FrameLayout> cell) {
        setOnClickListener(cell);
    }


    @CellRender
    public void postBindView(BaseCell<FrameLayout> cell) {
        if (cell.pos % 2 == 0) {
            setBackgroundColor(0xff6200EE);
        } else {
            setBackgroundColor(0xff03DAC5);
        }
        mTextView.setText(String.format(Locale.CHINA, "%s %d: %s", getClass().getSimpleName(), cell.pos, cell.optParam("text")));
    }


    @CellRender
    public void postUnBindView(BaseCell<FrameLayout> cell) {

    }
}
