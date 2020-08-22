package com.ekek.tfthobmodule.core;

import com.ekek.hardwaremodule.entity.CookerHardwareResponse;
import com.ekek.tfthobmodule.data.CookerSettingData;

public abstract class BaseCookerManager {
    protected abstract int notifyUpdateCookerMessage(CookerHardwareResponse response);
    public abstract void notifyUpdateCookerData(CookerSettingData data);
    protected abstract int notifyCookerPowerOff();
    protected abstract int notifyCookerReadyToCook();
    protected abstract int notifyCookerKeepWarm();
    protected abstract int notifyCookerAddTenMinute();

}
