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

public class Hob90SettingIndicatorView extends LinearLayout {
    private static final int THF_HOB_ID_1 = 1;
    private static final int THF_HOB_ID_2 = 2;
    private static final int THF_HOB_ID_3 = 3;
    private static final int THF_HOB_ID_4 = 4;
    private static final int THF_HOB_ID_5 = 5;
    private static final int THF_HOB_ID_6 = 6;
    private static final int THF_HOB_ID_7 = 7;
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
    @BindView(R2.id.iv_red_seperate_left_up)
    ImageView ivRedSeperateLeftUp;
    @BindView(R2.id.iv_red_seperate_left_down)
    ImageView ivRedSeperateLeftDown;
    @BindView(R2.id.iv_red_union_left)
    ImageView ivRedUnionLeft;
    @BindView(R2.id.iv_red_seperate_right_up)
    ImageView ivRedSeperateRightUp;
    @BindView(R2.id.iv_red_seperate_right_down)
    ImageView ivRedSeperateRightDown;
    @BindView(R2.id.iv_red_union_right)
    ImageView ivRedUnionRight;
    @BindView(R2.id.iv_red_center)
    ImageView ivRedCenter;


    public Hob90SettingIndicatorView(Context context) {
        this(context, null);
    }

    public Hob90SettingIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View mRootView = LayoutInflater.from(context).inflate(R.layout.viewmodule_hob_90_setting_indicator_view, this);
        unbinder = ButterKnife.bind(this, mRootView);

        //updateStatus(false,false,false,false,false);
    }


    public void updateUI(int cookerID,
                         boolean poweredOnA, boolean highTempA,
                         boolean poweredOnB, boolean highTempB,
                         boolean poweredOnAB, boolean highTempAB,
                         boolean poweredOnC, boolean highTempC,
                         boolean poweredOnE, boolean highTempE,
                         boolean poweredOnF, boolean highTempF,
                         boolean poweredOnEF, boolean highTempEF) {
        hideAllView();

        switch (cookerID) {
            case THF_HOB_ID_1:
                ivSeperateLeftDown.setVisibility(View.VISIBLE);
                ivDashLeft.setVisibility(View.VISIBLE);

                if (poweredOnB) {
                    ivSeperateLeftUp.setVisibility(View.VISIBLE);
                } else if (highTempB) {
                    ivRedSeperateLeftUp.setVisibility(View.VISIBLE);
                }

                if (poweredOnC) {
                    ivCenter.setVisibility(View.VISIBLE);
                } else if (highTempC) {
                    ivRedCenter.setVisibility(View.VISIBLE);
                }

                if (GlobalVars.getInstance().isEfUnited()) {
                    if (poweredOnEF) {
                        ivUnionRight.setVisibility(View.VISIBLE);
                    } else if (highTempEF) {
                        ivRedUnionRight.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (poweredOnE) {
                        ivSeperateRightUp.setVisibility(View.VISIBLE);
                    } else if (highTempE) {
                        ivRedSeperateRightUp.setVisibility(View.VISIBLE);
                    }

                    if (poweredOnF) {
                        ivSeperateRightDown.setVisibility(View.VISIBLE);
                    } else if (highTempF) {
                        ivRedSeperateRightDown.setVisibility(View.VISIBLE);
                    }
                }

                break;
            case THF_HOB_ID_2:
                ivSeperateLeftUp.setVisibility(View.VISIBLE);
                ivDashLeft.setVisibility(View.VISIBLE);

                if (poweredOnA) {
                    ivSeperateLeftDown.setVisibility(View.VISIBLE);
                } else if (highTempA) {
                    ivRedSeperateLeftDown.setVisibility(View.VISIBLE);
                }

                if (poweredOnC) {
                    ivCenter.setVisibility(View.VISIBLE);
                } else if (highTempC) {
                    ivRedCenter.setVisibility(View.VISIBLE);
                }

                if (GlobalVars.getInstance().isEfUnited()) {
                    if (poweredOnEF) {
                        ivUnionRight.setVisibility(View.VISIBLE);
                    } else if (highTempEF) {
                        ivRedUnionRight.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (poweredOnE) {
                        ivSeperateRightUp.setVisibility(View.VISIBLE);
                    } else if (highTempE) {
                        ivRedSeperateRightUp.setVisibility(View.VISIBLE);
                    }

                    if (poweredOnF) {
                        ivSeperateRightDown.setVisibility(View.VISIBLE);
                    } else if (highTempF) {
                        ivRedSeperateRightDown.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case THF_HOB_ID_1 * 10 + THF_HOB_ID_2:
                ivUnionLeft.setVisibility(View.VISIBLE);
                ivDashLeft.setVisibility(View.INVISIBLE);
                if (poweredOnC) {
                    ivCenter.setVisibility(View.VISIBLE);
                } else if (highTempC) {
                    ivRedCenter.setVisibility(View.VISIBLE);
                }

                if (GlobalVars.getInstance().isEfUnited()) {
                    if (poweredOnEF) {
                        ivUnionRight.setVisibility(View.VISIBLE);
                    } else if (highTempEF) {
                        ivRedUnionRight.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (poweredOnE) {
                        ivSeperateRightUp.setVisibility(View.VISIBLE);
                    } else if (highTempE) {
                        ivRedSeperateRightUp.setVisibility(View.VISIBLE);
                    }

                    if (poweredOnF) {
                        ivSeperateRightDown.setVisibility(View.VISIBLE);
                    } else if (highTempF) {
                        ivRedSeperateRightDown.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case THF_HOB_ID_3:
                ivCenter.setVisibility(View.VISIBLE);

                if (GlobalVars.getInstance().isAbUnited()) {
                    if (poweredOnAB) {
                        ivUnionLeft.setVisibility(View.VISIBLE);
                    } else if (highTempAB) {
                        ivRedUnionLeft.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (poweredOnA) {
                        ivSeperateLeftDown.setVisibility(View.VISIBLE);
                    } else if (highTempA) {
                        ivRedSeperateLeftDown.setVisibility(View.VISIBLE);
                    }

                    if (poweredOnB) {
                        ivSeperateLeftUp.setVisibility(View.VISIBLE);
                    } else if (highTempB) {
                        ivRedSeperateLeftUp.setVisibility(View.VISIBLE);
                    }
                }

                if (GlobalVars.getInstance().isEfUnited()) {
                    if (poweredOnEF) {
                        ivUnionRight.setVisibility(View.VISIBLE);
                    } else if (highTempEF) {
                        ivRedUnionRight.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (poweredOnE) {
                        ivSeperateRightUp.setVisibility(View.VISIBLE);
                    } else if (highTempE) {
                        ivRedSeperateRightUp.setVisibility(View.VISIBLE);
                    }

                    if (poweredOnF) {
                        ivSeperateRightDown.setVisibility(View.VISIBLE);
                    } else if (highTempF) {
                        ivRedSeperateRightDown.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case THF_HOB_ID_5:
                ivSeperateRightUp.setVisibility(View.VISIBLE);
                ivDashRight.setVisibility(View.VISIBLE);

                if (GlobalVars.getInstance().isAbUnited()) {
                    if (poweredOnAB) {
                        ivUnionLeft.setVisibility(View.VISIBLE);
                    } else if (highTempAB) {
                        ivRedUnionLeft.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (poweredOnA) {
                        ivSeperateLeftDown.setVisibility(View.VISIBLE);
                    } else if (highTempA) {
                        ivRedSeperateLeftDown.setVisibility(View.VISIBLE);
                    }

                    if (poweredOnB) {
                        ivSeperateLeftUp.setVisibility(View.VISIBLE);
                    } else if (highTempB) {
                        ivRedSeperateLeftUp.setVisibility(View.VISIBLE);
                    }
                }

                if (poweredOnC) {
                    ivCenter.setVisibility(View.VISIBLE);
                } else if (highTempC) {
                    ivRedCenter.setVisibility(View.VISIBLE);
                }

                if (poweredOnF) {
                    ivSeperateRightDown.setVisibility(View.VISIBLE);
                } else if (highTempF) {
                    ivRedSeperateRightDown.setVisibility(View.VISIBLE);
                }
                break;
            case THF_HOB_ID_6:
                ivSeperateRightDown.setVisibility(View.VISIBLE);
                ivDashRight.setVisibility(View.VISIBLE);

                if (GlobalVars.getInstance().isAbUnited()) {
                    if (poweredOnAB) {
                        ivUnionLeft.setVisibility(View.VISIBLE);
                    } else if (highTempAB) {
                        ivRedUnionLeft.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (poweredOnA) {
                        ivSeperateLeftDown.setVisibility(View.VISIBLE);
                    } else if (highTempA) {
                        ivRedSeperateLeftDown.setVisibility(View.VISIBLE);
                    }

                    if (poweredOnB) {
                        ivSeperateLeftUp.setVisibility(View.VISIBLE);
                    } else if (highTempB) {
                        ivRedSeperateLeftUp.setVisibility(View.VISIBLE);
                    }
                }

                if (poweredOnC) {
                    ivCenter.setVisibility(View.VISIBLE);
                } else if (highTempC) {
                    ivRedCenter.setVisibility(View.VISIBLE);
                }

                if (poweredOnE) {
                    ivSeperateRightUp.setVisibility(View.VISIBLE);
                } else if (highTempE) {
                    ivRedSeperateRightUp.setVisibility(View.VISIBLE);
                }
                break;
            case THF_HOB_ID_6 * 10 + THF_HOB_ID_5:
                ivUnionRight.setVisibility(View.VISIBLE);
                ivDashRight.setVisibility(View.INVISIBLE);

                if (GlobalVars.getInstance().isAbUnited()) {
                    if (poweredOnAB) {
                        ivUnionLeft.setVisibility(View.VISIBLE);
                    } else if (highTempAB) {
                        ivRedUnionLeft.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (poweredOnA) {
                        ivSeperateLeftDown.setVisibility(View.VISIBLE);
                    } else if (highTempA) {
                        ivRedSeperateLeftDown.setVisibility(View.VISIBLE);
                    }

                    if (poweredOnB) {
                        ivSeperateLeftUp.setVisibility(View.VISIBLE);
                    } else if (highTempB) {
                        ivRedSeperateLeftUp.setVisibility(View.VISIBLE);
                    }
                }

                if (poweredOnC) {
                    ivCenter.setVisibility(View.VISIBLE);
                } else if (highTempC) {
                    ivRedCenter.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void hideAllView() {
        ivUnionLeft.setVisibility(INVISIBLE);
        ivUnionRight.setVisibility(INVISIBLE);
        ivSeperateLeftDown.setVisibility(INVISIBLE);
        ivSeperateLeftUp.setVisibility(INVISIBLE);
        ivSeperateRightDown.setVisibility(INVISIBLE);
        ivSeperateRightUp.setVisibility(INVISIBLE);
        ivCenter.setVisibility(INVISIBLE);

        ivRedUnionLeft.setVisibility(INVISIBLE);
        ivRedUnionRight.setVisibility(INVISIBLE);
        ivRedSeperateLeftDown.setVisibility(INVISIBLE);
        ivRedSeperateLeftUp.setVisibility(INVISIBLE);
        ivRedSeperateRightDown.setVisibility(INVISIBLE);
        ivRedSeperateRightUp.setVisibility(INVISIBLE);
        ivRedCenter.setVisibility(INVISIBLE);

        ivDashLeft.setVisibility(VISIBLE);
        ivDashRight.setVisibility(VISIBLE);
    }

    private void updateStatus(boolean firstFlag, boolean secondFlag, boolean thirdFlag, boolean fourthFlag, boolean fifthFlag) {
      /*  if (firstFlag || secondFlag || thirdFlag || fourthFlag || fifthFlag) {
            this.setVisibility(VISIBLE);
        }else {
            this.setVisibility(INVISIBLE);
            return;
        }*/

        //////////////////////////////////////////////////////////////////
        if (firstFlag && secondFlag) {//wu qu
            updateUnionLeftUI(true);
            updateSeperateLeftDownUI(false);
            updateSeperateLeftUpUI(false);

        } else {
            updateUnionLeftUI(false);
            if (firstFlag) {
                updateSeperateLeftDownUI(true);
            } else {
                updateSeperateLeftDownUI(false);
            }

            if (secondFlag) {
                updateSeperateLeftUpUI(true);
            } else {
                updateSeperateLeftUpUI(false);
            }
        }

        ///////////////////////////////////////////////////////////////

        if (fourthFlag && fifthFlag) {//wu qu
            updateUnionRightUI(true);
            updateSeperateRightDownUI(false);
            updateSeperateRightUpUI(false);

        } else {
            updateUnionRightUI(false);
            if (fifthFlag) {
                updateSeperateRightDownUI(true);
            } else {
                updateSeperateRightDownUI(false);
            }

            if (fourthFlag) {
                updateSeperateRightUpUI(true);
            } else {
                updateSeperateRightUpUI(false);
            }
        }


        //////////////////////////////////////////////////////////////

        if (thirdFlag) {
            ivCenter.setVisibility(VISIBLE);
        } else {
            ivCenter.setVisibility(INVISIBLE);
        }

    }

    private void updateUnionLeftUI(boolean isShow) {
        if (isShow) {
            ivUnionLeft.setVisibility(VISIBLE);
            ivDashLeft.setVisibility(INVISIBLE);

        } else {
            ivUnionLeft.setVisibility(INVISIBLE);
            ivDashLeft.setVisibility(VISIBLE);
        }
    }

    private void updateUnionRightUI(boolean isShow) {
        if (isShow) {
            ivUnionRight.setVisibility(VISIBLE);
            ivDashRight.setVisibility(INVISIBLE);
        } else {
            ivUnionRight.setVisibility(INVISIBLE);
            ivDashRight.setVisibility(VISIBLE);
        }
    }

    private void updateSeperateLeftUpUI(boolean isShow) {
        if (isShow) {
            ivSeperateLeftUp.setVisibility(VISIBLE);
        } else {
            ivSeperateLeftUp.setVisibility(INVISIBLE);
        }
    }

    private void updateSeperateLeftDownUI(boolean isShow) {
        if (isShow) {
            ivSeperateLeftDown.setVisibility(VISIBLE);
        } else {
            ivSeperateLeftDown.setVisibility(INVISIBLE);
        }
    }

    private void updateSeperateRightUpUI(boolean isShow) {
        if (isShow) {
            ivSeperateRightUp.setVisibility(VISIBLE);
        } else {
            ivSeperateRightUp.setVisibility(INVISIBLE);
        }
    }

    private void updateSeperateRightDownUI(boolean isShow) {
        if (isShow) {
            ivSeperateRightDown.setVisibility(VISIBLE);
        } else {
            ivSeperateRightDown.setVisibility(INVISIBLE);
        }
    }

    private void updateCenterUI(boolean isShow) {
        if (isShow) {
            ivCenter.setVisibility(VISIBLE);
        } else {
            ivCenter.setVisibility(INVISIBLE);
        }
    }

    private void updateLeftDashUI(boolean isShow) {
        if (isShow) {
            ivDashLeft.setVisibility(VISIBLE);
        } else {
            ivDashLeft.setVisibility(INVISIBLE);
        }
    }

    private void updateRightDashUI(boolean isShow) {
        if (isShow) {
            ivDashRight.setVisibility(VISIBLE);
        } else {
            ivDashRight.setVisibility(INVISIBLE);
        }
    }

    public void bCookerShowHighTemperature(boolean flag){  // b cooker high temperature
        if(flag){
            ivSeperateLeftUp.setVisibility(INVISIBLE);
            ivRedSeperateLeftUp.setVisibility(VISIBLE);
            ivUnionLeft.setVisibility(INVISIBLE);
            ivRedUnionLeft.setVisibility(INVISIBLE);
        }else {
            ivSeperateLeftUp.setVisibility(VISIBLE);
            ivRedSeperateLeftUp.setVisibility(INVISIBLE);
            ivUnionLeft.setVisibility(INVISIBLE);
            ivRedUnionLeft.setVisibility(INVISIBLE);
        }
    }

    public void bCookerShowBlackground(){  // b cooker close
        ivSeperateLeftUp.setVisibility(INVISIBLE);
        ivRedSeperateLeftUp.setVisibility(INVISIBLE);
        ivUnionLeft.setVisibility(INVISIBLE);
        ivRedUnionLeft.setVisibility(INVISIBLE);
    }

    public void aCookerShowHighTemperature(boolean flag){  // a cooker high temperature
        if(flag){
            ivSeperateLeftDown.setVisibility(INVISIBLE);
            ivRedSeperateLeftDown.setVisibility(VISIBLE);
            ivUnionLeft.setVisibility(INVISIBLE);
            ivRedUnionLeft.setVisibility(INVISIBLE);
        }else {
            ivSeperateLeftDown.setVisibility(VISIBLE);
            ivRedSeperateLeftDown.setVisibility(INVISIBLE);
            ivUnionLeft.setVisibility(INVISIBLE);
            ivRedUnionLeft.setVisibility(INVISIBLE);
        }
    }


    public void aCookerShowBlackground(){  //  a cooker close
        ivSeperateLeftDown.setVisibility(INVISIBLE);
        ivRedSeperateLeftDown.setVisibility(INVISIBLE);
        ivUnionLeft.setVisibility(INVISIBLE);
        ivRedUnionLeft.setVisibility(INVISIBLE);
    }

    public void abCookerShowHighTemperature(boolean flag){  // ab cooker high temperature
        if(flag){
            ivSeperateLeftDown.setVisibility(INVISIBLE);
            ivRedSeperateLeftDown.setVisibility(INVISIBLE);
            ivSeperateLeftUp.setVisibility(INVISIBLE);
            ivRedSeperateLeftUp.setVisibility(INVISIBLE);


            ivRedUnionLeft.setVisibility(VISIBLE);
            ivUnionLeft.setVisibility(INVISIBLE);
        }else {
            ivSeperateLeftDown.setVisibility(INVISIBLE);
            ivRedSeperateLeftDown.setVisibility(INVISIBLE);
            ivSeperateLeftUp.setVisibility(INVISIBLE);
            ivRedSeperateLeftUp.setVisibility(INVISIBLE);


            ivRedUnionLeft.setVisibility(INVISIBLE);
            ivUnionLeft.setVisibility(VISIBLE);
        }
    }

    public void centerCookerShowBlackground(){  //  center cooker close
        ivCenter.setVisibility(INVISIBLE);
        ivRedCenter.setVisibility(INVISIBLE);
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

    public void eCookerShowHighTemperature(boolean flag){  // e cooker high temperature
        if(flag){
            ivSeperateRightUp.setVisibility(INVISIBLE);
            ivRedSeperateRightUp.setVisibility(VISIBLE);
            ivUnionRight.setVisibility(INVISIBLE);
            ivRedUnionRight.setVisibility(INVISIBLE);
        }else {
            ivSeperateRightUp.setVisibility(VISIBLE);
            ivRedSeperateRightUp.setVisibility(INVISIBLE);
            ivUnionRight.setVisibility(INVISIBLE);
            ivRedUnionRight.setVisibility(INVISIBLE);
        }
    }


    public void eCookerShowBlackground(){  //  e cooker close
        ivSeperateRightUp.setVisibility(INVISIBLE);
        ivRedSeperateRightUp.setVisibility(INVISIBLE);
        ivUnionRight.setVisibility(INVISIBLE);
        ivRedUnionRight.setVisibility(INVISIBLE);
    }

    public void fCookerShowHighTemperature(boolean flag){  // f cooker high temperature
        if(flag){
            ivSeperateRightDown.setVisibility(INVISIBLE);
            ivRedSeperateRightDown.setVisibility(VISIBLE);
            ivUnionRight.setVisibility(INVISIBLE);
            ivRedUnionRight.setVisibility(INVISIBLE);
        }else {
            ivSeperateRightDown.setVisibility(VISIBLE);
            ivRedSeperateRightDown.setVisibility(INVISIBLE);
            ivUnionRight.setVisibility(INVISIBLE);
            ivRedUnionRight.setVisibility(INVISIBLE);
        }
    }


    public void fCookerShowBlackground(){  // f cooker close
        ivSeperateRightDown.setVisibility(INVISIBLE);
        ivRedSeperateRightDown.setVisibility(INVISIBLE);
        ivUnionRight.setVisibility(INVISIBLE);
        ivRedUnionRight.setVisibility(INVISIBLE);
    }

    public void efCookerShowHighTemperature(boolean flag){  // ef cooker high temperature
        if(flag){
            ivSeperateRightDown.setVisibility(INVISIBLE);
            ivRedSeperateRightDown.setVisibility(INVISIBLE);
            ivSeperateRightUp.setVisibility(INVISIBLE);
            ivRedSeperateRightUp.setVisibility(INVISIBLE);

            ivRedUnionRight.setVisibility(VISIBLE);
            ivUnionRight.setVisibility(INVISIBLE);
        }else {
            ivSeperateRightDown.setVisibility(INVISIBLE);
            ivRedSeperateRightDown.setVisibility(INVISIBLE);
            ivSeperateRightUp.setVisibility(INVISIBLE);
            ivRedSeperateRightUp.setVisibility(INVISIBLE);

            ivRedUnionRight.setVisibility(INVISIBLE);
            ivUnionRight.setVisibility(VISIBLE);

        }
    }

}
