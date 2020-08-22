package com.ekek.tfthobmodule.model;

import com.ekek.commonmodule.base.BaseModel;
import com.ekek.commonmodule.base.BaseModelCallback;

public interface HobCookerSettingModel extends BaseModel {

    interface HobCookerSettingModelCallback extends BaseModelCallback {
        /**
         * HobIntroModel通知UI更新锅的状态显示
         * */
        void onHobStatusChange(int status);
        /**
         * HobIntroModel通知电磁炉开机
         * */
        void onHobPowerOn();
    }

}
