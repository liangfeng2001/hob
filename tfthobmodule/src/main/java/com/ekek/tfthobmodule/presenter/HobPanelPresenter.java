package com.ekek.tfthobmodule.presenter;

import android.support.annotation.NonNull;

import com.ekek.tfthobmodule.contract.HobPanelContract;
import com.ekek.tfthobmodule.data.CookerSettingData;
import com.ekek.tfthobmodule.model.HobPanelModelImpl;

import static com.google.common.base.Preconditions.checkNotNull;

public class HobPanelPresenter implements HobPanelContract.Presenter ,HobPanelModelImpl.HobPanelModelCallback{
    @NonNull
    private final HobPanelContract.View mView;
    @NonNull
    private final HobPanelModelImpl mModel;

    public HobPanelPresenter(@NonNull HobPanelContract.View view) {

        mView = checkNotNull(view, "view cannot be null");
        mModel = new HobPanelModelImpl(this);

        mView.setPresenter(this);
    }

    @Override
    public CookerSettingData getCookerSettingData(int cookerID) {

        return mModel.getCookerSettingData(cookerID);
    }

    @Override
    public CookerSettingData getCookerSettingDataForKeepWarm(int cookerID) {
        return mModel.getCookerSettingDataForKeepWarm(cookerID);
    }

    @Override
    public CookerSettingData getCookerSettingDataForAddTemMinute(int cookerID) {
        return mModel.getCookerSettingDataForAddTemMinute(cookerID);
    }

    @Override
    public void setCookerSettingData(CookerSettingData data) {

        mModel.setCookerSettingData(data);

    }

    @Override
    public void stopTimerForPoweroff(int cookerID) {
        mModel.stopTimerForPoweroff(cookerID);
    }

    @Override
    public void requestToCookForUserReady(int cookerID) {
        mModel.requestToCook(cookerID);
    }

    @Override
    public void doFinish() {

    }

    @Override
    public void start() {
        mModel.start();
    }

    @Override
    public void stop() {
        mModel.stop();
    }

    @Override
    public void onHobStatusChange(int status) {

    }

    @Override
    public void onHobPowerOn() {

    }

    @Override
    public void onUpdateTimer(int cookerID,int hour, int minute) {
        //LogUtil.d("HobPanelPresenter onUpateTimer");
        mView.updateHobTimerUI(cookerID,hour,minute);
    }

    @Override
    public void onTimeIsUp(int cookerID) {
        mView.updateHobStatusUIForTimeIsUp(cookerID);
    }

    @Override
    public void notifyKeepWarmForTimeIsUp(int cookerID) {
        mView.updateHobStatusUIForKeepWarm(cookerID);
    }

    @Override
    public void notifyUserPrepareToCook(int cookerID,int tempValue,int identifyResourceID) {
        mView.updateHobStatusUIForUserPrepare(cookerID,tempValue,identifyResourceID);
    }

    @Override
    public void notifyUserReadyToCook(int cookerID) {
        mView.updateHobStatusUIForReadyToCook(cookerID);
    }

    @Override
    public void nitifyStartCook(CookerSettingData data) {
        mView.updateHobStatusUI(data.getCookerID(),data);
    }

    @Override
    public void notifyRecoverToCook(int cookerID,CookerSettingData data, int hour, int minute) {
        mView.updateHobStatusUI(cookerID,data,hour,minute);
    }

    @Override
    public void onFireBGearTimeIsUp(int cookerID,CookerSettingData data) {
        mView.updateHobStatusUIForFireBGearTimeIsUp(cookerID,data);
    }

    @Override
    public void onCookerErrorOcurr(int cookerID, String errorMessage) {
        mView.updateHobStatusUIForErrorOcurr(cookerID,errorMessage);
    }

    @Override
    public void onCookerErrorDimiss(int cookerID) {
        mView.updateHobStatusUIForErrorDimiss(cookerID);
    }

    @Override
    public void onCookerHighTemp(int cookerID) {
        mView.updateHobStatusUIForHighTemp(cookerID);
    }

    @Override
    public void onCookerNoPan(int cookerID) {
        mView.updateHobStatusUIForNoPan(cookerID);
    }
}
