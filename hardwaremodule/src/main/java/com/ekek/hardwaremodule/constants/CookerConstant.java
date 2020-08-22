package com.ekek.hardwaremodule.constants;

public class CookerConstant {
    /**
     * 电磁炉类型： KSO 60
     */
    public static final int COOKER_TYPE_KSO_60 = 0;
    /**
     * 电磁炉类型： KSO 80
     */
    public static final int COOKER_TYPE_KSO_80 = 1;
    /**
     * 电磁炉类型： KSO 90
     */
    public static final int COOKER_TYPE_KSO_90 = 2;
    /**
     * 电磁炉类型： KSO AS
     */
    public static final int COOKER_TYPE_KSO_AS = 3;

    /**
     * 电磁炉类型： CATA 60
     */
    public static final int COOKER_TYPE_CATA_60 = 4;

    /**
     * 电磁炉类型： CATA 80
     */
    public static final int COOKER_TYPE_CATA_80 = 5;

    /**
     * 电磁炉类型： CATA 90
     */
    public static final int COOKER_TYPE_CATA_90 = 6;


    public static int COOKER_TYPE_CURRENT_TYPE = COOKER_TYPE_CATA_60;

    /**
     * 炉头编号：A炉
     */
    public static final int COOKER_ID_A_COOKDER = 1;
    /**
     * 炉头编号：B炉
     */
    public static final int COOKER_ID_B_COOKDER = 2;
    /**
     * 炉头编号：AB炉
     */
    public static final int COOKER_ID_AB_COOKDER = 12;
    /**
     * 炉头编号：C炉
     */
    public static final int COOKER_ID_C_COOKDER = 3;
    /**
     * 炉头编号：D炉
     */
    public static final int COOKER_ID_D_COOKDER = 4;
    /**
     * 炉头编号：E炉
     */
    public static final int COOKER_ID_E_COOKDER = 5;
    /**
     * 炉头编号：F炉
     */
    public static final int COOKER_ID_F_COOKDER = 6;
    /**
     * 炉头编号：EF炉
     */
    public static final int COOKER_ID_EF_COOKDER = 65;

    public static final int COOKER_WORK_MODE_POWER_OFF = 0;
    public static final int COOKER_WORK_MODE_FIRE_GEAR = 1;
    public static final int COOKER_WORK_MODE_SETTING = 4;
    public static final int COOKER_WORK_MODE_UNION = 5;
    public static final int COOKER_WORK_MODE_MATCH_HOOD = 0x1E;
    public static final int COOKER_WORK_MODE_ASK_FOR_SLEEP = 0xE1;

    public static final int REQUEST_SEND_COOKER_DATA_RESULT_SUCCESS = 0;

    public static final int REQUEST_SEND_COOKER_DATA_RESULT_FAIL = 1;
}
