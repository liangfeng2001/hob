package com.ekek.settingmodule.events;

public class DefaultSettingEvent {
    private int order=0;

    public int getOrder() {
        return order;
    }

    public DefaultSettingEvent(int order) {
        this.order = order;
    }

    public void setOrder(int order) {
        this.order = order;

    }
}
