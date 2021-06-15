package com.example.william.my.module.custom.activity;

import android.view.Gravity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.widget.iosalertdialog.IosAlertDialog;
import com.example.william.my.core.widget.iosalertdialog.IosItemAlertDialog;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.custom.R;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.CustomView.CustomView_IosDialog)
public class IosDialogActivity extends BaseResponseActivity {

    private boolean b;

    @Override
    public void setOnClick() {
        super.setOnClick();
        b = !b;
        if (b) {
            new IosAlertDialog(IosDialogActivity.this).builder()
                    .setTitle("标题")
                    .setMsg(Gravity.CENTER, "内容")
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .setLeftButton("左", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    })
                    .setRightButton("右", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    })
                    .show();
        } else {
            new IosItemAlertDialog(IosDialogActivity.this).builder()
                    .setTitle("标题")
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .addAlertItem("ITEM 1", new IosItemAlertDialog.OnItemClickListener() {
                        @Override
                        public void onClick(int which) {

                        }
                    })
                    .addAlertItem("ITEM 2", new IosItemAlertDialog.OnItemClickListener() {
                        @Override
                        public void onClick(int which) {

                        }
                    })
                    .addAlertItem("ITEM 3", R.color.colorPrimaryDark, new IosItemAlertDialog.OnItemClickListener() {
                        @Override
                        public void onClick(int which) {

                        }
                    })
                    .show();
        }
    }
}