package com.ekek.settingmodule.model;

public class SettingTestCookDataEvent {
    int order;
    int cookerID;

    public int getOrder() {
        return order;
    }
    public int getCookerID()
    {return cookerID;}
    public SettingTestCookDataEvent (int od,int id){
        this .order =od;
        this.cookerID=id;
    }
}
