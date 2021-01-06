package com.example.william.my.module.router.provider;

import com.alibaba.android.arouter.facade.template.IProvider;

import java.io.InputStream;

public interface FileIOUtilsService extends IProvider {

    boolean writeFileFromIS(String filePath, InputStream is);

}
