package com.example.william.my.module.sophix.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.william.my.module.sophix.R;

public class SophixActivity extends AppCompatActivity {

    private TextView mTextView;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sophix_activity_sophix);
        mTextView = findViewById(R.id.textView);
        mImageView = findViewById(R.id.imageView);
        mTextView.setVisibility(View.GONE);
    }
}