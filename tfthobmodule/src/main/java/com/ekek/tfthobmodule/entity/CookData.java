package com.ekek.tfthobmodule.entity;

public class CookData {

    private int tempStr;
    private int minute;

    public CookData(int tempStr, int minute) {
        this.tempStr = tempStr;
        this.minute = minute;
    }

    public int getTempStr() {
        return tempStr;
    }

    public void setTempStr(int tempStr) {
        this.tempStr = tempStr;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
