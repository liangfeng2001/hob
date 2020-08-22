package com.ekek.intromodule.splash.presenter;

import android.support.annotation.NonNull;

import com.ekek.intromodule.splash.contract.SplashContract;

import static com.google.common.base.Preconditions.checkNotNull;

public class SplashPresenter implements SplashContract.Presenter {

    @NonNull
    private final SplashContract.View mView;


    public SplashPresenter(@NonNull SplashContract.View view) {

        mView = checkNotNull(view, "view cannot be null");
        //mModel = new HobIntroModel(this);

        mView.setPresenter(this);
    }

    //初始化
    @Override
    public void start() {
        mView.startSplash();
    }
    //结束时回收资源
    @Override
    public void stop() {

    }

    @Override
    public void startSplash() {
        mView.startSplash();
    }
}
