package com.ekek.settingmodule.utils;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import com.ekek.commonmodule.utils.LogUtil;

/**
 * Created by Samhung on 2018/2/2.
 */

public class SoundUtil {

    private static final String ACTION_SET_SOUND_EFFECT = "ACTION_EKEK_SET_SOUND_EFFECT";

    public static int getSystemMaxVolume(Context context) {
        return getMaxVolume(context, AudioManager.STREAM_MUSIC);
    }

    public static int getAlarmMaxVolume(Context context) {
        return getMaxVolume(context, AudioManager.STREAM_ALARM);
    }

    public static int getMaxVolume(Context context, int streamType) {
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = 0;
        if (audioManager != null) {
            maxVolume = audioManager.getStreamMaxVolume(streamType);
        }
        return maxVolume;
    }

    public static int getSystemCurrentVolume(Context context) {
        return getCurrentVolume(context, AudioManager.STREAM_MUSIC);
    }

    public static int getAlarmCurrentVolume(Context context) {
        return getCurrentVolume(context, AudioManager.STREAM_ALARM);
    }

    public static int getCurrentVolume(Context context, int streamType) {
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        int volume = 0;
        if (audioManager != null) {
            volume = audioManager.getStreamVolume(streamType);
        }
        return volume;
    }

    public static void setSystemVolume(Context context , int volume) {
        setVolume(context, volume, AudioManager.STREAM_MUSIC);
    }

    public static void setAlarmVolume(Context context , int volume) {
        setVolume(context, volume, AudioManager.STREAM_ALARM);
    }

    public static void setVolume(Context context , int volume, int streamType) {
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            audioManager.setStreamVolume(streamType, volume, 0);
        }
    }

    public static void setSystemMute(Context context) {
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        }
    }

    public static void setSoundEffect(Context context, boolean enable) {
        Intent intent = new Intent();
        intent.setAction(ACTION_SET_SOUND_EFFECT);
        intent.putExtra("enable",enable);
        context.sendBroadcast(intent);
    }
}
