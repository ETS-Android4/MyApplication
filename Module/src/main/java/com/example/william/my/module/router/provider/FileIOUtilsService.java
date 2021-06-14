package com.example.william.my.module.router.provider;

import com.alibaba.android.arouter.facade.template.IProvider;

import java.io.File;
import java.io.InputStream;

public interface FileIOUtilsService extends IProvider {

    /**
     * 输入流写入文件
     * @param file
     * @param is
     * @return
     */
    boolean writeFileFromIS(File file, InputStream is);

    /**
     * 字符串写入文件
     * @param file
     * @param string
     * @return
     */
    boolean writeFileFromString(File file, String string);

}
