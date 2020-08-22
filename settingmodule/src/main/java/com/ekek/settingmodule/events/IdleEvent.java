package com.ekek.settingmodule.events;

public class IdleEvent {

    public static final int FRAGMENT_LANGUAGE = 0;
    public static final int FRAGMENT_DATE = 1;
    public static final int FRAGMENT_TIME = 2;

    private int fragment;

    public IdleEvent(int fragment) {
        this.fragment = fragment;
    }

    public int getFragment() {
        return fragment;
    }

    public void setFragment(int fragment) {
        this.fragment = fragment;
    }
}
