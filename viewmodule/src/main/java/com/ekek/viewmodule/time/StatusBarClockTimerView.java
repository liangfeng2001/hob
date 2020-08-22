package com.ekek.viewmodule.time;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.utils.DateTimeUtil;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.commonmodule.utils.Logger;
import com.ekek.viewmodule.R;
import com.ekek.viewmodule.R2;
import com.ekek.viewmodule.events.CountDownEvent;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class StatusBarClockTimerView extends LinearLayout {
    private static final String ACTION_TIME_DATE = Intent.ACTION_TIME_TICK;
    private static final String ACTION_TIME_CHANGED = Intent.ACTION_TIME_CHANGED;
    private static final String ACTION_TIMEZONE_CHANGED = Intent.ACTION_TIMEZONE_CHANGED;
    private static final String ACTION_CHANGE_12_24_FORMAT="ACTION_CHANGE_12_24_FORMAT";
    @BindView(R2.id.tv_clock)
    TextView tvClock;
    @BindView(R2.id.tv_timer)
    TextView tvTimer;
    Unbinder unbinder;
    @BindView(R2.id.ll_clock_timer_view)
    LinearLayout llClockTimerView;
    private boolean isTimeReceiverRegister = false;
    private boolean hasTimer = false;
    private int remainHour,remainMinute , remainSecond;
    private Disposable mDisposable;//定时器
    private Disposable mForSecondDot;  //  时间中间的点 更新 2018年9月24日8:59:42
    private OnStatusBarClockTimerListener mListener;
    private boolean ShowSecond_Flag=true;

    private String[] strs;
    private String  TimeBarTextView_ContentOn;
    private String mShowAlarmTimer="00:00";

    private boolean is24Format=true;


    private Handler handler;

    {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        if(ShowSecond_Flag){
                        //    timeStr=strs[0]+":"+strs[1];
                          //  tvClock.setText(TimeBarTextView_ContentOn);
                          /*  SpannableString spannableString = new SpannableString(TimeBarTextView_ContentOn);
                            spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), TimeBarTextView_ContentOn.indexOf(":"), TimeBarTextView_ContentOn.indexOf(":") + 1, Spannable .SPAN_INCLUSIVE_EXCLUSIVE);
                            tvClock.setText(spannableString);*/

                            tvTimer.setText(mShowAlarmTimer);
                            SpannableString  spannableString1 = new SpannableString(mShowAlarmTimer);
                            spannableString1.setSpan(new ForegroundColorSpan(Color.WHITE), mShowAlarmTimer.indexOf(":"), mShowAlarmTimer.indexOf(":") + 1, Spannable .SPAN_INCLUSIVE_EXCLUSIVE);
                            tvTimer.setText(spannableString1);

                        }else {
                           // timeStr=strs[0]+" "+strs[1];
                        //    tvClock.setText(strs[0]+" "+strs[1]);
                       //     tvClock.setText(TimeBarTextView_ContentOn);
                           /* SpannableString spannableString3 = new SpannableString(TimeBarTextView_ContentOn);
                            spannableString3.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), TimeBarTextView_ContentOn.indexOf(":"), TimeBarTextView_ContentOn.indexOf(":") + 1, Spannable .SPAN_INCLUSIVE_EXCLUSIVE);
                            tvClock.setText(spannableString3);*/

                            tvTimer.setText(mShowAlarmTimer);
                            SpannableString spannableString2 = new SpannableString(mShowAlarmTimer);
                            spannableString2.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), mShowAlarmTimer.indexOf(":"), mShowAlarmTimer.indexOf(":") + 1, Spannable .SPAN_INCLUSIVE_EXCLUSIVE);
                            tvTimer.setText(spannableString2);

                        }
                        ShowSecond_Flag=!ShowSecond_Flag; //屏蔽：不闪中间的点；不屏蔽：闪中间的点
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_TIME_DATE) || action.equals(ACTION_TIME_CHANGED)
                    || action.equals(ACTION_TIMEZONE_CHANGED)||action.equals(ACTION_CHANGE_12_24_FORMAT))  {
                notifyUpdateUI();
            }
        }
    };
    public StatusBarClockTimerView(Context context) {
        this(context, null);
    }

    public StatusBarClockTimerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View mRootView = LayoutInflater.from(context).inflate(R.layout.viewmodule_layout_status_bar_clock_timer, this);
        unbinder = ButterKnife.bind(this, mRootView);

        Typeface typeface = GlobalVars.getInstance().getDefaultFontRegular();

        tvClock.setTypeface(typeface);
        tvTimer.setTypeface(typeface);

        notifyUpdateUI();
        updateTimerUI();
        registerTimeReceiver();
      //  initSetTimeSecondClock();
        ForSecondDot();

    }
  /*  private void initSetTimeSecondClock(){


        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        };
        timer.schedule(timerTask, 0, 1000);// 1000 毫秒更新一次
    }*/

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unRegisterTimeReceiver();
    }

    public void setTimeFormat24(boolean flag){
        is24Format=flag;
     //   LogUtil.d("liang the format is = "+is24Format);
    }

    public void setClockTimerViewOrientation(int orientation) {
        //llClockTimerView.setOrientation(LinearLayout.HORIZONTAL);
        llClockTimerView.setOrientation(orientation);
    }

    private void notifyUpdateUI() {
       // tvClock.setText(getTime(Calendar.getInstance()));
      //  is24Format= SettingPreferencesUtil.getTimeFormat24(this);
      //  getTime(Calendar.getInstance());

        //   tvClock.setText(getTime(Calendar.getInstance()));
        //  tvClock.setText(DateTimeUtil.getTimeStr(is24Format));
      //  is24Format = SettingPreferencesUtil.getTimeFormat24(getContext());
        String timeStr= DateTimeUtil.getTimeStr(is24Format);
        String[] strs = timeStr.split(":");
        int timeHour,timeMinute;
        timeHour=Integer.valueOf(strs[0]);
        tvClock.setText(String.valueOf(timeHour)+":"+strs[1]);
    }

    private String getTime(Calendar c) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String timeStr = format.format(c.getTime());

        strs = timeStr.split(":");
        TimeBarTextView_ContentOn=strs[0]+":"+strs[1];


      //  String[] strs = timeStr.split(":");
        int timeHour,timeMinute;
        timeHour=Integer.valueOf(strs[0]);
        TimeBarTextView_ContentOn=String.valueOf(timeHour)+":"+strs[1];
        return timeStr;

    }

    private void registerTimeReceiver() {
        if (isTimeReceiverRegister)return;
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_TIME_DATE);
        filter.addAction(ACTION_TIME_CHANGED);
        filter.addAction(ACTION_TIMEZONE_CHANGED);
        filter.addAction(ACTION_CHANGE_12_24_FORMAT);
        getContext().registerReceiver(receiver, filter);
        isTimeReceiverRegister = true;

    }

    private void unRegisterTimeReceiver() {
        if (isTimeReceiverRegister) {
            getContext().unregisterReceiver(receiver);
            isTimeReceiverRegister = false;
        }
    }

    private void showTimerUI() {
        if (tvTimer.getVisibility() == INVISIBLE) tvTimer.setVisibility(VISIBLE);
    }

    private void hideTimerUI() {
        if (tvTimer.getVisibility() == VISIBLE) tvTimer.setVisibility(INVISIBLE);
    }

    private void updateTimerUI() {
        if (hasTimer) {

            tvTimer.setText(String.format("%02d", remainHour) + ":" + String.format("%02d", remainMinute));
            mShowAlarmTimer=String.format("%02d", remainHour) + ":" + String.format("%02d", remainMinute);
            showTimerUI();
        }else {
            hideTimerUI();
        }

    }
private void ForSecondDot(){

    mForSecondDot = Observable
            //.interval(0,1, TimeUnit.SECONDS)
            .interval(0,1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Exception {
                    //updateRemainTime();
                    handler.sendEmptyMessage(1);


                }
            })
            .doOnComplete(new Action() {
                @Override
                public void run() throws Exception {

                }
            })
            .subscribe();
}
    private void startCountdown() {
        Logger.getInstance().i("startCountdown()", true);
        doStopCountDown();
        mDisposable = Observable
                //.interval(0,1, TimeUnit.SECONDS)
                .interval(0,1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        updateRemainTime();
                        EventBus.getDefault().post(new CountDownEvent());
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribe();
    }

    private void updateRemainTime() {//period： 1s
        if (remainSecond != 0) {
            remainSecond--;
        }else {
            remainSecond = 60;
            if (remainMinute != 0) remainMinute--;
            else {
                remainMinute = 59;
                if (remainHour!= 0) {
                    remainHour--;
                }
            }
            updateTimerUI();
        }

        if (remainHour == 0 && remainMinute == 0 && remainSecond == 60) {
            doStopCountDown();
            mListener.onStatusBarClockTimerIsUp();
        }
    }

    private void doStopCountDown() {
        if (mDisposable != null) {
            if (!mDisposable.isDisposed()) {
                mDisposable.dispose();
                Logger.getInstance().i("doStopCountDown()");
            }
        }
    }

    public void setOnStatusBarClockTimerListener(OnStatusBarClockTimerListener listener) {
        mListener = listener;
    }

    public void setAndStartTimer(int hour, int minute) {
        hasTimer = true;
        remainHour = hour;
        remainMinute = minute;
        remainSecond = 60;
        updateTimerUI();
        startCountdown();
    }

    public void setAndStartTenMinuteTimer() {
        hasTimer = true;
        remainHour = 0;
        remainMinute = 10;
        remainSecond = 60;
        updateTimerUI();
        startCountdown();
    }


    public void closeTimer() {
        doStopCountDown();
        hasTimer = false;
        if (tvTimer.getVisibility() == VISIBLE) tvTimer.setVisibility(INVISIBLE);
    }

    public interface OnStatusBarClockTimerListener {
        void onStatusBarClockTimerIsUp();


    }

    public boolean getTimerIsWorking(){

        return hasTimer;
    }

    public int [] getRemainingTime(){
        int [] value =new int[2];
        value[0]=remainHour;
        value[1]=remainMinute;
        return value;
    }
}
