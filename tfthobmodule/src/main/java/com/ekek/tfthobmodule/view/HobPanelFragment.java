package com.ekek.tfthobmodule.view;

import android.os.Bundle;
import android.widget.ImageButton;

import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.tfthobmodule.R;
import com.ekek.tfthobmodule.constants.TFTHobConstant;
import com.ekek.tfthobmodule.contract.HobPanelContract;
import com.ekek.tfthobmodule.data.CookerSettingData;
import com.ekek.tfthobmodule.presenter.HobPanelPresenter;
import com.ekek.viewmodule.hob.CircleCookerView;

import butterknife.BindView;
import butterknife.OnClick;

public class HobPanelFragment extends BaseFragment implements HobPanelContract.View {
    @BindView(R.id.ccv_c)
    CircleCookerView ccvC;
    @BindView(R.id.ccv_b)
    CircleCookerView ccvB;
    @BindView(R.id.ccv_a)
    CircleCookerView ccvA;
    @BindView(R.id.ib_hood)
    ImageButton ibHood;
    private HobPanelContract.Presenter mPresenter;
    private boolean needSelectCooker = false;
    private CookerSettingData cookerSettingDataFromTableDetail;

    public HobPanelFragment() {
        mPresenter = new HobPanelPresenter(this);
    }

    @Override
    public int initLyaout() {
        return R.layout.tfthobmodule_fragment_hob_panel;
    }

    @Override
    public void initListener() {

    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        ccvA.setOnCircleCookerViewListener(new CircleCookerView.OnCircleCookerViewListener() {
            @Override
            public void onCookerPowerOff() {
                mPresenter.stopTimerForPoweroff(TFTHobConstant.COOKER_TYPE_A_COOKER);
            }

            @Override
            public void onCookerPowerOn() {
                //doOnRequestSettingCooker(mPresenter.getCookerSettingData(TFTHobConstant.COOKER_TYPE_A_COOKER));
                LogUtil.d("Enter:: onCookerPowerOn ");

                if (needSelectCooker) {
                    cookerSettingDataFromTableDetail.setCookerID(TFTHobConstant.COOKER_TYPE_A_COOKER);
                    setCookerSettingData(cookerSettingDataFromTableDetail);
                    needSelectCooker = false;

                }else {
                    doOnRequestSettingCooker(mPresenter.getCookerSettingData(TFTHobConstant.COOKER_TYPE_A_COOKER));

                }
            }

            @Override
            public void onSetGear() {
                CookerSettingData data = mPresenter.getCookerSettingData(TFTHobConstant.COOKER_TYPE_A_COOKER);
                data.resetSettingGearMode();
                doOnRequestSettingCooker(data);
            }

            @Override
            public void onSetTimer() {
                CookerSettingData data = mPresenter.getCookerSettingData(TFTHobConstant.COOKER_TYPE_A_COOKER);
                data.setCookerSettingMode(CookerSettingData.SETTING_MODE_TIMER);
                doOnRequestSettingCooker(data);
            }

            @Override
            public void onUpdateGear() {

            }

            @Override
            public void onTimerIsUp() {

            }

            @Override
            public void onRequestAddTenMinute() {
                CookerSettingData data = mPresenter.getCookerSettingDataForAddTemMinute(TFTHobConstant.COOKER_TYPE_A_COOKER);
                mPresenter.setCookerSettingData(data);
            }

            @Override
            public void onRequestKeepWarm() {
                CookerSettingData data = mPresenter.getCookerSettingDataForKeepWarm(TFTHobConstant.COOKER_TYPE_A_COOKER);
                mPresenter.setCookerSettingData(data);
            }

            @Override
            public void onRequestToCookForUserReady() {
                mPresenter.requestToCookForUserReady(TFTHobConstant.COOKER_TYPE_A_COOKER);
            }
        });

        ////////////////////////////////////////////////////////
        ccvB.setOnCircleCookerViewListener(new CircleCookerView.OnCircleCookerViewListener() {
            @Override
            public void onCookerPowerOff() {
                mPresenter.stopTimerForPoweroff(TFTHobConstant.COOKER_TYPE_B_COOKER);
            }

            @Override
            public void onCookerPowerOn() {
               // doOnRequestSettingCooker(mPresenter.getCookerSettingData(TFTHobConstant.COOKER_TYPE_B_COOKER));
                LogUtil.d("Enter:: onCookerPowerOn ");

                if (needSelectCooker) {
                    cookerSettingDataFromTableDetail.setCookerID(TFTHobConstant.COOKER_TYPE_B_COOKER);
                    setCookerSettingData(cookerSettingDataFromTableDetail);
                    needSelectCooker = false;

                }else {
                    doOnRequestSettingCooker(mPresenter.getCookerSettingData(TFTHobConstant.COOKER_TYPE_B_COOKER));

                }
            }

            @Override
            public void onSetGear() {
                CookerSettingData data = mPresenter.getCookerSettingData(TFTHobConstant.COOKER_TYPE_B_COOKER);
                data.resetSettingGearMode();
                doOnRequestSettingCooker(data);
            }

            @Override
            public void onSetTimer() {
                CookerSettingData data = mPresenter.getCookerSettingData(TFTHobConstant.COOKER_TYPE_B_COOKER);
                data.setCookerSettingMode(CookerSettingData.SETTING_MODE_TIMER);
                doOnRequestSettingCooker(data);
            }

            @Override
            public void onUpdateGear() {

            }

            @Override
            public void onTimerIsUp() {

            }

            @Override
            public void onRequestAddTenMinute() {
                CookerSettingData data = mPresenter.getCookerSettingDataForAddTemMinute(TFTHobConstant.COOKER_TYPE_B_COOKER);
                mPresenter.setCookerSettingData(data);
            }

            @Override
            public void onRequestKeepWarm() {
                CookerSettingData data = mPresenter.getCookerSettingDataForKeepWarm(TFTHobConstant.COOKER_TYPE_B_COOKER);
                mPresenter.setCookerSettingData(data);
            }

            @Override
            public void onRequestToCookForUserReady() {
                mPresenter.requestToCookForUserReady(TFTHobConstant.COOKER_TYPE_B_COOKER);
            }
        });

        ///////////////////////////////////////////////////////
        ccvC.setOnCircleCookerViewListener(new CircleCookerView.OnCircleCookerViewListener() {
            @Override
            public void onCookerPowerOff() {
                mPresenter.stopTimerForPoweroff(TFTHobConstant.COOKER_TYPE_C_COOKER);
            }

            @Override
            public void onCookerPowerOn() {
                LogUtil.d("Enter:: onCookerPowerOn ---->" + needSelectCooker);
                if (needSelectCooker) {
                    cookerSettingDataFromTableDetail.setCookerID(TFTHobConstant.COOKER_TYPE_C_COOKER);
                    setCookerSettingData(cookerSettingDataFromTableDetail);
                    needSelectCooker = false;

                }else {
                    doOnRequestSettingCooker(mPresenter.getCookerSettingData(TFTHobConstant.COOKER_TYPE_C_COOKER));

                }

            }

            @Override
            public void onSetGear() {
                CookerSettingData data = mPresenter.getCookerSettingData(TFTHobConstant.COOKER_TYPE_C_COOKER);
                data.resetSettingGearMode();
                doOnRequestSettingCooker(data);

            }

            @Override
            public void onSetTimer() {
                CookerSettingData data = mPresenter.getCookerSettingData(TFTHobConstant.COOKER_TYPE_C_COOKER);
                data.setCookerSettingMode(CookerSettingData.SETTING_MODE_TIMER);
                doOnRequestSettingCooker(data);
            }

            @Override
            public void onUpdateGear() {

            }

            @Override
            public void onTimerIsUp() {

            }

            @Override
            public void onRequestAddTenMinute() {
                CookerSettingData data = mPresenter.getCookerSettingDataForAddTemMinute(TFTHobConstant.COOKER_TYPE_C_COOKER);
                mPresenter.setCookerSettingData(data);
            }

            @Override
            public void onRequestKeepWarm() {
                CookerSettingData data = mPresenter.getCookerSettingDataForKeepWarm(TFTHobConstant.COOKER_TYPE_C_COOKER);
                mPresenter.setCookerSettingData(data);

            }

            @Override
            public void onRequestToCookForUserReady() {
                mPresenter.requestToCookForUserReady(TFTHobConstant.COOKER_TYPE_C_COOKER);
            }
        });

        LogUtil.d("ccvC.isHardwareAccelerated()------->" + ccvC.isHardwareAccelerated());
    }


    @Override
    public void updateHobStatusUI(int id, CookerSettingData data) {
        //LogUtil.d("Enter:: updateHobStatusUI-----id---->" + id);

        switch (id) {
            case TFTHobConstant.COOKER_TYPE_A_COOKER:
                if (data.getFireValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//档位模式
                    if (data.getTimerHourValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//有定时
                        ccvA.setFireGearWithTimer(data.getFireValue(), data.getTimerHourValue(), data.getTimerMinuteValue());
                    } else {//没定时
                        ccvA.setFireGearWithoutTimer(data.getFireValue());
                    }


                } else {//温控模式
                    if (data.getTimerHourValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//有定时
                        ccvA.setTempValueWithTimer(data.getTempValue(), data.getTempIdentifyDrawableResourceID(), data.getTimerHourValue(), data.getTimerMinuteValue());

                    } else {//没定时
                        ccvA.setTempValue(data.getTempValue(), data.getTempIdentifyDrawableResourceID());

                    }
                }
                break;
            case TFTHobConstant.COOKER_TYPE_B_COOKER:
                if (data.getFireValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//档位模式
                    if (data.getTimerHourValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//有定时
                        ccvB.setFireGearWithTimer(data.getFireValue(), data.getTimerHourValue(), data.getTimerMinuteValue());
                    } else {//没定时
                        ccvB.setFireGearWithoutTimer(data.getFireValue());
                    }


                } else {//温控模式
                    if (data.getTimerHourValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//有定时
                        ccvB.setTempValueWithTimer(data.getTempValue(), data.getTempIdentifyDrawableResourceID(), data.getTimerHourValue(), data.getTimerMinuteValue());

                    } else {//没定时
                        ccvB.setTempValue(data.getTempValue(), data.getTempIdentifyDrawableResourceID());
                    }
                }
                break;
            case TFTHobConstant.COOKER_TYPE_C_COOKER:
                //if (data.getFireValue() != -1 || data.getTimerHourValue() != -1) ccvC.setFireGearWithTimer(data.getFireValue(),data.getTimerHourValue(),data.getTimerMinuteValue());
                if (data.getFireValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//档位模式
                    if (data.getTimerHourValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//有定时
                        //LogUtil.d("samhung---Enter:: updateHobStatusUI");
                        ccvC.setFireGearWithTimer(data.getFireValue(), data.getTimerHourValue(), data.getTimerMinuteValue());
                    } else {//没定时
                        ccvC.setFireGearWithoutTimer(data.getFireValue());
                    }


                } else {//温控模式
                    if (data.getTimerHourValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//有定时
                        ccvC.setTempValueWithTimer(data.getTempValue(), data.getTempIdentifyDrawableResourceID(), data.getTimerHourValue(), data.getTimerMinuteValue());

                    } else {//没定时
                        ccvC.setTempValue(data.getTempValue(), data.getTempIdentifyDrawableResourceID());
                    }
                }
                break;
        }
    }

    @Override
    public void updateHobStatusUI(int id, CookerSettingData data, int hour, int minute) {
        switch (id) {
            case TFTHobConstant.COOKER_TYPE_A_COOKER:
                if (data.getFireValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//档位模式
                    if (data.getTimerHourValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//有定时
                        ccvA.setFireGearWithTimer(data.getFireValue(), hour, minute);
                    } else {//没定时
                        ccvA.setFireGearWithoutTimer(data.getFireValue());
                    }


                } else {//温控模式
                    if (data.getTimerHourValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//有定时
                        ccvA.setTempValueWithTimer(data.getTempValue(), data.getTempIdentifyDrawableResourceID(), hour, minute);

                    } else {//没定时
                        ccvA.setTempValue(data.getTempValue(), data.getTempIdentifyDrawableResourceID());

                    }
                }
                break;
            case TFTHobConstant.COOKER_TYPE_B_COOKER:
                if (data.getFireValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//档位模式
                    if (data.getTimerHourValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//有定时
                        ccvB.setFireGearWithTimer(data.getFireValue(), hour, minute);
                    } else {//没定时
                        ccvB.setFireGearWithoutTimer(data.getFireValue());
                    }


                } else {//温控模式
                    if (data.getTimerHourValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//有定时
                        ccvB.setTempValueWithTimer(data.getTempValue(), data.getTempIdentifyDrawableResourceID(), hour, minute);

                    } else {//没定时
                        ccvB.setTempValue(data.getTempValue(), data.getTempIdentifyDrawableResourceID());
                    }
                }
                break;
            case TFTHobConstant.COOKER_TYPE_C_COOKER:
                //if (data.getFireValue() != -1 || data.getTimerHourValue() != -1) ccvC.setFireGearWithTimer(data.getFireValue(),data.getTimerHourValue(),data.getTimerMinuteValue());
                if (data.getFireValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//档位模式
                    if (data.getTimerHourValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//有定时
                        LogUtil.d("samhung---Enter:: updateHobStatusUI----minute--->" + minute);
                        ccvC.setFireGearWithTimer(data.getFireValue(), hour, minute);
                    } else {//没定时
                        ccvC.setFireGearWithoutTimer(data.getFireValue());
                    }


                } else {//温控模式
                    if (data.getTimerHourValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//有定时
                        ccvC.setTempValueWithTimer(data.getTempValue(), data.getTempIdentifyDrawableResourceID(), hour, minute);

                    } else {//没定时
                        ccvC.setTempValue(data.getTempValue(), data.getTempIdentifyDrawableResourceID());
                    }
                }
                break;
        }
    }

    @Override
    public void updateHobTimerUI(int cookerID, int hour, int minute) {
        //LogUtil.d("Enter:: updateHobTimer----------id->" + cookerID);
        switch (cookerID) {
            case TFTHobConstant.COOKER_TYPE_A_COOKER:
                ccvA.updateTimerUI(cookerID, hour, minute);
                break;
            case TFTHobConstant.COOKER_TYPE_B_COOKER:
                ccvB.updateTimerUI(cookerID, hour, minute);
                break;
            case TFTHobConstant.COOKER_TYPE_C_COOKER:
                LogUtil.d("Enter:: updateHobTimer---->" + minute);
                ccvC.updateTimerUI(cookerID, hour, minute);
                break;

        }
    }

    @Override
    public void updateHobStatusUIForTimeIsUp(int cookerID) {
        switch (cookerID) {
            case TFTHobConstant.COOKER_TYPE_A_COOKER:
                ccvA.updateUIForTimeIsUp();
                break;
            case TFTHobConstant.COOKER_TYPE_B_COOKER:
                ccvB.updateUIForTimeIsUp();
                break;
            case TFTHobConstant.COOKER_TYPE_C_COOKER:
                LogUtil.d("Enter:: updateHobStatusUIForTimeIsUp");
                ccvC.updateUIForTimeIsUp();

                break;

        }
    }

    @Override
    public void updateHobStatusUIForKeepWarm(int cookerID) {
        switch (cookerID) {
            case TFTHobConstant.COOKER_TYPE_A_COOKER:
                ccvA.updateUIForKeepWarm();
                break;
            case TFTHobConstant.COOKER_TYPE_B_COOKER:
                ccvB.updateUIForKeepWarm();
                break;
            case TFTHobConstant.COOKER_TYPE_C_COOKER:
                ccvC.updateUIForKeepWarm();

                break;

        }
    }

    @Override
    public void updateHobStatusUIForUserPrepare(int cookerID, int tempValue, int identifyResourceID) {
        switch (cookerID) {
            case TFTHobConstant.COOKER_TYPE_A_COOKER:
                ccvA.updateUIForUserPrepareToCook(tempValue, identifyResourceID);
                break;
            case TFTHobConstant.COOKER_TYPE_B_COOKER:
                ccvB.updateUIForUserPrepareToCook(tempValue, identifyResourceID);
                break;
            case TFTHobConstant.COOKER_TYPE_C_COOKER:
                ccvC.updateUIForUserPrepareToCook(tempValue, identifyResourceID);

                break;

        }
    }

    @Override
    public void updateHobStatusUIForReadyToCook(int cookerID) {
        switch (cookerID) {
            case TFTHobConstant.COOKER_TYPE_A_COOKER:

                break;
            case TFTHobConstant.COOKER_TYPE_B_COOKER:

                break;
            case TFTHobConstant.COOKER_TYPE_C_COOKER:


                break;

        }
    }

    @Override
    public void updateHobStatusUIForFireBGearTimeIsUp(int cookerID, CookerSettingData data) {
        switch (cookerID) {
            case TFTHobConstant.COOKER_TYPE_A_COOKER:
                if (data.getTimerHourValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//有定时
                    ccvA.setFireGearWithTimer(data.getFireValue(), data.getTimerHourValue(), data.getTimerMinuteValue());
                } else {//没定时
                    ccvA.setFireGearWithoutTimer(data.getFireValue());
                }

                break;
            case TFTHobConstant.COOKER_TYPE_B_COOKER:
                if (data.getTimerHourValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//有定时
                    ccvB.setFireGearWithTimer(data.getFireValue(), data.getTimerHourValue(), data.getTimerMinuteValue());
                } else {//没定时
                    ccvB.setFireGearWithoutTimer(data.getFireValue());
                }

                break;
            case TFTHobConstant.COOKER_TYPE_C_COOKER:
                if (data.getTimerHourValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {//有定时
                    ccvC.setFireGearWithTimer(data.getFireValue(), data.getTimerHourValue(), data.getTimerMinuteValue());
                } else {//没定时
                    ccvC.setFireGearWithoutTimer(data.getFireValue());
                }

                break;

        }
    }

    @Override
    public void updateHobStatusUIForErrorOcurr(int cookerID, String errorMessage) {
        LogUtil.d("Enter:: updateHobStatusUIForErrorOcurr---->" + cookerID + "-----error---->" + errorMessage);
        switch (cookerID) {
            case TFTHobConstant.COOKER_TYPE_A_COOKER:
                ccvA.updateStatusUIForErrorOcurr(errorMessage);
                break;
            case TFTHobConstant.COOKER_TYPE_B_COOKER:
                ccvB.updateStatusUIForErrorOcurr(errorMessage);
                break;
            case TFTHobConstant.COOKER_TYPE_C_COOKER:
                ccvC.updateStatusUIForErrorOcurr(errorMessage);

                break;

        }


    }

    @Override
    public void updateHobStatusUIForErrorDimiss(int cookerID) {
      //  LogUtil.d("Enter:: updateHobStatusUIForErrorDimiss---->" + cookerID);
        switch (cookerID) {
            case TFTHobConstant.COOKER_TYPE_A_COOKER:
                ccvA.updateStatusUIForErrorDimiss();
                break;
            case TFTHobConstant.COOKER_TYPE_B_COOKER:
                ccvB.updateStatusUIForErrorDimiss();
                break;
            case TFTHobConstant.COOKER_TYPE_C_COOKER:
                ccvC.updateStatusUIForErrorDimiss();

                break;

        }

    }

    @Override
    public void updateHobStatusUIForNoPan(int cookerID) {
        switch (cookerID) {
            case TFTHobConstant.COOKER_TYPE_A_COOKER:
                ccvA.updateStatusUIForNoPan();
                break;
            case TFTHobConstant.COOKER_TYPE_B_COOKER:
                ccvB.updateStatusUIForNoPan();
                break;
            case TFTHobConstant.COOKER_TYPE_C_COOKER:
                ccvC.updateStatusUIForNoPan();
                break;

        }
    }

    @Override
    public void updateHobStatusUIForHighTemp(int cookerID) {
        switch (cookerID) {
            case TFTHobConstant.COOKER_TYPE_A_COOKER:
                ccvA.updateStatusUIForHighTemp();
                break;
            case TFTHobConstant.COOKER_TYPE_B_COOKER:
                ccvB.updateStatusUIForHighTemp();
                break;
            case TFTHobConstant.COOKER_TYPE_C_COOKER:
                LogUtil.d("Enter::updateHobStatusUIForHighTemp");
                ccvC.updateStatusUIForHighTemp();
                break;

        }
    }

    @Override
    public void doFinish() {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(HobPanelContract.Presenter presenter) {

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();

    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.stop();
    }

    public void setCookerSettingData(CookerSettingData data) {
        mPresenter.setCookerSettingData(data);

    }

    public void requestCookFromRecipes(int temp,int hour,int minute) {
        LogUtil.d("Enter:: requestCookFromRecipes");
        needSelectCooker = true;
        cookerSettingDataFromTableDetail = new CookerSettingData(-1);
        cookerSettingDataFromTableDetail.setTempValue(temp);
        cookerSettingDataFromTableDetail.setTimerHourValue(hour);
        cookerSettingDataFromTableDetail.setTimerMinuteValue(minute);
        cookerSettingDataFromTableDetail.setTempMode(CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR);
    }

    private void doOnRequestSettingCooker(CookerSettingData data) {
        LogUtil.d("doOnRequestSettingCooker----->" + data.toString());
        ((OnHobPanelFragmentListener) mListener).onRequestSettingCooker(data);
    }


    @OnClick(R.id.ib_hood)
    public void onViewClicked() {

        ((OnHobPanelFragmentListener) mListener).onHobPanelFragmentRequestSwitchToHoodPanel();
    }

    public interface OnHobPanelFragmentListener extends OnFragmentListener {
        void onHobPanelFragmentFinish();

        void onRequestSettingCooker(CookerSettingData data);

        void onHobPanelFragmentRequestSwitchToHoodPanel();

    }



}
