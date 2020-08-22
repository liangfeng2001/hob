package com.ekek.tfthobmodule.data;

import android.support.annotation.NonNull;

import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.hardwaremodule.protocol.InductionCookerProtocol;
import com.ekek.tfthobmodule.database.DatabaseHelper;
import com.ekek.tfthobmodule.database.SettingDbHelper;

public class CookerData implements ITimerData.TimerDataCallback,ICookerData{
    public static final int COOKER_INVALID_DATA = -1;
    public static final int COOKER_MODE_POWER_OFF = 0;
    public static final int COOKER_MODE_FIRE_GEAR_WITH_TIMER = 1;
    public static final int COOKER_MODE_FIRE_GEAR_WITHOUT_TIMER = 2;
    public static final int COOKER_MODE_FAST_TEMP_GEAR_WITH_TIMER = 3;
    public static final int COOKER_MODE_FAST_TEMP_GEAR_WITHOUT_TIMER = 4;
    public static final int COOKER_MODE_PRECISE_TEMP_GEAR_WITH_TIMER = 5;
    public static final int COOKER_MODE_PRECISE_TEMP_GEAR_WITHOUT_TIMER = 6;
    public static final int TEMP_MODE_NONE = 1;
    public static final int TEMP_MODE_FAST = 2;
    public static final int TEMP_MODE_PRECISE = 3;
    public static final int TEMP_MODE_FAST_WITHOUT_TEMPSENSOR = 4;

    private int id;//cooker id
    private int cookerMode = COOKER_MODE_POWER_OFF;//锅的模式
    private int tempMode = TEMP_MODE_NONE;
    private int fireGearValue = COOKER_INVALID_DATA;
    private int tempGearValue = COOKER_INVALID_DATA;
    private int tempIdentifyResourceID = COOKER_INVALID_DATA;
    private TimerData mTimerData;
    private CookerSettingData mCookerSettingData;//设置data
    @NonNull
    private CookerDataCallback callback;

    public CookerData(int id) {
        init(id);
    }

    private void init(int id) {
        this.id = id;
        mCookerSettingData = new CookerSettingData(id);
        mTimerData = new TimerData();
        mTimerData.setTimerDataCallback(this);
    }

    private void setFireGearValue(int fireGear) {
        setValue(fireGear,COOKER_INVALID_DATA,COOKER_INVALID_DATA,COOKER_INVALID_DATA,COOKER_INVALID_DATA,COOKER_INVALID_DATA);
    }

    private void setFireGearWithTimerValue(int fireGear , int hour, int minute, int second) {
        setValue(fireGear,COOKER_INVALID_DATA,COOKER_INVALID_DATA,hour,minute,second);

    }

    private void setTempGearValue(int mode,int tempGear,int tempIdentifyResourceID) {
        tempMode = mode;
        setValue(COOKER_INVALID_DATA,tempGear,tempIdentifyResourceID,COOKER_INVALID_DATA,COOKER_INVALID_DATA,COOKER_INVALID_DATA);

    }

    private void setTempGearWithTimerValue(int mode,int tempGear,int tempIdentifyResourceID,int hour,int minute,int second) {
        tempMode = mode;
        setValue(COOKER_INVALID_DATA,tempGear,tempIdentifyResourceID,hour,minute,second);
    }


    private void setValue(int fireGear,int tempGear , int tempIdentifyResourceID,int hour,int minute,int second) {
        if (fireGear != COOKER_INVALID_DATA ) {
            LogUtil.d("Enter:: set value");
            fireGearValue = fireGear;
            if (fireGearValue == 10) mTimerData.startFiveMinuteCountdown();
            if (hour != COOKER_INVALID_DATA) {
                mTimerData.startCountdown(hour,minute,second);
                cookerMode = COOKER_MODE_FIRE_GEAR_WITH_TIMER;

            }else {
                cookerMode = COOKER_MODE_FIRE_GEAR_WITHOUT_TIMER;
            }
            resetTempGear();

        }else if (tempGear != COOKER_INVALID_DATA) {
            tempGearValue = tempGear;
            if (hour != COOKER_INVALID_DATA) {
                mTimerData.startCountdown(hour,minute,second);
                if (tempMode == TEMP_MODE_FAST)cookerMode = COOKER_MODE_FAST_TEMP_GEAR_WITH_TIMER;
                else if (tempMode == TEMP_MODE_PRECISE) cookerMode = COOKER_MODE_PRECISE_TEMP_GEAR_WITH_TIMER;
            }else {
                if (tempMode == TEMP_MODE_FAST)cookerMode = COOKER_MODE_FAST_TEMP_GEAR_WITHOUT_TIMER;
                else if (tempMode == TEMP_MODE_PRECISE) cookerMode = COOKER_MODE_PRECISE_TEMP_GEAR_WITHOUT_TIMER;
            }
            if (tempIdentifyResourceID != COOKER_INVALID_DATA) {
                this.tempIdentifyResourceID = tempIdentifyResourceID;
            }
            resetFireGear();
        }

    }

    public void setCookerSettingData(CookerSettingData data) {
        mCookerSettingData = data;
        processCookerSettingData();



    }

    private void processCookerSettingData() {
        id = mCookerSettingData.getCookerID();
        if (mCookerSettingData.getFireValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//档位模式
            if (mCookerSettingData.getTimerHourValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//有定时
                setFireGearWithTimerValue(mCookerSettingData.getFireValue(),mCookerSettingData.getTimerHourValue(),mCookerSettingData.getTimerMinuteValue(),mCookerSettingData.getTimerSecondValue());

            }else {//没定时
                setFireGearValue(mCookerSettingData.getFireValue());
            }
            callback.onRequestToCook(mCookerSettingData);

        }else {//温控模式
            if (mCookerSettingData.getTimerHourValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//有定时
                callback.onRequestUserPrepareToCook();
                //setTempGearWithTimerValue(mCookerSettingData.getTempMode(),mCookerSettingData.getTempValue(),mCookerSettingData.getTempIdentifyDrawableResourceID(),mCookerSettingData.getTimerHourValue(),mCookerSettingData.getTimerMinuteValue(),mCookerSettingData.getTimerSecondValue());

            }else {//没定时
                setTempGearValue(mCookerSettingData.getTempMode(),mCookerSettingData.getTempValue(),mCookerSettingData.getTempIdentifyDrawableResourceID());
                callback.onRequestToCook(mCookerSettingData);
            }
        }

    }

    public void recoverToWork() {
        id = mCookerSettingData.getCookerID();
        if (mCookerSettingData.getFireValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//档位模式
            if (mCookerSettingData.getTimerHourValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//有定时
                setFireGearWithTimerValue(mCookerSettingData.getFireValue(),mCookerSettingData.getTimerHourValue(),mCookerSettingData.getTimerMinuteValue(),mCookerSettingData.getTimerSecondValue());

            }else {//没定时
                setFireGearValue(mCookerSettingData.getFireValue());
            }
            callback.onRecoverToCook(mCookerSettingData,mTimerData.getTimerRemainValue()[0],mTimerData.getTimerRemainValue()[1]);

        }else {//温控模式
            if (mCookerSettingData.getTimerHourValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//有定时
                callback.onRequestUserPrepareToCook();
                //setTempGearWithTimerValue(mCookerSettingData.getTempMode(),mCookerSettingData.getTempValue(),mCookerSettingData.getTempIdentifyDrawableResourceID(),mCookerSettingData.getTimerHourValue(),mCookerSettingData.getTimerMinuteValue(),mCookerSettingData.getTimerSecondValue());

            }else {//没定时
                setTempGearValue(mCookerSettingData.getTempMode(),mCookerSettingData.getTempValue(),mCookerSettingData.getTempIdentifyDrawableResourceID());
                //callback.onRequestToCook(mCookerSettingData);
                callback.onRecoverToCook(mCookerSettingData,mTimerData.getTimerRemainValue()[0],mTimerData.getTimerRemainValue()[1]);

            }
        }
    }

    public CookerSettingData getCookerSettingData() {
        return mCookerSettingData;
    }

    /*
    *
    * 是否占用蓝牙温度计，用于判断其他锅是否能用温度计来精确控温，同一时间只能一个锅使用温度计
    * */
    public boolean isUsingThermometer() {
        return cookerMode == COOKER_MODE_PRECISE_TEMP_GEAR_WITH_TIMER ||cookerMode == COOKER_MODE_PRECISE_TEMP_GEAR_WITHOUT_TIMER;
    }

    public void resetFireGear() {
        fireGearValue = COOKER_INVALID_DATA;
    }

    public void resetTempGear() {
        tempGearValue = COOKER_INVALID_DATA;
        tempIdentifyResourceID = COOKER_INVALID_DATA;
    }


    @Override
    public void onTimeIsUp() {
        tempGearValue = COOKER_INVALID_DATA;
        fireGearValue = COOKER_INVALID_DATA;
        callback.onTimeIsUp();

    }

    @Override
    public void onEightSecondTimeIsUp() {

    }

    @Override
    public void onFiveMinuteTimeisUp() {
        mCookerSettingData.setFireValue(9);
        callback.onFireBGearTimeIsUp(mCookerSettingData);
    }

    @Override
    public void onUpateTimer(int hour, int minute) {
       // LogUtil.d("CookerData onUpateTimer");

        callback.onUpateTimer(hour,minute);
    }

    @Override
    public void setCookerDataCallback(@NonNull CookerDataCallback callback) {
        this.callback = callback;
    }

    @Override
    public void stopTimerForPoweroff() {
        tempGearValue = COOKER_INVALID_DATA;
        fireGearValue = COOKER_INVALID_DATA;
        mTimerData.stopCountdown();
        mTimerData.resetTimerValue();
        mTimerData.stopFiveMinuteCountdown();
        int tempSensorStatus = SettingDbHelper.getTemperatureSensorStatus();
        if (tempSensorStatus != -1) {
            if (tempSensorStatus == id) SettingDbHelper.saveTemperatureSensorStatus(-1);
        }
        mCookerSettingData.resetSettingData();
        cookerMode = COOKER_MODE_POWER_OFF;
    }

    @Override
    public void stopTimerForNoPan() {
        //tempGearValue = COOKER_INVALID_DATA;
        //fireGearValue = COOKER_INVALID_DATA;
        mTimerData.pauseCountdown();
        mTimerData.pauseFiveMinuteCountdown();
        saveCookeData();
    }

    @Override
    public void stopAllTimerAndResetDataForErrorOcurr() {
        tempGearValue = COOKER_INVALID_DATA;
        fireGearValue = COOKER_INVALID_DATA;
        mTimerData.stopCountdown();
        mTimerData.resetTimerValue();
        mTimerData.stopEightSecondCountdown();
        mTimerData.startFiveMinuteCountdown();
        int tempSensorStatus = SettingDbHelper.getTemperatureSensorStatus();
        if (tempSensorStatus != -1) {
            if (tempSensorStatus == id) SettingDbHelper.saveTemperatureSensorStatus(-1);
        }
        mCookerSettingData.resetSettingData();
        cookerMode = COOKER_MODE_POWER_OFF;
        saveCookeData();
    }

    @Override
    public void requestToCook() {
        setTempGearWithTimerValue(mCookerSettingData.getTempMode(),mCookerSettingData.getTempValue(),mCookerSettingData.getTempIdentifyDrawableResourceID(),mCookerSettingData.getTimerHourValue(),mCookerSettingData.getTimerMinuteValue(),mCookerSettingData.getTimerSecondValue());

        callback.onRequestToCook(mCookerSettingData);
        LogUtil.d("samhung-----requestToCook");
    }

    @Override
    public void saveCookeData() {

       if (fireGearValue == COOKER_INVALID_DATA) {
           if (tempGearValue == COOKER_INVALID_DATA) {//关机模式
               DatabaseHelper.saveHobData(id, InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_SETTING,0,0);
//

           }else {
               LogUtil.d("tempMode----->" + tempMode);
               if (tempMode == TEMP_MODE_FAST || tempMode == TEMP_MODE_FAST_WITHOUT_TEMPSENSOR) {
                   DatabaseHelper.saveHobData(id, InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FAST_TEMPERATURE,0,tempGearValue);

               }else if (tempMode == TEMP_MODE_PRECISE) {
                   DatabaseHelper.saveHobData(id, InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_PRECISE_TEMPERATURE,0,tempGearValue);

               }else {
                   DatabaseHelper.saveHobData(id, InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FAST_TEMPERATURE,0,tempGearValue);

               }

           }


       }else {
           DatabaseHelper.saveHobData(id, InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FIRE_GEAR,fireGearValue,0);
       }
    }

    @Override
    public boolean isCookerPowerOn() {
        return cookerMode != COOKER_MODE_POWER_OFF;
    }

    @Override
    public void pauseCookerTimer() {
        mTimerData.pauseCountdown();
    }

    @Override
    public void resumeCookerTimer() {
        mTimerData.resumeCountdown();
    }
}
