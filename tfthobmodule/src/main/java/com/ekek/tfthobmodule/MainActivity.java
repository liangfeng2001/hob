package com.ekek.tfthobmodule;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalCons;
import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseActivity;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.commonmodule.base.BaseThread;
import com.ekek.commonmodule.utils.DateTimeUtil;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.commonmodule.utils.Logger;
import com.ekek.hardwaremodule.entity.CookerHardwareResponse;
import com.ekek.hardwaremodule.entity.CookerMessage;
import com.ekek.hardwaremodule.event.CookerHardwareEvent;
import com.ekek.hardwaremodule.event.PowerSwitchStateChangedEvent;
import com.ekek.hardwaremodule.utils.DataParseUtil;
import com.ekek.intromodule.hob.view.HobIntroFragment;
import com.ekek.intromodule.splash.view.SplashFragment;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.database.SecurityDBUtil;
import com.ekek.settingmodule.database.SettingPreferencesUtil;
import com.ekek.settingmodule.dialog.ConfirmDialog;
import com.ekek.settingmodule.entity.SecurityTable;
import com.ekek.settingmodule.events.DateOrTimeSetEvent;
import com.ekek.settingmodule.events.SendOrderTo;
import com.ekek.settingmodule.events.SetLanguage;
import com.ekek.settingmodule.events.SettingFragmentHiddenEvent;
import com.ekek.settingmodule.events.SettingsChangedEvent;
import com.ekek.settingmodule.model.AlarmClockIsEditedEvent;
import com.ekek.settingmodule.model.PlaySoundWhenSetTime;
import com.ekek.settingmodule.model.SendOrderForFirstSwitchOn;
import com.ekek.settingmodule.model.SendOrderRecoverHoodConnectivity;
import com.ekek.settingmodule.model.SettingDoBack;
import com.ekek.settingmodule.model.SettingTestCookDataEvent;
import com.ekek.settingmodule.model.ShowBannerInformation;
import com.ekek.settingmodule.view.AlarmClockSettingFragment;
import com.ekek.settingmodule.view.SettingFragment;
import com.ekek.tfthobmodule.base.BaseHobFragment;
import com.ekek.tfthobmodule.constants.TFTHobConfiguration;
import com.ekek.tfthobmodule.constants.TFTHobConstant;
import com.ekek.tfthobmodule.data.CookerSettingData;
import com.ekek.tfthobmodule.database.DatabaseHelper;
import com.ekek.tfthobmodule.entity.AlarmClockSetting;
import com.ekek.tfthobmodule.entity.AlarmClockSettingDao;
import com.ekek.tfthobmodule.entity.HoodData;
import com.ekek.tfthobmodule.entity.MyRecipesTable;
import com.ekek.tfthobmodule.event.BleBatteryEvent;
import com.ekek.tfthobmodule.event.BleTempEvent;
import com.ekek.tfthobmodule.event.BluetoothEevent;
import com.ekek.tfthobmodule.event.CloseDialogEvent;
import com.ekek.tfthobmodule.event.CookerPanelHighTempEvent;
import com.ekek.tfthobmodule.event.CookerUpdateEvent;
import com.ekek.tfthobmodule.event.DebugInfoEvent;
import com.ekek.tfthobmodule.event.DrawerEvent;
import com.ekek.tfthobmodule.event.HideErrorEvent;
import com.ekek.tfthobmodule.event.IdleEvent;
import com.ekek.tfthobmodule.event.NTCEvent;
import com.ekek.tfthobmodule.event.NoExternalSensorDetectedEvent;
import com.ekek.tfthobmodule.event.PlaySoundEvent;
import com.ekek.tfthobmodule.event.ScreenEvent;
import com.ekek.tfthobmodule.event.ShowErrorEvent;
import com.ekek.tfthobmodule.event.SoundEvent;
import com.ekek.tfthobmodule.event.TempSensorRequestEvent;
import com.ekek.tfthobmodule.event.TipsEvent;
import com.ekek.tfthobmodule.service.TFTHobService;
import com.ekek.tfthobmodule.utils.SoundUtil;
import com.ekek.tfthobmodule.view.Hob60PanelFragment;
import com.ekek.tfthobmodule.view.Hob80PanelFragment;
import com.ekek.tfthobmodule.view.Hob90PanelFragment;
import com.ekek.tfthobmodule.view.HobCookerSettingFragment;
import com.ekek.tfthobmodule.view.HobCookingTablesDetailFragment;
import com.ekek.tfthobmodule.view.HobCookingTablesFragment;
import com.ekek.tfthobmodule.view.HobMyRecipesEditFragment;
import com.ekek.tfthobmodule.view.HobMyRecipesFragment;
import com.ekek.tfthobmodule.view.HobPanelFragment;
import com.ekek.tfthobmodule.view.HobRecipesEditFragment;
import com.ekek.tfthobmodule.view.HobRecipesFoodDetailFragment;
import com.ekek.tfthobmodule.view.HobRecipesFragment;
import com.ekek.tfthobmodule.view.HobTemperatureModeSelectFragment;
import com.ekek.tfthobmodule.view.SystemUpgradeFragment;
import com.ekek.tfthobmodule.view.TempCtrlFragment;
import com.ekek.tfthobmodule.view.TestViewFragment;
import com.ekek.tfthoodmodule.constants.TFTHoodConstant;
import com.ekek.tfthoodmodule.model.OrderFromHoodPanel;
import com.ekek.tfthoodmodule.model.PowerOffEven;
import com.ekek.tfthoodmodule.view.HoodPanelFragment;
import com.ekek.tfthoodmodule.view.HoodPanelScheduleFragment;
import com.ekek.viewmodule.bar.HobMenuBar;
import com.ekek.viewmodule.bar.HoodMenuBar;
import com.ekek.viewmodule.common.BatteryIndicatorView;
import com.ekek.viewmodule.common.CataAlertDialog;
import com.ekek.viewmodule.common.CataDialog;
import com.ekek.viewmodule.common.CataDrawLayout;
import com.ekek.viewmodule.events.BluetoothEvent;
import com.ekek.viewmodule.events.CountDownEvent;
import com.ekek.viewmodule.listener.OnMenuBarSelectListener;
import com.ekek.viewmodule.listener.OnPasswordListener;
import com.ekek.viewmodule.listener.PatternLockViewListener;
import com.ekek.viewmodule.passwordview.PatternLockView;
import com.ekek.viewmodule.passwordview.PinPasswordView;
import com.ekek.viewmodule.product.ProductManager;
import com.ekek.viewmodule.time.StatusBarClockTimerView;
import com.ekek.viewmodule.utils.PatternLockUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

import static com.ekek.tfthobmodule.view.Hob90PanelFragment.OnHob90PanelFragmentListener;

public class MainActivity extends BaseActivity
        implements HobTemperatureModeSelectFragment.OnHobTemperatureModeSelectFragmentListener,
        TempCtrlFragment.OnTempCtrlFragmentListener,
        ConfirmDialog.OnConfirmedListener,
        OnHob90PanelFragmentListener,
        HobMyRecipesEditFragment.OnHobMyRecipesEditFragmentListener,
        HobRecipesEditFragment.OnHobRecipesEditFragmentListener,
        HobMyRecipesFragment.OnHobMyRecipesFragmentListener,
        HobRecipesFoodDetailFragment.OnHobRecipesFoodDetailFragmentListener,
        Hob80PanelFragment.OnHob80PanelFragmentListener,
        Hob60PanelFragment.OnHob60PanelFragmentListener,
        SettingFragment.OnSettingFragmentListener,
        HobCookingTablesDetailFragment.OnHobCookingTablesDetailFragmentListener,
        AlarmClockSettingFragment.OnTimerSettingFragmentListener,
        StatusBarClockTimerView.OnStatusBarClockTimerListener,
        HobCookingTablesFragment.OnHobCookingTablesFragmentListener,
        HobRecipesFragment.OnHobRecipesFragmentListener,
        OnMenuBarSelectListener,
        SplashFragment.OnSplashFragmentListener,
        HobIntroFragment.OnHobIntroFragmentListener,
        HobCookerSettingFragment.OnHobCookerSettingFragmentListener,
        HobPanelFragment.OnHobPanelFragmentListener,
        HoodPanelFragment.OnHoodPanelFragmentListener {
    private static final int HANDLER_COOKER_CONTINUE_WORK_FOR_HOOD = 100;
    private static final int HANDLER_COOKER_CONTINUE_WORK_FOR_HOB = 101;
    private static final int HANDLER_COOKER_UNLOCK = 102;
    private static final int HANDLER_UPDATE_NTC_TEMP_UI = 104;
    private static final int HANDLER_UPDATE_COOKER_PANEL_HIGH_TEMP = 105;
    private static final int HANDLER_UPDATE_BT_BATTERY_LEVEL_UI = 106;
    private static final int HANDLER_UPDATE_BT_BATTERY_UI = 107;
    private static final int HANDLER_CLOSE_FAN_LIGHT_HOOD = 108;
    private static final int HANDLER_SERIAL_DATA_EXCEPTION = 109;
    private static final int HANDLER_BLINK_TIMER_IS_UP_DIALOG = 110;
    private static final int HANDLER_CLOSE_TIMER_WHEN_POWER_OFF = 111;
    private static final int HANDLER_SWITCH_OFF_TFT_COMPLETELY_AFTER_PANEL_HIGH_TEMP_HAPPAN_ONE_MINUTE = 112;  //屏幕高温一分钟后，关掉警报声以及动画并进入动画--然后黑屏等待关机
    private static final int HANDLER_UPDATE_BT_BATTERY_CHARGE_STATE = 113;
    private static final int HANDLER_UPDATE_TIME_AFTER_UPDATA_TIME = 114;

    private static final long TOAST_SHOW_DURATION_3_SECOND = 3 * 1000;
    private static final String ACTION_SET_USB_DEBUG = "ACTION_EKEK_SET_USB_DEBUG";

    private static final int INTRO_STATUS_NONE = 0;
    private static final int INTRO_STATUS_HIBERNATE = 1;
    private static final int INTRO_STATUS_TOTAL_OFF = 2;

    private static float alarmClockBackgroundStartX = 357.0f;
    private static float alarmClockBackgroundEndX = 788.0f;
    private static float alarmClockBackgroundStartY = 314.0f;
    private static float alarmClockBackgroundEndY = 416.0f;

    @BindView(R.id.ib_up_info)
    ImageButton ibUpInfo;
    @BindView(R.id.ib_hood)
    ImageButton ibHood;
    @BindView(R.id.drawer_layout)
    CataDrawLayout drawerLayout;
    @BindView(R.id.hob_menu_bar)
    HobMenuBar hobMenuBar;
    @BindView(R.id.hood_menu_bar)
    HoodMenuBar hoodMenuBar;
    @BindView(R.id.ib_menu_handle)
    ImageButton ibMenuHandle;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.sbctv)
    StatusBarClockTimerView sbctv;
    @BindView(R.id.tv_up_toast)
    TextView tvUpToast;
    @BindView(R.id.tv_temp)
    TextView tvTemp;
    @BindView(R.id.tv_ntc_temp)
    TextView tvNtcTemp;
    @BindView(R.id.biv_bluetooth)
    BatteryIndicatorView bivBluetooth;
    @BindView(R.id.tv_debug_info)
    TextView tvDebugInfo;
    @BindView(R.id.tv_debug_info_bottom)
    TextView tvDebugInfoBottom;
    @BindView(R.id.right_back)
    ImageView rightBack;
    private Fragment currentFragment;
    // private HobPanelFragment mHobPanelFragment;
    private HobCookerSettingFragment mHobCookerSettingFragment;
    private HobTemperatureModeSelectFragment mHobTemperatureModeSelectFragment;
    private HobRecipesFragment mHobRecipesFragment;
    private HoodPanelFragment mHoodPanelFragment;
    private HobIntroFragment mHobIntroFragment;
    private SplashFragment mSplashFragment;
    private HobCookingTablesFragment mHobCookingTablesFragment;
    private HobCookingTablesDetailFragment mHobCookingTablesDetailFragment;
    private AlarmClockSettingFragment mAlarmClockSettingFragment;
    private BaseHobFragment mTFTHobPanelFragment;
    private SettingFragment mSettingFragment;
    private TestViewFragment mTestViewFragment;
    private HobRecipesFoodDetailFragment mHobRecipesFoodDetailFragment;
    private HobMyRecipesFragment mHobMyRecipesFragment;
    private HobMyRecipesEditFragment mHobMyRecipesEditFragment;
    private HobRecipesEditFragment mHobRecipesEditFragment;
    private HoodPanelScheduleFragment mHoodPanelScheduleFragment;
    private TempCtrlFragment mTempCtrlFragment;
    private SystemUpgradeFragment mSystemUpgradeFragment;
    private CataDialog timerIsUpDialog;
    private ImageView timerIsUpDialogContentView;
    private TextView timerIsUpDialogAddTen;
    private TextView timerIsUpDialogClose;
    private CataDialog lockDialog, pauseDialog, unLockDialog, pannelHighTempWarningDialog;
    private ConfirmDialog errorDialog;
    private TFTHobService mTFTHobService;
    private Intent serviceIntent;
    //private static final int RED_LIGHT = 0xffFF0000;
    //private static final int RED_DARK = 0xffaa0000;
    private static final int RED_LIGHT = 0x5fBE1622;
    private static final int RED_DARK = 0xffBE1622;

    private ValueAnimator colorAnim;
    private List<Integer> cookerIDs = new ArrayList<>();
    private Disposable mDisposable;//定时器
    private int HighTempAnim = 0; // 默认是关闭高温动画

    private long longClickStart = 0;
    private boolean showingErrER03FirstTime = true;
    private static final long LONG_CLICK_DURATION_ER03 = 10 * 1000;
    private static final int movingLength = 5;
    private boolean touchInsideCircle = false;
    private float startX = 0.0f;
    private float startY = 0.0f;

    private int errorId = -1;
    private int errorCookerId = -1;
    private String errorTitle = "";

    private CheckingThread checkingThread = null;
    private WaitCoolDownThread waitCoolDownThread = null;
    private int activationDuration;
    private int totalTurnOffDuration;

    private int mFanValueFromMainPanelB30 = 0;
    private int mLightValueFromMainPanelB30 = 0;

    private CataAlertDialog mCataAlertDialog;
    private boolean mTimerOfHoodIsWorking = false;
    private boolean mAlarmIsEdited = false;

    private long mTouchTime = SystemClock.elapsedRealtime();

    private ConfirmDialog defaultSettingsSuccessfullyDialog;
    private ConfirmDialog hoodConnectivityDialog;
    private ConfirmDialog deleteMyRecipeDialog;
    private ConfirmDialog externalSensorNotDetectedDialog;

    private long lastTimeGotSerialData = SystemClock.elapsedRealtime();

    private boolean hasCookerObnormalHighTemp = false;

    private PowerManager.WakeLock wakeLock;
    private int mSettingStatus=CataSettingConstant.SetLanguage; // 设置语言阶段。

    private int mCountdown5Minute = 0;

    private int batteryLevel;
    private int maxUsableMinutes;
    private boolean r2eErrorHandled;
    private int r2eErrorCookerId = -1;
    private boolean r3eErrorHandled;
    private boolean r5eErrorHandled;
    private boolean r6eErrorHandled;
    private R6eErrorAssistThread r6eErrorAssistThread;

    private int ptcCookerID = -1; // PTC: Precise Temperature control
    private int ptcTargetTempValue;
    private int lastPtcCookerID = -1;
    private long ptcCookerStartTime;
    private int ptcCookerStartTemp;

    private Map<Integer, Bitmap> bitmapMap = new HashMap<>();

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder localBinder) {
            TFTHobService.LocalBinder mLocalBinder = (TFTHobService.LocalBinder) localBinder;
            mTFTHobService = mLocalBinder.getService();
            mTFTHobService.bindActivity(MainActivity.this);
            //initStartUpData();
            if (cookerIDs.size() > 0) {
                for (Integer cookerID : cookerIDs) {
                    mTFTHobService.bindCooker(cookerID);
                }
                cookerIDs.clear();

            }

        }

        public void onServiceDisconnected(ComponentName arg0) {
            mTFTHobService = null;

        }
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                //mTFTHobService.recoverConnectForBT();
                LogUtil.d("screen on");
                // mTFTHobService.doWakeUp();
                requestWakeLock();
                EventBus.getDefault().post(new ScreenEvent(ScreenEvent.ACTION_SCREEN_ON));


            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                //mTFTHobService.doDisconnectForBT();
                LogUtil.e("screen ff");
                //  mTFTHobService.doSleep();
                releaseWakeLock();
                disableBT();

            }
        }
    };

    private Field findField(@NonNull Object obj, @NonNull String fieldName) {
        return doFindField(obj.getClass(), fieldName);
    }
    private Field doFindField(@NonNull Class<?> aClass, String fieldName) {
        Field f;
        try {
            f = aClass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            f = null;
        }
        if (f != null) {
            return f;
        }

        Class<?> superclass = aClass.getSuperclass();
        if (superclass == null) {
            return null;
        }

        return doFindField(superclass, fieldName);
    }

    private void setDrawerLeftEdgeSize(DrawerLayout drawerLayout, int newEdgeSize) {
        if (drawerLayout == null) return;
        try {
            // 找到 ViewDragHelper 并设置 Accessible 为true
            Field leftDraggerField = findField(drawerLayout, "mLeftDragger");
            if (leftDraggerField == null) {
                return;
            }

            leftDraggerField.setAccessible(true);
            ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField.get(drawerLayout);

            // 找到 edgeSizeField 并设置 Accessible 为true
            Field edgeSizeField = leftDragger.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(leftDragger);

            Logger.getInstance().d("ORIGINAL LEFT EDGE SIZE = " + edgeSize);
            Logger.getInstance().d("NEW LEFT EDGE SIZE = " + newEdgeSize);

            edgeSizeField.setInt(leftDragger, newEdgeSize);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            Logger.getInstance().e(e);
        }
    }

    private void setDrawerRightEdgeSize(DrawerLayout drawerLayout, int newEdgeSize) {
        if (drawerLayout == null) return;
        try {
            // 找到 ViewDragHelper 并设置 Accessible 为true
            Field rightDraggerField = findField(drawerLayout, "mRightDragger");
            if (rightDraggerField == null) {
                return;
            }

            rightDraggerField.setAccessible(true);
            ViewDragHelper rightDragger = (ViewDragHelper) rightDraggerField.get(drawerLayout);

            // 找到 edgeSizeField 并设置 Accessible 为true
            Field edgeSizeField = rightDragger.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(rightDragger);
            Logger.getInstance().d("ORIGINAL RIGHT EDGE SIZE = " + edgeSize);
            Logger.getInstance().d("NEW RIGHT EDGE SIZE = " + newEdgeSize);
            edgeSizeField.setInt(rightDragger, newEdgeSize);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            Logger.getInstance().e(e);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_COOKER_CONTINUE_WORK_FOR_HOOD:
                    if (pauseDialog != null && pauseDialog.isShowing()) {
                        pauseDialog.dismiss();
                        hideBottomUIMenu();
                        doPlayForHood();
                    }
                    break;
                case HANDLER_COOKER_CONTINUE_WORK_FOR_HOB:
                    resumeHob();
                    break;


                case HANDLER_COOKER_UNLOCK:
                    doUnlock(true);
                    break;
                case HANDLER_UPDATE_BT_BATTERY_LEVEL_UI:
                    if (TFTHobConfiguration.TFT_HOB_TYPE == TFTHobConfiguration.TFT_HOB_TYPE_60) {
                        if (currentFragment == mTFTHobPanelFragment && bivBluetooth.getVisibility() == View.INVISIBLE)
                            bivBluetooth.setVisibility(View.VISIBLE);
                        bivBluetooth.updateView(msg.arg1, msg.arg2);
                    }


                    break;
                case HANDLER_UPDATE_BT_BATTERY_UI:
                    if (msg.arg1 == BleBatteryEvent.BATTERY_VIEW_STATUS_HIDE) {
                        if (mCataAlertDialog != null && mCataAlertDialog.isShowing())
                            mCataAlertDialog.dismiss();
                        if (msg.arg2 == com.ekek.tfthobmodule.bluetooth.BluetoothManager.BLUETOOTH_TEMP_SENSOR_STOP_TYPE_PASSIVE) {
                            EventBus.getDefault().post(new SoundEvent(
                                    SoundEvent.SOUND_ACTION_PLAY,
                                    SoundUtil.SOUND_ID_ALARM,
                                    SoundEvent.SOUND_TYPE_COMMON_ALARM));
                        /*mCataAlertDialog = new CataAlertDialog(MainActivity.this, CataAlertDialog.CATA_ALERT_DIALOG_STYLE_ERROR, getString(R.string.settingmodule_no_external_sensor_detected));
                        mCataAlertDialog.show();*/
                            showExternalSensorNotDetectedDialog(NoExternalSensorDetectedEvent.SOURCE_HOB_TEMPERATURE_SENSOR_DISCONNECT);
                        }

                        if (TFTHobConfiguration.TFT_HOB_TYPE == TFTHobConfiguration.TFT_HOB_TYPE_60) {
                            bivBluetooth.setVisibility(View.INVISIBLE);
                        }

                    } else if (msg.arg1 == BleBatteryEvent.BATTERY_VIEW_STATUS_SHOW) {
                        if (mCataAlertDialog != null && mCataAlertDialog.isShowing())
                            mCataAlertDialog.dismiss();
                        mCataAlertDialog = new CataAlertDialog(
                                MainActivity.this,
                                CataAlertDialog.CATA_ALERT_DIALOG_STYLE_SENSOR_CONNECT_SUCCESS,
                                getString(R.string.tfthobmodule_dialog_message_sensor_connect_successfully));
                        mCataAlertDialog.show();
                        if (currentFragment == mTFTHobPanelFragment && TFTHobConfiguration.TFT_HOB_TYPE == TFTHobConfiguration.TFT_HOB_TYPE_60)
                            bivBluetooth.setVisibility(View.VISIBLE);
                    }
                    break;
                case HANDLER_UPDATE_NTC_TEMP_UI:
                    NTCEvent event = (NTCEvent) msg.obj;
                    //  tvNtcTemp.setText(event.getMessage()); // 暂时屏幕，在界面上会显示 NTC 温度。
                    break;
                case HANDLER_UPDATE_COOKER_PANEL_HIGH_TEMP:
                    if (currentFragment == mSplashFragment) {
                        // 在播放动画，延时处理此消息
                        Message newMsg = new Message();
                        newMsg.copyFrom(msg);
                        handler.sendMessageDelayed(newMsg, 500);
                        return;
                    } else if (currentFragment == mHobIntroFragment) {
                        if (mHobIntroFragment.isShowingBlackMask()) {
                            // 即将进入休眠模式
                            return;
                        }
                    }
                    boolean isHighTemp = (boolean) msg.obj;
                    if (isHighTemp) {
                        clearTimer();
                        setAnim();
                        //屏幕高温主动断开蓝牙温度计
                        EventBus.getDefault().post(new BluetoothEevent());
                        if (!handler.hasMessages(HANDLER_SWITCH_OFF_TFT_COMPLETELY_AFTER_PANEL_HIGH_TEMP_HAPPAN_ONE_MINUTE)) {
                            handler.sendEmptyMessageDelayed(HANDLER_SWITCH_OFF_TFT_COMPLETELY_AFTER_PANEL_HIGH_TEMP_HAPPAN_ONE_MINUTE,1000 * 60);//1分钟结束报警并准备进入低功耗

                        }
                        //  LogUtil.d("show the high temp");
                    } else {
                        cancleAnim();
                        //屏幕高温结束后 ，start blutooth search
                        mTFTHobService.recoverConnectForBT();
                        if (handler.hasMessages(HANDLER_SWITCH_OFF_TFT_COMPLETELY_AFTER_PANEL_HIGH_TEMP_HAPPAN_ONE_MINUTE)) {
                            handler.removeMessages(HANDLER_SWITCH_OFF_TFT_COMPLETELY_AFTER_PANEL_HIGH_TEMP_HAPPAN_ONE_MINUTE);

                        }
                        //   LogUtil.d("close the high temp");
                    }


                    break;
                case HANDLER_SWITCH_OFF_TFT_COMPLETELY_AFTER_PANEL_HIGH_TEMP_HAPPAN_ONE_MINUTE:
                    cancleAnim();
                    EventBus.getDefault().post(new IdleEvent(IdleEvent.IDLE_SWITCH_OFF_TFT_COMPLETE_FOR_PANNEL_HIGH_TEMP));
                    break;
                case HANDLER_CLOSE_FAN_LIGHT_HOOD:
                    EventBus.getDefault().post(new PowerOffEven(TFTHoodConstant.HOOD_CLOSE_FAN_LIGHT)); // 临时，关机命令
                    break;

                case HANDLER_SERIAL_DATA_EXCEPTION:
                    lastTimeGotSerialData = SystemClock.elapsedRealtime();
                    //Toast.makeText(MainActivity.this, "Something is wrong with the serial port.\nCannot receive the data.", Toast.LENGTH_SHORT).show();
                    break;
                case HANDLER_BLINK_TIMER_IS_UP_DIALOG:
                    if (timerIsUpDialog != null) {
                        float alpha = msg.arg1 / 255.0f;
                        timerIsUpDialogContentView.setAlpha(alpha);
                        timerIsUpDialogAddTen.setAlpha(alpha);
                        timerIsUpDialogClose.setAlpha(alpha);
                    }
                    break;
                case HANDLER_CLOSE_TIMER_WHEN_POWER_OFF:
                    if(mHobIntroFragment.isHidden()){    // 不在时钟界面

                    }else {
                        closeScheduleFragment();
                        DatabaseHelper.saveHoodData(0, 0);  // 同时 风机 灯光档位都是 零 2019年10月21日19:30:00
                    }

                    break;
                case HANDLER_UPDATE_TIME_AFTER_UPDATA_TIME:
                    hobMenuBar.setTimeElapsedRealtime();  // 时间设置后，刷新的时间长度不准确，以导致不能点击侧护栏上的按键。
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        CataTFTHobApplication.getInstance().addActivity(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);

        sendBroadcast(new Intent(ACTION_SET_USB_DEBUG));

        // setAnim();

        registerBroadcast();
        requestWakeLock();


    }


    private boolean resumeHob() {
        if (pauseDialog != null && pauseDialog.isShowing()) {
            pauseDialog.dismiss();
            hideBottomUIMenu();
            doPlayForHob();
            GlobalVars.getInstance().setShowingPauseHobDialog(false);
            return true;
        }
        return false;
    }


    private void requestWakeLock() {
        if (wakeLock == null) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CataTFTHobApplication:GTermWakelock");
            wakeLock.acquire();
        }

    }

    private void releaseWakeLock() {
        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }
    }

    private void disableBT() {
      //  mTFTHobService.doDisconnectForBT();
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
            if (bluetoothAdapter.isEnabled()) {
                bluetoothManager.getAdapter().disable();
            }
            //bluetoothManager.getAdapter().disable();
        }

    }

    private void enableBT() {
        //mTFTHobService.recoverConnectForBT();
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.enable();
            }
        }

    }

    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(receiver, filter);
    }

    private void unregisterBroadcast() {

        unregisterReceiver(receiver);

    }

    @Override
    public int initLyaout() {
        return R.layout.tfthobmodule_activity_main;
    }

    private ViewDragHelper mdDragHelper;

    @Override
    public void initView() {
        serviceIntent = new Intent(this, TFTHobService.class);
        startService(serviceIntent);
        boolean result = bindService(serviceIntent, mConnection, BIND_AUTO_CREATE);

        drawerLayout.setScrimColor(Color.TRANSPARENT);
        //drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                EventBus.getDefault().post(new DrawerEvent(
                        DrawerEvent.EVENT_DRAWER_SLIDE,
                        slideOffset));
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                EventBus.getDefault().post(new DrawerEvent(
                        DrawerEvent.EVENT_DRAWER_OPENED));
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                EventBus.getDefault().post(new DrawerEvent(
                        DrawerEvent.EVENT_DRAWER_CLOSED));
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                EventBus.getDefault().post(new DrawerEvent(
                        DrawerEvent.EVENT_DRAWER_STATE_CHANGED,
                        newState));
            }
        });

        setDrawerLeftEdgeSize(drawerLayout, 160);
        setDrawerRightEdgeSize(drawerLayout, 160);
    /*    drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                LogUtil.d("Enter:: onDrawerSlide------>" + slideOffset);
                View mContent = drawerLayout.getChildAt(0);
                View mMenu = drawerView;
                float scale = 1 - slideOffset;
                //改变DrawLayout侧栏透明度，若不需要效果可以不设置
                ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
                ViewHelper.setTranslationX(mContent,
                        -mMenu.getMeasuredWidth() * (1 - scale));
                ViewHelper.setPivotX(mContent, 0);
                ViewHelper.setPivotY(mContent, mContent.getMeasuredHeight() / 2);
                mContent.invalidate();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                LogUtil.d("Enter:: onDrawerOpened");
                //if (ibMenuHandle.getVisibility() == View.VISIBLE) ibMenuHandle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                LogUtil.d("Enter:: onDrawerClosed");
                //if (ibMenuHandle.getVisibility() == View.INVISIBLE) ibMenuHandle.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                LogUtil.d("Enter:: onDrawerStateChanged---->" + newState);
            }
        });*/


        initMenuBar();
        //showHobPanelFragment(null);

        //init battery view position
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if (TFTHobConfiguration.TFT_HOB_TYPE == TFTHobConfiguration.TFT_HOB_TYPE_60) {
            lp.gravity = Gravity.LEFT | Gravity.BOTTOM;
            bivBluetooth.setLayoutParams(lp);

        } else if (TFTHobConfiguration.TFT_HOB_TYPE == TFTHobConfiguration.TFT_HOB_TYPE_80) {
            lp.gravity = Gravity.BOTTOM | Gravity.CENTER;
            lp.setMargins(0, 0, 150, 0);
            bivBluetooth.setLayoutParams(lp);

        } else if (TFTHobConfiguration.TFT_HOB_TYPE == TFTHobConfiguration.TFT_HOB_TYPE_90) {
            lp.gravity = Gravity.BOTTOM | Gravity.CENTER;
            lp.setMargins(0, 0, 200, 0);
            bivBluetooth.setLayoutParams(lp);
        }


        int language = SettingPreferencesUtil.getDefaultLanguage2(this);
        if (language == CataSettingConstant.LANGUAGE_UNKNOWN) {
            GlobalVars.getInstance().setAppStartTag(GlobalCons.APP_START_FIRST_TIME);
            SettingPreferencesUtil.saveDefaultLanguage2(this, CataSettingConstant.DEFAULT_LANGUAGE_2);
        } else {
            // 有保存过，说明不是第一次开机
            GlobalVars.getInstance().setAppStartTag(GlobalCons.APP_START);
            mSettingStatus=CataSettingConstant.SetDate;
        }

        // 判断是否要播放动画的声音
        ifPlayTheSplashSound();
        showSplashFragment(SplashFragment.SPLASH_MODE_INTRO);

        //showHobCookingTablesFragment();
        //showHobCookingTablesDetailFragment();
        sbctv.setOnStatusBarClockTimerListener(this);
        sbctv.setTimeFormat24(SettingPreferencesUtil.getTimeFormat24(this));
        sbctv.setVisibility(View.INVISIBLE);// 上电后，在语言设置界面、日期设置界面、时钟设置界面不可见。
        //showTimerSettingFragment();

        //showHobPanel80Fragment();
        //showHobCookingTablesDetailFragment(HobCookingTablesDetailFragment.COOKING_TABLES_DETAIL_TYPE_MEAT);

        //test();
        //showSettingFragment();
        //LogUtil.d("date----->" + formatDateStampString());

        //  showTFTHobPanelFragment(Hob90PanelFragment.HOB_PANEL_WORK_MODE_NORMAL);  // 屏蔽 2019年1月19日11:27:36
        //  showNormalDialog();
        //showTestViewFragment();

        //showHobMyRecipesFragment();
        //showHobRecipesEditFragment(HobRecipesEditFragment.RECIPES_EDIT_TYPE_NAME);
        //showHobMyRecipesEditFragment(-1,"");


 /*       Intent mIntent=new Intent();
        mIntent.setClassName("com.ekek.tfthobmodule", "com.ekek.tfthobmodule.MainActivity");
        startActivity(mIntent);*/

        //startCountdown();
        initTimerOpenStatusOfHood();
        initClock();
        GlobalVars.getInstance().setHobType(TFTHobConfiguration.TFT_HOB_TYPE);  // 保存 当前电磁炉的类型 ： 60 80 90

    }

    private void ifPlayTheSplashSound() {
        boolean soundSwitchStatus = SettingPreferencesUtil.getSoundSwitchStatus(this).equals(CataSettingConstant.SOUND_SWITCH_STATUS_OPEN) ? true : false;
        if (!soundSwitchStatus) {   //不播放声音，
            com.ekek.settingmodule.utils.SoundUtil.setSystemMute(this);
        }
    }

    private void initClock() {
        DateTimeUtil.changeSystemDate(this, 2020, 6, 15);
        DateTimeUtil.changeSystemTime(this, 12, 0);
    }

    private void initTimerOpenStatusOfHood() {  // 默认是 定时器没有启动
        SettingPreferencesUtil.saveTimerOpenStatus(this, CataSettingConstant.TIMER_STATUS_CLOSE);
    }

    private void setAnim() {
        if (HighTempAnim == 0) {
            showPannelHighTempWarningDialog();
            colorAnim = ObjectAnimator.ofInt(pannelHighTempWarningDialog.getRootView(), "backgroundColor", RED_DARK, RED_LIGHT);
            // colorAnim = ObjectAnimator.ofInt(drawerLayout, "backgroundColor", RED_DARK, RED_LIGHT);
            colorAnim.setDuration(1000);//700
            colorAnim.setEvaluator(new ArgbEvaluator());
            colorAnim.setRepeatCount(ValueAnimator.INFINITE);
            colorAnim.setRepeatMode(ValueAnimator.REVERSE);
            colorAnim.start();
            LogUtil.d("set the high anim");
            HighTempAnim = 1;
            EventBus.getDefault().post(new SoundEvent(
                    SoundEvent.SOUND_ACTION_PLAY,
                    SoundUtil.SOUND_ID_ALARM_NTC,
                    SoundEvent.SOUND_TYPE_ALARM_NTC));
            Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PLAY, SoundUtil.SOUND_ID_ALARM_NTC, SoundEvent.SOUND_TYPE_ALARM_NTC)");
        }
    }

    private void cancleAnim() {
        if (colorAnim != null) {
            colorAnim.cancel();
            hidePannelHighTempWarningDialog();
            //drawerLayout.clearAnimation();
            //drawerLayout.setBackgroundResource(R.mipmap.wallpaper);
            LogUtil.d("high anim is close");
            HighTempAnim = 0;
            EventBus.getDefault().post(new SoundEvent(
                    SoundEvent.SOUND_ACTION_PAUSE,
                    SoundUtil.SOUND_ID_ALARM_NTC,
                    SoundEvent.SOUND_TYPE_ALARM_NTC));
            Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PAUSE, SoundUtil.SOUND_ID_ALARM_NTC, SoundEvent.SOUND_TYPE_ALARM_NTC)");
            colorAnim = null;
        }
    }

    private void showPannelHighTempWarningDialog() {
        if (pannelHighTempWarningDialog == null) {
            pannelHighTempWarningDialog = new CataDialog(this);
            pannelHighTempWarningDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        View view = LayoutInflater.from(this).inflate(R.layout.tfthobmodule_layout_dialog_pannel_high_temp, null);
        Display display = this.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
        pannelHighTempWarningDialog.setContentView(view, layoutParams);

        //pannelHighTempWarningDialog.setContentView(view);
        pannelHighTempWarningDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pannelHighTempWarningDialog.setCanceledOnTouchOutside(false);
        pannelHighTempWarningDialog.show();

    }

    private void hidePannelHighTempWarningDialog() {
        if (pannelHighTempWarningDialog != null && pannelHighTempWarningDialog.isShowing())
            pannelHighTempWarningDialog.dismiss();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        CataTFTHobApplication.getInstance().updateLatestTouchTime();
        GlobalVars.getInstance().updateLatestTouchTime();

        if (mAlarmClockSettingFragment != null) {   // 闹铃界面，5秒不操作则，确定并退出。
            mAlarmClockSettingFragment.setTouchTime(SystemClock.elapsedRealtime());
        }

        if (!GlobalVars.getInstance().isPowerOffEventHandled()) {
            // 串口发出了 PowerSwitchStateChangedEvent，但是还没有完全执行完毕，禁止用户操作
            Logger.getInstance().w("TouchEvent ignored!");
            return false;
        }

        int action = ev.getAction();
        float x1 = 0.0f, x2 = 0.0f, y1 = 0.0f, y2 = 0.0f;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                longClickStart = SystemClock.elapsedRealtime();
                CataTFTHobApplication.getInstance().setShowingErrER03(false);
                showingErrER03FirstTime = true;
                x1 = ev.getX();
                y1 = ev.getY();
                if (isInsideTheCircle(x1, y1)) {
                    touchInsideCircle = true;
                    startX = x1;
                    startY = y1;
                    //   LogUtil.d("liang the x1 is "+x1+" ; y1 is = "+y1);
                }
                // reproduceClickSound(); // 点击屏幕的声音
                break;
            case MotionEvent.ACTION_MOVE:
                if (SystemClock.elapsedRealtime() - longClickStart >= LONG_CLICK_DURATION_ER03) {
                    if (showingErrER03FirstTime) {
                        EventBus.getDefault().post(new SoundEvent(
                                SoundEvent.SOUND_ACTION_PLAY,
                                SoundUtil.SOUND_ID_ALARM_ONCE,
                                SoundEvent.SOUND_TYPE_ALARM_ONCE));

                        showingErrER03FirstTime = false;
                        if (currentFragment == mHobIntroFragment || currentFragment == mSplashFragment) {
                            // no need to do anything for now.
                        } else {
                            if (currentFragment != mTFTHobPanelFragment) {
                                showTFTHobPanelFragment(Hob60PanelFragment.HOB_PANEL_WORK_MODE_NORMAL);
                            }

                            onMenuBarSelect(hobMenuBar, HobMenuBar.HOB_MENU_BAR_ID_PAUSE);
                        }

                        ShowErrorEvent order = new ShowErrorEvent(ShowErrorEvent.ERROR_ER03);
                        showErrorMessage(
                                order.getError(),
                                order.getErrorTitle(this),
                                order.getErrorContent(this));
                        CataTFTHobApplication.getInstance().setShowingErrER03(true);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                x2 = ev.getX();
                y2 = ev.getY();

                if (CataTFTHobApplication.getInstance().isShowingErrER03()) {
                    return true;
                }

                if (isInsideTheCircle(x2, y2) && touchInsideCircle) {
                    if (isActiveLenght(startX, x2, startY, y2)) {
                        if (mAlarmClockSettingFragment != null
                                && currentFragment == mAlarmClockSettingFragment
                                && mAlarmClockSettingFragment.isVisible()) {   // 闹铃界面。
                            mAlarmClockSettingFragment.doConfirm();
                        }
                        // LogUtil.d("liang the isActiveLenght ");
                    }
                    // LogUtil.d("liang the x2 is "+x2+" ; y2 is = "+y2);
                }
                touchInsideCircle = false;
                break;
        }

        return super.dispatchTouchEvent(ev);
    }




    private void initMenuBar() {
        hobMenuBar.setOnMenuBarSelectListener(this);
        hoodMenuBar.setOnMenuBarSelectListener(this);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalVars.getInstance().setPowerSwitchState(CookerHardwareResponse.POWER_SWITCH_UNKNOWN);
        CataTFTHobApplication.getInstance().updateLatestTouchTime();
        GlobalVars.getInstance().updateLatestTouchTime();
        if (mTFTHobService != null) {
            mTFTHobService.resumeHardware();
        }
        registerEventBus();

        LogUtil.d("Enter:: onResume");
        activationDuration = SettingPreferencesUtil.getActivationTime(this);
        totalTurnOffDuration = SettingPreferencesUtil.getTotalTurnOffTime(this);
        checkingThread = new CheckingThread();
        checkingThread.start();
        lastTimeGotSerialData = SystemClock.elapsedRealtime();
        onLanguageChanged();
        //   DateTimeUtil.changeSystemDate(this, 2019, 5, 15);
        //   DateTimeUtil.changeSystemTime(this, 12, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (int i = 0; i < 11; i++) {
            if (handler.hasMessages(i)) {
                handler.removeMessages(i);
            }
        }
        if (mTFTHobService != null) {
            mTFTHobService.pauseHardware();
        }
        checkingThread.forceToStop = true;
        try {
            checkingThread.join(2000);
        } catch (InterruptedException e) {
            LogUtil.e(e.getMessage());
        }
        unregisterEventBus();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ShowErrorEvent event) {
        switch (event.getError()) {
            case ShowErrorEvent.ERROR_R1E:
                showErrorMessage(
                        event.getError(),
                        event.getErrorTitle(this),
                        event.getErrorContent(this),
                        R.mipmap.background_bn13);
                errorCookerId = event.getwParam();
                mTFTHobService.notifyCookerPause(errorCookerId);
                break;
            case ShowErrorEvent.ERROR_R2E:
                showErrorMessage(
                        event.getError(),
                        event.getErrorTitle(this),
                        event.getErrorContent(this),
                        R.mipmap.background_bn13);
                break;
            case ShowErrorEvent.ERROR_R3E:
            case ShowErrorEvent.ERROR_R4E:
                showErrorMessage(
                        event.getError(),
                        event.getErrorTitle(this),
                        event.getErrorContent(this),
                        R.mipmap.background_fast_mode_warning_dialog);
                break;
            case ShowErrorEvent.ERROR_R5E:
            case ShowErrorEvent.ERROR_R6E:
                if (currentFragment == mHobIntroFragment || currentFragment == mSplashFragment) {
                    // no need to do anything for now.
                } else {
                    if (currentFragment != mTFTHobPanelFragment) {
                        showTFTHobPanelFragment(Hob60PanelFragment.HOB_PANEL_WORK_MODE_NORMAL);
                    }

                    onMenuBarSelect(hobMenuBar, HobMenuBar.HOB_MENU_BAR_ID_PAUSE);
                }
                showErrorMessage(
                        event.getError(),
                        event.getErrorTitle(this),
                        event.getErrorContent(this),
                        R.mipmap.background_error_dialog);
                break;
            default:
                showErrorMessage(
                        event.getError(),
                        event.getErrorTitle(this),
                        event.getErrorContent(this));
                break;
        }
        EventBus.getDefault().post(new SoundEvent(
                SoundEvent.SOUND_ACTION_PLAY,
                SoundUtil.SOUND_ID_ALARM,
                SoundEvent.SOUND_TYPE_COMMON_ALARM));
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(HideErrorEvent event) {
        switch (event.getError()) {
            case ShowErrorEvent.ERROR_R6E:
                if (errorDialog != null
                        && errorDialog.isShowing()
                        && errorId == ShowErrorEvent.ERROR_R6E) {

                    closeErrorDialog();
                    resumeHob();
                    if (ptcCookerID != -1 && mTFTHobService != null) {
                        mTFTHobService.notifyCookerPowerOff(ptcCookerID);
                    }
                }
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PlaySoundEvent event) {
        switch (event.getOrder()) {
            case PlaySoundEvent.ORDER_TIME_OUT:
                if (errorDialog != null && errorDialog.isShowing()) {
                    switch (errorId) {
                        case ShowErrorEvent.ERROR_R2E:
                        case ShowErrorEvent.ERROR_R3E:
                            break;
                        case ShowErrorEvent.ERROR_R4E:
                            hideExternalSensorNotDetectedDialog();
                            break;
                        default:
                            hideErrorMessage();
                            if (errorCookerId != -1) {
                                mTFTHobService.notifyCookerPowerOff(errorCookerId);
                                if (errorCookerId == ptcCookerID) {
                                    ptcCookerID = -1;
                                }
                                errorCookerId = -1;
                            } else {
                                mTFTHobService.doPowerOffAllCookers();
                                ptcCookerID = -1;
                            }
                            break;
                    }
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DebugInfoEvent event) {
        LogUtil.d("Enter:: onMessageEvent(DebugInfoEvent event) ");
        if (event.isInDebugMode()) {
            String info = event.getDataSent().toRawDataString();
            if (!event.isSuccessfullySent()) {
                info = "?" + info;
            }
            tvDebugInfo.setText(info);
            tvDebugInfo.setVisibility(View.VISIBLE);

            info = DataParseUtil.bytesToHexString(
                    event.getDataReceived(),
                    event.getDataReceived().length);
            tvDebugInfoBottom.setText(info);
            tvDebugInfoBottom.setVisibility(View.VISIBLE);
        } else {
            tvDebugInfo.setVisibility(View.GONE);
            tvDebugInfoBottom.setVisibility(View.GONE);
        }
        Logger.getInstance().d(tvDebugInfoBottom.getText().toString());
        Logger.getInstance().d(tvDebugInfo.getText().toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NoExternalSensorDetectedEvent event) {
        showExternalSensorNotDetectedDialog(event.getSource());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CountDownEvent event){
        CataTFTHobApplication.getInstance().updateLatestTouchTime();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DateOrTimeSetEvent event) {
      //  onViewClicked(rightBack);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IdleEvent event) {
        int currentIntroStatus = getCurrentIntroStatus();
        switch (event.getIdleType()) {
            case IdleEvent.IDLE_HIBERNATION:
                if (currentIntroStatus == INTRO_STATUS_NONE) {
                    showSplashFragment(SplashFragment.SPLASH_MODE_ENDING_IDLE);
                }
                break;
            case IdleEvent.IDLE_SWITCH_OFF_TFT_COMPLETE_FOR_PANNEL_HIGH_TEMP:

                if (currentIntroStatus == INTRO_STATUS_NONE) {
                    showSplashFragment(SplashFragment.SPLASH_MODE_ENDING_PANNEL_HIGH_TEMP);
                }else if (currentIntroStatus == INTRO_STATUS_HIBERNATE) {//如果已经在时间界面了则进入黑屏等待进入低功耗
                    showHobIntroFragment(true, false, HobIntroFragment.TOTAL_POWER_OFF_FOR_PANNEL_HIGH_TEMP, false);
                    SettingPreferencesUtil.saveEnterPowerOffMode(this, CataSettingConstant.EnterPowerOffModel);
                }
                break;
            case IdleEvent.IDLE_TIMEOUT_DURING_INITIALIZING_PROCESS:
                //   showHobIntroFragment(, , , );
                if (GlobalVars.getInstance().isHibernationModeEnabled()) {
                    showHobIntroFragment(false, true, HobIntroFragment.TOTAL_POWER_OFF_NONE, true);
                    SettingPreferencesUtil.saveEnterPowerOffMode(this, CataSettingConstant.EnterHobIntroFragment);
                } else {
                    showHobIntroFragment(false, false, HobIntroFragment.TOTAL_POWER_OFF_NOW, false);
                    SettingPreferencesUtil.saveEnterPowerOffMode(this, CataSettingConstant.EnterPowerOffModel);
                }
                GlobalVars.getInstance().setInitializeProcessComplete(true);
                GlobalVars.getInstance().setAppStartTag(GlobalCons.APP_START_NONE);
                rightBack.setVisibility(View.INVISIBLE);  // 右向的箭头，不可见。（语言设置、时间及日期设置界面用）
                LogUtil.d("liang show HobIntroFragment");
                break;
            case IdleEvent.IDLE_TOTAL_POWER_OFF:
                if (currentIntroStatus == INTRO_STATUS_NONE) {
                    showSplashFragment(SplashFragment.SPLASH_MODE_ENDING_IDLE);
                } else if (currentIntroStatus == INTRO_STATUS_HIBERNATE) {
                    int totalPowerOffMode = hasCookerObnormalHighTemp ?
                            HobIntroFragment.TOTAL_POWER_OFF_UNTIL_COOL_DOWN : HobIntroFragment.TOTAL_POWER_OFF_NOW;
                    // 未开启待机模式，TotalOff 时间到，开启黑屏模式或待机等待模式
                    showHobIntroFragment(
                            true,
                            false,
                            totalPowerOffMode,
                            true);
                    if (hasCookerObnormalHighTemp) {
                        SettingPreferencesUtil.saveEnterPowerOffMode(this, CataSettingConstant.EnterPowerOffModelDelay);
                    } else {
                        SettingPreferencesUtil.saveEnterPowerOffMode(this, CataSettingConstant.EnterPowerOffModel);
                    }
                }
                break;
            case IdleEvent.IDLE_TOTAL_POWER_OFF_COOLED_DOWN:
                // TotalOff模式到时有高温，等待了一段时间，全部冷下来了
                showHobIntroFragment(
                        true,
                        false,
                        HobIntroFragment.TOTAL_POWER_OFF_NOW,
                        true);
                SettingPreferencesUtil.saveEnterPowerOffMode(this, CataSettingConstant.EnterPowerOffModel);
                break;
        }
    }

    private boolean receivedPowerOffSignal;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CookerHardwareEvent event) {
        CookerHardwareResponse response = event.getResponse();
        lastTimeGotSerialData = SystemClock.elapsedRealtime();

        if (!GlobalVars.getInstance().isInitializeProcessComplete()) return;


        CookerMessage aCookerMessage = response.getaCookerMessage();
        CookerMessage bCookerMessage = response.getbCookerMessage();
        CookerMessage cCookerMessage = response.getcCookerMessage();
        CookerMessage dCookerMessage = response.getdCookerMessage();
        CookerMessage eCookerMessage = response.geteCookerMessage();
        CookerMessage fCookerMessage = response.getfCookerMessage();
        hasCookerObnormalHighTemp = false;
        if (TFTHobConfiguration.TFT_HOB_TYPE == TFTHobConfiguration.TFT_HOB_TYPE_60) {
            hasCookerObnormalHighTemp = aCookerMessage.isHighTemp() || bCookerMessage.isHighTemp() || cCookerMessage.isHighTemp();
        } else if (TFTHobConfiguration.TFT_HOB_TYPE == TFTHobConfiguration.TFT_HOB_TYPE_80) {
            hasCookerObnormalHighTemp = aCookerMessage.isHighTemp() || bCookerMessage.isHighTemp() || cCookerMessage.isHighTemp() || dCookerMessage.isHighTemp();
        } else if (TFTHobConfiguration.TFT_HOB_TYPE == TFTHobConfiguration.TFT_HOB_TYPE_90) {
            hasCookerObnormalHighTemp = aCookerMessage.isHighTemp() || bCookerMessage.isHighTemp() || cCookerMessage.isHighTemp() || eCookerMessage.isHighTemp() || fCookerMessage.isHighTemp();
        }

        GlobalVars.getInstance().setOkToSendE2(!hasCookerObnormalHighTemp);

        if (receivedPowerOffSignal) {

            if (mSplashFragment != null && !mSplashFragment.isHidden()) {
                GlobalVars.getInstance().setPowerOffEventHandled(true);
                return;
            }

            if (mHobCookerSettingFragment != null && !mHobCookerSettingFragment.isHidden()) {
                hideFragment(mHobCookerSettingFragment);
                removeFragment(mHobCookerSettingFragment);
                mHobCookerSettingFragment = null;
            }

         //   closeScheduleFragment();// 点击关机时，要关闭定时界面。H107界面
            LogUtil.d("Enter::  onMessageEvent  power off  ");
            EventBus.getDefault().post(new SoundEvent(
                    SoundEvent.SOUND_ACTION_PAUSE_ALL,
                    SoundUtil.SOUND_ID_ALARM_TIMER,
                    SoundEvent.SOUND_TYPE_UNKNOWN));
            Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PAUSE_ALL, SoundUtil.SOUND_ID_ALARM_TIMER, SoundEvent.SOUND_TYPE_UNKNOWN)");
            EventBus .getDefault().post(new CloseDialogEvent(CloseDialogEvent.DIALOG_RECIPES_EDIT)) ;
            hideExternalSensorNotDetectedDialog();
            hideDialog();
            DatabaseHelper.powerOffHobAndHoodData();
            if (mHobIntroFragment != null && !mHobIntroFragment.isHidden()) {
                if (!mHobIntroFragment.isShowingBlackMask()) {
                    // 在待机时钟页面收到了关机信号，则直接黑屏
                    showHobIntroFragment(true, false, HobIntroFragment.TOTAL_POWER_OFF_NOW, false);
                    SettingPreferencesUtil.saveEnterPowerOffMode(this, CataSettingConstant.EnterPowerOffModelFromSerialPort);
                } else {
                    GlobalVars.getInstance().setPowerOffEventHandled(true);
                }

            } else {
                // 未进入待机时钟页面时，收到关机信号，则播放动画后进入时钟待机页面或者黑屏
                rightBack.setVisibility(View.INVISIBLE);  // 右向的箭头，不可见。（语言设置、时间及日期设置界面用）
                showSplashFragment(SplashFragment.SPLASH_MODE_ENDING_SERIAL);
            }

          //  closeScheduleFragment();// 点击关机时，要关闭定时界面。H107界面
            receivedPowerOffSignal = false;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PowerSwitchStateChangedEvent event) {
        receivedPowerOffSignal = false;
        int enterPowerMode = SettingPreferencesUtil.getEnterPowerOffMode(this);
        switch (event.getNewState()) {
            case CookerHardwareResponse.POWER_SWITCH_OFF:
                switch (enterPowerMode) {
                    case CataSettingConstant.EnterNone:
                    case CataSettingConstant.EnterPowerOnModel:
                    case CataSettingConstant.EnterHobWorkingFragment:
                        receivedPowerOffSignal = true;
                        GlobalVars.getInstance().setPowerOffEventHandled(false);
                        break;
                    case CataSettingConstant.EnterPowerOffModelDelay:
                    case CataSettingConstant.EnterLanguageSettingFragment:
                    case CataSettingConstant.EnterDateSettingFragment:
                    case CataSettingConstant.EnterTimeSettingFragment:
                    case CataSettingConstant.EnterHobIntroFragment:
                    case CataSettingConstant.EnterPowerOffModelFromSerialPort:
                    default:
                        break;
                }
                break;
            case CookerHardwareResponse.POWER_SWITCH_OFF_2:
                switch (enterPowerMode) {
                    case CataSettingConstant.EnterPowerOffModelDelay:
                    case CataSettingConstant.EnterHobIntroFragment:
                        receivedPowerOffSignal = true;
                        GlobalVars.getInstance().setPowerOffEventHandled(false);
                        break;
                    case CataSettingConstant.EnterNone:
                    case CataSettingConstant.EnterPowerOnModel:
                    case CataSettingConstant.EnterHobWorkingFragment:
                    case CataSettingConstant.EnterLanguageSettingFragment:
                    case CataSettingConstant.EnterDateSettingFragment:
                    case CataSettingConstant.EnterTimeSettingFragment:
                    case CataSettingConstant.EnterPowerOffModelFromSerialPort:
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SettingsChangedEvent event) {
        switch (event.getSettingChanged()) {
            case SettingsChangedEvent.SETTING_HIBERNATION_MODE:
                break;
            case SettingsChangedEvent.SETTING_ACTIVATION_TIME:
                activationDuration = SettingPreferencesUtil.getActivationTime(this);
                break;
            case SettingsChangedEvent.SETTING_TOTAL_TURN_OFF_MODE:
                break;
            case SettingsChangedEvent.SETTING_TOTAL_TURN_OFF_TIME:
                totalTurnOffDuration = SettingPreferencesUtil.getTotalTurnOffTime(this);
                break;
            case SettingsChangedEvent.SETTING_LANGUAGE:
                onLanguageChanged();
                break;
            case SettingsChangedEvent.SETTING_DEMO_SWITCH_STATUS:
                onDemoSwitchStatusChanged();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TipsEvent event) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BluetoothEvent event) {
        switch (event.getOrder()) {
            case BluetoothEvent.ORDER_FAILED:
                Logger.getInstance().e("BluetoothEvent.ORDER_FAILED");
                break;
            case BluetoothEvent.ORDER_START:
                if (mTFTHobService != null) {
                    mTFTHobService.doPowerOffAllCookers();
                    ptcCookerID = -1;
                }
                hideAndDisableMenuBar();
                Bundle bundle = new Bundle();
                bundle.putInt(SystemUpgradeFragment.KEY_TOTAL, event.getwParam());
                bundle.putString(SystemUpgradeFragment.KEY_VERSION, event.getsParam());
                createSystemUpgradeFragment();
                mSystemUpgradeFragment.setArguments(bundle);
                showFragment(mSystemUpgradeFragment);
                Logger.getInstance().i("BluetoothEvent.ORDER_START " + event.getwParam() + " of " + event.getsParam());
                break;
            case BluetoothEvent.ORDER_REPORT_PROGRESS:
                break;
            case BluetoothEvent.ORDER_FINISHED:
                Logger.getInstance().i("BluetoothEvent.ORDER_FINISHED");
                break;
        }
    }

    private void createSystemUpgradeFragment() {
        if (mSystemUpgradeFragment == null) {
            mSystemUpgradeFragment = new SystemUpgradeFragment();
            fragments.add(mSystemUpgradeFragment);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CookerPanelHighTempEvent event) {
        Message message = new Message();
        message.obj = event.isPanelHighTemp();
        message.what = HANDLER_UPDATE_COOKER_PANEL_HIGH_TEMP;
        handler.sendMessage(message);
        Logger.getInstance().d("CookerPanelHighTempEvent(" + event.isPanelHighTemp() + ")");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BleBatteryEvent event) {

        Message message = new Message();
        if (event.getBatteryViewStatus() == BleBatteryEvent.BATTERY_VIEW_STATUS_UPDATE_LEVEL) {
            message.what = HANDLER_UPDATE_BT_BATTERY_LEVEL_UI;
            message.arg1 = event.getChargeStatus();
            message.arg2 = event.getBatteryLevel();
            maxUsableMinutes = event.getMaxUsableMinutes();
            batteryLevel = event.getBatteryLevel();
            if (batteryLevel <= BleBatteryEvent.BATTERY_LEVEL_EXHAUSTED) {
                if (!r3eErrorHandled) {
                    EventBus.getDefault().post(new ShowErrorEvent(ShowErrorEvent.ERROR_R3E));
                    r3eErrorHandled = true;
                }
            }
        } else if (event.getBatteryViewStatus() == BleBatteryEvent.BATTERY_VIEW_STATUS_SHOW) {
            message.what = HANDLER_UPDATE_BT_BATTERY_UI;
            message.arg1 = event.getBatteryViewStatus();
            r2eErrorHandled = false;
            r3eErrorHandled = false;
            r5eErrorHandled = false;
            r6eErrorHandled = false;
        } else if (event.getBatteryViewStatus() == BleBatteryEvent.BATTERY_VIEW_STATUS_HIDE) {
            message.what = HANDLER_UPDATE_BT_BATTERY_UI;
            message.arg1 = event.getBatteryViewStatus();
            message.arg2 = event.getStopType();
            r2eErrorHandled = false;
            r3eErrorHandled = false;
            r5eErrorHandled = false;
            r6eErrorHandled = false;
        } else if (event.getBatteryViewStatus() == BleBatteryEvent.BATTERY_VIEW_STATUS_UPDATE_CHARGE_STATE) {
            message.what = HANDLER_UPDATE_BT_BATTERY_CHARGE_STATE;
            if (event.getChargeState() > 0) {
                if (errorDialog != null && errorDialog.isShowing()) {
                    if (errorId == ShowErrorEvent.ERROR_R1E
                            || errorId == ShowErrorEvent.ERROR_R2E
                            || errorId == ShowErrorEvent.ERROR_R3E
                            || errorId == ShowErrorEvent.ERROR_R4E) {
                        closeErrorDialog();
                    }
                }
            }
        }
        handler.sendMessage(message);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NTCEvent event) {
        Message message = new Message();
        message.what = HANDLER_UPDATE_NTC_TEMP_UI;
        message.obj = event;
        handler.sendMessage(message);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DrawerEvent event) {

        switch (event.getEvent()) {
            case DrawerEvent.EVENT_DRAWER_CLOSED:
                if ((mTFTHobPanelFragment != null && mTFTHobPanelFragment.isVisible()) ||
                        (mHobCookerSettingFragment != null && mHobCookerSettingFragment.isVisible()) ||
                        (mHobCookingTablesFragment != null && mHobCookingTablesFragment.isVisible()) ||
                        (mHobCookingTablesDetailFragment != null && mHobCookingTablesDetailFragment.isVisible()) ||
                        (mHobRecipesFoodDetailFragment != null && mHobRecipesFoodDetailFragment.isVisible())) {
                    if (mTFTHobPanelFragment != null) {
                        mTFTHobPanelFragment.setLateralMenuBarOpen(BaseHobFragment.LATERAL_MENU_BAR_CLOSE);
                    }
                    if (mHobCookerSettingFragment != null) {
                        mHobCookerSettingFragment.setLateralMenuBarOpen(BaseFragment.LATERAL_MENU_BAR_CLOSE);
                    }
                    if (mHobCookingTablesFragment != null) {
                        mHobCookingTablesFragment.setLateralMenuBarOpen(BaseFragment.LATERAL_MENU_BAR_CLOSE);
                    }
                    if (mHobCookingTablesDetailFragment != null) {
                        mHobCookingTablesDetailFragment.setLateralMenuBarOpen(BaseFragment.LATERAL_MENU_BAR_CLOSE);
                    }
                    if (mHobRecipesFoodDetailFragment != null) {
                        mHobRecipesFoodDetailFragment.setLateralMenuBarOpen(BaseFragment.LATERAL_MENU_BAR_CLOSE);
                    }
                }
                break;
            case DrawerEvent.EVENT_DRAWER_OPENED:
                if ((mTFTHobPanelFragment != null && mTFTHobPanelFragment.isVisible()) ||
                        (mHobCookerSettingFragment != null && mHobCookerSettingFragment.isVisible()) ||
                        (mHobCookingTablesFragment != null && mHobCookingTablesFragment.isVisible()) ||
                        (mHobCookingTablesDetailFragment != null && mHobCookingTablesDetailFragment.isVisible()) ||
                        (mHobRecipesFoodDetailFragment != null && mHobRecipesFoodDetailFragment.isVisible())) {
                    if (mTFTHobPanelFragment != null) {
                        mTFTHobPanelFragment.setLateralMenuBarOpen(BaseFragment.LATERAL_MENU_BAR_OPEN);
                    }
                    if (mHobCookerSettingFragment != null) {
                        mHobCookerSettingFragment.setLateralMenuBarOpen(BaseFragment.LATERAL_MENU_BAR_OPEN);
                    }
                    if (mHobCookingTablesFragment != null) {
                        mHobCookingTablesFragment.setLateralMenuBarOpen(BaseFragment.LATERAL_MENU_BAR_OPEN);
                    }
                    if (mHobCookingTablesDetailFragment != null) {
                        mHobCookingTablesDetailFragment.setLateralMenuBarOpen(BaseFragment.LATERAL_MENU_BAR_OPEN);
                    }
                    if (mHobRecipesFoodDetailFragment != null) {
                        mHobRecipesFoodDetailFragment.setLateralMenuBarOpen(BaseFragment.LATERAL_MENU_BAR_OPEN);
                    }
                }
                break;
        }
    }

    private void hideDialog() {
        if (timerIsUpDialog != null && timerIsUpDialog.isShowing()) {
            sbctv.closeTimer();
            timerIsUpDialog.dismiss();
            timerIsUpDialog = null;
            cancelTimerIsUpDialogBlinkThread();
        }
    }

    @Override
    protected int getContainerID() {
        return R.id.ll_container;
    }

    private void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void unregisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this);
    }

    private void onLanguageChanged() {
        LogUtil.d("Enter test:: onLanguageChanged");
        int language = SettingPreferencesUtil.getDefaultLanguage2(this);
        if (language == CataSettingConstant.LANGUAGE_UNKNOWN) {
            language = CataSettingConstant.DEFAULT_LANGUAGE_2;
        }
        switch (getLocalStr(language)) {
            case "el":
            case "ru":
            case "vi":
            case "pl":
                GlobalVars.getInstance().setDefaultFontBold(CataTFTHobApplication.getInstance().getRobotoFontBold());
                GlobalVars.getInstance().setDefaultFontRegular(CataTFTHobApplication.getInstance().getRobotoFontRegular());
                break;
            default:
                GlobalVars.getInstance().setDefaultFontBold(CataTFTHobApplication.getInstance().getHelveticaFontBold());
                GlobalVars.getInstance().setDefaultFontRegular(CataTFTHobApplication.getInstance().getHelveticaFontRegular());
                break;
        }
        Resources r = this.getResources();
        final Configuration c = r.getConfiguration();
        DisplayMetrics dm = r.getDisplayMetrics();
        Locale locale = new Locale(getLocalStr(language));
        c.setLocale(locale);
        GlobalVars.getInstance().setCurrentLocale(locale);
        r.updateConfiguration(c, dm);
        for (Fragment fragment : fragments) {
            fragment.onConfigurationChanged(r.getConfiguration());
        }
    }


    private void onDemoSwitchStatusChanged() {
        if (!GlobalVars.getInstance().isInDemoMode()) {
            if (mTFTHobService != null) {
                mTFTHobService.doPowerOffAllCookers();
                ptcCookerID = -1;
            }
        }
    }

    private String getLocalStr(int language) {
        for (int i = 0; i < CataSettingConstant.LANGUAGES.length; i++) {
            if (CataSettingConstant.LANGUAGES[i] == language) {
                return CataSettingConstant.LANGUAGE_LOCALES[i];
            }
        }
        return "en";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//蓝牙权限开启成功
                EventBus.getDefault().post(new TempSensorRequestEvent(-1, TempSensorRequestEvent.ACTION_FIND_AND_CONNECT_TEMP_SENSOR));
            } else {
                //Toast.makeText(MainActivity.this, "蓝牙权限未开启,请设置", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showHobRecipesEditFragment(int type, MyRecipesTable initData) {
        if (mHobRecipesEditFragment == null) {
            mHobRecipesEditFragment = new HobRecipesEditFragment();
            fragments.add(mHobRecipesEditFragment);
        }

        mHobRecipesEditFragment.setType(type);
        mHobRecipesEditFragment.setInitData(initData);
        showFragment(mHobRecipesEditFragment);
    }

    private void showHobMyRecipesEditFragment(int type, String content) {
        createHobMyRecipesEditFragment();
        mHobMyRecipesEditFragment.setContent(type, content);
        showFragment(mHobMyRecipesEditFragment);
    }

    private void showHobMyRecipesEditFragment(MyRecipesTable myRecipesTable) {
        createHobMyRecipesEditFragment();
        mHobMyRecipesEditFragment.setMyRecipesTable(myRecipesTable);
        showFragment(mHobMyRecipesEditFragment);
    }

    private void createHobMyRecipesEditFragment() {
        if (mHobMyRecipesEditFragment == null) {
            mHobMyRecipesEditFragment = new HobMyRecipesEditFragment();
            fragments.add(mHobMyRecipesEditFragment);
        }
    }

    private void showHobMyRecipesFragment() {

        if (mHobMyRecipesFragment == null) {
            mHobMyRecipesFragment = new HobMyRecipesFragment();
            fragments.add(mHobMyRecipesFragment);
        }
        showFragment(mHobMyRecipesFragment);
    }

    private void showTFTHobPanelFragment(int mode) {
        if (mTFTHobPanelFragment == null) {
            if (TFTHobConfiguration.TFT_HOB_TYPE == TFTHobConfiguration.TFT_HOB_TYPE_60) {
                mTFTHobPanelFragment = new Hob60PanelFragment();

            } else if (TFTHobConfiguration.TFT_HOB_TYPE == TFTHobConfiguration.TFT_HOB_TYPE_80) {
                mTFTHobPanelFragment = new Hob80PanelFragment();


            } else if (TFTHobConfiguration.TFT_HOB_TYPE == TFTHobConfiguration.TFT_HOB_TYPE_90) {
                mTFTHobPanelFragment = new Hob90PanelFragment();
            }
            mTFTHobPanelFragment.setMode(mode);
            fragments.add(mTFTHobPanelFragment);
        } else {
            mTFTHobPanelFragment.setMode(mode);
        }

        showFragment(mTFTHobPanelFragment);

    }

    private void showTestViewFragment() {
        if (mTestViewFragment == null) {
            mTestViewFragment = new TestViewFragment();
            fragments.add(mTestViewFragment);
        }
        showFragment(mTestViewFragment);
    }

    private void showSettingFragment(int mode) {
        LogUtil.d("Enter:: showSettingFragment");
        if (mSettingFragment == null) {
            mSettingFragment = new SettingFragment(mode);
            fragments.add(mSettingFragment);
        } else {
            mSettingFragment.setType(mode);
        }
        showFragment(mSettingFragment);
    }


    private void showTimerSettingFragment(int from) {
        int hour = CataSettingConstant.DEFAULT_ALARM_CLOCK_HOUR;
        int minute = CataSettingConstant.DEFAULT_ALARM_CLOCK_MINUTE;
        AlarmClockSettingDao dao = CataTFTHobApplication.getDaoSession().getAlarmClockSettingDao();
        List<AlarmClockSetting> settings = dao.loadAll();
        if (settings.size() == 1) {
            hour = 0;// settings.get(0).getHour();
            minute = 10;//settings.get(0).getMinute();
        }

        if (sbctv.getTimerIsWorking()) {  // 有定时在进行中
            int[] value = new int[2];
            value = sbctv.getRemainingTime();
            hour = value[0];
            minute = value[1];

        }
        // LogUtil.d("hour---->" + hour + "-----minute--->" + minute);
        if (mAlarmClockSettingFragment == null) {
            mAlarmClockSettingFragment = new AlarmClockSettingFragment(from, hour, minute);
            fragments.add(mAlarmClockSettingFragment);
        } else {
            mAlarmClockSettingFragment.initAlarmTimer(from, hour, minute);
        }
        showFragment(mAlarmClockSettingFragment);
    }

    private void showHobRecipesFoodDetailFragment(int image, String foodTypeName) {
        if (mHobRecipesFoodDetailFragment == null) {
            mHobRecipesFoodDetailFragment = new HobRecipesFoodDetailFragment(image, foodTypeName);
            mHobRecipesFoodDetailFragment.setLateralMenuBarOpen(mTFTHobPanelFragment.getLateralMenuBarOpen());
            fragments.add(mHobRecipesFoodDetailFragment);
        } else {
            mHobRecipesFoodDetailFragment.setRecipeImageId(image);
            mHobRecipesFoodDetailFragment.setFoodTypeName(foodTypeName);
        }
        showFragment(mHobRecipesFoodDetailFragment);
    }

    private void showHobCookingTablesDetailFragment(int type) {
        if (mHobCookingTablesDetailFragment == null) {
            mHobCookingTablesDetailFragment = new HobCookingTablesDetailFragment(type);
            mHobCookingTablesDetailFragment.setLateralMenuBarOpen(mTFTHobPanelFragment.getLateralMenuBarOpen());
            fragments.add(mHobCookingTablesDetailFragment);
        } else {
            mHobCookingTablesDetailFragment.setType(type);
        }
        showFragment(mHobCookingTablesDetailFragment);
    }

    private void showHobCookingTablesFragment() {
        if (mHobCookingTablesFragment == null) {
            mHobCookingTablesFragment = new HobCookingTablesFragment();
            mHobCookingTablesFragment.setLateralMenuBarOpen(mTFTHobPanelFragment.getLateralMenuBarOpen());
            fragments.add(mHobCookingTablesFragment);
        }
        showFragment(mHobCookingTablesFragment);
    }

    private void showHobRecipesFragment() {
        if (mHobRecipesFragment == null) {
            mHobRecipesFragment = new HobRecipesFragment();
            fragments.add(mHobRecipesFragment);
        }
        showFragment(mHobRecipesFragment);
    }

    private void showHoodFragment() {
        if (mHoodPanelFragment == null) {
            mHoodPanelFragment = new HoodPanelFragment(TFTHobConfiguration.TFT_HOB_TYPE);
            fragments.add(mHoodPanelFragment);
        }
        showFragment(mHoodPanelFragment);
    }

/*    private void showHobPanelFragment() {
        if (mHobPanelFragment == null) {
            mHobPanelFragment = new HobPanelFragment();
            fragments.add(mHobPanelFragment);
        }
        showFragment(mHobPanelFragment);
    }*/

/*    private void showHobPanel80Fragment() {
        if (mHobPanel80Fragment == null) {
            mHobPanel80Fragment = new HobPanel80Fragment();
            fragments.add(mHobPanel80Fragment);
        }
        showFragment(mHobPanel80Fragment);

    }*/

 /*   private void showHobPanelFragment(CookerSettingData data) {
        if (mHobPanelFragment == null) {
            mHobPanelFragment = new HobPanelFragment();
            fragments.add(mHobPanelFragment);
        } else {
            if (data != null) mHobPanelFragment.setCookerSettingData(data);
        }
        showFragment(mHobPanelFragment);
    }*/

    private void showHobCookerSettingFragment(CookerSettingData data) {
        LogUtil.d("showHobCookerSettingFragment");
        if (mHobCookerSettingFragment == null) {
            mHobCookerSettingFragment = new HobCookerSettingFragment(data);
            mHobCookerSettingFragment.setLateralMenuBarOpen(mTFTHobPanelFragment.getLateralMenuBarOpen());
            fragments.add(mHobCookerSettingFragment);
        } else {
            mHobCookerSettingFragment.setSettingInitData(data);
        }
        if (mTFTHobService != null) {
            // mTFTHobService.notifyStopReceiveError(cookerID);
        }
        showFragment(mHobCookerSettingFragment);
    }

    private void showHobTemperatureModeSelectFragment(CookerSettingData data) {
        if (mHobTemperatureModeSelectFragment == null) {
            mHobTemperatureModeSelectFragment = new HobTemperatureModeSelectFragment(data);
            fragments.add(mHobTemperatureModeSelectFragment);
        } else {
            mHobTemperatureModeSelectFragment.setSettingInitData(data);
        }

        showFragment(mHobTemperatureModeSelectFragment);
    }

    private void showHobIntroFragment(
            boolean respondPowerOnSignal,
            boolean clickToQuit,
            int totalOffMode,
            boolean forceShow) {
        try {
            Logger.getInstance().d(
                    "showHobIntroFragment(" + respondPowerOnSignal + "," + clickToQuit + "," + totalOffMode + "," + forceShow + ")",
                    true);

            //主动断开蓝牙温度计，避免进入低功耗后长时间没有断开蓝牙
            EventBus.getDefault().post(new BluetoothEevent());

            hideErrorMessage();
            hideDialog();
            hideExternalSensorNotDetectedDialog();
            EventBus .getDefault().post(new CloseDialogEvent(CloseDialogEvent.DIALOG_RECIPES_EDIT)) ;  // 隐藏对话框
            if (resumeHob()) {
                if (mTFTHobService != null) {
                    mTFTHobService.doPowerOffAllCookers();
                    ptcCookerID = -1;
                }
            }

            if (mHobIntroFragment == null) {
                mHobIntroFragment = new HobIntroFragment(TFTHobConfiguration.TFT_HOB_TYPE);
                fragments.add(mHobIntroFragment);
            }

            mHobIntroFragment.setRespondPowerOnSignal(respondPowerOnSignal);
            mHobIntroFragment.setClickToQuit(clickToQuit);
            mHobIntroFragment.setTotalPowerOffMode(totalOffMode);
            if (mHobIntroFragment.isHidden() || forceShow) {
                showFragment(mHobIntroFragment);
            }
            switch (totalOffMode) {
                case HobIntroFragment.TOTAL_POWER_OFF_NOW:
                    showTotalTurnOffFrame();
                    break;
                case HobIntroFragment.TOTAL_POWER_OFF_UNTIL_COOL_DOWN:
                    mHobIntroFragment.showBlackMask(!GlobalVars.getInstance().isHibernationModeEnabled());
                    startWaitCoolDownThread();
                    break;
                case HobIntroFragment.TOTAL_POWER_OFF_FOR_PANNEL_HIGH_TEMP:
                    mHobIntroFragment.showBlackMask(true);
                    startWaitCoolDownThread();
                    break;
                default:
                    mHobIntroFragment.showBlackMask(false);
                    break;
            }

            clearTimer();
        } finally {
            GlobalVars.getInstance().setPowerOffEventHandled(true);
        }
    }

    private void startWaitCoolDownThread() {
        cancelWaitCoolDownThread();
        waitCoolDownThread = new WaitCoolDownThread();
        waitCoolDownThread.start();
    }

    private void cancelWaitCoolDownThread() {
        if (waitCoolDownThread != null) {
            if (waitCoolDownThread.isAlive()) {
                waitCoolDownThread.forceToStop = true;
                try {
                    waitCoolDownThread.join(2000);
                } catch (InterruptedException e) {
                    LogUtil.e(e.getMessage());
                }
            }
        }
    }

    private void showSplashFragment(int mode) {

        if (mSplashFragment == null) {
            mSplashFragment = new SplashFragment(mode);
            fragments.add(mSplashFragment);
        } else {
            mSplashFragment.setMode(mode);
        }

        if (!GlobalVars.getInstance().isPlaySplashVideo()) {
            GlobalVars.getInstance().setPlaySplashVideo(true);
            showFragment(mSplashFragment);
        }
    }

    private void showTempCtrlFragment() {
        mTempCtrlFragment = new TempCtrlFragment();
        showFragment(mTempCtrlFragment);
    }

    @Override
    public void showFragment(Fragment fragment) {
//        LogUtil.d("Enter:: showFragment");
        currentFragment = fragment;
        super.showFragment(fragment);
        if (ibUpInfo.getVisibility() == View.VISIBLE) ibUpInfo.setVisibility(View.INVISIBLE);
        if (fragment == mHobIntroFragment || fragment == mSplashFragment) {
            //if (ibUpInfo.getVisibility() == View.VISIBLE) ibUpInfo.setVisibility(View.INVISIBLE);

        } else {
            //if (ibUpInfo.getVisibility() == View.INVISIBLE) ibUpInfo.setVisibility(View.VISIBLE);
        }

        if (fragment == mHobIntroFragment || fragment == mSplashFragment) {
            if (sbctv.getVisibility() == View.VISIBLE) sbctv.setVisibility(View.INVISIBLE);
            hideAndDisableMenuBar();
            hideBackButton();


        } else if (fragment == mHoodPanelFragment) {
            if (sbctv.getVisibility() == View.INVISIBLE) sbctv.setVisibility(View.VISIBLE);
            sbctv.setClockTimerViewOrientation(LinearLayout.VERTICAL);
            if (hoodMenuBar.getVisibility() == View.INVISIBLE)
                //   hoodMenuBar.setVisibility(View.VISIBLE);
                if (hobMenuBar.getVisibility() == View.VISIBLE)
                    hobMenuBar.setVisibility(View.INVISIBLE);
            hideAndDisableMenuBar();
            hideBackButton();

        /*} else if (fragment == mHobPanelFragment) {
            if (sbctv.getVisibility() == View.INVISIBLE) sbctv.setVisibility(View.VISIBLE);
            sbctv.setClockTimerViewOrientation(LinearLayout.VISIBLE); // VISIBLE
            if (hoodMenuBar.getVisibility() == View.VISIBLE)
                hoodMenuBar.setVisibility(View.INVISIBLE);
            if (hobMenuBar.getVisibility() == View.INVISIBLE)
                hobMenuBar.setVisibility(View.VISIBLE);
            hideBackButton();*/

        } else if (fragment == mTFTHobPanelFragment) {
            if (sbctv.getVisibility() == View.INVISIBLE) sbctv.setVisibility(View.VISIBLE);
            sbctv.setClockTimerViewOrientation(LinearLayout.HORIZONTAL);  // 更新 2019年1月14日14:36:10
            if (hoodMenuBar.getVisibility() == View.VISIBLE)
                hoodMenuBar.setVisibility(View.INVISIBLE);
            if (hobMenuBar.getVisibility() == View.INVISIBLE)
                hobMenuBar.setVisibility(View.VISIBLE);
            switch (mTFTHobPanelFragment.getLateralMenuBarOpen()) {
                case BaseHobFragment.LATERAL_MENU_BAR_NOT_DEFINED:
                case BaseHobFragment.LATERAL_MENU_BAR_OPEN:
                    showAndEnableMenuBar();
                    break;
                case BaseHobFragment.LATERAL_MENU_BAR_CLOSE:
                    hideMenuBar();
                    break;
            }

            hideBackButton();

            // setAnim();
        } else if (fragment == mHobRecipesFragment) { // C10
            if (sbctv.getVisibility() == View.INVISIBLE)
                sbctv.setVisibility(View.VISIBLE);  // 更新 2018年9月22日16:55:12
            sbctv.setClockTimerViewOrientation(LinearLayout.HORIZONTAL);
            hideAndDisableMenuBar();
            //showBackButton();

        } else if (fragment == mHobRecipesFoodDetailFragment) {
            switch (mHobRecipesFoodDetailFragment.getLateralMenuBarOpen()) {
                case BaseHobFragment.LATERAL_MENU_BAR_NOT_DEFINED:
                case BaseHobFragment.LATERAL_MENU_BAR_OPEN:
                    showAndEnableMenuBar();
                    break;
                case BaseHobFragment.LATERAL_MENU_BAR_CLOSE:
                    hideMenuBar();
                    break;
            }
            if (hoodMenuBar.getVisibility() == View.VISIBLE)
                hoodMenuBar.setVisibility(View.INVISIBLE);
            if (hobMenuBar.getVisibility() == View.INVISIBLE)
                hobMenuBar.setVisibility(View.VISIBLE);
        } else if (fragment == mHobCookingTablesFragment) { // C450
            if (sbctv.getVisibility() == View.INVISIBLE)
                sbctv.setVisibility(View.VISIBLE);// 更新 2018年9月22日16:55:12
            sbctv.setClockTimerViewOrientation(LinearLayout.HORIZONTAL);
            switch (mHobCookingTablesFragment.getLateralMenuBarOpen()) {
                case BaseHobFragment.LATERAL_MENU_BAR_NOT_DEFINED:
                case BaseHobFragment.LATERAL_MENU_BAR_OPEN:
                    showAndEnableMenuBar();
                    break;
                case BaseHobFragment.LATERAL_MENU_BAR_CLOSE:
                    hideMenuBar();
                    break;
            }
            //showBackButton();

        } else if (fragment == mHobCookingTablesDetailFragment) {// C457 ~ C4512
            hideBackButton();
        } else if (fragment == mHobCookerSettingFragment) {
            switch (mHobCookerSettingFragment.getLateralMenuBarOpen()) {
                case BaseHobFragment.LATERAL_MENU_BAR_NOT_DEFINED:
                case BaseHobFragment.LATERAL_MENU_BAR_OPEN:
                    showAndEnableMenuBar();
                    break;
                case BaseHobFragment.LATERAL_MENU_BAR_CLOSE:
                    hideMenuBar();
                    break;
            }
            hideBackButton();
            //  if (sbctv.getVisibility() == View.VISIBLE) sbctv.setVisibility(View.INVISIBLE); // 更新 2019年2月20日16:07:31
        } else if (fragment == mSettingFragment) {
            if (sbctv.getVisibility() == View.INVISIBLE)
                sbctv.setVisibility(View.VISIBLE);// 更新  2018年9月23日21:13:47
            // cancleAnim();
        } else {
            sbctv.setClockTimerViewOrientation(LinearLayout.HORIZONTAL);
            //if (scv.getVisibility() == View.INVISIBLE) scv.setVisibility(View.VISIBLE);
        }
        if (fragment == mTFTHobPanelFragment) {

            //bivBluetooth.setVisibility(View.VISIBLE);
        } else {
            bivBluetooth.setVisibility(View.INVISIBLE);
        }

    }

    private void showBackButton() {
        if (tvBack.getVisibility() == View.INVISIBLE) tvBack.setVisibility(View.VISIBLE);
    }

    private void hideBackButton() {
        if (tvBack.getVisibility() == View.VISIBLE) tvBack.setVisibility(View.INVISIBLE);
    }

    private void hideMenuBar() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        }

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);

        if (ibMenuHandle.getVisibility() == View.INVISIBLE) {
            ibMenuHandle.setVisibility(View.VISIBLE);
        }
    }

    private void hideAndDisableMenuBar() {
        hideMenuBar();

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        if (ibMenuHandle.getVisibility() == View.VISIBLE)
            ibMenuHandle.setVisibility(View.INVISIBLE);
    }

    private void showAndEnableMenuBar() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.openDrawer(GravityCompat.END);
        }
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);

        if (ibMenuHandle.getVisibility() == View.INVISIBLE)
            ibMenuHandle.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHobPanelFragmentFinish() {

    }

    @Override
    public void onRequestSettingCooker(CookerSettingData data) {
        showHobCookerSettingFragment(data);
    }

    @Override
    public void onHobPanelFragmentRequestSwitchToHoodPanel() {
        showHoodFragment();
    }

    @Override
    public void onHobCookerSettingFragmentFinish(CookerSettingData data, int finishType) {
        //showHobPanelFragment(data);

        int soundType = SoundEvent.getSoundType(data.getCookerID(), true);
        EventBus.getDefault().post(new SoundEvent(
                SoundEvent.SOUND_ACTION_PAUSE,
                SoundUtil.SOUND_ID_ALARM_TIMER,
                soundType));
        Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PAUSE, SoundUtil.SOUND_ID_ALARM_TIMER, " + soundType + ")");

        soundType = SoundEvent.getSoundType(data.getCookerID(), false);
        EventBus.getDefault().post(new SoundEvent(
                SoundEvent.SOUND_ACTION_PAUSE,
                SoundUtil.SOUND_ID_ALARM,
                soundType));
        Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PAUSE, SoundUtil.SOUND_ID_ALARM, " + soundType + ")");

        if (finishType == HobCookerSettingFragment.FINISH_TYPE_SAVE) {
            if (mTFTHobService != null) mTFTHobService.notifyCookerUpdateSettingData(data);
        }

        showTFTHobPanelFragment(Hob60PanelFragment.HOB_PANEL_WORK_MODE_NORMAL);

        removeFragment(mHobCookerSettingFragment);
        mHobCookerSettingFragment = null;
        if (data.getCookerID() == r2eErrorCookerId) {
            r2eErrorHandled = false;
        }
    }

    @Override
    public void onShowHobRecipesFragment() {
        showHobRecipesFragment();

        removeFragment(mHobCookerSettingFragment);
        mHobCookerSettingFragment = null;
    }


    @OnClick({R.id.ib_up_info, R.id.ib_hood, R.id.ib_menu_handle, R.id.tv_back,R.id.right_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_up_info:
            case R.id.ib_hood:
                break;
            case R.id.ib_menu_handle:
                LogUtil.d("Enter:: ib_menu_handle");
                break;
            case R.id.tv_back:
                doBack();
                break;
            case R.id.right_back:
                switch (mSettingStatus){
                    case CataSettingConstant.SetLanguage:
                        rightBack.setVisibility(View.INVISIBLE);
                        EventBus.getDefault().post(new SendOrderForFirstSwitchOn(CataSettingConstant.SetDate));
                        EventBus.getDefault().post(new SetLanguage(CataSettingConstant.SET_LANGUAGE_SUCCESSFUL));
                        EventBus.getDefault().post(new SettingFragmentHiddenEvent(SettingFragmentHiddenEvent.FRAGMENT_LANGUAGE_SETTING));
                        mSettingStatus=CataSettingConstant.SetDate;

                        break;
                    case CataSettingConstant.SetDate:
                        rightBack.setVisibility(View.INVISIBLE);
                        EventBus.getDefault().post(new SendOrderForFirstSwitchOn(CataSettingConstant.SetTime));
                        EventBus.getDefault().post(new SettingFragmentHiddenEvent(SettingFragmentHiddenEvent.FRAGMENT_DATE_SETTING));
                        mSettingStatus=CataSettingConstant.SetTime;
                        break;
                    case CataSettingConstant.SetTime:
                        SettingPreferencesUtil.saveTheFirstTimeSwitchOnHob(this, CataSettingConstant.THE_FIRST_TIME_SWITCH_ON_HOB);  // 保存 ，不是第一次开机
                        GlobalVars.getInstance().setAppStartTag(GlobalCons.APP_START_NONE);
                        GlobalVars.getInstance().setInitializeProcessComplete(true);
                        // EventBus.getDefault().post(new SendOrderForFirstSwitchOn(CataSettingConstant.GoBackToSetting));
                        EventBus.getDefault().post(new SendOrderForFirstSwitchOn(CataSettingConstant.ShowMainWorkSpace));
                        EventBus.getDefault().post(new SettingFragmentHiddenEvent(SettingFragmentHiddenEvent.FRAGMENT_TIME_SETTING));
                        break;
                }
                break;
        }
    }

    private void doBack() {
        if (!mHobRecipesFragment.isHidden()) {
            //showHobPanelFragment(null);
            showTFTHobPanelFragment(Hob60PanelFragment.HOB_PANEL_WORK_MODE_NORMAL);
        } else if (!mHobCookingTablesFragment.isHidden()) {
            showHobRecipesFragment();
        }
    }

    private void showLockDialog() {
        if (lockDialog == null) {
            lockDialog = new CataDialog(this);
            lockDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        View view = LayoutInflater.from(this).inflate(R.layout.tfthobmodule_layout_dialog_lock, null);
        lockDialog.setContentView(view);
        lockDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        lockDialog.setCanceledOnTouchOutside(false);
        lockDialog.show();

        ImageButton button = view.findViewById(R.id.ib_lock);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lockDialog.dismiss();
                hideBottomUIMenu();
                String securityMode = SettingPreferencesUtil.getDefaultSecurityMode(getApplicationContext());
                if (securityMode.equals(CataSettingConstant.SECURITY_MODE_PRESS_UNLOCK)) {
                    showUnLockDialogForPressUnlockMode();
                } else if (securityMode.equals(CataSettingConstant.SECURITY_MODE_PIN)) {

                } else if (securityMode.equals(CataSettingConstant.SECURITY_MODE_PATTERN)) {

                }

            }
        });
    }

    private void showUnLockDialogForPinLockMode(final SecurityTable security) {
        if (unLockDialog != null && unLockDialog.isShowing()) unLockDialog.dismiss();
        if (unLockDialog == null) {
            unLockDialog = new CataDialog(this);
            unLockDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        View view = LayoutInflater.from(this).inflate(R.layout.tfthobmodule_layout_dialog_unlock_pin_unlock_mode, null);
        unLockDialog.setContentView(view);
        unLockDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        unLockDialog.setCanceledOnTouchOutside(false);
        unLockDialog.show();
        final ImageButton ibUnlock = (ImageButton) view.findViewById(R.id.ib_lock);

        ibUnlock.setVisibility(View.VISIBLE);

        final PinPasswordView ppv = view.findViewById(R.id.ppv);
        ppv.setMode(PinPasswordView.MODE_CHECK_PASSWORD);
        ppv.setOnPasswordListener(new OnPasswordListener() {
            @Override
            public boolean onCheckPwd(String pwd) {
                if (pwd.equals(security.getPassword())) {
                    ibUnlock.setImageDrawable(getResources().getDrawable(R.mipmap.ic_unlock));
                    ibUnlock.setVisibility(View.VISIBLE);
                    ppv.setVisibility(View.INVISIBLE);
                    handler.sendEmptyMessageDelayed(HANDLER_COOKER_UNLOCK, 1000);
                    return true;
                } else {
                    ibUnlock.setVisibility(View.VISIBLE);
                    ppv.setVisibility(View.INVISIBLE);
                    ppv.clearEditText();
                    return false;
                }

            }

            @Override
            public void onWarning(String msg) {

            }
        });

        ppv.setVisibility(View.INVISIBLE);

        ibUnlock.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        ibUnlock.setVisibility(View.INVISIBLE);
                        ppv.setVisibility(View.VISIBLE);
                        break;
                }
                return false;
            }
        });


    }

    private void showUnLockDialogForPatternLockMode(final SecurityTable security) {
        if (unLockDialog != null && unLockDialog.isShowing()) unLockDialog.dismiss();
        if (unLockDialog == null) {
            unLockDialog = new CataDialog(this);
            unLockDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        View view = LayoutInflater.from(this).inflate(R.layout.tfthobmodule_layout_dialog_unlock_pattern_unlock_mode, null);
        unLockDialog.setContentView(view);
        unLockDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        unLockDialog.setCanceledOnTouchOutside(false);
        unLockDialog.show();
        final ImageButton ibUnlock = (ImageButton) view.findViewById(R.id.ib_lock);
        ibUnlock.setVisibility(View.VISIBLE);

        final PatternLockView patternLockView = view.findViewById(R.id.pattern_lock_view);
        patternLockView.setAspectRatioEnabled(true);
        patternLockView.setAspectRatio(PatternLockView.AspectRatio.ASPECT_RATIO_WIDTH_BIAS);
        patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
        patternLockView.setDotAnimationDuration(150);
        patternLockView.setPathEndAnimationDuration(100);
        patternLockView.setInStealthMode(false);
        patternLockView.setTactileFeedbackEnabled(true);
        patternLockView.setWrongStateColor(Color.RED);
        patternLockView.setInputEnabled(true);
        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                if (!PatternLockUtils.patternToString(patternLockView, pattern).equals(security.getPassword())) {
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);

                } else {
                    ibUnlock.setImageDrawable(getResources().getDrawable(R.mipmap.ic_unlock));
                    ibUnlock.setVisibility(View.VISIBLE);
                    patternLockView.setVisibility(View.INVISIBLE);
                    handler.sendEmptyMessageDelayed(HANDLER_COOKER_UNLOCK, 1000);
                }

            }

            @Override
            public void onCleared() {

            }
        });
        patternLockView.setVisibility(View.INVISIBLE);
        ibUnlock.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        ibUnlock.setVisibility(View.INVISIBLE);
                        patternLockView.setVisibility(View.VISIBLE);
                        break;
                }
                return false;
            }
        });


    }

    private void showUnLockDialogForPressUnlockMode() {
        if (unLockDialog != null && unLockDialog.isShowing()) unLockDialog.dismiss();
        if (unLockDialog == null) {
            unLockDialog = new CataDialog(this);
            unLockDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        View view = LayoutInflater.from(this).inflate(R.layout.tfthobmodule_layout_dialog_unlock_press_unlock_mode, null);
        unLockDialog.setContentView(view);
        unLockDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        unLockDialog.setCanceledOnTouchOutside(false);
        unLockDialog.show();

        final ImageButton ibUnlock = (ImageButton) view.findViewById(R.id.ib_lock);
        final TextView counterTv = (TextView) view.findViewById(R.id.tv_counter);

        Typeface typeface = GlobalVars.getInstance().getDefaultFontRegular();
        counterTv.setTypeface(typeface);

        ibUnlock.setOnTouchListener(new View.OnTouchListener() {
            long startTime, currentTime;
            int counter = 2;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startTime = SystemClock.elapsedRealtime();
                        counterTv.setText(String.valueOf(counter));
                        counterTv.setVisibility(View.VISIBLE);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        currentTime = SystemClock.elapsedRealtime();
                        if (currentTime - startTime > 1000 && currentTime - startTime <= 2000) {
                            if (counter == 2) {
                                counter = 1;
                                counterTv.setText(String.valueOf(counter));
                            }


                        } else if (currentTime - startTime > 2000 && currentTime - startTime <= 3000) {
                            if (counter == 1) {
                                counter = 0;
                                counterTv.setText(String.valueOf(counter));
                            }
                            //unLockDialog.dismiss();
                            //hideBottomUIMenu();
                            ibUnlock.setOnTouchListener(null);
                            ibUnlock.setImageDrawable(getResources().getDrawable(R.mipmap.ic_unlock));
                            handler.sendEmptyMessageDelayed(HANDLER_COOKER_UNLOCK, 1000);
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        counterTv.setVisibility(View.INVISIBLE);
                        counter = 2;
                        break;
                }


                return false;
            }
        });

    }

    private void showPauseDialogForHob() {
        if (pauseDialog == null) {
            pauseDialog = new CataDialog(this);
            pauseDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        View view = LayoutInflater.from(this).inflate(R.layout.tfthobmodule_layout_dialog_pause_hob, null);
        pauseDialog.setContentView(view);
        pauseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pauseDialog.setCanceledOnTouchOutside(false);

        final WindowManager.LayoutParams params = pauseDialog.getWindow().getAttributes();
        params.width = 930;
        params.height = 340;
        pauseDialog.getWindow().setAttributes(params);

        pauseDialog.show();
        //notifyCookerStopWork();
        final boolean[] state = {false};
        final ImageButton button = view.findViewById(R.id.ib_pause);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!state[0]) {
                    state[0] = true;
                    button.setImageDrawable(getResources().getDrawable(R.mipmap.play_p501));
                    handler.sendEmptyMessageDelayed(HANDLER_COOKER_CONTINUE_WORK_FOR_HOB, 100);
                }
            }
        });
        doPauseForHob();

        //stop alarm sound
        EventBus.getDefault().post(new SoundEvent(
                SoundEvent.SOUND_ACTION_PAUSE_ALL,
                SoundUtil.SOUND_ID_ALARM_TIMER,
                SoundEvent.SOUND_TYPE_UNKNOWN));
        Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PAUSE_ALL, SoundUtil.SOUND_ID_ALARM_TIMER, SoundEvent.SOUND_TYPE_UNKNOWN)");

    }

    private void showPauseDialogForHood() {

        if (pauseDialog == null) {
            pauseDialog = new CataDialog(this);
            pauseDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        View view = LayoutInflater.from(this).inflate(R.layout.tfthobmodule_layout_dialog_pause, null);
        pauseDialog.setContentView(view);
        pauseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pauseDialog.setCanceledOnTouchOutside(false);

        final WindowManager.LayoutParams params = pauseDialog.getWindow().getAttributes();
        params.width = 730;
        params.height = 340;
        pauseDialog.getWindow().setAttributes(params);

        pauseDialog.show();
        //notifyCookerStopWork();
        final boolean[] state = {false};
        final ImageButton button = view.findViewById(R.id.ib_pause);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!state[0]) {
                    state[0] = true;
                    button.setImageDrawable(getResources().getDrawable(R.mipmap.play_p501));
                    handler.sendEmptyMessageDelayed(HANDLER_COOKER_CONTINUE_WORK_FOR_HOOD, 100);
                }
            }
        });
        doPauseForHood();

        //stop alarm sound
        EventBus.getDefault().post(new SoundEvent(
                SoundEvent.SOUND_ACTION_PAUSE_ALL,
                SoundUtil.SOUND_ID_ALARM_TIMER,
                SoundEvent.SOUND_TYPE_UNKNOWN));
        Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PAUSE_ALL, SoundUtil.SOUND_ID_ALARM_TIMER, SoundEvent.SOUND_TYPE_UNKNOWN)");

    }

    private void doPauseForHood() {
        //  mTFTHobService.notifyCookerPause();
        if (mHoodPanelFragment != null) {
            mHoodPanelFragment.doPause();
        }
    }

    private void doPauseForHob() {
        mTFTHobService.notifyCookerPause();
       /* if (mHoodPanelFragment != null) {
            mHoodPanelFragment.doPause();
        }*/
    }


    private void doPlayForHood() {
        //  mTFTHobService.notifyCookerPlay();
        if (mHoodPanelFragment != null) {
            mHoodPanelFragment.doPlay();
        }
    }

    private void doPlayForHob() {
        mTFTHobService.notifyCookerPlay();
       /* if (mHoodPanelFragment != null) {
            mHoodPanelFragment.doPlay();
        }*/
    }

    private void showNormalDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MainActivity.this);
        normalDialog.setIcon(R.mipmap.ic_info);
        normalDialog.setTitle("警告");
        //normalDialog.setMessage("系统通信错误，代码：F8,请检查控制板通讯接口！");
        normalDialog.setMessage("系统时钟错误，代码：F9,请进行重启操作！");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }

    private TimerIsUpDialogBlinkThread blinkThread;
    private void startTimerIsUpDialogBlinkThread() {
        cancelTimerIsUpDialogBlinkThread();
        blinkThread = new TimerIsUpDialogBlinkThread();
        blinkThread.start();
    }
    private void cancelTimerIsUpDialogBlinkThread() {
        if (blinkThread != null) {
            if (blinkThread.isAlive()) {
                blinkThread.setCancelTask(true);
                try {
                    blinkThread.join(2000);
                } catch (InterruptedException e) {
                    Logger.getInstance().e(e);
                }
            }
            blinkThread = null;
        }
    }

    private void closeAlarmWhenHasNewTimer(){


        EventBus.getDefault().post(new SoundEvent(
                SoundEvent.SOUND_ACTION_PAUSE,
                SoundUtil.SOUND_ID_ALARM_TIMER,
                SoundEvent.SOUND_TYPE_COMMON_ALARM));
    }

    private void showTimerIsUpDialog() {
        if (timerIsUpDialog == null) {
            timerIsUpDialog = new CataDialog(this);
            // timerIsUpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        View view = LayoutInflater.from(this).inflate(R.layout.tfthobmodule_layout_dialog_timer_is_up, null);
        timerIsUpDialog.setContentView(view);
        timerIsUpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timerIsUpDialog.setCanceledOnTouchOutside(false);

        //去掉android4.4 dialog顶部蓝线
        Context context = timerIsUpDialog.getContext();
        int divierId = context.getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = timerIsUpDialog.findViewById(divierId);
        divider.setBackgroundColor(Color.TRANSPARENT);
        //去掉android4.4 dialog顶部蓝线

        timerIsUpDialogAddTen = view.findViewById(R.id.tv_add10);
        timerIsUpDialogAddTen.setTypeface(GlobalVars.getInstance().getDefaultFontRegular());
        timerIsUpDialogClose = view.findViewById(R.id.tv_close);
        timerIsUpDialogClose.setTypeface(GlobalVars.getInstance().getDefaultFontRegular());
        timerIsUpDialogClose.setText(R.string.settingmodule_fragment_information_key_to_icon_close);

        timerIsUpDialog.show();

        EventBus.getDefault().post(new SoundEvent(
                SoundEvent.SOUND_ACTION_PLAY,
                SoundUtil.SOUND_ID_ALARM_TIMER,
                SoundEvent.SOUND_TYPE_COMMON_ALARM));
        Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PLAY, SoundUtil.SOUND_ID_ALARM_TIMER, SoundEvent.SOUND_TYPE_COMMON_ALARM)");

        timerIsUpDialogContentView = view.findViewById(R.id.iv_timer);
        if (ProductManager.PRODUCT_TYPE == ProductManager.Haier) {
            timerIsUpDialogContentView.setImageBitmap(getBitmap(R.mipmap.bg_dialog_timer_is_up_haier));
        }
        timerIsUpDialogContentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_UP:
                        float y1 = view.getHeight() / 2;//圆点Y
                        float y2 = event.getY();//触摸点Y
                        if (y2 < y1) {//do add 10 minutes
                            sbctv.setAndStartTenMinuteTimer();
                            timerIsUpDialog.dismiss();
                            timerIsUpDialog = null;
                            cancelTimerIsUpDialogBlinkThread();
                            hideBottomUIMenu();
                            EventBus.getDefault().post(new SoundEvent(
                                    SoundEvent.SOUND_ACTION_PAUSE,
                                    SoundUtil.SOUND_ID_ALARM_TIMER,
                                    SoundEvent.SOUND_TYPE_COMMON_ALARM));
                            Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PAUSE, SoundUtil.SOUND_ID_ALARM_TIMER, SoundEvent.SOUND_TYPE_COMMON_ALARM)");

                        } else {//do close
                            timerIsUpDialog.dismiss();
                            timerIsUpDialog = null;
                            cancelTimerIsUpDialogBlinkThread();
                            sbctv.closeTimer();
                            hideBottomUIMenu();
                            EventBus.getDefault().post(new SoundEvent(
                                    SoundEvent.SOUND_ACTION_PAUSE,
                                    SoundUtil.SOUND_ID_ALARM_TIMER,
                                    SoundEvent.SOUND_TYPE_COMMON_ALARM));
                            Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PAUSE, SoundUtil.SOUND_ID_ALARM_TIMER, SoundEvent.SOUND_TYPE_COMMON_ALARM)");
                        }

                        break;
                }

                return true;
            }

        });

        startTimerIsUpDialogBlinkThread();

        if (unLockDialog != null && unLockDialog.isShowing()) {
            doUnlock(false);
            doLock(false);
        }
    }


    @Override
    public void onHoodPanelFragmentRequestSwitchToHobPanel() {
        //showHobPanelFragment(null);
        showTFTHobPanelFragment(Hob60PanelFragment.HOB_PANEL_WORK_MODE_NORMAL);
    }



    @Override
    public void onRequestSaveGearValue(int fanGear, int lightGear) {
        DatabaseHelper.saveHoodData(fanGear, lightGear);
    }

    public int[] onRequestGetGearValue(int index) {
        int[] gear = new int[4];
        switch (index) {
            case 1:

                break;
            case 2:
                HoodData hoodData = DatabaseHelper.getHoodData();

                gear[0] = hoodData.getFanGear();
                gear[1] = hoodData.getLightGear();
//LogUtil.d("liang the gear is "+gear[0]);
                break;
        }


        return gear;
    }

    @Override
    public int[] getFanLightValueFromMainPanelB30() {
        int[] value = new int[2];

        value[0] = mFanValueFromMainPanelB30;
        value[1] = mLightValueFromMainPanelB30;
        return value;
    }

    @Override
    public void sendFanLightValueFromMainPanelB30(int fanGear, int lightGear) {
        mFanValueFromMainPanelB30 = fanGear;
        mLightValueFromMainPanelB30 = lightGear;
    }

    @Override
    public void doBackSwitchToHoodPanel(int order) {

        showHoodFragment();
        if (order == TFTHoodConstant.HOOD_SHOW_HOOD_AGAIN) {

        } else {
            // handler.sendEmptyMessageDelayed(HANDLER_CLOSE_FAN_LIGHT_HOOD, 200);
            //   EventBus.getDefault().post(new PowerOffEven(1)); // 临时，关机命令

        }
    }

    @Override
    public void onHobIntroFragmentFinish(boolean silently) {
        LogUtil.d("Enter:: onHobIntroFragmentFinish");
        closeFanGear();
        CataTFTHobApplication.getInstance().updateLatestTouchTime();
        mHobIntroFragment.setTotalPowerOffMode(HobIntroFragment.TOTAL_POWER_OFF_NONE);
        hideFragment(mHobIntroFragment);
        mHobIntroFragment.showBlackMask(false); // 关闭黑色的幕
		//closeFanGear();
        int volume = Integer.valueOf(SettingPreferencesUtil.getDefaultSound(this));
        com.ekek.settingmodule.utils.SoundUtil.setSystemVolume(this, volume);
        cancelWaitCoolDownThread();
        if (silently) {
            showTFTHobPanelFragment(Hob60PanelFragment.HOB_PANEL_WORK_MODE_NORMAL);
            SettingPreferencesUtil.saveEnterPowerOffMode(this, CataSettingConstant.EnterPowerOnModel);
        } else {
            SettingPreferencesUtil.saveEnterPowerOffMode(this, CataSettingConstant.EnterPowerOnModel);
            showTFTHobPanelFragment(Hob60PanelFragment.HOB_PANEL_WORK_MODE_NORMAL);
            //  showSplashFragment(SplashFragment.SPLASH_MODE_INTRO);  // 时钟界面结束后，显示主界面
            LogUtil.d("liang HobIntroFragmentFinish");
        }
        GlobalVars.getInstance().setShowingPauseHobDialog(false); // 开机后，恢复 暂停标志到默认值。
        rightBack.setVisibility(View.INVISIBLE);  // 右向的箭头，不可见。（语言设置、时间及日期设置界面用）
        sbctv.setVisibility(View.VISIBLE); // 恢复时钟可见
    //    closeFanGear();
        GlobalVars.getInstance().setClickPowerOffButton(false);  // 点击物理开关键关机标记恢复
        closeScheduleFragment();// 关闭风机定时 2019年8月10日15:40:20

        //start blutooth search
        mTFTHobService.recoverConnectForBT();
    }

    private void closeFanGear(){
        GlobalVars.getInstance().setPause(false);// 风机 暂时标志恢复。
        if(mHoodPanelFragment!=null){
            mHoodPanelFragment.hiddenPauseIcon();  // 风机暂停标志消失
        }
        HoodData hoodData = DatabaseHelper.getHoodData();
        int fan=hoodData.getFanGear();
        int ligth=hoodData.getLightGear();
    //    DatabaseHelper.saveHoodData(0, ligth);// 风机数据清零 2019年8月12日16:20:00
        DatabaseHelper.saveHoodData(0, 0);// 风机 灯光 数据清零 2019年10月21日19:23:39
//        LogUtil.d("liang the ligth is "+ligth);
    }

    @Override
    public void onSplashFragmentFinish(int mode) {
        GlobalVars.getInstance().setPlaySplashVideo(false);
        if (GlobalVars.getInstance().getAppStartTag() == GlobalCons.APP_START_NONE) {  // 第二次开机
            switch (mode) {
                case SplashFragment.SPLASH_MODE_INTRO:
                    showTFTHobPanelFragment(Hob60PanelFragment.HOB_PANEL_WORK_MODE_NORMAL);
                    SettingPreferencesUtil.saveEnterPowerOffMode(this, CataSettingConstant.EnterPowerOnModel);

                    break;
                case SplashFragment.SPLASH_MODE_ENDING_PANNEL_HIGH_TEMP://进入黑屏，等待进入低功耗，因为屏幕高温1分钟后，客户要求完全完闭TFT
                    showHobIntroFragment(true, false, HobIntroFragment.TOTAL_POWER_OFF_FOR_PANNEL_HIGH_TEMP, false);
                    SettingPreferencesUtil.saveEnterPowerOffMode(this, CataSettingConstant.EnterPowerOffModel);
                    break;
                case SplashFragment.SPLASH_MODE_ENDING_IDLE:
                    int totalPowerOffMode = HobIntroFragment.TOTAL_POWER_OFF_NONE;
                    boolean clickToQuit = true;
                    boolean respondPowerOnSignal = false;
                    if (!GlobalVars.getInstance().isHibernationModeEnabled()) {
                        totalPowerOffMode = hasCookerObnormalHighTemp ? HobIntroFragment.TOTAL_POWER_OFF_UNTIL_COOL_DOWN : HobIntroFragment.TOTAL_POWER_OFF_NOW;
                        respondPowerOnSignal = true;
                        clickToQuit = false;
                    }
                    // 待机模式开启，待机时间到，则开启待机页面
                    showHobIntroFragment(respondPowerOnSignal, clickToQuit, totalPowerOffMode, true);
                    int enterMode = CataSettingConstant.EnterHobIntroFragment;
                    if (totalPowerOffMode == HobIntroFragment.TOTAL_POWER_OFF_NOW) {
                        enterMode = CataSettingConstant.EnterPowerOffModel;
                    } else if (totalPowerOffMode == HobIntroFragment.TOTAL_POWER_OFF_UNTIL_COOL_DOWN) {
                        enterMode = CataSettingConstant.EnterPowerOffModelDelay;
                    }
                    SettingPreferencesUtil.saveEnterPowerOffMode(this, enterMode);
                    rightBack.setVisibility(View.INVISIBLE);  // 右向的箭头，不可见。（语言设置、时间及日期设置界面用）
                    break;
                case SplashFragment.SPLASH_MODE_ENDING_SERIAL:
                    // 收到关机信号
                    if (mHobIntroFragment == null || mHobIntroFragment.isHidden()) {
                        if (GlobalVars.getInstance().isHibernationModeEnabled()) {
                            showHobIntroFragment(false, true, HobIntroFragment.TOTAL_POWER_OFF_NONE, true);
                            SettingPreferencesUtil.saveEnterPowerOffMode(this, CataSettingConstant.EnterHobIntroFragment);
                        } else {
                            showHobIntroFragment(true, false, HobIntroFragment.TOTAL_POWER_OFF_NOW, true);
                            SettingPreferencesUtil.saveEnterPowerOffMode(this, CataSettingConstant.EnterPowerOffModelFromSerialPort);
                        }
                    } else if (!mHobIntroFragment.isShowingBlackMask()) {
                        showHobIntroFragment(true, false, HobIntroFragment.TOTAL_POWER_OFF_NOW, true);
                        SettingPreferencesUtil.saveEnterPowerOffMode(this, CataSettingConstant.EnterPowerOffModelFromSerialPort);
                    } else {
                        GlobalVars.getInstance().setPowerOffEventHandled(true);
                    }
                //  closeScheduleFragment();
                    handler.sendEmptyMessageDelayed(HANDLER_CLOSE_TIMER_WHEN_POWER_OFF,3*1000 );  //延迟4秒后再发关闭命令
                    break;
            }

        } else if (GlobalVars.getInstance().getAppStartTag() == GlobalCons.APP_START ||
                GlobalVars.getInstance().getAppStartTag() == GlobalCons.APP_START_FIRST_TIME) {
            if (mode == SplashFragment.SPLASH_MODE_INTRO) {
                SettingPreferencesUtil.saveEnterPowerOffMode(this, CataSettingConstant.EnterPowerOnModel);
                showSettingFragment(CataSettingConstant.SETTING_NORMAL);
                hideAndDisableMenuBar();
             //   rightBack.setVisibility(View.VISIBLE);
                rightBack.setVisibility(View.INVISIBLE);
                sbctv.setVisibility(View.INVISIBLE);  // 时间不可见
                LogUtil.d("mainactivity is the first time " + GlobalVars.getInstance().getAppStartTag());
            }

            GlobalVars.getInstance().setPowerOffEventHandled(true);
        } else {
            GlobalVars.getInstance().setPowerOffEventHandled(true);
        }
    }

    @Override
    public void onMenuBarSelect(View view, int id) {
        if (view == hobMenuBar) {
            switch (id) {
                case HobMenuBar.HOB_MENU_BAR_ID_LOCK:
                    doLock(true);
                   // EventBus.getDefault().post(new BluetoothEevent());

                    break;
                case HobMenuBar.HOB_MENU_BAR_ID_PAUSE:
                    GlobalVars.getInstance().setShowingPauseHobDialog(true);
                    showPauseDialogForHob();
//                    LogUtil.d("liang show pause dialog~~");
                    break;
                case HobMenuBar.HOB_MENU_BAR_ID_MENU:
                    //showHobRecipesFragment();
                    break;
                case HobMenuBar.HOB_MENU_BAR_ID_TIMER:
                    if (mAlarmIsEdited) {

                    } else {
                        showTimerSettingFragment(AlarmClockSettingFragment.REQUEST_TIMER_SETTING_HOD);
                        mAlarmIsEdited = true;
                    }

                    break;
                case HobMenuBar.HOB_MENU_BAR_ID_SETTING:
                    switch (GlobalVars.getInstance().getAppStartTag()) {
                        case GlobalCons.APP_START_NONE: // 不是第一次开机
                            // 主界面
                            showSettingFragment(CataSettingConstant.SETTING_NORMAL);
                            break;
                        case GlobalCons.APP_START_FIRST_TIME: // 是第一次开机
                            showSettingFragment(CataSettingConstant.SETTING_AFTER_DEFAULT_SETTING);  // 开机后，在进行设置时，不操作，将进入hibernation界面
                            break;
                        case GlobalCons.APP_START:// 关机后，再开机
                            //  switchToDateSettingFragment();
                            showSettingFragment(CataSettingConstant.SETTING_AFTER_DEFAULT_SETTING);// 开机后，在进行设置时，不操作，将进入hibernation界面
                            break;
                    }
                    break;
            }


        } else if (view == hoodMenuBar) {
            switch (id) {
                case HoodMenuBar.HOB_MENU_BAR_ID_PAUSE:
                    //showLockDialog();
                    //showUnLockDialogForPressUnlockMode();
                    // showPauseDialog();
                    break;
                case HoodMenuBar.HOB_MENU_BAR_ID_CYCLIC:

                    break;
                case HoodMenuBar.HOB_MENU_BAR_ID_SCHEDULE:

                    break;
                case HoodMenuBar.HOB_MENU_BAR_ID_TIMER:
                    showTimerSettingFragment(AlarmClockSettingFragment.REQUEST_TIMER_SETTING_HOOD);
                    break;
                case HoodMenuBar.HOB_MENU_BAR_ID_SETTING:

                    break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OrderFromHoodPanel event) {
        int order;
        order = event.getOrder();
        switch (order) {
            case TFTHoodConstant.HOOD_DO_PAUSE:
                showPauseDialogForHood();
                break;
            case TFTHoodConstant.TIMER_FROM_H104_SCREEN:
                showScheduleFragment(TFTHoodConstant.TIMER_FROM_H104_SCREEN);
                hideAndDisableMenuBar();
                //   mTimerOfHoodIsWorking=true;
                LogUtil.d("liang the timer is working  from H104");
                break;
            case TFTHoodConstant.TIMER_FROM_MAIN_PANEL_SCREEN:
                if (GlobalVars.getInstance().isShowingPauseHobDialog()) {

                } else {
                    showScheduleFragment(TFTHoodConstant.TIMER_FROM_MAIN_PANEL_SCREEN);
                    SettingPreferencesUtil.saveTimerOpenStatus(this, CataSettingConstant.TIMER_STATUS_OPEN);
                    hideAndDisableMenuBar();
                    LogUtil.d("liang the timer is working  from MAIN PANEL");
                }
                break;
            case TFTHoodConstant.HOOD_GO_BACK_TO_HOB_PANEL:
                showTFTHobPanelFragment(Hob60PanelFragment.HOB_PANEL_WORK_MODE_NORMAL);
                break;
            case TFTHoodConstant.HOOD_TIMER_IS_UP:
                // mTimerOfHoodIsWorking=false;
                break;
            case TFTHoodConstant.TIMER_CLOSED_BY_OPENING_COOKER:
                if (mHoodPanelScheduleFragment != null) {
                    mHoodPanelScheduleFragment.stopTimer();
                    LogUtil.d("liang stop timer from TIMER_CLOSED_BY_OPENING_COOKER");
                }
                break;

        }
    }


    private void showScheduleFragment(int type) {
        if (mHoodPanelScheduleFragment == null) {
            mHoodPanelScheduleFragment = new HoodPanelScheduleFragment(type,TFTHobConfiguration.TFT_HOB_TYPE);
            fragments.add(mHoodPanelScheduleFragment);
        } else {
            mHoodPanelScheduleFragment.setType(type,TFTHobConfiguration.TFT_HOB_TYPE);
        }
        showFragment(mHoodPanelScheduleFragment);
    }

    private void closeScheduleFragment(){
        if (mHoodPanelScheduleFragment != null) {
            mHoodPanelScheduleFragment.closeScheduleFragment();
            LogUtil.d("liang stop timer from TIMER_CLOSED_BY_OPENING_COOKER");
        }
    }
    private void doLock(boolean playSound) {
        SecurityTable security = SecurityDBUtil.getDefaultSecurity(this);
        String type = security.getType();
        if (type.equals(CataSettingConstant.SECURITY_MODE_PRESS_UNLOCK)) {
            showUnLockDialogForPressUnlockMode();
        } else if (type.equals(CataSettingConstant.SECURITY_MODE_PIN)) {
            showUnLockDialogForPinLockMode(security);
        } else if (type.equals(CataSettingConstant.SECURITY_MODE_PATTERN)) {
            showUnLockDialogForPatternLockMode(security);
        }

        if (playSound) {
            SoundEvent event = new SoundEvent(
                    SoundEvent.SOUND_ACTION_PLAY,
                    SoundUtil.SOUND_ID_LOCK,
                    SoundEvent.SOUND_TYPE_LOCK);
            EventBus.getDefault().post(event);
            Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PLAY, SoundUtil.SOUND_ID_LOCK, SoundEvent.SOUND_TYPE_LOCK)");
        }
    }
    private void doUnlock(boolean playSound) {
        if (unLockDialog != null && unLockDialog.isShowing()) {
            unLockDialog.dismiss();
            hideBottomUIMenu();

            if (playSound) {
                SoundEvent event = new SoundEvent(
                        SoundEvent.SOUND_ACTION_PLAY,
                        SoundUtil.SOUND_ID_LOCK,
                        SoundEvent.SOUND_TYPE_LOCK);
                EventBus.getDefault().post(event);
                Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PLAY, SoundUtil.SOUND_ID_LOCK, SoundEvent.SOUND_TYPE_LOCK)");
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }


    @Override
    public void onHobRecipesFragmentSelect(int image, String foodTypeName) {
        switch (image) {
            case R.mipmap.food_type_cooking_table:
                showHobCookingTablesFragment();
                break;
            case R.mipmap.food_type_personalize:
                showHobMyRecipesFragment();
                break;
            default:
                showHobRecipesFoodDetailFragment(image, foodTypeName);
                break;
        }

        removeFragment(mHobRecipesFragment);
        mHobRecipesFragment = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK ) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onHobRecipesFragmentFinish() {
        hobMenuBar.notifyReleaseButton(HobMenuBar.NOTIFY_RELEASE_MENU);
        //showHobPanelFragment(null);
        showTFTHobPanelFragment(Hob60PanelFragment.HOB_PANEL_WORK_MODE_NORMAL);

        removeFragment(mHobRecipesFragment);
        mHobRecipesFragment = null;
    }

    @Override
    public void onHobCookingTablesFragmentSelect(int what) {
        switch (what) {
            case HobCookingTablesFragment.HOB_COOKING_TABLES_SELECT_MEAT:
                showHobCookingTablesDetailFragment(HobCookingTablesDetailFragment.COOKING_TABLES_DETAIL_TYPE_MEAT);
                break;
            case HobCookingTablesFragment.HOB_COOKING_TABLES_SELECT_LEGUMES_AND_CEREALS:
                showHobCookingTablesDetailFragment(HobCookingTablesDetailFragment.COOKING_TABLES_DETAIL_TYPE_LEGUMES_AND_CEREALS);

                break;
            case HobCookingTablesFragment.HOB_COOKING_TABLES_SELECT_FRUIT:
                showHobCookingTablesDetailFragment(HobCookingTablesDetailFragment.COOKING_TABLES_DETAIL_TYPE_FRUIT);

                break;
            case HobCookingTablesFragment.HOB_COOKING_TABLES_SELECT_SHELLFISH:
                showHobCookingTablesDetailFragment(HobCookingTablesDetailFragment.COOKING_TABLES_DETAIL_TYPE_SHELLFISH);

                break;
            case HobCookingTablesFragment.HOB_COOKING_TABLES_SELECT_VEGETABLES:
                showHobCookingTablesDetailFragment(HobCookingTablesDetailFragment.COOKING_TABLES_DETAIL_TYPE_VEGETABLES);

                break;
            case HobCookingTablesFragment.HOB_COOKING_TABLES_SELECT_FISH:
                showHobCookingTablesDetailFragment(HobCookingTablesDetailFragment.COOKING_TABLES_DETAIL_TYPE_FISH);

                break;
        }
    }

    @Override
    public void onHobCookingTablesFragmentFinish() {
        showHobRecipesFragment();
        //hideAndDisableMenuBar();

    }

    @Override
    public void onStatusBarClockTimerIsUp() {
        //CataTFTHobApplication.playAlarmSound();
        showTimerIsUpDialog();
        LogUtil.d("Enter:: onStatusBarClockTimerIsUp");
    }

    @Override
    public void onTimerSettingFragmentFinish(int from) {
        if (from == AlarmClockSettingFragment.REQUEST_TIMER_SETTING_HOD) {
            hobMenuBar.notifyReleaseButton(HobMenuBar.NOTIFY_RELEASE_TIMER);
            //showHobPanelFragment(null);
            showTFTHobPanelFragment(Hob60PanelFragment.HOB_PANEL_WORK_MODE_NORMAL);
        } else if (from == AlarmClockSettingFragment.REQUEST_TIMER_SETTING_HOOD) {
            hoodMenuBar.notifyReleaseButton(HoodMenuBar.NOTIFY_RELEASE_TIMER);
            showHoodFragment();
        }


    }

    @Override
    public void onTimerSettingFragmentRequestTimer(int from, int hour, int minute) {
        if (from == AlarmClockSettingFragment.REQUEST_TIMER_SETTING_HOD) {
            hobMenuBar.notifyReleaseButton(HobMenuBar.NOTIFY_RELEASE_TIMER);
            //showHobPanelFragment(null);
            showTFTHobPanelFragment(Hob60PanelFragment.HOB_PANEL_WORK_MODE_NORMAL);
        } else if (from == AlarmClockSettingFragment.REQUEST_TIMER_SETTING_HOOD) {
            hoodMenuBar.notifyReleaseButton(HoodMenuBar.NOTIFY_RELEASE_TIMER);
            showHoodFragment();
        }
        AlarmClockSettingDao dao = CataTFTHobApplication.getDaoSession().getAlarmClockSettingDao();
        AlarmClockSetting setting = new AlarmClockSetting(null, hour, minute, 0);
        dao.deleteAll();
        dao.insert(setting);
        sbctv.setAndStartTimer(hour, minute);
     //   closeAlarmWhenHasNewTimer();
    }

    @Override
    public void onTimerSettingFragmentCancel(int from) {
        if (from == AlarmClockSettingFragment.REQUEST_TIMER_SETTING_HOD) {
            hobMenuBar.notifyReleaseButton(HobMenuBar.NOTIFY_RELEASE_TIMER);
            //showHobPanelFragment(null);
            showTFTHobPanelFragment(Hob60PanelFragment.HOB_PANEL_WORK_MODE_NORMAL);
        } else if (from == AlarmClockSettingFragment.REQUEST_TIMER_SETTING_HOOD) {
            hoodMenuBar.notifyReleaseButton(HoodMenuBar.NOTIFY_RELEASE_TIMER);
            showHoodFragment();
        }
        clearTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseWakeLock();
        unbindService(mConnection);
        stopService(serviceIntent);
        unregisterBroadcast();
        CataTFTHobApplication.getInstance().AppExit();
    }

    @Override
    public void onHobCookingTablesDetailFragmentFinish() {
        showHobCookingTablesFragment();
    }

    @Override
    public void onHobCookingTablesDetailFragmentStartToCook(int temp, int hour, int minute) {  // C450 到 C457
        CookerSettingData data = new CookerSettingData();
        data.setTempValue(temp);
        data.setTimerMinuteValue(minute);
        data.setTimerHourValue(hour);
        data.setTimerSecondValue(59);
        data.setTempMode(CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR);
        showTFTHobPanelFragment(Hob60PanelFragment.HOB_PANEL_WORK_MODE_RECIPES_SELECT_COOKER_TO_COOK);
        mTFTHobPanelFragment.setRecipesSettingData(data);

    }

    @Override
    public void onHobCookingTablesDetailFragmentStartToCookNew(CookerSettingData data) {
     /*   if (mTFTHobService != null) mTFTHobService.notifyCookerUpdateSettingData(data);
        showTFTHobPanelFragment(Hob60PanelFragment.HOB_PANEL_WORK_MODE_NORMAL);*/

        showHobTemperatureModeSelectFragment(data);
    }

    @Override
    public void onSettingFragmentFinish() {
        //showHobPanelFragment(null);
        showTFTHobPanelFragment(Hob60PanelFragment.HOB_PANEL_WORK_MODE_NORMAL);
        hobMenuBar.notifyReleaseButton(HobMenuBar.NOTIFY_RELEASE_SETTING);
    }

    @Override
    public void onHob60PanelFragmentBindCooker(int cookerID) {
        Logger.getInstance().d("onHob60PanelFragmentBindCooker(" + cookerID + ")");
        if (mTFTHobService == null) {
            cookerIDs.add(cookerID);
        } else {
            mTFTHobService.bindCooker(cookerID);
        }

    }

    @Override
    public void onHob60PanelFragmentRequestSetCooker(CookerSettingData data) {
        Logger.getInstance().d("onHob60PanelFragmentRequestSetCooker(" + data.getCookerID() + ")");
        showHobCookerSettingFragment(data);
    }

    @Override
    public void onHob60PanelFragmentRequestCookerPowerOff(int cookerID) {
        Logger.getInstance().d("onHob60PanelFragmentRequestCookerPowerOff(" + cookerID + ")");
        if (cookerID == ptcCookerID) {
            ptcCookerID = 1;
        }
        if (mTFTHobService != null) {
            mTFTHobService.notifyCookerPowerOff(cookerID);
        }
    }

    @Override
    public void onHob60PanelFragmentRequestReadyToCook(int cookerID) {
        Logger.getInstance().d("onHob60PanelFragmentRequestReadyToCook(" + cookerID + ")");
        if (mTFTHobService != null) {
            mTFTHobService.notifyCookerReadyToCook(cookerID);
        }
    }

    @Override
    public void onHob60PanelFragmentRequestKeepWarm(int cookerID) {
        Logger.getInstance().d("onHob60PanelFragmentRequestKeepWarm(" + cookerID + ")");
        if (mTFTHobService != null) {
            mTFTHobService.notifyCookerKeepWarm(cookerID);
        }
    }

    @Override
    public void onHob60PanelFragmentRequestAddTenMinute(int cookerID) {
        Logger.getInstance().d("onHob60PanelFragmentRequestAddTenMinute(" + cookerID + ")");
        if (mTFTHobService != null) {
            mTFTHobService.notifyCookerAddTenMinute(cookerID);
        }
    }

    @Override
    public void onHob60PanelFragmentRequestSwitchToHoodPanel() {
        Logger.getInstance().d("onHob60PanelFragmentRequestSwitchToHoodPanel()");
        showHoodFragment();
    }

    @Override
    public void onHob60PanelFragmentRequestSelectCooker(CookerSettingData data) {
        Logger.getInstance().d("onHob60PanelFragmentRequestSelectCooker(" + data.getCookerID() + ")");
        if (mTFTHobService != null) mTFTHobService.notifyCookerUpdateSettingData(data);
        mTFTHobPanelFragment.setMode(Hob60PanelFragment.HOB_PANEL_WORK_MODE_NORMAL);

    }

    @Override
    public void onHob60PanelFragmentRequestAdjustCookerSettingDataToCook(CookerSettingData data) {
        Logger.getInstance().d("onHob60PanelFragmentRequestAdjustCookerSettingDataToCook(" + data.getCookerID() + ")");
        showHobCookerSettingFragment(data);
    }

    @Override
    public boolean getTimerOfHoodStatus() {
        return mTimerOfHoodIsWorking;
    }

    @Override
    public void onHob60PanelFragmentRequestSaveGearValue(int fanGearSend, int lightGearSend) {
        Logger.getInstance().d("onHob60PanelFragmentRequestSaveGearValue(" + fanGearSend + ", " + lightGearSend + ")");
        DatabaseHelper.saveHoodData(fanGearSend, lightGearSend);
    }

    @Override
    public void onHobRecipesFoodDetailFragmentFinish() {
        showHobRecipesFragment();

        removeFragment(mHobRecipesFoodDetailFragment);
        mHobRecipesFoodDetailFragment = null;
    }

    @Override
    public void OnHobRecipesFoodDetailFragmentRequestStartToCook(CookerSettingData data) { // c102 到 C30
        showHobTemperatureModeSelectFragment((CookerSettingData) data.clone());

        removeFragment(mHobRecipesFoodDetailFragment);
        mHobRecipesFoodDetailFragment = null;
    }

/*    @Override
    public void OnHobRecipesFoodDetailFragmentRequestAdjust(CookerSettingData data) {
        showTFTHobPanelFragment(Hob60PanelFragment.HOB_PANEL_WORK_MODE_RECIPES_ADJUST_TO_COOK);
        mTFTHobPanelFragment.setRecipesSettingData(data);
    }

    @Override
    public void OnHobRecipesFoodDetailFragmentRequestRelateRecipes(int type) {
        showHobRelatedRecipesFragment(type);
    }*/

    @Override
    public void onHobMyRecipesFragmentNewRecipes() {
        //showHobRecipesFragment();
        showHobMyRecipesEditFragment(null);
    }

    @Override
    public void onHobMyRecipesFragmentFinish() {
        showHobRecipesFragment();
    }

    @Override
    public void onHobMyRecipesFragmentRequestToCheckDetail(MyRecipesTable myRecipesTable) {
        showHobMyRecipesEditFragment(myRecipesTable);
    }

    @Override
    public void onHobRecipesEditFragmentFinish(int type, String content) {
        hideBottomUIMenu();
        showHobMyRecipesEditFragment(type, content);
    }

    @Override
    public void onHobRecipesEditFragmentCancel() {
        hideBottomUIMenu();

        // 通过设定为 RECIPES_EDIT_TYPE_CHOOSE_TEMPERATURE_MODE，以使其不改变任何现有值
        showHobMyRecipesEditFragment(
                HobMyRecipesEditFragment.RECIPES_EDIT_TYPE_CHOOSE_TEMPERATURE_MODE,
                "");
    }

    @Override
    public void onHobMyRecipesEditFragmentFinish(CookerSettingData data) {
        if (data == null) {
            showHobMyRecipesFragment();
        } else {
           /* if (mTFTHobService != null) mTFTHobService.notifyCookerUpdateSettingData(data);
            showTFTHobPanelFragment(Hob60PanelFragment.HOB_PANEL_WORK_MODE_NORMAL);*/


            showHobTemperatureModeSelectFragment(data);
        }
    }

    @Override
    public void onHobMyRecipesEditFragmentRequestToSet(int type, MyRecipesTable initData) {
        showHobRecipesEditFragment(type, initData);
    }

    @Override
    public void onHob90PanelFragmentBindCooker(int cookerID) {
        if (mTFTHobService == null) {
            cookerIDs.add(cookerID);
        } else {
            mTFTHobService.bindCooker(cookerID);
        }
    }

    @Override
    public void onHob90PanelFragmentRequestSetCooker(CookerSettingData data) {
        showHobCookerSettingFragment(data);
    }

    @Override
    public void onHob90PanelFragmentRequestCookerPowerOff(int cookerID) {
        if (cookerID == ptcCookerID) {
            ptcCookerID = -1;
        }
        if (mTFTHobService != null) {
            mTFTHobService.notifyCookerPowerOff(cookerID);
        }
    }

    @Override
    public void onHob90PanelFragmentRequestReadyToCook(int cookerID) {
        if (mTFTHobService != null) {
            mTFTHobService.notifyCookerReadyToCook(cookerID);
        }
    }

    @Override
    public void onHob90PanelFragmentRequestKeepWarm(int cookerID) {
        if (mTFTHobService != null) {
            mTFTHobService.notifyCookerKeepWarm(cookerID);
        }
    }

    @Override
    public void onHob90PanelFragmentRequestAddTenMinute(int cookerID) {
        if (mTFTHobService != null) {
            mTFTHobService.notifyCookerAddTenMinute(cookerID);
        }
    }

    @Override
    public void onHob90PanelFragmentRequestSwitchToHoodPanel() {
        showHoodFragment();
    }

    @Override
    public void onHob90PanelFragmentRequestSelectCooker(CookerSettingData data) {
        if (mTFTHobService != null) mTFTHobService.notifyCookerUpdateSettingData(data);
        mTFTHobPanelFragment.setMode(Hob60PanelFragment.HOB_PANEL_WORK_MODE_NORMAL);
    }

    @Override
    public void onHob90PanelFragmentRequestAdjustCookerSettingDataToCook(CookerSettingData data) {
        showHobCookerSettingFragment(data);
    }

    @Override
    public void onHob80PanelFragmentBindCooker(int cookerID) {
        if (mTFTHobService == null) {
            cookerIDs.add(cookerID);
        } else {
            mTFTHobService.bindCooker(cookerID);
        }
    }

    @Override
    public void onHob80PanelFragmentRequestSetCooker(CookerSettingData data) {
        showHobCookerSettingFragment(data);
    }

    @Override
    public void onHob80PanelFragmentRequestCookerPowerOff(int cookerID) {
        if (cookerID == ptcCookerID) {
            ptcCookerID = -1;
        }
        if (mTFTHobService != null) {
            mTFTHobService.notifyCookerPowerOff(cookerID);
        }
    }

    @Override
    public void onHob80PanelFragmentRequestReadyToCook(int cookerID) {
        if (mTFTHobService != null) {
            mTFTHobService.notifyCookerReadyToCook(cookerID);
        }
    }

    @Override
    public void onHob80PanelFragmentRequestKeepWarm(int cookerID) {
        if (mTFTHobService != null) {
            mTFTHobService.notifyCookerKeepWarm(cookerID);
        }
    }

    @Override
    public void onHob80PanelFragmentRequestAddTenMinute(int cookerID) {
        if (mTFTHobService != null) {
            mTFTHobService.notifyCookerAddTenMinute(cookerID);
        }
    }

    @Override
    public void onHob80PanelFragmentRequestSwitchToHoodPanel() {
        showHoodFragment();
    }

    @Override
    public void onHob80PanelFragmentRequestSelectCooker(CookerSettingData data) {
        if (mTFTHobService != null) mTFTHobService.notifyCookerUpdateSettingData(data);
        mTFTHobPanelFragment.setMode(Hob80PanelFragment.HOB_PANEL_WORK_MODE_NORMAL);
    }

    @Override
    public void onHob80PanelFragmentRequestAdjustCookerSettingDataToCook(CookerSettingData data) {
        showHobCookerSettingFragment(data);
    }

    @Override
    public void onTempCtrlFragmentFinish() {

    }

    private void doStopCountDown() {
        if (mDisposable != null) {
            if (!mDisposable.isDisposed()) mDisposable.dispose();
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SettingTestCookDataEvent event) {
        int order;
        order = event.getOrder();
        switch (order) {
            case TFTHobConstant.cooker_power_on:
                SetTestCookData(event.getCookerID());
                break;
            case TFTHobConstant.cooker_power_off:
                if (event.getCookerID() == ptcCookerID) {
                    ptcCookerID = -1;
                }
                if (mTFTHobService != null) {
                    mTFTHobService.notifyCookerPowerOff(event.getCookerID());
                }
                break;
        }

    }

    private void SetTestCookData(int cookerID) {
        CookerSettingData data = new CookerSettingData();
        data.setCookerID(cookerID);
        data.setCookerSettingMode(0);
        data.setTempMode(-1);
        data.setTempValue(-1);
        data.setTempIdentifyDrawableResourceID(-1);
        data.setFireValue(1);  // 开一档
        data.setTimerHourValue(-1);
        data.setTimerMinuteValue(-1);
        data.setTimerSecondValue(-1);

        if (mTFTHobService != null) mTFTHobService.notifyCookerUpdateSettingData(data);
    }

    private void clearTimer() {
        AlarmClockSettingDao dao = CataTFTHobApplication.getDaoSession().getAlarmClockSettingDao();
        dao.deleteAll();
        sbctv.closeTimer();
    }

    private void showErrorMessage(int id, String title, String content) {
        showErrorMessage(id, title, content, R.mipmap.background_error_dialog);
    }
    private void showErrorMessage(
            int id,
            String title,
            String content,
            int background) {

        if (errorDialog == null) {
            errorDialog = new ConfirmDialog(this);
            errorDialog.setListener(this);
        }
        if (!errorDialog.isShowing()) {
            errorDialog.setDialogBackground(background);
            errorDialog.setOKImage(R.mipmap.dialog_ok_hood_connectivity);
            errorDialog.showCancelIcom(false);
            errorDialog.setMessagePlace(ConfirmDialog.DEFAULT_POSTION, 700);
            errorDialog.show();
        }

        if (errorId != id || !errorTitle.equals(title)) {
            String msg = title;
            if (content != null && !content.equals("")) {
                msg += "\n" + content;
            }
            errorDialog.setMessage(msg);
            errorTitle = title;
            errorId = id;
        }
    }

    private void hideErrorMessage() {
        if (errorDialog != null && errorDialog.isShowing()) {
            errorDialog.dismiss();
            errorId = -1;
            errorTitle = "";
            CataTFTHobApplication.getInstance().setShowingErrER03(false);
        }
    }

    private void showTotalTurnOffFrame() {
        if (currentFragment == mHobIntroFragment
                && !mHobIntroFragment.isShowingBlackMask()) {
            mHobIntroFragment.showBlackMask(true); // 打开黑色的幕
            com.ekek.settingmodule.utils.SoundUtil.setSystemMute(this);
            mHobIntroFragment.setRespondPowerOnSignal(true);
            mHobIntroFragment.setTotalPowerOffMode(HobIntroFragment.TOTAL_POWER_OFF_NOW);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SendOrderForFirstSwitchOn event) {
        switch (event.getOrder()) {
            case CataSettingConstant.ShowMainWorkSpace:
                // 第一次开机初始化完成后，进入待机或关机页面
                boolean hibernationEnabled = GlobalVars.getInstance().isHibernationModeEnabled();
                int mode = hibernationEnabled ? HobIntroFragment.TOTAL_POWER_OFF_NONE : HobIntroFragment.TOTAL_POWER_OFF_NOW;
                boolean respondPowerOnSinal = hibernationEnabled ? false : true;
                boolean clickToQuit = hibernationEnabled ? true : false;
                int enterPowerOffMode = hibernationEnabled ? CataSettingConstant.EnterHobIntroFragment : CataSettingConstant.EnterPowerOffModel;
                showHobIntroFragment(respondPowerOnSinal, clickToQuit, mode, true);
                SettingPreferencesUtil.saveEnterPowerOffMode(this, enterPowerOffMode);
                mSettingFragment = null; // 第一次设置语言、时间及日期后，重新启动一个设置界面。（可避免在时间界面上点击右箭头后，出现信息图标。）
                rightBack.setVisibility(View.INVISIBLE);  // 右向的箭头，不可见。（语言设置、时间及日期设置界面用）
                break;
            case CataSettingConstant.SetLanguage:
                SettingPreferencesUtil.saveEnterPowerOffMode(this, CataSettingConstant.EnterLanguageSettingFragment);
                //   LogUtil.d("liang save enter the language ~~~");
             //   mSettingStatus=CataSettingConstant.SetLanguage;
                break;
            case CataSettingConstant.SetDate:
                SettingPreferencesUtil.saveEnterPowerOffMode(this, CataSettingConstant.EnterDateSettingFragment);
                //   LogUtil.d("liang save enter the date setting ~~~");
            //    mSettingStatus=CataSettingConstant.SetDate;
             //   rightBack.setVisibility(View.VISIBLE);
                rightBack.setVisibility(View.INVISIBLE);
                break;
            case CataSettingConstant.SetTime:
                SettingPreferencesUtil.saveEnterPowerOffMode(this, CataSettingConstant.EnterTimeSettingFragment);
             //   mSettingStatus=CataSettingConstant.SetTime;
                //  LogUtil.d("liang save enter the tiem setting  ~~~");
              //  rightBack.setVisibility(View.VISIBLE);
                rightBack.setVisibility(View.INVISIBLE);
                break;
            case CataSettingConstant.is12Format:   // 在date and time 界面修改时钟格式后， 更新 时钟显示格式
                sbctv.setTimeFormat24(false);
                //    LogUtil.d("Liang is 24 ");
                break;
            case CataSettingConstant.is24Format:
                sbctv.setTimeFormat24(true);
                // LogUtil.d("Liang is 12 ");
                break;
            case CataSettingConstant.FlashTheTimeOfTouchUI:
                 // 重新刷 最后一次触摸时间
                CataTFTHobApplication.getInstance().updateLatestTouchTime();
                handler.sendEmptyMessageDelayed(HANDLER_UPDATE_TIME_AFTER_UPDATA_TIME,2000 );

                break;
            case CataSettingConstant.SetIdle:
                break;

        }
    }

    private int getCurrentIntroStatus() {
        if (currentFragment == mHobIntroFragment && mHobIntroFragment != null) {
            if (mHobIntroFragment.isShowingBlackMask()) {
                return INTRO_STATUS_TOTAL_OFF;
            } else {
                return INTRO_STATUS_HIBERNATE;
            }
        } else {
            return INTRO_STATUS_NONE;
        }
    }

    @Override
    public void onHobTemperatureModeSelectFragmentFinish() {

    }

    @Override
    public void OnHobTemperatureModeSelectFragmentRequestStartToCook(CookerSettingData data) {
        if (mTFTHobService != null) mTFTHobService.notifyCookerUpdateSettingData(data);
        showTFTHobPanelFragment(Hob60PanelFragment.HOB_PANEL_WORK_MODE_NORMAL);
    }

    private class CheckingThread extends Thread {

        boolean forceToStop;
        boolean eventHibernateHasBeenHandled = false;
        boolean eventTotalPowerOffHasBeenHandled = false;
        boolean eventTimeoutDuringInitializingProcessHasBeenHandled = false;

        CheckingThread() {

        }

        @Override
        public void run() {
            while (!forceToStop) {
                try {
                    if (SystemClock.elapsedRealtime() - lastTimeGotSerialData > 10 * 1000) {
                        // 10 秒钟没有收到串口数据，则抛出串口异常的事件
                        handler.sendEmptyMessage(HANDLER_SERIAL_DATA_EXCEPTION);
                    }
                    if (!GlobalVars.getInstance().isInitializeProcessComplete()) {
                        // 如果初始化进程还未结束，则不进行计时
                        CataTFTHobApplication.getInstance().updateLatestTouchTime();
                    }

                    if (!CataTFTHobApplication.getInstance().isShowingErrER03()
                            && GlobalVars.getInstance().isShowingPauseHobDialog()) {
                        // 暂停状态下不进行计时
                        CataTFTHobApplication.getInstance().updateLatestTouchTime();
                    }

                    if (GlobalVars.getInstance().isWaitingToUpdate()) {
                        // 等待更新时，不进行计时
                        CataTFTHobApplication.getInstance().updateLatestTouchTime();
                    }

                    if (GlobalVars.getInstance().isHibernationModeEnabled()) {
                        // 休眠模式启用，totalTurnOffDuration 和 activationDuration 均有效，前者一定比后者大
                        if (getCurrentIntroStatus() == INTRO_STATUS_TOTAL_OFF) {
                            // 如果已经进入黑屏状态，则不进行计时
                            CataTFTHobApplication.getInstance().updateLatestTouchTime();
                        }

                        if (CataTFTHobApplication.getInstance().checkNoTouch(totalTurnOffDuration * 1000)) {
                            if (!eventTotalPowerOffHasBeenHandled) {
                                // 进入完全黑屏模式
                                EventBus.getDefault().post(new IdleEvent(IdleEvent.IDLE_TOTAL_POWER_OFF));
                                eventTotalPowerOffHasBeenHandled = true;
                            }
                        } else if (CataTFTHobApplication.getInstance().checkNoTouch(activationDuration * 1000)) {
                            if (!eventHibernateHasBeenHandled) {
                                // 进入时钟待机页面
                                EventBus.getDefault().post(new IdleEvent(IdleEvent.IDLE_HIBERNATION));
                                eventHibernateHasBeenHandled = true;
                            }
                        }  else {
                            eventHibernateHasBeenHandled = false;
                            eventTotalPowerOffHasBeenHandled = false;
                        }

                    } else {
                        // 休眠模式未启用，只有 activationDuration 有效，但此值代表的是进入完全黑屏的 Idle 时间
                        if (CataTFTHobApplication.getInstance().checkNoTouch(activationDuration * 1000)) {
                            if (!eventTotalPowerOffHasBeenHandled) {
                                EventBus.getDefault().post(new IdleEvent(IdleEvent.IDLE_TOTAL_POWER_OFF));
                                eventTotalPowerOffHasBeenHandled = true;
                            }
                        }  else {
                            eventTotalPowerOffHasBeenHandled = false;
                        }
                    }

                    int enterPowerOffMode = SettingPreferencesUtil.getEnterPowerOffMode(getApplicationContext());
                    if (enterPowerOffMode == CataSettingConstant.EnterLanguageSettingFragment
                            || enterPowerOffMode == CataSettingConstant.EnterDateSettingFragment
                            || enterPowerOffMode == CataSettingConstant.EnterTimeSettingFragment) {
                        // 如果当前是处在初始化过程中的三个页面，那么 5 分钟不操作时，则抛出 IDLE_TIMEOUT_DURING_INITIALIZING_PROCESS 事件
                        mCountdown5Minute++;
                        if (mCountdown5Minute >= 5 * 5 * 60) {
                            if (!eventTimeoutDuringInitializingProcessHasBeenHandled) {
                                EventBus.getDefault().post(new IdleEvent(IdleEvent.IDLE_TIMEOUT_DURING_INITIALIZING_PROCESS));
                                eventTimeoutDuringInitializingProcessHasBeenHandled = true;
                            }
                        } else {
                            eventTimeoutDuringInitializingProcessHasBeenHandled = false;
                        }
                    } else {
                        mCountdown5Minute = 0;
                    }

                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class WaitCoolDownThread extends Thread {

        boolean forceToStop;

        WaitCoolDownThread() {

        }

        @Override
        public void run() {
            while (!forceToStop) {
                try {
                    if (!hasCookerObnormalHighTemp) {
                        EventBus.getDefault().post(new IdleEvent(IdleEvent.IDLE_TOTAL_POWER_OFF_COOLED_DOWN));
                        break;
                    }
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent5(PlaySoundWhenSetTime event) {
        EventBus.getDefault().post(new SoundEvent(
                SoundEvent.SOUND_ACTION_PLAY,
                SoundUtil.SOUND_ID_SCROLL,
                SoundEvent.SOUND_TYPE_SCROLL));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ShowBannerInformation event) {
        int order;
        order = event.getOrder();
        switch (order) {
            case CataSettingConstant.DEFAULT_SETTINGS_SUCCESSFULLY:
                showDefaultSettingsSuccessfullyDialog();
                break;
            case CataSettingConstant.HOOD_CONNECTIVITY:
                HoodData hoodData = DatabaseHelper.getHoodData();  //
                int light=hoodData.getLightGear();
                DatabaseHelper.saveHoodData(0, light);
                showHoodConnectivityDialog();
                LogUtil.d("liang show hood connectivity dialog 1");
                break;
            case CataSettingConstant.DELETE_MY_RECIPE:
                showDeleteMyRecipeDialog(event.getwParam());
                break;
        }


    }

    private void showDeleteMyRecipeDialog(int position) {
        if (deleteMyRecipeDialog == null) {
            deleteMyRecipeDialog = new ConfirmDialog(this);
            deleteMyRecipeDialog.setListener(this);
        }
        deleteMyRecipeDialog.setUserParam(position);
        if (!deleteMyRecipeDialog.isShowing()) {
            deleteMyRecipeDialog.setDialogBackground(R.mipmap.background_delete_recipe_dialog);
            deleteMyRecipeDialog.setMessage(R.string.tfthobmodule_fragment_my_recipes_confirm_delete_recipe);
            deleteMyRecipeDialog.setOKImage(R.mipmap.dialog_ok_hood_connectivity);
            deleteMyRecipeDialog.showCancelIcom(true);
            deleteMyRecipeDialog.setCancelImage(R.mipmap.dialog_cancel_hood_connectivity);
            deleteMyRecipeDialog.setMessagePlace(ConfirmDialog.DEFAULT_POSTION, 760);
            deleteMyRecipeDialog.show();
        }
    }

    private void hideDeleteMyRecipeDialog() {
        if (deleteMyRecipeDialog != null && deleteMyRecipeDialog.isShowing()) {
            deleteMyRecipeDialog.dismiss();
        }
    }

    private void showHoodConnectivityDialog() {  //hoodConnectivityDialog
        if (hoodConnectivityDialog == null) {
            hoodConnectivityDialog = new ConfirmDialog(this);
            hoodConnectivityDialog.setListener(this);
        }
        if (!hoodConnectivityDialog.isShowing()) {
            hoodConnectivityDialog.setDialogBackground(R.mipmap.ic_banner_hood_connectivity);
            hoodConnectivityDialog.setOKImage(R.mipmap.dialog_ok_hood_connectivity);
            hoodConnectivityDialog.showCancelIcom(true);
            hoodConnectivityDialog.setCancelImage(R.mipmap.dialog_cancel_hood_connectivity);
            hoodConnectivityDialog.setMessagePlace(ConfirmDialog.DEFAULT_POSTION, 720);
            switch (GlobalVars.getInstance().getCurrentLocale().toString().toLowerCase()) {
                case "el":
                case "ru":
                    hoodConnectivityDialog.setMessageTextSize(36.0f);
                    break;
                default:
                    hoodConnectivityDialog.setMessageTextSize(40.0f);
                    break;
            }

            hoodConnectivityDialog.setMessage(R.string.settingmodule_fragment_setting_hood_options_content_dialog);
            hoodConnectivityDialog.show();
            // sensorWarningDialogShown = SystemClock.elapsedRealtime();
            LogUtil.d("liang show hood connectivity dialog 3");

        } else {

        }

    }

    private void hideHoodConnectivityDialog() {
        if (hoodConnectivityDialog != null && hoodConnectivityDialog.isShowing()) {
            hoodConnectivityDialog.dismiss();
            //  hideBottomUIMenu();
            //notifyCookerStartWork();
        }

    }

    private void showExternalSensorNotDetectedDialog(int source) {
        hideExternalSensorNotDetectedDialog();
        externalSensorNotDetectedDialog = new ConfirmDialog(this);
        externalSensorNotDetectedDialog.setListener(this);
        externalSensorNotDetectedDialog.setDialogBackground(R.mipmap.background_fast_mode_warning_dialog);
        externalSensorNotDetectedDialog.setOKImage(R.mipmap.dialog_ok_hood_connectivity);
        externalSensorNotDetectedDialog.showCancelIcom(false);
        externalSensorNotDetectedDialog.setMessagePlace(ConfirmDialog.DEFAULT_POSTION, 720);
        int msg = R.string.settingmodule_no_external_sensor_detected;
        if (source == NoExternalSensorDetectedEvent.SOURCE_HOB_TEMPERATURE_SENSOR_DISCONNECT) {
            msg = R.string.tfthobmodule_err_r4e;
        }
        externalSensorNotDetectedDialog.setMessage(msg);
        externalSensorNotDetectedDialog.setUserParam(source);
        externalSensorNotDetectedDialog.show();
    }

    private void hideExternalSensorNotDetectedDialog() {
        if (externalSensorNotDetectedDialog != null) {
            if (externalSensorNotDetectedDialog.isShowing()) {
                externalSensorNotDetectedDialog.dismiss();
            }
            externalSensorNotDetectedDialog = null;
        }
    }

    private void showDefaultSettingsSuccessfullyDialog() {
        if (defaultSettingsSuccessfullyDialog == null) {
            defaultSettingsSuccessfullyDialog = new ConfirmDialog(this);
            defaultSettingsSuccessfullyDialog.setListener(this);
        }


        if (!defaultSettingsSuccessfullyDialog.isShowing()) {
            defaultSettingsSuccessfullyDialog.setMessage(R.string.settingmodule_fragment_setting_default_config_message_setting_successful);
            defaultSettingsSuccessfullyDialog.show();
            // sensorWarningDialogShown = SystemClock.elapsedRealtime();
        } else {

        }
    }

    private void hideDefaultSettingsSuccessfullyDialog() {
        if (defaultSettingsSuccessfullyDialog != null && defaultSettingsSuccessfullyDialog.isShowing()) {
            defaultSettingsSuccessfullyDialog.dismiss();
            //  hideBottomUIMenu();
            //notifyCookerStartWork();
        }
    }


    @Override
    public void onConfirm(ConfirmDialog dialog, int action) {
        if (dialog == defaultSettingsSuccessfullyDialog) {
            switch (action) {
                case ConfirmDialog.ACTION_OK:
                    // defaultSettingsSuccessfullyDialog.dismiss();
                    //sensorWarningDialogShown = 0;
                    //  showHoodFragmentReset();
                    hideDefaultSettingsSuccessfullyDialog();
                    EventBus.getDefault().post(new SettingDoBack(CataSettingConstant.do_back));
                    // hideBottomUIMenu();
                    break;
            }
        } else if (dialog == hoodConnectivityDialog) {
            switch (action) {
                case ConfirmDialog.ACTION_OK:
                    hideHoodConnectivityDialog();

                   /* HoodData hoodData = DatabaseHelper.getHoodData();  //
                    int light=hoodData.getLightGear();
                    DatabaseHelper.saveHoodData(0, light);*/

                    //    EventBus.getDefault().post(new SettingDoBack(CataSettingConstant.do_back));
                    break;
                case ConfirmDialog.ACTION_CANCEL:
                    hideHoodConnectivityDialog();
                    EventBus.getDefault().post(new SendOrderRecoverHoodConnectivity(CataSettingConstant.CONNECTED_RECOVER));
                    break;
            }
        } else if (dialog == deleteMyRecipeDialog) {
            switch (action) {
                case ConfirmDialog.ACTION_OK:
                    hideDeleteMyRecipeDialog();
                    mHobMyRecipesFragment.deleteRecipe(dialog.getUserParam());
                    break;
                case ConfirmDialog.ACTION_CANCEL:
                    hideDeleteMyRecipeDialog();
                    break;
            }
        } else if (dialog == externalSensorNotDetectedDialog) {
            switch (externalSensorNotDetectedDialog.getUserParam()) {
                case NoExternalSensorDetectedEvent.SOURCE_HOB_COOKER_SETTING_FRAGMENT:
                    if (mHobCookerSettingFragment != null) {
                        mHobCookerSettingFragment.setIdleCheckingPaused(false);
                    }
                    break;
                case NoExternalSensorDetectedEvent.SOURCE_HOB_TEMPERATURE_SENSOR_DISCONNECT:
                    EventBus.getDefault().post(new SoundEvent(
                            SoundEvent.SOUND_ACTION_PAUSE,
                            SoundUtil.SOUND_ID_ALARM,
                            SoundEvent.SOUND_TYPE_COMMON_ALARM));
                    break;
            }
            hideExternalSensorNotDetectedDialog();
        } else if (dialog == errorDialog) {
            switch (action) {
                case ConfirmDialog.ACTION_OK:
                    closeErrorDialog();
                    break;
            }
        }
    }

    private void closeErrorDialog() {
        hideErrorMessage();
        EventBus.getDefault().post(new SoundEvent(
                SoundEvent.SOUND_ACTION_PAUSE,
                SoundUtil.SOUND_ID_ALARM,
                SoundEvent.SOUND_TYPE_COMMON_ALARM));
        EventBus.getDefault().post(new SoundEvent(
                SoundEvent.SOUND_ACTION_PAUSE,
                SoundUtil.SOUND_ID_ALARM_ONCE,
                SoundEvent.SOUND_TYPE_ALARM_ONCE));
        switch (errorId) {
            case ShowErrorEvent.ERROR_R2E:
                r2eErrorHandled = true;
                break;
            case ShowErrorEvent.ERROR_R3E:
                r3eErrorHandled = true;
                break;
            case ShowErrorEvent.ERROR_R4E:
                break;
            case ShowErrorEvent.ERROR_R5E:
                r5eErrorHandled = true;
                break;
            case ShowErrorEvent.ERROR_R6E:
                stopR6eErrorAssistThread();
                r6eErrorHandled = true;
                break;
            default:
                if (errorCookerId != -1) {
                    mTFTHobService.notifyCookerPlay(errorCookerId);
                    errorCookerId = -1;
                }
                break;
        }
    }

    private boolean isInsideTheCircle(float x, float y) {
        boolean ReturnValue = false;

        if (x <= alarmClockBackgroundEndX && x >= alarmClockBackgroundStartX && y <= alarmClockBackgroundEndY && y >= alarmClockBackgroundStartY) {
            ReturnValue = true;
        }

        return ReturnValue;
    }

    private boolean isActiveLenght(float xStar, float xEnd, float yStart, float yEnd) {
        boolean ReturnValue = false;
        double x = Math.abs(xStar - xEnd);
        double y = Math.abs(yStart - yEnd);
 //       LogUtil.d("liang x is = " + x + " y is = " + y);
        if (x <= movingLength && y <= movingLength) {
            ReturnValue = true;
        }

        return ReturnValue;
    }

    private boolean isClickSoundOpen() {
        boolean flag = false;
        boolean soundSwitchStatus = SettingPreferencesUtil.getSoundSwitchStatus(this).equals(CataSettingConstant.SOUND_SWITCH_STATUS_OPEN) ? true : false;
        boolean clickSoundSwitchStatus = SettingPreferencesUtil.getClickSoundSwitchStatus(this).equals(CataSettingConstant.CLICK_SOUND_SWITCH_STATUS_OPEN);
        if (soundSwitchStatus && clickSoundSwitchStatus) {
            flag = true;
        }
        return flag;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AlarmClockIsEditedEvent event) {
        mAlarmIsEdited = event.isOrder();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ScreenEvent event) {
        int action = event.getAction();
        if (action == ScreenEvent.ACTION_SCREEN_ON) {
            enableBT();
            //mTFTHobService.recoverConnectForBT();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SetLanguage event) {
        int order;
        order = event.getOrder();
        switch (order) {
            case CataSettingConstant.SET_LANGUAGE_SUCCESSFUL:
                //    SettingLanguage();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SendOrderTo event) {
        int order;
        order = event.getOrder();
        switch (order) {
            case CataSettingConstant.SetLanguage:
                mSettingStatus= CataSettingConstant.SetLanguage;
                SettingPreferencesUtil.saveEnterPowerOffMode(this, CataSettingConstant.EnterLanguageSettingFragment);
                break;
            case CataSettingConstant.SetDate:
                mSettingStatus= CataSettingConstant.SetDate;
                SettingPreferencesUtil.saveEnterPowerOffMode(this, CataSettingConstant.EnterDateSettingFragment);
                break;
            case CataSettingConstant.SetTime:
                mSettingStatus= CataSettingConstant.SetTime;
                SettingPreferencesUtil.saveEnterPowerOffMode(this, CataSettingConstant.EnterTimeSettingFragment);
                break;

        }
    }

    private Bitmap getBitmap(int source) {
        if (bitmapMap.containsKey(source)) {
            return bitmapMap.get(source);
        }

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), source);
        bitmapMap.put(source, bitmap);
        return bitmap;
    }

    private class TimerIsUpDialogBlinkThread extends BaseThread {

        private int alphaValue;
        private int direction;

        @Override
        protected boolean started() {
            alphaValue = 255;
            direction = 0;
            sendMessage();
            return true;
        }

        @Override
        protected boolean performTaskInLoop() {
            if (direction == 0) {
                alphaValue = alphaValue - 10;
                if (alphaValue < 110) {
                    alphaValue = 110;
                    direction = 1;
                }
            }else {
                alphaValue = alphaValue + 10;
                if (alphaValue > 255) {
                    alphaValue = 255;
                    direction = 0;
                }
            }
            sendMessage();
            return true;
        }

        @Override
        protected void finished() {
            alphaValue = 255;
            direction = 0;
            sendMessage();
        }

        private void sendMessage() {
            Message msg = new Message();
            msg.what = HANDLER_BLINK_TIMER_IS_UP_DIALOG;
            msg.arg1 = alphaValue;
            handler.sendMessage(msg);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BleTempEvent event){
        int realTempValue = event.getTempValue();
        if(realTempValue>205){  // 显示 ER03  , R5E 错误
            if (!r5eErrorHandled) {
                ShowErrorEvent order = new ShowErrorEvent(ShowErrorEvent.ERROR_R5E);
                EventBus.getDefault().post(order);
                r5eErrorHandled = true;
            }
        } else if (realTempValue <= 200) {
            r5eErrorHandled = false;
        }

        if (ptcCookerID != lastPtcCookerID) {
            lastPtcCookerID = ptcCookerID;
            if (ptcCookerID != -1) {
                // 刚刚开始工作
                ptcCookerStartTime = SystemClock.elapsedRealtime();
                ptcCookerStartTemp = realTempValue;
                r6eErrorHandled = false;
            } else {
                // 工作刚刚结束
                r6eErrorHandled = false;
            }
        } else {
            if (ptcCookerID != -1) {
                // 工作中
                long elapsedTime = SystemClock.elapsedRealtime() - ptcCookerStartTime;
                int checkTemp = 1;
                int checkDuration1 = 210 * 1000;
                int checkDuration2 = 150 * 1000;
                if (ptcTargetTempValue >= 100) {
                    checkTemp = 3;
                    checkDuration1 = 60 * 1000;
                    checkDuration2 = 60 * 1000;
                }

                if (realTempValue < ptcCookerStartTemp) {
                    // 如果蓝牙温度计刚从一个比较热的锅里拿出来，放入新的锅里时，蓝牙检测的温度刚开始可能会下降
                    ptcCookerStartTemp = realTempValue;
                }

                if (realTempValue - ptcCookerStartTemp < checkTemp) {
                    if (elapsedTime > checkDuration1) {
                        if (!r6eErrorHandled) {
                            ShowErrorEvent order = new ShowErrorEvent(ShowErrorEvent.ERROR_R6E);
                            EventBus.getDefault().post(order);
                            startR6eErrorAssistThread(checkDuration2);
                            r6eErrorHandled = true;
                        }
                    }
                }

            } else {
                // 未工作
                r6eErrorHandled = false;
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CookerUpdateEvent event) {
        if (!event.isLatestBatch()) {
            // 不处理过时的更新请求
            return;
        }

        if (!GlobalVars.getInstance().isHandlePowerOffAllCooker()) {
            //   GlobalVars.getInstance().setHandlePowerOffAllCooker(true);
            return;
        }

        int workMode = event.getWorkMode();
        boolean working = workMode != TFTHobConstant.HOB_VIEW_WORK_MODE_POWER_OFF
                && workMode != TFTHobConstant.HOB_VIEW_WORK_MODE_PAUSE
                && workMode != TFTHobConstant.HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP;
        if (working && !r2eErrorHandled && event.getTempMode() == TFTHobConstant.TEMP_MODE_PRECISE_TEMP) {
            ptcCookerID = event.getCookerID();
            ptcTargetTempValue = event.getTempValue();
            int totalMinutes = event.getHourValue() * 60 + event.getMinuteValue();
            Logger.getInstance().d("batteryLevel:" + batteryLevel + " maxUsableTime:" + maxUsableMinutes / 60 + ":" + maxUsableMinutes % 60 + " requiredTime:" + totalMinutes / 60 + ":" + totalMinutes % 60);
            if (batteryLevel < BleBatteryEvent.BATTERY_LEVEL_ALERT && totalMinutes > maxUsableMinutes) {
                if (errorId != ShowErrorEvent.ERROR_R2E && (errorDialog == null || !errorDialog.isShowing())) {
                    EventBus.getDefault().post(new ShowErrorEvent(ShowErrorEvent.ERROR_R2E));
                    r2eErrorHandled = true;
                    r2eErrorCookerId = event.getCookerID();
                }
            }
        }
        if (!working && event.getCookerID() == r2eErrorCookerId) {
            r2eErrorHandled = false;
        }
        if (event.getCookerID() == ptcCookerID) {
            if (!working || event.getTempMode() != TFTHobConstant.TEMP_MODE_PRECISE_TEMP) {
                ptcCookerID = -1;
            }
        }
    }

    private void startR6eErrorAssistThread(int checkDuration) {
        stopR6eErrorAssistThread();

        r6eErrorAssistThread = new R6eErrorAssistThread(checkDuration);
        r6eErrorAssistThread.start();
    }
    private void stopR6eErrorAssistThread() {
        if (r6eErrorAssistThread != null) {
            if (r6eErrorAssistThread.isAlive()) {
                r6eErrorAssistThread.setCancelTask(true);
                try{
                    r6eErrorAssistThread.join();
                } catch (InterruptedException ex) {
                    Logger.getInstance().e(ex);
                }
            }
            r6eErrorAssistThread = null;
        }
    }

    class R6eErrorAssistThread extends BaseThread {

        int autoCloseDuration;
        long timestamp;

        R6eErrorAssistThread(int autoCloseDuration) {
            this.autoCloseDuration = autoCloseDuration;
        }

        @Override
        protected boolean started() {
            timestamp = SystemClock.elapsedRealtime();
            return true;
        }

        @Override
        protected boolean performTaskInLoop() {
            try {
                Thread.sleep(50);
                if (SystemClock.elapsedRealtime() - timestamp >= autoCloseDuration) {
                    EventBus.getDefault().post(new HideErrorEvent(ShowErrorEvent.ERROR_R6E));
                    return false;
                }
            } catch (InterruptedException ex) {
                Logger.getInstance().e(ex);
            }
            return true;
        }

        @Override
        protected void finished() {

        }
    }
}
