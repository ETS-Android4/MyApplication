package com.example.william.my.module.custom.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.widget.spinner.Spinner;
import com.example.william.my.core.widget.spinner.SpinnerAdapter;
import com.example.william.my.module.custom.R;
import com.example.william.my.module.router.ARouterPath;

import java.util.Arrays;

@Route(path = ARouterPath.CustomView.CustomView_Spinner)
public class SpinnerActivity extends AppCompatActivity {

    private Button mButton;
    private Spinner mSpinner;

    private final String[] mData = new String[]{"第一条数据", "第二条数据", "第三条数据", "第四条数据"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_activity_spinner);

        mButton = findViewById(R.id.spinner_button);
        mButton.setText(mData[0]);

        mSpinner = new Spinner(SpinnerActivity.this, Arrays.asList(mData));
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSpinner.setWidth(mButton.getWidth());
                mSpinner.showAsDropDown(mButton);
            }
        });
        mSpinner.setItemListener(new SpinnerAdapter.SpinnerClickListener() {
            @Override
            public void onItemClick(int position) {
                mButton.setText(mData[position]);
            }
        });
    }
}