package com.ekek.tfthobmodule.presenter;

import android.support.annotation.NonNull;

import com.ekek.tfthobmodule.contract.HobCookerSettingContract;
import com.ekek.tfthobmodule.model.HobCookerSettingModelImpl;

import static com.google.common.base.Preconditions.checkNotNull;

public class HobCookerSettingPresenter implements HobCookerSettingContract.Presenter ,HobCookerSettingModelImpl.HobCookerSettingModelCallback{
    @NonNull
    private final HobCookerSettingContract.View mView;
    @NonNull
    private final HobCookerSettingModelImpl mModel;

    public HobCookerSettingPresenter(@NonNull HobCookerSettingContract.View view) {

        mView = checkNotNull(view, "view cannot be null");
        mModel = new HobCookerSettingModelImpl(this);

        mView.setPresenter(this);
    }

    @Override
    public void doFinish() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void onHobStatusChange(int status) {

    }

    @Override
    public void onHobPowerOn() {

    }
}
