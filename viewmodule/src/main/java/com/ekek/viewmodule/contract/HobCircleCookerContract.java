package com.ekek.viewmodule.contract;

public interface HobCircleCookerContract {
    /**
    * 获取Cooker ID
    * */
    int getCookerID();

    void setCookerID(int id);

    public void setOnHobCircleCookerListener(OnHobCircleCookerListener listener);

    public void updateCookerView(int workMode, int fireValue, int tempValue, int realTempValue,int hourValue, int minuteValue, int tempIndicatorID, int recipesID,int recipesShowOrder,String errorMessage);

    interface OnHobCircleCookerListener {
        void onCookerPowerOff();

        void onCookerPowerOn();

        void onSetGear();

        void onSetTimer();

        void onReadyToCook();

        void onRequestAddTenMinute();

        void onRequestKeepWarm();
    }

}
