package com.ekek.tfthobmodule.core;

import java.util.ArrayList;
import java.util.List;

public class CookerTimerManager {
    private List<CountDownTask> coundDownTaskList = new ArrayList<>();


    public long requestCountDown(int countDownType , int totalTime , final CookerTimerManagerCallBack callBack) {
        CountDownTask task = new CountDownTask(countDownType, totalTime, new CountDownTask.CountDownTaskCallBack() {
            @Override
            public void notifyTimeIsUp(long taskID, int notifyAction, int totalTime, int remainTime) {
                callBack.notifyTimeIsUp(taskID,notifyAction,totalTime,remainTime);
                if (notifyAction == CountDownTask.COUNT_DOWN_NOTIFY_ACTION_TOTAL_TIME_IS_UP) {

                    removeTask(taskID);
                }
            }
        });
        coundDownTaskList.add(task);
        return task.getTaskID();

    }

    public void pauseCountDown(long taskID) {
        doPauseCountDown(taskID);
    }

    public void resumeCountDown(long taskID) {
        doResumeCountDown(taskID);
    }

    public void finishCountDown(long taskID) {
        removeTask(taskID);
    }

    private void doPauseCountDown(long taskID) {
        for (CountDownTask task : coundDownTaskList) {
            if (task.getTaskID() == taskID) {
                task.pauseCountDown();
            }
        }
    }

    private void doResumeCountDown(long taskID) {
        for (CountDownTask task : coundDownTaskList) {
            if (task.getTaskID() == taskID) {
                task.resumeCountDown();
            }
        }
    }

    private void removeTask(long taskID) {
        synchronized(coundDownTaskList) {
            for (CountDownTask task : coundDownTaskList) {
                if (task.getTaskID() == taskID) {
                    task.finishCountDown();
                    coundDownTaskList.remove(task);
                    break;
                }
            }
        }

    }


    public void recyle() {
        for (CountDownTask task : coundDownTaskList) {
            task.finishCountDown();
            coundDownTaskList.remove(task);
        }
    }

    public interface CookerTimerManagerCallBack {
        void notifyTimeIsUp(long taskID,int notifyAction , int totalTime , int remainTime);//time unit : minute
    }

}