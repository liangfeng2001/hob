package com.ekek.viewmodule.bar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.viewmodule.R;
import com.ekek.viewmodule.R2;
import com.ekek.viewmodule.listener.OnMenuBarSelectListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HobMenuBar extends LinearLayout {
    public static final int HOB_MENU_BAR_ID_LOCK = 0;
    public static final int HOB_MENU_BAR_ID_PAUSE = 1;
    public static final int HOB_MENU_BAR_ID_MENU = 2;
    public static final int HOB_MENU_BAR_ID_TIMER = 3;
    public static final int HOB_MENU_BAR_ID_SETTING = 4;

    public static final int NOTIFY_RELEASE_MENU = 0;
    public static final int NOTIFY_RELEASE_TIMER = 1;
    public static final int NOTIFY_RELEASE_SETTING = 2;

    @BindView(R2.id.ib_menu_lock)
    ImageButton ibMenuLock;
    @BindView(R2.id.ib_menu_pause)
    ImageButton ibMenuPause;
    @BindView(R2.id.ib_menu_menu)
    ImageButton ibMenuMenu;
    @BindView(R2.id.ib_menu_timer)
    ImageButton ibMenuTimer;
    @BindView(R2.id.ib_menu_setting)
    ImageButton ibMenuSetting;
    @BindView(R2.id.iv_menu_arrow)
    ImageView ivMenuArrow;
    @BindView(R2.id.iv_timer_arrow)
    ImageView ivTimerArrow;
    @BindView(R2.id.iv_setting_arrow)
    ImageView ivSettingArrow;
    Unbinder unbinder;

    private OnMenuBarSelectListener mListener;
    private long clickStartTime = 0;

    public HobMenuBar(Context context) {
        this(context, null);
    }

    public HobMenuBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View mRootView = LayoutInflater.from(context).inflate(R.layout.viewmodule_layout_hob_menu_bar, this);
        unbinder = ButterKnife.bind(this, mRootView);

    }

    public void setOnMenuBarSelectListener(OnMenuBarSelectListener listener) {
        mListener = listener;
    }

    public void notifyReleaseButton(int id) {
        if (id == NOTIFY_RELEASE_MENU) {
            if (ivMenuArrow.getVisibility() == VISIBLE) ivMenuArrow.setVisibility(INVISIBLE);

        }else if (id == NOTIFY_RELEASE_TIMER) {
            if (ivTimerArrow.getVisibility() == VISIBLE) ivTimerArrow.setVisibility(INVISIBLE);

        }else if (id == NOTIFY_RELEASE_SETTING) {
            if (ivSettingArrow.getVisibility() == VISIBLE) ivSettingArrow.setVisibility(INVISIBLE);

        }

    }

    public void setTimeElapsedRealtime(){
        clickStartTime = System.currentTimeMillis();
    }


    @OnClick({R2.id.ib_menu_lock, R2.id.ib_menu_pause, R2.id.ib_menu_menu, R2.id.ib_menu_timer, R2.id.ib_menu_setting})
    public void onViewClicked(View view) {
        //防止短时间多次点击,1s内多次点击视为无效点击
        if (System.currentTimeMillis() - clickStartTime >= 900||System.currentTimeMillis() - clickStartTime<0) {
            clickStartTime = System.currentTimeMillis();
        }else {
            LogUtil.d("liang ,get return clickStartTime is ="+clickStartTime+"  ; currentTimeMillis is ="+System.currentTimeMillis());

            return;
        }

        int i = view.getId();
        if (i == R.id.ib_menu_lock) {
            mListener.onMenuBarSelect(this, HOB_MENU_BAR_ID_LOCK);
        } else if (i == R.id.ib_menu_pause) {
            mListener.onMenuBarSelect(this, HOB_MENU_BAR_ID_PAUSE);
        } else if (i == R.id.ib_menu_menu) {
            mListener.onMenuBarSelect(this, HOB_MENU_BAR_ID_MENU);
        } else if (i == R.id.ib_menu_timer) {
            mListener.onMenuBarSelect(this, HOB_MENU_BAR_ID_TIMER);
        } else if (i == R.id.ib_menu_setting) {
            mListener.onMenuBarSelect(this, HOB_MENU_BAR_ID_SETTING);
        }
        updateArrowUI(i);
    }

    private void updateArrowUI(int id) {
        if (id == R.id.ib_menu_setting) {
            if (ivMenuArrow.getVisibility() == VISIBLE) ivMenuArrow.setVisibility(INVISIBLE);
            if (ivSettingArrow.getVisibility() == INVISIBLE) ivSettingArrow.setVisibility(VISIBLE);
            if (ivTimerArrow.getVisibility() == VISIBLE) ivTimerArrow.setVisibility(INVISIBLE);

        }else if (id == R.id.ib_menu_timer) {
            if (ivMenuArrow.getVisibility() == VISIBLE) ivMenuArrow.setVisibility(INVISIBLE);
            if (ivSettingArrow.getVisibility() == VISIBLE) ivSettingArrow.setVisibility(INVISIBLE);
            if (ivTimerArrow.getVisibility() == INVISIBLE) ivTimerArrow.setVisibility(VISIBLE);
        }else if (id == R.id.ib_menu_menu) {
            if (ivMenuArrow.getVisibility() == INVISIBLE) ivMenuArrow.setVisibility(VISIBLE);
            if (ivSettingArrow.getVisibility() == VISIBLE) ivSettingArrow.setVisibility(INVISIBLE);
            if (ivTimerArrow.getVisibility() == VISIBLE) ivTimerArrow.setVisibility(INVISIBLE);
        }/*else {
            if (ivMenuArrow.getVisibility() == VISIBLE) ivMenuArrow.setVisibility(INVISIBLE);
            if (ivSettingArrow.getVisibility() == VISIBLE) ivSettingArrow.setVisibility(INVISIBLE);
            if (ivTimerArrow.getVisibility() == VISIBLE) ivTimerArrow.setVisibility(INVISIBLE);
        }*/
    }
}
