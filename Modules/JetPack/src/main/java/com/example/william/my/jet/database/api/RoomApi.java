package com.example.william.my.jet.database.api;

import android.text.TextUtils;

import com.example.william.my.core.network.base.RxRetrofitConfig;
import com.example.william.my.jet.database.AppDataBase;
import com.example.william.my.jet.database.dao.OAuthDao;
import com.example.william.my.jet.database.data.LoginData;
import com.example.william.my.jet.database.table.OAuth;
import com.example.william.my.jet.utils.MyKit;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RoomApi {

    private static RoomApi api;

    private final OAuthDao mOAuthDao;
    private static CompositeDisposable mDisposable;
    private static final long OVERDUE_TIME = 3600 * 24; // 过期时间1天

    public RoomApi() {
        mDisposable = new CompositeDisposable();
        mOAuthDao = AppDataBase.getInstance(RxRetrofitConfig.getApp()).getOAuthDao();
    }

    public static RoomApi getInstance() {
        if (api == null) {
            api = new RoomApi();
        }
        return api;
    }

    public void initOAuth() {
        mDisposable.add(mOAuthDao.getOAuth().subscribe(new Consumer<OAuth>() {
            @Override
            public void accept(OAuth oAuth) throws Exception {
                MyKit.setOAuth(oAuth);
            }
        }));
    }

    public void setOAuth(LoginData user) {
        String id = "default";
        if (!TextUtils.isEmpty(user.getUser().getId())) {
            id = user.getUser().getId();
        } else if (MyKit.getOAuth() != null && !TextUtils.isEmpty(MyKit.getOAuth().getId())) {
            id = MyKit.getOAuth().getId();
        }
        setOAuth(new OAuth(id, "Bearer " + user.getToken(), System.currentTimeMillis() / 1000 + OVERDUE_TIME));
    }

    public void setOAuth(final OAuth oAuthDo) {
        MyKit.setOAuth(oAuthDo);//内存中保存验证信息
        mDisposable.add(Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mOAuthDao.insertOAuth(oAuthDo);
            }
        })
                .subscribeOn(Schedulers.io())
                .subscribe());
    }

    public void deleteOAuth() {
        MyKit.setOAuth(null);//内存中保存验证信息
        mDisposable.add(Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mOAuthDao.deleteAllOAuth();
            }
        })
                .subscribeOn(Schedulers.io())
                .subscribe());
    }

    public static void exit() {
        mDisposable.clear();
        AppDataBase.exit();
        api = null;
    }
}
