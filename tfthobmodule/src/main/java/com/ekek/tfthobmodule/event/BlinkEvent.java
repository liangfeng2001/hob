package com.ekek.tfthobmodule.event;

public class BlinkEvent {

    // Constants
    public static final int BLINK_TYPE_TOGGLE = 0;
    public static final int BLINK_TYPE_VISIBLE = 1;
    public static final int BLINK_TYPE_INVISIBLE = 2;

    // Fields
    private int viewId;
    private int blinkType;

    // Constructors
    public BlinkEvent(int viewId, int blinkType) {
        this.viewId = viewId;
        this.blinkType = blinkType;
    }

    // Properties
    public int getViewId() {
        return viewId;
    }
    public void setViewId(int viewId) {
        this.viewId = viewId;
    }
    public int getBlinkType() {
        return blinkType;
    }
    public void setBlinkType(int blinkType) {
        this.blinkType = blinkType;
    }
}
