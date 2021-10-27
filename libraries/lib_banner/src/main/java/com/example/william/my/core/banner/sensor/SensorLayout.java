package com.example.william.my.core.banner.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;

import com.example.william.my.core.banner.observer.BannerLifecycleObserver;
import com.example.william.my.core.banner.observer.BannerLifecycleObserverAdapter;

public class SensorLayout extends FrameLayout implements SensorEventListener, BannerLifecycleObserver {

    private Scroller mScroller;
    private SensorManager mSensorManager;

    private Sensor mAcceleSensor, mMagneticSensor;
    private float[] mAcceleValues, mMagneticValues;

    private final float[] values = new float[3];//包含 x,y,z的偏移量
    private final float[] orientation = new float[9];//旋转矩阵

    private static final double mDegreeYMin = -50;//最小偏移度数  Y
    private static final double mDegreeYMax = 50;//最大偏移度数  Y
    private static final double mDegreeXMin = -50;//最小偏移度数  X
    private static final double mDegreeXMax = 50;//最大偏移度数  X
    private static final double mXMoveDistance = 50;//X轴移动偏移量 实际偏移为 mXMoveDistance * mDirection
    private static final double mYMoveDistance = 50;//Y轴移动偏移量 实际偏移为 mYMoveDistance * mDirection

    private float mDirection = 1;//偏移加速的倍率 可以通过设置此倍率改变偏移速度

    public SensorLayout(@NonNull Context context) {
        this(context, null);
    }

    public SensorLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SensorLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context);

        // 第一步：通过getSystemService获得SensorManager实例对象
        mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);

        if (mSensorManager != null) {
            // 第二步：通过SensorManager实例对象获得想要的传感器对象:参数决定获取哪个传感器
            mAcceleSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mMagneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

            // 第三步：在获得焦点时注册传感器，并让本类实现SensorEventListener接口
            mSensorManager.registerListener(this, mAcceleSensor, SensorManager.SENSOR_DELAY_GAME);
            mSensorManager.registerListener(this, mMagneticSensor, SensorManager.SENSOR_DELAY_GAME);
        }

        if (context instanceof FragmentActivity) {
            LifecycleOwner owner = (LifecycleOwner) context;
            owner.getLifecycle().addObserver(new BannerLifecycleObserverAdapter(owner, this));
        }
    }

    /**
     * 传感器事件值改变时的回调接口：执行此方法的频率与注册传感器时的频率有关
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        // 第四步：计算偏转角度
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mAcceleValues = event.values;
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mMagneticValues = event.values;
        }

        if (mMagneticValues != null && mAcceleValues != null) {
            SensorManager.getRotationMatrix(orientation, null, mAcceleValues, mMagneticValues);
        }

        SensorManager.getOrientation(orientation, values);
        // x轴的偏转角度
        double mDegreeX = (float) Math.toDegrees(values[1]);
        // y轴的偏转角度
        double mDegreeY = (float) Math.toDegrees(values[2]);

        boolean hasChangeX = false;
        boolean hasChangeY = false;
        int scrollX = mScroller.getFinalX();
        int scrollY = mScroller.getFinalY();

        // 第五步：根据偏转角度执行滑动
        if (mDegreeY <= 0 && mDegreeY > mDegreeYMin) {
            hasChangeX = true;
            scrollX = (int) (mDegreeY / Math.abs(mDegreeYMin) * mXMoveDistance * mDirection);
        } else if (mDegreeY > 0 && mDegreeY < mDegreeYMax) {
            hasChangeX = true;
            scrollX = (int) (mDegreeY / Math.abs(mDegreeYMax) * mXMoveDistance * mDirection);
        }
        if (mDegreeX <= 0 && mDegreeX > mDegreeXMin) {
            hasChangeY = true;
            scrollY = (int) (mDegreeX / Math.abs(mDegreeXMin) * mYMoveDistance * mDirection);
        } else if (mDegreeX > 0 && mDegreeX < mDegreeXMax) {
            hasChangeY = true;
            scrollY = (int) (mDegreeX / Math.abs(mDegreeXMax) * mYMoveDistance * mDirection);
        }
        smoothScrollTo(hasChangeX ? scrollX : mScroller.getFinalX(), hasChangeY ? scrollY : mScroller.getFinalY());
    }

    /**
     * 传感器精度发生改变的回调接口
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void smoothScrollTo(int destX, int destY) {
        int scrollY = getScrollY();
        int delta = destY - scrollY;
        mScroller.startScroll(destX, scrollY, 0, delta, 200);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    public void setDirection(float mDirection) {
        this.mDirection = mDirection;
    }

    @Override
    public void onResume(LifecycleOwner owner) {
        /*
         *第一个参数：SensorEventListener接口的实例对象
         *第二个参数：需要注册的传感器实例
         *第三个参数：传感器获取传感器事件event值频率：
         *              SensorManager.SENSOR_DELAY_FASTEST = 0：对应0微秒的更新间隔，最快，1微秒 = 1 % 1000000秒
         *              SensorManager.SENSOR_DELAY_GAME = 1：对应20000微秒的更新间隔，游戏中常用
         *              SensorManager.SENSOR_DELAY_UI = 2：对应60000微秒的更新间隔
         *              SensorManager.SENSOR_DELAY_NORMAL = 3：对应200000微秒的更新间隔
         *              键入自定义的int值x时：对应x微秒的更新间隔
         */
        mSensorManager.registerListener(this, mAcceleSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMagneticSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause(LifecycleOwner owner) {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onDestroy(LifecycleOwner owner) {

    }
}
