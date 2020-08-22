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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.settingmodule.R;
import com.ekek.settingmodule.R2;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.database.SettingPreferencesUtil;
import com.ekek.settingmodule.model.SettingDoBack;
import com.ekek.viewmodule.common.NoTextSwitch;
import com.ekek.viewmodule.product.ProductManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EnergySettingsFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {
    @BindView(R2.id.sw_hibernation_mode)
    NoTextSwitch swHibernationMode;
    @BindView(R2.id.tv_activation_time_options_10_sec)
    TextView tvActivationTimeOptions10Sec;
    @BindView(R2.id.tv_activation_time_options_30_sec)
    TextView tvActivationTimeOptions30Sec;
    @BindView(R2.id.tv_activation_time_options_1_min)
    TextView tvActivationTimeOptions1Min;
    @BindView(R2.id.tv_activation_time_options_5_min)
    TextView tvActivationTimeOptions5Min;
    @BindView(R2.id.tv_activation_time_options_15_min)
    TextView tvActivationTimeOptions15Min;
    @BindView(R2.id.tv_activation_time_options_30_min)
    TextView tvActivationTimeOptions30Min;
    @BindView(R2.id.ll_activation_time_duration)
    LinearLayout llActivationTimeDuration;
    @BindView(R2.id.fl_hibernation_mode)
    FrameLayout flHibernationMode;
    @BindView(R2.id.ll_activation_time)
    LinearLayout llActivationTime;
    @BindView(R2.id.tv_total_turn_off_time_options_30_min)
    TextView tvTotalTurnOffTimeOptions30Min;
    @BindView(R2.id.tv_total_turn_off_time_options_1_h)
    TextView tvTotalTurnOffTimeOptions1H;
    @BindView(R2.id.tv_total_turn_off_time_options_6_h)
    TextView tvTotalTurnOffTimeOptions6H;
    @BindView(R2.id.tv_total_turn_off_time_options_12_h)
    TextView tvTotalTurnOffTimeOptions12H;
    @BindView(R2.id.tv_total_turn_off_time_options_24_h)
    TextView tvTotalTurnOffTimeOptions24H;
    @BindView(R2.id.tv_total_turn_off_time_options_72_h)
    TextView tvTotalTurnOffTimeOptions72H;
    @BindView(R2.id.ll_turn_off)
    LinearLayout llTurnOff;
    @BindView(R2.id.ll_turn_off_duration)
    LinearLayout llTurnOffDuration;
    @BindView(R2.id.tv_hibernation_format)
    TextView tvHibernationFormat;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.title_hibernation_mode)
    TextView titleHibernationMode;
    @BindView(R2.id.title_activation_time)
    TextView titleActivationTime;
    @BindView(R2.id.title_total_turn_off)
    TextView titleTotalTurnOff;
    @BindView(R2.id.title_total_turn_off_time)
    TextView titleTotalTurnOffTime;
    Unbinder unbinder;
    @BindView(R2.id.iv_hibernation_button)
    ImageView ivHibernationButton;
    @BindView(R2.id.fl_hibernation_format)
    FrameLayout flHibernationFormat;
    private OnEnergySettingsFragmentListener listener;
    private Typeface typeface;


    @Override
    public int initLyaout() {
        return R.layout.settingmodule_fragment_energy_settings;
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

        adjustUI();
    }

    private void SetUI() {
        if (ProductManager.PRODUCT_TYPE == ProductManager.Haier) {
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_energy_settings_haier);
            drawable.setBounds(0, 0, 53, 53);
            tvTitle.setCompoundDrawables(drawable, null, null, null);
        }
    }

    @OnClick(R2.id.tv_title)
    public void onClick() {
        SetClickSoundEffect();
        EventBus.getDefault().post(new SettingDoBack(CataSettingConstant.do_back));
    }

    private void SetClickSoundEffect() {
        boolean clickSoundSwitchStatus = SettingPreferencesUtil.getClickSoundSwitchStatus(getContext()).equals(CataSettingConstant.CLICK_SOUND_SWITCH_STATUS_OPEN);
        AudioManager mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        if (clickSoundSwitchStatus) {
            mAudioManager.loadSoundEffects();
        } else {
            mAudioManager.unloadSoundEffects();
        }
    }


    private void SetFont() {

        typeface = GlobalVars.getInstance().getDefaultFontRegular();
        tvTitle.setTypeface(typeface);
        titleHibernationMode.setTypeface(typeface);
        titleActivationTime.setTypeface(typeface);
        titleTotalTurnOff.setTypeface(typeface);
        titleTotalTurnOffTime.setTypeface(typeface);
        tvHibernationFormat.setTypeface(typeface);
        tvActivationTimeOptions10Sec.setTypeface(typeface);
        tvActivationTimeOptions30Sec.setTypeface(typeface);
        tvActivationTimeOptions1Min.setTypeface(typeface);
        tvActivationTimeOptions5Min.setTypeface(typeface);
        tvActivationTimeOptions15Min.setTypeface(typeface);
        tvActivationTimeOptions30Min.setTypeface(typeface);
        tvTotalTurnOffTimeOptions30Min.setTypeface(typeface);
        tvTotalTurnOffTimeOptions1H.setTypeface(typeface);
        tvTotalTurnOffTimeOptions6H.setTypeface(typeface);
        tvTotalTurnOffTimeOptions12H.setTypeface(typeface);
        tvTotalTurnOffTimeOptions24H.setTypeface(typeface);
        tvTotalTurnOffTimeOptions72H.setTypeface(typeface);
    }

    @Override
    public void onStart() {
        super.onStart();
        init();

        adjustUI();
    }

    private void init() {
        String hibernationModeSwitchStatus = SettingPreferencesUtil.getHibernationModeSwitchStatus(getActivity());
        if (hibernationModeSwitchStatus.equals(CataSettingConstant.HIBERNATION_MODE_SWITCH_STATUS_OPEN)) {
            swHibernationMode.setChecked(true);
            updateHibernationModeUI(true);
        } else {
            swHibernationMode.setChecked(false);
            updateHibernationModeUI(false);
        }
        swHibernationMode.setOnCheckedChangeListener(this);

        int activationTime = SettingPreferencesUtil.getActivationTime(getActivity());
        if (activationTime == 10) {
            updateActivationTimeDurationUI(R.id.tv_activation_time_options_10_sec);
        } else if (activationTime == 30) {
            updateActivationTimeDurationUI(R.id.tv_activation_time_options_30_sec);
        } else if (activationTime == 60) {
            updateActivationTimeDurationUI(R.id.tv_activation_time_options_1_min);
        } else if (activationTime == 300) {
            updateActivationTimeDurationUI(R.id.tv_activation_time_options_5_min);
        } else if (activationTime == 60 * 15) {
            updateActivationTimeDurationUI(R.id.tv_activation_time_options_15_min);
        } else if (activationTime == 60 * 30) {
            updateActivationTimeDurationUI(R.id.tv_activation_time_options_30_min);
        }

        int totalTurnOffTime = SettingPreferencesUtil.getTotalTurnOffTime(getActivity());
        if (totalTurnOffTime == 30 * 60) {
            updateTotalTurnOffDurationUI(R.id.tv_total_turn_off_time_options_30_min);
        } else if (totalTurnOffTime == 60 * 60) {
            updateTotalTurnOffDurationUI(R.id.tv_total_turn_off_time_options_1_h);
        } else if (totalTurnOffTime == 60 * 60 * 6) {
            updateTotalTurnOffDurationUI(R.id.tv_total_turn_off_time_options_6_h);
        } else if (totalTurnOffTime == 60 * 60 * 12) {
            updateTotalTurnOffDurationUI(R.id.tv_total_turn_off_time_options_12_h);
        } else if (totalTurnOffTime == 60 * 60 * 24) {
            updateTotalTurnOffDurationUI(R.id.tv_total_turn_off_time_options_24_h);
        } else if (totalTurnOffTime == 60 * 60 * 72) {
            updateTotalTurnOffDurationUI(R.id.tv_total_turn_off_time_options_72_h);
        }

    }


    @OnClick({R2.id.iv_hibernation_button, R2.id.tv_hibernation_format, R2.id.tv_activation_time_options_10_sec, R2.id.tv_activation_time_options_30_sec, R2.id.tv_activation_time_options_1_min, R2.id.tv_activation_time_options_5_min, R2.id.tv_activation_time_options_15_min, R2.id.tv_activation_time_options_30_min, R2.id.tv_total_turn_off_time_options_30_min, R2.id.tv_total_turn_off_time_options_1_h, R2.id.tv_total_turn_off_time_options_6_h, R2.id.tv_total_turn_off_time_options_12_h, R2.id.tv_total_turn_off_time_options_24_h, R2.id.tv_total_turn_off_time_options_72_h})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.tv_hibernation_format || i == R.id.iv_hibernation_button) {
            listener.onEnergySettingsFragmentRequestFormatSetting();

        } else if (i == R.id.tv_activation_time_options_10_sec) {
            updateActivationTimeDurationUI(i);
            saveActivationTime(i);
        } else if (i == R.id.tv_activation_time_options_30_sec) {
            updateActivationTimeDurationUI(i);
            saveActivationTime(i);
        } else if (i == R.id.tv_activation_time_options_1_min) {
            updateActivationTimeDurationUI(i);
            saveActivationTime(i);
        } else if (i == R.id.tv_activation_time_options_5_min) {
            updateActivationTimeDurationUI(i);
            saveActivationTime(i);
        } else if (i == R.id.tv_activation_time_options_15_min) {
            updateActivationTimeDurationUI(i);
            saveActivationTime(i);
        } else if (i == R.id.tv_activation_time_options_30_min) {
            updateActivationTimeDurationUI(i);
            saveActivationTime(i);
        } else if (i == R.id.tv_total_turn_off_time_options_30_min) {
            updateTotalTurnOffDurationUI(i);
            saveTotalTurnOffTime(i);
        } else if (i == R.id.tv_total_turn_off_time_options_1_h) {
            updateTotalTurnOffDurationUI(i);
            saveTotalTurnOffTime(i);
        } else if (i == R.id.tv_total_turn_off_time_options_6_h) {
            updateTotalTurnOffDurationUI(i);
            saveTotalTurnOffTime(i);
        } else if (i == R.id.tv_total_turn_off_time_options_12_h) {
            updateTotalTurnOffDurationUI(i);
            saveTotalTurnOffTime(i);
        } else if (i == R.id.tv_total_turn_off_time_options_24_h) {
            updateTotalTurnOffDurationUI(i);
            saveTotalTurnOffTime(i);
        } else if (i == R.id.tv_total_turn_off_time_options_72_h) {
            updateTotalTurnOffDurationUI(i);
            saveTotalTurnOffTime(i);
        }
    }

    private void updateActivationTimeDurationUI(int id) {
        SpannableString spanText;
        spanText = new SpannableString("UnderlineSpan");
        spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        if (id == R.id.tv_activation_time_options_10_sec) {

            spanText = new SpannableString(CataSettingConstant.sActivationTime10sce);
            spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tvActivationTimeOptions10Sec.setText(spanText);

            tvActivationTimeOptions30Sec.setText("");
            tvActivationTimeOptions30Sec.setText(CataSettingConstant.sActivationTime30sce);

            tvActivationTimeOptions1Min.setText("");
            tvActivationTimeOptions1Min.setText(CataSettingConstant.sActivationTime1min);

            tvActivationTimeOptions5Min.setText("");
            tvActivationTimeOptions5Min.setText(CataSettingConstant.sActivationTime5min);

            tvActivationTimeOptions15Min.setText("");
            tvActivationTimeOptions15Min.setText(CataSettingConstant.sActivationTime15min);

            tvActivationTimeOptions30Min.setText("");
            tvActivationTimeOptions30Min.setText(CataSettingConstant.sActivationTime30min);

            tvActivationTimeOptions10Sec.setTextColor(getResources().getColor(R.color.settingmodule_white));
            tvActivationTimeOptions30Sec.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvActivationTimeOptions1Min.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvActivationTimeOptions5Min.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvActivationTimeOptions15Min.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvActivationTimeOptions30Min.setTextColor(getResources().getColor(R.color.settingmodule_gray));

        } else if (id == R.id.tv_activation_time_options_30_sec) {

            spanText = new SpannableString(CataSettingConstant.sActivationTime30sce);
            spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tvActivationTimeOptions30Sec.setText(spanText);

            tvActivationTimeOptions10Sec.setText("");
            tvActivationTimeOptions10Sec.setText(CataSettingConstant.sActivationTime10sce);

            tvActivationTimeOptions1Min.setText("");
            tvActivationTimeOptions1Min.setText(CataSettingConstant.sActivationTime1min);

            tvActivationTimeOptions5Min.setText("");
            tvActivationTimeOptions5Min.setText(CataSettingConstant.sActivationTime5min);

            tvActivationTimeOptions15Min.setText("");
            tvActivationTimeOptions15Min.setText(CataSettingConstant.sActivationTime15min);

            tvActivationTimeOptions30Min.setText("");
            tvActivationTimeOptions30Min.setText(CataSettingConstant.sActivationTime30min);

            tvActivationTimeOptions10Sec.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvActivationTimeOptions30Sec.setTextColor(getResources().getColor(R.color.settingmodule_white));
            tvActivationTimeOptions1Min.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvActivationTimeOptions5Min.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvActivationTimeOptions15Min.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvActivationTimeOptions30Min.setTextColor(getResources().getColor(R.color.settingmodule_gray));

        } else if (id == R.id.tv_activation_time_options_1_min) {

            spanText = new SpannableString(CataSettingConstant.sActivationTime1min);
            spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tvActivationTimeOptions1Min.setText(spanText);

            tvActivationTimeOptions30Sec.setText("");
            tvActivationTimeOptions30Sec.setText(CataSettingConstant.sActivationTime30sce);

            tvActivationTimeOptions10Sec.setText("");
            tvActivationTimeOptions10Sec.setText(CataSettingConstant.sActivationTime10sce);

            tvActivationTimeOptions5Min.setText("");
            tvActivationTimeOptions5Min.setText(CataSettingConstant.sActivationTime5min);

            tvActivationTimeOptions15Min.setText("");
            tvActivationTimeOptions15Min.setText(CataSettingConstant.sActivationTime15min);

            tvActivationTimeOptions30Min.setText("");
            tvActivationTimeOptions30Min.setText(CataSettingConstant.sActivationTime30min);

            tvActivationTimeOptions10Sec.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvActivationTimeOptions30Sec.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvActivationTimeOptions1Min.setTextColor(getResources().getColor(R.color.settingmodule_white));
            tvActivationTimeOptions5Min.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvActivationTimeOptions15Min.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvActivationTimeOptions30Min.setTextColor(getResources().getColor(R.color.settingmodule_gray));
        } else if (id == R.id.tv_activation_time_options_5_min) {

            spanText = new SpannableString(CataSettingConstant.sActivationTime5min);
            spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tvActivationTimeOptions5Min.setText(spanText);

            tvActivationTimeOptions30Sec.setText("");
            tvActivationTimeOptions30Sec.setText(CataSettingConstant.sActivationTime30sce);

            tvActivationTimeOptions10Sec.setText("");
            tvActivationTimeOptions10Sec.setText(CataSettingConstant.sActivationTime10sce);

            tvActivationTimeOptions1Min.setText("");
            tvActivationTimeOptions1Min.setText(CataSettingConstant.sActivationTime1min);

            tvActivationTimeOptions15Min.setText("");
            tvActivationTimeOptions15Min.setText(CataSettingConstant.sActivationTime15min);

            tvActivationTimeOptions30Min.setText("");
            tvActivationTimeOptions30Min.setText(CataSettingConstant.sActivationTime30min);

            tvActivationTimeOptions10Sec.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvActivationTimeOptions30Sec.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvActivationTimeOptions1Min.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvActivationTimeOptions5Min.setTextColor(getResources().getColor(R.color.settingmodule_white));
            tvActivationTimeOptions15Min.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvActivationTimeOptions30Min.setTextColor(getResources().getColor(R.color.settingmodule_gray));
        } else if (id == R.id.tv_activation_time_options_15_min) {

            spanText = new SpannableString(CataSettingConstant.sActivationTime15min);
            spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tvActivationTimeOptions15Min.setText(spanText);

            tvActivationTimeOptions30Sec.setText("");
            tvActivationTimeOptions30Sec.setText(CataSettingConstant.sActivationTime30sce);

            tvActivationTimeOptions10Sec.setText("");
            tvActivationTimeOptions10Sec.setText(CataSettingConstant.sActivationTime10sce);

            tvActivationTimeOptions5Min.setText("");
            tvActivationTimeOptions5Min.setText(CataSettingConstant.sActivationTime5min);

            tvActivationTimeOptions1Min.setText("");
            tvActivationTimeOptions1Min.setText(CataSettingConstant.sActivationTime1min);

            tvActivationTimeOptions30Min.setText("");
            tvActivationTimeOptions30Min.setText(CataSettingConstant.sActivationTime30min);


            tvActivationTimeOptions10Sec.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvActivationTimeOptions30Sec.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvActivationTimeOptions1Min.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvActivationTimeOptions5Min.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvActivationTimeOptions15Min.setTextColor(getResources().getColor(R.color.settingmodule_white));
            tvActivationTimeOptions30Min.setTextColor(getResources().getColor(R.color.settingmodule_gray));
        } else if (id == R.id.tv_activation_time_options_30_min) {

            spanText = new SpannableString(CataSettingConstant.sActivationTime30min);
            spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tvActivationTimeOptions30Min.setText(spanText);

            tvActivationTimeOptions30Sec.setText("");
            tvActivationTimeOptions30Sec.setText(CataSettingConstant.sActivationTime30sce);

            tvActivationTimeOptions10Sec.setText("");
            tvActivationTimeOptions10Sec.setText(CataSettingConstant.sActivationTime10sce);

            tvActivationTimeOptions5Min.setText("");
            tvActivationTimeOptions5Min.setText(CataSettingConstant.sActivationTime5min);

            tvActivationTimeOptions15Min.setText("");
            tvActivationTimeOptions15Min.setText(CataSettingConstant.sActivationTime15min);

            tvActivationTimeOptions1Min.setText("");
            tvActivationTimeOptions1Min.setText(CataSettingConstant.sActivationTime1min);

            tvActivationTimeOptions10Sec.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvActivationTimeOptions30Sec.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvActivationTimeOptions1Min.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvActivationTimeOptions5Min.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvActivationTimeOptions15Min.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvActivationTimeOptions30Min.setTextColor(getResources().getColor(R.color.settingmodule_white));
        }
    }

    private void updateTotalTurnOffDurationUI(int id) {
        SpannableString spanText;
        spanText = new SpannableString("UnderlineSpan");
        spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        if (id == R.id.tv_total_turn_off_time_options_30_min) {

            spanText = new SpannableString(CataSettingConstant.sTotalTurnOffTime30min);
            spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tvTotalTurnOffTimeOptions30Min.setText(spanText);

            tvTotalTurnOffTimeOptions1H.setText("");
            tvTotalTurnOffTimeOptions1H.setText(CataSettingConstant.sTotalTurnOffTime1h);

            tvTotalTurnOffTimeOptions6H.setText("");
            tvTotalTurnOffTimeOptions6H.setText(CataSettingConstant.sTotalTurnOffTime6h);


            tvTotalTurnOffTimeOptions12H.setText("");
            tvTotalTurnOffTimeOptions12H.setText(CataSettingConstant.sTotalTurnOffTime12h);

            tvTotalTurnOffTimeOptions24H.setText("");
            tvTotalTurnOffTimeOptions24H.setText(CataSettingConstant.sTotalTurnOffTime24h);

            tvTotalTurnOffTimeOptions72H.setText("");
            tvTotalTurnOffTimeOptions72H.setText(CataSettingConstant.sTotalTurnOffTime72h);


            tvTotalTurnOffTimeOptions30Min.setTextColor(getResources().getColor(R.color.settingmodule_white));
            tvTotalTurnOffTimeOptions1H.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvTotalTurnOffTimeOptions6H.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvTotalTurnOffTimeOptions12H.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvTotalTurnOffTimeOptions24H.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvTotalTurnOffTimeOptions72H.setTextColor(getResources().getColor(R.color.settingmodule_gray));

        } else if (id == R.id.tv_total_turn_off_time_options_1_h) {

            spanText = new SpannableString(CataSettingConstant.sTotalTurnOffTime1h);
            spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tvTotalTurnOffTimeOptions1H.setText(spanText);

            tvTotalTurnOffTimeOptions30Min.setText("");
            tvTotalTurnOffTimeOptions30Min.setText(CataSettingConstant.sTotalTurnOffTime30min);

            tvTotalTurnOffTimeOptions6H.setText("");
            tvTotalTurnOffTimeOptions6H.setText(CataSettingConstant.sTotalTurnOffTime6h);

            tvTotalTurnOffTimeOptions12H.setText("");
            tvTotalTurnOffTimeOptions12H.setText(CataSettingConstant.sTotalTurnOffTime12h);

            tvTotalTurnOffTimeOptions24H.setText("");
            tvTotalTurnOffTimeOptions24H.setText(CataSettingConstant.sTotalTurnOffTime24h);

            tvTotalTurnOffTimeOptions72H.setText("");
            tvTotalTurnOffTimeOptions72H.setText(CataSettingConstant.sTotalTurnOffTime72h);


            tvTotalTurnOffTimeOptions30Min.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvTotalTurnOffTimeOptions1H.setTextColor(getResources().getColor(R.color.settingmodule_white));
            tvTotalTurnOffTimeOptions6H.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvTotalTurnOffTimeOptions12H.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvTotalTurnOffTimeOptions24H.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvTotalTurnOffTimeOptions72H.setTextColor(getResources().getColor(R.color.settingmodule_gray));

        } else if (id == R.id.tv_total_turn_off_time_options_6_h) {

            spanText = new SpannableString(CataSettingConstant.sTotalTurnOffTime6h);
            spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tvTotalTurnOffTimeOptions6H.setText(spanText);

            tvTotalTurnOffTimeOptions30Min.setText("");
            tvTotalTurnOffTimeOptions30Min.setText(CataSettingConstant.sTotalTurnOffTime30min);

            tvTotalTurnOffTimeOptions1H.setText("");
            tvTotalTurnOffTimeOptions1H.setText(CataSettingConstant.sTotalTurnOffTime1h);

            tvTotalTurnOffTimeOptions12H.setText("");
            tvTotalTurnOffTimeOptions12H.setText(CataSettingConstant.sTotalTurnOffTime12h);

            tvTotalTurnOffTimeOptions24H.setText("");
            tvTotalTurnOffTimeOptions24H.setText(CataSettingConstant.sTotalTurnOffTime24h);

            tvTotalTurnOffTimeOptions72H.setText("");
            tvTotalTurnOffTimeOptions72H.setText(CataSettingConstant.sTotalTurnOffTime72h);


            tvTotalTurnOffTimeOptions30Min.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvTotalTurnOffTimeOptions1H.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvTotalTurnOffTimeOptions6H.setTextColor(getResources().getColor(R.color.settingmodule_white));
            tvTotalTurnOffTimeOptions12H.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvTotalTurnOffTimeOptions24H.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvTotalTurnOffTimeOptions72H.setTextColor(getResources().getColor(R.color.settingmodule_gray));

        } else if (id == R.id.tv_total_turn_off_time_options_12_h) {

            spanText = new SpannableString(CataSettingConstant.sTotalTurnOffTime12h);
            spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tvTotalTurnOffTimeOptions12H.setText(spanText);

            tvTotalTurnOffTimeOptions30Min.setText("");
            tvTotalTurnOffTimeOptions30Min.setText(CataSettingConstant.sTotalTurnOffTime30min);

            tvTotalTurnOffTimeOptions1H.setText("");
            tvTotalTurnOffTimeOptions1H.setText(CataSettingConstant.sTotalTurnOffTime1h);

            tvTotalTurnOffTimeOptions6H.setText("");
            tvTotalTurnOffTimeOptions6H.setText(CataSettingConstant.sTotalTurnOffTime6h);

            tvTotalTurnOffTimeOptions24H.setText("");
            tvTotalTurnOffTimeOptions24H.setText(CataSettingConstant.sTotalTurnOffTime24h);

            tvTotalTurnOffTimeOptions72H.setText("");
            tvTotalTurnOffTimeOptions72H.setText(CataSettingConstant.sTotalTurnOffTime72h);

            tvTotalTurnOffTimeOptions30Min.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvTotalTurnOffTimeOptions1H.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvTotalTurnOffTimeOptions6H.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvTotalTurnOffTimeOptions12H.setTextColor(getResources().getColor(R.color.settingmodule_white));
            tvTotalTurnOffTimeOptions24H.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvTotalTurnOffTimeOptions72H.setTextColor(getResources().getColor(R.color.settingmodule_gray));

        } else if (id == R.id.tv_total_turn_off_time_options_24_h) {

            spanText = new SpannableString(CataSettingConstant.sTotalTurnOffTime24h);
            spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tvTotalTurnOffTimeOptions24H.setText(spanText);

            tvTotalTurnOffTimeOptions30Min.setText("");
            tvTotalTurnOffTimeOptions30Min.setText(CataSettingConstant.sTotalTurnOffTime30min);

            tvTotalTurnOffTimeOptions1H.setText("");
            tvTotalTurnOffTimeOptions1H.setText(CataSettingConstant.sTotalTurnOffTime1h);

            tvTotalTurnOffTimeOptions12H.setText("");
            tvTotalTurnOffTimeOptions12H.setText(CataSettingConstant.sTotalTurnOffTime12h);

            tvTotalTurnOffTimeOptions6H.setText("");
            tvTotalTurnOffTimeOptions6H.setText(CataSettingConstant.sTotalTurnOffTime6h);

            tvTotalTurnOffTimeOptions72H.setText("");
            tvTotalTurnOffTimeOptions72H.setText(CataSettingConstant.sTotalTurnOffTime72h);

            tvTotalTurnOffTimeOptions30Min.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvTotalTurnOffTimeOptions1H.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvTotalTurnOffTimeOptions6H.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvTotalTurnOffTimeOptions12H.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvTotalTurnOffTimeOptions24H.setTextColor(getResources().getColor(R.color.settingmodule_white));
            tvTotalTurnOffTimeOptions72H.setTextColor(getResources().getColor(R.color.settingmodule_gray));

        } else if (id == R.id.tv_total_turn_off_time_options_72_h) {

            spanText = new SpannableString(CataSettingConstant.sTotalTurnOffTime72h);
            spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tvTotalTurnOffTimeOptions72H.setText(spanText);

            tvTotalTurnOffTimeOptions30Min.setText("");
            tvTotalTurnOffTimeOptions30Min.setText(CataSettingConstant.sTotalTurnOffTime30min);

            tvTotalTurnOffTimeOptions1H.setText("");
            tvTotalTurnOffTimeOptions1H.setText(CataSettingConstant.sTotalTurnOffTime1h);

            tvTotalTurnOffTimeOptions12H.setText("");
            tvTotalTurnOffTimeOptions12H.setText(CataSettingConstant.sTotalTurnOffTime12h);

            tvTotalTurnOffTimeOptions24H.setText("");
            tvTotalTurnOffTimeOptions24H.setText(CataSettingConstant.sTotalTurnOffTime24h);

            tvTotalTurnOffTimeOptions6H.setText("");
            tvTotalTurnOffTimeOptions6H.setText(CataSettingConstant.sTotalTurnOffTime6h);

            tvTotalTurnOffTimeOptions30Min.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvTotalTurnOffTimeOptions1H.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvTotalTurnOffTimeOptions6H.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvTotalTurnOffTimeOptions12H.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvTotalTurnOffTimeOptions24H.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tvTotalTurnOffTimeOptions72H.setTextColor(getResources().getColor(R.color.settingmodule_white));

        }
    }

    private void saveTotalTurnOffTime(int id) {
        if (id == R.id.tv_total_turn_off_time_options_30_min) {
            SettingPreferencesUtil.saveTotalTurnOffTime(getActivity(), 30 * 60);

        } else if (id == R.id.tv_total_turn_off_time_options_1_h) {
            SettingPreferencesUtil.saveTotalTurnOffTime(getActivity(), 60 * 60);

        } else if (id == R.id.tv_total_turn_off_time_options_6_h) {
            SettingPreferencesUtil.saveTotalTurnOffTime(getActivity(), 60 * 60 * 6);

        } else if (id == R.id.tv_total_turn_off_time_options_12_h) {
            SettingPreferencesUtil.saveTotalTurnOffTime(getActivity(), 60 * 60 * 12);

        } else if (id == R.id.tv_total_turn_off_time_options_24_h) {
            SettingPreferencesUtil.saveTotalTurnOffTime(getActivity(), 60 * 60 * 24);

        } else if (id == R.id.tv_total_turn_off_time_options_72_h) {
            SettingPreferencesUtil.saveTotalTurnOffTime(getActivity(), 60 * 60 * 72);

        }
    }

    private void saveActivationTime(int id) {
        if (id == R.id.tv_activation_time_options_10_sec) {
            SettingPreferencesUtil.saveActivationTime(getActivity(), 10);

        } else if (id == R.id.tv_activation_time_options_30_sec) {
            SettingPreferencesUtil.saveActivationTime(getActivity(), 30);

        } else if (id == R.id.tv_activation_time_options_1_min) {
            SettingPreferencesUtil.saveActivationTime(getActivity(), 60);
        } else if (id == R.id.tv_activation_time_options_5_min) {
            SettingPreferencesUtil.saveActivationTime(getActivity(), 60 * 5);
        } else if (id == R.id.tv_activation_time_options_15_min) {
            SettingPreferencesUtil.saveActivationTime(getActivity(), 60 * 15);
        } else if (id == R.id.tv_activation_time_options_30_min) {
            SettingPreferencesUtil.saveActivationTime(getActivity(), 60 * 30);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton == swHibernationMode) {
            if (b) {
                swHibernationMode.setChecked(true);
                updateHibernationModeUI(true);
                SettingPreferencesUtil.saveHibernationModeSwitchStatus(getActivity(), CataSettingConstant.HIBERNATION_MODE_SWITCH_STATUS_OPEN);
            } else {
                swHibernationMode.setChecked(false);
                updateHibernationModeUI(false);
                SettingPreferencesUtil.saveHibernationModeSwitchStatus(getActivity(), CataSettingConstant.HIBERNATION_MODE_SWITCH_STATUS_CLOSE);
            }
        }
    }

    private void updateHibernationModeUI(boolean isShow) {
        if (isShow) {
            flHibernationFormat.setVisibility(View.VISIBLE);
            llTurnOff.setVisibility(View.VISIBLE);
        } else {
            flHibernationFormat.setVisibility(View.GONE);
            llTurnOff.setVisibility(View.GONE);
        }
    }

    private void updateTotalTurnOffTimeUI(boolean isShow) {
        if (isShow) {
            llTurnOff.setVisibility(View.VISIBLE);
        } else {
            llTurnOff.setVisibility(View.GONE);
        }
    }

    private void adjustUI() {
        LinearLayout.LayoutParams lpActivationTime = (LinearLayout.LayoutParams)llActivationTime.getLayoutParams();
        LinearLayout.LayoutParams lpActivationTimeDuration = (LinearLayout.LayoutParams)llActivationTimeDuration.getLayoutParams();
        LinearLayout.LayoutParams lpHibernationMode = (LinearLayout.LayoutParams)flHibernationMode.getLayoutParams();
        LinearLayout.LayoutParams lpTurnOff = (LinearLayout.LayoutParams)llTurnOff.getLayoutParams();
        LinearLayout.LayoutParams lpTurnOffDuration = (LinearLayout.LayoutParams)llTurnOffDuration.getLayoutParams();

        LinearLayout.LayoutParams lpHibernationFormat = (LinearLayout.LayoutParams)flHibernationFormat.getLayoutParams();
        switch (GlobalVars.getInstance().getCurrentLocale().toString().toLowerCase()) {
            case "el":
            case "pl":
            case "ru":
            case "vi":
                lpActivationTime.topMargin = 5;
                lpActivationTimeDuration.topMargin = 16;
                lpHibernationMode.topMargin = 24;
                lpTurnOff.topMargin = 24;
                lpTurnOffDuration.topMargin = 16;
                lpHibernationFormat.topMargin = 16;
                break;
            default:
                lpActivationTime.topMargin = 10;
                lpActivationTimeDuration.topMargin = 28;
                lpHibernationMode.topMargin = 36;
                lpTurnOff.topMargin = 36;
                lpTurnOffDuration.topMargin = 28;
                lpHibernationFormat.topMargin = 28;
                break;
        }

        llActivationTime.setLayoutParams(lpActivationTime);
        llActivationTimeDuration.setLayoutParams(lpActivationTimeDuration);
        flHibernationMode.setLayoutParams(lpHibernationMode);
        llTurnOff.setLayoutParams(lpTurnOff);
        llTurnOffDuration.setLayoutParams(lpTurnOffDuration);
        flHibernationFormat.setLayoutParams(lpHibernationFormat);
    }


    public void setOnEnergySettingsFragmentListener(OnEnergySettingsFragmentListener listener) {
        this.listener = listener;
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

    public interface OnEnergySettingsFragmentListener extends OnFragmentListener {
        void onEnergySettingsFragmentRequestFormatSetting();
    }
}
