package com.ekek.tfthobmodule.core;

import android.content.Context;
import android.os.Message;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.commonmodule.utils.Logger;
import com.ekek.hardwaremodule.constants.CookingTimeTable;
import com.ekek.hardwaremodule.entity.CookerHardwareResponse;
import com.ekek.hardwaremodule.entity.CookerMessage;
import com.ekek.hardwaremodule.event.PowerSwitchStateChangedEvent;
import com.ekek.hardwaremodule.protocol.CookerErrorCode;
import com.ekek.hardwaremodule.protocol.InductionCookerProtocol;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.database.SettingPreferencesUtil;
import com.ekek.settingmodule.model.GetTestCookwareResult;
import com.ekek.tfthobmodule.CataTFTHobApplication;
import com.ekek.tfthobmodule.R;
import com.ekek.tfthobmodule.constants.TFTHobConstant;
import com.ekek.tfthobmodule.constants.TFTHobTemperatureValue;
import com.ekek.tfthobmodule.data.CookerSettingData;
import com.ekek.tfthobmodule.database.SettingDbHelper;
import com.ekek.tfthobmodule.entity.CookerDataTable;
import com.ekek.tfthobmodule.entity.CookerDataTableDao;
import com.ekek.tfthobmodule.event.BleTempEvent;
import com.ekek.tfthobmodule.event.CookerUnionUpdateEvent;
import com.ekek.tfthobmodule.event.CookerUpdateEvent;
import com.ekek.tfthobmodule.event.DetectCookerEvent;
import com.ekek.tfthobmodule.event.ShowErrorEvent;
import com.ekek.tfthobmodule.event.SoundEvent;
import com.ekek.tfthobmodule.event.TempSensorRequestEvent;
import com.ekek.tfthobmodule.event.TipsEvent;
import com.ekek.tfthobmodule.event.UnionEvent;
import com.ekek.tfthobmodule.utils.SoundUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 管理处理每个炉头的操作
 * */
public class CookerManager {
    private static final int HANDLER_SIMULATE_CHECK_TEMP_SENSOR_READY = 0;
    private static final int TIME_FOR_SIMULATE_CHECK_TEMP_SENSOR_READY = 0 * 1000;
    private static final int HANDLER_BLINK_WHEN_NO_PAN = 1;
    private static final int HANDLER_UPDATE_COOKER_TIMER = 2;
    private static final int HANDLER_BLINK_WHEN_NO_PAN_DELAY_VALUE=500;  // 更新 2019年1月16日9:21:34
    private static final int Test_Cookware_Has_Pan=1;
    private static final int Test_Cookware_No_Pan=0;
    private static final int COUNT_DOWN_TIME_FOR_NO_RESPONE_FOR_READY_TO_COOK = 15;//15分钟
    private static final int COUNT_DOWN_TIME_FOR_FAST_MODE_TEMP = 60 * 60 * 3;//20分钟
    private int cookerID;
    private int fireValue,tempValue,realTempValue,tempMode,remainHourValue,remainMinuteValue,remainSecondValue;
    private int workMode = TFTHobConstant.HOB_WORK_MODE_POWER_OFF;
    private int hardwareWorkMode = InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_SETTING;
    private int settingFireValue,settingTempValue,settingTempMode,settingHourValue,settingMinuteValue,settingSecondValue;
    private int settingTempIndicatorResID,settingRecipesResID ,settingTempShowOrder;
    private int tempIndicatorResID,recipesResID;
    private int countErrorMessageNumber=0;
    private String errorMessage = "";
    private String currentErrorMessage = "";
    private boolean isHighTemp = false;
    private boolean hasPan = true;
    private boolean needToRecoverCookerUIForNoPan = false;
    private boolean isNeedToRecoverCookerUIForErrorDimiss = false;
    private boolean isCookerPowerOn = false;

    private int saveFireValue,saveTempValue,saveTempMode,saveRemainHourValue,saveRemainMinuteValue,saveRemainSecondValue;
    private int lastSaveFireValue,lastSaveTempValue,lastSaveTempMode,lastSaveRemainHourValue,lastSaveRemainMinuteValue,lastSaveRemainSecondValue;
    private int saveHardwareWorkMode;
    private int lastSaveHardwareWorkMode;
    private int saveWorkMode;
    private int lastSaveWorkMode;
    private int saveTempIndicatorResID;
    private int lastSaveTempIndicatorResID;
    private int saveRecipesResID;
    private int lastSaveRecipesResID;
    private boolean saveHighTempFlag;
    private boolean savePanFlag;
    private boolean lastSavePanFlag;
    private String saveErrorMessage;
    private boolean savePowerOnFlag;
    private boolean lastSavePowerOnFlag;
    private boolean saveRecoverFlag;

    private Disposable mDisposable;//定时器
    private Disposable mFiveMinuteDisposable;//定时器
    private int remainFiveMinuteHourValue = 5;
    private long noPanStartTime = 0;
    private boolean isPause = false;
    private boolean needPauseForSetting = false;
    private Context context;
    private int ntcTempValue;
    private boolean blinkWhenNoPan=false;

    private boolean needToDetectCooker = true;//false 不需要检锅，true需要检锅 。 工作时如果是0档，则不需要检锅

    private CookerTimerManager mCookerTimerManager;

    private int deltaTempForFastMode = 0;//快速控温 温度补偿
    private int compensateTmepForFastMode = 0;
    private boolean timerStartedWhenPause = false;

    private android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_SIMULATE_CHECK_TEMP_SENSOR_READY:

                    EventBus.getDefault().post(new TempSensorRequestEvent(cookerID , TempSensorRequestEvent.ACTION_FIND_AND_CONNECT_TEMP_SENSOR));
                    EventBus.getDefault().post(new TipsEvent(context.getString(R.string.tfthobmodule_tips_message_search_and_connect_temp_sensor)));


                   /* setWorkMode(msg.arg1);

                    CookerDataTableDao dao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
                    CookerDataTable cookerData = dao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(cookerID)).build().unique();
                    if (cookerData != null) {
                        cookerData.setWorkMode(workMode);
                        dao.update(cookerData);
                    }
                    notifyUpdateCookerUI();*/
                    break;
                case HANDLER_BLINK_WHEN_NO_PAN:
                    blinkWhenNoPan=!blinkWhenNoPan;
                    break;
                case HANDLER_UPDATE_COOKER_TIMER:
                    updateTimerRemainTime();
                    break;
            }
        }
    };

    private void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void unregisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this);

    }

    public CookerManager(int cookerID , Context context) {
        this.cookerID = cookerID;
        this.context = context;
        init();
    }

    private void init() {
        registerEventBus();
        mCookerTimerManager = new CookerTimerManager();
        fireValue = -1;
        tempValue = -1;
        tempMode = -1;
        remainHourValue = -1;
        remainMinuteValue = -1;
        remainSecondValue = -1;

        saveFireValue = -1;
        saveTempValue = -1;
        saveTempMode = -1;
        saveRemainHourValue = -1;
        saveRemainMinuteValue = -1;
        saveRemainSecondValue = -1;

        settingFireValue = -1;
        settingTempValue = -1;
        settingTempMode = -1;
        settingHourValue = -1;
        settingMinuteValue = -1;
        settingSecondValue = -1;

        settingTempIndicatorResID = -1;
        settingRecipesResID = -1;
        settingTempShowOrder = -1;
        tempIndicatorResID = -1;
        recipesResID = -1;

        saveTempIndicatorResID = -1;
        saveRecipesResID = -1;
    }

    private void notifyNTCReadyToCook() {
        if (workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR) {
            setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_SENSOR_READY);
            saveWorkMode = TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_SENSOR_READY;
            CookerDataTableDao dao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
            CookerDataTable cookerData = dao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(cookerID)).build().unique();
            if (cookerData != null) {
                cookerData.setSaveWorkMode(saveWorkMode);
                cookerData.setWorkMode(workMode);
                dao.update(cookerData);
            }
            int soundType = SoundEvent.getSoundType(cookerID, false);
            EventBus.getDefault().post(new SoundEvent(
                    SoundEvent.SOUND_ACTION_PLAY,
                    SoundUtil.SOUND_ID_ALARM_TIMER,
                    soundType));

            Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PLAY, SoundUtil.SOUND_ID_ALARM, " + soundType + ")");
            doStopAutoSwitchOffTimeForReadyToCook();
            doStartAutoSwitchOffTimeForReadyToCook();
            doStopFastModeTemp();
        }
    }

    private void notifyTempSersorReadyToCook() {
        if (workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR) {
            setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_SENSOR_READY);
            saveWorkMode = TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_SENSOR_READY;
            CookerDataTableDao dao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
            CookerDataTable cookerData = dao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(cookerID)).build().unique();
            if (cookerData != null) {
                cookerData.setSaveWorkMode(saveWorkMode);
                cookerData.setWorkMode(workMode);
                dao.update(cookerData);
            }
            int soundType = SoundEvent.getSoundType(cookerID, false);
            EventBus.getDefault().post(new SoundEvent(
                    SoundEvent.SOUND_ACTION_PLAY,
                    SoundUtil.SOUND_ID_ALARM,
                    soundType));
            Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PLAY, SoundUtil.SOUND_ID_ALARM, " + soundType + ")");
            doStopAutoSwitchOffTimeForReadyToCook();
            doStartAutoSwitchOffTimeForReadyToCook();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UnionEvent event) {

        //needPauseForSetting = true;
        //unregisterEventBus();
        //LogUtil.d("liang samhung1--------------onMessageEvent");

    }


    long timeForCheckTemp = 0;
    long timeForCorrectTemp = 0;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BleTempEvent event) {
        LogUtil.d("Enter:: onMessageEvent---cookerID--->" + cookerID + "----event.getCookerID()---->" + event.getCookerID() + "---temp--->" + event.getTempValue());
        boolean isSameCooker = false;
        int bleCookerID = event.getCookerID();
        LogUtil.d("bleCookerID---->" + bleCookerID);
        if (bleCookerID > 100) {
            if (bleCookerID % 10 == cookerID || bleCookerID / 10 == cookerID) {
                isSameCooker = true;
            }
        }else {
            isSameCooker = bleCookerID == cookerID;
        }



       // if (isSameCooker && workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR && tempMode == TFTHobConstant.TEMP_MODE_PRECISE_TEMP) {
        if (isSameCooker && tempMode == TFTHobConstant.TEMP_MODE_PRECISE_TEMP) {
            boolean handled = false;
            if (GlobalVars.getInstance().isInDebugMode()) {
                switch (GlobalVars.getInstance().getDebugModeExtra()) {
                    case CataSettingConstant.DEBUG_MODE_EXTRA_PARAM_IGNORE_ERROR_AND_NO_PAN:
                        realTempValue += 5;
                        if (realTempValue > 118) {
                            realTempValue = 118;
                        }
                        handled = true;
                        break;
                }
            }

            if (!handled) {



                //为了避免煮水到了90多度的时候温度再也上不去的问题，当温度已经达到90度之后，并且长时间没有增加的趋势（5分钟），则手动补偿温度直接达到设置温度

                if (workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR && settingTempValue > 90 && realTempValue > 90 && realTempValue < event.getTempValue()) {
                    timeForCheckTemp = System.currentTimeMillis();
                    timeForCorrectTemp = System.currentTimeMillis();

                }

              //  realTempValue = event.getTempValue();
                if (realTempValue < event.getTempValue()) realTempValue = event.getTempValue();

                //为了避免煮水到了90多度的时候温度再也上不去的问题，当温度已经达到90度之后，并且长时间没有增加的趋势（5分钟），则手动补偿温度直接达到设置温度
                if (workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR && settingTempValue > 90 ) {
                    if (timeForCheckTemp > 0 && System.currentTimeMillis() - timeForCheckTemp >= 5 * 60 * 1000) {//

                        if (timeForCorrectTemp > 0 && System.currentTimeMillis() - timeForCorrectTemp >= 45 * 1000) {//1 * 60 * 1000
                            realTempValue = realTempValue + 1;
                            timeForCorrectTemp = System.currentTimeMillis();
                        }

                    }
                }


            }
            LogUtil.d("Enter::BleTempEvent----handle--->" + handled + "----real--->" + realTempValue);
            if (realTempValue >= settingTempValue
                    && workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR
                    && !GlobalVars.getInstance().isConfiguringCooker()) {//ready to cook
                //realTempValue = settingTempValue;
                setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_SENSOR_READY);
                saveWorkMode = TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_SENSOR_READY;
                CookerDataTableDao dao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
                CookerDataTable cookerData = dao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(cookerID)).build().unique();
                if (cookerData != null) {
                    cookerData.setSaveWorkMode(saveWorkMode);
                    cookerData.setWorkMode(workMode);
                    dao.update(cookerData);
                }

                int soundType = SoundEvent.getSoundType(cookerID, false);
                EventBus.getDefault().post(new SoundEvent(
                        SoundEvent.SOUND_ACTION_PLAY,
                        SoundUtil.SOUND_ID_ALARM,
                        soundType));
                LogUtil.d("Enter:: real temp > setting");
                Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PLAY, SoundUtil.SOUND_ID_ALARM, " + soundType + ")");
                doStopAutoSwitchOffTimeForReadyToCook();
                doStartAutoSwitchOffTimeForReadyToCook();
            }

            notifyUpdateCookerUI();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DetectCookerEvent event) {
        needToDetectCooker = event.getDetectCookerResult(cookerID);
//        LogUtil.d("Enter::DetectCookerEvent---id--->" + cookerID + "----->" + needToDetectCooker);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TempSensorRequestEvent event) {
        //LogUtil.d("Enter:: TempSensorRequestEvent--------1--------->");
        int id = SettingDbHelper.getTemperatureSensorStatus();
        String idStr = String.valueOf(id);
        //LogUtil.d("Enter:: TempSensorRequestEvent-----idStr--->" + idStr + "------cookerID---->" + cookerID);
        if (idStr.contains(String.valueOf(cookerID))) {
           // LogUtil.d("Enter:: TempSensorRequestEvent--------2--------->");
        //if (id == cookerID) {
            int action = event.getAction();
            if (action == TempSensorRequestEvent.ACTION_TEMP_SENSOR_READY_TO_WORK) {
                setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_SENSOR_READY);
                CookerDataTableDao dao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
                CookerDataTable cookerData = dao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(cookerID)).build().unique();
                if (cookerData != null) {
                    cookerData.setWorkMode(workMode);
                    dao.update(cookerData);
                }
                notifyUpdateCookerUI();

            }else if (action == TempSensorRequestEvent.ACTION_TEMP_SENSOR_CONNECT_FAIL) {

            }
        }


    }

    private boolean poweredOn = true;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PowerSwitchStateChangedEvent event) {
        int enterPowerMode = SettingPreferencesUtil.getEnterPowerOffMode(context);
        switch (event.getNewState()) {
            case CookerHardwareResponse.POWER_SWITCH_OFF:
                switch (enterPowerMode) {
                    case CataSettingConstant.EnterNone:
                    case CataSettingConstant.EnterPowerOnModel:
                    case CataSettingConstant.EnterHobWorkingFragment:
                        GlobalVars.getInstance().setClickPowerOffButton(true);
                        notifyCookerPowerOff();
                        poweredOn = false;
                        break;
                    case CataSettingConstant.EnterPowerOffModelDelay:
                    case CataSettingConstant.EnterLanguageSettingFragment:
                    case CataSettingConstant.EnterDateSettingFragment:
                    case CataSettingConstant.EnterTimeSettingFragment:
                    case CataSettingConstant.EnterHobIntroFragment:
                    case CataSettingConstant.EnterPowerOffModelFromSerialPort:
                        break;
                }
                break;
            case CookerHardwareResponse.POWER_SWITCH_OFF_2:
                switch (enterPowerMode) {
                    case CataSettingConstant.EnterNone:
                    case CataSettingConstant.EnterPowerOnModel:
                    case CataSettingConstant.EnterHobWorkingFragment:
                    case CataSettingConstant.EnterLanguageSettingFragment:
                    case CataSettingConstant.EnterDateSettingFragment:
                    case CataSettingConstant.EnterTimeSettingFragment:
                    case CataSettingConstant.EnterPowerOffModelFromSerialPort:
                        break;
                    case CataSettingConstant.EnterPowerOffModelDelay:
                    case CataSettingConstant.EnterHobIntroFragment:
                        notifyCookerPowerOff();
                        poweredOn = false;
                        break;
                }
                break;
            case CookerHardwareResponse.POWER_SWITCH_ON:
                switch (event.getOldState()) {
                    case CookerHardwareResponse.POWER_SWITCH_UNKNOWN:
                    case CookerHardwareResponse.POWER_SWITCH_00:
                    case CookerHardwareResponse.POWER_SWITCH_11:
                    case CookerHardwareResponse.POWER_SWITCH_88:
                    case CookerHardwareResponse.POWER_SWITCH_99:
                    case CookerHardwareResponse.POWER_SWITCH_OFF:
                    case CookerHardwareResponse.POWER_SWITCH_OFF_2:
                        poweredOn = true;
                        resetErrorDetectProcess();
                        break;
                }
                break;
            case CookerHardwareResponse.POWER_SWITCH_UNKNOWN:
                break;
            case CookerHardwareResponse.POWER_SWITCH_00:
            case CookerHardwareResponse.POWER_SWITCH_11:
            case CookerHardwareResponse.POWER_SWITCH_99:
                switch (event.getOldState()) {
                    case CookerHardwareResponse.POWER_SWITCH_UNKNOWN:
                        poweredOn = true;
                        resetErrorDetectProcess();
                        break;
                }
                break;
            case CookerHardwareResponse.POWER_SWITCH_88:
                switch (event.getOldState()) {
                    case CookerHardwareResponse.POWER_SWITCH_UNKNOWN:
                    case CookerHardwareResponse.POWER_SWITCH_OFF:
                        poweredOn = true;
                        resetErrorDetectProcess();
                        break;
                }
                break;
        }
    }

    public void notifyCookerTempSensorReady() {
        setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_SENSOR_READY);
        CookerDataTableDao dao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
        CookerDataTable cookerData = dao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(cookerID)).build().unique();
        if (cookerData != null) {
            cookerData.setWorkMode(workMode);
            dao.update(cookerData);
        }
        notifyUpdateCookerUI();
    }

    public int getCookerID() {
        return cookerID;
    }

    public void setCookerID(int cookerID) {
        this.cookerID = cookerID;
    }



    public void notifyUpdateCookerMessage(CookerHardwareResponse response) {
        if (isPause) {
            return;
        }
        if (needPauseForSetting) {
            return;
        }

        CookerMessage cookerMessage = null;
        CookerMessage cookerMessage2;
        if (cookerID == TFTHobConstant.COOKER_TYPE_A_COOKER) {
            cookerMessage = response.getaCookerMessage();

        }else if (cookerID == TFTHobConstant.COOKER_TYPE_B_COOKER) {
            cookerMessage = response.getbCookerMessage();
        }else if (cookerID == TFTHobConstant.COOKER_TYPE_C_COOKER) {
            cookerMessage = response.getcCookerMessage();
        }else if (cookerID == TFTHobConstant.COOKER_TYPE_D_COOKER) {
            cookerMessage = response.getdCookerMessage();
        }else if (cookerID == TFTHobConstant.COOKER_TYPE_E_COOKER) {
            cookerMessage = response.geteCookerMessage();
        }else if (cookerID == TFTHobConstant.COOKER_TYPE_F_COOKER) {
            cookerMessage = response.getfCookerMessage();
        }else if (cookerID == TFTHobConstant.COOKER_TYPE_AB_COOKER) {
            cookerMessage = response.getaCookerMessage();
            cookerMessage2 = response.getbCookerMessage();
            cookerMessage.setHighTemp(cookerMessage.isHighTemp() && cookerMessage2.isHighTemp());
        }else if (cookerID == TFTHobConstant.COOKER_TYPE_EF_COOKER) {
            cookerMessage = response.geteCookerMessage();
            cookerMessage2 = response.getfCookerMessage();
            cookerMessage.setHighTemp(cookerMessage.isHighTemp() && cookerMessage2.isHighTemp());
        }
        if (cookerMessage == null) {
            return;
        }

        ntcTempValue = cookerMessage.getTempValue();
        if (tempMode == TFTHobConstant.TEMP_MODE_FAST_TEMP || tempMode == TFTHobConstant.TEMP_MODE_FAST_TEMP_GEAR_WITHOUT_SENSOR) {
            /**
             * 开启快速控温温度补偿，原理是定时10分钟，每分钟加上固定的补偿温度如1度，让current temp value 尽快达到设置温度值，避免由于ntc回传温度值误差造成长时间无法到达设置温度
             * 减少用户等待的时间。这只是改变UI显示而已 ，但实际加热算法还是参考Ntc实际温度来做加热策略
             * */
            if (workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR && !GlobalVars.getInstance().isConfiguringCooker()) {
                //if (fastModeTempTaskID == -1 && (settingTempValue - ntcTempValue ) <= 30) {
                if (fastModeTempTaskID == -1) {
                    deltaTempForFastMode = (settingTempValue - ntcTempValue) / 10;
                    LogUtil.d("start delta temp for fast mode");
                    LogUtil.d("settingTempValue---->" + settingTempValue + "-----ntcTempValue---->" + ntcTempValue);
                    LogUtil.d("Enter: fast mode deltaTempForFastMode--->" + deltaTempForFastMode);
                    doStopFastModeTemp();
                    doStartFastModeTemp(settingTempValue - ntcTempValue);
                }




                if (cookerID == TFTHobConstant.COOKER_TYPE_AB_COOKER) {
                   int aTempValue = TFTHobTemperatureValue.getNTCTempValue(tempValue,response.getaCookerMessage().getTempValue());
                   aTempValue = TFTHobTemperatureValue.getNTCCurrentTempValue(tempValue,aTempValue);//补偿Ntc温度

                    int bTempValue = TFTHobTemperatureValue.getNTCTempValue(tempValue,response.getbCookerMessage().getTempValue());
                    bTempValue = TFTHobTemperatureValue.getNTCCurrentTempValue(tempValue,bTempValue);//补偿Ntc温度

                    if (Math.abs(aTempValue - bTempValue) > 10) {//A炉，B炉温度相差太大，则取大的值
                        if (aTempValue > bTempValue) realTempValue = aTempValue;
                        else realTempValue = bTempValue;
                    }else {//否则取平均值
                        realTempValue = (aTempValue + bTempValue) / 2;
                    }

                    LogUtil.d("Enter:: aTempValue--->" + aTempValue + "---bTempValue--->" + bTempValue + "----realTempValue---->" + realTempValue);

                }else if (cookerID == TFTHobConstant.COOKER_TYPE_EF_COOKER) {
                    int eTempValue = TFTHobTemperatureValue.getNTCTempValue(tempValue,response.geteCookerMessage().getTempValue());
                    eTempValue = TFTHobTemperatureValue.getNTCCurrentTempValue(tempValue,eTempValue);//补偿Ntc温度

                    int fTempValue = TFTHobTemperatureValue.getNTCTempValue(tempValue,response.getfCookerMessage().getTempValue());
                    fTempValue = TFTHobTemperatureValue.getNTCCurrentTempValue(tempValue,fTempValue);//补偿Ntc温度

                    if (Math.abs(eTempValue - fTempValue) > 10) {//A炉，B炉温度相差太大，则取大的值
                        if (eTempValue > fTempValue) realTempValue = eTempValue;
                        else realTempValue = fTempValue;
                    }else {//否则取平均值
                        realTempValue = (eTempValue + fTempValue) / 2;
                    }

                    LogUtil.d("Enter:: eTempValue--->" + eTempValue + "---fTempValue--->" + fTempValue + "----realTempValue---->" + realTempValue);

                }else {
                    realTempValue = TFTHobTemperatureValue.getNTCTempValue(tempValue,cookerMessage.getTempValue());
                    realTempValue = TFTHobTemperatureValue.getNTCCurrentTempValue(tempValue,realTempValue);//补偿Ntc温度
                }


                LogUtil.d("TEST FastMode----realTempValue--before->" + realTempValue + "---settingvalue--->" + settingTempValue);

                if (realTempValue < settingTempValue) {
                    //if (realTempValue + compensateTmepForFastMode < settingTempValue) realTempValue = realTempValue + compensateTmepForFastMode;
                    realTempValue = realTempValue + compensateTmepForFastMode;
                }
                if (realTempValue >= settingTempValue) notifyNTCReadyToCook();
                LogUtil.d("TEST FastMode----realTempValue--after->" + realTempValue + "-----compensateTmepForFastMode--->" + compensateTmepForFastMode);
            }

        }else {
            doStopFastModeTemp();
            deltaTempForFastMode = 0;
            compensateTmepForFastMode = 0;
        }

        //ntcTempValue = TFTHobTemperatureValue.getNTCTempValue(cookerMessage.getTempValue());
        //if (ntcTempValue >= settingTempValue) notifyNTCReadyToCook();
        saveSettingData();//saveNTC Temp value
        if (poweredOn) {//开机 0x55
            //如果无区模式错误代码：
            // 1. 两个小炉头都有相同的错误代码，则AB炉显示A炉错误代码，EF炉显示E炉错误代码
            // 2. 两个小炉头有不同的错误代码，则AB炉显示A炉错误代码，EF炉显示E炉错误代码
            // 3. 两个小炉头只有一个炉头有错误代码，则显示其中有错误代码的炉头的错误代码
            int errorCode = (cookerMessage.getErrorCode() & 0xFF);
            if (cookerID == TFTHobConstant.COOKER_TYPE_AB_COOKER) {
                if (errorCode == CookerErrorCode.COOKER_ERROR_NORMAL) {
                    int code = (response.getbCookerMessage().getErrorCode() & 0xFF);
                    if (code > CookerErrorCode.COOKER_ERROR_NORMAL) {//如果A炉没有错误代码，则判断B炉是否有错误代码，如有错误代码，则把B炉错误代码赋值给errorCode
                        errorCode = code;
                    }
                }


            }else if (cookerID == TFTHobConstant.COOKER_TYPE_EF_COOKER) {
                if (errorCode == CookerErrorCode.COOKER_ERROR_NORMAL) {
                    int code = (response.getfCookerMessage().getErrorCode() & 0xFF);
                    if (code > CookerErrorCode.COOKER_ERROR_NORMAL) {//如果E炉没有错误代码，则判断B炉是否有错误代码，如有错误代码，则把E炉错误代码赋值给errorCode
                        errorCode = code;
                    }
                }
            }else {
                errorCode = (cookerMessage.getErrorCode() & 0xFF);
            }

            if (GlobalVars.getInstance().isInDebugMode()) {
                switch (GlobalVars.getInstance().getDebugModeExtra()) {
                    case CataSettingConstant.DEBUG_MODE_EXTRA_PARAM_IGNORE_ERROR_AND_NO_PAN:
                    case CataSettingConstant.DEBUG_MODE_EXTRA_PARAM_IGNORE_ERROR:
                        errorCode = 0;
                        break;
                }
            }

            if (errorCode > CookerErrorCode.COOKER_ERROR_NORMAL) {
                // 有错误，优先处理错误
                countErrorMessageNumber++;
                if(countErrorMessageNumber >= 10){  // 连续收到 10 次相同的错误代码
                    errorMessage = CookerErrorCode.getErrorMessage(errorCode);
                    if (!errorMessage.equals(currentErrorMessage)) {
                        notifyCookerUpdateUIForErrorOccur(true);
                        currentErrorMessage = errorMessage;

                    }
                    if(countErrorMessageNumber%10==0){
                        notifyCookerUpdateUIForErrorOccur(false);
                    }
                  //  countErrorMessageNumber=0;
                } else {
                    errorMessage = "";
                    currentErrorMessage = "";
                }
            }else {
                //没有错误，处理异常，无锅，高温
                if (!errorMessage.equals("")) {
                    resetErrorDetectProcess();
                    isNeedToRecoverCookerUIForErrorDimiss = false;

                    // 发生错误时，炉头已经被关闭了，但显示了错误代码，现在错误消失时，
                    // 需要依靠下面的代码来将炉头彻底恢复成关闭的样子，而不是显示一个蓝色的底
                    setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_POWER_OFF);
                    //LogUtil.d("-------samhung-----------------------1");
                    notifyCookerPowerOff();
                }
                currentErrorMessage = "";
                countErrorMessageNumber=0;  // 清除累计次数 2019年9月6日19:28:28
                if (isCookerPowerOn && workMode != TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_WORK_FINISH_WAIT_USER_CONFIRM) {

                    if (needToDetectCooker) {
                        //无区无锅情况：
                        //如果两个小锅都没有锅，则无区区域才显示无锅标志
                        //如果两个效果任意一个有锅，则无区都认为有锅，正常工作
                        boolean hasCookerPan = cookerMessage.isHasCooker();
                        if (cookerID == TFTHobConstant.COOKER_TYPE_AB_COOKER) {
                            if (!hasCookerPan) {
                                if (response.getbCookerMessage().isHasCooker()) {
                                    hasCookerPan = true;
                                }

                            }


                        }else if (cookerID == TFTHobConstant.COOKER_TYPE_EF_COOKER) {
                            if (!hasCookerPan) {
                                if (response.getfCookerMessage().isHasCooker()) {
                                    hasCookerPan = true;
                                }

                            }

                        }

                        //如果已经开机，并且不处在等待用户操作的状态，则判断是否有锅
                        //boolean noPanDetected = !GlobalVars.getInstance().isInDemoMode() && !cookerMessage.isHasCooker();
                        boolean noPanDetected = !GlobalVars.getInstance().isInDemoMode() && !hasCookerPan;
                        if (GlobalVars.getInstance().isInDebugMode()) {
                            switch (GlobalVars.getInstance().getDebugModeExtra()) {
                                case CataSettingConstant.DEBUG_MODE_EXTRA_PARAM_IGNORE_ERROR_AND_NO_PAN:
                                case CataSettingConstant.DEBUG_MODE_EXTRA_PARAM_IGNORE_NO_PAN:
                                    noPanDetected = false;
                                    break;
                            }
                        }
                        if (noPanDetected) {
                            if (noPanStartTime == 0) {
                                noPanStartTime = System.currentTimeMillis();
                            }
                            long dat = System.currentTimeMillis() - noPanStartTime;
                            //LogUtil.d("no pan dat---->" + dat);
                            if (dat >= 15000 && dat < 1000 * 75) {//5000
                                hasPan = false;
                                needToRecoverCookerUIForNoPan = true;
                                setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_ABNORMAL_NO_PAN);
                                // notifyCookerUpdateUIForNoPan();
                                if(!blinkWhenNoPan){
                                    notifyCookerUpdateUIForNoPan();

                                }else {
                                    notifyCookerRecoverUIForNoPanWhenBlink();

                                }
                                if(handler.hasMessages(HANDLER_BLINK_WHEN_NO_PAN)){
                                    handler.removeMessages(HANDLER_BLINK_WHEN_NO_PAN);
                                }
                                handler.sendEmptyMessageDelayed(HANDLER_BLINK_WHEN_NO_PAN, HANDLER_BLINK_WHEN_NO_PAN_DELAY_VALUE);
                            }else if (dat >= 1000 * 75 ) {//60
                                hasPan = false;
                                notifyCookerPowerOff();
                                needToRecoverCookerUIForNoPan = false;
                            }
                            EventBus.getDefault().post(new GetTestCookwareResult(Test_Cookware_No_Pan));
                        } else {
                            noPanStartTime = 0;
                            if (needToRecoverCookerUIForNoPan) {//判断是否要从无锅中恢复显示
                                notifyCookerRecoverUIForNoPan();
                                needToRecoverCookerUIForNoPan = false;
                            }
                            EventBus.getDefault().post(new GetTestCookwareResult(Test_Cookware_Has_Pan));
                            //  LogUtil.d("liang 从无锅中恢复显示");
                        }
                    }else {
                        if (workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_ABNORMAL_NO_PAN) {
                            CookerDataTableDao dao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
                            CookerDataTable cookerData = dao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(cookerID)).build().unique();
                            if (cookerData != null) {
                                setWorkMode(cookerData.getSaveWorkMode());
                            }
                        }
                        noPanStartTime = 0;
                        hasPan = true;
                        needToRecoverCookerUIForNoPan = false;
                        savePanFlag = hasPan;
                    }

                }else {//如果已经关机，则判断是否高温
                    if (workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_SENSOR_READY
                            || workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR
                            || workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR_WITH_INDICATOR
                            || workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_WORK_FINISH_WAIT_USER_CONFIRM
                            || workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_FIRE_GEAR_WITH_TIMER
                            || workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_FIRE_GEAR_WITHOUT_TIMER
                            || workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER
                            || workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITHOUT_TIMER) {

                    }else {
                        //if (tempMode == TFTHobConstant.TEMP_MODE_PRECISE_TEMP) LogUtil.d("samhung**samhung high temp---cookerID---->" + cookerID + "---workMode---->" + workMode);
                        if (cookerMessage.isHighTemp()) {
                            notifyCookerUpdateUIForHighTemp();
                        }else {
                            if (isHighTemp) {//判断是否要从高温中恢复显示
                                notifyCookerRecoverUIForHighTemp();
                            }
                        }
                    }
                 //   LogUtil.d("liang power off ~~~");
                }
            }

        }else {//关机
            //LogUtil.d("-------samhung-----------------------3");
        }

        notifyUpdateCookerUI();
    }


    private void notifyCookerUpdateUIForHighTemp() {
        isHighTemp = true;
        setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP);
        saveWorkMode = workMode;
        CookerDataTableDao dao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
        CookerDataTable cookerData = dao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(cookerID)).build().unique();
        if (cookerData != null) {
            cookerData.setHighTempFlag(isHighTemp);
            cookerData.setWorkMode(workMode);
            cookerData.setSaveWorkMode(saveWorkMode);
            dao.update(cookerData);
        }
        notifyUpdateCookerUI();


    }

    private void notifyCookerRecoverUIForHighTemp() {
        isHighTemp = false;
        /*setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP);
        saveWorkMode = workMode;
        CookerDataTableDao dao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
        CookerDataTable cookerData = dao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(cookerID)).build().unique();
        if (cookerData != null) {
            cookerData.setHighTempFlag(isHighTemp);
            cookerData.setWorkMode(workMode);
            cookerData.setSaveWorkMode(saveWorkMode);
            dao.update(cookerData);
        }

        notifyUpdateCookerUI();*/
        //LogUtil.d("-------samhung-----------------------4");
        notifyCookerPowerOff();


    }

    private void notifyCookerRecoverUIForNoPan() {
        if (handler.hasMessages(HANDLER_SIMULATE_CHECK_TEMP_SENSOR_READY)) handler.removeMessages(HANDLER_SIMULATE_CHECK_TEMP_SENSOR_READY);
        hasPan = true;
        if (cookerID > 10) {
            //int id = cookerID / 10;
            int id = cookerID;
            CookerDataTableDao dao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
            CookerDataTable cookerData = dao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(id)).build().unique();
            if (cookerData != null) {
                setWorkMode(cookerData.getSaveWorkMode());
                hardwareWorkMode = cookerData.getSaveHardwareWorkMode();
                fireValue = cookerData.getSaveFireValue();
                tempValue = cookerData.getSaveTempValue();
                tempMode = cookerData.getSaveTempMode();
                savePanFlag = hasPan;
                cookerData.setWorkMode(workMode);
                cookerData.setHardwareWorkMode(hardwareWorkMode);
                cookerData.setFireValue(fireValue);
                cookerData.setTempValue(tempValue);
                cookerData.setTempMode(tempMode);
                cookerData.setPanFlag(hasPan);


                dao.update(cookerData);


            }
            if (remainHourValue != 0 || remainMinuteValue != 0) {
                doStartTimerCountDown();
            }
            if (remainFiveMinuteHourValue != 0) {
                doStopFiveTimerCountDown();
            }
            notifyUpdateCookerUI();

        }else {
            CookerDataTableDao dao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
            CookerDataTable cookerData = dao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(cookerID)).build().unique();
            if (cookerData != null) {
                setWorkMode(cookerData.getSaveWorkMode());
                hardwareWorkMode = cookerData.getSaveHardwareWorkMode();
                fireValue = cookerData.getSaveFireValue();
                tempValue = cookerData.getSaveTempValue();
                tempMode = cookerData.getSaveTempMode();
                savePanFlag = hasPan;
                cookerData.setWorkMode(workMode);
                cookerData.setHardwareWorkMode(hardwareWorkMode);
                cookerData.setFireValue(fireValue);
                cookerData.setTempValue(tempValue);
                cookerData.setTempMode(tempMode);
                cookerData.setPanFlag(hasPan);
                dao.update(cookerData);
            }
            if (remainHourValue != 0 || remainMinuteValue != 0) {
                doStartTimerCountDown();
            }
            if (remainFiveMinuteHourValue != 0) {
                doStopFiveTimerCountDown();
            }
            notifyUpdateCookerUI();
        }

    }
    private void notifyCookerRecoverUIForNoPanWhenBlink() {  //   无锅时 闪烁 ,更新 2019年1月16日10:12:00
        if (handler.hasMessages(HANDLER_SIMULATE_CHECK_TEMP_SENSOR_READY)) handler.removeMessages(HANDLER_SIMULATE_CHECK_TEMP_SENSOR_READY);
        hasPan = true;
        CookerDataTableDao dao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
        CookerDataTable cookerData = dao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(cookerID)).build().unique();
        if (cookerData != null) {
            setWorkMode(cookerData.getSaveWorkMode());
            LogUtil.d("the workmod is = "+ workMode);
            hardwareWorkMode = cookerData.getSaveHardwareWorkMode();
            fireValue = cookerData.getSaveFireValue();
            tempValue = cookerData.getSaveTempValue();
            tempMode = cookerData.getSaveTempMode();
            savePanFlag = hasPan;
            cookerData.setWorkMode(workMode);
            cookerData.setHardwareWorkMode(hardwareWorkMode);
            cookerData.setFireValue(fireValue);
            cookerData.setTempValue(tempValue);
            cookerData.setTempMode(tempMode);
            cookerData.setPanFlag(hasPan);
            dao.update(cookerData);
        }

        notifyUpdateCookerUI();
    }

    private void notifyCookerUpdateUIForNoPan() {
        if (handler.hasMessages(HANDLER_SIMULATE_CHECK_TEMP_SENSOR_READY)) handler.removeMessages(HANDLER_SIMULATE_CHECK_TEMP_SENSOR_READY);
        hasPan = false;

        CookerDataTableDao dao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
        CookerDataTable cookerData = dao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(cookerID)).build().unique();
        if (cookerData != null) {
            //hardwareWorkMode = InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_SETTING;
            //fireValue = 0;
            //tempValue = 0;
            //tempMode = TFTHobConstant.TEMP_MODE_NONE;
            savePanFlag = hasPan;
            cookerData.setWorkMode(workMode);
            //cookerData.setHardwareWorkMode(hardwareWorkMode);
            //cookerData.setFireValue(fireValue);
            //cookerData.setTempValue(tempValue);
            //cookerData.setTempMode(tempMode);
            cookerData.setPanFlag(hasPan);
            dao.update(cookerData);
        }
        doStopTimerCountDown();
        doStopFiveTimerCountDown();
        notifyUpdateCookerUI();

    }

    public void notifyCookerUpdateUIForErrorDismiss() {
        if (isNeedToRecoverCookerUIForErrorDimiss) {
            isNeedToRecoverCookerUIForErrorDimiss = false;
        }
    }

    public void notifyCookerUpdateUIForErrorOccur(boolean flag) {
        if (handler.hasMessages(HANDLER_SIMULATE_CHECK_TEMP_SENSOR_READY)) handler.removeMessages(HANDLER_SIMULATE_CHECK_TEMP_SENSOR_READY);
        isCookerPowerOn = false;
//LogUtil.d("liang get the error message~~");
        settingFireValue = -1;
        settingTempValue = -1;
        settingTempMode = -1;
        settingHourValue = -1;
        settingMinuteValue = -1;
        settingSecondValue = -1;
        settingTempIndicatorResID = -1;
        settingRecipesResID = -1;
        settingTempShowOrder = -1;


        setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_ABNORMAL_ERROR_OCURR);
        hardwareWorkMode = InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_SETTING;
        fireValue = 0;
        tempValue = 0;
        tempMode = TFTHobConstant.TEMP_MODE_NONE;
        remainHourValue = 0;
        remainMinuteValue = 0;
        remainSecondValue = 0;
        remainFiveMinuteHourValue = 0;
        tempIndicatorResID = 0;
        recipesResID = 0;
        isNeedToRecoverCookerUIForErrorDimiss = true;

        saveWorkMode = workMode;
        saveHardwareWorkMode = hardwareWorkMode;
        saveFireValue = fireValue;
        saveTempValue = tempValue;
        saveTempMode = tempMode;
        saveRemainHourValue = remainHourValue;
        saveRemainMinuteValue = remainMinuteValue;
        saveRemainSecondValue = remainSecondValue;
        saveTempIndicatorResID = tempIndicatorResID;
        saveRecipesResID = recipesResID;
        //LogUtil.d("Enter:: -----------------doStopFiveTimerCountDown-------------------5----");
        saveCookerPowerOffData();
        saveTempSensorStatus();
        doStopTimerCountDown();
        doStopFiveTimerCountDown();
        doStopAutoSwitchOffTime();
        notifyUpdateCookerUI();
        if(flag){
            EventBus.getDefault().post(new ShowErrorEvent(errorMessage));
        }

    }

    public void notifyCookerKeepWarm() {
        noPanStartTime = 0;
        if (cookerID > 10) {
            int id = cookerID / 10;
            CookerDataTableDao dao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
            CookerDataTable cookerData = dao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(id)).build().unique();
            if (cookerData != null) {
                isCookerPowerOn = true;
                savePowerOnFlag = true;
                setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITHOUT_TIMER);
                //hardwareWorkMode = cookerData.getSaveHardwareWorkMode();
                hardwareWorkMode = InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FAST_TEMPERATURE;
                //LogUtil.d("hardwaremode--->" + hardwareWorkMode);
               // fireValue = cookerData.getSaveFireValue();
                fireValue = -1;
                tempValue = 70;
                tempMode = TFTHobConstant.TEMP_MODE_FAST_TEMP;
                tempIndicatorResID = R.mipmap.temp_identification_keep_warm;
                remainMinuteValue = 0;
                remainHourValue = 0;
                remainSecondValue = 0;
                saveRemainHourValue = remainHourValue;
                saveRemainMinuteValue = remainMinuteValue;
                saveRemainSecondValue = remainSecondValue;
                saveWorkMode = workMode;
                saveTempMode = tempMode;
                saveTempValue = tempValue;
                saveFireValue = fireValue;
                saveTempIndicatorResID = tempIndicatorResID;
                saveRecipesResID = recipesResID;
                settingFireValue = fireValue;
                settingTempValue = tempValue;
                settingHourValue = remainHourValue;
                settingMinuteValue = remainMinuteValue;
                settingSecondValue = remainSecondValue;
                settingTempIndicatorResID = tempIndicatorResID;
                settingRecipesResID = recipesResID;
                //recipesResID = cookerData.getRecipesResID();
                settingTempMode = tempMode;

                saveSettingData();
          /*  cookerData.setRemainHourValue(remainHourValue);
            cookerData.setRemainMinuteValue(remainMinuteValue);
            cookerData.setRemainSecondValue(remainSecondValue);
            cookerData.setSaveRemainHourValue(saveRemainHourValue);
            cookerData.setSaveRemainMinuteValue(saveRemainMinuteValue);
            cookerData.setSaveRemainSecondValue(saveRemainSecondValue);
            cookerData.setWorkMode(workMode);
            cookerData.setHardwareWorkMode(hardwareWorkMode);
            cookerData.setFireValue(fireValue);
            cookerData.setTempValue(tempValue);
            cookerData.setTempMode(tempMode);

            dao.update(cookerData);*/
            }
            notifyUpdateCookerUI();
        }else {
            CookerDataTableDao dao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
            CookerDataTable cookerData = dao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(cookerID)).build().unique();
            if (cookerData != null) {
                isCookerPowerOn = true;
                savePowerOnFlag = true;
                setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITHOUT_TIMER);
                //hardwareWorkMode = cookerData.getSaveHardwareWorkMode();
                hardwareWorkMode = InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FAST_TEMPERATURE;
                //LogUtil.d("hardwaremode--->" + hardwareWorkMode);
                //fireValue = cookerData.getSaveFireValue();
                fireValue = -1;
                tempValue = 70;
                tempMode = TFTHobConstant.TEMP_MODE_FAST_TEMP;
                tempIndicatorResID = R.mipmap.temp_identification_keep_warm;
                remainMinuteValue = 0;
                remainHourValue = 0;
                remainSecondValue = 0;
                saveRemainHourValue = remainHourValue;
                saveRemainMinuteValue = remainMinuteValue;
                saveRemainSecondValue = remainSecondValue;
                saveWorkMode = workMode;
                saveTempMode = tempMode;
                saveTempValue = tempValue;
                saveFireValue = fireValue;
                saveTempIndicatorResID = tempIndicatorResID;
                saveRecipesResID = recipesResID;
                settingFireValue = fireValue;
                settingTempValue = tempValue;
                settingHourValue = remainHourValue;
                settingMinuteValue = remainMinuteValue;
                settingSecondValue = remainSecondValue;
                settingTempIndicatorResID = tempIndicatorResID;
                settingRecipesResID = recipesResID;
                //recipesResID = cookerData.getRecipesResID();
                settingTempMode = tempMode;

                saveSettingData();
          /*  cookerData.setRemainHourValue(remainHourValue);
            cookerData.setRemainMinuteValue(remainMinuteValue);
            cookerData.setRemainSecondValue(remainSecondValue);
            cookerData.setSaveRemainHourValue(saveRemainHourValue);
            cookerData.setSaveRemainMinuteValue(saveRemainMinuteValue);
            cookerData.setSaveRemainSecondValue(saveRemainSecondValue);
            cookerData.setWorkMode(workMode);
            cookerData.setHardwareWorkMode(hardwareWorkMode);
            cookerData.setFireValue(fireValue);
            cookerData.setTempValue(tempValue);
            cookerData.setTempMode(tempMode);

            dao.update(cookerData);*/
            }
            saveTempSensorStatus();
            notifyUpdateCookerUI();
        }
        int soundType = SoundEvent.getSoundType(cookerID, true);
        EventBus.getDefault().post(new SoundEvent(
                SoundEvent.SOUND_ACTION_PAUSE,
                SoundUtil.SOUND_ID_ALARM_TIMER,
                soundType));
        Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PAUSE, SoundUtil.SOUND_ID_ALARM_TIMER, " + soundType + ")");
    }

    public void notifyCookerAddTenMinute() {  // 再烹饪 10分钟
        noPanStartTime = 0;
        if (cookerID > 10) {
            //int id = cookerID / 2;
            int id = cookerID;
            CookerDataTableDao dao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
            CookerDataTable cookerData = dao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(id)).build().unique();
            if (cookerData != null) {
                isCookerPowerOn = true;
                savePowerOnFlag = true;
                setWorkMode(cookerData.getSaveWorkMode());
                hardwareWorkMode = cookerData.getSaveHardwareWorkMode();
                fireValue = cookerData.getSaveFireValue();
                tempValue = cookerData.getSaveTempValue();
                tempMode = cookerData.getSaveTempMode();
                remainMinuteValue = 10;
                remainHourValue = 0;
                remainSecondValue = 60;
                saveRemainHourValue = remainHourValue;
                saveRemainMinuteValue = remainMinuteValue;
                saveRemainSecondValue = remainSecondValue;
                settingHourValue = remainHourValue;
                settingMinuteValue = remainMinuteValue;
                settingSecondValue = remainSecondValue;
                tempIndicatorResID = cookerData.getTempIndicatorResID();
                recipesResID = cookerData.getRecipesResID();
                cookerData.setRemainHourValue(remainHourValue);
                cookerData.setRemainMinuteValue(remainMinuteValue);
                cookerData.setRemainSecondValue(remainSecondValue);
                cookerData.setSaveRemainHourValue(saveRemainHourValue);
                cookerData.setSaveRemainMinuteValue(saveRemainMinuteValue);
                cookerData.setSaveRemainSecondValue(saveRemainSecondValue);
                cookerData.setSettingHourValue(settingHourValue);
                cookerData.setSettingMinuteValue(settingMinuteValue);
                cookerData.setSettingSecondValue(settingSecondValue);
                cookerData.setWorkMode(workMode);
                cookerData.setHardwareWorkMode(hardwareWorkMode);
                cookerData.setFireValue(fireValue);
                cookerData.setTempValue(tempValue);
                cookerData.setTempMode(tempMode);
                dao.update(cookerData);
            }
            startAutoSwitchOffTime();
            doStartTimerCountDown();
            notifyUpdateCookerUI();
        }else {
            CookerDataTableDao dao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
            CookerDataTable cookerData = dao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(cookerID)).build().unique();
            if (cookerData != null) {
                isCookerPowerOn = true;
                savePowerOnFlag = true;
                setWorkMode(cookerData.getSaveWorkMode());
                hardwareWorkMode = cookerData.getSaveHardwareWorkMode();
                fireValue = cookerData.getSaveFireValue();
                tempValue = cookerData.getSaveTempValue();
                tempMode = cookerData.getSaveTempMode();
                remainMinuteValue = 10;
                remainHourValue = 0;
                remainSecondValue = 60;
                saveRemainHourValue = remainHourValue;
                saveRemainMinuteValue = remainMinuteValue;
                saveRemainSecondValue = remainSecondValue;
                settingHourValue = remainHourValue;
                settingMinuteValue = remainMinuteValue;
                settingSecondValue = remainSecondValue;
                tempIndicatorResID = cookerData.getTempIndicatorResID();
                recipesResID = cookerData.getRecipesResID();
                cookerData.setRemainHourValue(remainHourValue);
                cookerData.setRemainMinuteValue(remainMinuteValue);
                cookerData.setRemainSecondValue(remainSecondValue);
                cookerData.setSaveRemainHourValue(saveRemainHourValue);
                cookerData.setSaveRemainMinuteValue(saveRemainMinuteValue);
                cookerData.setSaveRemainSecondValue(saveRemainSecondValue);
                cookerData.setSettingHourValue(settingHourValue);
                cookerData.setSettingMinuteValue(settingMinuteValue);
                cookerData.setSettingSecondValue(settingSecondValue);
                cookerData.setWorkMode(workMode);
                cookerData.setHardwareWorkMode(hardwareWorkMode);
                cookerData.setFireValue(fireValue);
                cookerData.setTempValue(tempValue);
                cookerData.setTempMode(tempMode);
                dao.update(cookerData);
            }
            startAutoSwitchOffTime();
            doStartTimerCountDown();
            notifyUpdateCookerUI();
        }

        int soundType = SoundEvent.getSoundType(cookerID, true);
        EventBus.getDefault().post(new SoundEvent(
                SoundEvent.SOUND_ACTION_PAUSE,
                SoundUtil.SOUND_ID_ALARM_TIMER,
                soundType));
        Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PAUSE, SoundUtil.SOUND_ID_ALARM_TIMER, " + soundType + ")");
    }

    public void notifyCookerReadyToCook() {//demo
        doStopAutoSwitchOffTimeForReadyToCook();
        noPanStartTime = 0;
        if (settingHourValue != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//有定时
            isCookerPowerOn = true;
            if (settingTempIndicatorResID != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//with pic
                setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER);

            }else {//显示温度值
                if (settingRecipesResID > 0) {
                    LogUtil.d("Enter:: notifyCookerReadyToCook---settingRecipesResID--->" +  settingRecipesResID);
                    setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_FIRST);

                }else {
                    setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER);
                }

            }
            CookerDataTableDao dao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
            CookerDataTable cookerData = dao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(cookerID)).build().unique();
            if (cookerData != null) {
                tempValue = cookerData.getSettingtempValue();

                cookerData.setWorkMode(workMode);
                cookerData.setTempValue(tempValue);
                cookerData.setPowerOnFlag(isCookerPowerOn);
                dao.update(cookerData);
            }
            notifyUpdateCookerUI();
            doStartTimerCountDown();
        }else {//没定时
            isCookerPowerOn = true;
            if (settingTempIndicatorResID != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//with pic
                setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITHOUT_TIMER);
            }else {
                setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITHOUT_TIMER);
            }
        }

        startAutoSwitchOffTime();

        CookerDataTableDao dao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
        CookerDataTable cookerData = dao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(cookerID)).build().unique();
        if (cookerData != null) {
            tempValue = cookerData.getSettingtempValue();

            cookerData.setWorkMode(workMode);
            cookerData.setTempValue(tempValue);
            saveWorkMode = workMode;
            cookerData.setSaveWorkMode(saveWorkMode);
            cookerData.setPowerOnFlag(isCookerPowerOn);
            dao.update(cookerData);
        }
        notifyUpdateCookerUI();

    }

    public void notifyCookerPowerOff() {
        Logger.getInstance().d("notifyCookerPowerOff(" + cookerID + ")", true);
        resetErrorDetectProcess();
        if (handler.hasMessages(HANDLER_SIMULATE_CHECK_TEMP_SENSOR_READY)) handler.removeMessages(HANDLER_SIMULATE_CHECK_TEMP_SENSOR_READY);

        int soundType = SoundEvent.getSoundType(cookerID, true);
        EventBus.getDefault().post(new SoundEvent(
                SoundEvent.SOUND_ACTION_PAUSE,
                SoundUtil.SOUND_ID_ALARM_TIMER,
                soundType));

        soundType = SoundEvent.getSoundType(cookerID, false);
        EventBus.getDefault().post(new SoundEvent(
                SoundEvent.SOUND_ACTION_PAUSE,
                SoundUtil.SOUND_ID_ALARM,
                soundType));

        noPanStartTime = 0;
        hasPan = true;
        needToRecoverCookerUIForNoPan = false;
        savePanFlag = hasPan;
        isCookerPowerOn = false;

        settingFireValue = -1;
        settingTempValue = -1;
        settingTempMode = -1;
        settingHourValue = -1;
        settingMinuteValue = -1;
        settingSecondValue = -1;
        settingTempIndicatorResID = -1;
        settingRecipesResID = -1;
        settingTempShowOrder = -1;


        setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_POWER_OFF);
        hardwareWorkMode = InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_SETTING;
        fireValue = 0;
        tempValue = 0;
        realTempValue = 0;
        tempMode = TFTHobConstant.TEMP_MODE_NONE;
        remainHourValue = -1;
        remainMinuteValue = -1;  // 2019年5月8日10:47:10
        remainSecondValue = 0;
        remainFiveMinuteHourValue = 0;
        tempIndicatorResID = 0;
        recipesResID = 0;
        isHighTemp = false;
        saveHighTempFlag = isHighTemp;

        saveWorkMode = workMode;
        saveHardwareWorkMode = hardwareWorkMode;
        saveFireValue = fireValue;
        saveTempValue = tempValue;
        saveTempMode = tempMode;
        saveRemainHourValue = remainHourValue;
        saveRemainMinuteValue = remainMinuteValue;
        saveRemainSecondValue = remainSecondValue;
        saveTempIndicatorResID = tempIndicatorResID;
        saveRecipesResID = recipesResID;

        //precise temp control
        timeForCheckTemp = 0;
        timeForCorrectTemp = 0;

        //LogUtil.d("Enter:: ----------doStopTimerCountDown--------------3----------");
        saveCookerPowerOffData();
        saveTempSensorStatus();
        doStopTimerCountDown();
        doStopFiveTimerCountDown();
        doStopAutoSwitchOffTime();
        doStopAutoSwitchOffTimeForReadyToCook();
        notifyUpdateCookerUI();
        //LogUtil.d("settingHourValue---00--->" + settingHourValue);

    }

    public void notifyCookerPause() {
        isPause = true;
        if (handler.hasMessages(HANDLER_SIMULATE_CHECK_TEMP_SENSOR_READY)) handler.removeMessages(HANDLER_SIMULATE_CHECK_TEMP_SENSOR_READY);
        //LogUtil.d("cookerID--->" + cookerID + "-----savaworkmode----111-->" + saveWorkMode);

        if (workMode != TFTHobConstant.HOB_VIEW_WORK_MODE_ABNORMAL_NO_PAN) {

            // 无锅状态不是真正的模式，所以无需记录
            lastSaveWorkMode = saveWorkMode;
            saveWorkMode = workMode;
        }

        lastSaveHardwareWorkMode = saveHardwareWorkMode;
        saveHardwareWorkMode = hardwareWorkMode;

        lastSaveFireValue = saveFireValue;
        saveFireValue = fireValue;

        lastSaveTempValue = saveTempValue;
        saveTempValue = tempValue;

        lastSaveTempMode= saveTempMode;
        saveTempMode = tempMode;

        lastSaveTempIndicatorResID = saveTempIndicatorResID;
        saveTempIndicatorResID = tempIndicatorResID;

        lastSaveRecipesResID = saveRecipesResID;
        saveRecipesResID = recipesResID;

        lastSaveRemainSecondValue = saveRemainSecondValue;
        saveRemainSecondValue = remainSecondValue;

        lastSaveRemainMinuteValue = saveRemainMinuteValue;
        saveRemainMinuteValue = remainMinuteValue;

        lastSaveRemainHourValue = saveRemainHourValue;
        saveRemainHourValue = remainHourValue;

        lastSavePowerOnFlag = savePowerOnFlag;
        savePowerOnFlag = isCookerPowerOn;
        noPanStartTime = 0;
        if (!hasPan) {
            if (handler.hasMessages(HANDLER_BLINK_WHEN_NO_PAN)) {
                handler.removeMessages(HANDLER_BLINK_WHEN_NO_PAN);
            }
            if (!blinkWhenNoPan) {
                blinkWhenNoPan = true;
                notifyCookerRecoverUIForNoPanWhenBlink();
            }
            hasPan = true;
            needToRecoverCookerUIForNoPan = false;
        }
        lastSavePanFlag = savePanFlag;
        savePanFlag = hasPan;
        //isCookerPowerOn = false;
        setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_POWER_OFF);
        hardwareWorkMode = InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_SETTING;
        fireValue = 0;
        tempValue = 0;
        tempMode = TFTHobConstant.TEMP_MODE_NONE;
        //isHighTemp = false;
        saveCookerPowerOffData();
        saveTempSensorStatus();

        timerStartedWhenPause = mDisposable != null;
        if (timerStartedWhenPause) {
            doStopTimerCountDown();
        }
        doStopFiveTimerCountDown();
        doPauseAutoSwitchOffTime();
        doPauseAutoSwitchOffTimeForReadyToCook();
        notifyUpdateCookerUI();
        //LogUtil.d("cookerID--->" + cookerID + "-----savaworkmode---222--->" + saveWorkMode);
    }

    public void notifyCookerPlay() {
        if (handler.hasMessages(HANDLER_SIMULATE_CHECK_TEMP_SENSOR_READY)) handler.removeMessages(HANDLER_SIMULATE_CHECK_TEMP_SENSOR_READY);
        //LogUtil.d("cookerID--->" + cookerID + "-----savaworkmode--333---->" + saveWorkMode + "----workmode--->" + workMode);
        //LogUtil.d("cookerID--->" + cookerID + "----Enter:: notifyCookerPlay----fireValue--->" + fireValue + "----saveFireValue--->" + saveFireValue + "---lastSaveFireValue--->" + lastSaveFireValue);
        noPanStartTime = 0;
        hasPan = savePanFlag;
        isCookerPowerOn = savePowerOnFlag;
        setWorkMode(saveWorkMode);

        hardwareWorkMode = saveHardwareWorkMode;
        fireValue = saveFireValue;
        tempValue = saveTempValue;
        tempMode = saveTempMode;
        remainHourValue = saveRemainHourValue;
        remainMinuteValue = saveRemainMinuteValue;
        remainSecondValue = saveRemainSecondValue;
        tempIndicatorResID = saveTempIndicatorResID;
        recipesResID = saveRecipesResID;

        if (saveWorkMode == TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_WORK_FINISH_WAIT_USER_CONFIRM) {
            // 恢复 saveWorkMode 到在给用户确认之前，以便用户做出 +10min 选择后能正常工作
            saveWorkMode = lastSaveWorkMode;
            saveHardwareWorkMode = lastSaveHardwareWorkMode;
            saveFireValue = lastSaveFireValue;
            saveTempValue = lastSaveTempValue;
            saveTempMode = lastSaveTempMode;
            saveTempIndicatorResID = lastSaveTempIndicatorResID;
            saveRecipesResID = lastSaveRecipesResID;
            saveRemainSecondValue = lastSaveRemainSecondValue;
            saveRemainMinuteValue = lastSaveRemainMinuteValue;
            saveRemainHourValue = lastSaveRemainHourValue;
            savePowerOnFlag = lastSavePowerOnFlag;
            savePanFlag = lastSavePanFlag;

        }

        saveCookerPowerOffData();
        saveTempSensorStatus();
        LogUtil.d("notifyCookerPlay cookerID--->" + cookerID + "---remainHourValue--->" + remainHourValue + "----remainMinuteValue--->" + remainMinuteValue + "---remainSecondValue-->" + remainSecondValue);
        if ((remainHourValue == 0 && remainMinuteValue == 0 && remainSecondValue == 0) || (remainHourValue == -1 && remainMinuteValue == -1 && remainSecondValue == -1)) {

        }else {
            if (timerStartedWhenPause) {
                doStartTimerCountDown();
                timerStartedWhenPause = false;
            }
        }
        //f (remainFiveMinuteHourValue != 0) doStartFiveMinuteCountDown();
        doResumeAutoSwitchOffTime();
        doResumeAutoSwitchOffTimeForReadyToCook();
        notifyUpdateCookerUI();
        isPause = false;
    }

    public void notifyUpdateCookerData(CookerSettingData data) {
        //LogUtil.d("Enter::notifyUpdateCookerData--->" + data.getTimerHourValue() + "---" + data.getTimerMinuteValue());

        needPauseForSetting = true;


        //LogUtil.d("cooking switch off time--->" + CookingTimeTable.getCookingAutomaticSwitchOffTime((fireValue == -1)?tempValue:fireValue));
        CookerDataTableDao dao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
        CookerDataTable cookerData = dao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(cookerID)).build().unique();
        if (cookerData != null) {

           // LogUtil.d("Enter::notifyUpdateCookerData--set->" + cookerData.getSettingHourValue() + "---" + cookerData.getSettingMinuteValue());
            //如果有定时，设置档位时定时仍然要计时，不需要重新计时,如果工作模式改变了，则需重新计时
            if (cookerData.getTempMode() == tempMode) {
                if (cookerData.getSettingHourValue() != -1) {//有定时
                    if (data.getPreSettingtimerHourValue() == data.getTimerHourValue() &&
                            data.getPreSettingtimerMinuteValue() == data.getTimerMinuteValue()
                    ) {//定时设置时间没变
                        if (remainHourValue == data.getTimerRemainHourValue() && remainMinuteValue == data.getTimerRemainMinuteValue()) {
                            startAutoSwitchOffTime();
                        }else {
                            remainHourValue = data.getTimerHourValue();
                            remainMinuteValue = data.getTimerMinuteValue();
                            remainSecondValue = data.getTimerSecondValue();

                            settingHourValue = data.getTimerHourValue();
                            settingMinuteValue = data.getTimerMinuteValue();
                            settingSecondValue = data.getTimerSecondValue();

                            saveRemainHourValue = remainHourValue;
                            saveRemainMinuteValue = remainMinuteValue;
                            saveRemainSecondValue = remainSecondValue;

                            startAutoSwitchOffTime();
                        }


                    }else {//定时设置时间改变
                        remainHourValue = data.getTimerHourValue();
                        remainMinuteValue = data.getTimerMinuteValue();
                        remainSecondValue = data.getTimerSecondValue();

                        settingHourValue = data.getTimerHourValue();
                        settingMinuteValue = data.getTimerMinuteValue();
                        settingSecondValue = data.getTimerSecondValue();

                        saveRemainHourValue = remainHourValue;
                        saveRemainMinuteValue = remainMinuteValue;
                        saveRemainSecondValue = remainSecondValue;

                        startAutoSwitchOffTime();
                    }



                }else {//没有定时
                    remainHourValue = data.getTimerHourValue();
                    remainMinuteValue = data.getTimerMinuteValue();
                    remainSecondValue = data.getTimerSecondValue();

                    settingHourValue = data.getTimerHourValue();
                    settingMinuteValue = data.getTimerMinuteValue();
                    settingSecondValue = data.getTimerSecondValue();

                    saveRemainHourValue = remainHourValue;
                    saveRemainMinuteValue = remainMinuteValue;
                    saveRemainSecondValue = remainSecondValue;

                    startAutoSwitchOffTime();
                }

            }else {//工作模式已经改变
                if (cookerData.getSettingHourValue() != -1) {//有定时
                    doStopTimerCountDown();//先停止之前的计时
                }
                remainHourValue = data.getTimerHourValue();
                remainMinuteValue = data.getTimerMinuteValue();
                remainSecondValue = data.getTimerSecondValue();

                settingHourValue = data.getTimerHourValue();
                settingMinuteValue = data.getTimerMinuteValue();
                settingSecondValue = data.getTimerSecondValue();

                saveRemainHourValue = remainHourValue;
                saveRemainMinuteValue = remainMinuteValue;
                saveRemainSecondValue = remainSecondValue;

                startAutoSwitchOffTime();
            }
            Logger.getInstance().i("TimerOriginal(" + cookerID + ")=[" + cookerData.getSettingHourValue() + "," + cookerData.getSettingMinuteValue() + "," + cookerData.getRemainHourValue() + "," + cookerData.getRemainMinuteValue() +  "]");
            Logger.getInstance().i("TimerChangedTo(" + cookerID + ")=[" + data.getTimerHourValue() + "," + data.getTimerMinuteValue() + "," + data.getTimerRemainHourValue() + "," + data.getTimerRemainMinuteValue() +  "]");

          /*  if (cookerData.getSettingHourValue() != data.getTimerHourValue()
                    || cookerData.getSettingMinuteValue() != data.getTimerMinuteValue()
                    || cookerData.getRemainHourValue() != data.getTimerRemainHourValue()
                    || cookerData.getRemainMinuteValue() != data.getTimerRemainMinuteValue()) {

                Logger.getInstance().i("TimerOriginal(" + cookerID + ")=[" + cookerData.getSettingHourValue() + "," + cookerData.getSettingMinuteValue() + "," + cookerData.getRemainHourValue() + "," + cookerData.getRemainMinuteValue() +  "]");
                Logger.getInstance().i("TimerChangedTo(" + cookerID + ")=[" + data.getTimerHourValue() + "," + data.getTimerMinuteValue() + "," + data.getTimerRemainHourValue() + "," + data.getTimerRemainMinuteValue() +  "]");

                remainHourValue = data.getTimerHourValue();
                remainMinuteValue = data.getTimerMinuteValue();
                remainSecondValue = data.getTimerSecondValue();

                settingHourValue = data.getTimerHourValue();
                settingMinuteValue = data.getTimerMinuteValue();
                settingSecondValue = data.getTimerSecondValue();

                saveRemainHourValue = remainHourValue;
                saveRemainMinuteValue = remainMinuteValue;
                saveRemainSecondValue = remainSecondValue;

                startAutoSwitchOffTime();
            }*/

        }else {
            remainHourValue = data.getTimerHourValue();
            remainMinuteValue = data.getTimerMinuteValue();
            remainSecondValue = data.getTimerSecondValue();

            settingHourValue = data.getTimerHourValue();
            settingMinuteValue = data.getTimerMinuteValue();
            settingSecondValue = data.getTimerSecondValue();

            saveRemainHourValue = remainHourValue;
            saveRemainMinuteValue = remainMinuteValue;
            saveRemainSecondValue = remainSecondValue;

            startAutoSwitchOffTime();
        }

        fireValue = data.getFireValue();
        tempMode = data.getTempMode();
        tempValue = data.getTempValue();

        tempIndicatorResID = data.getTempIdentifyDrawableResourceID();
        recipesResID = data.getTempRecipesResID();
        settingFireValue = data.getFireValue();
        settingTempValue = data.getTempValue();
        settingTempMode = data.getTempMode();
        settingRecipesResID = recipesResID;

        settingTempIndicatorResID = data.getTempIdentifyDrawableResourceID();
        settingRecipesResID = data.getTempRecipesResID();
        settingTempShowOrder = data.getTempShowOrder();
       // isCookerPowerOn = true;
        hasPan = true;
        savePanFlag = hasPan;
        noPanStartTime = 0;

        saveWorkMode = workMode;
        saveHardwareWorkMode = hardwareWorkMode;
        saveFireValue = fireValue;
        saveTempValue = tempValue;
        saveTempMode = tempMode;

        saveTempIndicatorResID = tempIndicatorResID;
        saveRecipesResID = recipesResID;
        // LogUtil.d("settingHourValue---11--->" + settingHourValue);
        processSettingData();

        if (cookerData != null) {
            //只要重新设置档位自动关机都要重新计时，无论设置的档位和之前的档位是否一致，如9档设置成9档，也要重新计时
            /*if ((cookerData.getFireValue() != data.getFireValue()) || (cookerData.getTempValue() != data.getTempValue())) {
                startAutoSwitchOffTime();

            }else {

            }*/
            startAutoSwitchOffTime();

        }else {
            startAutoSwitchOffTime();
        }

        saveSettingData();
        saveTempSensorStatus();

        notifyUpdateCookerUI();
        needPauseForSetting = false;

    }

    public void notifyForceUpdateCookerUI() {
        notifyUpdateCookerUI();
    }

    private void notifyUpdateFireGearUIForBGearTimeIsUp() {
        fireValue = 9;
        settingFireValue = 9;
        saveFireValue = fireValue;
        CookerDataTableDao cookerDataTableDao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
        CookerDataTable cookerData = cookerDataTableDao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(cookerID)).build().unique();
        if (cookerData != null) {
            cookerData.setFireValue(fireValue);
            cookerData.setSettingFireValue(settingFireValue);
            cookerData.setSaveFireValue(saveFireValue);
            cookerDataTableDao.update(cookerData);
        }
        notifyUpdateCookerUI();
    }

    private void notifyUpdateFireGearUIFor9GearTimeIsUp(int gear) {
        fireValue = gear;
        settingFireValue = 9;
        saveFireValue = fireValue;
        CookerDataTableDao cookerDataTableDao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
        CookerDataTable cookerData = cookerDataTableDao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(cookerID)).build().unique();
        if (cookerData != null) {
            cookerData.setFireValue(fireValue);
            cookerData.setSettingFireValue(settingFireValue);
            cookerData.setSaveFireValue(saveFireValue);
            cookerDataTableDao.update(cookerData);
        }
        notifyUpdateCookerUIForAutoSwitchPower();
        //notifyUpdateCookerUI();
    }

    private void processSettingData() {
        LogUtil.d("Enter:: processSettingData---realTemp--->" + realTempValue + "---settingValue---->" + tempValue);
        if (settingFireValue != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//档位模式
            hardwareWorkMode = InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FIRE_GEAR;
            saveHardwareWorkMode = hardwareWorkMode;
            if (settingHourValue != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//有定时
                setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_FIRE_GEAR_WITH_TIMER);  // 火力 +定时
                fireValue = settingFireValue;
                //remainHourValue = settingHourValue;
                //remainMinuteValue = settingMinuteValue;
                //remainSecondValue = settingSecondValue;
                LogUtil.d("fireValue----->" + fireValue);
                if (fireValue == 0) {
                    notifyCookerPowerOff();
                    isCookerPowerOn = false;
                } else {
                    if (fireValue == 10) {
                        remainFiveMinuteHourValue = 5;
                        //doStartFiveMinuteCountDown();
                    }
                    doStartTimerCountDown();
                    isCookerPowerOn = true;
                }

            }else {//没定时
                setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_FIRE_GEAR_WITHOUT_TIMER);
                fireValue = settingFireValue;
                LogUtil.d("fireValue----->" + fireValue);
                if (fireValue == 0) {
                    notifyCookerPowerOff();
                    isCookerPowerOn = false;
                } else {
                    if (fireValue == 10) {
                        remainFiveMinuteHourValue = 5;
                        //doStartFiveMinuteCountDown();
                    }
                    isCookerPowerOn = true;
                }

                LogUtil.d("liang the  isCookerPowerOn = true");
            }


        }else if (settingTempValue != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//温控模式
            if (tempMode == TFTHobConstant.TEMP_MODE_FAST_TEMP || tempMode == TFTHobConstant.TEMP_MODE_FAST_TEMP_GEAR_WITHOUT_SENSOR) {
                hardwareWorkMode = InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FAST_TEMPERATURE;
            }else if (tempMode == TFTHobConstant.TEMP_MODE_PRECISE_TEMP) {
                //hardwareWorkMode = InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FAST_TEMPERATURE;
                hardwareWorkMode = InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_PRECISE_TEMPERATURE;
            }
            saveHardwareWorkMode = hardwareWorkMode;

            if (settingHourValue != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//有定时
                if (settingTempIndicatorResID != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//with pic
                    //if (settingRecipesResID != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//显示菜谱
                    if (settingRecipesResID > 0) {//显示菜谱
                        /*if (settingTempShowOrder == CookerSettingData.TEMP_RECIPES_SHOW_ORDER_RECIPES_FIRST) {
                            //**samhung 需要讨论，有菜谱有图片，也有温度指示，那温度指示要不要显示出来**
                            //setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER_AND_RECIPES_FIRST);
                            setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_FIRST); // 温度+定时　　＋　图片  601
                            LogUtil.d(" liang ------1 TEMP_RECIPES_SHOW_ORDER_RECIPES_FIRST");
                        }else {
                          //  setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_SECOND);
                            setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_FIRST);
                            // setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER_AND_RECIPES_SECOND);
                            LogUtil.d("set workmode is HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_SECOND ");

                        }*/

                        //doStartTimerCountDown();

                        if (isCookerPowerOn) {
                            if (workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR) {

                            }else {
                                doStartTimerCountDown();
                                setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_FIRST);
                            }/*else if ((realTempValue >= settingTempValue && workMode != TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR) || workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_FIRST) {
                                doStartTimerCountDown();
                                setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_FIRST);
                            }*/

                        }else {
                            setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR);
                            isCookerPowerOn = true;
                        }


                    }else {//不用显示菜谱
                        //setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER);

                        if (tempMode == TFTHobConstant.TEMP_MODE_FAST_TEMP || tempMode == TFTHobConstant.TEMP_MODE_FAST_TEMP_GEAR_WITHOUT_SENSOR) {
                            //setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER;  // 火力图标 + 定时
                            //doStartTimerCountDown();
                            //setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR;
                            //isCookerPowerOn = true;
                            if (isCookerPowerOn) {

                                if (realTempValue < tempValue) {
                                    setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR);
                                    doStopTimerCountDown();
                                }else {
                                    if (workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR) {
                                        notifyNTCReadyToCook();
                                    }else {
                                        setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER);
                                        doStartTimerCountDown();
                                    }

                                }


                            }else {
                                setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR);
                                isCookerPowerOn = true;
                            }
                        }else if (tempMode == TFTHobConstant.TEMP_MODE_PRECISE_TEMP){

                            if (workMode != TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER) {
                                //setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR_WITH_INDICATOR;
                                //setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER);


                                if (!isCookerPowerOn) {
                                    isCookerPowerOn = true;
                                    setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR);
                                    Message msg = new Message();
                                    msg.what = HANDLER_SIMULATE_CHECK_TEMP_SENSOR_READY;
                                    msg.arg1 = TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_SENSOR_READY;
                                    handler.sendMessageDelayed(msg,TIME_FOR_SIMULATE_CHECK_TEMP_SENSOR_READY);
                                }else {

                                    //if (ntcTempValue >= settingTempValue && workMode != TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR) doStartTimerCountDown();
                                    if (realTempValue < tempValue) {
                                        setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR);
                                        doStopTimerCountDown();
                                        //doStartTimerCountDown();
                                    }else {
                                        if (workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR) {
                                            notifyTempSersorReadyToCook();
                                        }else {
                                            setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER);
                                            doStartTimerCountDown();
                                        }

                                    }
                                }

                            }


                        }

                    }

                }else {//显示温度值

                    //if (settingRecipesResID != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//显示菜谱
                    if (settingRecipesResID > 0) {//显示菜谱

                        if (isCookerPowerOn) {

                            if (realTempValue < tempValue) {
                                setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR);
                                doStopTimerCountDown();
                            }else {
                                if (workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR) {
                                    notifyNTCReadyToCook();
                                }else {
                                    setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_FIRST);
                                    doStartTimerCountDown();
                                }

                            }

                         /*   if (workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR) {

                            }else {

                                doStartTimerCountDown();
                                setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_FIRST);
                            }*/

                        }else {
                            setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR);
                            isCookerPowerOn = true;
                        }

                    }else {//不用显示菜谱
                        //setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER;

                        if (tempMode == TFTHobConstant.TEMP_MODE_FAST_TEMP|| tempMode == TFTHobConstant.TEMP_MODE_FAST_TEMP_GEAR_WITHOUT_SENSOR) {

                            if (isCookerPowerOn) {

                                if (realTempValue < tempValue) {
                                    setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR);
                                    doStopTimerCountDown();
                                }else {
                                    if (workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR) {
                                        notifyNTCReadyToCook();
                                    }else {
                                        setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER);
                                        doStartTimerCountDown();
                                    }

                                }

                            }else {
                                setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR);
                                isCookerPowerOn = true;
                            }
                        }else if (tempMode == TFTHobConstant.TEMP_MODE_PRECISE_TEMP){
                            //setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR;
                            //handler.sendEmptyMessageDelayed(HANDLER_SIMULATE_CHECK_TEMP_SENSOR_READY,TIME_FOR_SIMULATE_CHECK_TEMP_SENSOR_READY);

                            //if (workMode != TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER) {

                                //setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER);

                                if (!isCookerPowerOn) {
                                    //tempValue = -1;
                                    //isCookerPowerOn = false;
                                    isCookerPowerOn = true;
                                    setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR);
                                    Message msg = new Message();
                                    msg.what = HANDLER_SIMULATE_CHECK_TEMP_SENSOR_READY;
                                    msg.arg1 = TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_SENSOR_READY;
                                    handler.sendMessageDelayed(msg,TIME_FOR_SIMULATE_CHECK_TEMP_SENSOR_READY);
                                }else {
                                     if (realTempValue < tempValue) {
                                         setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR);
                                         doStopTimerCountDown();
                                        // doStartTimerCountDown();
                                     }else {
                                         if (workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR) {
                                             notifyTempSersorReadyToCook();
                                         }else {
                                             setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER);
                                             doStartTimerCountDown();
                                         }

                                     }


                                    //if (ntcTempValue >= settingTempValue && workMode != TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR) doStartTimerCountDown();
                                }

                           // }


                        }


                    }
                }

            }else {//没定时
                if (settingTempIndicatorResID != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//with pic
                    //setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITHOUT_TIMER;
                    if (tempMode == TFTHobConstant.TEMP_MODE_FAST_TEMP|| tempMode == TFTHobConstant.TEMP_MODE_FAST_TEMP_GEAR_WITHOUT_SENSOR) {

                        if (isCookerPowerOn) {

                            if (realTempValue < tempValue) {
                                setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR);
                                doStopTimerCountDown();
                            }else {
                                if (workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR) {
                                    notifyNTCReadyToCook();
                                }else {
                                    setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITHOUT_TIMER);

                                }

                            }



                        }else {
                            setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR);
                            isCookerPowerOn = true;
                        }

                    }else if (tempMode == TFTHobConstant.TEMP_MODE_PRECISE_TEMP){
                        //if (workMode != TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITHOUT_TIMER) {
                            //setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITHOUT_TIMER);
                            if (!isCookerPowerOn) {
                                //tempValue = -1;
                                //isCookerPowerOn = false;
                                isCookerPowerOn = true;

                                //setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR_WITH_INDICATOR;
                                setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR);
                                Message msg = new Message();
                                msg.what = HANDLER_SIMULATE_CHECK_TEMP_SENSOR_READY;
                                msg.arg1 = TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_SENSOR_READY;
                                handler.sendMessageDelayed(msg,TIME_FOR_SIMULATE_CHECK_TEMP_SENSOR_READY);

                            }else {
                                if (realTempValue < tempValue) setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR);
                                else {
                                    if (workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR) {
                                        notifyTempSersorReadyToCook();
                                    }else {
                                        setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITHOUT_TIMER);

                                    }

                                }
                            }

                        //}



                    }
                }else {
                    //workMode = TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITHOUT_TIMER;
                    if (tempMode == TFTHobConstant.TEMP_MODE_FAST_TEMP|| tempMode == TFTHobConstant.TEMP_MODE_FAST_TEMP_GEAR_WITHOUT_SENSOR) {
                       // setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITHOUT_TIMER;
                        //setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR;
                        //isCookerPowerOn = true;

                        if (isCookerPowerOn) {

                            if (realTempValue < tempValue) {
                                setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR);
                                doStopTimerCountDown();
                            }else {
                                if (workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR) {
                                    notifyNTCReadyToCook();
                                }else {
                                    setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITHOUT_TIMER);

                                }

                            }

                        }else {
                            setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR);
                            isCookerPowerOn = true;
                        }



                    }else if (tempMode == TFTHobConstant.TEMP_MODE_PRECISE_TEMP){
                        //setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR;
                        // handler.sendEmptyMessageDelayed(HANDLER_SIMULATE_CHECK_TEMP_SENSOR_READY,TIME_FOR_SIMULATE_CHECK_TEMP_SENSOR_READY);

                        //if (workMode != TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITHOUT_TIMER) {
                            //setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITHOUT_TIMER);
                            if (!isCookerPowerOn) {
                                //tempValue = -1;
                                isCookerPowerOn = true;

                                setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR);
                                Message msg = new Message();
                                msg.what = HANDLER_SIMULATE_CHECK_TEMP_SENSOR_READY;
                                msg.arg1 = TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_SENSOR_READY;
                                handler.sendMessageDelayed(msg,TIME_FOR_SIMULATE_CHECK_TEMP_SENSOR_READY);
                            }else {
                                if (realTempValue < tempValue) setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR);
                                else {
                                    if (workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR) {
                                        notifyTempSersorReadyToCook();
                                    }else {
                                        setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITHOUT_TIMER);

                                    }
                                }
                            }

                       // }

                    }
                }
            }

        }


        saveWorkMode = workMode;

    }



 /*   private void saveTempSensorStatus() {
        if (tempMode == TFTHobConstant.TEMP_MODE_PRECISE_TEMP) {
            SettingDbHelper.saveTemperatureSensorStatus(cookerID);
        }else {
            if (SettingDbHelper.getTemperatureSensorStatus() == cookerID) {
                SettingDbHelper.saveTemperatureSensorStatus(-1);
                EventBus.getDefault().post(new TempSensorRequestEvent(cookerID , TempSensorRequestEvent.ACTION_STOP_TEMP_SENSOR));
                SettingDbHelper.saveTemperatureSensorValue(0);

            }

        }
    }*/

    private void saveTempSensorStatus() {
        int id = SettingDbHelper.getTemperatureSensorStatus();
        if (tempMode == TFTHobConstant.TEMP_MODE_PRECISE_TEMP) {
            if (id == -1) {
                SettingDbHelper.saveTemperatureSensorStatus(cookerID);
            }

        }else {
            if (id == cookerID) {
                SettingDbHelper.saveTemperatureSensorStatus(-1);
                EventBus.getDefault().post(new TempSensorRequestEvent(cookerID , TempSensorRequestEvent.ACTION_STOP_TEMP_SENSOR));
                SettingDbHelper.saveTemperatureSensorValue(0);

            }


        }
    }

    private void saveNTCTempValue() {

    }


    private void saveSettingData() {
        // LogUtil.d("CookerManager test fire---->" + fireValue + "---cookerID--->" + cookerID);
        CookerDataTableDao cookerDataTableDao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
        CookerDataTable cookerData = cookerDataTableDao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(cookerID)).build().unique();
        if (cookerData == null) {
            cookerData = new CookerDataTable(null,cookerID,workMode,hardwareWorkMode,tempMode,fireValue,tempValue,ntcTempValue,remainHourValue,remainMinuteValue,remainSecondValue,
                    tempIndicatorResID,recipesResID,isHighTemp,hasPan,errorMessage,isCookerPowerOn,isNeedToRecoverCookerUIForErrorDimiss,
                    saveWorkMode,saveHardwareWorkMode,saveTempMode,saveFireValue,saveTempValue,saveRemainHourValue,saveRemainMinuteValue,saveRemainSecondValue,
                    saveTempIndicatorResID,saveRecipesResID,saveHighTempFlag,savePanFlag,saveErrorMessage,savePowerOnFlag,saveRecoverFlag,
                    settingTempMode,settingFireValue,settingTempValue,settingHourValue,settingMinuteValue,settingSecondValue,
                    settingTempIndicatorResID,settingRecipesResID,""
            );
            cookerDataTableDao.insertOrReplace(cookerData);

        }else {
            cookerData.setFireValue(fireValue);
            cookerData.setTempValue(tempValue);
            cookerData.setTempMode(tempMode);
            cookerData.setRemainHourValue(remainHourValue);
            cookerData.setRemainMinuteValue(remainMinuteValue);
            cookerData.setRemainSecondValue(remainSecondValue);
            cookerData.setWorkMode(workMode);
            cookerData.setHardwareWorkMode(hardwareWorkMode);
            cookerData.setSaveHardwareWorkMode(hardwareWorkMode);
            cookerData.setTempIndicatorResID(tempIndicatorResID);
            cookerData.setRecipesResID(recipesResID);
            cookerData.setHighTempFlag(isHighTemp);
            cookerData.setPanFlag(hasPan);
            cookerData.setErrorMessage(errorMessage);
            cookerData.setPowerOnFlag(isCookerPowerOn);
            cookerData.setRecoverFlag(isNeedToRecoverCookerUIForErrorDimiss);

            cookerData.setSaveFireValue(saveFireValue);
            cookerData.setSaveTempValue(saveTempValue);
            cookerData.setSaveTempMode(saveTempMode);
            cookerData.setSaveRemainHourValue(saveRemainHourValue);
            cookerData.setSaveRemainMinuteValue(saveRemainMinuteValue);
            cookerData.setSaveRemainSecondValue(saveRemainSecondValue);
            cookerData.setSaveWorkMode(saveWorkMode);
            cookerData.setSaveTempIndicatorResID(saveTempIndicatorResID);
            cookerData.setSaveRecipesResID(saveRecipesResID);
            cookerData.setSaveHighTempFlag(saveHighTempFlag);
            cookerData.setSavePanFlag(savePanFlag);
            cookerData.setSaveErrorMessage(saveErrorMessage);
            cookerData.setSavePowerOnFlag(savePowerOnFlag);
            cookerData.setSaveRecoverFlag(saveRecoverFlag);

            cookerData.setSettingFireValue(settingFireValue);
            cookerData.setSettingTempMode(settingTempMode);
            cookerData.setSettingtempValue(settingTempValue);
            cookerData.setSettingHourValue(settingHourValue);
            cookerData.setSettingMinuteValue(settingMinuteValue);
            cookerData.setSettingSecondValue(settingSecondValue);
            cookerData.setSettingrecipesResID(settingRecipesResID);
            cookerData.setSettingtempIndicatorResID(settingTempIndicatorResID);
            cookerData.setNtcTempValue(ntcTempValue);
            cookerDataTableDao.update(cookerData);

        }


    }

    private void saveCookerPowerOffData() {
        CookerDataTableDao cookerDataTableDao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
        CookerDataTable cookerData = cookerDataTableDao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(cookerID)).build().unique();
        if (cookerData == null) {

            cookerData = new CookerDataTable(null,cookerID,workMode,hardwareWorkMode,tempMode,fireValue,tempValue,ntcTempValue,remainHourValue,remainMinuteValue,remainSecondValue,
                    tempIndicatorResID,recipesResID,isHighTemp,hasPan,errorMessage,isCookerPowerOn,isNeedToRecoverCookerUIForErrorDimiss,
                    saveWorkMode,saveHardwareWorkMode,saveTempMode,saveFireValue,saveTempValue,saveRemainHourValue,saveRemainMinuteValue,saveRemainSecondValue,
                    saveTempIndicatorResID,saveRecipesResID,saveHighTempFlag,savePanFlag,saveErrorMessage,savePowerOnFlag,saveRecoverFlag,
                    settingTempMode,settingFireValue,settingTempValue,settingHourValue,settingMinuteValue,settingSecondValue,
                    settingTempIndicatorResID,settingRecipesResID,""
            );
            cookerDataTableDao.insertOrReplace(cookerData);

        }else {

            cookerData.setFireValue(fireValue);
            cookerData.setTempValue(tempValue);
            cookerData.setTempMode(tempMode);
            cookerData.setRemainHourValue(remainHourValue);
            cookerData.setRemainMinuteValue(remainMinuteValue);
            cookerData.setRemainSecondValue(remainSecondValue);
            cookerData.setWorkMode(workMode);
            cookerData.setHardwareWorkMode(hardwareWorkMode);
            cookerData.setTempIndicatorResID(tempIndicatorResID);
            cookerData.setRecipesResID(recipesResID);
            cookerData.setHighTempFlag(isHighTemp);
            cookerData.setPanFlag(hasPan);
            cookerData.setErrorMessage(errorMessage);
            cookerData.setPowerOnFlag(isCookerPowerOn);
            cookerData.setRecoverFlag(isNeedToRecoverCookerUIForErrorDimiss);

            cookerData.setSaveFireValue(saveFireValue);
            cookerData.setSaveTempValue(saveTempValue);
            cookerData.setSaveTempMode(saveTempMode);
            cookerData.setSaveRemainHourValue(saveRemainHourValue);
            cookerData.setSaveRemainMinuteValue(saveRemainMinuteValue);
            cookerData.setSaveRemainSecondValue(saveRemainSecondValue);
            cookerData.setSaveWorkMode(saveWorkMode);
            cookerData.setSaveTempIndicatorResID(saveTempIndicatorResID);
            cookerData.setSaveRecipesResID(saveRecipesResID);
            cookerData.setSaveHighTempFlag(saveHighTempFlag);
            cookerData.setSavePanFlag(savePanFlag);
            cookerData.setSaveErrorMessage(saveErrorMessage);
            cookerData.setSavePowerOnFlag(savePowerOnFlag);
            cookerData.setSaveRecoverFlag(saveRecoverFlag);

            cookerData.setSettingFireValue(settingFireValue);
            cookerData.setSettingTempMode(settingTempMode);
            cookerData.setSettingtempValue(settingTempValue);
            cookerData.setSettingHourValue(settingHourValue);
            cookerData.setSettingMinuteValue(settingMinuteValue);
            cookerData.setSettingSecondValue(settingSecondValue);
            cookerData.setSettingrecipesResID(settingRecipesResID);
            cookerData.setSettingtempIndicatorResID(settingTempIndicatorResID);
            cookerData.setNtcTempValue(ntcTempValue);
            cookerDataTableDao.update(cookerData);

        }


    }



    private synchronized void notifyUpdateCookerUI() {
        if (EventBus.getDefault().hasSubscriberForEvent(CookerUpdateEvent.class)) {
//            LogUtil.d("Enter:: notifyUpdateCookerUI");
 //           LogUtil.d("samhung cookerID----->" + cookerID + " workmode----->" + workMode + "---realtemp--->" + realTempValue);
            CookerUpdateEvent event = null;
            if (fireValue ==11 || fireValue == 12) {
                int gear = 9;
                event = new CookerUpdateEvent(cookerID,workMode,tempMode,gear,tempValue,realTempValue,remainHourValue,remainMinuteValue,remainSecondValue,tempIndicatorResID,recipesResID,settingTempShowOrder,errorMessage);

            }else {
                if (tempMode == TFTHobConstant.TEMP_MODE_PRECISE_TEMP) {
                    event = new CookerUpdateEvent(cookerID,workMode,tempMode,fireValue,settingTempValue,realTempValue,remainHourValue,remainMinuteValue,remainSecondValue,tempIndicatorResID,recipesResID,settingTempShowOrder,errorMessage);

                }else {
                    event = new CookerUpdateEvent(cookerID,workMode,tempMode,fireValue,tempValue,realTempValue,remainHourValue,remainMinuteValue,remainSecondValue,tempIndicatorResID,recipesResID,settingTempShowOrder,errorMessage);

                }
            }

            EventBus.getDefault().post(event);

        }

    }

    private void notifyUpdateCookerUIForAutoSwitchPower() {
        if (EventBus.getDefault().hasSubscriberForEvent(CookerUpdateEvent.class)) {
//            LogUtil.d("Enter:: notifyUpdateCookerUI");
            LogUtil.d("samhung workmode----->" + workMode);
            CookerUpdateEvent event = null;
            int gear = 9;
            if (fireValue ==11 || fireValue == 12) gear = 9;
            if (tempMode == TFTHobConstant.TEMP_MODE_PRECISE_TEMP) {
                event = new CookerUpdateEvent(cookerID,workMode,tempMode,gear,settingTempValue,realTempValue,remainHourValue,remainMinuteValue,remainSecondValue,tempIndicatorResID,recipesResID,settingTempShowOrder,errorMessage);

            }else {
                event = new CookerUpdateEvent(cookerID,workMode,tempMode,gear,tempValue,realTempValue,remainHourValue,remainMinuteValue,remainSecondValue,tempIndicatorResID,recipesResID,settingTempShowOrder,errorMessage);

            }
            EventBus.getDefault().post(event);

        }

    }

    private void notifyUpdateCookerUnionUI(int unionCookerID, int upCookerID, int upWorkMode, int upFireValue, int upTempValue, int upHourValue, int upMinute, int upTempIndicatorID, int upRecipesID, String upErrorMessage, int downCookerID, int downWorkMode, int downFireValue, int downTempValue, int downHourValue, int downMinute, int downTempIndicatorID, int downRecipesID, String downErrorMessage) {
        if (EventBus.getDefault().hasSubscriberForEvent(CookerUnionUpdateEvent.class)) {
            CookerUnionUpdateEvent event = new CookerUnionUpdateEvent(unionCookerID, upCookerID, upWorkMode, upFireValue, upTempValue, upHourValue, upMinute, upTempIndicatorID, upRecipesID, upErrorMessage, downCookerID, downWorkMode, downFireValue, downTempValue, downHourValue, downMinute, downTempIndicatorID, downRecipesID, downErrorMessage);
            EventBus.getDefault().post(event);

        }


    }

    private void notifyUpdateCookerUIForTempTimerIsUp() {//warn user to keep warm or add 10 minute
        setWorkMode(TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_WORK_FINISH_WAIT_USER_CONFIRM);
        tempValue = 0;
        fireValue = 0;
        doStopAutoSwitchOffTime();
        //saveFireValue = fireValue;
        CookerDataTableDao cookerDataTableDao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
        CookerDataTable cookerData = cookerDataTableDao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(cookerID)).build().unique();
        if (cookerData != null) {
            LogUtil.d("cooker data  !=       null");
            cookerData.setWorkMode(workMode);
            cookerData.setFireValue(fireValue);
            cookerData.setTempValue(tempValue);
            cookerData.setSaveFireValue(saveFireValue);
            cookerData.setSaveTempValue(saveTempValue);
            cookerDataTableDao.update(cookerData);
        }else {
            LogUtil.d("cooker data  ==       null");
        }
        notifyUpdateCookerUI();
        int soundType = SoundEvent.getSoundType(cookerID, true);
        EventBus.getDefault().post(new SoundEvent(
                SoundEvent.SOUND_ACTION_PLAY,
                SoundUtil.SOUND_ID_ALARM_TIMER,
                soundType));
        Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PLAY, SoundUtil.SOUND_ID_ALARM_TIMER, " + soundType + ")");

    }



    private void doStartFiveMinuteCountDown() {
        LogUtil.d("Enter:: -----------------doStopFiveTimerCountDown---------------2--------");
        doStopFiveTimerCountDown();
        mFiveMinuteDisposable = Observable
                //.interval(0,1, TimeUnit.SECONDS)
                .interval(1,1, TimeUnit.MINUTES)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {

                        updateFiveMinuteRemainTime();


                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribe();
    }

    private void updateFiveMinuteRemainTime() {
        if (remainFiveMinuteHourValue != 0) {
            remainFiveMinuteHourValue--;
        }


        if (remainFiveMinuteHourValue == 0) {
            LogUtil.d("Enter:: -----------------doStopFiveTimerCountDown-----------------1------");
            doStopFiveTimerCountDown();
            notifyUpdateFireGearUIForBGearTimeIsUp();
        }
        LogUtil.d("remainFiveMinuteHourValue---------->" + remainFiveMinuteHourValue);
    }

    private void doStopFiveTimerCountDown() {
        LogUtil.d("Enter:: -----------------doStopFiveTimerCountDown-----------------------");
        if (mFiveMinuteDisposable != null) {
            if (!mFiveMinuteDisposable.isDisposed()) {
                mFiveMinuteDisposable.dispose();
            }
        }
    }

    private int remainMinuteValue1 = 0;
    private int remainSecondValue1 = 0;
    private void doStartTimerCountDown() {//定时器倒计时
        //LogUtil.d("Enter:: doStartTimerCountDown---cookerid--->" + cookerID);
        doStopTimerCountDown();
        mDisposable = Observable
                //.interval(0,1, TimeUnit.SECONDS)
                .interval(1,1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                       // LogUtil.d("Enter:: countdown----->" + aLong + "-----cookerid--->" + cookerID);

                        /*remainSecondValue1++;
                        if (remainSecondValue1 == 60) {
                            remainSecondValue1 = 0;
                            remainMinuteValue1++;
                        }

                        if (remainMinuteValue1 == 10) doStopTimerCountDown();

                        LogUtil.d("minute--->" + remainMinuteValue1 + "---->" + remainSecondValue1);*/

                        handler.sendEmptyMessage(HANDLER_UPDATE_COOKER_TIMER);

                       /* if (remainSecondValue != 0) {
                            remainSecondValue--;
                        }else {
                            remainSecondValue = 59;//60
                            if (remainMinuteValue != 0) remainMinuteValue--;
                            else {
                                remainMinuteValue = 59;
                                if (remainHourValue != 0) {
                                    remainHourValue--;
                                }
                            }
                           // notifyUpdateCookerUI();
                        }

                        if (remainMinuteValue == 0 && remainHourValue == 0 && remainSecondValue == 59) {
                            doStopTimerCountDown();
                        }
                        LogUtil.d("hour--->" + remainHourValue + "----minute--->" + remainMinuteValue + "---->" + remainSecondValue);*/




                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribe();
    }

    private void doStopTimerCountDown() {
        if (mDisposable != null) {
            if (!mDisposable.isDisposed()) {
                mDisposable.dispose();
            }

            mDisposable = null;
        }
    }

    private void updateTimerRemainTime() {
        if (remainSecondValue != 0) {
            remainSecondValue--;
            LogUtil.e("Enter:: -----------------onUpateTimer--------------1--------------->" + remainSecondValue);
        }else {
            remainSecondValue = 59;//60
            if (remainMinuteValue != 0) remainMinuteValue--;
            else {
                remainMinuteValue = 59;
                if (remainHourValue != 0) {
                    remainHourValue--;
                }
            }
            notifyUpdateCookerUI();
        }
       /* if (remainMinuteValue != 0) {
            remainMinuteValue--;
        }else {
            remainMinuteValue = 59;
            if (remainHourValue != 0) {
                remainHourValue--;
            }
        }*/
        saveRemainTime();

        if (remainMinuteValue == 0 && remainHourValue == 0 && remainSecondValue == 59) {
            LogUtil.d("Count down finish ");
            LogUtil.d("Enter:: ----------doStopTimerCountDown---------------5---------");
            doStopTimerCountDown();
            //
            if (workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_FIRE_GEAR_WITH_TIMER ||
                    workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER ||
                    workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER) {//我就觉得菜谱工作，工作完不需要提示保温和加十分钟吧 ？
                //LogUtil.d("-------samhung-----------------------5");
                notifyUpdateCookerUIForTempTimerIsUp();
            }else if (workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_FIRST ||
                    workMode == TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_SECOND
                    ){
                notifyCookerPowerOff();
            }

        }else {

        }

        LogUtil.d("Count down hour---->" + remainHourValue + "----remainMinuteValue-->" + remainMinuteValue + "----remainSecondValue---->" + remainSecondValue );
    }

    private void saveRemainTime() {
        saveRemainHourValue = remainHourValue;
        saveRemainMinuteValue = remainMinuteValue;
        saveRemainSecondValue = remainSecondValue;
    }

    long autoSwitchOffTimeTaskID = -1;
    private void startAutoSwitchOffTime() {
        doStopAutoSwitchOffTime();
        if (settingHourValue * 60 + settingMinuteValue == CookerSettingData.getMaxTimerMinutes(
                tempMode,
                fireValue,
                tempValue)) {
            doStartAutoSwitchOffTime(true);
        } else {
            doStartAutoSwitchOffTime(false);
        }
    }
    private void doStartAutoSwitchOffTime(final boolean withTimerMax) {
        LogUtil.d("Enter:: doStartAutoSwitchOffTime---->" + autoSwitchOffTimeTaskID);
        if (autoSwitchOffTimeTaskID != -1) return;
        int switchOffTime = CookingTimeTable.getCookingAutomaticSwitchOffTime((fireValue == -1)?tempValue:fireValue);
        LogUtil.d("cooking switch off time--->" + switchOffTime);
        if (fireValue == 9) {
            autoSwitchOffTimeTaskID = mCookerTimerManager.requestCountDown(CountDownTask.COUNT_DOWN_TASK_TYPE_COUNT_FOR_POWER_LEVEL_9, switchOffTime, new CookerTimerManager.CookerTimerManagerCallBack() {
                @Override
                public void notifyTimeIsUp(long taskID, int notifyAction, int totalTime, int remainTime) {
                    LogUtil.d("Enter:: notifyTimeIsUp---taskID--->" + taskID + "---notifyAction--->" + notifyAction + "---totalTime--->" + totalTime + "---remainTime--->" + remainTime);
                    if (notifyAction == CountDownTask.COUNT_DOWN_NOTIFY_ACTION_TOTAL_TIME_IS_UP) {
                        if (!withTimerMax) {
                            notifyCookerPowerOff();
                        }
                        autoSwitchOffTimeTaskID = -1;
                    }else if (notifyAction == CountDownTask.COUNT_DOWN_NOTIFY_ACTION_10_MINUTE_IS_UP) {
                        notifyUpdateFireGearUIFor9GearTimeIsUp(11);

                    }else if (notifyAction == CountDownTask.COUNT_DOWN_NOTIFY_ACTION_20_MINUTE_IS_UP) {
                        notifyUpdateFireGearUIFor9GearTimeIsUp(12);

                    }

                }
            });
        }else if (fireValue == 10) {
            autoSwitchOffTimeTaskID = mCookerTimerManager.requestCountDown(CountDownTask.COUNT_DOWN_TASK_TYPE_COUNT_FOR_POWER_LEVEL_B, switchOffTime, new CookerTimerManager.CookerTimerManagerCallBack() {
                @Override
                public void notifyTimeIsUp(long taskID, int notifyAction, int totalTime, int remainTime) {
                    LogUtil.d("Enter:: notifyTimeIsUp---taskID--->" + taskID + "---notifyAction--->" + notifyAction + "---totalTime--->" + totalTime + "---remainTime--->" + remainTime);


                    if (notifyAction == CountDownTask.COUNT_DOWN_NOTIFY_ACTION_TOTAL_TIME_IS_UP) {
                        if (!withTimerMax) {
                            notifyCookerPowerOff();
                        }
                        autoSwitchOffTimeTaskID = -1;
                    }else if (notifyAction == CountDownTask.COUNT_DOWN_NOTIFY_ACTION_5_MINUTE_IS_UP) {
                        notifyUpdateFireGearUIForBGearTimeIsUp();

                    }else if (notifyAction == CountDownTask.COUNT_DOWN_NOTIFY_ACTION_15_MINUTE_IS_UP) {
                        notifyUpdateFireGearUIFor9GearTimeIsUp(11);

                    }else if (notifyAction == CountDownTask.COUNT_DOWN_NOTIFY_ACTION_35_MINUTE_IS_UP) {
                        notifyUpdateFireGearUIFor9GearTimeIsUp(12);

                    }

                }
            });
        }else {
            autoSwitchOffTimeTaskID = mCookerTimerManager.requestCountDown(CountDownTask.COUNT_DOWN_TASK_TYPE_COUNT_NOTIFY_EVERY_MINUTE, switchOffTime, new CookerTimerManager.CookerTimerManagerCallBack() {
                @Override
                public void notifyTimeIsUp(long taskID, int notifyAction, int totalTime, int remainTime) {
                    LogUtil.d("Enter:: notifyTimeIsUp---taskID--->" + taskID + "---notifyAction--->" + notifyAction + "---totalTime--->" + totalTime + "---remainTime--->" + remainTime);
                    if (notifyAction == CountDownTask.COUNT_DOWN_NOTIFY_ACTION_TOTAL_TIME_IS_UP) {
                        if (!withTimerMax) {
                            notifyCookerPowerOff();
                        }
                        autoSwitchOffTimeTaskID = -1;
                    }
                }
            });
        }
    }

    private void doStopAutoSwitchOffTime() {
        if (autoSwitchOffTimeTaskID != -1) {
            mCookerTimerManager.finishCountDown(autoSwitchOffTimeTaskID);
            autoSwitchOffTimeTaskID = -1;
        }
    }

    private void doPauseAutoSwitchOffTime() {
        if (autoSwitchOffTimeTaskID != -1) {
            mCookerTimerManager.pauseCountDown(autoSwitchOffTimeTaskID);

        }
    }

    private void doResumeAutoSwitchOffTime() {
        if (autoSwitchOffTimeTaskID != -1) {
            mCookerTimerManager.resumeCountDown(autoSwitchOffTimeTaskID);

        }
    }

    /**
     * 当当前温度已经达到目标温度，同时UI已经显示了ready to cook,开启15Min倒计时，如果没人操作，则15分钟后自动关掉炉头
     * */
    long autoSwitchOffTimeForReadyToCookTaskID = -1;
    private void doStartAutoSwitchOffTimeForReadyToCook() {
        if (autoSwitchOffTimeForReadyToCookTaskID != -1) return;
        autoSwitchOffTimeForReadyToCookTaskID = mCookerTimerManager.requestCountDown(CountDownTask.COUNT_DOWN_TASK_TYPE_COUNT_NOTIFY_EVERY_MINUTE, COUNT_DOWN_TIME_FOR_NO_RESPONE_FOR_READY_TO_COOK, new CookerTimerManager.CookerTimerManagerCallBack() {
            @Override
            public void notifyTimeIsUp(long taskID, int notifyAction, int totalTime, int remainTime) {
                if (notifyAction == CountDownTask.COUNT_DOWN_NOTIFY_ACTION_TOTAL_TIME_IS_UP) {
                    LogUtil.d("Enter:: doStartAutoSwitchOffTimeForReadyToCook---time is up");
                    int soundType = SoundEvent.getSoundType(cookerID, false);
                    EventBus.getDefault().post(new SoundEvent(
                            SoundEvent.SOUND_ACTION_PAUSE,
                            SoundUtil.SOUND_ID_ALARM,
                            soundType));
                    Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PAUSE, SoundUtil.SOUND_ID_ALARM, " + soundType + ")");

                    notifyCookerPowerOff();
                    autoSwitchOffTimeTaskID = -1;
                }else if (notifyAction == CountDownTask.COUNT_DOWN_NOTIFY_ACTION_1_MINUTE_IS_UP) {
                    LogUtil.d("Enter:: doStartAutoSwitchOffTimeForReadyToCook---totalTime-->" + totalTime + "---remain---->" + remainTime);
                }
            }
        });
    }

    private void doStopAutoSwitchOffTimeForReadyToCook() {
        if (autoSwitchOffTimeForReadyToCookTaskID != -1) {
            mCookerTimerManager.finishCountDown(autoSwitchOffTimeForReadyToCookTaskID);
            autoSwitchOffTimeForReadyToCookTaskID = -1;
        }
    }

    private void doPauseAutoSwitchOffTimeForReadyToCook() {
        if (autoSwitchOffTimeForReadyToCookTaskID != -1) {
            mCookerTimerManager.pauseCountDown(autoSwitchOffTimeForReadyToCookTaskID);

        }
    }

    private void doResumeAutoSwitchOffTimeForReadyToCook() {
        if (autoSwitchOffTimeForReadyToCookTaskID != -1) {
            mCookerTimerManager.resumeCountDown(autoSwitchOffTimeForReadyToCookTaskID);

        }
    }

    /**
     * 开启快速控温温度补偿，原理是定时10分钟，每分钟加上固定的补偿温度如1度，让current temp value 尽快达到设置温度值，避免由于ntc回传温度值误差造成长时间无法到达设置温度
     * 减少用户等待的时间。这只是改变UI显示而已 ，但实际加热算法还是参考Ntc实际温度来做加热策略
     * */
    long fastModeTempTaskID = -1;
    int secondCouter = 0;
    int totalSecondForCompensateTmep = 20;
    private void doStartFastModeTemp(int deltaTemp) {
        if (fastModeTempTaskID != -1) return;



       /* if (deltaTemp >= 50) {
            totalSecondForCompensateTmep = (30 * 60) / deltaTemp;
        }else if (deltaTemp >= 40) {
            totalSecondForCompensateTmep = (25 * 60) / deltaTemp;
        }else if (deltaTemp >= 30) {
            totalSecondForCompensateTmep = (20 * 60) / deltaTemp;
        }else if (deltaTemp >= 20) {
            totalSecondForCompensateTmep = (15 * 60) / deltaTemp;
        }else {
            totalSecondForCompensateTmep = 20;
        }



        LogUtil.d("Enter:: totalSecondForCompensateTmep---->" + totalSecondForCompensateTmep + "-----deltaTemp----->" + deltaTemp);*/


        if (settingTempValue >= 90) totalSecondForCompensateTmep = 50;//65如果设置温度过高，大于等于90度，补偿温度的速度会慢一点，让炉头工作久一点，30表示30秒做一次补偿
        else if (settingTempValue >= 80) totalSecondForCompensateTmep = 65;//65
        else if (settingTempValue >= 60) totalSecondForCompensateTmep = 65;//60
        else totalSecondForCompensateTmep = 50;

        //totalSecondForCompensateTmep = 80;//100

        fastModeTempTaskID = mCookerTimerManager.requestCountDown(CountDownTask.COUNT_DOWN_TASK_TYPE_COUNT_NOTIFY_EVERY_SECOND, COUNT_DOWN_TIME_FOR_FAST_MODE_TEMP, new CookerTimerManager.CookerTimerManagerCallBack() {
            @Override
            public void notifyTimeIsUp(long taskID, int notifyAction, int totalTime, int remainTime) {
                if (notifyAction == CountDownTask.COUNT_DOWN_NOTIFY_ACTION_TOTAL_TIME_IS_UP) {
                    secondCouter = 0;
                    autoSwitchOffTimeTaskID = -1;
                    compensateTmepForFastMode = deltaTempForFastMode + compensateTmepForFastMode;
                }else if (notifyAction == CountDownTask.COUNT_DOWN_NOTIFY_ACTION_1_SECOND_IS_UP) {
                    //LogUtil.d("Enter:: fastModeTempTaskID---totalTime-->" + totalTime + "---remain---->" + remainTime);
                    if (secondCouter >= totalSecondForCompensateTmep) {
                        secondCouter = 0;
                        //deltaTempForFastMode = (settingTempValue - ntcTempValue) / remainTime;
                        deltaTempForFastMode = 1;
                        compensateTmepForFastMode = deltaTempForFastMode + compensateTmepForFastMode;
                        LogUtil.d("Enter: fast mode deltaTempForFastMode--->" + deltaTempForFastMode + "---totalSecondForCompensateTmep--->" + totalSecondForCompensateTmep);
                        LogUtil.d("Enter:: fastModeTempTaskID---totalTime-->" + totalTime + "---remain---->" + remainTime);

                    }else secondCouter++;

                }
            }
        });
    }

    private void doStopFastModeTemp() {
        if (fastModeTempTaskID != -1) {
            mCookerTimerManager.finishCountDown(fastModeTempTaskID);
            fastModeTempTaskID = -1;
        }
    }

    private void doPauseFastModeTemp() {
        if (fastModeTempTaskID != -1) {
            mCookerTimerManager.pauseCountDown(fastModeTempTaskID);

        }
    }

    private void doResumeFastModeTemp() {
        if (fastModeTempTaskID != -1) {
            mCookerTimerManager.resumeCountDown(fastModeTempTaskID);

        }
    }

    private void setWorkMode(int workMode) {
        int prevWorkMode = this.workMode;
        this.workMode = workMode;
        if (prevWorkMode != workMode) {
            String msg = "setWorkMode(Prev: " + getWorkModeString(prevWorkMode) + " Now " + getWorkModeString(workMode) + ") CookerID=" + cookerID;
            if (GlobalVars.getInstance().isInDebugMode()) {
                Logger.getInstance().i(msg, true);
            } else {
                Logger.getInstance().i(msg);
            }
        }
    }

    private void resetErrorDetectProcess() {
        countErrorMessageNumber = 0;
        errorMessage = "";
        currentErrorMessage = "";
    }

    private String getWorkModeString(int value) {
        switch (value) {
            case TFTHobConstant.HOB_VIEW_WORK_MODE_POWER_OFF:
                return "HOB_VIEW_WORK_MODE_POWER_OFF";
            case TFTHobConstant.HOB_WORK_MODE_FIRE_GEAR:
                return "HOB_WORK_MODE_FIRE_GEAR";
            case TFTHobConstant.HOB_WORK_MODE_FIRE_GEAR_WITH_TIMER:
                return "HOB_WORK_MODE_FIRE_GEAR_WITH_TIMER";
            case TFTHobConstant.HOB_WORK_MODE_TEMP_GEAR:
                return "HOB_WORK_MODE_TEMP_GEAR";
            case TFTHobConstant.HOB_WORK_MODE_TEMP_GEAR_WITH_TIMER:
                return "HOB_WORK_MODE_TEMP_GEAR_WITH_TIMER";
            case TFTHobConstant.HOB_WORK_MODE_TEMP_INDICATOR_GEAR:
                return "HOB_WORK_MODE_TEMP_INDICATOR_GEAR";
            case TFTHobConstant.HOB_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER:
                return "HOB_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER";
            case TFTHobConstant.HOB_WORK_MODE_ERROR_OCURR:
                return "HOB_WORK_MODE_ERROR_OCURR";
            case TFTHobConstant.HOB_WORK_MODE_STATUS_ABNORMAL_NO_PAN:
                return "HOB_WORK_MODE_STATUS_ABNORMAL_NO_PAN";
            case TFTHobConstant.HOB_WORK_MODE_STATUS_ABNORMAL_HIGH_TEMP:
                return "HOB_WORK_MODE_STATUS_ABNORMAL_HIGH_TEMP";
            case TFTHobConstant.HOB_WORK_MODE_PAUSE:
                return "HOB_WORK_MODE_PAUSE";
            case TFTHobConstant.HOB_WORK_MODE_WAIT_USER_CONFIRM:
                return "HOB_WORK_MODE_WAIT_USER_CONFIRM";
            case TFTHobConstant.HOB_WORK_MODE_WORK_ON_BACKGROUND:
                return "HOB_WORK_MODE_WORK_ON_BACKGROUND";
            case TFTHobConstant.HOB_WORK_MODE_PREPARE_TEMP_SENSOR:
                return "HOB_WORK_MODE_PREPARE_TEMP_SENSOR";
            case TFTHobConstant.HOB_WORK_MODE_TEMP_SENSOR_READY:
                return "HOB_WORK_MODE_TEMP_SENSOR_READY";
            case TFTHobConstant.HOB_WORK_MODE_TEMP_WORK_FINISH_WAIT_USER_CONFIRM:
                return "HOB_WORK_MODE_TEMP_WORK_FINISH_WAIT_USER_CONFIRM";
            case TFTHobConstant.HOB_VIEW_WORK_MODE_FIRE_GEAR_WITHOUT_TIMER:
                return "HOB_VIEW_WORK_MODE_FIRE_GEAR_WITHOUT_TIMER";
            case TFTHobConstant.HOB_VIEW_WORK_MODE_FIRE_GEAR_WITH_TIMER:
                return "HOB_VIEW_WORK_MODE_FIRE_GEAR_WITH_TIMER";
            case TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITHOUT_TIMER:
                return "HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITHOUT_TIMER";
            case TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITHOUT_TIMER:
                return "HOB_VIEW_WORK_MODE_TEMP_GEAR_WITHOUT_TIMER";
            case TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER:
                return "HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER";
            case TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER_AND_RECIPES_FIRST:
                return "HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER_AND_RECIPES_FIRST";
            case TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER_AND_RECIPES_SECOND:
                return "HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER_AND_RECIPES_SECOND";
            case TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER:
                return "HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER";
            case TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_FIRST:
                return "HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_FIRST";
            case TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_SECOND:
                return "HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_SECOND";
            case TFTHobConstant.HOB_VIEW_WORK_MODE_ABNORMAL_NO_PAN:
                return "HOB_VIEW_WORK_MODE_ABNORMAL_NO_PAN";
            case TFTHobConstant.HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP:
                return "HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP";
            case TFTHobConstant.HOB_VIEW_WORK_MODE_ABNORMAL_ERROR_OCURR:
                return "HOB_VIEW_WORK_MODE_ABNORMAL_ERROR_OCURR";
            case TFTHobConstant.HOB_VIEW_WORK_MODE_UPATE_TIMER:
                return "HOB_VIEW_WORK_MODE_UPATE_TIMER";
            case TFTHobConstant.HOB_VIEW_WORK_MODE_PAUSE:
                return "HOB_VIEW_WORK_MODE_PAUSE";
            case TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR:
                return "HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR";
            case TFTHobConstant.HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR_WITH_INDICATOR:
                return "HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR_WITH_INDICATOR";
            case TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_SENSOR_READY:
                return "HOB_VIEW_WORK_MODE_TEMP_SENSOR_READY";
            case TFTHobConstant.HOB_VIEW_WORK_MODE_TEMP_WORK_FINISH_WAIT_USER_CONFIRM:
                return "HOB_VIEW_WORK_MODE_TEMP_WORK_FINISH_WAIT_USER_CONFIRM";
            default:
                return "UNKNOWN " + value;
        }
    }
}
