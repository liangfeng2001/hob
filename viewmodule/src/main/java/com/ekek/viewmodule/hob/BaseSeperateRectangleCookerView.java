package com.ekek.viewmodule.hob;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewConfiguration;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.utils.DisplayUtil;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.commonmodule.utils.Marquee;
import com.ekek.viewmodule.R;
import com.ekek.viewmodule.contract.HobCircleCookerContract;
import com.ekek.viewmodule.utils.ViewUtils;

public abstract class BaseSeperateRectangleCookerView extends View {
 /*   private static final int HOB_VIEW_WORK_MODE_POWER_OFF = 0;//
    private static final int HOB_VIEW_WORK_MODE_FIRE_GEAR_WITHOUT_TIMER = 100;//
    private static final int HOB_VIEW_WORK_MODE_FIRE_GEAR_WITH_TIMER = 200;//
    private static final int HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITHOUT_TIMER = 300;//
    private static final int HOB_VIEW_WORK_MODE_TEMP_GEAR_WITHOUT_TIMER = 400;//
    private static final int HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER = 500;//
    private static final int HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER = 600;//
    private static final int HOB_VIEW_WORK_MODE_ABNORMAL_NO_PAN = 700;//
    private static final int HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP = 701;//
    private static final int HOB_VIEW_WORK_MODE_ABNORMAL_ERROR_OCURR = 702;//
    private static final int HOB_VIEW_WORK_MODE_UPATE_TIMER = 800;*/

    protected static final int HOB_VIEW_WORK_MODE_POWER_OFF = 0;//
    protected static final int HOB_VIEW_WORK_MODE_FIRE_GEAR_WITHOUT_TIMER = 100;//
    protected static final int HOB_VIEW_WORK_MODE_FIRE_GEAR_WITH_TIMER = 200;//
    protected static final int HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITHOUT_TIMER = 300;//
    protected static final int HOB_VIEW_WORK_MODE_TEMP_GEAR_WITHOUT_TIMER = 400;//
    protected static final int HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER = 500;//
    protected static final int HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER_AND_RECIPES_FIRST = 501;//先显示菜谱图片，再显示温度定时
    protected static final int HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER_AND_RECIPES_SECOND = 502;//先显示温度定时，再显示菜谱图片
    protected static final int HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER = 600;//
    protected static final int HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_FIRST = 601;//先显示菜谱图片，再显示温度定时
    protected static final int HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_SECOND = 602;//先显示温度定时，再显示菜谱图片
    protected static final int HOB_VIEW_WORK_MODE_ABNORMAL_NO_PAN = 700;//
    protected static final int HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP = 701;//
    protected static final int HOB_VIEW_WORK_MODE_ABNORMAL_ERROR_OCURR = 702;//
    protected static final int HOB_VIEW_WORK_MODE_UPATE_TIMER = 800;
    protected static final int HOB_VIEW_WORK_MODE_PAUSE = 900;//
    protected static final int HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR = 1000;//
    protected static final int HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR_WITH_INDICATOR = 1001;//
    protected static final int HOB_VIEW_WORK_MODE_TEMP_SENSOR_READY = 1002;//
    protected static final int HOB_VIEW_WORK_MODE_TEMP_WORK_FINISH_WAIT_USER_CONFIRM= 1003;

    private static final int TEMP_WORK_FINISH_PROGRESS_LIGHT = 0;
    private static final int TEMP_WORK_FINISH_PROGRESS_DARK = 1;
    private static final int TEMP_WORK_RECIPES_PROGRESS_PICTURE = 1;
    private static final int TEMP_WORK_RECIPES_PROGRESS_TEMP_VALUE = 0;

    private static final long TIME_FLASH_KEEP_WARM = 1000 * 1;//10s
    private static final int HANDLER_FLASH_KEEP_WARM_TIPS = 1;//
    private static final int HANDLER_FLASH_RECIPES = 2;
    private static final int HANDLER_BLINK = 3;
    private static final int HANDLER_BLINK_EX = 4;

    private static final long TIME_SHOW_RECIPES_PICTURE = 1000 * 1; // 10
    private static final long TIME_SHOW_RECIPES_TEMP_VALUE = 1000 * 2; //30

    private static final int DISTANCE_TO_POWEROFF = 25;

    private static final int POWER_OFF_REGION_OFFSET = 40;

    private static final int HOB_RETANGLE_VIEW_GAP = 5;//30
    private static final int HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON = 10;//30

    private static final int HOB_COOKER_POSITION_UP = 0;
    private static final int HOB_COOKER_POSITION_DOWN = 1;

    private static final float CLICK_HEIGHT_RATIO = 0.7f;
    private static final float CLICK_WIDTH_RATIO = 0.9f;//0.85f
    private static final int BOTTOM_COOKER_MARTO_BOTTOM=61;

    private static final int TEMP_WITH_TIMER_WORK_PROGRESS_WORK = 0;
    private static final int TEMP_WITH_TIMER_WORK_PROGRESS_WAIT_CONFIRM_SETTING_DATA = 1;
    private static final int TEMP_WITH_TIMER_WORK_PROGRESS_WAIT_CONFIRM_START_COOK = 2;
    private int currentTempTimerWorkProgress = TEMP_WITH_TIMER_WORK_PROGRESS_WAIT_CONFIRM_SETTING_DATA;

    protected int cookerID;
    protected int workMode = HOB_VIEW_WORK_MODE_POWER_OFF;
    protected int lastWorkMode = workMode;
    private RectF mViewRect;
    private float touchDownX;
    private float touchDownY;
    private Typeface fireGearTypeface,tempGearTypeface;
    private Typeface tfHelvetica57Condensed;
    private Paint mPaint;
    private Paint picPaint;
    private Paint mAlphaPaint;
    private Paint mArcPaint;//温度圆弧
    private int alpha = 100;
    float light = 1.0f;
    //private float mArcWidth = 7;
    private int[] mGradientColors = {getResources().getColor(R.color.viewmodule_colorCircleProgress), getResources().getColor(R.color.viewmodule_colorCircleProgress), getResources().getColor(R.color.viewmodule_colorCircleProgress)};
    private Paint.FontMetrics fontMetrics;
    //圆心坐标，半径
    private Point mCenterPoint;
    private float mRadius;
    private float mStartAngle, mSweepAngle;
    private RectF mRectF;
    private int tempWorkFinishProgress = TEMP_WORK_FINISH_PROGRESS_LIGHT;
    protected int tempWorkRecipesProgress = TEMP_WORK_RECIPES_PROGRESS_PICTURE;
    protected HobCircleCookerContract.OnHobCircleCookerListener mListener;

    private static final int BLINK_OPTIONS_ONE = 0;
    private static final int BLINK_OPTIONS_TWO = 1;
    private static final long BLINK_TIME_FOR_READY_TO_COOK = 2000;
    private static final long BLINK_TIME_FOR_RECIPE_IMAGE = 5000;
    private static final long BLINK_TIME_FOR_RECIPE_VALUE = 10000;
    private static final int BLINK_FLAG_HAVE_NOT_BLINK = 0;
    private static final int BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE = 1;
    private static final int BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_TWO = 2;
    private int blinkFlag = BLINK_FLAG_HAVE_NOT_BLINK;//false
    private int currentBlinkOptions = BLINK_OPTIONS_ONE;
    private int arrow = 0;
    protected int cookerPosition = HOB_COOKER_POSITION_UP;

    private Marquee tempIndicatorMarquee;
    private String tempIndicatorText;
    private boolean tempIndicatorTextChanged = true;
    private boolean tempIndicatorTextMarqueeNeeded = false;

    private int blinkCounter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_FLASH_KEEP_WARM_TIPS:
                    invalidate();
                    //handler.sendEmptyMessageDelayed(HANDLER_FLASH_KEEP_WARM_TIPS,TIME_FLASH_KEEP_WARM);
                    break;
                case HANDLER_FLASH_RECIPES:
                    if (tempWorkRecipesProgress ==TEMP_WORK_RECIPES_PROGRESS_PICTURE) {
                        tempWorkRecipesProgress = TEMP_WORK_RECIPES_PROGRESS_TEMP_VALUE;

                    }else if(tempWorkRecipesProgress ==TEMP_WORK_RECIPES_PROGRESS_TEMP_VALUE){
                    tempWorkRecipesProgress = TEMP_WORK_RECIPES_PROGRESS_PICTURE;
                    }
                 //   LogUtil.d("the flash is ="+tempWorkRecipesProgress ) ;
                    invalidate();
                    break;
                case HANDLER_BLINK:
                    if (currentBlinkOptions == BLINK_OPTIONS_ONE) {
                        currentBlinkOptions = BLINK_OPTIONS_TWO;
                    }else if (currentBlinkOptions == BLINK_OPTIONS_TWO) {
                        currentBlinkOptions = BLINK_OPTIONS_ONE;
                    }
                    blinkFlag = BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_TWO;
                    invalidate();
                    break;
                case HANDLER_BLINK_EX:
                    blinkCounter++;
                    if (blinkCounter >= msg.arg1) {
                        if (currentBlinkOptions == BLINK_OPTIONS_ONE) {
                            currentBlinkOptions = BLINK_OPTIONS_TWO;
                        }else {
                            currentBlinkOptions = BLINK_OPTIONS_ONE;
                        }

                        blinkCounter = 0;
                    }

                    invalidate();
                    break;
            }
        }
    };


    public BaseSeperateRectangleCookerView(Context context) {
        this(context,null);

    }

    @Nullable
    @Override
    public Parcelable onSaveInstanceState() {
        LogUtil.e("Enter:: onSaveInstanceState");
        //return super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        Parcelable superData = super.onSaveInstanceState();
        bundle.putParcelable("super_data", superData);
        bundle.putInt("workMode",workMode);

        return bundle;

    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        LogUtil.e("Enter:: onRestoreInstanceState");
        //super.onRestoreInstanceState(state);
        Bundle bundle = (Bundle) state;
        Parcelable superData = bundle.getParcelable("super_data");
        workMode = bundle.getInt("workMode");
        super.onRestoreInstanceState(superData);

    }


    public BaseSeperateRectangleCookerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
       /* TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.viewmodule_HobCookerView);
        cookerID = a.getInteger(R.styleable.viewmodule_HobCookerView_viewmodule_cooker_id,-1);
        a.recycle();*/
        init();
        setSaveEnabled(true);

    }

    private void init() {
        mViewRect = new RectF();
        fireGearTypeface = GlobalVars.getInstance().getDefaultFontBold();
        tempGearTypeface = GlobalVars.getInstance().getDefaultFontBold();
        tfHelvetica57Condensed = GlobalVars.getInstance().getDefaultFontRegular();


        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(ContextCompat.getColor(getContext(),R.color.viewmodule_gear_white));
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setStyle(Paint.Style.FILL);


        picPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setColor(mGradientColors[0]);
        // 设置画笔的样式，为FILL，FILL_OR_STROKE，或STROKE
        mArcPaint.setStyle(Paint.Style.STROKE);
        // 设置画笔粗细
        mArcPaint.setStrokeWidth(getRealTempProgressArcWidth());
        // 当画笔样式为STROKE或FILL_OR_STROKE时，设置笔刷的图形样式，如圆形样式
        // Cap.ROUND,或方形样式 Cap.SQUARE
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);

        mRectF = new RectF();
        mCenterPoint = new Point();

        mAlphaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAlphaPaint.setAntiAlias(true);
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
        return getCookerViewHeight();
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return getCookerViewWidth();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
  /*      mViewRect.set(0, 0, getWidth(), getHeight());
        Paint paint = new Paint();
        if (cookerID == 0) {
            paint.setColor(Color.RED);
        }else if (cookerID == 1) {
            paint.setColor(Color.BLUE);
        }else {
            paint.setColor(Color.RED);
        }

        canvas.drawRect(mViewRect,paint);*/

        //LogUtil.d("lastMode-->" + lastWorkMode + "---current Mode--->" + workMode);
        if (lastWorkMode != workMode) {
            lastWorkMode = workMode;
            if (handler.hasMessages(HANDLER_BLINK)) handler.removeMessages(HANDLER_BLINK);
            blinkFlag = BLINK_FLAG_HAVE_NOT_BLINK;
            currentBlinkOptions = BLINK_OPTIONS_ONE;
            mPaint.setAlpha(255);
        }

        drawView(canvas);
    }

    private Marquee keepWarmMarquee;
    private String keepWarmText;
    private boolean keepWarmTextChanged = true;
    private boolean keepWarmTextMarqueeNeeded = false;

    private Marquee readyToCookMarquee;
    private String readyToCookText;
    private boolean readyToCookTextChanged = true;
    private boolean readyToCookTextMarqueeNeeded = false;

    public synchronized void setKeepWarmText(String keepWarmText) {
        this.keepWarmText = keepWarmText;
        keepWarmTextChanged = true;
    }

    public void setReadyToCookText(String readyToCookText) {
        this.readyToCookText = readyToCookText;
        readyToCookTextChanged = true;
    }

    private void drawView(Canvas canvas) {
//        LogUtil.d("Enter:: drawView-------------->" + workMode);
        PointF p;
        float top,bottom;
        int baseLineY;
        switch (workMode) {
            case HOB_VIEW_WORK_MODE_POWER_OFF:  // 关机
                mPaint.setTextAlign(Paint.Align.CENTER);
                //canvas.drawBitmap(getPowerOffBgBitmap(),(getCookerViewWidth()/2 - getPowerOffBgBitmap().getWidth()/2),(getCookerViewHeight()/2 - getPowerOffBgBitmap().getHeight()/2),null);
                canvas.drawBitmap(getPowerOffBgBitmap(),0,(getCookerViewHeight()/2 - getPowerOffBgBitmap().getHeight()/2),null);

                break;
            case HOB_VIEW_WORK_MODE_ABNORMAL_NO_PAN: // 无锅
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawBitmap(getPowerOnWithoutPanBitmap(),0,(getCookerViewHeight()/2 - getPowerOnWithoutPanBitmap().getHeight()/2),null);
                if (cookerPosition == HOB_COOKER_POSITION_UP) {
                    canvas.drawBitmap(getPowerOffButtonBitmap(),getPowerOnWithoutPanBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,15,null);

                }else if (cookerPosition == HOB_COOKER_POSITION_DOWN) {
                    canvas.drawBitmap(getPowerOffButtonBitmap(),getPowerOnWithoutPanBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,getCookerViewHeight() - BOTTOM_COOKER_MARTO_BOTTOM,null);
                }
                break;
            case HOB_VIEW_WORK_MODE_FIRE_GEAR_WITHOUT_TIMER:  // 火力 + 无 定时
                canvas.drawBitmap(getPowerOnFireGearWithoutTimerBitmap(),0,(getCookerViewHeight()/2 - getPowerOnFireGearWithoutTimerBitmap().getHeight()/2),null);
                if (cookerPosition == HOB_COOKER_POSITION_UP) {
                    canvas.drawBitmap(getPowerOffButtonBitmap(),getPowerOnFireGearWithoutTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,15,null);

                }else if (cookerPosition == HOB_COOKER_POSITION_DOWN) {
                    canvas.drawBitmap(getPowerOffButtonBitmap(),getPowerOnFireGearWithoutTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,getCookerViewHeight() - 60,null);
                }

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTextSize(getFireGearWithoutTimerFontSize());
                mPaint.setTypeface(fireGearTypeface);
                mViewRect.set(
                        30,
                        (getCookerViewHeight()/2 - getPowerOnFireGearWithoutTimerBitmap().getHeight()/2),
                        getPowerOnFireGearWithoutTimerBitmap().getWidth(),
                        (float) (getCookerViewHeight() / 2 + getPowerOnFireGearWithoutTimerBitmap().getHeight() * 0.20));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom1
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getFireGearValue(),mViewRect.centerX(),baseLineY, mPaint);


                break;
            case HOB_VIEW_WORK_MODE_FIRE_GEAR_WITH_TIMER:  // 火力 + 定时
                canvas.drawBitmap(getPowerOnFireGearWithTimerBitmap(),0,(getCookerViewHeight()/2 - getPowerOnFireGearWithTimerBitmap().getHeight()/2),null);
                if (cookerPosition == HOB_COOKER_POSITION_UP) {
                    canvas.drawBitmap(getPowerOffButtonBitmap(),getPowerOnFireGearWithTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,15,null);

                }else if (cookerPosition == HOB_COOKER_POSITION_DOWN) {
                    canvas.drawBitmap(getPowerOffButtonBitmap(),getPowerOnFireGearWithTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,getCookerViewHeight() - BOTTOM_COOKER_MARTO_BOTTOM,null);
                }

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTextSize(getFireGearWithTimerFontSize());
                mPaint.setTypeface(fireGearTypeface);
                mViewRect.set(
                        30,
                        (getCookerViewHeight() / 4 ),
                        getPowerOnFireGearWithTimerBitmap().getWidth(),
                        (float) (getCookerViewHeight() * 0.30));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式1
                canvas.drawText(getFireGearValue(),mViewRect.centerX(),baseLineY, mPaint);

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTextSize((float) (getTimerFontSize()));
                mViewRect.set(
                        45,
                        (getCookerViewHeight() / 2 ),
                        getPowerOnFireGearWithTimerBitmap().getWidth(),
                        (float) (getCookerViewHeight() * 0.90 ));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getTimerValue(),mViewRect.centerX(),baseLineY, mPaint);

                break;
            case HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP: // 高温
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawBitmap(getHighTempBitmap(),0,(getCookerViewHeight()/2 - getHighTempBitmap().getHeight()/2),null);

                break;
            case HOB_VIEW_WORK_MODE_ABNORMAL_ERROR_OCURR: // 错误代码
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawBitmap(getErrorBitmap(),0,(getCookerViewHeight()/2 - getErrorBitmap().getHeight()/2),null);
                LogUtil.d("ekek the getCookerViewHeight()/2 is = "+(getCookerViewHeight()/2)+"; getErrorBitmap().getHeight()/2 is = "+( getErrorBitmap().getHeight()/2));
                mPaint.setTextSize(getErrorFontSize());
                mPaint.setTypeface(fireGearTypeface);
                mViewRect.set(0 , getCookerViewHeight() / 2 - getErrorBitmap().getHeight() / 2 , getErrorBitmap().getWidth(), getCookerViewHeight() / 2 + getErrorBitmap().getHeight() / 2);
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getErrorMessage(),mViewRect.centerX(),baseLineY, mPaint);
                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITHOUT_TIMER: // 温度 + 无 定时
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawBitmap(getTempGearWithoutTimerBitmap(),0,(getCookerViewHeight()/2 - getTempGearWithoutTimerBitmap().getHeight()/2),null);

                if (cookerPosition == HOB_COOKER_POSITION_UP) {
                    canvas.drawBitmap(getPowerOffButtonBitmap(),getTempGearWithoutTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,15,null);

                }else if (cookerPosition == HOB_COOKER_POSITION_DOWN) {
                    canvas.drawBitmap(getPowerOffButtonBitmap(),getTempGearWithoutTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,getCookerViewHeight() - BOTTOM_COOKER_MARTO_BOTTOM,null);
                }

                mPaint.setTypeface(tempGearTypeface);
                mPaint.setTextSize(getTempGearWithoutTimerFontSize());
                mViewRect.set(
                        30,
                        (float) (getCookerViewHeight() * 0.2),
                        getTempSensorReadyValueBitmap().getWidth(),
                        (float) (getCookerViewHeight() * 0.42));


                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getTempGearValue().length() <= 2?(" " + getTempGearValue()  + "º"): " " + getTempGearValue() + "º",mViewRect.centerX(),baseLineY, mPaint);//º


                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER:  // 温度+ 定时
                canvas.drawBitmap(getTempGearWithTimerBitmap(),0,(getCookerViewHeight()/2 - getTempGearWithoutTimerBitmap().getHeight()/2),null);

                if (cookerPosition == HOB_COOKER_POSITION_UP) {
                    canvas.drawBitmap(getPowerOffButtonBitmap(),getTempGearWithTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,15,null);

                }else if (cookerPosition == HOB_COOKER_POSITION_DOWN) {
                    canvas.drawBitmap(getPowerOffButtonBitmap(),getTempGearWithTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,getCookerViewHeight() - BOTTOM_COOKER_MARTO_BOTTOM,null);
                }
                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTypeface(tempGearTypeface);
                mPaint.setTextSize(getTempGearWithTimerFontSize());

                mViewRect.set(
                        30,
                        (getCookerViewHeight() / 4 ),
                        getPowerOnFireGearWithTimerBitmap().getWidth(),
                        (float) (getCookerViewHeight() * 0.30));

                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getTempGearValue().length() <= 2?(" " + getTempGearValue()  + "º"): " " + getTempGearValue() + "º",mViewRect.centerX(),baseLineY, mPaint);//º

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTextSize((float) (getTimerFontSize()));
                mViewRect.set(
                        30,
                        (getCookerViewHeight() / 2 ),
                        getPowerOnFireGearWithTimerBitmap().getWidth(),
                        (float) (getCookerViewHeight() * 0.90 ));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getTimerValue(),mViewRect.centerX(),baseLineY, mPaint);
                break;
            case HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITHOUT_TIMER:  // 无温度 + paly 键

                mPaint.setTextAlign(Paint.Align.CENTER);
                if (blinkFlag == BLINK_FLAG_HAVE_NOT_BLINK) {
                    handler.removeMessages(HANDLER_BLINK);
                    currentBlinkOptions = BLINK_OPTIONS_ONE;
                    blinkFlag = BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE;

                    canvas.drawBitmap(getTempGearIndicatorWithoutTimerBitmap(),0,(getCookerViewHeight()/2 - getTempGearIndicatorWithoutTimerBitmap().getHeight()/2),null);
                    if (cookerPosition == HOB_COOKER_POSITION_UP) {
                        canvas.drawBitmap(getPowerOffButtonBitmap(),getTempGearIndicatorWithoutTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,15,null);

                    }else if (cookerPosition == HOB_COOKER_POSITION_DOWN) {
                        canvas.drawBitmap(getPowerOffButtonBitmap(),getTempGearIndicatorWithoutTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,getCookerViewHeight() - BOTTOM_COOKER_MARTO_BOTTOM,null);
                    }

                    drawTempIndicator(canvas, getTempIndicatorBitmap(), getTempGearIndicatorWithoutTimerBitmap(), 0.4f);

                    handler.sendEmptyMessageDelayed(HANDLER_BLINK,BLINK_TIME_FOR_READY_TO_COOK);
                }else if(blinkFlag == BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE) {
                    if (currentBlinkOptions == BLINK_OPTIONS_ONE) {//ready to cook

                        canvas.drawBitmap(getTempGearIndicatorWithoutTimerBitmap(),0,(getCookerViewHeight()/2 - getTempGearIndicatorWithoutTimerBitmap().getHeight()/2),null);
                        if (cookerPosition == HOB_COOKER_POSITION_UP) {
                            canvas.drawBitmap(getPowerOffButtonBitmap(),getTempGearIndicatorWithoutTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,15,null);

                        }else if (cookerPosition == HOB_COOKER_POSITION_DOWN) {
                            canvas.drawBitmap(getPowerOffButtonBitmap(),getTempGearIndicatorWithoutTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,getCookerViewHeight() - BOTTOM_COOKER_MARTO_BOTTOM,null);
                        }

                        drawTempIndicator(canvas, getTempIndicatorBitmap(), getTempGearIndicatorWithoutTimerBitmap(), 0.4f);
                    }else {//value

                        canvas.drawBitmap(getTempGearWithoutTimerBitmap(),0,(getCookerViewHeight()/2 - getTempGearWithoutTimerBitmap().getHeight()/2),null);

                        if (cookerPosition == HOB_COOKER_POSITION_UP) {
                            canvas.drawBitmap(getPowerOffButtonBitmap(),getTempGearWithoutTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,15,null);

                        }else if (cookerPosition == HOB_COOKER_POSITION_DOWN) {
                            canvas.drawBitmap(getPowerOffButtonBitmap(),getTempGearWithoutTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,getCookerViewHeight() - BOTTOM_COOKER_MARTO_BOTTOM,null);
                        }

                        mPaint.setTextAlign(Paint.Align.CENTER);
                        mPaint.setTypeface(tempGearTypeface);
                        mPaint.setTextSize(getTempGearWithoutTimerFontSize());
                        mViewRect.set(
                                30,
                                (float) (getCookerViewHeight() * 0.2),
                                getTempSensorReadyValueBitmap().getWidth(),
                                (float) (getCookerViewHeight() * 0.42));


                        fontMetrics = mPaint.getFontMetrics();
                        top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                        bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                        baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                        canvas.drawText(getTempGearValue().length() <= 2?(" " + getTempGearValue()  + "º"): " " + getTempGearValue() + "º",mViewRect.centerX(),baseLineY, mPaint);//º

                    }

                }else if(blinkFlag == BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_TWO) {
                    handler.removeMessages(HANDLER_BLINK);
                    if (currentBlinkOptions == BLINK_OPTIONS_ONE) {

                        canvas.drawBitmap(getTempGearIndicatorWithoutTimerBitmap(),0,(getCookerViewHeight()/2 - getTempGearIndicatorWithoutTimerBitmap().getHeight()/2),null);
                        if (cookerPosition == HOB_COOKER_POSITION_UP) {
                            canvas.drawBitmap(getPowerOffButtonBitmap(),getTempGearIndicatorWithoutTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,15,null);

                        }else if (cookerPosition == HOB_COOKER_POSITION_DOWN) {
                            canvas.drawBitmap(getPowerOffButtonBitmap(),getTempGearIndicatorWithoutTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,getCookerViewHeight() - BOTTOM_COOKER_MARTO_BOTTOM,null);
                        }

                        drawTempIndicator(canvas, getTempIndicatorBitmap(), getTempGearIndicatorWithoutTimerBitmap(), 0.4f);
                        handler.sendEmptyMessageDelayed(HANDLER_BLINK,BLINK_TIME_FOR_READY_TO_COOK);
                    }else {//value


                        canvas.drawBitmap(getTempGearWithoutTimerBitmap(),0,(getCookerViewHeight()/2 - getTempGearWithoutTimerBitmap().getHeight()/2),null);
                        if (cookerPosition == HOB_COOKER_POSITION_UP) {
                            canvas.drawBitmap(getPowerOffButtonBitmap(),getTempGearWithoutTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,15,null);

                        }else if (cookerPosition == HOB_COOKER_POSITION_DOWN) {
                            canvas.drawBitmap(getPowerOffButtonBitmap(),getTempGearWithoutTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,getCookerViewHeight() - BOTTOM_COOKER_MARTO_BOTTOM,null);
                        }

                        mPaint.setTextAlign(Paint.Align.CENTER);
                        mPaint.setTypeface(tempGearTypeface);
                        mPaint.setTextSize(getTempGearWithoutTimerFontSize());
                        mViewRect.set(
                                30,
                                (float) (getCookerViewHeight() * 0.2),
                                getTempSensorReadyValueBitmap().getWidth(),
                                (float) (getCookerViewHeight() * 0.42));


                        fontMetrics = mPaint.getFontMetrics();
                        top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                        bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                        baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                        canvas.drawText(getTempGearValue().length() <= 2?(" " + getTempGearValue()  + "º"): " " + getTempGearValue() + "º",mViewRect.centerX(),baseLineY, mPaint);//º


                        handler.sendEmptyMessageDelayed(HANDLER_BLINK,BLINK_TIME_FOR_READY_TO_COOK);
                    }



                    blinkFlag = BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE;
                }

                break;
            case HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER:  // 火力图标 + 定时

                mPaint.setTextAlign(Paint.Align.CENTER);
                if (blinkFlag == BLINK_FLAG_HAVE_NOT_BLINK) {
                    handler.removeMessages(HANDLER_BLINK);
                    currentBlinkOptions = BLINK_OPTIONS_ONE;
                    blinkFlag = BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE;

                    canvas.drawBitmap(getTempGearIndicatorWithTimerBitmap(),0,(getCookerViewHeight()/2 - getTempGearIndicatorWithTimerBitmap().getHeight()/2),null);
                    if (cookerPosition == HOB_COOKER_POSITION_UP) {
                        canvas.drawBitmap(getPowerOffButtonBitmap(),getTempGearIndicatorWithTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,15,null);

                    }else if (cookerPosition == HOB_COOKER_POSITION_DOWN) {
                        canvas.drawBitmap(getPowerOffButtonBitmap(),getTempGearIndicatorWithTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,getCookerViewHeight() - BOTTOM_COOKER_MARTO_BOTTOM,null);
                    }

                    drawTempIndicator(canvas, getTempIndicatorBitmap(), getTempGearIndicatorWithTimerBitmap(), 0.43f);

                    mPaint.setTextAlign(Paint.Align.CENTER);
                    mPaint.setTypeface(tempGearTypeface);
                    mPaint.setTextSize((float) (getTimerFontSize()));
                    mViewRect.set(
                            30,
                            (getCookerViewHeight() / 2 ),
                            getPowerOnFireGearWithTimerBitmap().getWidth(),
                            (float) (getCookerViewHeight() * 0.90 ));
                    fontMetrics = mPaint.getFontMetrics();
                    top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                    bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                    baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                    canvas.drawText(getTimerValue(),mViewRect.centerX(),baseLineY, mPaint);


                    handler.sendEmptyMessageDelayed(HANDLER_BLINK,BLINK_TIME_FOR_READY_TO_COOK);
                }else if(blinkFlag == BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE) {
                    if (currentBlinkOptions == BLINK_OPTIONS_ONE) {//ready to cook

                        canvas.drawBitmap(getTempGearIndicatorWithTimerBitmap(),0,(getCookerViewHeight()/2 - getTempGearIndicatorWithTimerBitmap().getHeight()/2),null);
                        if (cookerPosition == HOB_COOKER_POSITION_UP) {
                            canvas.drawBitmap(getPowerOffButtonBitmap(),getTempGearIndicatorWithTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,15,null);

                        }else if (cookerPosition == HOB_COOKER_POSITION_DOWN) {
                            canvas.drawBitmap(getPowerOffButtonBitmap(),getTempGearIndicatorWithTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,getCookerViewHeight() - BOTTOM_COOKER_MARTO_BOTTOM,null);
                        }

                        drawTempIndicator(canvas, getTempIndicatorBitmap(), getTempGearIndicatorWithTimerBitmap(), 0.43f);

                        mPaint.setTextAlign(Paint.Align.CENTER);
                        mPaint.setTypeface(tempGearTypeface);
                        mPaint.setTextSize((float) (getTimerFontSize()));
                        mViewRect.set(
                                30,
                                (getCookerViewHeight() / 2 ),
                                getPowerOnFireGearWithTimerBitmap().getWidth(),
                                (float) (getCookerViewHeight() * 0.90 ));
                        fontMetrics = mPaint.getFontMetrics();
                        top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                        bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                        baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                        canvas.drawText(getTimerValue(),mViewRect.centerX(),baseLineY, mPaint);

                    }else {//value

                        canvas.drawBitmap(getTempGearWithTimerBitmap(),0,(getCookerViewHeight()/2 - getTempGearWithoutTimerBitmap().getHeight()/2),null);

                        if (cookerPosition == HOB_COOKER_POSITION_UP) {
                            canvas.drawBitmap(getPowerOffButtonBitmap(),getTempGearWithTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,15,null);

                        }else if (cookerPosition == HOB_COOKER_POSITION_DOWN) {
                            canvas.drawBitmap(getPowerOffButtonBitmap(),getTempGearWithTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,getCookerViewHeight() - BOTTOM_COOKER_MARTO_BOTTOM,null);
                        }

                        mPaint.setTextAlign(Paint.Align.CENTER);
                        mPaint.setTypeface(tempGearTypeface);
                        mPaint.setTextSize(getTempGearWithTimerFontSize());

                        mViewRect.set(
                                30,
                                (getCookerViewHeight() / 4 ),
                                getPowerOnFireGearWithTimerBitmap().getWidth(),
                                (float) (getCookerViewHeight() * 0.30));

                        fontMetrics = mPaint.getFontMetrics();
                        top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                        bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                        baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                        canvas.drawText(getTempGearValue().length() <= 2?(" " + getTempGearValue()  + "º"): " " + getTempGearValue() + "º",mViewRect.centerX(),baseLineY, mPaint);//º

                        mPaint.setTextAlign(Paint.Align.CENTER);
                        mPaint.setTextSize((float) (getTimerFontSize()));
                        mViewRect.set(
                                30,
                                (getCookerViewHeight() / 2 ),
                                getPowerOnFireGearWithTimerBitmap().getWidth(),
                                (float) (getCookerViewHeight() * 0.90 ));
                        fontMetrics = mPaint.getFontMetrics();
                        top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                        bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                        baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                        canvas.drawText(getTimerValue(),mViewRect.centerX(),baseLineY, mPaint);
                    }

                }else if(blinkFlag == BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_TWO) {
                    handler.removeMessages(HANDLER_BLINK);
                    if (currentBlinkOptions == BLINK_OPTIONS_ONE) {

                        canvas.drawBitmap(getTempGearIndicatorWithTimerBitmap(),0,(getCookerViewHeight()/2 - getTempGearIndicatorWithTimerBitmap().getHeight()/2),null);
                        if (cookerPosition == HOB_COOKER_POSITION_UP) {
                            canvas.drawBitmap(getPowerOffButtonBitmap(),getTempGearIndicatorWithTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,15,null);

                        }else if (cookerPosition == HOB_COOKER_POSITION_DOWN) {
                            canvas.drawBitmap(getPowerOffButtonBitmap(),getTempGearIndicatorWithTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,getCookerViewHeight() - BOTTOM_COOKER_MARTO_BOTTOM,null);
                        }

                        drawTempIndicator(canvas, getTempIndicatorBitmap(), getTempGearIndicatorWithTimerBitmap(), 0.43f);

                        mPaint.setTextAlign(Paint.Align.CENTER);
                        mPaint.setTypeface(tempGearTypeface);
                        mPaint.setTextSize((float) (getTimerFontSize()));
                        mViewRect.set(
                                30,
                                (getCookerViewHeight() / 2 ),
                                getPowerOnFireGearWithTimerBitmap().getWidth(),
                                (float) (getCookerViewHeight() * 0.90 ));
                        fontMetrics = mPaint.getFontMetrics();
                        top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                        bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                        baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                        canvas.drawText(getTimerValue(),mViewRect.centerX(),baseLineY, mPaint);

                        handler.sendEmptyMessageDelayed(HANDLER_BLINK,BLINK_TIME_FOR_READY_TO_COOK);
                    }else {//value


                        canvas.drawBitmap(getTempGearWithTimerBitmap(),0,(getCookerViewHeight()/2 - getTempGearWithTimerBitmap().getHeight()/2),null);
                        if (cookerPosition == HOB_COOKER_POSITION_UP) {
                            canvas.drawBitmap(getPowerOffButtonBitmap(),getTempGearWithTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,15,null);

                        }else if (cookerPosition == HOB_COOKER_POSITION_DOWN) {
                            canvas.drawBitmap(getPowerOffButtonBitmap(),getTempGearWithTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,getCookerViewHeight() - BOTTOM_COOKER_MARTO_BOTTOM,null);
                        }

                        mPaint.setTextAlign(Paint.Align.CENTER);
                        mPaint.setTypeface(tempGearTypeface);
                        mPaint.setTextSize(getTempGearWithTimerFontSize());
                        mViewRect.set(
                                30,
                                (getCookerViewHeight() / 4 ),
                                getPowerOnFireGearWithTimerBitmap().getWidth(),
                                (float) (getCookerViewHeight() * 0.30));

                        fontMetrics = mPaint.getFontMetrics();
                        top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                        bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                        baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                        canvas.drawText(getTempGearValue().length() <= 2?(" " + getTempGearValue()  + "º"): " " + getTempGearValue() + "º",mViewRect.centerX(),baseLineY, mPaint);//º

                        mPaint.setTextAlign(Paint.Align.CENTER);
                        mPaint.setTextSize((float) (getTimerFontSize()));
                        mViewRect.set(
                                30,
                                (getCookerViewHeight() / 2 ),
                                getPowerOnFireGearWithTimerBitmap().getWidth(),
                                (float) (getCookerViewHeight() * 0.90 ));
                        fontMetrics = mPaint.getFontMetrics();
                        top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                        bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                        baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                        canvas.drawText(getTimerValue(),mViewRect.centerX(),baseLineY, mPaint);

                        handler.sendEmptyMessageDelayed(HANDLER_BLINK,BLINK_TIME_FOR_READY_TO_COOK);
                    }



                    blinkFlag = BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE;
                }


                break;
            case HOB_VIEW_WORK_MODE_PAUSE:  // 暂停
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawBitmap(getPowerOffBgBitmap(),0,(getCookerViewHeight()/2 - getPowerOffBgBitmap().getHeight()/2),null);
                break;
            case HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR:  // 精确控温： 温度 + play 键
                canvas.drawBitmap(getTempPrepareSensorBitmap(),0,0,null);
                if (cookerPosition == HOB_COOKER_POSITION_UP) {
                    canvas.drawBitmap(getPowerOffButtonBitmap(),getTempPrepareSensorBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,15,null);

                }else if (cookerPosition == HOB_COOKER_POSITION_DOWN) {
                    canvas.drawBitmap(getPowerOffButtonBitmap(),getTempPrepareSensorBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,getCookerViewHeight() - BOTTOM_COOKER_MARTO_BOTTOM,null);
                }

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTypeface(fireGearTypeface);
                mPaint.setTextSize(getTempPreheatSettingValueFontSize());//140
                mPaint.setFakeBoldText(true);//getSeperateTempGearWithTimerBitmap
                mViewRect.set(
                        getTempPrepareSensorBitmap().getWidth() / 20,
                        (float) (getCookerViewHeight() / 4),//0.4
                        getTempPrepareSensorBitmap().getWidth(),
                        (float) (getCookerViewHeight() * 0.25));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getTempGearValue().length() <= 2?(" " + getTempGearValue()  + "º"): " " + getTempGearValue() + "º",mViewRect.centerX(),baseLineY, mPaint);//º

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTypeface(tfHelvetica57Condensed);
                mPaint.setTextSize(getTempPreheatRealValueFontSize());
                mPaint.setFakeBoldText(false);//getSeperateTempGearWithTimerBitmap
                mViewRect.set(
                        getTempPrepareSensorBitmap().getWidth() / 20,
                        (float) (getCookerViewHeight() / 2 ),
                        getTempPrepareSensorBitmap().getWidth(),
                        (float) (getCookerViewHeight() * getRealTempTextGap()));//1.23
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getRealTempValue() + "º",mViewRect.centerX(),baseLineY, mPaint);//º


                mCenterPoint.x = (int) (getTempPrepareSensorBitmap().getWidth() * 0.525); //0.503
                mCenterPoint.y = (int) (getCookerViewHeight() * getRealTempArcGap());//0.66
                mRectF.left = mCenterPoint.x - getRealTempProgressRadius() - getRealTempProgressArcWidth() / 2;
                mRectF.top = mCenterPoint.y - getRealTempProgressRadius() - getRealTempProgressArcWidth() / 2;
                mRectF.right = mCenterPoint.x + getRealTempProgressRadius() + getRealTempProgressArcWidth() / 2;
                mRectF.bottom = mCenterPoint.y + getRealTempProgressRadius() + getRealTempProgressArcWidth() / 2;
                mArcPaint.setStrokeWidth(getRealTempProgressArcWidth());
                canvas.drawArc(mRectF, -90, getSweepAngle(), false, mArcPaint);



                break;
            case HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR_WITH_INDICATOR: //  无温度 + 无定时
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawBitmap(getTempIndicatorPrepareSensorBitmap(),0,(getCookerViewHeight()/2 - getTempIndicatorPrepareSensorBitmap().getHeight()/2),null);
                p = getPowerOffButtonLocation(getTempIndicatorPrepareSensorBitmap().getWidth());
                canvas.drawBitmap(getPowerOffButtonBitmap(),p.x,p.y,null);

                canvas.drawBitmap(getTempIndicatorBitmapWithTimer(),getTempGearIndicatorWithTimerBitmap().getWidth() / 2 - getTempIndicatorBitmapWithTimer().getWidth() / 2, (float) (getCookerViewHeight() / 2 - getTempGearIndicatorWithTimerBitmap().getHeight() * 0.4),null);

                break;
            case HOB_VIEW_WORK_MODE_TEMP_SENSOR_READY:  // 蓝牙配对后，ready to cook

                mPaint.setTextAlign(Paint.Align.CENTER);
                if (readyToCookText == null) {
                    readyToCookText = getContext().getResources().getString(R.string.viewmodule_cooker_view_ready_to_cook);
                    readyToCookTextChanged = true;
                }

                if (readyToCookTextChanged) {
                    Rect rect = getStringRect(readyToCookText);
                    if (rect.width() > getInnerTextMaxWidthBig()) {
                        readyToCookTextMarqueeNeeded = true;
                        baseLineY = getReadyToCookY();
                        readyToCookMarquee = new Marquee(
                                readyToCookText,
                                new RectF(
                                        mViewRect.centerX() - getInnerTextMaxWidthBig() / 2,
                                        mViewRect.top,
                                        mViewRect.centerX() + getInnerTextMaxWidthBig() / 2,
                                        mViewRect.bottom
                                ),
                                new PointF(
                                        mViewRect.centerX() - getInnerTextMaxWidthBig() / 2,
                                        baseLineY
                                ),
                                4
                        );
                    } else {
                        readyToCookTextMarqueeNeeded = false;
                    }
                    readyToCookTextChanged = false;
                }

                Message msg = new Message();
                msg.what = HANDLER_BLINK_EX;
                if (blinkFlag == BLINK_FLAG_HAVE_NOT_BLINK) {
                    handler.removeMessages(HANDLER_BLINK_EX);
                    currentBlinkOptions = BLINK_OPTIONS_ONE;
                    blinkFlag = BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE;
                    blinkCounter = 0;
                    arrow = 0;
                    alpha = 255;
                    mAlphaPaint.setAlpha(alpha);
                    canvas.drawBitmap(getTempSensorReadyBitmap(),0,(getCookerViewHeight()/2 - getTempSensorReadyBitmap().getHeight()/2),mAlphaPaint);

                    if (cookerPosition == HOB_COOKER_POSITION_UP) {
                        canvas.drawBitmap(getPowerOffButtonBitmap(),getTempSensorReadyBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,15,null);

                    }else if (cookerPosition == HOB_COOKER_POSITION_DOWN) {
                        canvas.drawBitmap(getPowerOffButtonBitmap(),getTempSensorReadyBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,getCookerViewHeight() - BOTTOM_COOKER_MARTO_BOTTOM,null);
                    }

                    mPaint.setTextAlign(Paint.Align.CENTER);
                    mPaint.setTypeface(tempGearTypeface);
                    mPaint.setTextSize(getReadyToCookFontSize());
                    mPaint.setAlpha(alpha);
                    baseLineY = getReadyToCookY();
                    if (!readyToCookTextMarqueeNeeded) {
                        mPaint.setTextAlign(Paint.Align.CENTER);
                        canvas.drawText(readyToCookText, mViewRect.centerX(), baseLineY, mPaint);
                    } else {
                        mPaint.setTextAlign(Paint.Align.LEFT);
                        readyToCookMarquee.marquee(canvas, mPaint);
                    }

                    msg.arg1 = 67;
                    msg.arg2 = blinkFlag;
                    handler.sendMessageDelayed(msg, 30);

                } else {
                    handler.removeMessages(HANDLER_BLINK_EX);
                    if (arrow == 0) {
                        alpha = alpha - 10;
                        if (alpha < 110) {
                            alpha = 110;
                            arrow = 1;
                        }
                    }else {
                        alpha = alpha + 10;
                        if (alpha > 255) {
                            alpha = 255;
                            arrow = 0;
                        }
                    }

                    mAlphaPaint.setAlpha(alpha);
                    if (currentBlinkOptions == BLINK_OPTIONS_ONE) {
                        //ready to cook
                        canvas.drawBitmap(getTempSensorReadyBitmap(),0,(getCookerViewHeight()/2 - getTempSensorReadyBitmap().getHeight()/2),mAlphaPaint);

                        if (cookerPosition == HOB_COOKER_POSITION_UP) {
                            canvas.drawBitmap(getPowerOffButtonBitmap(),getTempSensorReadyBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,15,null);

                        }else if (cookerPosition == HOB_COOKER_POSITION_DOWN) {
                            canvas.drawBitmap(getPowerOffButtonBitmap(),getTempSensorReadyBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,getCookerViewHeight() - BOTTOM_COOKER_MARTO_BOTTOM,null);
                        }

                        mPaint.setTextAlign(Paint.Align.CENTER);
                        mPaint.setTypeface(tempGearTypeface);
                        mPaint.setTextSize(getReadyToCookFontSize());
                        mPaint.setAlpha(alpha);
                        baseLineY = getReadyToCookY();
                        if (!readyToCookTextMarqueeNeeded) {
                            mPaint.setTextAlign(Paint.Align.CENTER);
                            canvas.drawText(readyToCookText, mViewRect.centerX(), baseLineY, mPaint);
                        } else {
                            mPaint.setTextAlign(Paint.Align.LEFT);
                            readyToCookMarquee.marquee(canvas, mPaint);
                        }

                    }else {
                        //value
                        if (readyToCookTextMarqueeNeeded) {
                            readyToCookMarquee.reset(-24);
                        }
                        canvas.drawBitmap(getTempSensorReadyValueBitmap(),0,(getCookerViewHeight()/2 - getTempSensorReadyValueBitmap().getHeight()/2),mAlphaPaint);

                        if (cookerPosition == HOB_COOKER_POSITION_UP) {
                            canvas.drawBitmap(getPowerOffButtonBitmap(),getTempSensorReadyBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,15,null);

                        }else if (cookerPosition == HOB_COOKER_POSITION_DOWN) {
                            canvas.drawBitmap(getPowerOffButtonBitmap(),getTempSensorReadyBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,getCookerViewHeight() - BOTTOM_COOKER_MARTO_BOTTOM,null);
                        }

                        mPaint.setTextAlign(Paint.Align.CENTER);
                        mPaint.setTypeface(fireGearTypeface);
                        mPaint.setTextSize(getTempPreheatSettingValueFontSize());//140
                        mPaint.setFakeBoldText(true);
                        mPaint.setAlpha(alpha);
                        mViewRect.set(
                                40,
                                (getCookerViewHeight()/2 - getTempSensorReadyValueBitmap().getHeight()/2),
                                getTempSensorReadyValueBitmap().getWidth(),
                                (float) (getCookerViewHeight() * 0.52 ));
                        fontMetrics = mPaint.getFontMetrics();
                        top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                        bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                        baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                        canvas.drawText(getTempGearValue().length() <= 2?(" " + getTempGearValue()  + "º"): " " + getTempGearValue() + "º",mViewRect.centerX(),baseLineY, mPaint);//º
                    }

                    msg.arg1 = 67;
                    msg.arg2 = blinkFlag;
                    handler.sendMessageDelayed(msg, 30);
                }


                break;
            case HOB_VIEW_WORK_MODE_TEMP_WORK_FINISH_WAIT_USER_CONFIRM:  // +10 min    keep warm

                mPaint.setTextAlign(Paint.Align.CENTER);
                if (blinkFlag == BLINK_FLAG_HAVE_NOT_BLINK) {
                    handler.removeMessages(HANDLER_BLINK);
                    blinkFlag = BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE;
                    arrow = 0;
                    light = 1.0f;
                    alpha = 255;
                    mAlphaPaint.setAlpha(alpha);
                    canvas.drawBitmap(getTempWorkFinishLightBitmap(),0,(getCookerViewHeight()/2 - getTempWorkFinishLightBitmap().getHeight()/2),mAlphaPaint);
                    handler.sendEmptyMessageDelayed(HANDLER_BLINK,30);
                }else if(blinkFlag == BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE) {

                    canvas.drawBitmap(getTempWorkFinishLightBitmap(),0,(getCookerViewHeight()/2 - getTempWorkFinishLightBitmap().getHeight()/2),mAlphaPaint);

                }else if(blinkFlag == BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_TWO) {
                    handler.removeMessages(HANDLER_BLINK);

                    if (arrow == 0) {
                        alpha = alpha - 10;
                        if (alpha < 110) {
                            alpha = 110;
                            arrow = 1;
                        }
                    }else {
                        alpha = alpha + 10;
                        if (alpha > 255) {
                            alpha = 255;
                            arrow = 0;
                        }
                    }

                    mAlphaPaint.setAlpha(alpha);
                    canvas.drawBitmap(getTempWorkFinishLightBitmap(),0,(getCookerViewHeight()/2 - getTempWorkFinishLightBitmap().getHeight()/2),mAlphaPaint);
                    blinkFlag = BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE;
                    handler.sendEmptyMessageDelayed(HANDLER_BLINK,30);
                }


                if (cookerPosition == HOB_COOKER_POSITION_UP) {
                    canvas.drawBitmap(getPowerOffButtonBitmap(),getTempWorkFinishLightBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,15,null);

                }else if (cookerPosition == HOB_COOKER_POSITION_DOWN) {
                    canvas.drawBitmap(getPowerOffButtonBitmap(),getTempWorkFinishLightBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,getCookerViewHeight() - BOTTOM_COOKER_MARTO_BOTTOM,null);
                }

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTypeface(tempGearTypeface);
                mPaint.setAlpha(alpha);
                mPaint.setTextSize(getKeepWarmAndAddTenMinuteFontSize());
                mViewRect.set(0,
                        (float) (getCookerViewHeight() / 2 ),
                        getTempWorkFinishLightBitmap().getWidth(),
                        getCookerViewHeight()/ 2);
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("+10min",mViewRect.centerX(),baseLineY, mPaint);

                /*mViewRect.set(0, (float) (getTempWorkFinishLightBitmap().getHeight() *0.4), getTempWorkFinishLightBitmap().getWidth(), getCookerViewHeight());
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式

                if (keepWarmText == null) {
                    keepWarmText = getContext().getResources().getString(R.string.viewmodule_base_circle_cooker_view_keep_warm);
                    keepWarmTextChanged = true;
                }

                if (keepWarmTextChanged) {
                    Rect rect = getStringRect(keepWarmText);
                    if (rect.width() > getInnerTextMaxWidth()) {
                        keepWarmTextMarqueeNeeded = true;
                        keepWarmMarquee = new Marquee(
                                keepWarmText,
                                new RectF(
                                        mViewRect.centerX() - getInnerTextMaxWidth() / 2,
                                        mViewRect.top,
                                        mViewRect.centerX() + getInnerTextMaxWidth() / 2,
                                        mViewRect.bottom
                                ),
                                new PointF(
                                        mViewRect.centerX() - getInnerTextMaxWidth() / 2,
                                        baseLineY
                                ),
                                1);
                    } else {
                        keepWarmTextMarqueeNeeded = false;
                    }
                    keepWarmTextChanged = false;
                }

                if (!keepWarmTextMarqueeNeeded) {
                    mPaint.setTextAlign(Paint.Align.CENTER);
                    canvas.drawText(keepWarmText, mViewRect.centerX(), baseLineY, mPaint);
                } else {
                    mPaint.setTextAlign(Paint.Align.LEFT);
                    keepWarmMarquee.marquee(canvas, mPaint);
                }*/
                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_FIRST:  // 温度+定时　　＋　图片  601

                mPaint.setTextAlign(Paint.Align.CENTER);
                if (blinkFlag == BLINK_FLAG_HAVE_NOT_BLINK) {
                    handler.removeMessages(HANDLER_BLINK);
                    currentBlinkOptions = BLINK_OPTIONS_ONE;
                    blinkFlag = BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE;
                    canvas.drawBitmap(getRecipesBgBitmap(),0,(getCookerViewHeight()/2 - getRecipesBgBitmap().getHeight()/2),null);
                    if (cookerPosition == HOB_COOKER_POSITION_UP) {
                        canvas.drawBitmap(getPowerOffButtonBitmap(),getRecipesBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,15,null);

                    }else if (cookerPosition == HOB_COOKER_POSITION_DOWN) {
                        canvas.drawBitmap(getPowerOffButtonBitmap(),getRecipesBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,getCookerViewHeight() - BOTTOM_COOKER_MARTO_BOTTOM,null);
                    }

                    canvas.drawBitmap(
                            getRecipesPicBitmap(),
                            (getRecipesBgBitmap().getWidth() / 2 - getRecipesPicBitmap().getWidth()/2),
                            (getCookerViewHeight()/2 - getRecipesPicBitmap().getHeight()/2),
                            null);


                    handler.sendEmptyMessageDelayed(HANDLER_BLINK,5000);

                }else if(blinkFlag == BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE) {

                    if (currentBlinkOptions == BLINK_OPTIONS_ONE) {//recipes
                        canvas.drawBitmap(getRecipesBgBitmap(),0,(getCookerViewHeight()/2 - getRecipesBgBitmap().getHeight()/2),null);
                        if (cookerPosition == HOB_COOKER_POSITION_UP) {
                            canvas.drawBitmap(getPowerOffButtonBitmap(),getRecipesBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,15,null);

                        }else if (cookerPosition == HOB_COOKER_POSITION_DOWN) {
                            canvas.drawBitmap(getPowerOffButtonBitmap(),getRecipesBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,getCookerViewHeight() - BOTTOM_COOKER_MARTO_BOTTOM,null);
                        }

                        canvas.drawBitmap(getRecipesPicBitmap(),(getRecipesBgBitmap().getWidth() / 2 - getRecipesPicBitmap().getWidth()/2),(getCookerViewHeight()/2 - getRecipesPicBitmap().getHeight()/2),null);

                    }else {//value


                        canvas.drawBitmap(getTempGearWithTimerBitmap(),0,(getCookerViewHeight()/2 - getTempGearWithoutTimerBitmap().getHeight()/2),null);

                        if (cookerPosition == HOB_COOKER_POSITION_UP) {
                            canvas.drawBitmap(getPowerOffButtonBitmap(),getTempGearWithTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,15,null);

                        }else if (cookerPosition == HOB_COOKER_POSITION_DOWN) {
                            canvas.drawBitmap(getPowerOffButtonBitmap(),getTempGearWithTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,getCookerViewHeight() - BOTTOM_COOKER_MARTO_BOTTOM,null);
                        }
                        mPaint.setTextAlign(Paint.Align.CENTER);
                        mPaint.setTypeface(tempGearTypeface);
                        mPaint.setTextSize(getTempGearWithTimerFontSize());

                        mViewRect.set(
                                30,
                                (getCookerViewHeight() / 4 ),
                                getPowerOnFireGearWithTimerBitmap().getWidth(),
                                (float) (getCookerViewHeight() * 0.30));
                        fontMetrics = mPaint.getFontMetrics();
                        top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                        bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                        baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                        canvas.drawText(getTempGearValue().length() <= 2?(" " + getTempGearValue()  + "º"): " " + getTempGearValue() + "º",mViewRect.centerX(),baseLineY, mPaint);//º

                        mPaint.setTextAlign(Paint.Align.CENTER);
                        mPaint.setTextSize((float) (getTimerFontSize()));
                        mViewRect.set(
                                30,
                                (getCookerViewHeight() / 2 ),
                                getPowerOnFireGearWithTimerBitmap().getWidth(),
                                (float) (getCookerViewHeight() * 0.90 ));
                        fontMetrics = mPaint.getFontMetrics();
                        top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                        bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                        baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                        canvas.drawText(getTimerValue(),mViewRect.centerX(),baseLineY, mPaint);
                    }

                }else if(blinkFlag == BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_TWO) {
                    handler.removeMessages(HANDLER_BLINK);
                    if (currentBlinkOptions == BLINK_OPTIONS_ONE) {//ready to cook
                        canvas.drawBitmap(getRecipesBgBitmap(),0,(getCookerViewHeight()/2 - getRecipesBgBitmap().getHeight()/2),null);
                        if (cookerPosition == HOB_COOKER_POSITION_UP) {
                            canvas.drawBitmap(getPowerOffButtonBitmap(),getRecipesBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,15,null);

                        }else if (cookerPosition == HOB_COOKER_POSITION_DOWN) {
                            canvas.drawBitmap(getPowerOffButtonBitmap(),getRecipesBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,getCookerViewHeight() - BOTTOM_COOKER_MARTO_BOTTOM,null);
                        }

                        canvas.drawBitmap(getRecipesPicBitmap(),(getRecipesBgBitmap().getWidth() / 2 - getRecipesPicBitmap().getWidth()/2),(getCookerViewHeight()/2 - getRecipesPicBitmap().getHeight()/2),null);

                        handler.sendEmptyMessageDelayed(HANDLER_BLINK,5000);
                    }else {//value

                        canvas.drawBitmap(getTempGearWithTimerBitmap(),0,(getCookerViewHeight()/2 - getTempGearWithoutTimerBitmap().getHeight()/2),null);

                        if (cookerPosition == HOB_COOKER_POSITION_UP) {
                            canvas.drawBitmap(getPowerOffButtonBitmap(),getTempGearWithTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,15,null);

                        }else if (cookerPosition == HOB_COOKER_POSITION_DOWN) {
                            canvas.drawBitmap(getPowerOffButtonBitmap(),getTempGearWithTimerBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,getCookerViewHeight() - BOTTOM_COOKER_MARTO_BOTTOM,null);
                        }
                        mPaint.setTextAlign(Paint.Align.CENTER);
                        mPaint.setTypeface(tempGearTypeface);
                        mPaint.setTextSize(getTempGearWithTimerFontSize());

                        mViewRect.set(
                                30,
                                (getCookerViewHeight() / 4 ),
                                getPowerOnFireGearWithTimerBitmap().getWidth(),
                                (float) (getCookerViewHeight() * 0.30));

                        fontMetrics = mPaint.getFontMetrics();
                        top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                        bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                        baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                        canvas.drawText(getTempGearValue().length() <= 2?(" " + getTempGearValue()  + "º"): " " + getTempGearValue() + "º",mViewRect.centerX(),baseLineY, mPaint);//º

                        mPaint.setTextSize((float) (getTimerFontSize()));
                        mViewRect.set(
                                30,
                                (getCookerViewHeight() / 2 ),
                                getPowerOnFireGearWithTimerBitmap().getWidth(),
                                (float) (getCookerViewHeight() * 0.90 ));
                        fontMetrics = mPaint.getFontMetrics();
                        top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                        bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                        baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                        canvas.drawText(getTimerValue(),mViewRect.centerX(),baseLineY, mPaint);

                        handler.sendEmptyMessageDelayed(HANDLER_BLINK,10000);
                    }
                    blinkFlag = BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE;
                }
                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_SECOND:  // 602
                mPaint.setTextAlign(Paint.Align.CENTER);
                if (handler.hasMessages(HANDLER_FLASH_RECIPES)) {
                    handler.removeMessages(HANDLER_FLASH_RECIPES);
                }
                if (tempWorkRecipesProgress ==TEMP_WORK_RECIPES_PROGRESS_PICTURE) {

                    canvas.drawBitmap(getRecipesBgBitmap(),0,(getCookerViewHeight()/2 - getRecipesBgBitmap().getHeight()/2),null);
                    p = getPowerOffButtonLocation(getRecipesBgBitmap().getWidth());
                    canvas.drawBitmap(getPowerOffButtonBitmap(),p.x,p.y,null);

                    BitmapShader bitmapShader = new BitmapShader(getRecipesPicBitmap(), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

                    int size = Math.min(getRecipesBgBitmap().getWidth(), getRecipesBgBitmap().getHeight());
                    int mRadius = (int) (size * 0.45);

                    //float mScale = (mRadius * 2.0f) / Math.min(getRecipesPicBitmap().getHeight(), getRecipesPicBitmap().getWidth());

                    float mScale = 1.5f;

                    Matrix matrix = new Matrix();
                    matrix.setScale(mScale, mScale);
                    bitmapShader.setLocalMatrix(matrix);
                    picPaint.setShader(bitmapShader);
                    canvas.drawCircle(getRecipesBgBitmap().getWidth() / 2, getCookerViewHeight() / 2, mRadius, picPaint);
                    if (!handler.hasMessages(HANDLER_FLASH_RECIPES)) {
                        LogUtil.d("samhung-------1---------handler flash recipes------602---------");
                        handler.sendEmptyMessageDelayed(HANDLER_FLASH_RECIPES,TIME_SHOW_RECIPES_PICTURE);
                    }

                }else if (tempWorkRecipesProgress == TEMP_WORK_RECIPES_PROGRESS_TEMP_VALUE){

                    canvas.drawBitmap(getTempGearWithTimerBitmap(),0,(getCookerViewHeight()/2 - getTempGearWithTimerBitmap().getHeight()/2),null);
                    p = getPowerOffButtonLocation(getTempGearWithTimerBitmap().getWidth());
                    canvas.drawBitmap(getPowerOffButtonBitmap(),p.x,p.y,null);

                    mPaint.setTextAlign(Paint.Align.CENTER);
                    mPaint.setTypeface(fireGearTypeface);
                    mPaint.setTextSize(getTempGearWithTimerFontSize());
                    mViewRect.set(getTempGearWithTimerBitmap().getWidth() / 20, (float) (getCookerViewHeight() / 2 - getTempGearWithTimerBitmap().getHeight() * 0.4), getTempGearWithTimerBitmap().getWidth(), getCookerViewHeight() / 2);
                    fontMetrics = mPaint.getFontMetrics();
                    top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                    bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                    baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                    canvas.drawText(getTempGearValue(),mViewRect.centerX(),baseLineY, mPaint);

                    mPaint.setTextSize(getTimerFontSize());
                    mViewRect.set(getTempGearWithTimerBitmap().getWidth() / 7,  getCookerViewHeight() / 2, getTempGearWithTimerBitmap().getWidth(), (float) (getCookerViewHeight() / 2 + getTempGearWithTimerBitmap().getHeight() * 0.4));
                    fontMetrics = mPaint.getFontMetrics();
                    top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                    bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                    baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                    canvas.drawText(getTimerValue(),mViewRect.centerX(),baseLineY, mPaint);
                    if (!handler.hasMessages(HANDLER_FLASH_RECIPES)) {

                        handler.sendEmptyMessageDelayed(HANDLER_FLASH_RECIPES,TIME_SHOW_RECIPES_TEMP_VALUE);
                        LogUtil.d("samhung-------2---------handler flash recipes------602---------");
                    }

                }

                LogUtil.d("the change 602 blink~~~~");

                break;
        }
    }

    private void drawFireGearText(Canvas canvas,int fontSize,int width) {

    }


    private PointF getPowerOffButtonLocation(int width) {
        float d;
        PointF p = new PointF();
        d = width / 2 + getPowerOffButtonBitmap().getWidth() / 2 + 25;
        d = (float) (d / Math.sqrt(2));
        p.y = getCookerViewHeight() / 2 - d - getPowerOffButtonBitmap().getWidth() / 2;
        p.x = d + width / 2 - getPowerOffButtonBitmap().getHeight() / 2;
        return p;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDownX = event.getX();
                touchDownY = event.getY();
                //LogUtil.d("--------ACTION_DOWN----------------");
                return true;

            case MotionEvent.ACTION_MOVE:
                //LogUtil.d("--------ACTION_MOVE----------------");
                if (Math.abs(touchDownX - event.getX()) >= ViewConfiguration.get(getContext()).getScaledTouchSlop()||Math.abs(touchDownY - event.getY()) >= ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    return false;
                } else {
                    return true;
                }

            case MotionEvent.ACTION_UP:
                //LogUtil.d("--------ACTION_up----------------");
                if (ViewUtils.isShade(this)) {
                    return false;
                }
                if (Math.abs(event.getX() - touchDownX) > DisplayUtil.dp2px(getContext(),38) ||
                        Math.abs(event.getY() - touchDownY) > DisplayUtil.dp2px(getContext(),38)
                        ) {
                    return false;
                }else {
                    processClickEvent(event);
                    return true;
                }

        }
        return super.onTouchEvent(event);
    }

    protected void processClickEvent(MotionEvent event) {
        LogUtil.d("Enter:: processClickEvent");
        float touchUpX = event.getX();
        float touchUpY = event.getY();
        int bitmapWidth;
        float x1,y1;
        switch (workMode) {
            case HOB_VIEW_WORK_MODE_POWER_OFF:

                playSoundEffect(SoundEffectConstants.CLICK);
                mListener.onCookerPowerOn();

                break;
            case HOB_VIEW_WORK_MODE_ABNORMAL_NO_PAN:

                y1 = getCookerViewHeight() / 2;
                if (touchUpX > getPowerOnFireGearWithoutTimerBitmap().getWidth() * CLICK_WIDTH_RATIO && ((touchUpY < y1 * 1 && cookerPosition == HOB_COOKER_POSITION_UP) || (touchUpY > y1 * 1 && cookerPosition == HOB_COOKER_POSITION_DOWN))) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    recycle();
                    mListener.onCookerPowerOff();
                }else {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onSetGear();
                }

                break;
            case HOB_VIEW_WORK_MODE_FIRE_GEAR_WITHOUT_TIMER:
                y1 = getCookerViewHeight() / 2;
                if (touchUpX > getPowerOnFireGearWithoutTimerBitmap().getWidth() * CLICK_WIDTH_RATIO && ((touchUpY < y1 * 1 && cookerPosition == HOB_COOKER_POSITION_UP) || (touchUpY > y1 * 1 && cookerPosition == HOB_COOKER_POSITION_DOWN))) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    recycle();
                    mListener.onCookerPowerOff();
                }else if(touchUpX < getPowerOnFireGearWithoutTimerBitmap().getWidth() && touchUpY < getCookerViewHeight() * CLICK_HEIGHT_RATIO){
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onSetGear();
                }else if (touchUpX < getPowerOnFireGearWithoutTimerBitmap().getWidth() && touchUpY > getCookerViewHeight() * CLICK_HEIGHT_RATIO) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onSetTimer();
                }


                break;
                case HOB_VIEW_WORK_MODE_FIRE_GEAR_WITH_TIMER:
                    y1 = getCookerViewHeight() / 2;
                    if (touchUpX > getPowerOnFireGearWithoutTimerBitmap().getWidth() * CLICK_WIDTH_RATIO && ((touchUpY < y1 * 1 && cookerPosition == HOB_COOKER_POSITION_UP) || (touchUpY > y1 * 1 && cookerPosition == HOB_COOKER_POSITION_DOWN))) {
                        playSoundEffect(SoundEffectConstants.CLICK);
                        recycle();
                        mListener.onCookerPowerOff();
                    }else if(touchUpX < getPowerOnFireGearWithoutTimerBitmap().getWidth() && touchUpY < getCookerViewHeight() * 0.5){
                        playSoundEffect(SoundEffectConstants.CLICK);
                        mListener.onSetGear();
                    }else if (touchUpX < getPowerOnFireGearWithoutTimerBitmap().getWidth() && touchUpY > getCookerViewHeight() * 0.5) {
                        playSoundEffect(SoundEffectConstants.CLICK);
                        mListener.onSetTimer();
                    }

                    break;
            case HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP:
                playSoundEffect(SoundEffectConstants.CLICK);
                mListener.onCookerPowerOn();
                break;
            case HOB_VIEW_WORK_MODE_ABNORMAL_ERROR_OCURR:

                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITHOUT_TIMER:
                y1 = getCookerViewHeight() / 2;
                if (touchUpX > getPowerOnFireGearWithoutTimerBitmap().getWidth() * CLICK_WIDTH_RATIO && ((touchUpY < y1 * 1 && cookerPosition == HOB_COOKER_POSITION_UP) || (touchUpY > y1 * 1 && cookerPosition == HOB_COOKER_POSITION_DOWN))) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    recycle();
                    mListener.onCookerPowerOff();
                }else if(touchUpX < getPowerOnFireGearWithoutTimerBitmap().getWidth() && touchUpY < getCookerViewHeight() * CLICK_HEIGHT_RATIO){
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onSetGear();
                }else if (touchUpX < getPowerOnFireGearWithoutTimerBitmap().getWidth() && touchUpY > getCookerViewHeight() * CLICK_HEIGHT_RATIO) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onSetTimer();
                }
                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER:
                y1 = getCookerViewHeight() / 2;
                if (touchUpX > getPowerOnFireGearWithoutTimerBitmap().getWidth() * CLICK_WIDTH_RATIO && ((touchUpY < y1 * 1 && cookerPosition == HOB_COOKER_POSITION_UP) || (touchUpY > y1 * 1 && cookerPosition == HOB_COOKER_POSITION_DOWN))) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    recycle();
                    mListener.onCookerPowerOff();
                }else if(touchUpX < getPowerOnFireGearWithoutTimerBitmap().getWidth() && touchUpY < getCookerViewHeight() * 0.5){
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onSetGear();
                }else if (touchUpX < getPowerOnFireGearWithoutTimerBitmap().getWidth() && touchUpY > getCookerViewHeight() * 0.5) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onSetTimer();
                }
                break;
            case HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITHOUT_TIMER:
                y1 = getCookerViewHeight() / 2;
                if (touchUpX > getPowerOnFireGearWithoutTimerBitmap().getWidth() * CLICK_WIDTH_RATIO && ((touchUpY < y1 * 1 && cookerPosition == HOB_COOKER_POSITION_UP) || (touchUpY > y1 * 1 && cookerPosition == HOB_COOKER_POSITION_DOWN))) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    recycle();
                    mListener.onCookerPowerOff();
                }else if(touchUpX < getPowerOnFireGearWithoutTimerBitmap().getWidth() && touchUpY < getCookerViewHeight() * CLICK_HEIGHT_RATIO){
                    playSoundEffect(SoundEffectConstants.CLICK);
                    recycleIndicatorBitmap();
                    mListener.onSetGear();
                }else if (touchUpX < getPowerOnFireGearWithoutTimerBitmap().getWidth() && touchUpY > getCookerViewHeight() * CLICK_HEIGHT_RATIO) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onSetTimer();
                }


                break;
            case HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER:
                y1 = getCookerViewHeight() / 2;
                if (touchUpX > getPowerOnFireGearWithoutTimerBitmap().getWidth() * CLICK_WIDTH_RATIO && ((touchUpY < y1 * 1 && cookerPosition == HOB_COOKER_POSITION_UP) || (touchUpY > y1 * 1 && cookerPosition == HOB_COOKER_POSITION_DOWN))) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    recycle();
                    mListener.onCookerPowerOff();
                }else if(touchUpX < getPowerOnFireGearWithoutTimerBitmap().getWidth() && touchUpY < getCookerViewHeight() * 0.5){
                    playSoundEffect(SoundEffectConstants.CLICK);
                    recycleIndicatorBitmap();
                    mListener.onSetGear();
                }else if (touchUpX < getPowerOnFireGearWithoutTimerBitmap().getWidth() && touchUpY > getCookerViewHeight() * 0.5) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onSetTimer();
                }
                break;
            case HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR:

                y1 = getCookerViewHeight() / 2;
                if (touchUpX > getPowerOnFireGearWithoutTimerBitmap().getWidth() * CLICK_WIDTH_RATIO && ((touchUpY < y1 * 1 && cookerPosition == HOB_COOKER_POSITION_UP) || (touchUpY > y1 * 1 && cookerPosition == HOB_COOKER_POSITION_DOWN))) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    recycle();
                    mListener.onCookerPowerOff();
                }else {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onSetGear();
                }

                break;
            case HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR_WITH_INDICATOR:
                bitmapWidth = getTempIndicatorPrepareSensorBitmap().getWidth();
                x1 = bitmapWidth / 2.0f;
                y1 = getCookerViewHeight() / 2.0f;
                if (isOutOfCircle(getPowerOffBgBitmap().getWidth() / 2, x1,y1,touchUpX,touchUpY)) {//power off

                    if (mayClickPowerOffButton(touchUpX, touchUpY, bitmapWidth)) {
                        LogUtil.d("------------power off------------");
                        playSoundEffect(SoundEffectConstants.CLICK);
                        doPowerOff();
                    }

                }else {
                    y1 = y1;
                    if (touchUpY < y1) {
                        LogUtil.d("---------touch up----------");
                        //mListener.onSetGear();
                    }else {
                        LogUtil.d("---------touch down----------");
                        //mListener.onSetTimer();
                    }
                }
                break;
            case HOB_VIEW_WORK_MODE_TEMP_SENSOR_READY:


                y1 = getCookerViewHeight() / 2;
                if (touchUpX > getPowerOnFireGearWithoutTimerBitmap().getWidth() * CLICK_WIDTH_RATIO && ((touchUpY < y1 * 1 && cookerPosition == HOB_COOKER_POSITION_UP) || (touchUpY > y1 * 1 && cookerPosition == HOB_COOKER_POSITION_DOWN))) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    recycle();
                    mListener.onCookerPowerOff();
                }else if(touchUpX < getPowerOnFireGearWithoutTimerBitmap().getWidth() && touchUpY < getCookerViewHeight() * 0.5){

                }else if (touchUpX < getPowerOnFireGearWithoutTimerBitmap().getWidth() && touchUpY > getCookerViewHeight() * 0.5) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onReadyToCook();
                }

                break;
            case HOB_VIEW_WORK_MODE_TEMP_WORK_FINISH_WAIT_USER_CONFIRM:

                y1 = getCookerViewHeight() / 2;
                if (touchUpX > getPowerOnFireGearWithoutTimerBitmap().getWidth() * CLICK_WIDTH_RATIO && ((touchUpY < y1 * 1 && cookerPosition == HOB_COOKER_POSITION_UP) || (touchUpY > y1 * 1 && cookerPosition == HOB_COOKER_POSITION_DOWN))) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    recycle();
                    mListener.onCookerPowerOff();
                }else {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onRequestAddTenMinute();
                }

                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_FIRST: // 温度+定时　　＋　图片  601
                bitmapWidth = getTempSensorReadyBitmap().getWidth();
                x1 = bitmapWidth / 2.0f;
                y1 = getCookerViewHeight() / 2.0f;
                if (touchUpX > getPowerOnFireGearWithoutTimerBitmap().getWidth() * CLICK_WIDTH_RATIO && ((touchUpY < y1 * 1 && cookerPosition == HOB_COOKER_POSITION_UP) || (touchUpY > y1 * 1 && cookerPosition == HOB_COOKER_POSITION_DOWN))) {

                   /* if (mayClickPowerOffButton(touchUpX, touchUpY, bitmapWidth)) {//
                        LogUtil.d("------------power off------------");
                        playSoundEffect(SoundEffectConstants.CLICK);
                        doPowerOff();
                        recycleRecipesBitmap();
                        tempWorkRecipesProgress = TEMP_WORK_RECIPES_PROGRESS_PICTURE;
                    }*/

                    //只做粗略判断，后续优化
                    playSoundEffect(SoundEffectConstants.CLICK);
                    doPowerOff();
                    recycleRecipesBitmap();
                    tempWorkRecipesProgress = TEMP_WORK_RECIPES_PROGRESS_PICTURE;

                }else {
                    LogUtil.d("Enter:: recipes mode");
                    if (tempWorkRecipesProgress == TEMP_WORK_RECIPES_PROGRESS_PICTURE) {//正在显示图片时，点击图片，显示温度时间
                        if (handler.hasMessages(HANDLER_FLASH_RECIPES)) {
                            handler.removeMessages(HANDLER_FLASH_RECIPES);
                        }
                        playSoundEffect(SoundEffectConstants.CLICK);
                        tempWorkRecipesProgress = TEMP_WORK_RECIPES_PROGRESS_TEMP_VALUE;
                        invalidate();
                    }


                    if (currentBlinkOptions == BLINK_OPTIONS_ONE) {//recipes

                    }else {//value
                        playSoundEffect(SoundEffectConstants.CLICK);
                        if (touchUpY < y1) {
                            LogUtil.d("---------touch up----------");
                            mListener.onSetGear();
                        }else {
                            LogUtil.d("---------touch down----------");
                            mListener.onSetTimer();
                        }

                    }

                }
                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_SECOND:   // 602
                bitmapWidth = getTempSensorReadyBitmap().getWidth();
                x1 = bitmapWidth / 2.0f;
                y1 = getCookerViewHeight() / 2.0f;
                if (isOutOfCircle(getPowerOffBgBitmap().getWidth() / 2, x1,y1,touchUpX,touchUpY)) {//power off

                    if (mayClickPowerOffButton(touchUpX, touchUpY, bitmapWidth)) {
                        LogUtil.d("------------power off------------");
                        playSoundEffect(SoundEffectConstants.CLICK);
                        doPowerOff();
                        recycleRecipesBitmap();
                        tempWorkRecipesProgress = TEMP_WORK_RECIPES_PROGRESS_TEMP_VALUE;
                    }

                }else {
                    if (tempWorkRecipesProgress == TEMP_WORK_RECIPES_PROGRESS_PICTURE) {//正在显示图片时，点击图片，显示温度时间
                        if (handler.hasMessages(HANDLER_FLASH_RECIPES)) {
                            handler.removeMessages(HANDLER_FLASH_RECIPES);
                        }
                        playSoundEffect(SoundEffectConstants.CLICK);
                        tempWorkRecipesProgress = TEMP_WORK_RECIPES_PROGRESS_TEMP_VALUE;
                        invalidate();
                    }

                }
                break;
        }



    }

    protected String getWorkModeString(int value) {
        switch (value) {
            case HOB_VIEW_WORK_MODE_POWER_OFF:
                return "HOB_VIEW_WORK_MODE_POWER_OFF";
            case HOB_VIEW_WORK_MODE_FIRE_GEAR_WITHOUT_TIMER:
                return "HOB_VIEW_WORK_MODE_FIRE_GEAR_WITHOUT_TIMER";
            case HOB_VIEW_WORK_MODE_FIRE_GEAR_WITH_TIMER:
                return "HOB_VIEW_WORK_MODE_FIRE_GEAR_WITH_TIMER";
            case HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITHOUT_TIMER:
                return "HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITHOUT_TIMER";
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITHOUT_TIMER:
                return "HOB_VIEW_WORK_MODE_TEMP_GEAR_WITHOUT_TIMER";
            case HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER:
                return "HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER";
            case HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER_AND_RECIPES_FIRST:
                return "HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER_AND_RECIPES_FIRST";
            case HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER_AND_RECIPES_SECOND:
                return "HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER_AND_RECIPES_SECOND";
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER:
                return "HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER";
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_FIRST:
                return "HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_FIRST";
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_SECOND:
                return "HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_SECOND";
            case HOB_VIEW_WORK_MODE_ABNORMAL_NO_PAN:
                return "HOB_VIEW_WORK_MODE_ABNORMAL_NO_PAN";
            case HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP:
                return "HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP";
            case HOB_VIEW_WORK_MODE_ABNORMAL_ERROR_OCURR:
                return "HOB_VIEW_WORK_MODE_ABNORMAL_ERROR_OCURR";
            case HOB_VIEW_WORK_MODE_UPATE_TIMER:
                return "HOB_VIEW_WORK_MODE_UPATE_TIMER";
            case HOB_VIEW_WORK_MODE_PAUSE:
                return "HOB_VIEW_WORK_MODE_PAUSE";
            case HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR:
                return "HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR";
            case HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR_WITH_INDICATOR:
                return "HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR_WITH_INDICATOR";
            case HOB_VIEW_WORK_MODE_TEMP_SENSOR_READY:
                return "HOB_VIEW_WORK_MODE_TEMP_SENSOR_READY";
            case HOB_VIEW_WORK_MODE_TEMP_WORK_FINISH_WAIT_USER_CONFIRM:
                return "HOB_VIEW_WORK_MODE_TEMP_WORK_FINISH_WAIT_USER_CONFIRM";
            default:
                return "UNKNOWN " + value;
        }
    }

    private void doPowerOff() {
        recycle();
        mListener.onCookerPowerOff();
    }

    private double calDistance(float x1 ,float y1 , float x2 , float y2) {
        double x = Math.abs(x1 - x2);
        double y = Math.abs(y1 - y2);
        return Math.sqrt(x * x + y * y);
    }

    private boolean isOutOfCircle(int radius , float x1 ,float y1 , float x2 , float y2) {
        return calDistance(x1,y1,x2,y2) > radius;
    }

    private Rect getStringRect(String strText) {
        Rect rect = new Rect();
        mPaint.getTextBounds(strText, 0, strText.length(), rect);
        return rect;
    }

    private int getReadyToCookY() {
        mViewRect.set(
                0,
                (float) (getCookerViewHeight() / 2 - getTempSensorReadyBitmap().getHeight() * 0.4),
                getTempSensorReadyBitmap().getWidth(),
                getCookerViewHeight()/ 2);
        fontMetrics = mPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        int baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
        return baseLineY;
    }

    private void drawTempIndicator(Canvas canvas, Bitmap bitmap, Bitmap background, float ratio) {
        float bmTop = (float) (getCookerViewHeight() / 2 - background.getHeight() * ratio);
        canvas.drawBitmap(
                bitmap,
                background.getWidth() / 2 - bitmap.getWidth() / 2,
                bmTop,
                null);

        String newText = getTempIndicatorString();
        if (!newText.equals(tempIndicatorText)) {
            tempIndicatorText = newText;
            tempIndicatorTextChanged = true;
        }

        mPaint.setTypeface(tfHelvetica57Condensed);
        mPaint.setTextSize(getTempIndicatorStringFontSize());
        mViewRect.set(
                0,
                bmTop + bitmap.getHeight(),
                getTempGearWithoutTimerBitmap().getWidth(),
                bmTop + bitmap.getHeight() + getStringRect(tempIndicatorText).height() + 2 * getTempIndicatorStringPadding());
        fontMetrics = mPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        int baseLineY = (int) (mViewRect.centerY() - top / 2 - bottom / 2);//基线中间点的y轴计算公式

        if (tempIndicatorTextChanged) {
            Rect rect = getStringRect(tempIndicatorText);
            if (rect.width() > getInnerTextMaxWidth()) {
                tempIndicatorTextMarqueeNeeded = true;
                tempIndicatorMarquee = new Marquee(
                        tempIndicatorText,
                        new RectF(
                                mViewRect.centerX() - getInnerTextMaxWidth() / 2.0f,
                                mViewRect.top,
                                mViewRect.centerX() + getInnerTextMaxWidth() / 2.0f,
                                mViewRect.bottom
                        ),
                        new PointF(
                                mViewRect.centerX() - getInnerTextMaxWidth() / 2.0f,
                                baseLineY
                        ),
                        1);
            } else {
                tempIndicatorTextMarqueeNeeded = false;
            }
            tempIndicatorTextChanged = false;
        }

        if (!tempIndicatorTextMarqueeNeeded) {
            mPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(tempIndicatorText,mViewRect.centerX(),baseLineY, mPaint);
        } else {
            mPaint.setTextAlign(Paint.Align.LEFT);
            tempIndicatorMarquee.marquee(canvas, mPaint);
        }
    }

    private boolean mayClickPowerOffButton(
            float touchX,
            float touchY,
            int width) {
        int h = getPowerOffButtonBitmap().getHeight();
        PointF pointF = getPowerOffButtonLocation(width);
        return touchX > pointF.x - POWER_OFF_REGION_OFFSET && touchY < pointF.y + h + POWER_OFF_REGION_OFFSET;
    }


    /****************************************Interface*************************************************/

    /******************************get display bitmap***********************************************/
    protected abstract Bitmap getPowerOffBgBitmap();//get power off background bitmap
    protected abstract Bitmap getPowerOnWithoutPanBitmap();//get power off without pan bitmap
    protected abstract Bitmap getPowerOffButtonBitmap();//get power off button bitmap
    protected abstract Bitmap getPowerOnFireGearWithoutTimerBitmap();//get power on fire gear without timer bitmap
    protected abstract Bitmap getPowerOnFireGearWithTimerBitmap();//get power on fire gear with timer bitmap
    protected abstract Bitmap getHighTempBitmap();//get high temp bitmap
    protected abstract Bitmap getErrorBitmap();//get error bitmap
    protected abstract Bitmap getTempGearWithoutTimerBitmap();//get error bitmap
    protected abstract Bitmap getTempGearWithTimerBitmap();//get temp gear with timer bitmap
    protected abstract Bitmap getTempGearIndicatorWithoutTimerBitmap();//get temp gear indicator without timer bitmap
    protected abstract Bitmap getTempGearIndicatorWithTimerBitmap();//get temp gear indicator without timer bitmap
    protected abstract Bitmap getTempIndicatorBitmap();//get temp indicator bitmap
    protected abstract Bitmap getTempIndicatorBitmapWithTimer();//get temp indicator with timer bitmap
    protected abstract Bitmap getTempPrepareSensorBitmap();//get temp prepare temp bitmap
    protected abstract Bitmap getTempIndicatorPrepareSensorBitmap();//get temp indicator prepare temp bitmap
    protected abstract Bitmap getTempSensorReadyBitmap();//get temp sensor ready bitmap
    protected abstract Bitmap getTempSensorReadyValueBitmap();//get temp sensor ready value bitmap
    protected abstract Bitmap getTempWorkFinishLightBitmap();//get temp work finish and wait user confirm bitmap
    protected abstract Bitmap getTempWorkFinishDarkBitmap();//get temp work finish and wait user confirm bitmap
    protected abstract Bitmap getRecipesPicBitmap();//get recipes picture bitmap
    protected abstract Bitmap getRecipesBgBitmap();//get recipes picture bitmap





    /*******************************get display font size****************************************************************/
    protected abstract int getFireGearWithoutTimerFontSize();
    protected abstract int getFireGearWithTimerFontSize();
    protected abstract int getTimerFontSize();
    protected abstract int getTimerWithIndicatorTimerFontSize();
    protected abstract int getErrorFontSize();
    protected abstract int getTempGearWithoutTimerFontSize();
    protected abstract int getTempGearWithTimerFontSize();
    protected abstract int getKeepWarmAndAddTenMinuteFontSize();
    protected abstract int getReadyToCookFontSize();
    protected abstract int getTempPreheatSettingValueFontSize();//温控模式，预热状态的设置温度值
    protected abstract int getTempPreheatRealValueFontSize();//温控模式，预热状态的真实温度值
    protected abstract int getTempIndicatorStringFontSize();
    protected abstract int getTempIndicatorStringPadding();



    /***********************get display value****************************************/
    protected abstract String getFireGearValue();
    protected abstract String getTempGearValue();
    protected abstract String getTimerValue();
    protected abstract String getTempIndicatorString();
    protected abstract String getErrorMessage();
    protected abstract String getRealTempValue();//真实温度值
    protected abstract int getSweepAngle();//真实温度圆弧度数
    protected abstract float getRealTempArcGap();
    protected abstract float getRealTempTextGap();



    /*******************************get display size****************************************************/
    protected abstract int getCookerViewWidth();
    protected abstract int getCookerViewHeight();
    protected abstract int getRealTempProgressArcWidth();
    protected abstract int getRealTempProgressRadius();
    protected abstract int getInnerTextMaxWidth();
    protected abstract int getInnerTextMaxWidthBig();



    /*************************************recycle*************************************************************/
    protected abstract void recycle();
    protected abstract void recycleIndicatorBitmap();
    protected abstract void recycleRecipesBitmap();


    /****************************************user interface**********************************************************/
    protected abstract void upateUI(int workMode,int fireValue,int tempValue,int realTempValue,int hourValue,int minuteValue,int tempIndicatorID,int recipesID,int recipesShowOrder,String errorMessage);
}
