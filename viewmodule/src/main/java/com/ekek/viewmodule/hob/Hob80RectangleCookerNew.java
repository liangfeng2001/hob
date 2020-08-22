package com.ekek.viewmodule.hob;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.ekek.commonmodule.utils.ImageUtil;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.commonmodule.utils.Logger;
import com.ekek.viewmodule.R;
import com.ekek.viewmodule.contract.HobRectangleCookerContract;
import com.ekek.viewmodule.product.ProductManager;

public class Hob80RectangleCookerNew extends BaseRectangleCookerView implements HobRectangleCookerContract {
    /************need config here for every hob type*************/
    private static final int HOB_RETANGLE_COOKER_SIZE_WIDTH = 262 + 60;//480
    private static final int HOB_RETANGLE_COOKER_SIZE_HEIGHT = 555 + 0;//546
    private static final int POWER_OFF_UNION_BG_BITMAP_ID = R.mipmap.bg_hob_80_rectangle_union_cooker_power_off;
    private static final int POWER_OFF_SEPERATE_BG_BITMAP_ID = R.mipmap.bg_hob_80_rectangle_seperate_cooker_power_off;
    private static final int POWER_ON_SEPERATE_FIRE_WITHOUT_TIMER_BG_BITMAP_ID = R.mipmap.bg_hob_80_rectangle_seperate_cooker_fire_gear_without_timer;
    private static final int POWER_ON_SEPERATE_FIRE_WITH_TIMER_BG_BITMAP_ID = R.mipmap.bg_hob_80_rectangle_seperate_cooker_power_on_fire_with_timer;
    private static final int POWER_OFF_BUTTON_BITMAP_ID = R.mipmap.ic_hob_80_power_off_button;
    private static final int POWER_ON_UNION_FIRE_WITHOUT_TIMER_BG_BITMAP_ID = R.mipmap.bg_hob_90_rectangle_union_cooker_power_on_fire_without_timer;
    private static final int POWER_ON_UNION_FIRE_WITH_TIMER_BG_BITMAP_ID = R.mipmap.bg_hob_90_rectangle_union_cooker_power_on_fire_with_timer;
    private static final int POWER_ON_UNION_TEMP_WITHOUT_TIMER_BG_BITMAP_ID = R.mipmap.bg_hob_80_rectangle_union_cooker_power_on_temp_without_timer;
    private static final int POWER_ON_UNION_TEMP_WITH_TIMER_BG_BITMAP_ID = R.mipmap.bg_hob_80_rectangle_union_cooker_power_on_temp_with_timer;
    private static final int POWER_ON_SEPERATE_TEMP_WITHOUT_TIMER_BG_BITMAP_ID = R.mipmap.bg_hob_80_rectangle_seperate_cooker_power_on_temp_without_timer;
    private static final int POWER_ON_SEPERATE_TEMP_WITH_TIMER_BG_BITMAP_ID = R.mipmap.bg_hob_80_rectangle_seperate_cooker_power_on_temp_with_timer;
    private static final int POWER_ON_UNION_ABNORMAL_NO_PAN = R.mipmap.bg_hob_80_rectangle_union_cooker_no_pan;
    private static final int POWER_ON_SEPERATE_ABNORMAL_NO_PAN = R.mipmap.bg_hob_80_rectangle_seperate_cooker_no_pan;
    private static final int POWER_ON_UNION_TEMP_INDICATOR_WITHOUT_TIMER_BG_BITMAP_ID = R.mipmap.bg_hob_80_rectangle_union_cooker_power_on_temp_indicator_without_timer;
    private static final int POWER_ON_UNION_TEMP_INDICATOR_WITH_TIMER_BG_BITMAP_ID = R.mipmap.bg_hob_80_rectangle_union_cooker_power_on_temp_indicator_with_timer;
    private static final int POWER_ON_SEPERATE_TEMP_INDICATOR_WITHOUT_TIMER_BG_BITMAP_ID = R.mipmap.bg_hob_80_rectangle_seperate_cooker_power_on_temp_indicator_without_timer;
    private static final int POWER_ON_SEPERATE_TEMP_INDICATOR_WITH_TIMER_BG_BITMAP_ID = R.mipmap.bg_hob_80_rectangle_seperate_cooker_power_on_temp_indicator_with_timer;
    private static final int UNION_TEMP_FINISH_LIGHT_BG_BITMAP_ID = R.mipmap.bg_hob_90_union_keep_warm_light;
    private static final int UNION_TEMP_FINISH_DARK_BG_BITMAP_ID = R.mipmap.bg_hob_90_union_keep_warm_dark;
    private static final int SEPERATE_TEMP_FINISH_LIGHT_BG_BITMAP_ID = R.mipmap.bg_hob_80_seperate_keep_warm_light;
    private static final int SEPERATE_TEMP_FINISH_DARK_BG_BITMAP_ID = R.mipmap.bg_hob_90_seperate_keep_warm_dark;
    private static final int UNION_READY_TO_COOK_BG_BITMAP_ID = R.mipmap.bg_hob_80_union_ready_to_cook;
    private static final int UNION_READY_TO_COOK_VALUE_BG_BITMAP_ID = R.mipmap.bg_hob_80_union_ready_to_cook_value;
    private static final int SEPERATE_READY_TO_COOK_BG_BITMAP_ID = R.mipmap.bg_hob_80_seperate_ready_to_cook;
    private static final int SEPERATE_READY_TO_COOK_VALUE_BG_BITMAP_ID = R.mipmap.bg_hob_80_seperate_ready_to_cook_value;
    private static final int UNION_PREPARE_TEMP_SENSOR_BG_BITMAP_ID = R.mipmap.bg_hob_80_union_prepare_temp_sensor;
    private static final int UNION_PREPARE_TEMP_SENSOR_WITH_INDICATOR_BG_BITMAP_ID = R.mipmap.bg_hob_90_union_prepare_temp_sensor_with_indicator;
    private static final int SEPERATE_PREPARE_TEMP_SENSOR_BG_BITMAP_ID = R.mipmap.bg_hob_80_seperate_prepare_temp_sensor;
    private static final int SEPERATE_PREPARE_TEMP_SENSOR_WITH_INDICATOR_BG_BITMAP_ID = R.mipmap.bg_hob_90_seperate_prepare_temp_sensor_with_indicator;
    private static final int UNION_HIGH_TEMP_BG_BITMAP_ID = R.mipmap.bg_hob_80_union_high_temp;
    private static final int SEPERATE_HIGH_TEMP_BG_BITMAP_ID = R.mipmap.bg_hob_80_seperate_high_temp;
    private static final int UNION_ERROR_OCCUR_BG_BITMAP_ID = R.mipmap.bg_hob_80_union_cooker_error_occur;
    private static final int SEPERATE_ERROR_OCCUR_BG_BITMAP_ID = R.mipmap.bg_hob_80_seperate_cooker_error_occur;
    private static final int UNION_COOKER_RECIPES_BG_BITMAP_ID = R.mipmap.bg_hob_90_union_cooker_recipes;
    private static final int SEPERATE_COOKER_RECIPES_BG_BITMAP_ID = R.mipmap.bg_hob_90_seperate_cooker_recipes;


    private static final int FONT_SIZE_FOR_SEPERATE_FIRE_GEAR_WITHOUT_TIMER = 108;
    private static final int FONT_SIZE_FOR_SEPERATE_FIRE_GEAR_WITH_TIMER_GEAR_SIZE = 108;
    private static final int FONT_SIZE_FOR_SEPERATE_FIRE_GEAR_WITH_TIMER_TIMER_SIZE = 76;
    private static final int FONT_SIZE_FOR_UNION_FIRE_GEAR_WITHOUT_TIMER = 120;
    private static final int FONT_SIZE_FOR_UNION_FIRE_GEAR_WITH_TIMER_GEAR_SIZE = 108;
    private static final int FONT_SIZE_FOR_UNION_FIRE_GEAR_WITH_TIMER_TIMER_SIZE = 78;
    private static final int FONT_SIZE_FOR_UNION_TEMP_GEAR_WITHOUT_TIMER_BIG = 108;
    private static final int FONT_SIZE_FOR_UNION_TEMP_GEAR_WITHOUT_TIMER_SMALL = 88;
    private static final int FONT_SIZE_FOR_SEPERATE_TEMP_GEAR_WITHOUT_TIMER_TEMP_SIZE_SMALL = 72;
    private static final int FONT_SIZE_FOR_SEPERATE_TEMP_GEAR_WITHOUT_TIMER_TEMP_SIZE_BIG = 92;
    private static final int FONT_SIZE_FOR_UNION_TEMP_GEAR_WITH_TIMER_TEMP_SIZE_SMALL = 72;
    private static final int FONT_SIZE_FOR_UNION_TEMP_GEAR_WITH_TIMER_TEMP_SIZE_BIG = 92;
    private static final int FONT_SIZE_FOR_UNION_TEMP_GEAR_WITH_TIMER_TIMER_SIZE = 68;
    private static final int FONT_SIZE_FOR_SEPERATE_TEMP_GEAR_WITH_TIMER_TEMP_SIZE_SMALL = 72;
    private static final int FONT_SIZE_FOR_SEPERATE_TEMP_GEAR_WITH_TIMER_TEMP_SIZE_BIG = 92;
    private static final int FONT_SIZE_FOR_UNION_ERROR_MESSAGE = 130;
    private static final int FONT_SIZE_FOR_SEPERATE_ERROR_MESSAGE = 100;
    private static final int FONT_SIZE_TEMP_PREHEAT_SETTING_TEMP_VALUE_BIG = 92;//预热阶段
    private static final int FONT_SIZE_TEMP_PREHEAT_SETTING_TEMP_VALUE_SMALL = 74;//预热阶段
    private static final int FONT_SIZE_TEMP_PREHEAT_REAL_TEMP_VALUE = 36;//预热阶段
    private static final int FONT_SIZE_UNION_TEMP_PREHEAT_SETTING_TEMP_VALUE_BIG = 108;//预热阶段
    private static final int FONT_SIZE_UNION_TEMP_PREHEAT_SETTING_TEMP_VALUE_SMALL = 88;//预热阶段
    private static final int FONT_SIZE_UNION_TEMP_PREHEAT_REAL_TEMP_VALUE = 44;//预热阶段
    private static final int FONT_SIZE_UNION_READY_TO_COOK = 36;//
    private static final int FONT_SIZE_SEPERATE_READY_TO_COOK = 36;//
    private static final int FONT_SIZE_UNION_READY_TO_COOK_VALUE_SMALL = 72;//
    private static final int FONT_SIZE_UNION_READY_TO_COOK_VALUE_BIG= 92;//
    private static final int FONT_SIZE_SEPERATE_READY_TO_COOK_VALUE_SMALL = 72;//
    private static final int FONT_SIZE_SEPERATE_READY_TO_COOK_VALUE_BIG = 92;//

    private static final int REAL_TEMP_PROGRESS_ARC_WIDTH = 5;//真实温度圆弧宽度
    private static final int REAL_TEMP_PROGRESS_RADIUS = 54;//真实温度圆环半径


    private int upFireGearValue = 5,downFireGearValue = 6,unionFireGearValue = 7 ;
    private int upRealTempValue = 50;
    private int downRealTempValue = 60;
    private int unionRealTempValue = 60;
    private int unionTempGearValue = 80;
    private int unionTempIndicatorResID;
    private int seperateUpTempIndicatorResID,seperateDownTempIndicatorResID;
    private int unionRecipesID = R.mipmap.image_vegetable0 ,upRecipesID = R.mipmap.image_vegetable0,downRecipesID= R.mipmap.image_vegetable0;
    private int upTempGearValue = 100;
    private int downTempGearValue = 160;
    private int unionHourValue = 1,unionMinuteValue = 59;
    private int upHourValue = 1,upMinuteValue = 59;
    private int downHourValue = 1,downMinuteValue = 59;
    private String upErrorMessage = "E4",downErrorMessage = "E2" , unionErrorMessage = "E1";
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
    private Bitmap unionReadyToCookValueBpBg;
    private Bitmap seperateReadyToCookBpBg;
    private Bitmap seperateReadyToCookValueBpBg;
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



    //private OnHobRectangleCookerListener mListener;


    public Hob80RectangleCookerNew(Context context) {
        super(context);
    }

    public Hob80RectangleCookerNew(Context context, @Nullable AttributeSet attrs) {
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
        if (unionReadyToCookValueBpBg == null) {
            unionReadyToCookValueBpBg = BitmapFactory.decodeResource(getResources(), UNION_READY_TO_COOK_VALUE_BG_BITMAP_ID);
        }
        return unionReadyToCookValueBpBg;
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
        if (seperateReadyToCookValueBpBg == null) {
            seperateReadyToCookValueBpBg = BitmapFactory.decodeResource(getResources(), SEPERATE_READY_TO_COOK_VALUE_BG_BITMAP_ID);
        }
        return seperateReadyToCookValueBpBg;

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

        if (seperatePrepareTempSensorBpBg == null) {
            if(ProductManager.PRODUCT_TYPE==ProductManager.Haier){
                seperatePrepareTempSensorBpBg = BitmapFactory.decodeResource(getResources(), SEPERATE_PREPARE_TEMP_SENSOR_BG_BITMAP_ID);
            }else {
                seperatePrepareTempSensorBpBg = BitmapFactory.decodeResource(getResources(), SEPERATE_PREPARE_TEMP_SENSOR_BG_BITMAP_ID);
            }

        }
        return seperatePrepareTempSensorBpBg;
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
        if (seperateTempFinishLightBpBg == null) {
            seperateTempFinishLightBpBg = BitmapFactory.decodeResource(getResources(), SEPERATE_TEMP_FINISH_LIGHT_BG_BITMAP_ID);
        }
        return seperateTempFinishLightBpBg;

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
        if (unionTempFinishLightBpBg == null) {
            unionTempFinishLightBpBg = BitmapFactory.decodeResource(getResources(), UNION_TEMP_FINISH_LIGHT_BG_BITMAP_ID);
        }
        return unionTempFinishLightBpBg;

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
        return FONT_SIZE_SEPERATE_READY_TO_COOK;
    }

    @Override
    protected int getUnionReadyToCookValueFontSize() {
        if (unionTempGearValue >= 100) return FONT_SIZE_UNION_READY_TO_COOK_VALUE_SMALL;
        return FONT_SIZE_UNION_READY_TO_COOK_VALUE_BIG;
    }

    @Override
    protected int getSeperateUpReadyToCookValueFontSize() {
        if (upTempGearValue >= 100) return FONT_SIZE_SEPERATE_READY_TO_COOK_VALUE_SMALL;
        return FONT_SIZE_SEPERATE_READY_TO_COOK_VALUE_BIG;
    }

    @Override
    protected int getSeperateDownReadyToCookValueFontSize() {
        if (downTempGearValue >= 100) return FONT_SIZE_SEPERATE_READY_TO_COOK_VALUE_SMALL;
        return FONT_SIZE_SEPERATE_READY_TO_COOK_VALUE_BIG;
    }


    @Override
    protected int getUpTempPreheatSettingValueFontSize() {
        if (upTempGearValue >= 100) return FONT_SIZE_TEMP_PREHEAT_SETTING_TEMP_VALUE_SMALL;
        return FONT_SIZE_TEMP_PREHEAT_SETTING_TEMP_VALUE_BIG;
    }

    @Override
    protected int getUpTempPreheatRealValueFontSize() {
        return FONT_SIZE_TEMP_PREHEAT_REAL_TEMP_VALUE;
    }

    @Override
    protected int getDownTempPreheatSettingValueFontSize() {
        if (downTempGearValue >= 100) return FONT_SIZE_TEMP_PREHEAT_SETTING_TEMP_VALUE_SMALL;
        return FONT_SIZE_TEMP_PREHEAT_SETTING_TEMP_VALUE_BIG;
    }

    @Override
    protected int getDownTempPreheatRealValueFontSize() {
        return FONT_SIZE_TEMP_PREHEAT_REAL_TEMP_VALUE;
    }

    @Override
    protected int getUnionTempPreheatSettingValueFontSize() {
        if (unionTempGearValue >= 100) return FONT_SIZE_UNION_TEMP_PREHEAT_SETTING_TEMP_VALUE_SMALL;
        return FONT_SIZE_UNION_TEMP_PREHEAT_SETTING_TEMP_VALUE_BIG;
    }

    @Override
    protected int getUnionTempPreheatRealValueFontSize() {
        return FONT_SIZE_UNION_TEMP_PREHEAT_REAL_TEMP_VALUE;
    }

    @Override
    protected int getUnionReadyToCookFontSize() {
        return FONT_SIZE_UNION_READY_TO_COOK;
    }

    @Override
    protected String getUpFireGearValue() {
        if (upFireGearValue == 10) return "B";
        else return String.valueOf(upFireGearValue);
    }

    @Override
    protected String getUpTimerValue() {
        return upHourValue + ":" + String.format("%02d", upMinuteValue);
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
        return downHourValue + ":" + String.format("%02d", downMinuteValue);
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
        //return String.format("%02d", unionHourValue) + ":" + String.format("%02d", unionMinuteValue);
        return unionHourValue + ":" + String.format("%02d", unionMinuteValue);
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
        if (upRealTempValue < 20) return "20";
        else if(upRealTempValue >= upTempGearValue) return String.valueOf(upTempGearValue);
        else {
            return String.valueOf(upRealTempValue);
        }
    }

    @Override
    protected int getUpSweepAngle() {
        if (upRealTempValue < 20) return 0;
        else if(upRealTempValue >= upTempGearValue) return 360;
        else {
            float precent = Float.valueOf(upRealTempValue) / upTempGearValue;
            return (int) (360 * precent);
        }
    }

    @Override
    protected float getUpRealTempArcGap() {
        return 0.16f;
    }

    @Override
    protected float getUpRealTempTextGap() {
        return 1.23f;
    }

    @Override
    protected String getDownRealTempValue() {
        if (downRealTempValue < 20) return "20";
        else if(downRealTempValue >= downTempGearValue) return String.valueOf(downTempGearValue);
        else {
            return String.valueOf(downRealTempValue);
        }
    }

    @Override
    protected int getDownSweepAngle() {
        if (downRealTempValue < 20) return 0;
        else if(downRealTempValue >= downTempGearValue) return 360;
        else {
            float precent = Float.valueOf(downRealTempValue) / downTempGearValue;
            return (int) (360 * precent);
        }
    }

    @Override
    protected float getDownRealTempArcGap() {
        return 0.16f;
    }

    @Override
    protected float getDownRealTempTextGap() {
        return 1.23f;
    }

    @Override
    protected String getUnionRealTempValue() {
        if (unionRealTempValue < 20) return "20";
        else if(unionRealTempValue >= unionTempGearValue) return String.valueOf(unionTempGearValue);
        else {
            return String.valueOf(unionRealTempValue);
        }
    }

    @Override
    protected int getUnionSweepAngle() {
        if (unionRealTempValue < 20) return 0;
        else if(unionRealTempValue >= unionTempGearValue) return 360;
        else {
            float precent = Float.valueOf(unionRealTempValue) / unionTempGearValue;
            return (int) (360 * precent);
        }
    }

    @Override
    protected float getUnionRealTempArcGap() {
        return 0.16f;
    }

    @Override
    protected float getUnionRealTempTextGap() {
        return 1.23f;
    }


    @Override
    public int[] getCookerIDs() {
       // int[] ids = {downCookerID,upCookerID,unionCookerID};
        int[] ids = {downCookerID,upCookerID};
        return ids;
    }

    @Override
    public void setOnHobRectangleCookerListener(OnHobRectangleCookerListener listener) {
        mListener = listener;
    }

    private int index = 0;
    @Override
    public void updateCookerView(int cookerID, int workMode, int fireValue, int tempValue, int realTempValue,int hourValue, int minuteValue, int tempIndicatorID, int recipesID, String errorMessage) {

        if ((cookerID == upCookerID) && (workMode != upWorkMode)) {
            Logger.getInstance().i("updateCookerView(" + cookerID + ", From: " + getWorkModeString(upWorkMode) + " To: " + getWorkModeString(workMode) + ")");
        } else if ((cookerID == downCookerID) && (workMode != downWorkMode)) {
            Logger.getInstance().i("updateCookerView(" + cookerID + ", From: " + getWorkModeString(downWorkMode) + " To: " + getWorkModeString(workMode) + ")");
        } else if ((cookerID == unionCookerID) && (workMode != unionWorkMode)) {
            Logger.getInstance().i("updateCookerView(" + cookerID + ", From: " + getWorkModeString(unionWorkMode) + " To: " + getWorkModeString(workMode) + ")");
        }

        if (retangleHobWorkMode == HOB_RETANGLE_WORK_MODE_WORK_UNITE) {
            if (workMode != HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP) {
                index++;
                LogUtil.d("Enter:: updateCookerView  high temp------1---cookerID---->" + cookerID + "---workMode--->" + workMode);
            }
            else {
                LogUtil.d("Enter:: updateCookerView  high temp------2---cookerID---->" + cookerID + "---workMode--->" + workMode);
            }
        }
        LogUtil.d("updateCookerView index------->" + index);
        currentUpdateCookerID = cookerID;
        if (cookerID == upCookerID) {
            upWorkMode = workMode;
            upRealTempValue = realTempValue;
            upFireGearValue = fireValue;
            upTempGearValue = tempValue;
            upHourValue = hourValue;
            upMinuteValue = minuteValue;
            seperateUpTempIndicatorResID = tempIndicatorID;
            upErrorMessage = errorMessage;
            upRecipesID = recipesID;

        }else if (cookerID == downCookerID) {
            downWorkMode = workMode;
            downFireGearValue = fireValue;
            downTempGearValue = tempValue;
            downRealTempValue = realTempValue;
            downHourValue = hourValue;
            downMinuteValue = minuteValue;
            seperateDownTempIndicatorResID = tempIndicatorID;
            downErrorMessage = errorMessage;
            downRecipesID = recipesID;

        }else if (cookerID == unionCookerID){
            unionWorkMode = workMode;
            unionFireGearValue = fireValue;
            unionHourValue = hourValue;
            unionMinuteValue = minuteValue;
            unionTempGearValue = tempValue;
            unionRealTempValue = realTempValue;
            unionTempIndicatorResID = tempIndicatorID;
            unionErrorMessage = errorMessage;
            unionRecipesID = recipesID;
        }



        if (index == 2) {
            if (cookerID == 1 || cookerID == 2 || cookerID == 12) {
                LogUtil.d("Enter:: updateCookerView---cookerID--->" + cookerID + "updateCookerView---workMode--->" + workMode);

            }
            index = 0;
            if (upWorkMode == downWorkMode  && retangleHobWorkMode == HOB_RETANGLE_WORK_MODE_WORK_UNITE) {
                unionWorkMode = upWorkMode;
                unionFireGearValue = upFireGearValue;
                unionHourValue = upHourValue;
                unionMinuteValue = upMinuteValue;
                unionTempGearValue = upTempGearValue;
                unionRealTempValue = upRealTempValue;
                unionTempIndicatorResID = seperateUpTempIndicatorResID;
                unionErrorMessage = upErrorMessage;
                unionRecipesID = recipesID;
                invalidate();
            }else if (upWorkMode != downWorkMode && retangleHobWorkMode == HOB_RETANGLE_WORK_MODE_WORK_UNITE) {
                if (upWorkMode == HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP || downWorkMode == HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP) {
                    unionWorkMode = upWorkMode;
                    unionFireGearValue = upFireGearValue;
                    unionHourValue = upHourValue;
                    unionMinuteValue = upMinuteValue;
                    unionTempGearValue = upTempGearValue;
                    unionRealTempValue = upRealTempValue;
                    unionTempIndicatorResID = seperateUpTempIndicatorResID;
                    unionErrorMessage = upErrorMessage;
                    unionRecipesID = recipesID;
                    invalidate();


                }else if (upWorkMode == HOB_VIEW_WORK_MODE_POWER_OFF || downWorkMode == HOB_VIEW_WORK_MODE_POWER_OFF) {
                    retangleHobWorkMode =HOB_RETANGLE_WORK_MODE_WORK_SEPARATE;
                    invalidate();
                    LogUtil.d("Enter:: samhung samhung samhung -----upworkmode--->" + upWorkMode + "-----downWorkmode--->" + downWorkMode + "------unionworkmode---->" + unionWorkMode);

                }


            }

                LogUtil.d("updateCookerView------end--------------");
        }
        if (retangleHobWorkMode == HOB_RETANGLE_WORK_MODE_WORK_SEPARATE) {
            invalidate();
        }

        LogUtil.d("updateview--->" + upRealTempValue + "----" + downRealTempValue + "----" + unionRealTempValue);

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
        return REAL_TEMP_PROGRESS_ARC_WIDTH;
    }

    @Override
    protected int getSeperateRealTempProgressRadius() {
        return REAL_TEMP_PROGRESS_RADIUS;
    }

    @Override
    protected int getUnionRealTempProgressArcWidth() {
        return REAL_TEMP_PROGRESS_ARC_WIDTH;
    }

    @Override
    protected int getUnionRealTempProgressRadius() {
        return REAL_TEMP_PROGRESS_RADIUS;
    }

    @Override
    protected int getUnionInnerTextMaxWidth() {
        return 280;
    }

    @Override
    protected int getUnionInnerTextMaxWidthBig() {
        return 300;
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

    public int getHourValue(int cookerId) {
        if (cookerId == upCookerID) {
            return upHourValue;
        } else if (cookerId == downCookerID) {
            return downHourValue;
        } else if (cookerId == unionCookerID) {
            return unionHourValue;
        }
        return -1;
    }
    public int getMinuteValue(int cookerId) {
        if (cookerId == upCookerID) {
            return upMinuteValue;
        } else if (cookerId == downCookerID) {
            return downMinuteValue;
        } else if (cookerId == unionCookerID) {
            return unionMinuteValue;
        }
        return -1;
    }
}
