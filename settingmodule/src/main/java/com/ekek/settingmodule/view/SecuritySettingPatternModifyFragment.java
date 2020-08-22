package com.ekek.settingmodule.view;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.settingmodule.R;
import com.ekek.settingmodule.R2;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.database.SecurityDBUtil;
import com.ekek.settingmodule.entity.SecurityTable;
import com.ekek.settingmodule.model.SettingDoBack;
import com.ekek.settingmodule.model.WhichBannerWillBeShow;
import com.ekek.viewmodule.common.FocusedTextView;
import com.ekek.viewmodule.listener.PatternLockViewListener;
import com.ekek.viewmodule.passwordview.PatternLockView;
import com.ekek.viewmodule.product.ProductManager;
import com.ekek.viewmodule.utils.PatternLockUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SecuritySettingPatternModifyFragment extends BaseFragment implements PatternLockViewListener {
    private static final int SET_PIN_PROGRESS_FIRST_ENTER_PATTERN = 0;
    private static final int SET_PIN_PROGRESS_FIRST_CONFIRM_PATTERN = 1;
    private static final int SET_PIN_PROGRESS_MODIFY_ENTER_OLD_PATTERN = 2;
    private static final int SET_PIN_PROGRESS_MODIFY_ENTER_NEW_PATTERN = 3;
    private static final int SET_PIN_PROGRESS_MODIFY_CONFIRM_NEW_PATTERN = 4;
    private static final int PATTERN_MIN_DOTS = 4;
    @BindView(R2.id.tv_title)
    FocusedTextView tvTitle;
    @BindView(R2.id.pattern_lock_view)
    PatternLockView patternLockView;
    private int progress = SET_PIN_PROGRESS_FIRST_ENTER_PATTERN;
    private SecurityTable security;
    private String password ="";
    private OnSecuritySettingPatternModifyFragmentListener mListener;
    private Typeface typeface;
    @Override
    public int initLyaout() {
        return R.layout.settingmodule_fragment_security_setting_pattern_modify;
    }

    @Override
    public void initListener() {

    }

    @Override
    public void onStart() {
        super.onStart();
        init();

        SetUI();
    }

    private void SetUI(){
        if(ProductManager.PRODUCT_TYPE== ProductManager.Haier){
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_test_cookware_haier);
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
    }


    private void init() {
        password = "";
        security = SecurityDBUtil.getSecurity(getActivity(), CataSettingConstant.SECURITY_MODE_PATTERN);
        if (security.getPassword().equals("")) {
            progress = SET_PIN_PROGRESS_FIRST_ENTER_PATTERN;
            updateTitleUI(getString(R.string.settingmodule_fragment_setting_security_modify_pattern_title_set_lock_screen));

        }else {
            progress = SET_PIN_PROGRESS_MODIFY_ENTER_OLD_PATTERN;
            updateTitleUI(getString(R.string.settingmodule_fragment_setting_security_modify_pattern_title_confirm_lock_screen));

        }

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

        EventBus.getDefault().post(new WhichBannerWillBeShow(CataSettingConstant.SET_LOCK_SCREEN_PATTERN));
    }

    private void updateTitleUI(String msg) {
        tvTitle.setText(msg);
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        SetFont();
    }

    public void setOnSecuritySettingPatternModifyFragmentListener(OnSecuritySettingPatternModifyFragmentListener listener) {
        mListener = listener;
    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onProgress(List<PatternLockView.Dot> progressPattern) {

    }

    @Override
    public void onComplete(List<PatternLockView.Dot> pattern) {
        LogUtil.d("Enter:: oncomplete pattern progress---->" + progress);
        switch (progress) {
            case SET_PIN_PROGRESS_FIRST_ENTER_PATTERN:
                LogUtil.d("-----------------1----------------------");
                password = PatternLockUtils.patternToString(patternLockView,pattern);
                if (password.length() < PATTERN_MIN_DOTS) {
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                } else {
                    progress = SET_PIN_PROGRESS_FIRST_CONFIRM_PATTERN;
                    updateTitleUI(getString(R.string.settingmodule_fragment_setting_security_modify_pattern_title_confirm_lock_screen));
                    patternLockView.clearPattern();
                    EventBus.getDefault().post(new WhichBannerWillBeShow(CataSettingConstant.CONFIRM_LOCK_SCREEN_PATTERN));
                }
                break;
            case SET_PIN_PROGRESS_FIRST_CONFIRM_PATTERN:
                LogUtil.d("-----------------2----------------------");
                if (PatternLockUtils.patternToString(patternLockView,pattern).equals(password)){
                    // 清空上次设置的 pin 码
                    SecurityTable pinSecurity = SecurityDBUtil.getSecurity(getActivity(), CataSettingConstant.SECURITY_MODE_PIN);
                    if (!pinSecurity.getPassword().equals("")) {
                        SecurityDBUtil.setDefaultSecurity(getActivity(), CataSettingConstant.SECURITY_MODE_PIN,"");
                    }
                    // 设置为新的图像码
                    SecurityDBUtil.setDefaultSecurity(getActivity(),CataSettingConstant.SECURITY_MODE_PATTERN,password);
                    //设置完成
                    if (mListener != null) mListener.onSecuritySettingPatternModifyFragmentFinish();

                }else {
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                }
                break;
            case SET_PIN_PROGRESS_MODIFY_ENTER_OLD_PATTERN:

                if (PatternLockUtils.patternToString(patternLockView,pattern).equals(security.getPassword())){
                    progress = SET_PIN_PROGRESS_MODIFY_ENTER_NEW_PATTERN;
                    password = "";
                    updateTitleUI(getString(R.string.settingmodule_fragment_setting_security_modify_pattern_title_choose_your_pattern));
                    patternLockView.clearPattern();

                }else {
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                }

                break;
            case SET_PIN_PROGRESS_MODIFY_ENTER_NEW_PATTERN:
                password = PatternLockUtils.patternToString(patternLockView,pattern);
                if (password.length() < PATTERN_MIN_DOTS) {
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                } else {
                    progress = SET_PIN_PROGRESS_MODIFY_CONFIRM_NEW_PATTERN;
                    updateTitleUI(getString(R.string.settingmodule_fragment_setting_security_modify_pattern_title_confirm_lock_screen));
                    patternLockView.clearPattern();
                    EventBus.getDefault().post(new WhichBannerWillBeShow(CataSettingConstant.CONFIRM_LOCK_SCREEN_PATTERN));
                }
                break;
            case SET_PIN_PROGRESS_MODIFY_CONFIRM_NEW_PATTERN:
                if (PatternLockUtils.patternToString(patternLockView,pattern).equals(password)) {
                    // 清空上次设置的 pin 码
                    SecurityTable pinSecurity = SecurityDBUtil.getSecurity(getActivity(), CataSettingConstant.SECURITY_MODE_PIN);
                    if (!pinSecurity.getPassword().equals("")) {
                        SecurityDBUtil.setDefaultSecurity(getActivity(), CataSettingConstant.SECURITY_MODE_PIN,"");
                    }
                    // 设置为新的图像码
                    SecurityDBUtil.setDefaultSecurity(getActivity(),CataSettingConstant.SECURITY_MODE_PATTERN,password);

                    //设置完成
                    if (mListener != null) mListener.onSecuritySettingPatternModifyFragmentFinish();

                }else {
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                }
                break;
        }



    }

    @Override
    public void onCleared() {

    }


    public interface OnSecuritySettingPatternModifyFragmentListener {
        void onSecuritySettingPatternModifyFragmentFinish();

    }
}
