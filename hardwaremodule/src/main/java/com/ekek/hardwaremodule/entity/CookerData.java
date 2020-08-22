package com.ekek.hardwaremodule.entity;

public class CookerData {
    private int id,mode,value;

    public CookerData() {

    }

    public CookerData(int id, int mode) {
        this.id = id;
        this.mode = mode;
    }

    public CookerData(int id, int mode, int value) {
        this.id = id;
        this.mode = mode;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
