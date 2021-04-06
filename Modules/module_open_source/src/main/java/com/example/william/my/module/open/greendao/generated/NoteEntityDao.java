package com.example.william.my.module.open.greendao.generated;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.example.william.my.module.open.greendao.NoteEntity;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "NOTE_ENTITY".
 */
public class NoteEntityDao extends AbstractDao<NoteEntity, Long> {

    public static final String TABLENAME = "NOTE_ENTITY";

    /**
     * Properties of entity NoteEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
    }


    public NoteEntityDao(DaoConfig config) {
        super(config);
    }

    public NoteEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /**
     * Creates the underlying database table.
     */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "\"NOTE_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," + // 0: id
                "\"NAME\" TEXT);"); // 1: name
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_NOTE_ENTITY_NAME ON \"NOTE_ENTITY\"" +
                " (\"NAME\" ASC);");
    }

    /**
     * Drops the underlying database table.
     */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"NOTE_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, NoteEntity entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());

        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, NoteEntity entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());

        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }

    @Override
    public NoteEntity readEntity(Cursor cursor, int offset) {
        NoteEntity entity = new NoteEntity( //
                cursor.getLong(offset + 0), // id
                cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1) // name
        );
        return entity;
    }

    @Override
    public void readEntity(Cursor cursor, NoteEntity entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
    }

    @Override
    protected final Long updateKeyAfterInsert(NoteEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }

    @Override
    public Long getKey(NoteEntity entity) {
        if (entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(NoteEntity entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }

}
