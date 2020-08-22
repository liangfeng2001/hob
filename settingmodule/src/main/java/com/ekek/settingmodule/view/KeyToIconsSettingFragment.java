package com.ekek.settingmodule.view;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.settingmodule.R;
import com.ekek.settingmodule.R2;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.model.SettingDoBack;
import com.ekek.viewmodule.product.ProductManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class KeyToIconsSettingFragment extends BaseFragment {
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    Unbinder unbinder;
    @BindView(R2.id.tv_lock)
    TextView tvLock;
    @BindView(R2.id.tv_close_timer)
    TextView tvCloseTimer;
    @BindView(R2.id.tv_close_information)
    TextView tvCloseInformation;
    @BindView(R2.id.tv_pause)
    TextView tvPause;
    @BindView(R2.id.tv_close)
    TextView tvClose;
    @BindView(R2.id.tv_close_hood_options)
    TextView tvCloseHoodOptions;
    @BindView(R2.id.tv_recipes)
    TextView tvRecipes;
    @BindView(R2.id.tv_power)
    TextView tvPower;
    @BindView(R2.id.tv_timer)
    TextView tvTimer;
    @BindView(R2.id.tv_alarm)
    TextView tvAlarm;
    @BindView(R2.id.tv_temperature_sensor)
    TextView tvTemperatureSensor;
    @BindView(R2.id.tv_settings)
    TextView tvSettings;
    @BindView(R2.id.tv_temperature)
    TextView tvTemperature;
    private Typeface typeface;

    @Override
    public int initLyaout() {
        return R.layout.settingmodule_fragment_key_to_icons_setting;
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


    @OnClick({
            R2.id.tv_title,
            R2.id.tv_lock,
            R2.id.tv_close_timer,
            R2.id.tv_close_information,
            R2.id.tv_pause,
            R2.id.tv_close,
            R2.id.tv_close_hood_options,
            R2.id.tv_recipes,
            R2.id.tv_power,
            R2.id.tv_timer,
            R2.id.tv_alarm,
            R2.id.tv_temperature_sensor,
            R2.id.tv_settings,
            R2.id.tv_temperature})
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tv_title) {
            EventBus.getDefault().post(new SettingDoBack(CataSettingConstant.do_back));
        } else {
            View v = mRootView.findViewById(viewId);
            if (v != null) {
                v.requestFocus();
            }
        }
    }


    private void SetFont() {

        typeface = GlobalVars.getInstance().getDefaultFontRegular();
        tvTitle.setTypeface(typeface);
        tvLock.setTypeface(typeface);
        tvCloseTimer.setTypeface(typeface);
        tvCloseInformation.setTypeface(typeface);
        tvPause.setTypeface(typeface);
        tvClose.setTypeface(typeface);
        tvCloseHoodOptions.setTypeface(typeface);
        tvRecipes.setTypeface(typeface);
        tvPower.setTypeface(typeface);
        tvTimer.setTypeface(typeface);
        tvAlarm.setTypeface(typeface);
        tvTemperatureSensor.setTypeface(typeface);
        tvSettings.setTypeface(typeface);
        tvTemperature.setTypeface(typeface);
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
