package com.ekek.intromodule.splash.contract;

import com.ekek.commonmodule.base.BasePresenter;
import com.ekek.commonmodule.base.BaseView;

public interface SplashContract {
    /*
    * 控制view显示
    * */
    interface View extends BaseView<Presenter> {

        void startSplash();

        boolean isActive();
    }

    /*
    * 控制model处理数据
    * */
    interface Presenter extends BasePresenter {

        void startSplash();

    }
}
