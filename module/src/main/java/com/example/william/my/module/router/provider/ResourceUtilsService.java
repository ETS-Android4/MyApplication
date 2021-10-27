package com.example.william.my.module.router.provider;

import com.alibaba.android.arouter.facade.template.IProvider;

public interface ResourceUtilsService extends IProvider {

    String getAssets(String assetsFilePath);

}
