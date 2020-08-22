package com.ekek.tfthobmodule.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.ekek.tfthobmodule.R;

public class SoundUtil {
    public static final int SOUND_ID_ALARM = 1;
    public static final int SOUND_ID_LOCK = 2;
    public static final int SOUND_ID_ALARM_NTC = 3;
    public static final int SOUND_ID_ALARM_TIMER = 4;
    public static final int SOUND_ID_SCROLL = 5;
    public static final int SOUND_ID_ALARM_ONCE = 6;

    // SoundPool对象
    public static SoundPool mSoundPlayer = new SoundPool(10,
            AudioManager.STREAM_SYSTEM, 5);
    public static SoundUtil soundPlayUtils;
    // 上下文
    static Context mContext;

    /**
     * 初始化
     *
     * @param context
     */
    public static SoundUtil init(Context context) {
        if (soundPlayUtils == null) {
            soundPlayUtils = new SoundUtil();
        }
        // 初始化声音
        mContext = context;

        mSoundPlayer.load(mContext, R.raw.alarm_5min, 1);// 1
        mSoundPlayer.load(mContext, R.raw.lock, 1);// 1
        mSoundPlayer.load(mContext, R.raw.alarm_ntc, 1);// 1
        return soundPlayUtils;
    }


    public static void play(int soundID) {
        if (soundID == SOUND_ID_ALARM) {
            mSoundPlayer.stop(soundID);
            mSoundPlayer.play(soundID, 1, 1, 0, -1, 1);
        } if (soundID == SOUND_ID_ALARM_NTC) {
            mSoundPlayer.stop(soundID);
            mSoundPlayer.play(soundID, 1, 1, 0, -1, 1);
        }else {
            mSoundPlayer.stop(soundID);
            mSoundPlayer.play(soundID, 1, 1, 0, 0, 1);
        }



    }

    public static void stop(int soundID) {
        mSoundPlayer.stop(soundID);

    }

    public static void release() {
        mSoundPlayer.release();
    }

}
