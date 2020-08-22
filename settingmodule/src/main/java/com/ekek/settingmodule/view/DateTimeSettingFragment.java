package com.ekek.settingmodule.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.commonmodule.utils.DateTimeUtil;
import com.ekek.settingmodule.R;
import com.ekek.settingmodule.R2;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.database.SettingPreferencesUtil;
import com.ekek.settingmodule.model.SendOrderForFirstSwitchOn;
import com.ekek.settingmodule.model.SettingDoBack;
import com.ekek.viewmodule.common.NoTextSwitch;
import com.ekek.viewmodule.product.ProductManager;

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DateTimeSettingFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {
    private static final String ACTION_TIME_DATE = Intent.ACTION_TIME_TICK;
    private static final String ACTION_TIME_CHANGED = Intent.ACTION_TIME_CHANGED;
    private static final String ACTION_TIMEZONE_CHANGED = Intent.ACTION_TIMEZONE_CHANGED;
    private static final String ACTION_DATE_CHANGED = Intent.ACTION_DATE_CHANGED;
    private static final String ACTION_SET_24_HOUR_FORMAT = "ACTION_EKEK_SET_24_HOUR_FORMAT";
    @BindView(R2.id.tv_set_date)
    TextView tvSetDate;
    @BindView(R2.id.tv_set_time)
    TextView tvSetTime;
    @BindView(R2.id.sw_24_format)
    NoTextSwitch sw24Format;
    @BindView(R2.id.tv_alarm_setting)
    TextView tvAlarmSetting;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.title_set_date)
    TextView titleSetDate;
    @BindView(R2.id.title_set_time)
    TextView titleSetTime;
    @BindView(R2.id.title_use_24_hore_format)
    TextView titleUse24HoreFormat;
    Unbinder unbinder;
    private Typeface typeface;
    private OnDateTimeSettingFragmentListener listener;
    private boolean isTimeReceiverRegister = false;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            /*if (action.equals(ACTION_TIME_DATE) || action.equals(ACTION_TIME_CHANGED) || action.equals(ACTION_TIMEZONE_CHANGED))  {

            }*/
            if (action.equals(ACTION_TIME_DATE)) {
                //LogUtil.d("Enter:: receiver-----ACTION_TIME_DATE");
                boolean is24Format = SettingPreferencesUtil.getTimeFormat24(getActivity());
                updateTimeUI(is24Format);

            } else if (action.equals(ACTION_TIME_CHANGED)) {
                //LogUtil.d("Enter:: receiver-----ACTION_TIME_CHANGED");
                boolean is24Format = SettingPreferencesUtil.getTimeFormat24(getActivity());
                updateTimeUI(is24Format);

            } else if (action.equals(ACTION_DATE_CHANGED)) {
                tvSetDate.setText(formatDateStampString());
            }
        }
    };

    @Override
    public int initLyaout() {
        return R.layout.settingmodule_fragment_date_time_setting;
    }

    @Override
    public void initListener() {

    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        SetFont();
        SetUI();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        titleUse24HoreFormat.setText(R.string.settingmodule_fragment_date_time_setting_title_24_format);
    }

    private void SetUI(){
        if(ProductManager.PRODUCT_TYPE== ProductManager.Haier){
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_date_and_time_haier);
            drawable.setBounds(0, 0, 53, 53);
            tvTitle.setCompoundDrawables(drawable, null, null, null);
        }
    }
    private void SetFont() {

        typeface = GlobalVars.getInstance().getDefaultFontRegular();
        tvTitle.setTypeface(typeface);
        tvSetDate.setTypeface(typeface);
        tvSetTime.setTypeface(typeface);
        tvAlarmSetting.setTypeface(typeface);
        titleSetDate.setTypeface(typeface);
        titleSetTime.setTypeface(typeface);
        titleUse24HoreFormat.setTypeface(typeface);
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    @Override
    public void onStop() {
        super.onStop();
        unRegisterTimeReceiver();
    }

    private void init() {
        tvSetDate.setText(formatDateStampString());
        boolean is24Format = SettingPreferencesUtil.getTimeFormat24(getActivity());
        updateTimeUI(is24Format);
        if (is24Format) {
            sw24Format.setChecked(true);
        } else {
            sw24Format.setChecked(false);
        }
        sw24Format.setOnCheckedChangeListener(this);
        registerTimeReceiver();
    }

    private void updateTimeUI(boolean is24Format) {
        String timeStr  = DateTimeUtil.getTimeStr(is24Format);
        String   strs []= timeStr.split(":");
        int  timeHour=Integer.valueOf(strs[0]);
        tvSetTime.setText(String.valueOf(timeHour)+":"+strs[1]);
    }

    private String formatDateStampString() {
      /*  String result = DateTimeUtil.formatDateTime(
                new Date(System.currentTimeMillis()),
                GlobalVars.getInstance().getCurrentLocale(),
                DateFormat.FULL
        );
*/
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE,dd MMMM yyyy" , GlobalVars.getInstance().getCurrentLocale());
        Date date = new Date(System.currentTimeMillis());
        String result = simpleDateFormat.format(date);
        switch (GlobalVars.getInstance().getCurrentLocale().toString().toLowerCase()) {
            case "hu":
                result += " ";
                break;
        }
        return result;
    }


    @OnClick({R2.id.title_set_date, R2.id.tv_set_date, R2.id.title_set_time, R2.id.tv_set_time, R2.id.tv_alarm_setting})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.tv_set_date || i == R.id.title_set_date) {
            listener.onDateTimeSettingFragmentRequestSetDate();
        } else if (i == R.id.tv_set_time || i == R.id.title_set_time) {
            listener.onDateTimeSettingFragmentRequestSetTime();
        } else if (i == R.id.tv_alarm_setting) {
            listener.onDateTimeSettingFragmentRequestSetAlarm();

            /*LogUtil.d("24----->" + android.provider.Settings.System.getString(getActivity().getContentResolver(),
                    android.provider.Settings.System.TIME_12_24));*/
        }
    }

    public void setOnDateTimeSettingFragmentListener(OnDateTimeSettingFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton == sw24Format) {
            if (b) {
                sw24Format.setChecked(true);
                SettingPreferencesUtil.saveTimeFormat24(getActivity(), CataSettingConstant.TIME_FORMAT_24);
                EventBus.getDefault().post(new SendOrderForFirstSwitchOn(CataSettingConstant.is24Format));
            } else {
                sw24Format.setChecked(false);
                SettingPreferencesUtil.saveTimeFormat24(getActivity(), CataSettingConstant.TIME_FORMAT_12);
                EventBus.getDefault().post(new SendOrderForFirstSwitchOn(CataSettingConstant.is12Format));
            }
            updateTimeUI(SettingPreferencesUtil.getTimeFormat24(getActivity()));
            Intent intent = new Intent();
            intent.setAction("ACTION_CHANGE_12_24_FORMAT");
            getActivity().sendBroadcast(intent);
        }
    }

    private void notifySet24Format(boolean is24Hour) {
        Intent intent = new Intent();
        intent.setAction(ACTION_SET_24_HOUR_FORMAT);
        intent.putExtra("is24Hour", is24Hour);
        getActivity().sendBroadcast(intent);
    }

    private void registerTimeReceiver() {
        if (isTimeReceiverRegister) return;
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_TIME_DATE);
        filter.addAction(ACTION_TIME_CHANGED);
        filter.addAction(ACTION_TIMEZONE_CHANGED);
        filter.addAction(ACTION_DATE_CHANGED);
        getContext().registerReceiver(receiver, filter);
        isTimeReceiverRegister = true;

    }

    private void unRegisterTimeReceiver() {
        if (isTimeReceiverRegister) {
            getContext().unregisterReceiver(receiver);
            isTimeReceiverRegister = false;
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

    public interface OnDateTimeSettingFragmentListener extends OnFragmentListener {
        void onDateTimeSettingFragmentRequestSetDate();

        void onDateTimeSettingFragmentRequestSetTime();

        void onDateTimeSettingFragmentRequestSetAlarm();
    }
    @OnClick(R2.id.tv_title)
    public void onClick() {
        SetClickSoundEffect();
        EventBus.getDefault().post(new SettingDoBack(CataSettingConstant.do_back));
    }

    private void SetClickSoundEffect(){
        boolean clickSoundSwitchStatus = SettingPreferencesUtil.getClickSoundSwitchStatus(getContext()).equals(CataSettingConstant.CLICK_SOUND_SWITCH_STATUS_OPEN);
        AudioManager mAudioManager = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
        if(clickSoundSwitchStatus){
            mAudioManager.loadSoundEffects();
        }else {
            mAudioManager.unloadSoundEffects();
        }
    }
}
