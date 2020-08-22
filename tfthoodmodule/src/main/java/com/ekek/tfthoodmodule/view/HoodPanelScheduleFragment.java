package com.ekek.tfthoodmodule.view;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.database.SettingPreferencesUtil;
import com.ekek.tfthoodmodule.R;
import com.ekek.tfthoodmodule.R2;
import com.ekek.tfthoodmodule.constants.TFTHoodConstant;
import com.ekek.tfthoodmodule.model.GetCookersInformation;
import com.ekek.tfthoodmodule.model.OrderFromHoodPanel;
import com.ekek.viewmodule.hob.Hob80SettingIndicatorView;
import com.ekek.viewmodule.hob.Hob90SettingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

//import com.ekek.settingmodule.constants.CataSettingConstant;


public class HoodPanelScheduleFragment extends BaseFragment {


    private static final int WorkingStatus_Turbo = 0;  // turbo 档 ，5~1档 ，silent 档。
    private static final int WorkingStatus_5 = 1;
    private static final int WorkingStatus_4 = 2;
    private static final int WorkingStatus_3 = 3;
    private static final int WorkingStatus_2 = 4;
    private static final int WorkingStatus_1 = 5;
    private static final int WorkingStatus_Silent = 6;

    private static final int FearValueReal_Turbo = 0x06;
    private static final int FearValueReal_5 = 0x05;
    private static final int FearValueReal_4 = 0x04;
    private static final int FearValueReal_3 = 0x03;
    private static final int FearValueReal_2 = 0x02;
    private static final int FearValueReal_1 = 0x01;
    private static final int FearValueReal_Silent = 0x0f;
    private static final int FearValueReal_Poweroff = 0x00;
    private static final int FearValueReal_Poweroff_Manual = 0x80;

    private static final int FearValueReal_Turbo_manual = 0x86;
    private static final int FearValueReal_5manual = 0x85;
    private static final int FearValueReal_4manual = 0x84;
    private static final int FearValueReal_3manual = 0x83;
    private static final int FearValueReal_2manual = 0x82;
    private static final int FearValueReal_1manual = 0x81;
    private static final int FearValueReal_Silentmanual = 0x8f;

    private static final int LightValueReal_Turbo_manual = 0x86;
    private static final int LightValueReal_5manual = 0x85;
    private static final int LightValueReal_4manual = 0x84;
    private static final int LightValueReal_3manual = 0x83;
    private static final int LightValueReal_2manual = 0x82;
    private static final int LightValueReal_1manual = 0x81;
    private static final int LightValueReal_Silent = 0x8f;


    private static final int CountDownTotalTime_Power_25 = 25;
    private static final int CountDownTotalTime_Power_22 = 22;
    private static final int CountDownTotalTime_Power_17 = 17;
    private static final int CountDownTotalTime_Power_14 = 14;
    private static final int CountDownTotalTime_Power_11 = 11;
    private static final int CountDownTotalTime_Power_8 = 8;
    private static final int CountDownTotalTime_Power_5 = 5;

    private static final int CountDownTotalTime_Light_25 = 25;
    private static final int CountDownTotalTime_Light_20 = 20;
    private static final int CountDownTotalTime_Light_17 = 17;
    private static final int CountDownTotalTime_Light_14 = 14;
    private static final int CountDownTotalTime_Light_11 = 11;
    private static final int CountDownTotalTime_Light_8 = 8;
    private static final int CountDownTotalTime_Light_5 = 5;

    public static final int TFT_HOB_TYPE_60 = 0;
    public static final int TFT_HOB_TYPE_80 = 1;
    public static final int TFT_HOB_TYPE_90 = 2;

    @BindView(R2.id.big_MyBigCycle)
    MyBigCycle bigMyBigCycle;
    @BindView(R2.id.iv_timer)
    ImageView ivTimer;
    @BindView(R2.id.dynamic_5_min)
    TextView dynamic5Min;
    @BindView(R2.id.iv_cancel)
    ImageView ivCancel;
    @BindView(R2.id.big_cycle)
    FrameLayout bigCycle;
    Unbinder unbinder;
    @BindView(R2.id.iv_go_back_to_main_screen)
    ImageView ivGoBackToMainScreen;
    @BindView(R2.id.iv_small_a_cooker_open)
    ImageView ivSmallACookerOpen;
    @BindView(R2.id.iv_small_b_cooker_open)
    ImageView ivSmallBCookerOpen;
    @BindView(R2.id.iv_small_c_cooker_open)
    ImageView ivSmallCCookerOpen;
    @BindView(R2.id.high_temp_a)
    ImageView highTempA;
    @BindView(R2.id.high_temp_c)
    ImageView highTempC;
    @BindView(R2.id.high_temp_b)
    ImageView highTempB;
    @BindView(R2.id.h9siv)
    Hob90SettingIndicatorView h9siv;
    @BindView(R2.id.h8siv)
    Hob80SettingIndicatorView h8siv;

    private Disposable mDisposable;//定时器
    private int OneMinuteValue = 0; // 1 分钟 定时
    private boolean OneMinuteValue_Flag = false;

    private int CountValue = 0;
    private int CountValue5Second = 0;  // 5 秒后，返回 电磁炉主界面
    private boolean CountValue5SecondFlag = false;

    private int WorkingStatus = 0;
    private static final int Five_Minute = 5 * 60;  // 5 分钟 ，3 分钟
    private static final int Three_Minute = 3 * 60;

    private int FanGear = 0;
    private int LightGear = 0;

    private boolean ShowT22UI = false;
    private int NowMinute = 0;
    private int mTotalTime = 0;
    private int mCountDownNowSecond = 0;
    private int mNowSecond = 0;
    private int mNowStatus = 0;

    private Typeface typeface;
    private int mType; // 从 H104启动还是从 主界面启动
    private int mHobType; // 电磁炉的类型 60 80 90


    @Override
    public int initLyaout() {
        return R.layout.tfthoodmodule_fragment_hood_panel_schedule_new;
    }

    @Override
    public void initListener() {

    }

    public void setType(int type,int hobType) {
        this.mType = type;
        this .mHobType=hobType;
        LogUtil.d("liang the type is = " + mType);
    }


    public HoodPanelScheduleFragment() {

    }

    @SuppressLint("ValidFragment")
    public HoodPanelScheduleFragment(int type,int hobType) {
        this.mType = type;
        this .mHobType=hobType;
        LogUtil.d("liang the type is = " + mType);
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        // iniTimer();
        //    startCountdown();
        //  initCycleUI();
        SetFont();
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

    private void initCycleUI() {
        bigCycle.setScaleX(1.0f);
        bigCycle.setScaleY(1.0f);
    }

    private void SetFont() {
        dynamic5Min.setTypeface(GlobalVars.getInstance().getDefaultFontBold());
    }

    private void initCountDown_X_MinuteUI(int nowMinute) {
     /*   switch (status){
            case WorkingStatus_Turbo:
            case WorkingStatus_Silent:
                dynamic5Min.setText("5"+" "+ "min");
              //  dynamic5Min.setRotation();
                break;
                default:
                    dynamic5Min.setText("3"+" "+ "min");
                    break;

        }*/
        dynamic5Min.setText(String.valueOf(nowMinute) + " " + "min");
    }

    @Override
    public void onStart() {
        super.onStart();
        //   LogUtil.d("Liang start the T21 fragment onStart ~~~~~");
    }

    /*@Override
    public void onStop() {

        super.onStop();

    }*/

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            showCookersIcons();
          //  CountValue5Second = 0;
            CountValue5SecondFlag = true;
            if (CountValue == 0) {  // 定时结束，重新开始
                getPowerValueFromSQLite();
                initCountDown_X_MinuteUI(mTotalTime);
                initCycleProgressUI();
                startCountdown();
                initCycleUI();
                initUI();
            } else {   // 定时没有结束，继续定时

            }
            registerEventBus();

            LogUtil.d("Liang start the T21 fragment onHiddenChanged ~~~~~");
        } else {

            CountValue5SecondFlag = false;
            CountValue5Second = 0;
            unregisterEventBus();

        }
    }

    private void requestSaveGearValue(int fanValue, int lightValue) {
        ((HoodPanelFragment.OnHoodPanelFragmentListener) mListener).onRequestSaveGearValue(fanValue, lightValue);
        LogUtil.d("liang save data from schedulefragment");
    }

    private void getPowerValueFromSQLite() {
        // DatabaseHelper.saveHoodData(fanGear, lightGear);

        int[] value = ((HoodPanelFragment.OnHoodPanelFragmentListener) mListener).onRequestGetGearValue(2);
        // int[] value = ((HoodPanelFragment.OnHoodPanelFragmentListener) mListener).getFanLightValueFromMainPanelB30();
        FanGear = value[0];
        LightGear = value[1];
        LogUtil.d("liang ,The fan gear value is = " + FanGear);
        switch (FanGear) {
            case FearValueReal_Turbo:
            case FearValueReal_Turbo_manual:
                WorkingStatus = WorkingStatus_Turbo;
                mTotalTime = CountDownTotalTime_Power_22;
                mNowSecond = Five_Minute;
                break;
            case FearValueReal_5:
            case FearValueReal_5manual:
                WorkingStatus = WorkingStatus_5;
                mTotalTime = CountDownTotalTime_Power_17;
                mNowSecond = Three_Minute;
                break;
            case FearValueReal_4:
            case FearValueReal_4manual:
                WorkingStatus = WorkingStatus_4;
                mTotalTime = CountDownTotalTime_Power_14;
                mNowSecond = Three_Minute;
                break;
            case FearValueReal_3:
            case FearValueReal_3manual:
                WorkingStatus = WorkingStatus_3;
                mTotalTime = CountDownTotalTime_Power_11;
                mNowSecond = Three_Minute;
                break;
            case FearValueReal_2:
            case FearValueReal_2manual:
                WorkingStatus = WorkingStatus_2;
                mTotalTime = CountDownTotalTime_Power_8;
                mNowSecond = Three_Minute;
                break;
            case FearValueReal_1:
            case FearValueReal_1manual:
                WorkingStatus = WorkingStatus_1;
                mTotalTime = CountDownTotalTime_Power_5;
                mNowSecond = Three_Minute;
                break;
            case FearValueReal_Silent:
            case FearValueReal_Silentmanual:
                WorkingStatus = WorkingStatus_Silent;
                mTotalTime = CountDownTotalTime_Power_5;
                mNowSecond = Five_Minute;
                break;
         /*   case FearValueReal_Poweroff_Manual:
            case FearValueReal_Poweroff:

                break;*/
            default:
                WorkingStatus = WorkingStatus_Turbo;
                mTotalTime = CountDownTotalTime_Power_22;
                mNowSecond = Five_Minute;
                break;
        }
        mNowStatus = WorkingStatus;
        LogUtil.d("The WorkingStatus is = " + WorkingStatus);
    }


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
                        CountValue++;
                        mCountDownNowSecond++;
                        setShowT22UI(CountValue); // 显示 T22界面
                        updateTextViewInsideCycle(CountValue); // 更新圈内的数字
                        updateBigCycle(CountValue); // 更新圈进度
                        countDownPowerAndLightValue();// 降档位

                        goBackToMainScreen();  // 5 秒后 返回 电磁炉主界面
                     //   LogUtil.d("liang go to mainscreen, 2 is " + CountValue5Second);
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

    private void goBackToMainScreen() {

        if (CountValue5SecondFlag) {
            CountValue5Second++;
            if (CountValue5Second > 5) {
                CountValue5Second = 0;
                ((HoodPanelFragment.OnHoodPanelFragmentListener) mListener).onHoodPanelFragmentRequestSwitchToHobPanel();
                //  LogUtil.d("liang go to mainscreen, 1 is " + CountValue5Second);
            }
        }
    }

    private void countDownPowerAndLightValue() {
        int temp = 0x80;
        switch (WorkingStatus) {
            case WorkingStatus_Turbo:  //
                if (mCountDownNowSecond >= Five_Minute) {
                    mCountDownNowSecond = 0;
                    WorkingStatus = WorkingStatus + 1;
                    FanGear = FanGear - 1;
                    requestSaveGearValue(FanGear, LightGear);
                }

                break;
            case WorkingStatus_5:

                if (mCountDownNowSecond >= Three_Minute) {  // 3 分钟
                    mCountDownNowSecond = 0;
                    WorkingStatus = WorkingStatus + 1;
                    FanGear = FanGear - 1;
                    requestSaveGearValue(FanGear, LightGear);
                }
               /* if (LightGear == LightValueReal_Turbo_manual) {
                    LightGear = LightValueReal_5manual;
                    requestSaveGearValue(FanGear, LightGear);
                }*/

                break;
            case WorkingStatus_4:

                if (mCountDownNowSecond >= Three_Minute) {  // 3 分钟
                    mCountDownNowSecond = 0;
                    WorkingStatus = WorkingStatus + 1;
                    FanGear = FanGear - 1;
                    requestSaveGearValue(FanGear, LightGear);
                }

               /* if (LightGear == LightValueReal_Turbo_manual || LightGear == LightValueReal_5manual) {
                    LightGear = LightValueReal_4manual;
                    requestSaveGearValue(FanGear, LightGear);
                }*/

                break;
            case WorkingStatus_3:
                if (mCountDownNowSecond >= Three_Minute) {  // 3 分钟
                    mCountDownNowSecond = 0;
                    WorkingStatus = WorkingStatus + 1;
                    FanGear = FanGear - 1;
                    requestSaveGearValue(FanGear, LightGear);
                }

             /*   if (LightGear == LightValueReal_Turbo_manual || LightGear == LightValueReal_5manual || LightGear == LightValueReal_4manual) {
                    LightGear = LightValueReal_3manual;
                    requestSaveGearValue(FanGear, LightGear);
                }*/

                break;
            case WorkingStatus_2:
                if (mCountDownNowSecond >= Three_Minute) {  // 3 分钟
                    mCountDownNowSecond = 0;
                    WorkingStatus = WorkingStatus + 1;
                    FanGear = FanGear - 1;
                    requestSaveGearValue(FanGear, LightGear);
                }
              /*  if (LightGear == LightValueReal_Turbo_manual || LightGear == LightValueReal_5manual
                        || LightGear == LightValueReal_4manual || LightGear == LightValueReal_3manual) {
                    LightGear = LightValueReal_2manual;
                    requestSaveGearValue(FanGear, LightGear);
                }*/
                break;
            case WorkingStatus_1:
                if (mCountDownNowSecond >= Five_Minute) {  // 5 分钟
                    mCountDownNowSecond = 0;
                    resetAllData();
                    requestSaveGearValue(FearValueReal_Poweroff, FearValueReal_Poweroff);  // 烟机和 灯光 都关闭
                    //((HoodPanelFragment.OnHoodPanelFragmentListener) mListener).doBackSwitchToHoodPanel(TFTHoodConstant.HOOD_CLOSE_FAN_LIGHT);
                    EventBus.getDefault().post(new OrderFromHoodPanel(TFTHoodConstant.HOOD_GO_BACK_TO_HOB_PANEL));
                    SettingPreferencesUtil.saveTimerOpenStatus(getActivity(), CataSettingConstant.TIMER_STATUS_CLOSE);
                    //  EventBus .getDefault().post(new OrderFromHoodPanel(TFTHoodConstant.HOOD_TIMER_IS_UP));
                  //  LogUtil.d("liang send order to close ");
                }

               /* if (LightGear == LightValueReal_Turbo_manual || LightGear == LightValueReal_5manual
                        || LightGear == LightValueReal_4manual || LightGear == LightValueReal_3manual || LightGear == LightValueReal_2manual) {
                    LightGear = LightValueReal_1manual;
                    requestSaveGearValue(FanGear, LightGear);
                }*/
                break;
            case WorkingStatus_Silent:
                if (mCountDownNowSecond >= Five_Minute) {  // 5 分钟
                    mCountDownNowSecond = 0;
                    resetAllData();
                    requestSaveGearValue(FearValueReal_Poweroff, FearValueReal_Poweroff);  // 烟机和 灯光 都关闭
                    //((HoodPanelFragment.OnHoodPanelFragmentListener) mListener).doBackSwitchToHoodPanel(TFTHoodConstant.HOOD_CLOSE_FAN_LIGHT);
                    EventBus.getDefault().post(new OrderFromHoodPanel(TFTHoodConstant.HOOD_GO_BACK_TO_HOB_PANEL));
                    SettingPreferencesUtil.saveTimerOpenStatus(getActivity(), CataSettingConstant.TIMER_STATUS_CLOSE);
                    //  EventBus .getDefault().post(new OrderFromHoodPanel(TFTHoodConstant.HOOD_TIMER_IS_UP));
                 //   LogUtil.d("liang send order to close ");
                }

                if (LightGear == LightValueReal_Turbo_manual || LightGear == LightValueReal_5manual
                        || LightGear == LightValueReal_4manual || LightGear == LightValueReal_3manual
                        || LightGear == LightValueReal_2manual || LightGear == LightValueReal_1manual) {
                    LightGear = LightValueReal_Silent;
                    requestSaveGearValue(FanGear, LightGear);
                }

                break;
        }

    }

    private void resetAllData() {
        WorkingStatus = 0;
        CountValue = 0;
        ShowT22UI = false;
        doStopCountDown();
    }

    private void setShowT22UI(int value) {
        if (value == 60) {  // 1 分钟 到，判断是否 要弹出 T22 界面
            if (!ShowT22UI) {
                ShowT22UI = true;
                UpdataUI();   // 弹出 T22 界面
            }
        }
    }


    private void updateBigCycle(int nowSecond) {
        int TotalTime;
        TotalTime = mTotalTime;
        switch (TotalTime) {
            case CountDownTotalTime_Power_25:  // 25 分钟
                bigMyBigCycle.setProgress(nowSecond * 0.24f);
                break;
            case CountDownTotalTime_Power_22:
                bigMyBigCycle.setProgress(nowSecond * 0.3f);
                break;
            case CountDownTotalTime_Power_17:
                bigMyBigCycle.setProgress(nowSecond * 0.35f);
                break;
            case CountDownTotalTime_Power_14:
                bigMyBigCycle.setProgress(nowSecond * 0.42f);
                break;
            case CountDownTotalTime_Power_11:
                bigMyBigCycle.setProgress(nowSecond * 0.54f);
                break;
            case CountDownTotalTime_Power_8:
                bigMyBigCycle.setProgress(nowSecond * 0.75f);
                break;
            case CountDownTotalTime_Power_5:
                bigMyBigCycle.setProgress(nowSecond * 1.2f);
                break;
        }
    }

    private void updateTextViewInsideCycle(int value) {
        int tep1, tep2, tep3;

        tep1 = value / 60;
        tep2 = value % 60;
        if (tep2 == 0) {
            tep3 = mTotalTime - tep1;
            dynamic5Min.setText(Integer.toString(tep3) + " " + "min");
        }
    }


    private void initCycleProgressUI() {
        bigMyBigCycle.setProgress(0);
    }


    private void UpdataUI() {

        bigCycle.setVisibility(View.VISIBLE);

        bigCycle.setScaleX(1.0f);
        bigCycle.setScaleY(1.0f);

    }

    private void initUI() {
        bigCycle.setVisibility(View.VISIBLE);

    }

    private void doStopCountDown() {
        if (mDisposable != null) {
            if (!mDisposable.isDisposed()) mDisposable.dispose();
        }

    }

    @OnClick({R2.id.iv_cancel, R2.id.iv_go_back_to_main_screen, R2.id.iv_small_a_cooker_open,
            R2.id.iv_small_b_cooker_open, R2.id.iv_small_c_cooker_open, R2.id.high_temp_a, R2.id.high_temp_b, R2.id.high_temp_c,R2.id.h9siv,R2.id.h8siv})
    public void onClick(View view) {
        int i = view.getId();

        if (i == R.id.iv_cancel) {

            CountValue = 0;
            ShowT22UI = false;
            doStopCountDown();
            resetAllData();
            SettingPreferencesUtil.saveTimerOpenStatus(getActivity(), CataSettingConstant.TIMER_STATUS_CLOSE);
            // EventBus .getDefault().post(new OrderFromHoodPanel(TFTHoodConstant.HOOD_TIMER_IS_UP));
            if (mType == TFTHoodConstant.TIMER_FROM_H104_SCREEN) {
                ((HoodPanelFragment.OnHoodPanelFragmentListener) mListener).doBackSwitchToHoodPanel(TFTHoodConstant.HOOD_SHOW_HOOD_AGAIN);
               // LogUtil.d("liang go to hood panel ");
            } else if (mType == TFTHoodConstant.TIMER_FROM_MAIN_PANEL_SCREEN) {
                EventBus.getDefault().post(new OrderFromHoodPanel(TFTHoodConstant.HOOD_GO_BACK_TO_HOB_PANEL));
                boolean isFanWorkingAutoStatus = SettingPreferencesUtil.getFanWorkingStatus(getContext()).equals(CataSettingConstant.FAN_WORKING_STATUS_AUTO) ? true : false;
                if (isFanWorkingAutoStatus) {
                    requestSaveGearValue(FearValueReal_Poweroff, FearValueReal_Poweroff);  // 烟机和 灯光 都关闭
                }
               // LogUtil.d("liang show the main panel  from schedule fragment cancel");
            }
        } else if (i == R.id.iv_go_back_to_main_screen || i == R.id.iv_small_a_cooker_open || i == R.id.iv_small_b_cooker_open
                || i == R.id.iv_small_c_cooker_open || i == R.id.high_temp_a || i == R.id.high_temp_b || i == R.id.high_temp_c||i==R.id.h9siv||i==R.id.h8siv) {
            ((HoodPanelFragment.OnHoodPanelFragmentListener) mListener).onHoodPanelFragmentRequestSwitchToHobPanel();

        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GetCookersInformation event) {
        int aCookerMode, bCookerMode, cCookerMode,dCookerMode,eCookerMode,fCookerMode;
        boolean aCooker_HighTemp = false, bCooker_HighTemp = false, cCooker_HighTemp = false;
        boolean dCooker_HighTemp = false, eCooker_HighTemp = false, fCooker_HighTemp = false;
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

        switch (mHobType){
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
                if (aCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF && bCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF && cCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) {
                    ivGoBackToMainScreen.setVisibility(View.VISIBLE);
                    //     LogUtil.d("liang ivGoBackToMainScreen  VISIBLE");
                } else {
                    ivGoBackToMainScreen.setVisibility(View.VISIBLE);
                    //   LogUtil.d("liang ivGoBackToMainScreen  INVISIBLE");
                }

                break;
            case TFT_HOB_TYPE_80:
                if (aCooker_HighTemp) {
                    h8siv.aCookerShowHighTemperature(true);
                } else {
                    h8siv.aCookerShowHighTemperature(false);
                    if (aCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) {  // a 关机
                        h8siv.aCookerShowBlackground();
                    } else {

                    }
                }

                if (bCooker_HighTemp) {
                    h8siv.bCookerShowHighTemperature(true);
                } else {
                    h8siv.bCookerShowHighTemperature(false);
                    if (bCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) {  //  b 关机
                        h8siv.bCookerShowBlackground();
//                LogUtil.d("liang the b cooker is power off");
                    } else {
                    }
                }

                if (cCooker_HighTemp) {
                    h8siv.centerCookerShowHighTemperature(true);
                } else {
                    h8siv.centerCookerShowHighTemperature(false);
                    if (cCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) {  //  c  关机
                        h8siv.centerCookerShowBlackground();
                    } else {

                    }
                }

                if (dCooker_HighTemp) {
                    h8siv.rightCookerShowHighTemperature(true);
                } else {
                    h8siv.rightCookerShowHighTemperature(false);
                    if (dCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) {  //  d  关机
                        h8siv.rightCookerShowBlackground();
                    } else {
                    }
                }

                if(aCooker_HighTemp&&bCooker_HighTemp){  // a b high temperature at the same time
                    h8siv.abCookerShowHighTemperature(true);
                }
                break;
            case TFT_HOB_TYPE_90:
                if (aCooker_HighTemp) {
                    h9siv.aCookerShowHighTemperature(true);
                } else {
                    h9siv.aCookerShowHighTemperature(false);
                    if (aCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) {  // a 关机
                        h9siv.aCookerShowBlackground();
                    } else {

                    }
                }

                if (bCooker_HighTemp) {
                    h9siv.bCookerShowHighTemperature(true);
                } else {
                    h9siv.bCookerShowHighTemperature(false);
                    if (bCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) {  //  b 关机
                        h9siv.bCookerShowBlackground();
//                LogUtil.d("liang the b cooker is power off");
                    } else {
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

                if (eCooker_HighTemp) {
                    h9siv.eCookerShowHighTemperature(true);
                } else {
                    h9siv.eCookerShowHighTemperature(false);
                    if (eCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) {  //  e 关机
                        h9siv.eCookerShowBlackground();
                    } else {
                    }
                }

                if (fCooker_HighTemp) {
                    h9siv.fCookerShowHighTemperature(true);
                } else {
                    h9siv.fCookerShowHighTemperature(false);
                    if (fCookerMode == TFTHoodConstant.HOB_VIEW_WORK_MODE_POWER_OFF) {  //  e 关机
                        h9siv.fCookerShowBlackground();
                    } else {
                    }
                }

                if(aCooker_HighTemp&&bCooker_HighTemp){  // a b high temperature at the same time
                    h9siv.abCookerShowHighTemperature(true);
                }

                if(eCooker_HighTemp&&fCooker_HighTemp){  // e f high temperature at the same time
                    h9siv.efCookerShowHighTemperature(true);
                }
                break;
        }


    }

    public void stopTimer() {
        CountValue = 0;
        ShowT22UI = false;
        doStopCountDown();
        resetAllData();
        SettingPreferencesUtil.saveTimerOpenStatus(getActivity(), CataSettingConstant.TIMER_STATUS_CLOSE);
     //   LogUtil.d("liang close timer ");
    }

    public void closeScheduleFragment() {
        //  ((HoodPanelFragment.OnHoodPanelFragmentListener) mListener).doBackSwitchToHoodPanel(TFTHoodConstant.HOOD_SHOW_HOOD_AGAIN);

        CountValue = 0;
        ShowT22UI = false;
        doStopCountDown();
        resetAllData();
        requestSaveGearValue(FearValueReal_Poweroff, FearValueReal_Poweroff);  // 烟机和 灯光 都关闭
        SettingPreferencesUtil.saveTimerOpenStatus(getActivity(), CataSettingConstant.TIMER_STATUS_CLOSE);
      //  LogUtil.d("liang close timer  2");
    }

    private void showCookersIcons(){
        switch (mHobType) {
            case TFT_HOB_TYPE_60:
             //   ivGoBackToMainScreen.setImageBitmap(getBitmap(R.mipmap.ic_hob_60));
                ivGoBackToMainScreen.setVisibility(View.VISIBLE);
                h8siv.setVisibility(View.INVISIBLE);
                h9siv.setVisibility(View.INVISIBLE);
                break;
            case TFT_HOB_TYPE_80:
                //ivGoBackToMainScreen.setImageBitmap(getBitmap(R.mipmap.ic_hob_80));
                ivGoBackToMainScreen.setVisibility(View.INVISIBLE);
                h8siv.setVisibility(View.VISIBLE);
                h9siv.setVisibility(View.INVISIBLE);
                break;
            case TFT_HOB_TYPE_90:
              //  ivGoBackToMainScreen.setImageBitmap(getBitmap(R.mipmap.ic_hob_90));
                ivGoBackToMainScreen.setVisibility(View.INVISIBLE);
                h8siv.setVisibility(View.INVISIBLE);
                h9siv.setVisibility(View.VISIBLE);
                break;
        }

    }
}
