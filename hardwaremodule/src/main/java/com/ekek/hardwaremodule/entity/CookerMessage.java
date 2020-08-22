package com.ekek.hardwaremodule.entity;


import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.hardwaremodule.protocol.InductionCookerProtocol;

/**
 * Created by Samhung on 2018/1/9.
 */

public class CookerMessage {
    private int message,errorCode,tempValue;
    private boolean isHighTemp , hasCooker, isPanelHighTemp;

    public CookerMessage(int message, int errorCode,int tempValue) {
        this.message = message;
        this.errorCode = errorCode;
        this.tempValue = tempValue;
      //  LogUtil.d("tempvalue------>" + tempValue);
      //  LogUtil.d("message---EKEK--->" + message);
        parseCookerData();
    }

    public CookerMessage(int message, int errorCode) {
        this.message = message;
        this.errorCode = errorCode;
        parseCookerData();
    }

    private void parseCookerData() {
        if ((message & InductionCookerProtocol.PROTOCOL_FLAG_COOKER_HIGH_TEMPERATURE) == InductionCookerProtocol.PROTOCOL_FLAG_COOKER_HIGH_TEMPERATURE)isHighTemp = true;
        else isHighTemp = false;

        if ((message & InductionCookerProtocol.PROTOCOL_FLAG_COOKER_HAVE_COOKER_0X01) == InductionCookerProtocol.PROTOCOL_FLAG_COOKER_HAVE_COOKER_0X01||
                (message & InductionCookerProtocol.PROTOCOL_FLAG_COOKER_HAVE_COOKER_0X02) == InductionCookerProtocol.PROTOCOL_FLAG_COOKER_HAVE_COOKER_0X02 ||
                (message & InductionCookerProtocol.PROTOCOL_FLAG_COOKER_HAVE_COOKER_0X03) == InductionCookerProtocol.PROTOCOL_FLAG_COOKER_HAVE_COOKER_0X03){
            hasCooker = true;

        } else hasCooker = false;
       // LogUtil.d("the  panel status  ---EKEK--->" + hasCooker);
        if ((message & InductionCookerProtocol.PROTOCOL_FLAG_COOKER_PANEL_HIGH_TEMPERATURE) == InductionCookerProtocol.PROTOCOL_FLAG_COOKER_PANEL_HIGH_TEMPERATURE)isPanelHighTemp = true;
        else isPanelHighTemp = false;


    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public boolean isHighTemp() {
        return isHighTemp;
    }

    public void setHighTemp(boolean highTemp) {
        isHighTemp = highTemp;
    }

    public boolean isHasCooker() {
        return hasCooker;
    }

    public void setHasCooker(boolean hasCooker) {
        this.hasCooker = hasCooker;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int code) {
        errorCode = code;
    }

    public int getTempValue() {
        return tempValue;
    }

    public void setTempValue(int tempValue) {
        this.tempValue = tempValue;
    }

    public boolean isPanelHighTemp() {
        return isPanelHighTemp;
    }

    public void setPanelHighTemp(boolean panelHighTemp) {
        isPanelHighTemp = panelHighTemp;
    }
}
