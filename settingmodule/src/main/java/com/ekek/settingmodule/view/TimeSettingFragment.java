package com.ekek.settingmodule.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.ekek.commonmodule.GlobalCons;
import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.commonmodule.threads.CheckIdleThread;
import com.ekek.commonmodule.utils.DateTimeUtil;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.commonmodule.views.WheelViewEx;
import com.ekek.settingmodule.R;
import com.ekek.settingmodule.R2;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.database.SettingPreferencesUtil;
import com.ekek.settingmodule.events.DateOrTimeSetEvent;
import com.ekek.settingmodule.events.IdleEvent;
import com.ekek.settingmodule.events.SettingFragmentHiddenEvent;
import com.ekek.settingmodule.model.PlaySoundWhenSetTime;
import com.ekek.settingmodule.model.SendOrderForFirstSwitchOn;
import com.ekek.settingmodule.model.SettingDoBack;
import com.ekek.viewmodule.product.ProductManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TimeSettingFragment extends BaseFragment implements CheckIdleThread.CheckIdleThreadListener {
    private static final String ACTION_SET_TIME = "ACTION_EKEK_SET_TIME";
    @BindView(R2.id.wv_hour)
    WheelViewEx wvHour;
    @BindView(R2.id.wv_minute)
    WheelViewEx wvMinute;
    @BindView(R2.id.wv_time_format)
    WheelViewEx wvTimeFormat;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.right_back)
    ImageView rightBack;
    @BindView(R2.id.tv_dot_12)
    TextView tvDot12;
    @BindView(R2.id.tv_dot_24)
    TextView tvDot24;
    Unbinder unbinder;
    @BindView(R2.id.wv_test)
    WheelViewEx wvTest;
    @BindView(R2.id.tv_set_time_text)
    TextView tvSetTimeText;
    @BindView(R2.id.wv_test2)
    WheelViewEx wvTest2;
    @BindView(R2.id.wv_test3)
    WheelViewEx wvTest3;
    private int currentHour, currentMinute;
    private Typeface typeface;
    private boolean isAM;
    private boolean is24Format = false;
    private boolean mType = false;
    private CheckIdleThread checkIdleThread;


    @Override
    public int initLyaout() {
        return R.layout.settingmodule_fragment_time_setting;
    }

    @Override
    public void initListener() {

    }

    public TimeSettingFragment() {

    }

    @SuppressLint("ValidFragment")
    public TimeSettingFragment(boolean type) {
        this.mType = type;
    }

    public void setType(boolean type) {
        this.mType = type;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        SetFont();
        SetUI();

    }

    private void SetUI() {
        if (ProductManager.PRODUCT_TYPE == ProductManager.Haier) {
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_date_and_time_haier);
            drawable.setBounds(0, 0, 53, 53);
            tvTitle.setCompoundDrawables(drawable, null, null, null);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IdleEvent event){
        if (event.getFragment() == IdleEvent.FRAGMENT_TIME) {
            if (is24Format) {
                notifyUpdateTimeFor24Format();
            } else {
                notifyUpdateTime();
            }
            EventBus.getDefault().post(new SendOrderForFirstSwitchOn(CataSettingConstant.SetIdle));
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SettingFragmentHiddenEvent event) {
        switch (event.getFragment()) {
            case SettingFragmentHiddenEvent.FRAGMENT_TIME_SETTING:
                CheckIdleThread.cancel(checkIdleThread);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        registerEventBus();
        init();
    }

    @OnClick({R2.id.tv_title, R2.id.right_back,R2.id.wv_test2})
    public void onClick(View view) {
        if (view.getId() == R.id.tv_title) {

            CheckIdleThread.cancel(checkIdleThread);
            EventBus.getDefault().post(new SettingDoBack(CataSettingConstant.do_back));
//            LogUtil.d("liang show do back of timesetting 1");

        } else if (view.getId() == R.id.right_back||view.getId()==R.id.wv_test2) {

            CheckIdleThread.cancel(checkIdleThread);
            SettingPreferencesUtil.saveTheFirstTimeSwitchOnHob(getActivity(), CataSettingConstant.THE_FIRST_TIME_SWITCH_ON_HOB);  // 保存 ，不是第一次开机
            GlobalVars.getInstance().setAppStartTag(GlobalCons.APP_START_NONE);
            GlobalVars.getInstance().setInitializeProcessComplete(true);
            // EventBus.getDefault().post(new SendOrderForFirstSwitchOn(CataSettingConstant.GoBackToSetting));
            EventBus.getDefault().post(new SendOrderForFirstSwitchOn(CataSettingConstant.ShowMainWorkSpace));
            //  ((OvenCookerSettingFragment.OnCookingSettingFragmentListener) mListener).onCookingSettingFragmentRequestSwitchToOvenPanel();
            if (is24Format) {
                notifyUpdateTimeFor24Format();
            } else {
                notifyUpdateTime();
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DateOrTimeSetEvent event) {
        SettingPreferencesUtil.saveTheFirstTimeSwitchOnHob(getActivity(), CataSettingConstant.THE_FIRST_TIME_SWITCH_ON_HOB);  // 保存 ，不是第一次开机
        GlobalVars.getInstance().setAppStartTag(GlobalCons.APP_START_NONE);
        GlobalVars.getInstance().setInitializeProcessComplete(true);
        EventBus.getDefault().post(new SendOrderForFirstSwitchOn(CataSettingConstant.ShowMainWorkSpace));
        if (is24Format) {
            notifyUpdateTimeFor24Format();
        } else {
            notifyUpdateTime();
        }
    }


    private void SetFont() {

        typeface = GlobalVars.getInstance().getDefaultFontRegular();
        tvTitle.setTypeface(typeface);
        tvSetTimeText.setTypeface(typeface);
        typeface = GlobalVars.getInstance().getDefaultFontBold();

        tvDot12.setTypeface(typeface);
        tvDot24.setTypeface(typeface);


    }

    @Override
    public void onStop() {
        super.onStop();
       /* wvMinute.setOnItemSelectedListener(null);
        wvTimeFormat.setOnItemSelectedListener(null);
        wvHour.setOnItemSelectedListener(null);*/
        unregisterEventBus();
        CheckIdleThread.cancel(checkIdleThread);
        if (is24Format) {
            notifyUpdateTimeFor24Format();
        } else {
            notifyUpdateTime();
        }

//        LogUtil.d("liang time setting on stop");

    }

    private void initTest() {
        String[] timeFormats = getResources().getStringArray(R.array.settingmodule_time_format_test);
        wvTest.setCyclic(false);
        wvTest.setAdapter(new ArrayWheelAdapter<>(Arrays.asList(timeFormats)));
        wvTest.setTextSize(48);
        wvTest.setTypeface(typeface);

        //  String[] timeFormats = getResources().getStringArray(R.array.settingmodule_time_format_test);
        wvTest2.setCyclic(false);
        wvTest2.setAdapter(new ArrayWheelAdapter<>(Arrays.asList(timeFormats)));
        wvTest2.setTextSize(48);
        wvTest2.setTypeface(typeface);

        wvTest3.setCyclic(false);
        wvTest3.setAdapter(new ArrayWheelAdapter<>(Arrays.asList(timeFormats)));
        wvTest3.setTextSize(48);
        wvTest3.setTypeface(typeface);

    }

    private void init() {
        initTime();
        initHour();
        initMinute();
        initTimeFormat();
        initTest();

        if (GlobalVars.getInstance().getAppStartTag() == GlobalCons.APP_START_NONE) {// 不是第一次开机
            rightBack.setVisibility(View.INVISIBLE);
        } else if (GlobalVars.getInstance().getAppStartTag() == GlobalCons.APP_START_FIRST_TIME) {  // 是第一次开机
            if (mType) {
                rightBack.setVisibility(View.INVISIBLE);
            } else {
                rightBack.setVisibility(View.VISIBLE);
              //  rightBack.setVisibility(View.INVISIBLE);
            }
            checkIdleThread = CheckIdleThread.start(checkIdleThread, CheckIdleThread.DURATION_FIVE_MINUTES);
            checkIdleThread.setCheckIdleThreadListener(this);
            //   SettingPreferencesUtil.saveTheFirstTimeSwitchOnHob(getActivity(),CataSettingConstant.THE_FIRST_TIME_SWITCH_ON_HOB);
        } else if (GlobalVars.getInstance().getAppStartTag() == GlobalCons.APP_START) {  // 关机后，再开机
            if (mType) {
                rightBack.setVisibility(View.INVISIBLE);
            } else {
                rightBack.setVisibility(View.VISIBLE);
              //  rightBack.setVisibility(View.INVISIBLE);
            }
            checkIdleThread = CheckIdleThread.start(checkIdleThread, CheckIdleThread.DURATION_FIVE_MINUTES);
            checkIdleThread.setCheckIdleThreadListener(this);
        }
    }

    private void initTime() {
        is24Format = SettingPreferencesUtil.getTimeFormat24(getActivity());
        Calendar c = Calendar.getInstance();
        if (is24Format) {
            currentHour = c.get(Calendar.HOUR_OF_DAY);
        } else {
            currentHour = c.get(Calendar.HOUR);
        }

        LogUtil.d("currentHour---->" + currentHour);
        currentMinute = c.get(Calendar.MINUTE);
        isAM = c.get(Calendar.AM_PM) == 0 ? true : false;//0是am,1是


    }

    private void initTimeFormat() {
        if (is24Format) {
            wvTimeFormat.setVisibility(View.INVISIBLE);
            wvTest3.setVisibility(View.VISIBLE);
            tvDot12.setVisibility(View.VISIBLE);
            tvDot24.setVisibility(View.INVISIBLE);
        } else {
            wvTimeFormat.setVisibility(View.VISIBLE);
            wvTest3.setVisibility(View.INVISIBLE);
            tvDot12.setVisibility(View.VISIBLE);
            tvDot24.setVisibility(View.INVISIBLE);
            String[] timeFormats = getResources().getStringArray(R.array.settingmodule_time_format);
            wvTimeFormat.setCyclic(false);
            wvTimeFormat.setAdapter(new ArrayWheelAdapter<>(Arrays.asList(timeFormats)));
            if (isAM) {
                wvTimeFormat.setCurrentItem(0);
            } else {
                wvTimeFormat.setCurrentItem(1);
            }

            wvTimeFormat.setTextColorCenter(Color.WHITE);
            wvTimeFormat.setTextColorOut(Color.WHITE);
            wvTimeFormat.setTextSize(48);
            wvTimeFormat.setTypeface(typeface);
            wvTimeFormat.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    if (!isVisible()) return;
                    if (index == 0) isAM = true;
                    else isAM = false;
                }
            });
        }

    }

    private void initMinute() {
        wvMinute.setAdapter(new ArrayWheelAdapter(DateTimeUtil.getMinutes()));
        wvMinute.setCurrentItem(currentMinute);// 初始化时显示的数据
        wvMinute.setCyclic(true);
        wvMinute.setTextColorCenter(Color.WHITE);
        wvMinute.setTextColorOut(Color.WHITE);
        wvMinute.setTextSize(48);
        wvMinute.setTypeface(typeface);
        wvMinute.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (!isVisible()) return;
                String minuteStr = String.valueOf(wvMinute.getAdapter().getItem(index));
                currentMinute = Integer.valueOf(minuteStr);
            }
        });
        wvMinute.setItemScrollListener(new WheelViewEx.ItemScrollListener() {
            @Override
            public void OnItemScroll(float itemHeight, float totalScrollY) {
                EventBus.getDefault().post(new PlaySoundWhenSetTime(CataSettingConstant.PLAY_SOUND_TYPE_LIST));
            }
        });
        wvMinute.setCentralItemClickListener(new WheelViewEx.CentralItemClickListener() {
            @Override
            public void OnCentralItemClick() {
                CheckIdleThread.cancel(checkIdleThread);
                if (GlobalVars.getInstance().getAppStartTag() == GlobalCons.APP_START ||
                        GlobalVars.getInstance().getAppStartTag() == GlobalCons.APP_START_FIRST_TIME) {
                    EventBus.getDefault().post(new DateOrTimeSetEvent());
                } else {
                    onClick(tvTitle);
                }
            }
        });
    }

    private void initHour() {
        if (is24Format) {
            wvHour.setAdapter(new ArrayWheelAdapter(DateTimeUtil.getHoursFor24Format()));
            wvHour.setCurrentItem(currentHour);// 初始化时显示的数据
        } else {
            wvHour.setAdapter(new ArrayWheelAdapter(DateTimeUtil.getHours()));
            /*if (isAM && currentHour == 0) {
                wvHour.setCurrentItem(11);
            }else if (!isAM && currentHour == 0) {

            }*/
            if (currentHour == 0) {
                wvHour.setCurrentItem(11);
            } else {
                wvHour.setCurrentItem(currentHour - 1);// 初始化时显示的数据
            }

        }

        wvHour.setCyclic(true);
        wvHour.setTextColorCenter(Color.WHITE);
        wvHour.setTextColorOut(Color.WHITE);
        wvHour.setTextSize(48);
        wvHour.setTypeface(typeface);
        wvHour.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (!isVisible()) return;
                String hourStr = String.valueOf(wvHour.getAdapter().getItem(index));
                currentHour = Integer.valueOf(hourStr);
            }
        });
        wvHour.setItemScrollListener(new WheelViewEx.ItemScrollListener() {
            @Override
            public void OnItemScroll(float itemHeight, float totalScrollY) {
                EventBus.getDefault().post(new PlaySoundWhenSetTime(CataSettingConstant.PLAY_SOUND_TYPE_LIST));
            }
        });
        wvHour.setCentralItemClickListener(new WheelViewEx.CentralItemClickListener() {
            @Override
            public void OnCentralItemClick() {
                CheckIdleThread.cancel(checkIdleThread);
                if (GlobalVars.getInstance().getAppStartTag() == GlobalCons.APP_START ||
                        GlobalVars.getInstance().getAppStartTag() == GlobalCons.APP_START_FIRST_TIME) {
                    EventBus.getDefault().post(new DateOrTimeSetEvent());
                } else {
                    onClick(tvTitle);
                }
            }
        });
    }

    private void notifyUpdateTime() {
        LogUtil.d("Enter:: notifyUpdateTime");
        Intent intent = new Intent();
        intent.setAction(ACTION_SET_TIME);
        int setHour;
        if (isAM) {
            if (currentHour == 12) {
                intent.putExtra("hour", 0);
                setHour = 0;
            } else {
                intent.putExtra("hour", currentHour);
                setHour = currentHour;
            }
        } else {
            if (currentHour == 12) {
                intent.putExtra("hour", currentHour);
                setHour = currentHour;
            } else {
                intent.putExtra("hour", currentHour + 12);
                setHour = currentHour + 12;
            }
        }

        DateTimeUtil.changeSystemTime(getContext(), setHour, currentMinute);
    }

    private void notifyUpdateTimeFor24Format() {
        DateTimeUtil.changeSystemTime(getContext(), currentHour, currentMinute);
    }

    public void setTimeWhenDisappear(){
        if (is24Format) {
            notifyUpdateTimeFor24Format();
        } else {
            notifyUpdateTime();
        }
       // LogUtil.d("liang set time ~~~~");
        EventBus.getDefault().post(new SendOrderForFirstSwitchOn(CataSettingConstant.FlashTheTimeOfTouchUI));
     //   CataTFTHobApplication.getInstance().updateLastTouchUITime(System.currentTimeMillis());  // 重新刷 最后一次触摸时间
        // LogUtil.d("liang set time when disappear");
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
    public void onIdle() {
        EventBus.getDefault().post(new IdleEvent(IdleEvent.FRAGMENT_TIME));
    }
}
