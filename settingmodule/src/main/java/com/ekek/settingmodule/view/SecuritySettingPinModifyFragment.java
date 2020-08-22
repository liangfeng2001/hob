package com.ekek.settingmodule.view;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.settingmodule.R;
import com.ekek.settingmodule.R2;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.database.SecurityDBUtil;
import com.ekek.settingmodule.entity.SecurityTable;
import com.ekek.settingmodule.model.SettingDoBack;
import com.ekek.settingmodule.model.WhichBannerWillBeShow;
import com.ekek.viewmodule.common.FocusedTextView;
import com.ekek.viewmodule.listener.OnPasswordListener;
import com.ekek.viewmodule.passwordview.PinPasswordView;
import com.ekek.viewmodule.product.ProductManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class SecuritySettingPinModifyFragment extends BaseFragment implements OnPasswordListener {
    private static final int SET_PIN_PROGRESS_FIRST_ENTER_PIN = 0;
    private static final int SET_PIN_PROGRESS_FIRST_CONFIRM_PIN = 1;
    private static final int SET_PIN_PROGRESS_MODIFY_ENTER_OLD_PIN = 2;
    private static final int SET_PIN_PROGRESS_MODIFY_ENTER_NEW_PIN = 3;
    private static final int SET_PIN_PROGRESS_MODIFY_CONFIRM_NEW_PIN = 4;

    @BindView(R2.id.tv_title)
    FocusedTextView tvTitle;
    @BindView(R2.id.ppv)
    PinPasswordView ppv;
    private int progress = SET_PIN_PROGRESS_FIRST_ENTER_PIN;
    private SecurityTable security;
    private String password ="";
    private OnSecuritySettingPinModifyFragmentListener mListener;
    private Typeface typeface;

    @Override
    public int initLyaout() {
        return R.layout.settingmodule_fragment_security_setting_pin_modify;
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

    @Override
    public void onStart() {
        super.onStart();
        init();

    }

    private void init() {
        password = "";
        security = SecurityDBUtil.getSecurity(getActivity(), CataSettingConstant.SECURITY_MODE_PIN);
        if (security.getPassword().equals("")) {
            progress = SET_PIN_PROGRESS_FIRST_ENTER_PIN;
            updateTitleUI(getString(R.string.settingmodule_fragment_setting_security_modify_pin_title_set_lock_screen));

        }else {
            progress = SET_PIN_PROGRESS_MODIFY_ENTER_OLD_PIN;
            updateTitleUI(getString(R.string.settingmodule_fragment_setting_security_modify_pin_title_confirm_lock_screen));

        }
        ppv.setMode(PinPasswordView.MODE_SET_PASSWORD);
        ppv.setOnPasswordListener(this);

        EventBus.getDefault().post(new WhichBannerWillBeShow(CataSettingConstant.SET_LOCK_SCREEN_PIN));


    }

    private void updateTitleUI(String msg) {
        tvTitle.setText(msg);
    }

    @Override
    public boolean onCheckPwd(String pwd) {
        //boolean result =
        switch (progress) {
            case SET_PIN_PROGRESS_FIRST_ENTER_PIN:
                password = pwd;
                progress = SET_PIN_PROGRESS_FIRST_CONFIRM_PIN;
                updateTitleUI(getString(R.string.settingmodule_fragment_setting_security_modify_pin_title_confirm_lock_screen));
                EventBus.getDefault().post(new WhichBannerWillBeShow(CataSettingConstant.CONFIRM_LOCK_SCREEN_PIN));
                return true;
            case SET_PIN_PROGRESS_FIRST_CONFIRM_PIN:
                if (pwd.equals(password)){
                    // 清空上次的图像码
                    SecurityTable patternSecurity = SecurityDBUtil.getSecurity(getActivity(), CataSettingConstant.SECURITY_MODE_PATTERN);
                    if (!patternSecurity.getPassword().equals("")) {
                        SecurityDBUtil.setDefaultSecurity(getActivity(), CataSettingConstant.SECURITY_MODE_PATTERN,"");
                    }
                    // 设置为新的 pin 码
                    SecurityDBUtil.setDefaultSecurity(getActivity(),CataSettingConstant.SECURITY_MODE_PIN,password);
                    //设置完成
                    if (mListener != null) mListener.onSecuritySettingPinModifyFragmentFinish();
                    return true;
                }else return false;
            case SET_PIN_PROGRESS_MODIFY_ENTER_OLD_PIN:

                if (pwd.equals(security.getPassword())){
                    progress = SET_PIN_PROGRESS_MODIFY_ENTER_NEW_PIN;
                    password = "";
                    updateTitleUI(getString(R.string.settingmodule_fragment_setting_security_modify_pin_title_choose_your_pin));
                    return true;

                }
                else return false;

            case SET_PIN_PROGRESS_MODIFY_ENTER_NEW_PIN:
                password = pwd;
                progress = SET_PIN_PROGRESS_MODIFY_CONFIRM_NEW_PIN;
                updateTitleUI(getString(R.string.settingmodule_fragment_setting_security_modify_pin_title_confirm_your_pin));
                EventBus.getDefault().post(new WhichBannerWillBeShow(CataSettingConstant.CONFIRM_LOCK_SCREEN_PIN));
                return true;
            case SET_PIN_PROGRESS_MODIFY_CONFIRM_NEW_PIN:
                if (pwd.equals(password)) {
                    // 清空上次的图像码
                    SecurityTable patternSecurity = SecurityDBUtil.getSecurity(getActivity(), CataSettingConstant.SECURITY_MODE_PATTERN);
                    if (!patternSecurity.getPassword().equals("")) {
                        SecurityDBUtil.setDefaultSecurity(getActivity(), CataSettingConstant.SECURITY_MODE_PATTERN,"");
                    }
                    // 设置为新的 pin 码
                    SecurityDBUtil.setDefaultSecurity(getActivity(),CataSettingConstant.SECURITY_MODE_PIN,password);
                    //设置完成
                    if (mListener != null) mListener.onSecuritySettingPinModifyFragmentFinish();
                    return true;
                }
                else return false;
        }

        return false;
    }

    @Override
    public void onWarning(String msg) {

    }

    public void setOnSecuritySettingPinModifyFragmentListener(OnSecuritySettingPinModifyFragmentListener listener) {
        mListener = listener;
    }

    public interface OnSecuritySettingPinModifyFragmentListener {
        void onSecuritySettingPinModifyFragmentFinish();

    }
}
