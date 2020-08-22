package com.ekek.tfthobmodule.constants;

public class TFTHobConfiguration {
    //电磁炉类型: 60,80,90
    public static final int TFT_HOB_TYPE_60 = 0;
    public static final int TFT_HOB_TYPE_80 = 1;
    public static final int TFT_HOB_TYPE_90 = 2;
    public static final int TFT_HOB_TYPE = TFT_HOB_TYPE_80;
    public static boolean IS_TI = true;   // true 表示 贴合屏幕 false 表示 分离屏幕

    //电磁炉ID：
    //public static final int THF_HOB_ID_0 = 0;
    public static final int THF_HOB_ID_1 = 1;
    public static final int THF_HOB_ID_2 = 2;
    public static final int THF_HOB_ID_3 = 3;
    public static final int THF_HOB_ID_4 = 4;
    public static final int THF_HOB_ID_5 = 5;
    public static final int THF_HOB_ID_6 = 6;
    public static final int THF_HOB_ID_7 = 7;

    public static final int HOB_AGEING_TIME = 60;//60 * 90 ;//90分钟

    /**
     * demo模式，true：生效，就是不会和单片机通讯（包括收和发数据）；false：炉头正常工作，和单片机正常通讯
     *
     * */
    public static final boolean DEMO_MODE_ENABLE = false;


}
