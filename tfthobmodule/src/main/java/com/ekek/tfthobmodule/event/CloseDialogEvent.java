package com.ekek.tfthobmodule.event;

public class CloseDialogEvent {

    public static final int DIALOG_RECIPES_EDIT = 0;
    public static final int SOUND_ACTION_PAUSE = 1;
    int dialogName;

    public CloseDialogEvent(int dialogName) {
        this.dialogName = dialogName;
    }

    public int getDialogName() {
        return dialogName;
    }

    public void setDialogName(int dialogName) {
        this.dialogName = dialogName;
    }
}
