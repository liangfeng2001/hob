package com.ekek.tfthobmodule.event;

public class NTCEvent {
    private String message = "";

    public NTCEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
