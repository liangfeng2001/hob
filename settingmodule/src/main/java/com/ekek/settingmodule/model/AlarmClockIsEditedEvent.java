package com.ekek.settingmodule.model;

public class AlarmClockIsEditedEvent {

    boolean order=false;

    public AlarmClockIsEditedEvent(boolean order) {
        this.order = order;
    }

    public boolean isOrder() {
        return order;
    }

    public void setOrder(boolean order) {
        this.order = order;
    }
}
