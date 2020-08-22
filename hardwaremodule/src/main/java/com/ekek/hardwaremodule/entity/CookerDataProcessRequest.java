package com.ekek.hardwaremodule.entity;

/**
 * Created by Samhung on 2018/1/9.
 */

public class CookerDataProcessRequest {
    private long sn;
    private int action;
    private byte[] commandData;

    public long getSn() {
        return sn;
    }

    public void setSn(long sn) {
        this.sn = sn;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public byte[] getCommandData() {
        return commandData;
    }

    public void setCommandData(byte[] commandData) {
        this.commandData = commandData;
    }

    public void recyle() {
        commandData = null;
    }
}
