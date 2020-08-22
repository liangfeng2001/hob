package com.ekek.tfthobmodule.model;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class HobCookerSettingModelImpl implements HobCookerSettingModel {

    private HobCookerSettingModel.HobCookerSettingModelCallback mCallback;

    public HobCookerSettingModelImpl(@NonNull HobCookerSettingModel.HobCookerSettingModelCallback callback) {
        mCallback = checkNotNull(callback, "callback cannot be null");
    }


    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }


}
