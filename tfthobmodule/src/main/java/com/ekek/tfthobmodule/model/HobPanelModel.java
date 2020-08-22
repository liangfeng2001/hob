package com.ekek.tfthobmodule.model;

import com.ekek.commonmodule.base.BaseModel;
import com.ekek.commonmodule.base.BaseModelCallback;
import com.ekek.tfthobmodule.data.CookerSettingData;

public interface HobPanelModel extends BaseModel {

    CookerSettingData getCookerSettingData(int cookerID);

    CookerSettingData getCookerSettingDataForKeepWarm(int cookerID);

    CookerSettingData getCookerSettingDataForAddTemMinute(int cookerID);

    void setCookerSettingData(CookerSettingData data);

    void stopTimerForPoweroff(int cookerID);

    void requestToCook(int cookerID);

    interface HobPanelModelCallback extends BaseModelCallback {
        /**
         * HobIntroModel通知UI更新锅的状态显示
         * */
        void onHobStatusChange(int status);
        /**
         * HobIntroModel通知电磁炉开机
         * */
        void onHobPowerOn();

        void onUpdateTimer(int cookerID,int hour,int minute);

        void onTimeIsUp(int cookerID);

        void notifyKeepWarmForTimeIsUp(int cookerID);

        void notifyUserPrepareToCook(int cookerID,int tempValue,int identifyResourceID);

        void notifyUserReadyToCook(int cookerID);

        void nitifyStartCook(CookerSettingData data);

        void notifyRecoverToCook(int cookerID,CookerSettingData data,int hour,int minute);

        void onFireBGearTimeIsUp(int cookerID,CookerSettingData data);

        void onCookerErrorOcurr(int cookerID, String errorMessage);

        void onCookerErrorDimiss(int cookerID);

        void onCookerHighTemp(int cookerID);

        void onCookerNoPan(int cookerID);



    }

}
