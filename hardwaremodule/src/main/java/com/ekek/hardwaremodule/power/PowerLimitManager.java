package com.ekek.hardwaremodule.power;

import com.ekek.hardwaremodule.constants.CookerConstant;
import com.ekek.hardwaremodule.entity.AllCookerData;
import com.ekek.hardwaremodule.entity.CookerData;


public class PowerLimitManager {
    private static volatile PowerLimitManager instance;
    private static BaseCookerPowerData mCookerPowerData;
    private static int cookerType;

    private PowerLimitManager(int cookerType,int totalPower) {
        init(cookerType,totalPower);
    }

    public static PowerLimitManager getIstance(int cookerType,int totalPower) {
        if (instance == null) {
            synchronized (PowerLimitManager.class) {
                if (instance == null) {
                    instance = new PowerLimitManager(cookerType,totalPower);
                }
            }
        }else {


        }
        return instance;
    }

    private void init(int cookerType , int totalPower) {
        this.cookerType = cookerType;
        switch (cookerType) {
            case CookerConstant.COOKER_TYPE_KSO_60:
                mCookerPowerData = new KSO60CookerPowerData();
                break;
            case CookerConstant.COOKER_TYPE_KSO_80:
                mCookerPowerData = new KSO80CookerPowerData();
                break;
            case CookerConstant.COOKER_TYPE_KSO_90:
                mCookerPowerData = new KSO90CookerPowerData();
                break;
            case CookerConstant.COOKER_TYPE_KSO_AS:
                mCookerPowerData = new KSOASCookerPowerData();
                break;
            case CookerConstant.COOKER_TYPE_CATA_60:
                mCookerPowerData = new CATA60CookerPowerData(totalPower);

                break;
            case CookerConstant.COOKER_TYPE_CATA_80:
                mCookerPowerData = new CATA80CookerPowerData();
                break;
            case CookerConstant.COOKER_TYPE_CATA_90:
                mCookerPowerData = new CATA90CookerPowerData();
                break;
        }


    }

    public static int getCurrentTotalPower() {
        return mCookerPowerData.getCurrentTotalCookerPower();
    }

    public static int getCurrentTotalSingleLeftPower() {
        return mCookerPowerData.getCurrentTotalSingleLeftCookerPower();
    }

    public static int getCurrentTotalSingleRightPower() {
        return mCookerPowerData.getCurrentTotalSingleRightCookerPower();
    }

    public static int updateCookerPower(AllCookerData data) {
        return mCookerPowerData.updateCookerPower(data);

    }

    public static int checkPower(int value, CookerData... data) {
        return mCookerPowerData.checkCookerPower(value,data);
    }

    public static boolean setTotalPower(int power) {
        return mCookerPowerData.setCookerTotalPower(power);
    }

    public static int getTotalPower() {
        return mCookerPowerData.getTotalPower();
    }

    public static boolean checkPowerForDetectPan(AllCookerData data) {
        return mCookerPowerData.checkTotalPowerAvailable(data);
    }


}
