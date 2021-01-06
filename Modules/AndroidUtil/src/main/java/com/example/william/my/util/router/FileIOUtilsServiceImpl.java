package com.example.william.my.util.router;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.FileIOUtils;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.router.provider.FileIOUtilsService;

import java.io.InputStream;

@Route(path = ARouterPath.Service.FileIOUtilsService)
public class FileIOUtilsServiceImpl implements FileIOUtilsService {

    @Override
    public boolean writeFileFromIS(String filePath, InputStream is) {
        return FileIOUtils.writeFileFromIS(filePath, is);
    }

    @Override
    public void init(Context context) {

    }
}
