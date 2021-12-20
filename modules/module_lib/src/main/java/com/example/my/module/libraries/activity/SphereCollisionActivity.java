package com.example.my.module.libraries.activity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.my.module.libraries.R;
import com.example.william.my.core.spherecollision.widget.PoolBallView;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.Lib.Lib_SphereCollision)
public class SphereCollisionActivity extends AppCompatActivity {

    private Sensor mDefaultSensor;
    private SensorManager mSensorManager;

    private PoolBallView poolBall;

    private final int[] mImages = {R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,
            R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher};

    private final SensorEventListener mListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                int x = (int) event.values[0];
                int y = (int) (event.values[1] * 2.0f);
                if (lastX != x || lastY != y) {//防止频繁回调,画面抖动
                    poolBall.getBallView().rockBallByImpulse(-x, y);
                    Log.e("陀螺仪 ", x + "<----陀螺仪Y: " + y + "<-----");
                }

                lastX = x;
                lastY = y;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    private int lastX;
    private int lastY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lib_activity_sphere_collision);
        poolBall = findViewById(R.id.pool_ball);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mDefaultSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        findViewById(R.id.bottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("btn", "点击了");
                poolBall.getBallView().rockBallByImpulse();
            }
        });
        init();
    }

    private void init() {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;

        for (int image : mImages) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(image);
            imageView.setTag(R.id.circle_tag, true);
            poolBall.addView(imageView, layoutParams);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        poolBall.getBallView().onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        poolBall.getBallView().onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mListener, mDefaultSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mListener);
    }
}
