package com.ekek.settingmodule.events;

public class ConnectivitySettingEvent {
    //Bluetooth options
    /**
     * 关闭蓝牙
     * */
    public static final int BLUETOOTH_OPTIONS_DISABLE= 0;
    /**
     * 打开蓝牙温度计连接模式
     * */
    public static final int BLUETOOTH_OPTIONS_TEMPERTURE_SENSOR = 1;
    /**
     * 打开连接手机模式
     * */
    public static final int BLUETOOTH_OPTIONS_SMART_PHONE = 2;
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
