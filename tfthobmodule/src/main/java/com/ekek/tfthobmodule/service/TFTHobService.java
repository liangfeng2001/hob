package com.ekek.tfthobmodule.service;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.shell.EKEKSocketClient;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.commonmodule.utils.Logger;
import com.ekek.hardwaremodule.constants.CookerConstant;
import com.ekek.hardwaremodule.entity.CookerHardwareResponse;
import com.ekek.hardwaremodule.entity.FTFDataModel;
import com.ekek.hardwaremodule.entity.PIDModel;
import com.ekek.hardwaremodule.event.CookerHardwareEvent;
import com.ekek.hardwaremodule.power.PowerConstant;
import com.ekek.hardwaremodule.protocol.InductionCookerProtocol;
import com.ekek.hardwaremodule.serial.InductionCookerHardwareManager;
import com.ekek.hardwaremodule.utils.DataParseUtil;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.database.SettingPreferencesUtil;
import com.ekek.settingmodule.events.ConnectivitySettingEvent;
import com.ekek.settingmodule.events.EKEKSocketEvent;
import com.ekek.settingmodule.events.PowerLimitEvent;
import com.ekek.tfthobmodule.bluetooth.BleTempManager;
import com.ekek.tfthobmodule.bluetooth.BluetoothManager;
import com.ekek.tfthobmodule.bluetooth.CataBluetoothManager;
import com.ekek.tfthobmodule.constants.TFTHobConfiguration;
import com.ekek.tfthobmodule.constants.TFTHobConstant;
import com.ekek.tfthobmodule.core.CookerHelper;
import com.ekek.tfthobmodule.data.CookerSettingData;
import com.ekek.tfthobmodule.database.SettingDbHelper;
import com.ekek.tfthobmodule.event.AgeingEvent;
import com.ekek.tfthobmodule.event.BleBatteryEvent;
import com.ekek.tfthobmodule.event.BleTempEvent;
import com.ekek.tfthobmodule.event.BluetoothEevent;
import com.ekek.tfthobmodule.event.CookerPanelHighTempEvent;
import com.ekek.tfthobmodule.event.CookerPowerOffEvent;
import com.ekek.tfthobmodule.event.DebugInfoEvent;
import com.ekek.tfthobmodule.event.SoundEvent;
import com.ekek.tfthobmodule.event.TempSensorRequestEvent;
import com.ekek.tfthobmodule.model.AllCookerDataEx;
import com.ekek.tfthobmodule.utils.HardwareDataUtil;
import com.ekek.tfthobmodule.utils.SoundManager;
import com.ekek.tfthobmodule.utils.SoundUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TFTHobService extends Service implements InductionCookerHardwareManager.OnInductionCookerHardwareManagerListener, BleTempManager.OnBleTempManagerListener {
    private final IBinder binder = new LocalBinder();
    private InductionCookerHardwareManager mInductionCookerHardwareManager;
    private CookerHelper mCookerHelper;
    private int ageingCountdownTime = TFTHobConfiguration.HOB_AGEING_TIME;
    private Disposable ageingDisposable;//老化定时器
    private Disposable mDisposable;//定时器
    private SoundManager mSoundManager;
    private BleTempManager mBleTempManager;
    private BluetoothManager mBluetoothManager;
    private CataBluetoothManager mCataBluetoothManager;
    private Context context;
    private PIDModel mPIDModel;

    private PowerManager.WakeLock wakeLock;
    private ScreenStatusReceiver mScreenStatusReceiver;
    private volatile boolean hardwareResponsePaused = false;
    private boolean panelHighTempHandled;
    private boolean lastPanelHighTemp;
    private long panelHighTempStarted;
    private byte mhoodLevel=0;
    private boolean mSetTurboToNextPowerFlag=false;
    private boolean mTimerWorkingFlag=false;
    private int mSetTurboToNextPowerNum=0;

    private EKEKSocketClient mEKEKSocketClient;//系统socket，用于调用adb命令等操作

    @Override
    public void onCookerHardwareResponse(CookerHardwareResponse response) {
        if (hardwareResponsePaused) {
            return;
        }

        if (EventBus.getDefault().hasSubscriberForEvent(CookerHardwareEvent.class)) {
            CookerHardwareEvent event = new CookerHardwareEvent(response);
            EventBus.getDefault().post(event);
        }

        if (EventBus.getDefault().hasSubscriberForEvent(CookerPanelHighTempEvent.class)) {

            if (response.getaCookerMessage().isPanelHighTemp()) {
                if (!lastPanelHighTemp) {
                    // 从非高温状态变为高温状态
                    lastPanelHighTemp = true;
                    panelHighTempHandled = false;
                    panelHighTempStarted = SystemClock.elapsedRealtime();
                } else {
                    // 持续收到高温状态
                    if (SystemClock.elapsedRealtime() - panelHighTempStarted > 5 * 1000) {
                        if (!panelHighTempHandled) {
                            doPowerOffAllCookers();
                            CookerPanelHighTempEvent cookerPanelHighTempEvent = new CookerPanelHighTempEvent(true);
                            EventBus.getDefault().post(cookerPanelHighTempEvent);
                            panelHighTempHandled = true;
                        }
                    }
                }
            } else {
                if (lastPanelHighTemp) {
                    // 从高温状态变为非高温状态
                    if (panelHighTempHandled) {
                        CookerPanelHighTempEvent cookerPanelHighTempEvent = new CookerPanelHighTempEvent(false);
                        EventBus.getDefault().post(cookerPanelHighTempEvent);
                        panelHighTempHandled = false;
                    }
                }
            }
        }

        LogUtil.d("rawdata---->" + DataParseUtil.bytesToHexString(response.getRawData(),response.getRawData().length));

        DebugInfoEvent debugInfo = new DebugInfoEvent();
        debugInfo.setInDebugMode(GlobalVars.getInstance().isInDebugMode());
        debugInfo.setDataReceived(response.getRawData());

        mCookerHelper.notifyUpdateCookerMessage(response);
        if (mInductionCookerHardwareManager != null) {
            boolean mode04 = true;
            int enterPowerOffMode = SettingPreferencesUtil.getEnterPowerOffMode(context);
            switch (enterPowerOffMode) {
                case CataSettingConstant.EnterNone:
                case CataSettingConstant.EnterPowerOnModel:
                case CataSettingConstant.EnterHobWorkingFragment:
                    mode04 = true;
                    break;
                case CataSettingConstant.EnterPowerOffModelDelay:
                case CataSettingConstant.EnterLanguageSettingFragment:
                case CataSettingConstant.EnterDateSettingFragment:
                case CataSettingConstant.EnterTimeSettingFragment:
                case CataSettingConstant.EnterHobIntroFragment:
                case CataSettingConstant.EnterPowerOffModelFromSerialPort:
                    mode04 = false;
                    break;
            }

            FTFDataModel model = HardwareDataUtil.getFTFDataModel(
                    getApplicationContext(),
                    mode04);
            AllCookerDataEx dataEx = makeCookerData(model);
            AllCookerDataEx demoData = dataEx.clone();

            if (SettingPreferencesUtil.getDemoSwitchStatus(context)
                            .equals(CataSettingConstant.DEMO_SWITCH_STATUS_OPEN)) {
                if (demoData.getaMode() >= 1 && demoData.getaMode() <= 3) {
                    demoData.setaMode(4);
                }
                demoData.setaFireValue(0);
                demoData.setbMode(4);
                demoData.setbFireValue(0);
                demoData.setcMode(4);
                demoData.setcFireValue(0);
                demoData.setdMode(4);
                demoData.setdFireValue(0);
                demoData.seteMode(4);
                demoData.seteFireValue(0);
                demoData.setfMode(4);
                demoData.setfFireValue(0);
            } else {
                switch (TFTHobConfiguration.TFT_HOB_TYPE) {
                    case TFTHobConfiguration.TFT_HOB_TYPE_60:
                        break;
                    case TFTHobConfiguration.TFT_HOB_TYPE_90:
                        if (dataEx.geteMode() != InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_UNION
                                && dataEx.getfMode() != InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_UNION) {

                            int eFireValue = dataEx.geteFireValue();
                            int fFireValue = dataEx.getfFireValue();

                            if (fFireValue == 10) {
                                if (eFireValue > 0 && eFireValue < 7) {
                                    demoData.setfFireValue(9);
                                }
                            } else if (fFireValue == 9) {
                                if (eFireValue > 0 && eFireValue < 8) {
                                    demoData.setfFireValue(8);
                                }
                            }

                            if (eFireValue == 10) {
                                if (fFireValue > 0 && fFireValue < 8) {
                                    demoData.seteFireValue(9);
                                }
                            }
                        }
                    case TFTHobConfiguration.TFT_HOB_TYPE_80:

                        if (dataEx.getaMode() != InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_UNION
                                && dataEx.getbMode() != InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_UNION) {

                            int aFireValue = dataEx.getaFireValue();
                            int bFireValue = dataEx.getbFireValue();

                            if (aFireValue == 10) {
                                if (bFireValue > 0 && bFireValue < 7) {
                                    demoData.setaFireValue(9);
                                }
                            } else if (aFireValue == 9) {
                                if (bFireValue > 0 && bFireValue < 8) {
                                    demoData.setaFireValue(8);
                                }
                            }

                            if (bFireValue == 10) {
                                if (aFireValue > 0 && aFireValue < 8) {
                                    demoData.setbFireValue(9);
                                }
                            }
                        }
                        break;
                }
            }
            mhoodLevel=(byte) demoData.getHoodValue();
            mhoodLevel= (byte)(mhoodLevel & 0x0f);
            mTimerWorkingFlag=((demoData.getHoodValue() & 0x10)==0x10);  // 是否有定时
            if(mTimerWorkingFlag){
                mSetTurboToNextPowerFlag=false;
                GlobalVars.getInstance().setSetTurboToNextPower(false);
            }
            if(GlobalVars.getInstance().isSetTurboToNextPower()){  // Turbo 档 降到 5档
                byte  hoodLevel=(byte) demoData.getHoodValue();
                hoodLevel=(byte)(hoodLevel & 0xf0);
                hoodLevel=(byte)(hoodLevel|0x05);
                demoData.setHoodValue((int)hoodLevel);
             //   mSetTurboToNextPowerFlag=false;
               // LogUtil.d("liang set turbo 0  "+ mTimerWorkingFlag);
            }

            debugInfo.setDataSent(demoData);
            if (0 == mInductionCookerHardwareManager.requestSendCookerDataForDemoMode(
                    dataEx,
                    demoData,
                    true)) {
                debugInfo.setSuccessfullySent(true);
            }

            LogUtil.d("TotalPower--final--->" + mInductionCookerHardwareManager.requestGetFinalTotalPower() + "---current total--->"+ mInductionCookerHardwareManager.requestGetCurrentTotalPower() + "---Left--->" + mInductionCookerHardwareManager.requestGetCurrentTotalSingleLeftPower() + "----Right---->" + mInductionCookerHardwareManager.requestGetCurrentTotalSingleRightPower());
        }
        EventBus.getDefault().post(debugInfo);
    }

    private AllCookerDataEx makeCookerData(FTFDataModel data) {
        LogUtil.d("Enter:: makeCookerData--->" + data.getaTempGear());
        AllCookerDataEx allCookerData = new AllCookerDataEx(//模式 档位 温度
                data.getaMode(),data.getaFireGear(),data.getaTempGear(),
                data.getbMode(),data.getbFireGear(),data.getbTempGear(),
                data.getcMode(),data.getcFireGear(),data.getcTempGear(),
                data.getdMode(),data.getdFireGear(),data.getdTempGear(),
                data.geteMode(),data.geteFireGear(),data.geteTempGear(),
                data.getfMode(),data.getfFireGear(),data.getfTempGear(),
                data.getTempSensorValue(),data.getFanGear(),data.getLightGear()
        );
        return allCookerData;
    }

    /*
    *    public static final int COOKER_TYPE_A_COOKER = 1;
    public static final int COOKER_TYPE_B_COOKER = 2;
    public static final int COOKER_TYPE_C_COOKER = 3;
    public static final int COOKER_TYPE_D_COOKER = 4;
    public static final int COOKER_TYPE_E_COOKER = 5;
    public static final int COOKER_TYPE_F_COOKER = 6;
    * */
    public void doPowerOffAllCookers() {
        mCookerHelper.notifyCookerPowerOff(TFTHobConstant.COOKER_TYPE_A_COOKER);
        mCookerHelper.notifyCookerPowerOff(TFTHobConstant.COOKER_TYPE_B_COOKER);
        mCookerHelper.notifyCookerPowerOff(TFTHobConstant.COOKER_TYPE_AB_COOKER);
        mCookerHelper.notifyCookerPowerOff(TFTHobConstant.COOKER_TYPE_C_COOKER);
        mCookerHelper.notifyCookerPowerOff(TFTHobConstant.COOKER_TYPE_D_COOKER);
        mCookerHelper.notifyCookerPowerOff(TFTHobConstant.COOKER_TYPE_E_COOKER);
        mCookerHelper.notifyCookerPowerOff(TFTHobConstant.COOKER_TYPE_F_COOKER);
        mCookerHelper.notifyCookerPowerOff(TFTHobConstant.COOKER_TYPE_EF_COOKER);
    }

    public void pauseHardware() {
        hardwareResponsePaused = true;
    }

    public void resumeHardware() {
        hardwareResponsePaused = false;
    }

    @Override
    public void onTempSensorReady() {
        //LogUtil.d("Enter:: TempSensorRequestEvent--------00--------->");
        //TempSensorRequestEvent event = new TempSensorRequestEvent(-1,TempSensorRequestEvent.ACTION_TEMP_SENSOR_READY_TO_WORK);
        //EventBus.getDefault().post(event);
        EventBus.getDefault().post(new BleBatteryEvent(BleBatteryEvent.BATTERY_VIEW_STATUS_SHOW,0,0));
        mCookerHelper.notifyCookerTempSensorReady();
        mPIDModel = new PIDModel(80);

    }

    @Override
    public void onTempSensorStop() {
        LogUtil.d("samhungsamhungsamhung-------->onTempSensorStop");
        EventBus.getDefault().post(new BleBatteryEvent(BleBatteryEvent.BATTERY_VIEW_STATUS_HIDE,0,0));
        int cookerID = SettingDbHelper.getTemperatureSensorStatus();
        if (cookerID > 10) {
            mCookerHelper.notifyCookerPowerOff(cookerID / 10);
            mCookerHelper.notifyCookerPowerOff(cookerID % 10);
        }else {
            mCookerHelper.notifyCookerPowerOff(cookerID);
        }
        EventBus.getDefault().post(new CookerPowerOffEvent(cookerID));

        Toast.makeText(this, "Bluetooth connect fail, please try again",Toast.LENGTH_LONG).show();
        mPIDModel = null;
    }

    @Override
    public void onTempSensorTempUpdate(int tempValue) {
        LogUtil.d("temp sensor--->" + tempValue);
        tempValue = tempValue + 1;
        SettingDbHelper.saveTemperatureSensorValue(tempValue);
        EventBus.getDefault().post(new BleTempEvent(tempValue));
        if (mPIDModel != null) mPIDModel.updateValue(tempValue);

    }

    @Override
    public void onTempSensorBatteryUpdate(int chargeStatus, int batteryLevel) {
        LogUtil.d("Enter:: onTempSensorBatteryUpdate---->battery--->" + batteryLevel);
        EventBus.getDefault().post(new BleBatteryEvent(BleBatteryEvent.BATTERY_VIEW_STATUS_UPDATE_LEVEL,1,batteryLevel));
    }


    public class LocalBinder extends Binder {
        public TFTHobService getService() {
            return TFTHobService.this;
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Logger.getInstance().i("onBind()");
        return binder;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AgeingEvent event) {
        int action = event.getAction();
        switch (action) {
            case AgeingEvent.AGEING_ACTION_START:
                ageingCountdownTime = TFTHobConfiguration.HOB_AGEING_TIME;
                startAgeingCountDown();
                break;
            case AgeingEvent.AGEING_ACTION_RESTART:
                reStartAgeingCountDown();
                break;
            case AgeingEvent.AGEING_ACTION_END:
                endAgeing();
                break;
            case AgeingEvent.AGEING_ACTION_PLAY:
                ageingPlay();
                break;
            case AgeingEvent.AGEING_ACTION_PAUSE:
                ageingPause();
                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SoundEvent event) {
        int action = event.getAction();
        int soundID = event.getSoundID();
        LogUtil.d("action--->" + action + "---soundID---->" + soundID);
        if (action == SoundEvent.SOUND_ACTION_PAUSE_ALL) {
            mSoundManager.stopAlarm(SoundEvent.SOUND_TYPE_COMMON_ALARM);
            mSoundManager.stopAlarmOnce(SoundEvent.SOUND_TYPE_ALARM_ONCE);
            mSoundManager.stopAlarm(SoundEvent.SOUND_TYPE_ALARM_NTC);
            mSoundManager.stopAlarm(SoundEvent.SOUND_TYPE_ALARM_A_COOKER);
            mSoundManager.stopAlarm(SoundEvent.SOUND_TYPE_ALARM_B_COOKER);
            mSoundManager.stopAlarm(SoundEvent.SOUND_TYPE_ALARM_C_COOKER);
            mSoundManager.stopAlarm(SoundEvent.SOUND_TYPE_ALARM_D_COOKER);
            mSoundManager.stopAlarm(SoundEvent.SOUND_TYPE_ALARM_E_COOKER);
            mSoundManager.stopAlarm(SoundEvent.SOUND_TYPE_ALARM_F_COOKER);
            mSoundManager.stopAlarm(SoundEvent.SOUND_TYPE_TIMER_ALARM_A_COOKER);
            mSoundManager.stopAlarm(SoundEvent.SOUND_TYPE_TIMER_ALARM_B_COOKER);
            mSoundManager.stopAlarm(SoundEvent.SOUND_TYPE_TIMER_ALARM_C_COOKER);
            mSoundManager.stopAlarm(SoundEvent.SOUND_TYPE_TIMER_ALARM_D_COOKER);
            mSoundManager.stopAlarm(SoundEvent.SOUND_TYPE_TIMER_ALARM_E_COOKER);
            mSoundManager.stopAlarm(SoundEvent.SOUND_TYPE_TIMER_ALARM_F_COOKER);
            return;
        }
        switch (soundID) {
            case SoundUtil.SOUND_ID_ALARM:
                if (action == SoundEvent.SOUND_ACTION_PLAY) {
                    mSoundManager.playAlarm(event.getSoundType());
                }else if (action == SoundEvent.SOUND_ACTION_PAUSE){
                    mSoundManager.stopAlarm(event.getSoundType());
                }
                break;
            case SoundUtil.SOUND_ID_ALARM_ONCE:
                if (action == SoundEvent.SOUND_ACTION_PLAY) {
                    mSoundManager.playAlarmOnce(event.getSoundType());
                }else if (action == SoundEvent.SOUND_ACTION_PAUSE){
                    mSoundManager.stopAlarmOnce(event.getSoundType());
                }
                break;
            case SoundUtil.SOUND_ID_ALARM_NTC:
                if (action == SoundEvent.SOUND_ACTION_PLAY) {
                    mSoundManager.playAlarmForNtc();
                }else if (action == SoundEvent.SOUND_ACTION_PAUSE){
                    mSoundManager.stopAlarmForNtc();
                }
                break;
            case SoundUtil.SOUND_ID_ALARM_TIMER:
                if (action == SoundEvent.SOUND_ACTION_PLAY) {
                    mSoundManager.playAlarmForTimer(event.getSoundType());
                }else if (action == SoundEvent.SOUND_ACTION_PAUSE){
                    mSoundManager.stopAlarmForTimer(event.getSoundType());
                }
                break;
            case SoundUtil.SOUND_ID_SCROLL:
                if (action == SoundEvent.SOUND_ACTION_PLAY) {
                    String status = SettingPreferencesUtil.getClickSoundSwitchStatus(getApplicationContext());
                    if (CataSettingConstant.SOUND_SWITCH_STATUS_OPEN.equals(status)) {
                        mSoundManager.playScrollSound();
                    }
                }else if (action == SoundEvent.SOUND_ACTION_PAUSE){
                    mSoundManager.stopScrollSound();
                }
                break;
            default:
                if (action == SoundEvent.SOUND_ACTION_PAUSE) {
                    SoundUtil.stop(soundID);
                }else if (action == SoundEvent.SOUND_ACTION_PLAY) {
                    SoundUtil.play(soundID);
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PowerLimitEvent event) {

        String powerLimitSwitchStatus = SettingPreferencesUtil.getPowerLimitSwitchStatus(context);
        int totalPower = PowerConstant.POWER_LIMIT_TOTAL_LIMIT_DEFAULT;
        if (TFTHobConfiguration.TFT_HOB_TYPE == TFTHobConfiguration.TFT_HOB_TYPE_60) {
            totalPower = PowerConstant.POWER_LIMIT_TOTAL_LIMIT_6600;
        }else if (TFTHobConfiguration.TFT_HOB_TYPE == TFTHobConfiguration.TFT_HOB_TYPE_80) {
            totalPower = PowerConstant.POWER_LIMIT_TOTAL_LIMIT_7200;
        }else if (TFTHobConfiguration.TFT_HOB_TYPE == TFTHobConfiguration.TFT_HOB_TYPE_90) {
            totalPower = PowerConstant.POWER_LIMIT_TOTAL_LIMIT_9000;
        }


        if (powerLimitSwitchStatus.equals(CataSettingConstant.POWER_LIMIT_SWITCH_STATUS_OPEN)) {
            totalPower = SettingPreferencesUtil.getPowerLimitLevel(context);
        }

        boolean result = mInductionCookerHardwareManager.requestSetTotalPower(totalPower);
        LogUtil.d("Enter:: PowerLimitEvent result ---->" + result);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TempSensorRequestEvent event) {
        int action = event.getAction();
        if (action == TempSensorRequestEvent.ACTION_FIND_AND_CONNECT_TEMP_SENSOR) {
            mBleTempManager.findAndConnectSensorSlave(context);

        }else if (action == TempSensorRequestEvent.ACTION_STOP_TEMP_SENSOR) {
            mBleTempManager.stopSensorSlaveWork();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ConnectivitySettingEvent event) {
        int mode = event.getMode();
        if (mBluetoothManager != null) mBluetoothManager.setBTWorkMode(mode);
       // if (mBluetoothManager != null) mBluetoothManager.setBTWorkMode(mode);
        //if (mCataBluetoothManager != null) mCataBluetoothManager.setBTWorkMode(mode);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BluetoothEevent event) {
        doDisconnectForBT();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EKEKSocketEvent event) {
        List<String> commandList = event.getCommandList();
        for (String command : commandList) {
            mEKEKSocketClient.execute(command);
        }

    }




    @Override
    public void onCreate() {
        super.onCreate();
        registerEventBus();
        initEKEKSocketClient();
        initAndroidSystemLogTrack();
        initCooker();
        initHardWare();
        initSound();
       // initTempSensor();
        initBluetooth();
        startCountdown();
        //initCataBluetooth();
      //  requestWakeLock();
        //registSreenStatusReceiver();

    }

    private void initEKEKSocketClient() {
        mEKEKSocketClient = new EKEKSocketClient();

        //第一次启动或者app crash 后重新启动都会去清除蓝牙数据，以防crash是由于bluetooth share crash造成的
        //清除系统蓝牙配置数据，避免bluetooth share crash
        List<String> commandList = new ArrayList<>();
        commandList.add("rm /data/misc/bluedroid/bt_config.xml");
        commandList.add("rm /data/misc/bluedroid/bt_config.old");
        EventBus.getDefault().post(new EKEKSocketEvent(commandList));
    }

    private void initAndroidSystemLogTrack() {
        if (mEKEKSocketClient == null) mEKEKSocketClient = new EKEKSocketClient();

        String path = "/mnt/sdcard/catasystemlog/";
        File file = new File(path);
        if (!file.exists()) file.mkdir();
        else {
            File[] files = file.listFiles();
            Arrays.sort(files, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    long diff = f1.lastModified() - f2.lastModified();
                    if (diff > 0)
                        return 1;
                    else if (diff == 0)
                        return 0;
                    else
                        return -1;
                }

                public boolean equals(Object obj) {
                    return true;
                }

            });

            if (files.length > 10) {
                for (int i = 0; i < files.length; i++) {
                    if (i > 10) files[i].delete();
                }
            }

        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date(System.currentTimeMillis());
        String fileName = simpleDateFormat.format(date) + ".log";
        List<String> commandList = new ArrayList<>();
        commandList.add("logcat -v time > /mnt/sdcard/catasystemlog/" + fileName + "&");
        EventBus.getDefault().post(new EKEKSocketEvent(commandList));

    }


    private void requestWakeLock() {
        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK , "CataTFTHobApplication:GTermWakelock");
        wakeLock.acquire();
    }

    private void releaseWakeLock() {
        if (wakeLock != null) {
            wakeLock.release();
        }
    }

    private void initBluetooth() {
        int btMode = TFTHobConstant.BLUETOOTH_OPTIONS_DISABLE;
        if (SettingPreferencesUtil.getBluetoothSwitchStatus(getApplicationContext()).equals(CataSettingConstant.BLUETOOTH_SWITCH_STATUS_OPEN)) {
            if (SettingPreferencesUtil.getBluetoothStyle(getApplicationContext()).equals(CataSettingConstant.BLUETOOTH_SENSOR_STYLE)) {
                btMode = TFTHobConstant.BLUETOOTH_OPTIONS_TEMPERTURE_SENSOR;
            }else if (SettingPreferencesUtil.getBluetoothStyle(getApplicationContext()).equals(CataSettingConstant.BLUETOOTH_PHONE_STYLE)) {
                btMode = TFTHobConstant.BLUETOOTH_OPTIONS_SMART_PHONE;
            }else {
                //防止getBluetoothStyle取到的值是空的
                btMode = TFTHobConstant.BLUETOOTH_OPTIONS_TEMPERTURE_SENSOR;
            }
        }else if (SettingPreferencesUtil.getBluetoothSwitchStatus(getApplicationContext()).equals(CataSettingConstant.BLUETOOTH_SWITCH_STATUS_CLOSE)) {
            btMode = TFTHobConstant.BLUETOOTH_OPTIONS_DISABLE;
        }

        mBluetoothManager = new BluetoothManager(getApplicationContext(), btMode,new BluetoothManager.OnBluetoothManagerListener() {
            @Override
            public void onTempSensorReady() {
                LogUtil.d("Enter:: onTempSensorReady");
                SettingDbHelper.saveTemperatureSensorStatus(-1);
                EventBus.getDefault().post(new BleBatteryEvent(BleBatteryEvent.BATTERY_VIEW_STATUS_SHOW,0,0));
                mCookerHelper.notifyCookerTempSensorReady();
                mPIDModel = new PIDModel(80);
            }

            @Override
            public void onTempSensorStop(int stopType) {
                EventBus.getDefault().post(new BleBatteryEvent(BleBatteryEvent.BATTERY_VIEW_STATUS_HIDE,0,0 , stopType));
                int cookerID = SettingDbHelper.getTemperatureSensorStatus();
                LogUtil.d("Enter::samhung enter onTempSensorStop---->" + cookerID);
                mCookerHelper.notifyCookerPowerOff(cookerID);
                EventBus.getDefault().post(new CookerPowerOffEvent(cookerID));
                SettingDbHelper.saveTemperatureSensorStatus(0);
                mPIDModel = null;
            }

            @Override
            public void onTempSensorTempUpdate(int tempValue) {
                LogUtil.d("temp sensor--->" + tempValue + "---cookerID-->" + SettingDbHelper.getTemperatureSensorStatus());
                //tempValue = tempValue + 1;// + 1
                SettingDbHelper.saveTemperatureSensorValue(tempValue);
                BleTempEvent event = new BleTempEvent(tempValue,SettingDbHelper.getTemperatureSensorStatus());
                EventBus.getDefault().post(event);
                LogUtil.d("Enter:: onTempSensorTempUpdate--->" + event.getCookerID());
                if (mPIDModel != null) mPIDModel.updateValue(tempValue);
            }

            @Override
            public void onTempSensorBatteryUpdate(int chargeStatus, int batteryLevel) {
                LogUtil.d("Enter:: onTempSensorBatteryUpdate---->battery--->" + batteryLevel);
                EventBus.getDefault().post(new BleBatteryEvent(BleBatteryEvent.BATTERY_VIEW_STATUS_UPDATE_LEVEL,1,batteryLevel ));
            }

            @Override
            public void onTempSensorBatteryChargeStateUpdate(int chargeState) {
                LogUtil.d("Enter:: onTempSensorBatteryChargeStateUpdate---->chargeState--->" + chargeState);
                EventBus.getDefault().post(new BleBatteryEvent(BleBatteryEvent.BATTERY_VIEW_STATUS_UPDATE_CHARGE_STATE, chargeState));
            }
        });
    }

    /*private void initCataBluetooth() {
        int btMode = TFTHobConstant.BLUETOOTH_OPTIONS_DISABLE;
        if (SettingPreferencesUtil.getBluetoothSwitchStatus(getApplicationContext()).equals(CataSettingConstant.BLUETOOTH_SWITCH_STATUS_OPEN)) {
            if (SettingPreferencesUtil.getBluetoothStyle(getApplicationContext()).equals(CataSettingConstant.BLUETOOTH_SENSOR_STYLE)) {
                btMode = TFTHobConstant.BLUETOOTH_OPTIONS_TEMPERTURE_SENSOR;
            }else if (SettingPreferencesUtil.getBluetoothStyle(getApplicationContext()).equals(CataSettingConstant.BLUETOOTH_PHONE_STYLE)) {
                btMode = TFTHobConstant.BLUETOOTH_OPTIONS_SMART_PHONE;
            }
        }else if (SettingPreferencesUtil.getBluetoothSwitchStatus(getApplicationContext()).equals(CataSettingConstant.BLUETOOTH_SWITCH_STATUS_CLOSE)) {
            btMode = TFTHobConstant.BLUETOOTH_OPTIONS_DISABLE;
        }

        mCataBluetoothManager = new CataBluetoothManager(getApplicationContext(), btMode,new CataBluetoothManager.OnCataBluetoothManagerListener() {
            @Override
            public void onTempSensorReady() {
                LogUtil.d("Enter:: onTempSensorReady");
                SettingDbHelper.saveTemperatureSensorStatus(-1);
                EventBus.getDefault().post(new BleBatteryEvent(BleBatteryEvent.BATTERY_VIEW_STATUS_SHOW,0,0));
                mCookerHelper.notifyCookerTempSensorReady();
                mPIDModel = new PIDModel(80);
            }

            @Override
            public void onTempSensorStop() {

                EventBus.getDefault().post(new BleBatteryEvent(BleBatteryEvent.BATTERY_VIEW_STATUS_HIDE,0,0));
                int cookerID = SettingDbHelper.getTemperatureSensorStatus();

                if (cookerID > 10) {
                    mCookerHelper.notifyCookerPowerOff(cookerID / 10);
                    mCookerHelper.notifyCookerPowerOff(cookerID % 10);
                }else {
                    mCookerHelper.notifyCookerPowerOff(cookerID);
                }
                EventBus.getDefault().post(new CookerPowerOffEvent(cookerID));
                SettingDbHelper.saveTemperatureSensorStatus(0);
                mPIDModel = null;
            }

            @Override
            public void onTempSensorTempUpdate(int tempValue) {
                LogUtil.d("temp sensor--->" + tempValue);
                tempValue = tempValue + 1;
                SettingDbHelper.saveTemperatureSensorValue(tempValue);
                EventBus.getDefault().post(new BleTempEvent(tempValue,SettingDbHelper.getTemperatureSensorStatus()));
                if (mPIDModel != null) mPIDModel.updateValue(tempValue);
            }

            @Override
            public void onTempSensorBatteryUpdate(int chargeStatus, int batteryLevel) {
                LogUtil.d("Enter:: onTempSensorBatteryUpdate---->battery--->" + batteryLevel);
                EventBus.getDefault().post(new BleBatteryEvent(BleBatteryEvent.BATTERY_VIEW_STATUS_UPDATE_LEVEL,chargeStatus,batteryLevel));
            }
        });
    }

    public void doDisconnectForBT() {
        mBluetoothManager.stopSensorSlaveWork();
    }

    public void recoverConnectForBT() {
        mBluetoothManager.findAndConnectSensorSlave(getApplicationContext());
    }



    private void initTempSensor() {
        mBleTempManager = new BleTempManager(getApplicationContext(),this);
    }*/

    public void doDisconnectForBT() {
        mBluetoothManager.stopSensorSlaveWork();
    }

    public void recoverConnectForBT() {
        mBluetoothManager.findAndConnectSensorSlave(getApplicationContext());
    }

    private void initSound() {
        SoundUtil.init(getApplicationContext());
        mSoundManager = SoundManager.getInstance(getApplicationContext());
    }

    private void initCooker() {
        mCookerHelper = new CookerHelper();
    }

    private void initHardWare() {
        LogUtil.d("Enter:: initHardWare()");
        String powerLimitSwitchStatus = SettingPreferencesUtil.getPowerLimitSwitchStatus(context);
        int totalPower = PowerConstant.POWER_LIMIT_TOTAL_LIMIT_DEFAULT;
        if (powerLimitSwitchStatus.equals(CataSettingConstant.POWER_LIMIT_SWITCH_STATUS_OPEN)) {
            totalPower = SettingPreferencesUtil.getPowerLimitLevel(context);
        }
        mInductionCookerHardwareManager = InductionCookerHardwareManager
                .init(this)
                .setSerailPortParameter()
                .setCookerType(CookerConstant.COOKER_TYPE_CURRENT_TYPE)//电磁炉类型KSO_60,KSO_80,KSO_90,KSO_AS
                .setTotalPower(totalPower)
                .setCookerHardwareManagerListener(this)
                .build();

    }

    public void bindActivity(Activity activity) {
        Logger.getInstance().i("bindActivity(" + activity.getLocalClassName() + ")");
        context = activity;
    }

    public void bindCooker(int cookerID) {
        mCookerHelper.bindCooker(cookerID,getApplicationContext());
    }

    public void notifyCookerUpdateSettingData(CookerSettingData data) {
        mCookerHelper.notifyUpdateCookerSettingData(data);
    }

    public void notifyCookerPowerOff(int cookerID) {
        mCookerHelper.notifyCookerPowerOff(cookerID);
    }

    public void notifyCookerReadyToCook(int cookerID) {
        mCookerHelper.notifyCookerReadyToCook(cookerID);
    }

    public void notifyCookerKeepWarm(int cookerID) {
        mCookerHelper.notifyCookerKeepWarm(cookerID);
    }

    public void notifyCookerAddTenMinute(int cookerID) {
        mCookerHelper.notifyCookerAddTenMinute(cookerID);
    }

    public void notifyCookerPause() {
        notifyCookerPause(-1);
    }

    public void notifyCookerPause(int cookerId) {
        mCookerHelper.notifyCookerPause(cookerId);
    }

    private void notifyCookerAgeingPowerOff() {
        LogUtil.d("notifyCookerAgeingPowerOff");
    }

    public void notifyCookerPlay() {
        notifyCookerPlay(-1);
    }

    public void notifyCookerPlay(int cookerId) {
        mCookerHelper.notifyCookerPlay(cookerId);
    }
    private void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    /*public void notifyStopReceiveError(int cookerID) {
        //mCookerHelper.

    }*/

    private void unregisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this);
    }

    private void startAgeingCountDown() {
        stopAgeingCountDown();
        ageingDisposable = Observable
                //.interval(0,1, TimeUnit.SECONDS)
                .interval(1,1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {

                        ageingCountdownTime--;
                        if (ageingCountdownTime == 0) {
                            stopAgeingCountDown();
                            notifyCookerAgeingPowerOff();
                            ageingCountdownTime = TFTHobConfiguration.HOB_AGEING_TIME;
                        }

                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribe();
    }

    private void reStartAgeingCountDown() {
        ageingCountdownTime = TFTHobConfiguration.HOB_AGEING_TIME;
        startAgeingCountDown();
    }

    private void stopAgeingCountDown() {
        if (ageingDisposable != null) {
            if (!ageingDisposable.isDisposed()) {
                ageingDisposable.dispose();
            }
        }
    }

    private void endAgeing() {
        stopAgeingCountDown();
        ageingCountdownTime = TFTHobConfiguration.HOB_AGEING_TIME;
    }

    private void ageingPlay() {
        startAgeingCountDown();
    }

    private void ageingPause() {
        stopAgeingCountDown();
    }

    /*public void doSleep() {
        releaseWakeLock();
        //mBluetoothManager.setBTWorkMode(BluetoothManager.BLUETOOTH_OPTIONS_DISABLE);
    }

    public void doWakeUp() {
        requestWakeLock();
        //mBluetoothManager.setBTWorkMode(BluetoothManager.BLUETOOTH_OPTIONS_ENABLE);
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterEventBus();
        releaseWakeLock();
        mInductionCookerHardwareManager.recyle();
        mCataBluetoothManager.recyle();
        unregisterReceiver(mScreenStatusReceiver);

        if(mEKEKSocketClient != null) {
            mEKEKSocketClient.disconnect();
            mEKEKSocketClient = null;
        }

    }

    class ScreenStatusReceiver extends BroadcastReceiver {
        String SCREEN_ON = "android.intent.action.SCREEN_ON";
        String SCREEN_OFF = "android.intent.action.SCREEN_OFF";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (SCREEN_ON.equals(intent.getAction())) {  // 屏幕开
                requestWakeLock();
                LogUtil.d("liang 屏幕开");
            }
            else if (SCREEN_OFF.equals(intent.getAction())) {  // 屏幕关
                LogUtil.d("liang 屏幕关1");
                releaseWakeLock();
                LogUtil.d("liang 屏幕关2");

            }
        }

    }

    /* private void registSreenStatusReceiver() {
        mScreenStatusReceiver = new ScreenStatusReceiver();
        IntentFilter screenStatusIF = new IntentFilter();
        screenStatusIF.addAction(Intent.ACTION_SCREEN_ON);
        screenStatusIF.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mScreenStatusReceiver, screenStatusIF);
    }

    private void releaseWakeLock() {
        if (wakeLock != null) {
            wakeLock.release();
        }
    }

    public void doSleep() {
        releaseWakeLock();
        mBluetoothManager.setBTWorkMode(BluetoothManager.BLUETOOTH_OPTIONS_DISABLE);
    }

    public void doWakeUp() {
        requestWakeLock();
        mBluetoothManager.setBTWorkMode(BluetoothManager.BLUETOOTH_OPTIONS_ENABLE);
    }*/


    private void startCountdown() {
        doStopCountDown();
        mDisposable = Observable
                //.interval(0,1, TimeUnit.SECONDS)
                .interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        //  updateRemainTime();
                        // handler.sendEmptyMessage(0);
                        handleTurboStatus();

                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribe();
    }

    private void doStopCountDown() {
        if (mDisposable != null) {
            if (!mDisposable.isDisposed()) mDisposable.dispose();
        }

    }




    private void handleTurboStatus(){
        boolean isFanWorkingAutoStatus = SettingPreferencesUtil.getFanWorkingStatus(this).equals(CataSettingConstant.FAN_WORKING_STATUS_AUTO) ? true : false;
        if(isFanWorkingAutoStatus){  // 自动状态
            if( GlobalVars.getInstance().getHoodLevel()==6&&!mTimerWorkingFlag){  // turbo 档
                mSetTurboToNextPowerNum++;
                if(mSetTurboToNextPowerNum>=300){  // 5分钟  300
                    mSetTurboToNextPowerFlag=true;
                    mSetTurboToNextPowerNum=0;
                    // LogUtil.d("liang set turbo 1");
                    GlobalVars.getInstance().setSetTurboToNextPower(true);
                }
            }else {
                mSetTurboToNextPowerFlag=false;
                mSetTurboToNextPowerNum=0;
                // LogUtil.d("liang set turbo 2");
                GlobalVars.getInstance().setSetTurboToNextPower(false);
            }
        }else {  // 手动状态

            if(mhoodLevel==6&&!mTimerWorkingFlag){
                mSetTurboToNextPowerNum++;
                if(mSetTurboToNextPowerNum>=300){  // 5分钟  300
                    mSetTurboToNextPowerFlag=true;
                    mSetTurboToNextPowerNum=0;
                  //   LogUtil.d("liang set turbo 1");
                    GlobalVars.getInstance().setSetTurboToNextPower(true);
                }
            }else {
                mSetTurboToNextPowerFlag=false;
                mSetTurboToNextPowerNum=0;
              //   LogUtil.d("liang set turbo 2");
                GlobalVars.getInstance().setSetTurboToNextPower(false);
            }

        }



    }

}
