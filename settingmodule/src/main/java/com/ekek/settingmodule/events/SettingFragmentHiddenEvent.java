package com.ekek.settingmodule.events;

public class SettingFragmentHiddenEvent {

    public static final int FRAGMENT_LANGUAGE_SETTING = 0;
    public static final int FRAGMENT_DATE_SETTING = 1;
    public static final int FRAGMENT_TIME_SETTING = 2;

    private int fragment;

    public SettingFragmentHiddenEvent(int fragment) {
        this.fragment = fragment;
    }

    public int getFragment() {
        return fragment;
    }

    public void setFragment(int fragment) {
        this.fragment = fragment;
    }
}
