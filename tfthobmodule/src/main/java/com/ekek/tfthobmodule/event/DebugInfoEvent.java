package com.ekek.tfthobmodule.event;

import com.ekek.tfthobmodule.model.AllCookerDataEx;

public class DebugInfoEvent {

    // Fields
    private boolean inDebugMode;
    private byte[] dataReceived;
    private AllCookerDataEx dataSent;
    private boolean successfullySent;

    // Properties
    public boolean isInDebugMode() {
        return inDebugMode;
    }
    public void setInDebugMode(boolean inDebugMode) {
        this.inDebugMode = inDebugMode;
    }
    public byte[] getDataReceived() {
        return dataReceived;
    }
    public void setDataReceived(byte[] dataReceived) {
        this.dataReceived = dataReceived;
    }
    public AllCookerDataEx getDataSent() {
        return dataSent;
    }
    public void setDataSent(AllCookerDataEx dataSent) {
        this.dataSent = dataSent;
    }
    public boolean isSuccessfullySent() {
        return successfullySent;
    }
    public void setSuccessfullySent(boolean successfullySent) {
        this.successfullySent = successfullySent;
    }
}
