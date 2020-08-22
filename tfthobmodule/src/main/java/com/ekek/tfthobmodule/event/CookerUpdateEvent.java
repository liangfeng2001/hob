package com.ekek.tfthobmodule.event;

import java.util.HashMap;
import java.util.Map;

public class CookerUpdateEvent {

    public static Map<Integer, Integer> latestBatchIDs = new HashMap<>();
    public static final int MAX_BATCH_ID = 99;

    private int batchID;
    private int cookerID;
    private int workMode;
    private int tempMode;
    private int fireValue,tempValue,realTempValue,hourValue,minuteValue,secondValue;
    private int tempIndicatorResID,recipesResID;
    private int recipesShowOrder;
    private String errorMessage;

    public CookerUpdateEvent(int cookerID, int workMode, int tempMode, int fireValue, int tempValue, int realTempValue,int hourValue, int minuteValue, int secondValue, int tempIndicatorResID, int recipesResID, int recipesShowOrder, String errorMessage) {
        produceBatchID(cookerID);
        this.cookerID = cookerID;
        this.workMode = workMode;
        this.tempMode = tempMode;
        this.fireValue = fireValue;
        this.tempValue = tempValue;
        this.realTempValue = realTempValue;
        this.hourValue = hourValue;
        this.minuteValue = minuteValue;
        this.secondValue = secondValue;
        this.tempIndicatorResID = tempIndicatorResID;
        this.recipesResID = recipesResID;
        this.recipesShowOrder = recipesShowOrder;
        this.errorMessage = errorMessage;
    }

    private synchronized void produceBatchID (int cookerID) {
        if (!latestBatchIDs.containsKey(cookerID)) {
            latestBatchIDs.put(cookerID, 0);
        }

        batchID = latestBatchIDs.get(cookerID);
        batchID++;
        if (batchID > MAX_BATCH_ID) {
            batchID = 0;
        }
        latestBatchIDs.put(cookerID, batchID);
    }

    public int getCookerID() {
        return cookerID;
    }

    public void setCookerID(int cookerID) {
        this.cookerID = cookerID;
    }

    public int getWorkMode() {
        return workMode;
    }

    public void setWorkMode(int workMode) {
        this.workMode = workMode;
    }

    public int getTempMode() {
        return tempMode;
    }

    public void setTempMode(int tempMode) {
        this.tempMode = tempMode;
    }

    public int getFireValue() {
        return fireValue;
    }

    public void setFireValue(int fireValue) {
        this.fireValue = fireValue;
    }

    public int getTempValue() {
        return tempValue;
    }

    public void setTempValue(int tempValue) {
        this.tempValue = tempValue;
    }

    public int getRealTempValue() {
        return realTempValue;
    }

    public void setRealTempValue(int realTempValue) {
        this.realTempValue = realTempValue;
    }

    public int getHourValue() {
        return hourValue;
    }

    public void setHourValue(int hourValue) {
        this.hourValue = hourValue;
    }

    public int getMinuteValue() {
        return minuteValue;
    }

    public void setMinuteValue(int minuteValue) {
        this.minuteValue = minuteValue;
    }

    public int getSecondValue() {
        return secondValue;
    }

    public void setSecondValue(int secondValue) {
        this.secondValue = secondValue;
    }

    public int getTempIndicatorResID() {
        return tempIndicatorResID;
    }

    public void setTempIndicatorResID(int tempIndicatorResID) {
        this.tempIndicatorResID = tempIndicatorResID;
    }

    public int getRecipesResID() {
        return recipesResID;
    }

    public void setRecipesResID(int recipesResID) {
        this.recipesResID = recipesResID;
    }

    public int getRecipesShowOrder() {
        return recipesShowOrder;
    }

    public void setRecipesShowOrder(int recipesShowOrder) {
        this.recipesShowOrder = recipesShowOrder;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getBatchID() {
        return batchID;
    }

    public boolean isLatestBatch() {
        return batchID == latestBatchIDs.get(cookerID);
    }
}
