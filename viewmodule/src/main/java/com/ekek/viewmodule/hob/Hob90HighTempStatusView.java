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

public class Hob90HighTempStatusView extends LinearLayout {
    Unbinder unbinder;
    @BindView(R2.id.iv_seperate_left_up)
    ImageView ivSeperateLeftUp;
    @BindView(R2.id.iv_seperate_left_down)
    ImageView ivSeperateLeftDown;
    @BindView(R2.id.iv_union_left)
    ImageView ivUnionLeft;
    @BindView(R2.id.iv_dash_left)
    ImageView ivDashLeft;
    @BindView(R2.id.iv_seperate_right_up)
    ImageView ivSeperateRightUp;
    @BindView(R2.id.iv_seperate_right_down)
    ImageView ivSeperateRightDown;
    @BindView(R2.id.iv_union_right)
    ImageView ivUnionRight;
    @BindView(R2.id.iv_dash_right)
    ImageView ivDashRight;
    @BindView(R2.id.iv_center)
    ImageView ivCenter;


    public Hob90HighTempStatusView(Context context) {
        this(context, null);
    }

    public Hob90HighTempStatusView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View mRootView = LayoutInflater.from(context).inflate(R.layout.viewmodule_hob_90_high_temp_status_view, this);
        unbinder = ButterKnife.bind(this, mRootView);

        updateStatus(false,false,false,false,false);
    }


    public void updateStatus(boolean firstFlag,boolean secondFlag, boolean thirdFlag,boolean fourthFlag ,boolean fifthFlag) {
        if (firstFlag || secondFlag || thirdFlag || fourthFlag || fifthFlag) {
            this.setVisibility(VISIBLE);
        }else {
            this.setVisibility(INVISIBLE);
            return;
        }

        //////////////////////////////////////////////////////////////////
        if (firstFlag && secondFlag) {//wu qu
            updateUnionLeftUI(true);
            updateSeperateLeftDownUI(false);
            updateSeperateLeftUpUI(false);

        }else {
            updateUnionLeftUI(false);
            if (firstFlag) {
                updateSeperateLeftDownUI(true);
            }else {
                updateSeperateLeftDownUI(false);
            }

            if (secondFlag) {
                updateSeperateLeftUpUI(true);
            }else {
                updateSeperateLeftUpUI(false);
            }
        }

        ///////////////////////////////////////////////////////////////

        if (fourthFlag && fifthFlag) {//wu qu
            updateUnionRightUI(true);
            updateSeperateRightDownUI(false);
            updateSeperateRightUpUI(false);

        }else {
            updateUnionRightUI(false);
            if (fifthFlag) {
                updateSeperateRightDownUI(true);
            }else {
                updateSeperateRightDownUI(false);
            }

            if (fourthFlag) {
                updateSeperateRightUpUI(true);
            }else {
                updateSeperateRightUpUI(false);
            }
        }


        //////////////////////////////////////////////////////////////

        if (thirdFlag) {
            ivCenter.setVisibility(VISIBLE);
        }else {
            ivCenter.setVisibility(INVISIBLE);
        }

    }

    private void updateUnionLeftUI(boolean isShow) {
        if (isShow) {
            ivUnionLeft.setVisibility(VISIBLE);
            //ivDashLeft.setVisibility(INVISIBLE);
            ivDashLeft.setVisibility(VISIBLE);//双高温时，不能显示无区状态，即中间横线要显示出来

        }else {
            ivUnionLeft.setVisibility(INVISIBLE);
            ivDashLeft.setVisibility(VISIBLE);
        }
    }

    private void updateUnionRightUI(boolean isShow) {
        if (isShow) {
            ivUnionRight.setVisibility(VISIBLE);
            //ivDashRight.setVisibility(INVISIBLE);
            ivDashRight.setVisibility(VISIBLE);//双高温时，不能显示无区状态，即中间横线要显示出来
        }else {
            ivUnionRight.setVisibility(INVISIBLE);
            ivDashRight.setVisibility(VISIBLE);
        }
    }

    private void updateSeperateLeftUpUI(boolean isShow) {
        if (isShow) {
            ivSeperateLeftUp.setVisibility(VISIBLE);
        }else {
            ivSeperateLeftUp.setVisibility(INVISIBLE);
        }
    }

    private void updateSeperateLeftDownUI(boolean isShow) {
        if (isShow) {
            ivSeperateLeftDown.setVisibility(VISIBLE);
        }else {
            ivSeperateLeftDown.setVisibility(INVISIBLE);
        }
    }

    private void updateSeperateRightUpUI(boolean isShow) {
        if (isShow) {
            ivSeperateRightUp.setVisibility(VISIBLE);
        }else {
            ivSeperateRightUp.setVisibility(INVISIBLE);
        }
    }

    private void updateSeperateRightDownUI(boolean isShow) {
        if (isShow) {
            ivSeperateRightDown.setVisibility(VISIBLE);
        }else {
            ivSeperateRightDown.setVisibility(INVISIBLE);
        }
    }

    private void updateCenterUI(boolean isShow) {
        if (isShow) {
            ivCenter.setVisibility(VISIBLE);
        }else {
            ivCenter.setVisibility(INVISIBLE);
        }
    }

    private void updateLeftDashUI(boolean isShow) {
        if (isShow) {
            ivDashLeft.setVisibility(VISIBLE);
        }else {
            ivDashLeft.setVisibility(INVISIBLE);
        }
    }

    private void updateRightDashUI(boolean isShow) {
        if (isShow) {
            ivDashRight.setVisibility(VISIBLE);
        }else {
            ivDashRight.setVisibility(INVISIBLE);
        }
    }
}
