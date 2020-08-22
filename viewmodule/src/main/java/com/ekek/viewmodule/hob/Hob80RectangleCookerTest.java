package com.ekek.viewmodule.hob;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ekek.commonmodule.utils.DisplayUtil;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.viewmodule.R;
import com.ekek.viewmodule.R2;
import com.ekek.viewmodule.contract.HobCircleCookerContract;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Hob80RectangleCookerTest extends FrameLayout {
    private static final int HOB_RETANGLE_WORK_MODE_WORK_SEPARATE = 0;
    private static final int HOB_RETANGLE_WORK_MODE_WORK_UNITE = 1;

    private static final int HOB_VIEW_WORK_MODE_POWER_OFF = 0;//
    private static final int HOB_VIEW_WORK_MODE_FIRE_GEAR_WITHOUT_TIMER = 100;//
    private static final int HOB_VIEW_WORK_MODE_FIRE_GEAR_WITH_TIMER = 200;//
    private static final int HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITHOUT_TIMER = 300;//
    private static final int HOB_VIEW_WORK_MODE_TEMP_GEAR_WITHOUT_TIMER = 400;//
    private static final int HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER = 500;//
    private static final int HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER_AND_RECIPES_FIRST = 501;//先显示菜谱图片，再显示温度定时
    private static final int HOB_VIEW_WORK_MODE_TEMP_INDICATOR_GEAR_WITH_TIMER_AND_RECIPES_SECOND = 502;//先显示温度定时，再显示菜谱图片
    private static final int HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER = 600;//
    private static final int HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_FIRST = 601;//先显示菜谱图片，再显示温度定时
    private static final int HOB_VIEW_WORK_MODE_TEMP_GEAR_WITH_TIMER_AND_RECIPES_SECOND = 602;//先显示温度定时，再显示菜谱图片
    private static final int HOB_VIEW_WORK_MODE_ABNORMAL_NO_PAN = 700;//
    private static final int HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP = 701;//
    private static final int HOB_VIEW_WORK_MODE_ABNORMAL_ERROR_OCURR = 702;//
    private static final int HOB_VIEW_WORK_MODE_UPATE_TIMER = 800;
    private static final int HOB_VIEW_WORK_MODE_PAUSE = 900;//
    private static final int HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR = 1000;//
    private static final int HOB_VIEW_WORK_MODE_PREPARE_TEMP_SENSOR_WITH_INDICATOR = 1001;//
    private static final int HOB_VIEW_WORK_MODE_TEMP_SENSOR_READY = 1002;//
    private static final int HOB_VIEW_WORK_MODE_TEMP_WORK_FINISH_WAIT_USER_CONFIRM= 1003;

    private static final int HOB_COOKER_POSITION_UP = 0;
    private static final int HOB_COOKER_POSITION_DOWN = 1;

    private static final int RETANGLE_HOB_UI_MODE_SHOW_SEPERATE = 0;
    private static final int RETANGLE_HOB_UI_MODE_SHOW_UNION = 1;

    @BindView(R2.id.hsrc_a)
    Hob80SeperateRectangleCooker hsrcA;
    @BindView(R2.id.hsrc_b)
    Hob80SeperateRectangleCooker hsrcB;
    @BindView(R2.id.ll_seperate_area)
    LinearLayout llSeperateArea;
    @BindView(R2.id.hsrc_ab)
    Hob80UnionRectangleCooker hsrcAb;
    Unbinder unbinder;
    int unionCookerID = -1;
    int downCookerID = -1;
    int upCookerID = -1;
    private float touchDownX;
    private float touchDownY;
    private OnHobRectangleCookerListener mListener;
    private int retangleHobWorkMode = HOB_RETANGLE_WORK_MODE_WORK_SEPARATE;
    private int retangleHobUIMode = RETANGLE_HOB_UI_MODE_SHOW_SEPERATE; //用于控制如两个小炉头都关闭状态或者高温状态时，UI显示的是无区模式
    protected int upWorkMode = HOB_VIEW_WORK_MODE_POWER_OFF;
    protected int downWorkMode = HOB_VIEW_WORK_MODE_POWER_OFF;
    protected int unionWorkMode = HOB_VIEW_WORK_MODE_POWER_OFF;
    private CookerParameter upCookerParameter,downCookerParameter,unionCookerParameter;

    public Hob80RectangleCookerTest(@NonNull Context context) {
        super(context);
    }

    public Hob80RectangleCookerTest(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View mRootView = LayoutInflater.from(context).inflate(R.layout.viewmodule_hob_80_rectangle_cooker_test, this);
        unbinder = ButterKnife.bind(this, mRootView);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.viewmodule_HobRetangleCookerView);
        String idStr = a.getString(R.styleable.viewmodule_HobRetangleCookerView_viewmodule_retangle_cooker_id);
        a.recycle();

        if (idStr != null) {
            String[] idStrs = idStr.split("\\|");
            if (idStrs.length == 2) {
                downCookerID = Integer.valueOf(idStrs[0]);
                upCookerID = Integer.valueOf(idStrs[1]);
                unionCookerID = downCookerID * 10 + upCookerID;//如1|2，则unionCookerID为12
            }

        }


        initCooker();



    }

    private void initCooker() {
        hsrcA.setCookerID(downCookerID);
        hsrcA.setCookerPosition(HOB_COOKER_POSITION_DOWN);//设置是上炉头还是下炉头，因为上下炉头关机按钮位置不一样
        hsrcB.setCookerID(upCookerID);
        hsrcB.setCookerPosition(HOB_COOKER_POSITION_UP);
        hsrcAb.setCookerID(unionCookerID);

        upCookerParameter = new CookerParameter(upCookerID);
        downCookerParameter = new CookerParameter(downCookerID);
        unionCookerParameter = new CookerParameter(unionCookerID);

        hsrcA.setOnHobCircleCookerListener(new HobCircleCookerContract.OnHobCircleCookerListener() {
            @Override
            public void onCookerPowerOff() {
                if (mListener != null) mListener.onCookerPowerOff(downCookerID);
            }

            @Override
            public void onCookerPowerOn() {
                LogUtil.d("Enter:: hsrcA onCookerPowerOn");
                if (mListener != null) mListener.onCookerPowerOn(downCookerID);
            }

            @Override
            public void onSetGear() {
                if (mListener != null) mListener.onSetGear(downCookerID);
            }

            @Override
            public void onSetTimer() {
                if (mListener != null) mListener.onSetTimer(downCookerID);
            }

            @Override
            public void onReadyToCook() {
                if (mListener != null) mListener.onReadyToCook(downCookerID);
            }

            @Override
            public void onRequestAddTenMinute() {
                LogUtil.d("Enter:: onRequestAddTenMinute---->" + downCookerID);
                if (mListener != null) mListener.onRequestAddTenMinute(downCookerID);
            }

            @Override
            public void onRequestKeepWarm() {
                if (mListener != null) mListener.onRequestKeepWarm(downCookerID);
            }
        });

        hsrcB.setOnHobCircleCookerListener(new HobCircleCookerContract.OnHobCircleCookerListener() {
            @Override
            public void onCookerPowerOff() {
                if (mListener != null) mListener.onCookerPowerOff(upCookerID);
            }

            @Override
            public void onCookerPowerOn() {
                LogUtil.d("Enter:: hsrcB onCookerPowerOn");
                if (mListener != null) mListener.onCookerPowerOn(upCookerID);
            }

            @Override
            public void onSetGear() {
                if (mListener != null) mListener.onSetGear(upCookerID);
            }

            @Override
            public void onSetTimer() {
                if (mListener != null) mListener.onSetTimer(upCookerID);
            }

            @Override
            public void onReadyToCook() {
                if (mListener != null) mListener.onReadyToCook(upCookerID);
            }

            @Override
            public void onRequestAddTenMinute() {
                LogUtil.d("Enter:: onRequestAddTenMinute---->" + upCookerID);
                if (mListener != null) mListener.onRequestAddTenMinute(upCookerID);
            }

            @Override
            public void onRequestKeepWarm() {
                if (mListener != null) mListener.onRequestKeepWarm(upCookerID);
            }
        });


        hsrcAb.setOnHobCircleCookerListener(new HobCircleCookerContract.OnHobCircleCookerListener() {
            @Override
            public void onCookerPowerOff() {
                if (mListener != null) mListener.onCookerPowerOff(unionCookerID);
                retangleHobWorkMode = HOB_RETANGLE_WORK_MODE_WORK_SEPARATE;
            }

            @Override
            public void onCookerPowerOn() {
                LogUtil.d("Enter:: hsrcAb cooker power on");
                if (mListener != null) mListener.onCookerPowerOn(unionCookerID);
            }

            @Override
            public void onSetGear() {
                if (mListener != null) mListener.onSetGear(unionCookerID);
            }

            @Override
            public void onSetTimer() {
                if (mListener != null) mListener.onSetTimer(unionCookerID);
            }

            @Override
            public void onReadyToCook() {
                if (mListener != null) mListener.onReadyToCook(unionCookerID);
            }

            @Override
            public void onRequestAddTenMinute() {
                if (mListener != null) mListener.onRequestAddTenMinute(unionCookerID);
            }

            @Override
            public void onRequestKeepWarm() {
                if (mListener != null) mListener.onRequestKeepWarm(unionCookerID);
            }
        });


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogUtil.d("dispatchTouchEvent---action--->" + ev.getAction());
        LogUtil.d("dispatchTouchEvent---point count--->" + ev.getPointerCount() + "---action---->" + ev.getAction());
        LogUtil.d(" int index = event.getActionIndex()----->" + ev.getActionIndex());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDownX = ev.getX();
                touchDownY = ev.getY();
                return true;


            case MotionEvent.ACTION_MOVE:
                if (Math.abs(touchDownX - ev.getX()) >= ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    return false;
                } else {
                    return true;
                }

            case MotionEvent.ACTION_UP:
                LogUtil.d("Enter:: ontouch up");
                //切换炉头模式
                if (Math.abs(ev.getY() - touchDownY) > DisplayUtil.dp2px(getContext(),100) &&
                        ((touchDownY < hsrcAb.getCookerViewHeight() / 2 && ev.getY() > hsrcAb.getCookerViewHeight() / 2) || (touchDownY > hsrcAb.getCookerViewHeight() / 2 && ev.getY() < hsrcAb.getCookerViewHeight() / 2) )
                ) {
                    LogUtil.d("Enter:: select unite work mode---downMode--->" + downWorkMode + "---upmode---->" + upWorkMode);
                    if (downWorkMode == HOB_VIEW_WORK_MODE_POWER_OFF &&upWorkMode == HOB_VIEW_WORK_MODE_POWER_OFF ) {

                        switchRetangleHobWorkMode(true);
                    }else if (downWorkMode == HOB_VIEW_WORK_MODE_POWER_OFF && upWorkMode == HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP) {

                        switchRetangleHobWorkMode(true);
                    }else if (downWorkMode == HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP && upWorkMode == HOB_VIEW_WORK_MODE_POWER_OFF) {

                        switchRetangleHobWorkMode(true);
                    }else if (downWorkMode == HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP && upWorkMode == HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP) {

                        switchRetangleHobWorkMode(true);
                    }
                    playSoundEffect(SoundEffectConstants.CLICK);
                    return true;

                }
                //处理点击事件
                if (Math.abs(ev.getX() - touchDownX) > DisplayUtil.dp2px(getContext(),38) ||
                        Math.abs(ev.getY() - touchDownY) > DisplayUtil.dp2px(getContext(),38)
                ) {
                    return false;
                }else {
                    LogUtil.d("process click event--retangleHobWorkMode------>" + retangleHobWorkMode + "----downworkmode-->" + downWorkMode + "---upworkmode-->" + upWorkMode);
                    if (retangleHobWorkMode == HOB_RETANGLE_WORK_MODE_WORK_UNITE ) {
                        if (unionWorkMode == HOB_VIEW_WORK_MODE_POWER_OFF || unionWorkMode == HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP) {
                            if (downWorkMode == HOB_VIEW_WORK_MODE_POWER_OFF && upWorkMode == HOB_VIEW_WORK_MODE_POWER_OFF ) {
                                playSoundEffect(SoundEffectConstants.CLICK);
                                retangleHobWorkMode = HOB_RETANGLE_WORK_MODE_WORK_SEPARATE;
                                if (ev.getY() < hsrcAb.getCookerViewHeight() / 2) {
                                    if (mListener != null) mListener.onCookerPowerOn(upCookerID);

                                }else if (ev.getY() > hsrcAb.getCookerViewHeight() / 2) {
                                    if (mListener != null) mListener.onCookerPowerOn(downCookerID);

                                }

                            }else if (downWorkMode == HOB_VIEW_WORK_MODE_POWER_OFF && upWorkMode == HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP) {
                                playSoundEffect(SoundEffectConstants.CLICK);
                                retangleHobWorkMode = HOB_RETANGLE_WORK_MODE_WORK_SEPARATE;
                                if (ev.getY() < hsrcAb.getCookerViewHeight() / 2) {
                                    if (mListener != null) mListener.onCookerPowerOn(upCookerID);

                                }else if (ev.getY() > hsrcAb.getCookerViewHeight() / 2) {
                                    if (mListener != null) mListener.onCookerPowerOn(downCookerID);

                                }

                            }else if (downWorkMode == HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP && upWorkMode == HOB_VIEW_WORK_MODE_POWER_OFF) {
                                playSoundEffect(SoundEffectConstants.CLICK);
                                retangleHobWorkMode = HOB_RETANGLE_WORK_MODE_WORK_SEPARATE;
                                if (ev.getY() < hsrcAb.getCookerViewHeight() / 2) {
                                    if (mListener != null) mListener.onCookerPowerOn(upCookerID);

                                }else if (ev.getY() > hsrcAb.getCookerViewHeight() / 2) {
                                    if (mListener != null) mListener.onCookerPowerOn(downCookerID);

                                }

                            }else if (downWorkMode == HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP && upWorkMode == HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP) {
                                playSoundEffect(SoundEffectConstants.CLICK);
                                retangleHobWorkMode = HOB_RETANGLE_WORK_MODE_WORK_SEPARATE;
                                if (ev.getY() < hsrcAb.getCookerViewHeight() / 2) {
                                    if (mListener != null) mListener.onCookerPowerOn(upCookerID);

                                }else if (ev.getY() > hsrcAb.getCookerViewHeight() / 2) {
                                    if (mListener != null) mListener.onCookerPowerOn(downCookerID);

                                }

                            }
                        }else {
                            hsrcAb.processClickEvent(ev);
                        }



                    }else if (retangleHobWorkMode == HOB_RETANGLE_WORK_MODE_WORK_SEPARATE){
                        LogUtil.d("Enter:: click seperate");
                        if (ev.getY() < hsrcAb.getCookerViewHeight() / 2) {
                            LogUtil.d("Enter:: click seperate-----1");
                            hsrcB.processClickEvent(ev);
                            return true;
                        }else if (ev.getY() > hsrcAb.getCookerViewHeight() / 2) {
                            LogUtil.d("Enter:: click seperate------2");
                            //修正Y坐标
                            ev.setLocation(ev.getX(),ev.getY() - hsrcAb.getCookerViewHeight() / 2);
                            hsrcA.processClickEvent(ev);
                            return true;
                        }


                    }

                    return true;
                }

        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }


    private void switchRetangleHobWorkMode(boolean isUnion) {
        LogUtil.d("Enter:: switchRetangleHobWorkMode---isUnion--->" + isUnion);
        if (isUnion) {
            if (mListener != null) mListener.onCookerPowerOn(unionCookerID);
            retangleHobWorkMode = HOB_RETANGLE_WORK_MODE_WORK_UNITE;


        }else {
            retangleHobWorkMode = HOB_RETANGLE_WORK_MODE_WORK_SEPARATE;


        }
    }

    private void updateRetangleHobUI() {
        if (retangleHobWorkMode == HOB_RETANGLE_WORK_MODE_WORK_SEPARATE) {
            if (retangleHobUIMode == RETANGLE_HOB_UI_MODE_SHOW_UNION) {
                hsrcAb.invalidate();
                llSeperateArea.setVisibility(INVISIBLE);
                hsrcAb.setVisibility(VISIBLE);
            }else {
                hsrcA.invalidate();
                hsrcB.invalidate();
                llSeperateArea.setVisibility(VISIBLE);
                hsrcAb.setVisibility(INVISIBLE);
            }


        }else if (retangleHobWorkMode == HOB_RETANGLE_WORK_MODE_WORK_UNITE) {
            hsrcAb.invalidate();
            llSeperateArea.setVisibility(INVISIBLE);
            hsrcAb.setVisibility(VISIBLE);

        }
    }


    private synchronized void upateUI(int cookerID,int workMode, int fireValue, int tempValue, int realTempValue, int hourValue, int minuteValue, int tempIndicatorID, int recipesID, int recipesShowOrder, String errorMessage) {
        LogUtil.d("Enter:: updateUI----cookerID--->" + cookerID + "----workMode---->" + workMode + "----retangleHobWorkMode--->" + retangleHobWorkMode);
        if (cookerID == downCookerID ) {
            downWorkMode = workMode;
            downCookerParameter.updateParameter(cookerID,workMode,fireValue,tempValue,realTempValue,hourValue,minuteValue,tempIndicatorID,recipesID,recipesShowOrder,errorMessage);
            hsrcA.updateCookerView(workMode,fireValue,tempValue,realTempValue,hourValue,minuteValue,tempIndicatorID,recipesID,recipesShowOrder,errorMessage);
        }else if (cookerID == upCookerID ) {
            upWorkMode = workMode;
            upCookerParameter.updateParameter(cookerID,workMode,fireValue,tempValue,realTempValue,hourValue,minuteValue,tempIndicatorID,recipesID,recipesShowOrder,errorMessage);
            hsrcB.updateCookerView(workMode,fireValue,tempValue,realTempValue,hourValue,minuteValue,tempIndicatorID,recipesID,recipesShowOrder,errorMessage);
        }else if (cookerID == unionCookerID ) {
            unionWorkMode = workMode;
            unionCookerParameter.updateParameter(cookerID,workMode,fireValue,tempValue,realTempValue,hourValue,minuteValue,tempIndicatorID,recipesID,recipesShowOrder,errorMessage);
            hsrcAb.updateCookerView(workMode,fireValue,tempValue,realTempValue,hourValue,minuteValue,tempIndicatorID,recipesID,recipesShowOrder,errorMessage);
        }

        if (downWorkMode != upWorkMode) {
            retangleHobUIMode = RETANGLE_HOB_UI_MODE_SHOW_SEPERATE;
        }

        if (cookerID == unionCookerID ) {//
            //LogUtil.d("samhung workmode--->" + downWorkMode + "----up---->" + upWorkMode + "---->" + unionWorkMode);
            if (downWorkMode == upWorkMode && (downWorkMode == HOB_VIEW_WORK_MODE_POWER_OFF || downWorkMode == HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP)) {
                if (retangleHobWorkMode == HOB_RETANGLE_WORK_MODE_WORK_SEPARATE) {
                    retangleHobUIMode = RETANGLE_HOB_UI_MODE_SHOW_UNION;
                }else {
                    retangleHobUIMode = RETANGLE_HOB_UI_MODE_SHOW_SEPERATE;
                }


            }else if (downWorkMode == upWorkMode && downWorkMode == HOB_VIEW_WORK_MODE_ABNORMAL_ERROR_OCURR) {
                retangleHobUIMode = RETANGLE_HOB_UI_MODE_SHOW_SEPERATE;
                if (!hsrcA.getErrorMessage().equals(hsrcB.getErrorMessage())) {
                    retangleHobWorkMode = HOB_RETANGLE_WORK_MODE_WORK_SEPARATE;
                }

            }else if (downWorkMode != upWorkMode && (downWorkMode == HOB_VIEW_WORK_MODE_ABNORMAL_ERROR_OCURR || upWorkMode == HOB_VIEW_WORK_MODE_ABNORMAL_ERROR_OCURR)) {
                retangleHobUIMode = RETANGLE_HOB_UI_MODE_SHOW_SEPERATE;
                retangleHobWorkMode = HOB_RETANGLE_WORK_MODE_WORK_SEPARATE;
            }else if (downWorkMode != upWorkMode && (downWorkMode == HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP || upWorkMode == HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP) ) {
               // LogUtil.d("samhungeeeeee--------unionCookerParameter--->" + unionCookerParameter.fireValue + "---->" + unionCookerParameter.tempValue);
                //LogUtil.e("samhungeeeeee--------unionCookerParameter--->" + unionCookerParameter.workMode );
                if ((unionCookerParameter.fireValue == -1 || unionCookerParameter.fireValue == 0) && (unionCookerParameter.tempValue == -1 || unionCookerParameter.tempValue == 0) && (unionWorkMode == HOB_VIEW_WORK_MODE_POWER_OFF || unionWorkMode == HOB_VIEW_WORK_MODE_ABNORMAL_HIGH_TEMP )) {

                    retangleHobUIMode = RETANGLE_HOB_UI_MODE_SHOW_SEPERATE;
                    retangleHobWorkMode = HOB_RETANGLE_WORK_MODE_WORK_SEPARATE;



                }else {

                    retangleHobUIMode = RETANGLE_HOB_UI_MODE_SHOW_UNION;
                    retangleHobWorkMode = HOB_RETANGLE_WORK_MODE_WORK_UNITE;

                }

            }

            //如果无区模式炉头发生错误，
            // 1. 如两个小炉头错误代码一样，则工作模式还是无区；
            // 2. 如两个小炉头错误代码不一样，则工作模式变为有区，并分别显示错误代码；
            // 3. 如一个小炉头有错误代码，而另一个小炉头正常，则工作模式变为有区，正常炉头继续工作，有错误的炉头显示错误代码；



            updateRetangleHobUI();

        }





    }

    public boolean isUnited() {
        return retangleHobWorkMode == RETANGLE_HOB_UI_MODE_SHOW_UNION;
    }

    public int[] getCookerIDs() {
        int[] ids = {downCookerID,upCookerID,unionCookerID};
        return ids;
    }

    public void setOnHobRectangleCookerListener(OnHobRectangleCookerListener listener) {
        mListener = listener;
    }

    public void updateCookerView(int cookerID ,int workMode, int fireValue, int tempValue, int realTempValue,int hourValue, int minuteValue, int tempIndicatorID, int recipesID,int recipesShowOrder,String errorMessage) {
        upateUI(cookerID,workMode, fireValue, tempValue, realTempValue,hourValue, minuteValue, tempIndicatorID, recipesID,recipesShowOrder,errorMessage);
    }

    public int getHourValue(int cookerId) {
        if (cookerId == upCookerID) {
            return hsrcB.getHourValue();
        } else if (cookerId == downCookerID) {
            return hsrcA.getHourValue();
        } else if (cookerId == unionCookerID) {
            return hsrcAb.getHourValue();
        }
        return -1;
    }
    public int getMinuteValue(int cookerId) {
        if (cookerId == upCookerID) {
            return hsrcB.getMinuteValue();
        } else if (cookerId == downCookerID) {
            return hsrcA.getMinuteValue();
        } else if (cookerId == unionCookerID) {
            return hsrcAb.getMinuteValue();
        }
        return -1;
    }

    public void setReadyToCookText(String readyToCookText) {
        hsrcA.setReadyToCookText(readyToCookText);
        hsrcB.setReadyToCookText(readyToCookText);
        hsrcAb.setReadyToCookText(readyToCookText);
    }

    public interface OnHobRectangleCookerListener {
        void onCookerPowerOff(int cookerID);

        void onCookerPowerOn(int cookerID);

        void onSetGear(int cookerID);

        void onSetTimer(int cookerID);

        void onReadyToCook(int cookerID);

        void onRequestAddTenMinute(int cookerID);

        void onRequestKeepWarm(int cookerID);
    }

    private class CookerParameter {
        int cookerID;
        int workMode;
        int fireValue = 0;
        int tempValue = 0;
        int realTempValue;
        int hourValue;
        int minuteValue;
        int tempIndicatorID;
        int recipesID;
        int recipesShowOrder;
        String errorMessage;

        public CookerParameter(int cookerID) {
            this.cookerID = cookerID;
        }

        public CookerParameter(int cookerID, int workMode, int fireValue, int tempValue, int realTempValue, int hourValue, int minuteValue, int tempIndicatorID, int recipesID, int recipesShowOrder, String errorMessage) {
            this.cookerID = cookerID;
            this.workMode = workMode;
            this.fireValue = fireValue;
            this.tempValue = tempValue;
            this.realTempValue = realTempValue;
            this.hourValue = hourValue;
            this.minuteValue = minuteValue;
            this.tempIndicatorID = tempIndicatorID;
            this.recipesID = recipesID;
            this.recipesShowOrder = recipesShowOrder;
            this.errorMessage = errorMessage;
        }

        public void updateParameter(int cookerID, int workMode, int fireValue, int tempValue, int realTempValue, int hourValue, int minuteValue, int tempIndicatorID, int recipesID, int recipesShowOrder, String errorMessage) {
            this.cookerID = cookerID;
            this.workMode = workMode;
            this.fireValue = fireValue;
            this.tempValue = tempValue;
            this.realTempValue = realTempValue;
            this.hourValue = hourValue;
            this.minuteValue = minuteValue;
            this.tempIndicatorID = tempIndicatorID;
            this.recipesID = recipesID;
            this.recipesShowOrder = recipesShowOrder;
            this.errorMessage = errorMessage;
        }


        public int getCookerID() {
            return cookerID;
        }

        public void setCookerID(int cookerID) {
            this.cookerID = cookerID;
        }

        public int getWorkMode() {
            return workMode;
        }

        public void setWorkMode(int workMode) {
            this.workMode = workMode;
        }

        public int getFireValue() {
            return fireValue;
        }

        public void setFireValue(int fireValue) {
            this.fireValue = fireValue;
        }

        public int getTempValue() {
            return tempValue;
        }

        public void setTempValue(int tempValue) {
            this.tempValue = tempValue;
        }

        public int getRealTempValue() {
            return realTempValue;
        }

        public void setRealTempValue(int realTempValue) {
            this.realTempValue = realTempValue;
        }

        public int getHourValue() {
            return hourValue;
        }

        public void setHourValue(int hourValue) {
            this.hourValue = hourValue;
        }

        public int getMinuteValue() {
            return minuteValue;
        }

        public void setMinuteValue(int minuteValue) {
            this.minuteValue = minuteValue;
        }

        public int getTempIndicatorID() {
            return tempIndicatorID;
        }

        public void setTempIndicatorID(int tempIndicatorID) {
            this.tempIndicatorID = tempIndicatorID;
        }

        public int getRecipesID() {
            return recipesID;
        }

        public void setRecipesID(int recipesID) {
            this.recipesID = recipesID;
        }

        public int getRecipesShowOrder() {
            return recipesShowOrder;
        }

        public void setRecipesShowOrder(int recipesShowOrder) {
            this.recipesShowOrder = recipesShowOrder;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}
