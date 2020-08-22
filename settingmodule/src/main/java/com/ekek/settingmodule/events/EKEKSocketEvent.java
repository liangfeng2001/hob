package com.ekek.settingmodule.events;

import java.util.ArrayList;
import java.util.List;

public class EKEKSocketEvent {
    private List<String> commandList = new ArrayList<>();

    public EKEKSocketEvent(List<String> list) {
        this.commandList = list;
    }

    public List<String> getCommandList() {
        return commandList;
    }

    public void setCommandList(List<String> commandList) {
        this.commandList = commandList;
    }
}
