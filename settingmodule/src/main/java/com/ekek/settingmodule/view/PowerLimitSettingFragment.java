package com.ekek.settingmodule.view;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.settingmodule.R;
import com.ekek.settingmodule.R2;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.database.SettingPreferencesUtil;
import com.ekek.settingmodule.events.PowerLimitEvent;
import com.ekek.settingmodule.model.SettingDoBack;
import com.ekek.viewmodule.common.FocusedTextView;
import com.ekek.viewmodule.common.NoTextSwitch;
import com.ekek.viewmodule.product.ProductManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PowerLimitSettingFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {
    @BindView(R2.id.sw_power_limit)
    NoTextSwitch swPowerLimit;
    @BindView(R2.id.tv_2800)
    TextView tv2800;
    @BindView(R2.id.tv_3300)
    TextView tv3300;
    @BindView(R2.id.tv_4500)
    TextView tv4500;
    @BindView(R2.id.tv_5700)
    TextView tv5700;
    @BindView(R2.id.ll_power)
    LinearLayout llPower;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.title_use_power_limit)
    FocusedTextView titleUsePowerLimit;
    Unbinder unbinder;

    private static final int ID_INVALID = -1;

    private Typeface typeface;
    private TextView currentSelected;

    @Override
    public int initLyaout() {
        return R.layout.settingmodule_fragment_power_limit_setting;
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

    private void SetUI(){
        if(ProductManager.PRODUCT_TYPE== ProductManager.Haier){
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_power_limit_haier);
            drawable.setBounds(0, 0, 53, 53);
            tvTitle.setCompoundDrawables(drawable, null, null, null);
        }
    }

    @OnClick(R2.id.tv_title)
    public void onClick() {
        EventBus.getDefault().post(new SettingDoBack(CataSettingConstant.do_back));
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }



    private void SetFont() {

        typeface = GlobalVars.getInstance().getDefaultFontRegular();
        tvTitle.setTypeface(typeface);
        titleUsePowerLimit.setTypeface(typeface);
        tv2800.setTypeface(typeface);
        tv3300.setTypeface(typeface);
        tv4500.setTypeface(typeface);
        tv5700.setTypeface(typeface);
    }

    private void init() {
        swPowerLimit.setOnCheckedChangeListener(this);
        boolean powerLimitSwitchStatus = SettingPreferencesUtil.getPowerLimitSwitchStatus(getActivity()).equals(CataSettingConstant.POWER_LIMIT_SWITCH_STATUS_OPEN) ? true : false;
        enablePowerLimitOptions(powerLimitSwitchStatus);

        int level = SettingPreferencesUtil.getPowerLimitLevel(getActivity());
        switch (level) {
            case CataSettingConstant.POWER_LIMIT_LEVEL_2800:
                updatePowerLevelUI(R.id.tv_2800);
                break;
            case CataSettingConstant.POWER_LIMIT_LEVEL_3300:
                updatePowerLevelUI(R.id.tv_3300);
                break;
            case CataSettingConstant.POWER_LIMIT_LEVEL_4500:
                updatePowerLevelUI(R.id.tv_4500);
                break;
            case CataSettingConstant.POWER_LIMIT_LEVEL_5700:
                updatePowerLevelUI(R.id.tv_5700);
                break;
            default:
                updatePowerLevelUI(ID_INVALID);
                break;
        }
    }


    @OnClick({R2.id.tv_2800, R2.id.tv_3300, R2.id.tv_4500, R2.id.tv_5700})
    public void onViewClicked(View view) {
        int i = view.getId();
        updatePowerLevelUI(i);
        savePowerLevel(i);
    }

    private void savePowerLevel(int id) {
        if (id == R.id.tv_2800) {

            SettingPreferencesUtil.savePowerLimitLevel(getActivity(), CataSettingConstant.POWER_LIMIT_LEVEL_2800);
            notifyUpdatePowerLimit();
        } else if (id == R.id.tv_3300) {
            SettingPreferencesUtil.savePowerLimitLevel(getActivity(), CataSettingConstant.POWER_LIMIT_LEVEL_3300);
            notifyUpdatePowerLimit();
        } else if (id == R.id.tv_4500) {
            SettingPreferencesUtil.savePowerLimitLevel(getActivity(), CataSettingConstant.POWER_LIMIT_LEVEL_4500);
            notifyUpdatePowerLimit();
        } else if (id == R.id.tv_5700) {
            SettingPreferencesUtil.savePowerLimitLevel(getActivity(), CataSettingConstant.POWER_LIMIT_LEVEL_5700);
            notifyUpdatePowerLimit();
        }
    }

    private void notifyUpdatePowerLimit() {
        EventBus.getDefault().post(new PowerLimitEvent());
    }

    private void updatePowerLevelUI(int id) {
        SpannableString spanText;
        spanText = new SpannableString("UnderlineSpan");
        spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);


        if (id == R.id.tv_2800) {
            spanText = new SpannableString("2800W-12A");
            spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tv2800.setText(spanText);

            tv3300.setText("");
            tv3300.setText("3300W-14A");

            tv4500.setText("");
            tv4500.setText("4500W-20A");

            tv5700.setText("");
            tv5700.setText("5700W-25A");

            currentSelected = tv2800;
            tv2800.setTextColor(getResources().getColor(R.color.settingmodule_white));
            tv3300.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tv4500.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tv5700.setTextColor(getResources().getColor(R.color.settingmodule_gray));
        } else if (id == R.id.tv_3300) {

            spanText = new SpannableString("3300W-14A");
            spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tv3300.setText(spanText);

            tv2800.setText("");
            tv2800.setText("2800W-12A");

            tv4500.setText("");
            tv4500.setText("4500W-20A");

            tv5700.setText("");
            tv5700.setText("5700W-25A");

            currentSelected = tv3300;
            tv2800.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tv3300.setTextColor(getResources().getColor(R.color.settingmodule_white));
            tv4500.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tv5700.setTextColor(getResources().getColor(R.color.settingmodule_gray));
        } else if (id == R.id.tv_4500) {

            spanText = new SpannableString("4500W-20A");
            spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tv4500.setText(spanText);

            tv2800.setText("");
            tv2800.setText("2800W-12A");

            tv3300.setText("");
            tv3300.setText("3300W-14A");

            tv5700.setText("");
            tv5700.setText("5700W-25A");

            currentSelected = tv4500;
            tv2800.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tv3300.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tv4500.setTextColor(getResources().getColor(R.color.settingmodule_white));
            tv5700.setTextColor(getResources().getColor(R.color.settingmodule_gray));
        } else if (id == R.id.tv_5700) {

            spanText = new SpannableString("5700W-25A");
            spanText.setSpan(new UnderlineSpan(), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tv5700.setText(spanText);

            tv2800.setText("");
            tv2800.setText("2800W-12A");

            tv3300.setText("");
            tv3300.setText("3300W-14A");

            tv4500.setText("");
            tv4500.setText("4500W-20A");

            currentSelected = tv5700;
            tv2800.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tv3300.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tv4500.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tv5700.setTextColor(getResources().getColor(R.color.settingmodule_white));
        } else {
            currentSelected = null;

            tv2800.setText("");
            tv2800.setText("2800W-12A");

            tv3300.setText("");
            tv3300.setText("3300W-14A");

            tv4500.setText("");
            tv4500.setText("4500W-20A");

            tv5700.setText("");
            tv5700.setText("5700W-25A");

            tv2800.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tv3300.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tv4500.setTextColor(getResources().getColor(R.color.settingmodule_gray));
            tv5700.setTextColor(getResources().getColor(R.color.settingmodule_gray));
        }
    }

    private void enablePowerLimitOptions(boolean enabled) {
        tv2800.setEnabled(enabled);
        tv3300.setEnabled(enabled);
        tv4500.setEnabled(enabled);
        tv5700.setEnabled(enabled);

        if (enabled) {
            if (currentSelected != null) {
                updatePowerLevelUI(currentSelected.getId());
            } else {
                updatePowerLevelUI(ID_INVALID);
            }

            tv2800.setVisibility(View.VISIBLE);
            tv3300.setVisibility(View.VISIBLE);
            tv4500.setVisibility(View.VISIBLE);
            tv5700.setVisibility(View.VISIBLE);
            swPowerLimit.setChecked(true);
        } else {

            tv2800.setTextColor(getResources().getColor(R.color.settingmodule_gray80));
            tv3300.setTextColor(getResources().getColor(R.color.settingmodule_gray80));
            tv4500.setTextColor(getResources().getColor(R.color.settingmodule_gray80));
            tv5700.setTextColor(getResources().getColor(R.color.settingmodule_gray80));

            tv2800.setVisibility(View.INVISIBLE);
            tv3300.setVisibility(View.INVISIBLE);
            tv4500.setVisibility(View.INVISIBLE);
            tv5700.setVisibility(View.INVISIBLE);
            swPowerLimit.setChecked(false);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            swPowerLimit.setChecked(true);
            enablePowerLimitOptions(true);
            SettingPreferencesUtil.savePowerLimitSwitchStatus(getActivity(), CataSettingConstant.POWER_LIMIT_SWITCH_STATUS_OPEN);
            notifyUpdatePowerLimit();
        } else {
            swPowerLimit.setChecked(false);
            enablePowerLimitOptions(false);
            SettingPreferencesUtil.savePowerLimitSwitchStatus(getActivity(), CataSettingConstant.POWER_LIMIT_SWITCH_STATUS_CLOSE);
            notifyUpdatePowerLimit();
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
