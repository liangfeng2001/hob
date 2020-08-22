package com.ekek.intromodule.hob.model;

import com.ekek.commonmodule.base.BaseModel;
import com.ekek.commonmodule.base.BaseModelCallback;

public interface HobIntroModel extends BaseModel {

    interface HobIntroModelCallback extends BaseModelCallback {
        /**
         * HobIntroModel通知UI更新锅的状态显示
         * */
        void onHobStatusChange(Integer highTempIndicatorResID);

        void onHob90StatusChange(boolean firstFlag, boolean secondFlag, boolean thirdFlag, boolean fourthFlag, boolean fifthFlag);

        void onHob80StatusChange(boolean firstFlag, boolean secondFlag, boolean thirdFlag, boolean fourthFlag);


        void onHob80StatusChange();

        /**
         * HobIntroModel通知电磁炉开机
         * */
        void onHobPowerOn();
        void onHobPowerOff();
    }

}
