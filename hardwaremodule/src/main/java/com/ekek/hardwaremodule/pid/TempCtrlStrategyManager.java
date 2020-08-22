package com.ekek.hardwaremodule.pid;

import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.hardwaremodule.constants.HardwareConstant;
import com.ekek.hardwaremodule.entity.CookerData;
import com.ekek.hardwaremodule.power.PowerLimitManager;
import com.ekek.hardwaremodule.protocol.InductionCookerProtocol;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TempCtrlStrategyManager {



    private static final int WORK_PROGRESS_STATE_IDENTIFY_LIQUID_HEAT_LIQUID = 0;//2kw加热
    private static final int WORK_PROGRESS_STATE_IDENTIFY_LIQUID_WAIT_LIQUID_RISE = 1;//停止加热等待液体温度上升
    private static final int WORK_PROGRESS_STATE_FURTHER_IDENTIFY_WAIT_LIQUID_RISE = 2;//进一步确认,不加热等待12s
    private static final int WORK_PROGRESS_STATE_FURTHER_IDENTIFY_WAIT_LIQUID_RISE_SECOND = 3;//进一步确认，不加热等待10s
    private static final int WORK_PROGRESS_HEAT_WATER = 4;//加热水
    private static final int WORK_PROGRESS_HEAT_OID = 5;//加热油
    private static final int WORK_PROGRESS_KEEP_WARM_WATER = 6;//水保温
    private static final int WORK_PROGRESS_KEEP_WARM_OIL = 7;//油保温

    private static final long STATE_IDENTIFY_LIQUID_HEAT_LIQUID_TIME = 30 * 1000;//17s
    private static final long STATE_IDENTIFY_LIQUID_WAIT_LIQUID_RISE_TIME = 7 * 1000;//7s

    private static final int LIQUID_TYPE_UNKNOWN = -1;
    private static final int LIQUID_TYPE_WATER = 0;
    private static final int LIQUID_TYPE_OIL = 1;

    public static final int TEMP_MODE_FAST_TEMP = 2;
    public static final int TEMP_MODE_PRECISE_TEMP = 3;

    private int cookerType;//炉头类型
    private int t0;//温度初始值
    private int t3;
    private int tTarget;//目标温度
    private int workProgress = WORK_PROGRESS_STATE_IDENTIFY_LIQUID_HEAT_LIQUID;
    private long lastTime = 0;
    private int liquidType = LIQUID_TYPE_UNKNOWN;
    private long timeForHeat = STATE_IDENTIFY_LIQUID_HEAT_LIQUID_TIME;
    private int powerForHeat = 10;

    private int timeForCalTrend = 0;
    private long timeForLiquidRise = 8 * 1000;
    private int error;//current error
    private int lastError;//last error
    private int preError;//last last error
    private int sumError;//error sum
    private int delError;
    private int tempMode;

    public TempCtrlStrategyManager(int cookerType ,int tempMode , int initTemp , int targetTemp) {
        LogUtil.d("Enter:: TempCtrlStrategyManager-----ttartget--->" + targetTemp + "----initTemp--->" + initTemp);
        this.cookerType = cookerType;
        this.tempMode = tempMode;
        t0 = initTemp;
        tTarget = targetTemp;
        error = targetTemp - initTemp;
        lastError = error;
        timeForCalTrend = 0;
        if (cookerType == HardwareConstant.COOKER_TYPE_A_COOKER) {
            timeForHeat = 17 * 1000;
            powerForHeat = 10;
            timeForLiquidRise = 8 * 1000;
        }else if (cookerType == HardwareConstant.COOKER_TYPE_B_COOKER) {
            timeForHeat = 13 * 1000;
            powerForHeat = 10;
            timeForLiquidRise = 8 * 1000;
        }else if (cookerType == HardwareConstant.COOKER_TYPE_C_COOKER) {
            timeForHeat = 40 * 1000;
            powerForHeat = 10;
            timeForLiquidRise = 8 * 1000;
        }

        processCookerWork();
        doStartFiveMinuteCountDown();
    }

    private void processCookerWork() {
        switch (workProgress) {
            case WORK_PROGRESS_STATE_IDENTIFY_LIQUID_HEAT_LIQUID:
                lastTime = System.currentTimeMillis();
                if (t0 >= tTarget) {
                    workProgress = WORK_PROGRESS_KEEP_WARM_OIL;
                    liquidType = LIQUID_TYPE_OIL;
                }
                break;
        }
    }
    private String liquidTypeStr = "None";
    private int counterForIndentify = 0;
    private int counterForIndentifyWater = 0;
    private int counterForHeat = 0;
    private int counterForIntermittenceHeat = 0;
    public int getPowerLevel(int currentTemp) {
       // long deltaTime = System.currentTimeMillis() - timeForCalTrend;
        LogUtil.d("Enter:: getPowerLevel----currentTemp--->" + currentTemp + "-----tTarget--->" + tTarget + "---t0---->" + t0);
        //LogUtil.d("TEMPTest---timeForCalTrend---->" + timeForCalTrend);
        if (timeForCalTrend >= 6) {
           // timeForCalTrend = System.currentTimeMillis();
            error = tTarget - currentTemp;
            delError = error - lastError;
            preError = lastError;
            lastError = error;
            if (workProgress == WORK_PROGRESS_HEAT_WATER || workProgress == WORK_PROGRESS_HEAT_OID || workProgress == WORK_PROGRESS_KEEP_WARM_OIL || workProgress == WORK_PROGRESS_KEEP_WARM_WATER) {//正常工作阶段做进一步判断
                if (delError <= -2) {
                    counterForIndentifyWater = 0;
                    if (delError <= -4) counterForIndentify = counterForIndentify + 2;
                    else counterForIndentify++;
                    if (counterForIndentify >= 10) {//6
                        counterForIndentifyWater = 0;
                        counterForIndentify = 0;
                        if (liquidType == LIQUID_TYPE_WATER) {
                            liquidType = LIQUID_TYPE_OIL;
                        }
                        if (workProgress == WORK_PROGRESS_HEAT_WATER) {
                            if (error <= 0) workProgress = WORK_PROGRESS_KEEP_WARM_OIL;
                            else {
                                workProgress = WORK_PROGRESS_HEAT_OID;
                            }
                        }
                    }
                }else {
                    if (delError > -2) {
                        counterForIndentifyWater++;
                        if (counterForIndentifyWater>= 40) {//35
                            counterForIndentify = 0;
                            counterForIndentifyWater = 0;
                            if (liquidType == LIQUID_TYPE_OIL) {
                                liquidType = LIQUID_TYPE_WATER;
                            }

                            if (workProgress == WORK_PROGRESS_HEAT_OID) {
                                workProgress = WORK_PROGRESS_HEAT_WATER;
                            }
                        }
                    }

                }

                if (tempMode == TEMP_MODE_FAST_TEMP) {
                    liquidType = LIQUID_TYPE_WATER;
                    workProgress = WORK_PROGRESS_HEAT_WATER;
                }
            }

            if (workProgress == WORK_PROGRESS_HEAT_OID || workProgress == WORK_PROGRESS_HEAT_WATER) {
                if (delError >= 0) {//如果温度掉下去了，或者温度没变，则counterForHeat累加，当加到一定次数，说明需要更多的功率去加热
                    counterForHeat++;
                }else if (delError < 0) {
                    counterForHeat = 0;
                }

            }





            LogUtil.e("TEMPTest---Error--->" + error);
            LogUtil.e("TEMPTest---counterForHeat--->" + counterForHeat);
            if (delError == 0) {
                LogUtil.e("TEMPTest---delError--->" + delError);
            }else {
                LogUtil.e("TEMPTest---delError--->( " + delError + " )----time to target--->( " + (error / delError) * 5 + " s)");
            }

            timeForCalTrend = 0;

            if (counterForIntermittenceHeat == 0) counterForIntermittenceHeat = 1;
            else if (counterForIntermittenceHeat == 1) counterForIntermittenceHeat = 2;
            else if (counterForIntermittenceHeat == 2) counterForIntermittenceHeat = 3;
            else if (counterForIntermittenceHeat == 3) counterForIntermittenceHeat = 4;
            else if (counterForIntermittenceHeat == 4) counterForIntermittenceHeat = 0;
        }

        LogUtil.d("workProgress----->" + workProgress);

        int powerLevel = 0;
        switch (workProgress) {
            case WORK_PROGRESS_STATE_IDENTIFY_LIQUID_HEAT_LIQUID:
                if (System.currentTimeMillis() - lastTime >= timeForHeat) {
                    lastTime = System.currentTimeMillis();
                    workProgress = WORK_PROGRESS_STATE_IDENTIFY_LIQUID_WAIT_LIQUID_RISE;
                    powerLevel = 0;
                }else {
                    //powerLevel = powerForHeat;
                    powerLevel = getPower(powerForHeat);
                }

                break;
            case WORK_PROGRESS_STATE_IDENTIFY_LIQUID_WAIT_LIQUID_RISE:
                if (System.currentTimeMillis() - lastTime >= STATE_IDENTIFY_LIQUID_WAIT_LIQUID_RISE_TIME) {
                    lastTime = System.currentTimeMillis();
                    int deltaT = currentTemp - t0;
                    LogUtil.e("TEMPTest----deltaT--->" + deltaT);
                    /*if (deltaT >= 5) {//water
                        workProgress = WORK_PROGRESS_HEAT_WATER;
                        liquidType = LIQUID_TYPE_WATER;
                        powerLevel = 9;
                    }else*/ if (deltaT < 1) {//oil

                        workProgress = WORK_PROGRESS_HEAT_OID;
                        liquidType = LIQUID_TYPE_OIL;
                        //powerLevel = 9;
                        powerLevel = getPower(9);
                    }else {//need to identify
                        LogUtil.e("TEMPTest----need further identify");
                        workProgress = WORK_PROGRESS_STATE_FURTHER_IDENTIFY_WAIT_LIQUID_RISE;
                        powerLevel = 0;
                    }

                }else {
                    powerLevel = 0;
                }
                break;
            case WORK_PROGRESS_STATE_FURTHER_IDENTIFY_WAIT_LIQUID_RISE:
                if (System.currentTimeMillis() - lastTime >= timeForLiquidRise) {//12  //1L oid : 5 //
                    lastTime = System.currentTimeMillis();
                    t3 = currentTemp;
                    workProgress = WORK_PROGRESS_STATE_FURTHER_IDENTIFY_WAIT_LIQUID_RISE_SECOND;
                    powerLevel = 0;
                }else {
                    powerLevel = 0;
                }
                break;
            case WORK_PROGRESS_STATE_FURTHER_IDENTIFY_WAIT_LIQUID_RISE_SECOND:
                if (System.currentTimeMillis() - lastTime >= 10 * 1000) {
                    lastTime = System.currentTimeMillis();
                    int delta = currentTemp - t3;
                    delta = Math.abs(delta);
                    LogUtil.e("TEMPTest----further--->delta--->" + delta);
                    if (delta > 1) {//oil
                        workProgress = WORK_PROGRESS_HEAT_OID;
                        liquidType = LIQUID_TYPE_OIL;
                        //powerLevel = 9;
                        powerLevel = getPower(9);
                    }else {
                        workProgress = WORK_PROGRESS_HEAT_WATER;
                        liquidType = LIQUID_TYPE_WATER;
                        //powerLevel = 9;
                        powerLevel = getPower(9);
                    }

                }else {
                    powerLevel = 0;
                }
                break;
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
                            powerLevel = getPower(5);
                        }else if (cookerType == HardwareConstant.COOKER_TYPE_E_COOKER) {
                            powerLevel = getPower(5);
                        }else if (cookerType == HardwareConstant.COOKER_TYPE_F_COOKER) {
                            powerLevel = getPower(4);
                        }else if (cookerType == HardwareConstant.COOKER_TYPE_EF_COOKER) {
                            powerLevel = getPower(5);
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
                            powerLevel = getPower(7);
                        }else if (cookerType == HardwareConstant.COOKER_TYPE_E_COOKER) {
                            powerLevel = getPower(7);
                        }else if (cookerType == HardwareConstant.COOKER_TYPE_F_COOKER) {
                            powerLevel = getPower(6);
                        }else if (cookerType == HardwareConstant.COOKER_TYPE_EF_COOKER) {
                            powerLevel = getPower(7);
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



                break;
            case WORK_PROGRESS_HEAT_OID:
                int deltaTemp = tTarget - currentTemp;
                LogUtil.d("TEMPTest----WORK_PROGRESS_HEAT_OID---deltaTemp--->" + deltaTemp);
                //25
                int ctrlTemp = getOilControlTemp();
                LogUtil.d("TEMPTest----WORK_PROGRESS_HEAT_OID---ctrlTemp--->" + ctrlTemp);
                if (deltaTemp <= ctrlTemp) {
                    if (deltaTemp <= 0) {
                        workProgress = WORK_PROGRESS_KEEP_WARM_OIL;
                        powerLevel = getPower(1);
                    }else if (deltaTemp <= 1) {
                        powerLevel = getPower(1);
                    }else if(deltaTemp <= 12) {
                        //间歇加热
                        if (counterForIntermittenceHeat <= 1) {
                            if (tTarget <= 100) {
                                powerLevel = getPower(1);
                            }else {
                                powerLevel = getPower(3);
                            }
                        }else {
                            powerLevel = getPower(0);
                        }



                    }else {
                        if (tTarget <= 100) {
                            powerLevel = getPower(2);
                        }else {
                            powerLevel = getPower(4);
                        }

                    }

                }else {
                    if (tTarget <= 80) {
                        powerLevel = getPower(3);
                    }else {
                        powerLevel = getPower(8);
                    }

                }
                //LogUtil.e("TEMPTest---counterForHeat---> " + counterForHeat);
                if (counterForHeat >= 3 && workProgress == WORK_PROGRESS_HEAT_OID) {// 6 = 6 * 30 s , 如果小于设置温度，并且长时间没有温度没变化（6 × 5 = 30s）,则继续加热
                    LogUtil.e("TEMPTest---long time no heat , need further power");
                    if (deltaTemp <= 25) {
                        if (deltaTemp <= 0) {
                            powerLevel = getPower(1);//2
                        }else if (deltaTemp < 10) {
                            //间歇加热
                            if (counterForIntermittenceHeat <= 1) {
                                if (tTarget <= 80) {
                                    powerLevel = getPower(3);
                                }else {
                                    powerLevel = getPower(5);
                                }
                            }else {
                                powerLevel = getPower(0);
                            }



                        }else if (deltaTemp < 15) {
                            if (counterForIntermittenceHeat <= 1) {
                                if (tTarget <= 80) {
                                    powerLevel = getPower(3);
                                }else {
                                    powerLevel = getPower(5);
                                }
                            }else {
                                powerLevel = getPower(0);
                            }

                        }else {

                            if (tTarget <= 80) {
                                powerLevel = getPower(3);
                            }else {
                                powerLevel = getPower(7);
                            }
                        }

                    }else {
                        if (tTarget <= 80) {
                            powerLevel = getPower(4);
                        }else {
                            powerLevel = getPower(8);
                        }

                    }
                }

                break;
            case WORK_PROGRESS_KEEP_WARM_OIL:

                int deltaTempKeepWarmOil = tTarget - currentTemp;
                //LogUtil.d("Enter:: WORK_PROGRESS_KEEP_WARM_OIL---->" + deltaTempKeepWarmOil);
                if (deltaTempKeepWarmOil <= 0) {
                    powerLevel = 0;//1
                }else if (deltaTempKeepWarmOil > 0) {
                    if (deltaTempKeepWarmOil <= 1) {
                        powerLevel = getPower(1);//3
                    }/*else if (deltaTempKeepWarmOil <= 3) {
                        powerLevel = 3;
                    }*/else if (deltaTempKeepWarmOil <= 3) {
                        powerLevel = getPower(5);
                    }else if (deltaTempKeepWarmOil <= 5) {
                        powerLevel = getPower(6);
                    }else if (deltaTempKeepWarmOil <= 10) {
                        powerLevel = getPower(8);
                    }else if (deltaTempKeepWarmOil <= 20) {
                        powerLevel = getPower(9);
                    }else {
                        powerLevel = getPower(9);
                    }



                }

                break;
        }
        if (liquidType == LIQUID_TYPE_UNKNOWN) liquidTypeStr = "None";
        if (liquidType == LIQUID_TYPE_OIL) liquidTypeStr = "Oil";
        if (liquidType == LIQUID_TYPE_WATER) liquidTypeStr = "Water";
        LogUtil.d("TEMPTest----liquidtype---->( " + liquidTypeStr + " )---Temp--->( " + currentTemp +" )----t0--->( " + t0 + " )---powerlevel---->" + powerLevel);
        return powerLevel;
    }

    private PowerLimitManager mPowerLimitManager;

    private int getMaxPower() {
        CookerData cookerData = new CookerData(cookerType, InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FIRE_GEAR);
        int returnPowerValue = mPowerLimitManager.checkPower(9,cookerData);
        return returnPowerValue;
    }

    private int getMinPower() {
        CookerData cookerData = new CookerData(cookerType, InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FIRE_GEAR);
        int returnPowerValue = mPowerLimitManager.checkPower(1,cookerData);
        return returnPowerValue;
    }

    private int getPower(int level) {
        CookerData cookerData = new CookerData(cookerType, InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FIRE_GEAR);
        int returnPowerValue = mPowerLimitManager.checkPower(level,cookerData);
        return returnPowerValue;
    }
    private Disposable mFiveMinuteDisposable;//定时器
    private void doStartFiveMinuteCountDown() {
        doStopFiveTimerCountDown();
        mFiveMinuteDisposable = Observable
                .interval(0,1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {

                        if (timeForCalTrend < 6) timeForCalTrend++;


                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribe();
    }

    private void doStopFiveTimerCountDown() {
        if (mFiveMinuteDisposable != null) {
            if (!mFiveMinuteDisposable.isDisposed()) {
                mFiveMinuteDisposable.dispose();
            }
        }
    }

    private int getOilControlTemp() {
        float percent = 0.1f;
        if (tTarget <= 80) {
            if (cookerType == HardwareConstant.COOKER_TYPE_A_COOKER) {
                percent = 0.5f;
            }else if (cookerType == HardwareConstant.COOKER_TYPE_B_COOKER) {
                percent = 0.5f;
            }else if (cookerType == HardwareConstant.COOKER_TYPE_C_COOKER) {
                percent = 0.5f;
            }
        }
        else if (tTarget <= 100) percent = 0.2f;
        else if (tTarget <= 180) percent = 0.1f;
        else percent = 0.1f;
        int temp = (int) ((tTarget - t0) * percent);
        return temp;
    }

    public int getTargetTemp() {
        return tTarget;
    }

    public void setTartgetTemp(int temp) {
        this.tTarget = temp;
    }

    public void recyle() {
        doStopFiveTimerCountDown();
    }

}
