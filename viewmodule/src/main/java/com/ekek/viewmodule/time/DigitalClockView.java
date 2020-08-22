package com.ekek.viewmodule.time;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.utils.DateTimeUtil;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.viewmodule.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DigitalClockView extends View {
    private static final int DEFAULT_TIME_TEXT_SIZE = 60;
    private static final int DEFAULT_DATE_TEXT_SIZE = 40;
    private static final boolean DEFAULT_DATE_VIEW_VISIBILITY = true;
    private static final String ACTION_TIME_DATE = Intent.ACTION_TIME_TICK;
    private static final String ACTION_TIME_CHANGED = Intent.ACTION_TIME_CHANGED;
    private static final String ACTION_TIMEZONE_CHANGED = Intent.ACTION_TIMEZONE_CHANGED;
    private float timeTextSize,dateTextSize;
    private int timeTextColor,dateTextColor;
    private Paint timePaint,datePaint,ampmPaint;
    private String[] weeks, days, months;
    private boolean isShowDate = DEFAULT_DATE_VIEW_VISIBILITY;
    private boolean is24Format = true;
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
    public DigitalClockView(Context context) {
        super(context);

    }

    public DigitalClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.viewmodule_DigitalClockView);
        timeTextSize = a.getDimension(R.styleable.viewmodule_DigitalClockView_viewmodule_digitalTimeTextSize, DEFAULT_TIME_TEXT_SIZE);
        dateTextSize = a.getDimension(R.styleable.viewmodule_DigitalClockView_viewmodule_digitalDateTextSize, DEFAULT_DATE_TEXT_SIZE);
        timeTextColor = a.getColor(R.styleable.viewmodule_DigitalClockView_viewmodule_digitalTimeTextColor, Color.WHITE);
        dateTextColor = a.getColor(R.styleable.viewmodule_DigitalClockView_viewmodule_digitalDateTextColor, Color.WHITE);
        a.recycle();

        initResource();
        initPaint();
        registerTimeReceiver();
    }


    private void initPaint() {
        timePaint = new Paint();
        timePaint.setTextSize(timeTextSize);
        timePaint.setColor(timeTextColor);
        timePaint.setTypeface(GlobalVars.getInstance().getDefaultFontRegular());
        timePaint.setTextAlign(Paint.Align.CENTER);
        timePaint.setAntiAlias(true);

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
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Calendar c = Calendar.getInstance();
        String timeStr;
        String[] strs;
        String timevalue;
        int timeHour,timeMinute;


        if(is24Format){  // 24 小时格式
            timeStr  = DateTimeUtil.getTimeStr(is24Format);
            strs = timeStr.split(":");
            timeHour=Integer.valueOf(strs[0]);
            timevalue= String.valueOf(timeHour)+":"+strs[1];
        }else {  // 12 小时格式
            timeStr  = DateTimeUtil.getTimeStr(is24Format);
            strs = timeStr.split(":");
            timeHour=Integer.valueOf(strs[0]);
            strs = strs[1].split(" ");
            timevalue= String.valueOf(timeHour)+":"+strs[0];
        }


        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        int verticalOffset = getVerticalOffset(timePaint);

        canvas.drawText(timevalue, centerX, centerY + verticalOffset, timePaint);

        if (isShowDate){
            if(is24Format){
                canvas.drawText(getDate(c), centerX, centerY + 180, datePaint);
            }else {
                if(timevalue.length()>=5){  // 12:59
                    canvas.drawText(getDate(c), centerX+41, centerY + 180, datePaint);
                }else {  // 1:29
                    canvas.drawText(getDate(c), centerX+42, centerY + 180, datePaint);
                }
            }
        }


        if (is24Format){

        }else {
            if(timevalue.length()>=5){  // 12:59
                canvas.drawText(strs[1], centerX+260, centerY + verticalOffset, datePaint);
            }else {  // 1:29
                canvas.drawText(strs[1], centerX+210, centerY + verticalOffset, datePaint);
            }

        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unRegisterTimeReceiver();
    }

    public void onLanguageChanged() {
        timePaint.setTypeface(GlobalVars.getInstance().getDefaultFontRegular());
        datePaint.setTypeface(GlobalVars.getInstance().getDefaultFontRegular());
    }

    private String getTime(Calendar c) {

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String timeStr = format.format(c.getTime());
        String[] strs = timeStr.split(":");
        int timeHour,timeMinute;
        timeHour=Integer.valueOf(strs[0]);
        timeStr=String.valueOf(timeHour)+":"+strs[1];
        return timeStr;

    }

    private int getVerticalOffset(Paint textPaint) {
        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        return (int)((metrics.bottom - metrics.top) / 2 - metrics.bottom);
    }

    private String getDate(Calendar c) {

        String fullString = DateTimeUtil.formatDateTime(
                c.getTime(),
                GlobalVars.getInstance().getCurrentLocale(),
                DateFormat.FULL);
        return removeYearPart(
                fullString,
                Integer.toString(c.get(Calendar.YEAR)),
                GlobalVars.getInstance().getCurrentLocale());
    }

    private String removeYearPart(String dateString, String year, Locale locale) {
        switch (locale.toString()) {
            case "en":
                return dateString.substring(0, dateString.indexOf(year) - 2);
            case "es":
                return dateString.substring(0, dateString.indexOf(year) - 4);
            case "de":
                return dateString.substring(0, dateString.indexOf(year) - 1);
            case "fr":
                return dateString.substring(0, dateString.indexOf(year) - 1);
            case "it":
                return dateString.substring(0, dateString.indexOf(year) - 1);
            case "nl":
                return dateString.substring(0, dateString.indexOf(year) - 1);
            case "pt":
                return dateString.substring(0, dateString.indexOf(year) - 4);
            case "da":
                return dateString.substring(0, dateString.indexOf(year) - 1);
            case "sv":
                return dateString.substring(0, dateString.indexOf(year) - 1);
            case "no":
                return dateString.substring(0, dateString.indexOf(year) - 1);
            case "fi":
                return dateString.substring(0, dateString.indexOf(year) - 1);
            case "el":
                return dateString.substring(0, dateString.indexOf(year) - 1);
            case "ru":
                return dateString.substring(0, dateString.indexOf(year) - 1);
            case "pl":
                return dateString.substring(0, dateString.indexOf(year) - 1);
            case "hu":
                return dateString.substring(year.length() + 2);
            case "zh":
                return dateString.substring(year.length() + 1);
            case "vi":
                return dateString.substring(0, dateString.indexOf(year) - 2);
            case "ko":
                return dateString.substring(year.length() + 2);
        }

        return dateString;
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

    public void setDateVisibility(boolean isShow) {
        isShowDate = isShow;
        notifyUpdateUI();
    }

    public void setTimeFormat24(boolean flag){
        is24Format=flag;
       // LogUtil.d("liang the format is = "+is24Format);
    }

}
