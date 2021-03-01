package com.example.william.my.module.jetpack.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.william.my.module.jetpack.database.table.OAuth;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface OAuthDao {

    /**
     * 按指定列按降序排列
     */
    @Query("SELECT * FROM OAuth ORDER BY Expires DESC")
    Flowable<OAuth> getOAuth();

    @Query("SELECT * FROM oauth WHERE id IN (:userId)")
    OAuth getUserById(String userId);

    /**
     * 冲突策略为取代旧数据同时继续事务
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOAuth(OAuth oAuth);

    @Query("DELETE FROM OAuth")
    void deleteAllOAuth();

    @Insert
    void insert(OAuth oAuth);

    @Delete
    void delete(OAuth oAuth);

    @Update
    void update(OAuth oAuth);

    /*
     * 1.若数据库中没有用户，那么Maybe就会被complete（RxJava中概念）
     * 2.若数据库中有一个用户，那么Maybe就会触发onSuccess方法并且被complete
     * 3.若数据库中用户信息在Maybe被complete之后被更新了，啥都不会发生
     */
    @Query("SELECT * FROM oauth WHERE id = :userId")
    Maybe<OAuth> getUserByIdMaybe(int userId);

    /*
     * 1.若数据库中没有用户，那么Single就会触发onError(EmptyResultSetException.class)
     * 2.若数据库中有一个用户，那么Single就会触发onSuccess
     * 3.若数据库中用户信息在Single.onComplete调用之后被更新了，啥都不会发生，因为数据流已经完成了
     */
    @Query("SELECT * FROM oauth WHERE id = :userId")
    Single<OAuth> getUserByIdSingle(int userId);

    /*
     * 1.若数据库中没有用户，那么Flowable就不会发射事件，既不运行onNext,也不运行onError
     * 2.若数据库中有一个用户，那么Flowable就会触发onNext
     * 3.若数据库中用户信息被更新了，Flowable就会自动发射事件，允许你根据更新的数据来更新UI界面
     */
    @Query("SELECT * FROM oauth WHERE id = :userId")
    Flowable<OAuth> getUserByIdFlowable(int userId);
}
