package com.ekek.tfthobmodule.event;

public class BleBatteryEvent {
    public static final int BATTERY_VIEW_STATUS_SHOW = 0;
    public static final int BATTERY_VIEW_STATUS_HIDE = 1;
    public static final int BATTERY_VIEW_STATUS_UPDATE_LEVEL = 2;
    public static final int BATTERY_VIEW_STATUS_UPDATE_CHARGE_STATE = 3;

    public static final int BATTERY_LEVEL_ALERT = 20;
    public static final int BATTERY_LEVEL_EXHAUSTED = 5;

    public static final int BLUETOOTH_TEMP_SENSOR_STOP_TYPE_PASSIVE = 0;//蓝牙温度计被动断开
    public static final int BLUETOOTH_TEMP_SENSOR_STOP_TYPE_INITIATIVE = 1;//蓝牙温度计主动断开
    private int chargeStatus;//0:未在传输，1：正在传输
    private int chargeState; //3:正在充电，0：不充电
    private int batteryLevel;
    private int batteryViewStatus = BATTERY_VIEW_STATUS_HIDE;//是否显示电池
    private int stopType = -1;

    public BleBatteryEvent(int batteryViewStatus,int chargeState) {
        this.batteryViewStatus = batteryViewStatus;
        this.chargeState = chargeState;
        this.stopType = -1;
    }

    public BleBatteryEvent(int batteryViewStatus,int chargeStatus,int batteryLevel) {
        this.batteryViewStatus = batteryViewStatus;
        this.chargeStatus = chargeStatus;
        this.batteryLevel = batteryLevel;
        this.stopType = -1;
    }

    public BleBatteryEvent(int batteryViewStatus,int chargeStatus,int batteryLevel , int stopType) {
        this.batteryViewStatus = batteryViewStatus;
        this.chargeStatus = chargeStatus;
        this.batteryLevel = batteryLevel;
        this.stopType = stopType;
    }

    public int getMaxUsableMinutes() {
        if (batteryLevel >= 95) {
            return 1760;
        } else if (batteryLevel >= 90) {
            return 1758;
        } else if (batteryLevel >= 85) {
            return 1756;
        } else if (batteryLevel >= 80) {
            return 1746;
        } else if (batteryLevel >= 75) {
            return 1727;
        } else if (batteryLevel >= 70) {
            return 1703;
        } else if (batteryLevel >= 65) {
            return 1671;
        } else if (batteryLevel >= 60) {
            return 1626;
        } else if (batteryLevel >= 55) {
            return 1558;
        } else if (batteryLevel >= 50) {
            return 1469;
        } else if (batteryLevel >= 45) {
            return 1356;
        } else if (batteryLevel >= 44) {
            return 1304;
        } else if (batteryLevel >= 43) {
            return 1201;
        } else if (batteryLevel >= 42) {
            return 1105;
        } else if (batteryLevel >= 41) {
            return 996;
        } else if (batteryLevel >= 40) {
            return 912;
        } else if (batteryLevel >= 35) {
            return 619;
        } else if (batteryLevel >= 30) {
            return 595;
        } else if (batteryLevel >= 25) {
            return 415;
        } else if (batteryLevel >= 20) {
            return 217;
        }
        return 0;
    }

    public int getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(int chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public int getBatteryViewStatus() {
        return batteryViewStatus;
    }

    public void setBatteryViewStatus(int batteryViewStatus) {
        this.batteryViewStatus = batteryViewStatus;
    }

    public int getStopType() {
        return stopType;
    }

    public void setStopType(int stopType) {
        this.stopType = stopType;
    }

    public int getChargeState() {
        return chargeState;
    }

    public void setChargeState(int chargeState) {
        this.chargeState = chargeState;
    }
}
