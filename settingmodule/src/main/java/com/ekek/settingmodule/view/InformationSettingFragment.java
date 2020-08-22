package com.ekek.settingmodule.view;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.commonmodule.utils.Logger;
import com.ekek.settingmodule.R;
import com.ekek.settingmodule.R2;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.database.SettingPreferencesUtil;
import com.ekek.settingmodule.events.SettingsChangedEvent;
import com.ekek.settingmodule.model.SettingDoBack;
import com.ekek.viewmodule.common.NoTextSwitch;
import com.ekek.viewmodule.product.ProductManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.annotations.NonNull;

public class InformationSettingFragment extends BaseFragment
        implements CompoundButton.OnCheckedChangeListener {

    public static final int INFORMATION_SETTING_FRAGMENT_SELECT_INSTRUCTION_MANUAL = 0;
    public static final int INFORMATION_SETTING_FRAGMENT_SELECT_KEY_TO_ICONS = 1;
    public static final int INFORMATION_SETTING_FRAGMENT_SELECT_DEMO_MODE = 2;
    public static final int INFORMATION_SETTING_FRAGMENT_SELECT_UPDATES = 3;

    private static final long DURATION_LONG = 10 * 1000;
    private static final long DURATION_SHORT = 500;
    private static final int[][] CLICK_PASSWORDS = new int[][] {
            {3},
            {2, 1},
            {1, 2},
            {1, 1, 1}};

    @BindView(R2.id.tv_instruction_manual)
    TextView tvInstructionManual;
    @BindView(R2.id.tv_key_to_icons)
    TextView tvKeyToIcons;
    @BindView(R2.id.tv_software_version)
    TextView tvSoftwareVersion;
    @BindView(R2.id.tv_demo_mode)
    TextView tvDemoMode;
    @BindView(R2.id.tv_debug_mode)
    TextView tvDebugMode;
    @BindView(R2.id.tv_updates)
    TextView tvUpdates;
    @BindView(R2.id.sw_logo)
    NoTextSwitch swLogo;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.title_software_version)
    TextView titleSoftwareVersion;
    @BindView(R2.id.title_logo)
    TextView titleLogo;
    Unbinder unbinder;
    @BindView(R2.id.sw_demo)
    NoTextSwitch swDemo;
    @BindView(R2.id.fl_debug_mode)
    FrameLayout flDebugMode;
    @BindView(R2.id.sw_debug_mode)
    NoTextSwitch swDebugMode;
    @BindView(R2.id.rl_model_info)
    RelativeLayout rlModelInfo;
    @BindView(R2.id.tv_model_title)
    TextView tvModelTitle;
    @BindView(R2.id.tv_model)
    TextView tvModel;
    @BindView(R2.id.rl_system_version)
    RelativeLayout rlSystemVersion;
    @BindView(R2.id.tv_system_version_title)
    TextView tvSystemVersionTitle;
    @BindView(R2.id.tv_system_version)
    TextView tvSystemVersion;

    private Typeface typeface;
    private OnInformationSettingFragmentListener listener;

    private long lastClickTime = 0;
    private List<Integer> clickQueue = new ArrayList<>();

    private interface OnMultiClickCallback {
        void callback();
    }

    @Override
    public int initLyaout() {
        return R.layout.settingmodule_fragment_information_setting;
    }

    @Override
    public void initListener() {
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        SetFont();
        SetUI();
    }

    private void SetUI() {
        if (ProductManager.PRODUCT_TYPE == ProductManager.Haier) {
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_information_haier);
            drawable.setBounds(0, 0, 53, 53);
            tvTitle.setCompoundDrawables(drawable, null, null, null);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        tvSoftwareVersion.setText("v " + getVersion(getActivity()));
        tvModel.setText(ProductManager.getModelInfo());
        if (CataSettingConstant.BRAND_EKEK.equals(Build.BRAND)) {
            tvSystemVersion.setText(Build.DISPLAY);
        } else {
            tvSystemVersion.setText(CataSettingConstant.SYSTEM_VERSION_UNKNOWN);
        }

        String logoSwitchStatus = SettingPreferencesUtil.getLogoSwitchStatus(getActivity());
        if (logoSwitchStatus.equals(CataSettingConstant.LOGO_SWITCH_STATUS_OPEN)) {
            swLogo.setChecked(true);
        } else {
            swLogo.setChecked(false);
        }
        swLogo.setOnCheckedChangeListener(this);

        boolean demoSwitchStatus = SettingPreferencesUtil
                .getDemoSwitchStatus(getActivity())
                .equals(CataSettingConstant.DEMO_SWITCH_STATUS_OPEN) ? true :false;
        if(demoSwitchStatus){
            swDemo.setChecked(true);
        }else {
            swDemo.setChecked(false);
        }
        swDemo.setOnCheckedChangeListener(this);

        swDebugMode.setOnCheckedChangeListener(this);
        showDebugModeOption(GlobalVars.getInstance().isInDebugMode());
        refreshColorOfDebugMode();
    }

    @OnClick({R2.id.tv_instruction_manual, R2.id.tv_key_to_icons, R2.id.tv_demo_mode, R2.id.tv_debug_mode, R2.id.tv_updates, R2.id.tv_software_version, R2.id.title_software_version})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.tv_instruction_manual) {
            listener.onInformationSettingFragmentSelect(INFORMATION_SETTING_FRAGMENT_SELECT_INSTRUCTION_MANUAL);
        } else if (i == R.id.tv_key_to_icons) {
            listener.onInformationSettingFragmentSelect(INFORMATION_SETTING_FRAGMENT_SELECT_KEY_TO_ICONS);

        } else if (i == R.id.tv_demo_mode) {
            listener.onInformationSettingFragmentSelect(INFORMATION_SETTING_FRAGMENT_SELECT_DEMO_MODE);

        } else if (i == R.id.tv_updates) {
            listener.onInformationSettingFragmentSelect(INFORMATION_SETTING_FRAGMENT_SELECT_UPDATES);


        } else if (i == R.id.tv_software_version || i == R.id.title_software_version) {
            onMultiClick(new OnMultiClickCallback() {
                @Override
                public void callback() {
                    showDebugModeOption(flDebugMode.getVisibility() != View.VISIBLE);
                }
            });
        } else if (i == R.id.tv_debug_mode) {
            onMultiClick(new OnMultiClickCallback() {
                @Override
                public void callback() {
                    int debugModeExtra = GlobalVars.getInstance().getDebugModeExtra();
                    switch (debugModeExtra) {
                        case CataSettingConstant.DEBUG_MODE_EXTRA_PARAM_NONE:
                            debugModeExtra = CataSettingConstant.DEBUG_MODE_EXTRA_PARAM_IGNORE_ERROR_AND_NO_PAN;
                            break;
                        case CataSettingConstant.DEBUG_MODE_EXTRA_PARAM_IGNORE_ERROR_AND_NO_PAN:
                            debugModeExtra = CataSettingConstant.DEBUG_MODE_EXTRA_PARAM_IGNORE_ERROR;
                            break;
                        case CataSettingConstant.DEBUG_MODE_EXTRA_PARAM_IGNORE_ERROR:
                            debugModeExtra = CataSettingConstant.DEBUG_MODE_EXTRA_PARAM_IGNORE_NO_PAN;
                            break;
                        case CataSettingConstant.DEBUG_MODE_EXTRA_PARAM_IGNORE_NO_PAN:
                            debugModeExtra = CataSettingConstant.DEBUG_MODE_EXTRA_PARAM_NONE;
                            break;
                        default:
                            debugModeExtra = CataSettingConstant.DEBUG_MODE_EXTRA_PARAM_NONE;
                            break;
                    }
                    SettingPreferencesUtil.saveDebugModeExtra(mContext, debugModeExtra);
                    GlobalVars.getInstance().setDebugModeExtra(debugModeExtra);
                    refreshColorOfDebugMode();
                }
            });
        }
    }

    private void refreshColorOfDebugMode() {
        switch (GlobalVars.getInstance().getDebugModeExtra()) {
            case CataSettingConstant.DEBUG_MODE_EXTRA_PARAM_NONE:
                tvDebugMode.setTextColor(Color.WHITE);
                break;
            case CataSettingConstant.DEBUG_MODE_EXTRA_PARAM_IGNORE_ERROR_AND_NO_PAN:
                tvDebugMode.setTextColor(Color.RED);
                break;
            case CataSettingConstant.DEBUG_MODE_EXTRA_PARAM_IGNORE_ERROR:
                tvDebugMode.setTextColor(Color.GREEN);
                break;
            case CataSettingConstant.DEBUG_MODE_EXTRA_PARAM_IGNORE_NO_PAN:
                tvDebugMode.setTextColor(Color.BLUE);
                break;
            default:
                tvDebugMode.setTextColor(Color.WHITE);
                break;
        }
    }

    private String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void onMultiClick(OnMultiClickCallback multiClickCallback) {
        if (SystemClock.elapsedRealtime() - lastClickTime > DURATION_LONG) {
            clickQueue.clear();
            clickQueue.add(1);
        } else if (SystemClock.elapsedRealtime() - lastClickTime > DURATION_SHORT) {
            clickQueue.add(1);
        } else {
            if (clickQueue.size() == 0) {
                clickQueue.add(1);
            }
            int maxIndex = clickQueue.size() - 1;
            clickQueue.set(maxIndex, clickQueue.get(maxIndex) + 1);
        }
        lastClickTime = SystemClock.elapsedRealtime();

        if (matchClickPassword(CLICK_PASSWORDS)) {
            clickQueue.clear();
            multiClickCallback.callback();
        }
    }

    private boolean matchClickPassword(@NonNull int[][] passwords) {
        for (int[] password: passwords) {
            if (clickQueue.size() < password.length) {
                continue;
            }

            boolean correct = true;
            for (int i = 0; i < password.length; i++) {
                if (clickQueue.get(i) != password[i]) {
                    correct = false;
                    break;
                }
            }

            if (correct) {
                return true;
            }
        }
        return false;
    }

    public void setOnInformationSettingFragmentListener(OnInformationSettingFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton == swLogo) {
            if (b) {
                swLogo.setChecked(true);
                SettingPreferencesUtil.saveLogoSwitchStatus(getActivity(), CataSettingConstant.LOGO_SWITCH_STATUS_OPEN);
            } else {
                swLogo.setChecked(false);
                SettingPreferencesUtil.saveLogoSwitchStatus(getActivity(), CataSettingConstant.LOGO_SWITCH_STATUS_CLOSE);

            }
        }
        if(compoundButton==swDemo){
            if(b){
                swDemo.setChecked(true);
                SettingPreferencesUtil.saveDemoSwitchStatus(getActivity(),CataSettingConstant.DEMO_SWITCH_STATUS_OPEN);
                EventBus.getDefault().post(new SettingsChangedEvent(SettingsChangedEvent.SETTING_DEMO_SWITCH_STATUS));
            }else {
                swDemo.setChecked(false);
                SettingPreferencesUtil.saveDemoSwitchStatus(getActivity(),CataSettingConstant.DEMO_SWITCH_STATUS_CLOSE);
                EventBus.getDefault().post(new SettingsChangedEvent(SettingsChangedEvent.SETTING_DEMO_SWITCH_STATUS));
            }
        }

        if (compoundButton == swDebugMode) {
            SettingPreferencesUtil.saveDebugMode(getActivity(), swDebugMode.isChecked());
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

    @OnClick({R2.id.tv_title})
    public void onClick() {
        EventBus.getDefault().post(new SettingDoBack(CataSettingConstant.do_back));
    }


    private void SetFont() {

        typeface = GlobalVars.getInstance().getDefaultFontRegular();
        tvTitle.setTypeface(typeface);
        tvInstructionManual.setTypeface(typeface);
        tvKeyToIcons.setTypeface(typeface);
        titleSoftwareVersion.setTypeface(typeface);
        titleLogo.setTypeface(typeface);
        tvModelTitle.setTypeface(typeface);
        tvSystemVersionTitle.setTypeface(typeface);
        tvUpdates.setTypeface(typeface);
        tvDemoMode.setTypeface(typeface);
        tvDebugMode.setTypeface(typeface);
        tvModel.setTypeface(typeface);
        tvSoftwareVersion.setTypeface(typeface);
        tvSystemVersion.setTypeface(typeface);
    }
    private void showDebugModeOption(boolean visible) {
        Logger.getInstance().d("showDebugModeOption(" + visible + ")");
        int value = visible ? View.VISIBLE : View.GONE;
        flDebugMode.setVisibility(value);
        rlModelInfo.setVisibility(value);
        rlSystemVersion.setVisibility(value);
        Boolean inDebugMode = SettingPreferencesUtil.getDebugMode(getActivity());
        swDebugMode.setChecked(inDebugMode);
    }

    public interface OnInformationSettingFragmentListener extends OnFragmentListener {
        void onInformationSettingFragmentSelect(int what);
    }
}
