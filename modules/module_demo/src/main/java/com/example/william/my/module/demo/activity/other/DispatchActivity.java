package com.example.william.my.module.demo.activity.other;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.demo.R;
import com.example.william.my.module.router.ARouterPath;

/**
 * 事件传递，dispatchTouchEvent一般不太会改写，只关心onInterceptTouchEvent
 */
@Route(path = ARouterPath.Demo.Demo_Dispatch)
public class DispatchActivity extends AppCompatActivity {

    private TextView mLogcatTextView;
    private StringBuilder mStringBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity_dispatch);

        mStringBuilder = new StringBuilder();
        mLogcatTextView = findViewById(R.id.logcat);
        mLogcatTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStringBuilder = new StringBuilder();
                mLogcatTextView.setText(mStringBuilder);
            }
        });
    }

    public void showLogcat(String logcat) {
        mStringBuilder.append(logcat).append("\n");
        mLogcatTextView.setText(mStringBuilder);
    }
}