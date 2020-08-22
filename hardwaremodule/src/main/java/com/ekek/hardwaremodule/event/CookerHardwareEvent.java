package com.ekek.hardwaremodule.event;


import com.ekek.hardwaremodule.entity.CookerHardwareResponse;

/**
 * Created by Samhung on 2018/1/10.
 */

public class CookerHardwareEvent {
    private CookerHardwareResponse response;

    public CookerHardwareEvent(CookerHardwareResponse response) {
        this.response = response;
    }

    public CookerHardwareResponse getResponse() {
        return response;
    }

    public void setResponse(CookerHardwareResponse response) {
        this.response = response;
    }
}
