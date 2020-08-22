package com.ekek.hardwaremodule.entity;

/**
 * Created by Samhung on 2018/1/9.
 */

public class CookerHardwareResponse {

    public static final int POWER_SWITCH_UNKNOWN = -1;
    public static final int POWER_SWITCH_ON = 0;
    public static final int POWER_SWITCH_OFF = 1;
    public static final int POWER_SWITCH_OFF_2 = 2;
    public static final int POWER_SWITCH_00 = 3;
    public static final int POWER_SWITCH_11 = 4;
    public static final int POWER_SWITCH_88 = 5;
    public static final int POWER_SWITCH_99 = 6;

    private long sn;
    private byte[] rawData;
    private int powerSwitchState = POWER_SWITCH_UNKNOWN;
    private CookerMessage aCookerMessage , bCookerMessage , cCookerMessage ,dCookerMessage , eCookerMessage,fCookerMessage;

    public boolean getChecksumResult() {
        return checksumResult;
    }

    public void setChecksumResult(boolean checksumResult) {
        this.checksumResult = checksumResult;
    }

    private boolean checksumResult;

    public long getSn() {
        return sn;
    }

    public void setSn(long sn) {
        this.sn = sn;
    }

    public byte[] getRawData() {
        return rawData;
    }

    public void setRawData(byte[] rawData) {
        this.rawData = rawData;
    }

    public int getPowerSwitchState() {
        return powerSwitchState;
    }

    public void setPowerSwitchState(int powerSwitchState) {
        this.powerSwitchState = powerSwitchState;
    }

    public CookerMessage getaCookerMessage() {
        return aCookerMessage;
    }

    public void setaCookerMessage(CookerMessage aCookerMessage) {
        this.aCookerMessage = aCookerMessage;
    }

    public CookerMessage getbCookerMessage() {
        return bCookerMessage;
    }

    public void setbCookerMessage(CookerMessage bCookerMessage) {
        this.bCookerMessage = bCookerMessage;
    }

    public CookerMessage getcCookerMessage() {
        return cCookerMessage;
    }

    public void setcCookerMessage(CookerMessage cCookerMessage) {
        this.cCookerMessage = cCookerMessage;
    }

    public CookerMessage getdCookerMessage() {
        return dCookerMessage;
    }

    public void setdCookerMessage(CookerMessage dCookerMessage) {
        this.dCookerMessage = dCookerMessage;
    }

    public CookerMessage geteCookerMessage() {
        return eCookerMessage;
    }

    public void seteCookerMessage(CookerMessage eCookerMessage) {
        this.eCookerMessage = eCookerMessage;
    }

    public CookerMessage getfCookerMessage() {
        return fCookerMessage;
    }

    public void setfCookerMessage(CookerMessage fCookerMessage) {
        this.fCookerMessage = fCookerMessage;
    }
}
