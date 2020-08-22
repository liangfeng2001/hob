package com.ekek.tfthobmodule.event;

import android.content.Context;

import com.ekek.tfthobmodule.R;

public class ShowErrorEvent {

    public static final int ERROR_UNKNOWN = 0;
    public static final int ERROR_E1 = 1;
    public static final int ERROR_E2 = 2;
    public static final int ERROR_E3 = 3;
    public static final int ERROR_E4 = 4;
    public static final int ERROR_E5 = 5;
    public static final int ERROR_F1 = 6;
    public static final int ERROR_F3 = 7;
    public static final int ERROR_F4 = 8;
    public static final int ERROR_ER03 = 9;
    public static final int ERROR_R1E = 10;
    public static final int ERROR_R2E = 11;
    public static final int ERROR_R3E = 12;
    public static final int ERROR_R4E = 13;
    public static final int ERROR_R5E = 14;
    public static final int ERROR_R6E = 15;
    public static final int ERROR_R7E = 16;

    private int error;
    private int wParam;
    private int lParam;

    public ShowErrorEvent(int error) {
        this.error = error;
    }
    public ShowErrorEvent(String errorCode) {
        switch (errorCode) {
            case "E1":
                error = ERROR_E1;
                break;
            case "E2":
                error = ERROR_E2;
                break;
            case "E3":
                error = ERROR_E3;
                break;
            case "E4":
                error = ERROR_E4;
                break;
            case "E5":
                error = ERROR_E5;
                break;
            case "F1":
                error = ERROR_F1;
                break;
            case "F3":
                error = ERROR_F3;
                break;
            case "F4":
                error = ERROR_F4;
                break;
            default:
                error = ERROR_UNKNOWN;
                break;
        }
    }

    public String getErrorTitle(Context context) {
        switch (error) {
            case ERROR_ER03:
                return context.getResources().getString(R.string.tfthobmodule_err_er03_title);
            case ERROR_E1:
                return context.getResources().getString(R.string.tfthobmodule_err_e1);
            case ERROR_E2:
                return context.getResources().getString(R.string.tfthobmodule_err_e2);
            case ERROR_E3:
                return context.getResources().getString(R.string.tfthobmodule_err_e3);
            case ERROR_E4:
                return context.getResources().getString(R.string.tfthobmodule_err_e4);
            case ERROR_E5:
                return context.getResources().getString(R.string.tfthobmodule_err_e5);
            case ERROR_F1:
                return context.getResources().getString(R.string.tfthobmodule_err_f1);
            case ERROR_F3:
                return context.getResources().getString(R.string.tfthobmodule_err_f3);
            case ERROR_F4:
                return context.getResources().getString(R.string.tfthobmodule_err_f4);
            case ERROR_R1E:
                return context.getResources().getString(R.string.tfthobmodule_err_r1e);
            case ERROR_R2E:
                return context.getResources().getString(R.string.tfthobmodule_err_r2e);
            case ERROR_R3E:
                return context.getResources().getString(R.string.tfthobmodule_err_r3e);
            case ERROR_R4E:
                return context.getResources().getString(R.string.tfthobmodule_err_r4e);
            case ERROR_R5E:
                return context.getResources().getString(R.string.tfthobmodule_err_r5e);
            case ERROR_R6E:
                return context.getResources().getString(R.string.tfthobmodule_err_r6e);
            case ERROR_R7E:
                return context.getResources().getString(R.string.tfthobmodule_err_r7e);
            default:
                return "";
        }
    }
    public String getErrorContent(Context context) {
        switch (error) {
            case ERROR_ER03:
            default:
                return "";
        }
    }
    public int getError() {
        return error;
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
