package com.ekek.tfthobmodule.event;

public class AgeingEvent {
    public static final int AGEING_ACTION_START = 0;
    public static final int AGEING_ACTION_END = 1;
    public static final int AGEING_ACTION_RESTART = 2;
    public static final int AGEING_ACTION_PAUSE = 3;
    public static final int AGEING_ACTION_PLAY = 4;
    private int action;

    public AgeingEvent(int action) {
        this.action = action;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
