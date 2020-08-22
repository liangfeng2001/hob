package com.ekek.hardwaremodule.serial;

/**
 * Created by Samhung on 2017/9/2.
 */

public abstract class HardwareManager {
    protected OnHardwareManagerListener listener;
    public abstract void setOnHardwareManagerListener(OnHardwareManagerListener listener);
    public abstract void send(byte[] data);
    public abstract void pauseHardware();
    public abstract void resumeHardware();
    public abstract void closeHardware();



    public static abstract interface OnHardwareManagerListener {
        public abstract void onHardwareReceived(byte[] data, int size);
        public abstract void onHardwareResponse(int action, int code);
        public abstract void onHardwareError(int action, int error);
    }
}
