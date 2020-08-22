package com.ekek.viewmodule.hob;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.viewmodule.R;

public class CircleCookerView extends View {
    private static final int COOKER_MODE_POWER_OFF = 0;//炉头关火模式
    private static final int COOKER_MODE_FIRE_GEAR_WITH_TIMER = 1;//档位定时模式
    private static final int COOKER_MODE_FIRE_GEAR_WITHOUT_TIMER = 100;//档位没定时模式
    private static final int COOKER_MODE_FAST_TEMPERATURE_GEAR_WITH_TIMER = 2;//普通控温定时模式
    private static final int COOKER_MODE_FAST_TEMPERATURE_GEAR_WITHOUT_TIMER = 200;//普通控温没定时模式
    private static final int COOKER_MODE_PRECISE_TEMPERATURE_GEAR_WITH_TIMER = 3;//精确控温定时模式
    private static final int COOKER_MODE_PRECISE_TEMPERATURE_GEAR_WITHOUT_TIMER = 300;//精确控温没定时模式
    private static final int COOKER_MODE_ERROR_OCCUR = 4;//错误代码模式，显示错误代码
    private static final int COOKER_MODE_HIGH_TEMP = 5;//炉头高温模式
    private static final int COOKER_MODE_POWERON_WITHOUT_PAN = 6;//炉头开火没锅模式
    private static final int COOKER_MODE_TEMP_GEAR_WORD_WITH_TIMER = 7;//温控模式，文字+定时器
    private static final int COOKER_MODE_TEMP_GEAR_WORD_WITHOUT_TIMER = 700;//温控模式，文字-定时器
    private static final int COOKER_MODE_TEMP_GEAR_PICTURE_WITH_TIMER = 8;//温控模式，图片标识+定时器
    private static final int COOKER_MODE_TEMP_GEAR_PICTURE_WITHOUT_TIMER = 800;//温控模式，图片标识-定时器
    private static final int COOKER_MODE_KEEP_WARM_DARK_TIPS = 9;
    private static final int COOKER_MODE_KEEP_WARM_LIGHT_TIPS = 900;
    private static final int COOKER_MODE_USER_PREPARE_TO_COOK = 10000;
    private static final int COOKER_MODE_USER_PREPARE_TO_COOK_WITH_PICTURE = 10001;
    private static final int COOKER_MODE_USER_READY_TO_COOK = 10002;
    private static final int COOKER_MODE_COOKER_ERROR_OCURR = 10003;//炉头出现错误，显示错误代码
    private static final int COOKER_MODE_COOKER_STATUS_ABNORMAL_NO_PAN = 10004;//状态异常，指的是无锅，或者高温


    private static final int COOKER_VIEW_INCREASE_SIZE = 50;//比炉头尺寸大小增加50

    /*炉头尺寸类型*/
    private static final int COOKER_SIZE_BIG_COOKER = 0;
    private static final int COOKER_SIZE_MEDIUM_COOKER = 1;
    private static final int COOKER_SIZE_SMALL_COOKER = 2;

    private int cookerMode = COOKER_MODE_POWER_OFF;
    private int lastCookerMode = cookerMode;

    private RectF mViewRect;
    private Bitmap mCookerPowerOffBgBp;//炉头关火火背景
    private Bitmap mCookerPowerOnFireGearWithTimerBgBp;//炉头开火背景(fire + timer)
    private Bitmap mCookerPowerOnFireGearWithoutTimerBgBp;//炉头开火背景(fire - timer)
    private Bitmap mCookerPoweronTempGearWordWithTimerBgBp;//温控模式，文字+定时器
    private Bitmap mCookerPoweronTempGearWordWithoutTimerBgBp;//温控模式，文字-定时器
    private Bitmap mCookerPoweronTempGearPictureWithTimerBgBp;//温控模式，图片+定时器
    private Bitmap mCookerPoweronTempGearPictureWithoutTimerBgBp;//温控模式，图片-定时器
    private Bitmap mCookerKeepWarmDarkTipsBgBp;//keep warm 暗 背景
    private Bitmap mCookerKeepWarmLightTipsBgBp;//keep warm 亮 背景
    private Bitmap mCookerUserPrepareToCookBgBp;//prepare to cook 背景
    private Bitmap mCookerUserPrepareToCookWithPictureBgBp;//prepare to cook 背景
    private Bitmap mCookerUserReadyToCookBgBp;//ready to cook 背景
    private Bitmap mCookerErrorOcurrBgBp;// error ocurr 背景


    private Bitmap mCookerHighTempBg;//炉头高温背景
    private Bitmap mCookerPoweronWithoutPanBg;//炉头开火没锅背景

    private Bitmap mCookerPowerOnWithErrorBackgroundBitmap;//炉头出现错误背景
    private Bitmap mCookerHighTempBitmap;//炉头高温背景
    private Bitmap powerOffBtnBitmap;//关机按键

    private int mCookerSize;//炉头尺寸类型
    private int mCookerPowerOffBpID;//炉头关火背景ID
    private int mCookerPowerOnFireGearWithTimerBpID;//炉头开火背景ID（档位+定时）:有定时
    private int mCookerPowerOnFireGearWithoutTimerBpID;//炉头开火背景ID（档位-定时）：没定时
    private int mPoweroffBtnBpID;//关机按键ID
    private int mCookerHighTempBgID;//炉头高温背景ID
    private int mCookerPoweronWithoutPanBgID;//炉头开火没锅背景ID
    private int mCookerPoweronTempGearWordWithTimerBgID;////温控模式，文字+定时器
    private int mCookerPoweronTempGearWordWithoutTimerBgID;//温控模式，文字-定时器
    private int mCookerPoweronTempGearPictureWithTimerBgID;//温控模式，图片+定时器
    private int mCookerPoweronTempGearPictureWithoutTimerBgID;//温控模式，图片-定时器
    private int mCookerKeepWarmDarkTipsBgID;//keep warm 暗 背景ID
    private int mCookerKeepWarmLightTipsBgID;//keep warm 亮 背景ID
    private int mCookerUserPrepareToCookBgID;//prepare to cook ID
    private int mCookerUserPrepareToCookWithPictureBgID;//prepare to cook ID
    private int mCookerUserReadyToCookBgID;//ready to cook ID
    private int mCookerErrorOcurrBgID;// error ocurr id

    private int mViewWidth,mViewHeight;

    private Paint fireGearPaint, fireGearTimerPaint, tempGearPaint ,tempGearTimerPaint ,errorPaint;
    private Paint.FontMetrics fontMetrics;


    private String fireGearValue = "5";//档位值
    private String timerValue = "00:00";//定时器的值
    private String tempGearValue = "220";
    private int tempIdentifyResourceID = -1;
    private String errorMessage = "";//0 no error

    @NonNull
    private OnCircleCookerViewListener mListener;

    private static final long TIME_HIGH_TEMP_FINISH = 1000 * 10;//10s
    private static final long TIME_FLASH_KEEP_WARM = 1000 * 1;//10s
    private static final int HANDLER_HIGH_TEMP_FINISH = 0;//高温标志消失
    private static final int HANDLER_FLASH_KEEP_WARM_TIPS = 1;//

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_HIGH_TEMP_FINISH:
                    cookerMode = COOKER_MODE_POWER_OFF;
                    invalidate();
                    break;
                case HANDLER_FLASH_KEEP_WARM_TIPS:
                    if (cookerMode == COOKER_MODE_KEEP_WARM_DARK_TIPS)setCookerMode(COOKER_MODE_KEEP_WARM_LIGHT_TIPS);
                    else if (cookerMode == COOKER_MODE_KEEP_WARM_LIGHT_TIPS)setCookerMode(COOKER_MODE_KEEP_WARM_DARK_TIPS);
                    handler.sendEmptyMessageDelayed(HANDLER_FLASH_KEEP_WARM_TIPS,TIME_FLASH_KEEP_WARM);
                    break;
            }
        }
    };


    public CircleCookerView(Context context) {
        this(context,null);
    }

    public CircleCookerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleCookerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.viewmodule_CircleCookerView);
        mCookerPowerOffBpID = a.getResourceId(R.styleable.viewmodule_CircleCookerView_viewmodule_cooker_poweroff_background,R.mipmap.bg_big_circle_cooker_poweroff);
        //LogUtil.d("mCookerPowerOffBpID----->" + mCookerPowerOffBpID);
        //LogUtil.d("R.mipmap.bg_big_circle_cooker_poweroff----->" + R.mipmap.bg_big_circle_cooker_poweroff);
        //LogUtil.d("R.mipmap.bg_big_circle_cooker_poweroff----->" + R.mipmap.bg_big_circle_cooker_80_power_off);
        mCookerPowerOnFireGearWithTimerBpID = a.getResourceId(R.styleable.viewmodule_CircleCookerView_viewmodule_cooker_poweron_fire_gear_with_timer_background,R.mipmap.bg_big_circle_cooker_poweron_fire_gear_with_timer);
        mCookerPowerOnFireGearWithoutTimerBpID = a.getResourceId(R.styleable.viewmodule_CircleCookerView_viewmodule_cooker_poweron_fire_gear_without_timer_background,R.mipmap.bg_big_circle_cooker_poweron_fire_gear_without_timer);
        mPoweroffBtnBpID = a.getResourceId(R.styleable.viewmodule_CircleCookerView_viewmodule_cooker_poweroff_button,R.mipmap.ic_big_cooker_poweroff);
        mCookerSize = a.getInt(R.styleable.viewmodule_CircleCookerView_viewmodule_cooker_size,COOKER_SIZE_BIG_COOKER);
        mCookerHighTempBgID = a.getResourceId(R.styleable.viewmodule_CircleCookerView_viewmodule_cooker_high_temp,R.mipmap.bg_big_circle_cooker_high_temp);
        mCookerPoweronWithoutPanBgID = a.getResourceId(R.styleable.viewmodule_CircleCookerView_viewmodule_cooker_poweron_without_pan,R.mipmap.bg_big_circle_cooker_poweron_without_pan);
        mCookerPoweronTempGearWordWithTimerBgID = a.getResourceId(R.styleable.viewmodule_CircleCookerView_viewmodule_cooker_poweron_temp_gear_word_with_timer_background,R.mipmap.bg_big_circle_cooker_poweron_temp_gear_word_with_timer);
        mCookerPoweronTempGearWordWithoutTimerBgID = a.getResourceId(R.styleable.viewmodule_CircleCookerView_viewmodule_cooker_poweron_temp_gear_word_without_timer_background,R.mipmap.bg_big_circle_cooker_poweron_temp_gear_word_without_timer);
        mCookerPoweronTempGearPictureWithTimerBgID = a.getResourceId(R.styleable.viewmodule_CircleCookerView_viewmodule_cooker_poweron_temp_gear_picture_with_timer_background,R.mipmap.bg_big_circle_cooker_poweron_temp_gear_picture_with_timer);
        mCookerPoweronTempGearPictureWithoutTimerBgID = a.getResourceId(R.styleable.viewmodule_CircleCookerView_viewmodule_cooker_poweron_temp_gear_picture_without_timer_background,R.mipmap.bg_big_circle_cooker_poweron_temp_gear_picture_without_timer);

        mCookerKeepWarmDarkTipsBgID = a.getResourceId(R.styleable.viewmodule_CircleCookerView_viewmodule_cooker_keep_warm_tips_dark_background,R.mipmap.bg_big_cooker_keep_warm_tips_dark);
        mCookerKeepWarmLightTipsBgID = a.getResourceId(R.styleable.viewmodule_CircleCookerView_viewmodule_cooker_keep_warm_tips_light_background,R.mipmap.bg_big_cooker_keep_warm_tips_light);
        mCookerUserPrepareToCookBgID = a.getResourceId(R.styleable.viewmodule_CircleCookerView_viewmodule_cooker_prepare_to_cook_background,R.mipmap.bg_big_circle_cooker_user_prepare);
        mCookerUserReadyToCookBgID = a.getResourceId(R.styleable.viewmodule_CircleCookerView_viewmodule_cooker_ready_to_cook_background,R.mipmap.bg_big_circle_cooker_ready_to_cook);
        mCookerUserPrepareToCookWithPictureBgID = a.getResourceId(R.styleable.viewmodule_CircleCookerView_viewmodule_cooker_prepare_to_cook_with_picture_background,R.mipmap.bg_big_circle_cooker_user_prepare_with_picture);
        mCookerErrorOcurrBgID = a.getResourceId(R.styleable.viewmodule_CircleCookerView_viewmodule_cooker_error_ocurr_background,R.mipmap.bg_big_circle_cooker_error);

        a.recycle();


        initResourse();
        initPaint();
        setSoundEffectsEnabled(true);
    }

    private void initResourse() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        mCookerPowerOnFireGearWithTimerBgBp = BitmapFactory.decodeResource(getResources(),mCookerPowerOnFireGearWithTimerBpID,options);
        mViewWidth = options.outWidth + COOKER_VIEW_INCREASE_SIZE;
        mViewHeight = options.outHeight + COOKER_VIEW_INCREASE_SIZE;


        mViewRect = new RectF();


    }


    private void initPaint() {
        fireGearPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fireGearPaint.setColor(ContextCompat.getColor(getContext(),R.color.viewmodule_gear_white));
        fireGearPaint.setTextAlign(Paint.Align.CENTER);
        fireGearPaint.setStyle(Paint.Style.FILL);
        Typeface fireGearTypeface = GlobalVars.getInstance().getDefaultFontBold();
        fireGearPaint.setTypeface(fireGearTypeface);

        fireGearTimerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fireGearTimerPaint.setColor(ContextCompat.getColor(getContext(),R.color.viewmodule_gear_white));
        fireGearTimerPaint.setTextAlign(Paint.Align.CENTER);
        fireGearTimerPaint.setStyle(Paint.Style.FILL);
        fireGearTimerPaint.setTypeface(fireGearTypeface);

        tempGearPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tempGearPaint.setColor(ContextCompat.getColor(getContext(),R.color.viewmodule_gear_white));
        tempGearPaint.setTextAlign(Paint.Align.CENTER);
        tempGearPaint.setStyle(Paint.Style.FILL);
        Typeface tempGearTypeface = GlobalVars.getInstance().getDefaultFontBold();
        tempGearPaint.setTypeface(tempGearTypeface);

        tempGearTimerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tempGearTimerPaint.setColor(ContextCompat.getColor(getContext(),R.color.viewmodule_gear_white));
        tempGearTimerPaint.setTextAlign(Paint.Align.CENTER);
        tempGearTimerPaint.setStyle(Paint.Style.FILL);
        tempGearTimerPaint.setTypeface(tempGearTypeface);


        errorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        errorPaint.setColor(ContextCompat.getColor(getContext(),R.color.viewmodule_gear_white));
        errorPaint.setTextAlign(Paint.Align.CENTER);
        errorPaint.setStyle(Paint.Style.FILL);
        errorPaint.setTypeface(fireGearTypeface);


    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.AT_MOST:
                width = Math.min(getSuggestedMinimumWidth(),widthSize);
                break;
            case MeasureSpec.UNSPECIFIED:
                width = getSuggestedMinimumWidth();
        }
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                height = Math.min(getSuggestedMinimumHeight(),heightSize);
                break;
            case MeasureSpec.UNSPECIFIED:
                height = getSuggestedMinimumWidth();
        }
        setMeasuredDimension(width,height);

    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return mViewHeight;
    }

    @Override
    protected int getSuggestedMinimumWidth() {
       return mViewWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawView(canvas);

    }

    public void updateUI() {
        invalidate();
    }

    private void drawView(Canvas canvas) {
        switch (cookerMode) {
            case COOKER_MODE_POWER_OFF:
                if (mCookerPowerOffBgBp == null) mCookerPowerOffBgBp = BitmapFactory.decodeResource(getResources(), mCookerPowerOffBpID);

                mViewRect.set(0, 0, getWidth(), getHeight());
                canvas.drawBitmap(mCookerPowerOffBgBp,null, mViewRect,null);

                break;
            case COOKER_MODE_FIRE_GEAR_WITH_TIMER:
                if (mCookerPowerOnFireGearWithTimerBgBp == null) {
                    mCookerPowerOnFireGearWithTimerBgBp = BitmapFactory.decodeResource(getResources(), mCookerPowerOnFireGearWithTimerBpID);
                }
                if (powerOffBtnBitmap ==null) {
                    powerOffBtnBitmap = BitmapFactory.decodeResource(getResources(), mPoweroffBtnBpID);
                }
                mViewRect.set(0, 0, getWidth(), getHeight());
                canvas.drawBitmap(mCookerPowerOnFireGearWithTimerBgBp,null, mViewRect,null);
                mViewRect.set(getWidth() - powerOffBtnBitmap.getWidth(), 0, getWidth(), powerOffBtnBitmap.getHeight());
                canvas.drawBitmap(powerOffBtnBitmap,null, mViewRect,null);

                drawTextWithTimerForFireGear(canvas);

                break;
            case COOKER_MODE_FIRE_GEAR_WITHOUT_TIMER:
                if (mCookerPowerOnFireGearWithoutTimerBgBp == null) {
                    mCookerPowerOnFireGearWithoutTimerBgBp = BitmapFactory.decodeResource(getResources(), mCookerPowerOnFireGearWithoutTimerBpID);
                }
                if (powerOffBtnBitmap ==null) {
                    powerOffBtnBitmap = BitmapFactory.decodeResource(getResources(), mPoweroffBtnBpID);
                }
                mViewRect.set(0, 0, getWidth(), getHeight());
                canvas.drawBitmap(mCookerPowerOnFireGearWithoutTimerBgBp,null, mViewRect,null);
                mViewRect.set(getWidth() - powerOffBtnBitmap.getWidth(), 0, getWidth(), powerOffBtnBitmap.getHeight());
                canvas.drawBitmap(powerOffBtnBitmap,null, mViewRect,null);
                drawTextWithoutTimerForFireGear(canvas);
                break;
            case COOKER_MODE_HIGH_TEMP:
                if (mCookerHighTempBg == null) mCookerHighTempBg = BitmapFactory.decodeResource(getResources(), mCookerHighTempBgID);

                mViewRect.set(0, 0, getWidth(), getHeight());
                canvas.drawBitmap(mCookerHighTempBg,null, mViewRect,null);
                break;
            case COOKER_MODE_POWERON_WITHOUT_PAN:
                if (mCookerPoweronWithoutPanBg == null) {
                    mCookerPoweronWithoutPanBg = BitmapFactory.decodeResource(getResources(), mCookerPoweronWithoutPanBgID);
                }
                if (powerOffBtnBitmap ==null) {
                    powerOffBtnBitmap = BitmapFactory.decodeResource(getResources(), mPoweroffBtnBpID);
                }
                mViewRect.set(0, 0, getWidth(), getHeight());
                canvas.drawBitmap(mCookerPoweronWithoutPanBg,null, mViewRect,null);
                mViewRect.set(getWidth() - powerOffBtnBitmap.getWidth(), 0, getWidth(), powerOffBtnBitmap.getHeight());
                canvas.drawBitmap(powerOffBtnBitmap,null, mViewRect,null);
                break;
            case COOKER_MODE_TEMP_GEAR_WORD_WITH_TIMER:
                if (mCookerPoweronTempGearWordWithTimerBgBp == null) {
                    mCookerPoweronTempGearWordWithTimerBgBp = BitmapFactory.decodeResource(getResources(), mCookerPoweronTempGearWordWithTimerBgID);
                }
                if (powerOffBtnBitmap ==null) {
                    powerOffBtnBitmap = BitmapFactory.decodeResource(getResources(), mPoweroffBtnBpID);
                }
                mViewRect.set(0, 0, getWidth(), getHeight());
                canvas.drawBitmap(mCookerPoweronTempGearWordWithTimerBgBp,null, mViewRect,null);
                mViewRect.set(getWidth() - powerOffBtnBitmap.getWidth(), 0, getWidth(), powerOffBtnBitmap.getHeight());
                canvas.drawBitmap(powerOffBtnBitmap,null, mViewRect,null);

                drawTextWithTimerForTempGear(canvas);
                break;
            case COOKER_MODE_TEMP_GEAR_WORD_WITHOUT_TIMER:
                if (mCookerPoweronTempGearWordWithoutTimerBgBp == null) {
                    mCookerPoweronTempGearWordWithoutTimerBgBp = BitmapFactory.decodeResource(getResources(), mCookerPoweronTempGearWordWithoutTimerBgID);
                }
                if (powerOffBtnBitmap ==null) {
                    powerOffBtnBitmap = BitmapFactory.decodeResource(getResources(), mPoweroffBtnBpID);
                }
                mViewRect.set(0, 0, getWidth(), getHeight());
                canvas.drawBitmap(mCookerPoweronTempGearWordWithoutTimerBgBp,null, mViewRect,null);
                mViewRect.set(getWidth() - powerOffBtnBitmap.getWidth(), 0, getWidth(), powerOffBtnBitmap.getHeight());
                canvas.drawBitmap(powerOffBtnBitmap,null, mViewRect,null);

                drawTextWithoutTimerForTempGear(canvas);
                break;
            case COOKER_MODE_TEMP_GEAR_PICTURE_WITH_TIMER:
                if (mCookerPoweronTempGearPictureWithTimerBgBp == null) {
                    mCookerPoweronTempGearPictureWithTimerBgBp = BitmapFactory.decodeResource(getResources(), mCookerPoweronTempGearPictureWithTimerBgID);
                }
                if (powerOffBtnBitmap ==null) {
                    powerOffBtnBitmap = BitmapFactory.decodeResource(getResources(), mPoweroffBtnBpID);
                }
                mViewRect.set(0, 0, getWidth(), getHeight());
                canvas.drawBitmap(mCookerPoweronTempGearPictureWithTimerBgBp,null, mViewRect,null);
                mViewRect.set(getWidth() - powerOffBtnBitmap.getWidth(), 0, getWidth(), powerOffBtnBitmap.getHeight());
                canvas.drawBitmap(powerOffBtnBitmap,null, mViewRect,null);

                LogUtil.d("getwidth----->" + getWidth());
                LogUtil.d("bitmap width----->" + mCookerPoweronTempGearPictureWithTimerBgBp.getWidth());
                if (tempIdentifyResourceID > 0) {
                    Bitmap bitmap = decodeTempIdentifyBitmap(tempIdentifyResourceID,getWidth(),(getHeight() - COOKER_VIEW_INCREASE_SIZE)/ 2);
                    //Bitmap bitmap = BitmapFactory.decodeResource(getResources(),tempIdentifyResourceID);
                    mViewRect.set((getWidth() - bitmap.getWidth()) / 2, getHeight() / 2 - bitmap.getHeight() , getWidth() / 2 + bitmap.getWidth() / 2, getHeight() / 2 - 10);
                    /*Paint paint = new Paint();
                    paint.setColor(Color.RED);
                    canvas.drawRect(mViewRect,paint);*/
                    canvas.drawBitmap(bitmap,null, mViewRect,null);
                    bitmap.recycle();
                }

                drawTextWithTimerForTempGearPicture(canvas);

                break;
            case COOKER_MODE_TEMP_GEAR_PICTURE_WITHOUT_TIMER:
                if (mCookerPoweronTempGearPictureWithoutTimerBgBp == null) {
                    mCookerPoweronTempGearPictureWithoutTimerBgBp = BitmapFactory.decodeResource(getResources(), mCookerPoweronTempGearPictureWithoutTimerBgID);
                }
                if (powerOffBtnBitmap ==null) {
                    powerOffBtnBitmap = BitmapFactory.decodeResource(getResources(), mPoweroffBtnBpID);
                }
                mViewRect.set(0, 0, getWidth(), getHeight());
                canvas.drawBitmap(mCookerPoweronTempGearPictureWithoutTimerBgBp,null, mViewRect,null);
                mViewRect.set(getWidth() - powerOffBtnBitmap.getWidth(), 0, getWidth(), powerOffBtnBitmap.getHeight());
                canvas.drawBitmap(powerOffBtnBitmap,null, mViewRect,null);

                /*if (tempIdentifyResourceID > 0) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),tempIdentifyResourceID);
                    mViewRect.set((getWidth() - bitmap.getWidth()) / 2, getHeight() / 2 - bitmap.getHeight() , getWidth() / 2 + bitmap.getWidth() / 2, getHeight() / 2);
                    Paint paint = new Paint();
                    paint.setColor(Color.RED);
                    canvas.drawRect(mViewRect,paint);
                    canvas.drawBitmap(bitmap,null, mViewRect,null);
                    bitmap.recycle();
                }*/
                if (tempIdentifyResourceID > 0) {
                    Bitmap bitmap = decodeTempIdentifyBitmap(tempIdentifyResourceID,getWidth(),(getHeight() - COOKER_VIEW_INCREASE_SIZE)  / 2);
                    //Bitmap bitmap = BitmapFactory.decodeResource(getResources(),tempIdentifyResourceID);
                    mViewRect.set((getWidth() - bitmap.getWidth()) / 2, getHeight() * 3 / 8 - bitmap.getHeight() / 2, getWidth() / 2 + bitmap.getWidth() / 2, getHeight() * 3 / 8 + bitmap.getHeight() /2);
                    /*Paint paint = new Paint();
                    paint.setColor(Color.RED);
                    canvas.drawRect(mViewRect,paint);*/
                    canvas.drawBitmap(bitmap,null, mViewRect,null);
                    bitmap.recycle();
                }

                break;
            case COOKER_MODE_KEEP_WARM_DARK_TIPS:
                if (mCookerKeepWarmDarkTipsBgBp == null) {
                    mCookerKeepWarmDarkTipsBgBp = BitmapFactory.decodeResource(getResources(), mCookerKeepWarmDarkTipsBgID);
                }
                if (powerOffBtnBitmap ==null) {
                    powerOffBtnBitmap = BitmapFactory.decodeResource(getResources(), mPoweroffBtnBpID);
                }

                mViewRect.set(0, 0, getWidth(), getHeight());
                canvas.drawBitmap(mCookerKeepWarmDarkTipsBgBp,null, mViewRect,null);

                mViewRect.set(getWidth() - powerOffBtnBitmap.getWidth(), 0, getWidth(), powerOffBtnBitmap.getHeight());
                canvas.drawBitmap(powerOffBtnBitmap,null, mViewRect,null);

                drawTextForKeepWarm(canvas);

                break;
            case COOKER_MODE_KEEP_WARM_LIGHT_TIPS:
                if (mCookerKeepWarmLightTipsBgBp == null) {
                    mCookerKeepWarmLightTipsBgBp = BitmapFactory.decodeResource(getResources(), mCookerKeepWarmLightTipsBgID);
                }
                if (powerOffBtnBitmap ==null) {
                    powerOffBtnBitmap = BitmapFactory.decodeResource(getResources(), mPoweroffBtnBpID);
                }

                mViewRect.set(0, 0, getWidth(), getHeight());
                canvas.drawBitmap(mCookerKeepWarmLightTipsBgBp,null, mViewRect,null);

                mViewRect.set(getWidth() - powerOffBtnBitmap.getWidth(), 0, getWidth(), powerOffBtnBitmap.getHeight());
                canvas.drawBitmap(powerOffBtnBitmap,null, mViewRect,null);
                drawTextForKeepWarm(canvas);
                break;
            case COOKER_MODE_USER_PREPARE_TO_COOK:
                if (mCookerUserPrepareToCookBgBp == null) {
                    mCookerUserPrepareToCookBgBp = BitmapFactory.decodeResource(getResources(), mCookerUserPrepareToCookBgID);
                }
                if (powerOffBtnBitmap ==null) {
                    powerOffBtnBitmap = BitmapFactory.decodeResource(getResources(), mPoweroffBtnBpID);
                }

                mViewRect.set(0, 0, getWidth(), getHeight());
                canvas.drawBitmap(mCookerUserPrepareToCookBgBp,null, mViewRect,null);

                mViewRect.set(getWidth() - powerOffBtnBitmap.getWidth(), 0, getWidth(), powerOffBtnBitmap.getHeight());
                canvas.drawBitmap(powerOffBtnBitmap,null, mViewRect,null);
                drawTextForTempGearPrepareToCook(canvas);

                break;
            case COOKER_MODE_USER_READY_TO_COOK:

                if (mCookerUserReadyToCookBgBp == null) {
                    mCookerUserReadyToCookBgBp = BitmapFactory.decodeResource(getResources(), mCookerUserReadyToCookBgID);
                }
                if (powerOffBtnBitmap ==null) {
                    powerOffBtnBitmap = BitmapFactory.decodeResource(getResources(), mPoweroffBtnBpID);
                }

                mViewRect.set(0, 0, getWidth(), getHeight());
                canvas.drawBitmap(mCookerUserReadyToCookBgBp,null, mViewRect,null);

                mViewRect.set(getWidth() - powerOffBtnBitmap.getWidth(), 0, getWidth(), powerOffBtnBitmap.getHeight());
                canvas.drawBitmap(powerOffBtnBitmap,null, mViewRect,null);

                break;
            case COOKER_MODE_USER_PREPARE_TO_COOK_WITH_PICTURE:
                if (mCookerUserPrepareToCookWithPictureBgBp == null) {
                    mCookerUserPrepareToCookWithPictureBgBp = BitmapFactory.decodeResource(getResources(), mCookerUserPrepareToCookWithPictureBgID);
                }
                if (powerOffBtnBitmap ==null) {
                    powerOffBtnBitmap = BitmapFactory.decodeResource(getResources(), mPoweroffBtnBpID);
                }
                mViewRect.set(0, 0, getWidth(), getHeight());
                canvas.drawBitmap(mCookerUserPrepareToCookWithPictureBgBp,null, mViewRect,null);
                mViewRect.set(getWidth() - powerOffBtnBitmap.getWidth(), 0, getWidth(), powerOffBtnBitmap.getHeight());
                canvas.drawBitmap(powerOffBtnBitmap,null, mViewRect,null);


                if (tempIdentifyResourceID > 0) {
                    Bitmap bitmap = decodeTempIdentifyBitmap(tempIdentifyResourceID,getWidth(),(getHeight() - COOKER_VIEW_INCREASE_SIZE)/ 2);
                    //Bitmap bitmap = BitmapFactory.decodeResource(getResources(),tempIdentifyResourceID);
                    mViewRect.set((getWidth() - bitmap.getWidth()) / 2, getHeight() / 2 - bitmap.getHeight() , getWidth() / 2 + bitmap.getWidth() / 2, getHeight() / 2 - 10);
                    /*Paint paint = new Paint();
                    paint.setColor(Color.RED);
                    canvas.drawRect(mViewRect,paint);*/
                    canvas.drawBitmap(bitmap,null, mViewRect,null);
                    bitmap.recycle();
                }
                break;
            case COOKER_MODE_COOKER_ERROR_OCURR:
                if (mCookerErrorOcurrBgBp == null) {
                    mCookerErrorOcurrBgBp = BitmapFactory.decodeResource(getResources(), mCookerErrorOcurrBgID);
                }
              /*  if (powerOffBtnBitmap ==null) {
                    powerOffBtnBitmap = BitmapFactory.decodeResource(getResources(), mPoweroffBtnBpID);
                }*/
                mViewRect.set(0, 0, getWidth(), getHeight());
                canvas.drawBitmap(mCookerErrorOcurrBgBp,null, mViewRect,null);
               // mViewRect.set(getWidth() - powerOffBtnBitmap.getWidth(), 0, getWidth(), powerOffBtnBitmap.getHeight());
                //canvas.drawBitmap(powerOffBtnBitmap,null, mViewRect,null);

                drawTextForErrorMessage(canvas);
                break;
            case COOKER_MODE_COOKER_STATUS_ABNORMAL_NO_PAN:

                break;
        }
    }

    private void drawTextForErrorMessage(Canvas canvas) {
        float top,bottom;
        int baseLineY;
        switch (mCookerSize) {
            case COOKER_SIZE_BIG_COOKER:
                errorPaint.setTextSize(200);
                mViewRect.set(COOKER_VIEW_INCREASE_SIZE , COOKER_VIEW_INCREASE_SIZE , getWidth() - COOKER_VIEW_INCREASE_SIZE, getHeight() - COOKER_VIEW_INCREASE_SIZE);
                fontMetrics = errorPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(errorMessage,mViewRect.centerX(),baseLineY, errorPaint);

                break;
            case COOKER_SIZE_MEDIUM_COOKER:

                break;
            case COOKER_SIZE_SMALL_COOKER:
                errorPaint.setTextSize(120);
                mViewRect.set(COOKER_VIEW_INCREASE_SIZE , COOKER_VIEW_INCREASE_SIZE , getWidth() - COOKER_VIEW_INCREASE_SIZE, getHeight() - COOKER_VIEW_INCREASE_SIZE);
                fontMetrics = errorPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(errorMessage,mViewRect.centerX(),baseLineY, errorPaint);
                break;
        }

    }

    private void drawTextForKeepWarm(Canvas canvas) {
        float top,bottom;
        int baseLineY;
        switch (mCookerSize) {
            case COOKER_SIZE_BIG_COOKER:
                tempGearPaint.setTextSize(64);
                tempGearTimerPaint.setTextSize(64);
                mViewRect.set(COOKER_VIEW_INCREASE_SIZE + 20, COOKER_VIEW_INCREASE_SIZE +20, getWidth() - COOKER_VIEW_INCREASE_SIZE, (getHeight() - COOKER_VIEW_INCREASE_SIZE)/ 2);
                fontMetrics = tempGearPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText("+10min",mViewRect.centerX(),baseLineY, tempGearPaint);

                mViewRect.set(COOKER_VIEW_INCREASE_SIZE + 20,  getHeight() / 2, getWidth() - COOKER_VIEW_INCREASE_SIZE, getHeight() - COOKER_VIEW_INCREASE_SIZE);
                fontMetrics = tempGearTimerPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText("keep Warm",mViewRect.centerX(),baseLineY, tempGearTimerPaint);
                break;
            case COOKER_SIZE_MEDIUM_COOKER:

                break;
            case COOKER_SIZE_SMALL_COOKER:
                tempGearPaint.setTextSize(48);
                tempGearTimerPaint.setTextSize(48);
                mViewRect.set(COOKER_VIEW_INCREASE_SIZE + 10, COOKER_VIEW_INCREASE_SIZE +20, getWidth() - COOKER_VIEW_INCREASE_SIZE, (getHeight() - COOKER_VIEW_INCREASE_SIZE)/ 2);
                fontMetrics = tempGearPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText("+10min",mViewRect.centerX(),baseLineY, tempGearPaint);

                mViewRect.set(COOKER_VIEW_INCREASE_SIZE + 0,  getHeight() / 2, getWidth() - COOKER_VIEW_INCREASE_SIZE, getHeight() - COOKER_VIEW_INCREASE_SIZE);
                fontMetrics = tempGearTimerPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText("keep Warm",mViewRect.centerX(),baseLineY, tempGearTimerPaint);
                break;
        }



    }

    private void drawTextForTempGearPrepareToCook(Canvas canvas) {
        float top,bottom;
        int baseLineY;
        switch (mCookerSize) {
            case COOKER_SIZE_BIG_COOKER:

                //tempGearValue = "60";
                //timerValue = "00:10";
                if (tempGearValue.length() == 3) {
                    tempGearPaint.setTextSize(110);
                }else {
                    tempGearPaint.setTextSize(150);
                }

                mViewRect.set(COOKER_VIEW_INCREASE_SIZE + 20, COOKER_VIEW_INCREASE_SIZE +20, getWidth() - COOKER_VIEW_INCREASE_SIZE, (getHeight() - COOKER_VIEW_INCREASE_SIZE)/ 2);
                fontMetrics = tempGearPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(tempGearValue,mViewRect.centerX(),baseLineY, tempGearPaint);


                break;
            case COOKER_SIZE_MEDIUM_COOKER:

                break;
            case COOKER_SIZE_SMALL_COOKER:
                //tempGearValue = "60";
                if (tempGearValue.length() == 3) {
                    tempGearPaint.setTextSize(70);
                }else {
                    tempGearPaint.setTextSize(100);
                }

                mViewRect.set(COOKER_VIEW_INCREASE_SIZE + 10, COOKER_VIEW_INCREASE_SIZE, getWidth() - COOKER_VIEW_INCREASE_SIZE, (getHeight() - COOKER_VIEW_INCREASE_SIZE)/ 2);
                fontMetrics = tempGearPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(tempGearValue,mViewRect.centerX(),baseLineY, tempGearPaint);

                break;
        }

    }

    private void drawTextWithTimerForTempGear(Canvas canvas) {
        float top,bottom;
        int baseLineY;
        switch (mCookerSize) {
            case COOKER_SIZE_BIG_COOKER:

                //tempGearValue = "60";
                //timerValue = "00:10";
                if (tempGearValue.length() == 3) {
                    tempGearPaint.setTextSize(110);
                }else {
                    tempGearPaint.setTextSize(150);
                }

                tempGearTimerPaint.setTextSize(100);
                mViewRect.set(COOKER_VIEW_INCREASE_SIZE + 20, COOKER_VIEW_INCREASE_SIZE +20, getWidth() - COOKER_VIEW_INCREASE_SIZE, (getHeight() - COOKER_VIEW_INCREASE_SIZE)/ 2);
                fontMetrics = tempGearPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(tempGearValue,mViewRect.centerX(),baseLineY, tempGearPaint);

                mViewRect.set(COOKER_VIEW_INCREASE_SIZE + 80,  getHeight() / 2, getWidth() - COOKER_VIEW_INCREASE_SIZE, getHeight() - COOKER_VIEW_INCREASE_SIZE);
                fontMetrics = tempGearTimerPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(timerValue,mViewRect.centerX(),baseLineY, tempGearTimerPaint);
                break;
            case COOKER_SIZE_MEDIUM_COOKER:

                break;
            case COOKER_SIZE_SMALL_COOKER:
                //tempGearValue = "60";
                if (tempGearValue.length() == 3) {
                    tempGearPaint.setTextSize(70);
                }else {
                    tempGearPaint.setTextSize(100);
                }

                tempGearTimerPaint.setTextSize(65);

                mViewRect.set(COOKER_VIEW_INCREASE_SIZE + 10, COOKER_VIEW_INCREASE_SIZE, getWidth() - COOKER_VIEW_INCREASE_SIZE, (getHeight() - COOKER_VIEW_INCREASE_SIZE)/ 2);
                fontMetrics = tempGearPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(tempGearValue,mViewRect.centerX(),baseLineY, tempGearPaint);

                mViewRect.set(COOKER_VIEW_INCREASE_SIZE + 50,  getHeight() / 2, getWidth() - COOKER_VIEW_INCREASE_SIZE, getHeight() - COOKER_VIEW_INCREASE_SIZE);
                fontMetrics = tempGearTimerPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 10;//基线中间点的y轴计算公式
                canvas.drawText(timerValue,mViewRect.centerX(),baseLineY, tempGearTimerPaint);
                break;
        }

    }

    private void drawTextWithTimerForTempGearPicture(Canvas canvas) {
        float top,bottom;
        int baseLineY;
        switch (mCookerSize) {
            case COOKER_SIZE_BIG_COOKER:

                //tempGearValue = "60";
                //timerValue = "00:10";
         /*       if (tempGearValue.length() == 3) {
                    tempGearPaint.setTextSize(110);
                }else {
                    tempGearPaint.setTextSize(150);
                }*/

                tempGearTimerPaint.setTextSize(100);
               /* mViewRect.set(COOKER_VIEW_INCREASE_SIZE + 20, COOKER_VIEW_INCREASE_SIZE +20, getWidth() - COOKER_VIEW_INCREASE_SIZE, (getHeight() - COOKER_VIEW_INCREASE_SIZE)/ 2);
                fontMetrics = tempGearPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(tempGearValue,mViewRect.centerX(),baseLineY, tempGearPaint);*/

                mViewRect.set(COOKER_VIEW_INCREASE_SIZE + 80,  getHeight() / 2, getWidth() - COOKER_VIEW_INCREASE_SIZE, getHeight() - COOKER_VIEW_INCREASE_SIZE);
                fontMetrics = tempGearTimerPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(timerValue,mViewRect.centerX(),baseLineY, tempGearTimerPaint);
                break;
            case COOKER_SIZE_MEDIUM_COOKER:

                break;
            case COOKER_SIZE_SMALL_COOKER:
                //tempGearValue = "60";
             /*   if (tempGearValue.length() == 3) {
                    tempGearPaint.setTextSize(70);
                }else {
                    tempGearPaint.setTextSize(100);
                }*/

                tempGearTimerPaint.setTextSize(65);

            /*    mViewRect.set(COOKER_VIEW_INCREASE_SIZE + 10, COOKER_VIEW_INCREASE_SIZE, getWidth() - COOKER_VIEW_INCREASE_SIZE, (getHeight() - COOKER_VIEW_INCREASE_SIZE)/ 2);
                fontMetrics = tempGearPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(tempGearValue,mViewRect.centerX(),baseLineY, tempGearPaint);*/

                mViewRect.set(COOKER_VIEW_INCREASE_SIZE + 50,  getHeight() / 2, getWidth() - COOKER_VIEW_INCREASE_SIZE, getHeight() - COOKER_VIEW_INCREASE_SIZE);
                fontMetrics = tempGearTimerPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 10;//基线中间点的y轴计算公式
                canvas.drawText(timerValue,mViewRect.centerX(),baseLineY, tempGearTimerPaint);
                break;
        }

    }

    private void drawTextWithoutTimerForTempGear(Canvas canvas) {
        float top,bottom;
        int baseLineY;
        switch (mCookerSize) {
            case COOKER_SIZE_BIG_COOKER:

                //tempGearValue = "60";
                //timerValue = "00:10";
                if (tempGearValue.length() == 3) {
                    tempGearPaint.setTextSize(130);
                }else {
                    tempGearPaint.setTextSize(170);
                }

                mViewRect.set(COOKER_VIEW_INCREASE_SIZE + 10, COOKER_VIEW_INCREASE_SIZE + 100, getWidth() - COOKER_VIEW_INCREASE_SIZE, (getHeight() - COOKER_VIEW_INCREASE_SIZE)/ 2);
                fontMetrics = tempGearPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(tempGearValue,mViewRect.centerX(),baseLineY, tempGearPaint);


                break;
            case COOKER_SIZE_MEDIUM_COOKER:

                break;
            case COOKER_SIZE_SMALL_COOKER:
                //tempGearValue = "60";
                if (tempGearValue.length() == 3) {
                    tempGearPaint.setTextSize(90);
                }else {
                    tempGearPaint.setTextSize(120);
                }



                mViewRect.set(COOKER_VIEW_INCREASE_SIZE + 10, COOKER_VIEW_INCREASE_SIZE + 60, getWidth() - COOKER_VIEW_INCREASE_SIZE, (getHeight() - COOKER_VIEW_INCREASE_SIZE)/ 2);
                fontMetrics = tempGearPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(tempGearValue,mViewRect.centerX(),baseLineY, tempGearPaint);


                break;
        }

    }

    private void drawTextWithTimerForFireGear(Canvas canvas) {
        float top,bottom;
        int baseLineY;
        switch (mCookerSize) {
            case COOKER_SIZE_BIG_COOKER:
                /*if (fireGearValue.length() == 2) fireGearPaint.setTextSize(150);
                else fireGearPaint.setTextSize(170);*/
                fireGearPaint.setTextSize(170);
                fireGearTimerPaint.setTextSize(110);
                mViewRect.set(COOKER_VIEW_INCREASE_SIZE, COOKER_VIEW_INCREASE_SIZE, getWidth() - COOKER_VIEW_INCREASE_SIZE, (getHeight() - COOKER_VIEW_INCREASE_SIZE)/ 2);
                fontMetrics = fireGearPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(fireGearValue,mViewRect.centerX(),baseLineY, fireGearPaint);

                mViewRect.set(COOKER_VIEW_INCREASE_SIZE + 80,  getHeight() / 2, getWidth() - COOKER_VIEW_INCREASE_SIZE, getHeight() - COOKER_VIEW_INCREASE_SIZE);
                fontMetrics = fireGearTimerPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                LogUtil.d("samhung---Enter::draw timer value--->" + timerValue);
                canvas.drawText(timerValue,mViewRect.centerX(),baseLineY, fireGearTimerPaint);
                break;
            case COOKER_SIZE_MEDIUM_COOKER:

                break;
            case COOKER_SIZE_SMALL_COOKER:
               /* if (fireGearValue.length() == 2) fireGearPaint.setTextSize(110);
                else fireGearPaint.setTextSize(130);*/
                fireGearPaint.setTextSize(130);
                fireGearTimerPaint.setTextSize(70);
                mViewRect.set(COOKER_VIEW_INCREASE_SIZE, COOKER_VIEW_INCREASE_SIZE, getWidth() - COOKER_VIEW_INCREASE_SIZE, (getHeight() - COOKER_VIEW_INCREASE_SIZE)/ 2);
                fontMetrics = fireGearPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(fireGearValue,mViewRect.centerX(),baseLineY, fireGearPaint);

                mViewRect.set(COOKER_VIEW_INCREASE_SIZE + 50,  getHeight() / 2, getWidth() - COOKER_VIEW_INCREASE_SIZE, getHeight() - COOKER_VIEW_INCREASE_SIZE);
                fontMetrics = fireGearTimerPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 10;//基线中间点的y轴计算公式
                canvas.drawText(timerValue,mViewRect.centerX(),baseLineY, fireGearTimerPaint);
                break;
        }

    }

    private void drawTextWithoutTimerForFireGear(Canvas canvas) {
        float top,bottom;
        int baseLineY;
        switch (mCookerSize) {
            case COOKER_SIZE_BIG_COOKER:
            /*    if (fireGearValue.length() == 2) fireGearPaint.setTextSize(160);
                else fireGearPaint.setTextSize(180);*/
                fireGearPaint.setTextSize(180);
                fireGearTimerPaint.setTextSize(110);
                mViewRect.set(COOKER_VIEW_INCREASE_SIZE, COOKER_VIEW_INCREASE_SIZE, getWidth() - COOKER_VIEW_INCREASE_SIZE, (getHeight() - COOKER_VIEW_INCREASE_SIZE) * 2 / 3 );
                fontMetrics = fireGearPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(fireGearValue,mViewRect.centerX(),baseLineY, fireGearPaint);

                break;
            case COOKER_SIZE_MEDIUM_COOKER:

                break;
            case COOKER_SIZE_SMALL_COOKER:
                /*if (fireGearValue.length() == 2) fireGearPaint.setTextSize(110);
                else fireGearPaint.setTextSize(130);*/
                fireGearPaint.setTextSize(130);
                fireGearTimerPaint.setTextSize(70);
                mViewRect.set(COOKER_VIEW_INCREASE_SIZE, COOKER_VIEW_INCREASE_SIZE, getWidth() - COOKER_VIEW_INCREASE_SIZE, (getHeight() - COOKER_VIEW_INCREASE_SIZE) * 2 / 3);
                fontMetrics = fireGearPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(fireGearValue,mViewRect.centerX(),baseLineY, fireGearPaint);

                break;
        }
    }

    private Bitmap decodeTempIdentifyBitmap(int id, int viewWidth, int viewHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        BitmapFactory.decodeResource(getResources(),id,options);
        int bitmapWidth = options.outWidth;
        int bitmapHeight = options.outHeight;
        float scale = 1.0f;
        if(bitmapWidth > viewWidth || bitmapHeight > viewHeight){
            //double widthScale =  (float) viewWidth / (float) bitmapWidth);
            float widthScale = (float) viewWidth / bitmapWidth;
            float heightScale =  (float) viewHeight / bitmapHeight ;

            if (widthScale > heightScale) {
                if (heightScale < 1) scale = heightScale;
            }else {
                if (widthScale < 1) scale = widthScale;
                scale = widthScale;
            }
        }

        options.inJustDecodeBounds = false;
       // return BitmapFactory.decodeResource(getResources(),id,options);
        return bitmapScale(BitmapFactory.decodeResource(getResources(),id,options), (float) scale);
    }

    public Bitmap bitmapScale(Bitmap bitmap, float scale) {
        //scale = 0.7f;
        LogUtil.d("scale------------->" + scale);
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale); // 长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

    private Bitmap decodeTempIdentifyBitmap1(int id, int viewWidth, int viewHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        //设置为true,表示解析Bitmap对象，该对象不占内存
        options.inJustDecodeBounds = true;
        //BitmapFactory.decodeFile(path,id, options);
        BitmapFactory.decodeResource(getResources(),id,options);
        //设置缩放比例
        options.inSampleSize = computeScale(options, viewWidth, viewHeight);
        options.inSampleSize = 2;


        //设置为false,解析Bitmap对象加入到内存中
        options.inJustDecodeBounds = false;
        //Log.e(TAG, "get Iamge form file,  path = " + path);
        //返回Bitmap对象
        return BitmapFactory.decodeResource(getResources(),id,options);
    }

    private int computeScale(BitmapFactory.Options options, int viewWidth, int viewHeight){
        int inSampleSize = 1;
        if(viewWidth == 0 || viewWidth == 0){
            return inSampleSize;
        }
        int bitmapWidth = options.outWidth;
        int bitmapHeight = options.outHeight;

        //假如Bitmap的宽度或高度大于我们设定图片的View的宽高，则计算缩放比例
        if(bitmapWidth > viewWidth || bitmapHeight > viewHeight){
            int widthScale = (int) Math.ceil((float) bitmapWidth / (float) viewWidth);
            int heightScale = (int) Math.ceil((float) bitmapHeight / (float) viewHeight);

            //为了保证图片不缩放变形，我们取宽高比例最小的那个
            //inSampleSize = widthScale < heightScale ? widthScale : heightScale;
            inSampleSize = widthScale > heightScale ? widthScale : heightScale;
        }
        return inSampleSize;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_UP:
                processTouchEvent(event);
                break;
        }
        return true;
        //return super.onTouchEvent(event);
    }

    private void processTouchEvent(MotionEvent event) {
        float x1,y1;
        if (cookerMode == COOKER_MODE_FIRE_GEAR_WITHOUT_TIMER || cookerMode == COOKER_MODE_TEMP_GEAR_PICTURE_WITHOUT_TIMER ||
                cookerMode == COOKER_MODE_TEMP_GEAR_WORD_WITHOUT_TIMER) {
            x1 = getWidth() / 2;//圆点X
            y1 = getHeight() * 0.64f;//圆点Y
        }else {
            x1 = getWidth() / 2;//圆点X
            y1 = getHeight() / 2;//圆点Y
        }

        float x2 = event.getX();//触摸点X
        float y2 = event.getY();//触摸点Y


        if (cookerMode == COOKER_MODE_POWER_OFF) {//关机状态
            playSoundEffect(SoundEffectConstants.CLICK);
            mListener.onCookerPowerOn();
           // LogUtil.d("Enter:: liang-------------power off -----------");
        }else if (cookerMode == COOKER_MODE_HIGH_TEMP) {//炉头高温状态
            playSoundEffect(SoundEffectConstants.CLICK);
            mListener.onCookerPowerOn();
        }else {//其他状态，需要判断关机
            if (isOutOfCircle(x1,y1,x2,y2)) {
                if (x2 > x1 && y2 < y1) {
                  //  LogUtil.d("Enter:: --------------power off-----------");
                    playSoundEffect(SoundEffectConstants.CLICK);
                    processPowerOffEvent();
                }

            }else {
                if (y2 < y1) {
                    processUpEvent();
                //    LogUtil.d("Enter:: liang-------------up-----------");
                }else {

                    processDownEvent();
                  //  LogUtil.d("Enter:: liang-------------down-----------");
                }
            }
           // LogUtil.d("Enter:: liang-------------none-----------");
        }




    }

    private void processDownEvent() {
        switch (cookerMode) {
            case COOKER_MODE_FIRE_GEAR_WITH_TIMER:
                mListener.onSetTimer();
                break;
            case COOKER_MODE_FIRE_GEAR_WITHOUT_TIMER:
                mListener.onSetTimer();
                break;
            case COOKER_MODE_TEMP_GEAR_WORD_WITH_TIMER:
                mListener.onSetTimer();
                break;
            case COOKER_MODE_TEMP_GEAR_WORD_WITHOUT_TIMER:
                mListener.onSetTimer();
                break;
            case COOKER_MODE_TEMP_GEAR_PICTURE_WITH_TIMER:
                mListener.onSetTimer();
                break;
            case COOKER_MODE_TEMP_GEAR_PICTURE_WITHOUT_TIMER:
                mListener.onSetTimer();
                break;
            case COOKER_MODE_KEEP_WARM_DARK_TIPS:
                if (handler.hasMessages(HANDLER_FLASH_KEEP_WARM_TIPS))handler.removeMessages(HANDLER_FLASH_KEEP_WARM_TIPS);
                mListener.onRequestKeepWarm();
                break;
            case COOKER_MODE_KEEP_WARM_LIGHT_TIPS:
                if (handler.hasMessages(HANDLER_FLASH_KEEP_WARM_TIPS))handler.removeMessages(HANDLER_FLASH_KEEP_WARM_TIPS);
                mListener.onRequestKeepWarm();
                break;
            case COOKER_MODE_USER_PREPARE_TO_COOK:
                setCookerMode(COOKER_MODE_USER_READY_TO_COOK);
                break;
            case COOKER_MODE_USER_READY_TO_COOK:
                mListener.onRequestToCookForUserReady();
                break;
            case COOKER_MODE_USER_PREPARE_TO_COOK_WITH_PICTURE:
                setCookerMode(COOKER_MODE_USER_READY_TO_COOK);
                break;
        }
    }


    private void processUpEvent() {
        switch (cookerMode) {
            case COOKER_MODE_FIRE_GEAR_WITH_TIMER:
                mListener.onSetGear();
                break;
            case COOKER_MODE_FIRE_GEAR_WITHOUT_TIMER:
                mListener.onSetGear();
                break;
            case COOKER_MODE_TEMP_GEAR_WORD_WITH_TIMER:
                mListener.onSetGear();
                break;
            case COOKER_MODE_TEMP_GEAR_WORD_WITHOUT_TIMER:
                mListener.onSetGear();
                break;
            case COOKER_MODE_TEMP_GEAR_PICTURE_WITH_TIMER:
                mListener.onSetGear();
                break;
            case COOKER_MODE_TEMP_GEAR_PICTURE_WITHOUT_TIMER:
                mListener.onSetGear();
                break;
            case COOKER_MODE_KEEP_WARM_DARK_TIPS:
                if (handler.hasMessages(HANDLER_FLASH_KEEP_WARM_TIPS))handler.removeMessages(HANDLER_FLASH_KEEP_WARM_TIPS);
                mListener.onRequestAddTenMinute();
                break;
            case COOKER_MODE_KEEP_WARM_LIGHT_TIPS:
                if (handler.hasMessages(HANDLER_FLASH_KEEP_WARM_TIPS))handler.removeMessages(HANDLER_FLASH_KEEP_WARM_TIPS);
                mListener.onRequestAddTenMinute();
                break;
            case COOKER_MODE_USER_PREPARE_TO_COOK:
                mListener.onSetGear();
                break;
            case COOKER_MODE_USER_PREPARE_TO_COOK_WITH_PICTURE:
                mListener.onSetGear();
                break;
        }

    }

    private void processPowerOffEvent() {
        cookerMode = COOKER_MODE_POWER_OFF;
        invalidate();
        mListener.onCookerPowerOff();
        //if (handler.hasMessages(HANDLER_FLASH_KEEP_WARM_TIPS))handler.removeMessages(HANDLER_FLASH_KEEP_WARM_TIPS);
        //handler.sendEmptyMessageDelayed(HANDLER_HIGH_TEMP_FINISH,TIME_HIGH_TEMP_FINISH);

    }

    private double calDistance(float x1 ,float y1 , float x2 , float y2) {
        double x = Math.abs(x1 - x2);
        double y = Math.abs(y1 - y2);
        return Math.sqrt(x * x + y * y);
    }

    private boolean isOutOfCircle(float x1 ,float y1 , float x2 , float y2) {
        int radius = mViewWidth / 2;
        return calDistance(x1,y1,x2,y2) > radius;
    }

    private void setCookerMode(int mode) {
        lastCookerMode = cookerMode;
        cookerMode = mode;
        invalidate();
    }

    /***********************************public method*********************************************************************/
    /**
     * 获取档位值
     * */
    public String getFireGearValue() {
        return fireGearValue;
    }
    /**
     * 设置档位值
     * */
    public void setFireGearValue(int cookerMode ,String fireGearValue) {
        this.fireGearValue = fireGearValue;
        setCookerMode(cookerMode);
    }
    /**
     * 获取定时器的值
     * */
    public String getTimerValue() {
        return timerValue;
    }
    /**
     * 设置档位值
     * */
    public void setTimerValue(int cookerMode , String timerValue) {
        this.timerValue = timerValue;
        setCookerMode(COOKER_MODE_FIRE_GEAR_WITH_TIMER);
    }

    public void setFireGearWithoutTimer(int fireGear) {
        if (fireGear == 10) fireGearValue = "B";
        else fireGearValue = String.valueOf(fireGear);
        setCookerMode(COOKER_MODE_FIRE_GEAR_WITHOUT_TIMER);
    }

    public void setFireGearWithTimer(int fireGear,int hour,int minute) {
        //LogUtil.d("samhung---Enter:: setFireGearWithTimer");
        //fireGearValue = String.valueOf(fireGear);
        if (fireGear == 10) fireGearValue = "B";
        else fireGearValue = String.valueOf(fireGear);
        timerValue = String.format("%02d", hour) + ":" + String.format("%02d", minute);
        setCookerMode(COOKER_MODE_FIRE_GEAR_WITH_TIMER);
    }

    public void setTempValue(int tempValue,int indentifyResourceID) {
        tempGearValue = String.valueOf(tempValue);
        tempIdentifyResourceID = indentifyResourceID;
        if (indentifyResourceID != -1) {//显示温控图示
            setCookerMode(COOKER_MODE_TEMP_GEAR_PICTURE_WITHOUT_TIMER);
        }else {//显示温度值
            setCookerMode(COOKER_MODE_TEMP_GEAR_WORD_WITHOUT_TIMER);
        }

    }

    public void setTempValueWithTimer(int tempValue,int indentifyResourceID,int hour,int minute) {
        tempGearValue = String.valueOf(tempValue);
        timerValue = String.format("%02d", hour) + ":" + String.format("%02d", minute);
        tempIdentifyResourceID = indentifyResourceID;
        if (indentifyResourceID != -1) {//显示温控图示
            setCookerMode(COOKER_MODE_TEMP_GEAR_PICTURE_WITH_TIMER);
        }else {//显示温度值
            setCookerMode(COOKER_MODE_TEMP_GEAR_WORD_WITH_TIMER);
        }
    }


    public void updateTimerUI(int id,int hour, int minute) {
        //LogUtil.d("Enter:: updateTimerUI-------id------>" + id);
        LogUtil.d("samhung---hour---->" + hour);
        LogUtil.d("samhung---minute---->" + minute);

        timerValue = String.format("%02d", hour) + ":" + String.format("%02d", minute);
        LogUtil.d("samhung---timerValue---->" + timerValue);
        LogUtil.d("samhung---mode--->" + cookerMode);
        invalidate();
    }

    public void updateUIForKeepWarm() {
        setCookerMode(COOKER_MODE_KEEP_WARM_LIGHT_TIPS);
        handler.sendEmptyMessageDelayed(HANDLER_FLASH_KEEP_WARM_TIPS,TIME_FLASH_KEEP_WARM);

    }

    public void updateStatusUIForErrorOcurr(String errorMessage) {
        this.errorMessage = errorMessage;
        setCookerMode(COOKER_MODE_COOKER_ERROR_OCURR);
    }

    public void updateStatusUIForErrorDimiss() {
        setCookerMode(COOKER_MODE_POWER_OFF);
    }

    public void updateStatusUIForHighTemp() {
        setCookerMode(COOKER_MODE_HIGH_TEMP);
    }

    public void updateStatusUIForNoPan() {
        setCookerMode(COOKER_MODE_POWERON_WITHOUT_PAN);
    }

    public void updateUIForUserPrepareToCook(int tempValue,int indentifyResourceID) {
        tempGearValue = String.valueOf(tempValue);
        tempIdentifyResourceID = indentifyResourceID;
        if (indentifyResourceID != -1) {//显示温控图示
            setCookerMode(COOKER_MODE_USER_PREPARE_TO_COOK_WITH_PICTURE);
        }else {//显示温度值
            setCookerMode(COOKER_MODE_USER_PREPARE_TO_COOK);

        }
    }



    public void updateUIForUserReadyToCook() {
        setCookerMode(COOKER_MODE_USER_READY_TO_COOK);
    }

    public void updateUIForTimeIsUp() {
        processPowerOffEvent();
    }

    public void updateHighTempFinishStatusUI() {
        handler.sendEmptyMessage(HANDLER_HIGH_TEMP_FINISH);
    }


    public void setOnCircleCookerViewListener(@NonNull OnCircleCookerViewListener listener) {
        this.mListener = listener;
    }

    public interface OnCircleCookerViewListener {
        void onCookerPowerOff();

        void onCookerPowerOn();

        void onSetGear();

        void onSetTimer();

        void onUpdateGear();

        void onTimerIsUp();

        void onRequestAddTenMinute();

        void onRequestKeepWarm();

        void onRequestToCookForUserReady();
    }

}
