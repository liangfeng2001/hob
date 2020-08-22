package com.ekek.commonmodule.utils;

import android.content.Context;
import android.content.Intent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtil {
    private static final int SET_DATE_TIME_TIME_OUT = 3 * 1000;
    private static final int SET_DATE_TIME_RETRY_TIMES = 3;
    private static final int SET_DATE_TIME_WAIT_FOR_FINISH_TIME_OUT = 20;
    private static final String FMT_LONG_DATE = "yyyy-MM-dd HH:mm:ss";

    public static ArrayList<String> getYears(int startYear, int endYear) {
        ArrayList<String> years = new ArrayList<>();
        for (int i = startYear; i < endYear; i++) {
            years.add(String.format("%s", i));
        }
        return years;
    }

    public static ArrayList<String> getDays(int year,int month) {
        int dayCount = getDaysOfMonth(year,month);
        ArrayList<String> days = new ArrayList<>();
        for (int i = 1; i <= dayCount; i++) {
            days.add(String.format("%s", i));
        }
        return days;
    }

    public static int getDaysOfMonth(int year,int month) {
        Calendar c=Calendar.getInstance();
        c.clear();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month - 1);
        int day = c.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数
        //LogUtil.d("current days ------->" + day);
        return day;

    }

    public static ArrayList<String> getMinutes() {
        ArrayList<String> minutes = new ArrayList<>();
        for (int i = 0; i <= 59; i++) {
            minutes.add(String.format("%02d", i));
        }
        return minutes;
    }

    public static ArrayList<String> getHours() {
        ArrayList<String> hours = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            hours.add(String.format("%s", i));
        }
        return hours;
    }

    public static ArrayList<String> getHoursFor24Format() {
        ArrayList<String> hours = new ArrayList<>();
        for (int i = 0; i <= 23; i++) {
            hours.add(String.format("%s", i));
        }
        return hours;
    }

    public static String getTimeStr(boolean is24Format) {
        Calendar c = Calendar.getInstance();
        String timeStr = "";
        if (is24Format) {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            timeStr = format.format(c.getTime());
        }else {
            //SimpleDateFormat format = new SimpleDateFormat("hh:mm");
            SimpleDateFormat format = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
            timeStr = format.format(c.getTime());
        }

        return timeStr;
    }

    public final static String dateToStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(FMT_LONG_DATE);
        String str = format.format(date);
        return str;
    }
    public final static Date strToDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat(FMT_LONG_DATE);
        Date date = null;
        try {
            date = format.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
    public static int getDayInterval(Date startDate, Date endDate) {
        if (startDate==null |endDate==null)
            return 0 ;
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
    }

    public static String millisecondToString(long milliseconds) {

        long ms = milliseconds;
        int hours = (int)(ms / 1000 / 60 / 60);
        ms -= hours * 60 * 60 * 1000;
        int minutes = (int)(ms / 1000 / 60);
        ms -= minutes * 60 * 1000;
        int seconds = (int)(ms / 1000);
        ms -= seconds * 1000;

        String format = "%02d:%02d:%02d.%03d";
        return String.format(Locale.ENGLISH, format, hours, minutes, seconds, (int)ms);
    }

    public static String formatDateTime() {
        return formatDateTime(System.currentTimeMillis());
    }
    public static String formatDateTime(long when) {
        return formatDateTime(new Date(when));
    }
    public static String formatDateTime(Date when) {
        return formatDateTime(when, Locale.getDefault());
    }
    public static String formatDateTime(Date when, Locale locale) {
        return formatDateTime(when, locale, DateFormat.DEFAULT);
    }

    /**
     * format datetime using DateFormat class
     * @param when the date
     * @param locale the locale
     * @param style the style
     *              DateFormat.FULL
     *              DateFormat.LONG
     *              DateFormat.MEDIUM
     *              DateFormat.SHORT
     * @return
     */
    public static String formatDateTime(Date when, Locale locale, int style) {
        DateFormat dateFormat = DateFormat.getDateInstance(style, locale);
        return dateFormat.format(when);
    }

    public static void changeSystemDate(Context context, int year, int month, int day) {
        ChangeSystemDateTimeThread thread = new ChangeSystemDateTimeThread(
                context,
                year,
                month,
                day);
        thread.start();
        try {
            thread.join(SET_DATE_TIME_TIME_OUT);
        } catch (InterruptedException e) {
            LogUtil.e(e.getMessage());
        }
    }

    public static void changeSystemTime(Context context, int hour, int minute) {
        ChangeSystemDateTimeThread thread = new ChangeSystemDateTimeThread(
                context,
                hour,
                minute);
        thread.start();
        try {
            thread.join(SET_DATE_TIME_TIME_OUT);
        } catch (InterruptedException e) {
            LogUtil.e(e.getMessage());
        }
    }

    static class ChangeSystemDateTimeThread extends Thread {

        final String ACTION_SET_DATE = "ACTION_EKEK_SET_DATE";
        final String ACTION_SET_TIME = "ACTION_EKEK_SET_TIME";

        final int SET_DATE = 0;
        final int SET_TIME = 1;
        Context context;
        int year;
        int month;
        int day;
        int hour;
        int minute;
        int setType;
        boolean setResult = false;

        ChangeSystemDateTimeThread(Context context, int year, int month, int day) {
            this.context = context;
            this.year = year;
            this.month = month;
            this.day = day;
            setType = SET_DATE;
        }

        ChangeSystemDateTimeThread(Context context, int hour, int minute) {
            this.context = context;
            this.hour = hour;
            this.minute = minute;
            setType = SET_TIME;
        }

        @Override
        public void run() {
            for (int i = 0; i < SET_DATE_TIME_RETRY_TIMES; i++) {
                doSetDateTime();
                if (setResult) {
                    break;
                }
            }
        }

        private void doSetDateTime() {
            Intent intent = new Intent();
            if (setType == SET_DATE) {
                intent.setAction(ACTION_SET_DATE);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", day);
            } else if (setType == SET_TIME) {
                intent.setAction(ACTION_SET_TIME);
                intent.putExtra("hour", hour);
                intent.putExtra("minute", minute);
            }

            context.sendBroadcast(intent);

            boolean changed = false;
            int waitTimes = 0;
            while (!changed) {
                Calendar c = Calendar.getInstance();
                changed = (setType == SET_DATE
                        && c.get(Calendar.YEAR) == year
                        && c.get(Calendar.MONTH) + 1 == month
                        && c.get(Calendar.DATE) == day);
                changed = changed || (setType == SET_TIME
                        && c.get(Calendar.HOUR_OF_DAY) == hour
                        && c.get(Calendar.MINUTE) == minute);

                if (changed) {
                    setResult = true;
                    break;
                }

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    LogUtil.e(e.getMessage());
                }

                waitTimes++;
                if (waitTimes > SET_DATE_TIME_WAIT_FOR_FINISH_TIME_OUT) {
                    break;
                }
            }
        }
    }
}
