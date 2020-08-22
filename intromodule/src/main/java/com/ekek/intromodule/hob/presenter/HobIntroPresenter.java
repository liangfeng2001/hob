package com.ekek.intromodule.hob.presenter;

import android.support.annotation.NonNull;

import com.ekek.intromodule.hob.contract.HobIntroContract;
import com.ekek.intromodule.hob.model.HobIntroModelImpl;
import com.ekek.intromodule.hob.view.HobIntroFragment;

import static com.google.common.base.Preconditions.checkNotNull;

public class HobIntroPresenter implements HobIntroContract.Presenter,HobIntroModelImpl.HobIntroModelCallback {
    @NonNull
    private final HobIntroContract.View mView;
    @NonNull
    private final HobIntroModelImpl mModel;


    public HobIntroPresenter(@NonNull HobIntroContract.View view,int hobType) {

        mView = checkNotNull(view, "view cannot be null");
        mModel = new HobIntroModelImpl(this,hobType);

        mView.setPresenter(this);
    }

    //初始化
    @Override
    public void start() {
        mModel.start();
    }
    //结束时回收资源
    @Override
    public void stop() {
        mModel.stop();
    }

    //model通知更新UI
    @Override
    public void onHobStatusChange(Integer highTempIndicatorResID) {
        mView.updateHobStatusUI(highTempIndicatorResID);
    }

    @Override
    public void onHob90StatusChange(boolean firstFlag, boolean secondFlag, boolean thirdFlag, boolean fourthFlag, boolean fifthFlag) {
        mView.updateHob90StatusUI(firstFlag, secondFlag, thirdFlag, fourthFlag, fifthFlag);
    }

    @Override
    public void onHob80StatusChange(boolean firstFlag, boolean secondFlag, boolean thirdFlag, boolean fourthFlag) {
        mView.updateHob80StatusUI(firstFlag, secondFlag, thirdFlag, fourthFlag);

    }

    @Override
    public void onHob80StatusChange() {

    }


    //model通知开机
    @Override
    public void onHobPowerOn() {
        if (mView.isRespondPowerOnSignal()) {
            mView.doFinish(mView.getTotalPowerOffMode() == HobIntroFragment.TOTAL_POWER_OFF_NONE);
        }
    }

    @Override
    public void onHobPowerOff() {

    }

    @Override
    public void doFinish(boolean silently) {
        mView.doFinish(silently);
    }
}
