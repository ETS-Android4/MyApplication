package com.example.william.my.network.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.network.retrofit.download.DownloadUtils;
import com.example.william.my.core.network.retrofit.download.database.table.DownloadTask;
import com.example.william.my.module.activity.ResponseActivity;
import com.example.william.my.module.base.Urls;
import com.example.william.my.module.router.ARouterPath;

/**
 * {@link DownloadUtils}
 */
@Route(path = ARouterPath.NetWork.NetWork_RetrofitDownload)
public class RetrofitDownloadActivity extends ResponseActivity {

    @Override
    public void setOnClick() {
        super.setOnClick();
        download();
    }

    private void download() {
        DownloadTask task = new DownloadTask(Urls.download);
        DownloadUtils.getInstance().enqueue(task);
    }
}