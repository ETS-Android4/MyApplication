package com.example.william.my.module.jetpack.database.table;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * 授权认证
 */
@Entity(tableName = "oauth")
public class OAuth {

    /**
     * 唯一标识
     */
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;
    /**
     * Token
     */
    @ColumnInfo(name = "RefreshToken")
    private String refreshToken;
    /**
     * 过期时长
     */
    @ColumnInfo(name = "Expires")
    private long expires;

    /**
     * 不需要添加到数据表中的属性
     */
    @Ignore
    private String mRemark;

    public OAuth(@NonNull String id, String refreshToken, long expires) {
        this.id = id;
        this.refreshToken = refreshToken;
        this.expires = expires;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }
}
