package com.ekek.settingmodule.view;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.settingmodule.R;
import com.ekek.settingmodule.R2;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.database.SecurityDBUtil;
import com.ekek.settingmodule.database.SettingPreferencesUtil;
import com.ekek.settingmodule.entity.SecurityTable;
import com.ekek.settingmodule.model.SettingDoBack;
import com.ekek.viewmodule.product.ProductManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Samhung on 2018/2/4.
 */

public class SecuritySettingFragment extends Fragment {

    public static final int SECURITY_SET_PIN_PASSWORD = 0;
    public static final int SECURITY_SET_PATTERN_PASSWORD = 1;

    @BindView(R2.id.ct_pin)
    CheckedTextView ctPin;
    @BindView(R2.id.ib_set_pin)
    ImageButton ibSetPin;
    @BindView(R2.id.ct_pattern)
    CheckedTextView ctPattern;
    @BindView(R2.id.ib_set_pattern)
    ImageButton ibSetPattern;
    @BindView(R2.id.ct_press_unlock)
    CheckedTextView ctPressUnlock;
    Unbinder unbinder;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.title_screen_lock)
    TextView titleScreenLock;
    private MaterialDialog passwordDialog;
    private OnSecuritySettingFragmentListener mListener;
    private Typeface typeface;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settingmodule_fragment_setting_security, container, false);

        unbinder = ButterKnife.bind(this, view);
        SetFont();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();

        SetUI();
        LogUtil.d("the security is start ~~~");
    }

    private void SetUI(){
        if(ProductManager.PRODUCT_TYPE== ProductManager.Haier){
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_security_haier);
            drawable.setBounds(0, 0, 53, 53);
            tvTitle.setCompoundDrawables(drawable, null, null, null);
        }
    }


    private void init() {
      /*  String securityMode = SettingPreferencesUtil.getDefaultSecurityMode(getActivity());
        if (securityMode.equals(CataSettingConstant.SECURITY_MODE_PIN)) {
            updateSecurityPinMode();
        }else if (securityMode.equals(CataSettingConstant.SECURITY_MODE_PATTERN)) {
            updateSecurityPatternMode();
        }else if (securityMode.equals(CataSettingConstant.SECURITY_MODE_PRESS_UNLOCK)) {
            updateSecurityPressUnlockMode();
        }*/

        SecurityTable defaultSecurityTable = SecurityDBUtil.getDefaultSecurity(getActivity());
        String securityType = defaultSecurityTable.getType();
        if (securityType.equals(CataSettingConstant.SECURITY_MODE_PIN)) {
            updateSecurityPinMode();
        } else if (securityType.equals(CataSettingConstant.SECURITY_MODE_PATTERN)) {
            updateSecurityPatternMode();
        } else if (securityType.equals(CataSettingConstant.SECURITY_MODE_PRESS_UNLOCK)) {
            updateSecurityPressUnlockMode();
        }

        ibSetPin.setVisibility(View.INVISIBLE);
        ibSetPattern.setVisibility(View.INVISIBLE);
    }
    @OnClick(R2.id.tv_title)
    public void onClick() {
        EventBus.getDefault().post(new SettingDoBack(CataSettingConstant.do_back));
    }



    private void SetFont() {

        typeface = GlobalVars.getInstance().getDefaultFontRegular();
        tvTitle.setTypeface(typeface);
        titleScreenLock.setTypeface(typeface);
        ctPin.setTypeface(typeface);
        ctPattern.setTypeface(typeface);
        ctPressUnlock.setTypeface(typeface);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R2.id.ct_pin, R2.id.ib_set_pin, R2.id.ct_pattern, R2.id.ib_set_pattern, R2.id.ct_press_unlock})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.ct_pin) {   // PIN 码
            updateSecurityPinMode();
            //SettingPreferencesUtil.saveDefaultSecurityMode(getActivity(),CataSettingConstant.SECURITY_MODE_PIN);
            SecurityTable pinSecurity = SecurityDBUtil.getSecurity(getActivity(), CataSettingConstant.SECURITY_MODE_PIN);
            if (pinSecurity.getPassword().equals("")) {
                mListener.onSecuritySettingFragmentRequestSetPassword(SECURITY_SET_PIN_PASSWORD);
            } else {
                SecurityDBUtil.setDefaultSecurity(getActivity(), CataSettingConstant.SECURITY_MODE_PIN);
            }

        } else if (i == R.id.ib_set_pin) {
            //showSetPinPasswordDialog();
            mListener.onSecuritySettingFragmentRequestSetPassword(SECURITY_SET_PIN_PASSWORD);

        } else if (i == R.id.ct_pattern) {  // 图形码
            updateSecurityPatternMode();

            SecurityTable patternSecurity = SecurityDBUtil.getSecurity(getActivity(), CataSettingConstant.SECURITY_MODE_PATTERN);
            if (patternSecurity.getPassword().equals("")) {
                mListener.onSecuritySettingFragmentRequestSetPassword(SECURITY_SET_PATTERN_PASSWORD);
            } else {
                SecurityDBUtil.setDefaultSecurity(getActivity(), CataSettingConstant.SECURITY_MODE_PATTERN);
            }


        } else if (i == R.id.ib_set_pattern) {
            mListener.onSecuritySettingFragmentRequestSetPassword(SECURITY_SET_PATTERN_PASSWORD);

        } else if (i == R.id.ct_press_unlock) {
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

            updateSecurityPressUnlockMode();
            SecurityDBUtil.setDefaultSecurity(getActivity(), CataSettingConstant.SECURITY_MODE_PRESS_UNLOCK);
        }
    }

    private void updateSecurityPinMode() {
        ctPin.setChecked(true);
        ctPattern.setChecked(false);
        ctPressUnlock.setChecked(false);
    }

    private void updateSecurityPatternMode() {
        ctPin.setChecked(false);
        ctPattern.setChecked(true);
        ctPressUnlock.setChecked(false);
    }

    private void updateSecurityPressUnlockMode() {
        ctPin.setChecked(false);
        ctPattern.setChecked(false);
        ctPressUnlock.setChecked(true);
    }

    /*private void showSetPinPasswordDialog() {
        if (passwordDialog != null && passwordDialog.isShowing()) passwordDialog.dismiss();
        passwordDialog = null;
        String content = "";

        if (SettingPreferencesUtil.getSecurityPinPassword(getActivity()).equals("")) {
            content = getString(R.string.settingmodule_fragment_setting_security_content_new_pin_password);

        } else {
            content = getString(R.string.settingmodule_fragment_setting_security_content_old_pin_password);

        }

        passwordDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.settingmodule_fragment_setting_security_title_pin)
                .content(content)
                .inputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD)
                .inputRange(4, 4, Color.RED)
                .input(null, null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {

                    }
                })
                .positiveColor(getResources().getColor(R.color.settingmodule_darkBlue))
                .negativeColor(getResources().getColor(R.color.settingmodule_darkBlue))
                .positiveText(R.string.settingmodule_fragment_setting_security_dialog_button_confirm)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    String pwdStr = "";
                    int flag = 0;//0 set new pwd, 1 change pwd

                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String conentStr = dialog.getContentView().getText().toString();
                        if (conentStr.equals(getString(R.string.settingmodule_fragment_setting_security_content_new_pin_password))) {
                            if (flag == 0) {
                                dialog.setContent(getString(R.string.settingmodule_fragment_setting_security_content_new_pin_password_again));
                                pwdStr = dialog.getInputEditText().getText().toString();
                                dialog.getBuilder().positiveText(getString(R.string.settingmodule_fragment_setting_security_dialog_button_set));
                                dialog.getInputEditText().setText("");

                            } else {
                                dialog.setContent(getString(R.string.settingmodule_fragment_setting_security_content_new_pin_password_again));
                                pwdStr = dialog.getInputEditText().getText().toString();
                                dialog.getBuilder().positiveText(getString(R.string.settingmodule_fragment_setting_security_dialog_button_set));
                                dialog.getInputEditText().setText("");

                            }


                        } else if (conentStr.equals(getString(R.string.settingmodule_fragment_setting_security_content_new_pin_password_again))) {
                            if (flag == 0) {
                                String currentPwdStr = dialog.getInputEditText().getText().toString();
                                if (pwdStr.equals(currentPwdStr)) {
                                    SettingPreferencesUtil.saveSecurityPinPassword(getActivity(), pwdStr);
                                    dialog.dismiss();
                                } else {
                                    dialog.getInputEditText().setText("");
                                }
                            } else {
                                String currentPwdStr = dialog.getInputEditText().getText().toString();
                                if (pwdStr.equals(currentPwdStr)) {
                                    SettingPreferencesUtil.saveSecurityPinPassword(getActivity(), pwdStr);
                                    dialog.dismiss();


                                } else {
                                    dialog.getInputEditText().setText("");
                                }
                            }


                        } else if (conentStr.equals(getString(R.string.settingmodule_fragment_setting_security_content_old_pin_password))) {
                            flag = 1;
                            String oldPwdStr = dialog.getInputEditText().getText().toString();
                            if (SettingPreferencesUtil.getSecurityPinPassword(getActivity()).equals(oldPwdStr)) {
                                dialog.getInputEditText().setText("");
                                dialog.setContent(getString(R.string.settingmodule_fragment_setting_security_content_new_pin_password));


                            } else {
                                dialog.getInputEditText().setText("");
                            }
                        }


                    }
                })
                .negativeText(R.string.settingmodule_fragment_setting_security_dialog_button_cancel)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        dialog.dismiss();
                    }
                })
                .autoDismiss(false)
                .cancelable(false)
                .show();

    }*/

    public void doBack() {
        if (mListener != null) {
            if (ctPressUnlock.isChecked()) {
                mListener.onSecuritySettingFragmentFinish();
            } else if (ctPattern.isChecked()) {
                if (SettingPreferencesUtil.getSecurityPatternPassword(getActivity()).equals("")) {
                } else {
                    mListener.onSecuritySettingFragmentFinish();
                }
            } else if (ctPin.isChecked()) {
                if (SettingPreferencesUtil.getSecurityPinPassword(getActivity()).equals("")) {
                } else {
                    mListener.onSecuritySettingFragmentFinish();
                }
            }
        }

    }

    public void setOnSecuritySettingFragmentListener(OnSecuritySettingFragmentListener listener) {
        mListener = listener;
    }

    public interface OnSecuritySettingFragmentListener {
        void onSecuritySettingFragmentFinish();

        void onSecuritySettingFragmentRequestSetPassword(int what);

    }
}
