package com.ekek.hardwaremodule.entity;

import com.ekek.commonmodule.utils.LogUtil;

public class PIDModel {
    private int pv;//current value
    private int sv;//setting value
    private float pGain = 0.5f;
    private int iGain;
    private int dGain;
    private int time;
    private int err1,err2;//err1 : 这一次偏差, err2: 上一次偏差
    private int errSum;

    public PIDModel(int sv) {
        this.sv = sv;
    }

    public void updateValue(int value) {
        pv = value;
        err2 = err1;
        err1 = sv - pv;
        errSum = errSum + err1;
        LogUtil.d("err1----->" + err1 + "\r\n" + "errSum---->" + errSum);
        calPid();
    }

    private void calPid() {
        float outputValue = pGain * err1;
        LogUtil.d("outputValue----->" + outputValue);

    }

}
