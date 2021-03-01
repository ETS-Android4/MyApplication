package com.example.william.my.core.network.retrofit.download.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.william.my.core.network.retrofit.download.database.dao.DownloadDao;
import com.example.william.my.core.network.retrofit.download.database.table.DownloadTask;

/**
 * exportSchema = false 或者
 * arg("room.schemaLocation", "$projectDir/schemas")
 */
@Database(entities = {DownloadTask.class}, version = 1, exportSchema = false)
public abstract class DownloadDataBase extends RoomDatabase {

    private static DownloadDataBase INSTANCE;

    private static final String DB_NAME = "download-db";

    public static DownloadDataBase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (DownloadDataBase.class) {
                if (INSTANCE == null) {
                    return Room.databaseBuilder(context, DownloadDataBase.class, DB_NAME)
                            //是否允许在主线程进行查询
                            .allowMainThreadQueries()
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

    public abstract DownloadDao getDownloadDao();
}