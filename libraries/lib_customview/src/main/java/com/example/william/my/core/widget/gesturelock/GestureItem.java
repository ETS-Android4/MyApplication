package com.example.william.my.core.widget.gesturelock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class GestureItem extends View {

    private Path mPath;
    private Paint mPaint;

    private int mCurrentStatus;//当前状态
    public static final int STATUS_NO_FINGER = 0;
    public static final int STATUS_FINGER_ON = 1;
    public static final int STATUS_FINGER_UP = 2;

    private int mColorInnerOn, mColorOuterOn, mColorInnerUp, mColorOuterUp;

    private final int mStrokeWidth = 2;//边线宽度
    private int mRadius, mCenterX, mCenterY;//半径，坐标

    private int mArrowDegree = -1;//旋转角度

    //三种状态，普通,按下,松开
    @IntDef({STATUS_NO_FINGER, STATUS_FINGER_ON, STATUS_FINGER_UP})
    @Retention(RetentionPolicy.SOURCE)
    private @interface Mode {
    }

    @Mode
    public int getMode() {
        return mCurrentStatus;
    }

    public void setMode(@Mode int mCurrentStatus) {
        this.mCurrentStatus = mCurrentStatus;
        invalidate();//重新绘制
    }

    public void setArrowDegree(int mArrowDegree) {
        this.mArrowDegree = mArrowDegree;
    }

    public GestureItem(@NonNull Context context) {
        super(context);
    }

    public GestureItem(@NonNull Context context, int mColorOuterOn, int mColorInnerOn, int mColorOuterUp, int mColorInnerUp) {
        super(context);
        this.mColorOuterOn = mColorOuterOn;
        this.mColorInnerOn = mColorInnerOn;
        this.mColorOuterUp = mColorOuterUp;
        this.mColorInnerUp = mColorInnerUp;

        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int mWidth = MeasureSpec.getSize(widthMeasureSpec);
        int mHeight = MeasureSpec.getSize(heightMeasureSpec);

        mWidth = Math.min(mWidth, mHeight);

        mRadius = mCenterX = mCenterY = mWidth / 2;
        mRadius -= mStrokeWidth / 2;//减去外圈的半径

        // 绘制三角形，初始时是个默认箭头朝上的一个等腰三角形，用户绘制结束后，根据由两个GestureLockView决定需要旋转多少度
        float mArrowLength = (float) mWidth / 2 * 0.333F;
        mPath.moveTo((float) mWidth / 2, mStrokeWidth + 2);
        mPath.lineTo((float) mWidth / 2 - mArrowLength, mStrokeWidth + 2 + mArrowLength);
        mPath.lineTo((float) mWidth / 2 + mArrowLength, mStrokeWidth + 2 + mArrowLength);
        mPath.close();
        mPath.setFillType(Path.FillType.WINDING);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (getMode()) {
            case STATUS_NO_FINGER:
                //绘制外圆边线
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(mStrokeWidth);
                mPaint.setColor(mColorOuterOn);
                canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
                break;
            case STATUS_FINGER_ON:
                // 绘制外圆
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(mColorOuterOn);
                canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
                // 绘制内圆
                mPaint.setColor(mColorInnerOn);
                canvas.drawCircle(mCenterX, mCenterY, mRadius * 0.333F, mPaint);
                break;
            case STATUS_FINGER_UP:
                // 绘制外圆
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(mColorOuterUp);
                canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
                // 绘制内圆
                mPaint.setColor(mColorInnerUp);
                canvas.drawCircle(mCenterX, mCenterY, mRadius * 0.333F, mPaint);
                //绘制箭头
                //drawArrow(canvas);
                break;
        }
    }

    /**
     * 绘制箭头
     */
    private void drawArrow(Canvas canvas) {
        if (mArrowDegree != -1) {
            mPaint.setStyle(Paint.Style.FILL);
            canvas.save();
            canvas.rotate(mArrowDegree, mCenterX, mCenterY);//旋转
            canvas.drawPath(mPath, mPaint);
            canvas.restore();//还原
        }
    }
}