package com.ekek.tfthobmodule.database;

import com.ekek.tfthobmodule.CataTFTHobApplication;
import com.ekek.tfthobmodule.constants.TFTHobDatabaseConstant;
import com.ekek.tfthobmodule.entity.Setting;
import com.ekek.tfthobmodule.entity.SettingDao;

import java.util.List;

public class SettingDbHelper {

    /**
     * 保存温度传感器使用状态，如是-1则表示空闲，其他值表示哪个炉头再使用，如1，表示炉头1在使用，0表示未连接
     * @param status 炉头ID，表示哪个炉头占用了，如是-1则表示传感器空闲状态
     * @see com.ekek.tfthobmodule.constants.TFTHobConstant
     *
     * */
    public static synchronized void saveTemperatureSensorStatus(int status) {
        SettingDao settingDao = CataTFTHobApplication.getDaoSession().getSettingDao();
        List<Setting> settings = settingDao.queryBuilder().where(SettingDao.Properties.Item.eq(TFTHobDatabaseConstant.KEY_TEMPERATURE_SENSOR_STATUS)).list();
        if (settings.size() == 0) {
            Setting setting = new Setting(null , TFTHobDatabaseConstant.KEY_TEMPERATURE_SENSOR_STATUS ,String.valueOf(status) , "0");
            settingDao.insertOrReplace(setting);
        }else if (settings.size() == 1) {
            Setting setting = settings.get(0);
            setting.setParameter(String.valueOf(status));
            settingDao.update(setting);
        }

    }

    /**
     * 获取温度传感器使用状态，如是-1则表示空闲，其他值表示哪个炉头再使用，如1，表示炉头1在使用,-1表示温度传感器已连接，但没有被占用，0表示未连接
     *
     * */
    public static synchronized int getTemperatureSensorStatus() {
        int status = -1;
        SettingDao settingDao = CataTFTHobApplication.getDaoSession().getSettingDao();
        List<Setting> settings = settingDao.queryBuilder().where(SettingDao.Properties.Item.eq(TFTHobDatabaseConstant.KEY_TEMPERATURE_SENSOR_STATUS)).list();
        if (settings.size() == 0) {
            return status;
        }else if (settings.size() == 1) {
            Setting setting = settings.get(0);
            return Integer.valueOf(setting.getParameter());
        }
        return status;
    }

    public static synchronized void saveTemperatureSensorValue(int value) {
        SettingDao settingDao = CataTFTHobApplication.getDaoSession().getSettingDao();
        List<Setting> settings = settingDao.queryBuilder().where(SettingDao.Properties.Item.eq(TFTHobDatabaseConstant.KEY_TEMPERATURE_SENSOR_VALUE)).list();
        if (settings.size() == 0) {
            Setting setting = new Setting(null , TFTHobDatabaseConstant.KEY_TEMPERATURE_SENSOR_VALUE ,String.valueOf(value) , "0");
            settingDao.insertOrReplace(setting);
        }else if (settings.size() == 1) {
            Setting setting = settings.get(0);
            setting.setParameter(String.valueOf(value));
            settingDao.update(setting);
        }

    }

    public static synchronized int getTemperatureSensorValue() {
        int value = 26;
        SettingDao settingDao = CataTFTHobApplication.getDaoSession().getSettingDao();
        List<Setting> settings = settingDao.queryBuilder().where(SettingDao.Properties.Item.eq(TFTHobDatabaseConstant.KEY_TEMPERATURE_SENSOR_VALUE)).list();
        if (settings.size() == 0) {
            return value;
        }else if (settings.size() == 1) {
            Setting setting = settings.get(0);
            return Integer.valueOf(setting.getParameter());
        }
        return value;
    }

    public static synchronized void saveRecipesCookerID(int id) {
        SettingDao settingDao = CataTFTHobApplication.getDaoSession().getSettingDao();
        List<Setting> settings = settingDao.queryBuilder().where(SettingDao.Properties.Item.eq(TFTHobDatabaseConstant.KEY_RECIPES_COOKER_ID)).list();
        if (settings.size() == 0) {
            Setting setting = new Setting(null , TFTHobDatabaseConstant.KEY_RECIPES_COOKER_ID ,String.valueOf(id) , "0");
            settingDao.insertOrReplace(setting);
        }else if (settings.size() == 1) {
            Setting setting = settings.get(0);
            setting.setParameter(String.valueOf(id));
            settingDao.update(setting);
        }

    }

    public static synchronized int getRecipesCookerID() {
        int value = 1;
        SettingDao settingDao = CataTFTHobApplication.getDaoSession().getSettingDao();
        List<Setting> settings = settingDao.queryBuilder().where(SettingDao.Properties.Item.eq(TFTHobDatabaseConstant.KEY_RECIPES_COOKER_ID)).list();
        if (settings.size() == 0) {
            return value;
        }else if (settings.size() == 1) {
            Setting setting = settings.get(0);
            return Integer.valueOf(setting.getParameter());
        }
        return value;
    }

}
