package com.ekek.tfthoodmodule.model;

public class GetCookersInformation {
    int a_cooker_is_open;
    int b_cooker_is_open;
    int c_cooker_is_open;
    int d_cooker_is_open;
    int e_cooker_is_open;
    int f_cooker_is_open;
    int hood_value=0;

    boolean a_cooker_is_HighTemp;
    boolean b_cooker_is_HighTemp;
    boolean c_cooker_is_HighTemp;
    boolean d_cooker_is_HighTemp;
    boolean e_cooker_is_HighTemp;
    boolean f_cooker_is_HighTemp;
    boolean abWorking=false;
    boolean efWorking=false;

    public GetCookersInformation(int a_cooker_is_open, int b_cooker_is_open, int c_cooker_is_open, int d_cooker_is_open,
                                 int e_cooker_is_open, int f_cooker_is_open, boolean a_cooker_is_HighTemp,
                                 boolean b_cooker_is_HighTemp, boolean c_cooker_is_HighTemp, boolean d_cooker_is_HighTemp,
                                 boolean e_cooker_is_HighTemp, boolean f_cooker_is_HighTemp,int hood_value,boolean ab_working,boolean ef_working) {
        this.a_cooker_is_open = a_cooker_is_open;
        this.b_cooker_is_open = b_cooker_is_open;
        this.c_cooker_is_open = c_cooker_is_open;
        this.d_cooker_is_open = d_cooker_is_open;
        this.e_cooker_is_open = e_cooker_is_open;
        this.f_cooker_is_open = f_cooker_is_open;
        this.a_cooker_is_HighTemp = a_cooker_is_HighTemp;
        this.b_cooker_is_HighTemp = b_cooker_is_HighTemp;
        this.c_cooker_is_HighTemp = c_cooker_is_HighTemp;
        this.d_cooker_is_HighTemp = d_cooker_is_HighTemp;
        this.e_cooker_is_HighTemp = e_cooker_is_HighTemp;
        this.f_cooker_is_HighTemp = f_cooker_is_HighTemp;
        this.hood_value=hood_value;
        this.abWorking=ab_working;
        this.efWorking=ef_working;
    }



    public boolean isEfWorking() {
        return efWorking;
    }

    public void setEfWorking(boolean efWorking) {
        this.efWorking = efWorking;
    }

    public boolean isAbWorking() {
        return abWorking;
    }

    public void setAbWorking(boolean abWorking) {
        this.abWorking = abWorking;
    }

    public int getHood_value() {
        return hood_value;
    }

    public void setHood_value(int hood_value) {
        this.hood_value = hood_value;
    }

    public int getA_cooker_is_open() {
        return a_cooker_is_open;
    }

    public void setA_cooker_is_open(int a_cooker_is_open) {
        this.a_cooker_is_open = a_cooker_is_open;
    }

    public int getB_cooker_is_open() {
        return b_cooker_is_open;
    }

    public void setB_cooker_is_open(int b_cooker_is_open) {
        this.b_cooker_is_open = b_cooker_is_open;
    }

    public int getC_cooker_is_open() {
        return c_cooker_is_open;
    }

    public void setC_cooker_is_open(int c_cooker_is_open) {
        this.c_cooker_is_open = c_cooker_is_open;
    }

    public int getD_cooker_is_open() {
        return d_cooker_is_open;
    }

    public void setD_cooker_is_open(int d_cooker_is_open) {
        this.d_cooker_is_open = d_cooker_is_open;
    }

    public int getE_cooker_is_open() {
        return e_cooker_is_open;
    }

    public void setE_cooker_is_open(int e_cooker_is_open) {
        this.e_cooker_is_open = e_cooker_is_open;
    }

    public int getF_cooker_is_open() {
        return f_cooker_is_open;
    }

    public void setF_cooker_is_open(int f_cooker_is_open) {
        this.f_cooker_is_open = f_cooker_is_open;
    }

    public boolean isA_cooker_is_HighTemp() {
        return a_cooker_is_HighTemp;
    }

    public void setA_cooker_is_HighTemp(boolean a_cooker_is_HighTemp) {
        this.a_cooker_is_HighTemp = a_cooker_is_HighTemp;
    }

    public boolean isB_cooker_is_HighTemp() {
        return b_cooker_is_HighTemp;
    }

    public void setB_cooker_is_HighTemp(boolean b_cooker_is_HighTemp) {
        this.b_cooker_is_HighTemp = b_cooker_is_HighTemp;
    }

    public boolean isC_cooker_is_HighTemp() {
        return c_cooker_is_HighTemp;
    }

    public void setC_cooker_is_HighTemp(boolean c_cooker_is_HighTemp) {
        this.c_cooker_is_HighTemp = c_cooker_is_HighTemp;
    }

    public boolean isD_cooker_is_HighTemp() {
        return d_cooker_is_HighTemp;
    }

    public void setD_cooker_is_HighTemp(boolean d_cooker_is_HighTemp) {
        this.d_cooker_is_HighTemp = d_cooker_is_HighTemp;
    }

    public boolean isE_cooker_is_HighTemp() {
        return e_cooker_is_HighTemp;
    }

    public void setE_cooker_is_HighTemp(boolean e_cooker_is_HighTemp) {
        this.e_cooker_is_HighTemp = e_cooker_is_HighTemp;
    }

    public boolean isF_cooker_is_HighTemp() {
        return f_cooker_is_HighTemp;
    }

    public void setF_cooker_is_HighTemp(boolean f_cooker_is_HighTemp) {
        this.f_cooker_is_HighTemp = f_cooker_is_HighTemp;
    }
}
