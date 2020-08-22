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
import com.ekek.settingmodule.model.SendOrderRecoverHoodConnectivity;
import com.ekek.settingmodule.model.SettingDoBack;
import com.ekek.settingmodule.model.ShowBannerInformation;
import com.ekek.viewmodule.common.NoTextSwitch;
import com.ekek.viewmodule.product.ProductManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Samhung on 2018/2/2.
 */

public class HoodOptionsSettingFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {


    @BindView(R2.id.sw_stablish_connection)
    NoTextSwitch swStablishConnection;
    @BindView(R2.id.sw_auto_mode)
    NoTextSwitch swAutoMode;
    @BindView(R2.id.ct_mode_intense)
    CheckedTextView ctModeIntense;
    @BindView(R2.id.ct_mode_ecologic)
    CheckedTextView ctModeEcologic;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.title_stabishi_connection)
    TextView titleStabishiConnection;
    @BindView(R2.id.title_auto_mode)
    TextView titleAutoMode;
    Unbinder unbinder;
    private Typeface typeface;


    private void init() {
      /*  if (SettingPreferencesUtil.getDefaultHoodMode(getActivity()).equals(CataSettingConstant.HOOD_MODE_MANUAL)) {
            updateManualMode();
        }else {
            updateAutomaticMode();
        }

        String hoodLevel = SettingPreferencesUtil.getDefaultHoodLevel(getActivity());
        if (hoodLevel.equals(CataSettingConstant.HOOD_LEVEL_LOW)) {
            updateHoodLevelUI(R.id.fl_low);
        }else if (hoodLevel.equals(CataSettingConstant.HOOD_LEVEL_MEDIUM)) {
            updateHoodLevelUI(R.id.fl_medium);
        }else if (hoodLevel.equals(CataSettingConstant.HOOD_LEVEL_HIGH)) {
            updateHoodLevelUI(R.id.fl_high);
        }*/
        boolean stablishConnectionSwitchStatus = SettingPreferencesUtil.getStablishConnectionSwitchStatus(getActivity()).equals(CataSettingConstant.STABLISH_CONNECTION_SWITCH_STATUS_OPEN) ? true : false;
        if (stablishConnectionSwitchStatus) {
            swStablishConnection.setChecked(true);
            ctModeIntense.setVisibility(View.VISIBLE);
            ctModeEcologic.setVisibility(View.VISIBLE);
            titleAutoMode.setVisibility(View.VISIBLE);
        } else {
            swStablishConnection.setChecked(false);
            ctModeIntense.setVisibility(View.INVISIBLE);
            ctModeEcologic.setVisibility(View.INVISIBLE);
            titleAutoMode.setVisibility(View.INVISIBLE);

        }
        boolean autoSwitchStatus = SettingPreferencesUtil.getHoodOptionsAutoModeSwitchStatus(getActivity()).equals(CataSettingConstant.DEFAULT_HOOD_OPTIONS_AUTO_MODE_SWITCH_STATUS) ? true : false;
        if (autoSwitchStatus) {
          /*  swAutoMode.setChecked(true);
            ctModeEcologic.setVisibility(View.VISIBLE);
            ctModeIntense.setVisibility(View.VISIBLE);*/

        } else {
         /*   swAutoMode.setChecked(false);
            ctModeEcologic.setVisibility(View.INVISIBLE);
            ctModeIntense.setVisibility(View.INVISIBLE);*/
        }

        int autoMode = SettingPreferencesUtil.getHoodOptionsAutoMode(getActivity());
        if (autoMode == CataSettingConstant.HOOD_OPTIONS_AUTO_MODE_INTENSE) {
            ctModeIntense.setChecked(true);
            ctModeEcologic.setChecked(false);

        } else if (autoMode == CataSettingConstant.HOOD_OPTIONS_AUTO_MODE_ECOLOGIC) {
            ctModeIntense.setChecked(false);
            ctModeEcologic.setChecked(true);
        }


        swAutoMode.setOnCheckedChangeListener(this);
        swStablishConnection.setOnCheckedChangeListener(this);


    }


    @Override
    public int initLyaout() {
        return R.layout.settingmodule_fragment_setting_hood_options;
    }

    @Override
    public void initListener() {

    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
      //  init();
        SetFont();
        SetUI();
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
        SetClickSoundEffect();
        registerEventBus();
    }

    @Override
    public void onStop() {
        super.onStop();
        //SettingPreferencesUtil.saveRequireHoodConnection(getActivity(), false);
    }

    private void SetUI(){
        if(ProductManager.PRODUCT_TYPE== ProductManager.Haier){
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_hood_options_haier);
            drawable.setBounds(0, 0, 53, 53);
            tvTitle.setCompoundDrawables(drawable, null, null, null);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton == swAutoMode) {
            if (b) {
                ctModeIntense.setVisibility(View.VISIBLE);
                ctModeEcologic.setVisibility(View.VISIBLE);
              //  SettingPreferencesUtil.saveHoodOptionsAutoModeSwitchStatus(getActivity(), CataSettingConstant.HOOD_OPTIONS_AUTO_MODE_SWITCH_STATUS_OPEN);
            } else {
                ctModeIntense.setVisibility(View.INVISIBLE);
                ctModeEcologic.setVisibility(View.INVISIBLE);
               // SettingPreferencesUtil.saveHoodOptionsAutoModeSwitchStatus(getActivity(), CataSettingConstant.HOOD_OPTIONS_AUTO_MODE_SWITCH_STATUS_CLOSE);

            }
        } else if (compoundButton == swStablishConnection) {
            if (b) {
                ctModeIntense.setVisibility(View.VISIBLE);
                ctModeEcologic.setVisibility(View.VISIBLE);
                titleAutoMode.setVisibility(View.VISIBLE);
                swStablishConnection.setChecked(true);
                EventBus.getDefault().post(new ShowBannerInformation(CataSettingConstant.HOOD_CONNECTIVITY));
                SettingPreferencesUtil.saveStablishConnectionSwitchStatus(getActivity(), CataSettingConstant.STABLISH_CONNECTION_SWITCH_STATUS_OPEN);
                SettingPreferencesUtil.saveFanWorkingStatus(getContext(), CataSettingConstant.FAN_WORKING_STATUS_AUTO);
              //  SettingPreferencesUtil.saveHoodSettingModeStatus(getActivity(), CataSettingConstant.HOOD_SETTING_MODE_OPEN);
              //  LogUtil.d("liang the hood connectivity is active ~~~");
            } else {
                ctModeIntense.setVisibility(View.INVISIBLE);
                ctModeEcologic.setVisibility(View.INVISIBLE);
                titleAutoMode.setVisibility(View.INVISIBLE);
                swStablishConnection.setChecked(false);
                SettingPreferencesUtil.saveStablishConnectionSwitchStatus(getActivity(), CataSettingConstant.STABLISH_CONNECTION_SWITCH_STATUS_CLOSE);
                SettingPreferencesUtil.saveFanWorkingStatus(getContext(), CataSettingConstant.FAN_WORKING_STATUS_MANUAL);
            }

        }

    }


    @OnClick({R2.id.ct_mode_intense, R2.id.ct_mode_ecologic})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.ct_mode_intense) {
            ctModeIntense.setChecked(true);
            ctModeEcologic.setChecked(false);
            SettingPreferencesUtil.saveHoodOptionsAutoMode(getActivity(), CataSettingConstant.HOOD_OPTIONS_AUTO_MODE_INTENSE);

        } else if (i == R.id.ct_mode_ecologic) {
            ctModeIntense.setChecked(false);
            ctModeEcologic.setChecked(true);
            SettingPreferencesUtil.saveHoodOptionsAutoMode(getActivity(), CataSettingConstant.HOOD_OPTIONS_AUTO_MODE_ECOLOGIC);
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
    @OnClick(R2.id.tv_title)
    public void onClick() {
        EventBus.getDefault().post(new SettingDoBack(CataSettingConstant.do_back));
    }



    private void SetFont() {

        typeface = GlobalVars.getInstance().getDefaultFontRegular();
        tvTitle.setTypeface(typeface);
        ctModeIntense.setTypeface(typeface);
        ctModeEcologic.setTypeface(typeface);
        titleStabishiConnection.setTypeface(typeface);
        titleAutoMode.setTypeface(typeface);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SendOrderRecoverHoodConnectivity event){
        int order;
        order =event .getOrder() ;
        if(order ==CataSettingConstant.CONNECTED_RECOVER){
            swStablishConnection.setChecked(false);
            ctModeIntense.setVisibility(View.INVISIBLE);
            ctModeEcologic.setVisibility(View.INVISIBLE);
            titleAutoMode.setVisibility(View.INVISIBLE);
          //  SettingPreferencesUtil.saveHobConnectivitySwitchStatus(getActivity(), CataSettingConstant.HOB_CONNECTIVITY_SWITCH_STATUS_CLOSE);
          //  SettingPreferencesUtil.saveHoodSettingModeStatus(getActivity(), CataSettingConstant.HOOD_SETTING_MODE_CLOSE);
            SettingPreferencesUtil.saveStablishConnectionSwitchStatus(getActivity(), CataSettingConstant.STABLISH_CONNECTION_SWITCH_STATUS_CLOSE);
            LogUtil.d("liang recover from~~~ 1");
        }
        LogUtil.d("liang recover from~~~ 2");
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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            SetClickSoundEffect();
            registerEventBus();
        }else {
            unregisterEventBus();
        }
    }


}
