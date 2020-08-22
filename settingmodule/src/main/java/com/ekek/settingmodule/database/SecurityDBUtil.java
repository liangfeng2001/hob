package com.ekek.settingmodule.database;

import android.content.Context;

import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.entity.SecurityTable;
import com.ekek.settingmodule.entity.SecurityTableDao;

import java.util.List;

public class SecurityDBUtil {

    public static SecurityTable getDefaultSecurity(Context context) {
        SecurityTableDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSecurityTableDao();
        SecurityTable defaultSecurity = dao.queryBuilder().where(SecurityTableDao.Properties.Enable.eq(true)).build().unique();
        return defaultSecurity;
    }

    public static SecurityTable getSecurity(Context context,String type) {
        SecurityTableDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSecurityTableDao();
        SecurityTable security = dao.queryBuilder().where(SecurityTableDao.Properties.Type.eq(type)).build().unique();
        return security;
    }

    public static void setDefaultSecurity(Context context,String type,String password) {
        SecurityTableDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSecurityTableDao();
        List<SecurityTable> securityTables = dao.loadAll();
        for (SecurityTable securityTable : securityTables) {
            if (securityTable.getType().equals(type)) {
                securityTable.setEnable(true);
                securityTable.setPassword(password);
            }else securityTable.setEnable(false);
            dao.update(securityTable);
        }
    }

    public static void setDefaultSecurity(Context context,String type) {
        SecurityTableDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSecurityTableDao();
        List<SecurityTable> securityTables = dao.loadAll();
        for (SecurityTable securityTable : securityTables) {
            if (securityTable.getType().equals(type)) {
                securityTable.setEnable(true);
            }else securityTable.setEnable(false);
            dao.update(securityTable);
        }
    }



    public static void updatePassword(Context context,String type,String password) {
        SecurityTableDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSecurityTableDao();
        SecurityTable securityTable = dao.queryBuilder()
                .where(SecurityTableDao.Properties.Type.eq(type))
                .build().unique();

        securityTable.setPassword(password);
        dao.update(securityTable);
    }

    public static void initSecurityDatabase(Context context) {
        SecurityTableDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSecurityTableDao();
        if (dao.count() == 0) {
            SecurityTable pressSecurityTable = new SecurityTable(null, CataSettingConstant.SECURITY_MODE_PRESS_UNLOCK,"",true,"");
            dao.insert(pressSecurityTable);
            SecurityTable patternSecurityTable = new SecurityTable(null, CataSettingConstant.SECURITY_MODE_PATTERN,"",false,"1");
            dao.insert(patternSecurityTable);
            SecurityTable pinSecurityTable = new SecurityTable(null, CataSettingConstant.SECURITY_MODE_PIN,"",false,"2");
            dao.insert(pinSecurityTable);
        }

    }

}
