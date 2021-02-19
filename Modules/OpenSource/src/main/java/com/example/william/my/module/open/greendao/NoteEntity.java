package com.example.william.my.module.open.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class NoteEntity {

    @Id(autoincrement = true)//设置自增长
    private long id;

    @Index(unique = true)//设置唯一性
    private String name;

    @Generated(hash = 1981305349)
    public NoteEntity(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Generated(hash = 734234824)
    public NoteEntity() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
