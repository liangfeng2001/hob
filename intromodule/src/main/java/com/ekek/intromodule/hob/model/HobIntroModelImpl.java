package com.ekek.intromodule.hob.model;

import android.support.annotation.NonNull;

import com.ekek.commonmodule.GlobalCons;
import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.hardwaremodule.entity.CookerHardwareResponse;
import com.ekek.hardwaremodule.entity.CookerMessage;
import com.ekek.hardwaremodule.event.CookerHardwareEvent;
import com.ekek.hardwaremodule.event.PowerSwitchStateChangedEvent;
import com.ekek.intromodule.R;
import com.ekek.intromodule.hob.view.HobIntroFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

import static com.google.common.base.Preconditions.checkNotNull;

public class HobIntroModelImpl implements HobIntroModel{

    private HobIntroModelCallback mCallback;
    private int hobType;

    public HobIntroModelImpl(@NonNull HobIntroModelCallback callback ,int hobType) {
        mCallback = checkNotNull(callback, "callback cannot be null");
        this.hobType = hobType;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PowerSwitchStateChangedEvent event) {
        switch (event.getNewState()) {
            case CookerHardwareResponse.POWER_SWITCH_UNKNOWN:
                break;
            case CookerHardwareResponse.POWER_SWITCH_00:
            case CookerHardwareResponse.POWER_SWITCH_11:
            case CookerHardwareResponse.POWER_SWITCH_88:
            case CookerHardwareResponse.POWER_SWITCH_99:
                switch (event.getOldState()) {
                    case CookerHardwareResponse.POWER_SWITCH_UNKNOWN:
                        mCallback.onHobPowerOn();
                        break;
                }
                break;
            case CookerHardwareResponse.POWER_SWITCH_ON:
                switch (event.getOldState()) {
                    case CookerHardwareResponse.POWER_SWITCH_UNKNOWN:
                    case CookerHardwareResponse.POWER_SWITCH_00:
                    case CookerHardwareResponse.POWER_SWITCH_11:
                    case CookerHardwareResponse.POWER_SWITCH_88:
                    case CookerHardwareResponse.POWER_SWITCH_99:
                    case CookerHardwareResponse.POWER_SWITCH_OFF:
                    case CookerHardwareResponse.POWER_SWITCH_OFF_2:
                        mCallback.onHobPowerOn();
                        break;
                }
                break;
            case CookerHardwareResponse.POWER_SWITCH_OFF:
                switch (event.getOldState()) {
                    case CookerHardwareResponse.POWER_SWITCH_UNKNOWN:
                    case CookerHardwareResponse.POWER_SWITCH_00:
                    case CookerHardwareResponse.POWER_SWITCH_11:
                    case CookerHardwareResponse.POWER_SWITCH_88:
                    case CookerHardwareResponse.POWER_SWITCH_99:
                    case CookerHardwareResponse.POWER_SWITCH_ON:
                        mCallback.onHobPowerOff();
                        break;
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CookerHardwareEvent event) {
        if (!GlobalVars.getInstance().isInitializeProcessComplete()) return;

        CookerHardwareResponse response = event.getResponse();
        switch (hobType) {
            case HobIntroFragment.TFT_HOB_TYPE_60:
                processCookerMessageForHob60(response);
                break;
            case HobIntroFragment.TFT_HOB_TYPE_80:
                procesCookerMessageForHob80(response);
                break;
            case HobIntroFragment.TFT_HOB_TYPE_90:
                processCookerMessageForHob90(response);
                break;
        }
    }

    private void procesCookerMessageForHob80(CookerHardwareResponse response) {
        CookerMessage aCookerMessage = response.getaCookerMessage();
        CookerMessage bCookerMessage = response.getbCookerMessage();
        CookerMessage cCookerMessage = response.getcCookerMessage();
        CookerMessage dCookerMessage = response.getdCookerMessage();

        mCallback.onHob80StatusChange(aCookerMessage.isHighTemp(),bCookerMessage.isHighTemp(),cCookerMessage.isHighTemp(),dCookerMessage.isHighTemp());

    }

    private void processCookerMessageForHob90(CookerHardwareResponse response) {

        CookerMessage aCookerMessage = response.getaCookerMessage();
        CookerMessage bCookerMessage = response.getbCookerMessage();
        CookerMessage cCookerMessage = response.getcCookerMessage();
        CookerMessage eCookerMessage = response.geteCookerMessage();
        CookerMessage fCookerMessage = response.getfCookerMessage();
        mCallback.onHob90StatusChange(aCookerMessage.isHighTemp(),bCookerMessage.isHighTemp(),cCookerMessage.isHighTemp(),eCookerMessage.isHighTemp(),fCookerMessage.isHighTemp());

    }

    private void processCookerMessageForHob60(CookerHardwareResponse response) {
        CookerMessage aCookerMessage = response.getaCookerMessage();
        CookerMessage bCookerMessage = response.getbCookerMessage();
        CookerMessage cCookerMessage = response.getcCookerMessage();
        int highTempCode = 0;
        if (aCookerMessage.isHighTemp()) highTempCode = highTempCode + 1;
        if (bCookerMessage.isHighTemp()) highTempCode = highTempCode + 3;
        if (cCookerMessage.isHighTemp()) highTempCode = highTempCode + 5;
        switch (highTempCode) {
            case 0://全低温
              //  mCallback.onHobStatusChange(R.mipmap.ic_high_temp_normal);
               mCallback.onHobStatusChange(null);
                break;
            case 1://a high temp
                mCallback.onHobStatusChange(R.mipmap.ic_high_temp_a);
                break;
            case 3://b high temp
                mCallback.onHobStatusChange(R.mipmap.ic_high_temp_b);
                break;
            case 5://c high temp
                mCallback.onHobStatusChange(R.mipmap.ic_high_temp_c);
                break;
            case 4://a b high temp
                mCallback.onHobStatusChange(R.mipmap.ic_high_temp_ab);
                break;
            case 6://a c high temp
                mCallback.onHobStatusChange(R.mipmap.ic_high_temp_ac);
                break;
            case 8://b c high temp
                mCallback.onHobStatusChange(R.mipmap.ic_high_temp_bc);
                break;
            case 9://全高温
                mCallback.onHobStatusChange(R.mipmap.ic_high_temp_abc);
                break;


        }

    }

    @Override
    public void start() {
        LogUtil.d("registerEventBus");
        registerEventBus();
    }

    @Override
    public void stop() {
        LogUtil.d("un       registerEventBus");
        unregisterEventBus();
    }

    private void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void unregisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this);
    }

}
