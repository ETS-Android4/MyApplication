package com.example.william.my.module.demo.activity.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.demo.R;
import com.example.william.my.module.demo.hook.HookManager;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.utils.L;

@Route(path = ARouterPath.Demo.Demo_Hook)
public class HookActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basics_layout_response);
        mTextView = findViewById(R.id.basics_response);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.e("TAG", "onClick!");
                HookManager.setViewTag(v, "name", "hook");
            }
        });
        HookManager.hookOnClick(this, mTextView);
    }
}