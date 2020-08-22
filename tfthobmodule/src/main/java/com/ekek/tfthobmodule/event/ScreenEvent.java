package com.ekek.tfthobmodule.event;

public class ScreenEvent {
    public static final int ACTION_SCREEN_OFF = 0;
    public static final int ACTION_SCREEN_ON = 1;
    int action;

    public ScreenEvent(int action) {
        this.action = action;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
