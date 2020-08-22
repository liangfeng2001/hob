package com.ekek.tfthobmodule.event;

public class ConnectivitySettingEvent {

    private int mode;

    public ConnectivitySettingEvent(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
