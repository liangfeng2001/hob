package com.ekek.viewmodule.events;

public class BluetoothEvent {

    public static final int ORDER_FAILED = 0;
    public static final int ORDER_START = 1;
    public static final int ORDER_REPORT_PROGRESS = 2;
    public static final int ORDER_FINISHED = 3;

    private int order;
    private int wParam;
    private int lParam;
    private String sParam;

    public BluetoothEvent(int order, int wParam, int lParam, String sParam) {
        this.order = order;
        this.wParam = wParam;
        this.lParam = lParam;
        this.sParam = sParam;
    }
    public BluetoothEvent(int order) {
        this.order = order;
    }
    public BluetoothEvent(int order, String sParam) {
        this.order = order;
        this.sParam = sParam;
    }
    public BluetoothEvent(int order, int wParam, int lParam) {
        this.order = order;
        this.wParam = wParam;
        this.lParam = lParam;
    }
    public BluetoothEvent(int order, int wParam) {
        this.order = order;
        this.wParam = wParam;
    }

    public int getOrder() {
        return order;
    }
    public void setOrder(int order) {
        this.order = order;
    }
    public int getwParam() {
        return wParam;
    }
    public void setwParam(int wParam) {
        this.wParam = wParam;
    }
    public int getlParam() {
        return lParam;
    }
    public void setlParam(int lParam) {
        this.lParam = lParam;
    }
    public String getsParam() {
        return sParam;
    }
    public void setsParam(String sParam) {
        this.sParam = sParam;
    }
}
