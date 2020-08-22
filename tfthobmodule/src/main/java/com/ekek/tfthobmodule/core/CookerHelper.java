package com.ekek.tfthobmodule.core;

import android.content.Context;

import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.hardwaremodule.entity.CookerHardwareResponse;
import com.ekek.tfthobmodule.constants.TFTHobConstant;
import com.ekek.tfthobmodule.data.CookerSettingData;
import com.ekek.tfthobmodule.database.SettingDbHelper;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 管理所有炉头工作
 * */
public class CookerHelper {
    private CopyOnWriteArrayList<CookerManager> cookerManagerList;
    public CookerHelper() {
        cookerManagerList = new CopyOnWriteArrayList<>();
    }

    public void bindCooker(int cookerID , Context context) {
        cookerManagerList.add(new CookerManager(cookerID,context));
        LogUtil.d("bindCookerID---->" + cookerID);
        //cookerManagerList.add(cookerID,new CookerManager(cookerID));
    }

    public void notifyUpdateCookerMessage(CookerHardwareResponse response) {
        for (CookerManager cookerManager : cookerManagerList) {
            cookerManager.notifyUpdateCookerMessage(response);
        }
    }

    public void notifyUpdateCookerSettingData(CookerSettingData data) {
        int cookerID = data.getCookerID();
        for (CookerManager cookerManager : cookerManagerList) {
            if (cookerManager.getCookerID() == cookerID) {
                cookerManager.notifyUpdateCookerData(data);
                if (data.getTempMode() == TFTHobConstant.TEMP_MODE_PRECISE_TEMP)   {
                    SettingDbHelper.saveTemperatureSensorStatus(cookerID);
                }
            }

        }
        notifyOtherCookerUpdateUI(cookerID);


    }

    public void notifyCookerPowerOff(int cookerID) {
        for (CookerManager cookerManager : cookerManagerList) {
            if (cookerManager.getCookerID() == cookerID) cookerManager.notifyCookerPowerOff();
        }
        notifyOtherCookerUpdateUI(cookerID);
    }

    public void notifyCookerReadyToCook(int cookerID) {

        for (CookerManager cookerManager : cookerManagerList) {
            if (cookerManager.getCookerID() == cookerID) cookerManager.notifyCookerReadyToCook();
        }

        notifyOtherCookerUpdateUI(cookerID);

    }

    public void notifyCookerKeepWarm(int cookerID) {
        for (CookerManager cookerManager : cookerManagerList) {
            if (cookerManager.getCookerID() == cookerID) cookerManager.notifyCookerKeepWarm();
        }

        notifyOtherCookerUpdateUI(cookerID);
    }

    public void notifyCookerAddTenMinute(int cookerID) {

        for (CookerManager cookerManager : cookerManagerList) {
            if (cookerManager.getCookerID() == cookerID) cookerManager.notifyCookerAddTenMinute();
        }

        notifyOtherCookerUpdateUI(cookerID);
    }

    public void notifyCookerPause() {
        notifyCookerPause(-1);
    }
    public void notifyCookerPause(int cookerId) {
        for (CookerManager cookerManager : cookerManagerList) {
            if (cookerId == -1 || cookerId == cookerManager.getCookerID()) {
                cookerManager.notifyCookerPause();
            }
        }
    }

    public void notifyCookerPlay() {
        notifyCookerPlay(-1);
    }

    public void notifyCookerPlay(int cookerId) {
        for (CookerManager cookerManager : cookerManagerList) {
            if (cookerId == -1 || cookerId == cookerManager.getCookerID()) {
                cookerManager.notifyCookerPlay();
            }
        }
    }

    public void notifyCookerTempSensorReady() {
        int cookerID = SettingDbHelper.getTemperatureSensorStatus();

        for (CookerManager cookerManager : cookerManagerList) {
            if (cookerManager.getCookerID() == cookerID) cookerManager.notifyCookerTempSensorReady();
        }

        notifyOtherCookerUpdateUI(cookerID);
    }

    private void notifyOtherCookerUpdateUI(int cookerID) {
        for (CookerManager cookerManager : cookerManagerList) {
            if (cookerID != TFTHobConstant.COOKER_TYPE_AB_COOKER && cookerID != TFTHobConstant.COOKER_TYPE_EF_COOKER) {
                if (cookerManager.getCookerID() != cookerID ) {

                    cookerManager.notifyForceUpdateCookerUI();

                }
            }


        }
    }
    
}
