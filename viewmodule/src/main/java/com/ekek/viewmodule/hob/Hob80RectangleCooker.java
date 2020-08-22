package com.ekek.viewmodule.hob;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.ekek.commonmodule.utils.ImageUtil;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.viewmodule.R;
import com.ekek.viewmodule.contract.HobRectangleCookerContract;

public class Hob80RectangleCooker extends BaseRectangleCookerView implements HobRectangleCookerContract{
    /************need config here for every hob type*************/
    private static final int HOB_RETANGLE_COOKER_SIZE_WIDTH = 262 + 60;//259 + 80
    private static final int HOB_RETANGLE_COOKER_SIZE_HEIGHT = 546 + 0;//540 + 50
    private static final int POWER_OFF_UNION_BG_BITMAP_ID = R.mipmap.bg_hob_80_rectangle_union_cooker_power_off;
    private static final int POWER_OFF_SEPERATE_BG_BITMAP_ID = R.mipmap.bg_hob_80_rectangle_seperate_cooker_power_off;
    private static final int POWER_ON_SEPERATE_FIRE_WITHOUT_TIMER_BG_BITMAP_ID = R.mipmap.bg_hob_80_rectangle_seperate_cooker_fire_gear_without_timer;
    private static final int POWER_ON_SEPERATE_FIRE_WITH_TIMER_BG_BITMAP_ID = R.mipmap.bg_hob_80_rectangle_seperate_cooker_power_on_fire_with_timer;
    private static final int POWER_OFF_BUTTON_BITMAP_ID = R.mipmap.ic_hob_80_power_off_button;
    private static final int POWER_ON_UNION_FIRE_WITHOUT_TIMER_BG_BITMAP_ID = R.mipmap.bg_hob_80_rectangle_union_cooker_power_on_fire_without_timer;
    private static final int POWER_ON_UNION_FIRE_WITH_TIMER_BG_BITMAP_ID = R.mipmap.bg_hob_80_rectangle_union_cooker_power_on_fire_with_timer;
    private static final int POWER_ON_UNION_TEMP_WITHOUT_TIMER_BG_BITMAP_ID = R.mipmap.bg_hob_80_rectangle_union_cooker_power_on_temp_without_timer;
    private static final int POWER_ON_UNION_TEMP_WITH_TIMER_BG_BITMAP_ID = R.mipmap.bg_hob_80_rectangle_union_cooker_power_on_temp_with_timer;
    private static final int POWER_ON_SEPERATE_TEMP_WITHOUT_TIMER_BG_BITMAP_ID = R.mipmap.bg_hob_90_rectangle_seperate_cooker_power_on_temp_without_timer;
    private static final int POWER_ON_SEPERATE_TEMP_WITH_TIMER_BG_BITMAP_ID = R.mipmap.bg_hob_90_rectangle_seperate_cooker_power_on_temp_with_timer;
    private static final int POWER_ON_UNION_ABNORMAL_NO_PAN = R.mipmap.bg_hob_80_rectangle_union_cooker_no_pan;
    private static final int POWER_ON_SEPERATE_ABNORMAL_NO_PAN = R.mipmap.bg_hob_80_rectangle_seperate_cooker_no_pan;
    private static final int POWER_ON_UNION_TEMP_INDICATOR_WITHOUT_TIMER_BG_BITMAP_ID = R.mipmap.bg_hob_80_rectangle_union_cooker_power_on_temp_indicator_without_timer;
    private static final int POWER_ON_UNION_TEMP_INDICATOR_WITH_TIMER_BG_BITMAP_ID = R.mipmap.bg_hob_80_rectangle_union_cooker_power_on_temp_indicator_with_timer;
    private static final int POWER_ON_SEPERATE_TEMP_INDICATOR_WITHOUT_TIMER_BG_BITMAP_ID = R.mipmap.bg_hob_80_rectangle_seperate_cooker_power_on_temp_indicator_without_timer;
    private static final int POWER_ON_SEPERATE_TEMP_INDICATOR_WITH_TIMER_BG_BITMAP_ID = R.mipmap.bg_hob_80_rectangle_seperate_cooker_power_on_temp_indicator_with_timer;
    private static final int UNION_TEMP_FINISH_LIGHT_BG_BITMAP_ID = R.mipmap.bg_hob_80_union_keep_warm_light;
    private static final int UNION_TEMP_FINISH_DARK_BG_BITMAP_ID = R.mipmap.bg_hob_80_union_keep_warm_dark;
    private static final int SEPERATE_TEMP_FINISH_LIGHT_BG_BITMAP_ID = R.mipmap.bg_hob_80_seperate_keep_warm_light;
    private static final int SEPERATE_TEMP_FINISH_DARK_BG_BITMAP_ID = R.mipmap.bg_hob_80_seperate_keep_warm_dark;
    private static final int UNION_READY_TO_COOK_BG_BITMAP_ID = R.mipmap.bg_hob_80_union_ready_to_cook;
    private static final int SEPERATE_READY_TO_COOK_BG_BITMAP_ID = R.mipmap.bg_hob_80_seperate_ready_to_cook;
    private static final int UNION_PREPARE_TEMP_SENSOR_BG_BITMAP_ID = R.mipmap.bg_hob_80_union_prepare_temp_sensor;
    private static final int UNION_PREPARE_TEMP_SENSOR_WITH_INDICATOR_BG_BITMAP_ID = R.mipmap.bg_hob_80_union_prepare_temp_sensor_with_indicator;
    private static final int SEPERATE_PREPARE_TEMP_SENSOR_BG_BITMAP_ID = R.mipmap.bg_hob_80_seperate_prepare_temp_sensor;
    private static final int SEPERATE_PREPARE_TEMP_SENSOR_WITH_INDICATOR_BG_BITMAP_ID = R.mipmap.bg_hob_80_seperate_prepare_temp_sensor_with_indicator;
    private static final int UNION_HIGH_TEMP_BG_BITMAP_ID = R.mipmap.bg_hob_90_union_high_temp;
    private static final int SEPERATE_HIGH_TEMP_BG_BITMAP_ID = R.mipmap.bg_hob_90_seperate_high_temp;
    private static final int UNION_ERROR_OCCUR_BG_BITMAP_ID = R.mipmap.bg_hob_80_union_cooker_error_occur;
    private static final int SEPERATE_ERROR_OCCUR_BG_BITMAP_ID = R.mipmap.bg_hob_80_seperate_cooker_error_occur;
    private static final int UNION_COOKER_RECIPES_BG_BITMAP_ID = R.mipmap.bg_hob_90_union_cooker_recipes;
    private static final int SEPERATE_COOKER_RECIPES_BG_BITMAP_ID = R.mipmap.bg_hob_90_seperate_cooker_recipes;


    private static final int FONT_SIZE_FOR_SEPERATE_FIRE_GEAR_WITHOUT_TIMER = 100;
    private static final int FONT_SIZE_FOR_SEPERATE_FIRE_GEAR_WITH_TIMER_GEAR_SIZE = 90;
    private static final int FONT_SIZE_FOR_SEPERATE_FIRE_GEAR_WITH_TIMER_TIMER_SIZE = 55;
    private static final int FONT_SIZE_FOR_UNION_FIRE_GEAR_WITHOUT_TIMER = 120;
    private static final int FONT_SIZE_FOR_UNION_FIRE_GEAR_WITH_TIMER_GEAR_SIZE = 120;
    private static final int FONT_SIZE_FOR_UNION_FIRE_GEAR_WITH_TIMER_TIMER_SIZE = 60;
    private static final int FONT_SIZE_FOR_UNION_TEMP_GEAR_WITHOUT_TIMER_BIG = 90;
    private static final int FONT_SIZE_FOR_UNION_TEMP_GEAR_WITHOUT_TIMER_SMALL = 80;
    private static final int FONT_SIZE_FOR_SEPERATE_TEMP_GEAR_WITHOUT_TIMER_TEMP_SIZE_SMALL = 60;
    private static final int FONT_SIZE_FOR_SEPERATE_TEMP_GEAR_WITHOUT_TIMER_TEMP_SIZE_BIG = 90;
    private static final int FONT_SIZE_FOR_UNION_TEMP_GEAR_WITH_TIMER_TEMP_SIZE_SMALL = 60;
    private static final int FONT_SIZE_FOR_UNION_TEMP_GEAR_WITH_TIMER_TEMP_SIZE_BIG = 80;
    private static final int FONT_SIZE_FOR_UNION_TEMP_GEAR_WITH_TIMER_TIMER_SIZE = 50;
    private static final int FONT_SIZE_FOR_SEPERATE_TEMP_GEAR_WITH_TIMER_TEMP_SIZE_SMALL = 60;
    private static final int FONT_SIZE_FOR_SEPERATE_TEMP_GEAR_WITH_TIMER_TEMP_SIZE_BIG = 80;
    private static final int FONT_SIZE_FOR_UNION_ERROR_MESSAGE = 120;
    private static final int FONT_SIZE_FOR_SEPERATE_ERROR_MESSAGE = 90;

    private int upFireGearValue = 5,downFireGearValue = 6,unionFireGearValue = 7;
    private int unionTempGearValue = 170;
    private int unionTempIndicatorResID;
    private int seperateUpTempIndicatorResID,seperateDownTempIndicatorResID;
    private int unionRecipesID = R.mipmap.image_vegetable0,upRecipesID = R.mipmap.image_vegetable0,downRecipesID = R.mipmap.image_vegetable0;
    private String upErrorMessage = "",downErrorMessage = "" ,unionErrorMessage = "";
    private int upTempGearValue = 160;
    private int downTempGearValue = 150;
    private int unionHourValue = 1,unionMinuteValue = 59;
    private int upHourValue = 2,upMinuteValue = 39;
    private int downHourValue = 3,downMinuteValue = 19;
    private Bitmap unionPowerOffBgBp;
    private Bitmap seperatePowerOffBgBp;
    private Bitmap powerOnUnionFireWithoutTimerBpBg;
    private Bitmap powerOnUnionFireWithTimerBpBg;
    private Bitmap powerOffButonBp;
    private Bitmap powerOnSeperateFireWithoutTimerBpBg;
    private Bitmap powerOnSeperateFireWithTimerBpBg;
    private Bitmap powerOnSeperateTempWithoutTimerBpBg;
    private Bitmap powerOnUnionTempWithoutTimerBpBg;
    private Bitmap powerOnUnionTempWithTimerBpBg;
    private Bitmap powerOnSeperateTempWithTimerBpBg;
    private Bitmap powerOnUnionAbnormalNoPanBp;
    private Bitmap powerOnSeperateAbnormalNoPanB;
    private Bitmap powerOnUnionTempIndicatorWithoutTimerBpBg;
    private Bitmap powerOnUnionTempIndicatorWithTimerBpBg;
    private Bitmap powerOnSeperateTempIndicatorWithoutTimerBpBg;
    private Bitmap powerOnSeperateTempIndicatorWithTimerBpBg;
    private Bitmap powerOnUnionTempIndicatorWithoutTimerBg;
    private Bitmap powerOnUnionTempIndicatorWithTimerBg;
    private Bitmap unionTempIndicatorBitmap;
    private Bitmap seperateUpTempIndicatorBitmap;
    private Bitmap seperateDownTempIndicatorBitmap;
    private Bitmap unionTempFinishLightBpBg;
    private Bitmap unionTempFinishDarkBpBg;
    private Bitmap seperateTempFinishLightBpBg;
    private Bitmap seperateempFinishDarkBpBg;
    private Bitmap unionReadyToCookBpBg;
    private Bitmap seperateReadyToCookBpBg;
    private Bitmap unionPrepareTempSensorBpBg;
    private Bitmap unionPrepareTempSensorWithIndicatorBpBg;
    private Bitmap seperatePrepareTempSensorBpBg;
    private Bitmap seperatePrepareTempSensorWithIndicatorBpBg;
    private Bitmap unionHighTempBgBp;
    private Bitmap seperateHighTempBgBp;
    private Bitmap unionErrorOccurBgBp;
    private Bitmap seperateErrorOccurBgBp;
    private Bitmap unionRecipesBgBp;
    private Bitmap seperateRecipesBgBp;
    private Bitmap unionRecipesPicBp;
    private Bitmap upRecipesPicBp;
    private Bitmap downRecipesPicBp;

    public Hob80RectangleCooker(Context context) {
        super(context);
    }

    public Hob80RectangleCooker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected Bitmap getUnionPowerOffBgBitmap() {
        if (unionPowerOffBgBp == null) {
            unionPowerOffBgBp = BitmapFactory.decodeResource(getResources(), POWER_OFF_UNION_BG_BITMAP_ID);
        }
        return unionPowerOffBgBp;
    }

    @Override
    protected Bitmap getSeperatePowerOffBgBitmap() {
        if (seperatePowerOffBgBp == null) {
            seperatePowerOffBgBp = BitmapFactory.decodeResource(getResources(), POWER_OFF_SEPERATE_BG_BITMAP_ID);
        }
        return seperatePowerOffBgBp;
    }

    @Override
    protected Bitmap getSeperatePowerOnFireWithoutTimerBgBitmap() {
        if (powerOnSeperateFireWithoutTimerBpBg == null) {
            powerOnSeperateFireWithoutTimerBpBg = BitmapFactory.decodeResource(getResources(), POWER_ON_SEPERATE_FIRE_WITHOUT_TIMER_BG_BITMAP_ID);
        }
        return powerOnSeperateFireWithoutTimerBpBg;
    }

    @Override
    protected Bitmap getSeperatePowerOnFireWithTimerBgBitmap() {
        if (powerOnSeperateFireWithTimerBpBg == null) {
            powerOnSeperateFireWithTimerBpBg = BitmapFactory.decodeResource(getResources(), POWER_ON_SEPERATE_FIRE_WITH_TIMER_BG_BITMAP_ID);
        }
        return powerOnSeperateFireWithTimerBpBg;
    }

    @Override
    protected Bitmap getPowerOffButtonBitmap() {
        if (powerOffButonBp == null) {
            powerOffButonBp = BitmapFactory.decodeResource(getResources(), POWER_OFF_BUTTON_BITMAP_ID);
        }
        return powerOffButonBp;
    }

    @Override
    protected Bitmap getUnionPowerOnFireWithoutTimerBgBitmap() {
        if (powerOnUnionFireWithoutTimerBpBg == null) {
            powerOnUnionFireWithoutTimerBpBg = BitmapFactory.decodeResource(getResources(), POWER_ON_UNION_FIRE_WITHOUT_TIMER_BG_BITMAP_ID);
        }
        return powerOnUnionFireWithoutTimerBpBg;
    }

    @Override
    protected Bitmap getUnionPowerOnFireWithTimerBgBitmap() {

        if (powerOnUnionFireWithTimerBpBg == null) {
            powerOnUnionFireWithTimerBpBg = BitmapFactory.decodeResource(getResources(), POWER_ON_UNION_FIRE_WITH_TIMER_BG_BITMAP_ID);
        }
        return powerOnUnionFireWithTimerBpBg;
    }

    @Override
    protected Bitmap getUnionPowerOnTempWithoutTimerBgBitmap() {

        if (powerOnUnionTempWithoutTimerBpBg == null) {
            powerOnUnionTempWithoutTimerBpBg = BitmapFactory.decodeResource(getResources(), POWER_ON_UNION_TEMP_WITHOUT_TIMER_BG_BITMAP_ID);
        }
        return powerOnUnionTempWithoutTimerBpBg;
    }

    @Override
    protected Bitmap getUnionPowerOnTempWithTimerBgBitmap() {
        if (powerOnUnionTempWithTimerBpBg == null) {
            powerOnUnionTempWithTimerBpBg = BitmapFactory.decodeResource(getResources(), POWER_ON_UNION_TEMP_WITH_TIMER_BG_BITMAP_ID);
        }
        return powerOnUnionTempWithTimerBpBg;
    }

    @Override
    protected Bitmap getSeperatePowerOnTempWithoutTimerBgBitmap() {

        if (powerOnSeperateTempWithoutTimerBpBg == null) {
            powerOnSeperateTempWithoutTimerBpBg = BitmapFactory.decodeResource(getResources(), POWER_ON_SEPERATE_TEMP_WITHOUT_TIMER_BG_BITMAP_ID);
        }
        return powerOnSeperateTempWithoutTimerBpBg;
    }

    @Override
    protected Bitmap getSeperatePowerOnTempWithTimerBgBitmap() {
        if (powerOnSeperateTempWithTimerBpBg == null) {
            powerOnSeperateTempWithTimerBpBg = BitmapFactory.decodeResource(getResources(), POWER_ON_SEPERATE_TEMP_WITH_TIMER_BG_BITMAP_ID);
        }
        return powerOnSeperateTempWithTimerBpBg;
    }

    @Override
    protected Bitmap getUnionAbnormalNoPanBgBitmap() {
        if (powerOnUnionAbnormalNoPanBp == null) {
            powerOnUnionAbnormalNoPanBp = BitmapFactory.decodeResource(getResources(), POWER_ON_UNION_ABNORMAL_NO_PAN);
        }
        return powerOnUnionAbnormalNoPanBp;
    }

    @Override
    protected Bitmap getSeperateAbnormalNoPanBgBitmap() {

        if (powerOnSeperateAbnormalNoPanB == null) {
            powerOnSeperateAbnormalNoPanB = BitmapFactory.decodeResource(getResources(), POWER_ON_SEPERATE_ABNORMAL_NO_PAN);
        }
        return powerOnSeperateAbnormalNoPanB;
    }

    @Override
    protected Bitmap getUnionPowerOnTempIndicatorWithoutTimerBgBitmap() {
        if (powerOnUnionTempIndicatorWithoutTimerBpBg == null) {
            powerOnUnionTempIndicatorWithoutTimerBpBg = BitmapFactory.decodeResource(getResources(), POWER_ON_UNION_TEMP_INDICATOR_WITHOUT_TIMER_BG_BITMAP_ID);
        }
        return powerOnUnionTempIndicatorWithoutTimerBpBg;
    }

    @Override
    protected Bitmap getUnionPowerOnTempIndicatorWithTimerBgBitmap() {
        if (powerOnUnionTempIndicatorWithTimerBpBg == null) {
            powerOnUnionTempIndicatorWithTimerBpBg = BitmapFactory.decodeResource(getResources(), POWER_ON_UNION_TEMP_INDICATOR_WITH_TIMER_BG_BITMAP_ID);
        }
        return powerOnUnionTempIndicatorWithTimerBpBg;
    }

    @Override
    protected Bitmap getSeperatePowerOnTempIndicatorWithoutTimerBgBitmap() {
        if (powerOnSeperateTempIndicatorWithoutTimerBpBg == null) {
            powerOnSeperateTempIndicatorWithoutTimerBpBg = BitmapFactory.decodeResource(getResources(), POWER_ON_SEPERATE_TEMP_INDICATOR_WITHOUT_TIMER_BG_BITMAP_ID);
        }
        return powerOnSeperateTempIndicatorWithoutTimerBpBg;
    }

    @Override
    protected Bitmap getSeperatePowerOnTempIndicatorWithTimerBgBitmap() {
        if (powerOnSeperateTempIndicatorWithTimerBpBg == null) {
            powerOnSeperateTempIndicatorWithTimerBpBg = BitmapFactory.decodeResource(getResources(), POWER_ON_SEPERATE_TEMP_INDICATOR_WITH_TIMER_BG_BITMAP_ID);
        }
        return powerOnSeperateTempIndicatorWithTimerBpBg;
    }

    @Override
    protected Bitmap getUnionTempIndicatorWithoutTimerBitmap() {
        if (unionTempIndicatorBitmap == null) {
            unionTempIndicatorBitmap = ImageUtil.decodeTempIdentifyBitmap(getContext(), unionTempIndicatorResID,getUnionPowerOnTempIndicatorWithoutTimerBgBitmap().getWidth(),getUnionPowerOnTempIndicatorWithoutTimerBgBitmap().getHeight() / 2);

        }
        return unionTempIndicatorBitmap;
    }

    @Override
    protected Bitmap getUnionTempIndicatorWithTimerBitmap() {
        if (unionTempIndicatorBitmap == null) {
            unionTempIndicatorBitmap = ImageUtil.decodeTempIdentifyBitmap(getContext(), unionTempIndicatorResID,getUnionPowerOnTempIndicatorWithTimerBgBitmap().getWidth(),getUnionPowerOnTempIndicatorWithTimerBgBitmap().getHeight() / 2);

        }
        return unionTempIndicatorBitmap;
    }

    @Override
    protected Bitmap getSeperateUpTempIndicatorWithoutTimerBitmap() {
        if (seperateUpTempIndicatorBitmap == null) {
            seperateUpTempIndicatorBitmap = ImageUtil.decodeTempIdentifyBitmap(getContext(), seperateUpTempIndicatorResID,getSeperatePowerOnTempIndicatorWithoutTimerBgBitmap().getWidth(),getSeperatePowerOnTempIndicatorWithoutTimerBgBitmap().getHeight() / 2);

        }
        return seperateUpTempIndicatorBitmap;


    }

    @Override
    protected Bitmap getSeperateUpTempIndicatorWithTimerBitmap() {
        if (seperateUpTempIndicatorBitmap == null) {
            seperateUpTempIndicatorBitmap = ImageUtil.decodeTempIdentifyBitmap(getContext(), seperateUpTempIndicatorResID,getSeperatePowerOnTempIndicatorWithoutTimerBgBitmap().getWidth(),getSeperatePowerOnTempIndicatorWithoutTimerBgBitmap().getHeight() / 3);

        }
        return seperateUpTempIndicatorBitmap;
    }

    @Override
    protected Bitmap getSeperateDownTempIndicatorWithoutTimerBitmap() {
        if (seperateDownTempIndicatorBitmap == null) {
            seperateDownTempIndicatorBitmap = ImageUtil.decodeTempIdentifyBitmap(getContext(), seperateDownTempIndicatorResID,getSeperatePowerOnTempIndicatorWithoutTimerBgBitmap().getWidth(),getSeperatePowerOnTempIndicatorWithoutTimerBgBitmap().getHeight() / 2);

        }
        return seperateDownTempIndicatorBitmap;
    }

    @Override
    protected Bitmap getSeperateDownTempIndicatorWithTimerBitmap() {
        if (seperateDownTempIndicatorBitmap == null) {
            seperateDownTempIndicatorBitmap = ImageUtil.decodeTempIdentifyBitmap(getContext(), seperateDownTempIndicatorResID,getSeperatePowerOnTempIndicatorWithoutTimerBgBitmap().getWidth(),getSeperatePowerOnTempIndicatorWithoutTimerBgBitmap().getHeight() / 3);

        }
        return seperateDownTempIndicatorBitmap;
    }

    @Override
    protected Bitmap getUnionTempFinishLightBgBitmap() {

        if (unionTempFinishLightBpBg == null) {
            unionTempFinishLightBpBg = BitmapFactory.decodeResource(getResources(), UNION_TEMP_FINISH_LIGHT_BG_BITMAP_ID);
        }
        return unionTempFinishLightBpBg;
    }

    @Override
    protected Bitmap getUnionTempFinishDarkBgBitmap() {
        if (unionTempFinishDarkBpBg == null) {
            unionTempFinishDarkBpBg = BitmapFactory.decodeResource(getResources(), UNION_TEMP_FINISH_DARK_BG_BITMAP_ID);
        }
        return unionTempFinishDarkBpBg;
    }

    @Override
    protected Bitmap getSeperateTempFinishLightBgBitmap() {
        if (seperateTempFinishLightBpBg == null) {
            seperateTempFinishLightBpBg = BitmapFactory.decodeResource(getResources(), SEPERATE_TEMP_FINISH_LIGHT_BG_BITMAP_ID);
        }
        return seperateTempFinishLightBpBg;
    }

    @Override
    protected Bitmap getSeperateTempFinishDarkBgBitmap() {
        if (seperateempFinishDarkBpBg == null) {
            seperateempFinishDarkBpBg = BitmapFactory.decodeResource(getResources(), SEPERATE_TEMP_FINISH_DARK_BG_BITMAP_ID);
        }
        return seperateempFinishDarkBpBg;
    }

    @Override
    protected Bitmap getUnionReadyToCookBgBitmap() {
        if (unionReadyToCookBpBg == null) {
            unionReadyToCookBpBg = BitmapFactory.decodeResource(getResources(), UNION_READY_TO_COOK_BG_BITMAP_ID);
        }
        return unionReadyToCookBpBg;

    }

    @Override
    protected Bitmap getUnionReadyToCookValueBgBitmap() {
        return null;
    }

    @Override
    protected Bitmap getSeperateReadyToCookBgBitmap() {
        if (seperateReadyToCookBpBg == null) {
            seperateReadyToCookBpBg = BitmapFactory.decodeResource(getResources(), SEPERATE_READY_TO_COOK_BG_BITMAP_ID);
        }
        return seperateReadyToCookBpBg;
    }

    @Override
    protected Bitmap getSeperateReadyToCookValueBgBitmap() {
        return null;
    }

    @Override
    protected Bitmap getUnionPrepareTempSensorBgBitmap() {
        if (unionPrepareTempSensorBpBg == null) {
            unionPrepareTempSensorBpBg = BitmapFactory.decodeResource(getResources(), UNION_PREPARE_TEMP_SENSOR_BG_BITMAP_ID);
        }
        return unionPrepareTempSensorBpBg;
    }

    @Override
    protected Bitmap getUnionPrepareTempSensorWithIndicatorBgBitmap() {
        if (unionPrepareTempSensorWithIndicatorBpBg == null) {
            unionPrepareTempSensorWithIndicatorBpBg = BitmapFactory.decodeResource(getResources(), UNION_PREPARE_TEMP_SENSOR_WITH_INDICATOR_BG_BITMAP_ID);
        }
        return unionPrepareTempSensorWithIndicatorBpBg;
    }

    @Override
    protected Bitmap getSeperatePrepareTempSensorBgBitmap() {
        if (seperatePrepareTempSensorBpBg == null) {
            seperatePrepareTempSensorBpBg = BitmapFactory.decodeResource(getResources(), SEPERATE_PREPARE_TEMP_SENSOR_BG_BITMAP_ID);
        }
        return seperatePrepareTempSensorBpBg;
    }

    @Override
    protected Bitmap getSeperatePrepareTempSensorWithIndicatorBgBitmap() {
        if (seperatePrepareTempSensorWithIndicatorBpBg == null) {
            seperatePrepareTempSensorWithIndicatorBpBg = BitmapFactory.decodeResource(getResources(), SEPERATE_PREPARE_TEMP_SENSOR_WITH_INDICATOR_BG_BITMAP_ID);
        }
        return seperatePrepareTempSensorWithIndicatorBpBg;
    }

    @Override
    protected Bitmap getSeperateHighTempBgBitmap() {
        if (seperateHighTempBgBp == null) {
            seperateHighTempBgBp = BitmapFactory.decodeResource(getResources(), SEPERATE_HIGH_TEMP_BG_BITMAP_ID);
        }
        return seperateHighTempBgBp;
    }

    @Override
    protected Bitmap getUnionHighTempBgBitmap() {

        if (unionHighTempBgBp == null) {
            unionHighTempBgBp = BitmapFactory.decodeResource(getResources(), UNION_HIGH_TEMP_BG_BITMAP_ID);
        }
        return unionHighTempBgBp;
    }

    @Override
    protected Bitmap getUnionErrorOccurBgBitmap() {
        if (unionErrorOccurBgBp == null) {
            unionErrorOccurBgBp = BitmapFactory.decodeResource(getResources(), UNION_ERROR_OCCUR_BG_BITMAP_ID);
        }
        return unionErrorOccurBgBp;

    }

    @Override
    protected Bitmap getSeperateErrorOccurBgBitmap() {
        if (seperateErrorOccurBgBp == null) {
            seperateErrorOccurBgBp = BitmapFactory.decodeResource(getResources(), SEPERATE_ERROR_OCCUR_BG_BITMAP_ID);
        }
        return seperateErrorOccurBgBp;
    }

    @Override
    protected Bitmap getUnionRecipesPicBitmap() {
        if (unionRecipesPicBp == null) {
            unionRecipesPicBp = BitmapFactory.decodeResource(getResources(), unionRecipesID);
            unionRecipesPicBp = ImageUtil.centerSquareScaleBitmap(unionRecipesPicBp,getUnionRecipesBgBitmap().getWidth() - 30);
            unionRecipesPicBp = ImageUtil.getRoundedCornerBitmap(unionRecipesPicBp);

        }
        return unionRecipesPicBp;
    }

    @Override
    protected Bitmap getUpRecipesPicBitmap() {

        if (upRecipesPicBp == null) {
            upRecipesPicBp = BitmapFactory.decodeResource(getResources(), upRecipesID);
            upRecipesPicBp = ImageUtil.centerSquareScaleBitmap(upRecipesPicBp,getSeperateRecipesBgBitmap().getWidth() - 30);
            upRecipesPicBp = ImageUtil.getRoundedCornerBitmap(upRecipesPicBp);

        }
        return upRecipesPicBp;
    }

    @Override
    protected Bitmap getDownRecipesPicBitmap() {

        if (downRecipesPicBp == null) {
            downRecipesPicBp = BitmapFactory.decodeResource(getResources(), downRecipesID);
            downRecipesPicBp = ImageUtil.centerSquareScaleBitmap(downRecipesPicBp,getSeperateRecipesBgBitmap().getWidth() - 30);
            downRecipesPicBp = ImageUtil.getRoundedCornerBitmap(downRecipesPicBp);

        }
        return downRecipesPicBp;
    }

    @Override
    protected Bitmap getUnionRecipesBgBitmap() {
        if (unionRecipesBgBp == null) {
            unionRecipesBgBp = BitmapFactory.decodeResource(getResources(), UNION_COOKER_RECIPES_BG_BITMAP_ID);
        }
        return unionRecipesBgBp;
    }

    @Override
    protected Bitmap getSeperateRecipesBgBitmap() {
        if (seperateRecipesBgBp == null) {
            seperateRecipesBgBp = BitmapFactory.decodeResource(getResources(), SEPERATE_COOKER_RECIPES_BG_BITMAP_ID);
        }
        return seperateRecipesBgBp;
    }

    @Override
    protected Bitmap getSeperateTempPrepareSensorBitmap() {
        return null;
    }

    @Override
    protected Bitmap getSeperateTempIndicatorPrepareSensorBitmap() {
        return null;
    }

    @Override
    protected Bitmap getSeperateTempSensorReadyBitmap() {
        return null;
    }

    @Override
    protected Bitmap getSeperateTempSensorReadyValueBitmap() {
        return null;
    }

    @Override
    protected Bitmap getSeperateTempWorkFinishLightBitmap() {
        return null;
    }


    @Override
    protected Bitmap getSeperateTempWorkFinishDarkBitmap() {
        return null;
    }

    @Override
    protected Bitmap getSeperateTempGearWithTimerBitmap() {
        return null;
    }

    @Override
    protected Bitmap getUnionTempWorkFinishLightBitmap() {
        return null;
    }


    @Override
    protected int getSeperateFireGearWithoutTimerFontSize() {
        return FONT_SIZE_FOR_SEPERATE_FIRE_GEAR_WITHOUT_TIMER;
    }

    @Override
    protected int getSeperateFireGearWithTimerGearFontSize() {
        return FONT_SIZE_FOR_SEPERATE_FIRE_GEAR_WITH_TIMER_GEAR_SIZE;
    }

    @Override
    protected int getSeperateFireGearWithTimerTimerFontSize() {
        return FONT_SIZE_FOR_SEPERATE_FIRE_GEAR_WITH_TIMER_TIMER_SIZE;
    }

    @Override
    protected int getSeperateTempGearWithTimerTimerFontSize() {
        return 0;
    }

    @Override
    protected int getSeperateUpTempGearWithoutTimerTempFontSize() {
        if (upTempGearValue >= 100) {
            return FONT_SIZE_FOR_SEPERATE_TEMP_GEAR_WITHOUT_TIMER_TEMP_SIZE_SMALL;
        }else {
            return FONT_SIZE_FOR_SEPERATE_TEMP_GEAR_WITHOUT_TIMER_TEMP_SIZE_BIG;
        }
    }

    @Override
    protected int getSeperateDownTempGearWithoutTimerTempFontSize() {
        if (downTempGearValue >= 100) {
            return FONT_SIZE_FOR_SEPERATE_TEMP_GEAR_WITHOUT_TIMER_TEMP_SIZE_SMALL;
        }else {
            return FONT_SIZE_FOR_SEPERATE_TEMP_GEAR_WITHOUT_TIMER_TEMP_SIZE_BIG;
        }
    }

    @Override
    protected int getSeperateUpTempGearWithTimerTempFontSize() {
        if (upTempGearValue >= 100) {
            return FONT_SIZE_FOR_SEPERATE_TEMP_GEAR_WITH_TIMER_TEMP_SIZE_SMALL;
        }else {
            return FONT_SIZE_FOR_SEPERATE_TEMP_GEAR_WITH_TIMER_TEMP_SIZE_BIG;
        }
    }

    @Override
    protected int getSeperateDownTempGearWithTimerTempFontSize() {
        if (downTempGearValue >= 100) {
            return FONT_SIZE_FOR_SEPERATE_TEMP_GEAR_WITH_TIMER_TEMP_SIZE_SMALL;
        }else {
            return FONT_SIZE_FOR_SEPERATE_TEMP_GEAR_WITH_TIMER_TEMP_SIZE_BIG;
        }
    }


    @Override
    protected int getUnionFireGearWithoutTimerFontSize() {
        return FONT_SIZE_FOR_UNION_FIRE_GEAR_WITHOUT_TIMER;
    }

    @Override
    protected int getUnionFireGearWithTimerGearFontSize() {
        return FONT_SIZE_FOR_UNION_FIRE_GEAR_WITH_TIMER_GEAR_SIZE;
    }

    @Override
    protected int getUnionFireGearWithTimerTimerFontSize() {
        return FONT_SIZE_FOR_UNION_FIRE_GEAR_WITH_TIMER_TIMER_SIZE;
    }

    @Override
    protected int getUnionTempGearWithoutTimerFontSize() {
        if (unionTempGearValue >= 100) {
            return FONT_SIZE_FOR_UNION_TEMP_GEAR_WITHOUT_TIMER_SMALL;
        }else {
            return FONT_SIZE_FOR_UNION_TEMP_GEAR_WITHOUT_TIMER_BIG;
        }
    }

    @Override
    protected int getUnionTempGearWithTimerTimerFontSize() {
        return FONT_SIZE_FOR_UNION_TEMP_GEAR_WITH_TIMER_TIMER_SIZE;
    }

    @Override
    protected int getUnionTempGearWithTimerTempFontSize() {
        if (unionTempGearValue >= 100) {
            return FONT_SIZE_FOR_UNION_TEMP_GEAR_WITH_TIMER_TEMP_SIZE_SMALL;
        }else {
            return FONT_SIZE_FOR_UNION_TEMP_GEAR_WITH_TIMER_TEMP_SIZE_BIG;
        }
    }

    @Override
    protected int getUnionErrorMessageFontSize() {
        return FONT_SIZE_FOR_UNION_ERROR_MESSAGE;
    }

    @Override
    protected int getSeperateErrorMessageFontSize() {
        return FONT_SIZE_FOR_SEPERATE_ERROR_MESSAGE;
    }

    @Override
    protected int getSeperateKeepWarmAndAddTenMinuteFontSize() {
        return 0;
    }

    @Override
    protected int getSeperateReadyToCookFontSize() {
        return 0;
    }

    @Override
    protected int getUnionReadyToCookValueFontSize() {
        return 0;
    }

    @Override
    protected int getSeperateUpReadyToCookValueFontSize() {
        return 0;
    }

    @Override
    protected int getSeperateDownReadyToCookValueFontSize() {
        return 0;
    }

    @Override
    protected int getUpTempPreheatSettingValueFontSize() {
        return 0;
    }

    @Override
    protected int getUpTempPreheatRealValueFontSize() {
        return 0;
    }

    @Override
    protected int getDownTempPreheatSettingValueFontSize() {
        return 0;
    }

    @Override
    protected int getDownTempPreheatRealValueFontSize() {
        return 0;
    }

    @Override
    protected int getUnionTempPreheatSettingValueFontSize() {
        return 0;
    }

    @Override
    protected int getUnionTempPreheatRealValueFontSize() {
        return 0;
    }

    @Override
    protected int getUnionReadyToCookFontSize() {
        return 0;
    }


    @Override
    protected String getUpFireGearValue() {
        if (upFireGearValue == 10) return "B";
        else return String.valueOf(upFireGearValue);
    }

    @Override
    protected String getUpTimerValue() {
        return String.format("%02d", upHourValue) + ":" + String.format("%02d", upMinuteValue);

    }

    @Override
    protected String getUpTempValue() {
        return String.valueOf(upTempGearValue);
    }

    @Override
    protected String getDownFireGearValue() {
        if (downFireGearValue == 10) return "B";
        else return String.valueOf(downFireGearValue);
    }

    @Override
    protected String getDownTimerValue() {
        return String.format("%02d", downHourValue) + ":" + String.format("%02d", downMinuteValue);

    }

    @Override
    protected String getDownTempValue() {
        return String.valueOf(downTempGearValue);
    }

    @Override
    protected String getUnionFireGearValue() {
        if (unionFireGearValue == 10) return "B";
        else return String.valueOf(unionFireGearValue);

    }

    @Override
    protected String getUnionTimerValue() {
        return String.format("%02d", unionHourValue) + ":" + String.format("%02d", unionMinuteValue);
    }

    @Override
    protected String getUnionTempGearValue() {
        return String.valueOf(unionTempGearValue);
    }

    @Override
    protected String getUnionErrorMessage() {
        return unionErrorMessage;
    }

    @Override
    protected String getUpErrorMessage() {
        return upErrorMessage;
    }

    @Override
    protected String getDownErrorMessage() {
        return downErrorMessage;
    }

    @Override
    protected String getUpRealTempValue() {
        return null;
    }

    @Override
    protected int getUpSweepAngle() {
        return 0;
    }

    @Override
    protected float getUpRealTempArcGap() {
        return 0;
    }

    @Override
    protected float getUpRealTempTextGap() {
        return 0;
    }

    @Override
    protected String getDownRealTempValue() {
        return null;
    }

    @Override
    protected int getDownSweepAngle() {
        return 0;
    }

    @Override
    protected float getDownRealTempArcGap() {
        return 0;
    }

    @Override
    protected float getDownRealTempTextGap() {
        return 0;
    }

    @Override
    protected String getUnionRealTempValue() {
        return null;
    }

    @Override
    protected int getUnionSweepAngle() {
        return 0;
    }

    @Override
    protected float getUnionRealTempArcGap() {
        return 0;
    }

    @Override
    protected float getUnionRealTempTextGap() {
        return 0;
    }

    @Override
    protected int getCookerViewWidth() {
        return HOB_RETANGLE_COOKER_SIZE_WIDTH;
    }

    @Override
    protected int getCookerViewHeight() {
        return HOB_RETANGLE_COOKER_SIZE_HEIGHT;
    }

    @Override
    protected int getSeperateRealTempProgressArcWidth() {
        return 0;
    }

    @Override
    protected int getSeperateRealTempProgressRadius() {
        return 0;
    }

    @Override
    protected int getUnionRealTempProgressArcWidth() {
        return 0;
    }

    @Override
    protected int getUnionRealTempProgressRadius() {
        return 0;
    }

    @Override
    protected int getUnionInnerTextMaxWidth() {
        return 0;
    }

    @Override
    protected int getUnionInnerTextMaxWidthBig() {
        return 0;
    }

    @Override
    protected void recyle() {

    }

    @Override
    protected void recyleUnionIndicatorBitmap() {
        if (unionTempIndicatorBitmap != null) {
            unionTempIndicatorBitmap.recycle();
            unionTempIndicatorBitmap = null;
        }

    }

    @Override
    protected void recyleSeperateUpIndicatorBitmap() {
        if (seperateUpTempIndicatorBitmap != null) {
            seperateUpTempIndicatorBitmap.recycle();
            seperateUpTempIndicatorBitmap = null;
        }
    }

    @Override
    protected void recyleSeperateDownIndicatorBitmap() {
        if (seperateDownTempIndicatorBitmap != null) {
            seperateDownTempIndicatorBitmap.recycle();
            seperateDownTempIndicatorBitmap = null;
        }
    }

    @Override
    protected void recyleRecipesBitmap() {

    }

    @Override
    protected void recyleUnionRecipesBitmap() {
        if (unionRecipesPicBp != null) {
            unionRecipesPicBp.recycle();
            unionRecipesPicBp = null;
        }
    }

    @Override
    protected void recyleSeperateUpRecipesBitmap() {
        if (upRecipesPicBp != null) {
            upRecipesPicBp.recycle();
            upRecipesPicBp = null;
        }
    }

    @Override
    protected void recyleSeperateDownRecipesBitmap() {
        if (downRecipesPicBp != null) {
            downRecipesPicBp.recycle();
            downRecipesPicBp = null;
        }
    }


    @Override
    public int[] getCookerIDs() {
        int[] ids = {downCookerID,upCookerID,unionCookerID};
        return ids;
    }

    @Override
    public void setOnHobRectangleCookerListener(OnHobRectangleCookerListener listener) {
        mListener = listener;
    }

    @Override
    public void updateCookerView(int cookerID, int workMode, int fireValue, int tempValue, int realTempValue,int hourValue, int minuteValue, int tempIndicatorID, int recipesID, String errorMessage) {
        LogUtil.d("Enter:: updateCookerView---cookerID--->" + cookerID);
        LogUtil.d("Enter:: updateCookerView---workMode--->" + workMode);
        currentUpdateCookerID = cookerID;
        if (cookerID == upCookerID) {
            upWorkMode = workMode;
            upFireGearValue = fireValue;
            upTempGearValue = tempValue;
            upHourValue = hourValue;
            upMinuteValue = minuteValue;

        }else if (cookerID == downCookerID) {
            downWorkMode = workMode;
            downFireGearValue = fireValue;
            downTempGearValue = tempValue;
            downHourValue = hourValue;
            downMinuteValue = minuteValue;

        }else if (cookerID == unionCookerID){
            unionWorkMode = workMode;
            unionFireGearValue = fireValue;
            unionHourValue = hourValue;
            unionMinuteValue = minuteValue;
            unionTempGearValue = tempValue;
            unionTempIndicatorResID = tempIndicatorID;
        }

        invalidate();
    }

    @Override
    public void updateUnionCookerView(int unionCookerID, int upCookerID, int upWorkMode, int upFireValue, int upTempValue, int upHourValue, int upMinute, int upTempIndicatorID, int upRecipesID, String upErrorMessage, int downCookerID, int downWorkMode, int downFireValue, int downTempValue, int downHourValue, int downMinute, int downTempIndicatorID, int downRecipesID, String downErrorMessage) {
        currentUpdateCookerID = unionCookerID;

        this.unionCookerID = unionCookerID;
        this.upCookerID = upCookerID;
        this.upFireGearValue  = upFireValue;
        this.upTempGearValue = upTempValue;
        this.upHourValue = upHourValue;
        this.upMinuteValue = upMinute;
        this.seperateUpTempIndicatorResID = upTempIndicatorID;
        this.seperateDownTempIndicatorResID = upTempIndicatorID;
        this.upErrorMessage = upErrorMessage;

        this.downCookerID = downCookerID;
        this.downFireGearValue  = downFireValue;
        this.downTempGearValue = downTempValue;
        this.downHourValue = downHourValue;
        this.downMinuteValue = downMinute;
        this.seperateUpTempIndicatorResID = downTempIndicatorID;
        this.seperateDownTempIndicatorResID = downTempIndicatorID;
        this.downErrorMessage = downErrorMessage;

        invalidate();
    }

    @Override
    public void cookerPoweroff() {
        retangleHobWorkMode = HOB_RETANGLE_WORK_MODE_WORK_SEPARATE;
    }
}
