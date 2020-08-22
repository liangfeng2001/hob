package com.ekek.tfthobmodule.model;

import android.support.annotation.NonNull;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.hardwaremodule.entity.CookerHardwareResponse;
import com.ekek.hardwaremodule.entity.CookerMessage;
import com.ekek.hardwaremodule.event.CookerHardwareEvent;
import com.ekek.hardwaremodule.protocol.CookerErrorCode;
import com.ekek.tfthobmodule.R;
import com.ekek.tfthobmodule.constants.TFTHobConstant;
import com.ekek.tfthobmodule.data.CookerData;
import com.ekek.tfthobmodule.data.CookerSettingData;
import com.ekek.tfthobmodule.data.ICookerData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.google.common.base.Preconditions.checkNotNull;

public class HobPanelModelImpl implements HobPanelModel {

    private HobPanelModelCallback mCallback;
    private CookerData aCookerData,bCookerData,cCookerData;

    public HobPanelModelImpl(@NonNull HobPanelModelCallback callback) {
        mCallback = checkNotNull(callback, "callback cannot be null");
        init();
    }

    private void init() {
        aCookerData = new CookerData(TFTHobConstant.COOKER_TYPE_A_COOKER);
        aCookerData.setCookerDataCallback(new ICookerData.CookerDataCallback() {
            @Override
            public void onUpateTimer(int hour, int minute) {
                mCallback.onUpdateTimer(TFTHobConstant.COOKER_TYPE_A_COOKER,hour,minute);
            }

            @Override
            public void onTimeIsUp() {


                if (aCookerData.getCookerSettingData().getFireValue() == CookerSettingData.COOKER_SETTING_INVALID_VALUE) {
                    mCallback.notifyKeepWarmForTimeIsUp(TFTHobConstant.COOKER_TYPE_A_COOKER);
                }else {
                    mCallback.onTimeIsUp(TFTHobConstant.COOKER_TYPE_A_COOKER);
                }
                aCookerData.saveCookeData();
            }

            @Override
            public void onRequestUserPrepareToCook() {
                mCallback.notifyUserPrepareToCook(TFTHobConstant.COOKER_TYPE_A_COOKER,aCookerData.getCookerSettingData().getTempValue(),aCookerData.getCookerSettingData().getTempIdentifyDrawableResourceID());
            }

            @Override
            public void onRequestUserReadyToCook() {
                mCallback.notifyUserReadyToCook(TFTHobConstant.COOKER_TYPE_A_COOKER);
            }

            @Override
            public void onRequestToCook(CookerSettingData data) {
                mCallback.nitifyStartCook(data);
                //aCookerData.saveCookeData();
            }

            @Override
            public void onFireBGearTimeIsUp(CookerSettingData data) {
                mCallback.onFireBGearTimeIsUp(TFTHobConstant.COOKER_TYPE_A_COOKER,data);
                aCookerData.saveCookeData();
            }

            @Override
            public void onRecoverToCook(CookerSettingData data, int hour, int minute) {
                mCallback.notifyRecoverToCook(TFTHobConstant.COOKER_TYPE_A_COOKER,data,hour, minute);
            }
        });
        bCookerData = new CookerData(TFTHobConstant.COOKER_TYPE_B_COOKER);
        bCookerData.setCookerDataCallback(new ICookerData.CookerDataCallback() {
            @Override
            public void onUpateTimer(int hour, int minute) {
                mCallback.onUpdateTimer(TFTHobConstant.COOKER_TYPE_B_COOKER,hour,minute);
            }

            @Override
            public void onTimeIsUp() {

                if (bCookerData.getCookerSettingData().getFireValue() == CookerSettingData.COOKER_SETTING_INVALID_VALUE) {
                    mCallback.notifyKeepWarmForTimeIsUp(TFTHobConstant.COOKER_TYPE_B_COOKER);
                }else {
                    mCallback.onTimeIsUp(TFTHobConstant.COOKER_TYPE_B_COOKER);
                }
                bCookerData.saveCookeData();
            }

            @Override
            public void onRequestUserPrepareToCook() {
                mCallback.notifyUserPrepareToCook(TFTHobConstant.COOKER_TYPE_B_COOKER,bCookerData.getCookerSettingData().getTempValue(),bCookerData.getCookerSettingData().getTempIdentifyDrawableResourceID());
            }

            @Override
            public void onRequestUserReadyToCook() {
                mCallback.notifyUserReadyToCook(TFTHobConstant.COOKER_TYPE_B_COOKER);
            }

            @Override
            public void onRequestToCook(CookerSettingData data) {
                mCallback.nitifyStartCook(data);
                //bCookerData.saveCookeData();
            }

            @Override
            public void onFireBGearTimeIsUp(CookerSettingData data) {
                mCallback.onFireBGearTimeIsUp(TFTHobConstant.COOKER_TYPE_B_COOKER,data);
                bCookerData.saveCookeData();
            }

            @Override
            public void onRecoverToCook(CookerSettingData data, int hour, int minute) {
                mCallback.notifyRecoverToCook(TFTHobConstant.COOKER_TYPE_B_COOKER,data,hour, minute);
            }
        });
        cCookerData = new CookerData(TFTHobConstant.COOKER_TYPE_C_COOKER);
        cCookerData.setCookerDataCallback(new ICookerData.CookerDataCallback() {
            @Override
            public void onUpateTimer(int hour, int minute) {
                //LogUtil.d("HobPanelModelImpl onUpateTimer");
                mCallback.onUpdateTimer(TFTHobConstant.COOKER_TYPE_C_COOKER,hour,minute);
            }

            @Override
            public void onTimeIsUp() {
                if (cCookerData.getCookerSettingData().getFireValue() == CookerSettingData.COOKER_SETTING_INVALID_VALUE) {
                    mCallback.notifyKeepWarmForTimeIsUp(TFTHobConstant.COOKER_TYPE_C_COOKER);
                }else {
                    mCallback.onTimeIsUp(TFTHobConstant.COOKER_TYPE_C_COOKER);
                }
                cCookerData.saveCookeData();
            }

            @Override
            public void onRequestUserPrepareToCook() {
                mCallback.notifyUserPrepareToCook(TFTHobConstant.COOKER_TYPE_C_COOKER,cCookerData.getCookerSettingData().getTempValue(),cCookerData.getCookerSettingData().getTempIdentifyDrawableResourceID());
            }

            @Override
            public void onRequestUserReadyToCook() {
                mCallback.notifyUserReadyToCook(TFTHobConstant.COOKER_TYPE_C_COOKER);
            }

            @Override
            public void onRequestToCook(CookerSettingData data) {
//                LogUtil.d("samhung---Enter:: onRequestToCook");
                mCallback.nitifyStartCook(data);
                //cCookerData.saveCookeData();
            }

            @Override
            public void onFireBGearTimeIsUp(CookerSettingData data) {
                mCallback.onFireBGearTimeIsUp(TFTHobConstant.COOKER_TYPE_C_COOKER,data);
                cCookerData.saveCookeData();
            }

            @Override
            public void onRecoverToCook(CookerSettingData data, int hour, int minute) {
                mCallback.notifyRecoverToCook(TFTHobConstant.COOKER_TYPE_C_COOKER,data,hour, minute);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CookerHardwareEvent event) {
        if (!GlobalVars.getInstance().isInitializeProcessComplete()) return;

        CookerHardwareResponse response = event.getResponse();
        processHardwareData(response);

    }
    private long aTimeForNoPan,bTimeForNoPan,cTimeForNoPan;
    private void processHardwareData(CookerHardwareResponse response) {
        //LogUtil.d("Enter:: processHardwareData");
        if (response.getPowerSwitchState() == CookerHardwareResponse.POWER_SWITCH_ON) {
            CookerMessage aCookerMessage = response.getaCookerMessage();
            int aErrorCode = aCookerMessage.getErrorCode();
            if (aErrorCode > CookerErrorCode.COOKER_ERROR_NORMAL) {//have error , 1. 停止炉头定时器；2. 通知炉头显示错误信息

                aCookerData.stopAllTimerAndResetDataForErrorOcurr();
                mCallback.onCookerErrorOcurr(TFTHobConstant.COOKER_TYPE_A_COOKER,CookerErrorCode.getErrorMessage(aErrorCode));

            }else {//no error ,1. 恢复炉头定时器工作；2. 通知炉头恢复工作显示

                if (!aCookerMessage.isHasCooker()) {
                    if (aCookerData.isCookerPowerOn()) {
                        if (aTimeForNoPan == 0) aTimeForNoPan = System.currentTimeMillis();
                        long dat = System.currentTimeMillis() - aTimeForNoPan;
                        LogUtil.d("enter:: time out-----a---->" + (System.currentTimeMillis() - aTimeForNoPan));
                        if (dat >= 5000 && dat < 1000 * 60) {
                            mCallback.onCookerNoPan(TFTHobConstant.COOKER_TYPE_A_COOKER);
                            aCookerData.stopTimerForNoPan();
                        }else if (dat >= 1000 * 60) {
                            stopTimerForPoweroff(TFTHobConstant.COOKER_TYPE_A_COOKER);
                            mCallback.onTimeIsUp(TFTHobConstant.COOKER_TYPE_A_COOKER);
                        }



                    }else {//高温和无锅同时存在，如果关机则需要通知高温
                        if (aCookerMessage.isHighTemp()) {
                            mCallback.onCookerHighTemp(TFTHobConstant.COOKER_TYPE_A_COOKER);
                        }else {
                            mCallback.onCookerErrorDimiss(TFTHobConstant.COOKER_TYPE_A_COOKER);
                        }
                    }
                    //LogUtil.d("Enter:: COOKER_ERROR_CODE_NO_COOKER");

                }else if (aCookerMessage.isHighTemp()){//判断是否高温
                    aTimeForNoPan = 0;
                    if (!aCookerData.isCookerPowerOn()) {
                        mCallback.onCookerHighTemp(TFTHobConstant.COOKER_TYPE_A_COOKER);
                    }else {//高温但开机，则继续工作

                        mCallback.onCookerErrorDimiss(TFTHobConstant.COOKER_TYPE_A_COOKER);
                        setCookerSettingData(aCookerData.getCookerSettingData());
                    }
                    //LogUtil.d("Enter:: COOKER_ERROR_CODE_HIGH_TEMP");
                }else {
                    aTimeForNoPan = 0;
                    if (aCookerData.isCookerPowerOn()) {
                        mCallback.onCookerErrorDimiss(TFTHobConstant.COOKER_TYPE_A_COOKER);
                        //setCookerSettingData(aCookerData.getCookerSettingData());
                        aCookerData.recoverToWork();
                    }else {//恢复错误
                        mCallback.onCookerErrorDimiss(TFTHobConstant.COOKER_TYPE_A_COOKER);
                    }
                }

            }

            //////////////////////////////////B cooker///////////////////////////////////////////////
            CookerMessage bCookerMessage = response.getbCookerMessage();
            int bErrorCode = bCookerMessage.getErrorCode();
            if (bErrorCode > CookerErrorCode.COOKER_ERROR_NORMAL) {//have error , 1. 停止炉头定时器；2. 通知炉头显示错误信息

                bCookerData.stopAllTimerAndResetDataForErrorOcurr();
                mCallback.onCookerErrorOcurr(TFTHobConstant.COOKER_TYPE_B_COOKER,CookerErrorCode.getErrorMessage(bErrorCode));

            }else {//no error ,1. 恢复炉头定时器工作；2. 通知炉头恢复工作显示

                if (!bCookerMessage.isHasCooker()) {
                    if (bCookerData.isCookerPowerOn()) {
                        if (bTimeForNoPan == 0) bTimeForNoPan = System.currentTimeMillis();
                        long dbt = System.currentTimeMillis() - bTimeForNoPan;
                        LogUtil.d("enter:: time out-----b---->" + (System.currentTimeMillis() - bTimeForNoPan));
                        if (dbt >= 5000 && dbt < 1000 * 60) {
                            mCallback.onCookerNoPan(TFTHobConstant.COOKER_TYPE_B_COOKER);
                            bCookerData.stopTimerForNoPan();
                        }else if (dbt >= 1000 * 60) {
                            stopTimerForPoweroff(TFTHobConstant.COOKER_TYPE_B_COOKER);
                            mCallback.onTimeIsUp(TFTHobConstant.COOKER_TYPE_B_COOKER);
                        }



                    }else {//高温和无锅同时存在，如果关机则需要通知高温
                        if (bCookerMessage.isHighTemp()) {
                            mCallback.onCookerHighTemp(TFTHobConstant.COOKER_TYPE_B_COOKER);
                        }else {
                            mCallback.onCookerErrorDimiss(TFTHobConstant.COOKER_TYPE_B_COOKER);
                        }
                    }
                    //LogUtil.d("Enter:: COOKER_ERROR_CODE_NO_COOKER");

                }else if (bCookerMessage.isHighTemp()){//判断是否高温
                    bTimeForNoPan = 0;
                    if (!bCookerData.isCookerPowerOn()) {
                        mCallback.onCookerHighTemp(TFTHobConstant.COOKER_TYPE_B_COOKER);
                    }else {//高温但开机，则继续工作

                        mCallback.onCookerErrorDimiss(TFTHobConstant.COOKER_TYPE_B_COOKER);
                        setCookerSettingData(bCookerData.getCookerSettingData());
                    }
                    //LogUtil.d("Enter:: COOKER_ERROR_CODE_HIGH_TEMP");
                }else {
                    bTimeForNoPan = 0;
                    if (bCookerData.isCookerPowerOn()) {
                        mCallback.onCookerErrorDimiss(TFTHobConstant.COOKER_TYPE_B_COOKER);
                        //setCookerSettingData(bCookerData.getCookerSettingData());
                        bCookerData.recoverToWork();
                    }else {//恢复错误
                        mCallback.onCookerErrorDimiss(TFTHobConstant.COOKER_TYPE_B_COOKER);
                    }
                }

            }
            //////////////////////////////////C cooker///////////////////////////////////////////////
            CookerMessage cCookerMessage = response.getcCookerMessage();
            int cErrorCode = cCookerMessage.getErrorCode();
            if (cErrorCode > CookerErrorCode.COOKER_ERROR_NORMAL) {//have error , 1. 停止炉头定时器；2. 通知炉头显示错误信息

                cCookerData.stopAllTimerAndResetDataForErrorOcurr();
                mCallback.onCookerErrorOcurr(TFTHobConstant.COOKER_TYPE_C_COOKER,CookerErrorCode.getErrorMessage(cErrorCode));

            }else {//no error ,1. 恢复炉头定时器工作；2. 通知炉头恢复工作显示
                if (!cCookerMessage.isHasCooker()) {
                    if (cCookerData.isCookerPowerOn()) {
                        if (cTimeForNoPan == 0) cTimeForNoPan = System.currentTimeMillis();
                        LogUtil.d("enter:: time out-----c---->" + (System.currentTimeMillis() - cTimeForNoPan));
                        long dct = System.currentTimeMillis() - cTimeForNoPan;
                        if ( dct >= 5000 && dct < 1000 * 60) {
                            mCallback.onCookerNoPan(TFTHobConstant.COOKER_TYPE_C_COOKER);
                            cCookerData.stopTimerForNoPan();
                        }else if (dct >= 1000 * 60) {
                            stopTimerForPoweroff(TFTHobConstant.COOKER_TYPE_C_COOKER);
                            mCallback.onTimeIsUp(TFTHobConstant.COOKER_TYPE_C_COOKER);
                        }
                    }else {//高温和无锅同时存在，如果关机则需要通知高温
                        if (cCookerMessage.isHighTemp()) {
                            mCallback.onCookerHighTemp(TFTHobConstant.COOKER_TYPE_C_COOKER);
                        }else {
                            mCallback.onCookerErrorDimiss(TFTHobConstant.COOKER_TYPE_C_COOKER);
                        }
                    }
                    //LogUtil.d("Enter:: COOKER_ERROR_CODE_NO_COOKER");
                }else if (cCookerMessage.isHighTemp()){//判断是否高温
                    cTimeForNoPan = 0;
                    if (!cCookerData.isCookerPowerOn()) {
                        mCallback.onCookerHighTemp(TFTHobConstant.COOKER_TYPE_C_COOKER);
                    }else {//高温但开机，则继续工作
                        mCallback.onCookerErrorDimiss(TFTHobConstant.COOKER_TYPE_C_COOKER);
                        setCookerSettingData(cCookerData.getCookerSettingData());
                    }
                    //LogUtil.d("Enter:: COOKER_ERROR_CODE_HIGH_TEMP");
                }else {
                    cTimeForNoPan = 0;
                    if (cCookerData.isCookerPowerOn()) {
                        LogUtil.d("samhung---no error data");
                        mCallback.onCookerErrorDimiss(TFTHobConstant.COOKER_TYPE_C_COOKER);
                        //setCookerSettingData(cCookerData.getCookerSettingData());
                        cCookerData.recoverToWork();
                    }else {//恢复错误
                        mCallback.onCookerErrorDimiss(TFTHobConstant.COOKER_TYPE_C_COOKER);
                    }
                }
            }
        }else {//关机所有数据清零
            stopTimerForPoweroff(TFTHobConstant.COOKER_TYPE_A_COOKER);
            mCallback.onTimeIsUp(TFTHobConstant.COOKER_TYPE_A_COOKER);
            stopTimerForPoweroff(TFTHobConstant.COOKER_TYPE_B_COOKER);
            mCallback.onTimeIsUp(TFTHobConstant.COOKER_TYPE_B_COOKER);
            stopTimerForPoweroff(TFTHobConstant.COOKER_TYPE_C_COOKER);
            mCallback.onTimeIsUp(TFTHobConstant.COOKER_TYPE_C_COOKER);
        }
    }


    @Override
    public void start() {
        registerEventBus();
    }

    @Override
    public void stop() {
        unregisterEventBus();
    }


    private void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void unregisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this);
    }


    @Override
    public CookerSettingData getCookerSettingData(int cookerID) {
        switch (cookerID) {
            case TFTHobConstant.COOKER_TYPE_A_COOKER:

                return aCookerData.getCookerSettingData();
            case TFTHobConstant.COOKER_TYPE_B_COOKER:

                return bCookerData.getCookerSettingData();
            case TFTHobConstant.COOKER_TYPE_C_COOKER:

                return cCookerData.getCookerSettingData();

        }
        return null;
    }

    @Override
    public CookerSettingData getCookerSettingDataForKeepWarm(int cookerID) {
        switch (cookerID) {
            case TFTHobConstant.COOKER_TYPE_A_COOKER:

                return makeCookerSettingDataForKeepWarm(aCookerData.getCookerSettingData());
            case TFTHobConstant.COOKER_TYPE_B_COOKER:

                return makeCookerSettingDataForKeepWarm(bCookerData.getCookerSettingData());
            case TFTHobConstant.COOKER_TYPE_C_COOKER:

                return makeCookerSettingDataForKeepWarm(cCookerData.getCookerSettingData());

        }
        return null;
    }

    private CookerSettingData makeCookerSettingDataForKeepWarm(CookerSettingData data) {
        data.resetTimerData();
        data.setTempValue(70);
        data.setTempIdentifyDrawableResourceID(R.mipmap.temp_identification_keep_warm);
        return data;
    }

    @Override
    public CookerSettingData getCookerSettingDataForAddTemMinute(int cookerID) {
        switch (cookerID) {
            case TFTHobConstant.COOKER_TYPE_A_COOKER:

                return makeCookerSettingDataForAddTemMinute(aCookerData.getCookerSettingData());
            case TFTHobConstant.COOKER_TYPE_B_COOKER:

                return makeCookerSettingDataForAddTemMinute(bCookerData.getCookerSettingData());
            case TFTHobConstant.COOKER_TYPE_C_COOKER:

                return makeCookerSettingDataForAddTemMinute(cCookerData.getCookerSettingData());

        }
        return null;
    }

    private CookerSettingData makeCookerSettingDataForAddTemMinute(CookerSettingData data) {
        data.setTimer(0,10);

        return data;
    }

    @Override
    public void setCookerSettingData(CookerSettingData data) {
        switch (data.getCookerID()) {
            case TFTHobConstant.COOKER_TYPE_A_COOKER:
                aCookerData.setCookerSettingData(data);
                aCookerData.saveCookeData();
                break;
            case TFTHobConstant.COOKER_TYPE_B_COOKER:
                bCookerData.setCookerSettingData(data);
                bCookerData.saveCookeData();
                break;
            case TFTHobConstant.COOKER_TYPE_C_COOKER:
                LogUtil.d("Enter:: c------->setCookerSettingData");
                cCookerData.setCookerSettingData(data);
                cCookerData.saveCookeData();
                break;
        }

    }


    @Override
    public void stopTimerForPoweroff(int cookerID) {
        switch (cookerID) {
            case TFTHobConstant.COOKER_TYPE_A_COOKER:
                aTimeForNoPan = 0;
                aCookerData.stopTimerForPoweroff();
                aCookerData.saveCookeData();
                break;
            case TFTHobConstant.COOKER_TYPE_B_COOKER:
                bTimeForNoPan = 0;
                bCookerData.stopTimerForPoweroff();
                bCookerData.saveCookeData();
                break;
            case TFTHobConstant.COOKER_TYPE_C_COOKER:
                cTimeForNoPan = 0;
                cCookerData.stopTimerForPoweroff();
                cCookerData.saveCookeData();
                break;
        }

    }

    @Override
    public void requestToCook(int cookerID) {
        switch (cookerID) {
            case TFTHobConstant.COOKER_TYPE_A_COOKER:
                aCookerData.requestToCook();
                aCookerData.saveCookeData();
                break;
            case TFTHobConstant.COOKER_TYPE_B_COOKER:
                bCookerData.requestToCook();
                bCookerData.saveCookeData();
                break;
            case TFTHobConstant.COOKER_TYPE_C_COOKER:
                cCookerData.requestToCook();
                cCookerData.saveCookeData();
                break;
        }
    }


}
