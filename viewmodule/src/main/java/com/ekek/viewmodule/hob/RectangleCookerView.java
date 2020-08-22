package com.ekek.viewmodule.hob;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.viewmodule.R;

public class RectangleCookerView extends View implements GestureDetector.OnGestureListener{
    private static final int VIEW_SIZE_WIDTH = 263;
    private static final int VIEW_SIZE_HEIGHT = 544;
    /*
    * 炉头模式：上下小单炉，双炉
    * */
    private static final int RECTANGLE_COOKER_MODE_NONE_COOKER_WORK = 0;
    private static final int RECTANGLE_COOKER_MODE_DOUBLE_COOKER_WORK = 1;
    private static final int RECTANGLE_COOKER_MODE_SINGLE_UP_COOKER_WORK = 2;
    private static final int RECTANGLE_COOKER_MODE_SINGLE_DOWN_COOKER_WORK = 3;

    /*
    * 炉头工作模式，档位，普通控温，精准控温，定时
    * */
    public final static int HOB_WORK_MODE_FIRE_GEAR = 0;
    public final static int HOB_WORK_MODE_FIRE_GEAR_WITH_TIMER = 1;
    public final static int HOB_WORK_MODE_FAST_TEMP_GEAR = 2;
    public final static int HOB_WORK_MODE_FAST_TEMP_GEAR_WITH_TIMER = 3;
    public final static int HOB_WORK_MODE_PRECISE_TEMP_GEAR = 4;
    public final static int HOB_WORK_MODE_PRECISE_TEMP_GEAR_WITH_TIMER = 5;
    public final static int HOB_WORK_MODE_POWER_OFF = 6;
    public final static int HOB_WORK_MODE_ERROR_OCURR = 7;
    public final static int HOB_WORK_MODE_STATUS_ABNORMAL_NO_PAN = 8;
    public final static int HOB_WORK_MODE_STATUS_ABNORMAL_HIGH_TEMP = 9;

    private int rectangleCookerMode = RECTANGLE_COOKER_MODE_DOUBLE_COOKER_WORK;
    private int rectangleCookerWorkMode = HOB_WORK_MODE_POWER_OFF;
    private Bitmap mDoubleCookerPowerOffBp;
    private Bitmap mSingleCookerPowerOffBp;
    private Paint gearPaint;
    private RectF mViewRect;
    private int mViewWidth = 263;
    private int mViewHeight = 544;
    private GestureDetector mDetector;

    public RectangleCookerView(Context context) {
        this(context,null);
    }

    public RectangleCookerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (mDoubleCookerPowerOffBp == null) mDoubleCookerPowerOffBp = BitmapFactory.decodeResource(getResources(), R.mipmap.bg_rectangle_cooker_double_power_off);


        init(context);
    }

    private void init(Context context) {
        mDetector = new GestureDetector(context,this);
        initPaint();
    }

    private void initPaint() {
        gearPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gearPaint.setColor(ContextCompat.getColor(getContext(),R.color.viewmodule_gear_white));
        gearPaint.setTextAlign(Paint.Align.CENTER);
        gearPaint.setStyle(Paint.Style.FILL);
        gearPaint.setTypeface(GlobalVars.getInstance().getDefaultFontBold());
        mViewRect = new RectF();
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
        return VIEW_SIZE_HEIGHT;
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return VIEW_SIZE_WIDTH;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawView(canvas);
    }

    private void drawView(Canvas canvas) {
        switch (rectangleCookerWorkMode) {
            case HOB_WORK_MODE_POWER_OFF:
                if (rectangleCookerMode == RECTANGLE_COOKER_MODE_DOUBLE_COOKER_WORK) {
                    if (mDoubleCookerPowerOffBp == null) mDoubleCookerPowerOffBp = BitmapFactory.decodeResource(getResources(), R.mipmap.bg_rectangle_cooker_double_power_off);
                    mViewRect.set(0, 0, getWidth(), getHeight());
                    canvas.drawBitmap(mDoubleCookerPowerOffBp,null, mViewRect,null);
                }else if (rectangleCookerMode == RECTANGLE_COOKER_MODE_SINGLE_UP_COOKER_WORK) {

                }else if (rectangleCookerMode == RECTANGLE_COOKER_MODE_SINGLE_DOWN_COOKER_WORK) {

                }



                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtil.d("Enter:: ontouch------>" + event.getAction());
        if (event.getAction() == MotionEvent.ACTION_MOVE) LogUtil.d("Enter:: ontouch-----move");
        //LogUtil.d("Enter:: onTouchEvent");
        //return super.onTouchEvent(event);
        return mDetector.onTouchEvent(event);
        //return true;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        LogUtil.d("Enter:: onDown");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        LogUtil.d("Enter:: onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        LogUtil.d("Enter:: onSingleTapUp");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        LogUtil.d("Enter:: onScroll");
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        LogUtil.d("Enter:: onLongPress");
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        LogUtil.d("Enter:: onFling");
        return false;
    }
}
