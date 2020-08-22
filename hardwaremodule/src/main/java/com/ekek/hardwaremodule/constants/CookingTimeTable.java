package com.ekek.hardwaremodule.constants;

public class CookingTimeTable {
    private static final int COOKING_AUTOMATIC_SWITCH_OFF_TIME_FOR_POWER_LEVEL_ONE_TO_THREE = 360;// minute, 档位1到3档，自动关火花时间为360分钟

    private static final int COOKING_AUTOMATIC_SWITCH_OFF_TIME_FOR_POWER_LEVEL_FOUR_TO_SIX = 180;// minute, 档位4-6，自动关火花时间为180分钟

    private static final int COOKING_AUTOMATIC_SWITCH_OFF_TIME_FOR_POWER_LEVEL_SEVEN_TO_EIGHT = 120;// minute, 档位7-8，自动关火花时间为120分钟

    private static final int COOKING_AUTOMATIC_SWITCH_OFF_TIME_FOR_POWER_LEVEL_NIGHT = 90;// minute ,档位9，自动关火花时间为90分钟

    private static final int COOKING_AUTOMATIC_SWITCH_OFF_TIME_FOR_20_TO_65_DEGREE = 48 * 60;// minute, 温度：20到65度自动关机时间为48小时

    private static final int COOKING_AUTOMATIC_SWITCH_OFF_TIME_FOR_66_TO_85_DEGREE = 36 * 60;// minute, 温度：66到85度自动关机时间为36小时

    private static final int COOKING_AUTOMATIC_SWITCH_OFF_TIME_FOR_86_TO_90_DEGREE = 10 * 60;// minute, 温度：86到90度自动关机时间为10小时

    private static final int COOKING_AUTOMATIC_SWITCH_OFF_TIME_FOR_91_TO_100_DEGREE = 6 * 60;// minute, 温度：91到100度自动关机时间为6小时

    private static final int COOKING_AUTOMATIC_SWITCH_OFF_TIME_FOR_101_TO_180_DEGREE = 1 * 60;// minute, 温度：101到180度自动关机时间为1小时

    private static final int COOKING_AUTOMATIC_SWITCH_OFF_TIME_FOR_NONE = 0;// minute

    public static int getCookingAutomaticSwitchOffTime(int Level) {
        int time = COOKING_AUTOMATIC_SWITCH_OFF_TIME_FOR_NONE;
        if (Level <= 10) {//档位模式
            if (Level == 0) {
                time = COOKING_AUTOMATIC_SWITCH_OFF_TIME_FOR_NONE;
            }else if (Level <= 3) {//<=3
                time = COOKING_AUTOMATIC_SWITCH_OFF_TIME_FOR_POWER_LEVEL_ONE_TO_THREE;
            }else if (Level <= 6) { // 3 < level <= 6
                time = COOKING_AUTOMATIC_SWITCH_OFF_TIME_FOR_POWER_LEVEL_FOUR_TO_SIX;
            }else if (Level <= 8) { // 6 < level <= 8
                time = COOKING_AUTOMATIC_SWITCH_OFF_TIME_FOR_POWER_LEVEL_SEVEN_TO_EIGHT;
            }else if (Level <= 10) {// 8 < level <= 10 : 9和B档
                time = COOKING_AUTOMATIC_SWITCH_OFF_TIME_FOR_POWER_LEVEL_NIGHT;
            }

        }else {//温控模式
            if (Level <= 65) { // 20 <= level <= 65度
                time = COOKING_AUTOMATIC_SWITCH_OFF_TIME_FOR_20_TO_65_DEGREE;
            }else if (Level <= 85) {// 65 <= level <= 85度
                time = COOKING_AUTOMATIC_SWITCH_OFF_TIME_FOR_66_TO_85_DEGREE;
            }else if (Level <= 90) {// 86 <= level <= 90度
                time = COOKING_AUTOMATIC_SWITCH_OFF_TIME_FOR_86_TO_90_DEGREE;
            }else if (Level <= 100) {// 91 <= level <= 100度
                time = COOKING_AUTOMATIC_SWITCH_OFF_TIME_FOR_91_TO_100_DEGREE;
            }else if (Level <= 180) {// 101 <= level <= 180度
                time = COOKING_AUTOMATIC_SWITCH_OFF_TIME_FOR_101_TO_180_DEGREE;
            }

        }

        return time;
    }
}
