package com.example.william.my.util.router;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ResourceUtils;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.router.provider.ResourceUtilsService;

@Route(path = ARouterPath.Service.ResourceUtilsService)
public class ResourceUtilsServiceImpl implements ResourceUtilsService {

    @Override
    public String getAssets(String assetsFilePath) {
        return ResourceUtils.readAssets2String(assetsFilePath);
    }

    @Override
    public void init(Context context) {

    }
}
