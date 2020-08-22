package com.ekek.tfthobmodule.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.SystemClock;

import com.ekek.commonmodule.base.BaseThread;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.commonmodule.utils.Logger;
import com.ekek.tfthobmodule.event.PlaySoundEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

public class PlaySoundTask {

    // Constants
    public static final int TASK_TYPE_ONCE = 0;
    public static final int TASK_TYPE_FOR_EVER = 1;
    public static final int TASK_TYPE_INTERMITTENT = 2;

    // Fields
    private static int taskCount = 0;
    private int id;
    private Context context;
    private PlaySoundParams params;
    private int userParam;
    private Object userParamObj;
    private MediaPlayer mediaPlayer;
    private PlayingThread playingThread;
    private OnPlaySoundListener listener;

    // Interfaces
    public interface OnPlaySoundListener {
        void onHeartBeat(int userParam, Object userParamObj);
    }

    // Constructors
    public PlaySoundTask(
            Context context,
            PlaySoundParams params,
            int userParam,
            Object userParamObj) {
        taskCount++;
        id = taskCount;
        this.context = context;
        this.params = params;
        this.userParam = userParam;
        this.userParamObj = userParamObj;
    }

    // Public functions
    public void start() {
        if (playingThread != null && playingThread.isAlive()) {
            Logger.getInstance().e("The task has been already started!");
            return;
        }
        if (playingThread == null) {
            playingThread = new PlayingThread();
        }
        playingThread.start();
    }
    public void stop() {
        if (playingThread == null) {
            Logger.getInstance().e("The task has not been started yet!");
            return;
        }

        if (playingThread.isAlive()) {
            playingThread.setCancelTask(true);
            try {
                playingThread.join();
            } catch (InterruptedException e) {
                Logger.getInstance().e(e);
            }
        }
        playingThread = null;
    }

    // Private functions
    private void doPlay(int streamType, int audio, float volume, final boolean looping) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setAudioStreamType(streamType);
            AssetFileDescriptor afd = context.getResources().openRawResourceFd(audio);
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.setVolume(volume, volume);
            mediaPlayer.setLooping(looping);
            mediaPlayer.prepare();
            afd.close();
            mediaPlayer.start();
        } catch (IOException e) {
            LogUtil.e(e.getMessage());
        }
    }
    private void doStop() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.setLooping(false);
                mediaPlayer.stop();
                mediaPlayer.release();
            }
            mediaPlayer = null;
        }
    }
    private void onHeartBeat() {
        if (listener != null) {
            listener.onHeartBeat(userParam, userParamObj);
        }
    }

    // Properties
    public int getId() {
        return id;
    }
    public PlaySoundParams getParams() {
        return params;
    }
    public void setParams(PlaySoundParams params) {
        this.params = params;
    }
    public int getUserParam() {
        return userParam;
    }
    public void setUserParam(int userParam) {
        this.userParam = userParam;
    }
    public Object getUserParamObj() {
        return userParamObj;
    }
    public void setUserParamObj(Object userParamObj) {
        this.userParamObj = userParamObj;
    }
    public void setListener(OnPlaySoundListener listener) {
        this.listener = listener;
    }

    // Classes
    class PlayingThread extends BaseThread {

        private static final int MAX_PLAYING_DURATION = 1000 * 60 * 30;
        private static final int HEART_BEAT_INTERVAL = 1000;

        private long startTime = 0;
        private long lastTimeHeartBeat = 0;
        private long lastTimeStartPlaying = 0;
        private long lastTimeStopPlaying = 0;
        private boolean isPlaying;

        @Override
        protected boolean started() {
            startTime = SystemClock.elapsedRealtime();
            lastTimeHeartBeat = SystemClock.elapsedRealtime();
            lastTimeStartPlaying = SystemClock.elapsedRealtime();
            doPlay(params.getStreamType(), params.getResource(), params.getVolume(), params.isLooping());
            Logger.getInstance().d("started(" + getId() + "," + params.getSoundType() + ")");
            isPlaying = true;
            return true;
        }

        @Override
        protected boolean performTaskInLoop() {
            if (SystemClock.elapsedRealtime() - startTime > MAX_PLAYING_DURATION) {
                EventBus.getDefault().post(new PlaySoundEvent(PlaySoundEvent.ORDER_TIME_OUT));
                return false;
            }
            if (SystemClock.elapsedRealtime() - lastTimeHeartBeat >= HEART_BEAT_INTERVAL) {
                onHeartBeat();
                lastTimeHeartBeat = SystemClock.elapsedRealtime();
            }

            switch (params.getTaskType()) {
                case TASK_TYPE_ONCE:
                    if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
                        // 播放结束
                        return false;
                    }
                    break;
                case TASK_TYPE_FOR_EVER:
                    // 无需做任何事
                    break;
                case TASK_TYPE_INTERMITTENT:
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        if (params.isLooping()) {
                            // 如果当前正在循环播放文件，而设定的播放持续时间到了，则强制进行停止
                            if (SystemClock.elapsedRealtime() - lastTimeStartPlaying > params.getPlayingDuration()) {
                                doStop();
                                isPlaying = false;
                                lastTimeStopPlaying = SystemClock.elapsedRealtime();
                                Logger.getInstance().d("doPause(" + getId() + "," + params.getSoundType() + ")");
                            }
                        }
                    } else {
                        // 此分支对应非循环播放的情形
                        if (isPlaying) {
                            // 上次记录的状态 isPlaying 为 True，而此刻 mediaPlayer 却不在播放中，意味着刚刚播放结束，
                            // 故可以记录下停止播放的时间
                            lastTimeStopPlaying = SystemClock.elapsedRealtime();
                            isPlaying = false;
                            Logger.getInstance().d("stopped(" + getId() + "," + params.getSoundType() + ")");
                        }

                        if (SystemClock.elapsedRealtime() - lastTimeStopPlaying >= params.getPostponeDuration()) {
                            // 设定的延迟播放时间到达，则重启播放
                            doPlay(params.getStreamType(), params.getResource(), params.getVolume(), params.isLooping());
                            lastTimeStartPlaying = SystemClock.elapsedRealtime();
                            isPlaying = true;
                            Logger.getInstance().d("doResume(" + getId() + "," + params.getSoundType() + ")");
                        }
                    }
                    break;
            }
            return true;
        }

        @Override
        protected void finished() {
            doStop();
            Logger.getInstance().d("finished(" + getId() + "," + params.getSoundType() + ")");
        }
    }
}
