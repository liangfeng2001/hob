package com.ekek.tfthobmodule.utils;

import com.ekek.tfthobmodule.constants.TFTHobConstant;

/**
 * Created by Samhung on 2017/12/29.
 */

public class CookerGearUtil {

    public static int getFireGearIndex(String value) {
        if (value.equals("10")) value = "B";
        int index = TFTHobConstant.COOKER_DEFAULT_FIRE_GEAR;
        for (int i = 0 ; i < TFTHobConstant.COOKER_FIRE_GEAR_LIST.length ; i ++) {
            if (TFTHobConstant.COOKER_FIRE_GEAR_LIST[i].equals(value)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public static int getFastTempGearIndex(String value) {
        if (!value.contains("°")) value = value + "°";
        int index = TFTHobConstant.COOKER_DEFAULT_FAST_TEMP_GEAR;
        for (int i = 0 ; i < TFTHobConstant.COOKER_FAST_TEMP_LIST.length ; i++) {
            if (TFTHobConstant.COOKER_FAST_TEMP_LIST[i].equals(value)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public static int getPreciseTempGearIndex(String value) {
        int index = TFTHobConstant.COOKER_DEFAULT_FAST_TEMP_GEAR;
        String[] temps = new String[141];
        for (int i = 180; i >= 40; i--) {
            temps[180 - i] = String.valueOf(i);
        }
        for (int i = 0; i < temps.length ; i++) {
            if (temps[i].equals(value)) {
                index = i;
                break;
            }
        }

        return index;
    }

    public static int getTimerHourIndex(String[] hours ,String value) {
        int index = 22;
        for (int i = 0; i < hours.length ; i++) {
            if (hours[i].equals(value)) {
                index = i;
                break;
            }
        }

        return index;
    }

    public static int getTimerMinuteIndex(String[] mintues ,String value) {
        int index = 59;
        for (int i = 0; i < mintues.length ; i++) {
            if (mintues[i].equals(value)) {
                index = i;
                break;
            }
        }

        return index;
    }

    public static int getTempIdentifyResourceID(int value) {
        int id = -1;
        for (int i = 0; i < TFTHobConstant.TEMP_INDENTIFY_LIST.length ;i++) {
            if (TFTHobConstant.TEMP_INDENTIFY_LIST[i][0] == value) {
                id = TFTHobConstant.TEMP_INDENTIFY_LIST[i][1];
                break;
            }
        }

        return id;
    }
}
