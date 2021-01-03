package com.example.william.my.util.router;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.william.my.module.router.provider.CheckPermissionIProvider;

public class CheckPermissionService implements CheckPermissionIProvider {

    private Context mContext;

    @Override
    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.permission(
                    PermissionConstants.STORAGE
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

    @Override
    public void init(Context context) {
        this.mContext = context;
    }
}
