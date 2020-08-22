package com.ekek.hardwaremodule.pid;

import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.hardwaremodule.constants.HardwareConstant;
import com.ekek.hardwaremodule.entity.CookerData;
import com.ekek.hardwaremodule.power.PowerLimitManager;
import com.ekek.hardwaremodule.protocol.InductionCookerProtocol;

public class FastTempCtrlStrategyManager {
    private static final int WORK_PROGRESS_HEAT_WATER = 4;//加热水
    private static final int WORK_PROGRESS_HEAT_OID = 5;//加热油
    private static final int WORK_PROGRESS_KEEP_WARM_WATER = 6;//水保温
    private static final int WORK_PROGRESS_KEEP_WARM_OIL = 7;//油保温

    private int cookerType;//炉头类型
    private int t0;//温度初始值
    private int tTarget;//目标温度
    int powerLevel = 0;
    private int workProgress = WORK_PROGRESS_HEAT_WATER;
    private PowerLimitManager mPowerLimitManager;
    public FastTempCtrlStrategyManager(int cookerType , int initTemp , int targetTemp) {
        this.cookerType = cookerType;
        t0 = initTemp;
        tTarget = targetTemp;

    }

    public int getPowerLevel(int currentTemp) {
        switch (workProgress) {
            case WORK_PROGRESS_HEAT_WATER:
                int delta = tTarget - currentTemp;
                if (delta <= 10) {
                    if (delta <= 0) {
                        if (delta < 0) {
                            powerLevel = getPower(0);
                        }else if (delta == 0) {
                            powerLevel = getPower(1);
                        }

                    }else if(delta <= 1) {
                        if (cookerType == HardwareConstant.COOKER_TYPE_A_COOKER) {
                            powerLevel = getPower(5);
                        }else if (cookerType == HardwareConstant.COOKER_TYPE_B_COOKER) {
                            powerLevel = getPower(4);//1
                        }else if (cookerType == HardwareConstant.COOKER_TYPE_AB_COOKER) {
                            powerLevel = getPower(5);//1
                        }else if (cookerType == HardwareConstant.COOKER_TYPE_C_COOKER) {
                            powerLevel = getPower(6);
                        }else if (cookerType == HardwareConstant.COOKER_TYPE_D_COOKER) {
                            powerLevel = getPower(6);
                        }else if (cookerType == HardwareConstant.COOKER_TYPE_E_COOKER) {
                            powerLevel = getPower(5);
                        }else if (cookerType == HardwareConstant.COOKER_TYPE_F_COOKER) {
                            powerLevel = getPower(4);//1
                        }else if (cookerType == HardwareConstant.COOKER_TYPE_EF_COOKER) {
                            powerLevel = getPower(5);//1
                        }


                    }else if(delta <= 3) {
                        if (cookerType == HardwareConstant.COOKER_TYPE_A_COOKER) {
                            powerLevel = getPower(7);
                        }else if (cookerType == HardwareConstant.COOKER_TYPE_B_COOKER) {
                            powerLevel = getPower(6);//2
                        }else if (cookerType == HardwareConstant.COOKER_TYPE_AB_COOKER) {
                            powerLevel = getPower(7);//2
                        }else if (cookerType == HardwareConstant.COOKER_TYPE_C_COOKER) {
                            powerLevel = getPower(8);
                        }else if (cookerType == HardwareConstant.COOKER_TYPE_D_COOKER) {
                            powerLevel = getPower(8);
                        }else if (cookerType == HardwareConstant.COOKER_TYPE_E_COOKER) {
                            powerLevel = getPower(7);
                        }else if (cookerType == HardwareConstant.COOKER_TYPE_F_COOKER) {
                            powerLevel = getPower(6);//1
                        }else if (cookerType == HardwareConstant.COOKER_TYPE_EF_COOKER) {
                            powerLevel = getPower(7);//1
                        }


                    }else {
                        if (tTarget <= 80) {
                            powerLevel = getPower(7);
                        }else {
                            powerLevel = getPower(8);
                        }

                    }

                }else {
                    if (tTarget <= 80) {
                        powerLevel = getPower(7);
                    }else {
                        powerLevel = getPower(8);
                    }

                }
                LogUtil.d("FastTempTest---tTarget--->" + tTarget + "---currentTemp--->" + currentTemp + "----deltaTmep---->" + delta + "----powerLevel---->" + powerLevel);

                break;
            case WORK_PROGRESS_KEEP_WARM_WATER:

                break;
        }
        return powerLevel;
    }

    private int getPower(int level) {
        CookerData cookerData = new CookerData(cookerType, InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FIRE_GEAR);
        int returnPowerValue = mPowerLimitManager.checkPower(level,cookerData);
        return returnPowerValue;
    }

    public int getTargetTemp() {
        return tTarget;
    }

    public void setTartgetTemp(int temp) {
        this.tTarget = temp;
    }

    public void recyle() {

    }
}
