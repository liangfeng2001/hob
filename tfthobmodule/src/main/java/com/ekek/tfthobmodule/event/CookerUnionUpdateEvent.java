package com.ekek.tfthobmodule.event;

public class CookerUnionUpdateEvent {
    int unionCookerID;
    int upCookerID;
    int upWorkMode;
    int upFireValue;
    int upTempValue;
    int upHourValue;
    int upMinute;
    int upTempIndicatorID;
    int upRecipesID;
    String upErrorMessage;
    int downCookerID;
    int downWorkMode;
    int downFireValue;
    int downTempValue;
    int downHourValue;
    int downMinute;
    int downTempIndicatorID;
    int downRecipesID;
    String downErrorMessage;

    public CookerUnionUpdateEvent(int unionCookerID, int upCookerID, int upWorkMode, int upFireValue, int upTempValue, int upHourValue, int upMinute, int upTempIndicatorID, int upRecipesID, String upErrorMessage, int downCookerID, int downWorkMode, int downFireValue, int downTempValue, int downHourValue, int downMinute, int downTempIndicatorID, int downRecipesID, String downErrorMessage) {
        this.unionCookerID = unionCookerID;
        this.upCookerID = upCookerID;
        this.upWorkMode = upWorkMode;
        this.upFireValue = upFireValue;
        this.upTempValue = upTempValue;
        this.upHourValue = upHourValue;
        this.upMinute = upMinute;
        this.upTempIndicatorID = upTempIndicatorID;
        this.upRecipesID = upRecipesID;
        this.upErrorMessage = upErrorMessage;
        this.downCookerID = downCookerID;
        this.downWorkMode = downWorkMode;
        this.downFireValue = downFireValue;
        this.downTempValue = downTempValue;
        this.downHourValue = downHourValue;
        this.downMinute = downMinute;
        this.downTempIndicatorID = downTempIndicatorID;
        this.downRecipesID = downRecipesID;
        this.downErrorMessage = downErrorMessage;
    }

    public int getUnionCookerID() {
        return unionCookerID;
    }

    public void setUnionCookerID(int unionCookerID) {
        this.unionCookerID = unionCookerID;
    }

    public int getUpCookerID() {
        return upCookerID;
    }

    public void setUpCookerID(int upCookerID) {
        this.upCookerID = upCookerID;
    }

    public int getUpWorkMode() {
        return upWorkMode;
    }

    public void setUpWorkMode(int upWorkMode) {
        this.upWorkMode = upWorkMode;
    }

    public int getUpFireValue() {
        return upFireValue;
    }

    public void setUpFireValue(int upFireValue) {
        this.upFireValue = upFireValue;
    }

    public int getUpTempValue() {
        return upTempValue;
    }

    public void setUpTempValue(int upTempValue) {
        this.upTempValue = upTempValue;
    }

    public int getUpHourValue() {
        return upHourValue;
    }

    public void setUpHourValue(int upHourValue) {
        this.upHourValue = upHourValue;
    }

    public int getUpMinute() {
        return upMinute;
    }

    public void setUpMinute(int upMinute) {
        this.upMinute = upMinute;
    }

    public int getUpTempIndicatorID() {
        return upTempIndicatorID;
    }

    public void setUpTempIndicatorID(int upTempIndicatorID) {
        this.upTempIndicatorID = upTempIndicatorID;
    }

    public int getUpRecipesID() {
        return upRecipesID;
    }

    public void setUpRecipesID(int upRecipesID) {
        this.upRecipesID = upRecipesID;
    }

    public String getUpErrorMessage() {
        return upErrorMessage;
    }

    public void setUpErrorMessage(String upErrorMessage) {
        this.upErrorMessage = upErrorMessage;
    }

    public int getDownCookerID() {
        return downCookerID;
    }

    public void setDownCookerID(int downCookerID) {
        this.downCookerID = downCookerID;
    }

    public int getDownWorkMode() {
        return downWorkMode;
    }

    public void setDownWorkMode(int downWorkMode) {
        this.downWorkMode = downWorkMode;
    }

    public int getDownFireValue() {
        return downFireValue;
    }

    public void setDownFireValue(int downFireValue) {
        this.downFireValue = downFireValue;
    }

    public int getDownTempValue() {
        return downTempValue;
    }

    public void setDownTempValue(int downTempValue) {
        this.downTempValue = downTempValue;
    }

    public int getDownHourValue() {
        return downHourValue;
    }

    public void setDownHourValue(int downHourValue) {
        this.downHourValue = downHourValue;
    }

    public int getDownMinute() {
        return downMinute;
    }

    public void setDownMinute(int downMinute) {
        this.downMinute = downMinute;
    }

    public int getDownTempIndicatorID() {
        return downTempIndicatorID;
    }

    public void setDownTempIndicatorID(int downTempIndicatorID) {
        this.downTempIndicatorID = downTempIndicatorID;
    }

    public int getDownRecipesID() {
        return downRecipesID;
    }

    public void setDownRecipesID(int downRecipesID) {
        this.downRecipesID = downRecipesID;
    }

    public String getDownErrorMessage() {
        return downErrorMessage;
    }

    public void setDownErrorMessage(String downErrorMessage) {
        this.downErrorMessage = downErrorMessage;
    }
}
