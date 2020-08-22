package com.ekek.commonmodule.base;

/**
 * des:baseModel
 * Created by xsf
 * on 2016.08.14:50
 */
public interface BaseModel {
    /**
     * Model初始化
     * */
    void start();
    /**
     * Model结束时回收资源
     * */
    void stop();
}
