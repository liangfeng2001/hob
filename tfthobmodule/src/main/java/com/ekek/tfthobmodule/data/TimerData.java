package com.ekek.tfthobmodule.data;

import android.support.annotation.NonNull;

import com.ekek.commonmodule.utils.LogUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class TimerData implements ITimerData {
    private static final int INVALID_DATA = -1;
    private static final int TIME_EIGHT_SECOND_COUNT = 8;//8s
   // private static final int TIME_FIVE_MINUTE_COUNT = 10;//5 min
    private static final int TIME_FIVE_MINUTE_COUNT = 5 * 60;//5 min
    private int targetHourValue = INVALID_DATA;//目标计时的小时
    private int targetMinuteValue = INVALID_DATA;//目标计时的分
    private int targetSecondValue = INVALID_DATA;//目标计时的秒
    private int remainHourValue = 0;//剩余时
    private int remainMinuteValue = 0;//剩余分
    private int remainSecondValue = 60;//剩余秒
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private Disposable eightSecondDisposable;//8秒计时器
    private Disposable mDisposable;//定时器
    private Disposable fiveMinuteDisposable;//5分钟定时器，如果B档，经过5分钟后降为9档
    private int remainSecondValueForBGear = 0;//5分钟，300秒
    @NonNull
    private TimerDataCallback mCallback;

    @Override
    public void setTimer(int hour, int minute, int second) {
        feedTimerValue(hour,minute,second);
    }

    @Override
    public void startCountdown(int hour, int minute, int second) {
        LogUtil.e("Enter:: startCountdown-----------------------1");
        //doStopCountdown();

        if (remainHourValue == 0 && remainMinuteValue == 0 && remainSecondValue == 60) {
            feedTimerValue(hour,minute,second);
        }

        doStartCountdown();
    }

    private void doStartCountdown() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            LogUtil.e("Enter:: startCountdown------------------2");
            //mDisposable.isDisposed();
            return;
        }
        LogUtil.e("Enter:: startCountdown------------------3");
        mDisposable = Observable
                //.interval(0,1, TimeUnit.SECONDS)
                .interval(0,1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        //LogUtil.d("Enter:: countdown----->" + aLong);
                        LogUtil.d("hour--->" + remainHourValue + "----minute--->" + remainMinuteValue + "---->" + remainSecondValue);
                        updateRemainTime();


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
        if (remainSecondValue != 0) {
            remainSecondValue--;
            LogUtil.e("Enter:: -----------------onUpateTimer--------------1--------------->" + remainSecondValue);
        }else {

            remainSecondValue = 60;
            if (remainMinuteValue != 0) remainMinuteValue--;
            else {
                remainMinuteValue = 59;
                if (remainHourValue != 0) {
                    remainHourValue--;
                }
            }
            LogUtil.e("Enter:: -----------------onUpateTimer---------------2------------minute-->" + remainMinuteValue);
            mCallback.onUpateTimer(remainHourValue,remainMinuteValue);
        }


       /* if (remainSecondValue != 59) {
            remainSecondValue++;
        }else {
            remainSecondValue = 0;
            if (remainMinuteValue != 0) remainMinuteValue--;
            if (remainHourValue != 0) {
                if (remainMinuteValue == 0) {
                    remainMinuteValue = 59;
                    if (remainHourValue != 0) remainHourValue--;

                }
            }
        }*/
        //if (remainHourValue == 0 && remainMinuteValue == 0 && remainSecondValue == 0) {
        if (remainHourValue == 0 && remainMinuteValue == 0 && remainSecondValue == 60) {
        //if (remainHourValue == 0 && remainMinuteValue == 0) {
            LogUtil.d("Enter:: updateRemainTime time is up");
            doStopCountDown();
            mCallback.onTimeIsUp();
        }
        //LogUtil.d(remainHourValue + "------" + remainMinuteValue + "---------" + remainSecondValue);
    }

    private void doStopCountDown() {
        if (mDisposable != null) {
            if (!mDisposable.isDisposed()) {
                mDisposable.dispose();
            }
            mCompositeDisposable.remove(mDisposable);
        }

    }

    @Override
    public void pauseCountdown() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            mCompositeDisposable.remove(mDisposable);
        }
    }

    @Override
    public void resumeCountdown() {
        doResumeCountdown();
    }

    private void doResumeCountdown() {
        doStartCountdown();
    }

    @Override
    public void stopCountdown() {
        doStopCountdown();
        resetTimerValue();
    }

    private void doStopCountdown() {
        if (mDisposable != null) {
            if (!mDisposable.isDisposed()) {
                mDisposable.dispose();
                mCompositeDisposable.remove(mDisposable);

            }
        }

    }

    @Override
    public void startEightSecondCountdown() {
        LogUtil.d("startEightSecondCountdown");
        doEightSecondCountdown();

    }

    private void doEightSecondCountdown() {
        eightSecondDisposable = Observable
                .interval(0,TIME_EIGHT_SECOND_COUNT, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        doStopEightSecondCountDown();
                        mCallback.onEightSecondTimeIsUp();


                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribe();
        mCompositeDisposable.add(eightSecondDisposable);
    }

    @Override
    public void stopEightSecondCountdown() {
        doStopEightSecondCountDown();


    }

    @Override
    public void startFiveMinuteCountdown() {
        doStartFiveMinuteCountdown();
    }

    private void doStartFiveMinuteCountdown() {
        if (remainSecondValueForBGear == 0) remainSecondValueForBGear = 300;
        doStopFiveMinuteCountdown();
        fiveMinuteDisposable = Observable
                //.interval(0,TIME_FIVE_MINUTE_COUNT, TimeUnit.SECONDS)
                //.interval(TIME_FIVE_MINUTE_COUNT,0, TimeUnit.SECONDS)
                .interval(0,1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        LogUtil.d("Enter:: doStartFiveMinuteCountdown------>" + aLong);
                        remainSecondValueForBGear--;
                        if (remainSecondValueForBGear == 0) {
                            doStopFiveMinuteCountdown();
                            mCallback.onFiveMinuteTimeisUp();
                        }



                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {//68

                    }
                })
                .subscribe();
        mCompositeDisposable.add(fiveMinuteDisposable);
    }



    @Override
    public void stopFiveMinuteCountdown() {
        doStopFiveMinuteCountdown();
        remainSecondValueForBGear = 0;
    }

    @Override
    public void pauseFiveMinuteCountdown() {
        doStopFiveMinuteCountdown();
    }

    private void doStopFiveMinuteCountdown() {
        if (fiveMinuteDisposable != null && !fiveMinuteDisposable.isDisposed()) {
            LogUtil.d("Enter:: doStopFiveMinuteCountdown");
            fiveMinuteDisposable.dispose();
            mCompositeDisposable.remove(fiveMinuteDisposable);
        }
    }

    private void doStopEightSecondCountDown() {
        if (eightSecondDisposable != null && !eightSecondDisposable.isDisposed()) {
            eightSecondDisposable.dispose();
            mCompositeDisposable.remove(eightSecondDisposable);
        }
    }

    @Override
    public void recyle() {
        mCompositeDisposable.clear();
    }

    @Override
    public boolean isTimerFinish() {
        return false;
    }

    @Override
    public void setTimerDataCallback(@NonNull TimerDataCallback callback) {
        mCallback = callback;
    }

    public int[] getTimerTargetValue() {
        int[] values = {targetHourValue,targetMinuteValue,targetSecondValue};
        return values;
    }

    public int[] getTimerRemainValue() {
        int[] values = {remainHourValue,remainMinuteValue,remainSecondValue};
        return values;
    }

    public void resetTimerValue() {
        targetHourValue = INVALID_DATA;
        targetMinuteValue = INVALID_DATA;
        targetSecondValue = INVALID_DATA;
        remainHourValue = 0;
        remainMinuteValue = 0;
        remainSecondValue = 60;
    }

    private void feedTimerValue(int hour, int minute, int second) {
        targetHourValue = hour;
        targetMinuteValue = minute;
        targetSecondValue = second;
        remainHourValue = targetHourValue;
        remainMinuteValue = targetMinuteValue;
        remainSecondValue = targetSecondValue;
    }

    private DisposableObserver getObserver(final int id) {
        DisposableObserver disposableObserver = new DisposableObserver<Object>() {
            @Override
            public void onNext(Object o) {
                LogUtil.d("timer----------->" + String.valueOf(o));
               // Log.d(id + "数据", String.valueOf(o))
            }

            @Override
            public void onComplete() {
                //Log.d(id + TAG, "onComplete");
            }

            @Override
            public void onError(Throwable e) {
               // Log.e(id + TAG, e.toString(), e);
            }
        };

        return disposableObserver;
    }

    private DisposableObserver getObserver1(final int id) {
        DisposableObserver disposableObserver = new DisposableObserver<Object>() {
            @Override
            public void onNext(Object o) {
                LogUtil.d("timer----------------------------------->" + String.valueOf(o));
                //mCompositeDisposable.dispose();
                // Log.d(id + "数据", String.valueOf(o));

            }

            @Override
            public void onComplete() {
                LogUtil.d("timer---------------------------------->complete" );
            }

            @Override
            public void onError(Throwable e) {
                // Log.e(id + TAG, e.toString(), e);
            }
        };

        return disposableObserver;
    }

}
