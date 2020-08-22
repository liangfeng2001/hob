package com.ekek.settingmodule.view;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.commonmodule.utils.LogUtil;
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

/**
 * Created by Samhung on 2018/2/2.
 */

public class SoundSettingFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {
    @BindView(R2.id.sw_sound)
    NoTextSwitch swSound;
    @BindView(R2.id.sb_sound)
    SeekBar sbSound;
    @BindView(R2.id.ll_sound)
    LinearLayout llSound;
    @BindView(R2.id.sw_click_sound)
    NoTextSwitch swClickSound;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.title_sound)
    TextView titleSound;
    @BindView(R2.id.title_intensity)
    TextView titleIntensity;
    @BindView(R2.id.title_click_sound)
    TextView titleClickSound;
    Unbinder unbinder;
    private Typeface typeface;

    private void init() {
        boolean soundSwitchStatus = SettingPreferencesUtil.getSoundSwitchStatus(getActivity()).equals(CataSettingConstant.SOUND_SWITCH_STATUS_OPEN) ? true : false;
        if (soundSwitchStatus) {
            swSound.setChecked(true);
            llSound.setVisibility(View.VISIBLE);
        } else {
            swSound.setChecked(false);
            llSound.setVisibility(View.GONE);
        }

        boolean clickSoundSwitchStatus = SettingPreferencesUtil.getClickSoundSwitchStatus(getActivity()).equals(CataSettingConstant.CLICK_SOUND_SWITCH_STATUS_OPEN);
        if (clickSoundSwitchStatus) {
            swClickSound.setChecked(true);
        } else {
            swClickSound.setChecked(false);
        }


        swSound.setOnCheckedChangeListener(this);
        swClickSound.setOnCheckedChangeListener(this);

        sbSound.setMax(SoundUtil.getSystemMaxVolume(getActivity()));
        sbSound.setProgress(SoundUtil.getSystemCurrentVolume(getActivity()));
        sbSound.setOnSeekBarChangeListener(this);
        LogUtil.d("liang getSystemMaxVolume is "+ SoundUtil.getSystemMaxVolume(getActivity())+"; getSystemCurrentVolume(getActivity()) is  "+SoundUtil.getSystemCurrentVolume(getActivity()));

    }


    @Override
    public int initLyaout() {
        return R.layout.settingmodule_fragment_setting_sound;
    }

    @Override
    public void initListener() {

    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
      //  init();
        SetFont();
        SetUI();
      //  LogUtil.d("liang show soundsetting fragment ~~~");
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
        SetClickSoundEffect();
        LogUtil.d("liang start sound screen 1 ~~~~~~~~~~~~");
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

    private void SetUI(){
        if(ProductManager.PRODUCT_TYPE== ProductManager.Haier){
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_sound_haier);
            drawable.setBounds(0, 0, 53, 53);
            tvTitle.setCompoundDrawables(drawable, null, null, null);
        }
    }


    @OnClick(R2.id.tv_title)
    public void onClick() {
        EventBus.getDefault().post(new SettingDoBack(CataSettingConstant.do_back));
    }


    private void SetFont() {

        typeface = GlobalVars.getInstance().getDefaultFontRegular();
        tvTitle.setTypeface(typeface);
        titleClickSound.setTypeface(typeface);
        titleIntensity.setTypeface(typeface);
        titleSound.setTypeface(typeface);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton == swSound) {
            onSwSoundCheckedChanged(b);
        } else if (compoundButton == swClickSound) {
            onSwClickSoundCheckedChanged(b);
        }
    }



    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        SoundUtil.setSystemVolume(getActivity(), progress);
        SettingPreferencesUtil.saveDefaultSound(getActivity(), String.valueOf(progress));
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

    private void onSwSoundCheckedChanged(boolean checked) {
        if (checked) {
            llSound.setVisibility(View.VISIBLE);
            SettingPreferencesUtil.saveSoundSwitchStatus(getActivity(), CataSettingConstant.SOUND_SWITCH_STATUS_OPEN);
            int volume = Integer.valueOf(SettingPreferencesUtil.getDefaultSound(getActivity()));
            if(volume<=0){
                volume=1;
            }
            LogUtil.d("liang the sound volume is "+volume);
            sbSound.setProgress(volume);
            SoundUtil.setSystemVolume(getActivity(), volume);
            swSound.setChecked(true);
            onSwClickSoundCheckedChanged(swClickSound.isChecked());
        } else {
            llSound.setVisibility(View.GONE);
            // SoundUtil.setSystemMute(getActivity());
            SoundUtil.setSystemVolume(getActivity(), 1);
            SettingPreferencesUtil.saveSoundSwitchStatus(getActivity(), CataSettingConstant.SOUND_SWITCH_STATUS_CLOSE);
            onSwClickSoundCheckedChanged(false);
        }
    }
    private void onSwClickSoundCheckedChanged(boolean checked) {
        if (checked) {
            SettingPreferencesUtil.saveClickSoundSwitchStatus(getActivity(), CataSettingConstant.CLICK_SOUND_SWITCH_STATUS_OPEN);
            SoundUtil.setSoundEffect(getActivity(), true);
        } else {
            SettingPreferencesUtil.saveClickSoundSwitchStatus(getActivity(), CataSettingConstant.CLICK_SOUND_SWITCH_STATUS_CLOSE);
            SoundUtil.setSoundEffect(getActivity(), false);
        }
    }
}
