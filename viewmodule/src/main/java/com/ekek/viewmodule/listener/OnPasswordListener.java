package com.ekek.viewmodule.listener;

public interface OnPasswordListener {
    boolean onCheckPwd(String pwd);
    void onWarning(String msg);
}
