package com.ekek.settingmodule.view;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.ekek.settingmodule.events.DateOrTimeSetEvent;
import com.ekek.settingmodule.events.IdleEvent;
import com.ekek.settingmodule.events.SetLanguage;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DateSettingFragment extends BaseFragment implements CheckIdleThread.CheckIdleThreadListener {
    private static final String ACTION_SET_DATE = "ACTION_EKEK_SET_DATE";

    @BindView(R2.id.wv_month)
    WheelViewEx wvMonth;
    @BindView(R2.id.wv_day)
    WheelViewEx wvDay;
    @BindView(R2.id.wv_year)
    WheelViewEx wvYear;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.right_back)
    ImageView rightBack;
    @BindView(R2.id.wv_test2)
    WheelViewEx wvTest2;
    Unbinder unbinder;
    private Typeface typeface;
    private int currentYear, currentDay, currentMonth;
    private int cureentYearIndex,currentDayIndex,currentMonthIndex;
    List<String> monthList;
    private ArrayWheelAdapter daysAdapter;
    private SettingFragment mSettingFragment;

    private static final int SetLanguage = 0;
    private static final int SetDate = 1;
    private static final int SetTime = 2;
    private static final int GoBackToSetting = 3;
    private static final int ShowMainWorkSpace = 4;
    private static final int SetIdle = 5;

    private boolean mType =false;
    private CheckIdleThread checkIdleThread;

    @Override
    public int initLyaout() {
        return R.layout.settingmodule_fragment_date_setting;
    }

    @Override
    public void initListener() {

    }

    public DateSettingFragment(){

    }
    @SuppressLint("ValidFragment")
    public DateSettingFragment(boolean type){
        this .mType=type;
    }

    public void setType(boolean type) {
        this.mType = type;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        typeface = GlobalVars.getInstance().getDefaultFontRegular();
        tvTitle.setTypeface(typeface);

        typeface = GlobalVars.getInstance().getDefaultFontBold();
        SetUI();


    }
    private void SetUI(){
        if(ProductManager.PRODUCT_TYPE== ProductManager.Haier){
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_date_and_time_haier);
            drawable.setBounds(0, 0, 53, 53);
            tvTitle.setCompoundDrawables(drawable, null, null, null);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        registerEventBus();
        init();
    }

    @Override
    public void onStop() {
        super.onStop();
        /*wvMonth.setOnItemSelectedListener(null);
        wvYear.setOnItemSelectedListener(null);
        wvDay.setOnItemSelectedListener(null);*/
        unregisterEventBus();
        CheckIdleThread.cancel(checkIdleThread);
        notifyUpdateDate();
      //  LogUtil.d("liang datasettings  onStop ");
        EventBus.getDefault().post(new SendOrderForFirstSwitchOn(CataSettingConstant.FlashTheTimeOfTouchUI));

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IdleEvent event){
        if (event.getFragment() == IdleEvent.FRAGMENT_DATE) {
            notifyUpdateDate();
            EventBus.getDefault().post(new SendOrderForFirstSwitchOn(SetIdle));
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SettingFragmentHiddenEvent event) {
        switch (event.getFragment()) {
            case SettingFragmentHiddenEvent.FRAGMENT_DATE_SETTING:
                CheckIdleThread.cancel(checkIdleThread);
                break;
        }
    }

    private void init() {
        initDate();
        initMonths();
        initYears();
        initDays();
        initTest();
        if (GlobalVars.getInstance().getAppStartTag() == GlobalCons.APP_START_NONE) {// 不是第一次开机
            rightBack.setVisibility(View.INVISIBLE);

        } else if(GlobalVars.getInstance().getAppStartTag() == GlobalCons.APP_START_FIRST_TIME){  // 是第一次开机
            if(mType){
                rightBack.setVisibility(View.INVISIBLE);
            }else {
                rightBack.setVisibility(View.VISIBLE);
              //  rightBack.setVisibility(View.INVISIBLE);
            }
            checkIdleThread = CheckIdleThread.start(checkIdleThread, CheckIdleThread.DURATION_FIVE_MINUTES);
            checkIdleThread.setCheckIdleThreadListener(this);
            //   SettingPreferencesUtil.saveTheFirstTimeSwitchOnHob(getActivity(),CataSettingConstant.THE_FIRST_TIME_SWITCH_ON_HOB);
        }else if (GlobalVars.getInstance().getAppStartTag() == GlobalCons.APP_START){  // 关机后，再开机
            if(mType){
                rightBack.setVisibility(View.INVISIBLE);
            }else {
                rightBack.setVisibility(View.VISIBLE);
              //  rightBack.setVisibility(View.INVISIBLE);
            }
            checkIdleThread = CheckIdleThread.start(checkIdleThread, CheckIdleThread.DURATION_FIVE_MINUTES);
            checkIdleThread.setCheckIdleThreadListener(this);
        }
    }

    private void initTest() {
        String[] timeFormats = getResources().getStringArray(R.array.settingmodule_time_format_test);
        //  String[] timeFormats = getResources().getStringArray(R.array.settingmodule_time_format_test);
        wvTest2.setCyclic(false);
        wvTest2.setAdapter(new ArrayWheelAdapter<>(Arrays.asList(timeFormats)));
        wvTest2.setTextSize(48);
        wvTest2.setTypeface(typeface);

    }

    private void initDate() {
        Calendar cal = Calendar.getInstance();
        currentYear = cal.get(cal.YEAR);
        currentMonth = cal.get(cal.MONTH) + 1;
        currentDay = cal.get(cal.DATE);
    }

    private void initMonths() {
        String[] months = getResources().getStringArray(R.array.settingmodule_months);
        monthList = Arrays.asList(months);
        wvMonth.setCyclic(true);
        wvMonth.setAdapter(new ArrayWheelAdapter<>(monthList));
        wvMonth.setCurrentItem(currentMonth - 1);
        wvMonth.setTextColorCenter(Color.WHITE);
        wvMonth.setTextColorOut(Color.WHITE);
        wvMonth.setTextSize(48);
        wvMonth.setTypeface(typeface);
        wvMonth.setItemScrollListener(new WheelViewEx.ItemScrollListener() {
            @Override
            public void OnItemScroll(float itemHeight, float totalScrollY) {
                EventBus.getDefault().post(new PlaySoundWhenSetTime(CataSettingConstant.PLAY_SOUND_TYPE_LIST));
            }
        });
        wvMonth.setCentralItemClickListener(new WheelViewEx.CentralItemClickListener() {
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
        wvMonth.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (!isVisible()) return;

                if(wvMonth.getAdapter()!=null){
                    Object obj = wvMonth.getAdapter().getItem(index);
                    if(obj!=null){
                        String monthStr = String.valueOf(wvMonth.getAdapter().getItem(index));
                        currentMonth = monthList.indexOf(monthStr) + 1;
                        updateDaysUI();
                    }else {
                        LogUtil.d("liang wvmonth Adapter is null 1 ");
                    }

                }else {
                    LogUtil.d("liang wvmonth Adapter is null 2 ");
                }

            }
        });
        wvMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("liang wvmonth  is setOnClickListener  ");
            }
        });

    }

    private void initYears() {
        wvYear.setAdapter(new ArrayWheelAdapter(DateTimeUtil.getYears(2020, 2038)));// 设置"年"的显示数据
        wvYear.setCurrentItem(currentYear - 2020);// 初始化时显示的数据 currentYear - 2020
       // wvYear.setAdapter(new ArrayWheelAdapter(DateTimeUtil.getYears(2019, 2099)));// 设置"年"的显示数据
       // wvYear.setCurrentItem(0);// 初始化时显示的数据
        wvYear.setCyclic(true);
        wvYear.setTextColorCenter(Color.WHITE);
        wvYear.setTextColorOut(Color.WHITE);
        wvYear.setTextSize(48);
        wvYear.setTypeface(typeface);
        wvYear.setItemScrollListener(new WheelViewEx.ItemScrollListener() {
            @Override
            public void OnItemScroll(float itemHeight, float totalScrollY) {
                EventBus.getDefault().post(new PlaySoundWhenSetTime(CataSettingConstant.PLAY_SOUND_TYPE_LIST));
            }
        });
        wvYear.setCentralItemClickListener(new WheelViewEx.CentralItemClickListener() {
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
        wvYear.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (!isVisible()) return;
                //LogUtil.d("Enter:: onItemSelected---->" + index);
                //LogUtil.d("wvYear.getCurrentItem()------>" + wvYear.getCurrentItem());
                //LogUtil.d("content------>" + String.valueOf(wvYear.getAdapter().getItem(index)));
                if (wvYear.getAdapter() != null) {
                    Object obj = wvYear.getAdapter().getItem(index);
                    if (obj != null) {
                        String yearStr = String.valueOf(obj);
                        currentYear = Integer.valueOf(yearStr);
                        updateDaysUI();
                    }else {
                        LogUtil.d("Enter::wvYear.setOnItemSelectedListener----obj == null ");
                    }

                }else {
                    LogUtil.d("Enter::wvYear.setOnItemSelectedListener----adapter == null ");
                }
            }
        });
    }

    private void initDays() {
        daysAdapter = new ArrayWheelAdapter(DateTimeUtil.getDays(currentYear, currentMonth));
        wvDay.setAdapter(daysAdapter);// 设置"年"的显示数据
        wvDay.setCurrentItem(currentDay-1);// 初始化时显示的数据
        wvDay.setCyclic(true);
        wvDay.setTextColorCenter(Color.WHITE);
        wvDay.setTextColorOut(Color.WHITE);
        wvDay.setTextSize(48);
        wvDay.setTypeface(typeface);
        wvDay.setItemScrollListener(new WheelViewEx.ItemScrollListener() {
            @Override
            public void OnItemScroll(float itemHeight, float totalScrollY) {
                EventBus.getDefault().post(new PlaySoundWhenSetTime(CataSettingConstant.PLAY_SOUND_TYPE_LIST));
            }
        });
        wvDay.setCentralItemClickListener(new WheelViewEx.CentralItemClickListener() {
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
        wvDay.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (!isVisible()) return;
                String dayStr = String.valueOf(wvDay.getAdapter().getItem(index));
                currentDay = Integer.valueOf(dayStr);
               // LogUtil.d("liang the setting day is "+currentDay);
          //      LogUtil.d("liang the index is "+index);
            }
        });

    }

    private void updateDaysUI() {
        //LogUtil.d("currentYear---->" + currentYear);
        //LogUtil.d("currentMonth---->" + currentMonth);
        daysAdapter = new ArrayWheelAdapter(DateTimeUtil.getDays(currentYear, currentMonth));
        wvDay.setAdapter(daysAdapter);
    }

    private void notifyUpdateDate() {
        //LogUtil.d("Enter:: notifyUpdateDate");
        Calendar cal = Calendar.getInstance();
        int year = cal.get(cal.YEAR);
        int month = cal.get(cal.MONTH) + 1;
        int day = cal.get(cal.DATE);
        if (year == currentYear && month == currentMonth && day == currentDay) {
            //  LogUtil.d("Enter:: no need to notifyUpdateDate");
        } else {
           /* Intent intent = new Intent();
            intent.setAction(ACTION_SET_DATE);
            intent.putExtra("year", currentYear);
            intent.putExtra("month", currentMonth - 1);
            intent.putExtra("day", currentDay);
            getActivity().sendBroadcast(intent);
            LogUtil.d("the setting is done~~~~~~~~~");*/
            DateTimeUtil.changeSystemDate(getContext(), currentYear, currentMonth, currentDay);

        }

        LogUtil.d("the setting currentYear is = "+currentYear+";  currentMonth is = "+(currentMonth)+";  currentDay is = "+currentDay);
        LogUtil.d("the now year is = "+year+";  now month is = "+(month)+";  now Day is = "+day);


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

    @OnClick({R2.id.tv_title,R2.id.right_back,R2.id.wv_test2})
    public void onClick(View view) {
        if(view .getId() ==R.id.tv_title){
            CheckIdleThread.cancel(checkIdleThread);
            EventBus.getDefault().post(new SettingDoBack(CataSettingConstant.do_back));
        }else if(view.getId()==R.id.right_back||view.getId()==R.id.wv_test2){
            CheckIdleThread.cancel(checkIdleThread);
            notifyUpdateDate();
            EventBus.getDefault().post(new SendOrderForFirstSwitchOn(SetTime));

        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DateOrTimeSetEvent event) {
        notifyUpdateDate();
        EventBus.getDefault().post(new SendOrderForFirstSwitchOn(SetTime));
    }

    @Override
    public void onIdle() {
        EventBus.getDefault().post(new IdleEvent(IdleEvent.FRAGMENT_DATE));
    }
}
