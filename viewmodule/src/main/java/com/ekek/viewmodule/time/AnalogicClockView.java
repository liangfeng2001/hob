package com.ekek.viewmodule.time;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.viewmodule.R;

import java.util.Calendar;

public class AnalogicClockView extends View {

    // Constants
    private static final int DEFAULT_DATE_TEXT_SIZE = 40;
    private static final boolean DEFAULT_DATE_VIEW_VISIBILITY = true;
    private static final String ACTION_TIME_DATE = Intent.ACTION_TIME_TICK;
    private static final String ACTION_TIME_CHANGED = Intent.ACTION_TIME_CHANGED;
    private static final String ACTION_TIMEZONE_CHANGED = Intent.ACTION_TIMEZONE_CHANGED;
    public static final int DIAL_PLATE_SIZE_NORMAL = 0;
    public static final int DIAL_PLATE_SIZE_BIG = 1;

    // Fields
    private String[] weeks, days, months;
    private float dateTextSize;
    private int dateTextColor;
    private int dialPlateSize;
    private Paint datePaint;
    private boolean showDate = DEFAULT_DATE_VIEW_VISIBILITY;
    private boolean isTimeReceiverRegister = false;
    private PaintFlagsDrawFilter pfd;

    private Bitmap mBmpDial;
    private Bitmap mBmpHour;
    private Bitmap mBmpMinute;
    private Bitmap mBmpSecond;
    private Bitmap mBmpDot;

    private BitmapDrawable bmdHour;
    private BitmapDrawable bmdMinute;
    private BitmapDrawable bmdSecond;
    private BitmapDrawable bmdDial;
    private BitmapDrawable bmdDot;

    private int mWidth;
    private int mHeigh;
    private int mTempWidth;
    private int mTempHeigh;
    private int centerX;
    private int centerY;

    private int availableWidth;
    private int availableHeight;


    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_TIME_DATE) || action.equals(ACTION_TIME_CHANGED) || action.equals(ACTION_TIMEZONE_CHANGED)) {
                notifyUpdateUI();
            }
        }
    };
    public AnalogicClockView(Context context) {
        super(context);
    }

    public AnalogicClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.viewmodule_AnalogicClockView);
        dateTextSize = a.getDimension(R.styleable.viewmodule_AnalogicClockView_viewmodule_analogicDateTextSize, DEFAULT_DATE_TEXT_SIZE);
        dateTextColor = a.getColor(R.styleable.viewmodule_AnalogicClockView_viewmodule_analogicDateTextColor, Color.WHITE);
        dialPlateSize = a.getInt(R.styleable.viewmodule_AnalogicClockView_viewmodule_analogic_size, DIAL_PLATE_SIZE_NORMAL);
        a.recycle();

        initResource();
        initPaint();
        registerTimeReceiver();

    }



    private void initPaint() {
        pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);

        datePaint = new Paint();
        datePaint.setTextSize(dateTextSize);
        datePaint.setColor(dateTextColor);
        datePaint.setTypeface(GlobalVars.getInstance().getDefaultFontRegular());
        datePaint.setTextAlign(Paint.Align.CENTER);
        datePaint.setAntiAlias(true);
    }

    private void initResource() {
        Resources res = getResources();
        weeks = res.getStringArray(R.array.viewmodule_week);
        days = res.getStringArray(R.array.viewmodule_day);
        months = res.getStringArray(R.array.viewmodule_month);

        mBmpSecond = BitmapFactory.decodeResource(getResources(),
                R.mipmap.minute_hand);
        bmdSecond = new BitmapDrawable(getResources(), mBmpSecond);

        setDialPlateResource(false);

        mBmpDot = BitmapFactory.decodeResource(getResources(),
                R.mipmap.clock_dot);

        bmdDot = new BitmapDrawable(getResources(), mBmpDot);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Log.d("samhung","dispatchDraw");
        canvas.setDrawFilter(pfd);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Calendar c = Calendar.getInstance();

        drawTimeView(canvas,c, showDate ? -70: 0);
        if (showDate) canvas.drawText(getDate(c), getWidth() / 2  , getHeight() / 2 + 230, datePaint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unRegisterTimeReceiver();
    }


    private void drawTimeView(
            Canvas canvas ,
            Calendar cal,
            int offsetY) {
        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);
        float hourRotate = hour * 30.0f + minute / 60.0f * 30.0f + 30;//24
        float minuteRotate = minute * 6.0f - 60;


        boolean scaled = false;

        centerX = getWidth() / 2 ;
        centerY = getHeight() / 2 + offsetY;

        if (availableWidth < mWidth || availableHeight < mHeigh) {
            scaled = true;
            float scale = Math.min((float) availableWidth / (float) mWidth,
                    (float) availableHeight / (float) mHeigh);
            canvas.save();
            canvas.scale(scale, scale, centerX, centerY);
        }


        bmdDial.setBounds(centerX - (mWidth / 2), centerY - (mHeigh / 2),
                centerX + (mWidth / 2), centerY + (mHeigh / 2));
        bmdDial.draw(canvas);


        mTempWidth = bmdMinute.getIntrinsicWidth();
        mTempHeigh = bmdMinute.getIntrinsicHeight();
        canvas.save();
        canvas.rotate(minuteRotate, centerX, centerY);


        bmdMinute.setBounds(
                centerX - 42 , centerY - 100,
                centerX + mTempWidth - 42 , centerY + mTempHeigh - 100);
        bmdMinute.draw(canvas);
        canvas.restore();

        mTempWidth = bmdHour.getIntrinsicWidth();
        mTempHeigh = bmdHour.getIntrinsicHeight();
        canvas.save();
        canvas.rotate(hourRotate, centerX, centerY);

        bmdHour.setBounds(
                centerX - 77 , centerY - 130,
                centerX + mTempWidth - 77 , centerY + mTempHeigh - 130);

        bmdHour.draw(canvas);

        canvas.restore();

        canvas.save();
        mTempWidth = bmdDot.getIntrinsicWidth();
        mTempHeigh = bmdDot.getIntrinsicHeight();
        bmdDot.setBounds(centerX - (mTempWidth / 2), centerY - (mTempHeigh / 2),
                centerX + (mTempWidth / 2), centerY + (mTempHeigh / 2));
        bmdDot.draw(canvas);


        if (scaled) {
            canvas.restore();
        }

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

    private String getDate(Calendar c) {
        String week = weeks[c.get(Calendar.DAY_OF_WEEK) - 1];
        String day = days[c.get(Calendar.DAY_OF_MONTH) - 1];
        String month = months[c.get(Calendar.MONTH)];
        String dateStr = week + "," + " " + day + " " + month;

        return dateStr;
    }

    private void notifyUpdateUI() {
        invalidate();
    }

    private synchronized void setDialPlateResource(boolean refresh) {
        int dialResouce =  R.mipmap.analogic_clock_dial_plate;
        int hourHandResource = R.mipmap.hour_hand;
        int minuteHandResource = R.mipmap.minute_hand;
        if (dialPlateSize == DIAL_PLATE_SIZE_BIG) {
            dialResouce = R.mipmap.analogic_clock_dial_plate_big;
            hourHandResource = R.mipmap.hour_hand_big;
            minuteHandResource = R.mipmap.minute_hand_big;
        }

        mBmpHour = BitmapFactory.decodeResource(getResources(),
                hourHandResource);
        bmdHour = new BitmapDrawable(getResources(), mBmpHour);

        mBmpMinute = BitmapFactory.decodeResource(getResources(),
                minuteHandResource);
        bmdMinute = new BitmapDrawable(getResources(), mBmpMinute);

        mBmpDial = BitmapFactory.decodeResource(getResources(),
                dialResouce);
        bmdDial = new BitmapDrawable(getResources(), mBmpDial);

        availableWidth = mBmpDial.getWidth();
        availableHeight = mBmpDial.getHeight();

        mWidth = mBmpDial.getWidth();
        mHeigh = mBmpDial.getHeight();
        centerX = availableWidth / 2;
        centerY = availableHeight / 2;

        if (refresh) {
            invalidate();
        }
    }

    public void setDateVisibility(boolean isShow) {
        showDate = isShow;
        notifyUpdateUI();
    }

    public void recycle() {
        mBmpDial.recycle();
        mBmpDial = null;

        mBmpHour.recycle();
        mBmpHour = null;

        mBmpMinute.recycle();
        mBmpMinute = null;

        mBmpSecond.recycle();
        mBmpSecond = null;

        mBmpDot.recycle();
        mBmpDot = null;
    }

    public void setDialPlateSize(int dialPlateSize) {
        this.dialPlateSize = dialPlateSize;
        setDialPlateResource(true);
    }

    public boolean isShowDate() {
        return showDate;
    }
}
