package com.ekek.settingmodule.view;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalCons;
import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.settingmodule.R;
import com.ekek.settingmodule.R2;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.database.SettingPreferencesUtil;
import com.ekek.settingmodule.dialog.ToastDialog;
import com.ekek.settingmodule.events.SendOrderTo;
import com.ekek.settingmodule.events.TestCookwareSettingFragmentContentChanged;
import com.ekek.settingmodule.model.SendOrderForFirstSwitchOn;
import com.ekek.settingmodule.model.SendOrderToTestCookwareFragment;
import com.ekek.settingmodule.model.SettingDoBack;
import com.ekek.settingmodule.model.WhichBannerWillBeShow;
import com.ekek.viewmodule.product.ProductManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SettingFragment extends BaseFragment implements DateTimeSettingFragment.OnDateTimeSettingFragmentListener, SecuritySettingFragment.OnSecuritySettingFragmentListener, SettingMainFragment.OnSettingMainFragmentListener, InformationSettingFragment.OnInformationSettingFragmentListener, EnergySettingsFragment.OnEnergySettingsFragmentListener, SecuritySettingPinModifyFragment.OnSecuritySettingPinModifyFragmentListener, SecuritySettingPatternModifyFragment.OnSecuritySettingPatternModifyFragmentListener {
    @BindView(R2.id.ib_back)
    ImageButton ibBack;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.ic_info)
    ImageView icInfo;
    @BindView(R2.id.tv_up_toast)
    TextView tvUpToast;
    Unbinder unbinder;
    @BindView(R2.id.rl_full_screen)
    RelativeLayout rlFullScreen;
    private Fragment currentShowFragment, lastShowFragment;
    private SettingMainFragment mSettingMainFragment;
    private DisplaySettingFragment mDisplaySettingFragment;
    private SoundSettingFragment mSoundSettingFragment;
    private LanguageSettingFragment mLanguageSettingFragment;
    private HoodOptionsSettingFragment mHoodOptionsSettingFragment;
    private DefaultConfigSettingFragment mDefaultConfigSettingFragment;
    private PowerLimitSettingFragment mPowerLimitSettingFragment;
    private SecuritySettingFragment mSecuritySettingFragment;
    private SecuritySettingPinModifyFragment mSecuritySettingPinModifyFragment;
    private ConnectivitySettingFragment mConnectivitySettingFragment;
    private DateTimeSettingFragment mDateTimeSettingFragment;
    private InformationSettingFragment mInformationSettingFragment;
    private TestCookwareSettingFragment mTestCookwareSettingFragment;
    private DateSettingFragment mDateSettingFragment;
    private TimeSettingFragment mTimeSettingFragment;
    private AlarmSettingsFragment mAlarmSettingsFragment;
    private InstructionManualSettingFragment mInstructionManualSettingFragment;
    private KeyToIconsSettingFragment mKeyToIconsSettingFragment;
    private DemoModeSettingFragment mDemoModeSettingFragment;
    private UpdateSettingFragment mUpdateSettingFragment;
    private EnergySettingsFragment mEnergySettingsFragment;
    private HibernationFormatSettingFragment mHibernationFormatSettingFragment;
    private SecuritySettingPatternModifyFragment mSecuritySettingPatternModifyFragment;

    private static final int HANDLER_TOAST_FINISH = 2;

    private String bannerInfo = "";
    private boolean mHaveBeenClickSettingButton=false;  // 是否点击过 设置 按键
    private int mMode=1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case HANDLER_TOAST_FINISH:
                    updateToastUI(false, "");
                    break;
            }
        }
    };

    public SettingFragment(){

    }

    @SuppressLint("ValidFragment")
    public SettingFragment(int mode){
        this .mMode=mode;
    }

    public void setType(int mode) {
        this.mMode = mode;
    }

    @Override
    public int initLyaout() {
        return R.layout.settingmodule_fragment_setting;
    }

    @Override
    public void initListener() {

    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        Typeface typeface = GlobalVars.getInstance().getDefaultFontRegular();
        tvTitle.setTypeface(typeface);
        tvUpToast.setTypeface(typeface);
        //switchToSettingMainFragment();

    /*    switch (GlobalVars.getInstance().getAppStartTag()) {
            case GlobalCons.APP_START_NONE:  // // 不是第一次开机
                // 主界面
                switchToSettingMainFragment();
                break;
            case GlobalCons.APP_START_FIRST_TIME:  // 是第一次开机
                switchToLanguageSettingFragment();
                break;
            case GlobalCons.APP_START:
                switchToDateSettingFragment();  // 关机后，再开机
                break;
        }*/
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (mRootView == null) {
            return;
        }

//        LogUtil.d("liang Enter:: setting onHiddenChanged");
        if (!hidden) {
            // switchToSettingMainFragment();
            switch (GlobalVars.getInstance().getAppStartTag()) {
                case GlobalCons.APP_START_NONE: // 不是第一次开机
                    // 主界面
                    setMarginEnd(180);
                    switchToSettingMainFragment();
                    break;
                case GlobalCons.APP_START_FIRST_TIME: // 是第一次开机
                    if(mMode==CataSettingConstant.SETTING_AFTER_DEFAULT_SETTING){
                        setMarginEnd(180);
                        switchToSettingMainFragment();  // 主界面
                    }else {
                        setMarginEnd(30);
                        switchToLanguageSettingFragment(mHaveBeenClickSettingButton);
                        ibBack.setVisibility(View.INVISIBLE);
                        SettingPreferencesUtil.saveEnterPowerOffMode(getContext(), CataSettingConstant.EnterLanguageSettingFragment);
                    }
                    break;
                case GlobalCons.APP_START:// 关机后，再开机
                    //  switchToDateSettingFragment();
                    if(mMode==CataSettingConstant.SETTING_AFTER_DEFAULT_SETTING){
                        setMarginEnd(180);
                        switchToSettingMainFragment();  // 主界面
                    }else {
                        setMarginEnd(30);
                        switchToDateSettingFragment(mHaveBeenClickSettingButton);
                        SettingPreferencesUtil.saveEnterPowerOffMode(getContext(), CataSettingConstant.EnterDateSettingFragment);
                    }
                    break;
            }
            registerEventBus();
        } else {
            if (tvTitle != null) {
                   switchToSettingMainFragment();
            }
            unregisterEventBus();
        }
    }

    private void switchSecuritySettingPatternModifyFragment() {
        if (mSecuritySettingPatternModifyFragment == null) {
            mSecuritySettingPatternModifyFragment = new SecuritySettingPatternModifyFragment();
            mSecuritySettingPatternModifyFragment.setOnSecuritySettingPatternModifyFragmentListener(this);
        }
        switchFragment(mSecuritySettingPatternModifyFragment);
    }

    private void switchToHibernationFormatSettingFragment() {
        if (mHibernationFormatSettingFragment == null) {
            mHibernationFormatSettingFragment = new HibernationFormatSettingFragment();
        }
        switchFragment(mHibernationFormatSettingFragment);
    }

    private void switchToEnergySettingsFragment() {
        if (mEnergySettingsFragment == null) {
            mEnergySettingsFragment = new EnergySettingsFragment();
            mEnergySettingsFragment.setOnEnergySettingsFragmentListener(this);
        }
        switchFragment(mEnergySettingsFragment);
    }

    private void switchToUpdateSettingFragment() {

        if (mUpdateSettingFragment == null) {
            mUpdateSettingFragment = new UpdateSettingFragment();
        }
        switchFragment(mUpdateSettingFragment);
    }

    private void switchTomDemoModeSettingFragment() {

        if (mDemoModeSettingFragment == null) {
            mDemoModeSettingFragment = new DemoModeSettingFragment();
        }
        switchFragment(mDemoModeSettingFragment);
    }

    private void switchToKeyToIconsSettingFragment() {
        if (mKeyToIconsSettingFragment == null) {
            mKeyToIconsSettingFragment = new KeyToIconsSettingFragment();
        }
        switchFragment(mKeyToIconsSettingFragment);
    }

    private void switchToInstructionManualSettingFragment() {
        if (mInstructionManualSettingFragment == null) {
            mInstructionManualSettingFragment = new InstructionManualSettingFragment();
        }
        switchFragment(mInstructionManualSettingFragment);
    }

    private void switchToAlarmSettingsFragment() {
        if (mAlarmSettingsFragment == null) {
            mAlarmSettingsFragment = new AlarmSettingsFragment();
        }
        switchFragment(mAlarmSettingsFragment);
    }

    private void switchToTimeSettingFragment(boolean type) {
        if (mTimeSettingFragment == null) {
            mTimeSettingFragment = new TimeSettingFragment(type);
        }else {
            mTimeSettingFragment.setType(type);
        }
        switchFragment(mTimeSettingFragment);
    }

    private void switchToDateSettingFragment(boolean type) {
        if (mDateSettingFragment == null) {
            mDateSettingFragment = new DateSettingFragment(type);
        }else {
            mDateSettingFragment.setType(type);
        }
        switchFragment(mDateSettingFragment);
    }

    private void switchToTestCookwareSettingFragment() {
        if (mTestCookwareSettingFragment == null) {
            mTestCookwareSettingFragment = new TestCookwareSettingFragment();
            //   LogUtil.d("liang  mTestCookwareSettingFragment is null ");
        }
        switchFragment(mTestCookwareSettingFragment);
    }

    private void switchToInformationSettingFragment() {
        if (mInformationSettingFragment == null) {
            mInformationSettingFragment = new InformationSettingFragment();
            mInformationSettingFragment.setOnInformationSettingFragmentListener(this);
        }
        switchFragment(mInformationSettingFragment);
    }

    private void switchToDateTimeSettingFragment() {
        if (mDateTimeSettingFragment == null) {
            mDateTimeSettingFragment = new DateTimeSettingFragment();
            mDateTimeSettingFragment.setOnDateTimeSettingFragmentListener(this);
        }
        switchFragment(mDateTimeSettingFragment);
    }

    private void switchToConnectivitySettingFragment() {
        if (mConnectivitySettingFragment == null) {
            mConnectivitySettingFragment = new ConnectivitySettingFragment();
        }
        switchFragment(mConnectivitySettingFragment);
    }

    private void switchToSecuritySettingPinModifyFragment() {
        if (mSecuritySettingPinModifyFragment == null) {
            mSecuritySettingPinModifyFragment = new SecuritySettingPinModifyFragment();
            mSecuritySettingPinModifyFragment.setOnSecuritySettingPinModifyFragmentListener(this);

        }
        switchFragment(mSecuritySettingPinModifyFragment);
    }

    private void switchToSecuritySettingFragment() {
        if (mSecuritySettingFragment == null) {
            mSecuritySettingFragment = new SecuritySettingFragment();
            mSecuritySettingFragment.setOnSecuritySettingFragmentListener(this);
        }
        switchFragment(mSecuritySettingFragment);
    }

    private void switchToPowerLimitSettingFragment() {
        if (mPowerLimitSettingFragment == null) {
            mPowerLimitSettingFragment = new PowerLimitSettingFragment();
        }
        switchFragment(mPowerLimitSettingFragment);
    }

    private void switchToDefaultConfigSettingFragment() {
        if (mDefaultConfigSettingFragment == null) {
            mDefaultConfigSettingFragment = new DefaultConfigSettingFragment();
        }
        switchFragment(mDefaultConfigSettingFragment);
    }

    private void switchToHoodOptionsSettingFragment() {
        if (mHoodOptionsSettingFragment == null) {
            mHoodOptionsSettingFragment = new HoodOptionsSettingFragment();
        }
        switchFragment(mHoodOptionsSettingFragment);
    }

    private void switchToLanguageSettingFragment(boolean type) {

        if (mLanguageSettingFragment == null) {
            mLanguageSettingFragment = new LanguageSettingFragment(type);
        }else {
            mLanguageSettingFragment.setType(type);
        }
        switchFragment(mLanguageSettingFragment);
    }

    private void switchToSoundSettingFragment() {

        if (mSoundSettingFragment == null) {
            mSoundSettingFragment = new SoundSettingFragment();
        }
        switchFragment(mSoundSettingFragment);
    }

    private void switchToSettingMainFragment() {
        if (mSettingMainFragment == null) {
            mSettingMainFragment = new SettingMainFragment();
            mSettingMainFragment.setOnSettingMainFragmentListener(this);
        }else {
            mSettingMainFragment.updateUI();
        }
        switchFragment(mSettingMainFragment);

        updateTitleUI(R.mipmap.ic_setting, getString(R.string.settingmodule_setting_title_settings));
        mHaveBeenClickSettingButton=true;
    }

    private void switchToDisplaySettingFragment() {
        if (mDisplaySettingFragment == null) {
            mDisplaySettingFragment = new DisplaySettingFragment();
        }
        switchFragment(mDisplaySettingFragment);
    }

    private void switchFragment(Fragment fragment) {
        if (currentShowFragment == fragment) return;
        if (fragment != mConnectivitySettingFragment) {
            GlobalVars.getInstance().setWaitingToUpdate(false);
        }

        lastShowFragment = currentShowFragment;
        currentShowFragment = fragment;
        icInfo.setVisibility(View.VISIBLE);

        if (currentShowFragment == mHoodOptionsSettingFragment) {
            SettingPreferencesUtil.saveHoodSettingModeStatus(getActivity(), CataSettingConstant.HOOD_SETTING_MODE_OPEN);
            // LogUtil.d("liang HOOD_SETTING_MODE_OPEN is  open");
        } else {
            SettingPreferencesUtil.saveHoodSettingModeStatus(getActivity(), CataSettingConstant.HOOD_SETTING_MODE_CLOSE);
            //   LogUtil.d("liang HOOD_SETTING_MODE_OPEN is  close");
        }

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.ll_container, fragment, "currentFragment");
        transaction.addToBackStack(null);
        transaction.commit();

        if(currentShowFragment==mDateSettingFragment||currentShowFragment==mTimeSettingFragment||currentShowFragment==mKeyToIconsSettingFragment){
            icInfo.setVisibility(View.INVISIBLE);
        }else {
            icInfo.setVisibility(View.VISIBLE);
        }

        ibBack.setVisibility(View.VISIBLE);
    }


    @OnClick({R2.id.ib_back,R2.id.tv_title,R2.id.rl_full_screen})
    public void onViewClicked(View view) {
        if(view.getId()==R.id.ib_back||view.getId()==R.id.tv_title){
            doBack();  // 2019年2月21日21:26:53 屏蔽不用了。
        }else if(view.getId()==R.id.rl_full_screen){
            updateToastUI(false,"");
        }
    }

    public void doBack() {
        if (currentShowFragment == mDisplaySettingFragment || currentShowFragment == mSoundSettingFragment ||
                currentShowFragment == mLanguageSettingFragment || currentShowFragment == mHoodOptionsSettingFragment ||
                currentShowFragment == mDefaultConfigSettingFragment || currentShowFragment == mSecuritySettingFragment ||
                currentShowFragment == mConnectivitySettingFragment || currentShowFragment == mDateTimeSettingFragment ||
                currentShowFragment == mInformationSettingFragment || currentShowFragment == mTestCookwareSettingFragment ||
                currentShowFragment == mPowerLimitSettingFragment || currentShowFragment == mEnergySettingsFragment) {

            if (currentShowFragment == mTestCookwareSettingFragment) {
                EventBus.getDefault().post(new SendOrderToTestCookwareFragment(CataSettingConstant.OrderForTestCookwareFragment));
                LogUtil.d("liang set order to testcookware ~~~");
            }

            if (currentShowFragment == mLanguageSettingFragment) {
                if (GlobalVars.getInstance().getAppStartTag() == GlobalCons.APP_START_NONE) {// 不是第一次开机
                    switchToSettingMainFragment();
                }else if(GlobalVars.getInstance().getAppStartTag() == GlobalCons.APP_START_FIRST_TIME){   // 是第一次开机
                    if(mHaveBeenClickSettingButton){
                        switchToSettingMainFragment();  // 主界面
                    }

                }else if(GlobalVars.getInstance().getAppStartTag() == GlobalCons.APP_START){  // 关机后，再开机
                    if(mHaveBeenClickSettingButton){
                        switchToSettingMainFragment();  // 主界面
                    }
                }

                //
            } else {
                switchToSettingMainFragment();
            }
            //  switchToDateTimeSettingFragment();
        }else if (currentShowFragment == mSettingMainFragment) {
            ((OnSettingFragmentListener) mListener).onSettingFragmentFinish();
        } else if (currentShowFragment == mDateSettingFragment || currentShowFragment == mTimeSettingFragment || currentShowFragment == mAlarmSettingsFragment) {
            if (currentShowFragment == mDateSettingFragment || currentShowFragment == mTimeSettingFragment) {
                if (GlobalVars.getInstance().getAppStartTag() == GlobalCons.APP_START_NONE) {// 不是第一次开机
                    switchToDateTimeSettingFragment();
                }else if(GlobalVars.getInstance().getAppStartTag() == GlobalCons.APP_START_FIRST_TIME){   // 是第一次开机
                    if(mHaveBeenClickSettingButton){
                        switchToDateTimeSettingFragment();

                    }else {
                        if (currentShowFragment == mDateSettingFragment){
                            switchToLanguageSettingFragment(mHaveBeenClickSettingButton);
                            ibBack.setVisibility(View.INVISIBLE);
                            EventBus.getDefault().post(new SendOrderTo(CataSettingConstant.SetLanguage));
                        }
                        if(currentShowFragment == mTimeSettingFragment){
                            switchToDateSettingFragment(mHaveBeenClickSettingButton);
                            EventBus.getDefault().post(new SendOrderTo(CataSettingConstant.SetDate));
                        }
                    }
                }else if(GlobalVars.getInstance().getAppStartTag() == GlobalCons.APP_START){  // 关机后，再开机
                    if(mHaveBeenClickSettingButton){
                        switchToDateTimeSettingFragment();
                        LogUtil.d("liang show datesettingfragment 1");
                    }else {
                        if (currentShowFragment == mDateSettingFragment){
                        }
                        if(currentShowFragment == mTimeSettingFragment){
                            switchToDateSettingFragment(mHaveBeenClickSettingButton);
                            LogUtil.d("liang show datesettingfragment 2");
                        }
                        EventBus.getDefault().post(new SendOrderTo(CataSettingConstant.SetDate));
                    }
                }
            }else {
                switchToDateTimeSettingFragment();
            }
        }else if (currentShowFragment == mInstructionManualSettingFragment || currentShowFragment == mKeyToIconsSettingFragment || currentShowFragment == mDemoModeSettingFragment || currentShowFragment == mUpdateSettingFragment) {
            switchToInformationSettingFragment();
        } else if (currentShowFragment == mHibernationFormatSettingFragment) {
            switchToEnergySettingsFragment();
        } else if (currentShowFragment == mSecuritySettingPinModifyFragment ||
                currentShowFragment == mSecuritySettingPatternModifyFragment) {
            switchToSecuritySettingFragment();
        }

    }


    @Override
    public void onSettingMainFragmentSelect(int what, int logoResID, String title) {
        switch (what) {
            case SettingMainFragment.SETTING_MAIN_SELECT_DISPLAY_SETTING:
                switchToDisplaySettingFragment();
                break;
            case SettingMainFragment.SETTING_MAIN_SELECT_LANGUAGE_SETTING:
                switchToLanguageSettingFragment(mHaveBeenClickSettingButton);
                break;
            case SettingMainFragment.SETTING_MAIN_SELECT_SOUND_SETTING:
                switchToSoundSettingFragment();
                break;
            case SettingMainFragment.SETTING_MAIN_SELECT_SECURITY_SETTING:
                switchToSecuritySettingFragment();
                break;
            case SettingMainFragment.SETTING_MAIN_SELECT_CONNECTIVITY_SETTING:
                switchToConnectivitySettingFragment();
                break;
            case SettingMainFragment.SETTING_MAIN_SELECT_HOOD_OPTIONS_SETTING:
                switchToHoodOptionsSettingFragment();
                break;
            case SettingMainFragment.SETTING_MAIN_SELECT_POWER_LIMIT_SETTING:
                switchToPowerLimitSettingFragment();
                break;
            case SettingMainFragment.SETTING_MAIN_SELECT_DEFAULT_SETTINGS_SETTING:
                switchToDefaultConfigSettingFragment();
                break;
            case SettingMainFragment.SETTING_MAIN_SELECT_DATE_AND_TIME_SETTING:
                switchToDateTimeSettingFragment();
                break;
            case SettingMainFragment.SETTING_MAIN_SELECT_INFORMATION_SETTING:
                switchToInformationSettingFragment();
                break;
            case SettingMainFragment.SETTING_MAIN_SELECT_ENERGY_SETTINGS_SETTING:
                switchToEnergySettingsFragment();
                break;
            case SettingMainFragment.SETTING_MAIN_SELECT_TEST_COOKWARE_SETTING:
                switchToTestCookwareSettingFragment();
                break;
        }
        updateTitleUI(logoResID, title);
    }

    private void updateTitleUI(int logoResID, String title) {
        if (logoResID != -1) {
            Drawable drawable = getResources().getDrawable(logoResID);
            drawable.setBounds(0, 0, 52, 52);
            tvTitle.setCompoundDrawables(drawable, null, null, null);
        }
        if (title != null) tvTitle.setText(title);

    }

    @Override
    public void onSecuritySettingFragmentFinish() {

    }

    @Override
    public void onSecuritySettingFragmentRequestSetPassword(int what) {
        if (what == SecuritySettingFragment.SECURITY_SET_PATTERN_PASSWORD) {
            switchSecuritySettingPatternModifyFragment();

        } else if (what == SecuritySettingFragment.SECURITY_SET_PIN_PASSWORD) {
            switchToSecuritySettingPinModifyFragment();

        }
    }


    @Override
    public void onDateTimeSettingFragmentRequestSetDate() {
        switchToDateSettingFragment(mHaveBeenClickSettingButton);
        //updateTitleUI();
    }

    @Override
    public void onDateTimeSettingFragmentRequestSetTime() {
        switchToTimeSettingFragment(mHaveBeenClickSettingButton);
    }

    @Override
    public void onDateTimeSettingFragmentRequestSetAlarm() {
        switchToAlarmSettingsFragment();
    }

    @Override
    public void onInformationSettingFragmentSelect(int what) {
        switch (what) {
            case InformationSettingFragment.INFORMATION_SETTING_FRAGMENT_SELECT_INSTRUCTION_MANUAL:
                switchToInstructionManualSettingFragment();
                break;
            case InformationSettingFragment.INFORMATION_SETTING_FRAGMENT_SELECT_KEY_TO_ICONS:
                switchToKeyToIconsSettingFragment();
                break;
            case InformationSettingFragment.INFORMATION_SETTING_FRAGMENT_SELECT_DEMO_MODE:
                switchTomDemoModeSettingFragment();
                break;
            case InformationSettingFragment.INFORMATION_SETTING_FRAGMENT_SELECT_UPDATES:
                switchToUpdateSettingFragment();
                break;
        }
    }

    @Override
    public void onEnergySettingsFragmentRequestFormatSetting() {
        switchToHibernationFormatSettingFragment();
    }

    @Override
    public void onSecuritySettingPinModifyFragmentFinish() {
        switchToSecuritySettingFragment();
    }

    @Override
    public void onSecuritySettingPatternModifyFragmentFinish() {
        switchToSecuritySettingFragment();
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

    @OnClick(R2.id.ic_info)
    public void onClick() {
        if (ProductManager.PRODUCT_TYPE == ProductManager.Haier_china) {
            if (currentShowFragment == mDisplaySettingFragment) { // E20
                updateToastUI(true, "选择屏幕的亮度及自定义墙纸。");

            } else if (currentShowFragment == mSoundSettingFragment) { //E30
                updateToastUI(true, "启动/关闭声音和调整其强度及启用/禁用点击响声。");
            } else if (currentShowFragment == mConnectivitySettingFragment) {  // E40
                updateToastUI(true, "通过蓝牙连接设备。");
            }/*else if (currentShowFragment==mPowerLimitSettingFragment){
                    updateToastUI(true  , "Select the power limitation.");
                }*/ else if (currentShowFragment == mDateTimeSettingFragment) { //E60
                updateToastUI(true, "设置当前日期和时间及其显示格式。");
            } else if (currentShowFragment == mDateSettingFragment || currentShowFragment == mTimeSettingFragment) {
                updateToastUI(false, "  ");
            } else if (currentShowFragment == mAlarmSettingsFragment) {  //E603
                updateToastUI(true, "闹铃设置。");
            } else if (currentShowFragment == mLanguageSettingFragment) {  //E70
                updateToastUI(true, "语言设置。");
            } else if (currentShowFragment == mSecuritySettingFragment) {  //E80
                updateToastUI(true, "设置锁模式。");
            } else if (currentShowFragment == mSecuritySettingPinModifyFragment) { //E806
                updateToastUI(true, "选择 PIN 码，长度是4个数字。");
            } else if (currentShowFragment == mSecuritySettingPatternModifyFragment) {// E801
                updateToastUI(true, "选择图案，最少4个连接点。");
            }/*else if ( currentShowFragment == mHoodOptionsSettingFragment){
                    updateToastUI(true  , "Set the control of the hood.");
                }*/ else if (currentShowFragment == mDefaultConfigSettingFragment) { // E100
                updateToastUI(true, "恢复出厂设置。");
            } else if (currentShowFragment == mInformationSettingFragment) {   //E110
                updateToastUI(true, "通过访问如下网址可获得本电磁炉的更多信息。 \n\"www.cata.es\"..");
            } else if (currentShowFragment == mInstructionManualSettingFragment) {
                updateToastUI(true, "通过访问如下网址可获得本电磁炉的更多信息。 \n \"www.cata.es\"..");
            } else if (currentShowFragment == mKeyToIconsSettingFragment) {
                updateToastUI(false, "  ");
            } else if (currentShowFragment == mDemoModeSettingFragment) { // E1102
                updateToastUI(true, "可选择教程和演示视频。");
            } else if (currentShowFragment == mUpdateSettingFragment) {//E1101
                updateToastUI(true, "请等待升级完成。");
            } else if (currentShowFragment == mEnergySettingsFragment) { //E120
                updateToastUI(true, "设置休眠模式和待机模式的时间以提高本电磁炉的工作效率。");
            } else if (currentShowFragment == mHibernationFormatSettingFragment) {
                updateToastUI(true, "设置休眠模式显示效果，其在不点击屏幕一段时间后显示。");
            }/*else if (currentShowFragment == mTestCookwareSettingFragment){
                    updateToastUI(true  , "By choose the correct cookware allows an optimus \nperformance of the hob and  avoids possible\nmalfunctions.");
                }*/ else if (currentShowFragment == mSettingMainFragment) {  //E10
                updateToastUI(true, "自定义本电磁炉。");
                // LogUtil.d("show the toast~~~");
            }
        } else {
            if (currentShowFragment == mDisplaySettingFragment) {
                ToastDialog.showDialog(
                        getActivity(),
                        R.string.settingmodule_fragment_display_toast_content,
                        ToastDialog.WIDTH_LONG,
                        icInfo,
                        ToastDialog.ANCHOR_DIRECTION_BELOW_END,
                        CataSettingConstant.TOAST_SHOW_DURATION);
            } else if (currentShowFragment == mSoundSettingFragment) {
                ToastDialog.showDialog(
                        getActivity(),
                        R.string.settingmodule_fragment_sound_toast_content,
                        1050,//长度加多50，为了让Activate / Deactivate the general sound and adjust the intensity.整句话在同一行，bug no.148,鸿
                        Gravity.TOP | Gravity.END,
                        200,
                        160,
                        CataSettingConstant.TOAST_SHOW_DURATION);
            } else if (currentShowFragment == mConnectivitySettingFragment) {  //
                ToastDialog.showDialog(
                        getActivity(),
                        R.string.settingmodule_fragment_connectivity_toast_content,
                        ToastDialog.WIDTH_LONG,
                        icInfo,
                        ToastDialog.ANCHOR_DIRECTION_BELOW_END,
                        CataSettingConstant.TOAST_SHOW_DURATION);
            } else if (currentShowFragment == mPowerLimitSettingFragment) {
                ToastDialog.showDialog(
                        getActivity(),
                        R.string.settingmodule_fragment_power_limit_toast_content,
                        ToastDialog.WIDTH_LONG,
                        icInfo,
                        ToastDialog.ANCHOR_DIRECTION_BELOW_END,
                        CataSettingConstant.TOAST_SHOW_DURATION);
            } else if (currentShowFragment == mDateTimeSettingFragment) {
                int width;
                switch (GlobalVars.getInstance().getCurrentLocale().toString().toLowerCase()) {
                    case "fr":
                    case "it":
                    case "nl":
                        width = ToastDialog.WIDTH_SHORT;
                        break;
                    case "es":
                    case "ko":
                    case "ru":
                        width = ToastDialog.WIDTH_MEDIUM;
                        break;
                    case "de":
                    case "el":
                        width = ToastDialog.WIDTH_LONG;
                        break;
                    default:
                        width = ToastDialog.WIDTH_EXTRA_LONG;
                        break;
                }
                ToastDialog.showDialog(
                        getActivity(),
                        R.string.settingmodule_fragment_date_time_toast_content,
                        width,
                        icInfo,
                        ToastDialog.ANCHOR_DIRECTION_BELOW_END,
                        CataSettingConstant.TOAST_SHOW_DURATION);
            } else if (currentShowFragment == mDateSettingFragment || currentShowFragment == mTimeSettingFragment) {
            } else if (currentShowFragment == mAlarmSettingsFragment) {
                ToastDialog.showDialog(
                        getActivity(),
                        R.string.settingmodule_fragment_alarm_toast_content,
                        ToastDialog.WIDTH_LONG,
                        icInfo,
                        ToastDialog.ANCHOR_DIRECTION_BELOW_END,
                        CataSettingConstant.TOAST_SHOW_DURATION);
            } else if (currentShowFragment == mLanguageSettingFragment) {
                ToastDialog.showDialog(
                        getActivity(),
                        R.string.settingmodule_fragment_language_toast_content,
                        ToastDialog.WIDTH_LONG,
                        icInfo,
                        ToastDialog.ANCHOR_DIRECTION_BELOW_END,
                        CataSettingConstant.TOAST_SHOW_DURATION);
            } else if (currentShowFragment == mSecuritySettingFragment) {    // Security
                ToastDialog.showDialog(
                        getActivity(),
                        R.string.settingmodule_fragment_security_toast_content,
                        ToastDialog.WIDTH_LONG,
                        icInfo,
                        ToastDialog.ANCHOR_DIRECTION_BELOW_END,
                        CataSettingConstant.TOAST_SHOW_DURATION);
            } else if (currentShowFragment == mSecuritySettingPinModifyFragment) {  // PIN
                if (bannerInfo.equals(CataSettingConstant.SET_LOCK_SCREEN_PIN)) {
                    ToastDialog.showDialog(
                            getActivity(),
                            R.string.settingmodule_fragment_security_pin_set_toast_content,
                            ToastDialog.WIDTH_LONG,
                            icInfo,
                            ToastDialog.ANCHOR_DIRECTION_BELOW_END,
                            CataSettingConstant.TOAST_SHOW_DURATION);
                } else if (bannerInfo.equals(CataSettingConstant.CONFIRM_LOCK_SCREEN_PIN)) {
                    ToastDialog.showDialog(
                            getActivity(),
                            R.string.settingmodule_fragment_security_pin_confirm_toast_content,
                            ToastDialog.WIDTH_LONG,
                            icInfo,
                            ToastDialog.ANCHOR_DIRECTION_BELOW_END,
                            CataSettingConstant.TOAST_SHOW_DURATION);
                }
            } else if (currentShowFragment == mSecuritySettingPatternModifyFragment) {  // Pattern
                if (bannerInfo.equals(CataSettingConstant.SET_LOCK_SCREEN_PATTERN)) {
                    int width;
                    switch (GlobalVars.getInstance().getCurrentLocale().toString().toLowerCase()) {
                        case "el":
                        case "hu":
                        case "no":
                            width = ToastDialog.WIDTH_SHORT;
                            break;
                        case "de":
                            width = ToastDialog.WIDTH_MEDIUM;
                            break;
                        case "ru":
                            width = 760;
                            break;
                        default:
                            width = ToastDialog.WIDTH_EXTRA_LONG;
                            break;
                    }
                    ToastDialog.showDialog(
                            getActivity(),
                            R.string.settingmodule_fragment_security_pattern_set_toast_content,
                            width,
                            icInfo,
                            ToastDialog.ANCHOR_DIRECTION_BELOW_END,
                            CataSettingConstant.TOAST_SHOW_DURATION);
                } else if (bannerInfo.equals(CataSettingConstant.CONFIRM_LOCK_SCREEN_PATTERN)) {
                    ToastDialog.showDialog(
                            getActivity(),
                            R.string.settingmodule_fragment_security_pattern_confirm_toast_content,
                            ToastDialog.WIDTH_LONG,
                            icInfo,
                            ToastDialog.ANCHOR_DIRECTION_BELOW_END,
                            CataSettingConstant.TOAST_SHOW_DURATION);
                }
            } else if (currentShowFragment == mHoodOptionsSettingFragment) {
                ToastDialog.showDialog(
                        getActivity(),
                        R.string.settingmodule_fragment_hood_options_toast_content,
                        ToastDialog.WIDTH_LONG,
                        icInfo,
                        ToastDialog.ANCHOR_DIRECTION_BELOW_END,
                        CataSettingConstant.TOAST_SHOW_DURATION);
            } else if (currentShowFragment == mDefaultConfigSettingFragment) {   // E100
                if (bannerInfo.equals(CataSettingConstant.INPUT_THE_PATTERN)) {
                    ToastDialog.showDialog(
                            getActivity(),
                            R.string.settingmodule_fragment_default_config_pattern_toast_content,
                            ToastDialog.WIDTH_LONG,
                            icInfo,
                            ToastDialog.ANCHOR_DIRECTION_BELOW_END,
                            CataSettingConstant.TOAST_SHOW_DURATION);
                } else if (bannerInfo.equals(CataSettingConstant.INPUT_THE_PIN_CODE)) {
                    ToastDialog.showDialog(
                            getActivity(),
                            R.string.settingmodule_fragment_default_config_pin_toast_content,
                            ToastDialog.WIDTH_LONG,
                            icInfo,
                            ToastDialog.ANCHOR_DIRECTION_BELOW_END,
                            CataSettingConstant.TOAST_SHOW_DURATION);
                } else {
                    ToastDialog.showDialog(
                            getActivity(),
                            R.string.settingmodule_fragment_default_config_else_toast_content,
                            ToastDialog.WIDTH_LONG,
                            icInfo,
                            ToastDialog.ANCHOR_DIRECTION_BELOW_END,
                            CataSettingConstant.TOAST_SHOW_DURATION);
                }
            } else if (currentShowFragment == mInformationSettingFragment) {
                ToastDialog.showDialog(
                        getActivity(),
                        R.string.settingmodule_fragment_information_toast_content,
                        760,
                        icInfo,
                        ToastDialog.ANCHOR_DIRECTION_BELOW_END,
                        CataSettingConstant.TOAST_SHOW_DURATION);
            } else if (currentShowFragment == mInstructionManualSettingFragment) {
                ToastDialog.showDialog(
                        getActivity(),
                        R.string.settingmodule_fragment_instruction_manual_toast_content,
                        ToastDialog.WIDTH_LONG,
                        icInfo,
                        ToastDialog.ANCHOR_DIRECTION_BELOW_END,
                        CataSettingConstant.TOAST_SHOW_DURATION);
            } else if (currentShowFragment == mKeyToIconsSettingFragment) {
            } else if (currentShowFragment == mDemoModeSettingFragment) {
                ToastDialog.showDialog(
                        getActivity(),
                        R.string.settingmodule_fragment_demo_mode_toast_content,
                        ToastDialog.WIDTH_LONG,
                        icInfo,
                        ToastDialog.ANCHOR_DIRECTION_BELOW_END,
                        CataSettingConstant.TOAST_SHOW_DURATION);
            } else if (currentShowFragment == mUpdateSettingFragment) {
                ToastDialog.showDialog(
                        getActivity(),
                        R.string.settingmodule_fragment_updates_toast_content,
                        ToastDialog.WIDTH_LONG,
                        icInfo,
                        ToastDialog.ANCHOR_DIRECTION_BELOW_END,
                        CataSettingConstant.TOAST_SHOW_DURATION);
            } else if (currentShowFragment == mEnergySettingsFragment) {  // energy
                ToastDialog.showDialog(
                        getActivity(),
                        R.string.settingmodule_fragment_energy_toast_content,
                        ToastDialog.WIDTH_LONG,
                        icInfo,
                        ToastDialog.ANCHOR_DIRECTION_BELOW_END,
                        CataSettingConstant.TOAST_SHOW_DURATION);
            } else if (currentShowFragment == mTestCookwareSettingFragment) {  // test cooker
                if (bannerInfo.equals(CataSettingConstant.SELECT_THE_COOKING_ZONE)) {
                    ToastDialog.showDialog(
                            getActivity(),
                            R.string.settingmodule_fragment_test_cookware_select_toast_content,
                            ToastDialog.WIDTH_LONG,
                            icInfo,
                            ToastDialog.ANCHOR_DIRECTION_BELOW_END,
                            CataSettingConstant.TOAST_SHOW_DURATION);
                } else if (bannerInfo.equals(CataSettingConstant.COOKWARE_IS_NOT_COMPATIBLE)) {
                    ToastDialog.showDialog(
                            getActivity(),
                            R.string.settingmodule_fragment_test_cookware_not_compatible_toast_content,
                            ToastDialog.WIDTH_LONG,
                            icInfo,
                            ToastDialog.ANCHOR_DIRECTION_BELOW_END,
                            CataSettingConstant.TOAST_SHOW_DURATION);
                } else if (bannerInfo.equals(CataSettingConstant.COOKWARE_IS_COMPATIBLE)) {
                    ToastDialog.showDialog(
                            getActivity(),
                            R.string.settingmodule_fragment_test_cookware_is_compatible_toast_content,
                            ToastDialog.WIDTH_LONG,
                            icInfo,
                            ToastDialog.ANCHOR_DIRECTION_BELOW_END,
                            CataSettingConstant.TOAST_SHOW_DURATION);
                } else if (bannerInfo.equals(CataSettingConstant.PLACE_THE_COOKER)) {
                    ToastDialog.showDialog(
                            getActivity(),
                            R.string.settingmodule_fragment_test_cookware_place_toast_content,
                            ToastDialog.WIDTH_LONG,
                            icInfo,
                            ToastDialog.ANCHOR_DIRECTION_BELOW_END,
                            CataSettingConstant.TOAST_SHOW_DURATION);
                } else {
                    ToastDialog.showDialog(
                            getActivity(),
                            R.string.settingmodule_fragment_test_cookware_else_toast_content,
                            ToastDialog.WIDTH_LONG,
                            icInfo,
                            ToastDialog.ANCHOR_DIRECTION_BELOW_END,
                            CataSettingConstant.TOAST_SHOW_DURATION);
                }

            } else if (currentShowFragment == mSettingMainFragment) {
                ToastDialog.showDialog(
                        getActivity(),
                        R.string.settingmodule_fragment_main_toast_content,
                        ToastDialog.WIDTH_LONG,
                        icInfo,
                        ToastDialog.ANCHOR_DIRECTION_BELOW_END,
                        CataSettingConstant.TOAST_SHOW_DURATION);
            } else if (currentShowFragment == mHibernationFormatSettingFragment) {
                ToastDialog.showDialog(
                        getActivity(),
                        R.string.settingmodule_fragment_hibernation_format_toast_content,
                        ToastDialog.WIDTH_LONG,
                        icInfo,
                        ToastDialog.ANCHOR_DIRECTION_BELOW_END,
                        CataSettingConstant.TOAST_SHOW_DURATION);
            }
        }

    }

    private void updateToastUI(boolean isShow, String msg) {
        if (isShow) {
            if (handler.hasMessages(HANDLER_TOAST_FINISH))
                handler.removeMessages(HANDLER_TOAST_FINISH);
            tvUpToast.setText(msg);
            if (tvUpToast.getVisibility() == View.INVISIBLE) tvUpToast.setVisibility(View.VISIBLE);
            handler.sendEmptyMessageDelayed(HANDLER_TOAST_FINISH, CataSettingConstant.TOAST_SHOW_DURATION);

        } else {
            if (handler.hasMessages(HANDLER_TOAST_FINISH))
                handler.removeMessages(HANDLER_TOAST_FINISH);
            tvUpToast.setText(msg);
            if (tvUpToast.getVisibility() == View.VISIBLE) tvUpToast.setVisibility(View.INVISIBLE);

        }
    }

    public interface OnSettingFragmentListener extends OnFragmentListener {
        void onSettingFragmentFinish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SettingDoBack event) {
        if (event.getOrder() == CataSettingConstant.do_back) {
            doBack();
//            LogUtil.d("liang show do back of timesetting 2");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent3(WhichBannerWillBeShow event) {
        int id = 0;
        bannerInfo = event.getNameBanner();
        LogUtil.d("liang get the banner information~~~~~~~~~~~  " + bannerInfo);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent2(SendOrderForFirstSwitchOn event) {
        int id = 0;
        id = event.getOrder();
        switch (id) {
            case CataSettingConstant.SetLanguage:
                //   switchToLanguageSettingFragment();
                LogUtil.d("ready to set language ~~~");
                break;
            case CataSettingConstant.SetDate:
                switchToDateSettingFragment(mHaveBeenClickSettingButton);
                break;
            case CataSettingConstant.SetTime:
                switchToTimeSettingFragment(mHaveBeenClickSettingButton);
                break;
            case CataSettingConstant.GoBackToSetting:
                switchToSettingMainFragment();  // 主界面
                break;
            case CataSettingConstant.ShowMainWorkSpace:
                if(mTimeSettingFragment!=null){
                    mTimeSettingFragment.setTimeWhenDisappear();

                }
                break;
            default:
                LogUtil.d("not ready to set language ~~~");

                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TestCookwareSettingFragmentContentChanged event) {
        switch (event.getUiName()) {
            case "E1303":
                icInfo.setVisibility(View.INVISIBLE);
                break;
            default:
                icInfo.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setMarginEnd(int marginEnd){
      /*  RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)rlSetting.getLayoutParams();
        layoutParams.setMarginEnd(marginEnd);
        rlSetting.setLayoutParams(layoutParams);*/

        LinearLayout.LayoutParams layoutParams1= (LinearLayout.LayoutParams) rlFullScreen.getLayoutParams();
        layoutParams1.setMarginEnd(marginEnd);
        rlFullScreen.setLayoutParams(layoutParams1);

    }
}
