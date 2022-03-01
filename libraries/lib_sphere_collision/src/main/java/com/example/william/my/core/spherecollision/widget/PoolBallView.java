package com.example.william.my.core.spherecollision.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2018-05-17.
 * https://github.com/truemi/Sphere-collision
 */
public class PoolBallView extends FrameLayout {

    private final BallView mBallView;

    public PoolBallView(Context context) {
        this(context, null);
    }

    public PoolBallView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PoolBallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);//重写onDraw需要
        this.mBallView = new BallView(context, this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.mBallView.onLayout(changed);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mBallView.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mBallView.onSizeChanged(w, h);
    }

    public BallView getBallView() {
        return mBallView;
    }
}
