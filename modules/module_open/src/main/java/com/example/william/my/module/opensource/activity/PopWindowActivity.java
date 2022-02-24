package com.example.william.my.module.opensource.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.opensource.R;
import com.example.william.my.module.router.ARouterPath;
import com.example.zhouwei.library.CustomPopWindow;

/**
 * https://github.com/pinguo-zhouwei/CustomPopwindow
 */
@Route(path = ARouterPath.OpenSource.OpenSource_PopWindow)
public class PopWindowActivity extends BaseActivity implements View.OnClickListener {

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_activity_popwindow);

        mButton = findViewById(R.id.pop_button);
        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.pop_button) {
            //CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(this)
            //        .setView(R.layout.basics_layout_response)//显示的布局
            //        .create()//创建PopupWindow
            //        .showAsDropDown(mButton, 0, 10);//显示PopupWindow
            View contentView = LayoutInflater.from(this).inflate(R.layout.basics_layout_response, null);
            contentView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
            //处理popWindow 显示内容
            handleLogic(contentView);
            //创建并显示popWindow
            CustomPopWindow mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                    .setView(contentView)
                    .size(getResources().getDimensionPixelOffset(R.dimen.basics_btn_width),
                            getResources().getDimensionPixelOffset(R.dimen.basics_btn_height))
                    .setFocusable(true)//是否获取焦点，默认为ture
                    .setOutsideTouchable(true)//是否PopupWindow以外触摸dismiss
                    .create()
                    .showAsDropDown(mButton, 0, 20);
        } else if (id == R.id.basics_response) {
            Toast.makeText(this, "您点击了按钮", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleLogic(View contentView) {
        contentView.findViewById(R.id.basics_response).setOnClickListener(this);
    }
}