package com.ekek.intromodule.hob.contract;

import com.ekek.commonmodule.base.BasePresenter;
import com.ekek.commonmodule.base.BaseView;

public interface HobIntroContract {
    /*
    * 控制view显示
    * */
    interface View extends BaseView<Presenter> {

        void updateHobStatusUI(Integer id);

        void updateHob90StatusUI(boolean firstFlag,boolean secondFlag, boolean thirdFlag,boolean fourthFlag ,boolean fifthFlag);

        void updateHob80StatusUI(boolean firstFlag,boolean secondFlag, boolean thirdFlag,boolean fourthFlag);


        void doFinish(boolean silently);

        boolean isActive();

        boolean isRespondPowerOnSignal();
        boolean isClickToQuit();
        int getTotalPowerOffMode();
        void showBlackMask(boolean visible);
        boolean isShowingBlackMask();
    }

    /*
    * 控制model处理数据
    * */
    interface Presenter extends BasePresenter {

        void doFinish(boolean silently);

    }
}
