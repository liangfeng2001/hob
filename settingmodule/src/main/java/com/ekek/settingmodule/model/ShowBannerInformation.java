package com.ekek.settingmodule.model;

public class ShowBannerInformation {



    int order;
    int wParam;
    int lParam;

    public ShowBannerInformation(int order) {
        this(order, 0, 0);
    }

    public ShowBannerInformation(int order, int wParam, int lParam) {
        this.order = order;
        this.wParam = wParam;
        this.lParam = lParam;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getwParam() {
        return wParam;
    }

    public void setwParam(int wParam) {
        this.wParam = wParam;
    }

    public int getlParam() {
        return lParam;
    }

    public void setlParam(int lParam) {
        this.lParam = lParam;
    }
}
