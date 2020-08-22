package com.ekek.tfthobmodule.utils;

import android.content.Context;
import android.os.SystemClock;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.hardwaremodule.entity.AllCookerData;
import com.ekek.hardwaremodule.entity.FTFDataModel;
import com.ekek.hardwaremodule.pid.FastTempCtrlStrategyManager;
import com.ekek.hardwaremodule.pid.TempControlManager;
import com.ekek.hardwaremodule.pid.TempCtrlStrategyManager;
import com.ekek.hardwaremodule.power.PowerLimitManager;
import com.ekek.hardwaremodule.protocol.InductionCookerProtocol;
import com.ekek.hardwaremodule.utils.TFTDataUtil;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.database.SettingPreferencesUtil;
import com.ekek.tfthobmodule.CataTFTHobApplication;
import com.ekek.tfthobmodule.constants.TFTHobConfiguration;
import com.ekek.tfthobmodule.constants.TFTHobConstant;
import com.ekek.tfthobmodule.constants.TFTHobTemperatureValue;
import com.ekek.tfthobmodule.database.DatabaseHelper;
import com.ekek.tfthobmodule.database.SettingDbHelper;
import com.ekek.tfthobmodule.entity.CookerDataTable;
import com.ekek.tfthobmodule.entity.CookerDataTableDao;
import com.ekek.tfthobmodule.entity.HoodData;
import com.ekek.tfthobmodule.event.BluetoothEevent;
import com.ekek.tfthobmodule.event.DetectCookerEvent;
import com.ekek.tfthobmodule.event.NTCEvent;

import org.greenrobot.eventbus.EventBus;

public class HardwareDataUtil {

    private static TempCtrlStrategyManager mATempCtrlStrategyManager,mBTempCtrlStrategyManager,mCTempCtrlStrategyManager;
    private static TempCtrlStrategyManager mDTempCtrlStrategyManager,mETempCtrlStrategyManager,mFTempCtrlStrategyManager;
    private static TempCtrlStrategyManager mABTempCtrlStrategyManager,mEFTempCtrlStrategyManager;

    private static FastTempCtrlStrategyManager mAFastTempCtrlStrategyManager, mBFastTempCtrlStrategyManager;
    private static FastTempCtrlStrategyManager mABFastTempCtrlStrategyManager;

    private static boolean lastReqPowerOffModelDelay;
    private static long delayStart;

    private static int lastEnterPowerOffMode = -1;
    private static byte changingByte = 0;

    public static FTFDataModel getFTFDataModel(Context context ,boolean mode04) {
        byte aMode = 0;
        byte aFireGear = 0;
        byte aTempGear = 0;
        byte aNTCTempValue = 0;

        byte bMode = 0;
        byte bFireGear = 0;
        byte bTempGear = 0;
        byte bNTCTempValue = 0;

        byte abMode = 0;
        byte abFireGear = 0;
        byte abTempGear = 0;
        byte abNTCTempValue = 0;

        byte cMode = 0;
        byte cFireGear = 0;
        byte cTempGear = 0;
        byte cNTCTempValue = 0;

        byte dMode = 0;
        byte dFireGear = 0;
        byte dTempGear = 0;
        byte dNTCTempValue = 0;

        byte eMode = 0;
        byte eFireGear = 0;
        byte eTempGear = 0;
        byte eNTCTempValue = 0;

        byte fMode = 0;
        byte fFireGear = 0;
        byte fTempGear = 0;
        byte fanGear = 0;
        byte fNTCTempValue = 0;

        byte efMode = 0;
        byte efFireGear = 0;
        byte efTempGear = 0;
        byte efNTCTempValue = 0;

        byte lightGear = 0;
        if (mode04) {
            aMode = 4;
            bMode = 4;
            abMode = 4;
            cMode = 4;
            dMode = 4;
            eMode = 4;
            fMode = 4;
            efMode = 4;
        }
        String fastMessage = "";
        int tempSensorValue = SettingDbHelper.getTemperatureSensorValue();
        CookerDataTableDao dao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
        CookerDataTable aCookerData = dao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(TFTHobConstant.COOKER_TYPE_A_COOKER)).build().unique();
       // LogUtil.e("aCookerData---amode--->" + aCookerData.getHardwareWorkMode() + "---fire---->" + aCookerData.getFireValue() + "----temp---->" + aCookerData.getTempValue());
        if (aCookerData != null) {
            aMode = (byte) aCookerData.getHardwareWorkMode();
            aNTCTempValue = (byte) aCookerData.getNtcTempValue();
            //LogUtil.d("Enter:: fire      value ================== " + aCookerData.getFireValue());


            if (aMode == InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_PRECISE_TEMPERATURE) {
                if (mATempCtrlStrategyManager == null) {
                    /*mATempControlManager = new TempControlManager();
                    mATempControlManager.init(aCookerData.getTempValue(),tempSensorValue);*/
                    mATempCtrlStrategyManager = new TempCtrlStrategyManager(TFTHobConstant.COOKER_TYPE_A_COOKER,TFTHobConstant.TEMP_MODE_PRECISE_TEMP,tempSensorValue,aCookerData.getTempValue());
                }
                //byte[] data = getPIDDataForPrecise(mATempControlManager,aCookerData.getTempValue(),tempSensorValue);
                byte[] data = getTempCtrlData(mATempCtrlStrategyManager,tempSensorValue,aCookerData.getTempValue());
               // aMode = data[0];
                aFireGear = data[1];
                aTempGear = 0;
            }else if (aMode == InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FAST_TEMPERATURE) {
                int aCurrentTempValue = TFTHobTemperatureValue.getNTCTempValue(aCookerData.getTempValue(),aNTCTempValue);
                int tATarget = aCookerData.getTempValue();
                if (TFTHobConfiguration.TFT_HOB_TYPE == TFTHobConfiguration.TFT_HOB_TYPE_80) {
                    if (aCookerData.getTempValue() == 90) {
                        tATarget = tATarget - 20;
                    }else  if (aCookerData.getTempValue() == 80) {
                        tATarget = tATarget - 19;
                    }else  if (aCookerData.getTempValue() == 100) {
                        tATarget = tATarget - 0;
                    }else {
                        tATarget = tATarget - 18;
                    }

                }else {
                    tATarget = tATarget - 10;
                }
                if (mATempCtrlStrategyManager == null) {
                    //mATempControlManager = new TempControlManager();
                    //mATempControlManager.init(TFTHobTemperatureValue.getNTCSettingTempValue(aCookerData.getTempValue()),TFTHobTemperatureValue.getNTCCurrentTempValue(aCookerData.getTempValue(),aCurrentTempValue));
                    mATempCtrlStrategyManager = new TempCtrlStrategyManager(TFTHobConstant.COOKER_TYPE_A_COOKER,TFTHobConstant.TEMP_MODE_FAST_TEMP,TFTHobTemperatureValue.getNTCCurrentTempValue(aCookerData.getTempValue(),aCurrentTempValue),TFTHobTemperatureValue.getNTCSettingTempValue(tATarget));

                }

                fastMessage = fastMessage + "A:" + aCurrentTempValue + " ";
                //byte[] data = getFastSettingData(TFTHobTemperatureValue.getNTCSettingTempValue(aCookerData.getTempValue()), aCurrentTempValue);
                //byte[] data = getPIDDataForFast(mATempControlManager,TFTHobTemperatureValue.getNTCSettingTempValue(aCookerData.getTempValue()),TFTHobTemperatureValue.getNTCCurrentTempValue(aCookerData.getTempValue(),aCurrentTempValue));
                byte[] data = getTempCtrlData(mATempCtrlStrategyManager,TFTHobTemperatureValue.getNTCCurrentTempValue(aCookerData.getTempValue(),aCurrentTempValue),tATarget);
              //  aMode = data[0];
                aFireGear = data[1];
                aTempGear = 0;
            }else {
                if (mATempCtrlStrategyManager != null) {
                    mATempCtrlStrategyManager.recyle();
                    mATempCtrlStrategyManager = null;
                }

               /* if (mAFastTempCtrlStrategyManager != null) {
                    mAFastTempCtrlStrategyManager.recyle();
                    mAFastTempCtrlStrategyManager = null;
                }*/

                aFireGear = (byte) (aCookerData.getFireValue() == -1? 0 : aCookerData.getFireValue());
                aTempGear = (byte) (aCookerData.getTempValue() == -1? 0 : aCookerData.getTempValue());
            }
        }

        CookerDataTable bCookerData = dao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(TFTHobConstant.COOKER_TYPE_B_COOKER)).build().unique();
        if (bCookerData != null) {
            bMode = (byte) bCookerData.getHardwareWorkMode();
            bNTCTempValue = (byte) bCookerData.getNtcTempValue();
            /*bFireGear = (byte) (bCookerData.getFireValue() == -1? 0 : bCookerData.getFireValue());
            bTempGear = (byte) (bCookerData.getTempValue() == -1? 0 : bCookerData.getTempValue());*/

           // LogUtil.d("samhung samhung-------11111------------->" + bMode);
            if (bMode == InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_PRECISE_TEMPERATURE) {
                if (mBTempCtrlStrategyManager == null) {
                    //mBTempControlManager = new TempControlManager();
                    //mBTempControlManager.init(bCookerData.getTempValue(),tempSensorValue);
                    mBTempCtrlStrategyManager = new TempCtrlStrategyManager(TFTHobConstant.COOKER_TYPE_B_COOKER,TFTHobConstant.TEMP_MODE_PRECISE_TEMP,tempSensorValue,bCookerData.getTempValue());
                }
                //byte[] data = getPIDDataForPrecise(mBTempControlManager,bCookerData.getTempValue(),tempSensorValue);
                byte[] data = getTempCtrlData(mBTempCtrlStrategyManager,tempSensorValue,bCookerData.getTempValue());
              //  bMode = data[0];
                bFireGear = data[1];
                bTempGear = 0;
               // LogUtil.d("samhung samhung-------11111-----------1--");
            }else if (bMode == InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FAST_TEMPERATURE) {
                int bCurrentTempValue = TFTHobTemperatureValue.getNTCTempValue(bCookerData.getTempValue(),bNTCTempValue);
                if (mBTempCtrlStrategyManager == null) {
                    //mBTempControlManager = new TempControlManager();
                    //mBTempControlManager.init(TFTHobTemperatureValue.getNTCSettingTempValue(bCookerData.getTempValue()),TFTHobTemperatureValue.getNTCCurrentTempValue(bCookerData.getTempValue(),bCurrentTempValue));
                    mBTempCtrlStrategyManager = new TempCtrlStrategyManager(TFTHobConstant.COOKER_TYPE_B_COOKER,TFTHobConstant.TEMP_MODE_FAST_TEMP,TFTHobTemperatureValue.getNTCCurrentTempValue(bCookerData.getTempValue(),bCurrentTempValue),TFTHobTemperatureValue.getNTCSettingTempValue(bCookerData.getTempValue() - 10));
                }

                fastMessage = fastMessage + "B:" + bCurrentTempValue + " ";
                //byte[] data = getFastSettingData(bCookerData.getTempValue(), bCurrentTempValue);
                //byte[] data = getFastSettingData(TFTHobTemperatureValue.getNTCSettingTempValue(bCookerData.getTempValue()), bCurrentTempValue);
                //byte[] data = getPIDDataForFast(mBTempControlManager,TFTHobTemperatureValue.getNTCSettingTempValue(bCookerData.getTempValue()),TFTHobTemperatureValue.getNTCCurrentTempValue(bCookerData.getTempValue(),bCurrentTempValue));
                byte[] data = getTempCtrlData(mBTempCtrlStrategyManager,TFTHobTemperatureValue.getNTCCurrentTempValue(bCookerData.getTempValue(),bCurrentTempValue),bCookerData.getTempValue() - 10);
                        // byte[] data = getFastSettingData(bCookerData.getTempValue(), TFTHobTemperatureValue.getNTCTempValue(bNTCTempValue));
              //  bMode = data[0];
                bFireGear = data[1];
                bTempGear = 0;
            }else {
                if (mBTempCtrlStrategyManager != null) {
                    mBTempCtrlStrategyManager.recyle();
                    mBTempCtrlStrategyManager = null;
                }

            /*    if (mBFastTempCtrlStrategyManager != null) {
                    mBFastTempCtrlStrategyManager.recyle();
                    mBFastTempCtrlStrategyManager = null;
                }*/

                bFireGear = (byte) (bCookerData.getFireValue() == -1? 0 : bCookerData.getFireValue());
                bTempGear = (byte) (bCookerData.getTempValue() == -1? 0 : bCookerData.getTempValue());
                //LogUtil.d("samhung samhung-------11111-----------2--");
            }
        }

        //处理无区模式：
        //现在长方形炉头相当于是由3个炉头共同组成并单独工作：无区的大炉头 + 独立的上下两个炉头
        //当两个独立的炉头的模式都是0x04时，去读取数据库的无区大炉头，看此时炉头是什么模式，如还是0x04,说明无区模式也没开启，如果是其他模式（不是0x01,0x02,0x03）,则说明炉头进入无区模式
        //则响应得把上下两个炉头的模式置为0x05,同时档位值为大炉头的档位值
        if (aMode == 0x04 && bMode == 0x04) {
            CookerDataTable abCookerData = dao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(TFTHobConstant.COOKER_TYPE_AB_COOKER)).build().unique();
            if (abCookerData != null) {
                abMode = (byte) abCookerData.getHardwareWorkMode();
                if (abMode == InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_PRECISE_TEMPERATURE ||
                        abMode == InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FAST_TEMPERATURE ||
                        abMode == InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FIRE_GEAR
                ) {
                    abNTCTempValue = (byte) abCookerData.getNtcTempValue();
                    if (abMode == InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_PRECISE_TEMPERATURE) {
                        if (mABTempCtrlStrategyManager == null) {
                            mABTempCtrlStrategyManager = new TempCtrlStrategyManager(TFTHobConstant.COOKER_TYPE_AB_COOKER,TFTHobConstant.TEMP_MODE_PRECISE_TEMP,tempSensorValue,abCookerData.getTempValue());
                        }

                        byte[] data = getTempCtrlData(mABTempCtrlStrategyManager,tempSensorValue,abCookerData.getTempValue());
                        // aMode = data[0];
                        abFireGear = data[1];
                        abTempGear = 0;
                    }else if (abMode == InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FAST_TEMPERATURE) {
                        int abCurrentTempValue = TFTHobTemperatureValue.getNTCTempValue(abCookerData.getTempValue(),abNTCTempValue);
                        if (mABTempCtrlStrategyManager == null) {
                            mABTempCtrlStrategyManager = new TempCtrlStrategyManager(TFTHobConstant.COOKER_TYPE_AB_COOKER,TFTHobConstant.TEMP_MODE_FAST_TEMP,TFTHobTemperatureValue.getNTCCurrentTempValue(abCookerData.getTempValue(),abCurrentTempValue),TFTHobTemperatureValue.getNTCSettingTempValue(abCookerData.getTempValue() - 10));

                        }

                        fastMessage = fastMessage + "AB:" + abCurrentTempValue + " ";
                        byte[] data = getTempCtrlData(mABTempCtrlStrategyManager,TFTHobTemperatureValue.getNTCCurrentTempValue(abCookerData.getTempValue(),abCurrentTempValue),abCookerData.getTempValue() - 10);
                        abFireGear = data[1];
                        abTempGear = 0;
                    }else {
                        if (mABTempCtrlStrategyManager != null) {
                            mABTempCtrlStrategyManager.recyle();
                            mABTempCtrlStrategyManager = null;
                        }

                      /*  if (mABFastTempCtrlStrategyManager != null) {
                            mABFastTempCtrlStrategyManager.recyle();
                            mABFastTempCtrlStrategyManager = null;
                        }*/

                        abFireGear = (byte) (abCookerData.getFireValue() == -1? 0 : abCookerData.getFireValue());
                        abTempGear = (byte) (abCookerData.getTempValue() == -1? 0 : abCookerData.getTempValue());
                    }

                    aMode = InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_UNION;
                    bMode = InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_UNION;

                    aFireGear = abFireGear;
                    bFireGear = abFireGear;

                    aTempGear = abTempGear;
                    bTempGear = abTempGear;

                }


            }
        }





        CookerDataTable cCookerData = dao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(TFTHobConstant.COOKER_TYPE_C_COOKER)).build().unique();
        if (cCookerData != null) {
            cMode = (byte) cCookerData.getHardwareWorkMode();
            cNTCTempValue = (byte) cCookerData.getNtcTempValue();
    /*        cFireGear = (byte) (cCookerData.getFireValue() == -1? 0 : cCookerData.getFireValue());
            cTempGear = (byte) (cCookerData.getTempValue() == -1? 0 : cCookerData.getTempValue());*/

            if (cMode == InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_PRECISE_TEMPERATURE) {
                if (mCTempCtrlStrategyManager == null) {
                    //mCTempControlManager = new TempControlManager();
                   // mCTempControlManager.init(cCookerData.getTempValue(),tempSensorValue);
                    mCTempCtrlStrategyManager = new TempCtrlStrategyManager(TFTHobConstant.COOKER_TYPE_C_COOKER,TFTHobConstant.TEMP_MODE_PRECISE_TEMP,tempSensorValue,cCookerData.getTempValue());
                }
                //byte[] data = getPIDDataForPrecise(mCTempControlManager,cCookerData.getTempValue(),tempSensorValue);
                byte[] data = getTempCtrlData(mCTempCtrlStrategyManager,tempSensorValue,cCookerData.getTempValue());
               // cMode = data[0];
                cFireGear = data[1];
                cTempGear = 0;
            }else if (cMode == InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FAST_TEMPERATURE) {
                int cCurrentTempValue = TFTHobTemperatureValue.getNTCTempValue(cCookerData.getTempValue(),cNTCTempValue);
                if (mCTempCtrlStrategyManager == null) {
                    //mCTempControlManager = new TempControlManager();
                    //mCTempControlManager.init(TFTHobTemperatureValue.getNTCSettingTempValue(cCookerData.getTempValue()),TFTHobTemperatureValue.getNTCCurrentTempValue(cCookerData.getTempValue(),cCurrentTempValue));

                    if (TFTHobConfiguration.TFT_HOB_TYPE == TFTHobConfiguration.TFT_HOB_TYPE_90) {
                        if (cCookerData.getTempValue() == 70) {
                            mCTempCtrlStrategyManager = new TempCtrlStrategyManager(TFTHobConstant.COOKER_TYPE_C_COOKER,TFTHobConstant.TEMP_MODE_FAST_TEMP,TFTHobTemperatureValue.getNTCCurrentTempValue(cCookerData.getTempValue(),cCurrentTempValue),TFTHobTemperatureValue.getNTCSettingTempValue(cCookerData.getTempValue() - 20));

                        }else if (cCookerData.getTempValue() == 80) {
                            mCTempCtrlStrategyManager = new TempCtrlStrategyManager(TFTHobConstant.COOKER_TYPE_C_COOKER,TFTHobConstant.TEMP_MODE_FAST_TEMP,TFTHobTemperatureValue.getNTCCurrentTempValue(cCookerData.getTempValue(),cCurrentTempValue),TFTHobTemperatureValue.getNTCSettingTempValue(cCookerData.getTempValue() - 20));

                        }else if (cCookerData.getTempValue() == 90) {
                            mCTempCtrlStrategyManager = new TempCtrlStrategyManager(TFTHobConstant.COOKER_TYPE_C_COOKER,TFTHobConstant.TEMP_MODE_FAST_TEMP,TFTHobTemperatureValue.getNTCCurrentTempValue(cCookerData.getTempValue(),cCurrentTempValue),TFTHobTemperatureValue.getNTCSettingTempValue(cCookerData.getTempValue() - 25));

                        }else if (cCookerData.getTempValue() == 100) {
                            mCTempCtrlStrategyManager = new TempCtrlStrategyManager(TFTHobConstant.COOKER_TYPE_C_COOKER,TFTHobConstant.TEMP_MODE_FAST_TEMP,TFTHobTemperatureValue.getNTCCurrentTempValue(cCookerData.getTempValue(),cCurrentTempValue),TFTHobTemperatureValue.getNTCSettingTempValue(cCookerData.getTempValue() - 25));

                        }else {
                            mCTempCtrlStrategyManager = new TempCtrlStrategyManager(TFTHobConstant.COOKER_TYPE_C_COOKER,TFTHobConstant.TEMP_MODE_FAST_TEMP,TFTHobTemperatureValue.getNTCCurrentTempValue(cCookerData.getTempValue(),cCurrentTempValue),TFTHobTemperatureValue.getNTCSettingTempValue(cCookerData.getTempValue() - 10));

                        }


                    }else {
                        mCTempCtrlStrategyManager = new TempCtrlStrategyManager(TFTHobConstant.COOKER_TYPE_C_COOKER,TFTHobConstant.TEMP_MODE_FAST_TEMP,TFTHobTemperatureValue.getNTCCurrentTempValue(cCookerData.getTempValue(),cCurrentTempValue),TFTHobTemperatureValue.getNTCSettingTempValue(cCookerData.getTempValue() - 10));

                    }

                }

                fastMessage = fastMessage + "C:" + cCurrentTempValue + " ";
                int tTarget = cCookerData.getTempValue();
                if (TFTHobConfiguration.TFT_HOB_TYPE == TFTHobConfiguration.TFT_HOB_TYPE_90) {
                    if (cCookerData.getTempValue() == 70) {
                        tTarget = tTarget - 15;
                    }else if (cCookerData.getTempValue() == 80) {
                        tTarget = tTarget - 20;
                    }else if (cCookerData.getTempValue() == 90) {
                        tTarget = tTarget - 22;
                    }else if (cCookerData.getTempValue() == 100) {
                        tTarget = tTarget - 0;//25
                    }else {
                        tTarget = tTarget - 10;
                    }


                }else {
                    tTarget = tTarget - 10;
                }


                //byte[] data = getTempCtrlData(mCTempCtrlStrategyManager,TFTHobTemperatureValue.getNTCCurrentTempValue(cCookerData.getTempValue(),cCurrentTempValue),cCookerData.getTempValue() - 10);
                byte[] data = getTempCtrlData(mCTempCtrlStrategyManager,TFTHobTemperatureValue.getNTCCurrentTempValue(cCookerData.getTempValue(),cCurrentTempValue),tTarget);


                cFireGear = data[1];
                cTempGear = 0;
            }else {
                if (mCTempCtrlStrategyManager != null) {
                    mCTempCtrlStrategyManager.recyle();
                    mCTempCtrlStrategyManager = null;
                }
                cFireGear = (byte) (cCookerData.getFireValue() == -1? 0 : cCookerData.getFireValue());
                cTempGear = (byte) (cCookerData.getTempValue() == -1? 0 : cCookerData.getTempValue());
            }
        }

        CookerDataTable dCookerData = dao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(TFTHobConstant.COOKER_TYPE_D_COOKER)).build().unique();
        if (dCookerData != null) {
            dMode = (byte) dCookerData.getHardwareWorkMode();
            dNTCTempValue = (byte) dCookerData.getNtcTempValue();
            /*dFireGear = (byte) (dCookerData.getFireValue() == -1? 0 : dCookerData.getFireValue());
            dTempGear = (byte) (dCookerData.getTempValue() == -1? 0 : dCookerData.getTempValue());*/

            if (dMode == InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_PRECISE_TEMPERATURE) {
                if (mDTempCtrlStrategyManager == null) {
                   // mDTempControlManager = new TempControlManager();
                    //mDTempControlManager.init(dCookerData.getTempValue(),tempSensorValue);
                    mDTempCtrlStrategyManager = new TempCtrlStrategyManager(TFTHobConstant.COOKER_TYPE_D_COOKER,TFTHobConstant.TEMP_MODE_PRECISE_TEMP,tempSensorValue,dCookerData.getTempValue());

                }
                //byte[] data = getPIDDataForPrecise(mDTempControlManager,dCookerData.getTempValue(),tempSensorValue);
                byte[] data = getTempCtrlData(mDTempCtrlStrategyManager,tempSensorValue,dCookerData.getTempValue());

                // dMode = data[0];
                dFireGear = data[1];
                dTempGear = 0;
            }else if (dMode == InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FAST_TEMPERATURE) {
                int dCurrentTempValue = TFTHobTemperatureValue.getNTCTempValue(dCookerData.getTempValue(),dNTCTempValue);
                int tDTarget = dCookerData.getTempValue();
                if (TFTHobConfiguration.TFT_HOB_TYPE == TFTHobConfiguration.TFT_HOB_TYPE_80) {

                    if (dCookerData.getTempValue() == 90) {
                        tDTarget = tDTarget - 12;
                    }else if (dCookerData.getTempValue() == 100) {
                        tDTarget = tDTarget + 5;
                    }else {
                        tDTarget = tDTarget - 5;
                    }

                }else {
                    tDTarget = tDTarget - 10;
                }

                if (mDTempCtrlStrategyManager == null) {
                   // mDTempControlManager = new TempControlManager();
                   // mDTempControlManager.init(TFTHobTemperatureValue.getNTCSettingTempValue(dCookerData.getTempValue()),dCurrentTempValue);
                    mDTempCtrlStrategyManager = new TempCtrlStrategyManager(TFTHobConstant.COOKER_TYPE_D_COOKER,TFTHobConstant.TEMP_MODE_FAST_TEMP,TFTHobTemperatureValue.getNTCCurrentTempValue(dCookerData.getTempValue(),dCurrentTempValue),TFTHobTemperatureValue.getNTCSettingTempValue(tDTarget));

                }
                fastMessage = fastMessage + "D:" + dCurrentTempValue + " ";
               // byte[] data = getFastSettingData(dCookerData.getTempValue(), dCurrentTempValue);
                //byte[] data = getFastSettingData(TFTHobTemperatureValue.getNTCSettingTempValue(dCookerData.getTempValue()), dCurrentTempValue);
                //byte[] data = getPIDDataForFast(mDTempControlManager,TFTHobTemperatureValue.getNTCSettingTempValue(dCookerData.getTempValue()),dCurrentTempValue);
                byte[] data = getTempCtrlData(mDTempCtrlStrategyManager,TFTHobTemperatureValue.getNTCCurrentTempValue(dCookerData.getTempValue(),dCurrentTempValue),tDTarget);

                //byte[] data = getFastSettingData(dCookerData.getTempValue(), TFTHobTemperatureValue.getNTCTempValue(dNTCTempValue));
              //  dMode = data[0];
                dFireGear = data[1];
                dTempGear = 0;
            }else {
                if (mDTempCtrlStrategyManager != null) {
                    mDTempCtrlStrategyManager.recyle();
                    mDTempCtrlStrategyManager = null;
                }
                dFireGear = (byte) (dCookerData.getFireValue() == -1? 0 : dCookerData.getFireValue());
                dTempGear = (byte) (dCookerData.getTempValue() == -1? 0 : dCookerData.getTempValue());
            }
        }

        CookerDataTable eCookerData = dao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(TFTHobConstant.COOKER_TYPE_E_COOKER)).build().unique();
        if (eCookerData != null) {
            eMode = (byte) eCookerData.getHardwareWorkMode();
            eNTCTempValue = (byte) eCookerData.getNtcTempValue();
          /*  eFireGear = (byte) (eCookerData.getFireValue() == -1? 0 : eCookerData.getFireValue());
            eTempGear = (byte) (eCookerData.getTempValue() == -1? 0 : eCookerData.getTempValue());*/

            if (eMode == InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_PRECISE_TEMPERATURE) {
                if (mETempCtrlStrategyManager == null) {
                    //mETempControlManager = new TempControlManager();
                    //mETempControlManager.init(eCookerData.getTempValue(),tempSensorValue);
                    mETempCtrlStrategyManager = new TempCtrlStrategyManager(TFTHobConstant.COOKER_TYPE_E_COOKER,TFTHobConstant.TEMP_MODE_PRECISE_TEMP,tempSensorValue,eCookerData.getTempValue());

                }
                //byte[] data = getPIDDataForPrecise(mETempControlManager,eCookerData.getTempValue(),tempSensorValue);
                byte[] data = getTempCtrlData(mETempCtrlStrategyManager,tempSensorValue,eCookerData.getTempValue());

                //  eMode = data[0];
                eFireGear = data[1];
                eTempGear = 0;
            }else if (eMode == InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FAST_TEMPERATURE) {
                int eCurrentTempValue = TFTHobTemperatureValue.getNTCTempValue(eCookerData.getTempValue(),eNTCTempValue);
                if (mETempCtrlStrategyManager == null) {
                    //mETempControlManager = new TempControlManager();
                   // mETempControlManager.init(TFTHobTemperatureValue.getNTCSettingTempValue(eCookerData.getTempValue()),eCurrentTempValue);

                    mETempCtrlStrategyManager = new TempCtrlStrategyManager(TFTHobConstant.COOKER_TYPE_E_COOKER,TFTHobConstant.TEMP_MODE_FAST_TEMP,TFTHobTemperatureValue.getNTCCurrentTempValue(eCookerData.getTempValue(),eCurrentTempValue),TFTHobTemperatureValue.getNTCSettingTempValue(eCookerData.getTempValue() - 10));


                }
                fastMessage = fastMessage + "E:" + eCurrentTempValue + " ";
                //byte[] data = getFastSettingData(eCookerData.getTempValue(), eCurrentTempValue);
                //byte[] data = getFastSettingData(TFTHobTemperatureValue.getNTCSettingTempValue(eCookerData.getTempValue()), eCurrentTempValue);
               // byte[] data = getPIDDataForFast(mETempControlManager,TFTHobTemperatureValue.getNTCSettingTempValue(eCookerData.getTempValue()),eCurrentTempValue);
                byte[] data = getTempCtrlData(mETempCtrlStrategyManager,TFTHobTemperatureValue.getNTCCurrentTempValue(eCookerData.getTempValue(),eCurrentTempValue),eCookerData.getTempValue() - 10);

                //byte[] data = getFastSettingData(eCookerData.getTempValue(), TFTHobTemperatureValue.getNTCTempValue(eNTCTempValue));
              //  eMode = data[0];
                eFireGear = data[1];
                eTempGear = 0;
            }else {
                if (mETempCtrlStrategyManager != null) {
                    mETempCtrlStrategyManager.recyle();
                    mETempCtrlStrategyManager = null;
                }
                eFireGear = (byte) (eCookerData.getFireValue() == -1? 0 : eCookerData.getFireValue());
                eTempGear = (byte) (eCookerData.getTempValue() == -1? 0 : eCookerData.getTempValue());
            }
        }

        CookerDataTable fCookerData = dao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(TFTHobConstant.COOKER_TYPE_F_COOKER)).build().unique();
        if (fCookerData != null) {
            fMode = (byte) fCookerData.getHardwareWorkMode();
            fNTCTempValue = (byte) fCookerData.getNtcTempValue();
            /*fFireGear = (byte) (fCookerData.getFireValue() == -1? 0 : fCookerData.getFireValue());
            fTempGear = (byte) (fCookerData.getTempValue() == -1? 0 : fCookerData.getTempValue());*/

            if (fMode == InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_PRECISE_TEMPERATURE) {
                if (mFTempCtrlStrategyManager == null) {
                    //mFTempControlManager = new TempControlManager();
                    //mFTempControlManager.init(fCookerData.getTempValue(),tempSensorValue);
                    mFTempCtrlStrategyManager = new TempCtrlStrategyManager(TFTHobConstant.COOKER_TYPE_F_COOKER,TFTHobConstant.TEMP_MODE_PRECISE_TEMP,tempSensorValue,fCookerData.getTempValue());

                }
                byte[] data = getTempCtrlData(mFTempCtrlStrategyManager,tempSensorValue,fCookerData.getTempValue());
              //  fMode = data[0];
                fFireGear = data[1];
                fTempGear = 0;
            }else if (fMode == InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FAST_TEMPERATURE) {
                int fCurrentTempValue = TFTHobTemperatureValue.getNTCTempValue(fCookerData.getTempValue(),fNTCTempValue);
                if (mFTempCtrlStrategyManager == null) {
                    //mFTempControlManager = new TempControlManager();
                    //mFTempControlManager.init(TFTHobTemperatureValue.getNTCSettingTempValue(fCookerData.getTempValue()),fCurrentTempValue);
                    mFTempCtrlStrategyManager = new TempCtrlStrategyManager(TFTHobConstant.COOKER_TYPE_F_COOKER,TFTHobConstant.TEMP_MODE_FAST_TEMP,TFTHobTemperatureValue.getNTCCurrentTempValue(fCookerData.getTempValue(),fCurrentTempValue),TFTHobTemperatureValue.getNTCSettingTempValue(fCookerData.getTempValue() - 10));

                }
                fastMessage = fastMessage + "F:" + fCurrentTempValue + " ";
               // byte[] data = getFastSettingData(fCookerData.getTempValue(), fCurrentTempValue);
                //byte[] data = getFastSettingData(TFTHobTemperatureValue.getNTCSettingTempValue(fCookerData.getTempValue()), fCurrentTempValue);
                byte[] data = getTempCtrlData(mFTempCtrlStrategyManager,TFTHobTemperatureValue.getNTCCurrentTempValue(fCookerData.getTempValue(),fCurrentTempValue),fCookerData.getTempValue() - 10);

                //byte[] data = getFastSettingData(fCookerData.getTempValue(), TFTHobTemperatureValue.getNTCTempValue(fNTCTempValue));
              //  fMode = data[0];
                fFireGear = data[1];
                fTempGear = 0;
            }else {
                if (mFTempCtrlStrategyManager != null) {
                    mFTempCtrlStrategyManager.recyle();
                    mFTempCtrlStrategyManager = null;
                }
                fFireGear = (byte) (fCookerData.getFireValue() == -1? 0 : fCookerData.getFireValue());
                fTempGear = (byte) (fCookerData.getTempValue() == -1? 0 : fCookerData.getTempValue());
            }
        }

        //处理无区模式：
        //现在长方形炉头相当于是由3个炉头共同组成并单独工作：无区的大炉头 + 独立的上下两个炉头
        //当两个独立的炉头的模式都是0x04时，去读取数据库的无区大炉头，看此时炉头是什么模式，如还是0x04,说明无区模式也没开启，如果是其他模式（不是0x01,0x02,0x03）,则说明炉头进入无区模式
        //则响应得把上下两个炉头的模式置为0x05,同时档位值为大炉头的档位值
        if (eMode == 0x04 && fMode == 0x04) {
            CookerDataTable efCookerData = dao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(TFTHobConstant.COOKER_TYPE_EF_COOKER)).build().unique();
            if (efCookerData != null) {
                efMode = (byte) efCookerData.getHardwareWorkMode();
                if (efMode == InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_PRECISE_TEMPERATURE ||
                        efMode == InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FAST_TEMPERATURE ||
                        efMode == InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FIRE_GEAR
                ) {
                    efNTCTempValue = (byte) efCookerData.getNtcTempValue();
                    if (efMode == InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_PRECISE_TEMPERATURE) {
                        if (mEFTempCtrlStrategyManager == null) {
                            mEFTempCtrlStrategyManager = new TempCtrlStrategyManager(TFTHobConstant.COOKER_TYPE_EF_COOKER,TFTHobConstant.TEMP_MODE_PRECISE_TEMP,tempSensorValue,efCookerData.getTempValue());
                        }

                        byte[] data = getTempCtrlData(mEFTempCtrlStrategyManager,tempSensorValue,efCookerData.getTempValue());
                        // aMode = data[0];
                        efFireGear = data[1];
                        efTempGear = 0;
                    }else if (efMode == InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FAST_TEMPERATURE) {
                        int abCurrentTempValue = TFTHobTemperatureValue.getNTCTempValue(efCookerData.getTempValue(),efNTCTempValue);
                        if (mEFTempCtrlStrategyManager == null) {
                            mEFTempCtrlStrategyManager = new TempCtrlStrategyManager(TFTHobConstant.COOKER_TYPE_EF_COOKER,TFTHobConstant.TEMP_MODE_FAST_TEMP,TFTHobTemperatureValue.getNTCCurrentTempValue(efCookerData.getTempValue(),abCurrentTempValue),TFTHobTemperatureValue.getNTCSettingTempValue(efCookerData.getTempValue() - 10));

                        }

                        fastMessage = fastMessage + "AB:" + abCurrentTempValue + " ";
                        byte[] data = getTempCtrlData(mEFTempCtrlStrategyManager,TFTHobTemperatureValue.getNTCCurrentTempValue(efCookerData.getTempValue(),abCurrentTempValue),efCookerData.getTempValue() - 10);
                        efFireGear = data[1];
                        efTempGear = 0;
                    }else {
                        if (mEFTempCtrlStrategyManager != null) {
                            mEFTempCtrlStrategyManager.recyle();
                            mEFTempCtrlStrategyManager = null;
                        }
                        efFireGear = (byte) (efCookerData.getFireValue() == -1? 0 : efCookerData.getFireValue());
                        efTempGear = (byte) (efCookerData.getTempValue() == -1? 0 : efCookerData.getTempValue());
                    }

                    eMode = InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_UNION;
                    fMode = InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_UNION;

                    eFireGear = efFireGear;
                    fFireGear = efFireGear;

                    eTempGear = efTempGear;
                    fTempGear = efTempGear;

                }


            }
        }


        boolean isHoodSettingModeOn = SettingPreferencesUtil.getHoodSettingModeStatus(context).equals(CataSettingConstant.HOOD_SETTING_MODE_OPEN) ?true:false;
        boolean isHoodConnectivity =  SettingPreferencesUtil.getStablishConnectionSwitchStatus(context).equals(CataSettingConstant.STABLISH_CONNECTION_SWITCH_STATUS_OPEN) ? true : false;
        if (isHoodSettingModeOn && isHoodConnectivity) {
            aMode = 0x1E;
            bMode = 0x1E;
            abMode = 0x1E;
            cMode = 0x1E;
            dMode = 0x1E;
            eMode = 0x1E;
            fMode = 0x1E;
            efMode = 0x1E;
        }

        int enterPowerOffMode = SettingPreferencesUtil.getEnterPowerOffMode(context);
        boolean reqPowerOffModelDelay = enterPowerOffMode == CataSettingConstant.EnterPowerOffModelDelay;
        if (reqPowerOffModelDelay && !lastReqPowerOffModelDelay) {
            delayStart = SystemClock.elapsedRealtime();
        }
        lastReqPowerOffModelDelay = reqPowerOffModelDelay;

        lastEnterPowerOffMode = enterPowerOffMode;

        switch (enterPowerOffMode) {
            case CataSettingConstant.EnterPowerOffModelFromSerialPort:
                if (!GlobalVars.getInstance().isOkToSendE2()) {
                    aMode = (byte) 0xE1;
                } else {
                    //主动断开蓝牙温度计，避免进入低功耗后长时间没有断开蓝牙
                    EventBus.getDefault().post(new BluetoothEevent());
                    aMode = (byte) 0xE2;
                }
                break;
            case CataSettingConstant.EnterPowerOffModel:
                if (!GlobalVars.getInstance().isOkToSendE2()) {
                    aMode = (byte) 0xE1;
                } else {
                    //主动断开蓝牙温度计，避免进入低功耗后长时间没有断开蓝牙
                    EventBus.getDefault().post(new BluetoothEevent());

                    aMode = (byte) 0xE2;   // 按照 最新协议修改成E2，原来是D1 ，2019年5月11日15:32:35
                }
                break;
            case CataSettingConstant.EnterPowerOffModelDelay:
                if (SystemClock.elapsedRealtime() - delayStart >= 1000 * 60 * 2) {
                    if (!GlobalVars.getInstance().isOkToSendE2()) {
                        aMode = (byte) 0xE1;
                    } else {
                        //主动断开蓝牙温度计，避免进入低功耗后长时间没有断开蓝牙
                        EventBus.getDefault().post(new BluetoothEevent());
                        aMode = (byte) 0xE2;
                    }
                } else {
                    aMode = (byte) 0xE1;
                }
                break;
            case CataSettingConstant.EnterHobIntroFragment:
                aMode = (byte) 0xE1;
                break;
            case CataSettingConstant.EnterLanguageSettingFragment:
                aMode = (byte) 0xF1;
                break;
            case CataSettingConstant.EnterDateSettingFragment:
                aMode = (byte) 0xF2;
                break;
            case CataSettingConstant.EnterTimeSettingFragment:
                aMode = (byte) 0xF3;
                break;
        }

        if (GlobalVars.getInstance().isPlaySplashVideo()) {
            aMode = 0;
            bMode = 0;
            cMode = 0;
            dMode = 0;
            eMode = 0;
            fMode = 0;
        }

        HoodData hoodData = DatabaseHelper.getHoodData();
        if (hoodData != null) {
            fanGear = (byte) (hoodData.getFanGear() & 0xFF);
            lightGear = (byte) (hoodData.getLightGear() & 0xFF);
            if(isHoodConnectivity){  // 允许联动
                fanGear=(byte)(0x60|fanGear);
         //       LogUtil.d("liang activity the hod connectivity ");
            }else {                 // 禁止联动
                fanGear=(byte)(0xbf&fanGear);
               // LogUtil.d("liang not activity the hod connectivity ");
            }
        }
      boolean  mTimerIsWorking= SettingPreferencesUtil.getTimerOpenStatus(context).equals(CataSettingConstant.TIMER_STATUS_OPEN) ? true :false;
        if(mTimerIsWorking){
            fanGear=(byte)(fanGear|0x30);   // 有定时进行中 根据最新协议 原来是0x20 ，2019年7月1日21:42:59 ，
        }else {
            fanGear=(byte)(fanGear&0xef);   // 无定时进行中,原来是0xdf；2019年7月1日21:50:46
        }

        if(!isHoodConnectivity){  // 连接开关 关闭
            lightGear=(byte)0x80;          // 关闭时，风机、灯光 都没有档位发送。
            fanGear=(byte)0xa0;   // 原来是 0x80 2019年7月1日21:51:41
        }
        if(GlobalVars.getInstance().isPause()){
            fanGear=(byte)(fanGear&0xf0);
        }

        if ((fanGear & 0x0F) > 0) {
            // 烟机挡位不为0时，不进入待机模式
            CataTFTHobApplication.getInstance().updateLatestTouchTime();
        }

        //precise

      /*  if (!fastMessage.isEmpty()) {
            EventBus.getDefault().post(new NTCEvent(fastMessage));
        }*/

        EventBus.getDefault().post(new NTCEvent(fastMessage));

        if (changingByte == (byte)0xFF) {
            changingByte = 0;
        } else {
            changingByte++;
        }

        if (TFTHobConfiguration.TFT_HOB_TYPE == TFTHobConfiguration.TFT_HOB_TYPE_60) {
            DetectCookerEvent detectCookerEvent = new DetectCookerEvent(//三个炉头同时温度档时，暂停恢复会出现三个B档的情况，暂时还没想到办法解决，先如果出现三个B档时不要去检锅，后续有更好方法再替换
                    (((aMode == 2) || (aMode == 3)) && ((aFireGear == 0) || (aFireGear == 10 && bFireGear == 10 && cFireGear == 10)))? false:true,
                    (((bMode == 2) || (bMode == 3)) && ((bFireGear == 0) || (aFireGear == 10 && bFireGear == 10 && cFireGear == 10)))? false:true,
                    (((cMode == 2) || (cMode == 3)) && ((cFireGear == 0) || (aFireGear == 10 && bFireGear == 10 && cFireGear == 10)))? false:true,
                    (((dMode == 2) || (dMode == 3)) && ((dFireGear == 0) || (aFireGear == 10 && bFireGear == 10 && cFireGear == 10)))? false:true,
                    (((eMode == 2) || (eMode == 3)) && ((eFireGear == 0) || (aFireGear == 10 && bFireGear == 10 && cFireGear == 10)))? false:true,
                    (((fMode == 2) || (fMode == 3)) && ((fFireGear == 0) || (aFireGear == 10 && bFireGear == 10 && cFireGear == 10)))? false:true
            );
            EventBus.getDefault().post(detectCookerEvent);
        }else if (TFTHobConfiguration.TFT_HOB_TYPE == TFTHobConfiguration.TFT_HOB_TYPE_80) {
           /* boolean causeByPowerOver = (
                    ((aMode == 2) || (aMode == 3)) && (aFireGear == 10 && bFireGear == 10)
            );
*/

            AllCookerData allCookerData = new AllCookerData(
                    aMode,  aFireGear,  aTempGear,
                    bMode, bFireGear, bTempGear,
                    cMode, cFireGear, cTempGear,
                    dMode,  dFireGear,  dTempGear,
                    eMode,  eFireGear, eTempGear,
                    fMode, fFireGear, fTempGear,
                    (byte) changingByte,fanGear,lightGear);

            boolean causeByPowerOver  = PowerLimitManager.checkPowerForDetectPan(allCookerData);
            LogUtil.d("causeByPowerOver---->" + causeByPowerOver);

            DetectCookerEvent detectCookerEvent = new DetectCookerEvent(//三个炉头同时温度档时，暂停恢复会出现三个B档的情况，暂时还没想到办法解决，先如果出现三个B档时不要去检锅，后续有更好方法再替换
                    (!causeByPowerOver || (((aMode == 2) || (aMode == 3) || (aMode == 5)) && (aFireGear == 0)))? false:true,
                    (!causeByPowerOver || (((bMode == 2) || (bMode == 3) || (bMode == 5)) && (bFireGear == 0)))? false:true,
                    (!causeByPowerOver || (((cMode == 2) || (cMode == 3) ) && (cFireGear == 0)))? false:true,
                    (!causeByPowerOver || (((dMode == 2) || (dMode == 3) ) && (dFireGear == 0)))? false:true,
                    (!causeByPowerOver || (((eMode == 2) || (eMode == 3) || (eMode == 5)) && (eFireGear == 0)))? false:true,
                    (!causeByPowerOver || (((fMode == 2) || (fMode == 3) || (fMode == 5)) && (fFireGear == 0)))? false:true
            );
            EventBus.getDefault().post(detectCookerEvent);

        }else if (TFTHobConfiguration.TFT_HOB_TYPE == TFTHobConfiguration.TFT_HOB_TYPE_90) {
           /* boolean causeByPowerOver = (
                    ((aMode == 2) || (aMode == 3)) && (aFireGear == 10 && bFireGear == 10)) || (((fMode == 2) || (fMode == 3)) && (eFireGear == 10 && fFireGear == 10));
            */
            AllCookerData allCookerData = new AllCookerData(
                    aMode,  aFireGear,  aTempGear,
                    bMode, bFireGear, bTempGear,
                    cMode, cFireGear, cTempGear,
                    dMode,  dFireGear,  dTempGear,
                    eMode,  eFireGear, eTempGear,
                    fMode, fFireGear, fTempGear,
                    (byte) changingByte,fanGear,lightGear);

            boolean causeByPowerOver  = PowerLimitManager.checkPowerForDetectPan(allCookerData);

            DetectCookerEvent detectCookerEvent = new DetectCookerEvent(//三个炉头同时温度档时，暂停恢复会出现三个B档的情况，暂时还没想到办法解决，先如果出现三个B档时不要去检锅，后续有更好方法再替换
                    (!causeByPowerOver || (((aMode == 2) || (aMode == 3) || (aMode == 5)) && (aFireGear == 0)))? false:true,
                    (!causeByPowerOver || (((bMode == 2) || (bMode == 3) || (bMode == 5)) && (bFireGear == 0)))? false:true,
                    (!causeByPowerOver || (((cMode == 2) || (cMode == 3)) && (cFireGear == 0)))? false:true,
                    (!causeByPowerOver || (((dMode == 2) || (dMode == 3) || (dMode == 5)) && (dFireGear == 0)))? false:true,
                    (!causeByPowerOver || (((eMode == 2) || (eMode == 3) || (eMode == 5)) && (eFireGear == 0)))? false:true,
                    (!causeByPowerOver || (((fMode == 2) || (fMode == 3) || (fMode == 5)) && (fFireGear == 0)))? false:true
            );
            EventBus.getDefault().post(detectCookerEvent);

        }




        return TFTDataUtil.makeFTFDataModel(
                aMode,  aFireGear,  aTempGear,
                bMode, bFireGear, bTempGear,
                cMode, cFireGear, cTempGear,
                dMode,  dFireGear,  dTempGear,
                eMode,  eFireGear, eTempGear,
                fMode, fFireGear, fTempGear,
                (byte) changingByte,fanGear,lightGear);
    }

    private static byte[] getPIDDataForPrecise(TempControlManager mTempControlManager, int settingTempValue, int currentTempValue) {

        if (mTempControlManager == null) return null;
        byte[] datas = new byte[2];
        if (settingTempValue == -1 || settingTempValue == 0) {
            datas[0] = InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FIRE_GEAR;
            datas[1] = 0;
        }else {
            LogUtil.d("PIDTEST--setting value---->" + settingTempValue + "---current value---->" + currentTempValue);
            float out = mTempControlManager.calPID(settingTempValue,currentTempValue);
            datas[0] = InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FIRE_GEAR;
            if (out > 1000) datas[1] = 9;
            else if (out <= 1000 && out > 600) datas[1] = 9;
            else if (out <= 600 && out > 500) datas[1] = 8;
            else if (out <= 500 && out > 400) datas[1] = 8;
            else if (out <= 400 && out > 300) datas[1] = 7;
            else if (out <= 300 && out > 200) datas[1] = 7;
            else if (out <= 200 && out > 100) datas[1] = 5;
            else if (out <= 100 && out > 50) datas[1] = 3;
            else if (out <= 50 && out > 0) datas[1] = 2;
            else datas[1] = 1;
            LogUtil.i("PIDTEST--Power---->" + datas[1]);
        }

        return datas;
    }

    private static byte[] getPIDDataForFast(TempControlManager mTempControlManager, int settingTempValue, int currentTempValue) {

        if (mTempControlManager == null) return null;
        byte[] datas = new byte[2];
        if (settingTempValue == -1 || settingTempValue == 0) {
            datas[0] = InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FIRE_GEAR;
            datas[1] = 0;
        }else {

            LogUtil.d("PIDTEST--setting value---->" + settingTempValue + "---current value---->" + currentTempValue);
            float out = mTempControlManager.calPIDForFastMode(settingTempValue,currentTempValue);
            datas[0] = InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FIRE_GEAR;
            if (out > 1000) datas[1] = 9;
            else if (out <= 1000 && out > 600) datas[1] = 9;
            else if (out <= 600 && out > 500) datas[1] = 8;
            else if (out <= 500 && out > 400) datas[1] = 8;
            else if (out <= 400 && out > 300) datas[1] = 7;
            else if (out <= 300 && out > 200) datas[1] = 7;
            else if (out <= 200 && out > 100) datas[1] = 6;
            else if (out <= 100 && out > 50) datas[1] = 6;
            else if (out <= 50 && out > 0) datas[1] = 2;
            else datas[1] = 1;
            LogUtil.i("PIDTEST--Power---->" + datas[1]);
        }

        return datas;
    }

    private static byte[] getPreciseSettingData(int settingTempValue,int currentTempValue) {
        currentTempValue = TFTHobTemperatureValue.getPreciseCurrentTempValue(settingTempValue,currentTempValue);
        LogUtil.d("samhung----settingTemp--->" + settingTempValue + "----currentTemp---->" + currentTempValue);
        byte[] datas = new byte[2];
        datas[0] = InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FIRE_GEAR;
        if (settingTempValue == -1) {
            datas[1] = 0;
        }else {
            if (settingTempValue <= currentTempValue) {
                datas[1] = 1;

            }else if (settingTempValue > currentTempValue) {
                if (settingTempValue - currentTempValue < 10) {
                    if (settingTempValue - currentTempValue < 5) {
                        if (settingTempValue - currentTempValue < 2) {
                            datas[1] = 2;//6
                        }else {
                            datas[1] = 7;
                        }

                    }else {
                        datas[1] = 8;
                    }

                }else {
                    datas[1] = 9;
                }

            }



        /*    if (settingTempValue - currentTempValue < 10) {
                if (settingTempValue - currentTempValue < 5) {
                    if (settingTempValue - currentTempValue < 3) {
                        datas[1] = 1;
                    }else {
                        datas[1] = 5;
                    }

                }else {
                    datas[1] = 8;
                }

            }else {
                datas[1] = 9;
            }*/
        }




        return datas;
    }

    private static byte[] getFastSettingData(int settingTempValue,int currentTempValue) {
        LogUtil.d("samhung----fast settingTemp--->" + settingTempValue + "----fast currentTemp---->" + currentTempValue);
        byte[] datas = new byte[2];
        datas[0] = InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FIRE_GEAR;
        if (settingTempValue == -1) {
            datas[1] = 0;
        }else {
            if (settingTempValue <= currentTempValue) {
                datas[1] = 1;

            }else if (settingTempValue > currentTempValue) {
                if (settingTempValue - currentTempValue < 10) {
                    if (settingTempValue - currentTempValue < 5) {
                        if (settingTempValue - currentTempValue < 2) {
                            datas[1] = 6;//1
                        }else {
                            datas[1] = 7;
                        }

                    }else {
                        datas[1] = 8;
                    }

                }else {
                    datas[1] = 9;
                }

            }

        }

        return datas;
    }

    private static byte[] getTempCtrlData(TempCtrlStrategyManager manager,int currentTempValue,int tTargetTemp) {
        byte[] datas = new byte[2];
        if (manager.getTargetTemp() != tTargetTemp) manager.setTartgetTemp(tTargetTemp);
        int powerLevel = manager.getPowerLevel(currentTempValue);
        if (powerLevel == 0) {
            //datas[0] = InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_SETTING;
            datas[0] = InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FIRE_GEAR;
        }else {
            datas[0] = InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FIRE_GEAR;
        }
        datas[1] = (byte) powerLevel;
        LogUtil.d("Enter:: getTempCtrlData--powerlevel--->" + powerLevel);
        return datas;
    }

    private static byte[] getFastTempCtrlData(FastTempCtrlStrategyManager manager,int currentTempValue,int tTargetTemp) {
        byte[] datas = new byte[2];
        if (manager.getTargetTemp() != tTargetTemp) manager.setTartgetTemp(tTargetTemp);
        int powerLevel = manager.getPowerLevel(currentTempValue);
        if (powerLevel == 0) {
            //datas[0] = InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_SETTING;
            datas[0] = InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FIRE_GEAR;
        }else {
            datas[0] = InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FIRE_GEAR;
        }
        datas[1] = (byte) powerLevel;
        LogUtil.d("Enter:: getTempCtrlData--powerlevel--->" + powerLevel);
        return datas;
    }
}
