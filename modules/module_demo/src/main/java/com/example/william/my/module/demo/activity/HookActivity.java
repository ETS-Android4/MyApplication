package com.example.william.my.module.demo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.demo.R;
import com.example.william.my.module.demo.hook.HookManager;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.Demo.Demo_Hook)
public class HookActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basics_layout_response);
        mTextView = findViewById(R.id.basics_response);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "onClick!");
                HookManager.setViewTag(v, "name", "hook");
            }
        });
        HookManager.hookOnClick(this, mTextView);
    }
}