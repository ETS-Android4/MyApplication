package com.example.william.my.module.util.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.CrashUtils;
import com.blankj.utilcode.util.LogUtils;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;

/**
 * Thread.UncaughtExceptionHandler
 */
@Route(path = ARouterPath.Util.Util_Crash)
public class CrashUtilsActivity extends BaseResponseActivity {

    @Override
    public void initView() {
        super.initView();

        //CrashUtils.init();//默认getExternalFilesDir目录
        CrashUtils.init(new CrashUtils.OnCrashListener() {
            @Override
            public void onCrash(CrashUtils.CrashInfo crashInfo) {
                LogUtils.e(TAG, crashInfo.toString());
            }
        });
    }

    @Override
    public void setOnClick() {
        super.setOnClick();
        throw new NullPointerException("空指针");
    }
}