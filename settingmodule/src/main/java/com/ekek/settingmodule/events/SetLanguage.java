package com.ekek.settingmodule.events;

public class SetLanguage {
    int order;

    public SetLanguage(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
