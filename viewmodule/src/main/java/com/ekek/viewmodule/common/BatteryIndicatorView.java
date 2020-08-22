package com.ekek.viewmodule.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ekek.viewmodule.R;

public class BatteryIndicatorView extends FrameLayout {
    public final static int BATTERY_CHARGE_STATUS_CHARGING = 0;
    public final static int BATTERY_CHARGE_STATUS_NO_CHARGE = 1;
    private ImageView ivBattery;

    public BatteryIndicatorView(@NonNull Context context) {
        super(context);
    }

    public BatteryIndicatorView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.viewmodule_layout_battery_indicator, this);
        ivBattery = findViewById(R.id.iv_battery);
        initResource();
    }

    public BatteryIndicatorView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }


    private void initResource() {
        ivBattery.setBackgroundResource(R.mipmap.ic_battery_100);
    }


    public void updateView(int chargeStatus ,int batteryLevel) {
        if (chargeStatus == BATTERY_CHARGE_STATUS_CHARGING) {
            ivBattery.setBackgroundResource(R.mipmap.ic_battery_charging);

        }else if (chargeStatus == BATTERY_CHARGE_STATUS_NO_CHARGE) {
            if (batteryLevel <= 20) {//20%
                ivBattery.setBackgroundResource(R.mipmap.ic_battery_20);
            }else if(batteryLevel <= 40) {//40%
                ivBattery.setBackgroundResource(R.mipmap.ic_battery_40);
            }else if (batteryLevel <=60) {
                ivBattery.setBackgroundResource(R.mipmap.ic_battery_60);
            }else if (batteryLevel <= 80) {
                ivBattery.setBackgroundResource(R.mipmap.ic_battery_80);
            }else {
                ivBattery.setBackgroundResource(R.mipmap.ic_battery_100);
            }

        }
    }
}
