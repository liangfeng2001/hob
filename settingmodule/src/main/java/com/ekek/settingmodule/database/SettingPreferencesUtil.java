package com.ekek.settingmodule.database;

import android.content.Context;
import android.util.Log;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.commonmodule.utils.Logger;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.constants.SettingDatabaseConstant;
import com.ekek.settingmodule.entity.SettingPreferences;
import com.ekek.settingmodule.entity.SettingPreferencesDao;
import com.ekek.settingmodule.events.SettingsChangedEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Samhung on 2018/2/1.
 */

public class SettingPreferencesUtil {
    //default theme
    public static void saveDefaultTheme(Context context,String name) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.DEFAULT_THEME)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null, SettingDatabaseConstant.DEFAULT_THEME,name);
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(name);
            dao.update(settingPreferences);
        }
    }

    public static String getDefaultTheme(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.DEFAULT_THEME)).build().unique();
        if (settingPreferences == null) {
            return CataSettingConstant.DEFAULT_THEME;
        }else {
            return settingPreferences.getParameter();
        }
    }

    //default brightness
    public static void saveDefaultBrightness(Context context,String brightness) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.DEFAULT_BRIGHTNESS)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.DEFAULT_BRIGHTNESS,brightness);
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(brightness);
            dao.update(settingPreferences);
        }
    }

    public static String getDefaultBrightness(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.DEFAULT_BRIGHTNESS)).build().unique();
        if (settingPreferences == null) {
            return CataSettingConstant.DEFAULT_BRIGHTNESS;
        }else {
            return settingPreferences.getParameter();
        }
    }

    //default language
    public static void saveDefaultLanguage(Context context,String language) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.DEFAULT_LANGUAGE)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.DEFAULT_LANGUAGE,language);
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(language);
            dao.update(settingPreferences);
        }
    }

    public static String getDefaultLanguage(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.DEFAULT_LANGUAGE)).build().unique();
        if (settingPreferences == null) {
            return CataSettingConstant.DEFAULT_LANGUAGE;
        }else {
            return settingPreferences.getParameter();
        }
    }

    public static void saveDefaultLanguage2(Context context,int language) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(
                SettingDatabaseConstant.DEFAULT_LANGUAGE_2)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.DEFAULT_LANGUAGE_2,"" + language);
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter("" + language);
            dao.update(settingPreferences);
        }
    }

    public static int getDefaultLanguage2(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(
                SettingDatabaseConstant.DEFAULT_LANGUAGE_2)).build().unique();
        if (settingPreferences == null) {
            return CataSettingConstant.LANGUAGE_UNKNOWN;
        }else {
            return Integer.parseInt(settingPreferences.getParameter());
        }
    }

    // default bluetooth style

    public static void saveBluetoothStyle(Context context,String status) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.BLUETOOTH_STYLE)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.BLUETOOTH_STYLE,status);
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(status);
            dao.update(settingPreferences);
        }
    }

    public static String getBluetoothStyle(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.BLUETOOTH_STYLE)).build().unique();
        if (settingPreferences == null) {
            return CataSettingConstant.DEFAULT_BLUETOOTH_STYLE;
        }else {
            return settingPreferences.getParameter();
        }
    }

    // default bluetooth switch status

    public static void saveBluetoothSwitchStatus(Context context,String status) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.BLUETOOTH_SWITCH_STATUS)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.BLUETOOTH_SWITCH_STATUS,status);
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(status);
            dao.update(settingPreferences);
        }
    }

    public static String getBluetoothSwitchStatus(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.BLUETOOTH_SWITCH_STATUS)).build().unique();
        if (settingPreferences == null) {
            return CataSettingConstant.DEFAULT_BLUETOOTH_SWITCH_STATUS;
        }else {
            return settingPreferences.getParameter();
        }
    }


    //default sound switch status
    public static void saveSoundSwitchStatus(Context context,String status) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.SOUND_SWITCH_STATUS)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.SOUND_SWITCH_STATUS,status);
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(status);
            dao.update(settingPreferences);
        }
    }


    public static String getSoundSwitchStatus(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.SOUND_SWITCH_STATUS)).build().unique();
        if (settingPreferences == null) {
            return CataSettingConstant.DEFAULT_SOUND_SWITCH_STATUS;
        }else {
            return settingPreferences.getParameter();
        }
    }

    //default sound
    public static void saveDefaultSound(Context context,String level) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.DEFAULT_SOUND)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.DEFAULT_SOUND,level);
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(level);
            dao.update(settingPreferences);
        }
    }

    public static String getDefaultSound(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.DEFAULT_SOUND)).build().unique();
        if (settingPreferences == null) {
            return CataSettingConstant.DEFAULT_SOUND_LEVEL;
        }else {
            return settingPreferences.getParameter();
        }
    }

    //default power limit
    public static void saveDefaultPowerLimit(Context context,String level) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.DEFAULT_POWER_LIMIT)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.DEFAULT_POWER_LIMIT,level);
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(level);
            dao.update(settingPreferences);
        }
    }

    public static String getDefaultPowerLimit(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.DEFAULT_POWER_LIMIT)).build().unique();
        if (settingPreferences == null) {
            return CataSettingConstant.DEFAULT_POWER_LIMIT;
        }else {
            return settingPreferences.getParameter();
        }
    }

    //default hood mode
    public static void saveDefaultHoodMode(Context context,String mode) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.DEFAULT_HOOD_MODE)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.DEFAULT_HOOD_MODE,mode);
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(mode);
            dao.update(settingPreferences);
        }
    }

    public static String getDefaultHoodMode(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.DEFAULT_HOOD_MODE)).build().unique();
        if (settingPreferences == null) {
            return CataSettingConstant.DEFAULT_HOOD_MODE;
        }else {
            return settingPreferences.getParameter();
        }
    }


    //default hood level
    public static void saveDefaultHoodLevel(Context context,String level) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.DEFAULT_HOOD_LEVEL)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.DEFAULT_HOOD_LEVEL,level);
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(level);
            dao.update(settingPreferences);
        }
    }

    public static String getDefaultHoodLevel(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.DEFAULT_HOOD_LEVEL)).build().unique();
        if (settingPreferences == null) {
            return CataSettingConstant.DEFAULT_HOOD_LEVEL;
        }else {
            return settingPreferences.getParameter();
        }
    }


    //default security mode
    public static void saveDefaultSecurityMode(Context context,String mode) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.DEFAULT_SECURITY_MODE)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.DEFAULT_SECURITY_MODE,mode);
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(mode);
            dao.update(settingPreferences);
        }
    }

    public static String getDefaultSecurityMode(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.DEFAULT_SECURITY_MODE)).build().unique();
        if (settingPreferences == null) {
            return CataSettingConstant.DEFAULT_SECURITY_MODE;
        }else {
            return settingPreferences.getParameter();
        }
    }

    //security pin password
    public static void saveSecurityPinPassword(Context context,String pwd) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.SECURITY_PIN_PASSWORD)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.SECURITY_PIN_PASSWORD,pwd);
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(pwd);
            dao.update(settingPreferences);
        }
    }

    public static String getSecurityPinPassword(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.SECURITY_PIN_PASSWORD)).build().unique();
        if (settingPreferences == null) {
            return "";
        }else {
            return settingPreferences.getParameter();
        }
    }


    //security pattern password
    public static void saveSecurityPatternPassword(Context context,String pwd) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.SECURITY_PATTERN_PASSWORD)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.SECURITY_PATTERN_PASSWORD,pwd);
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(pwd);
            dao.update(settingPreferences);
        }
    }

    public static String getSecurityPatternPassword(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.SECURITY_PATTERN_PASSWORD)).build().unique();
        if (settingPreferences == null) {
            return "";
        }else {
            return settingPreferences.getParameter();
        }
    }

    //default hood options switch status
    public static void saveStablishConnectionSwitchStatus(Context context,String status) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.STABLISH_CONNECTION)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.STABLISH_CONNECTION,status);
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(status);
            dao.update(settingPreferences);
        }
    }

    public static String getStablishConnectionSwitchStatus(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.STABLISH_CONNECTION)).build().unique();
        if (settingPreferences == null) {
            return CataSettingConstant.DEFAULT_STABLISH_CONNECTION_SWITCH_STATUS;
        }else {
            return settingPreferences.getParameter();
        }
    }

    //default hood options switch status
    public static void saveHoodOptionsAutoModeSwitchStatus(Context context,String status) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.HOOD_OPTIONS_AUTO_MODE_STATUS)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.HOOD_OPTIONS_AUTO_MODE_STATUS,status);
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(status);
            dao.update(settingPreferences);
        }
    }

    public static String getHoodOptionsAutoModeSwitchStatus(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.HOOD_OPTIONS_AUTO_MODE_STATUS)).build().unique();
        if (settingPreferences == null) {
            return CataSettingConstant.DEFAULT_HOOD_OPTIONS_AUTO_MODE_SWITCH_STATUS;
        }else {
            return settingPreferences.getParameter();
        }
    }

    //default hood options auto mode
    public static void saveHoodOptionsAutoMode(Context context,int mode) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.HOOD_OPTIONS_AUTO_MODE)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.HOOD_OPTIONS_AUTO_MODE,String.valueOf(mode));
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(String.valueOf(mode));
            dao.update(settingPreferences);
        }
    }

    public static int getHoodOptionsAutoMode(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.HOOD_OPTIONS_AUTO_MODE)).build().unique();
        if (settingPreferences == null) {
            return CataSettingConstant.DEFAULT_HOOD_OPTIONS_AUTO_MODE;
        }else {
            return Integer.valueOf(settingPreferences.getParameter());
        }
    }

    //default hood options switch status
    public static void savePowerLimitSwitchStatus(Context context,String status) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.POWER_LIMIT_SWITCH_STATUS)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.POWER_LIMIT_SWITCH_STATUS,status);
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(status);
            dao.update(settingPreferences);
        }
    }

    public static String getPowerLimitSwitchStatus(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.POWER_LIMIT_SWITCH_STATUS)).build().unique();
        if (settingPreferences == null) {
            return CataSettingConstant.DEFAULT_POWER_LIMIT_SWITCH_STATUS;
        }else {
            return settingPreferences.getParameter();
        }
    }

    //default power limit level
    public static void savePowerLimitLevel(Context context,int level) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.POWER_LIMIT_POWER_LEVEL)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.POWER_LIMIT_POWER_LEVEL,String.valueOf(level));
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(String.valueOf(level));
            dao.update(settingPreferences);
        }
    }

    public static int getPowerLimitLevel(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.POWER_LIMIT_POWER_LEVEL)).build().unique();
        if (settingPreferences == null) {
            return CataSettingConstant.DEFAULT_POWER_LIMIT_LEVEL;
        }else {
            return Integer.valueOf(settingPreferences.getParameter());
        }
    }

    //default click sound switch status
    public static void saveClickSoundSwitchStatus(Context context,String status) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.CLICK_SOUND_SWITCH)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.CLICK_SOUND_SWITCH,status);
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(status);
            dao.update(settingPreferences);
        }
    }

    public static String getClickSoundSwitchStatus(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.CLICK_SOUND_SWITCH)).build().unique();
        if (settingPreferences == null) {
            return CataSettingConstant.DEFAULT_CLICK_SOUND_SWITCH_STATUS;
        }else {
            return settingPreferences.getParameter();
        }
    }

    //default click sound switch status
    public static void saveTimeFormat24(Context context,int timeFormat) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.TIME_FORMAT_24)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.TIME_FORMAT_24,String.valueOf(timeFormat));
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(String.valueOf(timeFormat));
            dao.update(settingPreferences);
        }
    }

    public static boolean getTimeFormat24(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.TIME_FORMAT_24)).build().unique();
        if (settingPreferences == null) {
            return true;
        }else {
            int timeFormat = Integer.valueOf(settingPreferences.getParameter());
            return (timeFormat == CataSettingConstant.DEFAULT_TIME_24_FORMAT);
        }
    }


    //default increase volume switch status
    public static void saveIncreaseVolumeSwitchStatus(Context context,String status) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.INCREASE_VOLUME_SWITCH_STATUS)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.INCREASE_VOLUME_SWITCH_STATUS,status);
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(status);
            dao.update(settingPreferences);
        }
    }

    public static String getIncreaseVolumeSwitchStatus(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.INCREASE_VOLUME_SWITCH_STATUS)).build().unique();
        if (settingPreferences == null) {
            return CataSettingConstant.DEFAULT_INCREASE_VOLUME_SWITCH_STATUS;
        }else {
            return settingPreferences.getParameter();
        }
    }

    //default alarm volume level
    public static void saveAlarmVolumeLevel(Context context,int level) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.ALARM_VOLUME_LEVEL)).build().unique();

        if (settingPreferences == null) {
            LogUtil.d("Enter::---------------------1");
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.ALARM_VOLUME_LEVEL,String.valueOf(level));
            dao.insertOrReplace(settingPreferences);
        }else {
            LogUtil.d("Enter::--------------------2");
            settingPreferences.setParameter(String.valueOf(level));
            dao.update(settingPreferences);
        }
    }

    public static int getAlarmVolumeLevel(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.ALARM_VOLUME_LEVEL)).build().unique();
        if (settingPreferences == null) {
            return CataSettingConstant.DEFAULT_ALARM_VOLUME_LEVEL;
        }else {
            return Integer.valueOf(settingPreferences.getParameter());
        }
    }

    //default alarm duration
    public static void saveAlarmDuration(Context context,int duration) {//秒
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.ALARM_DURATION)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.ALARM_DURATION,String.valueOf(duration));
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(String.valueOf(duration));
            dao.update(settingPreferences);
        }
    }

    public static int getAlarmDuration(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.ALARM_DURATION)).build().unique();
        if (settingPreferences == null) {
            return CataSettingConstant.DEFAULT_ALARM_DURATION;
        }else {
            return Integer.valueOf(settingPreferences.getParameter());
        }
    }

    //default alarm duration
    public static void saveAlarmPostponeDuration(Context context,int duration) {//秒
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.ALARM_POSTPONE_DURATION)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.ALARM_POSTPONE_DURATION,String.valueOf(duration));
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(String.valueOf(duration));
            dao.update(settingPreferences);
        }
    }

    public static int getAlarmPostponeDuration(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.ALARM_POSTPONE_DURATION)).build().unique();
        if (settingPreferences == null) {
            return CataSettingConstant.DEFAULT_ALARM_POSTPONE_DURATION;
        }else {
            return Integer.valueOf(settingPreferences.getParameter());
        }
    }

    //demo switch status
    public static void saveDemoSwitchStatus(Context context,String status) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.DEMO_SWITCH_STATUS)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.DEMO_SWITCH_STATUS,status);
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(status);
            dao.update(settingPreferences);
        }
        GlobalVars.getInstance().setInDemoMode(status.equals(CataSettingConstant.DEMO_SWITCH_STATUS_OPEN));
    }

    public static String getDemoSwitchStatus(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.DEMO_SWITCH_STATUS)).build().unique();
        String result;
        if (settingPreferences == null) {
            result = CataSettingConstant.DEFAULT_DEMO_SWITCH_STATUS;
        }else {
            result = settingPreferences.getParameter();
        }

        GlobalVars.getInstance().setInDemoMode(result.equals(CataSettingConstant.DEMO_SWITCH_STATUS_OPEN));
        return result;
    }

    //logo switch status
    public static void saveLogoSwitchStatus(Context context,String status) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.LOGO_SWITCH_STATUS)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.LOGO_SWITCH_STATUS,status);
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(status);
            dao.update(settingPreferences);
        }
    }

    public static String getLogoSwitchStatus(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.LOGO_SWITCH_STATUS)).build().unique();
        if (settingPreferences == null) {
            return CataSettingConstant.DEFAULT_LOGO_SWITCH_STATUS;
        }else {
            return settingPreferences.getParameter();
        }
    }

    //hibernation mode switch status
    public static void saveHibernationModeSwitchStatus(Context context,String status) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.HIBERNATION_MODE_SWITCH_STATUS)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.HIBERNATION_MODE_SWITCH_STATUS,status);
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(status);
            dao.update(settingPreferences);
        }
        GlobalVars.getInstance().setHibernationModeEnabled(status.equals(CataSettingConstant.HIBERNATION_MODE_SWITCH_STATUS_OPEN));
        EventBus.getDefault().post(new SettingsChangedEvent(SettingsChangedEvent.SETTING_HIBERNATION_MODE));
    }

    public static String getHibernationModeSwitchStatus(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.HIBERNATION_MODE_SWITCH_STATUS)).build().unique();

        String result;
        if (settingPreferences == null) {
            result = CataSettingConstant.DEFAULT_HIBERNATION_MODE_SWITCH_STATUS;
        }else {
            result = settingPreferences.getParameter();
        }

        GlobalVars.getInstance().setHibernationModeEnabled(result.equals(CataSettingConstant.HIBERNATION_MODE_SWITCH_STATUS_OPEN));
        return result;
    }

    //total turn off switch status

    /**
     * Discarded
     * @param context
     * @param status
     */
    public static void saveTotalTurnOffSwitchStatus(Context context,String status) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.TOTAL_TURN_OFF_SWITCH_STATUS)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.TOTAL_TURN_OFF_SWITCH_STATUS,status);
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(status);
            dao.update(settingPreferences);
        }

        GlobalVars.getInstance().setTotalTurnOffModeEnabled(status.equals(CataSettingConstant.TOTAL_TURN_OFF_SWITCH_STATUS_OPEN) );
        EventBus.getDefault().post(new SettingsChangedEvent(SettingsChangedEvent.SETTING_TOTAL_TURN_OFF_MODE));
    }

    /**
     * Discarded
     * @param context
     * @return
     */
    public static String getTotalTurnOffSwitchStatus(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.TOTAL_TURN_OFF_SWITCH_STATUS)).build().unique();

        String result;
        if (settingPreferences == null) {
            result = CataSettingConstant.DEFAULT_TOTAL_TURN_OFF_SWITCH_STATUS;
        }else {
            result = settingPreferences.getParameter();
        }

        GlobalVars.getInstance().setTotalTurnOffModeEnabled(result.equals(CataSettingConstant.TOTAL_TURN_OFF_SWITCH_STATUS_OPEN));
        return result;
    }

    //activation time duration
    public static void saveActivationTime(Context context,int duration) {//秒
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.ACTIVATION_TIME)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.ACTIVATION_TIME,String.valueOf(duration));
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(String.valueOf(duration));
            dao.update(settingPreferences);
        }
        EventBus.getDefault().post(new SettingsChangedEvent(SettingsChangedEvent.SETTING_ACTIVATION_TIME));
    }

    public static int getActivationTime(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.ACTIVATION_TIME)).build().unique();
        if (settingPreferences == null) {
            return CataSettingConstant.DEFAULT_ACTIVATION_TIME;
        }else {
            return Integer.valueOf(settingPreferences.getParameter());
        }
    }

    //Enter power off mode when total turn off time is up
    public static void saveEnterPowerOffMode(Context context, int flag) {// 进入关机模式，标志
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.ENTER_POWER_OFF_MODE)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.ENTER_POWER_OFF_MODE,String.valueOf(flag));
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(String.valueOf(flag));
            dao.update(settingPreferences);
        }

        Logger.getInstance().i("saveEnterPowerOffMode(" + getEnterPowerOffString(flag) + ")", true);
    }

    public static int getEnterPowerOffMode(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.ENTER_POWER_OFF_MODE)).build().unique();

        int result;
        if (settingPreferences == null) {
            result = CataSettingConstant.EnterPowerOnModel;
        }else {
            result = Integer.valueOf(settingPreferences.getParameter());
        }
        return result;
    }

    private static String getEnterPowerOffString(int value) {
        switch (value) {
            case CataSettingConstant.EnterNone:
                return "EnterNone";
            case CataSettingConstant.EnterPowerOffModel:
                return "EnterPowerOffModel";
            case CataSettingConstant.EnterPowerOffModelDelay:
                return "EnterPowerOffModelDelay";
            case CataSettingConstant.EnterPowerOnModel:
                return "EnterPowerOnModel";
            case CataSettingConstant.EnterLanguageSettingFragment:
                return "EnterLanguageSettingFragment";
            case CataSettingConstant.EnterDateSettingFragment:
                return "EnterDateSettingFragment";
            case CataSettingConstant.EnterTimeSettingFragment:
                return "EnterTimeSettingFragment";
            case CataSettingConstant.EnterHobIntroFragment:
                return "EnterHobIntroFragment";
            case CataSettingConstant.EnterHobWorkingFragment:
                return "EnterHobWorkingFragment";
            case CataSettingConstant.EnterPowerOffModelFromSerialPort:
                return "EnterPowerOffModelFromSerialPort";
        }
        return "Unknown";
    }

    //total turn off time
    public static void saveTotalTurnOffTime(Context context,int duration) {//秒
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.TOTAL_TURN_OFF_TIME)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.TOTAL_TURN_OFF_TIME,String.valueOf(duration));
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(String.valueOf(duration));
            dao.update(settingPreferences);
        }
        EventBus.getDefault().post(new SettingsChangedEvent(SettingsChangedEvent.SETTING_TOTAL_TURN_OFF_TIME));
    }

    public static int getTotalTurnOffTime(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.TOTAL_TURN_OFF_TIME)).build().unique();
        if (settingPreferences == null) {
            return CataSettingConstant.DEFAULT_TOTAL_TURN_OFF_TIME;
        }else {
            return Integer.valueOf(settingPreferences.getParameter());
        }
    }

    //hibernation  format switch status
    public static void saveHibernationFormatDateSwitchStatus(Context context,String status) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.INTRO_DATE_SWITCH_STATUS)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.INTRO_DATE_SWITCH_STATUS,status);
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(status);
            dao.update(settingPreferences);
        }
    }

    public static String getHibernationFormatDateSwitchStatus(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.INTRO_DATE_SWITCH_STATUS)).build().unique();
        if (settingPreferences == null) {

            return CataSettingConstant.HIBERNATION_FORMAT_DATE_SWITCH_STATUS;
        }else {
            return settingPreferences.getParameter();
        }
    }

    //hibernation Hour format
    public static void saveHibernationHourFormat(Context context,String status) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.HOUR_FORMAT)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.HOUR_FORMAT,status);
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(status);
            dao.update(settingPreferences);
        }
    }

    public static String getHibernationHourFormat(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.HOUR_FORMAT)).build().unique();
        if (settingPreferences == null) {

            return CataSettingConstant.HIBERNATION_FORMAT_HOUR_FORMAT;
        }else {
            return settingPreferences.getParameter();
        }
    }

    //hood setting mode status
    public static void saveHoodSettingModeStatus(Context context,String status) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.HOOD_SETTING_MODE)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.HOOD_SETTING_MODE,status);
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(status);
            dao.update(settingPreferences);
        }
    }

    public static String getHoodSettingModeStatus(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.HOOD_SETTING_MODE)).build().unique();
        if (settingPreferences == null) {
            return CataSettingConstant.DEFAULT_HOOD_SETTING_MODE;
        }else {
            return settingPreferences.getParameter();
        }
    }

    //alarm clock hour
    public static void saveDefaultAlarmClockHour(Context context,int hourValue) {//秒
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.DEFAULT_ALARM_CLOCK_SETTING_HOUR_VALUE)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.DEFAULT_ALARM_CLOCK_SETTING_HOUR_VALUE,String.valueOf(hourValue));
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(String.valueOf(hourValue));
            dao.update(settingPreferences);
        }
    }

    public static int getDefaultAlarmClockHour(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.DEFAULT_ALARM_CLOCK_SETTING_HOUR_VALUE)).build().unique();
        if (settingPreferences == null) {
            return CataSettingConstant.DEFAULT_ALARM_CLOCK_SETTING_HOUR_VALUE;
        }else {
            return Integer.valueOf(settingPreferences.getParameter());
        }
    }

    //alarm clock minute
    public static void saveDefaultAlarmClockMinute(Context context,int hourValue) {//秒
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.DEFAULT_ALARM_CLOCK_SETTING_MINUTE_VALUE)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.DEFAULT_ALARM_CLOCK_SETTING_MINUTE_VALUE,String.valueOf(hourValue));
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(String.valueOf(hourValue));
            dao.update(settingPreferences);
        }
    }

    public static int getDefaultAlarmClockMinute(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.DEFAULT_ALARM_CLOCK_SETTING_MINUTE_VALUE)).build().unique();
        if (settingPreferences == null) {
            return CataSettingConstant.DEFAULT_ALARM_CLOCK_SETTING_MINUTE_VALUE;
        }else {
            return Integer.valueOf(settingPreferences.getParameter());
        }
    }

    // Debug mode extra
    public static void saveDebugModeExtra(Context context, int value) {
        SettingPreferencesDao dao = SettingDatabaseHelper
                .getInstance(context)
                .getDaoSession()
                .getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao
                .queryBuilder()
                .where(SettingPreferencesDao
                        .Properties.Item.eq(SettingDatabaseConstant.DEBUG_MODE_EXTRA_PARAM))
                .build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(
                    null,
                    SettingDatabaseConstant.DEBUG_MODE_EXTRA_PARAM,
                    String.valueOf(value));
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(String.valueOf(value));
            dao.update(settingPreferences);
        }
    }

    public static int getDebugModeExtra(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper
                .getInstance(context)
                .getDaoSession()
                .getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao
                .queryBuilder()
                .where(SettingPreferencesDao
                        .Properties.Item.eq(SettingDatabaseConstant.DEBUG_MODE_EXTRA_PARAM))
                .build().unique();
        if (settingPreferences == null) {
            return CataSettingConstant.DEFAULT_DEBUG_MODE_EXTRA_PARAM;
        }else {
            return Integer.valueOf(settingPreferences.getParameter());
        }
    }


    public static String getTheFirstTimeSwitchOnHob(Context context){

        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.DEFAULT_THE_FIRST_TIME)).build().unique();
        if (settingPreferences == null) {

            return CataSettingConstant.DEFAULT_THE_FIRST_TIME_SWITCH_ON_HOB;
        }else {
            return settingPreferences.getParameter();
        }

    }

    public static void saveTheFirstTimeSwitchOnHob(Context context,String status) {//
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.DEFAULT_THE_FIRST_TIME)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.DEFAULT_THE_FIRST_TIME,status);
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(status);
            dao.update(settingPreferences);
        }
    }

    //timer open status (H103)
    public static void saveTimerOpenStatus(Context context,String status) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.TIMER_WHAT_STATUS)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.TIMER_WHAT_STATUS,status);
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(status);
            dao.update(settingPreferences);
        }
    }

    public static String getTimerOpenStatus(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.TIMER_WHAT_STATUS)).build().unique();
        if (settingPreferences == null) {

            return CataSettingConstant.TIMER_STATUS;
        }else {
            return settingPreferences.getParameter();
        }
    }

    //fan auto/manual status (H104)
    public static void saveFanWorkingStatus(Context context,String status) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.FAN_WORKING_STATUS)).build().unique();

        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(null,SettingDatabaseConstant.FAN_WORKING_STATUS,status);
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(status);
            dao.update(settingPreferences);
        }
    }

    public static String getFanWorkingStatus(Context context) {
        SettingPreferencesDao dao = SettingDatabaseHelper.getInstance(context).getDaoSession().getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao.queryBuilder().where(SettingPreferencesDao.Properties.Item.eq(SettingDatabaseConstant.FAN_WORKING_STATUS)).build().unique();
        if (settingPreferences == null) {

            return CataSettingConstant.FAN_WORKING_STATUS;
        }else {
            return settingPreferences.getParameter();
        }
    }

    public static void saveDebugMode(Context context, boolean active) {
        SettingPreferencesDao dao = SettingDatabaseHelper
                .getInstance(context)
                .getDaoSession()
                .getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao
                .queryBuilder()
                .where(SettingPreferencesDao
                        .Properties.Item
                        .eq(SettingDatabaseConstant.DEBUG_MODE))
                .build()
                .unique();

        String value = active ? "1" : "0";
        if (settingPreferences == null) {
            settingPreferences = new SettingPreferences(
                    null,
                    SettingDatabaseConstant.DEBUG_MODE,
                    value);
            dao.insertOrReplace(settingPreferences);
        }else {
            settingPreferences.setParameter(value);
            dao.update(settingPreferences);
        }
        GlobalVars.getInstance().setInDebugMode(active);
        if (active) {
            Logger.getInstance().setLoggingLevel(Log.DEBUG);
        } else {
            Logger.getInstance().resetLoggingLevel();
        }
    }

    public static Boolean getDebugMode(Context context) {

        SettingPreferencesDao dao = SettingDatabaseHelper
                .getInstance(context)
                .getDaoSession()
                .getSettingPreferencesDao();
        SettingPreferences settingPreferences = dao
                .queryBuilder()
                .where(SettingPreferencesDao
                        .Properties.Item
                        .eq(SettingDatabaseConstant.DEBUG_MODE))
                .build()
                .unique();
        Boolean result;
        if (settingPreferences == null) {
            result = CataSettingConstant.DEFAULT_DEBUG_MODE == CataSettingConstant.DEBUG_MODE_YES;
        }else {
            result = Integer.parseInt(settingPreferences.getParameter()) == CataSettingConstant.DEBUG_MODE_YES;
        }
        GlobalVars.getInstance().setInDebugMode(result);
        if (result) {
            Logger.getInstance().setLoggingLevel(Log.DEBUG);
        } else {
            Logger.getInstance().resetLoggingLevel();
        }
        return result;
    }
}
