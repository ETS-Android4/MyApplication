package com.example.william.my.module.bugly.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.william.my.module.bugly.R;

/**
 * http://bugly.qq.com/docs/user-guide/instruction-manual-android-hotfix
 * fd7808109f
 */
public class BuglyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bugly_activity_bugly);
    }
}