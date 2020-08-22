package com.ekek.tfthobmodule.core;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CountDownTask {
    public static final int COUNT_DOWN_TASK_TYPE_COUNT_NOTIFY_EVERY_SECOND = 0;//每秒钟通知
    public static final int COUNT_DOWN_TASK_TYPE_COUNT_NOTIFY_EVERY_MINUTE = 1;//每分钟通知
    public static final int COUNT_DOWN_TASK_TYPE_COUNT_FOR_POWER_LEVEL_B = 2;//B档第5分钟，第15分钟，第35分钟都要通知一次 ,每分钟通知
    public static final int COUNT_DOWN_TASK_TYPE_COUNT_FOR_POWER_LEVEL_9 = 3;//9档第10分钟，第20分钟都要通知一次 ,每分钟通知

    public static final int COUNT_DOWN_NOTIFY_ACTION_1_SECOND_IS_UP = 0;
    public static final int COUNT_DOWN_NOTIFY_ACTION_1_MINUTE_IS_UP = 1;
    public static final int COUNT_DOWN_NOTIFY_ACTION_5_MINUTE_IS_UP = 2;
    public static final int COUNT_DOWN_NOTIFY_ACTION_15_MINUTE_IS_UP = 3;
    public static final int COUNT_DOWN_NOTIFY_ACTION_35_MINUTE_IS_UP = 4;
    public static final int COUNT_DOWN_NOTIFY_ACTION_10_MINUTE_IS_UP = 5;
    public static final int COUNT_DOWN_NOTIFY_ACTION_20_MINUTE_IS_UP = 6;
    public static final int COUNT_DOWN_NOTIFY_ACTION_TOTAL_TIME_IS_UP = 7;

    private long taskID = -1;
    private int totalTime;//unit : minute
    private int remainTime;//unit : minute
    private boolean needNotifyEveryMinute = false;//每计时一分钟发出通知
    private Disposable mDisposable;//定时器
    private CountDownTaskCallBack callBack;
    private int countDownType = COUNT_DOWN_TASK_TYPE_COUNT_NOTIFY_EVERY_MINUTE;
    public CountDownTask(int countDownType , int totalTime , CountDownTaskCallBack callBack) {
        //taskID = System.currentTimeMillis();
        taskID = System.nanoTime();
        this.totalTime = totalTime;
        this.countDownType = countDownType;
        remainTime = totalTime;
        this.callBack = callBack;
        assignAndStartCountDownTask();
    }


    public long getTaskID() {
        return taskID;
    }

    public void setTaskID(long taskID) {
        this.taskID = taskID;
    }

    public void pauseCountDown() {
        doStopCountDown();
    }

    public void resumeCountDown() {
        assignAndStartCountDownTask();
    }

    public void finishCountDown() {
        doStopCountDown();
        finish();
    }

    private void assignAndStartCountDownTask() {
        if (countDownType == COUNT_DOWN_TASK_TYPE_COUNT_FOR_POWER_LEVEL_B) {
            doStartCountDownNotifyMinute();

        }if (countDownType == COUNT_DOWN_TASK_TYPE_COUNT_FOR_POWER_LEVEL_9) {
            doStartCountDownNotifyMinute();

        }else if (countDownType == COUNT_DOWN_TASK_TYPE_COUNT_NOTIFY_EVERY_MINUTE) {
            doStartCountDownNotifyMinute();

        }else if (countDownType == COUNT_DOWN_TASK_TYPE_COUNT_NOTIFY_EVERY_SECOND) {
            doStartCountDownNotifySecond();

        }

    }

    private void doStartCountDownNotifyMinute() {
        doStopCountDown();
        mDisposable = Observable
               .interval(1,1, TimeUnit.MINUTES)
               // .interval(1,1, TimeUnit.SECONDS)//用秒来测试
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                    updateTimer();

                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribe();
    }

    private void doStartCountDownNotifySecond() {
        mDisposable = Observable
                .interval(1,1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        updateTimer();


                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribe();
    }

    private void updateTimer() {
        remainTime--;
        if (remainTime == 0) {
            callBack.notifyTimeIsUp(taskID,COUNT_DOWN_NOTIFY_ACTION_TOTAL_TIME_IS_UP,totalTime,remainTime);
            doStopCountDown();
            finish();
        }else {
            if (countDownType == COUNT_DOWN_TASK_TYPE_COUNT_FOR_POWER_LEVEL_B) {
                int deltaMinuteForPowerLevelB = totalTime - remainTime;
                if (deltaMinuteForPowerLevelB == 5) {
                    callBack.notifyTimeIsUp(taskID,COUNT_DOWN_NOTIFY_ACTION_5_MINUTE_IS_UP,totalTime,remainTime);

                }else if (deltaMinuteForPowerLevelB == 15) {
                    callBack.notifyTimeIsUp(taskID,COUNT_DOWN_NOTIFY_ACTION_15_MINUTE_IS_UP,totalTime,remainTime);

                }else if (deltaMinuteForPowerLevelB == 25) {
                    callBack.notifyTimeIsUp(taskID,COUNT_DOWN_NOTIFY_ACTION_35_MINUTE_IS_UP,totalTime,remainTime);

                }else {
                    callBack.notifyTimeIsUp(taskID,COUNT_DOWN_NOTIFY_ACTION_1_MINUTE_IS_UP,totalTime,remainTime);
                }


            }if (countDownType == COUNT_DOWN_TASK_TYPE_COUNT_FOR_POWER_LEVEL_9) {
                int deltaMinuteForPowerLevel9 = totalTime - remainTime;
                if (deltaMinuteForPowerLevel9 == 10) {
                    callBack.notifyTimeIsUp(taskID,COUNT_DOWN_NOTIFY_ACTION_10_MINUTE_IS_UP,totalTime,remainTime);

                }else if (deltaMinuteForPowerLevel9 == 20) {
                    callBack.notifyTimeIsUp(taskID,COUNT_DOWN_NOTIFY_ACTION_20_MINUTE_IS_UP,totalTime,remainTime);

                }else {
                    callBack.notifyTimeIsUp(taskID,COUNT_DOWN_NOTIFY_ACTION_1_MINUTE_IS_UP,totalTime,remainTime);
                }


            }else if (countDownType == COUNT_DOWN_TASK_TYPE_COUNT_NOTIFY_EVERY_SECOND) {
                callBack.notifyTimeIsUp(taskID,COUNT_DOWN_NOTIFY_ACTION_1_SECOND_IS_UP,totalTime,remainTime);


            }else if (countDownType == COUNT_DOWN_TASK_TYPE_COUNT_NOTIFY_EVERY_MINUTE) {
                callBack.notifyTimeIsUp(taskID,COUNT_DOWN_NOTIFY_ACTION_1_MINUTE_IS_UP,totalTime,remainTime);

            }

        }
    }


    private void doStopCountDown() {
        if (mDisposable != null) {
            if (!mDisposable.isDisposed()) {
                mDisposable.dispose();
            }
        }
    }

    private void finish() {
        taskID = -1;
        totalTime = 0;
        remainTime = 0;
    }

    public interface CountDownTaskCallBack {
        void notifyTimeIsUp(long taskID,int notifyAction , int totalTime , int remainTime);//time unit : minute

    }
}
