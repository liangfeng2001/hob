package com.ekek.tfthobmodule.data;

import android.support.annotation.NonNull;

public interface ITimerData {

    /*
     * 设置计时器
     * */
    void setTimer(int hour,int minute,int second);

    /*
    * 开始计时
    * */
    void startCountdown(int hour, int minute, int second);
    /*
     * 暂停计时
     * */
    void pauseCountdown();

    /*
     * 恢复计时
     * */
    void resumeCountdown();
    /*
     * 停止计时
     * */
    void stopCountdown();

    /*
    * 开始8秒计时
    * */
    void startEightSecondCountdown();
    /*
     * 停止8秒计时
     * */
    void stopEightSecondCountdown();

    /*
     * 开始5分钟计时
     * */
    void startFiveMinuteCountdown();
    /*
     * 停止5分钟计时
     * */
    void stopFiveMinuteCountdown();

    void pauseFiveMinuteCountdown();

    /*
    * 回收资源
    * */
    void recyle();

    /*
    * 计时器是否已完成
    * */
    boolean isTimerFinish();

    void setTimerDataCallback(@NonNull TimerDataCallback callback);

    interface TimerDataCallback {
        void onTimeIsUp();

        void onEightSecondTimeIsUp();

        void onFiveMinuteTimeisUp();

        void onUpateTimer(int hour,int minute);

    }
}
