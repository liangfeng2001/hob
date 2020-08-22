package com.ekek.tfthoodmodule.view;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.database.SettingPreferencesUtil;
import com.ekek.settingmodule.model.PlaySoundWhenSetTime;
import com.ekek.tfthoodmodule.R;
import com.ekek.tfthoodmodule.R2;
import com.ekek.tfthoodmodule.constants.TFTHoodConstant;
import com.ekek.tfthoodmodule.model.GetCookersInformation;
import com.ekek.tfthoodmodule.model.OrderFromHoodPanel;
import com.ekek.tfthoodmodule.model.PowerOffEven;
import com.ekek.viewmodule.common.NumberPickerView;
import com.ekek.viewmodule.hob.Hob80SettingIndicatorView;
import com.ekek.viewmodule.hob.Hob90SettingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class HoodPanelFragment extends BaseFragment implements NumberPickerView.OnValueChangeListener, NumberPickerView.OnBottomViewChangeListener {
    public static final int TFT_HOB_TYPE_60 = 0;
    public static final int TFT_HOB_TYPE_80 = 1;
    public static final int TFT_HOB_TYPE_90 = 2;
    private static final int FAN_MODE_AUTO = 0;
    private static final int FAN_MODE_MANUAL = 1;
    private static final int FAN_STATUS_POWER_OFF = 0;
    private static final int FAN_STATUS_POWER_ON = 1;

    private static final int HANDLER_SHOW_UI_IN_AUTO_MODE = 1;
    private static final int HANDLER_SHOW_HOOD_PANEL = 2;
    private static final int HANDLER_SHOW_FAN_4_POWER= 3;
    private static final int DEALY_TIME = 400;
    private static final int DEALY_TIME1000 = 1100;

    @BindView(R2.id.ib_hood_fan)
    ImageButton ibHoodFan;
    @BindView(R2.id.ib_turbo)
    TextView ibTurbo;
    @BindView(R2.id.npv_fan_gear)
    NumberPickerView npvFanGear;
    @BindView(R2.id.ib_silent)
    TextView ibSilent;
    @BindView(R2.id.ib_hood_fan_center)
    ImageButton ibHoodFanCenter;
    @BindView(R2.id.ib_hood_fan_auto)
    ImageButton ibHoodFanAuto;
    @BindView(R2.id.ib_hood_light)
    ImageButton ibHoodLight;
    @BindView(R2.id.ib_max)
    TextView ibMax;
    @BindView(R2.id.npv_light_gear)
    NumberPickerView npvLightGear;
    @BindView(R2.id.ib_min)
    TextView ibMin;
    @BindView(R2.id.ib_hood_light_center)
    ImageButton ibHoodLightCenter;
    @BindView(R2.id.ib_hob)
    ImageButton ibHob;
    @BindView(R2.id.ib_hood_mode_manual)
    ImageButton ibHoodModeManual;
    @BindView(R2.id.iv_pause)
    ImageView ivPause;
    @BindView(R2.id.iv_timer_when_stop_cooker)
    ImageView ivTimerWhenStopCooker;
    @BindView(R2.id.iv_go_back_to_main_screen)
    ImageView ivGoBackToMainScreen;
    @BindView(R2.id.fl_fan_gear)
    FrameLayout flFanGear;
    @BindView(R2.id.fl_light_gear)
    FrameLayout flLightGear;
    Unbinder unbinder;
    @BindView(R2.id.iv_small_a_cooker_open)
    ImageView ivSmallACookerOpen;
    @BindView(R2.id.iv_small_b_cooker_open)
    ImageView ivSmallBCookerOpen;
    @BindView(R2.id.iv_small_c_cooker_open)
    ImageView ivSmallCCookerOpen;
    @BindView(R2.id.high_temp_a)
    ImageView highTempA;
    @BindView(R2.id.high_temp_b)
    ImageView highTempB;
    @BindView(R2.id.high_temp_c)
    ImageView highTempC;
    @BindView(R2.id.ib_pause)
    ImageButton ibPause;
    @BindView(R2.id.h6siv)
    FrameLayout h6siv;
    @BindView(R2.id.h9siv)
    Hob90SettingIndicatorView h9siv;
    @BindView(R2.id.h8siv)
    Hob80SettingIndicatorView h8siv;

    private int fanGear, lightGear, fanMode = FAN_MODE_MANUAL;
    private int fanStatus = FAN_STATUS_POWER_OFF;
    private int hobType = TFT_HOB_TYPE_60;

    private int mSaveFanStatus = 0;

    private int fangear_send = 0;
    private int lightgear_send = 0;

    private Typeface typeface;

    private int IndexOfFanValue = 4;
    private int IndexOfLightValue = 1;

    private boolean mTimerIsWorking = false;

    private boolean mCookersIsWorking = false;  // 炉头全关

    private boolean mPauseIsWorking = false;
    private boolean mTheFirstTime = true;

    private boolean mCountdown5MinuteForTurboFlag =false;
    private int mCountdown5MinuteForTurboNum=0;

    String[] mFangear;
    private int mHoodValue=0;

    private Disposable mDisposable;//定时器
    private int mCountNum =0;

    private Map<Integer, Bitmap> bitmapMap = new HashMap<>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_SHOW_UI_IN_AUTO_MODE:
                    int fangear = getFanGearOfFromAutoMode();
                    if (fangear != 7) {  // 是 0档 ，不更新界面 2019年8月15日23:30:08
                        handleTurboStatusInAutoMode();
                       // npvFanGear.setValue(getFanGearOfFromAutoMode());

                        setTurboAndSilentUI();
                        SetFanGearSend(npvFanGear.getValue());  //  转换风机数据的格式
                        requestSaveGearValue();
                        LogUtil.d("liang the fan is  = " + npvFanGear.getValue());
                    }
                    break;
                case HANDLER_SHOW_HOOD_PANEL:  // 显示太快了，电磁炉那边保存风机数据不够快，从而导致读取的风机数据库的数据不对。
                    // timer 结束后，关风机 和灯光

                    int fan, light;
                    int[] value = new int[2];
                    boolean isFanWorkingAutoStatus = SettingPreferencesUtil.getFanWorkingStatus(getContext()).equals(CataSettingConstant.FAN_WORKING_STATUS_AUTO) ? true : false;
                    if (GlobalVars.getInstance().isFirstTimeForHoodPanelFragment()) { // 第一次开 hood 界面
                        GlobalVars.getInstance().setFirstTimeForHoodPanelFragment(false);
                        npvFanGear.setValue(TFTHoodConstant.HOOD_FAN_DEFAULT_GEAR_INDEX);
                        npvLightGear.setValue(TFTHoodConstant.HOOD_LIGHT_DEFAULT_GEAR_INDEX);
                        if (fanMode == FAN_MODE_AUTO || isFanWorkingAutoStatus) {
                            value = ((OnHoodPanelFragmentListener) mListener).onRequestGetGearValue(2);
                            // value[0]=0x00;  // fan
                            //  value[1]=0x00;  // light

                        } else {
                            value[0] = 0x83;  // fan
                            value[1] = 0x84;  // light
                        }

                    //    LogUtil.d("liang the first time");
                    } else {
                        value = ((OnHoodPanelFragmentListener) mListener).onRequestGetGearValue(2);
                       // LogUtil.d("liang the second time");
                    }
                    fan = value[0];
                    light = value[1];
                  //  LogUtil.d("liang get fan gear from hod is = " + fan);
                    fan = fan & 0x0f;  // 自动是 0x80，手动是0x00
                    light = light & 0x0f;

                    // 是 风机自动模式，则自动打开风机显示

                    if (fanMode == FAN_MODE_AUTO || isFanWorkingAutoStatus) {
                        ivTimerWhenStopCooker.setImageBitmap(getBitmap(R.mipmap.hood_timer_button_gray));
                        //  setNpvFanGearWhenIsAutoMode(fan);
                        setAutoModeUI(fan);
                        fanMode = FAN_MODE_AUTO;


                  //      LogUtil.d("liang show  screen from hidden 1~~~~~~~" + fan);
                    } else {
                        ivTimerWhenStopCooker.setImageBitmap(getBitmap(R.mipmap.hood_timer_button));
                    }

                    if (fan == 0) {  //  已经关风机
                        CloseFan();
                      //  LogUtil.d("liang close fan 1");
                    } else {
                        switchOnFan();
                        showTurboSilent();
                        SetFanGearSend(npvFanGear.getValue());  //风机
                        requestSaveGearValue();
                        ibHoodFan.setVisibility(View.VISIBLE);  // 第一次，自动模式下啊，没有显示风机档位
                        ibHoodFanCenter.setVisibility(View.INVISIBLE);
                       // LogUtil.d("liang close fan 4 "+fan);

                    }

                    if (light == 0) {  //  已经关灯光
                        CloseLight();
                    } else {
                        // requestSaveGearValue();
                        switchOnLight();
                        showMaxMin();
                        updateLightGearValue(npvLightGear.getValue()); // 灯
                        requestSaveGearValue();
                    }
                    if (GlobalVars.getInstance().isPause()) {  // 点击了 暂停键
                        //  doPause();     // 如果此时 暂停 风机，则恢复原样。
                        if (ibHoodFan.getVisibility() == View.VISIBLE)
                            ibHoodFan.setVisibility(View.INVISIBLE);
                        if (ibHoodFanCenter.getVisibility() == View.INVISIBLE)
                            ibHoodFanCenter.setVisibility(View.VISIBLE);
                        if (npvFanGear.getVisibility() == View.VISIBLE)
                            npvFanGear.setVisibility(View.INVISIBLE);
                        flFanGear.setBackgroundColor(Color.BLACK);

                        SetFanGearSend(7);
                        requestSaveGearValue();

                        if (ibTurbo.getVisibility() == View.VISIBLE)
                            ibTurbo.setVisibility(View.INVISIBLE);
                        if (ibSilent.getVisibility() == View.VISIBLE)
                            ibSilent.setVisibility(View.INVISIBLE);

                        // GlobalVars.getInstance().setPause();
                    }
                    mCountNum=0;  //用于显示 4档， 快速点击turbo时会出现显示混乱。2020年3月21日9:31:43
                    break;
                case HANDLER_SHOW_FAN_4_POWER:
                        if(fangear_send==0x85){  // 显示4档
                            npvFanGear.setValue(1);
                            ibTurbo.setVisibility(View.VISIBLE);
                        }
                    break;
            }
        }
    };

    public HoodPanelFragment() {

    }


    @SuppressLint("ValidFragment")
    public HoodPanelFragment(int hobType) {
        this.hobType = hobType;
//        LogUtil.d("liang hobType is  "+hobType);
    }

    @Override
    public int initLyaout() {
        return R.layout.tfthoodmodule_fragment_hood_panel;
    }

    @Override
    public void initListener() {

    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        init();
        SetFont();
    }

    private void SetFont() {
        typeface = GlobalVars.getInstance().getDefaultFontBold();
        ibTurbo.setTypeface(typeface);
        ibSilent.setTypeface(typeface);
        ibMax.setTypeface(typeface);
        ibMin.setTypeface(typeface);
    }

    private void showCookersIcons(){
        switch (hobType) {
            case TFT_HOB_TYPE_60:
                ibHob.setImageBitmap(getBitmap(R.mipmap.ic_hob_60));
                ivGoBackToMainScreen.setVisibility(View.VISIBLE);
                h8siv.setVisibility(View.INVISIBLE);
                h9siv.setVisibility(View.INVISIBLE);
                break;
            case TFT_HOB_TYPE_80:
                ibHob.setImageBitmap(getBitmap(R.mipmap.ic_hob_80));
                ivGoBackToMainScreen.setVisibility(View.INVISIBLE);
                h8siv.setVisibility(View.VISIBLE);
                h9siv.setVisibility(View.INVISIBLE);
                break;
            case TFT_HOB_TYPE_90:
                ibHob.setImageBitmap(getBitmap(R.mipmap.ic_hob_90));
                ivGoBackToMainScreen.setVisibility(View.INVISIBLE);
                h8siv.setVisibility(View.INVISIBLE);
                h9siv.setVisibility(View.VISIBLE);
                break;
        }

    }

    private void init() {

        showCookersIcons();

        initFanGearView();
        initLightGearView();
        setFanStatus();
        getPowerValueFromSQLite();
        requestSaveGearValue();  // 保存 风机档位 、灯光档位
        switchOffLight();
        updateLightGearValue(7); // 关灯光
        requestSaveGearValue();
    }

    private void setFanStatus()
    {
        boolean isFanWorkingAutoStatus = SettingPreferencesUtil.getFanWorkingStatus(getContext()).equals(CataSettingConstant.FAN_WORKING_STATUS_AUTO) ? true : false;
        if(isFanWorkingAutoStatus){
//            LogUtil.d("liang fan is auto");
            fanMode = FAN_MODE_AUTO;
        }else {
            switchOffFan();
            SetFanGearSend(7); // 关风机
         //   LogUtil.d("liang fan is not auto");
        }
    }

    private void getPowerValueFromSQLite() {
        // DatabaseHelper.saveHoodData(fanGear, lightGear);

      /*  int[] value = ((OnHoodPanelFragmentListener) mListener).onRequestGetGearValue(1);
        FanGear = value[0];
        LightGear = value[1];  // 保存的是 序号
        //   GetFanGearIndex(FanGear);
        //  GetLightIndex(LightGear);
        IndexOfFanValue = FanGear;
        IndexOfLightValue = LightGear;
        LogUtil.d("The fangear value is = " + FanGear);
        LogUtil.d("The LightGear gear value is = " + LightGear);*/
        int [] value = ((OnHoodPanelFragmentListener) mListener).onRequestGetGearValue(2);
        int fan = value[0];
       // SetFanGearSend(fan);
        SetFanGearSendFromSQL(fan);



    }

    private void setNpvFanGearWhenIsAutoMode(int fan) {
        fan = fan & 0x0f;
        if (fan != 0x0f) {
            npvFanGear.setValue(6 - fan);
        } else if (fan != 0) {                  // silent  档位
            npvFanGear.setValue(5);
        }
    }

    private void setAutoModeUI(int fanGear) {
        if (fanGear != 0) {
            if (npvFanGear.getValue() == TFTHoodConstant.HOOD_FAN_SILENT_GEAR_INDEX - 1) {
                ibSilent.setVisibility(View.INVISIBLE);
            } else {
                ibSilent.setVisibility(View.VISIBLE);
            }
            if (npvFanGear.getValue() == TFTHoodConstant.HOOD_FAN_TURBO_GEAR_INDEX) {
                ibTurbo.setVisibility(View.INVISIBLE);
            } else {
                ibTurbo.setVisibility(View.VISIBLE);
            }
            npvFanGear.setVisibility(View.VISIBLE);
            ibHoodFanCenter.setVisibility(View.INVISIBLE);
            ibHoodFan.setVisibility(View.VISIBLE);
            flFanGear.setBackgroundResource(R.mipmap.bg_hood_gear_gray);

            fanStatus = FAN_STATUS_POWER_ON;
            if(GlobalVars.getInstance().isSetTurboToNextPower()){

            }else {
                setNpvFanGearWhenIsAutoMode(fanGear);
            }

        }
        //  fanMode = FAN_MODE_AUTO;
        ibHoodModeManual.setImageBitmap(getBitmap(R.mipmap.ic_auto_mode));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            handler.sendEmptyMessageDelayed(HANDLER_SHOW_HOOD_PANEL, DEALY_TIME);
       //     LogUtil.d("liang show  screen from hidden  2~~~~~~~");
            registerEventBus();
            startCountdown();

        } else {
            ((OnHoodPanelFragmentListener) mListener).sendFanLightValueFromMainPanelB30(fangear_send, lightgear_send);
            unregisterEventBus();
            doStopCountDown();
           // LogUtil.d("liang send data to other screen" + "fangear_send is " + fangear_send);
        }
    }

    private void initFanGearView() {
        Resources res = getResources();
        mFangear = res.getStringArray(com.ekek.settingmodule.R.array.settingmodule_b30_turbo_silent);

        npvFanGear.setDisplayedValues(mFangear);
        npvFanGear.setOnValueChangedListener(this);
        npvFanGear.setOnBottomViewChangeListener(this);
        npvFanGear.setMaxValue(TFTHoodConstant.HOOD_FAN_GEAR_LIST.length - 1);
        npvFanGear.setMinValue(0);
        npvFanGear.setValue(TFTHoodConstant.HOOD_FAN_DEFAULT_GEAR_INDEX);
        calAndUpdateFanGearValue(TFTHoodConstant.HOOD_FAN_DEFAULT_GEAR_VALUE);
        npvFanGear.setFriction(10 * ViewConfiguration.get(getActivity()).getScrollFriction());
        Typeface typeface = GlobalVars.getInstance().getDefaultFontBold();
        npvFanGear.setContentTextTypeface(typeface);
        npvFanGear.setHintTextTypeface(typeface);
        npvFanGear.setItemScrollListener(new NumberPickerView.ItemScrollListener() {
            @Override
            public void OnItemScroll(float itemHeight, float totalScrollY) {
                EventBus.getDefault().post(new PlaySoundWhenSetTime(CataSettingConstant.PLAY_SOUND_TYPE_LIST));
            }
        });
    }

    private void initLightGearView() {
        npvLightGear.setDisplayedValues(TFTHoodConstant.HOOD_LIGHT_GEAR_LIST);
        npvLightGear.setOnValueChangedListener(this);
        npvLightGear.setOnBottomViewChangeListener(this);
        npvLightGear.setMaxValue(TFTHoodConstant.HOOD_LIGHT_GEAR_LIST.length - 1);
        npvLightGear.setMinValue(0);
        npvLightGear.setValue(TFTHoodConstant.HOOD_LIGHT_DEFAULT_GEAR_INDEX);
        lightGear = TFTHoodConstant.HOOD_LIGHT_DEFAULT_GEAR_VALUE;
        npvLightGear.setFriction(10 * ViewConfiguration.get(getActivity()).getScrollFriction());
        Typeface typeface = GlobalVars.getInstance().getDefaultFontBold();
        npvLightGear.setContentTextTypeface(typeface);
        npvLightGear.setHintTextTypeface(typeface);
        npvLightGear.setItemScrollListener(new NumberPickerView.ItemScrollListener() {
            @Override
            public void OnItemScroll(float itemHeight, float totalScrollY) {
                EventBus.getDefault().post(new PlaySoundWhenSetTime(CataSettingConstant.PLAY_SOUND_TYPE_LIST));
            }
        });
    }


    @OnClick({R2.id.iv_go_back_to_main_screen, R2.id.iv_timer_when_stop_cooker,
            R2.id.iv_pause, R2.id.ib_hood_fan, R2.id.ib_turbo, R2.id.ib_silent,
            R2.id.ib_hood_fan_center, R2.id.ib_hood_light, R2.id.ib_max, R2.id.ib_min,
            R2.id.ib_hood_light_center, R2.id.ib_hob, R2.id.ib_hood_mode_manual,
            R2.id.iv_small_a_cooker_open, R2.id.iv_small_b_cooker_open,
            R2.id.iv_small_c_cooker_open, R2.id.high_temp_a, R2.id.high_temp_b,
            R2.id.high_temp_c, R2.id.ib_pause,R2.id.h8siv,R2.id.h9siv})
    public void onViewClicked(View view) {
        int i = view.getId();

        if (i == R.id.ib_hood_fan) {  // 左边第一个按钮

            //  switchFanAutoModeForPowerOff();  // 关风机；
            fanMode = FAN_MODE_AUTO;
            switchFanMode();
            switchOffFan();
            SetFanGearSend(7);  // 关风机
            requestSaveGearValue();
            showTurboSlientIcon();
            mCountdown5MinuteForTurboFlag =false;
            GlobalVars.getInstance().setSetTurboToNextPower(false);
        } else if (i == R.id.ib_turbo) {   // turbo
            fanMode = FAN_MODE_MANUAL;
            SettingPreferencesUtil.saveFanWorkingStatus(getContext(), CataSettingConstant.FAN_WORKING_STATUS_MANUAL);
            setFanGearToTurboGear();
            calAndUpdateFanGearValue(0);
            SetFanGearSend(0);
            requestSaveGearValue();
            ivTimerWhenStopCooker.setImageBitmap(getBitmap(R.mipmap.hood_timer_button));
            LogUtil.d("liang fangear_send is " + fangear_send);
            mCountdown5MinuteForTurboFlag =true;
            GlobalVars.getInstance().setSetTurboToNextPower(false);
        } else if (i == R.id.ib_silent) {    // silent
            fanMode = FAN_MODE_MANUAL;
            SettingPreferencesUtil.saveFanWorkingStatus(getContext(), CataSettingConstant.FAN_WORKING_STATUS_MANUAL);
            setFanGearToSilentGear();
            //   calAndUpdateFanGearValue(6);
            SetFanGearSend(5);
            requestSaveGearValue();
            ivTimerWhenStopCooker.setImageBitmap(getBitmap(R.mipmap.hood_timer_button));
            LogUtil.d("liang fangear_send is " + fangear_send);
            mCountdown5MinuteForTurboFlag =false;
            GlobalVars.getInstance().setSetTurboToNextPower(false);
        } else if (i == R.id.ib_hood_fan_center) {   // 开风机  // 中间按键
            if (GlobalVars.getInstance().isPause()) return;
            if ((fanMode == FAN_MODE_AUTO) && !mCookersIsWorking) {  // 自动模式下，炉头关闭时，不能打开这个开关

            } else {
                switchOnFan();
                showTurboSilent();
                SetFanGearSend(npvFanGear.getValue());
                requestSaveGearValue();
                // fanMode = FAN_MODE_MANUAL;
                LogUtil.d("liang the fangear is " + npvFanGear.getValue() + " the mode is =" + fanMode);
            }

            mCountdown5MinuteForTurboFlag =false;

        } else if (i == R.id.ib_hood_light) {  // 灯光关的按键

            switchOffLight();
            updateLightGearValue(6);
            updateLightGearValue(7);
            requestSaveGearValue();
            showMaxMinIcon();
        } else if (i == R.id.ib_max) {

            setLightGearToMaxGear();
            updateLightGearValue(0);
            requestSaveGearValue();
        } else if (i == R.id.ib_min) {

            setLightGearToMinGear();
            updateLightGearValue(4);
            requestSaveGearValue();
        } else if (i == R.id.ib_hood_light_center) {

            switchOnLight();
            showMaxMin();
            updateLightGearValue(npvLightGear.getValue());
            requestSaveGearValue();
        } else if (i == R.id.ib_hob) {

            //  switchToHobPanel();

        } else if (i == R.id.ib_hood_mode_manual) {  // 自动与手动之间转换
            if (GlobalVars.getInstance().isPause()) return;
            switchFanMode();
        } else if (i == R.id.iv_pause) {  // 右侧菜单上的
            if (GlobalVars.getInstance().isPause()) return;
            //  EventBus.getDefault().post(new OrderFromHoodPanel(TFTHoodConstant.HOOD_DO_PAUSE));
            mPauseIsWorking = true;
            ibPause.setVisibility(View.VISIBLE);
            ivPause.setImageBitmap(getBitmap(R.mipmap.hood_pause_gray));

            if (ibSilent.getVisibility() == View.VISIBLE) ibSilent.setVisibility(View.INVISIBLE);
            if (ibTurbo.getVisibility() == View.VISIBLE) ibTurbo.setVisibility(View.INVISIBLE);
            //   ivTimerWhenStopCooker.setImageBitmap(getBitmap(R.mipmap.hood_timer_button_gray));
            doPause();
        } else if (i == R.id.ib_pause) {  // 左边的
            mPauseIsWorking = false;
            if (ibSilent.getVisibility() == View.INVISIBLE) ibSilent.setVisibility(View.VISIBLE);
            if (ibTurbo.getVisibility() == View.INVISIBLE) ibTurbo.setVisibility(View.VISIBLE);
            ibPause.setVisibility(View.INVISIBLE);
            ivPause.setImageBitmap(getBitmap(R.mipmap.hood_pause));
            doPlay();
        } else if (i == R.id.iv_timer_when_stop_cooker) {  // 定时图标
            if (GlobalVars.getInstance().isPause()) return;
            if (fanMode == FAN_MODE_AUTO) {

            } else {
                if (fangear_send == 0x80 || fangear_send == 0x00) {  // 风机档位是 零时，不启动 定时操作

                } else {
                    EventBus.getDefault().post(new OrderFromHoodPanel(TFTHoodConstant.TIMER_FROM_H104_SCREEN));
                    SettingPreferencesUtil.saveTimerOpenStatus(getActivity(), CataSettingConstant.TIMER_STATUS_OPEN);
                    ivTimerWhenStopCooker.setImageBitmap(getBitmap(R.mipmap.hood_timer_button));
                }

            }

        } else if (i == R.id.iv_go_back_to_main_screen || i == R.id.iv_small_a_cooker_open || i == R.id.iv_small_b_cooker_open
                || i == R.id.iv_small_c_cooker_open || i == R.id.high_temp_a || i == R.id.high_temp_b || i == R.id.high_temp_c||i==R.id.h8siv||i==R.id.h9siv) {
            EventBus.getDefault().post(new OrderFromHoodPanel(TFTHoodConstant.HOOD_GO_BACK_TO_HOB_PANEL));
        }
    }

    public void doPause() {
        mSaveFanStatus = fanStatus;
        switchFanAutoModeForPowerOff();
        switchOffFan();
        //calAndUpdateFanGearValue(6);
        // calAndUpdateFanGearValueForSwitchOff(6);
        SetFanGearSend(7);
        requestSaveGearValue();

       /* switchOffLight();
        updateLightGearValue(7);
        requestSaveGearValue();*/
        if (ibTurbo.getVisibility() == View.VISIBLE) ibTurbo.setVisibility(View.INVISIBLE);
        if (ibSilent.getVisibility() == View.VISIBLE) ibSilent.setVisibility(View.INVISIBLE);
        LogUtil.d("liang do pause~~ " + mSaveFanStatus);
        GlobalVars.getInstance().setPause(true);
        ivTimerWhenStopCooker.setImageBitmap(getBitmap(R.mipmap.hood_timer_button_gray));

    }

    private void SetFanGearSendFromSQL(int newval){
        if (fanMode == FAN_MODE_MANUAL) {
            switch (newval) {
                case 0:
                    fangear_send = 0x80 | 0x00;
                    break;
                case 1:
                    fangear_send = 0x80 | 0x01;
                    break;
                case 2:
                    fangear_send = 0x80 | 0x02;
                    break;
                case 3:
                    fangear_send = 0x80 | 0x03;
                    break;
                case 4:
                    fangear_send = 0x80 | 0x04;
                    break;
                case 5:
                    fangear_send = 0x80 | 0x05;
                    break;
                case 6:
                    fangear_send = 0x80 | 0x06;
                    break;
                case 7:
                    fangear_send = 0x80 | 0x00;
                    break;
                default:
                    fangear_send = 0x80 | 0x00;
                    break;
            }
        } else {
            switch (newval) {
                case 0:
                    fangear_send = 0x00 | 0x00;
                    break;
                case 1:
                    fangear_send = 0x00 | 0x01;
                    break;
                case 2:
                    fangear_send = 0x00 | 0x02;
                    break;
                case 3:
                    fangear_send = 0x00 | 0x03;
                    break;
                case 4:
                    fangear_send = 0x00 | 0x04;
                    break;
                case 5:
                    fangear_send = 0x00 | 0x05;
                    break;
                case 6:
                    fangear_send = 0x00 | 0x06;
                    break;
                case 7:
                    fangear_send = 0x00 | 0x00;
                    break;
                default:
                    fangear_send = 0x00 | 0x00;
                    break;
            }
        }

    }

    private void SetFanGearSend(int newval) {
        if (fanMode == FAN_MODE_MANUAL) {
            switch (newval) {
                case 0:
                    fangear_send = 0x80 | 0x06;
                    break;
                case 1:
                    fangear_send = 0x80 | 0x05;
                    break;
                case 2:
                    fangear_send = 0x80 | 0x04;
                    break;
                case 3:
                    fangear_send = 0x80 | 0x03;
                    break;
                case 4:
                    fangear_send = 0x80 | 0x02;
                    break;
                case 5:
                    fangear_send = 0x80 | 0x01;
                    break;
                case 6:
                    fangear_send = 0x80 | 0x0f;
                    break;
                case 7:
                    fangear_send = 0x80 | 0x00;
                    break;
                default:
                    fangear_send = 0x80 | 0x0f;
                    break;
            }
        } else {
            switch (newval) {
                case 0:
                    fangear_send = 0x00 | 0x06;
                    break;
                case 1:
                    fangear_send = 0x00 | 0x05;
                    break;
                case 2:
                    fangear_send = 0x00 | 0x04;
                    break;
                case 3:
                    fangear_send = 0x00 | 0x03;
                    break;
                case 4:
                    fangear_send = 0x00 | 0x02;
                    break;
                case 5:
                    fangear_send = 0x00 | 0x01;
                    break;
                case 6:
                    fangear_send = 0x00 | 0x0f;
                    break;
                case 7:
                    fangear_send = 0x00 | 0x00;
                    break;
                default:
                    fangear_send = 0x00 | 0x0f;
                    break;
            }
        }
        //  setTimerDataForOutsideHood();

    }

    private void setTimerDataForOutsideHood() {
        if (mTimerIsWorking) {
            fangear_send = (byte) fangear_send | 0x20;   // 有定时进行中
        } else {
            fangear_send = (byte) fangear_send & 0xdf;   // 无定时进行中
        }
    }

    public void doPlay() {
        if (ibTurbo.getVisibility() == View.INVISIBLE) ibTurbo.setVisibility(View.VISIBLE);
        if (ibSilent.getVisibility() == View.INVISIBLE) ibSilent.setVisibility(View.VISIBLE);

        if (mSaveFanStatus == FAN_STATUS_POWER_OFF) {
            LogUtil.d("liang do play 1");
        } else {
            switchOnFan();
            boolean isFanWorkingAutoStatus = SettingPreferencesUtil.getFanWorkingStatus(getContext()).equals(CataSettingConstant.FAN_WORKING_STATUS_AUTO) ? true : false;
            if (GlobalVars.getInstance().isPause() && !mCookersIsWorking && isFanWorkingAutoStatus) {  // 暂停时,且炉头关闭时，，不

            } else {
                calAndUpdateFanGearValue(npvFanGear.getValue());
                SetFanGearSend(npvFanGear.getValue());
                requestSaveGearValue();
            }
            showTurboSilent();
            LogUtil.d("liang do play  2");
        }


      /*  switchOnLight();
        updateLightGearValue(npvLightGear.getValue());
        requestSaveGearValue();*/

      //  LogUtil.d("do play~~");
        GlobalVars.getInstance().setPause(false);
       // ivTimerWhenStopCooker.setImageBitmap(getBitmap(R.mipmap.hood_timer_button_gray));
        ivTimerWhenStopCooker.setImageBitmap(getBitmap(R.mipmap.hood_timer_button));
    }

    private void switchToHobPanel() {
        ((OnHoodPanelFragmentListener) mListener).onHoodPanelFragmentRequestSwitchToHobPanel();

    }

    private void switchFanAutoModeForPowerOff() {
        //   fanMode = FAN_MODE_AUTO;
        if (ibTurbo.getVisibility() == View.INVISIBLE) ibTurbo.setVisibility(View.VISIBLE);
        if (ibSilent.getVisibility() == View.INVISIBLE) ibSilent.setVisibility(View.VISIBLE);

        if (npvFanGear.getVisibility() == View.INVISIBLE) npvFanGear.setVisibility(View.VISIBLE);
        if (ibHoodFan.getVisibility() == View.INVISIBLE) ibHoodFan.setVisibility(View.VISIBLE);
        if (ibHoodFanCenter.getVisibility() == View.VISIBLE)
            ibHoodFanCenter.setVisibility(View.INVISIBLE);

    }

    private int getFanGearOfFromAutoMode() {

        int[] value = ((OnHoodPanelFragmentListener) mListener).onRequestGetGearValue(2);
        int fan = value[0];
        int light = value[1];
        int setFanGear = 0;
        switch (fan) {
            case 0x0f:
            case 0x8f:
                setFanGear = 5;
                break;
            case 0x01:
            case 0x81:
                setFanGear = 5;
                break;
            case 0x02:
            case 0x82:
                setFanGear = 4;
                break;
            case 0x03:
            case 0x83:
                setFanGear = 3;
                break;
            case 0x04:
            case 0x84:
                setFanGear = 2;
                break;
            case 0x05:
            case 0x85:
                setFanGear = 1;
                break;
            case 0x06:
            case 0x86:
                setFanGear = 0;
                break;
            case 0x00:
            case 0x80:
                setFanGear = 7;
                break;
            default:
                setFanGear = 5;
                break;
        }
//        LogUtil.d("liang the setFanGear is  = " + setFanGear);

        return setFanGear;
    }

    private void switchFanMode() {
        switch (fanMode) {
            case FAN_MODE_MANUAL:
                if (fanStatus == FAN_STATUS_POWER_OFF && !mCookersIsWorking) {// 风机 关闭时，且 所有炉头关后，不管。

                    SettingPreferencesUtil.saveFanWorkingStatus(getContext(), CataSettingConstant.FAN_WORKING_STATUS_AUTO);
                    fanMode = FAN_MODE_AUTO;
                    SetFanGearSend(7);  // 关风机,及转换风机数据的格式
                    requestSaveGearValue();
                    ivTimerWhenStopCooker.setImageBitmap(getBitmap(R.mipmap.hood_timer_button_gray));
                    ibHoodModeManual.setImageBitmap(getBitmap(R.mipmap.ic_auto_mode));
                    LogUtil.d("liang the fanStatus == FAN_STATUS_POWER_OFF");
                } else {
                    if (mCookersIsWorking) {
                        SettingPreferencesUtil.saveFanWorkingStatus(getContext(), CataSettingConstant.FAN_WORKING_STATUS_AUTO);
                        fanStatus = FAN_STATUS_POWER_ON;
                        if (ibHoodFan.getVisibility() == View.INVISIBLE)
                            ibHoodFan.setVisibility(View.VISIBLE);
                        if (ibHoodFanCenter.getVisibility() == View.VISIBLE)
                            ibHoodFanCenter.setVisibility(View.INVISIBLE);

                        flFanGear.setBackgroundResource(R.mipmap.bg_hood_gear_gray);
                        LogUtil.d("liang set the background gray");

                        handler.sendEmptyMessageDelayed(HANDLER_SHOW_UI_IN_AUTO_MODE, DEALY_TIME1000);
                        if (npvFanGear.getVisibility() == View.INVISIBLE)
                            npvFanGear.setVisibility(View.VISIBLE);
                        ivTimerWhenStopCooker.setImageBitmap(getBitmap(R.mipmap.hood_timer_button_gray));
                        ibHoodModeManual.setImageBitmap(getBitmap(R.mipmap.ic_auto_mode));
                        fanMode = FAN_MODE_AUTO;
                    }
                    // LogUtil.d("liang the npvfangear is "+getFanGearOfFromAutoMode());
                }
                break;
            case FAN_MODE_AUTO:
                fanMode = FAN_MODE_MANUAL;
                setTurboAndSilentUI();
                if (fanStatus == FAN_STATUS_POWER_OFF) {   // 风机 关闭时，不管。
                    SetFanGearSend(7);  // 关风机,及转换风机数据的格式
                    requestSaveGearValue();
                    showTurboSlientIcon();
                } else {
                    flFanGear.setBackgroundResource(R.mipmap.bg_hood_gear);
                    if (npvFanGear.getVisibility() == View.INVISIBLE)
                        npvFanGear.setVisibility(View.VISIBLE);
                    SetFanGearSend(npvFanGear.getValue());  //  转换风机数据的格式
                    requestSaveGearValue();
                    LogUtil.d("liang set the background gray " + npvFanGear.getValue());
                }
                ibHoodModeManual.setImageBitmap(getBitmap(R.mipmap.ic_manual_mode));
                SettingPreferencesUtil.saveFanWorkingStatus(getContext(), CataSettingConstant.FAN_WORKING_STATUS_MANUAL);
                ivTimerWhenStopCooker.setImageBitmap(getBitmap(R.mipmap.hood_timer_button));
                break;
        }

    }

    private void setTurboAndSilentUI() {
        if (npvFanGear.getValue() == TFTHoodConstant.HOOD_FAN_SILENT_GEAR_INDEX - 1) {
            if (ibSilent.getVisibility() == View.VISIBLE)
                ibSilent.setVisibility(View.INVISIBLE);
        } else {
            if (ibSilent.getVisibility() == View.INVISIBLE)
                ibSilent.setVisibility(View.VISIBLE);
        }
        if (npvFanGear.getValue() == TFTHoodConstant.HOOD_FAN_TURBO_GEAR_INDEX) {
            if (ibTurbo.getVisibility() == View.VISIBLE) ibTurbo.setVisibility(View.INVISIBLE);
        } else {
            if (ibTurbo.getVisibility() == View.INVISIBLE) ibTurbo.setVisibility(View.VISIBLE);
        }
    }


    private void showTurboSilent() {
        if (npvFanGear.getValue() == TFTHoodConstant.HOOD_FAN_SILENT_GEAR_INDEX - 1) {
            if (ibSilent.getVisibility() == View.VISIBLE)
                ibSilent.setVisibility(View.INVISIBLE);
        } else {
            if (ibSilent.getVisibility() == View.INVISIBLE)
                ibSilent.setVisibility(View.VISIBLE);
        }
        if (npvFanGear.getValue() == TFTHoodConstant.HOOD_FAN_TURBO_GEAR_INDEX) {
            if (ibTurbo.getVisibility() == View.VISIBLE) ibTurbo.setVisibility(View.INVISIBLE);
        } else {
            if (ibTurbo.getVisibility() == View.INVISIBLE) ibTurbo.setVisibility(View.VISIBLE);
        }
    }


    private void showMaxMin() {
        if (npvLightGear.getValue() == TFTHoodConstant.HOOD_LIGHT_MAX_GEAR_INDEX) {
            if (ibMax.getVisibility() == View.VISIBLE) {
                ibMax.setVisibility(View.INVISIBLE);
            }
        } else {
            if (ibMax.getVisibility() == View.INVISIBLE) {
                ibMax.setVisibility(View.VISIBLE);
            }
        }

        if (npvLightGear.getValue() == TFTHoodConstant.HOOD_LIGHT_MIN_GEAR_INDEX - 2) {
            if (ibMin.getVisibility() == View.VISIBLE) {
                ibMin.setVisibility(View.INVISIBLE);
            }
        } else {
            if (ibMin.getVisibility() == View.INVISIBLE) {
                ibMin.setVisibility(View.VISIBLE);
            }
        }
    }

    private void switchOnLight() {
        if (ibHoodLight.getVisibility() == View.INVISIBLE) ibHoodLight.setVisibility(View.VISIBLE);
        if (ibHoodLightCenter.getVisibility() == View.VISIBLE)
            ibHoodLightCenter.setVisibility(View.INVISIBLE);
        if (npvLightGear.getVisibility() == View.INVISIBLE)
            npvLightGear.setVisibility(View.VISIBLE);

        flLightGear.setBackgroundResource(R.mipmap.bg_hood_gear);
    }

    private void switchOffLight() {
        if (ibHoodLight.getVisibility() == View.VISIBLE) ibHoodLight.setVisibility(View.INVISIBLE);
        if (ibHoodLightCenter.getVisibility() == View.INVISIBLE)
            ibHoodLightCenter.setVisibility(View.VISIBLE);
        if (npvLightGear.getVisibility() == View.VISIBLE)
            npvLightGear.setVisibility(View.INVISIBLE);


        flLightGear.setBackgroundColor(Color.BLACK);
    }

    private void switchOnFan() {
        fanStatus = FAN_STATUS_POWER_ON;
        if (ibHoodFan.getVisibility() == View.INVISIBLE) ibHoodFan.setVisibility(View.VISIBLE);
        if (ibHoodFanCenter.getVisibility() == View.VISIBLE)
            ibHoodFanCenter.setVisibility(View.INVISIBLE);

        if (fanMode == FAN_MODE_AUTO) {

            if (npvFanGear.getVisibility() == View.INVISIBLE)
                npvFanGear.setVisibility(View.VISIBLE);

            flFanGear.setBackgroundResource(R.mipmap.bg_hood_gear_gray);
            ibHoodModeManual.setImageBitmap(getBitmap(R.mipmap.ic_auto_mode));
//            LogUtil.d("liang set the background gray");

        } else if (fanMode == FAN_MODE_MANUAL) {
            if (npvFanGear.getVisibility() == View.INVISIBLE)
                npvFanGear.setVisibility(View.VISIBLE);
            //if (ibSilent.getVisibility() == View.INVISIBLE) ibSilent.setVisibility(View.VISIBLE);
            //if (ibTurbo.getVisibility() == View.INVISIBLE) ibTurbo.setVisibility(View.VISIBLE);
            flFanGear.setBackgroundResource(R.mipmap.bg_hood_gear);
            ibHoodModeManual.setImageBitmap(getBitmap(R.mipmap.ic_manual_mode));
        }

    }

    private void switchOffFan() {
        fanStatus = FAN_STATUS_POWER_OFF;
        if (ibHoodFan.getVisibility() == View.VISIBLE) ibHoodFan.setVisibility(View.INVISIBLE);
        if (ibHoodFanCenter.getVisibility() == View.INVISIBLE)
            ibHoodFanCenter.setVisibility(View.VISIBLE);
        if (npvFanGear.getVisibility() == View.VISIBLE) npvFanGear.setVisibility(View.INVISIBLE);
        flFanGear.setBackgroundColor(Color.BLACK);

        //if (ibSilent.getVisibility() == View.VISIBLE) ibSilent.setVisibility(View.INVISIBLE);
        //if (ibTurbo.getVisibility() == View.VISIBLE) ibTurbo.setVisibility(View.INVISIBLE);
        LogUtil.d("liang do switchOffFan ");
    }

    private void setFanGearToSilentGear() {
        npvFanGear.setValue(TFTHoodConstant.HOOD_FAN_SILENT_GEAR_INDEX - 1);
        if (ibSilent.getVisibility() == View.VISIBLE) ibSilent.setVisibility(View.INVISIBLE);
        if (ibTurbo.getVisibility() == View.INVISIBLE) ibTurbo.setVisibility(View.VISIBLE);
        switchOnFan();
    }

    private void setFanGearToTurboGear() {
        npvFanGear.setValue(TFTHoodConstant.HOOD_FAN_TURBO_GEAR_INDEX);
        if (ibSilent.getVisibility() == View.INVISIBLE) ibSilent.setVisibility(View.VISIBLE);
        if (ibTurbo.getVisibility() == View.VISIBLE) ibTurbo.setVisibility(View.INVISIBLE);
        switchOnFan();
    }

    private void setLightGearToMinGear() {
        npvLightGear.setValue(TFTHoodConstant.HOOD_LIGHT_MIN_GEAR_INDEX - 2);
        if (ibMin.getVisibility() == View.VISIBLE) ibMin.setVisibility(View.INVISIBLE);
        if (ibMax.getVisibility() == View.INVISIBLE) ibMax.setVisibility(View.VISIBLE);
        switchOnLight();
    }

    private void setLightGearToMaxGear() {
        npvLightGear.setValue(TFTHoodConstant.HOOD_LIGHT_MAX_GEAR_INDEX);
        if (ibMin.getVisibility() == View.INVISIBLE) ibMin.setVisibility(View.VISIBLE);
        if (ibMax.getVisibility() == View.VISIBLE) ibMax.setVisibility(View.INVISIBLE);
        switchOnLight();
    }

    @Override
    public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {
        LogUtil.d("Enter:: onValueChange------newVal----->" + newVal);
        if (picker == npvFanGear) {
            if (newVal == 5) {
                ibSilent.setVisibility(View.INVISIBLE);
                ibTurbo.setVisibility(View.VISIBLE);
                mCountdown5MinuteForTurboFlag =false;
            } else if (newVal == 0) {
                ibTurbo.setVisibility(View.INVISIBLE);
                ibSilent.setVisibility(View.VISIBLE);
                mCountdown5MinuteForTurboFlag =true;
            } else {
                ibSilent.setVisibility(View.VISIBLE);
                ibTurbo.setVisibility(View.VISIBLE);
                mCountdown5MinuteForTurboFlag =false;
            }

            fanMode = FAN_MODE_MANUAL;
            calAndUpdateFanGearValue(newVal);
            SetFanGearSend(newVal);
            requestSaveGearValue();


            flFanGear.setBackgroundResource(R.mipmap.bg_hood_gear);
            ivTimerWhenStopCooker.setImageBitmap(getBitmap(R.mipmap.hood_timer_button));
            ibHoodModeManual.setImageBitmap(getBitmap(R.mipmap.ic_manual_mode));
            SettingPreferencesUtil.saveFanWorkingStatus(getContext(), CataSettingConstant.FAN_WORKING_STATUS_MANUAL);
            GlobalVars.getInstance().setSetTurboToNextPower(false);
        } else if (picker == npvLightGear) {
            if (newVal == 4) {
                ibMin.setVisibility(View.INVISIBLE);
                ibMax.setVisibility(View.VISIBLE);
            } else if (newVal == 0) {
                ibMax.setVisibility(View.INVISIBLE);
                ibMin.setVisibility(View.VISIBLE);
            } else {
                ibMin.setVisibility(View.VISIBLE);
                ibMax.setVisibility(View.VISIBLE);

            }
            updateLightGearValue(newVal);
            requestSaveGearValue();
        }
    }

/*    private void updateGearValue() {
        int fireGear = 0;
        int lightGear = 0;
        String fearStr = TFTHoodConstant.HOOD_FAN_GEAR_LIST[npvFanGear.getValue()];
        if (fearStr.equals("TURBO")) fireGear = 6;
        else if (fearStr.equals("SILENT")) fireGear = 0;
        else fireGear = Integer.valueOf(fearStr);

        String lightStr = TFTHoodConstant.HOOD_LIGHT_GEAR_LIST[npvLightGear.getValue()];
        if (lightStr.equals("MAX")) lightGear = 6;
        else if (lightStr.equals("MIN")) lightGear = 0;
        else lightGear = Integer.valueOf(lightGear);

    }*/

    private void requestSaveGearValue() {
        ((OnHoodPanelFragmentListener) mListener).onRequestSaveGearValue(fangear_send, lightgear_send);
//        LogUtil.d("liang the fan is "+fangear_send);
    }


    private void calAndUpdateFanGearValue(int offset) {
        if (offset == 6) {
            if (fanMode == FAN_MODE_MANUAL) {
                fanGear = 0 | (1 << 7);
            } else if (fanMode == FAN_MODE_AUTO) {
                fanGear = 1 << offset;
            }
        } else {
            offset = TFTHoodConstant.HOOD_FAN_GEAR_LIST.length - offset - 1;
            if (fanMode == FAN_MODE_MANUAL) {
                //fanGear = (1 << offset) | (1 << 7);
                fanGear = offset | (1 << 7);
            } else if (fanMode == FAN_MODE_AUTO) {
                fanGear = offset;
            }

        }


    }


    private void updateLightGearValue(int index) {
        switch (index) {
            case 0:
                lightgear_send = 0x80 | 0x05;
                break;
            case 1:
                lightgear_send = 0x80 | 0x04;
                break;
            case 2:
                lightgear_send = 0x80 | 0x03;
                break;
            case 3:
                lightgear_send = 0x80 | 0x02;
                break;
            case 4:
                lightgear_send = 0x80 | 0x01;
                break;
            case 5:
                lightgear_send = 0x80 | 0x01;
                break;
            case 6:
                lightgear_send = 0x80 | 0x0f;
                break;
            case 7:
                lightgear_send = 0x80 | 0x00;
                break;
            default:
                lightgear_send = 0x80 | 0x05;
                break;
        }

    }

    @Override
    public void onBottomViewChange(boolean visible) {
   /*
        LogUtil.d("Enter:: onBottomViewChange----->"+ visible);
        if (visible) ibSilent.setVisibility(View.INVISIBLE);
        else  ibSilent.setVisibility(View.VISIBLE);*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public interface OnHoodPanelFragmentListener extends OnFragmentListener {
        void onHoodPanelFragmentRequestSwitchToHobPanel();

        void onRequestSaveGearValue(int fanGear, int lightGear);

        int[] getFanLightValueFromMainPanelB30();

        void doBackSwitchToHoodPanel(int order);

        int[] onRequestGetGearValue(int index);

        void sendFanLightValueFromMainPanelB30(int fanGear, int lightGear);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PowerOffEven event) {
        int order;
        order = event.getOrder();

        if (order == TFTHoodConstant.HOOD_CLOSE_FAN_LIGHT) {
            // 关灯 、关风机
            CloseLight();
            CloseFan();
            LogUtil.d("liang close fan 2");
          //  LogUtil.d("liang received  order to close the fan and light");

        }
        if (order == TFTHoodConstant.HOOD_CLOSE_FAN) {
            CloseFan();
            LogUtil.d("liang close fan 3");
        }
        if (order == TFTHoodConstant.HOOD_RESET_DATA) {
            init();
        }

        LogUtil.d("liang received  order ");

    }

    private void CloseLight() {
        switchOffLight();
        updateLightGearValue(7); // 关灯
        requestSaveGearValue();
        showMaxMinIcon();

    }

    private void CloseFan() {
        switchFanAutoModeForPowerOff();
        switchOffFan();
        SetFanGearSend(7);  // 关风机
        requestSaveGearValue();
        showTurboSlientIcon();
        LogUtil.d("liang close fan~~~");
    }

    private void closeFanUI() {
        switchFanAutoModeForPowerOff();
        switchOffFan();
        showTurboSlientIcon();
    }

    private void handleFanUI(boolean flag){
        if (flag) {   // 炉头关
            showCookersIcons();
            mCookersIsWorking = false;
            if (fanMode == FAN_MODE_AUTO) {  //  风机档位显示值
                // CloseFan();    // 关风机
                closeFanUI();
                hiddenTurboSilentWhenfanIsPause();
                // SetFanGearSend(npvFanGear.getValue());  //  转换风机数据的格式
                // requestSaveGearValue();
            }else if(fanMode != FAN_MODE_AUTO){  // 手动 档位
                handleTurboStatusInManualMode();
 //               LogUtil.d("liang fanMode is not auto ");
            }
        } else {   // 炉头开

            showCookersIcons();
            mCookersIsWorking = true;
            if (mCookersIsWorking && fanMode == FAN_MODE_AUTO) {  // 不断更新 风机档位显示值
                handleTurboStatusInAutoMode();
                setTurboAndSilentUI();
                hiddenTurboSilentWhenfanIsPause();

            }else if(fanMode != FAN_MODE_AUTO){
                handleTurboStatusInManualMode();
            }

//            LogUtil.d("liang ivGoBackToMainScreen  INVISIBLE");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GetCookersInformation event) {  // 左下方的 炉头指示图标
        int aCookerMode, bCookerMode, cCookerMode,dCookerMode,eCookerMode,fCookerMode;
        int hoodValue;
        boolean aCooker_HighTemp = false, bCooker_HighTemp = false, cCooker_HighTemp = false;
        boolean dCooker_HighTemp = false, eCooker_HighTemp = false, fCooker_HighTemp = false;
        boolean abWorking=false,efWorking=false;
        aCookerMode = event.getA_cooker_is_open();
        bCookerMode = event.getB_cooker_is_open();
        cCookerMode = event.getC_cooker_is_open();
        dCookerMode = event.getD_cooker_is_open();
        eCookerMode = event.getE_cooker_is_open();
        fCookerMode = event.getF_cooker_is_open();

        aCooker_HighTemp = event.isA_cooker_is_HighTemp();
        bCooker_HighTemp = event.isB_cooker_is_HighTemp();
        cCooker_HighTemp = event.isC_cooker_is_HighTemp();
        dCooker_HighTemp = event.isD_cooker_is_HighTemp();
        eCooker_HighTemp = event.isE_cooker_is_HighTemp();
        fCooker_HighTemp = event.isF_cooker_is_HighTemp();

        abWorking=event.isAbWorking();
        efWorking=event.isEfWorking();

        mHoodValue=event.getHood_value();

        boolean  mIfAllCookerClose=false;
        switch (hobType) {
            case TFT_HOB_TYPE_60:
                if (aCooker_HighTemp) {
                    highTempA.setVisibility(View.VISIBLE);
                    ivSmallACookerOpen.setVisibility(View.INVISIBLE);
                } else {
                    highTempA.setVisibility(View.INVISIBLE);
                    if (aCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) {  // a 关机
                        ivSmallACookerOpen.setVisibility(View.INVISIBLE);
                    } else {
                        ivSmallACookerOpen.setVisibility(View.VISIBLE);
                    }
                }
                if (bCooker_HighTemp) {
                    highTempB.setVisibility(View.VISIBLE);
                    ivSmallBCookerOpen.setVisibility(View.INVISIBLE);
                } else {
                    highTempB.setVisibility(View.INVISIBLE);
                    if (bCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) {  //  b 关机
                        ivSmallBCookerOpen.setVisibility(View.INVISIBLE);
//                LogUtil.d("liang the b cooker is power off");
                    } else {
                        ivSmallBCookerOpen.setVisibility(View.VISIBLE);
                    }
                }
                if (cCooker_HighTemp) {
                    highTempC.setVisibility(View.VISIBLE);
                    ivSmallCCookerOpen.setVisibility(View.INVISIBLE);
                } else {
                    highTempC.setVisibility(View.INVISIBLE);
                    if (cCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) {  //  c  关机
                        ivSmallCCookerOpen.setVisibility(View.INVISIBLE);
                    } else {
                        ivSmallCCookerOpen.setVisibility(View.VISIBLE);
                    }
                }

                mIfAllCookerClose=((aCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) || aCooker_HighTemp) && ((bCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) || bCooker_HighTemp) && ((cCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) || cCooker_HighTemp);
                handleFanUI(mIfAllCookerClose);


      /*  if (mCookersIsWorking && fanMode == FAN_MODE_AUTO) {  // 不断更新 风机档位显示值
            npvFanGear.setValue(getFanGearOfFromAutoMode());
            setTurboAndSilentUI();
            // SetFanGearSend(npvFanGear.getValue());  //  转换风机数据的格式
            // requestSaveGearValue();
        }
*/
                break;
            case TFT_HOB_TYPE_80:

                if(abWorking){
                    if (aCooker_HighTemp||bCooker_HighTemp){
                        h8siv.abCookerShowHighTemperature(true);
                       // LogUtil.d("liang all high temp");
                    }else {
                        h8siv.abCookerShowHighTemperature(false);
                       // LogUtil.d("liang  not all high temp");
                    }

                }else {
                    if (aCooker_HighTemp) {
                        h8siv.aCookerShowHighTemperature(true);
                       // LogUtil.d("liang aCooker_HighTemp  is  HighTemp");
                    } else {
                        h8siv.aCookerShowHighTemperature(false);
                        if (aCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) {  // a 关机
                            h8siv.aCookerShowBlackground();
                           // LogUtil.d("liang a mode 1 is "+aCookerMode);
                        } else {
                           // LogUtil.d("liang a mode 2  is "+aCookerMode);
                        }
                    }

                    if (bCooker_HighTemp) {
                        h8siv.bCookerShowHighTemperature(true);
                        //LogUtil.d("liang bCooker_HighTemp  is  HighTemp");
                    } else {
                        h8siv.bCookerShowHighTemperature(false);
                        if (bCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) {  //  b 关机
                            h8siv.bCookerShowBlackground();
//                LogUtil.d("liang the b cooker is power off");
                        } else {
                           // LogUtil.d("liang a mode is "+bCookerMode);
                        }
                    }
                }



                if (cCooker_HighTemp) {
                    h8siv.centerCookerShowHighTemperature(true);
                } else {
                    h8siv.centerCookerShowHighTemperature(false);
                    if (cCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) {  //  c  关机
                        h8siv.centerCookerShowBlackground();
                    } else {
                //        LogUtil.d("liang a mode is "+cCookerMode);
                    }
                }

                if (dCooker_HighTemp) {
                    h8siv.rightCookerShowHighTemperature(true);
                } else {
                    h8siv.rightCookerShowHighTemperature(false);
                    if (dCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) {  //  d  关机
                        h8siv.rightCookerShowBlackground();
                    } else {
                     //   LogUtil.d("liang a mode is "+dCookerMode);
                    }
                }

                if(aCooker_HighTemp&&bCooker_HighTemp){  // a b high temperature at the same time
                    h8siv.bCookerShowBlackground();
                    h8siv.aCookerShowBlackground();
                    h8siv.abCookerShowHighTemperature(true);

                  //  LogUtil.d("liang all high temp");
                }


                mIfAllCookerClose=((aCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) || aCooker_HighTemp) && ((bCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) || bCooker_HighTemp) && ((cCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) || cCooker_HighTemp)&& ((dCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) || dCooker_HighTemp);
                handleFanUI(mIfAllCookerClose);
                break;
            case TFT_HOB_TYPE_90:
                if(abWorking){
                    if (aCooker_HighTemp||bCooker_HighTemp){
                        h9siv.abCookerShowHighTemperature(true);
                      //  LogUtil.d("liang set ab high temp ");
                    }else {
                        h9siv.abCookerShowHighTemperature(false);
                       // LogUtil.d("liang set ab not high temp ");
                    }
                }else {
                    if (aCooker_HighTemp) {
                        h9siv.aCookerShowHighTemperature(true);
                       // LogUtil.d("liang set a high temp ");
                    } else {
                        h9siv.aCookerShowHighTemperature(false);
                        if (aCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) {  // a 关机
                            h9siv.aCookerShowBlackground();

                        } else {
                         //   LogUtil.d("liang set a not  high temp ");
                        }
                    }

                    if (bCooker_HighTemp) {
                        h9siv.bCookerShowHighTemperature(true);
                      //  LogUtil.d("liang set b high temp ");
                    } else {
                        h9siv.bCookerShowHighTemperature(false);
                        if (bCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) {  //  b 关机
                            h9siv.bCookerShowBlackground();
//                LogUtil.d("liang the b cooker is power off");
                        } else {
                           // LogUtil.d("liang set b not  high temp ");
                        }
                    }
                }



                if (cCooker_HighTemp) {
                    h9siv.centerCookerShowHighTemperature(true);
                } else {
                    h9siv.centerCookerShowHighTemperature(false);
                    if (cCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) {  //  c  关机
                        h9siv.centerCookerShowBlackground();
                    } else {

                    }
                }

                if(efWorking){
                    if (eCooker_HighTemp||fCooker_HighTemp){
                        h9siv.efCookerShowHighTemperature(true);
                       // LogUtil.d("liang set ef high temp ");
                    }else {
                        h9siv.efCookerShowHighTemperature(false);
                       // LogUtil.d("liang set ef not high temp ");
                    }
                }else {
                    if (eCooker_HighTemp) {
                        h9siv.eCookerShowHighTemperature(true);
                       // LogUtil.d("liang set e high temp ");
                     //   LogUtil.d("liang ecooker is hightemp");
                    } else {
                        h9siv.eCookerShowHighTemperature(false);
                        if (eCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) {  //  e 关机
                            h9siv.eCookerShowBlackground();
                        } else {
                         //   LogUtil.d("liang set e not high temp ");
                        }
                    }

                    if (fCooker_HighTemp) {
                        h9siv.fCookerShowHighTemperature(true);
                       // LogUtil.d("liang set f high temp ");
//                    LogUtil.d("liang fcooker is hightemp");
                    } else {
                        h9siv.fCookerShowHighTemperature(false);
                        if (fCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) {  //  e 关机
                            h9siv.fCookerShowBlackground();
                        } else {
                         //   LogUtil.d("liang set f not high temp ");
                        }
                    }
                }



                if(aCooker_HighTemp&&bCooker_HighTemp){  // a b high temperature at the same time
                    h9siv.bCookerShowBlackground();
                    h9siv.aCookerShowBlackground();
                    h9siv.abCookerShowHighTemperature(true);

                //   LogUtil.d("liang set ab high temp ");
                }

                if(eCooker_HighTemp&&fCooker_HighTemp){  // e f high temperature at the same time
                    h9siv.eCookerShowBlackground();
                    h9siv.fCookerShowBlackground();
                    h9siv.efCookerShowHighTemperature(true);

                 //   LogUtil.d("liang set ef high temp ");
                }

                mIfAllCookerClose=((aCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) || aCooker_HighTemp) && ((bCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) || bCooker_HighTemp) && ((cCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) || cCooker_HighTemp)&& ((eCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) || eCooker_HighTemp)&& ((fCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) || fCooker_HighTemp);
                handleFanUI(mIfAllCookerClose);

                break;
        }

    }

    private void handleTurboStatusInManualMode()
    {
        if(GlobalVars.getInstance().isSetTurboToNextPower()){
            npvFanGear.setValue(1);
            ibTurbo.setVisibility(View.VISIBLE);
            SetFanGearSend(1);
            requestSaveGearValue();
         //   GlobalVars.getInstance().setSetTurboToNextPower(false);
        //    LogUtil.d("liang is not set 5 ");
        }else {
        //    LogUtil.d("liang is not set 4 ");
        }
    }

    private  void handleTurboStatusInAutoMode()  // turbo 档位 5分钟后，降为 4档
    {
        if(GlobalVars.getInstance().isSetTurboToNextPower()){

                npvFanGear.setValue(1);
                ibTurbo.setVisibility(View.VISIBLE);
                SetFanGearSend(1);
                requestSaveGearValue();
                LogUtil.d("liang set turbo 1");
          //      GlobalVars.getInstance().setSetTurboToNextPower(false);
        }else{
//            LogUtil.d("liang set turbo 2");
            npvFanGear.setValue(getFanGearOfFromAutoMode());
        }
    }

    private void hiddenTurboSilentWhenfanIsPause() {
        if (GlobalVars.getInstance().isPause()) {   // 风机暂停时 ，turbo 及 silent 不显示。
            if (ibTurbo.getVisibility() == View.VISIBLE) ibTurbo.setVisibility(View.INVISIBLE);
            if (ibSilent.getVisibility() == View.VISIBLE) ibSilent.setVisibility(View.INVISIBLE);
        }
    }

    private void showTurboSlientIcon() {
        if (ibTurbo.getVisibility() == View.INVISIBLE) ibTurbo.setVisibility(View.VISIBLE);
        if (ibSilent.getVisibility() == View.INVISIBLE) ibSilent.setVisibility(View.VISIBLE);
    }

    private void showMaxMinIcon() {
        if (ibMax.getVisibility() == View.INVISIBLE) ibMax.setVisibility(View.VISIBLE);
        if (ibMin.getVisibility() == View.INVISIBLE) ibMin.setVisibility(View.VISIBLE);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        ibTurbo.setText(R.string.viewmodule_hood_panel_turbo);
        ibSilent.setText(R.string.viewmodule_hood_panel_silent);
        Resources res = getResources();
        mFangear = res.getStringArray(com.ekek.settingmodule.R.array.settingmodule_b30_turbo_silent);
        npvFanGear.setDisplayedValues(mFangear);
    }

    public void hiddenPauseIcon() {
        ibPause.setVisibility(View.INVISIBLE);
        ivPause.setImageBitmap(getBitmap(R.mipmap.hood_pause));
        LogUtil.d("liang ");
    }

    private Bitmap getBitmap(int source) {
        if (bitmapMap.containsKey(source)) {
            return bitmapMap.get(source);
        }

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), source);
        bitmapMap.put(source, bitmap);
        return bitmap;
    }

    private void startCountdown() {
        doStopCountDown();
        mDisposable = Observable
                //.interval(0,1, TimeUnit.SECONDS)
                .interval(0, 400, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if( fanMode ==FAN_MODE_MANUAL){
                            if(npvFanGear.getStatusOfFanFlag()){
                                mCountNum=5;  // 停止点击后 2秒 恢复显示 4档。
                            }else {
                                if(mCountNum>0) {
                                    mCountNum--;
                                }else{
                                    handler.sendEmptyMessageDelayed(HANDLER_SHOW_FAN_4_POWER, 100);
                                }
                            }

                        }
                        //  LogUtil.d("liang the CountValue is " + CountValue);
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
}
