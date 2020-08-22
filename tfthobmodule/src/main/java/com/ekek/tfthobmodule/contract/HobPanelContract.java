package com.ekek.tfthobmodule.contract;

import com.ekek.commonmodule.base.BasePresenter;
import com.ekek.commonmodule.base.BaseView;
import com.ekek.tfthobmodule.data.CookerSettingData;

public interface HobPanelContract {
    /*
     * 控制view显示
     * */
    interface View extends BaseView<Presenter> {

        void updateHobStatusUI(int id,CookerSettingData data);

        void updateHobStatusUI(int id ,CookerSettingData data,int hour,int minute);

        void updateHobTimerUI(int cookerID , int hour, int minute);

        void updateHobStatusUIForTimeIsUp(int cookerID);

        void updateHobStatusUIForKeepWarm(int cookerID);

        void updateHobStatusUIForUserPrepare(int cookerID,int tempValue,int identifyResourceID);

        void updateHobStatusUIForReadyToCook(int cookerID);

        void updateHobStatusUIForFireBGearTimeIsUp(int cookerID ,CookerSettingData data);

        void updateHobStatusUIForErrorOcurr(int cookerID,String errorMessage);

        void updateHobStatusUIForErrorDimiss(int cookerID);

        void updateHobStatusUIForNoPan(int cookerID);

        void updateHobStatusUIForHighTemp(int cookerID);

        void doFinish();

        boolean isActive();
    }

    /*
     * 控制model处理数据
     * */
    interface Presenter extends BasePresenter {

        /*
        * 获取炉头设置数据
        *
        * */
        CookerSettingData getCookerSettingData(int cookerID);

        CookerSettingData getCookerSettingDataForKeepWarm(int cookerID);

        CookerSettingData getCookerSettingDataForAddTemMinute(int cookerID);

        void setCookerSettingData(CookerSettingData data);

        void stopTimerForPoweroff(int cookerID);

        void requestToCookForUserReady(int cookerID);


        void doFinish();

    }
}
