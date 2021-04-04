package com.example.william.my.module.network.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.NetWork.NetWork_RetrofitDownload)
public class RetrofitDownloadActivity extends BaseResponseActivity {

    @Override
    public void setOnClick() {
        super.setOnClick();
        download();
    }

    private void download() {

    }
}