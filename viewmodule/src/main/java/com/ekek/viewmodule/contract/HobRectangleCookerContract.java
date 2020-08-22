package com.ekek.viewmodule.contract;

public interface HobRectangleCookerContract {
    /**
     * 获取Cooker ID
     * */
    int[] getCookerIDs();

    public void setOnHobRectangleCookerListener(OnHobRectangleCookerListener listener);

    public void updateCookerView(int cookerID,int workMode, int fireValue, int tempValue, int realTempValue, int hourValue, int minuteValue, int tempIndicatorID, int recipesID,String errorMessage);

    public void updateUnionCookerView(int unionCookerID,
                                      int upCookerID, int upWorkMode,int upFireValue,int upTempValue,int upHourValue,int upMinute,int upTempIndicatorID,int upRecipesID,String upErrorMessage,
                                      int downCookerID, int downWorkMode,int downFireValue,int downTempValue,int downHourValue,int downMinute,int downTempIndicatorID,int downRecipesID,String downErrorMessage

    );

    public void cookerPoweroff();

    interface OnHobRectangleCookerListener {
        void onCookerPowerOff(int cookerID);

        void onCookerPowerOn(int cookerID);

        void onSetGear(int cookerID);

        void onSetTimer(int cookerID);

        void onReadyToCook(int cookerID);

        void onRequestAddTenMinute(int cookerID);

        void onRequestKeepWarm(int cookerID);
    }
}
