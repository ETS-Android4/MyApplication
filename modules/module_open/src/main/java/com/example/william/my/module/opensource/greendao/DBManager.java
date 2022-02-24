package com.example.william.my.module.opensource.greendao;

import com.example.william.my.library.base.BaseApp;
import com.example.william.my.module.opensource.greendao.generated.DaoMaster;
import com.example.william.my.module.opensource.greendao.generated.DaoSession;
import com.example.william.my.module.opensource.greendao.generated.NoteEntityDao;

import java.util.List;

public class DBManager {

    private static DBManager mInstance;

    public static DBManager getInstance() {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager();
                }
            }
        }
        return mInstance;
    }

    private final NoteEntityDao mNoteDao;

    private DBManager() {
        DaoMaster.DevOpenHelper mHelper = new DaoMaster.DevOpenHelper(BaseApp.getApp(), "person.db", null);
        DaoMaster mDaoMaster = new DaoMaster(mHelper.getWritableDatabase());
        DaoSession mDaoSession = mDaoMaster.newSession();
        mNoteDao = mDaoSession.getNoteEntityDao();
    }

    /**
     * 插入 / 替换
     */
    public void insertOrReplace(NoteEntity noteEntity) {
        mNoteDao.insertOrReplace(noteEntity);
    }

    /**
     * 更新数据
     */
    public void update(NoteEntity noteEntity) {
        NoteEntity mNoteEntity = mNoteDao.queryBuilder().where(
                NoteEntityDao.Properties.Id.eq(noteEntity.getId())
        ).build().unique();//拿到之前的记录
        if (mNoteEntity != null) {
            mNoteEntity.setName("张三");
            mNoteDao.update(noteEntity);
        }
    }

    /**
     * 删除数据
     */
    public void delete(String name) {
        mNoteDao.queryBuilder().where(
                NoteEntityDao.Properties.Name.eq(name)
        ).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    /**
     * 查询所有数据
     */
    public List<NoteEntity> searchAll() {
        return mNoteDao.queryBuilder().list();
    }

    /**
     * 按条件查询数据
     */
    public List<NoteEntity> searchByWhere(String name) {
        return mNoteDao.queryBuilder().where(
                NoteEntityDao.Properties.Name.eq(name)
        ).build().list();
    }

}
