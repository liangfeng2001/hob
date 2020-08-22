package com.ekek.tfthobmodule.data;

import android.support.annotation.NonNull;

public interface ICookerData {

    void setCookerDataCallback(@NonNull CookerDataCallback callback);

    void stopTimerForPoweroff();

    void stopTimerForNoPan();

    void stopAllTimerAndResetDataForErrorOcurr();

    void requestToCook();



    void saveCookeData();

    boolean isCookerPowerOn();

    void pauseCookerTimer();

    void resumeCookerTimer();

    interface CookerDataCallback {

        void onUpateTimer(int hour,int minute);

        void onTimeIsUp();

        void onRequestUserPrepareToCook();

        void onRequestUserReadyToCook();

        void onRequestToCook(CookerSettingData data);

        void onFireBGearTimeIsUp(CookerSettingData data);

        void onRecoverToCook(CookerSettingData data,int hour,int minute);


    }
}
