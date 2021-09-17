package com.example.william.my.module.demo.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.william.my.module.demo.R;

/**
 * 拉伸区域(左上)：设置拉伸区域
 * 内容区域(右下)：识别内容区域
 * Show lock：当鼠标在图片区域的时候显示不可编辑区域
 * Show patches：在编辑区域显示图片拉伸的区域
 * Show bad patches：在编辑区域显示不好的图片拉伸的区域
 * Show content：在预览区域显示图片的内容区域
 */
public class NinePatchActivity extends AppCompatActivity {

    private TextView mTextView;
    private static final String ninePatch = "http://peanut-oss.cn-bj.ufileos.com/img/%E9%B2%B8%E9%B1%BC.9.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basics_layout_response);

        mTextView = findViewById(R.id.basics_response);
    }
}
