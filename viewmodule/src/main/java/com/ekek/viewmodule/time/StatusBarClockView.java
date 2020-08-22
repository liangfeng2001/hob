package com.ekek.viewmodule.time;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.viewmodule.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StatusBarClockView extends View {
    private static final int DEFAULT_TIME_TEXT_SIZE = 40;
    private static final String ACTION_TIME_DATE = Intent.ACTION_TIME_TICK;
    private static final String ACTION_TIME_CHANGED = Intent.ACTION_TIME_CHANGED;
    private static final String ACTION_TIMEZONE_CHANGED = Intent.ACTION_TIMEZONE_CHANGED;
    private float timeTextSize;
    private int timeTextColor;
    private Paint timePaint;
    /**
     * 绘制时控制文本绘制的范围
     */
    private Rect mBound;
    private boolean isTimeReceiverRegister = false;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_TIME_DATE) || action.equals(ACTION_TIME_CHANGED) || action.equals(ACTION_TIMEZONE_CHANGED))  {
                notifyUpdateUI();
            }
        }
    };
    public StatusBarClockView(Context context) {
        super(context);
    }

    public StatusBarClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.viewmodule_DigitalClockView);
        timeTextSize = a.getDimension(R.styleable.viewmodule_StatusBarClockView_viewmodule_timeTextSize, DEFAULT_TIME_TEXT_SIZE);
        timeTextColor = a.getColor(R.styleable.viewmodule_StatusBarClockView_viewmodule_timeTextColor, Color.WHITE);
        a.recycle();

        initResource();
        initPaint();
        registerTimeReceiver();
    }

    private void initResource() {

    }

    private void initPaint() {
        timePaint = new Paint();
        timePaint.setTextSize(timeTextSize);
        timePaint.setColor(timeTextColor);
        Typeface timeTypeface = GlobalVars.getInstance().getDefaultFontRegular();
        timePaint.setTypeface(timeTypeface);
        timePaint.setTextAlign(Paint.Align.CENTER);
        timePaint.setAntiAlias(true);

        mBound = new Rect();
        String timeStr = getTime(Calendar.getInstance());
        timePaint.getTextBounds(timeStr, 0, timeStr.length(), mBound);
    }

    private String getTime(Calendar c) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String timeStr = format.format(c.getTime());
        return timeStr;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height ;
        if (widthMode == MeasureSpec.EXACTLY){
            width = widthSize;
        } else{
            timePaint.setTextSize(timeTextSize);
            String timeStr = getTime(Calendar.getInstance());
            timePaint.getTextBounds(timeStr, 0, timeStr.length(), mBound);
            float textWidth = mBound.width();
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = desired + 70;
        }

        if (heightMode == MeasureSpec.EXACTLY){
            height = heightSize;
        } else{
            timePaint.setTextSize(timeTextSize);
            String timeStr = getTime(Calendar.getInstance());
            timePaint.getTextBounds(timeStr, 0, timeStr.length(), mBound);
            float textHeight = mBound.height();
            int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            height = desired;
        }



        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unRegisterTimeReceiver();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        timePaint.setColor(Color.RED);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), timePaint);
        timePaint.setColor(timeTextColor);
        String timeStr = getTime(Calendar.getInstance());
        canvas.drawText(timeStr, getWidth() / 2 - mBound.width() / 2, getHeight() / 2 + mBound.height() / 2, timePaint);

    }

    private void notifyUpdateUI() {
        invalidate();
    }

    private void registerTimeReceiver() {
        if (isTimeReceiverRegister)return;
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_TIME_DATE);
        filter.addAction(ACTION_TIME_CHANGED);
        filter.addAction(ACTION_TIMEZONE_CHANGED);
        getContext().registerReceiver(receiver, filter);
        isTimeReceiverRegister = true;

    }

    private void unRegisterTimeReceiver() {
        if (isTimeReceiverRegister) {
            getContext().unregisterReceiver(receiver);
            isTimeReceiverRegister = false;
        }

    }
}
