package com.ekek.intromodule.hob.view;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.intromodule.R;
import com.ekek.intromodule.R2;
import com.ekek.intromodule.hob.contract.HobIntroContract;
import com.ekek.intromodule.hob.presenter.HobIntroPresenter;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.database.SettingPreferencesUtil;
import com.ekek.viewmodule.hob.Hob80HighTempStatusView;
import com.ekek.viewmodule.hob.Hob90HighTempStatusView;
import com.ekek.viewmodule.time.AnalogicClockView;
import com.ekek.viewmodule.time.DigitalClockView;
import com.ekek.viewmodule.product.ProductManager;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HobIntroFragment extends BaseFragment implements HobIntroContract.View {

    public static final int TFT_HOB_TYPE_60 = 0;
    public static final int TFT_HOB_TYPE_80 = 1;
    public static final int TFT_HOB_TYPE_90 = 2;

    public static final int TOTAL_POWER_OFF_NONE = 0;
    public static final int TOTAL_POWER_OFF_NOW = 1;
    public static final int TOTAL_POWER_OFF_UNTIL_COOL_DOWN = 2;
    public static final int TOTAL_POWER_OFF_FOR_PANNEL_HIGH_TEMP = 3;

    @BindView(R2.id.cl_hob_intro)
    ConstraintLayout clHobIntro;
    @BindView(R2.id.iv_hob_status)
    ImageView ivHobStatus;
    @BindView(R2.id.iv_logo)
    ImageView ivLogo;
    @BindView(R2.id.dcv)
    DigitalClockView dcv;
    @BindView(R2.id.acv)
    AnalogicClockView acv;
    @BindView(R2.id.hht)
    Hob90HighTempStatusView hht;
    Unbinder unbinder;
    @BindView(R2.id.hht_80)
    Hob80HighTempStatusView hht80;
    @BindView(R2.id.iv_black_mask)
    ImageView ivBlackMask;
    private int hobType = TFT_HOB_TYPE_60;
    private boolean isDigitalClock = false;
    private HobIntroContract.Presenter mPresenter;
    private boolean respondPowerOnSignal;
    private boolean clickToQuit = true;
    private int totalPowerOffMode = TOTAL_POWER_OFF_NONE;

    private boolean blackMaskVisibleChanged = false;
    private boolean blackMaskVisible = false;
    private Map<Integer, Bitmap> bitmapMap = new HashMap<>();

    @SuppressLint("ValidFragment")
    public HobIntroFragment(int hobType) {
        this.hobType = hobType;
        mPresenter = new HobIntroPresenter(this, hobType);
    }

    public HobIntroFragment() {
        mPresenter = new HobIntroPresenter(this, hobType);
    }

    @Override
    public int initLyaout() {
        return R.layout.intromodule_fragment_hob_intro;
    }

    @Override
    public void initListener() {
        //((OnHobIntroFragmentListener)mListener).onHobIntroFragmentFinish();
        Log.d("samhung", "Enter:: initListener");

    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        ivHobStatus.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //mPresenter.doFinish();
                return false;
            }
        });
        if(ProductManager.PRODUCT_TYPE==ProductManager.Haier){
            ivLogo.setImageBitmap(getBitmap(R.mipmap.logo_haier));
        }else {
            ivLogo.setImageBitmap(getBitmap(R.mipmap.logo_cata));
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dcv.onLanguageChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();

    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.stop();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            mPresenter.stop();
        } else {
            mPresenter.start();

            if (mRootView == null) return;

            if (blackMaskVisibleChanged) {
                ivBlackMask.setVisibility(blackMaskVisible ? View.VISIBLE : View.INVISIBLE);
                blackMaskVisibleChanged = false;
            }

            String hibernationHourFormat = SettingPreferencesUtil.getHibernationHourFormat(getActivity());
            if (hibernationHourFormat.equals(CataSettingConstant.HIBERNATION_FORMAT_HOUR_FORMAT_DIGITAL)) {
                dcv.setVisibility(View.VISIBLE);
                acv.setVisibility(View.GONE);
            } else {
                acv.setVisibility(View.VISIBLE);
                dcv.setVisibility(View.GONE);
            }

            String logoSwitchStatus = SettingPreferencesUtil.getLogoSwitchStatus(getActivity());
            if (logoSwitchStatus.equals(CataSettingConstant.LOGO_SWITCH_STATUS_OPEN)) {
                ivLogo.setVisibility(View.VISIBLE);
            } else {
                ivLogo.setVisibility(View.GONE);
            }
            String hibernationFormatDateSwitchStatus = SettingPreferencesUtil.getHibernationFormatDateSwitchStatus(getContext());
            if (hibernationFormatDateSwitchStatus.equals(CataSettingConstant.HIBERNATION_FORMAT_DATE_SWITCH_STATUS_OPEN)) {
                dcv.setDateVisibility(true);
                acv.setDateVisibility(true);
            } else {
                dcv.setDateVisibility(false);
                acv.setDateVisibility(false);
            }

            if (acv.getVisibility() == View.VISIBLE
                    && ivLogo.getVisibility() != View.VISIBLE
                    && !acv.isShowDate()) {
                acv.setDialPlateSize(AnalogicClockView.DIAL_PLATE_SIZE_BIG);
                LogUtil.d("acv.setDialPlateSize(AnalogicClockView.DIAL_PLATE_SIZE_BIG)");
            } else {
                acv.setDialPlateSize(AnalogicClockView.DIAL_PLATE_SIZE_NORMAL);
                LogUtil.d("acv.setDialPlateSize(AnalogicClockView.DIAL_PLATE_SIZE_NORMAL)");
            }

            boolean is24Format = SettingPreferencesUtil.getTimeFormat24(getActivity());
            if(is24Format){
                dcv.setTimeFormat24(true);
            }else {
                dcv.setTimeFormat24(false);
            }

           /* switch (hobType) {
                case TFT_HOB_TYPE_60:
                    ivHobStatus.setVisibility(View.VISIBLE);
                    hht.setVisibility(View.INVISIBLE);
                    hht80.setVisibility(View.INVISIBLE);
                    break;
                case TFT_HOB_TYPE_80:
                    ivHobStatus.setVisibility(View.INVISIBLE);
                    hht.setVisibility(View.INVISIBLE);
                    hht80.setVisibility(View.VISIBLE);
                    break;
                case TFT_HOB_TYPE_90:
                    ivHobStatus.setVisibility(View.INVISIBLE);
                    hht80.setVisibility(View.INVISIBLE);
                    hht.setVisibility(View.VISIBLE);
                    break;
            }*/

        }
    }

    @Override
    public void updateHobStatusUI(Integer id) {
        if (id == null) {
            ivHobStatus.setVisibility(View.INVISIBLE);
        } else {
            ivHobStatus.setImageBitmap(getBitmap(id));
            ivHobStatus.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void updateHob90StatusUI(boolean firstFlag, boolean secondFlag, boolean thirdFlag, boolean fourthFlag, boolean fifthFlag) {
        LogUtil.d("Enter:: updateHob90StatusUI--------1");
        if (hht != null) {
            LogUtil.d("Enter:: updateHob90StatusUI");
            if (hht.getVisibility() == View.INVISIBLE) hht.setVisibility(View.VISIBLE);
            hht.updateStatus(firstFlag, secondFlag, thirdFlag, fourthFlag, fifthFlag);
        }


    }

    @Override
    public void updateHob80StatusUI(boolean firstFlag, boolean secondFlag, boolean thirdFlag, boolean fourthFlag) {
        if (hht80 != null) {
            if (hht80.getVisibility() == View.INVISIBLE) hht80.setVisibility(View.VISIBLE);
            hht80.updateStatus(firstFlag, secondFlag, thirdFlag, fourthFlag);

        }
    }



    @Override
    public void doFinish(boolean silently) {
        ((OnHobIntroFragmentListener) mListener).onHobIntroFragmentFinish(silently);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public boolean isRespondPowerOnSignal() {
        return respondPowerOnSignal;
    }

    public void setRespondPowerOnSignal(boolean respondPowerOnSignal) {
        this.respondPowerOnSignal = respondPowerOnSignal;
    }

    @Override
    public boolean isClickToQuit() {
        return clickToQuit;
    }

    public void setClickToQuit(boolean clickToQuit) {
        this.clickToQuit = clickToQuit;
    }

    @Override
    public int getTotalPowerOffMode() {
        return totalPowerOffMode;
    }

    @Override
    public void showBlackMask(boolean visible) {
        if (ivBlackMask != null) {
            ivBlackMask.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        } else {
            blackMaskVisibleChanged = true;
            blackMaskVisible = visible;
        }
    }

    @Override
    public boolean isShowingBlackMask() {
        return ivBlackMask != null && ivBlackMask.getVisibility() == View.VISIBLE;
    }

    public void setTotalPowerOffMode(int totalPowerOffMode) {
        this.totalPowerOffMode = totalPowerOffMode;
    }

    @Override
    public void setPresenter(HobIntroContract.Presenter presenter) {

    }

    private static final String ACTION_SET_USB_DEBUG = "ACTION_EKEK_SET_USB_DEBUG";
    @OnClick(R2.id.cl_hob_intro)
    public void onViewClicked() {
        //LogUtil.d("Enter:: onViewClicked");
        // mPresenter.doFinish();
        //getActivity().sendBroadcast(new Intent("ACTION_EKEK_SET_USB_DEBUG"));
        if (isClickToQuit()) {
            mPresenter.doFinish(true);
        }
    }

    private Bitmap getBitmap(int source) {
        if (bitmapMap.containsKey(source)) {
            return bitmapMap.get(source);
        }

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), source);
        bitmapMap.put(source, bitmap);
        return bitmap;
    }

    public interface OnHobIntroFragmentListener extends OnFragmentListener {
        void onHobIntroFragmentFinish(boolean silently);

    }


}
