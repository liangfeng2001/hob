package com.ekek.tfthobmodule.entity;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.ekek.tfthobmodule.utils.StringConverter;
import java.util.List;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SHELLFISH_TABLE".
*/
public class ShellfishTableDao extends AbstractDao<ShellfishTable, Long> {

    public static final String TABLENAME = "SHELLFISH_TABLE";

    /**
     * Properties of entity ShellfishTable.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "name");
        public final static Property Weight = new Property(2, String.class, "weight", false, "weight");
        public final static Property CookDatas = new Property(3, String.class, "cookDatas", false, "COOK_DATAS");
    }

    private final StringConverter cookDatasConverter = new StringConverter();

    public ShellfishTableDao(DaoConfig config) {
        super(config);
    }
    
    public ShellfishTableDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SHELLFISH_TABLE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"name\" TEXT," + // 1: name
                "\"weight\" TEXT," + // 2: weight
                "\"COOK_DATAS\" TEXT);"); // 3: cookDatas
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SHELLFISH_TABLE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ShellfishTable entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String weight = entity.getWeight();
        if (weight != null) {
            stmt.bindString(3, weight);
        }
 
        List cookDatas = entity.getCookDatas();
        if (cookDatas != null) {
            stmt.bindString(4, cookDatasConverter.convertToDatabaseValue(cookDatas));
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ShellfishTable entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String weight = entity.getWeight();
        if (weight != null) {
            stmt.bindString(3, weight);
        }
 
        List cookDatas = entity.getCookDatas();
        if (cookDatas != null) {
            stmt.bindString(4, cookDatasConverter.convertToDatabaseValue(cookDatas));
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public ShellfishTable readEntity(Cursor cursor, int offset) {
        ShellfishTable entity = new ShellfishTable( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // weight
            cursor.isNull(offset + 3) ? null : cookDatasConverter.convertToEntityProperty(cursor.getString(offset + 3)) // cookDatas
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ShellfishTable entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setWeight(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCookDatas(cursor.isNull(offset + 3) ? null : cookDatasConverter.convertToEntityProperty(cursor.getString(offset + 3)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(ShellfishTable entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(ShellfishTable entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ShellfishTable entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
