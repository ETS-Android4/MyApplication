package com.example.william.my.core.widget.gestureLock;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.william.my.core.widget.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 包含n*n个GestureLockView
 * <p>
 * GestureLockGroup边长：mGestureItemWidth * mCount + mMarginBetween * (mCount - 1)
 * mWidth = mMarginBetween * 4 * mCount + mMarginBetween * (mCount - 1);
 * 每个GestureLockView中间的间距mMarginBetween：mGestureItemWidth / 4
 */
public class GestureLock extends RelativeLayout {

    private final Path mPath;
    private final Paint mPaint;

    private final int mCount;//每行个数
    private int mTryTimes;//最大尝试次数
    private final int mColorInnerOn;
    private final int mColorOuterOn;
    private final int mColorInnerUp;
    private final int mColorOuterUp;

    private GestureItem[] mGestureItems;
    private int mGestureItemWidth;

    private final Point mGestureItemsPoint;
    private int mRoutePathX, mRoutePathY;

    private final List<Integer> mChoose;//选中的GestureItem的id
    private int[] mAnswer;//绘制的密码

    public GestureLock(@NonNull Context context) {
        this(context, null);
    }

    public GestureLock(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureLock(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);//描边
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);

        mGestureItemsPoint = new Point();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GestureLock);
        mCount = a.getColor(R.styleable.GestureLock_count, 3);//每行个数
        mTryTimes = a.getColor(R.styleable.GestureLock_tryTimes, 5);//最大尝试次数
        mColorOuterOn = a.getColor(R.styleable.GestureLock_colorOuterOn, ContextCompat.getColor(context, R.color.colorPrimary));
        mColorInnerOn = a.getColor(R.styleable.GestureLock_colorInnerOn, ContextCompat.getColor(context, R.color.colorPrimaryDark));
        mColorOuterUp = a.getColor(R.styleable.GestureLock_colorOuterUp, ContextCompat.getColor(context, R.color.colorPrimary));
        mColorInnerUp = a.getColor(R.styleable.GestureLock_colorInnerUp, ContextCompat.getColor(context, R.color.colorPrimaryDark));
        a.recycle();

        mChoose = new ArrayList<>();
        mAnswer = new int[]{};
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int mWidth = MeasureSpec.getSize(widthMeasureSpec);
        int mHeight = MeasureSpec.getSize(heightMeasureSpec);

        int mMeasureSpecSize = Math.min(mWidth, mHeight);
        int mGestureItemMargin = mMeasureSpecSize / (5 * mCount + 1);

        if (mGestureItems == null) {
            mGestureItems = new GestureItem[mCount * mCount];
            mGestureItemWidth = mGestureItemMargin * 4;
            //设置画笔的宽度为GestureLockView的内圆直径的一半
            mPaint.setStrokeWidth(mGestureItemWidth * 0.333F / 2);
            for (int i = 0; i < mGestureItems.length; i++) {
                //初始化每个GestureLockView
                mGestureItems[i] = new GestureItem(getContext(), mColorOuterOn, mColorInnerOn, mColorOuterUp, mColorInnerUp);
                mGestureItems[i].setId(i + 1);
                LayoutParams params = new LayoutParams(mGestureItemWidth, mGestureItemWidth);
                // 不是每行的第一个，则设置位置为前一个的右边
                if (i % mCount != 0) {
                    params.addRule(RelativeLayout.RIGHT_OF, mGestureItems[i - 1].getId());
                }
                // 从第二行开始，设置为上一行同一位置View的下面
                if (i > mCount - 1) {
                    params.addRule(RelativeLayout.BELOW, mGestureItems[i - mCount].getId());
                }
                //每个item都有右外边距和底外边距
                int marginLeft = 0;
                int marginTop = 0;
                // 第一列设置左边距
                if (i % mCount == 0) {
                    marginLeft = mGestureItemMargin;
                }
                // 第一行设置上边距
                if (i < mCount) {
                    marginTop = mGestureItemMargin;
                }
                params.setMargins(marginLeft, marginTop, mGestureItemMargin, mGestureItemMargin);
                mGestureItems[i].setMode(GestureItem.STATUS_NO_FINGER);
                addView(mGestureItems[i], params);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                performClick();
                break;
            case MotionEvent.ACTION_MOVE:
                mPaint.setColor(mColorInnerOn);//设置路线颜色
                GestureItem child = getChildByPos((int) event.getX(), (int) event.getY());
                if (child != null) {
                    int cId = child.getId();
                    if (!mChoose.contains(cId)) {
                        mChoose.add(cId);
                        child.setMode(GestureItem.STATUS_FINGER_ON);
                        if (mOnGestureLockListener != null) {
                            mOnGestureLockListener.onItemSelected(cId);
                        }
                        // 设置指引线的起点
                        mRoutePathX = child.getLeft() / 2 + child.getRight() / 2;
                        mRoutePathY = child.getTop() / 2 + child.getBottom() / 2;
                        if (mChoose.size() == 1) {
                            mPath.moveTo(mRoutePathX, mRoutePathY);
                        } else {
                            mPath.lineTo(mRoutePathX, mRoutePathY);
                        }
                    }
                }
                mGestureItemsPoint.x = (int) event.getX();
                mGestureItemsPoint.y = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                mPaint.setColor(mColorInnerUp);//设置路线颜色
                this.mTryTimes--;
                // 回调是否成功
                if (mOnGestureLockListener != null && mChoose.size() > 0) {
                    mOnGestureLockListener.onGestureEvent(checkAnswer());
                    if (this.mTryTimes == 0) {
                        mOnGestureLockListener.onExceed();
                    }
                }
                // 将终点设置位置为起点，即取消指引线
                mGestureItemsPoint.x = mRoutePathX;
                mGestureItemsPoint.y = mRoutePathY;

                changeItemMode();//改变子元素的状态为UP

                // 计算每个元素中箭头需要旋转的角度
                for (int i = 0; i + 1 < mChoose.size(); i++) {
                    int childId = mChoose.get(i);
                    int nextChildId = mChoose.get(i + 1);
                    GestureItem startChild = findViewById(childId);
                    GestureItem nextChild = findViewById(nextChildId);
                    int dx = nextChild.getLeft() - startChild.getLeft();
                    int dy = nextChild.getTop() - startChild.getTop();
                    // 计算角度
                    int angle = (int) Math.toDegrees(Math.atan2(dy, dx)) + 90;
                    startChild.setArrowDegree(angle);
                }
                break;
        }
        invalidate();//重绘
        return true;
    }

    @Override
    public boolean performClick() {
        reset();// 重置
        return super.performClick();
    }

    /**
     * 绘制子控件
     */
    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        //绘制GestureLockView间的连线
        if (mPath != null) {
            canvas.drawPath(mPath, mPaint);
        }
        //绘制指引线
        if (mChoose.size() > 0) {
            if (mRoutePathX != 0 && mRoutePathY != 0)
                canvas.drawLine(mRoutePathX, mRoutePathY, mGestureItemsPoint.x, mGestureItemsPoint.y, mPaint);
        }
    }

    /**
     * 重置状态
     */
    public void reset() {
        mChoose.clear();
        mPath.reset();
        for (GestureItem gestureLockView : mGestureItems) {
            gestureLockView.setMode(GestureItem.STATUS_NO_FINGER);
            gestureLockView.setArrowDegree(-1);
        }
    }

    /**
     * 通过触摸坐标获取GestureItem
     */
    private GestureItem getChildByPos(int x, int y) {
        for (GestureItem gestureItem : mGestureItems) {
            if (checkPositionInChild(gestureItem, x, y)) {
                return gestureItem;
            }
        }
        return null;
    }

    /**
     * 判断是否触摸在GestureItem上
     */
    private boolean checkPositionInChild(View child, int x, int y) {
        int padding = (int) (mGestureItemWidth * 0.2);
        return x >= child.getLeft() + padding
                && x <= child.getRight() - padding
                && y >= child.getTop() + padding
                && y <= child.getBottom() - padding;
    }

    /**
     * 检查密码是否正确
     */
    private boolean checkAnswer() {
        if (mAnswer.length != mChoose.size()) {
            return false;
        } else {
            for (int i = 0; i < mAnswer.length; i++) {
                if (mAnswer[i] != mChoose.get(i))
                    return false;
            }
        }
        return true;
    }

    /**
     * 改变状态为UP
     */
    private void changeItemMode() {
        for (GestureItem gestureLockView : mGestureItems) {
            if (mChoose.contains(gestureLockView.getId())) {
                gestureLockView.setMode(GestureItem.STATUS_FINGER_UP);
            }
        }
    }

    /**
     * 设置密码
     */
    public void setAnswer(int[] answer) {
        this.mAnswer = answer;
    }

    /**
     * 最大尝试次数
     */
    public void setMaxTryTimes(int tryTimes) {
        this.mTryTimes = tryTimes;
    }

    /**
     * 触摸监听
     */
    private OnGestureLockListener mOnGestureLockListener;

    public void setOnGestureLockListener(OnGestureLockListener listener) {
        this.mOnGestureLockListener = listener;
    }

    public interface OnGestureLockListener {
        /**
         * 选中item的Id
         */
        void onItemSelected(int cId);

        /**
         * 是否匹配
         */
        void onGestureEvent(boolean matched);

        /**
         * 超过尝试次数
         */
        void onExceed();
    }
}
