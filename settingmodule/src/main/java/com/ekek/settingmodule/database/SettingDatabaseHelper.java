package com.ekek.settingmodule.database;

import android.content.Context;

import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.entity.DaoMaster;
import com.ekek.settingmodule.entity.DaoSession;

import org.greenrobot.greendao.database.Database;

public class SettingDatabaseHelper {
    private static SettingDatabaseHelper sigleton = null;
    private static  DaoMaster.DevOpenHelper helper;
    private static DaoSession daoSession;

    public SettingDatabaseHelper(Context context) {
        initDatabase(context);
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    private static void initDatabase(Context context) {
        helper = new DaoMaster.DevOpenHelper(context, CataSettingConstant.DATABASE_ENCRYPTED ? CataSettingConstant.DATABASE_NAME_ENCRYPTED : CataSettingConstant.DATABASE_NAME);
        Database db = CataSettingConstant.DATABASE_ENCRYPTED  ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public static SettingDatabaseHelper getInstance(Context context) {
        if(sigleton == null){
            synchronized(SettingDatabaseHelper.class){
                if(sigleton == null){
                    sigleton = new SettingDatabaseHelper(context);
                }
            }
        }
        return sigleton;
    }
}
