package com.ekek.hardwaremodule.protocol;

/**
 * Created by Samhung on 2018/1/9.
 */

public class CookerErrorCode {
    public static final int COOKER_ERROR_NORMAL = 0x00;

    public static final int COOKER_ERROR_CODE_LJ_ERR = 0x01;

    public static final int COOKER_ERROR_CODE_V_H = 0x02;

    public static final int COOKER_ERROR_CODE_V_L = 0x04;

    public static final int COOKER_ERROR_CODE_IGBT_TH = 0x08;

    public static final int COOKER_ERROR_CODE_GUO_TH = 0x10;

    public static final int COOKER_ERROR_CODE_IGBT_E = 0x20;

    public static final int COOKER_ERROR_CODE_GUO_E = 0x40;

    public static final int COOKER_ERROR_CODE_F_T45M = 0x80;

    public static final int COOKER_ERROR_CODE_NO_COOKER = 0x11;

    public static final int COOKER_ERROR_CODE_HIGH_TEMP = 0x22;

    public static String getErrorMessage(int code) {
        String message = "";

        if((code & COOKER_ERROR_CODE_LJ_ERR)==COOKER_ERROR_CODE_LJ_ERR){
            message = "F1";
        }else if((code & COOKER_ERROR_CODE_V_H)==COOKER_ERROR_CODE_V_H){
            message = "E3";
        }else if((code & COOKER_ERROR_CODE_V_L)==COOKER_ERROR_CODE_V_L){
            message = "E4";
        }else if((code & COOKER_ERROR_CODE_IGBT_TH)==COOKER_ERROR_CODE_IGBT_TH){
            message = "E2";
        }else if((code & COOKER_ERROR_CODE_GUO_TH)==COOKER_ERROR_CODE_GUO_TH){
            message = "E1";
        }else if((code & COOKER_ERROR_CODE_IGBT_E)==COOKER_ERROR_CODE_IGBT_E){
            message = "F4";
        }else if((code & COOKER_ERROR_CODE_GUO_E)==COOKER_ERROR_CODE_GUO_E){
            message = "F3";
        }else if((code & COOKER_ERROR_CODE_F_T45M)==COOKER_ERROR_CODE_F_T45M){
            message = "E5";
        }

     /*   switch (code) {
            case COOKER_ERROR_NORMAL:
                message = "";
                break;
            case COOKER_ERROR_CODE_LJ_ERR:
                message = "F1";
                break;
            case COOKER_ERROR_CODE_V_H:
                message = "E3";
                break;
            case COOKER_ERROR_CODE_V_L:
                message = "E4";
                break;
            case COOKER_ERROR_CODE_IGBT_TH:
                message = "E2";
                break;
            case COOKER_ERROR_CODE_GUO_TH:
                message = "E1";
            break;
            case COOKER_ERROR_CODE_IGBT_E:
                message = "F4";
                break;
            case COOKER_ERROR_CODE_GUO_E:
                message = "F3";
                break;
            case COOKER_ERROR_CODE_F_T45M:
                message = "E5";
                break;
            case COOKER_ERROR_CODE_NO_COOKER:
                message = "U";
                break;
            case COOKER_ERROR_CODE_HIGH_TEMP:
                message = "H";
                break;
        }*/

        return message;
    }
}
