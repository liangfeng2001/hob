package com.ekek.viewmodule.hob;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ekek.viewmodule.R;
import com.ekek.viewmodule.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Hob80HighTempStatusView extends LinearLayout {
    Unbinder unbinder;
    @BindView(R2.id.iv_seperate_up)
    ImageView ivSeperateUp;
    @BindView(R2.id.iv_seperate_down)
    ImageView ivSeperateDown;
    @BindView(R2.id.iv_union)
    ImageView ivUnion;
    @BindView(R2.id.iv_dash_left)
    ImageView ivDashLeft;
    @BindView(R2.id.iv_center)
    ImageView ivCenter;
    @BindView(R2.id.iv_right)
    ImageView ivRight;

    public Hob80HighTempStatusView(Context context) {
        this(context, null);
    }

    public Hob80HighTempStatusView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        View mRootView = LayoutInflater.from(context).inflate(R.layout.viewmodule_hob_80_high_temp_status_view, this);
        unbinder = ButterKnife.bind(this, mRootView);
        updateStatus(false,false,false,false);
    }

    public void updateStatus(boolean firstFlag,boolean secondFlag, boolean thirdFlag,boolean fourthFlag) {
        if (firstFlag || secondFlag || thirdFlag || fourthFlag ) {
            this.setVisibility(VISIBLE);
        }else {
            this.setVisibility(INVISIBLE);
            return;
        }

        //////////////////////////////////////////////////////////////////
        if (firstFlag && secondFlag) {//wu qu
            updateUnionUI(true);
            updateSeperateUpUI(false);
            updateSeperateDownUI(false);

        }else {
            updateUnionUI(false);
            if (firstFlag) {
                updateSeperateDownUI(true);
            }else {
                updateSeperateDownUI(false);
            }

            if (secondFlag) {
                updateSeperateUpUI(true);
            }else {
                updateSeperateUpUI(false);
            }
        }

        ///////////////////////////////////////////////////////////////

        if (thirdFlag) {
            ivCenter.setVisibility(VISIBLE);
        }else {
            ivCenter.setVisibility(INVISIBLE);
        }

        //////////////////////////////////////////////////////////////
        if (fourthFlag) {
            ivRight.setVisibility(VISIBLE);
        }else {
            ivRight.setVisibility(INVISIBLE);
        }


    }

    private void updateUnionUI(boolean isShow) {
        if (isShow) {
            ivUnion.setVisibility(VISIBLE);
            //ivDashLeft.setVisibility(INVISIBLE);
            ivDashLeft.setVisibility(VISIBLE);//双高温时，不能显示无区状态，即中间横线要显示出来

        }else {
            ivUnion.setVisibility(INVISIBLE);
            ivDashLeft.setVisibility(VISIBLE);
        }
    }

    private void updateSeperateUpUI(boolean isShow) {
        if (isShow) {
            ivSeperateUp.setVisibility(VISIBLE);
        }else {
            ivSeperateUp.setVisibility(INVISIBLE);
        }
    }

    private void updateSeperateDownUI(boolean isShow) {
        if (isShow) {
            ivSeperateDown.setVisibility(VISIBLE);
        }else {
            ivSeperateDown.setVisibility(INVISIBLE);
        }
    }

    private void updateCenterUI(boolean isShow) {
        if (isShow) {
            ivCenter.setVisibility(VISIBLE);
        }else {
            ivCenter.setVisibility(INVISIBLE);
        }
    }

    private void updateRightUI(boolean isShow) {
        if (isShow) {
            ivRight.setVisibility(VISIBLE);
        }else {
            ivRight.setVisibility(INVISIBLE);
        }
    }

}
