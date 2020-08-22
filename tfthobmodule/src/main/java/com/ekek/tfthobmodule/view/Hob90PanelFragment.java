package com.ekek.tfthobmodule.view;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseThread;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.commonmodule.utils.Logger;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.database.SettingPreferencesUtil;
import com.ekek.tfthobmodule.CataTFTHobApplication;
import com.ekek.tfthobmodule.R;
import com.ekek.tfthobmodule.base.BaseHobFragment;
import com.ekek.tfthobmodule.constants.TFTHobConstant;
import com.ekek.tfthobmodule.data.CookerSettingData;
import com.ekek.tfthobmodule.entity.CookerDataTable;
import com.ekek.tfthobmodule.entity.CookerDataTableDao;
import com.ekek.tfthobmodule.event.BleBatteryEvent;
import com.ekek.tfthobmodule.event.BlinkEvent;
import com.ekek.tfthobmodule.event.CookerPowerOffEvent;
import com.ekek.tfthobmodule.event.CookerUnionUpdateEvent;
import com.ekek.tfthobmodule.event.CookerUpdateEvent;
import com.ekek.tfthobmodule.event.SoundEvent;
import com.ekek.tfthobmodule.event.UnionEvent;
import com.ekek.tfthobmodule.utils.SoundUtil;
import com.ekek.tfthoodmodule.constants.TFTHoodConstant;
import com.ekek.tfthoodmodule.model.GetCookersInformation;
import com.ekek.tfthoodmodule.model.OrderFromHoodPanel;
import com.ekek.tfthoodmodule.view.HoodPanelFragment;
import com.ekek.viewmodule.common.BatteryIndicatorView;
import com.ekek.viewmodule.contract.HobCircleCookerContract;
import com.ekek.viewmodule.contract.HobRectangleCookerContract;
import com.ekek.viewmodule.hob.Hob80RectangleCookerNew;
import com.ekek.viewmodule.hob.Hob80RectangleCookerTest;
import com.ekek.viewmodule.hob.Hob90CircleCooker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Hob90PanelFragment extends BaseHobFragment {
    public static final int HOB_PANEL_WORK_MODE_NORMAL = 0;
    public static final int HOB_PANEL_WORK_MODE_RECIPES_SELECT_COOKER_TO_COOK = 1;
    public static final int HOB_PANEL_WORK_MODE_RECIPES_ADJUST_TO_COOK = 2;
    public static final int HOB_PANEL_WORK_MODE_TEST = 3;

    @BindView(R.id.hrc_a)
    Hob80RectangleCookerNew hrcA;
    @BindView(R.id.hbcc_b)
    Hob90CircleCooker hbccB;
    @BindView(R.id.hrc_c)
    Hob80RectangleCookerNew hrcC;
    @BindView(R.id.ib_hood)
    ImageButton ibHood;
    @BindView(R.id.tv_demo)
    TextView tvDemo;
    @BindView(R.id.ib_hood_mask)
    ImageButton ibHoodMask;
    Unbinder unbinder;
    @BindView(R.id.h8rc_ab)
    Hob80RectangleCookerTest h8rcAb;
    @BindView(R.id.h8rc_ef)
    Hob80RectangleCookerTest h8rcEf;
    @BindView(R.id.biv_bluetooth)
    BatteryIndicatorView bivBluetooth;
    @BindView(R.id.ll_bottom_view)
    LinearLayout llBottomView;
    @BindView(R.id.fl_hood_view)
    FrameLayout flHoodView;
    private int hobPanelWorkMode = HOB_PANEL_WORK_MODE_NORMAL;
    private CookerSettingData recipesSettingData;


    private boolean aIsCookerHighTemp = false;
    private boolean bIsCookerHighTemp = false;
    private boolean cIsCookerHighTemp = false;
    private boolean eIsCookerHighTemp = false;
    private boolean fIsCookerHighTemp = false;
    private boolean abIsCookerHighTemp = false;
    private boolean efIsCookerHighTemp = false;

    private boolean aIsCookerOff = false;
    private boolean bIsCookerOff = false;
    private boolean cIsCookerOff = false;
    private boolean eIsCookerOff = false;
    private boolean fIsCookerOff = false;
    private boolean abIsCookerOff = false;
    private boolean efIsCookerOff = false;

    private int aCookerWorkMode = 0;
    private int bCookerWorkMode = 0;
    private int cCookerWorkMode = 0;
    private int eCookerWorkMode = 0;
    private int fCookerWorkMode = 0;
    private int abCookerWorkMode = 0;
    private int efCookerWorkMode = 0;

    private boolean mTimerIsWorking = false;

    private boolean mTheCookerHasBeenOpen = false;

    boolean isTimerDoingNow = false;

    private int aCookerHobLevel = 0;
    private int bCookerHobLevel = 0;
    private int cCookerHobLevel = 0;
    private int dCookerHobLevel = 0;
    private int eCookerHobLevel = 0;
    private int fCookerHobLevel = 0;
    private int abCookerHobLevel = 0;
    private int efCookerHobLevel = 0;
    private boolean abWorkingNow=false ;
    private boolean efWorkingNow=false ;

    private int mHoodLevel = 0;
    private BackgroundWorkingThread backgroundWorkingThread = null;

    private ValueAnimator alphaAnim;

    private static final int ALPHA_MAX = 255;
    private static final int ALPHA_MIN = 0;
    private static final String ANIM_ATTRIBUTE = "imageAlpha";
    private static final int ANIM_DURATION = 1000;
    private boolean mIsFanIconFlash = false;

    private ArgbEvaluator evaluator = new ArgbEvaluator();
    private Map<Integer, Bitmap> bitmapMap = new HashMap<>();

    private static final int HANDLER_UPDATE_BT_BATTERY_LEVEL_UI = 6;
    private static final int HANDLER_UPDATE_BT_BATTERY_UI = 7;
    public static final int HANDLER_SHOW_TIMER_H104=8;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_UPDATE_BT_BATTERY_LEVEL_UI:
                    if (bivBluetooth.getVisibility() == View.GONE) {
                        bivBluetooth.setVisibility(View.VISIBLE);
                        if (ibHood.getVisibility() == View.GONE && ibHoodMask.getVisibility() == View.GONE) llBottomView.setPadding(75, 0, 0, 0);
                        else if (ibHood.getVisibility() == View.VISIBLE || ibHoodMask.getVisibility() == View.VISIBLE) llBottomView.setPadding(26, 0, 0, 0);

                    }

                    bivBluetooth.updateView(msg.arg1, msg.arg2);
                    break;
                case HANDLER_UPDATE_BT_BATTERY_UI:
                    if (msg.arg1 == BleBatteryEvent.BATTERY_VIEW_STATUS_HIDE) {
                        bivBluetooth.setVisibility(View.GONE);
                        llBottomView.setPadding(60, 0, 0, 0);
                    } else if (msg.arg1 == BleBatteryEvent.BATTERY_VIEW_STATUS_SHOW) {

                        bivBluetooth.setVisibility(View.VISIBLE);
                        if (ibHood.getVisibility() == View.GONE && ibHoodMask.getVisibility() == View.GONE) llBottomView.setPadding(75, 0, 0, 0);
                        else if (ibHood.getVisibility() == View.VISIBLE || ibHoodMask.getVisibility() == View.VISIBLE) llBottomView.setPadding(26, 0, 0, 0);

                    }
                    break;
                case HANDLER_SHOW_TIMER_H104:
                    EventBus.getDefault().post(new OrderFromHoodPanel(TFTHoodConstant.TIMER_FROM_MAIN_PANEL_SCREEN));
                    break;


            }
        }
    };

    @Override
    public int initLyaout() {
        return R.layout.tfthobmodule_fragment_hob_90_panel;
    }

    @Override
    public void initListener() {

    }

    @Override
    public void setMode(int mode) {
        hobPanelWorkMode = mode;
        if (hobPanelWorkMode == HOB_PANEL_WORK_MODE_NORMAL) {
            recipesSettingData = null;
        }
    }

    @Override
    public void setRecipesSettingData(CookerSettingData data) {
        recipesSettingData = data;

        //for test
        //recipesSettingData.setTimerMinuteValue(1);
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        tvDemo.setTypeface(GlobalVars.getInstance().getDefaultFontBold());
        hbccB.setOnHobCircleCookerListener(new HobCircleCookerContract.OnHobCircleCookerListener() {
            @Override
            public void onCookerPowerOff() {
                ((OnHob90PanelFragmentListener) mListener).onHob90PanelFragmentRequestCookerPowerOff(hbccB.getCookerID());

                int soundType = SoundEvent.getSoundType(hbccB.getCookerID(), true);
                EventBus.getDefault().post(new SoundEvent(
                        SoundEvent.SOUND_ACTION_PAUSE,
                        SoundUtil.SOUND_ID_ALARM_TIMER,
                        soundType));
                Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PAUSE, SoundUtil.SOUND_ID_ALARM_TIMER, " + soundType + ")");

                soundType = SoundEvent.getSoundType(hbccB.getCookerID(), false);
                EventBus.getDefault().post(new SoundEvent(
                        SoundEvent.SOUND_ACTION_PAUSE,
                        SoundUtil.SOUND_ID_ALARM,
                        soundType));
                Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PAUSE, SoundUtil.SOUND_ID_ALARM, " + soundType + ")");
            }

            @Override
            public void onCookerPowerOn() {
                if (hobPanelWorkMode == HOB_PANEL_WORK_MODE_NORMAL) {
                    doPowerOn(hbccB.getCookerID());
                } else if (hobPanelWorkMode == HOB_PANEL_WORK_MODE_RECIPES_SELECT_COOKER_TO_COOK) {
                    if (recipesSettingData != null) {
                        recipesSettingData.setCookerID(hbccB.getCookerID());
                        doSelectCookerToCook();
                    }
                } else if (hobPanelWorkMode == HOB_PANEL_WORK_MODE_RECIPES_ADJUST_TO_COOK) {
                    if (recipesSettingData != null) {
                        recipesSettingData.setCookerID(hbccB.getCookerID());
                        doAjustToCook();
                    }

                }
            }

            @Override
            public void onSetGear() {
                doSetGear(hbccB.getCookerID(), hbccB.getHourValue(), hbccB.getMinuteValue());
            }

            @Override
            public void onSetTimer() {
                doSetTimer(hbccB.getCookerID(), hbccB.getHourValue(), hbccB.getMinuteValue());
            }

            @Override
            public void onReadyToCook() {
                doReadyToCook(hbccB.getCookerID());
            }

            @Override
            public void onRequestAddTenMinute() {
                doRequestAddTenMinute(hbccB.getCookerID());
            }

            @Override
            public void onRequestKeepWarm() {
                doRequestKeepWarm(hbccB.getCookerID());
            }
        });

        hrcA.setOnHobRectangleCookerListener(new HobRectangleCookerContract.OnHobRectangleCookerListener() {
            @Override
            public void onCookerPowerOff(int cookerID) {
                ((OnHob90PanelFragmentListener) mListener).onHob90PanelFragmentRequestCookerPowerOff(cookerID);

                int soundType = SoundEvent.getSoundType(cookerID, true);
                EventBus.getDefault().post(new SoundEvent(
                        SoundEvent.SOUND_ACTION_PAUSE,
                        SoundUtil.SOUND_ID_ALARM_TIMER,
                        soundType));
                Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PAUSE, SoundUtil.SOUND_ID_ALARM_TIMER, " + soundType + ")");

                soundType = SoundEvent.getSoundType(cookerID, false);
                EventBus.getDefault().post(new SoundEvent(
                        SoundEvent.SOUND_ACTION_PAUSE,
                        SoundUtil.SOUND_ID_ALARM,
                        soundType));
                Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PAUSE, SoundUtil.SOUND_ID_ALARM, " + soundType + ")");
            }

            @Override
            public void onCookerPowerOn(int cookerID) {
                if (hobPanelWorkMode == HOB_PANEL_WORK_MODE_NORMAL) {
                    //LogUtil.d("samhung1------------------samhung1");
                    EventBus.getDefault().post(new UnionEvent());
                    doPowerOn(cookerID);
                } else if (hobPanelWorkMode == HOB_PANEL_WORK_MODE_RECIPES_SELECT_COOKER_TO_COOK) {
                    if (recipesSettingData != null) {
                        recipesSettingData.setCookerID(cookerID);
                        doSelectCookerToCook();
                    }
                } else if (hobPanelWorkMode == HOB_PANEL_WORK_MODE_RECIPES_ADJUST_TO_COOK) {
                    if (recipesSettingData != null) {
                        recipesSettingData.setCookerID(cookerID);
                        doAjustToCook();
                    }

                }
            }

            @Override
            public void onSetGear(int cookerID) {
                doSetGear(cookerID, hrcA.getHourValue(cookerID), hrcA.getMinuteValue(cookerID));

            }

            @Override
            public void onSetTimer(int cookerID) {
                doSetTimer(cookerID, hrcA.getHourValue(cookerID), hrcA.getMinuteValue(cookerID));
            }

            @Override
            public void onReadyToCook(int cookerID) {
                doReadyToCook(cookerID);
            }

            @Override
            public void onRequestAddTenMinute(int cookerID) {
                doRequestAddTenMinute(cookerID);
            }

            @Override
            public void onRequestKeepWarm(int cookerID) {
                doRequestKeepWarm(cookerID);
            }
        });

        hrcC.setOnHobRectangleCookerListener(new HobRectangleCookerContract.OnHobRectangleCookerListener() {
            @Override
            public void onCookerPowerOff(int cookerID) {
                ((OnHob90PanelFragmentListener) mListener).onHob90PanelFragmentRequestCookerPowerOff(cookerID);

                int soundType = SoundEvent.getSoundType(cookerID, true);
                EventBus.getDefault().post(new SoundEvent(
                        SoundEvent.SOUND_ACTION_PAUSE,
                        SoundUtil.SOUND_ID_ALARM_TIMER,
                        soundType));
                Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PAUSE, SoundUtil.SOUND_ID_ALARM_TIMER, " + soundType + ")");

                soundType = SoundEvent.getSoundType(cookerID, false);
                EventBus.getDefault().post(new SoundEvent(
                        SoundEvent.SOUND_ACTION_PAUSE,
                        SoundUtil.SOUND_ID_ALARM,
                        soundType));
                Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PAUSE, SoundUtil.SOUND_ID_ALARM, " + soundType + ")");

            }

            @Override
            public void onCookerPowerOn(int cookerID) {
                if (hobPanelWorkMode == HOB_PANEL_WORK_MODE_NORMAL) {
                    EventBus.getDefault().post(new UnionEvent());
                    doPowerOn(cookerID);
                } else if (hobPanelWorkMode == HOB_PANEL_WORK_MODE_RECIPES_SELECT_COOKER_TO_COOK) {
                    if (recipesSettingData != null) {
                        recipesSettingData.setCookerID(cookerID);
                        doSelectCookerToCook();
                    }
                } else if (hobPanelWorkMode == HOB_PANEL_WORK_MODE_RECIPES_ADJUST_TO_COOK) {
                    if (recipesSettingData != null) {
                        recipesSettingData.setCookerID(cookerID);
                        doAjustToCook();
                    }

                }
            }

            @Override
            public void onSetGear(int cookerID) {
                doSetGear(cookerID, hrcC.getHourValue(cookerID), hrcC.getMinuteValue(cookerID));
            }

            @Override
            public void onSetTimer(int cookerID) {
                doSetTimer(cookerID, hrcC.getHourValue(cookerID), hrcC.getMinuteValue(cookerID));
            }

            @Override
            public void onReadyToCook(int cookerID) {
                doReadyToCook(cookerID);
            }

            @Override
            public void onRequestAddTenMinute(int cookerID) {
                doRequestAddTenMinute(cookerID);
            }

            @Override
            public void onRequestKeepWarm(int cookerID) {
                doRequestKeepWarm(cookerID);
            }
        });

        h8rcAb.setOnHobRectangleCookerListener(new Hob80RectangleCookerTest.OnHobRectangleCookerListener() {
            @Override
            public void onCookerPowerOff(int cookerID) {
                ((OnHob90PanelFragmentListener) mListener).onHob90PanelFragmentRequestCookerPowerOff(cookerID);

                int soundType = SoundEvent.getSoundType(cookerID, true);
                EventBus.getDefault().post(new SoundEvent(
                        SoundEvent.SOUND_ACTION_PAUSE,
                        SoundUtil.SOUND_ID_ALARM_TIMER,
                        soundType));
                Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PAUSE, SoundUtil.SOUND_ID_ALARM_TIMER, " + soundType + ")");

                soundType = SoundEvent.getSoundType(cookerID, false);
                EventBus.getDefault().post(new SoundEvent(
                        SoundEvent.SOUND_ACTION_PAUSE,
                        SoundUtil.SOUND_ID_ALARM,
                        soundType));
                Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PAUSE, SoundUtil.SOUND_ID_ALARM, " + soundType + ")");
            }

            @Override
            public void onCookerPowerOn(int cookerID) {
                LogUtil.d("Enter:: new 80 cooker onCookerPowerOn--->" + cookerID);
                if (hobPanelWorkMode == HOB_PANEL_WORK_MODE_NORMAL) {
                    EventBus.getDefault().post(new UnionEvent());
                    doPowerOn(cookerID);
                } else if (hobPanelWorkMode == HOB_PANEL_WORK_MODE_RECIPES_SELECT_COOKER_TO_COOK) {
                    if (recipesSettingData != null) {
                        recipesSettingData.setCookerID(cookerID);
                        doSelectCookerToCook();
                    }
                } else if (hobPanelWorkMode == HOB_PANEL_WORK_MODE_RECIPES_ADJUST_TO_COOK) {
                    if (recipesSettingData != null) {
                        recipesSettingData.setCookerID(cookerID);
                        doAjustToCook();
                    }

                }
            }

            @Override
            public void onSetGear(int cookerID) {
                doSetGear(cookerID, h8rcAb.getHourValue(cookerID), h8rcAb.getMinuteValue(cookerID));
            }

            @Override
            public void onSetTimer(int cookerID) {
                doSetTimer(cookerID, h8rcAb.getHourValue(cookerID), h8rcAb.getMinuteValue(cookerID));
            }

            @Override
            public void onReadyToCook(int cookerID) {
                doReadyToCook(cookerID);
            }

            @Override
            public void onRequestAddTenMinute(int cookerID) {
                doRequestAddTenMinute(cookerID);
            }

            @Override
            public void onRequestKeepWarm(int cookerID) {
                doRequestKeepWarm(cookerID);
            }
        });

        h8rcEf.setOnHobRectangleCookerListener(new Hob80RectangleCookerTest.OnHobRectangleCookerListener() {
            @Override
            public void onCookerPowerOff(int cookerID) {
                ((OnHob90PanelFragmentListener) mListener).onHob90PanelFragmentRequestCookerPowerOff(cookerID);

                int soundType = SoundEvent.getSoundType(cookerID, true);
                EventBus.getDefault().post(new SoundEvent(
                        SoundEvent.SOUND_ACTION_PAUSE,
                        SoundUtil.SOUND_ID_ALARM_TIMER,
                        soundType));
                Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PAUSE, SoundUtil.SOUND_ID_ALARM_TIMER, " + soundType + ")");

                soundType = SoundEvent.getSoundType(cookerID, false);
                EventBus.getDefault().post(new SoundEvent(
                        SoundEvent.SOUND_ACTION_PAUSE,
                        SoundUtil.SOUND_ID_ALARM,
                        soundType));
                Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PAUSE, SoundUtil.SOUND_ID_ALARM, " + soundType + ")");
            }

            @Override
            public void onCookerPowerOn(int cookerID) {
                LogUtil.d("Enter:: new 80 cooker onCookerPowerOn--->" + cookerID);
                if (hobPanelWorkMode == HOB_PANEL_WORK_MODE_NORMAL) {
                    EventBus.getDefault().post(new UnionEvent());
                    doPowerOn(cookerID);
                } else if (hobPanelWorkMode == HOB_PANEL_WORK_MODE_RECIPES_SELECT_COOKER_TO_COOK) {
                    if (recipesSettingData != null) {
                        recipesSettingData.setCookerID(cookerID);
                        doSelectCookerToCook();
                    }
                } else if (hobPanelWorkMode == HOB_PANEL_WORK_MODE_RECIPES_ADJUST_TO_COOK) {
                    if (recipesSettingData != null) {
                        recipesSettingData.setCookerID(cookerID);
                        doAjustToCook();
                    }

                }
            }

            @Override
            public void onSetGear(int cookerID) {
                doSetGear(cookerID, h8rcEf.getHourValue(cookerID), h8rcEf.getMinuteValue(cookerID));
            }

            @Override
            public void onSetTimer(int cookerID) {
                doSetTimer(cookerID, h8rcEf.getHourValue(cookerID), h8rcEf.getMinuteValue(cookerID));
            }

            @Override
            public void onReadyToCook(int cookerID) {
                doReadyToCook(cookerID);
            }

            @Override
            public void onRequestAddTenMinute(int cookerID) {
                doRequestAddTenMinute(cookerID);
            }

            @Override
            public void onRequestKeepWarm(int cookerID) {
                doRequestKeepWarm(cookerID);
            }
        });


        ((OnHob90PanelFragmentListener) mListener).onHob90PanelFragmentBindCooker(hbccB.getCookerID());
     /*   int[] cCookerIDs = hrcC.getCookerIDs();
        for (int i = 0; i < cCookerIDs.length; i++) {
            ((OnHob90PanelFragmentListener) mListener).onHob90PanelFragmentBindCooker(cCookerIDs[i]);

        }
        int[] aCookerIDs = hrcA.getCookerIDs();
        for (int i = 0; i < aCookerIDs.length; i++) {
            ((OnHob90PanelFragmentListener) mListener).onHob90PanelFragmentBindCooker(aCookerIDs[i]);
        }*/

        int[] cCookerIDs = h8rcAb.getCookerIDs();
        for (int i = 0; i < cCookerIDs.length; i++) {
            ((OnHob90PanelFragmentListener) mListener).onHob90PanelFragmentBindCooker(cCookerIDs[i]);

        }
        int[] aCookerIDs = h8rcEf.getCookerIDs();
        for (int i = 0; i < aCookerIDs.length; i++) {
            ((OnHob90PanelFragmentListener) mListener).onHob90PanelFragmentBindCooker(aCookerIDs[i]);
        }
        showFanIconDownRight();
    }

    private void doRequestAddTenMinute(int cookerID) {
        ((OnHob90PanelFragmentListener) mListener).onHob90PanelFragmentRequestAddTenMinute(cookerID);

        int soundType = SoundEvent.getSoundType(cookerID, true);
        EventBus.getDefault().post(new SoundEvent(
                SoundEvent.SOUND_ACTION_PAUSE,
                SoundUtil.SOUND_ID_ALARM_TIMER,
                soundType));
        Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PAUSE, SoundUtil.SOUND_ID_ALARM_TIMER, " + soundType + ")");

    }

    private void doReadyToCook(int cookerID) {
        ((OnHob90PanelFragmentListener) mListener).onHob90PanelFragmentRequestReadyToCook(cookerID);

        if (cookerID > 100) {
            int soundTypeH = SoundEvent.getSoundType(cookerID % 10, false);
            EventBus.getDefault().post(new SoundEvent(
                    SoundEvent.SOUND_ACTION_PAUSE,
                    SoundUtil.SOUND_ID_ALARM,
                    soundTypeH));
            Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PAUSE, SoundUtil.SOUND_ID_ALARM, " + soundTypeH + ")");

            int soundTypeL = SoundEvent.getSoundType(cookerID / 10, false);
            EventBus.getDefault().post(new SoundEvent(
                    SoundEvent.SOUND_ACTION_PAUSE,
                    SoundUtil.SOUND_ID_ALARM,
                    soundTypeL));
            Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PAUSE, SoundUtil.SOUND_ID_ALARM, " + soundTypeL + ")");

        } else {
            int soundType = SoundEvent.getSoundType(cookerID, false);
            EventBus.getDefault().post(new SoundEvent(
                    SoundEvent.SOUND_ACTION_PAUSE,
                    SoundUtil.SOUND_ID_ALARM,
                    soundType));
            Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PAUSE, SoundUtil.SOUND_ID_ALARM, " + soundType + ")");
        }
    }

    private void doRequestKeepWarm(int cookerID) {
        ((OnHob90PanelFragmentListener) mListener).onHob90PanelFragmentRequestKeepWarm(cookerID);

        int soundType = SoundEvent.getSoundType(cookerID, true);
        EventBus.getDefault().post(new SoundEvent(
                SoundEvent.SOUND_ACTION_PAUSE,
                SoundUtil.SOUND_ID_ALARM_TIMER,
                soundType));
        Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PAUSE, SoundUtil.SOUND_ID_ALARM_TIMER, " + soundType + ")");
    }

    private void doSetTimer(int cookerID, int remainHour, int remainMinute) {
        if (cookerID > 100) {
            int id = cookerID / 10;
            CookerDataTableDao cookerDataTableDao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
            CookerDataTable cookerData = cookerDataTableDao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(id)).build().unique();
            if (cookerData == null) {
                CookerSettingData data = new CookerSettingData(cookerID);
                ((OnHob90PanelFragmentListener) mListener).onHob90PanelFragmentRequestSetCooker(data);
            } else {
                CookerSettingData data = new CookerSettingData(cookerID);
                data.setCookerSettingMode(CookerSettingData.SETTING_MODE_TIMER);
                data.setTempMode(cookerData.getSettingTempMode());
                data.setFireValue(cookerData.getSettingFireValue());
                data.setTempValue(cookerData.getSettingtempValue());
                data.setTimerHourValue(cookerData.getSettingHourValue());
                data.setTimerMinuteValue(cookerData.getSettingMinuteValue());
                data.setTimerSecondValue(cookerData.getSettingSecondValue());
                data.setPreSettingtimerHourValue(cookerData.getSettingHourValue());
                data.setPreSettingtimerMinuteValue(cookerData.getSettingMinuteValue());
                data.setPreSettingtimerSecondValue(cookerData.getSettingSecondValue());
                data.setTempIdentifyDrawableResourceID(cookerData.getSettingtempIndicatorResID());
                data.setTempRecipesResID(cookerData.getRecipesResID());
                data.setTimerRemainHourValue(remainHour);
                data.setTimerRemainMinuteValue(remainMinute);
                ((OnHob90PanelFragmentListener) mListener).onHob90PanelFragmentRequestSetCooker(data);
            }
        } else {
            CookerDataTableDao cookerDataTableDao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
            CookerDataTable cookerData = cookerDataTableDao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(cookerID)).build().unique();
            if (cookerData == null) {
                CookerSettingData data = new CookerSettingData(cookerID);
                ((OnHob90PanelFragmentListener) mListener).onHob90PanelFragmentRequestSetCooker(data);
            } else {
                CookerSettingData data = new CookerSettingData(cookerID);
                data.setCookerSettingMode(CookerSettingData.SETTING_MODE_TIMER);
                data.setTempMode(cookerData.getSettingTempMode());
                data.setFireValue(cookerData.getSettingFireValue());
                data.setTempValue(cookerData.getSettingtempValue());
                data.setTimerHourValue(cookerData.getSettingHourValue());
                data.setTimerMinuteValue(cookerData.getSettingMinuteValue());
                data.setTimerSecondValue(cookerData.getSettingSecondValue());
                data.setPreSettingtimerHourValue(cookerData.getSettingHourValue());
                data.setPreSettingtimerMinuteValue(cookerData.getSettingMinuteValue());
                data.setPreSettingtimerSecondValue(cookerData.getSettingSecondValue());
                data.setTempIdentifyDrawableResourceID(cookerData.getSettingtempIndicatorResID());
                data.setTempRecipesResID(cookerData.getRecipesResID());
                data.setTimerRemainHourValue(remainHour);
                data.setTimerRemainMinuteValue(remainMinute);
                ((OnHob90PanelFragmentListener) mListener).onHob90PanelFragmentRequestSetCooker(data);
            }
        }


    }

    private void doSetGear(int cookerID, int remainHour, int remainMinute) {
        // unregisterEventBus();
        if (cookerID > 100) {
            int id = cookerID / 10;
            CookerDataTableDao cookerDataTableDao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
            CookerDataTable cookerData = cookerDataTableDao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(id)).build().unique();
            if (cookerData == null) {
                CookerSettingData data = new CookerSettingData(cookerID);
                ((OnHob90PanelFragmentListener) mListener).onHob90PanelFragmentRequestSetCooker(data);
            } else {
                CookerSettingData data = new CookerSettingData(cookerID);
                LogUtil.d("workmode----->" + cookerData.getWorkMode());
                if (cookerData.getWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_FIRE_GEAR_WITHOUT_TIMER || cookerData.getWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_FIRE_GEAR_WITH_TIMER) {
                    data.setCookerSettingMode(CookerSettingData.SETTING_MODE_FIRE_GEAR);
                } else if (cookerData.getWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_ABNORMAL_NO_PAN) {
                    if (cookerData.getSaveWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_FIRE_GEAR_WITHOUT_TIMER || cookerData.getSaveWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_FIRE_GEAR_WITH_TIMER) {
                        data.setCookerSettingMode(CookerSettingData.SETTING_MODE_FIRE_GEAR);

                    } else {
                        if (cookerData.getSaveTempMode() == TFTHobConstant.TEMP_MODE_FAST_TEMP) {
                            data.setCookerSettingMode(CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR);
                        } else if (cookerData.getSaveTempMode() == TFTHobConstant.TEMP_MODE_PRECISE_TEMP) {
                            data.setCookerSettingMode(CookerSettingData.SETTING_MODE_PRECISE_TEMP_GEAR);
                        } else {
                            data.setCookerSettingMode(CookerSettingData.SETTING_MODE_TEMP_TYPE_CHOOSEN);
                        }
                    }


                } else {
                    if (cookerData.getTempMode() == TFTHobConstant.TEMP_MODE_FAST_TEMP) {
                        data.setCookerSettingMode(CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR);
                    } else if (cookerData.getTempMode() == TFTHobConstant.TEMP_MODE_PRECISE_TEMP) {
                        data.setCookerSettingMode(CookerSettingData.SETTING_MODE_PRECISE_TEMP_GEAR);
                    } else {
                        data.setCookerSettingMode(CookerSettingData.SETTING_MODE_TEMP_TYPE_CHOOSEN);
                    }

                }

                data.setTempMode(cookerData.getSettingTempMode());
                data.setFireValue(cookerData.getSettingFireValue());
                data.setTempValue(cookerData.getSettingtempValue());
                data.setTimerHourValue(cookerData.getSettingHourValue());
                data.setTimerMinuteValue(cookerData.getSettingMinuteValue());
                data.setTimerSecondValue(cookerData.getSettingSecondValue());
                data.setPreSettingtimerHourValue(cookerData.getSettingHourValue());
                data.setPreSettingtimerMinuteValue(cookerData.getSettingMinuteValue());
                data.setPreSettingtimerSecondValue(cookerData.getSettingSecondValue());
                data.setTempIdentifyDrawableResourceID(cookerData.getSettingtempIndicatorResID());
                data.setTempRecipesResID(cookerData.getRecipesResID());
                data.setTimerRemainHourValue(remainHour);
                data.setTimerRemainMinuteValue(remainMinute);
                ((OnHob90PanelFragmentListener) mListener).onHob90PanelFragmentRequestSetCooker(data);
            }
        } else {
            CookerDataTableDao cookerDataTableDao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
            CookerDataTable cookerData = cookerDataTableDao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(cookerID)).build().unique();
            if (cookerData == null) {
                CookerSettingData data = new CookerSettingData(cookerID);
                ((OnHob90PanelFragmentListener) mListener).onHob90PanelFragmentRequestSetCooker(data);
            } else {
                CookerSettingData data = new CookerSettingData(cookerID);
                LogUtil.d("workmode----->" + cookerData.getWorkMode());
                if (cookerData.getWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_FIRE_GEAR_WITHOUT_TIMER || cookerData.getWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_FIRE_GEAR_WITH_TIMER) {
                    data.setCookerSettingMode(CookerSettingData.SETTING_MODE_FIRE_GEAR);
                } else if (cookerData.getWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_ABNORMAL_NO_PAN) {
                    if (cookerData.getSaveWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_FIRE_GEAR_WITHOUT_TIMER || cookerData.getSaveWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_FIRE_GEAR_WITH_TIMER) {
                        data.setCookerSettingMode(CookerSettingData.SETTING_MODE_FIRE_GEAR);
                    } else {
                        if (cookerData.getSaveTempMode() == TFTHobConstant.TEMP_MODE_FAST_TEMP) {
                            data.setCookerSettingMode(CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR);
                        } else if (cookerData.getSaveTempMode() == TFTHobConstant.TEMP_MODE_PRECISE_TEMP) {
                            data.setCookerSettingMode(CookerSettingData.SETTING_MODE_PRECISE_TEMP_GEAR);
                        } else {
                            data.setCookerSettingMode(CookerSettingData.SETTING_MODE_TEMP_TYPE_CHOOSEN);
                        }
                    }

                } else {
                    if (cookerData.getTempMode() == TFTHobConstant.TEMP_MODE_FAST_TEMP) {
                        data.setCookerSettingMode(CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR);
                    } else if (cookerData.getTempMode() == TFTHobConstant.TEMP_MODE_PRECISE_TEMP) {
                        data.setCookerSettingMode(CookerSettingData.SETTING_MODE_PRECISE_TEMP_GEAR);
                    } else {
                        data.setCookerSettingMode(CookerSettingData.SETTING_MODE_TEMP_TYPE_CHOOSEN);
                    }

                }

                data.setTempMode(cookerData.getSettingTempMode());
                data.setFireValue(cookerData.getSettingFireValue());
                data.setTempValue(cookerData.getSettingtempValue());
                data.setTimerHourValue(cookerData.getSettingHourValue());
                data.setTimerMinuteValue(cookerData.getSettingMinuteValue());
                data.setTimerSecondValue(cookerData.getSettingSecondValue());
                data.setPreSettingtimerHourValue(cookerData.getSettingHourValue());
                data.setPreSettingtimerMinuteValue(cookerData.getSettingMinuteValue());
                data.setPreSettingtimerSecondValue(cookerData.getSettingSecondValue());
                data.setTempIdentifyDrawableResourceID(cookerData.getSettingtempIndicatorResID());
                data.setTempRecipesResID(cookerData.getRecipesResID());
                data.setTimerRemainHourValue(remainHour);
                data.setTimerRemainMinuteValue(remainMinute);
                ((OnHob90PanelFragmentListener) mListener).onHob90PanelFragmentRequestSetCooker(data);
            }
        }


    }

    private void doPowerOn(int cookerID) {
        CookerDataTableDao cookerDataTableDao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
        CookerDataTable cookerData = cookerDataTableDao.queryBuilder().where(CookerDataTableDao.Properties.CookerID.eq(cookerID)).build().unique();
        if (cookerData == null) {
            CookerSettingData data = new CookerSettingData(cookerID);
            LogUtil.d("Enter:: do power on fire------>" + data.getFireValue());
            ((OnHob90PanelFragmentListener) mListener).onHob90PanelFragmentRequestSetCooker(data);
        } else {
            CookerSettingData data = new CookerSettingData(cookerID);
            data.setTempMode(cookerData.getSettingTempMode());
            data.setFireValue(cookerData.getSettingFireValue());
            data.setTempValue(cookerData.getSettingtempValue());
            data.setTimerHourValue(cookerData.getSettingHourValue());
            data.setTimerMinuteValue(cookerData.getSettingMinuteValue());
            data.setTimerSecondValue(cookerData.getSettingSecondValue());
            data.setPreSettingtimerHourValue(cookerData.getSettingHourValue());
            data.setPreSettingtimerMinuteValue(cookerData.getSettingMinuteValue());
            data.setPreSettingtimerSecondValue(cookerData.getSettingSecondValue());
            data.setTempIdentifyDrawableResourceID(cookerData.getSettingtempIndicatorResID());
            data.setTempRecipesResID(cookerData.getRecipesResID());
            ((OnHob90PanelFragmentListener) mListener).onHob90PanelFragmentRequestSetCooker(data);
        }

    }

    private void doSelectCookerToCook() {
        ((OnHob90PanelFragmentListener) mListener).onHob90PanelFragmentRequestSelectCooker(recipesSettingData);
    }

    private void doAjustToCook() {
        ((OnHob90PanelFragmentListener) mListener).onHob90PanelFragmentRequestAdjustCookerSettingDataToCook(recipesSettingData);
    }

    private int getTheHoodLevelFromHodLevel(int temperature) {
        int value = 0;
        if (temperature >= 40 && temperature < 60) {
            value = 4;
        } else if (temperature >= 60 && temperature < 80) {
            value = 6;
        } else if (temperature >= 80 && temperature < 100) {
            value = 8;
        } else if (temperature >= 100) {
            value = 9;
        } else {
            value = 0;
        }
        return value;
    }

    private int getCookerLevel(int fire, int temperature) {
        int value = 0;
        if (fire != -1) { //有火力档位
            value = fire;
        } else if (temperature != -1) { // 有温度档位
            value = getTheHoodLevelFromHodLevel(temperature);
        } else {
            value = 0;
        }
        return value;
    }

    private int getHoodLevel(int hobLevel) {
        int value = 0;
        int autoMode = SettingPreferencesUtil.getHoodOptionsAutoMode(getActivity());
        switch (autoMode) {
            case CataSettingConstant.HOOD_OPTIONS_AUTO_MODE_INTENSE:
                if (hobLevel >= 1 && hobLevel <= 4) {
                    value = 2;
                } else if (hobLevel >= 5 && hobLevel <= 9) {
                    value = 3;
                } else if (hobLevel >= 10 && hobLevel <= 14) {
                    value = 4;
                } else if (hobLevel >= 15 && hobLevel <= 19) {
                    value = 5;
                } else if (hobLevel > 19) {
                    value = 6;
                }
                break;
            case CataSettingConstant.HOOD_OPTIONS_AUTO_MODE_ECOLOGIC:
                if (hobLevel >= 1 && hobLevel <= 5) {
                    value = 1;
                } else if (hobLevel >= 6 && hobLevel <= 10) {
                    value = 2;
                } else if (hobLevel >= 11 && hobLevel <= 15) {
                    value = 3;
                } else if (hobLevel >= 16 && hobLevel <= 21) {
                    value = 4;
                } else if (hobLevel >= 22 && hobLevel <= 27) {
                    value = 5;
                } else if (hobLevel > 27) {
                    value = 6;
                }
                break;
            default:
                if (hobLevel >= 1 && hobLevel <= 4) {
                    value = 2;
                } else if (hobLevel >= 5 && hobLevel <= 9) {
                    value = 3;
                } else if (hobLevel >= 10 && hobLevel <= 14) {
                    value = 4;
                } else if (hobLevel >= 15 && hobLevel <= 19) {
                    value = 5;
                } else if (hobLevel > 19) {
                    value = 6;
                }
                break;

        }


        return value;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BleBatteryEvent event) {
        Message message = new Message();
        if (event.getBatteryViewStatus() == BleBatteryEvent.BATTERY_VIEW_STATUS_UPDATE_LEVEL) {
            message.what = HANDLER_UPDATE_BT_BATTERY_LEVEL_UI;
            message.arg1 = event.getChargeStatus();
            message.arg2 = event.getBatteryLevel();
        } else if (event.getBatteryViewStatus() == BleBatteryEvent.BATTERY_VIEW_STATUS_SHOW) {
            message.what = HANDLER_UPDATE_BT_BATTERY_UI;
            message.arg1 = event.getBatteryViewStatus();
        } else if (event.getBatteryViewStatus() == BleBatteryEvent.BATTERY_VIEW_STATUS_HIDE) {
            message.what = HANDLER_UPDATE_BT_BATTERY_UI;
            message.arg1 = event.getBatteryViewStatus();
        }
        handler.sendMessage(message);


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BlinkEvent event) {
        switch (event.getBlinkType()) {
            case BlinkEvent.BLINK_TYPE_INVISIBLE:
                tvDemo.setVisibility(View.INVISIBLE);
                break;
            case BlinkEvent.BLINK_TYPE_VISIBLE:
                tvDemo.setVisibility(View.VISIBLE);
                break;
            case BlinkEvent.BLINK_TYPE_TOGGLE:
                tvDemo.setVisibility(tvDemo.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
                break;
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

        int cookerID = event.getCookerID();
        boolean aCookerWorking = false;
        boolean aCookerHighTemp = false;
        boolean bCookerWorking = false;
        boolean bCookerHighTemp = false;
        boolean cCookerWorking = false;
        boolean cCookerHighTemp = false;
        boolean eCookerWorking = false;
        boolean eCookerHighTemp = false;
        boolean fCookerWorking = false;
        boolean fCookerHighTemp = false;
        boolean abCookerHighTemp = false;
        boolean abCookerWorking = false;
        boolean efCookerWorking = false;
        boolean efCookerHighTemp = false;

        LogUtil.d("Enter:: hob 90  cookerID---->" + cookerID);

        boolean aCookerStatus = false;
        boolean bCookerStatus = false;
        boolean cCookerStauts = false;
        boolean eCookerStatus = false;
        boolean fCookerStauts = false;
        boolean abCookerStauts = false;
        boolean efCookerStauts = false;

        int hobLevel = 0;

        switch (cookerID) {
            case TFTHobConstant.COOKER_TYPE_A_COOKER:  // a cooker
                //hrcA.updateCookerView(cookerID, event.getWorkMode(), event.getFireValue(), event.getTempValue(), event.getRealTempValue(), event.getHourValue(), event.getMinuteValue(), event.getTempIndicatorResID(), event.getRecipesResID(), event.getErrorMessage());
                h8rcAb.updateCookerView(cookerID, event.getWorkMode(), event.getFireValue(), event.getTempValue(), event.getRealTempValue(), event.getHourValue(), event.getMinuteValue(), event.getTempIndicatorResID(), event.getRecipesResID(), event.getRecipesShowOrder(), event.getErrorMessage());


                aCookerHighTemp = event.getWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP;
                aCookerWorking = event.getWorkMode() != TFTHobConstant.HOB_VIEW_WORK_MODE_POWER_OFF && !aCookerHighTemp;
                aIsCookerOff = event.getWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_POWER_OFF;
                aCookerWorkMode = event.getWorkMode();
                aIsCookerHighTemp = event.getWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP;
                // LogUtil.d("liang the a fire is = "+event.getFireValue()+"the temperature is = "+ event.getTempValue());
                // 计算 电磁炉档位，从而计算风机档位。在风机自动档时。
                aCookerHobLevel = getCookerLevel(event.getFireValue(), event.getTempValue());
                CookerSettingData dataA = new CookerSettingData(TFTHobConstant.COOKER_TYPE_A_COOKER);
                dataA.setTimerRemainHourValue(event.getHourValue());
                dataA.setTimerRemainMinuteValue(event.getMinuteValue());
                break;
            case TFTHobConstant.COOKER_TYPE_B_COOKER:  // b cooker
                //hrcA.updateCookerView(cookerID, event.getWorkMode(), event.getFireValue(), event.getTempValue(), event.getRealTempValue(), event.getHourValue(), event.getMinuteValue(), event.getTempIndicatorResID(), event.getRecipesResID(), event.getErrorMessage());
                h8rcAb.updateCookerView(cookerID, event.getWorkMode(), event.getFireValue(), event.getTempValue(), event.getRealTempValue(), event.getHourValue(), event.getMinuteValue(), event.getTempIndicatorResID(), event.getRecipesResID(), event.getRecipesShowOrder(), event.getErrorMessage());


                //   hbccB.updateCookerView(event.getWorkMode(), event.getFireValue(), event.getTempValue(), event.getRealTempValue(), event.getHourValue(), event.getMinuteValue(), event.getTempIndicatorResID(), event.getRecipesResID(), event.getRecipesShowOrder(), event.getErrorMessage());
                bCookerHighTemp = event.getWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP;
                bIsCookerOff = event.getWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_POWER_OFF;
                bCookerWorking = event.getWorkMode() != TFTHobConstant.HOB_VIEW_WORK_MODE_POWER_OFF && !bCookerHighTemp;
                bCookerWorkMode = event.getWorkMode();
                bIsCookerHighTemp = event.getWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP;
                //   LogUtil.d("liang the b fire is = "+event.getFireValue()+"the temperature is = "+ event.getTempValue());
                // 计算 电磁炉档位，从而计算风机档位。在风机自动档时。
                bCookerHobLevel = getCookerLevel(event.getFireValue(), event.getTempValue());
                CookerSettingData dataB = new CookerSettingData(TFTHobConstant.COOKER_TYPE_B_COOKER);
                dataB.setTimerRemainHourValue(event.getHourValue());
                dataB.setTimerRemainMinuteValue(event.getMinuteValue());
                break;
            case TFTHobConstant.COOKER_TYPE_C_COOKER:  // c cooker
                hbccB.updateCookerView(event.getWorkMode(), event.getFireValue(), event.getTempValue(), event.getRealTempValue(), event.getHourValue(), event.getMinuteValue(), event.getTempIndicatorResID(), event.getRecipesResID(), event.getRecipesShowOrder(), event.getErrorMessage());
                cCookerHighTemp = event.getWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP;
                cCookerWorking = event.getWorkMode() != TFTHobConstant.HOB_VIEW_WORK_MODE_POWER_OFF && !cCookerHighTemp;
                cIsCookerOff = event.getWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_POWER_OFF;
                cCookerWorkMode = event.getWorkMode();
                cIsCookerHighTemp = event.getWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP;
//                LogUtil.d("liang the c fire is = " + event.getFireValue() + "the mode is = " + event.getWorkMode());
                // 计算 电磁炉档位，从而计算风机档位。在风机自动档时。
                cCookerHobLevel = getCookerLevel(event.getFireValue(), event.getTempValue());
                CookerSettingData dataC = new CookerSettingData(TFTHobConstant.COOKER_TYPE_C_COOKER);
                dataC.setTimerRemainHourValue(event.getHourValue());
                dataC.setTimerRemainMinuteValue(event.getMinuteValue());

                break;
            case TFTHobConstant.COOKER_TYPE_AB_COOKER:
                //hrcA.updateCookerView(cookerID, event.getWorkMode(), event.getFireValue(), event.getTempValue(), event.getRealTempValue(), event.getHourValue(), event.getMinuteValue(), event.getTempIndicatorResID(), event.getRecipesResID(), event.getErrorMessage());
                h8rcAb.updateCookerView(cookerID, event.getWorkMode(), event.getFireValue(), event.getTempValue(), event.getRealTempValue(), event.getHourValue(), event.getMinuteValue(), event.getTempIndicatorResID(), event.getRecipesResID(), event.getRecipesShowOrder(), event.getErrorMessage());

                abCookerHighTemp = event.getWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP;
                abCookerWorking = event.getWorkMode() != TFTHobConstant.HOB_VIEW_WORK_MODE_POWER_OFF && !abCookerHighTemp;


                abIsCookerOff = event.getWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_POWER_OFF;

                abCookerWorkMode = event.getWorkMode();


                abIsCookerHighTemp = event.getWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP;

                abCookerHobLevel = getCookerLevel(event.getFireValue(), event.getTempValue()) * 2;
                if(event.getWorkMode()!=0&&event.getWorkMode()!=TFTHobConstant.HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP){
                    abWorkingNow=true;
                }else {
                    abWorkingNow=false;
                }

                break;
            case TFTHobConstant.COOKER_TYPE_EF_COOKER://56
                //hrcC.updateCookerView(cookerID, event.getWorkMode(), event.getFireValue(), event.getTempValue(), event.getRealTempValue(), event.getHourValue(), event.getMinuteValue(), event.getTempIndicatorResID(), event.getRecipesResID(), event.getErrorMessage());
                h8rcEf.updateCookerView(cookerID, event.getWorkMode(), event.getFireValue(), event.getTempValue(), event.getRealTempValue(), event.getHourValue(), event.getMinuteValue(), event.getTempIndicatorResID(), event.getRecipesResID(), event.getRecipesShowOrder(), event.getErrorMessage());

                efCookerHighTemp = event.getWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP;
                efCookerWorking = event.getWorkMode() != TFTHobConstant.HOB_VIEW_WORK_MODE_POWER_OFF && !efCookerHighTemp;


                efIsCookerOff = event.getWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_POWER_OFF;

                efCookerWorkMode = event.getWorkMode();


                efIsCookerHighTemp = event.getWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP;

                efCookerHobLevel = getCookerLevel(event.getFireValue(), event.getTempValue()) * 2;
                if(event.getWorkMode()!=0&&event.getWorkMode()!=TFTHobConstant.HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP){
                    efWorkingNow=true;
                }else {
                    efWorkingNow=false;
                }

                break;
            case TFTHobConstant.COOKER_TYPE_E_COOKER:  // e cooker
                // hrcC.updateCookerView(cookerID, event.getWorkMode(), event.getFireValue(), event.getTempValue(), event.getRealTempValue(), event.getHourValue(), event.getMinuteValue(), event.getTempIndicatorResID(), event.getRecipesResID(), event.getErrorMessage());
                h8rcEf.updateCookerView(cookerID, event.getWorkMode(), event.getFireValue(), event.getTempValue(), event.getRealTempValue(), event.getHourValue(), event.getMinuteValue(), event.getTempIndicatorResID(), event.getRecipesResID(), event.getRecipesShowOrder(), event.getErrorMessage());


                eCookerHighTemp = event.getWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP;
                eCookerWorking = event.getWorkMode() != TFTHobConstant.HOB_VIEW_WORK_MODE_POWER_OFF && !eCookerHighTemp;
                eIsCookerOff = event.getWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_POWER_OFF;
                eCookerWorkMode = event.getWorkMode();
                eIsCookerHighTemp = event.getWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP;
                //  LogUtil.d("liang the c fire is = "+event.getFireValue()+"the temperature is = "+ event.getTempValue());
                // 计算 电磁炉档位，从而计算风机档位。在风机自动档时。
                eCookerHobLevel = getCookerLevel(event.getFireValue(), event.getTempValue());
                CookerSettingData dataE = new CookerSettingData(TFTHobConstant.COOKER_TYPE_E_COOKER);
                dataE.setTimerRemainHourValue(event.getHourValue());
                dataE.setTimerRemainMinuteValue(event.getMinuteValue());

                break;
            case TFTHobConstant.COOKER_TYPE_F_COOKER:  // f cooker
                hrcC.updateCookerView(cookerID, event.getWorkMode(), event.getFireValue(), event.getTempValue(), event.getRealTempValue(), event.getHourValue(), event.getMinuteValue(), event.getTempIndicatorResID(), event.getRecipesResID(), event.getErrorMessage());
                h8rcEf.updateCookerView(cookerID, event.getWorkMode(), event.getFireValue(), event.getTempValue(), event.getRealTempValue(), event.getHourValue(), event.getMinuteValue(), event.getTempIndicatorResID(), event.getRecipesResID(), event.getRecipesShowOrder(), event.getErrorMessage());


                fCookerHighTemp = event.getWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP;
                fCookerWorking = event.getWorkMode() != TFTHobConstant.HOB_VIEW_WORK_MODE_POWER_OFF && !fCookerHighTemp;
                fIsCookerOff = event.getWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_POWER_OFF;
                fCookerWorkMode = event.getWorkMode();
                fIsCookerHighTemp = event.getWorkMode() == TFTHobConstant.HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP;
                //  LogUtil.d("liang the c fire is = "+event.getFireValue()+"the temperature is = "+ event.getTempValue());
                // 计算 电磁炉档位，从而计算风机档位。在风机自动档时。
                fCookerHobLevel = getCookerLevel(event.getFireValue(), event.getTempValue());
                CookerSettingData dataF = new CookerSettingData(TFTHobConstant.COOKER_TYPE_F_COOKER);
                dataF.setTimerRemainHourValue(event.getHourValue());
                dataF.setTimerRemainMinuteValue(event.getMinuteValue());
                break;
        }

        hobLevel = aCookerHobLevel + bCookerHobLevel + cCookerHobLevel + eCookerHobLevel + fCookerHobLevel + abCookerHobLevel + efCookerHobLevel;
        mHoodLevel = getHoodLevel(hobLevel);
        // 获取当前发送到下位机的烟机的档位
        int[] value = ((HoodPanelFragment.OnHoodPanelFragmentListener) mListener).onRequestGetGearValue(2);
        int fan = value[0];
        int light = value[1];
        boolean isFanWorkingAutoStatus = SettingPreferencesUtil.getFanWorkingStatus(getContext()).equals(CataSettingConstant.FAN_WORKING_STATUS_AUTO) ? true : false;
        if (mHoodLevel != 0 && isFanWorkingAutoStatus) { // 保存通过计算得到的 风机档位，给串口发送数据使用
            //
            ((Hob60PanelFragment.OnHob60PanelFragmentListener) mListener).onHob60PanelFragmentRequestSaveGearValue(mHoodLevel, light);
           // LogUtil.d("liang the saved hood level is = " + mHoodLevel);
        }

        //    LogUtil.d("liang the hoodlevel is = "+ mHoodLevel);

        if (aCookerWorking || bCookerWorking || cCookerWorking || eCookerWorking || fCookerWorking || abCookerWorking || efCookerWorking) {
            CataTFTHobApplication.getInstance().updateLatestTouchTime();
        }

        if (aCookerWorking || bCookerWorking || cCookerWorking || eCookerWorking || fCookerWorking || abCookerWorking || efCookerWorking) {  // 曾经开机过
            mTheCookerHasBeenOpen = true;
            //   LogUtil.d("liang show the cooker is working "+aCookerWorking+" "+bCookerWorking+" "+cCookerWorking
            //  +" the  aCookerWorkMode is "+ cCookerWorkMode);
        } else {

        }

        aCookerStatus = aIsCookerHighTemp || aIsCookerOff; //  判断是否 关炉头了，关炉头或出现高温 都是 true
        bCookerStatus = bIsCookerHighTemp || bIsCookerOff; //
        cCookerStauts = cIsCookerHighTemp || cIsCookerOff; //
        eCookerStatus = eIsCookerHighTemp || eIsCookerOff;
        fCookerStauts = fIsCookerHighTemp || fIsCookerOff;
        abCookerStauts = abIsCookerHighTemp || abIsCookerOff;
        efCookerStauts = efIsCookerHighTemp || efIsCookerOff;
        isTimerDoingNow = SettingPreferencesUtil.getTimerOpenStatus(getContext()).equals(CataSettingConstant.TIMER_STATUS_OPEN) ? true : false;
        if (aCookerStatus && bCookerStatus && cCookerStauts && eCookerStatus && fCookerStauts && abCookerStauts && efCookerStauts) {  // 无 炉头工作

            boolean isHoodConnectivity = SettingPreferencesUtil.getStablishConnectionSwitchStatus(getContext()).equals(CataSettingConstant.STABLISH_CONNECTION_SWITCH_STATUS_OPEN) ? true : false; // 是否与电磁炉连接，即开关是否打开。


            if (mTheCookerHasBeenOpen && isHoodConnectivity && !isTimerDoingNow) {  // 炉头曾经启动过

                mTheCookerHasBeenOpen = false;

                fan = fan & 0x0f;  // 自动是 0x80，手动是0x00
                if (fan == 0 || GlobalVars.getInstance().isPause()) {  // 风机 是零档及风机暂停，不进行定时操作 2019年8月12日15:26:37
                    ((Hob60PanelFragment.OnHob60PanelFragmentListener) mListener).onHob60PanelFragmentRequestSaveGearValue(0, light);// 如果风机暂停，则风机档位是零。2019年8月12日20:34:57
                } else {
                    if (GlobalVars.getInstance().isClickPowerOffButton()) {
                      //  EventBus.getDefault().post(new OrderFromHoodPanel(TFTHoodConstant.TIMER_FROM_MAIN_PANEL_SCREEN));
                        handler.sendEmptyMessageDelayed(HANDLER_SHOW_TIMER_H104, 200);
                    } else {
                      //  EventBus.getDefault().post(new OrderFromHoodPanel(TFTHoodConstant.TIMER_FROM_MAIN_PANEL_SCREEN));
                        handler.sendEmptyMessageDelayed(HANDLER_SHOW_TIMER_H104, 200);
                    }
                    //    SettingPreferencesUtil.saveTimerOpenStatus(getActivity(), CataSettingConstant.TIMER_STATUS_OPEN);
                    LogUtil.d("liang show the schedule fragment");
                }

            } else {
                //mTheCookerHasBeenOpen=false;
            }

            GlobalVars.getInstance().setAllCookersIsClose(true);  // 所有炉头都关掉了，for settings 界面 的 power limit 按键
            //   LogUtil.d("liang  setAllCookersIs Close ");
        } else {  // 有炉头工作
            //  mTimerIsWorking=false;
            //    LogUtil.d("liang there is cooker opening");
            if (isTimerDoingNow) {
                // EventBus.getDefault().post(new OrderFromHoodPanel(TFTHoodConstant.TIMER_CLOSED_BY_OPENING_COOKER));
                LogUtil.d("liang there is cooker opening and TIMER_CLOSED_BY_OPENING_COOKER");
            }

            GlobalVars.getInstance().setAllCookersIsClose(false);
            //  LogUtil.d("liang  setAllCookersIs Open ");
        }

        showFanIconDownRight();
        // 发送数据 到 H107 界面 进行 显示当前的炉头状态
        if(!abCookerStauts) {
            aIsCookerHighTemp=bIsCookerHighTemp=false;
        }
        if(!efCookerStauts){
            eIsCookerHighTemp=fIsCookerHighTemp=false;
        }

        if(abIsCookerHighTemp){
            abCookerWorkMode=TFTHobConstant.HOB_VIEW_WORK_MODE_POWER_OFF;
        }

        if(efIsCookerHighTemp){
            efCookerWorkMode=TFTHobConstant.HOB_VIEW_WORK_MODE_POWER_OFF;
        }

        GlobalVars.getInstance().setAbUnited(h8rcAb.isUnited());
        GlobalVars.getInstance().setEfUnited(h8rcEf.isUnited());
        GlobalVars.getInstance().setHoodLevel(mHoodLevel);
        EventBus.getDefault().post(new GetCookersInformation(aCookerWorkMode + abCookerWorkMode, bCookerWorkMode + abCookerWorkMode, cCookerWorkMode,
                0, eCookerWorkMode + efCookerWorkMode, fCookerWorkMode + efCookerWorkMode, aIsCookerHighTemp , bIsCookerHighTemp ,
                cIsCookerHighTemp, false, eIsCookerHighTemp , fIsCookerHighTemp,mHoodLevel,abWorkingNow,efWorkingNow ));

//LogUtil.d("liang aCookerWorkMode is "+aCookerWorkMode+" "+ "bCookerWorkMode is "+" "+bCookerWorkMode+" "+"abCookerWorkMode is  "+abCookerWorkMode+"  aIsCookerHighTemp is " + aIsCookerHighTemp +" "+ "  abIsCookerHighTemp is "+abIsCookerHighTemp
//+ " efCookerWorking is "+efCookerWorking);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CookerUnionUpdateEvent event) {
        int unionCookerID = event.getUnionCookerID();
        switch (unionCookerID) {
            case TFTHobConstant.COOKER_TYPE_A_COOKER * 10 + TFTHobConstant.COOKER_TYPE_B_COOKER:
                hrcA.updateUnionCookerView(event.getUnionCookerID(), event.getUpCookerID(), event.getUpWorkMode(), event.getUpFireValue(), event.getUpTempValue(), event.getUpHourValue(), event.getUpMinute(), event.getUpTempIndicatorID(), event.getUpRecipesID(), event.getUpErrorMessage(),
                        event.getDownCookerID(), event.getDownWorkMode(), event.getDownFireValue(), event.getDownTempValue(), event.getDownHourValue(), event.getDownMinute(), event.getDownTempIndicatorID(), event.getDownRecipesID(), event.getDownErrorMessage()
                );
                LogUtil.d("liang cooker 8");
                break;
            case TFTHobConstant.COOKER_TYPE_E_COOKER * 10 + TFTHobConstant.COOKER_TYPE_F_COOKER:
                hrcC.updateUnionCookerView(event.getUnionCookerID(), event.getUpCookerID(), event.getUpWorkMode(), event.getUpFireValue(), event.getUpTempValue(), event.getUpHourValue(), event.getUpMinute(), event.getUpTempIndicatorID(), event.getUpRecipesID(), event.getUpErrorMessage(),
                        event.getDownCookerID(), event.getDownWorkMode(), event.getDownFireValue(), event.getDownTempValue(), event.getDownHourValue(), event.getDownMinute(), event.getDownTempIndicatorID(), event.getDownRecipesID(), event.getDownErrorMessage()
                );
                LogUtil.d("liang cooker 9");
                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CookerPowerOffEvent event) {
        int unionCookerID = event.getCookerID();
        //LogUtil.d("CookerPowerOffEvent------cookerid---->" + unionCookerID);
        switch (unionCookerID) {
            case TFTHobConstant.COOKER_TYPE_A_COOKER * 10 + TFTHobConstant.COOKER_TYPE_B_COOKER:
                hrcA.cookerPoweroff();

                break;
            case TFTHobConstant.COOKER_TYPE_F_COOKER * 10 + TFTHobConstant.COOKER_TYPE_E_COOKER:
                hrcC.cookerPoweroff();
                break;
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            //   registerEventBus();
            backgroundWorkingThread = new BackgroundWorkingThread();
            backgroundWorkingThread.start();

            if (mRootView != null) {
                if (GlobalVars.getInstance().isInDemoMode()) {
                    tvDemo.setVisibility(View.VISIBLE);
                    backgroundWorkingThread.setBlinkDemoText(true);
                } else {
                    tvDemo.setVisibility(View.INVISIBLE);
                    backgroundWorkingThread.setBlinkDemoText(false);
                }
            }

            if (bivBluetooth.getVisibility() == View.GONE) {
                llBottomView.setPadding(60, 0, 0, 0);
            } else if (bivBluetooth.getVisibility() == View.VISIBLE) {
                if (ibHood.getVisibility() == View.GONE && ibHoodMask.getVisibility() == View.GONE) llBottomView.setPadding(75, 0, 0, 0);
                else if (ibHood.getVisibility() == View.VISIBLE || ibHoodMask.getVisibility() == View.VISIBLE) llBottomView.setPadding(26, 0, 0, 0);

            }


        } else {
            //  unregisterEventBus();
            if (backgroundWorkingThread != null) {
                if (backgroundWorkingThread.isAlive()) {
                    backgroundWorkingThread.setCancelTask(true);
                    try {
                        backgroundWorkingThread.join();
                    } catch (InterruptedException e) {
                        LogUtil.e(e.getMessage());
                    }
                }
                backgroundWorkingThread = null;
            }
        }
    }

    @OnClick({R.id.ib_hood, R.id.ib_hood_mask,R.id.fl_hood_view})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.ib_hood || view.getId() == R.id.ib_hood_mask) {
            isTimerDoingNow = SettingPreferencesUtil.getTimerOpenStatus(getContext()).equals(CataSettingConstant.TIMER_STATUS_OPEN) ? true : false;
            if (isTimerDoingNow) {  // 显示 定时界面（H107）
                EventBus.getDefault().post(new OrderFromHoodPanel(TFTHoodConstant.TIMER_FROM_MAIN_PANEL_SCREEN));
            } else {  // 显示 烟机界面（H104）
                ((OnHob90PanelFragmentListener) mListener).onHob90PanelFragmentRequestSwitchToHoodPanel();

            }
        }else if (view.getId() == R.id.fl_hood_view) {
            if (ibHood.getVisibility() == View.VISIBLE || ibHoodMask.getVisibility() == View.VISIBLE) {
                isTimerDoingNow = SettingPreferencesUtil.getTimerOpenStatus(getContext()).equals(CataSettingConstant.TIMER_STATUS_OPEN) ? true : false;
                if (isTimerDoingNow) {  // 显示 定时界面（H107）
                    EventBus.getDefault().post(new OrderFromHoodPanel(TFTHoodConstant.TIMER_FROM_MAIN_PANEL_SCREEN));
                } else {  // 显示 烟机界面（H104）
                    ((OnHob90PanelFragmentListener) mListener).onHob90PanelFragmentRequestSwitchToHoodPanel();
                }
            }
        }
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        String keepWarm = mContext.getResources().getString(com.ekek.viewmodule.R.string.viewmodule_base_circle_cooker_view_keep_warm);
        String readyToCook = mContext.getResources().getString(com.ekek.viewmodule.R.string.viewmodule_cooker_view_ready_to_cook);

        hrcA.setKeepWarmText(keepWarm);
        hrcA.setReadyToCookText(readyToCook);
        hbccB.setKeepWarmText(keepWarm);
        hbccB.setReadyToCookText(readyToCook);
        hbccB.refreshTempIndicatorText();
        hrcC.setKeepWarmText(keepWarm);
        hrcC.setReadyToCookText(readyToCook);
        h8rcAb.setReadyToCookText(readyToCook);
        h8rcEf.setReadyToCookText(readyToCook);
    }

    public interface OnHob90PanelFragmentListener extends OnFragmentListener {
        void onHob90PanelFragmentBindCooker(int cookerID);

        void onHob90PanelFragmentRequestSetCooker(CookerSettingData data);

        void onHob90PanelFragmentRequestCookerPowerOff(int cookerID);

        void onHob90PanelFragmentRequestReadyToCook(int cookerID);

        void onHob90PanelFragmentRequestKeepWarm(int cookerID);

        void onHob90PanelFragmentRequestAddTenMinute(int cookerID);

        void onHob90PanelFragmentRequestSwitchToHoodPanel();

        void onHob90PanelFragmentRequestSelectCooker(CookerSettingData data);

        void onHob90PanelFragmentRequestAdjustCookerSettingDataToCook(CookerSettingData data);
    }


    private void setInvisibilityOfFanIcon() {
        if (ibHoodMask.getVisibility() == View.VISIBLE) ibHoodMask.setVisibility(View.GONE);
        if (ibHood.getVisibility() == View.VISIBLE) ibHood.setVisibility(View.GONE);
        cancelAnim();
        if (mIsFanIconFlash) {
            mIsFanIconFlash = false;
        }
    }

    private void setFanIconFlash() {

        if (ibHoodMask.getVisibility() == View.GONE) {
            ibHoodMask.setVisibility(View.VISIBLE);
        }
        if (!mIsFanIconFlash) {
            mIsFanIconFlash = true;
            setAnim(ibHoodMask);
        }
    }

    private void stopFanIconFlash() {
        if (ibHoodMask.getVisibility() == View.VISIBLE) ibHoodMask.setVisibility(View.GONE);
        if (mIsFanIconFlash) {
            mIsFanIconFlash = false;
        }
        cancelAnim();
    }

    private class BackgroundWorkingThread extends BaseThread {

        private boolean blinkDemoText = false;
        private long lastBlinkedTime;

        @Override
        protected boolean started() {
            if (blinkDemoText) {
                EventBus.getDefault().post(new BlinkEvent(tvDemo.getId(), BlinkEvent.BLINK_TYPE_VISIBLE));
            }
            return true;
        }

        @Override
        protected boolean performTaskInLoop() {
            if (blinkDemoText && SystemClock.elapsedRealtime() - lastBlinkedTime > 500) {
                EventBus.getDefault().post(new BlinkEvent(tvDemo.getId(), BlinkEvent.BLINK_TYPE_TOGGLE));
                lastBlinkedTime = SystemClock.elapsedRealtime();
            }
            return true;
        }

        @Override
        protected void finished() {
            if (blinkDemoText) {
                EventBus.getDefault().post(new BlinkEvent(tvDemo.getId(), BlinkEvent.BLINK_TYPE_INVISIBLE));
            }
        }

        public void setBlinkDemoText(boolean blinkDemoText) {
            this.blinkDemoText = blinkDemoText;
            lastBlinkedTime = SystemClock.elapsedRealtime();
        }
    }

    private void setAnim(ImageView view) {
        if (alphaAnim != null) {
            cancelAnim();
        }

        showMasks(view, true);

        alphaAnim = ObjectAnimator.ofInt(
                view,
                ANIM_ATTRIBUTE,
                ALPHA_MAX,
                ALPHA_MIN);
        alphaAnim.setDuration(ANIM_DURATION);
        alphaAnim.setEvaluator(evaluator);
        alphaAnim.setRepeatCount(ValueAnimator.INFINITE);
        alphaAnim.setRepeatMode(ValueAnimator.REVERSE);

        alphaAnim.start();
    }

    private void cancelAnim() {
        showMasks(null, false);

        if (alphaAnim != null) {
            alphaAnim.cancel();
            alphaAnim = null;
        }
    }

    private void showMasks(View view, boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
      /*  if (ivAMask != null) {
            ivAMask.setVisibility(View.INVISIBLE);
        }
        if (ivBMask != null) {
            ivBMask.setVisibility(View.INVISIBLE);
        }
        if (ivCMask != null) {
            ivCMask.setVisibility(View.INVISIBLE);
        }*/
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    private Bitmap getBitmap(int source) {
        if (bitmapMap.containsKey(source)) {
            return bitmapMap.get(source);
        }

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), source);
        bitmapMap.put(source, bitmap);
        return bitmap;
    }

    private void showFanIconDownRight() {
        int fan, light;
        boolean isTimerDoingNow = SettingPreferencesUtil.getTimerOpenStatus(getContext()).equals(CataSettingConstant.TIMER_STATUS_OPEN) ? true : false;
        boolean stablishConnectionSwitchStatus = SettingPreferencesUtil.getStablishConnectionSwitchStatus(getActivity()).equals(CataSettingConstant.STABLISH_CONNECTION_SWITCH_STATUS_OPEN) ? true : false;
        boolean isFanWorkingAutoStatus = SettingPreferencesUtil.getFanWorkingStatus(getContext()).equals(CataSettingConstant.FAN_WORKING_STATUS_AUTO) ? true : false;

        int[] value = ((HoodPanelFragment.OnHoodPanelFragmentListener) mListener).onRequestGetGearValue(2);
        fan = value[0];
        light = value[1];
        fan = fan & 0x0f;  // 自动是 0x80，手动是0x00

        if (stablishConnectionSwitchStatus) { // 与电磁炉建立了连接
            if (fan == 0) {// 风机关了
                if (isFanWorkingAutoStatus) {
                    ibHood.setImageBitmap(getBitmap(R.mipmap.ic_hood_link_auto_off));
                } else {
                    ibHood.setImageBitmap(getBitmap(R.mipmap.ic_hood_link_manual_off));
                }
                stopFanIconFlash();
            } else {// 风机在工作
                if (isFanWorkingAutoStatus) { // 自动模式
                    ibHood.setImageBitmap(getBitmap(R.mipmap.ic_hood_link_auto_on));
                    // LogUtil.d("liang the fan is auto");
                } else { // 手动模式
                    ibHood.setImageBitmap(getBitmap(R.mipmap.ic_hood_link_manual_on));
                    //   LogUtil.d("liang the fan is manual");
                }

                setFanIconFlash();
            }

            if (ibHood.getVisibility() == View.GONE) {
                ibHood.setVisibility(View.VISIBLE);
            }
        } else {  // 没有连接电磁炉
            setInvisibilityOfFanIcon();
        }
    }
}
