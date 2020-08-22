package com.ekek.tfthobmodule.event;

public class CookerPowerOffEvent {
    int cookerID;

    public CookerPowerOffEvent(int cookerID) {
        this.cookerID = cookerID;
    }

    public int getCookerID() {
        return cookerID;
    }

    public void setCookerID(int cookerID) {
        this.cookerID = cookerID;
    }
}
