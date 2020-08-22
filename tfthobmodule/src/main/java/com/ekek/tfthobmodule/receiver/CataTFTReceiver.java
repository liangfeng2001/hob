package com.ekek.tfthobmodule.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ekek.commonmodule.utils.LogUtil;

public class CataTFTReceiver  extends BroadcastReceiver {
     String SCREEN_ON = "android.intent.action.SCREEN_ON";
     String SCREEN_OFF = "android.intent.action.SCREEN_OFF";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (SCREEN_ON.equals(intent.getAction())) {  // 屏幕开
          //  requestWakeLock();
          //  LogUtil.d("liang 屏幕开");
        }
        else if (SCREEN_OFF.equals(intent.getAction())) {  // 屏幕关
           // releaseWakeLock();
            //LogUtil.d("liang 屏幕关");
        }
    }
}
