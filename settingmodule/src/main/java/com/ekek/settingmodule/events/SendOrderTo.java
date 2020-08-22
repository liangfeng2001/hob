package com.ekek.settingmodule.events;

public class SendOrderTo {
    private int order=0;
    public SendOrderTo(int d){
        order =d;
    }
    public int getOrder(){
        return order ;
    }
}
