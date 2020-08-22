package com.ekek.settingmodule.view;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.commonmodule.utils.LogUtil;
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

public class HibernationFormatSettingFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {
    @BindView(R2.id.ct_hour_format)
    TextView ctHourFormat;
    @BindView(R2.id.ct_hour_digital)
    CheckedTextView ctHourDigital;
    @BindView(R2.id.ct_hour_analogic)
    CheckedTextView ctHourAnalogic;
    @BindView(R2.id.sw_date)
    NoTextSwitch swDate;
    @BindView(R2.id.ct_message)
    CheckedTextView ctMessage;
    @BindView(R2.id.ct_off)
    CheckedTextView ctOff;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.title_date)
    TextView titleDate;
    @BindView(R2.id.sw_logo)
    NoTextSwitch swLogo;
    Unbinder unbinder;
    @BindView(R2.id.title_logo)
    TextView titleLogo;

    public Typeface typeface;


    @Override
    public int initLyaout() {
        return R.layout.settingmodule_fragment_hibernation_format_setting;
    }

    @Override
    public void initListener() {

    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        SetFont();
        SetUI();
    }

    private void SetUI(){
        if(ProductManager.PRODUCT_TYPE== ProductManager.Haier){
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_energy_settings_haier);
            drawable.setBounds(0, 0, 53, 53);
            tvTitle.setCompoundDrawables(drawable, null, null, null);
        }
    }



    private void SetFont() {

        typeface = GlobalVars.getInstance().getDefaultFontRegular();
        tvTitle.setTypeface(typeface);
        ctHourFormat.setTypeface(typeface);
        ctHourDigital.setTypeface(typeface);
        ctHourAnalogic.setTypeface(typeface);
        ctMessage.setTypeface(typeface);
        ctOff.setTypeface(typeface);
        titleDate.setTypeface(typeface);
        titleLogo.setTypeface(typeface);
    }
    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        String hibernationHourFormat = SettingPreferencesUtil.getHibernationHourFormat(getActivity());
        if (hibernationHourFormat.equals(CataSettingConstant.HIBERNATION_FORMAT_HOUR_FORMAT_DIGITAL)) {
            ctHourDigital.setChecked(true);
            ctHourAnalogic.setChecked(false);
        } else {
            ctHourDigital.setChecked(false);
            ctHourAnalogic.setChecked(true);
        }

        String hibernationFormatDateSwitchStatus = SettingPreferencesUtil.getHibernationFormatDateSwitchStatus(getActivity());
        if (hibernationFormatDateSwitchStatus.equals(CataSettingConstant.HIBERNATION_FORMAT_DATE_SWITCH_STATUS_OPEN)) {
            swDate.setChecked(true);
        } else {
            swDate.setChecked(false);
        }

        swDate.setOnCheckedChangeListener(this);

        String logoSwitchStatus = SettingPreferencesUtil.getLogoSwitchStatus(getActivity());
        if (logoSwitchStatus.equals(CataSettingConstant.LOGO_SWITCH_STATUS_OPEN)) {
            swLogo.setChecked(true);
        } else {
            swLogo.setChecked(false);
        }
        swLogo.setOnCheckedChangeListener(this);
    }

    @OnClick({R2.id.ct_hour_format, R2.id.tv_title, R2.id.ct_hour_digital, R2.id.ct_hour_analogic, R2.id.ct_message, R2.id.ct_off})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.ct_hour_format) {

        } else if (i == R.id.ct_hour_digital) {
            ctHourDigital.setChecked(true);
            ctHourAnalogic.setChecked(false);
            SettingPreferencesUtil.saveHibernationHourFormat(getActivity(), CataSettingConstant.HIBERNATION_FORMAT_HOUR_FORMAT_DIGITAL);
        } else if (i == R.id.ct_hour_analogic) {
            ctHourDigital.setChecked(false);
            ctHourAnalogic.setChecked(true);
            SettingPreferencesUtil.saveHibernationHourFormat(getActivity(), CataSettingConstant.HIBERNATION_FORMAT_HOUR_FORMAT_ANALOGIC);

        } else if (i == R.id.ct_message) {

        } else if (i == R.id.ct_off) {

        }else if (i == R.id.tv_title) {
            EventBus.getDefault().post(new SettingDoBack(CataSettingConstant.do_back));
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton == swDate) {
            if (b) {
                swDate.setChecked(true);
                SettingPreferencesUtil.saveHibernationFormatDateSwitchStatus(getActivity(), CataSettingConstant.HIBERNATION_FORMAT_DATE_SWITCH_STATUS_OPEN);
            } else {
                swDate.setChecked(false);
                SettingPreferencesUtil.saveHibernationFormatDateSwitchStatus(getActivity(), CataSettingConstant.HIBERNATION_FORMAT_DATE_SWITCH_STATUS_CLOSE);

            }
        }

        if (compoundButton == swLogo) {
            if (b) {
                swLogo.setChecked(true);
                SettingPreferencesUtil.saveLogoSwitchStatus(getActivity(), CataSettingConstant.LOGO_SWITCH_STATUS_OPEN);
            } else {
                swLogo.setChecked(false);
                SettingPreferencesUtil.saveLogoSwitchStatus(getActivity(), CataSettingConstant.LOGO_SWITCH_STATUS_CLOSE);

            }
        }

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            //    init();
            SetClickSoundEffect();
      //      LogUtil.d("liang show");
        } else {
          //  LogUtil.d("liang hidden");
        }
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
}
