package com.ekek.viewmodule.hob;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
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
import com.ekek.viewmodule.contract.HobRectangleCookerContract;
import com.ekek.viewmodule.utils.ViewUtils;

public abstract class BaseRectangleCookerView extends View {

    protected static final int HOB_RETANGLE_WORK_MODE_WORK_SEPARATE = 0;
    protected static final int HOB_RETANGLE_WORK_MODE_WORK_UNITE = 1;

    protected static final int HOB_VIEW_WORK_MODE_POWER_OFF = 0;
    protected static final int HOB_VIEW_WORK_MODE_FIRE_GEAR_WITHOUT_TIMER = 100;
    protected static final int HOB_VIEW_WORK_MODE_FIRE_GEAR_WITH_TIMER = 200;
    protected static final int HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITHOUT_TIMER = 300;
    protected static final int HOB_VIEW_WORK_MODE_TEMP_GEAR_WITHOUT_TIMER = 400;
    protected static final int HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER = 500;
    protected static final int HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER_AND_RECIPES_FIRST = 501;//先显示菜谱图片，再显示温度定时
    protected static final int HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER_AND_RECIPES_SECOND = 502;//先显示温度定时，再显示菜谱图片
    protected static final int HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER = 600;
    protected static final int HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_FIRST = 601;//先显示菜谱图片，再显示温度定时
    protected static final int HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_SECOND = 602;//先显示温度定时，再显示菜谱图片
    protected static final int HOB_VIEW_WORK_MODE_ABNORMAL_NO_PAN = 700;
    protected static final int HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP = 701;
    protected static final int HOB_VIEW_WORK_MODE_ABNORMAL_ERROR_OCURR = 702;
    protected static final int HOB_VIEW_WORK_MODE_UPATE_TIMER = 800;
    protected static final int HOB_VIEW_WORK_MODE_PAUSE = 900;
    protected static final int HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR = 1000;
    protected static final int HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR_WITH_INDICATOR = 1001;
    protected static final int HOB_VIEW_WORK_MODE_TEMP_SENSOR_READY = 1002;
    protected static final int HOB_VIEW_WORK_MODE_TEMP_WORK_FINISH_WAIT_USER_CONFIRM= 1003;

    private static final int HOB_RETANGLE_VIEW_GAP = 5;//30
    private static final int HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON = 10;//30

    private static final int TEMP_WORK_FINISH_PROGRESS_LIGHT = 0;
    private static final int TEMP_WORK_FINISH_PROGRESS_DARK = 1;
    private static final int TEMP_WORK_RECIPES_PROGRESS_PICTURE = 1;
    private static final int TEMP_WORK_RECIPES_PROGRESS_TEMP_VALUE = 0;

    private static final long TIME_FLASH_KEEP_WARM = 1000 * 1;//10s
    private static final int HANDLER_FLASH_KEEP_WARM_TIPS = 1;//
    private static final int HANDLER_FLASH_KEEP_WARM_TIPS_UP_COOKER = 2;//
    private static final int HANDLER_FLASH_KEEP_WARM_TIPS_DOWN_COOKER = 3;//
    private static final int HANDLER_FLASH_RECIPES_FOR_UNION = 4;
    private static final int HANDLER_FLASH_RECIPES_FOR_SEPERATE_UP = 5;
    private static final int HANDLER_FLASH_RECIPES_FOR_SEPERATE_DOWN = 6;
    private static final int HANDLER_UNION_BLINK = 7;
    private static final int HANDLER_SEPERATE_UP_BLINK = 8;
    private static final int HANDLER_SEPERATE_DOWN_BLINK = 9;

    private static final long TIME_SHOW_RECIPES_PICTURE = 1000 * 10;
    private static final long TIME_SHOW_RECIPES_TEMP_VALUE = 1000 * 30;

    private static final int BLINK_OPTIONS_ONE = 0;
    private static final int BLINK_OPTIONS_TWO = 1;
    private static final long BLINK_TIME_FOR_READY_TO_COOK = 2000;
    private static final long BLINK_TIME_FOR_RECIPE_IMAGE = 5000;
    private static final long BLINK_TIME_FOR_RECIPE_VALUE = 10000;
    private static final int BLINK_FLAG_HAVE_NOT_BLINK = 0;
    private static final int BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE = 1;
    private static final int BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_TWO = 2;
    private int unionBlinkFlag = BLINK_FLAG_HAVE_NOT_BLINK;//false
    private int upBlinkFlag = BLINK_FLAG_HAVE_NOT_BLINK;//false
    private int downBlinkFlag = BLINK_FLAG_HAVE_NOT_BLINK;//false
    private int unionCurrentBlinkOptions = BLINK_OPTIONS_ONE;
    private int upCurrentBlinkOptions = BLINK_OPTIONS_ONE;
    private int downCurrentBlinkOptions = BLINK_OPTIONS_ONE;

    private int arrow = 0;

    protected int retangleHobWorkMode = HOB_RETANGLE_WORK_MODE_WORK_SEPARATE;
    protected int upCookerID;
    protected int downCookerID;
    protected int unionCookerID;
    protected int currentUpdateCookerID = unionCookerID;
    protected int upWorkMode = HOB_VIEW_WORK_MODE_POWER_OFF;
    protected int downWorkMode = HOB_VIEW_WORK_MODE_POWER_OFF;
    protected int unionWorkMode = HOB_VIEW_WORK_MODE_POWER_OFF;
    protected int lastUnionWorkMode = unionWorkMode;
    protected int lastUpWorkMode = upWorkMode;
    protected int lastDownWorkMode = downWorkMode;
    private RectF mViewRect;
    private float touchDownX;
    private float touchDownY;
    private boolean click2Detected;
    private int firstFingerTouch;
    private Typeface fireGearTypeface,tempGearTypeface;
    private Typeface tfHelvetica57Condensed;
    private Paint mPaint;
    private Paint picPaint;
    private Paint mAlphaPaint;
    private RectF mRectF;
    //圆心坐标，半径
    private Point mCenterPoint;
    private Paint.FontMetrics fontMetrics;
    private Paint mArcPaint;//温度圆弧
    private int alpha = 100;
    float light = 1.0f;
    private int upTempWorkFinishProgress = TEMP_WORK_FINISH_PROGRESS_LIGHT;
    private int downTempWorkFinishProgress = TEMP_WORK_FINISH_PROGRESS_LIGHT;
    protected int upTempWorkRecipesProgress = TEMP_WORK_RECIPES_PROGRESS_PICTURE;
    protected int downTempWorkRecipesProgress = TEMP_WORK_RECIPES_PROGRESS_PICTURE;
    private int tempWorkFinishProgress = TEMP_WORK_FINISH_PROGRESS_LIGHT;
    protected int tempWorkRecipesProgressForUnion = TEMP_WORK_RECIPES_PROGRESS_PICTURE;
    protected int tempWorkRecipesProgressForSeperateUp = TEMP_WORK_RECIPES_PROGRESS_PICTURE;
    protected int tempWorkRecipesProgressForSeperateDown = TEMP_WORK_RECIPES_PROGRESS_PICTURE;
    protected HobRectangleCookerContract.OnHobRectangleCookerListener mListener;
    private int[] mGradientColors = {getResources().getColor(R.color.viewmodule_colorCircleProgress), getResources().getColor(R.color.viewmodule_colorCircleProgress), getResources().getColor(R.color.viewmodule_colorCircleProgress)};

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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_FLASH_KEEP_WARM_TIPS:
                    invalidate();
                    //handler.sendEmptyMessageDelayed(HANDLER_FLASH_KEEP_WARM_TIPS,TIME_FLASH_KEEP_WARM);
                    break;
                case HANDLER_FLASH_KEEP_WARM_TIPS_UP_COOKER:
                    invalidate();
                    break;
                case HANDLER_FLASH_KEEP_WARM_TIPS_DOWN_COOKER:
                    invalidate();
                    break;
                case HANDLER_FLASH_RECIPES_FOR_UNION:
                    if (tempWorkRecipesProgressForUnion ==TEMP_WORK_RECIPES_PROGRESS_PICTURE) {
                        tempWorkRecipesProgressForUnion = TEMP_WORK_RECIPES_PROGRESS_TEMP_VALUE;

                    }else if(tempWorkRecipesProgressForUnion ==TEMP_WORK_RECIPES_PROGRESS_TEMP_VALUE){
                        tempWorkRecipesProgressForUnion = TEMP_WORK_RECIPES_PROGRESS_PICTURE;
                    }
                    invalidate();
                    break;
                case HANDLER_FLASH_RECIPES_FOR_SEPERATE_UP:
                    if (tempWorkRecipesProgressForSeperateUp ==TEMP_WORK_RECIPES_PROGRESS_PICTURE) {
                        tempWorkRecipesProgressForSeperateUp = TEMP_WORK_RECIPES_PROGRESS_TEMP_VALUE;

                    }else if(tempWorkRecipesProgressForSeperateUp ==TEMP_WORK_RECIPES_PROGRESS_TEMP_VALUE){
                        tempWorkRecipesProgressForSeperateUp = TEMP_WORK_RECIPES_PROGRESS_PICTURE;
                    }
                    invalidate();
                    break;
                case HANDLER_FLASH_RECIPES_FOR_SEPERATE_DOWN:
                    if (tempWorkRecipesProgressForSeperateDown ==TEMP_WORK_RECIPES_PROGRESS_PICTURE) {
                        tempWorkRecipesProgressForSeperateDown = TEMP_WORK_RECIPES_PROGRESS_TEMP_VALUE;

                    }else if(tempWorkRecipesProgressForSeperateDown ==TEMP_WORK_RECIPES_PROGRESS_TEMP_VALUE){
                        tempWorkRecipesProgressForSeperateDown = TEMP_WORK_RECIPES_PROGRESS_PICTURE;
                    }
                    invalidate();
                    break;
                case HANDLER_UNION_BLINK:
                    if (unionCurrentBlinkOptions == BLINK_OPTIONS_ONE) {
                        unionCurrentBlinkOptions = BLINK_OPTIONS_TWO;
                    }else if (unionCurrentBlinkOptions == BLINK_OPTIONS_TWO) {
                        unionCurrentBlinkOptions = BLINK_OPTIONS_ONE;
                    }
                    unionBlinkFlag = BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_TWO;
                    invalidate();
                    break;
                case HANDLER_SEPERATE_UP_BLINK:
                    if (upCurrentBlinkOptions == BLINK_OPTIONS_ONE) {
                        upCurrentBlinkOptions = BLINK_OPTIONS_TWO;
                    }else if (upCurrentBlinkOptions == BLINK_OPTIONS_TWO) {
                        upCurrentBlinkOptions = BLINK_OPTIONS_ONE;
                    }
                    upBlinkFlag = BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_TWO;
                    invalidate();
                    break;
                case HANDLER_SEPERATE_DOWN_BLINK:
                    if (downCurrentBlinkOptions == BLINK_OPTIONS_ONE) {
                        downCurrentBlinkOptions = BLINK_OPTIONS_TWO;
                    }else if (downCurrentBlinkOptions == BLINK_OPTIONS_TWO) {
                        downCurrentBlinkOptions = BLINK_OPTIONS_ONE;
                    }
                    downBlinkFlag = BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_TWO;
                    invalidate();
                    break;

            }
        }
    };

    public BaseRectangleCookerView(Context context) {
        this(context,null);
    }

    public BaseRectangleCookerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.viewmodule_HobRetangleCookerView);
        String idStr = a.getString(R.styleable.viewmodule_HobRetangleCookerView_viewmodule_retangle_cooker_id);
        a.recycle();
        if (idStr != null) {
            String[] idStrs = idStr.split("\\|");
            if (idStrs.length == 2) {
                downCookerID = Integer.valueOf(idStrs[0]);
                upCookerID = Integer.valueOf(idStrs[1]);
                unionCookerID = downCookerID * 10 + upCookerID;//如1|2，则unionCookerID为12
            }

        }
        init();
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

        mRectF = new RectF();
        mCenterPoint = new Point();

        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setColor(mGradientColors[0]);
        // 设置画笔的样式，为FILL，FILL_OR_STROKE，或STROKE
        mArcPaint.setStyle(Paint.Style.STROKE);
        // 设置画笔粗细
        mArcPaint.setStrokeWidth(getSeperateRealTempProgressArcWidth());
        // 当画笔样式为STROKE或FILL_OR_STROKE时，设置笔刷的图形样式，如圆形样式
        // Cap.ROUND,或方形样式 Cap.SQUARE
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);

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
      /*  mViewRect.set(0, 0, getWidth(), getHeight());
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawRect(mViewRect,paint);*/

    /*    if (retangleHobWorkMode == HOB_RETANGLE_WORK_MODE_WORK_UNITE) {
            processUnionEvent(canvas);


        }else if (retangleHobWorkMode == HOB_RETANGLE_WORK_MODE_WORK_SEPARATE) {
            if (upWorkMode == HOB_VIEW_WORK_MODE_POWER_OFF && downWorkMode == HOB_VIEW_WORK_MODE_POWER_OFF) {
                drawUpDownPowerOffView(canvas);
            }else {
                drawUpCookerView(canvas);
                drawDownCookerView(canvas);
            }

        }*/


    /*    if (currentUpdateCookerID == unionCookerID) {
        //if (unionCookerID == 56) {
            //processUnionEvent(canvas);
            drawUnionCookerView(canvas);

        }else {
            if (upWorkMode == HOB_VIEW_WORK_MODE_POWER_OFF && downWorkMode == HOB_VIEW_WORK_MODE_POWER_OFF) {
                drawUpDownPowerOffView(canvas);
            }else {
                drawUpCookerView(canvas);
                drawDownCookerView(canvas);
            }

        }*/

        //for test
     /*   retangleHobWorkMode = HOB_RETANGLE_WORK_MODE_WORK_SEPARATE;
        upWorkMode = HOB_VIEW_WORK_MODE_TEMP_WORK_FINISH_WAIT_USER_CONFIRM;
        downWorkMode = HOB_VIEW_WORK_MODE_TEMP_WORK_FINISH_WAIT_USER_CONFIRM;
        unionWorkMode = HOB_VIEW_WORK_MODE_TEMP_WORK_FINISH_WAIT_USER_CONFIRM;*/

        if (lastUpWorkMode != upWorkMode) {
            lastUpWorkMode = upWorkMode;
            if (handler.hasMessages(HANDLER_SEPERATE_UP_BLINK)) handler.removeMessages(HANDLER_SEPERATE_UP_BLINK);
            upBlinkFlag = BLINK_FLAG_HAVE_NOT_BLINK;
            upCurrentBlinkOptions = BLINK_OPTIONS_ONE;
            mPaint.setAlpha(255);
        }

        if (lastDownWorkMode != downWorkMode) {
            lastDownWorkMode = downWorkMode;
            if (handler.hasMessages(HANDLER_SEPERATE_DOWN_BLINK)) handler.removeMessages(HANDLER_SEPERATE_DOWN_BLINK);
            downBlinkFlag = BLINK_FLAG_HAVE_NOT_BLINK;
            downCurrentBlinkOptions = BLINK_OPTIONS_ONE;
            mPaint.setAlpha(255);
        }

        if (lastUnionWorkMode != unionWorkMode) {
            lastUnionWorkMode = unionWorkMode;
            if (handler.hasMessages(HANDLER_UNION_BLINK)) handler.removeMessages(HANDLER_UNION_BLINK);
            unionBlinkFlag = BLINK_FLAG_HAVE_NOT_BLINK;
            unionCurrentBlinkOptions = BLINK_OPTIONS_ONE;
            mPaint.setAlpha(255);
        }

        if (retangleHobWorkMode == HOB_RETANGLE_WORK_MODE_WORK_UNITE) {
            if (upWorkMode == downWorkMode) {

                drawUnionCookerView(canvas);
            }else {
               // LogUtil.d("upworkMode----->" + upWorkMode);
                LogUtil.d("downworkMode----->" + downWorkMode);

                drawUpCookerView(canvas);
                drawDownCookerView(canvas);
            }


        }else if (retangleHobWorkMode == HOB_RETANGLE_WORK_MODE_WORK_SEPARATE) {
            if (upWorkMode == HOB_VIEW_WORK_MODE_POWER_OFF && downWorkMode == HOB_VIEW_WORK_MODE_POWER_OFF) {
                drawUpDownPowerOffView(canvas);
                LogUtil.d("get draw rectangle cooker  1");
            }else if (upWorkMode == HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP && downWorkMode == HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP) {

                unionWorkMode = HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP;
                drawUnionCookerView(canvas);
                LogUtil.d("get draw rectangle cooker  2");

            }else {

                drawUpCookerView(canvas);
                drawDownCookerView(canvas);
                LogUtil.d("get draw rectangle cooker  3");
            }

        }

        LogUtil.d("get draw rectangle cooker  4");


    }

    private void processUnionEvent(Canvas canvas) {
        if (upWorkMode == downWorkMode) {
            drawUnionCookerView(canvas);
        }else {
            drawUpCookerView(canvas);
            drawDownCookerView(canvas);
        }

    }

    private void drawUpDownPowerOffView(Canvas canvas) {
        retangleHobWorkMode = HOB_RETANGLE_WORK_MODE_WORK_SEPARATE;
        canvas.drawBitmap(getUnionPowerOffBgBitmap(),0,(getCookerViewHeight()/2 - getUnionPowerOffBgBitmap().getHeight()/2),null);




    }

    private void drawDownCookerView(Canvas canvas) {
        //if (retangleHobWorkMode == HOB_RETANGLE_WORK_MODE_WORK_UNITE) return;
        PointF p;
        float top,bottom;
        int baseLineY;
        switch (downWorkMode) {
            case HOB_VIEW_WORK_MODE_POWER_OFF:
                //canvas.drawBitmap(getSeperatePowerOffBgBitmap(),(getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth() - getSeperatePowerOffBgBitmap().getWidth()) / 2,(getCookerViewHeight()/2 + getUnionPowerOffBgBitmap().getHeight()/2 - getSeperatePowerOffBgBitmap().getHeight() ),null);
                canvas.drawBitmap(getSeperatePowerOffBgBitmap(),0,(getCookerViewHeight()/2 + getUnionPowerOffBgBitmap().getHeight()/2 - getSeperatePowerOffBgBitmap().getHeight() ),null);

                break;
            case HOB_VIEW_WORK_MODE_FIRE_GEAR_WITHOUT_TIMER:
                canvas.drawBitmap(getSeperatePowerOnFireWithoutTimerBgBitmap(),0,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),null);
                canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP / 2 + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperatePowerOnFireWithoutTimerBgBitmap().getHeight() - getPowerOffButtonBitmap().getHeight())+9,null);

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTextSize(getSeperateFireGearWithoutTimerFontSize());
                mPaint.setTypeface(fireGearTypeface);
                mViewRect.set(
                        30,
                        (getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),
                        getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth(),
                        (float) (getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperatePowerOnFireWithoutTimerBgBitmap().getHeight() * 0.63));//0.7
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getDownFireGearValue(),mViewRect.centerX(),baseLineY, mPaint);

                break;
            case HOB_VIEW_WORK_MODE_FIRE_GEAR_WITH_TIMER:
                canvas.drawBitmap(getSeperatePowerOnFireWithTimerBgBitmap(),0,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),null);
                canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperatePowerOnFireWithTimerBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP / 2 + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperatePowerOnFireWithTimerBgBitmap().getHeight() - getPowerOffButtonBitmap().getHeight()),null);

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTextSize(getSeperateFireGearWithTimerGearFontSize());
                mPaint.setTypeface(fireGearTypeface);
                mViewRect.set(
                        30,
                        (getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),
                        getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth(),
                        (float) (getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperatePowerOnFireWithoutTimerBgBitmap().getHeight() * 0.5));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getDownFireGearValue(),mViewRect.centerX(),baseLineY, mPaint);

                mPaint.setTextSize(getSeperateFireGearWithTimerTimerFontSize());
                mPaint.setTypeface(fireGearTypeface);
                mViewRect.set(
                        45,
                        (float) (getCookerViewHeight() / 2 - HOB_RETANGLE_VIEW_GAP + getSeperatePowerOnFireWithoutTimerBgBitmap().getHeight() * 0.5),
                        getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth(),
                        (float) (getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperatePowerOnFireWithoutTimerBgBitmap().getHeight()));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getDownTimerValue(),mViewRect.centerX(),baseLineY, mPaint);

                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITHOUT_TIMER:
                canvas.drawBitmap(getSeperatePowerOnTempWithoutTimerBgBitmap(),0,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),null);
                canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperatePowerOnTempWithoutTimerBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP / 2 + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperatePowerOnTempWithoutTimerBgBitmap().getHeight() - getPowerOffButtonBitmap().getHeight()),null);

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTextSize(getSeperateDownTempGearWithoutTimerTempFontSize());
                mPaint.setTypeface(fireGearTypeface);
                mViewRect.set(
                        30,
                        (getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),
                        getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth(),
                        (float) (getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperatePowerOnFireWithoutTimerBgBitmap().getHeight() * 0.63));//0.7
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getDownTempValue().length() <= 2?(" " + getDownTempValue()  + "º"): " " + getDownTempValue() + "º",mViewRect.centerX(),baseLineY, mPaint);
                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER:
                canvas.drawBitmap(getSeperatePowerOnTempWithTimerBgBitmap(),0,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),null);
                canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperatePowerOnTempWithTimerBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP / 2 + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperatePowerOnTempWithTimerBgBitmap().getHeight() - getPowerOffButtonBitmap().getHeight()),null);

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTextSize(getSeperateDownTempGearWithTimerTempFontSize());
                mPaint.setTypeface(fireGearTypeface);
                mViewRect.set(
                        30,
                        (getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),
                        getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth(),
                        (float) (getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperatePowerOnFireWithoutTimerBgBitmap().getHeight() * 0.5));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getDownTempValue().length() <= 2?(" " + getDownTempValue()  + "º"): " " + getDownTempValue() + "º",mViewRect.centerX(),baseLineY, mPaint);

                mPaint.setTextSize(getSeperateFireGearWithTimerTimerFontSize());
                mPaint.setTypeface(fireGearTypeface);
                mViewRect.set(
                        25,
                        (float) (getCookerViewHeight() / 2 - HOB_RETANGLE_VIEW_GAP + getSeperatePowerOnFireWithoutTimerBgBitmap().getHeight() * 0.5),
                        getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth(),
                        (float) (getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperatePowerOnFireWithoutTimerBgBitmap().getHeight() * 0.95));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getDownTimerValue(),mViewRect.centerX(),baseLineY, mPaint);


                break;
            case HOB_VIEW_WORK_MODE_ABNORMAL_NO_PAN:

                canvas.drawBitmap(getSeperateAbnormalNoPanBgBitmap(),0,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),null);
                canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperateAbnormalNoPanBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP / 2 + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperateAbnormalNoPanBgBitmap().getHeight() - getPowerOffButtonBitmap().getHeight()),null);

                break;
            case HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITHOUT_TIMER:
                canvas.drawBitmap(getSeperatePowerOnTempIndicatorWithoutTimerBgBitmap(),0,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),null);
                canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperatePowerOnTempIndicatorWithoutTimerBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP / 2 + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperatePowerOnTempIndicatorWithoutTimerBgBitmap().getHeight() - getPowerOffButtonBitmap().getHeight()),null);
                canvas.drawBitmap(getSeperateDownTempIndicatorWithoutTimerBitmap(),getSeperatePowerOnTempIndicatorWithoutTimerBgBitmap().getWidth() / 2 - getSeperateDownTempIndicatorWithoutTimerBitmap().getWidth() / 2, (float) (getCookerViewHeight() * 0.55),null);


                break;
            case HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER:
                canvas.drawBitmap(getSeperatePowerOnTempIndicatorWithTimerBgBitmap(),0,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),null);
                canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperatePowerOnTempIndicatorWithTimerBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP / 2 + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperatePowerOnTempIndicatorWithTimerBgBitmap().getHeight() - getPowerOffButtonBitmap().getHeight()),null);
                canvas.drawBitmap(getSeperateDownTempIndicatorWithTimerBitmap(),getSeperatePowerOnTempIndicatorWithTimerBgBitmap().getWidth() / 2 - getSeperateDownTempIndicatorWithTimerBitmap().getWidth() / 2, (float) (getCookerViewHeight() * 0.55),null);

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTextSize(getSeperateFireGearWithTimerTimerFontSize());
                mPaint.setTypeface(fireGearTypeface);
                mViewRect.set(
                        30,
                        (float) (getCookerViewHeight() / 2 - HOB_RETANGLE_VIEW_GAP + getSeperatePowerOnTempWithTimerBgBitmap().getHeight() * 0.50),
                        getSeperatePowerOnTempWithTimerBgBitmap().getWidth(),
                        (float) (getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperatePowerOnTempWithTimerBgBitmap().getHeight()));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getDownTimerValue(),mViewRect.centerX(),baseLineY, mPaint);
                break;
            case HOB_VIEW_WORK_MODE_TEMP_WORK_FINISH_WAIT_USER_CONFIRM:
                //LogUtil.d("Enter:: samhung up flash");
                /*if (downTempWorkFinishProgress == TEMP_WORK_FINISH_PROGRESS_LIGHT) {
                    canvas.drawBitmap(getSeperateTempFinishLightBgBitmap(),0,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),null);
                    downTempWorkFinishProgress = TEMP_WORK_FINISH_PROGRESS_DARK;

                }else {
                    canvas.drawBitmap(getSeperateTempFinishDarkBgBitmap(),0,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),null);
                    downTempWorkFinishProgress = TEMP_WORK_FINISH_PROGRESS_LIGHT;
                }
                canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperateTempFinishLightBgBitmap().getWidth() + + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),null);


                mPaint.setTypeface(tempGearTypeface);
                mPaint.setTextSize(40);
                mViewRect.set(0, (float) (getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2), getSeperateTempFinishLightBgBitmap().getWidth(), (float) (getCookerViewHeight() * 0.5 + getSeperateTempFinishLightBgBitmap().getHeight() / 2));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText("+10min",mViewRect.centerX(),baseLineY, mPaint);

                mViewRect.set(0, (float) (getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getUnionTempFinishLightBgBitmap().getHeight() * 0.5), getSeperateTempFinishLightBgBitmap().getWidth(), (float) (getCookerViewHeight() * 0.75));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText("keep Warm",mViewRect.centerX(),baseLineY, mPaint);

                if (handler.hasMessages(HANDLER_FLASH_KEEP_WARM_TIPS_DOWN_COOKER)) {
                    handler.removeMessages(HANDLER_FLASH_KEEP_WARM_TIPS_DOWN_COOKER);

                }
                handler.sendEmptyMessageDelayed(HANDLER_FLASH_KEEP_WARM_TIPS_DOWN_COOKER,TIME_FLASH_KEEP_WARM);*/


                mPaint.setTextAlign(Paint.Align.CENTER);
                if (downBlinkFlag == BLINK_FLAG_HAVE_NOT_BLINK) {
                    handler.removeMessages(HANDLER_SEPERATE_DOWN_BLINK);
                    downBlinkFlag = BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE;
                    arrow = 0;
                    light = 1.0f;
                    alpha = 255;
                    mAlphaPaint.setAlpha(alpha);
                    canvas.drawBitmap(getSeperateTempWorkFinishLightBitmap(),0,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),mAlphaPaint);

                    //canvas.drawBitmap(getSeperateTempWorkFinishLightBitmap(),0,(getCookerViewHeight()/2 - getSeperateReadyToCookBgBitmap().getHeight()/2),mAlphaPaint);
                    handler.sendEmptyMessageDelayed(HANDLER_SEPERATE_DOWN_BLINK,30);
                }else if(downBlinkFlag == BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE) {
                    canvas.drawBitmap(getSeperateTempWorkFinishLightBitmap(),0,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),mAlphaPaint);

                    //canvas.drawBitmap(getSeperateTempWorkFinishLightBitmap(),0,(getCookerViewHeight()/2 - getSeperateReadyToCookBgBitmap().getHeight()/2),mAlphaPaint);

                }else if(downBlinkFlag == BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_TWO) {
                    handler.removeMessages(HANDLER_SEPERATE_DOWN_BLINK);

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
                    canvas.drawBitmap(getSeperateTempWorkFinishLightBitmap(),0,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),mAlphaPaint);

                    downBlinkFlag = BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE;
                    handler.sendEmptyMessageDelayed(HANDLER_SEPERATE_DOWN_BLINK,30);
                }

                canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperateTempFinishLightBgBitmap().getWidth() + + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),mAlphaPaint);

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTypeface(tempGearTypeface);
                mPaint.setAlpha(alpha);
                mPaint.setTextSize(getSeperateReadyToCookFontSize());

                mViewRect.set(0, (float) (getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2), getSeperateTempFinishLightBgBitmap().getWidth(), (float) (getCookerViewHeight() * 0.5 + getSeperateTempFinishLightBgBitmap().getHeight() / 2));

                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("+10min",mViewRect.centerX(),baseLineY, mPaint);


                mViewRect.set(0, (float) (getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getUnionTempFinishLightBgBitmap().getHeight() * 0.5), getSeperateTempFinishLightBgBitmap().getWidth(), (float) (getCookerViewHeight() * 0.75));

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
                    if (rect.width() > getUnionInnerTextMaxWidth()) {
                        keepWarmTextMarqueeNeeded = true;
                        keepWarmMarquee = new Marquee(
                                keepWarmText,
                                new RectF(
                                        mViewRect.centerX() - getUnionInnerTextMaxWidth() / 2,
                                        mViewRect.top,
                                        mViewRect.centerX() + getUnionInnerTextMaxWidth() / 2,
                                        mViewRect.bottom
                                ),
                                new PointF(
                                        mViewRect.centerX() - getUnionInnerTextMaxWidth() / 2,
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
                }


                break;
            case HOB_VIEW_WORK_MODE_TEMP_SENSOR_READY:
                canvas.drawBitmap(getSeperateReadyToCookBgBitmap(),0,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),null);
                canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperateReadyToCookBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP / 2 + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperateReadyToCookBgBitmap().getHeight() - getPowerOffButtonBitmap().getHeight()),null);

                mPaint.setTextAlign(Paint.Align.CENTER);
                if (readyToCookText == null) {
                    readyToCookText = getContext().getResources().getString(R.string.viewmodule_cooker_view_ready_to_cook);
                    readyToCookTextChanged = true;
                }

                if (readyToCookTextChanged) {
                    Rect rect = getStringRect(readyToCookText);
                    if (rect.width() > getUnionInnerTextMaxWidthBig()) {
                        readyToCookTextMarqueeNeeded = true;
                        baseLineY = getReadyToCookY();
                        readyToCookMarquee = new Marquee(
                                readyToCookText,
                                new RectF(
                                        mViewRect.centerX() - getUnionInnerTextMaxWidthBig() / 2,
                                        mViewRect.top,
                                        mViewRect.centerX() + getUnionInnerTextMaxWidthBig() / 2,
                                        mViewRect.bottom
                                ),
                                new PointF(
                                        mViewRect.centerX() - getUnionInnerTextMaxWidthBig() / 2,
                                        baseLineY
                                ),
                                4
                        );
                    } else {
                        readyToCookTextMarqueeNeeded = false;
                    }
                    readyToCookTextChanged = false;
                }

                if (downBlinkFlag == BLINK_FLAG_HAVE_NOT_BLINK) {
                    handler.removeMessages(HANDLER_SEPERATE_DOWN_BLINK);
                    downCurrentBlinkOptions = BLINK_OPTIONS_ONE;
                    downBlinkFlag = BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE;
                    canvas.drawBitmap(getSeperateReadyToCookBgBitmap(),0,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),null);
                    canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperateReadyToCookBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP / 2 + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperateReadyToCookBgBitmap().getHeight() - getPowerOffButtonBitmap().getHeight()),null);


                    mPaint.setTypeface(tempGearTypeface);
                    mPaint.setTextSize(getSeperateReadyToCookFontSize());
                    baseLineY = getDownReadyToCookY();
                    if (!readyToCookTextMarqueeNeeded) {
                        mPaint.setTextAlign(Paint.Align.CENTER);
                        canvas.drawText(readyToCookText, mViewRect.centerX(), baseLineY, mPaint);
                    } else {
                        mPaint.setTextAlign(Paint.Align.LEFT);
                        readyToCookMarquee.marquee(canvas, mPaint);
                    }

                    handler.sendEmptyMessageDelayed(HANDLER_SEPERATE_DOWN_BLINK,BLINK_TIME_FOR_READY_TO_COOK);


                }else if(downBlinkFlag == BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE) {
                    if (downCurrentBlinkOptions == BLINK_OPTIONS_ONE) {//ready to cook
                        canvas.drawBitmap(getSeperateReadyToCookBgBitmap(),0,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),null);
                        canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperateReadyToCookBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP / 2 + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperateReadyToCookBgBitmap().getHeight() - getPowerOffButtonBitmap().getHeight()),null);


                        mPaint.setTypeface(tempGearTypeface);
                        mPaint.setTextSize(getSeperateReadyToCookFontSize());
                        baseLineY = getDownReadyToCookY();
                        if (!readyToCookTextMarqueeNeeded) {
                            mPaint.setTextAlign(Paint.Align.CENTER);
                            canvas.drawText(readyToCookText, mViewRect.centerX(), baseLineY, mPaint);
                        } else {
                            mPaint.setTextAlign(Paint.Align.LEFT);
                            readyToCookMarquee.marquee(canvas, mPaint);
                        }

                    }else {//value
                        if (readyToCookTextMarqueeNeeded) {
                            readyToCookMarquee.reset(-24);
                        }
                        canvas.drawBitmap(getSeperateReadyToCookValueBgBitmap(),0,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),null);
                        canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperateReadyToCookBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP / 2 + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperateReadyToCookBgBitmap().getHeight() - getPowerOffButtonBitmap().getHeight()),null);

                        mPaint.setTextAlign(Paint.Align.CENTER);
                        mPaint.setTypeface(fireGearTypeface);
                        mPaint.setTextSize(getSeperateDownReadyToCookValueFontSize());//140
                        mPaint.setFakeBoldText(true);


                        mViewRect.set(
                                30,
                                (getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),
                                getSeperateReadyToCookBgBitmap().getWidth(),
                                (float) (getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperateReadyToCookBgBitmap().getHeight() * 0.55));


                        fontMetrics = mPaint.getFontMetrics();
                        top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                        bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                        baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                        canvas.drawText(getDownTempValue().length() <= 2?(" " + getDownTempValue()  + "º"): " " + getDownTempValue() + "º",mViewRect.centerX(),baseLineY, mPaint);//º

                    }


                }else if(downBlinkFlag == BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_TWO) {
                    handler.removeMessages(HANDLER_SEPERATE_DOWN_BLINK);
                    if (downCurrentBlinkOptions == BLINK_OPTIONS_ONE) {//ready to cook
                        canvas.drawBitmap(getSeperateReadyToCookBgBitmap(),0,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),null);
                        canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperateReadyToCookBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP / 2 + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperateReadyToCookBgBitmap().getHeight() - getPowerOffButtonBitmap().getHeight()),null);

                        mPaint.setTextAlign(Paint.Align.CENTER);
                        mPaint.setTypeface(tempGearTypeface);
                        mPaint.setTextSize(getSeperateReadyToCookFontSize());
                        baseLineY = getDownReadyToCookY();
                        if (!readyToCookTextMarqueeNeeded) {
                            mPaint.setTextAlign(Paint.Align.CENTER);
                            canvas.drawText(readyToCookText, mViewRect.centerX(), baseLineY, mPaint);
                        } else {
                            mPaint.setTextAlign(Paint.Align.LEFT);
                            readyToCookMarquee.marquee(canvas, mPaint);
                        }

                        handler.sendEmptyMessageDelayed(HANDLER_SEPERATE_DOWN_BLINK,BLINK_TIME_FOR_READY_TO_COOK);
                    }else {//value
                        if (readyToCookTextMarqueeNeeded) {
                            readyToCookMarquee.reset(-24);
                        }
                        canvas.drawBitmap(getSeperateReadyToCookValueBgBitmap(),0,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),null);
                        canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperateReadyToCookBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP / 2 + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperateReadyToCookBgBitmap().getHeight() - getPowerOffButtonBitmap().getHeight()),null);

                        mPaint.setTextAlign(Paint.Align.CENTER);
                        mPaint.setTypeface(fireGearTypeface);
                        mPaint.setTextSize(getSeperateDownReadyToCookValueFontSize());//140
                        mPaint.setFakeBoldText(true);
                        // mViewRect.set(getUnionReadyToCookValueBgBitmap().getWidth() / 20, (float) (getCookerViewHeight() / 2 - getUnionReadyToCookValueBgBitmap().getHeight() * 0.4), getUnionReadyToCookValueBgBitmap().getWidth(), getCookerViewHeight() / 2);

                        mViewRect.set(
                                30,
                                (getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),
                                getSeperateReadyToCookBgBitmap().getWidth(),
                                (float) (getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperateReadyToCookBgBitmap().getHeight() * 0.55));


                        fontMetrics = mPaint.getFontMetrics();
                        top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                        bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                        baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                        canvas.drawText(getDownTempValue().length() <= 2?(" " + getDownTempValue()  + "º"): " " + getDownTempValue() + "º",mViewRect.centerX(),baseLineY, mPaint);//º

                        handler.sendEmptyMessageDelayed(HANDLER_SEPERATE_DOWN_BLINK,BLINK_TIME_FOR_READY_TO_COOK);
                    }



                    downBlinkFlag = BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE;
                }



                break;
            case HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR:
/*                canvas.drawBitmap(getSeperatePrepareTempSensorBgBitmap(),0,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),null);
                canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperatePrepareTempSensorBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP / 2 + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperatePrepareTempSensorBgBitmap().getHeight() - getPowerOffButtonBitmap().getHeight()),null);

                mPaint.setTextSize(getSeperateDownTempGearWithTimerTempFontSize());
                mPaint.setTypeface(fireGearTypeface);
                mViewRect.set(
                        10,
                        (getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),
                        getSeperatePowerOnTempWithTimerBgBitmap().getWidth(),
                        (float) (getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperatePowerOnTempWithTimerBgBitmap().getHeight() * 0.5));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getDownTempValue(),mViewRect.centerX(),baseLineY, mPaint);*/


                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawBitmap(getSeperatePrepareTempSensorBgBitmap(),0,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),null);
                canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperatePrepareTempSensorBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP / 2 + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperatePrepareTempSensorBgBitmap().getHeight() - getPowerOffButtonBitmap().getHeight()),null);

                mPaint.setTypeface(fireGearTypeface);
                mPaint.setTextSize(getDownTempPreheatSettingValueFontSize());//140
                mPaint.setFakeBoldText(true);//getSeperateTempGearWithTimerBitmap
                mViewRect.set(
                        getSeperateTempPrepareSensorBitmap().getWidth() / 20,
                        (float) (getCookerViewHeight() / 2 - getSeperateTempPrepareSensorBitmap().getHeight() * 0.4),
                        getSeperateTempPrepareSensorBitmap().getWidth(),
                        (float) (getCookerViewHeight() * 0.95));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getDownTempValue().length() <= 2?(" " + getDownTempValue()  + "º"): " " + getDownTempValue() + "º",mViewRect.centerX(),baseLineY, mPaint);//º


                mPaint.setTypeface(tfHelvetica57Condensed);
                mPaint.setTextSize(getDownTempPreheatRealValueFontSize());
                mPaint.setFakeBoldText(false);//getSeperateTempGearWithTimerBitmap
                mViewRect.set(
                        getSeperateTempPrepareSensorBitmap().getWidth() / 20,
                        (float) (getCookerViewHeight() / 2 - getSeperateTempPrepareSensorBitmap().getHeight() * 0.4),
                        getSeperateTempPrepareSensorBitmap().getWidth(),
                        (float) (getCookerViewHeight() * 1.44));//1.23 getUpRealTempTextGap()
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getDownRealTempValue() + "º",mViewRect.centerX(),baseLineY, mPaint);//º

                mCenterPoint.x = (int) (getSeperateTempPrepareSensorBitmap().getWidth() * 0.525);
                mCenterPoint.y = (int) (getCookerViewHeight() * 0.843);//0.16 getUpRealTempArcGap()
                mRectF.left = mCenterPoint.x - getSeperateRealTempProgressRadius() - getSeperateRealTempProgressArcWidth() / 2;
                mRectF.top = mCenterPoint.y - getSeperateRealTempProgressRadius() - getSeperateRealTempProgressArcWidth() / 2;
                mRectF.right = mCenterPoint.x + getSeperateRealTempProgressRadius() + getSeperateRealTempProgressArcWidth() / 2;
                mRectF.bottom = mCenterPoint.y + getSeperateRealTempProgressRadius() + getSeperateRealTempProgressArcWidth() / 2;
                mArcPaint.setStrokeWidth(getSeperateRealTempProgressArcWidth());
                canvas.drawArc(mRectF, -90, getDownSweepAngle(), false, mArcPaint);



                break;
            case HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR_WITH_INDICATOR:
                canvas.drawBitmap(getSeperatePrepareTempSensorWithIndicatorBgBitmap(),0,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),null);
                canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperatePrepareTempSensorWithIndicatorBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP / 2 + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperatePrepareTempSensorWithIndicatorBgBitmap().getHeight() - getPowerOffButtonBitmap().getHeight()),null);
                canvas.drawBitmap(getSeperateDownTempIndicatorWithTimerBitmap(),getSeperatePrepareTempSensorWithIndicatorBgBitmap().getWidth() / 2 - getSeperateDownTempIndicatorWithTimerBitmap().getWidth() / 2, (float) (getCookerViewHeight() * 0.55),null);


                break;
            case HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP:
                //canvas.drawBitmap(getSeperateHighTempBgBitmap(),0,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperateHighTempBgBitmap().getHeight()),null);
                canvas.drawBitmap(getSeperateHighTempBgBitmap(),0,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),null);

                break;
            case HOB_VIEW_WORK_MODE_ABNORMAL_ERROR_OCURR:
                canvas.drawBitmap(getSeperateErrorOccurBgBitmap(),0,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),null);
                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTextSize(getSeperateErrorMessageFontSize());
                mPaint.setTypeface(fireGearTypeface);
                mViewRect.set(
                        0,
                        (getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),
                        getSeperateErrorOccurBgBitmap().getWidth(),
                        (float) (getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperateErrorOccurBgBitmap().getHeight()));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getDownErrorMessage(),mViewRect.centerX(),baseLineY, mPaint);
                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_FIRST:
                if (tempWorkRecipesProgressForSeperateDown ==TEMP_WORK_RECIPES_PROGRESS_PICTURE) {
                    canvas.drawBitmap(getSeperateRecipesBgBitmap(),0,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),null);
                    canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperateRecipesBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP / 2 + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperateRecipesBgBitmap().getHeight() - getPowerOffButtonBitmap().getHeight()),null);
                    canvas.drawBitmap(getDownRecipesPicBitmap(),15,(getCookerViewHeight()/2 + HOB_RETANGLE_VIEW_GAP / 2 ) + (getSeperateRecipesBgBitmap().getHeight() - getDownRecipesPicBitmap().getHeight()) / 2,null);

                    if (!handler.hasMessages(HANDLER_FLASH_RECIPES_FOR_SEPERATE_DOWN)) {
                        handler.sendEmptyMessageDelayed(HANDLER_FLASH_RECIPES_FOR_SEPERATE_DOWN,TIME_SHOW_RECIPES_PICTURE);
                    }

                }else if (tempWorkRecipesProgressForSeperateDown ==TEMP_WORK_RECIPES_PROGRESS_TEMP_VALUE) {
                    canvas.drawBitmap(getSeperatePowerOnTempWithTimerBgBitmap(),0,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),null);
                    canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperatePowerOnTempWithTimerBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP / 2 + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperatePowerOnTempWithTimerBgBitmap().getHeight() - getPowerOffButtonBitmap().getHeight()),null);

                    mPaint.setTextAlign(Paint.Align.CENTER);
                    mPaint.setTextSize(getSeperateDownTempGearWithTimerTempFontSize());
                    mPaint.setTypeface(fireGearTypeface);
                    mViewRect.set(
                            10,
                            (getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),
                            getSeperatePowerOnTempWithTimerBgBitmap().getWidth(),
                            (float) (getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperatePowerOnTempWithTimerBgBitmap().getHeight() * 0.5));
                    fontMetrics = mPaint.getFontMetrics();
                    top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                    bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                    baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                    canvas.drawText(getDownTempValue(),mViewRect.centerX(),baseLineY, mPaint);

                    mPaint.setTextSize(getSeperateFireGearWithTimerTimerFontSize());
                    mPaint.setTypeface(fireGearTypeface);
                    mViewRect.set(
                            30,
                            (float) (getCookerViewHeight() / 2 - HOB_RETANGLE_VIEW_GAP + getSeperatePowerOnTempWithTimerBgBitmap().getHeight() * 0.45),
                            getSeperatePowerOnTempWithTimerBgBitmap().getWidth(),
                            (float) (getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperatePowerOnTempWithTimerBgBitmap().getHeight()));
                    fontMetrics = mPaint.getFontMetrics();
                    top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                    bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                    baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                    canvas.drawText(getDownTimerValue(),mViewRect.centerX(),baseLineY, mPaint);
                    if (!handler.hasMessages(HANDLER_FLASH_RECIPES_FOR_SEPERATE_DOWN)) {
                        handler.sendEmptyMessageDelayed(HANDLER_FLASH_RECIPES_FOR_SEPERATE_DOWN,TIME_SHOW_RECIPES_TEMP_VALUE);
                    }
                }

                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_SECOND:
                if (tempWorkRecipesProgressForSeperateDown ==TEMP_WORK_RECIPES_PROGRESS_PICTURE) {
                    canvas.drawBitmap(getSeperateRecipesBgBitmap(),0,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),null);
                    canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperateRecipesBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP / 2 + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperateRecipesBgBitmap().getHeight() - getPowerOffButtonBitmap().getHeight()),null);
                    canvas.drawBitmap(getDownRecipesPicBitmap(),15,(getCookerViewHeight()/2 + HOB_RETANGLE_VIEW_GAP / 2 ) + (getSeperateRecipesBgBitmap().getHeight() - getDownRecipesPicBitmap().getHeight()) / 2,null);

                    if (!handler.hasMessages(HANDLER_FLASH_RECIPES_FOR_SEPERATE_DOWN)) {
                        handler.sendEmptyMessageDelayed(HANDLER_FLASH_RECIPES_FOR_SEPERATE_DOWN,TIME_SHOW_RECIPES_PICTURE);
                    }

                }else if (tempWorkRecipesProgressForSeperateDown ==TEMP_WORK_RECIPES_PROGRESS_TEMP_VALUE) {
                    canvas.drawBitmap(getSeperatePowerOnTempWithTimerBgBitmap(),0,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),null);
                    canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperatePowerOnTempWithTimerBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP / 2 + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperatePowerOnTempWithTimerBgBitmap().getHeight() - getPowerOffButtonBitmap().getHeight()),null);

                    mPaint.setTextAlign(Paint.Align.CENTER);
                    mPaint.setTextSize(getSeperateDownTempGearWithTimerTempFontSize());
                    mPaint.setTypeface(fireGearTypeface);
                    mViewRect.set(
                            10,
                            (getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),
                            getSeperatePowerOnTempWithTimerBgBitmap().getWidth(),
                            (float) (getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperatePowerOnTempWithTimerBgBitmap().getHeight() * 0.5));
                    fontMetrics = mPaint.getFontMetrics();
                    top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                    bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                    baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                    canvas.drawText(getDownTempValue(),mViewRect.centerX(),baseLineY, mPaint);

                    mPaint.setTextSize(getSeperateFireGearWithTimerTimerFontSize());
                    mPaint.setTypeface(fireGearTypeface);
                    mViewRect.set(
                            30,
                            (float) (getCookerViewHeight() / 2 - HOB_RETANGLE_VIEW_GAP + getSeperatePowerOnTempWithTimerBgBitmap().getHeight() * 0.45),
                            getSeperatePowerOnTempWithTimerBgBitmap().getWidth(),
                            (float) (getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2 + getSeperatePowerOnTempWithTimerBgBitmap().getHeight()));
                    fontMetrics = mPaint.getFontMetrics();
                    top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                    bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                    baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                    canvas.drawText(getDownTimerValue(),mViewRect.centerX(),baseLineY, mPaint);
                    if (!handler.hasMessages(HANDLER_FLASH_RECIPES_FOR_SEPERATE_DOWN)) {
                        handler.sendEmptyMessageDelayed(HANDLER_FLASH_RECIPES_FOR_SEPERATE_DOWN,TIME_SHOW_RECIPES_TEMP_VALUE);
                    }
                }
                break;

        }
    }

    private void drawUpCookerView(Canvas canvas) {
        //if (retangleHobWorkMode == HOB_RETANGLE_WORK_MODE_WORK_UNITE) return;
        PointF p;
        float top,bottom;
        int baseLineY;
        switch (upWorkMode) {
            case HOB_VIEW_WORK_MODE_POWER_OFF:
                //canvas.drawBitmap(getSeperatePowerOffBgBitmap(),(getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth() - getSeperatePowerOffBgBitmap().getWidth()) / 2,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOffBgBitmap().getHeight()),null);
                //canvas.drawBitmap(getSeperatePowerOffBgBitmap(),(getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth() - getSeperatePowerOffBgBitmap().getWidth()) / 2,(getCookerViewHeight()/2 - getUnionPowerOffBgBitmap().getHeight()/2),null);
                canvas.drawBitmap(getSeperatePowerOffBgBitmap(),0,(getCookerViewHeight()/2 - getUnionPowerOffBgBitmap().getHeight()/2),null);

                break;
            case HOB_VIEW_WORK_MODE_FIRE_GEAR_WITHOUT_TIMER:
                canvas.drawBitmap(getSeperatePowerOnFireWithoutTimerBgBitmap(),0,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnFireWithoutTimerBgBitmap().getHeight()),null);
                canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnFireWithoutTimerBgBitmap().getHeight()),null);

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTextSize(getSeperateFireGearWithoutTimerFontSize());
                mPaint.setTypeface(fireGearTypeface);
                mViewRect.set(
                        30,
                        (getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnFireWithoutTimerBgBitmap().getHeight()),
                        getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth(),
                        getCookerViewHeight() / 2 - HOB_RETANGLE_VIEW_GAP / 2 - 100);//70
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom1
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getUpFireGearValue(),mViewRect.centerX(),baseLineY, mPaint);

                break;
            case HOB_VIEW_WORK_MODE_FIRE_GEAR_WITH_TIMER:
                canvas.drawBitmap(getSeperatePowerOnFireWithTimerBgBitmap(),0,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnFireWithTimerBgBitmap().getHeight()),null);
                canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperatePowerOnFireWithTimerBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnFireWithTimerBgBitmap().getHeight()),null);

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTextSize(getSeperateFireGearWithTimerGearFontSize());
                mPaint.setTypeface(fireGearTypeface);
                mViewRect.set(
                        30,
                        (getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnFireWithoutTimerBgBitmap().getHeight()),
                        getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth(),
                        (float) (getCookerViewHeight() / 2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnFireWithoutTimerBgBitmap().getHeight() * 0.48));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式1
                canvas.drawText(getUpFireGearValue(),mViewRect.centerX(),baseLineY, mPaint);


                mPaint.setTextSize((float) (getSeperateFireGearWithTimerTimerFontSize()));
                mViewRect.set(
                        45,
                        (getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnFireWithoutTimerBgBitmap().getHeight() / 2),
                        getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth(),
                        (float) (getCookerViewHeight() / 2 - HOB_RETANGLE_VIEW_GAP * 2 ));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getUpTimerValue(),mViewRect.centerX(),baseLineY, mPaint);

                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITHOUT_TIMER:
                canvas.drawBitmap(getSeperatePowerOnTempWithoutTimerBgBitmap(),0,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnTempWithoutTimerBgBitmap().getHeight()),null);
                canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperatePowerOnTempWithoutTimerBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnTempWithoutTimerBgBitmap().getHeight()),null);

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTextSize(getSeperateUpTempGearWithoutTimerTempFontSize());
                mPaint.setTypeface(fireGearTypeface);
                mViewRect.set(
                        30,
                        (getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnFireWithoutTimerBgBitmap().getHeight()),
                        getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth(),
                        getCookerViewHeight() / 2 - HOB_RETANGLE_VIEW_GAP / 2 - 100);
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getUpTempValue().length() <= 2?(" " + getUpTempValue()  + "º"): " " + getUpTempValue() + "º",mViewRect.centerX(),baseLineY, mPaint);
                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER:

                canvas.drawBitmap(getSeperatePowerOnTempWithTimerBgBitmap(),0,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnTempWithTimerBgBitmap().getHeight()),null);
                canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperatePowerOnTempWithTimerBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnTempWithTimerBgBitmap().getHeight()),null);

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTextSize(getSeperateUpTempGearWithTimerTempFontSize());
                mPaint.setTypeface(fireGearTypeface);
                mViewRect.set(
                        30,
                        (getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnFireWithoutTimerBgBitmap().getHeight()),
                        getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth(),
                        (float) (getCookerViewHeight() / 2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnFireWithoutTimerBgBitmap().getHeight() * 0.48));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getUpTempValue().length() <= 2?(" " + getUpTempValue()  + "º"): " " + getUpTempValue() + "º",mViewRect.centerX(),baseLineY, mPaint);


                mPaint.setTextSize(getSeperateFireGearWithTimerTimerFontSize());
                mPaint.setTypeface(fireGearTypeface);
                mViewRect.set(
                        25,
                        (getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnFireWithoutTimerBgBitmap().getHeight() / 2),
                        getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth(),
                        (float) (getCookerViewHeight() / 2 - HOB_RETANGLE_VIEW_GAP * 4 ));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getUpTimerValue(),mViewRect.centerX(),baseLineY, mPaint);
                break;
            case HOB_VIEW_WORK_MODE_ABNORMAL_NO_PAN:
                canvas.drawBitmap(getSeperateAbnormalNoPanBgBitmap(),0,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperateAbnormalNoPanBgBitmap().getHeight()),null);
                canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperateAbnormalNoPanBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperateAbnormalNoPanBgBitmap().getHeight()),null);

                break;
            case HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITHOUT_TIMER:
                canvas.drawBitmap(getSeperatePowerOnTempIndicatorWithoutTimerBgBitmap(),0,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnTempIndicatorWithoutTimerBgBitmap().getHeight()),null);
                canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperatePowerOnTempIndicatorWithoutTimerBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnTempIndicatorWithoutTimerBgBitmap().getHeight()),null);

                canvas.drawBitmap(getSeperateUpTempIndicatorWithoutTimerBitmap(),getSeperatePowerOnTempIndicatorWithoutTimerBgBitmap().getWidth() / 2 - getSeperateUpTempIndicatorWithoutTimerBitmap().getWidth() / 2, (float) (getCookerViewHeight() / 10),null);




                break;
            case HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER:
                canvas.drawBitmap(getSeperatePowerOnTempIndicatorWithTimerBgBitmap(),0,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnTempIndicatorWithTimerBgBitmap().getHeight()),null);
                canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperatePowerOnTempIndicatorWithTimerBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnTempIndicatorWithTimerBgBitmap().getHeight()),null);
                canvas.drawBitmap(getSeperateUpTempIndicatorWithTimerBitmap(),getSeperatePowerOnTempIndicatorWithTimerBgBitmap().getWidth() / 2 - getSeperateUpTempIndicatorWithTimerBitmap().getWidth() / 2, (float) (getCookerViewHeight() / 10),null);

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTextSize(getSeperateFireGearWithTimerTimerFontSize());
                mPaint.setTypeface(fireGearTypeface);
                mViewRect.set(
                        30,
                        (getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnTempIndicatorWithTimerBgBitmap().getHeight() / 2),
                        getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth(),
                        (float) (getCookerViewHeight() / 2 - HOB_RETANGLE_VIEW_GAP * 3 ));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getUpTimerValue(),mViewRect.centerX(),baseLineY, mPaint);

                break;
            case HOB_VIEW_WORK_MODE_TEMP_WORK_FINISH_WAIT_USER_CONFIRM:
                //LogUtil.d("Enter:: samhung up flash");
                /*if (upTempWorkFinishProgress == TEMP_WORK_FINISH_PROGRESS_LIGHT) {
                    canvas.drawBitmap(getSeperateTempFinishLightBgBitmap(),0,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperateTempFinishLightBgBitmap().getHeight()),null);
                    upTempWorkFinishProgress = TEMP_WORK_FINISH_PROGRESS_DARK;

                }else {
                    canvas.drawBitmap(getSeperateTempFinishDarkBgBitmap(),0,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2- getSeperateTempFinishDarkBgBitmap().getHeight()),null);
                    upTempWorkFinishProgress = TEMP_WORK_FINISH_PROGRESS_LIGHT;
                }
                canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperateTempFinishLightBgBitmap().getWidth() + + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperateTempFinishLightBgBitmap().getHeight() ),null);


                mPaint.setTypeface(tempGearTypeface);
                mPaint.setTextSize(40);
                mViewRect.set(0, (float) (getCookerViewHeight() / 2 - getSeperateTempFinishLightBgBitmap().getHeight() ), getSeperateTempFinishLightBgBitmap().getWidth(), getCookerViewHeight()/ 4);
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText("+10min",mViewRect.centerX(),baseLineY, mPaint);

                mViewRect.set(0, (float) (getUnionTempFinishLightBgBitmap().getHeight() * 0.5), getSeperateTempFinishLightBgBitmap().getWidth(), getCookerViewHeight() / 4);
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText("keep Warm",mViewRect.centerX(),baseLineY, mPaint);

                if (handler.hasMessages(HANDLER_FLASH_KEEP_WARM_TIPS_UP_COOKER)) {
                    handler.removeMessages(HANDLER_FLASH_KEEP_WARM_TIPS_UP_COOKER);

                }
                handler.sendEmptyMessageDelayed(HANDLER_FLASH_KEEP_WARM_TIPS_UP_COOKER,TIME_FLASH_KEEP_WARM);*/



                mPaint.setTextAlign(Paint.Align.CENTER);
                if (upBlinkFlag == BLINK_FLAG_HAVE_NOT_BLINK) {
                    handler.removeMessages(HANDLER_SEPERATE_UP_BLINK);
                    upBlinkFlag = BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE;
                    arrow = 0;
                    light = 1.0f;
                    alpha = 255;
                    mAlphaPaint.setAlpha(alpha);
                    canvas.drawBitmap(getSeperateTempFinishLightBgBitmap(),0,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperateTempFinishLightBgBitmap().getHeight()),mAlphaPaint);

                    //canvas.drawBitmap(getSeperateTempWorkFinishLightBitmap(),0,(getCookerViewHeight()/2 - getSeperateReadyToCookBgBitmap().getHeight()/2),mAlphaPaint);
                    handler.sendEmptyMessageDelayed(HANDLER_SEPERATE_UP_BLINK,30);
                }else if(upBlinkFlag == BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE) {
                    canvas.drawBitmap(getSeperateTempFinishLightBgBitmap(),0,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperateTempFinishLightBgBitmap().getHeight()),mAlphaPaint);

                    //canvas.drawBitmap(getSeperateTempWorkFinishLightBitmap(),0,(getCookerViewHeight()/2 - getSeperateReadyToCookBgBitmap().getHeight()/2),mAlphaPaint);

                }else if(upBlinkFlag == BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_TWO) {
                    handler.removeMessages(HANDLER_SEPERATE_UP_BLINK);

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
                    canvas.drawBitmap(getSeperateTempFinishLightBgBitmap(),0,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperateTempFinishLightBgBitmap().getHeight()),mAlphaPaint);

                    //canvas.drawBitmap(getSeperateTempWorkFinishLightBitmap(),0,(getCookerViewHeight()/2 - getSeperateReadyToCookBgBitmap().getHeight()/2),mAlphaPaint);
                    upBlinkFlag = BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE;
                    handler.sendEmptyMessageDelayed(HANDLER_SEPERATE_UP_BLINK,30);
                }

                canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperateTempFinishLightBgBitmap().getWidth() + + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperateTempFinishLightBgBitmap().getHeight() ),mAlphaPaint);

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTypeface(tempGearTypeface);
                mPaint.setAlpha(alpha);
                mPaint.setTextSize(getSeperateReadyToCookFontSize());
                /*mViewRect.set(0,
                        (float) (getCookerViewHeight() / 2 - getSeperateTempWorkFinishLightBitmap().getHeight() * 0.4),
                        getSeperateTempWorkFinishLightBitmap().getWidth(),
                        (float) (getCookerViewHeight()* 0.1));*/
                mViewRect.set(0, (float) (getCookerViewHeight() / 2 - getSeperateTempFinishLightBgBitmap().getHeight() ), getSeperateTempFinishLightBgBitmap().getWidth(), getCookerViewHeight()/ 4);

                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("+10min",mViewRect.centerX(),baseLineY, mPaint);

                /*mViewRect.set(0,
                        (float) (getSeperateTempWorkFinishLightBitmap().getHeight() *0.4),
                        getSeperateTempWorkFinishLightBitmap().getWidth(),
                        (float) (getCookerViewHeight() * 0.5));*/
                mViewRect.set(0, (float) (getUnionTempFinishLightBgBitmap().getHeight() * 0.5), getSeperateTempFinishLightBgBitmap().getWidth(), getCookerViewHeight() / 4);

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
                    if (rect.width() > getUnionInnerTextMaxWidth()) {
                        keepWarmTextMarqueeNeeded = true;
                        keepWarmMarquee = new Marquee(
                                keepWarmText,
                                new RectF(
                                        mViewRect.centerX() - getUnionInnerTextMaxWidth() / 2,
                                        mViewRect.top,
                                        mViewRect.centerX() + getUnionInnerTextMaxWidth() / 2,
                                        mViewRect.bottom
                                ),
                                new PointF(
                                        mViewRect.centerX() - getUnionInnerTextMaxWidth() / 2,
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
                }




                break;
            case HOB_VIEW_WORK_MODE_TEMP_SENSOR_READY:

                mPaint.setTextAlign(Paint.Align.CENTER);
                if (readyToCookText == null) {
                    readyToCookText = getContext().getResources().getString(R.string.viewmodule_cooker_view_ready_to_cook);
                    readyToCookTextChanged = true;
                }

                if (readyToCookTextChanged) {
                    Rect rect = getStringRect(readyToCookText);
                    if (rect.width() > getUnionInnerTextMaxWidthBig()) {
                        readyToCookTextMarqueeNeeded = true;
                        baseLineY = getReadyToCookY();
                        readyToCookMarquee = new Marquee(
                                readyToCookText,
                                new RectF(
                                        mViewRect.centerX() - getUnionInnerTextMaxWidthBig() / 2,
                                        mViewRect.top,
                                        mViewRect.centerX() + getUnionInnerTextMaxWidthBig() / 2,
                                        mViewRect.bottom
                                ),
                                new PointF(
                                        mViewRect.centerX() - getUnionInnerTextMaxWidthBig() / 2,
                                        baseLineY
                                ),
                                4
                        );
                    } else {
                        readyToCookTextMarqueeNeeded = false;
                    }
                    readyToCookTextChanged = false;
                }

                if (upBlinkFlag == BLINK_FLAG_HAVE_NOT_BLINK) {
                    handler.removeMessages(HANDLER_SEPERATE_UP_BLINK);
                    upCurrentBlinkOptions = BLINK_OPTIONS_ONE;
                    upBlinkFlag = BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE;
                    canvas.drawBitmap(getSeperateReadyToCookBgBitmap(),0,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperateReadyToCookBgBitmap().getHeight()),null);
                    //canvas.drawBitmap(getSeperateReadyToCookBgBitmap(),0,(getCookerViewHeight()/2 - getSeperateReadyToCookBgBitmap().getHeight()/2),null);
                    canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperateReadyToCookBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperateReadyToCookBgBitmap().getHeight()),null);


                    mPaint.setTypeface(tempGearTypeface);
                    mPaint.setTextSize(getSeperateReadyToCookFontSize());
                    baseLineY = getUpReadyToCookY();
                    if (!readyToCookTextMarqueeNeeded) {
                        mPaint.setTextAlign(Paint.Align.CENTER);
                        canvas.drawText(readyToCookText, mViewRect.centerX(), baseLineY, mPaint);
                    } else {
                        mPaint.setTextAlign(Paint.Align.LEFT);
                        readyToCookMarquee.marquee(canvas, mPaint);
                    }

                    handler.sendEmptyMessageDelayed(HANDLER_SEPERATE_UP_BLINK,BLINK_TIME_FOR_READY_TO_COOK);


                }else if(upBlinkFlag == BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE) {
                    if (upCurrentBlinkOptions == BLINK_OPTIONS_ONE) {//ready to cook
                        canvas.drawBitmap(getSeperateReadyToCookBgBitmap(),0,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperateReadyToCookBgBitmap().getHeight()),null);

                        //canvas.drawBitmap(getSeperateReadyToCookBgBitmap(),0,(getCookerViewHeight()/2 - getSeperateReadyToCookBgBitmap().getHeight()/2),null);
                        canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperateReadyToCookBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperateReadyToCookBgBitmap().getHeight()),null);


                        mPaint.setTypeface(tempGearTypeface);
                        mPaint.setTextSize(getSeperateReadyToCookFontSize());
                        baseLineY = getUpReadyToCookY();
                        if (!readyToCookTextMarqueeNeeded) {
                            mPaint.setTextAlign(Paint.Align.CENTER);
                            canvas.drawText(readyToCookText, mViewRect.centerX(), baseLineY, mPaint);
                        } else {
                            mPaint.setTextAlign(Paint.Align.LEFT);
                            readyToCookMarquee.marquee(canvas, mPaint);
                        }

                    }else {//value
                        if (readyToCookTextMarqueeNeeded) {
                            readyToCookMarquee.reset(-24);
                        }
                        canvas.drawBitmap(getSeperateReadyToCookValueBgBitmap(),0,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperateReadyToCookValueBgBitmap().getHeight()),null);
                        canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperateReadyToCookBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperateReadyToCookBgBitmap().getHeight()),null);

                        mPaint.setTextAlign(Paint.Align.CENTER);
                        mPaint.setTypeface(fireGearTypeface);
                        mPaint.setTextSize(getSeperateUpReadyToCookValueFontSize());//140
                        mPaint.setFakeBoldText(true);

                        mViewRect.set(
                                30,
                                (getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnFireWithoutTimerBgBitmap().getHeight()),
                                getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth(),
                                (float) (getCookerViewHeight() / 2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnFireWithoutTimerBgBitmap().getHeight() * 0.48));


                        fontMetrics = mPaint.getFontMetrics();
                        top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                        bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                        baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                        canvas.drawText(getUpTempValue().length() <= 2?(" " + getUpTempValue()  + "º"): " " + getUpTempValue() + "º",mViewRect.centerX(),baseLineY, mPaint);//º

                    }


                }else if(upBlinkFlag == BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_TWO) {
                    handler.removeMessages(HANDLER_SEPERATE_UP_BLINK);
                    if (upCurrentBlinkOptions == BLINK_OPTIONS_ONE) {//ready to cook
                        canvas.drawBitmap(getSeperateReadyToCookBgBitmap(),0,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperateReadyToCookBgBitmap().getHeight()),null);

                        //canvas.drawBitmap(getSeperateReadyToCookBgBitmap(),0,(getCookerViewHeight()/2 - getUnionReadyToCookBgBitmap().getHeight()/2),null);
                        canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperateReadyToCookBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperateReadyToCookBgBitmap().getHeight()),null);

                        mPaint.setTextAlign(Paint.Align.CENTER);
                        mPaint.setTypeface(tempGearTypeface);
                        mPaint.setTextSize(getSeperateReadyToCookFontSize());
                        baseLineY = getUpReadyToCookY();
                        if (!readyToCookTextMarqueeNeeded) {
                            mPaint.setTextAlign(Paint.Align.CENTER);
                            canvas.drawText(readyToCookText, mViewRect.centerX(), baseLineY, mPaint);
                        } else {
                            mPaint.setTextAlign(Paint.Align.LEFT);
                            readyToCookMarquee.marquee(canvas, mPaint);
                        }

                        handler.sendEmptyMessageDelayed(HANDLER_SEPERATE_UP_BLINK,BLINK_TIME_FOR_READY_TO_COOK);
                    }else {//value
                        if (readyToCookTextMarqueeNeeded) {
                            readyToCookMarquee.reset(-24);
                        }
                        canvas.drawBitmap(getSeperateReadyToCookValueBgBitmap(),0,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperateReadyToCookValueBgBitmap().getHeight()),null);

                        //canvas.drawBitmap(getSeperateReadyToCookValueBgBitmap(),0,(getCookerViewHeight()/2 - getUnionReadyToCookValueBgBitmap().getHeight()/2),null);
                        canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperateReadyToCookBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperateReadyToCookBgBitmap().getHeight()),null);

                        mPaint.setTextAlign(Paint.Align.CENTER);
                        mPaint.setTypeface(fireGearTypeface);
                        mPaint.setTextSize(getSeperateUpReadyToCookValueFontSize());//140
                        mPaint.setFakeBoldText(true);
                        // mViewRect.set(getUnionReadyToCookValueBgBitmap().getWidth() / 20, (float) (getCookerViewHeight() / 2 - getUnionReadyToCookValueBgBitmap().getHeight() * 0.4), getUnionReadyToCookValueBgBitmap().getWidth(), getCookerViewHeight() / 2);

                        mViewRect.set(
                                30,
                                (getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnFireWithoutTimerBgBitmap().getHeight()),
                                getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth(),
                                (float) (getCookerViewHeight() / 2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnFireWithoutTimerBgBitmap().getHeight() * 0.48));


                        fontMetrics = mPaint.getFontMetrics();
                        top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                        bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                        baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                        canvas.drawText(getUpTempValue().length() <= 2?(" " + getUpTempValue()  + "º"): " " + getUpTempValue() + "º",mViewRect.centerX(),baseLineY, mPaint);//º

                        handler.sendEmptyMessageDelayed(HANDLER_SEPERATE_UP_BLINK,BLINK_TIME_FOR_READY_TO_COOK);
                    }



                    upBlinkFlag = BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE;
                }



                break;
            case HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR:


                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawBitmap(getSeperateTempPrepareSensorBitmap(),0,0,null);
                canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperatePrepareTempSensorBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePrepareTempSensorBgBitmap().getHeight()),null);

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTypeface(fireGearTypeface);
                mPaint.setTextSize(getUpTempPreheatSettingValueFontSize());//140
                mPaint.setFakeBoldText(true);//getSeperateTempGearWithTimerBitmap
                mViewRect.set(
                        getSeperateTempPrepareSensorBitmap().getWidth() / 20,
                        (float) (getCookerViewHeight() / 2 - getSeperateTempPrepareSensorBitmap().getHeight() * 0.85),//0.4
                        getSeperateTempPrepareSensorBitmap().getWidth(),
                        getCookerViewHeight() / 6);
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getUpTempValue().length() <= 2?(" " + getUpTempValue()  + "º"): " " + getUpTempValue() + "º",mViewRect.centerX(),baseLineY, mPaint);//º

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTypeface(tfHelvetica57Condensed);
                mPaint.setTextSize(getUpTempPreheatRealValueFontSize());
                mPaint.setFakeBoldText(false);//getSeperateTempGearWithTimerBitmap
                mViewRect.set(
                        getSeperateTempPrepareSensorBitmap().getWidth() / 20,
                        (float) (getCookerViewHeight() / 2 - getSeperateTempPrepareSensorBitmap().getHeight() * 0.4),
                        getSeperateTempPrepareSensorBitmap().getWidth(),
                        (float) (getCookerViewHeight() * 0.43));//1.23 getUpRealTempTextGap()
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getUpRealTempValue() + "º",mViewRect.centerX(),baseLineY, mPaint);//º


                mCenterPoint.x = (int) (getSeperateTempPrepareSensorBitmap().getWidth() * 0.525); //0.503
                mCenterPoint.y = (int) (getCookerViewHeight() * 0.34);//0.16 getUpRealTempArcGap()
                mRectF.left = mCenterPoint.x - getSeperateRealTempProgressRadius() - getSeperateRealTempProgressArcWidth() / 2;
                mRectF.top = mCenterPoint.y - getSeperateRealTempProgressRadius() - getSeperateRealTempProgressArcWidth() / 2;
                mRectF.right = mCenterPoint.x + getSeperateRealTempProgressRadius() + getSeperateRealTempProgressArcWidth() / 2;
                mRectF.bottom = mCenterPoint.y + getSeperateRealTempProgressRadius() + getSeperateRealTempProgressArcWidth() / 2;
                mArcPaint.setStrokeWidth(getSeperateRealTempProgressArcWidth());
                canvas.drawArc(mRectF, -90, getUpSweepAngle(), false, mArcPaint);


                break;
            case HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR_WITH_INDICATOR:
                canvas.drawBitmap(getSeperatePrepareTempSensorWithIndicatorBgBitmap(),0,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePrepareTempSensorWithIndicatorBgBitmap().getHeight()),null);
                canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperatePrepareTempSensorWithIndicatorBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePrepareTempSensorWithIndicatorBgBitmap().getHeight()),null);

                canvas.drawBitmap(getSeperateUpTempIndicatorWithTimerBitmap(),getSeperatePrepareTempSensorWithIndicatorBgBitmap().getWidth() / 2 - getSeperateUpTempIndicatorWithTimerBitmap().getWidth() / 2, (float) (getCookerViewHeight() / 10),null);

                break;
            case HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP:
                canvas.drawBitmap(getSeperateHighTempBgBitmap(),0,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperateHighTempBgBitmap().getHeight()),null);

                break;
            case HOB_VIEW_WORK_MODE_ABNORMAL_ERROR_OCURR:
                canvas.drawBitmap(getSeperateErrorOccurBgBitmap(),0,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperateErrorOccurBgBitmap().getHeight()),null);

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTextSize(getSeperateErrorMessageFontSize());
                mPaint.setTypeface(fireGearTypeface);
                mViewRect.set(
                        0,
                        (getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperateErrorOccurBgBitmap().getHeight()),
                        getSeperateErrorOccurBgBitmap().getWidth(),
                        (float) (getCookerViewHeight() / 2 - HOB_RETANGLE_VIEW_GAP / 2 ));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getUpErrorMessage(),mViewRect.centerX(),baseLineY, mPaint);
                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_FIRST:
                if (tempWorkRecipesProgressForSeperateUp ==TEMP_WORK_RECIPES_PROGRESS_PICTURE) {
                    canvas.drawBitmap(getSeperateRecipesBgBitmap(),0,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperateRecipesBgBitmap().getHeight()),null);
                    canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperateRecipesBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperateRecipesBgBitmap().getHeight()),null);
                    canvas.drawBitmap(getUpRecipesPicBitmap(),15,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperateRecipesBgBitmap().getHeight()) + (getSeperateRecipesBgBitmap().getHeight() - getUpRecipesPicBitmap().getHeight()) / 2,null);

                    if (!handler.hasMessages(HANDLER_FLASH_RECIPES_FOR_SEPERATE_UP)) {
                        handler.sendEmptyMessageDelayed(HANDLER_FLASH_RECIPES_FOR_SEPERATE_UP,TIME_SHOW_RECIPES_PICTURE);
                    }

                }else if (tempWorkRecipesProgressForSeperateUp ==TEMP_WORK_RECIPES_PROGRESS_TEMP_VALUE) {
                    canvas.drawBitmap(getSeperatePowerOnTempWithTimerBgBitmap(),0,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnTempWithTimerBgBitmap().getHeight()),null);
                    canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperatePowerOnTempWithTimerBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnTempWithTimerBgBitmap().getHeight()),null);

                    mPaint.setTextAlign(Paint.Align.CENTER);
                    mPaint.setTextSize(getSeperateUpTempGearWithTimerTempFontSize());
                    mPaint.setTypeface(fireGearTypeface);
                    mViewRect.set(
                            10,
                            (getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnFireWithoutTimerBgBitmap().getHeight()),
                            getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth(),
                            (float) (getCookerViewHeight() / 2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnFireWithoutTimerBgBitmap().getHeight() * 0.51));
                    fontMetrics = mPaint.getFontMetrics();
                    top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                    bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                    baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                    canvas.drawText(getUpTempValue(),mViewRect.centerX(),baseLineY, mPaint);

                    mPaint.setTextAlign(Paint.Align.CENTER);
                    mPaint.setTextSize(getSeperateFireGearWithTimerTimerFontSize());
                    mPaint.setTypeface(fireGearTypeface);
                    mViewRect.set(
                            30,
                            (getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnFireWithoutTimerBgBitmap().getHeight() / 2),
                            getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth(),
                            (float) (getCookerViewHeight() / 2 - HOB_RETANGLE_VIEW_GAP * 3 ));
                    fontMetrics = mPaint.getFontMetrics();
                    top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                    bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                    baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                    canvas.drawText(getUpTimerValue(),mViewRect.centerX(),baseLineY, mPaint);

                    if (!handler.hasMessages(HANDLER_FLASH_RECIPES_FOR_SEPERATE_UP)) {
                        handler.sendEmptyMessageDelayed(HANDLER_FLASH_RECIPES_FOR_SEPERATE_UP,TIME_SHOW_RECIPES_TEMP_VALUE);

                    }
                }


                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_SECOND:
                if (tempWorkRecipesProgressForSeperateUp ==TEMP_WORK_RECIPES_PROGRESS_PICTURE) {
                    canvas.drawBitmap(getSeperateRecipesBgBitmap(),0,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperateRecipesBgBitmap().getHeight()),null);
                    canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperateRecipesBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperateRecipesBgBitmap().getHeight()),null);
                    canvas.drawBitmap(getUpRecipesPicBitmap(),15,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperateRecipesBgBitmap().getHeight()) + (getSeperateRecipesBgBitmap().getHeight() - getUpRecipesPicBitmap().getHeight()) / 2,null);

                    if (!handler.hasMessages(HANDLER_FLASH_RECIPES_FOR_SEPERATE_UP)) {
                        handler.sendEmptyMessageDelayed(HANDLER_FLASH_RECIPES_FOR_SEPERATE_UP,TIME_SHOW_RECIPES_PICTURE);
                    }

                }else if (tempWorkRecipesProgressForSeperateUp ==TEMP_WORK_RECIPES_PROGRESS_TEMP_VALUE) {
                    canvas.drawBitmap(getSeperatePowerOnTempWithTimerBgBitmap(),0,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnTempWithTimerBgBitmap().getHeight()),null);
                    canvas.drawBitmap(getPowerOffButtonBitmap(),getSeperatePowerOnTempWithTimerBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnTempWithTimerBgBitmap().getHeight()),null);

                    mPaint.setTextAlign(Paint.Align.CENTER);
                    mPaint.setTextSize(getSeperateUpTempGearWithTimerTempFontSize());
                    mPaint.setTypeface(fireGearTypeface);
                    mViewRect.set(
                            10,
                            (getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnFireWithoutTimerBgBitmap().getHeight()),
                            getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth(),
                            (float) (getCookerViewHeight() / 2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnFireWithoutTimerBgBitmap().getHeight() * 0.51));
                    fontMetrics = mPaint.getFontMetrics();
                    top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                    bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                    baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                    canvas.drawText(getUpTempValue(),mViewRect.centerX(),baseLineY, mPaint);

                    mPaint.setTextAlign(Paint.Align.CENTER);
                    mPaint.setTextSize(getSeperateFireGearWithTimerTimerFontSize());
                    mPaint.setTypeface(fireGearTypeface);
                    mViewRect.set(
                            30,
                            (getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperatePowerOnFireWithoutTimerBgBitmap().getHeight() / 2),
                            getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth(),
                            (float) (getCookerViewHeight() / 2 - HOB_RETANGLE_VIEW_GAP * 3 ));
                    fontMetrics = mPaint.getFontMetrics();
                    top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                    bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                    baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                    canvas.drawText(getUpTimerValue(),mViewRect.centerX(),baseLineY, mPaint);

                    if (!handler.hasMessages(HANDLER_FLASH_RECIPES_FOR_SEPERATE_UP)) {
                        handler.sendEmptyMessageDelayed(HANDLER_FLASH_RECIPES_FOR_SEPERATE_UP,TIME_SHOW_RECIPES_TEMP_VALUE);

                    }
                }


                break;



        }
    }

    private void drawUnionCookerView(Canvas canvas) {
        //if (retangleHobWorkMode == HOB_RETANGLE_WORK_MODE_WORK_SEPARATE) return;
        PointF p;
        float top,bottom;
        int baseLineY;
        switch (unionWorkMode) {
            case HOB_VIEW_WORK_MODE_POWER_OFF:
                canvas.drawBitmap(getUnionPowerOffBgBitmap(),0,(getCookerViewHeight()/2 - getUnionPowerOffBgBitmap().getHeight()/2),null);

                break;
            case HOB_VIEW_WORK_MODE_FIRE_GEAR_WITHOUT_TIMER:
                canvas.drawBitmap(getUnionPowerOnFireWithoutTimerBgBitmap(),0,(getCookerViewHeight()/2 - getUnionPowerOnFireWithoutTimerBgBitmap().getHeight()/2),null);
                canvas.drawBitmap(getPowerOffButtonBitmap(),getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() + + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getUnionPowerOnFireWithoutTimerBgBitmap().getHeight() / 2),null);

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTextSize(getUnionFireGearWithoutTimerFontSize());
                mPaint.setTypeface(fireGearTypeface);
                mViewRect.set(
                        30,
                        (getCookerViewHeight()/2 - getUnionPowerOnFireWithoutTimerBgBitmap().getHeight()/2),
                        getUnionPowerOnFireWithoutTimerBgBitmap().getWidth(),
                        (float) (getCookerViewHeight() / 2 + getUnionPowerOnFireWithoutTimerBgBitmap().getHeight() * 0.25));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getUnionFireGearValue(),mViewRect.centerX(),baseLineY, mPaint);

                break;
            case HOB_VIEW_WORK_MODE_FIRE_GEAR_WITH_TIMER:
                canvas.drawBitmap(getUnionPowerOnFireWithTimerBgBitmap(),0,(getCookerViewHeight()/2 - getUnionPowerOnFireWithTimerBgBitmap().getHeight()/2),null);
                canvas.drawBitmap(getPowerOffButtonBitmap(),getUnionPowerOnFireWithTimerBgBitmap().getWidth() + + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getUnionPowerOnFireWithTimerBgBitmap().getHeight() / 2),null);

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTypeface(fireGearTypeface);
                mPaint.setTextSize(getUnionFireGearWithTimerGearFontSize());
                mViewRect.set(
                        30,
                        (getCookerViewHeight()/2 - getUnionPowerOnFireWithTimerBgBitmap().getHeight()/2),
                        getUnionPowerOnFireWithTimerBgBitmap().getWidth(),
                        (float) (getCookerViewHeight() / 2 ));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getUnionFireGearValue(),mViewRect.centerX(),baseLineY, mPaint);

                mPaint.setTextSize(getUnionFireGearWithTimerTimerFontSize());
                mViewRect.set(
                        30,
                        (getCookerViewHeight()/2 ),
                        getUnionPowerOnFireWithTimerBgBitmap().getWidth(),
                        (float) (getCookerViewHeight() / 2 + getUnionPowerOnFireWithTimerBgBitmap().getHeight() * 0.5));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getUnionTimerValue(),mViewRect.centerX(),baseLineY, mPaint);

                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITHOUT_TIMER:
                canvas.drawBitmap(getUnionPowerOnTempWithoutTimerBgBitmap(),0,(getCookerViewHeight()/2 - getUnionPowerOnTempWithoutTimerBgBitmap().getHeight()/2),null);
                canvas.drawBitmap(getPowerOffButtonBitmap(),getUnionPowerOnTempWithoutTimerBgBitmap().getWidth() + + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getUnionPowerOnTempWithoutTimerBgBitmap().getHeight() / 2),null);

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTextSize(getUnionTempGearWithoutTimerFontSize());
                mPaint.setTypeface(fireGearTypeface);
                mViewRect.set(
                        40,
                        (getCookerViewHeight()/2 - getUnionPowerOnFireWithoutTimerBgBitmap().getHeight()/2),
                        getUnionPowerOnFireWithoutTimerBgBitmap().getWidth(),
                        (float) (getCookerViewHeight() / 2 + getUnionPowerOnFireWithoutTimerBgBitmap().getHeight() * 0.28));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getUnionTempGearValue().length() <= 2?(" " + getUnionTempGearValue()  + "º"): " " + getUnionTempGearValue() + "º",mViewRect.centerX(),baseLineY, mPaint);

                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER:
                canvas.drawBitmap(getUnionPowerOnTempWithTimerBgBitmap(),0,(getCookerViewHeight()/2 - getUnionPowerOnTempWithTimerBgBitmap().getHeight()/2),null);
                canvas.drawBitmap(getPowerOffButtonBitmap(),getUnionPowerOnTempWithTimerBgBitmap().getWidth() + + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getUnionPowerOnTempWithTimerBgBitmap().getHeight() / 2),null);

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTypeface(fireGearTypeface);
                mPaint.setTextSize(getUnionTempGearWithTimerTempFontSize());
                mViewRect.set(
                        40,
                        (getCookerViewHeight()/2 - getUnionPowerOnFireWithTimerBgBitmap().getHeight()/2),
                        getUnionPowerOnFireWithTimerBgBitmap().getWidth(),
                        (float) (getCookerViewHeight() / 2 ));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getUnionTempGearValue().length() <= 2?(" " + getUnionTempGearValue()  + "º"): " " + getUnionTempGearValue() + "º",mViewRect.centerX(),baseLineY, mPaint);

                mPaint.setTextSize(getUnionTempGearWithTimerTimerFontSize());
                mPaint.setTypeface(tempGearTypeface);
                mViewRect.set(
                        30,
                        (getCookerViewHeight()/2 ),
                        getUnionPowerOnFireWithTimerBgBitmap().getWidth(),
                        (float) (getCookerViewHeight() / 2 + getUnionPowerOnFireWithTimerBgBitmap().getHeight() * 0.47));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getUnionTimerValue(),mViewRect.centerX(),baseLineY, mPaint);

                break;
            case HOB_VIEW_WORK_MODE_ABNORMAL_NO_PAN:
                canvas.drawBitmap(getUnionAbnormalNoPanBgBitmap(),0,(getCookerViewHeight()/2 - getUnionAbnormalNoPanBgBitmap().getHeight()/2),null);
                canvas.drawBitmap(getPowerOffButtonBitmap(),getUnionAbnormalNoPanBgBitmap().getWidth() + + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getUnionAbnormalNoPanBgBitmap().getHeight() / 2),null);

                break;
            case HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITHOUT_TIMER:
                canvas.drawBitmap(getUnionPowerOnTempIndicatorWithoutTimerBgBitmap(),0,(getCookerViewHeight()/2 - getUnionPowerOnTempIndicatorWithoutTimerBgBitmap().getHeight()/2),null);
                canvas.drawBitmap(getPowerOffButtonBitmap(),getUnionPowerOnTempIndicatorWithoutTimerBgBitmap().getWidth() + + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getUnionPowerOnTempIndicatorWithoutTimerBgBitmap().getHeight() / 2),null);

                canvas.drawBitmap(getUnionTempIndicatorWithoutTimerBitmap(),getUnionPowerOnTempIndicatorWithoutTimerBgBitmap().getWidth() / 2 - getUnionTempIndicatorWithoutTimerBitmap().getWidth() / 2, (float) (getCookerViewHeight() / 2 - getUnionTempIndicatorWithoutTimerBitmap().getHeight() * 0.8),null);

                break;
            case HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER:
                canvas.drawBitmap(getUnionPowerOnTempIndicatorWithTimerBgBitmap(),0,(getCookerViewHeight()/2 - getUnionPowerOnTempIndicatorWithTimerBgBitmap().getHeight()/2),null);
                canvas.drawBitmap(getPowerOffButtonBitmap(),getUnionPowerOnTempIndicatorWithTimerBgBitmap().getWidth() + + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getUnionPowerOnTempIndicatorWithTimerBgBitmap().getHeight() / 2),null);
                canvas.drawBitmap(getUnionTempIndicatorWithTimerBitmap(),getUnionPowerOnTempIndicatorWithTimerBgBitmap().getWidth() / 2 - getUnionTempIndicatorWithTimerBitmap().getWidth() / 2, (float) (getCookerViewHeight() / 6),null);

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTextSize(getUnionTempGearWithTimerTimerFontSize());
                mPaint.setTypeface(tempGearTypeface);
                mViewRect.set(
                        30,
                        (getCookerViewHeight()/2 ),
                        getUnionPowerOnFireWithTimerBgBitmap().getWidth(),
                        (float) (getCookerViewHeight() / 2 + getUnionPowerOnFireWithTimerBgBitmap().getHeight() * 0.50));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getUnionTimerValue(),mViewRect.centerX(),baseLineY, mPaint);
                break;
            case HOB_VIEW_WORK_MODE_TEMP_WORK_FINISH_WAIT_USER_CONFIRM:

                /*if (tempWorkFinishProgress == TEMP_WORK_FINISH_PROGRESS_LIGHT) {
                    canvas.drawBitmap(getUnionTempFinishLightBgBitmap(),0,(getCookerViewHeight()/2 - getUnionTempFinishLightBgBitmap().getHeight()/2),null);
                    tempWorkFinishProgress = TEMP_WORK_FINISH_PROGRESS_DARK;

                }else {
                    canvas.drawBitmap(getUnionTempFinishDarkBgBitmap(),0,(getCookerViewHeight()/2 - getUnionTempFinishDarkBgBitmap().getHeight()/2),null);
                    tempWorkFinishProgress = TEMP_WORK_FINISH_PROGRESS_LIGHT;
                }
                canvas.drawBitmap(getPowerOffButtonBitmap(),getUnionTempFinishDarkBgBitmap().getWidth() + + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getUnionTempFinishDarkBgBitmap().getHeight() / 2),null);


                mPaint.setTypeface(tempGearTypeface);
                mPaint.setTextSize(40);
                mViewRect.set(0, (float) (getCookerViewHeight() / 2 - getUnionTempFinishLightBgBitmap().getHeight() * 0.45), getUnionTempFinishLightBgBitmap().getWidth(), getCookerViewHeight()/ 2);
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText("+10min",mViewRect.centerX(),baseLineY, mPaint);

                mViewRect.set(0, (float) (getUnionTempFinishLightBgBitmap().getHeight() *0.45), getUnionTempFinishLightBgBitmap().getWidth(), getCookerViewHeight());
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText("keep Warm",mViewRect.centerX(),baseLineY, mPaint);

                if (handler.hasMessages(HANDLER_FLASH_KEEP_WARM_TIPS)) {
                    handler.removeMessages(HANDLER_FLASH_KEEP_WARM_TIPS);

                }
                handler.sendEmptyMessageDelayed(HANDLER_FLASH_KEEP_WARM_TIPS,TIME_FLASH_KEEP_WARM);*/

                mPaint.setTextAlign(Paint.Align.CENTER);
                if (unionBlinkFlag == BLINK_FLAG_HAVE_NOT_BLINK) {
                    handler.removeMessages(HANDLER_UNION_BLINK);
                    unionBlinkFlag = BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE;
                    arrow = 0;
                    light = 1.0f;
                    alpha = 255;
                    mAlphaPaint.setAlpha(alpha);
                    canvas.drawBitmap(getUnionTempWorkFinishLightBitmap(),0,(getCookerViewHeight()/2 - getUnionReadyToCookBgBitmap().getHeight()/2),mAlphaPaint);
                    handler.sendEmptyMessageDelayed(HANDLER_UNION_BLINK,30);
                }else if(unionBlinkFlag == BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE) {

                    canvas.drawBitmap(getUnionTempWorkFinishLightBitmap(),0,(getCookerViewHeight()/2 - getUnionReadyToCookBgBitmap().getHeight()/2),mAlphaPaint);

                }else if(unionBlinkFlag == BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_TWO) {
                    handler.removeMessages(HANDLER_UNION_BLINK);

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
                    canvas.drawBitmap(getUnionTempWorkFinishLightBitmap(),0,(getCookerViewHeight()/2 - getUnionReadyToCookBgBitmap().getHeight()/2),mAlphaPaint);
                    unionBlinkFlag = BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE;
                    handler.sendEmptyMessageDelayed(HANDLER_UNION_BLINK,30);
                }

                canvas.drawBitmap(getPowerOffButtonBitmap(),getUnionTempFinishDarkBgBitmap().getWidth() + + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getUnionTempFinishDarkBgBitmap().getHeight() / 2),null);

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTypeface(tempGearTypeface);
                mPaint.setAlpha(alpha);
                mPaint.setTextSize(getUnionReadyToCookFontSize());
                mViewRect.set(0,
                        (float) (getCookerViewHeight() / 2 - getUnionTempWorkFinishLightBitmap().getHeight() * 0.4),
                        getUnionTempWorkFinishLightBitmap().getWidth(),
                        (float) (getCookerViewHeight()* 0.4));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("+10min",mViewRect.centerX(),baseLineY, mPaint);

                mViewRect.set(0,
                        (float) (getUnionTempWorkFinishLightBitmap().getHeight() *0.4),
                        getUnionTempWorkFinishLightBitmap().getWidth(),
                        (float) (getCookerViewHeight() * 1.05));
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
                    if (rect.width() > getUnionInnerTextMaxWidth()) {
                        keepWarmTextMarqueeNeeded = true;
                        keepWarmMarquee = new Marquee(
                                keepWarmText,
                                new RectF(
                                        mViewRect.centerX() - getUnionInnerTextMaxWidth() / 2,
                                        mViewRect.top,
                                        mViewRect.centerX() + getUnionInnerTextMaxWidth() / 2,
                                        mViewRect.bottom
                                ),
                                new PointF(
                                        mViewRect.centerX() - getUnionInnerTextMaxWidth() / 2,
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
                }




                break;
            case HOB_VIEW_WORK_MODE_TEMP_SENSOR_READY:

                mPaint.setTextAlign(Paint.Align.CENTER);
                if (readyToCookText == null) {
                    readyToCookText = getContext().getResources().getString(R.string.viewmodule_cooker_view_ready_to_cook);
                    readyToCookTextChanged = true;
                }

                if (readyToCookTextChanged) {
                    Rect rect = getStringRect(readyToCookText);
                    if (rect.width() > getUnionInnerTextMaxWidthBig()) {
                        readyToCookTextMarqueeNeeded = true;
                        baseLineY = getReadyToCookY();
                        readyToCookMarquee = new Marquee(
                                readyToCookText,
                                new RectF(
                                        mViewRect.centerX() - getUnionInnerTextMaxWidthBig() / 2,
                                        mViewRect.top,
                                        mViewRect.centerX() + getUnionInnerTextMaxWidthBig() / 2,
                                        mViewRect.bottom
                                ),
                                new PointF(
                                        mViewRect.centerX() - getUnionInnerTextMaxWidthBig() / 2,
                                        baseLineY
                                ),
                                4
                        );
                    } else {
                        readyToCookTextMarqueeNeeded = false;
                    }
                    readyToCookTextChanged = false;
                }

                if (unionBlinkFlag == BLINK_FLAG_HAVE_NOT_BLINK) {
                    handler.removeMessages(HANDLER_UNION_BLINK);
                    unionCurrentBlinkOptions = BLINK_OPTIONS_ONE;
                    unionBlinkFlag = BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE;
                    canvas.drawBitmap(getUnionReadyToCookBgBitmap(),0,(getCookerViewHeight()/2 - getUnionReadyToCookBgBitmap().getHeight()/2),null);
                    canvas.drawBitmap(getPowerOffButtonBitmap(),getUnionReadyToCookBgBitmap().getWidth() + + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getUnionReadyToCookBgBitmap().getHeight() / 2),null);


                    mPaint.setTypeface(tempGearTypeface);
                    mPaint.setTextSize(getUnionReadyToCookFontSize());
                    baseLineY = getReadyToCookY();
                    if (!readyToCookTextMarqueeNeeded) {
                        mPaint.setTextAlign(Paint.Align.CENTER);
                        canvas.drawText(readyToCookText, mViewRect.centerX(), baseLineY, mPaint);
                    } else {
                        mPaint.setTextAlign(Paint.Align.LEFT);
                        readyToCookMarquee.marquee(canvas, mPaint);
                    }

                    handler.sendEmptyMessageDelayed(HANDLER_UNION_BLINK,BLINK_TIME_FOR_READY_TO_COOK);


                }else if(unionBlinkFlag == BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE) {
                    if (unionCurrentBlinkOptions == BLINK_OPTIONS_ONE) {//ready to cook
                        canvas.drawBitmap(getUnionReadyToCookBgBitmap(),0,(getCookerViewHeight()/2 - getUnionReadyToCookBgBitmap().getHeight()/2),null);
                        canvas.drawBitmap(getPowerOffButtonBitmap(),getUnionReadyToCookBgBitmap().getWidth() + + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getUnionReadyToCookBgBitmap().getHeight() / 2),null);


                        mPaint.setTypeface(tempGearTypeface);
                        mPaint.setTextSize(getUnionReadyToCookFontSize());
                        baseLineY = getReadyToCookY();
                        if (!readyToCookTextMarqueeNeeded) {
                            mPaint.setTextAlign(Paint.Align.CENTER);
                            canvas.drawText(readyToCookText, mViewRect.centerX(), baseLineY, mPaint);
                        } else {
                            mPaint.setTextAlign(Paint.Align.LEFT);
                            readyToCookMarquee.marquee(canvas, mPaint);
                        }

                    }else {//value
                        if (readyToCookTextMarqueeNeeded) {
                            readyToCookMarquee.reset(-24);
                        }
                        canvas.drawBitmap(getUnionReadyToCookValueBgBitmap(),0,(getCookerViewHeight()/2 - getUnionReadyToCookValueBgBitmap().getHeight()/2),null);
                        canvas.drawBitmap(getPowerOffButtonBitmap(),getUnionReadyToCookBgBitmap().getWidth() + + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getUnionReadyToCookBgBitmap().getHeight() / 2),null);

                        mPaint.setTextAlign(Paint.Align.CENTER);
                        mPaint.setTypeface(fireGearTypeface);
                        mPaint.setTextSize(getUnionReadyToCookValueFontSize());//140
                        mPaint.setFakeBoldText(true);
                    /*    mViewRect.set(
                                getUnionReadyToCookValueBgBitmap().getWidth() / 20,
                                (float) (getCookerViewHeight() / 2 - getUnionReadyToCookValueBgBitmap().getHeight() * 0.4),
                                getUnionReadyToCookValueBgBitmap().getWidth(),
                                getCookerViewHeight() / 2);*/

                        mViewRect.set(
                                40,
                                (getCookerViewHeight()/2 - getUnionPowerOnFireWithTimerBgBitmap().getHeight()/2),
                                getUnionPowerOnFireWithTimerBgBitmap().getWidth(),
                                (float) (getCookerViewHeight() * 0.52 ));

                        fontMetrics = mPaint.getFontMetrics();
                        top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                        bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                        baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                        canvas.drawText(getUnionTempGearValue().length() <= 2?(" " + getUnionTempGearValue()  + "º"): " " + getUnionTempGearValue() + "º",mViewRect.centerX(),baseLineY, mPaint);//º

                    }


                }else if(unionBlinkFlag == BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_TWO) {
                    handler.removeMessages(HANDLER_UNION_BLINK);
                    if (unionCurrentBlinkOptions == BLINK_OPTIONS_ONE) {//ready to cook
                        canvas.drawBitmap(getUnionReadyToCookBgBitmap(),0,(getCookerViewHeight()/2 - getUnionReadyToCookBgBitmap().getHeight()/2),null);
                        canvas.drawBitmap(getPowerOffButtonBitmap(),getUnionReadyToCookBgBitmap().getWidth() + + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getUnionReadyToCookBgBitmap().getHeight() / 2),null);

                        mPaint.setTypeface(tempGearTypeface);
                        mPaint.setTextSize(getUnionReadyToCookFontSize());
                        baseLineY = getReadyToCookY();
                        if (!readyToCookTextMarqueeNeeded) {
                            mPaint.setTextAlign(Paint.Align.CENTER);
                            canvas.drawText(readyToCookText, mViewRect.centerX(), baseLineY, mPaint);
                        } else {
                            mPaint.setTextAlign(Paint.Align.LEFT);
                            readyToCookMarquee.marquee(canvas, mPaint);
                        }

                        handler.sendEmptyMessageDelayed(HANDLER_UNION_BLINK,BLINK_TIME_FOR_READY_TO_COOK);
                    }else {//value
                        if (readyToCookTextMarqueeNeeded) {
                            readyToCookMarquee.reset(-24);
                        }
                        canvas.drawBitmap(getUnionReadyToCookValueBgBitmap(),0,(getCookerViewHeight()/2 - getUnionReadyToCookValueBgBitmap().getHeight()/2),null);
                        canvas.drawBitmap(getPowerOffButtonBitmap(),getUnionReadyToCookBgBitmap().getWidth() + + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getUnionReadyToCookBgBitmap().getHeight() / 2),null);

                        mPaint.setTextAlign(Paint.Align.CENTER);
                        mPaint.setTypeface(fireGearTypeface);
                        mPaint.setTextSize(getUnionReadyToCookValueFontSize());//140
                        mPaint.setFakeBoldText(true);
                        // mViewRect.set(getUnionReadyToCookValueBgBitmap().getWidth() / 20, (float) (getCookerViewHeight() / 2 - getUnionReadyToCookValueBgBitmap().getHeight() * 0.4), getUnionReadyToCookValueBgBitmap().getWidth(), getCookerViewHeight() / 2);

                        mViewRect.set(
                                40,
                                (getCookerViewHeight()/2 - getUnionPowerOnFireWithTimerBgBitmap().getHeight()/2),
                                getUnionPowerOnFireWithTimerBgBitmap().getWidth(),
                                (float) (getCookerViewHeight() * 0.52 ));

                        fontMetrics = mPaint.getFontMetrics();
                        top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                        bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                        baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                        canvas.drawText(getUnionTempGearValue().length() <= 2?(" " + getUnionTempGearValue()  + "º"): " " + getUnionTempGearValue() + "º",mViewRect.centerX(),baseLineY, mPaint);//º

                        handler.sendEmptyMessageDelayed(HANDLER_UNION_BLINK,BLINK_TIME_FOR_READY_TO_COOK);
                    }



                    unionBlinkFlag = BLINK_FLAG_HAVE_BLINK_AUTO_BLINK_ONE;
                }



                break;
            case HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR:
                LogUtil.d("Enter:: HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR---->" + getUnionSweepAngle());
                canvas.drawBitmap(getUnionPrepareTempSensorBgBitmap(),0,(getCookerViewHeight()/2 - getUnionPrepareTempSensorBgBitmap().getHeight()/2),null);
                canvas.drawBitmap(getPowerOffButtonBitmap(),getUnionPrepareTempSensorBgBitmap().getWidth() + + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getUnionPrepareTempSensorBgBitmap().getHeight() / 2),null);

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTypeface(fireGearTypeface);
                mPaint.setTextSize(getUnionTempPreheatSettingValueFontSize());//140
                mPaint.setFakeBoldText(true);//getSeperateTempGearWithTimerBitmap
                mViewRect.set(
                        getUnionPrepareTempSensorBgBitmap().getWidth() / 8,
                        (float) (getCookerViewHeight() / 2 - getUnionPrepareTempSensorBgBitmap().getHeight() * 0.85),//0.4
                        getUnionPrepareTempSensorBgBitmap().getWidth(),
                        (float) (getCookerViewHeight() * 1.14));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getUnionTempGearValue().length() <= 2?(" " + getUnionTempGearValue()  + "º"): " " + getUnionTempGearValue() + "º",mViewRect.centerX(),baseLineY, mPaint);//º

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTypeface(tfHelvetica57Condensed);
                mPaint.setTextSize(getUnionTempPreheatRealValueFontSize());
                mPaint.setFakeBoldText(false);//getSeperateTempGearWithTimerBitmap
                mViewRect.set(
                        getUnionPrepareTempSensorBgBitmap().getWidth() / 20,
                        (float) (getCookerViewHeight() / 2 - getUnionPrepareTempSensorBgBitmap().getHeight() * 0.4),
                        getUnionPrepareTempSensorBgBitmap().getWidth(),
                        (float) (getCookerViewHeight() * 1.15));//1.23 getUpRealTempTextGap()
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getUnionRealTempValue() + "º",mViewRect.centerX(),baseLineY, mPaint);//º


                mCenterPoint.x = (int) (getUnionPrepareTempSensorBgBitmap().getWidth() * 0.525); //0.503
                mCenterPoint.y = (int) (getCookerViewHeight() * 0.59);//0.16 getUpRealTempArcGap()
                mRectF.left = mCenterPoint.x - getUnionRealTempProgressRadius() - getUnionRealTempProgressArcWidth() / 2;
                mRectF.top = mCenterPoint.y - getUnionRealTempProgressRadius() - getUnionRealTempProgressArcWidth() / 2;
                mRectF.right = mCenterPoint.x + getUnionRealTempProgressRadius() + getUnionRealTempProgressArcWidth() / 2;
                mRectF.bottom = mCenterPoint.y + getUnionRealTempProgressRadius() + getUnionRealTempProgressArcWidth() / 2;
                mArcPaint.setStrokeWidth(getUnionRealTempProgressArcWidth());
                canvas.drawArc(mRectF, -90, getUnionSweepAngle(), false, mArcPaint);



                break;
            case HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR_WITH_INDICATOR:
                canvas.drawBitmap(getUnionPrepareTempSensorWithIndicatorBgBitmap(),0,(getCookerViewHeight()/2 - getUnionPrepareTempSensorWithIndicatorBgBitmap().getHeight()/2),null);
                canvas.drawBitmap(getPowerOffButtonBitmap(),getUnionPrepareTempSensorWithIndicatorBgBitmap().getWidth() + + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getUnionPrepareTempSensorBgBitmap().getHeight() / 2),null);

                canvas.drawBitmap(getUnionTempIndicatorWithTimerBitmap(),getUnionPrepareTempSensorWithIndicatorBgBitmap().getWidth() / 2 - getUnionTempIndicatorWithTimerBitmap().getWidth() / 2, (float) (getCookerViewHeight() / 6),null);

                break;
            case HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP:
                canvas.drawBitmap(getUnionHighTempBgBitmap(),0,(getCookerViewHeight()/2 - getUnionHighTempBgBitmap().getHeight()/2),null);

                break;
            case HOB_VIEW_WORK_MODE_ABNORMAL_ERROR_OCURR:
                canvas.drawBitmap(getUnionErrorOccurBgBitmap(),0,(getCookerViewHeight()/2 - getUnionErrorOccurBgBitmap().getHeight()/2),null);

                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTypeface(fireGearTypeface);
                mPaint.setTextSize(getUnionErrorMessageFontSize());
                mViewRect.set(
                        0,
                        (getCookerViewHeight()/2 - getUnionErrorOccurBgBitmap().getHeight()/2),
                        getUnionErrorOccurBgBitmap().getWidth(),
                        (float) (getCookerViewHeight() / 2  + getUnionErrorOccurBgBitmap().getHeight()/2));
                fontMetrics = mPaint.getFontMetrics();
                top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                canvas.drawText(getUnionErrorMessage(),mViewRect.centerX(),baseLineY, mPaint);
                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_FIRST:
                if (tempWorkRecipesProgressForUnion ==TEMP_WORK_RECIPES_PROGRESS_PICTURE) {

                    canvas.drawBitmap(getUnionRecipesBgBitmap(),0,(getCookerViewHeight()/2 - getUnionRecipesBgBitmap().getHeight()/2),null);
                    canvas.drawBitmap(getPowerOffButtonBitmap(),getUnionRecipesBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getUnionRecipesBgBitmap().getHeight() / 2),null);
                    canvas.drawBitmap(getUnionRecipesPicBitmap(),15,getCookerViewHeight() / 2 - getUnionRecipesBgBitmap().getHeight() / 2 + (getUnionRecipesBgBitmap().getHeight() / 2 - getUnionRecipesPicBitmap().getHeight() ) / 2,null);
                    canvas.drawBitmap(getUnionRecipesPicBitmap(),15, getCookerViewHeight() / 2 + (getUnionRecipesBgBitmap().getHeight() / 2 - getUnionRecipesPicBitmap().getHeight() ) / 2,null);

                    if (!handler.hasMessages(HANDLER_FLASH_RECIPES_FOR_UNION)) {
                        handler.sendEmptyMessageDelayed(HANDLER_FLASH_RECIPES_FOR_UNION,TIME_SHOW_RECIPES_PICTURE);
                    }

                }else if(tempWorkRecipesProgressForUnion ==TEMP_WORK_RECIPES_PROGRESS_TEMP_VALUE) {
                    canvas.drawBitmap(getUnionPowerOnTempWithTimerBgBitmap(),0,(getCookerViewHeight()/2 - getUnionPowerOnTempWithTimerBgBitmap().getHeight()/2),null);
                    canvas.drawBitmap(getPowerOffButtonBitmap(),getUnionPowerOnTempWithTimerBgBitmap().getWidth() + + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getUnionPowerOnTempWithTimerBgBitmap().getHeight() / 2),null);

                    mPaint.setTextAlign(Paint.Align.CENTER);
                    mPaint.setTypeface(fireGearTypeface);
                    mPaint.setTextSize(getUnionTempGearWithTimerTempFontSize());
                    mViewRect.set(
                            10,
                            (getCookerViewHeight()/2 - getUnionPowerOnFireWithTimerBgBitmap().getHeight()/2),
                            getUnionPowerOnFireWithTimerBgBitmap().getWidth(),
                            (float) (getCookerViewHeight() / 2  - HOB_RETANGLE_VIEW_GAP * 6));
                    fontMetrics = mPaint.getFontMetrics();
                    top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                    bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                    baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                    canvas.drawText(getUnionTempGearValue(),mViewRect.centerX(),baseLineY, mPaint);

                    mPaint.setTextSize(getUnionTempGearWithTimerTimerFontSize());
                    mPaint.setTypeface(tempGearTypeface);
                    mViewRect.set(
                            40,
                            (getCookerViewHeight()/2 ),
                            getUnionPowerOnFireWithTimerBgBitmap().getWidth(),
                            (float) (getCookerViewHeight() / 2 + getUnionPowerOnFireWithTimerBgBitmap().getHeight() * 0.47));
                    fontMetrics = mPaint.getFontMetrics();
                    top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                    bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                    baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                    canvas.drawText(getUnionTimerValue(),mViewRect.centerX(),baseLineY, mPaint);
                    if (!handler.hasMessages(HANDLER_FLASH_RECIPES_FOR_UNION)) {
                        handler.sendEmptyMessageDelayed(HANDLER_FLASH_RECIPES_FOR_UNION,TIME_SHOW_RECIPES_TEMP_VALUE);

                    }
                }

                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_SECOND:
                if (tempWorkRecipesProgressForUnion ==TEMP_WORK_RECIPES_PROGRESS_PICTURE) {

                    canvas.drawBitmap(getUnionRecipesBgBitmap(),0,(getCookerViewHeight()/2 - getUnionRecipesBgBitmap().getHeight()/2),null);
                    canvas.drawBitmap(getPowerOffButtonBitmap(),getUnionRecipesBgBitmap().getWidth() + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getUnionRecipesBgBitmap().getHeight() / 2),null);
                    canvas.drawBitmap(getUnionRecipesPicBitmap(),15,getCookerViewHeight() / 2 - getUnionRecipesBgBitmap().getHeight() / 2 + (getUnionRecipesBgBitmap().getHeight() / 2 - getUnionRecipesPicBitmap().getHeight() ) / 2,null);
                    canvas.drawBitmap(getUnionRecipesPicBitmap(),15, getCookerViewHeight() / 2 + (getUnionRecipesBgBitmap().getHeight() / 2 - getUnionRecipesPicBitmap().getHeight() ) / 2,null);

                    if (!handler.hasMessages(HANDLER_FLASH_RECIPES_FOR_UNION)) {
                        handler.sendEmptyMessageDelayed(HANDLER_FLASH_RECIPES_FOR_UNION,TIME_SHOW_RECIPES_PICTURE);
                    }

                }else if(tempWorkRecipesProgressForUnion ==TEMP_WORK_RECIPES_PROGRESS_TEMP_VALUE) {
                    canvas.drawBitmap(getUnionPowerOnTempWithTimerBgBitmap(),0,(getCookerViewHeight()/2 - getUnionPowerOnTempWithTimerBgBitmap().getHeight()/2),null);
                    canvas.drawBitmap(getPowerOffButtonBitmap(),getUnionPowerOnTempWithTimerBgBitmap().getWidth() + + HOB_RETANGLE_VIEW_GAP_COOKER_POWER_OFF_BUTTON ,(getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getUnionPowerOnTempWithTimerBgBitmap().getHeight() / 2),null);

                    mPaint.setTextAlign(Paint.Align.CENTER);
                    mPaint.setTypeface(fireGearTypeface);
                    mPaint.setTextSize(getUnionTempGearWithTimerTempFontSize());
                    mViewRect.set(
                            10,
                            (getCookerViewHeight()/2 - getUnionPowerOnFireWithTimerBgBitmap().getHeight()/2),
                            getUnionPowerOnFireWithTimerBgBitmap().getWidth(),
                            (float) (getCookerViewHeight() / 2  - HOB_RETANGLE_VIEW_GAP * 6));
                    fontMetrics = mPaint.getFontMetrics();
                    top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                    bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                    baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                    canvas.drawText(getUnionTempGearValue(),mViewRect.centerX(),baseLineY, mPaint);

                    mPaint.setTextAlign(Paint.Align.CENTER);
                    mPaint.setTextSize(getUnionTempGearWithTimerTimerFontSize());
                    mPaint.setTypeface(tempGearTypeface);
                    mViewRect.set(
                            40,
                            (getCookerViewHeight()/2 ),
                            getUnionPowerOnFireWithTimerBgBitmap().getWidth(),
                            (float) (getCookerViewHeight() / 2 + getUnionPowerOnFireWithTimerBgBitmap().getHeight() * 0.47));
                    fontMetrics = mPaint.getFontMetrics();
                    top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                    bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                    baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
                    canvas.drawText(getUnionTimerValue(),mViewRect.centerX(),baseLineY, mPaint);
                    if (!handler.hasMessages(HANDLER_FLASH_RECIPES_FOR_UNION)) {
                        handler.sendEmptyMessageDelayed(HANDLER_FLASH_RECIPES_FOR_UNION,TIME_SHOW_RECIPES_TEMP_VALUE);

                    }
                }
                break;
        }

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

        float centerY = getCookerViewHeight() / 2.0f;

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:  // 第一个点被按下
                click2Detected = false;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:// 非第一个点被按下
                break;
            case MotionEvent.ACTION_POINTER_UP:  // 非最后一个点被释放
                if (event.getPointerCount() == 2) {
                    if ((event.getY(0) < centerY && event.getY(1) > centerY)
                            || (event.getY(0) > centerY && event.getY(1) < centerY)) {
                        click2Detected = true;
                        firstFingerTouch = event.getY(1) > centerY ? 1 : 0;
                    }
                }
                break;
            case MotionEvent.ACTION_UP: // 最后一个点被释放
                if (ViewUtils.isShade(this)) {
                    return false;
                }
                if (click2Detected) {
                    click2Detected = false;
                    if ((firstFingerTouch == 0 && event.getY() > centerY) || (firstFingerTouch == 1 && event.getY() < centerY)) {
                        doUnion();
                        return true;
                    }
                }
                break;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDownX = event.getX();
                touchDownY = event.getY();
                LogUtil.d("--------ACTION_DOWN----------------");
                return true;

            case MotionEvent.ACTION_MOVE:
                //LogUtil.d("--------ACTION_MOVE----------------");
                //if (Math.abs(touchDownX - event.getX()) >= ViewConfiguration.get(getContext()).getScaledTouchSlop()||Math.abs(touchDownY - event.getY()) >= ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                if (Math.abs(touchDownX - event.getX()) >= ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    // LogUtil.d("--------ACTION_MOVE-----------1-----");
                    return false;
                } else {
                    return true;
                }

            case MotionEvent.ACTION_UP:
                LogUtil.d("--------ACTION_up----------------");
                if (ViewUtils.isShade(this)) {
                    return false;
                }

                //LogUtil.d("event.getY()--->" + event.getY());
                // LogUtil.d("touchDownY--->" + touchDownY);
                // LogUtil.d("getCookerViewHeight() / 2--->" + getCookerViewHeight() / 2);
                //切换炉头模式
                boolean swipeDetected = (touchDownY < centerY && event.getY() > centerY)
                        || (touchDownY > centerY && event.getY() < centerY);
                swipeDetected = swipeDetected && (Math.abs(event.getY() - touchDownY) > DisplayUtil.dp2px(getContext(),100));
                /* */
                if (swipeDetected
                    // && unionWorkMode == HOB_VIEW_WORK_MODE_POWER_OFF
                ) {
                    LogUtil.d("samhung--switch hob work mode-------->" + downWorkMode);
                    LogUtil.d("samhung--switch hob work mode-------->" + upWorkMode);
                    doUnion();

                    LogUtil.d("Enter::  y -----100");

                    return true;
                }
                //处理点击事件
                if (Math.abs(event.getX() - touchDownX) > DisplayUtil.dp2px(getContext(),38) ||
                        Math.abs(event.getY() - touchDownY) > DisplayUtil.dp2px(getContext(),38)
                ) {
                    return false;
                }else {
                    LogUtil.d("process click event--retangleHobWorkMode------>" + retangleHobWorkMode);
                    if (retangleHobWorkMode == HOB_RETANGLE_WORK_MODE_WORK_UNITE) {
                        processUniteClickEvent(event);

                    }else if (retangleHobWorkMode == HOB_RETANGLE_WORK_MODE_WORK_SEPARATE){

                        if (event.getY() < getCookerViewHeight() / 2) {
                            LogUtil.d("---------up------------------");
                        }else if (event.getY() > getCookerViewHeight() / 2){
                            LogUtil.d("---------down------------------");
                        }

                        processSepareteClickEvent(event);

                    }

                    return true;
                }

        }
        return super.onTouchEvent(event);
    }

    private void processUniteClickEvent(MotionEvent event) {
        float touchUpX = event.getX();
        float touchUpY = event.getY();
        float x1,y1;
        switch (unionWorkMode) {
            case HOB_VIEW_WORK_MODE_POWER_OFF:
                //mListener.onCookerPowerOff(unionCookerID);
                break;
            case HOB_VIEW_WORK_MODE_FIRE_GEAR_WITHOUT_TIMER:
                x1 = getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() / 2;
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < y1) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    doUnitePowerOff();
                }else if(touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < getCookerViewHeight() * 0.7){
                    // LogUtil.d("Enter:: samhung unite set gear");
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onSetGear(unionCookerID);
                }else if (touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > getCookerViewHeight() * 0.7) {
                    // LogUtil.d("Enter:: samhung unite set timer");
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onSetTimer(unionCookerID);
                }


                break;
            case HOB_VIEW_WORK_MODE_FIRE_GEAR_WITH_TIMER:
                x1 = getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() / 2;
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < y1) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    doUnitePowerOff();
                }else if(touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < getCookerViewHeight() * 0.5){
                    //LogUtil.d("Enter:: samhung unite set gear");
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onSetGear(unionCookerID);
                }else if (touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > getCookerViewHeight() * 0.5) {
                    //LogUtil.d("Enter:: samhung unite set timer");
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onSetTimer(unionCookerID);
                }

                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITHOUT_TIMER:
                x1 = getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() / 2;
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < y1) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    doUnitePowerOff();
                }else if(touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < getCookerViewHeight() * 0.7){
                    // LogUtil.d("Enter:: samhung unite set gear");
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onSetGear(unionCookerID);
                }else if (touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > getCookerViewHeight() * 0.7) {
                    // LogUtil.d("Enter:: samhung unite set timer");
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onSetTimer(unionCookerID);
                }
                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER:
                x1 = getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() / 2;
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < y1) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    doUnitePowerOff();
                }else if(touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < getCookerViewHeight() * 0.5){
                    //LogUtil.d("Enter:: samhung unite set gear");
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onSetGear(unionCookerID);
                }else if (touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > getCookerViewHeight() * 0.5) {
                    //LogUtil.d("Enter:: samhung unite set timer");
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onSetTimer(unionCookerID);
                }
                break;
            case HOB_VIEW_WORK_MODE_ABNORMAL_NO_PAN:
                x1 = getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() / 2;
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < y1) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    doUnitePowerOff();
                }else if (touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth()) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onSetGear(unionCookerID);

                }
                break;
            case HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITHOUT_TIMER:
                x1 = getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() / 2;
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < y1) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    recyleUnionIndicatorBitmap();
                    doUnitePowerOff();
                }else if(touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < getCookerViewHeight() * 0.7){
                    // LogUtil.d("Enter:: samhung unite set gear");
                    playSoundEffect(SoundEffectConstants.CLICK);
                    recyleUnionIndicatorBitmap();
                    mListener.onSetGear(unionCookerID);
                }else if (touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > getCookerViewHeight() * 0.7) {
                    // LogUtil.d("Enter:: samhung unite set timer");
                    playSoundEffect(SoundEffectConstants.CLICK);
                    recyleUnionIndicatorBitmap();
                    mListener.onSetTimer(unionCookerID);
                }
                break;
            case HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER:
                x1 = getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() / 2;
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < y1) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    recyleUnionIndicatorBitmap();
                    doUnitePowerOff();
                }else if(touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < getCookerViewHeight() * 0.5){
                    //LogUtil.d("Enter:: samhung unite set gear");
                    playSoundEffect(SoundEffectConstants.CLICK);
                    recyleUnionIndicatorBitmap();
                    mListener.onSetGear(unionCookerID);
                }else if (touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > getCookerViewHeight() * 0.5) {
                    //LogUtil.d("Enter:: samhung unite set timer");
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onSetTimer(unionCookerID);
                }
                break;
            case HOB_VIEW_WORK_MODE_TEMP_WORK_FINISH_WAIT_USER_CONFIRM:
                x1 = getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() / 2;
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < y1) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    recyleUnionIndicatorBitmap();
                    doUnitePowerOff();
                }else if(touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < getCookerViewHeight() * 0.5){
                    tempWorkFinishProgress = TEMP_WORK_FINISH_PROGRESS_LIGHT;
                    //LogUtil.d("Enter:: samhung unite set gear");
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onRequestAddTenMinute(unionCookerID);
                }else if (touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > getCookerViewHeight() * 0.5) {
                    //LogUtil.d("Enter:: samhung unite set timer");
                    tempWorkFinishProgress = TEMP_WORK_FINISH_PROGRESS_LIGHT;
                    playSoundEffect(SoundEffectConstants.CLICK);
                    recyleUnionIndicatorBitmap();
                    mListener.onRequestKeepWarm(unionCookerID);

                }
                break;
            case HOB_VIEW_WORK_MODE_TEMP_SENSOR_READY:
                x1 = getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() / 2;
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < y1) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    doUnitePowerOff();
                }else if(touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < getCookerViewHeight() * 0.5){
                    //LogUtil.d("Enter:: samhung unite set gear");

                }else if (touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > getCookerViewHeight() * 0.5) {
                    //LogUtil.d("Enter:: samhung unite set timer");
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onReadyToCook(unionCookerID);
                }
                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_FIRST:
                x1 = getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() / 2;
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < y1) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    doUnitePowerOff();
                    recyleUnionRecipesBitmap();

                }else if(touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth()){

                    if (tempWorkRecipesProgressForUnion == TEMP_WORK_RECIPES_PROGRESS_PICTURE) {//正在显示图片时，点击图片，显示温度时间
                        if (handler.hasMessages(HANDLER_FLASH_RECIPES_FOR_UNION)) {
                            handler.removeMessages(HANDLER_FLASH_RECIPES_FOR_UNION);
                        }
                        tempWorkRecipesProgressForUnion = TEMP_WORK_RECIPES_PROGRESS_TEMP_VALUE;
                        invalidate();
                    }

                }
                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_SECOND:
                x1 = getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() / 2;
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < y1) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    doUnitePowerOff();
                    recyleUnionRecipesBitmap();

                }else if(touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth()){

                    if (tempWorkRecipesProgressForUnion == TEMP_WORK_RECIPES_PROGRESS_PICTURE) {//正在显示图片时，点击图片，显示温度时间
                        if (handler.hasMessages(HANDLER_FLASH_RECIPES_FOR_UNION)) {
                            handler.removeMessages(HANDLER_FLASH_RECIPES_FOR_UNION);
                        }
                        tempWorkRecipesProgressForUnion = TEMP_WORK_RECIPES_PROGRESS_TEMP_VALUE;
                        invalidate();
                    }

                }
                break;
            case HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR:
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getUnionPrepareTempSensorBgBitmap().getWidth() && touchUpY < y1) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    doUnitePowerOff();
                }else {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onSetGear(unionCookerID);
                }
                break;
            case HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR_WITH_INDICATOR:
                y1 = getCookerViewHeight() / 2;
                if (touchUpX > getUnionPrepareTempSensorWithIndicatorBgBitmap().getWidth() && touchUpY < y1) {

                    doUnitePowerOff();
                }
                break;
        }

    }

    private void doUnitePowerOff() {
        //mListener.onCookerPowerOff(unionCookerID);
        mListener.onCookerPowerOff(upCookerID);
        mListener.onCookerPowerOff(downCookerID);
        retangleHobWorkMode = HOB_RETANGLE_WORK_MODE_WORK_SEPARATE;
    }

    private void processSepareteClickEvent(MotionEvent event) {
        float touchUpX = event.getX();
        float touchUpY = event.getY();
        float x1,y1;
        if (touchUpY < getCookerViewHeight() / 2) {
            processUpClickEvent(event);

        }else if (touchUpY > getCookerViewHeight() / 2){
            processDownClickEvent(event);

        }

    }

    private void processUpClickEvent(MotionEvent event) {
        float touchUpX = event.getX();
        float touchUpY = event.getY();
        float x1,y1;
        switch (upWorkMode) {
            case HOB_VIEW_WORK_MODE_POWER_OFF:
                playSoundEffect(SoundEffectConstants.CLICK);
                mListener.onCookerPowerOn(upCookerID);

                break;
            case HOB_VIEW_WORK_MODE_FIRE_GEAR_WITHOUT_TIMER:
                x1 = getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() / 2;
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < y1) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onCookerPowerOff(upCookerID);
                }else if(touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < getCookerViewHeight() * 0.35){
                    playSoundEffect(SoundEffectConstants.CLICK);
                    //LogUtil.d("Enter::samhung set gear up ");
                    mListener.onSetGear(upCookerID);
                }else if (touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > getCookerViewHeight() * 0.35) {
                    //LogUtil.d("Enter::samhung set timer up  ");
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onSetTimer(upCookerID);
                }


                break;
            case HOB_VIEW_WORK_MODE_FIRE_GEAR_WITH_TIMER:
                x1 = getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() / 2;
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < y1) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onCookerPowerOff(upCookerID);
                }else if(touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < getCookerViewHeight() * 0.25){
                    playSoundEffect(SoundEffectConstants.CLICK);
                    LogUtil.d("Enter::samhung set gear up ");
                    mListener.onSetGear(upCookerID);
                }else if (touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > getCookerViewHeight() * 0.25) {
                    LogUtil.d("Enter::samhung set timer up  ");
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onSetTimer(upCookerID);
                }

                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITHOUT_TIMER:
                x1 = getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() / 2;
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < y1) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onCookerPowerOff(upCookerID);
                }else if(touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < getCookerViewHeight() * 0.35){
                    playSoundEffect(SoundEffectConstants.CLICK);
                    //LogUtil.d("Enter::samhung set gear up ");
                    mListener.onSetGear(upCookerID);
                }else if (touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > getCookerViewHeight() * 0.35) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    //LogUtil.d("Enter::samhung set timer up  ");
                    mListener.onSetTimer(upCookerID);
                }
                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER:
                x1 = getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() / 2;
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < y1) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onCookerPowerOff(upCookerID);
                }else if(touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < getCookerViewHeight() * 0.25){
                    playSoundEffect(SoundEffectConstants.CLICK);
                    //LogUtil.d("Enter::samhung set gear up ");
                    mListener.onSetGear(upCookerID);
                }else if (touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > getCookerViewHeight() * 0.25) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    //LogUtil.d("Enter::samhung set timer up  ");
                    mListener.onSetTimer(upCookerID);
                }
                break;
            case HOB_VIEW_WORK_MODE_ABNORMAL_NO_PAN:
                LogUtil.d("samhung up----no---pan");
                x1 = getSeperateAbnormalNoPanBgBitmap().getWidth() / 2;
                y1 = getCookerViewHeight() / 2;
                LogUtil.d("samhung123 touchUpX----->" + touchUpX);
                LogUtil.d("samhung touchUpY----->" + touchUpY);
                LogUtil.d("samhung y1----->" + y1);

                if (touchUpX > getSeperateAbnormalNoPanBgBitmap().getWidth() && touchUpY < y1) {
                    LogUtil.d("samhung---power off----");
                    mListener.onCookerPowerOff(upCookerID);
                }else if (touchUpX < getSeperateAbnormalNoPanBgBitmap().getWidth()){
                    LogUtil.d("samhung---set gear----");
                    mListener.onSetGear(upCookerID);
                }
                break;
            case HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITHOUT_TIMER:
                x1 = getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() / 2;
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < y1) {
                    recyleSeperateUpIndicatorBitmap();
                    mListener.onCookerPowerOff(upCookerID);
                }else if(touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < getCookerViewHeight() * 0.35){

                    //LogUtil.d("Enter::samhung set gear up ");
                    recyleSeperateUpIndicatorBitmap();
                    mListener.onSetGear(upCookerID);
                }else if (touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > getCookerViewHeight() * 0.35) {
                    //LogUtil.d("Enter::samhung set timer up  ");
                    recyleSeperateUpIndicatorBitmap();
                    mListener.onSetTimer(upCookerID);
                }
                break;
            case HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER:
                x1 = getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() / 2;
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < y1) {
                    recyleSeperateUpIndicatorBitmap();
                    mListener.onCookerPowerOff(upCookerID);
                }else if(touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < getCookerViewHeight() * 0.25){

                    //LogUtil.d("Enter::samhung set gear up ");
                    recyleSeperateUpIndicatorBitmap();
                    mListener.onSetGear(upCookerID);
                }else if (touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > getCookerViewHeight() * 0.25) {
                    //LogUtil.d("Enter::samhung set timer up  ");
                    mListener.onSetTimer(upCookerID);
                }
                break;
            case HOB_VIEW_WORK_MODE_TEMP_WORK_FINISH_WAIT_USER_CONFIRM:
                x1 = getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() / 2;
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < y1) {
                    recyleSeperateUpIndicatorBitmap();
                    doUnitePowerOff();
                }else if(touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < getCookerViewHeight() * 0.25){
                    upTempWorkFinishProgress = TEMP_WORK_FINISH_PROGRESS_LIGHT;
                    //LogUtil.d("Enter:: samhung unite set gear");
                    mListener.onRequestAddTenMinute(upCookerID);
                }else if (touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > getCookerViewHeight() * 0.25) {
                    //LogUtil.d("Enter:: samhung unite set timer");
                    upTempWorkFinishProgress = TEMP_WORK_FINISH_PROGRESS_LIGHT;
                    recyleSeperateUpIndicatorBitmap();
                    mListener.onRequestKeepWarm(upCookerID);

                }
                break;
            case HOB_VIEW_WORK_MODE_TEMP_SENSOR_READY:
                x1 = getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() / 2;
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < y1) {

                    mListener.onCookerPowerOff(upCookerID);
                }else if(touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < getCookerViewHeight() * 0.25){


                }else if (touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > getCookerViewHeight() * 0.25) {
                    //LogUtil.d("Enter::samhung set timer up  ");
                    mListener.onReadyToCook(upCookerID);
                }
                break;
            case HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP:
                mListener.onCookerPowerOn(upCookerID);
                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_SECOND:
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < y1) {
                    mListener.onCookerPowerOff(upCookerID);
                    recyleSeperateUpRecipesBitmap();


                }else if(touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth()){

                    if (tempWorkRecipesProgressForSeperateUp == TEMP_WORK_RECIPES_PROGRESS_PICTURE) {//正在显示图片时，点击图片，显示温度时间
                        if (handler.hasMessages(HANDLER_FLASH_RECIPES_FOR_SEPERATE_UP)) {
                            handler.removeMessages(HANDLER_FLASH_RECIPES_FOR_SEPERATE_UP);
                        }
                        tempWorkRecipesProgressForSeperateUp = TEMP_WORK_RECIPES_PROGRESS_TEMP_VALUE;
                        invalidate();
                    }

                }

                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_FIRST:
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < y1) {
                    mListener.onCookerPowerOff(upCookerID);
                    recyleSeperateUpRecipesBitmap();


                }else if(touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth()){

                    if (tempWorkRecipesProgressForSeperateUp == TEMP_WORK_RECIPES_PROGRESS_PICTURE) {//正在显示图片时，点击图片，显示温度时间
                        if (handler.hasMessages(HANDLER_FLASH_RECIPES_FOR_SEPERATE_UP)) {
                            handler.removeMessages(HANDLER_FLASH_RECIPES_FOR_SEPERATE_UP);
                        }
                        tempWorkRecipesProgressForSeperateUp = TEMP_WORK_RECIPES_PROGRESS_TEMP_VALUE;
                        invalidate();
                    }

                }
                break;
            case HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR:

                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getSeperatePrepareTempSensorBgBitmap().getWidth() && touchUpY < y1) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onCookerPowerOff(upCookerID);
                }else {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onSetGear(upCookerID);
                }
                break;
            case HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR_WITH_INDICATOR:

                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getSeperatePrepareTempSensorWithIndicatorBgBitmap().getWidth() && touchUpY < y1) {
                    mListener.onCookerPowerOff(upCookerID);
                }
                break;
        }
    }

    private void processDownClickEvent(MotionEvent event) {
        float touchUpX = event.getX();
        float touchUpY = event.getY();
        float x1,y1;
        switch (downWorkMode) {
            case HOB_VIEW_WORK_MODE_POWER_OFF:
                playSoundEffect(SoundEffectConstants.CLICK);
                mListener.onCookerPowerOn(downCookerID);
                break;
            case HOB_VIEW_WORK_MODE_FIRE_GEAR_WITHOUT_TIMER:
                x1 = getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth() / 2;
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > y1) {
                    LogUtil.d("---power off----");
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onCookerPowerOff(downCookerID);
                }else if(touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < getCookerViewHeight() * 0.75){
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onSetGear(downCookerID);
                    //LogUtil.d("samhung---set gear-- down--");
                }else if (touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > getCookerViewHeight() * 0.75) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onSetTimer(downCookerID);
                    //LogUtil.d("samhung---set timer---down-");
                }



                break;
            case HOB_VIEW_WORK_MODE_FIRE_GEAR_WITH_TIMER:
                x1 = getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth() / 2;
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > y1) {
                    //LogUtil.d("---power off----");
                    mListener.onCookerPowerOff(downCookerID);
                }else if(touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < getCookerViewHeight() * 0.75){
                    mListener.onSetGear(downCookerID);
                    // LogUtil.d("samhung---set gear-- down--");
                }else if (touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > getCookerViewHeight() * 0.75) {
                    mListener.onSetTimer(downCookerID);
                    LogUtil.d("samhung---set timer---down-");
                }
                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITHOUT_TIMER:
                x1 = getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth() / 2;
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > y1) {
                    LogUtil.d("---power off----");
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onCookerPowerOff(downCookerID);
                }else if(touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < getCookerViewHeight() * 0.75){
                    playSoundEffect(SoundEffectConstants.CLICK);
                    recyleSeperateDownIndicatorBitmap();
                    mListener.onSetGear(downCookerID);
                    //LogUtil.d("samhung---set gear-- down--");
                }else if (touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > getCookerViewHeight() * 0.75) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    recyleSeperateDownIndicatorBitmap();
                    mListener.onSetTimer(downCookerID);
                    //LogUtil.d("samhung---set timer---down-");
                }
                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER:
                x1 = getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth() / 2;
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > y1) {
                    //LogUtil.d("---power off----");
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onCookerPowerOff(downCookerID);
                }else if(touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < getCookerViewHeight() * 0.75){
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onSetGear(downCookerID);
                    // LogUtil.d("samhung---set gear-- down--");
                }else if (touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > getCookerViewHeight() * 0.75) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onSetTimer(downCookerID);
                    //LogUtil.d("samhung---set timer---down-");
                }
                break;
            case HOB_VIEW_WORK_MODE_ABNORMAL_NO_PAN:
                x1 = getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth() / 2;
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > y1) {
                    //LogUtil.d("---power off----");
                    mListener.onCookerPowerOff(downCookerID);
                }else if (touchUpX < getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth()){
                    mListener.onSetGear(downCookerID);
                }
                break;
            case HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITHOUT_TIMER:
                x1 = getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth() / 2;
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > y1) {
                    LogUtil.d("---power off----");
                    recyleSeperateDownIndicatorBitmap();
                    mListener.onCookerPowerOff(downCookerID);
                }else if(touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < getCookerViewHeight() * 0.75){
                    recyleSeperateDownIndicatorBitmap();
                    mListener.onSetGear(downCookerID);
                    //LogUtil.d("samhung---set gear-- down--");
                }else if (touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > getCookerViewHeight() * 0.75) {
                    recyleSeperateDownIndicatorBitmap();
                    mListener.onSetTimer(downCookerID);
                    //LogUtil.d("samhung---set timer---down-");
                }
                break;
            case HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER:
                x1 = getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth() / 2;
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > y1) {
                    //LogUtil.d("---power off----");
                    recyleSeperateDownIndicatorBitmap();
                    mListener.onCookerPowerOff(downCookerID);
                }else if(touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < getCookerViewHeight() * 0.75){
                    recyleSeperateDownIndicatorBitmap();
                    mListener.onSetGear(downCookerID);
                    // LogUtil.d("samhung---set gear-- down--");
                }else if (touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > getCookerViewHeight() * 0.75) {
                    mListener.onSetTimer(downCookerID);
                    //LogUtil.d("samhung---set timer---down-");
                }
                break;
            case HOB_VIEW_WORK_MODE_TEMP_WORK_FINISH_WAIT_USER_CONFIRM:
                x1 = getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() / 2;
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < y1) {
                    recyleSeperateDownIndicatorBitmap();
                    doUnitePowerOff();
                }else if(touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < getCookerViewHeight() * 0.75){
                    downTempWorkFinishProgress = TEMP_WORK_FINISH_PROGRESS_LIGHT;
                    //LogUtil.d("Enter:: samhung unite set gear");
                    mListener.onRequestAddTenMinute(downCookerID);
                }else if (touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > getCookerViewHeight() * 0.75) {
                    //LogUtil.d("Enter:: samhung unite set timer");
                    downTempWorkFinishProgress = TEMP_WORK_FINISH_PROGRESS_LIGHT;
                    recyleSeperateDownIndicatorBitmap();
                    mListener.onRequestKeepWarm(downCookerID);

                }
                break;
            case HOB_VIEW_WORK_MODE_TEMP_SENSOR_READY:
                x1 = getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth() / 2;
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > y1) {
                    //LogUtil.d("---power off----");
                    mListener.onCookerPowerOff(downCookerID);
                }else if(touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY < getCookerViewHeight() * 0.75){

                }else if (touchUpX < getUnionPowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > getCookerViewHeight() * 0.75) {
                    mListener.onReadyToCook(downCookerID);
                    //LogUtil.d("samhung---set timer---down-");
                }
                break;
            case HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP:


                mListener.onCookerPowerOn(downCookerID);
                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_FIRST:
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > y1) {
                    //LogUtil.d("---power off----");
                    mListener.onCookerPowerOff(downCookerID);
                    recyleSeperateDownRecipesBitmap();
                }else if (touchUpX < getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth()){
                    if (tempWorkRecipesProgressForSeperateDown == TEMP_WORK_RECIPES_PROGRESS_PICTURE) {//正在显示图片时，点击图片，显示温度时间
                        if (handler.hasMessages(HANDLER_FLASH_RECIPES_FOR_SEPERATE_DOWN)) {
                            handler.removeMessages(HANDLER_FLASH_RECIPES_FOR_SEPERATE_DOWN);
                        }
                        tempWorkRecipesProgressForSeperateDown = TEMP_WORK_RECIPES_PROGRESS_TEMP_VALUE;
                        invalidate();
                    }
                }

                break;
            case HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_SECOND:
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth() && touchUpY > y1) {
                    //LogUtil.d("---power off----");
                    mListener.onCookerPowerOff(downCookerID);
                    recyleSeperateDownRecipesBitmap();
                }else if (touchUpX < getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth()){
                    if (tempWorkRecipesProgressForSeperateDown == TEMP_WORK_RECIPES_PROGRESS_PICTURE) {//正在显示图片时，点击图片，显示温度时间
                        if (handler.hasMessages(HANDLER_FLASH_RECIPES_FOR_SEPERATE_DOWN)) {
                            handler.removeMessages(HANDLER_FLASH_RECIPES_FOR_SEPERATE_DOWN);
                        }
                        tempWorkRecipesProgressForSeperateDown = TEMP_WORK_RECIPES_PROGRESS_TEMP_VALUE;
                        invalidate();
                    }
                }

                break;
            case HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR:
                y1 = getCookerViewHeight() / 2;

                if (touchUpX > getSeperatePrepareTempSensorBgBitmap().getWidth() && touchUpY > y1) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onCookerPowerOff(downCookerID);
                }else {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mListener.onSetGear(downCookerID);
                }

                break;
            case HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR_WITH_INDICATOR:
                y1 = getCookerViewHeight() / 2;
                if (touchUpX > getSeperatePrepareTempSensorWithIndicatorBgBitmap().getWidth() && touchUpY > y1) {

                    mListener.onCookerPowerOff(downCookerID);
                }
                break;


        }
    }

    private void switchRetangleHobWorkMode(boolean isUnion) {
        LogUtil.d("samhung--switch hob work mode--->" + isUnion);
        if (isUnion) {
            retangleHobWorkMode = HOB_RETANGLE_WORK_MODE_WORK_UNITE;


            mListener.onCookerPowerOn(unionCookerID);
        }else {
            retangleHobWorkMode = HOB_RETANGLE_WORK_MODE_WORK_SEPARATE;

        }
    }


    private void doPowerOff() {
        recyle();
        // mListener.onCookerPowerOff();
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
                (float) (getCookerViewHeight() / 2 - getUnionReadyToCookBgBitmap().getHeight() * 0.4),
                getUnionReadyToCookBgBitmap().getWidth(),
                getCookerViewHeight()/ 2);
        fontMetrics = mPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        int baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
        return baseLineY;
    }

    private int getUpReadyToCookY() {
        mViewRect.set(
                0,
                (getCookerViewHeight()/2 - HOB_RETANGLE_VIEW_GAP / 2 - getSeperateReadyToCookBgBitmap().getHeight() / 2),
                getSeperateReadyToCookBgBitmap().getWidth(),
                (float) (getCookerViewHeight() / 18));

        fontMetrics = mPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        int baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
        return baseLineY;
    }

    private int getDownReadyToCookY() {
        mViewRect.set(
                0,
                (getCookerViewHeight() / 2 + HOB_RETANGLE_VIEW_GAP / 2),
                getSeperatePowerOnFireWithoutTimerBgBitmap().getWidth(),
                (float) (getCookerViewHeight() * 0.8 ));


        fontMetrics = mPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        int baseLineY = (int) (mViewRect.centerY() - top/2 - bottom/2) + 0;//基线中间点的y轴计算公式
        return baseLineY;
    }

    private boolean doUnion() {
        if (downWorkMode == HOB_VIEW_WORK_MODE_POWER_OFF && upWorkMode == HOB_VIEW_WORK_MODE_POWER_OFF ) {
            playSoundEffect(SoundEffectConstants.CLICK);
            switchRetangleHobWorkMode(true);
            return true;
        }else if (downWorkMode == HOB_VIEW_WORK_MODE_POWER_OFF && upWorkMode == HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP) {
            playSoundEffect(SoundEffectConstants.CLICK);
            switchRetangleHobWorkMode(true);
            return true;
        }else if (downWorkMode == HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP && upWorkMode == HOB_VIEW_WORK_MODE_POWER_OFF) {
            playSoundEffect(SoundEffectConstants.CLICK);
            switchRetangleHobWorkMode(true);
            return true;
        }else if (downWorkMode == HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP && upWorkMode == HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP) {
            playSoundEffect(SoundEffectConstants.CLICK);
            switchRetangleHobWorkMode(true);
            return true;
        }
        return false;
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

    /****************************************Interface*************************************************/

    /******************************get display bitmap***********************************************/
    protected abstract Bitmap getUnionPowerOffBgBitmap();//get union power off background bitmap
    protected abstract Bitmap getSeperatePowerOffBgBitmap();//get union power off background bitmap
    protected abstract Bitmap getSeperatePowerOnFireWithoutTimerBgBitmap();//get union power off background bitmap
    protected abstract Bitmap getSeperatePowerOnFireWithTimerBgBitmap();//get union power off background bitmap
    protected abstract Bitmap getPowerOffButtonBitmap();//get power off button bitmap
    protected abstract Bitmap getUnionPowerOnFireWithoutTimerBgBitmap();//get union power on background bitmap
    protected abstract Bitmap getUnionPowerOnFireWithTimerBgBitmap();//get union power on background bitmap
    protected abstract Bitmap getUnionPowerOnTempWithoutTimerBgBitmap();//get union power on background bitmap
    protected abstract Bitmap getUnionPowerOnTempWithTimerBgBitmap();//get union power on background bitmap
    protected abstract Bitmap getSeperatePowerOnTempWithoutTimerBgBitmap();//get union power off background bitmap
    protected abstract Bitmap getSeperatePowerOnTempWithTimerBgBitmap();//get union power off background bitmap
    protected abstract Bitmap getUnionAbnormalNoPanBgBitmap();//get union power off background bitmap
    protected abstract Bitmap getSeperateAbnormalNoPanBgBitmap();//get union power off background bitmap
    protected abstract Bitmap getUnionPowerOnTempIndicatorWithoutTimerBgBitmap();
    protected abstract Bitmap getUnionPowerOnTempIndicatorWithTimerBgBitmap();
    protected abstract Bitmap getSeperatePowerOnTempIndicatorWithoutTimerBgBitmap();
    protected abstract Bitmap getSeperatePowerOnTempIndicatorWithTimerBgBitmap();
    protected abstract Bitmap getUnionTempIndicatorWithoutTimerBitmap();
    protected abstract Bitmap getUnionTempIndicatorWithTimerBitmap();
    protected abstract Bitmap getSeperateUpTempIndicatorWithoutTimerBitmap();
    protected abstract Bitmap getSeperateUpTempIndicatorWithTimerBitmap();
    protected abstract Bitmap getSeperateDownTempIndicatorWithoutTimerBitmap();
    protected abstract Bitmap getSeperateDownTempIndicatorWithTimerBitmap();
    protected abstract Bitmap getUnionTempFinishLightBgBitmap();
    protected abstract Bitmap getUnionTempFinishDarkBgBitmap();
    protected abstract Bitmap getSeperateTempFinishLightBgBitmap();
    protected abstract Bitmap getSeperateTempFinishDarkBgBitmap();
    protected abstract Bitmap getUnionReadyToCookBgBitmap();
    protected abstract Bitmap getUnionReadyToCookValueBgBitmap();
    protected abstract Bitmap getSeperateReadyToCookBgBitmap();
    protected abstract Bitmap getSeperateReadyToCookValueBgBitmap();
    protected abstract Bitmap getUnionPrepareTempSensorBgBitmap();
    protected abstract Bitmap getUnionPrepareTempSensorWithIndicatorBgBitmap();
    protected abstract Bitmap getSeperatePrepareTempSensorBgBitmap();
    protected abstract Bitmap getSeperatePrepareTempSensorWithIndicatorBgBitmap();
    protected abstract Bitmap getSeperateHighTempBgBitmap();
    protected abstract Bitmap getUnionHighTempBgBitmap();
    protected abstract Bitmap getUnionErrorOccurBgBitmap();
    protected abstract Bitmap getSeperateErrorOccurBgBitmap();
    protected abstract Bitmap getUnionRecipesPicBitmap();//get recipes picture bitmap
    protected abstract Bitmap getUpRecipesPicBitmap();//get recipes picture bitmap
    protected abstract Bitmap getDownRecipesPicBitmap();//get recipes picture bitmap
    protected abstract Bitmap getUnionRecipesBgBitmap();//get recipes picture bitmap
    protected abstract Bitmap getSeperateRecipesBgBitmap();//get recipes picture bitmap
    protected abstract Bitmap getSeperateTempPrepareSensorBitmap();//get temp prepare temp bitmap
    protected abstract Bitmap getSeperateTempIndicatorPrepareSensorBitmap();//get temp indicator prepare temp bitmap
    protected abstract Bitmap getSeperateTempSensorReadyBitmap();//get temp sensor ready bitmap
    protected abstract Bitmap getSeperateTempSensorReadyValueBitmap();//get temp sensor ready value bitmap
    protected abstract Bitmap getSeperateTempWorkFinishDarkBitmap();//get temp work finish and wait user confirm bitmap
    protected abstract Bitmap getSeperateTempGearWithTimerBitmap();//get temp gear with timer bitmap
    protected abstract Bitmap getUnionTempWorkFinishLightBitmap();
    protected abstract Bitmap getSeperateTempWorkFinishLightBitmap();





    /*******************************get display font size****************************************************************/
    protected abstract int getSeperateFireGearWithoutTimerFontSize();
    protected abstract int getSeperateFireGearWithTimerGearFontSize();
    protected abstract int getSeperateFireGearWithTimerTimerFontSize();
    protected abstract int getSeperateTempGearWithTimerTimerFontSize();
    protected abstract int getSeperateUpTempGearWithoutTimerTempFontSize();
    protected abstract int getSeperateDownTempGearWithoutTimerTempFontSize();
    protected abstract int getSeperateUpTempGearWithTimerTempFontSize();
    protected abstract int getSeperateDownTempGearWithTimerTempFontSize();
    protected abstract int getUnionFireGearWithoutTimerFontSize();
    protected abstract int getUnionFireGearWithTimerGearFontSize();
    protected abstract int getUnionFireGearWithTimerTimerFontSize();
    protected abstract int getUnionTempGearWithoutTimerFontSize();
    protected abstract int getUnionTempGearWithTimerTimerFontSize();
    protected abstract int getUnionTempGearWithTimerTempFontSize();
    protected abstract int getUnionErrorMessageFontSize();
    protected abstract int getSeperateErrorMessageFontSize();
    protected abstract int getSeperateKeepWarmAndAddTenMinuteFontSize();
    protected abstract int getUpTempPreheatSettingValueFontSize();//温控模式，预热状态的设置温度值
    protected abstract int getUpTempPreheatRealValueFontSize();//温控模式，预热状态的真实温度值
    protected abstract int getDownTempPreheatSettingValueFontSize();//温控模式，预热状态的设置温度值
    protected abstract int getDownTempPreheatRealValueFontSize();//温控模式，预热状态的真实温度值
    protected abstract int getUnionTempPreheatSettingValueFontSize();//温控模式，预热状态的设置温度值
    protected abstract int getUnionTempPreheatRealValueFontSize();//温控模式，预热状态的真实温度值
    protected abstract int getUnionReadyToCookFontSize();//ready to cook
    protected abstract int getSeperateReadyToCookFontSize();//ready to cook
    protected abstract int getUnionReadyToCookValueFontSize();//ready to cook value
    protected abstract int getSeperateUpReadyToCookValueFontSize();//ready to cook value
    protected abstract int getSeperateDownReadyToCookValueFontSize();//ready to cook value




    /***********************get display value****************************************/
    protected abstract String getUpFireGearValue();
    protected abstract String getUpTimerValue();
    protected abstract String getUpTempValue();
    protected abstract String getDownFireGearValue();
    protected abstract String getDownTimerValue();
    protected abstract String getDownTempValue();
    protected abstract String getUnionFireGearValue();
    protected abstract String getUnionTimerValue();
    protected abstract String getUnionTempGearValue();
    protected abstract String getUnionErrorMessage();
    protected abstract String getUpErrorMessage();
    protected abstract String getDownErrorMessage();
    protected abstract String getUpRealTempValue();//真实温度值
    protected abstract int getUpSweepAngle();//真实温度圆弧度数
    protected abstract float getUpRealTempArcGap();
    protected abstract float getUpRealTempTextGap();
    protected abstract String getDownRealTempValue();//真实温度值
    protected abstract int getDownSweepAngle();//真实温度圆弧度数
    protected abstract float getDownRealTempArcGap();
    protected abstract float getDownRealTempTextGap();
    protected abstract String getUnionRealTempValue();//真实温度值
    protected abstract int getUnionSweepAngle();//真实温度圆弧度数
    protected abstract float getUnionRealTempArcGap();
    protected abstract float getUnionRealTempTextGap();

    // protected abstract String getTempGearValue();
    // protected abstract String getTimerValue();
    // protected abstract String getErrorMessage();


    /*******************************get display size****************************************************/
    protected abstract int getCookerViewWidth();
    protected abstract int getCookerViewHeight();
    protected abstract int getSeperateRealTempProgressArcWidth();
    protected abstract int getSeperateRealTempProgressRadius();
    protected abstract int getUnionRealTempProgressArcWidth();
    protected abstract int getUnionRealTempProgressRadius();
    protected abstract int getUnionInnerTextMaxWidth();
    protected abstract int getUnionInnerTextMaxWidthBig();


    /*************************************recycle*************************************************************/
    protected abstract void recyle();
    protected abstract void recyleUnionIndicatorBitmap();
    protected abstract void recyleSeperateUpIndicatorBitmap();
    protected abstract void recyleSeperateDownIndicatorBitmap();
    protected abstract void recyleRecipesBitmap();
    protected abstract void recyleUnionRecipesBitmap();
    protected abstract void recyleSeperateUpRecipesBitmap();
    protected abstract void recyleSeperateDownRecipesBitmap();

    /****************************************user interface**********************************************************/
    //protected abstract void upateUI(int workMode,int fireValue,int tempValue,int hourValue,int minuteValue,int tempIndicatorID,int recipesID,int recipesShowOrder,String errorMessage);
}
