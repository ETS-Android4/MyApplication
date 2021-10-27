package com.example.william.my.module.util.router;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.FileIOUtils;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.router.provider.FileIOUtilsService;

import java.io.File;
import java.io.InputStream;

@Route(path = ARouterPath.Service.FileIOUtilsService)
public class FileIOUtilsServiceImpl implements FileIOUtilsService {

    @Override
    public boolean writeFileFromIS(File file, InputStream is) {
        return FileIOUtils.writeFileFromIS(file, is);
    }

    @Override
    public boolean writeFileFromString(File file, String string) {
        return FileIOUtils.writeFileFromString(file, string);
    }

    @Override
    public void init(Context context) {

    }
}
