package com.ekek.commonmodule.base;

import com.ekek.commonmodule.utils.LogUtil;

public abstract class BaseThread extends Thread {

    // Fields
    private boolean cancelTask = false;
    protected int userParamInt;
    protected Object userParamObj;

    // Abstract functions
    protected abstract boolean started();
    protected abstract boolean performTaskInLoop();
    protected abstract void finished();

    // Constructors
    public BaseThread() {
        this(0, null);
    }
    public BaseThread(int userParamInt, Object userParamObj) {
        this.userParamInt = userParamInt;
        this.userParamObj = userParamObj;
    }

    @Override
    public void run() {
        try {
            if (!started()) {
                return;
            }
            while (!cancelTask) {
                if (!performTaskInLoop()) {
                    break;
                }
                Thread.sleep(50);
            }
            finished();
        } catch (InterruptedException e) {
            LogUtil.e(e.getMessage());
        }
    }

    public void setCancelTask(boolean cancelTask) {
        this.cancelTask = cancelTask;
    }
}
