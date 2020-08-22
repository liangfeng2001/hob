package com.ekek.commonmodule.base;

import solid.ren.skinlibrary.base.SkinBaseApplication;

public class BaseApplication extends SkinBaseApplication
{

   /* private static BaseApplication instance;   // SkinBaseApplication

    public static BaseApplication getInstance() {
        return instance;
    }*/
    @Override
    public void onCreate() {
        super.onCreate();
        //instance = this;
       // MultiDex.install(this);
    }

}
