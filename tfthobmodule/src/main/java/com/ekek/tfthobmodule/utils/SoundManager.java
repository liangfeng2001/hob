package com.ekek.tfthobmodule.utils;

import android.content.Context;
import android.media.AudioManager;

import com.ekek.commonmodule.utils.Logger;
import com.ekek.settingmodule.database.SettingPreferencesUtil;
import com.ekek.tfthobmodule.CataTFTHobApplication;
import com.ekek.tfthobmodule.R;
import com.ekek.tfthobmodule.event.SoundEvent;

import java.util.ArrayList;
import java.util.List;

public class SoundManager {

    // Fields
    private static volatile SoundManager instance;
    private Context context;
    private List<PlaySoundTask> taskList = new ArrayList<>();

    // Singleton
    private SoundManager(Context context) {
        this.context = context;
    }
    public static SoundManager getInstance(Context context) {
        if (instance == null) {
            synchronized (SoundManager.class) {
                if (instance == null) {
                    instance = new SoundManager(context);
                }
            }
        }
        return instance;
    }

    // Public functions
    public void playAlarm(int soundType) {
        doStopTask(soundType);
        PlaySoundTask task = new PlaySoundTask(
                context,
                new PlaySoundParams(
                        PlaySoundTask.TASK_TYPE_INTERMITTENT,
                        getAlarmResource(),
                        soundType,
                        false,
                        1000 * SettingPreferencesUtil.getAlarmDuration(context),
                        1000 * SettingPreferencesUtil.getAlarmPostponeDuration(context),
                        AudioManager.STREAM_ALARM,
                        1.0f),
                0,null);
        task.setListener(new PlaySoundTask.OnPlaySoundListener() {
            @Override
            public void onHeartBeat(int userParam, Object userParamObj) {
                CataTFTHobApplication.getInstance().updateLatestTouchTime();
            }
        });
        task.start();
        taskList.add(task);
    }
    public void stopAlarm(int soundType) {
        doStopTask(soundType);
    }
    public void playAlarmOnce(int soundType) {
        doStopTask(soundType);
        PlaySoundTask task = new PlaySoundTask(
                context,
                new PlaySoundParams(
                        PlaySoundTask.TASK_TYPE_ONCE,
                        getAlarmResource(),
                        soundType,
                        false,
                        0,
                        0,
                        AudioManager.STREAM_ALARM,
                        1.0f),
                0,null);
        task.setListener(new PlaySoundTask.OnPlaySoundListener() {
            @Override
            public void onHeartBeat(int userParam, Object userParamObj) {
                CataTFTHobApplication.getInstance().updateLatestTouchTime();
            }
        });
        task.start();
        taskList.add(task);
    }
    public void stopAlarmOnce(int soundType) {
        doStopTask(soundType);
    }
    public void playAlarmForTimer(int soundType) {
        stopAlarmForTimer(soundType);
        PlaySoundTask task = new PlaySoundTask(
                context,
                new PlaySoundParams(
                        PlaySoundTask.TASK_TYPE_INTERMITTENT,
                        getAlarmResource(),
                        soundType,
                        false,
                        1000 * SettingPreferencesUtil.getAlarmDuration(context),
                        1000 * SettingPreferencesUtil.getAlarmPostponeDuration(context),
                        AudioManager.STREAM_ALARM,
                        1.0f),
                0,null);
        task.setListener(new PlaySoundTask.OnPlaySoundListener() {
            @Override
            public void onHeartBeat(int userParam, Object userParamObj) {
                CataTFTHobApplication.getInstance().updateLatestTouchTime();
            }
        });
        task.start();
        taskList.add(task);
    }
    public void stopAlarmForTimer(int soundType) {
        doStopTask(soundType);
    }
    public void playAlarmForNtc() {
        doStopTask(SoundEvent.SOUND_TYPE_ALARM_NTC);
        PlaySoundTask task = new PlaySoundTask(
                context,
                new PlaySoundParams(
                        PlaySoundTask.TASK_TYPE_FOR_EVER,
                        R.raw.alarm_ntc,
                        SoundEvent.SOUND_TYPE_ALARM_NTC,
                        true,
                        0,
                        0,
                        AudioManager.STREAM_ALARM,
                        1.0f),
                0,null);
        task.setListener(new PlaySoundTask.OnPlaySoundListener() {
            @Override
            public void onHeartBeat(int userParam, Object userParamObj) {
                CataTFTHobApplication.getInstance().updateLatestTouchTime();
            }
        });
        task.start();
        taskList.add(task);
    }
    public void stopAlarmForNtc() {
        doStopTask(SoundEvent.SOUND_TYPE_ALARM_NTC);
    }

    // Private functions
    private PlaySoundTask getExistedTask(int soundType) {
        for (PlaySoundTask task: taskList) {
            if (task.getParams().getSoundType() == soundType) {
                return task;
            }
        }
        return null;
    }
    private void doStopTask(int soundType) {
        PlaySoundTask task = getExistedTask(soundType);
        if (task != null) {
            task.stop();
            taskList.remove(task);
        }
    }

    public void playScrollSound() {
        PlaySoundTask task = new PlaySoundTask(
                context,
                new PlaySoundParams(
                        PlaySoundTask.TASK_TYPE_ONCE,
                        R.raw.scroll_short,
                        SoundEvent.SOUND_TYPE_SCROLL,
                        false,
                        0,
                        0,
                        AudioManager.STREAM_MUSIC,
                        1.0f),
                0,null);
        task.setListener(new PlaySoundTask.OnPlaySoundListener() {
            @Override
            public void onHeartBeat(int userParam, Object userParamObj) {
                CataTFTHobApplication.getInstance().updateLatestTouchTime();
            }
        });
        task.start();
    }
    public void stopScrollSound() {
        doStopTask(SoundEvent.SOUND_TYPE_SCROLL);
    }

    private int getAlarmResource() {
        int alarmDuration = SettingPreferencesUtil.getAlarmDuration(context);
        Logger.getInstance().d("alarmDuration = " + alarmDuration);
        switch (alarmDuration) {
            case 30:
                return R.raw.alarm_30sec;
            case 60:
                return R.raw.alarm_1min;
            case 60 * 2:
                return R.raw.alarm_2min;
            case 60 * 3:
                return R.raw.alarm_3min;
            case 60 * 4:
                return R.raw.alarm_4min;
            case 60 * 5:
                return R.raw.alarm_5min;
        }
        return R.raw.alarm_5min;
    }
}
