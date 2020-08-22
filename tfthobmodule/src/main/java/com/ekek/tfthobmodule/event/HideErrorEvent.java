package com.ekek.tfthobmodule.event;

public class HideErrorEvent {

    private int error;

    public HideErrorEvent(int error) {
        this.error = error;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }
}
