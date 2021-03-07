package com.example.william.my.module.sophix.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.william.my.module.sophix.R;

public class SophixActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sophix_activity_sophix);
        mTextView = findViewById(R.id.textView);
        mTextView.setText(getString(R.string.sophix_app_name));
        //mTextView.setText("热修复");
    }
}