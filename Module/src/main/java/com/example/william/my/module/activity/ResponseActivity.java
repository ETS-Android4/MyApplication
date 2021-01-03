package com.example.william.my.module.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.william.my.module.R;

public class ResponseActivity extends AppCompatActivity implements View.OnClickListener {

    public final String TAG = this.getClass().getSimpleName();

    public TextView mResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, getClass().getSimpleName());
        setContentView(R.layout.basics_layout_response);

        findView();
        initView();
    }

    private void findView() {
        mResponse = findViewById(R.id.basics_response);
        mResponse.setOnClickListener(this);
    }

    public void initView() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.basics_response) {
            setOnClick();
        }
    }

    public void setOnClick() {

    }

    public void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mResponse.setText(response);
            }
        });
    }
}
