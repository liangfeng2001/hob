package com.ekek.hardwaremodule.pid;

import com.ekek.commonmodule.utils.LogUtil;

public class PIDData {
    private int sv;//setting value
    private int pv;//current value
    private float kp;//p
    private float ki;//i
    private float kd;//d
    private int error;//current error
    private int lastError;//last error
    private int preError;//last last error
    private int sumError;//error sum
    private int delError;

    private float t,ti,td;

    public PIDData(int settingValue) {
        sv = settingValue;
        init();
    }

    private void init() {
        kp = 70;
        t = 1000;
        ti = 5000;
        td = 2500;
    }

    public int getSv() {
        return sv;
    }

    public void setSv(int sv) {
        this.sv = sv;
    }

    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
        error = sv - pv;
        LogUtil.d("PIDTEST---pv--->" + pv + "---sv---->" + sv);
        sumError += error;
        delError = error - lastError;
        preError = lastError;
        lastError = error;



    }

    public void setSvPv(int sv,int pv) {
        this.sv = sv;
        this.pv = pv;
        error = sv - pv;
        LogUtil.d("PIDTEST---pv--->" + pv + "---sv---->" + sv);
        sumError += error;
        delError = error - lastError;
        preError = lastError;
        lastError = error;



    }

    public int getDelError() {
        return delError;
    }

    public void setDelError(int delError) {
        this.delError = delError;
    }

    public float getKp() {
        return kp;
    }

    public void setKp(float kp) {
        this.kp = kp;
    }

    public float getKi() {
        return ki;
    }

    public void setKi(float ki) {
        this.ki = ki;
    }

    public float getKd() {
        return kd;
    }

    public void setKd(float kd) {
        this.kd = kd;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getLastError() {
        return lastError;
    }

    public void setLastError(int lastError) {
        this.lastError = lastError;
    }

    public int getPreError() {
        return preError;
    }

    public void setPreError(int preError) {
        this.preError = preError;
    }

    public int getSumError() {
        return sumError;
    }

    public void setSumError(int sumError) {
        this.sumError = sumError;
    }

    public float getT() {
        return t;
    }

    public void setT(float t) {
        this.t = t;
    }

    public float getTi() {
        return ti;
    }

    public void setTi(float ti) {
        this.ti = ti;
    }

    public float getTd() {
        return td;
    }

    public void setTd(float td) {
        this.td = td;
    }
}
