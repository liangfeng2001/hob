package com.ekek.tfthobmodule.event;

public class IdleEvent {

    public static final int IDLE_HIBERNATION = 1;
    public static final int IDLE_TOTAL_POWER_OFF = 2;
    public static final int IDLE_TOTAL_POWER_OFF_COOLED_DOWN = 3;


    /**
     * 开机后，在语言、日期、时间等初始化设置页面，如果用户 5分钟内都不触摸屏幕，就会发出此事件
     * 以便可以回到时钟页面，并等待一段时间后，发送 0xE2 到下位机，使其关闭并进入低功耗模式
     */
    public static final int IDLE_TIMEOUT_DURING_INITIALIZING_PROCESS = 4;

    /**
     * 当屏幕高温发生一分钟后（即高温报警声音和动画已经播放一分钟后），进入ending 动画，然后黑屏等待进入低功耗
     *
     */
    public static final int IDLE_SWITCH_OFF_TFT_COMPLETE_FOR_PANNEL_HIGH_TEMP = 5;

    private int idleType;

    public IdleEvent(int idleType) {
        this.idleType = idleType;
    }

    public int getIdleType() {
        return idleType;
    }
}
