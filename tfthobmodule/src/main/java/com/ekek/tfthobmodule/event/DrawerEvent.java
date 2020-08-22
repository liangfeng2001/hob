package com.ekek.tfthobmodule.event;

public class DrawerEvent {

    // Constants
    public static final int EVENT_DRAWER_SLIDE = 1;
    public static final int EVENT_DRAWER_OPENED = 2;
    public static final int EVENT_DRAWER_CLOSED = 3;
    public static final int EVENT_DRAWER_STATE_CHANGED = 4;

    // Fields
    private int event;
    private int newState;
    private float slideOffset;

    // Constructors
    public DrawerEvent(int event) {
        this.event = event;
    }
    public DrawerEvent(int event, int newState) {
        this.event = event;
        this.newState = newState;
    }
    public DrawerEvent(int event, float slideOffset) {
        this.event = event;
        this.slideOffset = slideOffset;
    }

    // Properties
    public int getEvent() {
        return event;
    }
    public void setEvent(int event) {
        this.event = event;
    }
    public int getNewState() {
        return newState;
    }
    public void setNewState(int newState) {
        this.newState = newState;
    }
    public float getSlideOffset() {
        return slideOffset;
    }
    public void setSlideOffset(float slideOffset) {
        this.slideOffset = slideOffset;
    }
}
