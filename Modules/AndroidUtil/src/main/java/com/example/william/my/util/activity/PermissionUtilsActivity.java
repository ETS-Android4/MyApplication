package com.example.william.my.util.activity;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.william.my.module.activity.ResponseActivity;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.Util.Util_Permission)
public class PermissionUtilsActivity extends ResponseActivity {

    @Override
    public void setOnClick() {
        super.setOnClick();
        if (ContextCompat.checkSelfPermission(PermissionUtilsActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.permission(
                    PermissionConstants.CALENDAR,//日历
                    PermissionConstants.CAMERA,//相机
                    PermissionConstants.CONTACTS,//联系人
                    PermissionConstants.LOCATION,//位置
                    PermissionConstants.MICROPHONE,//麦克风
                    PermissionConstants.PHONE,//手机权限
                    PermissionConstants.SENSORS,//传感器
                    PermissionConstants.SMS,//短信
                    PermissionConstants.STORAGE//存储
            ).callback(new PermissionUtils.SimpleCallback() {

                @Override
                public void onGranted() {

                }

                @Override
                public void onDenied() {

                }
            }).request();
        } else {
            ToastUtils.showShort("已有权限");
        }
    }
}