package com.example.william.my.module.jetpack.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.jetpack.database.AppDataBase;
import com.example.william.my.module.jetpack.database.dao.OAuthDao;
import com.example.william.my.module.jetpack.database.table.OAuth;
import com.example.william.my.module.router.ARouterPath;
import com.google.gson.Gson;

import java.util.concurrent.Executors;

/**
 * https://developer.android.google.cn/topic/libraries/architecture/room
 */
@Route(path = ARouterPath.JetPack.JetPack_Room)
public class RoomActivity extends BaseResponseActivity {

    private OAuthDao mOAuthDao;

    @Override
    public void initView() {
        super.initView();

        mOAuthDao = AppDataBase.getInstance(this).getOAuthDao();
        OAuth oAuth = mOAuthDao.getUserById("01");
        showResponse(new Gson().toJson(oAuth));
    }


    @Override
    public void setOnClick() {
        super.setOnClick();
        /*
         * query ,Insert ,Update ,需要在后台线程执行
         */
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                mOAuthDao.insertOAuth(new OAuth("01", String.valueOf(System.currentTimeMillis()), 3600 * 24));
                OAuth oAuth = mOAuthDao.getUserById("01");
                showResponse(new Gson().toJson(oAuth));
            }
        });
    }
}
