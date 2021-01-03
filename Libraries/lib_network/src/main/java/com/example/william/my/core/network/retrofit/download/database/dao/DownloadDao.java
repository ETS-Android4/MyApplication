package com.example.william.my.core.network.retrofit.download.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.william.my.core.network.retrofit.download.database.table.DownloadTask;

import java.util.List;

@Dao
public interface DownloadDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDownloadTask(DownloadTask downloadTask);

    @Query("DELETE FROM download WHERE url IN (:url)")
    void deleteDownloadTask(String url);

    @Query("SELECT * FROM download ")
    LiveData<List<DownloadTask>> getDownloadTaskList();

    @Query("SELECT * FROM download WHERE url IN (:url)")
    LiveData<DownloadTask> getDownloadTask(String url);

    @Query("DELETE FROM download")
    void deleteAllDownloadTask();
}
