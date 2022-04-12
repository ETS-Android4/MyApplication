package com.example.william.my.module.opensource.activity;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.opensource.R;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.utils.T;
import com.example.zhouwei.library.CustomPopWindow;

/**
 * https://github.com/pinguo-zhouwei/CustomPopwindow
 */
@Route(path = ARouterPath.OpenSource.OpenSource_PopWindow)
public class PopWindowActivity extends BaseResponseActivity {

    @Override
    public void setOnClick() {
        super.setOnClick();
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
                .showAtLocation(mResponse, Gravity.CENTER, 0, 0);
    }

    private void handleLogic(View contentView) {
        contentView.findViewById(R.id.basics_response).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                T.show("您点击了按钮");
            }
        });
    }
}