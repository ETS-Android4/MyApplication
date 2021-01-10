package com.example.william.my.module.router.provider;

import com.alibaba.android.arouter.facade.template.IProvider;

import java.io.File;
import java.io.InputStream;

public interface FileIOUtilsService extends IProvider {

    boolean writeFileFromIS(File file, InputStream is);

    boolean writeFileFromString(File file, String string);

}
