package com.ekek.viewmodule.hob;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.viewmodule.R;
import com.ekek.viewmodule.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Hob80SettingIndicatorView extends LinearLayout {
    private static final int THF_HOB_ID_1 = 1;
    private static final int THF_HOB_ID_2 = 2;
    private static final int THF_HOB_ID_3 = 3;
    private static final int THF_HOB_ID_4 = 4;
    private static final int THF_HOB_ID_5 = 5;
    private static final int THF_HOB_ID_6 = 6;
    private static final int THF_HOB_ID_7 = 7;
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
    @BindView(R2.id.iv_seperate_red_up)
    ImageView ivSeperateRedUp;
    @BindView(R2.id.iv_seperate_red_down)
    ImageView ivSeperateRedDown;
    @BindView(R2.id.iv_red_union)
    ImageView ivRedUnion;
    @BindView(R2.id.iv_red_center)
    ImageView ivRedCenter;
    @BindView(R2.id.iv_red_right)
    ImageView ivRedRight;

    public Hob80SettingIndicatorView(Context context) {
        this(context, null);
    }

    public Hob80SettingIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        View mRootView = LayoutInflater.from(context).inflate(R.layout.viewmodule_hob_80_setting_indicator_view, this);
        unbinder = ButterKnife.bind(this, mRootView);
        //updateStatus(false,false,false,false);


    }

    public void updateUI(int cookerID,
                         boolean poweredOnA, boolean highTempA,
                         boolean poweredOnB, boolean highTempB,
                         boolean poweredOnAB, boolean highTempAB,
                         boolean poweredOnC, boolean highTempC,
                         boolean poweredOnD, boolean highTempD) {
        hideAllView();
        switch (cookerID) {
            case THF_HOB_ID_1:
                ivSeperateDown.setVisibility(View.VISIBLE);
                ivDashLeft.setVisibility(View.VISIBLE);

                if (poweredOnB) {
                    ivSeperateUp.setVisibility(View.VISIBLE);
                } else if (highTempB) {
                    ivSeperateRedUp.setVisibility(View.VISIBLE);
                }

                if (poweredOnC) {
                    ivCenter.setVisibility(View.VISIBLE);
                } else if (highTempC) {
                    ivRedCenter.setVisibility(View.VISIBLE);
                }

                if (poweredOnD) {
                    ivRight.setVisibility(View.VISIBLE);
                } else if (highTempD) {
                    ivRedRight.setVisibility(View.VISIBLE);
                }
                break;
            case THF_HOB_ID_2:
                ivSeperateUp.setVisibility(View.VISIBLE);
                ivDashLeft.setVisibility(VISIBLE);

                if (poweredOnA) {
                    ivSeperateDown.setVisibility(View.VISIBLE);
                } else if (highTempA) {
                    ivSeperateRedDown.setVisibility(View.VISIBLE);
                }

                if (poweredOnC) {
                    ivCenter.setVisibility(View.VISIBLE);
                } else if (highTempC) {
                    ivRedCenter.setVisibility(View.VISIBLE);
                }

                if (poweredOnD) {
                    ivRight.setVisibility(View.VISIBLE);
                } else if (highTempD) {
                    ivRedRight.setVisibility(View.VISIBLE);
                }
                break;
            case THF_HOB_ID_1 * 10 + THF_HOB_ID_2:
                ivDashLeft.setVisibility(INVISIBLE);
                ivUnion.setVisibility(View.VISIBLE);

                if (poweredOnC) {
                    ivCenter.setVisibility(View.VISIBLE);
                } else if (highTempC) {
                    ivRedCenter.setVisibility(View.VISIBLE);
                }

                if (poweredOnD) {
                    ivRight.setVisibility(View.VISIBLE);
                } else if (highTempD) {
                    ivRedRight.setVisibility(View.VISIBLE);
                }
                break;
            case THF_HOB_ID_3:
                ivCenter.setVisibility(View.VISIBLE);
                if (poweredOnD) {
                    ivRight.setVisibility(View.VISIBLE);
                } else if (highTempD) {
                    ivRedRight.setVisibility(View.VISIBLE);
                }

                if (GlobalVars.getInstance().isAbUnited()) {
                    if (poweredOnAB) {
                        ivUnion.setVisibility(View.VISIBLE);
                        ivDashLeft.setVisibility(View.INVISIBLE);
                    } else if (highTempAB) {
                        ivRedUnion.setVisibility(View.VISIBLE);
                        ivDashLeft.setVisibility(View.INVISIBLE);
                    }
                } else {
                    if (poweredOnA) {
                        ivSeperateDown.setVisibility(View.VISIBLE);
                    } else if (highTempA) {
                        ivSeperateRedDown.setVisibility(View.VISIBLE);
                    }

                    if (poweredOnB) {
                        ivSeperateUp.setVisibility(View.VISIBLE);
                    } else if (highTempB) {
                        ivSeperateRedUp.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case THF_HOB_ID_4:
                ivRight.setVisibility(View.VISIBLE);
                if (poweredOnC) {
                    ivCenter.setVisibility(View.VISIBLE);
                } else if (highTempC) {
                    ivRedCenter.setVisibility(View.VISIBLE);
                }

                if (GlobalVars.getInstance().isAbUnited()) {
                    if (poweredOnAB) {
                        ivUnion.setVisibility(View.VISIBLE);
                        ivDashLeft.setVisibility(View.INVISIBLE);
                    } else if (highTempAB) {
                        ivRedUnion.setVisibility(View.VISIBLE);
                        ivDashLeft.setVisibility(View.INVISIBLE);
                    }
                } else {
                    if (poweredOnA) {
                        ivSeperateDown.setVisibility(View.VISIBLE);
                    } else if (highTempA) {
                        ivSeperateRedDown.setVisibility(View.VISIBLE);
                    }

                    if (poweredOnB) {
                        ivSeperateUp.setVisibility(View.VISIBLE);
                    } else if (highTempB) {
                        ivSeperateRedUp.setVisibility(View.VISIBLE);
                    }
                }
                break;

        }


    }

    private void hideAllView() {
        ivUnion.setVisibility(INVISIBLE);
        ivSeperateDown.setVisibility(INVISIBLE);
        ivSeperateUp.setVisibility(INVISIBLE);
        ivCenter.setVisibility(INVISIBLE);
        ivDashLeft.setVisibility(VISIBLE);
        ivRight.setVisibility(INVISIBLE);
    }

    public void updateStatus(boolean firstFlag, boolean secondFlag, boolean thirdFlag, boolean fourthFlag) {
        if (firstFlag || secondFlag || thirdFlag || fourthFlag) {
            this.setVisibility(VISIBLE);
        } else {
            this.setVisibility(INVISIBLE);
            return;
        }

        //////////////////////////////////////////////////////////////////
        if (firstFlag && secondFlag) {//wu qu
            updateUnionUI(true);
            updateSeperateUpUI(false);
            updateSeperateDownUI(false);

        } else {
            updateUnionUI(false);
            if (firstFlag) {
                updateSeperateDownUI(true);
            } else {
                updateSeperateDownUI(false);
            }

            if (secondFlag) {
                updateSeperateUpUI(true);
            } else {
                updateSeperateUpUI(false);
            }
        }

        ///////////////////////////////////////////////////////////////

        if (thirdFlag) {
            ivCenter.setVisibility(VISIBLE);
        } else {
            ivCenter.setVisibility(INVISIBLE);
        }

        //////////////////////////////////////////////////////////////
        if (fourthFlag) {
            ivRight.setVisibility(VISIBLE);
        } else {
            ivRight.setVisibility(INVISIBLE);
        }


    }

    private void updateUnionUI(boolean isShow) {
        if (isShow) {
            ivUnion.setVisibility(VISIBLE);
            ivDashLeft.setVisibility(INVISIBLE);

        } else {
            ivUnion.setVisibility(INVISIBLE);
            ivDashLeft.setVisibility(VISIBLE);
        }
    }

    private void updateSeperateUpUI(boolean isShow) {
        if (isShow) {
            ivSeperateUp.setVisibility(VISIBLE);
        } else {
            ivSeperateUp.setVisibility(INVISIBLE);
        }
    }

    private void updateSeperateDownUI(boolean isShow) {
        if (isShow) {
            ivSeperateDown.setVisibility(VISIBLE);
        } else {
            ivSeperateDown.setVisibility(INVISIBLE);
        }
    }

    private void updateCenterUI(boolean isShow) {
        if (isShow) {
            ivCenter.setVisibility(VISIBLE);
        } else {
            ivCenter.setVisibility(INVISIBLE);
        }
    }

    private void updateRightUI(boolean isShow) {
        if (isShow) {
            ivRight.setVisibility(VISIBLE);
        } else {
            ivRight.setVisibility(INVISIBLE);
        }
    }

    public void bCookerShowHighTemperature(boolean flag){  // b cooker high temperature
        if(flag){
            ivSeperateUp.setVisibility(INVISIBLE);
            ivSeperateRedUp.setVisibility(VISIBLE);
            ivRedUnion.setVisibility(INVISIBLE);
            ivUnion.setVisibility(INVISIBLE);
        }else {
            ivSeperateUp.setVisibility(VISIBLE);
            ivSeperateRedUp.setVisibility(INVISIBLE);
            ivRedUnion.setVisibility(INVISIBLE);
            ivUnion.setVisibility(INVISIBLE);
        }
    }

    public void bCookerShowBlackground(){  // b cooker close
        ivSeperateUp.setVisibility(INVISIBLE);
        ivSeperateRedUp.setVisibility(INVISIBLE);
        ivRedUnion.setVisibility(INVISIBLE);
        ivUnion.setVisibility(INVISIBLE);
    }

    public void aCookerShowBlackground(){  //  a cooker close
        ivSeperateDown.setVisibility(INVISIBLE);
        ivSeperateRedDown.setVisibility(INVISIBLE);
        ivRedUnion.setVisibility(INVISIBLE);
        ivUnion.setVisibility(INVISIBLE);
    }

    public void centerCookerShowBlackground(){  //  center cooker close
        ivCenter.setVisibility(INVISIBLE);
        ivRedCenter.setVisibility(INVISIBLE);
    }

    public void rightCookerShowBlackground(){  //  right cooker close
        ivRight.setVisibility(INVISIBLE);
        ivRedRight.setVisibility(INVISIBLE);
    }


    public void aCookerShowHighTemperature(boolean flag){  // a cooker high temperature
        if(flag){
            ivSeperateDown.setVisibility(INVISIBLE);
            ivSeperateRedDown.setVisibility(VISIBLE);
            ivRedUnion.setVisibility(INVISIBLE);
            ivUnion.setVisibility(INVISIBLE);
        }else {
            ivSeperateDown.setVisibility(VISIBLE);
            ivSeperateRedDown.setVisibility(INVISIBLE);
            ivRedUnion.setVisibility(INVISIBLE);
            ivUnion.setVisibility(INVISIBLE);
        }
    }

    public void abCookerShowHighTemperature(boolean flag){  // ab cooker high temperature
        if(flag){
            ivSeperateDown.setVisibility(INVISIBLE);
            ivSeperateRedDown.setVisibility(INVISIBLE);
            ivSeperateUp.setVisibility(INVISIBLE);
            ivSeperateRedUp.setVisibility(INVISIBLE);

            ivRedUnion.setVisibility(VISIBLE);
            ivUnion.setVisibility(INVISIBLE);
        }else {
            ivSeperateDown.setVisibility(INVISIBLE);
            ivSeperateRedDown.setVisibility(INVISIBLE);
            ivSeperateUp.setVisibility(INVISIBLE);
            ivSeperateRedUp.setVisibility(INVISIBLE);

            ivRedUnion.setVisibility(INVISIBLE);
            ivUnion.setVisibility(VISIBLE);
        }
    }

    public void centerCookerShowHighTemperature(boolean flag){  // center cooker high temperature
        if(flag){
            ivCenter.setVisibility(INVISIBLE);
            ivRedCenter.setVisibility(VISIBLE);
        }else {
            ivCenter.setVisibility(VISIBLE);
            ivRedCenter.setVisibility(INVISIBLE);
        }
    }



    public void rightCookerShowHighTemperature(boolean flag){  // right cooker high temperature
        if(flag){
            ivRight.setVisibility(INVISIBLE);
            ivRedRight.setVisibility(VISIBLE);
        }else {
            ivRight.setVisibility(VISIBLE);
            ivRedRight.setVisibility(INVISIBLE);
        }
    }


}
