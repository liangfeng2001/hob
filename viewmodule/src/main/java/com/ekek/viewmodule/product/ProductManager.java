package com.ekek.viewmodule.product;

public class ProductManager {

    public static final int CATA_60 = 0;
    public static final int CATA_80 = 1;
    public static final int CATA_90 = 2;
    public static final int Haier = 3;
    public static final int Haier_china = 4;
    // 这里的值改了无效，它的值由 TFTHobConfiguration.TFT_HOB_TYPE 读取而来
    public static int PRODUCT_TYPE = CATA_60;
    // 这里的值改了无效，它的值由 TFTHobConfiguration.IS_TI 读取而来
    public static boolean IS_TI = false;

    public static final int CUSTOMER_CATA=0;
    public static final int CUSTOMER_GUTMANN=1;
    // 增加客户的名称选择功能，不同的客户有不同的主控板程序号
    public static int CUSTOMER_NAME=CUSTOMER_GUTMANN;

    public static final String MODEL_INFO_UNKNOWN = "Unknown";
    public static final String MODEL_INFO_TI_POSTFIX = " TI";
    public static final String MODEL_INFO_CATA60 = "IN5.T 01 MCP.CT 01";
    public static final String MODEL_INFO_CATA60_TI = MODEL_INFO_CATA60 + MODEL_INFO_TI_POSTFIX;
    public static final String MODEL_INFO_CATA80 = "IN5.T 04 MCP.CT 04";
    public static final String MODEL_INFO_CATA80_TI = MODEL_INFO_CATA80 + MODEL_INFO_TI_POSTFIX;
    public static final String MODEL_INFO_CATA90 = "IN5.T 05 MCP.CT 05";
    public static final String MODEL_INFO_CATA90_TI = MODEL_INFO_CATA90 + MODEL_INFO_TI_POSTFIX;

    public static final String MODEL_INFO_GUTMANN60 = "IN5.T 01 MCP.GT 01";
    public static final String MODEL_INFO_GUTMANN60_TI = MODEL_INFO_GUTMANN60 + MODEL_INFO_TI_POSTFIX;

    public static final String MODEL_INFO_GUTMANN80 = "IN5.T 04 MCP.GT 02";
    public static final String MODEL_INFO_GUTMANN80_TI = MODEL_INFO_GUTMANN80 + MODEL_INFO_TI_POSTFIX;

    public static final String MODEL_INFO_GUTMANN90 = "IN5.T 05 MCP.GT 03";
    public static final String MODEL_INFO_GUTMANN90_TI = MODEL_INFO_GUTMANN90 + MODEL_INFO_TI_POSTFIX;

    public static String getModelInfo() {
        String returnValu="";
        switch(CUSTOMER_NAME){
            case CUSTOMER_CATA:
                switch(PRODUCT_TYPE){
                    case CATA_60:
                        returnValu= IS_TI ? MODEL_INFO_CATA60_TI : MODEL_INFO_CATA60;
                        break;
                    case CATA_80:
                        returnValu= IS_TI ? MODEL_INFO_CATA80_TI : MODEL_INFO_CATA80;
                        break;
                    case CATA_90:
                        returnValu= IS_TI ? MODEL_INFO_CATA90_TI : MODEL_INFO_CATA90;
                        break;
                    default:
                        returnValu= MODEL_INFO_UNKNOWN;
                        break;
                }
                break;
            case CUSTOMER_GUTMANN:
                switch (PRODUCT_TYPE) {
                    case CATA_60:
                        returnValu= IS_TI ? MODEL_INFO_GUTMANN60_TI : MODEL_INFO_GUTMANN60;
                        break;
                    case CATA_80:
                        returnValu= IS_TI ? MODEL_INFO_GUTMANN80_TI : MODEL_INFO_GUTMANN80;
                        break;
                    case CATA_90:
                        returnValu= IS_TI ? MODEL_INFO_GUTMANN90_TI : MODEL_INFO_GUTMANN90;
                        break;
                    default:
                        returnValu= MODEL_INFO_UNKNOWN;
                        break;
                }
                break;
            default :
                returnValu= MODEL_INFO_UNKNOWN;
                break;
        }
        return returnValu;
    }
}
