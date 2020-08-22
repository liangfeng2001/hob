package com.ekek.tfthobmodule.contract;

import com.ekek.commonmodule.base.BasePresenter;
import com.ekek.commonmodule.base.BaseView;

public interface HobCookerSettingContract {

    /*
     * 控制view显示
     * */
    interface View extends BaseView<HobCookerSettingContract.Presenter> {

        void doFinish();

        boolean isActive();
    }

    /*
     * 控制model处理数据
     * */
    interface Presenter extends BasePresenter {

        void doFinish();

    }
}
