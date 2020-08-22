package com.ekek.settingmodule.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.settingmodule.R;
import com.ekek.settingmodule.R2;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.database.SettingPreferencesUtil;
import com.ekek.settingmodule.model.SettingDoBack;
import com.ekek.settingmodule.utils.SoundUtil;
import com.ekek.viewmodule.common.NoTextSwitch;
import com.ekek.viewmodule.product.ProductManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AlarmSettingsFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {
    @BindView(R2.id.sw_increase_volume)
    NoTextSwitch swIncreaseVolume;
    @BindView(R2.id.sb_volume)
    SeekBar sbVolume;
    @BindView(R2.id.tv_alarm_duration_options_30)
    TextView tvAlarmDurationOptions30;
    @BindView(R2.id.tv_alarm_duration_options_1)
    TextView tvAlarmDurationOptions1;
    @BindView(R2.id.tv_alarm_duration_options_2)
    TextView tvAlarmDurationOptions2;
    @BindView(R2.id.tv_alarm_duration_options_3)
    TextView tvAlarmDurationOptions3;
    @BindView(R2.id.tv_alarm_duration_options_4)
    TextView tvAlarmDurationOptions4;
    @BindView(R2.id.tv_alarm_duration_options_5)
    TextView tvAlarmDurationOptions5;
    @BindView(R2.id.tv_postpone_duration_options_1)
    TextView tvPostponeDurationOptions1;
    @BindView(R2.id.tv_postpone_duration_options_2)
    TextView tvPostponeDurationOptions2;
    @BindView(R2.id.tv_postpone_duration_options_5)
    TextView tvPostponeDurationOptions5;
    @BindView(R2.id.tv_postpone_duration_options_10)
    TextView tvPostponeDurationOptions10;
    @BindView(R2.id.tv_postpone_duration_options_15)
    TextView tvPostponeDurationOptions15;
    @BindView(R2.id.tv_postpone_duration_options_20)
    TextView tvPostponeDurationOptions20;
    @BindView(R2.id.ll_volume_adjust)
    LinearLayout llVolumeAdjust;
    @BindView(R2.id.volume)
    TextView volume;
    @BindView(R2.id.alarm_duration)
    TextView alarmDuration;
    @BindView(R2.id.postpon_duration)
    TextView postponDuration;
    Unbinder unbinder;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    private Typeface typeface;

    @Override
    public int initLyaout() {
        return R.layout.settingmodule_fragment_alarm_setting;
    }

    @Override
    public void initListener() {

    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        SetUI();
        SetFont();
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
        adjustUI();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        adjustUI();
    }

    private void SetUI(){
        if(ProductManager.PRODUCT_TYPE== ProductManager.Haier){
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_date_and_time_haier);
            drawable.setBounds(0, 0, 53, 53);
            tvTitle.setCompoundDrawables(drawable, null, null, null);
        }
    }

    private void init() {

        String increaseVolumeSwitchStatus = SettingPreferencesUtil.getIncreaseVolumeSwitchStatus(getActivity());
        if (increaseVolumeSwitchStatus.equals(CataSettingConstant.INCREASE_VOLUME_SWITCH_STATUS_OPEN)) {
            swIncreaseVolume.setChecked(true);
            updateVolumeAdjustUI(true);
        } else {
            swIncreaseVolume.setChecked(false);
            updateVolumeAdjustUI(false);
        }
        swIncreaseVolume.setOnCheckedChangeListener(this);

        sbVolume.setMax(SoundUtil.getAlarmMaxVolume(getActivity()));
        sbVolume.setProgress(SoundUtil.getAlarmCurrentVolume(getActivity()));
        sbVolume.setOnSeekBarChangeListener(this);

        int alarmDuration = SettingPreferencesUtil.getAlarmDuration(getActivity()) / 60;
        switch (alarmDuration) {
            case 0://30秒
                updateAlarmDurationUI(R.id.tv_alarm_duration_options_30);
                break;
            case 1:// 1 minute
                updateAlarmDurationUI(R.id.tv_alarm_duration_options_1);
                break;
            case 2:
                updateAlarmDurationUI(R.id.tv_alarm_duration_options_2);
                break;
            case 3:
                updateAlarmDurationUI(R.id.tv_alarm_duration_options_3);
                break;
            case 4:
                updateAlarmDurationUI(R.id.tv_alarm_duration_options_4);
                break;
            case 5:
                updateAlarmDurationUI(R.id.tv_alarm_duration_options_5);
                break;
        }

        int alarmPostponeDuration = SettingPreferencesUtil.getAlarmPostponeDuration(getActivity()) / 60;
        switch (alarmPostponeDuration) {
            case 1:
                updateAlarmPostponeDurationUI(R.id.tv_postpone_duration_options_1);
                break;
            case 2:
                updateAlarmPostponeDurationUI(R.id.tv_postpone_duration_options_2);
                break;
            case 5:
                updateAlarmPostponeDurationUI(R.id.tv_postpone_duration_options_5);
                break;
            case 10:
                updateAlarmPostponeDurationUI(R.id.tv_postpone_duration_options_10);
                break;
            case 15:
                updateAlarmPostponeDurationUI(R.id.tv_postpone_duration_options_15);
                break;
            case 20:
                updateAlarmPostponeDurationUI(R.id.tv_postpone_duration_options_20);
                break;
        }

    }

    private void adjustUI() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)llVolumeAdjust.getLayoutParams();
        switch (GlobalVars.getInstance().getCurrentLocale().toString().toLowerCase()) {
            case "el":
            case "pl":
            case "ru":
            case "vi":
                lp.topMargin = 5;
                break;
            default:
                lp.topMargin = 25;
                break;
        }

        llVolumeAdjust.setLayoutParams(lp);
    }

    private void SetFont() {

        typeface = GlobalVars.getInstance().getDefaultFontRegular();
        tvTitle.setTypeface(typeface);
        tvAlarmDurationOptions30.setTypeface(typeface);
        tvAlarmDurationOptions1.setTypeface(typeface);
        tvAlarmDurationOptions2.setTypeface(typeface);
        tvAlarmDurationOptions3.setTypeface(typeface);
        tvAlarmDurationOptions1.setTypeface(typeface);
        tvAlarmDurationOptions2.setTypeface(typeface);
        tvAlarmDurationOptions3.setTypeface(typeface);
        tvAlarmDurationOptions4.setTypeface(typeface);
        tvAlarmDurationOptions5.setTypeface(typeface);
        tvPostponeDurationOptions1.setTypeface(typeface);
        tvPostponeDurationOptions2.setTypeface(typeface);
        tvPostponeDurationOptions5.setTypeface(typeface);
        tvPostponeDurationOptions10.setTypeface(typeface);
        tvPostponeDurationOptions15.setTypeface(typeface);
        tvPostponeDurationOptions20.setTypeface(typeface);
        volume.setTypeface(typeface);
        alarmDuration.setTypeface(typeface);
        postponDuration.setTypeface(typeface);
    }

    private void updateVolumeAdjustUI(boolean isShow) {
        if (isShow) {
            llVolumeAdjust.setVisibility(View.VISIBLE);
        } else {
            llVolumeAdjust.setVisibility(View.GONE);
        }
    }

    @OnClick({R2.id.tv_alarm_duration_options_30, R2.id.tv_alarm_duration_options_1, R2.id.tv_alarm_duration_options_2, R2.id.tv_alarm_duration_options_3, R2.id.tv_alarm_duration_options_4, R2.id.tv_alarm_duration_options_5, R2.id.tv_postpone_duration_options_1, R2.id.tv_postpone_duration_options_2, R2.id.tv_postpone_duration_options_5, R2.id.tv_postpone_duration_options_10, R2.id.tv_postpone_duration_options_15, R2.id.tv_postpone_duration_options_20})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.tv_alarm_duration_options_30) {
            updateAlarmDurationUI(i);
            saveAlarmDuration(i);
        } else if (i == R.id.tv_alarm_duration_options_1) {
            updateAlarmDurationUI(i);
            saveAlarmDuration(i);
        } else if (i == R.id.tv_alarm_duration_options_2) {
            updateAlarmDurationUI(i);
            saveAlarmDuration(i);
        } else if (i == R.id.tv_alarm_duration_options_3) {
            updateAlarmDurationUI(i);
            saveAlarmDuration(i);
        } else if (i == R.id.tv_alarm_duration_options_4) {
            updateAlarmDurationUI(i);
            saveAlarmDuration(i);
        } else if (i == R.id.tv_alarm_duration_options_5) {
            updateAlarmDurationUI(i);
            saveAlarmDuration(i);
        } else if (i == R.id.tv_postpone_duration_options_1) {
            updateAlarmPostponeDurationUI(i);
            saveAlarmPostponeDuration(i);
        } else if (i == R.id.tv_postpone_duration_options_2) {
            updateAlarmPostponeDurationUI(i);
            saveAlarmPostponeDuration(i);
        } else if (i == R.id.tv_postpone_duration_options_5) {
            updateAlarmPostponeDurationUI(i);
            saveAlarmPostponeDuration(i);
        } else if (i == R.id.tv_postpone_duration_options_10) {
            updateAlarmPostponeDurationUI(i);
            saveAlarmPostponeDuration(i);
        } else if (i == R.id.tv_postpone_duration_options_15) {
            updateAlarmPostponeDurationUI(i);
            saveAlarmPostponeDuration(i);
        } else if (i == R.id.tv_postpone_duration_options_20) {
            updateAlarmPostponeDurationUI(i);
            saveAlarmPostponeDuration(i);
        }
    }

    private void updateAlarmDurationUI(int id) {

        SpannableString spanText;
        spanText = new SpannableString("UnderlineSpan");
        spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        if (id == R.id.tv_alarm_duration_options_30) {

            spanText = new SpannableString(CataSettingConstant.sAlarmDuration30sce);
            spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tvAlarmDurationOptions30.setText(spanText);

            tvAlarmDurationOptions1.setText("");
            tvAlarmDurationOptions1.setText(CataSettingConstant.sAlarmDuration1min);

            tvAlarmDurationOptions2.setText("");
            tvAlarmDurationOptions2.setText(CataSettingConstant.sAlarmDuration2min);

            tvAlarmDurationOptions3.setText("");
            tvAlarmDurationOptions3.setText(CataSettingConstant.sAlarmDuration3min);

            tvAlarmDurationOptions4.setText("");
            tvAlarmDurationOptions4.setText(CataSettingConstant.sAlarmDuration4min);

            tvAlarmDurationOptions5.setText("");
            tvAlarmDurationOptions5.setText(CataSettingConstant.sAlarmDuration5min);



            tvAlarmDurationOptions30.setTextColor(getResources().getColor(R.color.settingmodule_white));
            tvAlarmDurationOptions1.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvAlarmDurationOptions2.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvAlarmDurationOptions3.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvAlarmDurationOptions4.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvAlarmDurationOptions5.setTextColor(getResources().getColor(R.color.settingmodule_gray));
        } else if (id == R.id.tv_alarm_duration_options_1) {

            spanText = new SpannableString(CataSettingConstant.sAlarmDuration1min);
            spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tvAlarmDurationOptions1.setText(spanText);

            tvAlarmDurationOptions30.setText("");
            tvAlarmDurationOptions30.setText(CataSettingConstant.sAlarmDuration30sce);

            tvAlarmDurationOptions2.setText("");
            tvAlarmDurationOptions2.setText(CataSettingConstant.sAlarmDuration2min);

            tvAlarmDurationOptions3.setText("");
            tvAlarmDurationOptions3.setText(CataSettingConstant.sAlarmDuration3min);

            tvAlarmDurationOptions4.setText("");
            tvAlarmDurationOptions4.setText(CataSettingConstant.sAlarmDuration4min);

            tvAlarmDurationOptions5.setText("");
            tvAlarmDurationOptions5.setText(CataSettingConstant.sAlarmDuration5min);


            tvAlarmDurationOptions30.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvAlarmDurationOptions1.setTextColor(getResources().getColor(R.color.settingmodule_white));
            tvAlarmDurationOptions2.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvAlarmDurationOptions3.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvAlarmDurationOptions4.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvAlarmDurationOptions5.setTextColor(getResources().getColor(R.color.settingmodule_gray));
        } else if (id == R.id.tv_alarm_duration_options_2) {

            spanText = new SpannableString(CataSettingConstant.sAlarmDuration2min);
            spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tvAlarmDurationOptions2.setText(spanText);

            tvAlarmDurationOptions30.setText("");
            tvAlarmDurationOptions30.setText(CataSettingConstant.sAlarmDuration30sce);

            tvAlarmDurationOptions1.setText("");
            tvAlarmDurationOptions1.setText(CataSettingConstant.sAlarmDuration1min);

            tvAlarmDurationOptions3.setText("");
            tvAlarmDurationOptions3.setText(CataSettingConstant.sAlarmDuration3min);

            tvAlarmDurationOptions4.setText("");
            tvAlarmDurationOptions4.setText(CataSettingConstant.sAlarmDuration4min);

            tvAlarmDurationOptions5.setText("");
            tvAlarmDurationOptions5.setText(CataSettingConstant.sAlarmDuration5min);

            tvAlarmDurationOptions30.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvAlarmDurationOptions1.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvAlarmDurationOptions2.setTextColor(getResources().getColor(R.color.settingmodule_white));
            tvAlarmDurationOptions3.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvAlarmDurationOptions4.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvAlarmDurationOptions5.setTextColor(getResources().getColor(R.color.settingmodule_gray));
        } else if (id == R.id.tv_alarm_duration_options_3) {

            spanText = new SpannableString(CataSettingConstant.sAlarmDuration3min);
            spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tvAlarmDurationOptions3.setText(spanText);

            tvAlarmDurationOptions30.setText("");
            tvAlarmDurationOptions30.setText(CataSettingConstant.sAlarmDuration30sce);

            tvAlarmDurationOptions1.setText("");
            tvAlarmDurationOptions1.setText(CataSettingConstant.sAlarmDuration1min);

            tvAlarmDurationOptions2.setText("");
            tvAlarmDurationOptions2.setText(CataSettingConstant.sAlarmDuration2min);

            tvAlarmDurationOptions4.setText("");
            tvAlarmDurationOptions4.setText(CataSettingConstant.sAlarmDuration4min);

            tvAlarmDurationOptions5.setText("");
            tvAlarmDurationOptions5.setText(CataSettingConstant.sAlarmDuration5min);


            tvAlarmDurationOptions30.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvAlarmDurationOptions1.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvAlarmDurationOptions2.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvAlarmDurationOptions3.setTextColor(getResources().getColor(R.color.settingmodule_white));
            tvAlarmDurationOptions4.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvAlarmDurationOptions5.setTextColor(getResources().getColor(R.color.settingmodule_gray));
        } else if (id == R.id.tv_alarm_duration_options_4) {

            spanText = new SpannableString(CataSettingConstant.sAlarmDuration4min);
            spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tvAlarmDurationOptions4.setText(spanText);

            tvAlarmDurationOptions30.setText("");
            tvAlarmDurationOptions30.setText(CataSettingConstant.sAlarmDuration30sce);

            tvAlarmDurationOptions1.setText("");
            tvAlarmDurationOptions1.setText(CataSettingConstant.sAlarmDuration1min);

            tvAlarmDurationOptions2.setText("");
            tvAlarmDurationOptions2.setText(CataSettingConstant.sAlarmDuration2min);

            tvAlarmDurationOptions3.setText("");
            tvAlarmDurationOptions3.setText(CataSettingConstant.sAlarmDuration3min);

            tvAlarmDurationOptions5.setText("");
            tvAlarmDurationOptions5.setText(CataSettingConstant.sAlarmDuration5min);

            tvAlarmDurationOptions30.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvAlarmDurationOptions1.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvAlarmDurationOptions2.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvAlarmDurationOptions3.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvAlarmDurationOptions4.setTextColor(getResources().getColor(R.color.settingmodule_white));
            tvAlarmDurationOptions5.setTextColor(getResources().getColor(R.color.settingmodule_gray));
        } else if (id == R.id.tv_alarm_duration_options_5) {

            spanText = new SpannableString(CataSettingConstant.sAlarmDuration5min);
            spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tvAlarmDurationOptions5.setText(spanText);

            tvAlarmDurationOptions30.setText("");
            tvAlarmDurationOptions30.setText(CataSettingConstant.sAlarmDuration30sce);

            tvAlarmDurationOptions1.setText("");
            tvAlarmDurationOptions1.setText(CataSettingConstant.sAlarmDuration1min);

            tvAlarmDurationOptions2.setText("");
            tvAlarmDurationOptions2.setText(CataSettingConstant.sAlarmDuration2min);

            tvAlarmDurationOptions4.setText("");
            tvAlarmDurationOptions4.setText(CataSettingConstant.sAlarmDuration4min);

            tvAlarmDurationOptions3.setText("");
            tvAlarmDurationOptions3.setText(CataSettingConstant.sAlarmDuration3min);

            tvAlarmDurationOptions30.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvAlarmDurationOptions1.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvAlarmDurationOptions2.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvAlarmDurationOptions3.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvAlarmDurationOptions4.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvAlarmDurationOptions5.setTextColor(getResources().getColor(R.color.settingmodule_white));
        }
    }

    private void saveAlarmDuration(int id) {
        if (id == R.id.tv_alarm_duration_options_30) {
            SettingPreferencesUtil.saveAlarmDuration(getActivity(), 30);
        } else if (id == R.id.tv_alarm_duration_options_1) {
            SettingPreferencesUtil.saveAlarmDuration(getActivity(), 60);
        } else if (id == R.id.tv_alarm_duration_options_2) {
            SettingPreferencesUtil.saveAlarmDuration(getActivity(), 60 * 2);
        } else if (id == R.id.tv_alarm_duration_options_3) {
            SettingPreferencesUtil.saveAlarmDuration(getActivity(), 60 * 3);
        } else if (id == R.id.tv_alarm_duration_options_4) {
            SettingPreferencesUtil.saveAlarmDuration(getActivity(), 60 * 4);
        } else if (id == R.id.tv_alarm_duration_options_5) {
            SettingPreferencesUtil.saveAlarmDuration(getActivity(), 60 * 5);
        }
    }

    private void updateAlarmPostponeDurationUI(int id) {
        SpannableString spanText;
        spanText = new SpannableString("UnderlineSpan");
        spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        if (id == R.id.tv_postpone_duration_options_1) {

            spanText = new SpannableString(CataSettingConstant.sPostponeDuration1min);
            spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tvPostponeDurationOptions1.setText(spanText);

            tvPostponeDurationOptions2.setText("");
            tvPostponeDurationOptions2.setText(CataSettingConstant.sPostponeDuration2min);

            tvPostponeDurationOptions5.setText("");
            tvPostponeDurationOptions5.setText(CataSettingConstant.sPostponeDuration5min);


            tvPostponeDurationOptions10.setText("");
            tvPostponeDurationOptions10.setText(CataSettingConstant.sPostponeDuration10min);

            tvPostponeDurationOptions15.setText("");
            tvPostponeDurationOptions15.setText(CataSettingConstant.sPostponeDuration15min);

            tvPostponeDurationOptions20.setText("");
            tvPostponeDurationOptions20.setText(CataSettingConstant.sPostponeDuration20min);


            tvPostponeDurationOptions1.setTextColor(getResources().getColor(R.color.settingmodule_white));
            tvPostponeDurationOptions2.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvPostponeDurationOptions5.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvPostponeDurationOptions10.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvPostponeDurationOptions15.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvPostponeDurationOptions20.setTextColor(getResources().getColor(R.color.settingmodule_gray));

        } else if (id == R.id.tv_postpone_duration_options_2) {

            spanText = new SpannableString(CataSettingConstant.sPostponeDuration2min);
            spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tvPostponeDurationOptions2.setText(spanText);

            tvPostponeDurationOptions1.setText("");
            tvPostponeDurationOptions1.setText(CataSettingConstant.sPostponeDuration1min);

            tvPostponeDurationOptions5.setText("");
            tvPostponeDurationOptions5.setText(CataSettingConstant.sPostponeDuration5min);


            tvPostponeDurationOptions10.setText("");
            tvPostponeDurationOptions10.setText(CataSettingConstant.sPostponeDuration10min);

            tvPostponeDurationOptions15.setText("");
            tvPostponeDurationOptions15.setText(CataSettingConstant.sPostponeDuration15min);

            tvPostponeDurationOptions20.setText("");
            tvPostponeDurationOptions20.setText(CataSettingConstant.sPostponeDuration20min);
            tvPostponeDurationOptions1.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvPostponeDurationOptions2.setTextColor(getResources().getColor(R.color.settingmodule_white));
            tvPostponeDurationOptions5.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvPostponeDurationOptions10.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvPostponeDurationOptions15.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvPostponeDurationOptions20.setTextColor(getResources().getColor(R.color.settingmodule_gray));
        } else if (id == R.id.tv_postpone_duration_options_5) {

            spanText = new SpannableString(CataSettingConstant.sPostponeDuration5min);
            spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tvPostponeDurationOptions5.setText(spanText);

            tvPostponeDurationOptions2.setText("");
            tvPostponeDurationOptions2.setText(CataSettingConstant.sPostponeDuration2min);

            tvPostponeDurationOptions1.setText("");
            tvPostponeDurationOptions1.setText(CataSettingConstant.sPostponeDuration1min);


            tvPostponeDurationOptions10.setText("");
            tvPostponeDurationOptions10.setText(CataSettingConstant.sPostponeDuration10min);

            tvPostponeDurationOptions15.setText("");
            tvPostponeDurationOptions15.setText(CataSettingConstant.sPostponeDuration15min);

            tvPostponeDurationOptions20.setText("");
            tvPostponeDurationOptions20.setText(CataSettingConstant.sPostponeDuration20min);

            tvPostponeDurationOptions1.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvPostponeDurationOptions2.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvPostponeDurationOptions5.setTextColor(getResources().getColor(R.color.settingmodule_white));
            tvPostponeDurationOptions10.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvPostponeDurationOptions15.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvPostponeDurationOptions20.setTextColor(getResources().getColor(R.color.settingmodule_gray));
        } else if (id == R.id.tv_postpone_duration_options_10) {

            spanText = new SpannableString(CataSettingConstant.sPostponeDuration10min);
            spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tvPostponeDurationOptions10.setText(spanText);

            tvPostponeDurationOptions2.setText("");
            tvPostponeDurationOptions2.setText(CataSettingConstant.sPostponeDuration2min);

            tvPostponeDurationOptions5.setText("");
            tvPostponeDurationOptions5.setText(CataSettingConstant.sPostponeDuration5min);


            tvPostponeDurationOptions1.setText("");
            tvPostponeDurationOptions1.setText(CataSettingConstant.sPostponeDuration1min);

            tvPostponeDurationOptions15.setText("");
            tvPostponeDurationOptions15.setText(CataSettingConstant.sPostponeDuration15min);

            tvPostponeDurationOptions20.setText("");
            tvPostponeDurationOptions20.setText(CataSettingConstant.sPostponeDuration20min);

            tvPostponeDurationOptions1.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvPostponeDurationOptions2.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvPostponeDurationOptions5.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvPostponeDurationOptions10.setTextColor(getResources().getColor(R.color.settingmodule_white));
            tvPostponeDurationOptions15.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvPostponeDurationOptions20.setTextColor(getResources().getColor(R.color.settingmodule_gray));
        } else if (id == R.id.tv_postpone_duration_options_15) {

            spanText = new SpannableString(CataSettingConstant.sPostponeDuration15min);
            spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tvPostponeDurationOptions15.setText(spanText);

            tvPostponeDurationOptions2.setText("");
            tvPostponeDurationOptions2.setText(CataSettingConstant.sPostponeDuration2min);

            tvPostponeDurationOptions5.setText("");
            tvPostponeDurationOptions5.setText(CataSettingConstant.sPostponeDuration5min);


            tvPostponeDurationOptions10.setText("");
            tvPostponeDurationOptions10.setText(CataSettingConstant.sPostponeDuration10min);

            tvPostponeDurationOptions1.setText("");
            tvPostponeDurationOptions1.setText(CataSettingConstant.sPostponeDuration1min);

            tvPostponeDurationOptions20.setText("");
            tvPostponeDurationOptions20.setText(CataSettingConstant.sPostponeDuration20min);

            tvPostponeDurationOptions1.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvPostponeDurationOptions2.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvPostponeDurationOptions5.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvPostponeDurationOptions10.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvPostponeDurationOptions15.setTextColor(getResources().getColor(R.color.settingmodule_white));
            tvPostponeDurationOptions20.setTextColor(getResources().getColor(R.color.settingmodule_gray));
        } else if (id == R.id.tv_postpone_duration_options_20) {

            spanText = new SpannableString(CataSettingConstant.sPostponeDuration20min);
            spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tvPostponeDurationOptions20.setText(spanText);

            tvPostponeDurationOptions2.setText("");
            tvPostponeDurationOptions2.setText(CataSettingConstant.sPostponeDuration2min);

            tvPostponeDurationOptions5.setText("");
            tvPostponeDurationOptions5.setText(CataSettingConstant.sPostponeDuration5min);


            tvPostponeDurationOptions10.setText("");
            tvPostponeDurationOptions10.setText(CataSettingConstant.sPostponeDuration10min);

            tvPostponeDurationOptions15.setText("");
            tvPostponeDurationOptions15.setText(CataSettingConstant.sPostponeDuration15min);

            tvPostponeDurationOptions1.setText("");
            tvPostponeDurationOptions1.setText(CataSettingConstant.sPostponeDuration1min);

            tvPostponeDurationOptions1.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvPostponeDurationOptions2.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvPostponeDurationOptions5.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvPostponeDurationOptions10.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvPostponeDurationOptions15.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvPostponeDurationOptions20.setTextColor(getResources().getColor(R.color.settingmodule_white));
        }
    }

    private void saveAlarmPostponeDuration(int id) {
        if (id == R.id.tv_postpone_duration_options_1) {
            SettingPreferencesUtil.saveAlarmPostponeDuration(getActivity(), 60);

        } else if (id == R.id.tv_postpone_duration_options_2) {
            SettingPreferencesUtil.saveAlarmPostponeDuration(getActivity(), 60 * 2);
        } else if (id == R.id.tv_postpone_duration_options_5) {
            SettingPreferencesUtil.saveAlarmPostponeDuration(getActivity(), 60 * 5);
        } else if (id == R.id.tv_postpone_duration_options_10) {
            SettingPreferencesUtil.saveAlarmPostponeDuration(getActivity(), 60 * 10);
        } else if (id == R.id.tv_postpone_duration_options_15) {
            SettingPreferencesUtil.saveAlarmPostponeDuration(getActivity(), 60 * 15);
        } else if (id == R.id.tv_postpone_duration_options_20) {
            SettingPreferencesUtil.saveAlarmPostponeDuration(getActivity(), 60 * 20);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton == swIncreaseVolume) {
            if (b) {
                swIncreaseVolume.setChecked(true);
                updateVolumeAdjustUI(true);
                SettingPreferencesUtil.saveIncreaseVolumeSwitchStatus(getActivity(), CataSettingConstant.INCREASE_VOLUME_SWITCH_STATUS_OPEN);
            } else {
                swIncreaseVolume.setChecked(false);
                updateVolumeAdjustUI(false);
                SettingPreferencesUtil.saveIncreaseVolumeSwitchStatus(getActivity(), CataSettingConstant.INCREASE_VOLUME_SWITCH_STATUS_CLOSE);

            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        SettingPreferencesUtil.saveAlarmVolumeLevel(getActivity(), progress);
        SoundUtil.setAlarmVolume(getActivity(), progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

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
