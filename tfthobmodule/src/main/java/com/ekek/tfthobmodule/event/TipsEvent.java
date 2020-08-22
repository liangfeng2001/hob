package com.ekek.tfthobmodule.event;

import com.ekek.tfthobmodule.constants.TFTHobConstant;

public class TipsEvent {
    public static final int TIPS_DURATION_3_SECOND = 3 * 1000;
    public static final int TIPS_DURATION_4_SECOND = 2 * 1000;
    public static final int TIPS_DURATION_NONE = -1;
    private String msg = "";
    private long duration = TIPS_DURATION_NONE;
    private int priority = TFTHobConstant.TOAST_PRIORITY_LOW;

    public TipsEvent(String msg, long duration, int priority) {
        this.msg = msg;
        this.duration = duration;
        this.priority = priority;
    }

    public TipsEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
