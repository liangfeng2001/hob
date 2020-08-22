package com.ekek.tfthobmodule.event;

import com.ekek.commonmodule.utils.LogUtil;

public class BleTempEvent {
    private int cookerID;
    private int tempValue;
    private int chargeStatus;
    private int batteryLevel;


    public BleTempEvent(int tempValue) {
        this.tempValue = tempValue;

    }

    public BleTempEvent(int tempValue,int id) {
        LogUtil.d("Enter::BleTempEvent----> " + id);
        this.tempValue = tempValue;
        this.cookerID = id;

    }

    public BleTempEvent(int tempValue,int chargeStatus,int batteryLevel) {
        this.tempValue = tempValue;
        this.chargeStatus = chargeStatus;
        this.batteryLevel = batteryLevel;
    }

    public int getTempValue() {
        return tempValue;
    }

    public void setTempValue(int tempValue) {
        this.tempValue = tempValue;
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

    public int getCookerID() {
        return cookerID;
    }

    public void setCookerID(int cookerID) {
        this.cookerID = cookerID;
    }

}
