package com.ekek.settingmodule.events;

public class SettingsChangedEvent {

    // Constants
    public static final int SETTING_HIBERNATION_MODE = 1;
    public static final int SETTING_ACTIVATION_TIME = 2;
    public static final int SETTING_TOTAL_TURN_OFF_MODE = 3;
    public static final int SETTING_TOTAL_TURN_OFF_TIME = 4;
    public static final int SETTING_LANGUAGE = 5;
    public static final int SETTING_DEMO_SWITCH_STATUS = 6;

    // Fields
    private int settingChanged;

    // Constructors
    public SettingsChangedEvent(int settingChanged) {
        this.settingChanged = settingChanged;
    }

    // Properties
    public int getSettingChanged() {
        return settingChanged;
    }
}
