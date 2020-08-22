package com.ekek.tfthobmodule.event;

public class TempSensorRequestEvent {

    public static final int ACTION_FIND_AND_CONNECT_TEMP_SENSOR = 0;
    public static final int ACTION_STOP_TEMP_SENSOR = 1;
    public static final int ACTION_TEMP_SENSOR_READY_TO_WORK = 2;
    public static final int ACTION_TEMP_SENSOR_CONNECT_FAIL = 3;
    private int cookerID;
    private int action;

    public TempSensorRequestEvent(int cookerID , int action) {
        this.cookerID = cookerID;
        this.action = action;
    }

    public int getCookerID() {
        return cookerID;
    }

    public void setCookerID(int cookerID) {
        this.cookerID = cookerID;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
