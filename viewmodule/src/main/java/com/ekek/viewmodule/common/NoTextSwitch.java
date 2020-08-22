package com.ekek.viewmodule.common;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Switch;

public class NoTextSwitch extends Switch {
    public NoTextSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTextOff("");
        setTextOn("");
    }

}
