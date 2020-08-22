package com.ekek.settingmodule.model;

public class PlaySoundWhenSetTime {
    int soundType;

    public PlaySoundWhenSetTime(int soundType) {
        this.soundType = soundType;
    }

    public int getSoundType() {
        return soundType;
    }

    public void setSoundType(int soundType) {
        this.soundType = soundType;
    }
}
