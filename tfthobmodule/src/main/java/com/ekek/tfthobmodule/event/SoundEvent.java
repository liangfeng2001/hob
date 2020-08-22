package com.ekek.tfthobmodule.event;

import com.ekek.tfthobmodule.constants.TFTHobConstant;

public class SoundEvent {

    public static final int SOUND_ACTION_PLAY = 0;
    public static final int SOUND_ACTION_PAUSE = 1;
    public static final int SOUND_ACTION_PAUSE_ALL = 2;

    public static final int SOUND_TYPE_UNKNOWN = -1;
    public static final int SOUND_TYPE_SCROLL = 0;
    public static final int SOUND_TYPE_LOCK = 1;
    public static final int SOUND_TYPE_ALARM_NTC = 2;
    public static final int SOUND_TYPE_COMMON_ALARM = 3;
    public static final int SOUND_TYPE_ALARM_ONCE = 4;
    public static final int SOUND_TYPE_ALARM_A_COOKER = 101;
    public static final int SOUND_TYPE_ALARM_B_COOKER = 102;
    public static final int SOUND_TYPE_ALARM_C_COOKER = 103;
    public static final int SOUND_TYPE_ALARM_D_COOKER = 104;
    public static final int SOUND_TYPE_ALARM_E_COOKER = 105;
    public static final int SOUND_TYPE_ALARM_F_COOKER = 106;
    public static final int SOUND_TYPE_ALARM_AB_COOKER = 107;
    public static final int SOUND_TYPE_ALARM_EF_COOKER = 108;
    public static final int SOUND_TYPE_TIMER_ALARM_A_COOKER = 201;
    public static final int SOUND_TYPE_TIMER_ALARM_B_COOKER = 202;
    public static final int SOUND_TYPE_TIMER_ALARM_C_COOKER = 203;
    public static final int SOUND_TYPE_TIMER_ALARM_D_COOKER = 204;
    public static final int SOUND_TYPE_TIMER_ALARM_E_COOKER = 205;
    public static final int SOUND_TYPE_TIMER_ALARM_F_COOKER = 206;
    public static final int SOUND_TYPE_TIMER_ALARM_AB_COOKER = 207;
    public static final int SOUND_TYPE_TIMER_ALARM_EF_COOKER = 208;

    /**
     * 要执行的动作
     *   SOUND_ACTION_PLAY
     *   SOUND_ACTION_PAUSE
     *   SOUND_ACTION_PAUSE_ALL
     */
    private int action;

    /**
     * 音频文件资源ID
     */
    private int soundID;

    /**
     * 用于细分不同种类的声音，如果是针对某个炉头的，里面会包含该炉头的 cookerId
     */
    private int soundType;

    public SoundEvent(int action, int soundID, int soundType) {
        this.action = action;
        this.soundID = soundID;
        this.soundType = soundType;
    }

    public static int getSoundType(int cookerID, boolean isTimerAlarm) {
        switch (cookerID) {
            case TFTHobConstant.COOKER_TYPE_A_COOKER:
                return isTimerAlarm ? SoundEvent.SOUND_TYPE_TIMER_ALARM_A_COOKER : SoundEvent.SOUND_TYPE_ALARM_A_COOKER;
            case TFTHobConstant.COOKER_TYPE_B_COOKER:
                return isTimerAlarm ? SoundEvent.SOUND_TYPE_TIMER_ALARM_B_COOKER : SoundEvent.SOUND_TYPE_ALARM_B_COOKER;
            case TFTHobConstant.COOKER_TYPE_AB_COOKER:
                return isTimerAlarm ? SoundEvent.SOUND_TYPE_TIMER_ALARM_AB_COOKER : SoundEvent.SOUND_TYPE_ALARM_AB_COOKER;
            case TFTHobConstant.COOKER_TYPE_C_COOKER:
                return isTimerAlarm ? SoundEvent.SOUND_TYPE_TIMER_ALARM_C_COOKER : SoundEvent.SOUND_TYPE_ALARM_C_COOKER;
            case TFTHobConstant.COOKER_TYPE_D_COOKER:
                return isTimerAlarm ? SoundEvent.SOUND_TYPE_TIMER_ALARM_D_COOKER : SoundEvent.SOUND_TYPE_ALARM_D_COOKER;
            case TFTHobConstant.COOKER_TYPE_E_COOKER:
                return isTimerAlarm ? SoundEvent.SOUND_TYPE_TIMER_ALARM_E_COOKER : SoundEvent.SOUND_TYPE_ALARM_E_COOKER;
            case TFTHobConstant.COOKER_TYPE_F_COOKER:
                return isTimerAlarm ? SoundEvent.SOUND_TYPE_TIMER_ALARM_F_COOKER : SoundEvent.SOUND_TYPE_ALARM_F_COOKER;
            case TFTHobConstant.COOKER_TYPE_EF_COOKER:
                return isTimerAlarm ? SoundEvent.SOUND_TYPE_TIMER_ALARM_EF_COOKER : SoundEvent.SOUND_TYPE_ALARM_EF_COOKER;
        }
        return SoundEvent.SOUND_TYPE_UNKNOWN;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getSoundID() {
        return soundID;
    }

    public void setSoundID(int soundID) {
        this.soundID = soundID;
    }

    public int getSoundType() {
        return soundType;
    }

    public void setSoundType(int soundType) {
        this.soundType = soundType;
    }
}
