package com.ekek.settingmodule.view;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.commonmodule.utils.Logger;
import com.ekek.settingmodule.R;
import com.ekek.settingmodule.R2;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.dialog.ToastDialog;
import com.ekek.settingmodule.model.AlarmClockIsEditedEvent;
import com.ekek.settingmodule.model.PlaySoundWhenSetTime;
import com.ekek.settingmodule.utils.TimerPickerUtil;
import com.ekek.viewmodule.product.ProductManager;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.ekek.viewmodule.common.NumberPickerView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AlarmClockSettingFragment extends BaseFragment implements NumberPickerView.OnValueChangeListener {
    public static final int REQUEST_TIMER_SETTING_HOD = 0;
    public static final int REQUEST_TIMER_SETTING_HOOD = 1;
    @BindView(R2.id.npv_timer_hour)
    NumberPickerView npvTimerHour;
    @BindView(R2.id.npv_timer_minute)
    NumberPickerView npvTimerMinute;
    @BindView(R2.id.ib_timer_setting_back)
    ImageButton ibTimerSettingBack;
    @BindView(R2.id.ib_timer_setting_cancel)
    ImageButton ibTimerSettingCancel;
    @BindView(R2.id.tv_timer_content)
    TextView tvTimerContent;
    @BindView(R2.id.tv_timer_center)
    TextView tvTimerCenter;
    @BindView(R2.id.ll_timer)
    LinearLayout llTimer;
    @BindView(R2.id.iv_info)
    ImageView ivInfo;
    Unbinder unbinder;
    private String[] hours = new String[24];
    private String[] minutes = new String[60];
    private int from = REQUEST_TIMER_SETTING_HOD;
    private int preSettingHour = 1;
    private int preSettingMinute = 0;
    private Typeface typeface;

    private int mInitSettingHour=0;
    private int mInitSettingMinute=0;
    private Disposable mDisposable;//定时器
    private long mTouchTime=SystemClock.elapsedRealtime();
    private boolean fragmentIsShow=true;

    public AlarmClockSettingFragment() {

    }

    @SuppressLint("ValidFragment")
    public AlarmClockSettingFragment(int from, int hour, int minute) {//from: come from hob or hood ?
        initAlarmTimer(from, hour, minute);

    }

    public void initAlarmTimer(int from, int hour, int minute) {
        this.from = from;
        preSettingHour = hour;
        preSettingMinute = minute;
        //LogUtil.d("preSettingHour---->" + preSettingHour);
    }

    @Override
    public int initLyaout() {
        return R.layout.settingmodule_fragment_timer_setting;
    }

    @Override
    public void initListener() {

    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        //LogUtil.d("Enter:: initAllMembersView");
        SetFont();
        initTimerView();
        if(ProductManager.PRODUCT_TYPE== ProductManager.Haier){
            llTimer.setBackgroundResource(R.mipmap.bg_timer_setting_haier);
        }else {

        }
       /* llTimer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
               // processOperateAreaTouchEvent(v,event);
                return true;
            }
        });*/

    }

    private void SetFont() {

        typeface = GlobalVars.getInstance().getDefaultFontBold();
        npvTimerMinute.setContentTextTypeface(typeface);
        npvTimerMinute.setHintTextTypeface(typeface);

        npvTimerHour.setContentTextTypeface(typeface);
        npvTimerHour.setHintTextTypeface(typeface);
        tvTimerCenter.setTypeface(typeface);

    }

    private void initTimerView() {

        for (int i = 23; i >= 0; i--) {

            hours[23 - i] = String.format("%02d", i);
        }


        for (int i = 59; i >= 0; i--) {
            minutes[59 - i] = String.format("%02d", i);
        }

        npvTimerHour.setDisplayedValues(hours);
        npvTimerHour.setOnValueChangedListener(this);
        //npvFireGear.setOnScrollListener(this);
        npvTimerHour.setMaxValue(hours.length - 1);
        npvTimerHour.setMinValue(0);
       /* if (mCookerSettingData.getTimerHourValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {
            npvTimerHour.setValue(TimerPickerUtil.getTimerHourIndex(hours, String.valueOf(mCookerSettingData.getTimerHourValue())));
        } else {
            npvTimerHour.setValue(22);
        }*/

        npvTimerHour.setValue(TimerPickerUtil.getTimerHourIndex(hours, String.valueOf(preSettingHour)));

        // npvTimerHour.setValue(22);

        npvTimerHour.setFriction(5 * ViewConfiguration.get(getActivity()).getScrollFriction());
        npvTimerHour.setItemScrollListener(new NumberPickerView.ItemScrollListener() {
            @Override
            public void OnItemScroll(float itemHeight, float totalScrollY) {
                EventBus.getDefault().post(new PlaySoundWhenSetTime(CataSettingConstant.PLAY_SOUND_TYPE_LIST));
            }
        });

        npvTimerMinute.setDisplayedValues(minutes);
        npvTimerMinute.setOnValueChangedListener(this);
        //npvFireGear.setOnScrollListener(this);
        npvTimerMinute.setMaxValue(minutes.length - 1);
        npvTimerMinute.setMinValue(0);/*
        if (mCookerSettingData.getTimerMinuteValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {
            npvTimerMinute.setValue(TimerPickerUtil.getTimerMinuteIndex(minutes, String.valueOf(mCookerSettingData.getTimerMinuteValue())));

        } else {
            npvTimerMinute.setValue(59);//59
        }*/
        //npvTimerMinute.setValue(59);//59

        npvTimerMinute.setValue(TimerPickerUtil.getTimerMinuteIndex(minutes, String.valueOf(preSettingMinute)));


        npvTimerMinute.setFriction(5 * ViewConfiguration.get(getActivity()).getScrollFriction());

        npvTimerMinute.setItemScrollListener(new NumberPickerView.ItemScrollListener() {
            @Override
            public void OnItemScroll(float itemHeight, float totalScrollY) {
                EventBus.getDefault().post(new PlaySoundWhenSetTime(CataSettingConstant.PLAY_SOUND_TYPE_LIST));
            }
        });
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            npvTimerHour.setValue(TimerPickerUtil.getTimerHourIndex(hours, String.format("%02d", preSettingHour)));
            npvTimerMinute.setValue(TimerPickerUtil.getTimerMinuteIndex(minutes, String.format("%02d", preSettingMinute)));

            LogUtil.d("the value c is = "+ preSettingHour+"the value preSettingMinute is = "+preSettingMinute);

            updateTimerContent();
            startCountdown();
            fragmentIsShow = true;
            EventBus.getDefault().post(new AlarmClockIsEditedEvent(true));
        }else {
            ToastDialog.dimissDialog();
            doStopCountDown();
            fragmentIsShow = false;
            EventBus.getDefault().post(new AlarmClockIsEditedEvent(false));
        }
    }

    @OnClick({ R2.id.ib_timer_setting_back, R2.id.ib_timer_setting_cancel, R2.id.tv_timer_center, R2.id.tv_timer_content, R2.id.iv_info})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.ib_timer_setting_back) {
            doBack();
        } else if (i == R.id.ib_timer_setting_cancel) {
            doCancel();
        } else if (i == R.id.tv_timer_center) {
            if (from == REQUEST_TIMER_SETTING_HOOD) {
                switchMode();
            }

        } else if (i == R.id.tv_timer_content) {
            switchMode();
        } else if (i == R.id.iv_info) {
            ToastDialog.showDialog(
                    getActivity(),
                    R.string.settingmodule_fragment_alarm_clock_setting_toast_content,
                    ToastDialog.WIDTH_LONG,
                    Gravity.BOTTOM | Gravity.END,
                    140,
                    40,
                    CataSettingConstant.TOAST_SHOW_DURATION);
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

    public void setTouchTime(long touchTime){
        this.mTouchTime=touchTime;
    }

    private void startCountdown() {
        doStopCountDown();
        Logger.getInstance().i("startCountdown()");
        mDisposable = Observable
                //.interval(0,1, TimeUnit.SECONDS)
                .interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                    long deference= SystemClock.elapsedRealtime()- mTouchTime;
                    if(deference>5000){
                        doBack();
                    }

                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribe();
    }


    private void doCancel() {
        // ((OnTimerSettingFragmentListener)mListener).onTimerSettingFragmentFinish(from);
        ((OnTimerSettingFragmentListener) mListener).onTimerSettingFragmentCancel(from);

    }

    private void doBack() {
        if (!isVisible()) {
            Logger.getInstance().w("Ignored doBack()!");
            return;
        }

        if (from == REQUEST_TIMER_SETTING_HOD) {
            String minuteStr = minutes[npvTimerMinute.getValue()];
            String hourStr = hours[npvTimerHour.getValue()];
            if (minuteStr.equals("00") && hourStr.equals("00")) {
                doCancel();
            } else {
                ((OnTimerSettingFragmentListener) mListener).onTimerSettingFragmentRequestTimer(from, Integer.valueOf(hourStr), Integer.valueOf(minuteStr));
            }

        } else {
            if (tvTimerContent.getVisibility() == View.VISIBLE) {
                String minuteStr = minutes[npvTimerMinute.getValue()];
                String hourStr = hours[npvTimerHour.getValue()];
                if (minuteStr.equals("00") && hourStr.equals("00")) {
                    doCancel();
                } else {
                    ((OnTimerSettingFragmentListener) mListener).onTimerSettingFragmentRequestTimer(from, Integer.valueOf(hourStr), Integer.valueOf(minuteStr));
                }
            } else {
                doCancel();
            }
        }

        doStopCountDown();

    }

    private void switchMode() {
        if (tvTimerContent.getVisibility() == View.VISIBLE) {
            tvTimerContent.setVisibility(View.INVISIBLE);
            if (npvTimerHour.getVisibility() == View.INVISIBLE)
                npvTimerHour.setVisibility(View.VISIBLE);
            if (npvTimerMinute.getVisibility() == View.INVISIBLE)
                npvTimerMinute.setVisibility(View.VISIBLE);
        } else if (tvTimerContent.getVisibility() == View.INVISIBLE) {
            tvTimerContent.setVisibility(View.VISIBLE);
            if (npvTimerHour.getVisibility() == View.VISIBLE)
                npvTimerHour.setVisibility(View.INVISIBLE);
            if (npvTimerMinute.getVisibility() == View.VISIBLE)
                npvTimerMinute.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {
        if (picker == npvTimerHour) {
            updateTimerContent();
        } else if (picker == npvTimerMinute) {
            updateTimerContent();
        }

    }

    private void updateTimerContent() {
        String minuteStr = minutes[npvTimerMinute.getValue()];
        String hourStr = hours[npvTimerHour.getValue()];
        tvTimerContent.setText(hourStr + " " + minuteStr);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public interface OnTimerSettingFragmentListener extends OnFragmentListener {
        void onTimerSettingFragmentFinish(int from);

        void onTimerSettingFragmentRequestTimer(int from, int hour, int minute);

        void onTimerSettingFragmentCancel(int from);

    }


    private void processOperateAreaTouchEvent(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            resetIdleStart();
            float touchX = event.getX();
            float touchY = event.getY();
            float centerX = view.getWidth() / 2;
            float centerY = view.getHeight() / 2;
            // LogUtil.d(touchX + "------" + touchY + "---------" + centerX + "----------" + centerY);
            if (isOutOfCircle(centerX, centerY, touchX, touchY)) {
          LogUtil.d("liang touch the outofcircle");
            } else {
               doBack();
                LogUtil.d("liang touch the insideofcircle");
            }
        }
    }

    private void resetIdleStart() {
      //  mTouchTime = SystemClock.elapsedRealtime();
    }

    private boolean isOutOfCircle(float x1, float y1, float x2, float y2) {
        int radius = 420 / 2;
        return calDistance(x1, y1, x2, y2) > radius;
    }

    private double calDistance(float x1, float y1, float x2, float y2) {
        double x = Math.abs(x1 - x2);
        double y = Math.abs(y1 - y2);
        return Math.sqrt(x * x + y * y);
    }

    public void doConfirm(){
        if(fragmentIsShow){
            doBack();
        }
    }
}
