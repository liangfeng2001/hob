package com.ekek.viewmodule.bar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ekek.viewmodule.R;
import com.ekek.viewmodule.R2;
import com.ekek.viewmodule.listener.OnMenuBarSelectListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HoodMenuBar extends LinearLayout {
    public static final int HOB_MENU_BAR_ID_PAUSE = 0;
    public static final int HOB_MENU_BAR_ID_CYCLIC = 1;
    public static final int HOB_MENU_BAR_ID_SCHEDULE = 2;
    public static final int HOB_MENU_BAR_ID_TIMER = 3;
    public static final int HOB_MENU_BAR_ID_SETTING = 4;

    public static final int NOTIFY_RELEASE_CYCLIC = 0;
    public static final int NOTIFY_RELEASE_SCHEDULE = 1;
    public static final int NOTIFY_RELEASE_TIMER = 2;
    public static final int NOTIFY_RELEASE_SETTING = 3;
    @BindView(R2.id.ib_menu_pause)
    ImageButton ibMenuPause;
    @BindView(R2.id.ib_menu_cyclic)
    ImageButton ibMenuCyclic;
    @BindView(R2.id.iv_cyclic_arrow)
    ImageView ivCyclicArrow;
    @BindView(R2.id.ib_menu_schedule)
    ImageButton ibMenuSchedule;
    @BindView(R2.id.iv_schedule_arrow)
    ImageView ivScheduleArrow;
    @BindView(R2.id.ib_menu_timer)
    ImageButton ibMenuTimer;
    @BindView(R2.id.iv_timer_arrow)
    ImageView ivTimerArrow;
    @BindView(R2.id.ib_menu_setting)
    ImageButton ibMenuSetting;
    @BindView(R2.id.iv_setting_arrow)
    ImageView ivSettingArrow;
    Unbinder unbinder;

    private OnMenuBarSelectListener mListener;
    private long clickStartTime = 0;

    public HoodMenuBar(Context context) {
        this(context, null);
    }

    public HoodMenuBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View mRootView = LayoutInflater.from(context).inflate(R.layout.viewmodule_layout_hood_menu_bar, this);
        unbinder = ButterKnife.bind(this, mRootView);
    }

    public void setOnMenuBarSelectListener(OnMenuBarSelectListener listener) {
        mListener = listener;
    }

    public void notifyReleaseButton(int id) {
        if (id == NOTIFY_RELEASE_CYCLIC) {
            if (ivCyclicArrow.getVisibility() == VISIBLE) ivCyclicArrow.setVisibility(INVISIBLE);

        }else if (id == NOTIFY_RELEASE_SCHEDULE) {
            if (ivScheduleArrow.getVisibility() == VISIBLE) ivScheduleArrow.setVisibility(INVISIBLE);

        }else if (id == NOTIFY_RELEASE_TIMER) {
            if (ivTimerArrow.getVisibility() == VISIBLE) ivTimerArrow.setVisibility(INVISIBLE);

        }else if (id == NOTIFY_RELEASE_SETTING) {
            if (ivSettingArrow.getVisibility() == VISIBLE) ivSettingArrow.setVisibility(INVISIBLE);

        }

    }


    @OnClick({R2.id.ib_menu_pause, R2.id.ib_menu_cyclic, R2.id.ib_menu_schedule, R2.id.ib_menu_timer, R2.id.ib_menu_setting})
    public void onViewClicked(View view) {
        //防止短时间多次点击,1s内多次点击视为无效点击
        if (System.currentTimeMillis() - clickStartTime >= 1000) {
            clickStartTime = System.currentTimeMillis();
        }else {
            return;
        }
        int i = view.getId();
        if (i == R.id.ib_menu_pause) {
            mListener.onMenuBarSelect(this,HOB_MENU_BAR_ID_PAUSE);

        } else if (i == R.id.ib_menu_cyclic) {
            mListener.onMenuBarSelect(this,HOB_MENU_BAR_ID_CYCLIC);

        } else if (i == R.id.ib_menu_schedule) {
            mListener.onMenuBarSelect(this,HOB_MENU_BAR_ID_SCHEDULE);

        } else if (i == R.id.ib_menu_timer) {
            mListener.onMenuBarSelect(this,HOB_MENU_BAR_ID_TIMER);

        } else if (i == R.id.ib_menu_setting) {
            mListener.onMenuBarSelect(this,HOB_MENU_BAR_ID_SETTING);

        }

        updateArrowUI(i);
    }

    private void updateArrowUI(int id) {
        if (id == R.id.ib_menu_cyclic) {

            if (ivCyclicArrow.getVisibility() == INVISIBLE) ivCyclicArrow.setVisibility(VISIBLE);
            if (ivScheduleArrow.getVisibility() == VISIBLE) ivScheduleArrow.setVisibility(INVISIBLE);
            if (ivTimerArrow.getVisibility() == VISIBLE) ivTimerArrow.setVisibility(INVISIBLE);
            if (ivSettingArrow.getVisibility() == VISIBLE) ivSettingArrow.setVisibility(INVISIBLE);

        }else if (id == R.id.ib_menu_schedule) {

            if (ivCyclicArrow.getVisibility() == VISIBLE) ivCyclicArrow.setVisibility(INVISIBLE);
            if (ivScheduleArrow.getVisibility() == INVISIBLE) ivScheduleArrow.setVisibility(VISIBLE);
            if (ivTimerArrow.getVisibility() == VISIBLE) ivTimerArrow.setVisibility(INVISIBLE);
            if (ivSettingArrow.getVisibility() == VISIBLE) ivSettingArrow.setVisibility(INVISIBLE);

        }else if (id == R.id.ib_menu_timer) {

            if (ivCyclicArrow.getVisibility() == VISIBLE) ivCyclicArrow.setVisibility(INVISIBLE);
            if (ivScheduleArrow.getVisibility() == VISIBLE) ivScheduleArrow.setVisibility(INVISIBLE);
            if (ivTimerArrow.getVisibility() == INVISIBLE) ivTimerArrow.setVisibility(VISIBLE);
            if (ivSettingArrow.getVisibility() == VISIBLE) ivSettingArrow.setVisibility(INVISIBLE);

        }else if (id == R.id.ib_menu_setting) {

            if (ivCyclicArrow.getVisibility() == VISIBLE) ivCyclicArrow.setVisibility(INVISIBLE);
            if (ivScheduleArrow.getVisibility() == VISIBLE) ivScheduleArrow.setVisibility(INVISIBLE);
            if (ivTimerArrow.getVisibility() == VISIBLE) ivTimerArrow.setVisibility(INVISIBLE);
            if (ivSettingArrow.getVisibility() == INVISIBLE) ivSettingArrow.setVisibility(VISIBLE);

        }/*else {
            if (ivMenuArrow.getVisibility() == VISIBLE) ivMenuArrow.setVisibility(INVISIBLE);
            if (ivSettingArrow.getVisibility() == VISIBLE) ivSettingArrow.setVisibility(INVISIBLE);
            if (ivTimerArrow.getVisibility() == VISIBLE) ivTimerArrow.setVisibility(INVISIBLE);
        }*/
    }
}
