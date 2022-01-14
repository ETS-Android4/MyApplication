package com.example.william.my.module.libraries.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.libraries.R;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.Lib.Lib_KeyValue)
public class KeyValueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basics_layout_response);
    }
}