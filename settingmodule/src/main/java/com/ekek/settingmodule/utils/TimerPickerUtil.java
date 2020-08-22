package com.ekek.settingmodule.utils;

/**
 * Created by Samhung on 2017/12/29.
 */

public class TimerPickerUtil {

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


}
