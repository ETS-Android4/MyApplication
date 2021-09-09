package com.example.william.my.module.demo.activity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.william.my.module.demo.R;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAcceleSensor, mMagneticSensor;
    private float[] mAcceleValues, mMageneticValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        //第一步：通过getSystemService获得SensorManager实例对象
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //第二步：通过SensorManager实例对象获得想要的传感器对象:参数决定获取哪个传感器

        // 重力传感器
        mAcceleSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // 地磁场传感器
        mMagneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    /**
     * 第三步：在获得焦点时注册传感器并让本类实现SensorEventListener接口
     */
    @Override
    protected void onResume() {
        super.onResume();
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
            mMageneticValues = event.values;
        }

        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, mAcceleValues, mMageneticValues);
        SensorManager.getOrientation(R, values);
        // x轴的偏转角度
        values[1] = (float) Math.toDegrees(values[1]);
        // y轴的偏转角度
        values[2] = (float) Math.toDegrees(values[2]);

    }

    /**
     * 传感器精度发生改变的回调接口
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 第五步：在失去焦点时注销传感器
     */
    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}