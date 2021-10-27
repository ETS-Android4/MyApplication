package com.example.william.my.module.jetpack.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.william.my.module.jetpack.database.dao.OAuthDao;
import com.example.william.my.module.jetpack.database.table.OAuth;

/**
 * exportSchema = false 或者
 * arg("room.schemaLocation", "$projectDir/schemas")
 */
@Database(entities = {OAuth.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase INSTANCE;

    private static final String DB_NAME = "personal-db";

    public static AppDataBase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDataBase.class) {
                if (INSTANCE == null) {
                    return Room.databaseBuilder(context, AppDataBase.class, DB_NAME)
                            //是否允许在主线程进行查询
                            .allowMainThreadQueries()
                            //数据库创建和打开后的回调
                            //.addCallback()
                            //设置查询的线程池
                            //.setQueryExecutor()
                            //.openHelperFactory()
                            //room的日志模式
                            //.setJournalMode()
                            //数据库升级异常之后的回滚
                            //.fallbackToDestructiveMigration()
                            //数据库升级异常后根据指定版本进行回滚
                            //.fallbackToDestructiveMigrationFrom()
                            // .addMigrations(CacheDatabase.sMigration)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static void exit() {
        if (INSTANCE != null) {
            INSTANCE.close();
            INSTANCE = null;
        }
    }

    public abstract OAuthDao getOAuthDao();

}