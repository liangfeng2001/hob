package com.ekek.tfthobmodule.constants;

import com.ekek.tfthobmodule.R;

/**
 * Created by Samhung on 2017/12/27.
 */

public class TFTHobConstant {
   //public static final int COOKER_TYPE_BIG_COOKER = 0;//A
  //  public static final int COOKER_TYPE_SMALL_UP_COOKER = 1;//B
   // public static final int COOKER_TYPE_SMALL_DOWN_COOKER = 2;//C

/*    public static final int COOKER_TYPE_A_COOKER = 0;
    public static final int COOKER_TYPE_B_COOKER = 1;
    public static final int COOKER_TYPE_C_COOKER = 2;
    public static final int COOKER_TYPE_D_COOKER = 3;
    public static final int COOKER_TYPE_E_COOKER = 4;
    public static final int COOKER_TYPE_F_COOKER = 5;*/
    public static final int COOKER_TYPE_A_COOKER = 1;
    public static final int COOKER_TYPE_B_COOKER = 2;
    public static final int COOKER_TYPE_AB_COOKER = 12;
    public static final int COOKER_TYPE_C_COOKER = 3;
    public static final int COOKER_TYPE_D_COOKER = 4;
    public static final int COOKER_TYPE_CD_COOKER = 34;
    public static final int COOKER_TYPE_E_COOKER = 5;
    public static final int COOKER_TYPE_F_COOKER = 6;
    public static final int COOKER_TYPE_EF_COOKER = 65;

    public static final int TEMP_CONTROL_MODE_FAST = 0;
    public static final int TEMP_CONTROL_MODE_PRECISE = 1;

    public static final int COOKER_MODE_FIRE = 0;
    public static final int COOKER_MODE_TEMP = 1;

    public static final String[] COOKER_FIRE_GEAR_LIST = {"B","9","8","7","6","5","4","3","2","1","0"};
    public static int COOKER_DEFAULT_FIRE_GEAR = 5;
    public static int COOKER_BOOST_FIRE_GEAR = 0;

    public static final int COOKER_FAST_TEMP_MAX = 100;
    public static final int COOKER_FAST_TEMP_MIN = 40;
    public static final int COOKER_FAST_TEMP_STEP = 10;
    public static final int[] COOKER_FAST_TEMP_VALUE_LIST = {100, 90, 80, 70, 60, 50, 40};
    public static final String[] COOKER_FAST_TEMP_LIST = {"100°","90°","80°","70°","60°","50°","40°"};
    public static final int COOKER_DEFAULT_FAST_TEMP_GEAR = 3;

    public static final int COOKER_PRECISE_TEMP_MAX = 180;
    public static final int COOKER_PRECISE_TEMP_MIN = 40;
    public static final int COOKER_PRECISE_TEMP_LEN = COOKER_PRECISE_TEMP_MAX - COOKER_PRECISE_TEMP_MIN + 1;
    public static final int COOKER_DEFAULT_PRECISE_TEMP_GEAR = COOKER_PRECISE_TEMP_MAX - 85;

    public final static String DATABASE_NAME = "inductioncooker.db";

    public final static String DATABASE_NAME_ENCRYPTED = "inductioncooker.db.encrypted";

    public static final boolean DATABASE_ENCRYPTED = false;

    public static final String DEFAULT_THEME = "skin_black.skin";
    public static final String DEFAULT_BRIGHTNESS = "-1";
    public static final String DEFAULT_LANGUAGE = "English(United States)";

    public static final String SOUND_SWITCH_STATUS_CLOSE = "CLOSE";
    public static final String SOUND_SWITCH_STATUS_OPEN = "OPEN";
    public static final String DEFAULT_SOUND_SWITCH_STATUS = SOUND_SWITCH_STATUS_OPEN;

    public static final String DEFAULT_SOUND_LEVEL = "0";

    public static final String POWER_LIMIT_LOW = "Low";
    public static final String POWER_LIMIT_MEDIUM = "Medium";
    public static final String POWER_LIMIT_HIGH = "High";
    public static final String DEFAULT_POWER_LIMIT = POWER_LIMIT_MEDIUM;

    public static final String HOOD_MODE_MANUAL = "MANUAL";
    public static final String HOOD_MODE_AUTOMATIC = "AUTOMATIC";
    public static final String DEFAULT_HOOD_MODE = HOOD_MODE_MANUAL;

    public static final String HOOD_LEVEL_LOW = "Low";
    public static final String HOOD_LEVEL_MEDIUM = "Medium";
    public static final String HOOD_LEVEL_HIGH = "High";
    public static final String DEFAULT_HOOD_LEVEL = HOOD_LEVEL_MEDIUM;

    public static final String SECURITY_MODE_PIN = "PIN";
    public static final String SECURITY_MODE_PATTERN = "PATTERN";
    public static final String SECURITY_MODE_PRESS_UNLOCK = "PRESS_UNLOCK";
    public static final String DEFAULT_SECURITY_MODE = SECURITY_MODE_PRESS_UNLOCK;

    public static final int NO_TOUCH_TIME = 1000 * 60 * 2;

    public final static long MILLI_SECOND_IN_FUTURE = 365 * 24 * 60 * 60 * 1000;

    public final static long COUNT_DOWN_INTERVAL = 1000;

    public final static int[][] TEMP_INDENTIFY_LIST = {
            {40, R.mipmap.temp_identification_melting},
            {70, R.mipmap.temp_identification_keep_warm},
            {80, R.mipmap.temp_identification_slow_cock},
            {90, R.mipmap.temp_identification_simmering},
            {100, R.mipmap.temp_identification_boiling},
            {120, R.mipmap.temp_identification_frying},
    };

    public final static int[][] TEMP_INDENTIFY_STRING_LIST = {
            {40, R.string.tfthobmodule_title_melting},
            {70, R.string.tfthobmodule_title_keep_warm},
            {80, R.string.tfthobmodule_title_slow_cook},
            {90, R.string.tfthobmodule_title_simering},
            {100, R.string.tfthobmodule_title_boiling},
    };

    /*
    * hob setting mode
    * */
    public final static int HOB_SETTING_MODE_SET_FIRE_GEAR = 0;
    public final static int HOB_SETTING_MODE_SET_FIRE_GEAR_WITH_TIMER = 1;
    public final static int HOB_SETTING_MODE_SET_TEMP_CHOOSE_TEMP_MODE = 2;
    public final static int HOB_SETTING_MODE_SET_FAST_TEMP_GEAR = 3;
    public final static int HOB_SETTING_MODE_SET_FAST_TEMP_GEAR_WITH_TIMER = 4;
    public final static int HOB_SETTING_MODE_SET_PRECISE_TEMP_GEAR = 5;
    public final static int HOB_SETTING_MODE_SET_PRECISE_TEMP_GEAR_WITH_TIMER = 6;
    public final static int HOB_SETTING_MODE_SET_TIMER = 7;
    public final static int HOB_SETTING_MODE_DEFAULT_MODE = HOB_SETTING_MODE_SET_FIRE_GEAR;

    /*
    * hob work mode
    * */
/*    public final static int HOB_WORK_MODE_POWER_OFF = 0;
    public final static int HOB_WORK_MODE_FIRE_GEAR = 1;
    public final static int HOB_WORK_MODE_FIRE_GEAR_WITH_TIMER = 2;
    public final static int HOB_WORK_MODE_FAST_TEMP_GEAR = 3;
    public final static int HOB_WORK_MODE_FAST_TEMP_GEAR_WITH_TIMER = 4;
    public final static int HOB_WORK_MODE_PRECISE_TEMP_GEAR = 5;
    public final static int HOB_WORK_MODE_PRECISE_TEMP_GEAR_WITH_TIMER = 6;
    public final static int HOB_WORK_MODE_ERROR_OCURR = 7;
    public final static int HOB_WORK_MODE_STATUS_ABNORMAL_NO_PAN = 8;
    public final static int HOB_WORK_MODE_STATUS_ABNORMAL_HIGH_TEMP = 9;
    public final static int HOB_WORK_MODE_PAUSE = 10;
    public final static int HOB_WORK_MODE_WAIT_USER_CONFIRM = 11;
    public final static int HOB_WORK_MODE_WORK_ON_BACKGROUND = 12;
    public final static int HOB_WORK_MODE_PREPARE_TEMP_SENSOR = 13;
    public final static int HOB_WORK_MODE_TEMP_SENSOR_READY = 14;
    public final static int HOB_WORK_MODE_TEMP_WORK_FINISH_WAIT_USER_CONFIRM = 15;*/

    public final static int HOB_WORK_MODE_POWER_OFF = 0;
    public final static int HOB_WORK_MODE_FIRE_GEAR = 1;
    public final static int HOB_WORK_MODE_FIRE_GEAR_WITH_TIMER = 2;
    public final static int HOB_WORK_MODE_TEMP_GEAR = 3;
    public final static int HOB_WORK_MODE_TEMP_GEAR_WITH_TIMER = 4;
    public final static int HOB_WORK_MODE_TEMP_INDICATOR_GEAR = 5;
    public final static int HOB_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER = 6;
    public final static int HOB_WORK_MODE_ERROR_OCURR = 7;
    public final static int HOB_WORK_MODE_STATUS_ABNORMAL_NO_PAN = 8;
    public final static int HOB_WORK_MODE_STATUS_ABNORMAL_HIGH_TEMP = 9;
    public final static int HOB_WORK_MODE_PAUSE = 10;
    public final static int HOB_WORK_MODE_WAIT_USER_CONFIRM = 11;
    public final static int HOB_WORK_MODE_WORK_ON_BACKGROUND = 12;
    public final static int HOB_WORK_MODE_PREPARE_TEMP_SENSOR = 13;
    public final static int HOB_WORK_MODE_TEMP_SENSOR_READY = 14;
    public final static int HOB_WORK_MODE_TEMP_WORK_FINISH_WAIT_USER_CONFIRM = 15;

    /*
    *
    * update hob view mode
    * */
    public static final int HOB_VIEW_WORK_MODE_POWER_OFF = 0;
    public static final int HOB_VIEW_WORK_MODE_FIRE_GEAR_WITHOUT_TIMER = 100;
    public static final int HOB_VIEW_WORK_MODE_FIRE_GEAR_WITH_TIMER = 200;
    public static final int HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITHOUT_TIMER = 300;
    public static final int HOB_VIEW_WORK_MODE_TEMP_GEAR_WITHOUT_TIMER = 400;
    public static final int HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER = 500;
    public static final int HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER_AND_RECIPES_FIRST = 501;//先显示菜谱图片，再显示温度定时
    public static final int HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER_AND_RECIPES_SECOND = 502;//先显示温度定时，再显示菜谱图片
    public static final int HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER = 600;
    public static final int HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_FIRST = 601;//先显示菜谱图片，再显示温度定时
    public static final int HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_SECOND = 602;//先显示温度定时，再显示菜谱图片
    public static final int HOB_VIEW_WORK_MODE_ABNORMAL_NO_PAN = 700;
    public static final int HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP = 701;
    public static final int HOB_VIEW_WORK_MODE_ABNORMAL_ERROR_OCURR = 702;
    public static final int HOB_VIEW_WORK_MODE_UPATE_TIMER = 800;
    public static final int HOB_VIEW_WORK_MODE_PAUSE = 900;
    public static final int HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR = 1000;
    public static final int HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR_WITH_INDICATOR = 1001;
    public static final int HOB_VIEW_WORK_MODE_TEMP_SENSOR_READY = 1002;
    public static final int HOB_VIEW_WORK_MODE_TEMP_WORK_FINISH_WAIT_USER_CONFIRM= 1003;


    /*
    * temp mode
    * */
    public static final int TEMP_MODE_NONE = 1;
    public static final int TEMP_MODE_FAST_TEMP = 2;
    public static final int TEMP_MODE_PRECISE_TEMP = 3;
    public static final int TEMP_MODE_FAST_TEMP_GEAR_WITHOUT_SENSOR = 5;

    /*
    * Toast
    * */
    public static final int TOAST_PRIORITY_HIGH = 0;
    public static final int TOAST_PRIORITY_LOW = 1;

    //---------------test cookware-----------------
    public static final int cooker_power_on=1;
    public static final int cooker_power_off=0;


     //Bluetooth options
    /**
    * 关闭蓝牙
    * */
    public static final int BLUETOOTH_OPTIONS_DISABLE= 0;
    /**
     * 打开蓝牙温度计连接模式
     * */
    public static final int BLUETOOTH_OPTIONS_TEMPERTURE_SENSOR = 1;
    /**
     * 打开连接手机模式
     * */
    public static final int BLUETOOTH_OPTIONS_SMART_PHONE = 2;



}
