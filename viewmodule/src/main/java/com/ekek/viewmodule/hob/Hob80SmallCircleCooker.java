package com.ekek.viewmodule.hob;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.utils.ImageUtil;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.commonmodule.utils.Logger;
import com.ekek.viewmodule.R;
import com.ekek.viewmodule.contract.HobCircleCookerContract;
import com.ekek.viewmodule.product.ProductManager;
import com.ekek.viewmodule.utils.RecipesUtil;

import java.util.Locale;

public class Hob80SmallCircleCooker extends BaseCircleCookerView implements HobCircleCookerContract {
    /************need config here for every hob type*************/
    private static final int HOB_CIRCLE_COOKER_SIZE_WIDTH = 263 + 100;//350
    private static final int HOB_CIRCLE_COOKER_SIZE_HEIGHT = 315;//264 + 40;//300//
    private static final int POWER_OFF_BG_BITMAP_ID = R.mipmap.bg_hob_80_small_circle_cooker_poweroff;
    private static final int POWER_ON_WITHOUT_PAN_BG_BITMAP_ID = R.mipmap.bg_hob_80_small_circle_cooker_poweron_without_pan;
    private static final int POWER_ON_WITHOUT_PAN_BG_BITMAP_ID_haier = R.mipmap.bg_hob_60_small_circle_cooker_poweron_without_pan_haier;

    private static final int POWER_OFF_BUTTON_BITMAP_ID = R.mipmap.ic_hob_60_cooker_poweroff;

    private static final int POWER_ON_FIRE_GEAR_WITHOUT_TIMER_BG_BITMAP_ID = R.mipmap.bg_hob_80_small_circle_cooker_poweron_fire_gear_without_timer;
    private static final int POWER_ON_FIRE_GEAR_WITHOUT_TIMER_BG_BITMAP_ID_haier = R.mipmap.bg_hob_60_small_circle_cooker_poweron_fire_gear_without_timer_haier;

    private static final int POWER_ON_FIRE_GEAR_WITH_TIMER_BG_BITMAP_ID = R.mipmap.bg_hob_80_small_circle_cooker_poweron_fire_gear_with_timer;
    private static final int POWER_ON_FIRE_GEAR_WITH_TIMER_BG_BITMAP_ID_haier = R.mipmap.bg_hob_60_small_circle_cooker_poweron_fire_gear_with_timer_haier;

    private static final int HIGH_TEMP_BG_BITMAP_ID = R.mipmap.bg_hob_80_small_circle_cooker_high_temp;

    private static final int ERROR_BG_BITMAP_ID = R.mipmap.bg_hob_80_small_circle_cooker_error;
    private static final int ERROR_BG_BITMAP_ID_haier = R.mipmap.bg_hob_60_small_circle_cooker_error_haier;

    private static final int POWER_ON_TEMP_GEAR_WITHOUT_TIMER_BG_BITMAP_ID = R.mipmap.bg_hob_80_small_circle_cooker_poweron_temp_gear_word_without_timer;
    private static final int POWER_ON_TEMP_GEAR_WITHOUT_TIMER_BG_BITMAP_ID_haier = R.mipmap.bg_hob_60_small_circle_cooker_poweron_temp_gear_word_without_timer_haier;

    private static final int POWER_ON_TEMP_GEAR_WITH_TIMER_BG_BITMAP_ID = R.mipmap.bg_hob_80_small_circle_cooker_poweron_temp_gear_word_with_timer;
    private static final int POWER_ON_TEMP_GEAR_WITH_TIMER_BG_BITMAP_ID_haier = R.mipmap.bg_hob_60_small_circle_cooker_poweron_temp_gear_word_with_timer_haier;

    private static final int POWER_ON_TEMP_GEAR_INDICATOR_WITHOUT_TIMER_BG_BITMAP_ID = R.mipmap.bg_hob_80_small_circle_cooker_poweron_temp_gear_picture_without_timer;
    private static final int POWER_ON_TEMP_GEAR_INDICATOR_WITHOUT_TIMER_BG_BITMAP_ID_haier = R.mipmap.bg_hob_60_small_circle_cooker_poweron_temp_gear_picture_without_timer_haier;

    private static final int POWER_ON_TEMP_GEAR_INDICATOR_WITH_TIMER_BG_BITMAP_ID = R.mipmap.bg_hob_80_small_circle_cooker_poweron_temp_gear_picture_with_timer;
    private static final int POWER_ON_TEMP_GEAR_INDICATOR_WITH_TIMER_BG_BITMAP_ID_haier = R.mipmap.bg_hob_60_small_circle_cooker_poweron_temp_gear_picture_with_timer_haier;

    private static final int TEMP_PREPARE_SENSOR_BG_BITMAP_ID = R.mipmap.bg_hob_80_small_circle_cooker_temp_mode_preheat;
    private static final int TEMP_PREPARE_SENSOR_BG_BITMAP_ID_haier = R.mipmap.bg_hob_60_small_circle_cooker_user_prepare_haier;

    private static final int TEMP_INDICATOR_PREPARE_SENSOR_BG_BITMAP_ID = R.mipmap.bg_hob_80_small_circle_cooker_user_prepare_with_picture;
    private static final int TEMP_INDICATOR_PREPARE_SENSOR_BG_BITMAP_ID_haier= R.mipmap.bg_hob_60_small_circle_cooker_user_prepare_with_picture_haier;

    private static final int TEMP_SENSOR_READY_BG_BITMAP_ID = R.mipmap.bg_hob_80_small_circle_cooker_ready_to_cook;
    private static final int TEMP_SENSOR_READY_BG_BITMAP_ID_haier = R.mipmap.bg_hob_60_small_circle_cooker_ready_to_cook_haier;

    private static final int TEMP_SENSOR_READY_VALUE_BG_BITMAP_ID = R.mipmap.bg_hob_80_small_circle_cooker_ready_to_cook_value;

    private static final int TEMP_WORK_FINISH_LIGHT_BG_BITMAP_ID = R.mipmap.bg_hob_80_small_cooker_add_10_minutes_tips_light;
    private static final int TEMP_WORK_FINISH_LIGHT_BG_BITMAP_ID_haier= R.mipmap.bg_hob_60_small_cooker_keep_warm_tips_light_haier;

    private static final int TEMP_WORK_FINISH_DARK_BG_BITMAP_ID = R.mipmap.bg_small_cooker_keep_warm_tips_dark;
    private static final int TEMP_WORK_FINISH_DARK_BG_BITMAP_ID_haier = R.mipmap.bg_hob_60_small_cooker_keep_warm_tips_dark_haier;

    private static final int TEMP_WORK_RECIPES_BG_BITMAP_ID = R.mipmap.bg_hob_80_small_cooker_recipes_temp_mode;

    private static final int FONT_SIZE_FOR_FIRE_GEAR_WITHOUT_TIMER = 88;  // 110
    private static final int FONT_SIZE_FOR_FIRE_GEAR_WITH_TIMER = 88; // 100
    private static final int FONT_SIZE_FOR_TIMER = 45;  // 时间
    private static final int FONT_SIZE_FOR_TIMER_SMALL = 45;  // 时间
    private static final int FONT_SIZE_FOR_TIMER_WITH_INDICATOR = 45;
    private static final int FONT_SIZE_FOR_TIMER_WITH_INDICATOR_SMALL = 45;
    private static final int FONT_SIZE_FOR_ERROR = 120;
    private static final int FONT_SIZE_FOR_TEMP_GEAR_WITHOUT_TIMER_BIG = 80;//两位数  不带时间显示
    private static final int FONT_SIZE_FOR_TEMP_GEAR_WITHOUT_TIMER_SMALL = 65;//三位数
    private static final int FONT_SIZE_FOR_TEMP_GEAR_WITH_TIMER_BIG = 80;//两位数  带时间显示
    private static final int FONT_SIZE_FOR_TEMP_GEAR_WITH_TIMER_SMALL = 65;//三位数  55
    private static final int FONT_SIZE_FOR_KEEP_WARM_AND_ADD_TEN_MINUTE = 60;
    private static final int FONT_SZIE_FOR_READY_TO_COOK = 32;
    private static final int FONT_SIZE_TEMP_PREHEAT_SETTING_TEMP_VALUE_BIG = 84;//预热阶段
    private static final int FONT_SIZE_TEMP_PREHEAT_SETTING_TEMP_VALUE_SMALL = 70;//预热阶段
    private static final int FONT_SIZE_TEMP_PREHEAT_REAL_TEMP_VALUE = 32;//预热阶段
    private static final int FONT_SZIE_FOR_TEMP_INDICATOR = 24;
    private static final int PADDING_FOR_TEMP_INDICATOR = 6;

    private static final int REAL_TEMP_PROGRESS_ARC_WIDTH = 4;//真实温度圆弧宽度
    private static final int REAL_TEMP_PROGRESS_RADIUS = 48;//真实温度圆环半径

    private int fireGearValue = 5,tempGearValue = 260;
    private int realTempValue = 42;
    private int hourValue = -1,minuteValue = -1;
    private int tempIndicatorID,recipesID;
    private boolean recipeChanged = true;
    private String errorMessage = "E1";
    private Bitmap powerOffBgBp;
    private Bitmap powerOnWithoutPanBgBp;
    private Bitmap powerOffButonBp;
    private Bitmap powerOnFireGearWithoutTimerBgBp;
    private Bitmap powerOnFireGearWithTimerBgBp;
    private Bitmap highTempBgBp;
    private Bitmap errorBgBp;
    private Bitmap powerOnTempGearWithoutTimerBgBp;
    private Bitmap powerOnTempGearWithTimerBgBp;
    private Bitmap powerOnTempGearIndicatorWithoutTimerBgBp;
    private Bitmap powerOnTempGearIndicatorWithTimerBgBp;
    private Bitmap tempIndicatorBp;
    private Bitmap tempPrepareSensorBgBp;
    private Bitmap tempIndicatorPrepareSensorBgBp;
    private Bitmap tempSensorReadyBgBp;
    private Bitmap tempSensorReadyValueBitmap;
    private Bitmap tempWorkFinishLightBgBp;
    private Bitmap tempWorkFinishDarkBgBp;
    private Bitmap recipesPicBp;
    private Bitmap recipesBgBp;

    public Hob80SmallCircleCooker(Context context) {
        super(context);
    }

    public Hob80SmallCircleCooker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected Bitmap getPowerOffBgBitmap() {
        if (powerOffBgBp == null) {
            powerOffBgBp = BitmapFactory.decodeResource(getResources(), POWER_OFF_BG_BITMAP_ID);
        }
        return powerOffBgBp;
    }

    @Override
    protected Bitmap getPowerOnWithoutPanBitmap() {
        if (powerOnWithoutPanBgBp == null) {
            if(ProductManager.PRODUCT_TYPE== ProductManager.Haier){
                powerOnWithoutPanBgBp = BitmapFactory.decodeResource(getResources(), POWER_ON_WITHOUT_PAN_BG_BITMAP_ID_haier);
            }else {
                powerOnWithoutPanBgBp = BitmapFactory.decodeResource(getResources(), POWER_ON_WITHOUT_PAN_BG_BITMAP_ID);
            }

        }
        return powerOnWithoutPanBgBp;
    }

    @Override
    protected Bitmap getPowerOffButtonBitmap() {
        if (powerOffButonBp == null) {
            powerOffButonBp = BitmapFactory.decodeResource(getResources(), POWER_OFF_BUTTON_BITMAP_ID);
        }
        return powerOffButonBp;
    }

    @Override
    protected Bitmap getPowerOnFireGearWithoutTimerBitmap() {
        if (powerOnFireGearWithoutTimerBgBp == null) {
            if(ProductManager.PRODUCT_TYPE==ProductManager.Haier){
                powerOnFireGearWithoutTimerBgBp = BitmapFactory.decodeResource(getResources(), POWER_ON_FIRE_GEAR_WITHOUT_TIMER_BG_BITMAP_ID_haier);
            }else {
                powerOnFireGearWithoutTimerBgBp = BitmapFactory.decodeResource(getResources(), POWER_ON_FIRE_GEAR_WITHOUT_TIMER_BG_BITMAP_ID);
            }

        }
        return powerOnFireGearWithoutTimerBgBp;
    }

    @Override
    protected Bitmap getPowerOnFireGearWithTimerBitmap() {
        if (powerOnFireGearWithTimerBgBp == null) {
            if(ProductManager.PRODUCT_TYPE==ProductManager.Haier){
                powerOnFireGearWithTimerBgBp = BitmapFactory.decodeResource(getResources(), POWER_ON_FIRE_GEAR_WITH_TIMER_BG_BITMAP_ID_haier);
            }else {
                powerOnFireGearWithTimerBgBp = BitmapFactory.decodeResource(getResources(), POWER_ON_FIRE_GEAR_WITH_TIMER_BG_BITMAP_ID);
            }

        }
        return powerOnFireGearWithTimerBgBp;
    }

    @Override
    protected Bitmap getHighTempBitmap() {
        if (highTempBgBp == null) {
            highTempBgBp = BitmapFactory.decodeResource(getResources(), HIGH_TEMP_BG_BITMAP_ID);
        }
        return highTempBgBp;
    }

    @Override
    protected Bitmap getErrorBitmap() {
        if (errorBgBp == null) {
            if(ProductManager.PRODUCT_TYPE==ProductManager.Haier){
                errorBgBp = BitmapFactory.decodeResource(getResources(), ERROR_BG_BITMAP_ID_haier);
                LogUtil.d("set ekek error bitmap haier");
            }else {
                errorBgBp = BitmapFactory.decodeResource(getResources(), ERROR_BG_BITMAP_ID);
                LogUtil.d("set ekek error bitmap cata");
            }

        }
        return errorBgBp;
    }

    @Override
    protected Bitmap getTempGearWithoutTimerBitmap() {
        if (powerOnTempGearWithoutTimerBgBp == null) {
            if(ProductManager.PRODUCT_TYPE==ProductManager.Haier){
                powerOnTempGearWithoutTimerBgBp = BitmapFactory.decodeResource(getResources(), POWER_ON_TEMP_GEAR_WITHOUT_TIMER_BG_BITMAP_ID_haier);
            }else {
                powerOnTempGearWithoutTimerBgBp = BitmapFactory.decodeResource(getResources(), POWER_ON_TEMP_GEAR_WITHOUT_TIMER_BG_BITMAP_ID);
            }

        }
        return powerOnTempGearWithoutTimerBgBp;
    }

    @Override
    protected Bitmap getTempGearWithTimerBitmap() {
        if (powerOnTempGearWithTimerBgBp == null) {
            if(ProductManager.PRODUCT_TYPE==ProductManager.Haier){
                powerOnTempGearWithTimerBgBp = BitmapFactory.decodeResource(getResources(), POWER_ON_TEMP_GEAR_WITH_TIMER_BG_BITMAP_ID_haier);
            }else {
                powerOnTempGearWithTimerBgBp = BitmapFactory.decodeResource(getResources(), POWER_ON_TEMP_GEAR_WITH_TIMER_BG_BITMAP_ID);
            }

        }
        return powerOnTempGearWithTimerBgBp;
    }

    @Override
    protected Bitmap getTempGearIndicatorWithoutTimerBitmap() {
        if (powerOnTempGearIndicatorWithoutTimerBgBp == null) {
            if(ProductManager.PRODUCT_TYPE==ProductManager.Haier){
                powerOnTempGearIndicatorWithoutTimerBgBp = BitmapFactory.decodeResource(getResources(), POWER_ON_TEMP_GEAR_INDICATOR_WITHOUT_TIMER_BG_BITMAP_ID_haier);
            }else {
                powerOnTempGearIndicatorWithoutTimerBgBp = BitmapFactory.decodeResource(getResources(), POWER_ON_TEMP_GEAR_INDICATOR_WITHOUT_TIMER_BG_BITMAP_ID);
            }

        }
        return powerOnTempGearIndicatorWithoutTimerBgBp;
    }

    @Override
    protected Bitmap getTempGearIndicatorWithTimerBitmap() {
        if (powerOnTempGearIndicatorWithTimerBgBp == null) {
            if(ProductManager.PRODUCT_TYPE==ProductManager.Haier){
                powerOnTempGearIndicatorWithTimerBgBp = BitmapFactory.decodeResource(getResources(), POWER_ON_TEMP_GEAR_INDICATOR_WITH_TIMER_BG_BITMAP_ID_haier);
            }else {
                powerOnTempGearIndicatorWithTimerBgBp = BitmapFactory.decodeResource(getResources(), POWER_ON_TEMP_GEAR_INDICATOR_WITH_TIMER_BG_BITMAP_ID);
            }

        }
        return powerOnTempGearIndicatorWithTimerBgBp;
    }

    @Override
    protected Bitmap getTempIndicatorBitmap() {
        /*if (tempIndicatorBp == null) {
            tempIndicatorBp = BitmapFactory.decodeResource(getResources(), TEMP_GEAR_INDICATOR_BG_BIGMAP_ID);
        }
        return tempIndicatorBp;*/
        if (tempIndicatorBp == null) {
            //tempIndicatorBp = ImageUtil.decodeTempIdentifyBitmap(getContext(),TEMP_GEAR_INDICATOR_BG_BIGMAP_ID,getTempGearIndicatorWithoutTimerBitmap().getWidth(), (int) (getTempGearIndicatorWithoutTimerBitmap().getHeight() * 0.4));
            tempIndicatorBp = ImageUtil.decodeTempIdentifyBitmap(
                    getContext(),
                    tempIndicatorID,
                    getTempGearIndicatorWithoutTimerBitmap().getWidth(),
                    (int) (getTempGearIndicatorWithoutTimerBitmap().getHeight() * 0.25));//0.4

        }
        return tempIndicatorBp;
    }

    @Override
    protected String getTempIndicatorString() {
        int r = GlobalVars.getInstance().getTempIndicatorString(tempGearValue);
        return r <= 0 ? "" : getContext().getResources().getString(r);
    }

    @Override
    protected int getTempIndicatorStringFontSize() {
        return FONT_SZIE_FOR_TEMP_INDICATOR;
    }

    @Override
    protected int getTempIndicatorStringPadding() {
        return PADDING_FOR_TEMP_INDICATOR;
    }

    @Override
    protected Bitmap getTempIndicatorBitmapWithTimer() {
        if (tempIndicatorBp == null) {
            //tempIndicatorBp = ImageUtil.decodeTempIdentifyBitmap(getContext(),TEMP_GEAR_INDICATOR_BG_BIGMAP_ID,getTempGearIndicatorWithoutTimerBitmap().getWidth(), (int) (getTempGearIndicatorWithoutTimerBitmap().getHeight() * 0.35));
            tempIndicatorBp = ImageUtil.decodeTempIdentifyBitmap(
                    getContext(),
                    tempIndicatorID,
                    getTempGearIndicatorWithoutTimerBitmap().getWidth(),
                    (int) (getTempGearIndicatorWithoutTimerBitmap().getHeight() * 0.25));

        }
        return tempIndicatorBp;
    }

    @Override
    protected Bitmap getTempPrepareSensorBitmap() {
        if (tempPrepareSensorBgBp == null) {
            if(ProductManager.PRODUCT_TYPE==ProductManager.Haier){
                tempPrepareSensorBgBp = BitmapFactory.decodeResource(getResources(), TEMP_PREPARE_SENSOR_BG_BITMAP_ID_haier);
            }else {
                tempPrepareSensorBgBp = BitmapFactory.decodeResource(getResources(), TEMP_PREPARE_SENSOR_BG_BITMAP_ID);
            }

        }
        return tempPrepareSensorBgBp;
    }

    @Override
    protected Bitmap getTempIndicatorPrepareSensorBitmap() {
        if (tempIndicatorPrepareSensorBgBp == null) {
            if(ProductManager.PRODUCT_TYPE==ProductManager.Haier){
                tempIndicatorPrepareSensorBgBp = BitmapFactory.decodeResource(getResources(), TEMP_INDICATOR_PREPARE_SENSOR_BG_BITMAP_ID_haier);
            }else {
                tempIndicatorPrepareSensorBgBp = BitmapFactory.decodeResource(getResources(), TEMP_INDICATOR_PREPARE_SENSOR_BG_BITMAP_ID);
            }

        }
        return tempIndicatorPrepareSensorBgBp;
    }

    @Override
    protected Bitmap getTempSensorReadyBitmap() {
        if (tempSensorReadyBgBp == null) {
            if(ProductManager.PRODUCT_TYPE==ProductManager.Haier){
                tempSensorReadyBgBp = BitmapFactory.decodeResource(getResources(), TEMP_SENSOR_READY_BG_BITMAP_ID_haier);
            }else {
                tempSensorReadyBgBp = BitmapFactory.decodeResource(getResources(), TEMP_SENSOR_READY_BG_BITMAP_ID);
            }

        }
        return tempSensorReadyBgBp;

    }

    @Override
    protected Bitmap getTempSensorReadyValueBitmap() {
        if (tempSensorReadyValueBitmap == null) {
            tempSensorReadyValueBitmap = BitmapFactory.decodeResource(getResources(), TEMP_SENSOR_READY_VALUE_BG_BITMAP_ID);
        }

        return tempSensorReadyValueBitmap;
    }

    @Override
    protected Bitmap getTempWorkFinishLightBitmap() {
        if (tempWorkFinishLightBgBp == null) {
            if(ProductManager.PRODUCT_TYPE==ProductManager.Haier){
                tempWorkFinishLightBgBp = BitmapFactory.decodeResource(getResources(), TEMP_WORK_FINISH_LIGHT_BG_BITMAP_ID_haier);
            }else {
                tempWorkFinishLightBgBp = BitmapFactory.decodeResource(getResources(), TEMP_WORK_FINISH_LIGHT_BG_BITMAP_ID);
            }

        }
        return tempWorkFinishLightBgBp;
    }

    @Override
    protected Bitmap getTempWorkFinishDarkBitmap() {
        if (tempWorkFinishDarkBgBp == null) {
            if(ProductManager.PRODUCT_TYPE==ProductManager.Haier){
                tempWorkFinishDarkBgBp = BitmapFactory.decodeResource(getResources(), TEMP_WORK_FINISH_DARK_BG_BITMAP_ID_haier);
            }else {
                tempWorkFinishDarkBgBp = BitmapFactory.decodeResource(getResources(), TEMP_WORK_FINISH_DARK_BG_BITMAP_ID);
            }

        }
        return tempWorkFinishDarkBgBp;
    }

    @Override
    protected Bitmap getRecipesPicBitmap() {
        if (recipeChanged) {
            if (recipesPicBp != null) {
                recipesPicBp.recycle();
                recipesPicBp = null;
            }
            recipeChanged = false;
        }
        if (recipesPicBp == null) {
            //recipesPicBp = BitmapFactory.decodeResource(getResources(), recipesID);
            recipesPicBp = ImageUtil.decodeBitmapForScale(getContext(), RecipesUtil.getRecipesImageForCircleCooker(recipesID),0.75f);
        }
        return recipesPicBp;
    }

    @Override
    protected Bitmap getRecipesBgBitmap() {
        if (recipesBgBp == null) {
            if (RecipesUtil.getRecipesImageForCircleCooker(recipesID) == R.mipmap.food_recipe_customize_default_image) {
                recipesBgBp = BitmapFactory.decodeResource(getResources(), TEMP_WORK_FINISH_LIGHT_BG_BITMAP_ID);
            }else {
                recipesBgBp = BitmapFactory.decodeResource(getResources(), TEMP_WORK_RECIPES_BG_BITMAP_ID);
            }
        }
        return recipesBgBp;
    }

    @Override
    protected int getFireGearWithoutTimerFontSize() {
        return FONT_SIZE_FOR_FIRE_GEAR_WITHOUT_TIMER;
    }

    @Override
    protected int getFireGearWithTimerFontSize() {
        return FONT_SIZE_FOR_FIRE_GEAR_WITH_TIMER;
    }

    @Override
    protected int getTimerFontSize() {
        return FONT_SIZE_FOR_TIMER;
    }

    @Override
    protected int getTimerFontSizeSmall() {
        return FONT_SIZE_FOR_TIMER_SMALL;
    }

    @Override
    protected int getTimerWithIndicatorTimerFontSize() {
        return FONT_SIZE_FOR_TIMER_WITH_INDICATOR;
    }

    @Override
    protected int getTimerWithIndicatorTimerFontSizeSmall() {
        return FONT_SIZE_FOR_TIMER_WITH_INDICATOR_SMALL;
    }

    @Override
    protected int getErrorFontSize() {
        return FONT_SIZE_FOR_ERROR;
    }

    @Override
    protected int getTempGearWithoutTimerFontSize() {
        if (tempGearValue < 100) return FONT_SIZE_FOR_TEMP_GEAR_WITHOUT_TIMER_BIG;
        else return FONT_SIZE_FOR_TEMP_GEAR_WITHOUT_TIMER_SMALL;
    }

    @Override
    protected int getTempGearWithTimerFontSize() {
        if (tempGearValue < 100) return FONT_SIZE_FOR_TEMP_GEAR_WITH_TIMER_BIG;
        else return FONT_SIZE_FOR_TEMP_GEAR_WITH_TIMER_SMALL;
    }

    @Override
    protected int getKeepWarmAndAddTenMinuteFontSize() {
        return FONT_SIZE_FOR_KEEP_WARM_AND_ADD_TEN_MINUTE;
    }

    @Override
    protected int getReadyToCookFontSize() {
        return FONT_SZIE_FOR_READY_TO_COOK;
    }

    @Override
    protected int getTempPreheatSettingValueFontSize() {
        if (tempGearValue >= 100) return FONT_SIZE_TEMP_PREHEAT_SETTING_TEMP_VALUE_SMALL;
        return FONT_SIZE_TEMP_PREHEAT_SETTING_TEMP_VALUE_BIG;
    }

    @Override
    protected int getTempPreheatRealValueFontSize() {
        return FONT_SIZE_TEMP_PREHEAT_REAL_TEMP_VALUE;
    }


    @Override
    protected String getFireGearValue() {
        if (fireGearValue == 10) return "B";
        else return String.valueOf(fireGearValue);
    }

    @Override
    protected String getTempGearValue() {
        return String.valueOf(tempGearValue);
    }

    @Override
    protected String getTimerValue() {
        String hourFormat = "%02d";
        if (workMode == HOB_VIEW_WORK_MODE_FIRE_GEAR_WITH_TIMER || hourValue < 10) {
            hourFormat = "%d";
        }
        return String.format(Locale.ENGLISH, hourFormat, hourValue) + ":" + String.format(Locale.ENGLISH, "%02d", minuteValue);
    }

    @Override
    protected String getErrorMessage() {
        return errorMessage;
    }

    @Override
    protected String getRealTempValue() {
        if (realTempValue < 20) return "20";
        else if(realTempValue >= tempGearValue) return String.valueOf(tempGearValue);
        else {
            return String.valueOf(realTempValue);
        }
    }

    @Override
    protected int getSweepAngle() {
        if (realTempValue < 20) return 0;
        else if(realTempValue >= tempGearValue) return 360;
        else {
            float precent = Float.valueOf(realTempValue) / tempGearValue;
            return (int) (360 * precent);
        }


    }


    @Override
    protected float getRealTempArcGap() {
        return 0.16f;
    }//0.18

    @Override
    protected float getRealTempTextGap() {
        return 1.25f;
    }//1.33f

    @Override
    protected int getCookerViewWidth() {
        return HOB_CIRCLE_COOKER_SIZE_WIDTH;
    }

    @Override
    protected int getCookerViewHeight() {
        return HOB_CIRCLE_COOKER_SIZE_HEIGHT;
    }

    @Override
    protected int getRealTempProgressArcWidth() {
        return REAL_TEMP_PROGRESS_ARC_WIDTH;
    }

    @Override
    protected int getRealTempProgressRadius() {
        return REAL_TEMP_PROGRESS_RADIUS;
    }

    @Override
    protected int getInnerTextMaxWidth() {
        return 200;
    }

    @Override
    protected int getInnerTextMaxWidthBig() {
        return 200;
    }

    @Override
    protected void recycle() {
        if (powerOnWithoutPanBgBp != null) {
            powerOnWithoutPanBgBp.recycle();
            powerOnWithoutPanBgBp = null;
        }
        if (powerOffButonBp != null) {
            powerOffButonBp.recycle();
            powerOffButonBp = null;
        }
        if (powerOnFireGearWithoutTimerBgBp != null) {
            powerOnFireGearWithoutTimerBgBp.recycle();
            powerOnFireGearWithoutTimerBgBp = null;
        }
        if (powerOnFireGearWithTimerBgBp != null) {
            powerOnFireGearWithTimerBgBp.recycle();
            powerOnFireGearWithTimerBgBp = null;
        }
        if (highTempBgBp != null) {
            highTempBgBp.recycle();
            highTempBgBp = null;
        }
        if (errorBgBp != null) {
            errorBgBp.recycle();
            errorBgBp = null;
        }
        if (powerOnTempGearWithoutTimerBgBp != null) {
            powerOnTempGearWithoutTimerBgBp.recycle();
            powerOnTempGearWithoutTimerBgBp = null;
        }
        if (powerOnTempGearWithTimerBgBp != null) {
            powerOnTempGearWithTimerBgBp.recycle();
            powerOnTempGearWithTimerBgBp = null;
        }
        if (powerOnTempGearIndicatorWithoutTimerBgBp != null) {
            powerOnTempGearIndicatorWithoutTimerBgBp.recycle();
            powerOnTempGearIndicatorWithoutTimerBgBp = null;
        }
        if (powerOnTempGearIndicatorWithTimerBgBp != null) {
            powerOnTempGearIndicatorWithTimerBgBp.recycle();
            powerOnTempGearIndicatorWithTimerBgBp = null;
        }
        if (tempIndicatorBp != null) {
            tempIndicatorBp.recycle();
            tempIndicatorBp = null;
        }
        if (tempPrepareSensorBgBp != null) {
            tempPrepareSensorBgBp.recycle();
            tempPrepareSensorBgBp = null;
        }
        if (tempIndicatorPrepareSensorBgBp != null) {
            tempIndicatorPrepareSensorBgBp.recycle();
            tempIndicatorPrepareSensorBgBp = null;
        }
        if (tempSensorReadyBgBp != null) {
            tempSensorReadyBgBp.recycle();
            tempSensorReadyBgBp = null;
        }
        if (tempSensorReadyValueBitmap != null) {
            tempSensorReadyValueBitmap.recycle();
            tempSensorReadyValueBitmap = null;
        }
        if (tempWorkFinishLightBgBp != null) {
            tempWorkFinishLightBgBp.recycle();
            tempWorkFinishLightBgBp = null;
        }
        if (tempWorkFinishDarkBgBp != null) {
            tempWorkFinishDarkBgBp.recycle();
            tempWorkFinishDarkBgBp = null;
        }
        if (recipesPicBp != null) {
            recipesPicBp.recycle();
            recipesPicBp = null;
        }
        if (recipesBgBp != null) {
            recipesBgBp.recycle();
            recipesBgBp = null;
        }
        System.gc();
    }

    @Override
    protected void recycleIndicatorBitmap() {
        if (tempIndicatorBp != null) {
            tempIndicatorBp.recycle();
            tempIndicatorBp = null;
        }
    }

    @Override
    protected void recycleRecipesBitmap() {
        if (recipesPicBp != null) {
            recipesPicBp.recycle();
            recipesPicBp = null;
            recipesID = 0;
            recipeChanged = true;
        }
    }


    public int getHourValue() {
        return hourValue;
    }

    public int getMinuteValue() {
        return minuteValue;
    }

    @Override
    protected void upateUI(int workMode, int fireValue, int tempValue, int realTempValue,int hourValue, int minuteValue, int tempIndicatorID, int recipesID,int recipesShowOrder,String errorMessage) {

        if (this.workMode != workMode) {
            Logger.getInstance().i("UpdateUI(Prev:" + getWorkModeString(this.workMode) + ", Now:" + getWorkModeString(workMode) + ") CookerID=" + cookerID);
        }
        this.workMode = workMode;
        this.fireGearValue = fireValue;
        this.tempGearValue = tempValue;
        this.realTempValue = realTempValue;
        this.hourValue = hourValue;
        this.minuteValue = minuteValue;
        this.tempIndicatorID = tempIndicatorID;
        if (this.recipesID != recipesID) {
            recipeChanged = true;
        }
        this.recipesID = recipesID;
        this.errorMessage = errorMessage;
        this.tempWorkRecipesProgress = recipesShowOrder;
        LogUtil.d("Enter:: cirecle updateUI------->" + workMode);
        invalidate();
    }

    @Override
    public int getCookerID() {
        return cookerID;
    }

    @Override
    public void setCookerID(int id) {

    }

    @Override
    public void setOnHobCircleCookerListener(OnHobCircleCookerListener listener) {
        mListener = listener;
    }

    @Override
    public void updateCookerView(int workMode, int fireValue, int tempValue, int realTempValue,int hourValue, int minuteValue, int tempIndicatorID, int recipesID,int recipesShowOrder,String errorMessage) {
        upateUI(workMode, fireValue, tempValue,realTempValue, hourValue, minuteValue, tempIndicatorID, recipesID,recipesShowOrder,errorMessage);

    }
}
