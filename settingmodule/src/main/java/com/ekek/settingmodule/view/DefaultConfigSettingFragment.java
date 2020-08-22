package com.ekek.settingmodule.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.commonmodule.utils.Logger;
import com.ekek.settingmodule.R;
import com.ekek.settingmodule.R2;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.database.SecurityDBUtil;
import com.ekek.settingmodule.database.SettingPreferencesUtil;
import com.ekek.settingmodule.entity.SecurityTable;
import com.ekek.settingmodule.events.DefaultSettingEvent;
import com.ekek.settingmodule.events.SendOrderTo;
import com.ekek.settingmodule.model.SendOrderForFirstSwitchOn;
import com.ekek.settingmodule.model.SettingDoBack;
import com.ekek.settingmodule.model.ShowBannerInformation;
import com.ekek.settingmodule.model.WhichBannerWillBeShow;
import com.ekek.settingmodule.utils.SoundUtil;
import com.ekek.viewmodule.listener.OnPasswordListener;
import com.ekek.viewmodule.listener.PatternLockViewListener;
import com.ekek.viewmodule.passwordview.PatternLockView;
import com.ekek.viewmodule.passwordview.PinPasswordView;
import com.ekek.viewmodule.product.ProductManager;
import com.ekek.viewmodule.utils.PatternLockUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Samhung on 2018/2/2.
 */

public class DefaultConfigSettingFragment extends BaseFragment implements OnPasswordListener, PatternLockViewListener {

    @BindView(R2.id.tv_restore)
    TextView tvRestore;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    Unbinder unbinder;
    @BindView(R2.id.tv_restore_question)
    TextView tvRestoreQuestion;
    @BindView(R2.id.tv_question_yes)
    TextView tvQuestionYes;
    @BindView(R2.id.tv_question_no)
    TextView tvQuestionNo;
    @BindView(R2.id.pattern_lock_view)
    PatternLockView patternLockView;
    @BindView(R2.id.ppv)
    PinPasswordView ppv;
    private Typeface typeface;

    private SecurityTable security;
    private String password ="";



    @Override
    public int initLyaout() {
        return R.layout.settingmodule_fragment_setting_default_config;
    }

    @Override
    public void initListener() {

    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        SetFont();
        SetUI();
        ShowUI("E100");
    }

    private void SetUI() {
        if (ProductManager.PRODUCT_TYPE == ProductManager.Haier) {
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_default_setting_haier);
            drawable.setBounds(0, 0, 53, 53);
            tvTitle.setCompoundDrawables(drawable, null, null, null);
        }
    }

    private void SetFont() {

        typeface = GlobalVars.getInstance().getDefaultFontRegular();
        tvTitle.setTypeface(typeface);
        tvRestore.setTypeface(typeface);
        tvRestoreQuestion.setTypeface(typeface);
        tvQuestionYes.setTypeface(typeface);
        tvQuestionNo.setTypeface(typeface);
    }

    @OnClick(R2.id.tv_restore)
    public void onViewClicked() {
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

    @OnClick({R2.id.tv_title, R2.id.tv_restore,R2.id.tv_question_no,R2.id.tv_question_yes})
    public void onClick(View view) {
        if (view.getId() == R.id.tv_title) {
            EventBus.getDefault().post(new SettingDoBack(CataSettingConstant.do_back));
        } else if (view.getId() == R.id.tv_restore) {
            ShowUI("E101");
        }else if(view .getId() ==R.id.tv_question_no){
            ShowUI("E100");
           // LogUtil.d("liang show e100");
        }else if(view.getId() ==R.id.tv_question_yes){
            ShowUI("E100");
        //    LogUtil.d("liang show e100");
         //   SettingPreferencesUtil.saveTheFirstTimeSwitchOnHob(getActivity(),CataSettingConstant.DEFAULT_THE_FIRST_TIME_SWITCH_ON_HOB);  // 清除数据
            ShowSecurityCode();
        }
    }

    private void ShowSecurityCode() {
        SecurityTable security = SecurityDBUtil.getDefaultSecurity(getContext());
        String type = security.getType();
        if (type.equals(CataSettingConstant.SECURITY_MODE_PRESS_UNLOCK)) {  // 2秒

            ShowUI("E104");
            ReadyToReset();
            ResetAllSetting();
        } else if (type.equals(CataSettingConstant.SECURITY_MODE_PIN)) {  // 数字  PIN
            initPinModify();
            EventBus.getDefault().post(new WhichBannerWillBeShow(CataSettingConstant.INPUT_THE_PIN_CODE));
            ShowUI("E103");

        } else if (type.equals(CataSettingConstant.SECURITY_MODE_PATTERN)) {  // 图形  Pattern
            initPatternModify();
            EventBus.getDefault().post(new WhichBannerWillBeShow(CataSettingConstant.INPUT_THE_PATTERN));
            ShowUI("E102");

        }
    }

    private void initPinModify() {
        password = "";
        security = SecurityDBUtil.getSecurity(getActivity(), CataSettingConstant.SECURITY_MODE_PIN);
        ppv.setMode(PinPasswordView.MODE_SET_PASSWORD);
        ppv.setOnPasswordListener(this);

    }

    private void initPatternModify() {
        password = "";
        security = SecurityDBUtil.getSecurity(getActivity(), CataSettingConstant.SECURITY_MODE_PATTERN);

        patternLockView.setAspectRatioEnabled(true);
        patternLockView.setAspectRatio(PatternLockView.AspectRatio.ASPECT_RATIO_WIDTH_BIAS);
        patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
        patternLockView.setDotAnimationDuration(150);
        patternLockView.setPathEndAnimationDuration(100);
        patternLockView.setInStealthMode(false);
        patternLockView.setTactileFeedbackEnabled(true);
        patternLockView.setWrongStateColor(Color.RED);
        patternLockView.setInputEnabled(true);
        patternLockView.addPatternLockListener(this);
        patternLockView.clearPattern();
    }

    private void ResetAllSetting() {

        // display
        SettingPreferencesUtil.saveDefaultBrightness(getActivity(), String.valueOf(255)); //  显示的亮度
        // sound
        // sound
        SettingPreferencesUtil.saveSoundSwitchStatus(getActivity(), CataSettingConstant.SOUND_SWITCH_STATUS_OPEN);
        SoundUtil.setSystemVolume(getActivity(), 12);
        SoundUtil.setAlarmVolume(getActivity(), 5);
        SettingPreferencesUtil.saveDefaultSound(getActivity(), String.valueOf(12));
        SettingPreferencesUtil.saveClickSoundSwitchStatus(getActivity(), CataSettingConstant.CLICK_SOUND_SWITCH_STATUS_OPEN);
        // connectivity
        SettingPreferencesUtil.saveBluetoothSwitchStatus(getActivity(), CataSettingConstant.BLUETOOTH_SWITCH_STATUS_OPEN);
        SettingPreferencesUtil.saveBluetoothStyle(getActivity(),CataSettingConstant.BLUETOOTH_SENSOR_STYLE);

        // power limit
        SettingPreferencesUtil.savePowerLimitSwitchStatus(getActivity(), CataSettingConstant.POWER_LIMIT_SWITCH_STATUS_CLOSE);
        SettingPreferencesUtil.savePowerLimitLevel(getActivity(), CataSettingConstant.POWER_LIMIT_LEVEL_5700);

        // date & time
        SettingPreferencesUtil.saveTimeFormat24(getActivity(), CataSettingConstant.TIME_FORMAT_24);


        // alarm setting
        SettingPreferencesUtil.saveAlarmDuration(getActivity(), 60 * 2);
        SettingPreferencesUtil.saveAlarmPostponeDuration(getActivity(), 60 * 10);
        SettingPreferencesUtil.saveAlarmVolumeLevel(getActivity(), 5);

        // language
        SettingPreferencesUtil.saveDefaultLanguage(getActivity(), CataSettingConstant.DEFAULT_LANGUAGE);
        SettingPreferencesUtil.saveDefaultLanguage2(getActivity(), CataSettingConstant.LANGUAGE_UNKNOWN);

        // security
        cleanAllPassword();// 清空上次设置的  码
        SecurityDBUtil.setDefaultSecurity(getActivity(), CataSettingConstant.SECURITY_MODE_PRESS_UNLOCK);

        // hood options
        SettingPreferencesUtil.saveStablishConnectionSwitchStatus(getActivity(), CataSettingConstant.STABLISH_CONNECTION_SWITCH_STATUS_CLOSE);
        SettingPreferencesUtil.saveHoodOptionsAutoMode(getActivity(), CataSettingConstant.HOOD_OPTIONS_AUTO_MODE_INTENSE);
        SettingPreferencesUtil.saveFanWorkingStatus(getContext(), CataSettingConstant.FAN_WORKING_STATUS_MANUAL);

        // information

        SettingPreferencesUtil.saveDemoSwitchStatus(getActivity(),CataSettingConstant.DEMO_SWITCH_STATUS_CLOSE);


        // energy setting

        SettingPreferencesUtil.saveActivationTime(getActivity(), 60 * 5);
        SettingPreferencesUtil.saveTotalTurnOffTime(getActivity(), 60 * 60 * 24);
        SettingPreferencesUtil.saveHibernationModeSwitchStatus(getActivity(), CataSettingConstant.HIBERNATION_MODE_SWITCH_STATUS_OPEN);

        // hibernation format
        SettingPreferencesUtil.saveHibernationFormatDateSwitchStatus(getActivity(), CataSettingConstant.HIBERNATION_FORMAT_DATE_SWITCH_STATUS_OPEN);
        SettingPreferencesUtil.saveHibernationHourFormat(getActivity(), CataSettingConstant.HIBERNATION_FORMAT_HOUR_FORMAT_DIGITAL);
        SettingPreferencesUtil.saveLogoSwitchStatus(getActivity(), CataSettingConstant.LOGO_SWITCH_STATUS_OPEN);

        // others
        SettingPreferencesUtil.saveTheFirstTimeSwitchOnHob(getActivity(),CataSettingConstant.DEFAULT_THE_FIRST_TIME_SWITCH_ON_HOB);  // 保存 ，是第一次开机

        // air renovation

        // start configuration E130
        EventBus.getDefault().post(new SendOrderForFirstSwitchOn(CataSettingConstant.is24Format));
        Intent intent = new Intent();
        intent.setAction("ACTION_CHANGE_12_24_FORMAT");
        getActivity().sendBroadcast(intent);
       // EventBus.getDefault().post(new DefaultSettingEvent(CataSettingConstant.SETTING_AFTER_DEFAULT_SETTING));
        GlobalVars.getInstance().setFirstTimeForHoodPanelFragment(true);// 第一次启动 hood 界面

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Logger.getInstance().e(e);
                }
                EventBus.getDefault().post(new ShowBannerInformation(CataSettingConstant.DEFAULT_SETTINGS_SUCCESSFULLY));
            }
        }).start();
    }

    private void cleanAllPassword(){
        // 清空上次设置的 pin 码
        SecurityTable pinSecurity = SecurityDBUtil.getSecurity(getActivity(), CataSettingConstant.SECURITY_MODE_PIN);
        if (!pinSecurity.getPassword().equals("")) {
            SecurityDBUtil.setDefaultSecurity(getActivity(), CataSettingConstant.SECURITY_MODE_PIN,"");
        }

        // 清空上次的图像码
        SecurityTable patternSecurity = SecurityDBUtil.getSecurity(getActivity(), CataSettingConstant.SECURITY_MODE_PATTERN);
        if (!patternSecurity.getPassword().equals("")) {
            SecurityDBUtil.setDefaultSecurity(getActivity(), CataSettingConstant.SECURITY_MODE_PATTERN,"");
        }
    }

    private void ReadyToReset(){
      //  EventBus.getDefault().post(new SendOrderTo(CataSettingConstant.CannotDoBack));
        /*mDoingReset=true;
        iniProgressBar();
        startCountdown();*/
    }

    private void ShowUI(String id){
        switch (id){
            case "E100":
                tvRestore.setVisibility(View.VISIBLE);
                tvRestoreQuestion.setVisibility(View.INVISIBLE);
                tvQuestionYes.setVisibility(View.INVISIBLE);
                tvQuestionNo.setVisibility(View.INVISIBLE);
                patternLockView.setVisibility(View.INVISIBLE);
                ppv.setVisibility(View.INVISIBLE);
                break;
            case "E101":
                tvRestore.setVisibility(View.INVISIBLE);
                tvRestoreQuestion.setVisibility(View.VISIBLE);
                tvQuestionYes.setVisibility(View.VISIBLE);
                tvQuestionNo.setVisibility(View.VISIBLE);
                patternLockView.setVisibility(View.INVISIBLE);
                ppv.setVisibility(View.INVISIBLE);
                break;
            case "E102":
                tvRestore.setVisibility(View.INVISIBLE);
                tvRestoreQuestion.setVisibility(View.INVISIBLE);
                tvQuestionYes.setVisibility(View.INVISIBLE);
                tvQuestionNo.setVisibility(View.INVISIBLE);
                patternLockView.setVisibility(View.VISIBLE);
                ppv.setVisibility(View.INVISIBLE);
                break;
            case "E103":
                tvRestore.setVisibility(View.INVISIBLE);
                tvRestoreQuestion.setVisibility(View.INVISIBLE);
                tvQuestionYes.setVisibility(View.INVISIBLE);
                tvQuestionNo.setVisibility(View.INVISIBLE);
                patternLockView.setVisibility(View.INVISIBLE);
                ppv.setVisibility(View.VISIBLE);

                break;
            case "E104":
                tvRestore.setVisibility(View.INVISIBLE);
                tvRestoreQuestion.setVisibility(View.INVISIBLE);
                tvQuestionYes.setVisibility(View.INVISIBLE);
                tvQuestionNo.setVisibility(View.INVISIBLE);
                patternLockView.setVisibility(View.INVISIBLE);
                ppv.setVisibility(View.INVISIBLE);
                break;

        }
    }

    @Override
    public boolean onCheckPwd(String pwd) {

        if (pwd.equals(security.getPassword())) {
            //  SecurityDBUtil.setDefaultSecurity(getActivity(), CataSettingConstant.SECURITY_MODE_PIN,password);
            //设置完成
            ShowUI("E104");
            ReadyToReset();
            ResetAllSetting();
            return true;
        }else return false;
    }

    @Override
    public void onWarning(String msg) {

    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onProgress(List<PatternLockView.Dot> progressPattern) {

    }

    @Override
    public void onComplete(List<PatternLockView.Dot> pattern) {
        if (PatternLockUtils.patternToString(patternLockView,pattern).equals(security.getPassword())){

            patternLockView.clearPattern();

            ShowUI("E104");
            ReadyToReset();
            ResetAllSetting();

        }else {
            patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
        }
    }

    @Override
    public void onCleared() {

    }
}
