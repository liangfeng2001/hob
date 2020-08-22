package com.ekek.hardwaremodule.event;

public class PowerSwitchStateChangedEvent {
    private int oldState;
    private int newState;

    public PowerSwitchStateChangedEvent(int oldState, int newState) {
        this.oldState = oldState;
        this.newState = newState;
    }

    public int getOldState() {
        return oldState;
    }

    public void setOldState(int oldState) {
        this.oldState = oldState;
    }

    public int getNewState() {
        return newState;
    }

    public void setNewState(int newState) {
        this.newState = newState;
    }
}
