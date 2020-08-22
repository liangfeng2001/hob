package com.ekek.tfthobmodule.event;

public class PlaySoundEvent {

    public static final int ORDER_TIME_OUT = 0;

    private int order;

    public PlaySoundEvent() {
        this.order = ORDER_TIME_OUT;
    }
    public PlaySoundEvent(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
