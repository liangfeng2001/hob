package com.ekek.tfthobmodule.view;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.commonmodule.utils.Logger;
import com.ekek.tfthobmodule.R;
import com.ekek.tfthobmodule.constants.TFTHobConstant;
import com.ekek.tfthobmodule.data.CookerSettingData;
import com.ekek.tfthobmodule.database.SettingDbHelper;
import com.ekek.tfthobmodule.event.BleBatteryEvent;
import com.ekek.tfthobmodule.event.NoExternalSensorDetectedEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class HobTemperatureModeSelectFragment extends BaseFragment {
    private static final int SETTING_MODE_FAST_TEMP_GEAR = 2;
    private static final int SETTING_MODE_PRECISE_TEMP_GEAR = 3;
    @BindView(R.id.iv_temp_mode)
    ImageView ivTempMode;
    @BindView(R.id.tv_precise)
    TextView tvPrecise;
    @BindView(R.id.tv_fast)
    TextView tvFast;
    private CookerSettingData mCookerSettingData;
    private Map<Integer, Bitmap> bitmapMap = new HashMap<>();

    private boolean noSensorDetectedHandled = false;

    public HobTemperatureModeSelectFragment() {

    }

    @SuppressLint("ValidFragment")
    public HobTemperatureModeSelectFragment(CookerSettingData data) {
        mCookerSettingData = data;

    }

    public void setSettingInitData(CookerSettingData data) {
        mCookerSettingData = data;
    }

    @Override
    public int initLyaout() {
        return R.layout.tfthobmodule_fragment_temperature_mode_select;
    }

    @Override
    public void initListener() {

    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (mRootView != null) {
            noSensorDetectedHandled = false;
            if (!hidden) {
                init();
                registerEventBus();
            } else {
                unregisterEventBus();
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        initTexts();
    }

    private void init() {
        if (isTempSensorEnable()) {
            noSensorDetectedHandled = false;
            ivTempMode.setImageBitmap(getBitmap(R.mipmap.bg_cooker_setting_temp_mode_fast_precise));
        } else {
            ivTempMode.setImageBitmap(getBitmap(R.mipmap.bg_cooker_setting_fast_precise_mode_disable));
            if (!noSensorDetectedHandled) {
                EventBus.getDefault().post(new NoExternalSensorDetectedEvent(
                        NoExternalSensorDetectedEvent.SOURCE_HOB_TEMPERATURE_MODE_SELECT_FRAGMENT));
                noSensorDetectedHandled = true;
            }
        }

        ivTempMode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                processOperateAreaTouchEvent(view, motionEvent);
                return true;
            }
        });

        initTexts();
    }

    private void initTexts() {
        tvFast.setTypeface(GlobalVars.getInstance().getDefaultFontRegular());
        tvFast.setText(R.string.tfthobmodule_title_fast);
        tvPrecise.setTypeface(GlobalVars.getInstance().getDefaultFontRegular());
        tvPrecise.setText(R.string.tfthobmodule_title_precise);

        if (GlobalVars.getInstance().getCurrentLocale().toString().toLowerCase().equals("ru")) {
            tvPrecise.setTextSize(52.0f);
        } else {
            tvPrecise.setTextSize(60.0f);
        }
    }

    protected void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    protected void unregisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BleBatteryEvent event) {
       /* if (event.getBatteryViewStatus() == BleBatteryEvent.BATTERY_VIEW_STATUS_SHOW) {//bt connect

        }else if (event.getBatteryViewStatus() == BleBatteryEvent.BATTERY_VIEW_STATUS_HIDE) {//bt lost

        }*/
        if (isVisible()) {
            updateUI();
        }
    }

    private void updateUI() {
        if (isTempSensorEnable()) {
            noSensorDetectedHandled = false;
            ivTempMode.setImageBitmap(getBitmap(R.mipmap.bg_cooker_setting_temp_mode_fast_precise));
        } else {
            ivTempMode.setImageBitmap(getBitmap(R.mipmap.bg_cooker_setting_fast_precise_mode_disable));
            if (!noSensorDetectedHandled) {
                EventBus.getDefault().post(new NoExternalSensorDetectedEvent(
                        NoExternalSensorDetectedEvent.SOURCE_HOB_TEMPERATURE_MODE_SELECT_FRAGMENT));
                noSensorDetectedHandled = true;
            }
        }
    }

    private boolean isTempSensorEnable() {
        int sensorStatus = SettingDbHelper.getTemperatureSensorStatus();
        LogUtil.d("sensorStatus---->" + sensorStatus);
        return sensorStatus == -1 || sensorStatus == mCookerSettingData.getCookerID();//-1表示温度传感器空闲
    }

    private void processOperateAreaTouchEvent(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float touchY = event.getY();
            float centerY = view.getHeight() / 2;
                if (touchY > centerY) {
                    mCookerSettingData.setTempMode(SETTING_MODE_FAST_TEMP_GEAR);
                    int tempValue = mCookerSettingData.getTempValue();
                    if (tempValue % 10 != 0) {
                        tempValue = (int)(Math.round(tempValue / 10.0) * 10);
                        if (tempValue > TFTHobConstant.COOKER_FAST_TEMP_MAX) {
                            tempValue = TFTHobConstant.COOKER_FAST_TEMP_MAX;
                        } else if (tempValue < TFTHobConstant.COOKER_FAST_TEMP_MIN) {
                            tempValue = TFTHobConstant.COOKER_FAST_TEMP_MIN;
                        }
                        mCookerSettingData.setTempValue(tempValue);
                    }
                    startToCook();
                } else if (touchY < centerY) {
                    if (isTempSensorEnable()) {
                        mCookerSettingData.setTempMode(SETTING_MODE_PRECISE_TEMP_GEAR);
                        startToCook();
                }

                }
        }
    }

    private void startToCook() {
        ((OnHobTemperatureModeSelectFragmentListener)mListener).OnHobTemperatureModeSelectFragmentRequestStartToCook(mCookerSettingData);
    }

    private Bitmap getBitmap(int source) {
        if (bitmapMap.containsKey(source)) {
            return bitmapMap.get(source);
        }

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), source);
        bitmapMap.put(source, bitmap);
        return bitmap;
    }

    public interface OnHobTemperatureModeSelectFragmentListener extends OnFragmentListener {
        void onHobTemperatureModeSelectFragmentFinish();
        void OnHobTemperatureModeSelectFragmentRequestStartToCook(CookerSettingData data);
    }

}
