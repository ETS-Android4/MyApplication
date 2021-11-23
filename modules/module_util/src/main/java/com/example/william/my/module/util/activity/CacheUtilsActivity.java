package com.example.william.my.module.util.activity;

import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.CacheDiskStaticUtils;
import com.blankj.utilcode.util.CacheMemoryStaticUtils;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.util.R;

@Route(path = ARouterPath.Util.Util_Cache)
public class CacheUtilsActivity extends BaseResponseActivity {

    @Override
    public void initView() {
        super.initView();

        mResponse.setText("");
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_launcher);

        // 内存缓存
        CacheMemoryStaticUtils.put("ic_launcher", drawable);
        mResponse.setBackground(CacheMemoryStaticUtils.get("ic_launcher"));

        // 磁盘缓存
        CacheDiskStaticUtils.put("ic_launcher", drawable);
        mResponse.setBackground(CacheDiskStaticUtils.getDrawable("ic_launcher"));
    }
}