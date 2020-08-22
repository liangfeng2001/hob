package com.ekek.tfthobmodule.data;

import java.util.Objects;

public class CookerSettingData {
    public static final int COOKER_SETTING_INVALID_VALUE = -1;//没有设置值，如温度值=-1，意思是没有设置有温度
    public static final int SETTING_MODE_FIRE_GEAR = 0;
    public static final int SETTING_MODE_TEMP_TYPE_CHOOSEN = 1;
    public static final int SETTING_MODE_FAST_TEMP_GEAR = 2;
    public static final int SETTING_MODE_PRECISE_TEMP_GEAR = 3;
    public static final int SETTING_MODE_TIMER = 4;
    public static final int SETTING_MODE_FAST_TEMP_GEAR_WITHOUT_SENSOR = 5;

    public static final int TEMP_RECIPES_SHOW_ORDER_TEMP_FIRST = 0;
    public static final int TEMP_RECIPES_SHOW_ORDER_RECIPES_FIRST = 1;

    private int cookerSettingMode = SETTING_MODE_FIRE_GEAR;
    private int cookerID = COOKER_SETTING_INVALID_VALUE;//炉头ID
    private int tempMode = COOKER_SETTING_INVALID_VALUE;//温控模式
    private int tempValue = COOKER_SETTING_INVALID_VALUE;//温控档位值
    private int tempIdentifyDrawableResourceID = COOKER_SETTING_INVALID_VALUE;//温度值指示图标
    private int tempRecipesResID = COOKER_SETTING_INVALID_VALUE;
    private int tempShowOrder = COOKER_SETTING_INVALID_VALUE;
    private int fireValue = COOKER_SETTING_INVALID_VALUE;//档位值
    private int timerHourValue = COOKER_SETTING_INVALID_VALUE;//定时器小时
    private int timerMinuteValue = COOKER_SETTING_INVALID_VALUE;//定时器分钟
    private int timerSecondValue = COOKER_SETTING_INVALID_VALUE;//定时器秒

    private int preSettingtimerHourValue = COOKER_SETTING_INVALID_VALUE;//定时器小时
    private int preSettingtimerMinuteValue = COOKER_SETTING_INVALID_VALUE;//定时器分钟
    private int preSettingtimerSecondValue = COOKER_SETTING_INVALID_VALUE;//定时器秒

    private int timerRemainHourValue=COOKER_SETTING_INVALID_VALUE; // 小时剩余值
    private int timerRemainMinuteValue=COOKER_SETTING_INVALID_VALUE;// 分钟剩余值

    public int getTimerRemainHourValue() {
        return timerRemainHourValue;
    }

    public void setTimerRemainHourValue(int timerRemainHourValue) {
        this.timerRemainHourValue = timerRemainHourValue;
    }

    public int getTimerRemainMinuteValue() {
        return timerRemainMinuteValue;
    }

    public void setTimerRemainMinuteValue(int timerRemainMinuteValue) {
        this.timerRemainMinuteValue = timerRemainMinuteValue;
    }

    private boolean canUseTempSensor = true;

    public CookerSettingData() {
        this.tempRecipesResID = -1;
    }

    public CookerSettingData(int cookerID) {
        this.cookerID = cookerID;
        this.tempRecipesResID = -1;
    }

    public CookerSettingData(int cookerSettingMode, int cookerID, int tempMode, int tempValue, int tempIdentifyDrawableResourceID, int fireValue, int timerHourValue, int timerMinuteValue, int timerSecondValue ,boolean canUseTempSensor) {
        this.cookerSettingMode = cookerSettingMode;
        this.cookerID = cookerID;
        this.tempMode = tempMode;
        this.tempValue = tempValue;
        this.tempIdentifyDrawableResourceID = tempIdentifyDrawableResourceID;
        this.fireValue = fireValue;
        this.timerHourValue = timerHourValue;
        this.timerMinuteValue = timerMinuteValue;
        this.timerSecondValue = timerSecondValue;
        this.canUseTempSensor = canUseTempSensor;
        this.tempRecipesResID = -1;
    }

    public static int getMaxTimerHours(int tempMode, int fireValue, int tempValue) {
        switch (tempMode) {
            case CookerSettingData.SETTING_MODE_PRECISE_TEMP_GEAR:
            case CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR:
            case CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR_WITHOUT_SENSOR:
                if (tempValue > 100) {
                    return 1;
                } else if (tempValue > 90) {
                    return 6;
                } else if (tempValue > 85) {
                    return 10;
                } else if (tempValue > 65) {
                    return 36;
                } else if (tempValue >= 20) {
                    return 48;
                } else {
                    return 48;
                }
            case CookerSettingData.SETTING_MODE_TEMP_TYPE_CHOOSEN:
            case CookerSettingData.COOKER_SETTING_INVALID_VALUE:
                if (fireValue > 8) {
                    return 1;
                } else if (fireValue > 6) {
                    return 2;
                } else if (fireValue > 3) {
                    return 3;
                } else {
                    return 6;
                }
            default:
                return 9;
        }
    }

    public static int getMaxTimerMinutes(int tempMode, int fireValue, int tempValue) {
        int hours = getMaxTimerHours(tempMode, fireValue, tempValue);
        int minutes = hours * 60;
        if (minutes == 60 && fireValue > 8) {
            minutes += 30;
        }
        return minutes;
    }

    public boolean isCanUseTempSensor() {
        return canUseTempSensor;
    }

    public void setCanUseTempSensor(boolean canUseTempSensor) {
        this.canUseTempSensor = canUseTempSensor;
    }

    public int getCookerSettingMode() {
        return cookerSettingMode;
    }

    public void setCookerSettingMode(int cookerSettingMode) {
        this.cookerSettingMode = cookerSettingMode;
    }

    public int getCookerID() {
        return cookerID;
    }

    public void setCookerID(int cookerID) {
        this.cookerID = cookerID;
    }

    public int getTempMode() {
        return tempMode;
    }

    public void setTempMode(int tempMode) {
        this.tempMode = tempMode;
    }

    public int getTempValue() {
        return tempValue;
    }

    public void setTempValue(int tempValue) {
        this.tempValue = tempValue;
    }

    public int getTempIdentifyDrawableResourceID() {
        return tempIdentifyDrawableResourceID;
    }

    public void setTempIdentifyDrawableResourceID(int tempIdentifyDrawableResourceID) {
        this.tempIdentifyDrawableResourceID = tempIdentifyDrawableResourceID;
    }


    public int getTempRecipesResID() {
        return tempRecipesResID;
    }

    public void setTempRecipesResID(int tempRecipesResID) {
        this.tempRecipesResID = tempRecipesResID;
    }

    public int getTempShowOrder() {
        return tempShowOrder;
    }

    public void setTempShowOrder(int tempShowOrder) {
        this.tempShowOrder = tempShowOrder;
    }

    public int getFireValue() {
        return fireValue;
    }

    public void setFireValue(int fireValue) {
        this.fireValue = fireValue;
    }

    public int getTimerHourValue() {
        return timerHourValue;
    }

    public void setTimerHourValue(int timerHourValue) {
        this.timerHourValue = timerHourValue;
    }

    public int getTimerMinuteValue() {
        return timerMinuteValue;
    }

    public void setTimerMinuteValue(int timerMinuteValue) {
        this.timerMinuteValue = timerMinuteValue;
    }

    public int getTimerSecondValue() {
        return timerSecondValue;
    }


    public void setTimerSecondValue(int timerSecondValue) {
        this.timerSecondValue = timerSecondValue;
    }

    public int getPreSettingtimerHourValue() {
        return preSettingtimerHourValue;
    }

    public void setPreSettingtimerHourValue(int preSettingtimerHourValue) {
        this.preSettingtimerHourValue = preSettingtimerHourValue;
    }

    public int getPreSettingtimerMinuteValue() {
        return preSettingtimerMinuteValue;
    }

    public void setPreSettingtimerMinuteValue(int preSettingtimerMinuteValue) {
        this.preSettingtimerMinuteValue = preSettingtimerMinuteValue;
    }

    public int getPreSettingtimerSecondValue() {
        return preSettingtimerSecondValue;
    }

    public void setPreSettingtimerSecondValue(int preSettingtimerSecondValue) {
        this.preSettingtimerSecondValue = preSettingtimerSecondValue;
    }

    public void setTimer(int hour, int minute) {
        this.timerHourValue = hour;
        this.timerMinuteValue = minute;
        this.timerSecondValue = 59;
    }

    public void setTimer(int hour,int minute ,int second) {
        this.timerHourValue = hour;
        this.timerMinuteValue = minute;
        //this.timerSecondValue = 0;
        this.timerSecondValue = second;
    }

    public void resetSettingGearMode() {
        if (fireValue != COOKER_SETTING_INVALID_VALUE) {
            cookerSettingMode = SETTING_MODE_FIRE_GEAR;
        }else {
            cookerSettingMode = tempMode;
        }
    }

    public void resetSettingData() {
        cookerSettingMode = SETTING_MODE_FIRE_GEAR;
        tempMode = COOKER_SETTING_INVALID_VALUE;//温控模式
        tempValue = COOKER_SETTING_INVALID_VALUE;//温控档位值
        tempIdentifyDrawableResourceID = COOKER_SETTING_INVALID_VALUE;//温度值指示图标
        fireValue = COOKER_SETTING_INVALID_VALUE;//档位值
        timerHourValue = COOKER_SETTING_INVALID_VALUE;//定时器小时
        timerMinuteValue = COOKER_SETTING_INVALID_VALUE;//定时器分钟
        timerSecondValue = COOKER_SETTING_INVALID_VALUE;//定时器秒
    }

    public void resetTimerData() {
        timerHourValue = COOKER_SETTING_INVALID_VALUE;//定时器小时
        timerMinuteValue = COOKER_SETTING_INVALID_VALUE;//定时器分钟
        timerSecondValue = COOKER_SETTING_INVALID_VALUE;//定时器秒
    }

    public static String getCookerSettingModeStr(int mode) {
        switch (mode) {
            case COOKER_SETTING_INVALID_VALUE:
                return "COOKER_SETTING_INVALID_VALUE";
            case SETTING_MODE_FIRE_GEAR:
                return "SETTING_MODE_FIRE_GEAR";
            case SETTING_MODE_TEMP_TYPE_CHOOSEN:
                return "SETTING_MODE_TEMP_TYPE_CHOOSEN";
            case SETTING_MODE_FAST_TEMP_GEAR:
                return "SETTING_MODE_FAST_TEMP_GEAR";
            case SETTING_MODE_PRECISE_TEMP_GEAR:
                return "SETTING_MODE_PRECISE_TEMP_GEAR";
            case SETTING_MODE_TIMER:
                return "SETTING_MODE_TIMER";
            case SETTING_MODE_FAST_TEMP_GEAR_WITHOUT_SENSOR:
                return "SETTING_MODE_FAST_TEMP_GEAR_WITHOUT_SENSOR";
        }
        return "Unknown: " + mode;
    }

    public String toStringShort() {
        StringBuffer sb = new StringBuffer();
        sb.append("settingMode=");
        sb.append(getCookerSettingModeStr(cookerSettingMode));
        sb.append(",");

        sb.append("cooker=");
        sb.append(cookerID);
        sb.append(",");

        sb.append("tempMode=");
        sb.append(getCookerSettingModeStr(tempMode));
        sb.append(",");

        sb.append("tempValue=");
        sb.append(tempValue);
        sb.append(",");

        sb.append("fireValue=");
        sb.append(fireValue);
        sb.append(",");

        sb.append(String.format("%02d", timerHourValue));
        sb.append(":");
        sb.append(String.format("%02d", timerMinuteValue));
        sb.append(":");
        sb.append(String.format("%02d", timerSecondValue));

        return sb.toString();
    }

    @Override
    public String toString() {
        String str = "";
        str = str + "\r\n" +
                "cookerSettingMode------>" + cookerSettingMode + "\r\n" +
                "cookerID------>" + cookerID + "\r\n" +
                "tempMode------>" + tempMode + "\r\n" +
                "tempValue------>" + tempValue + "\r\n" +
                "tempIdentifyDrawableResourceID------>" + tempIdentifyDrawableResourceID + "\r\n" +
                "fireValue------>" + fireValue + "\r\n" +
                "timerHourValue------>" + timerHourValue + "\r\n" +
                "timerMinuteValue------>" + timerMinuteValue + "\r\n" +
                "timerSecondValue------>" + timerSecondValue + "\r\n";

        return str;
    }

    @Override
    public Object clone() {
        CookerSettingData obj = new CookerSettingData();
        obj.cookerSettingMode = cookerSettingMode;
        obj.cookerID = cookerID;
        obj.tempMode = tempMode;
        obj.tempValue = tempValue;
        obj.tempIdentifyDrawableResourceID = tempIdentifyDrawableResourceID;
        obj.tempRecipesResID = tempRecipesResID;
        obj.tempShowOrder = tempShowOrder;
        obj.fireValue = fireValue;
        obj.timerHourValue = timerHourValue;
        obj.timerMinuteValue = timerMinuteValue;
        obj.timerSecondValue = timerSecondValue;
        obj.canUseTempSensor = canUseTempSensor;
        return obj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CookerSettingData that = (CookerSettingData) o;
        return cookerSettingMode == that.cookerSettingMode &&
                cookerID == that.cookerID &&
                tempMode == that.tempMode &&
                tempValue == that.tempValue &&
                tempIdentifyDrawableResourceID == that.tempIdentifyDrawableResourceID &&
                tempRecipesResID == that.tempRecipesResID &&
                tempShowOrder == that.tempShowOrder &&
                fireValue == that.fireValue &&
                timerHourValue == that.timerHourValue &&
                timerMinuteValue == that.timerMinuteValue &&
                timerSecondValue == that.timerSecondValue &&
                canUseTempSensor == that.canUseTempSensor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                cookerSettingMode,
                cookerID,
                tempMode,
                tempValue,
                tempIdentifyDrawableResourceID,
                tempRecipesResID,
                tempShowOrder,
                fireValue,
                timerHourValue,
                timerMinuteValue,
                timerSecondValue,
                canUseTempSensor);
    }
}
