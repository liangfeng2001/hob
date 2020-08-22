package com.ekek.tfthobmodule.event;

public class NoExternalSensorDetectedEvent {

    public static final int SOURCE_HOB_COOKER_SETTING_FRAGMENT = 0;
    public static final int SOURCE_HOB_TEMPERATURE_MODE_SELECT_FRAGMENT = 1;
    public static final int SOURCE_HOB_TEMPERATURE_SENSOR_DISCONNECT = 2;//蓝牙温度计异常断开

    private int source;

    public NoExternalSensorDetectedEvent(int source) {
        this.source = source;
    }

    public int getSource() {
        return source;
    }
}
