package com.ekek.tfthobmodule.view;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.commonmodule.utils.Logger;
import com.ekek.hardwaremodule.constants.CookerConstant;
import com.ekek.hardwaremodule.entity.CookerData;
import com.ekek.hardwaremodule.power.PowerConstant;
import com.ekek.hardwaremodule.power.PowerLimitManager;
import com.ekek.hardwaremodule.protocol.InductionCookerProtocol;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.database.SettingPreferencesUtil;
import com.ekek.settingmodule.dialog.ToastDialog;
import com.ekek.tfthobmodule.CataTFTHobApplication;
import com.ekek.tfthobmodule.R;
import com.ekek.tfthobmodule.constants.TFTHobConfiguration;
import com.ekek.tfthobmodule.constants.TFTHobConstant;
import com.ekek.tfthobmodule.contract.HobCookerSettingContract;
import com.ekek.tfthobmodule.data.CookerSettingData;
import com.ekek.tfthobmodule.database.SettingDbHelper;
import com.ekek.tfthobmodule.entity.CookerDataTable;
import com.ekek.tfthobmodule.entity.CookerDataTableDao;
import com.ekek.tfthobmodule.event.BleBatteryEvent;
import com.ekek.tfthobmodule.event.NoExternalSensorDetectedEvent;
import com.ekek.tfthobmodule.event.ShowErrorEvent;
import com.ekek.tfthobmodule.event.SoundEvent;
import com.ekek.tfthobmodule.presenter.HobCookerSettingPresenter;
import com.ekek.tfthobmodule.utils.CookerGearUtil;
import com.ekek.tfthobmodule.utils.SoundUtil;
import com.ekek.viewmodule.common.ArcuateTextView;
import com.ekek.viewmodule.common.NumberPickerView;
import com.ekek.viewmodule.common.OvalImageView;
import com.ekek.viewmodule.hob.Hob80SettingIndicatorView;
import com.ekek.viewmodule.hob.Hob90SettingIndicatorView;
import com.ekek.viewmodule.product.ProductManager;
import com.ekek.viewmodule.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HobCookerSettingFragment extends BaseFragment
        implements HobCookerSettingContract.View, NumberPickerView.OnValueChangeListener, NumberPickerView.OnScrollListener {

    private static final int DURATION_SLEEP = 50;
    private static final int DURATION_CHECK = 2 * 1000;
    private static final int DURATION_IDLE = 5 * 1000;

    private static final int MSG_IDLE = 10001;
    private static final int MSG_UPDATE_COOKERS_INDICATOR = 10002;

    public static final int FINISH_TYPE_SAVE = 0;
    public static final int FINISH_TYPE_CANCEL = 1;

    private static final int ALPHA_MAX = 255;
    private static final int ALPHA_MIN = 0;
    private static final String ANIM_ATTRIBUTE = "imageAlpha";
    private static final int ANIM_DURATION = 1000;

    @BindView(R.id.ib_cooker_setting_mode)
    ImageButton ibCookerSettingMode;
    @BindView(R.id.ib_cooker_setting_timer)
    ImageButton ibCookerSettingTimer;
    @BindView(R.id.ib_cooker_setting_confirm)
    ImageButton ibCookerSettingConfirm;
    @BindView(R.id.iv_cooker_setting_operate_area)
    ImageView ivCookerSettingOperateArea;
    @BindView(R.id.npv_fire_gear)
    NumberPickerView npvFireGear;
    @BindView(R.id.npv_fast_temp_gear)
    NumberPickerView npvFastTempGear;
    @BindView(R.id.npv_precise_temp_gear)
    NumberPickerView npvPreciseTempGear;
    @BindView(R.id.npv_timer_hour)
    NumberPickerView npvTimerHour;
    @BindView(R.id.npv_timer_minute)
    NumberPickerView npvTimerMinute;
    @BindView(R.id.ll_timer)
    LinearLayout llTimer;
    @BindView(R.id.iv_timer_display)
    ImageView ivTimerDisplay;
    @BindView(R.id.ib_timer_cancel)
    ImageButton ibTimerCancel;
    @BindView(R.id.ll_temp_identification)
    LinearLayout llTempIdentification;
    @BindView(R.id.iv_temp_identification)
    ImageView ivTempIdentification;
    @BindView(R.id.tv_temp_identification)
    TextView tvTempIdentification;
    @BindView(R.id.iv_boost)
    TextView ivBoost;
    @BindView(R.id.h9siv)
    Hob90SettingIndicatorView h9siv;
    @BindView(R.id.fl_cooker_setting_confirm)
    FrameLayout flCookerSettingConfirm;
    Unbinder unbinder;
    @BindView(R.id.h8siv)
    Hob80SettingIndicatorView h8siv;
    @BindView(R.id.ib_cooker_setting_menu)
    ImageButton ibCookerSettingMenu;
    Unbinder unbinder1;
    @BindView(R.id.tfthobmodule_timer_symbol)
    TextView tfthobmoduleTimerSymbol;
    @BindView(R.id.ivAMask)
    OvalImageView ivAMask;
    @BindView(R.id.ivBMask)
    OvalImageView ivBMask;
    @BindView(R.id.ivCMask)
    OvalImageView ivCMask;
    @BindView(R.id.ivAMask90)
    OvalImageView ivAMask90;
    @BindView(R.id.ivBMask90)
    OvalImageView ivBMask90;
    @BindView(R.id.ivABMask90)
    ImageView ivABMask90;
    @BindView(R.id.ivCMask90)
    OvalImageView ivCMask90;
    @BindView(R.id.ivEMask90)
    OvalImageView ivEMask90;
    @BindView(R.id.ivFMask90)
    OvalImageView ivFMask90;
    @BindView(R.id.ivEFMask90)
    ImageView ivEFMask90;
    @BindView(R.id.ivAMask80)
    OvalImageView ivAMask80;
    @BindView(R.id.ivBMask80)
    OvalImageView ivBMask80;
    @BindView(R.id.ivABMask80)
    ImageView ivABMask80;
    @BindView(R.id.ivCMask80)
    OvalImageView ivCMask80;
    @BindView(R.id.ivDMask80)
    OvalImageView ivDMask80;
    @BindView(R.id.iv_info)
    ImageView ivInfo;
    @BindView(R.id.tv_precise)
    TextView tvPrecise;
    @BindView(R.id.tv_fast)
    TextView tvFast;
    @BindView(R.id.tv_fast_or_precise)
    ArcuateTextView tvFastOrPrecise;

    private int settingMode = CookerSettingData.SETTING_MODE_FIRE_GEAR;
    private int lastSettingMode = settingMode;
    private int tempSettingMode = CookerSettingData.SETTING_MODE_TEMP_TYPE_CHOOSEN;

    private String[] hours = new String[0];
    private String[] minutes = new String[0];
    private String[] preciseTemps = new String[TFTHobConstant.COOKER_PRECISE_TEMP_LEN];

    private HobCookerSettingContract.Presenter mPresenter;

    private CookerSettingData mCookerSettingData;

    private boolean canUseTempSensor = true;

    private long timestampIdleStart;
    private CheckingIdleThread checkingIdleThread;
    private MessageHandler handler;

    private ValueAnimator alphaAnim;
    private ArgbEvaluator evaluator = new ArgbEvaluator();

    private PowerLimitManager mPowerLimitManager;

    private boolean idleCheckingPaused = false;

    private boolean isCheckingPower = false; //用于控制功率限制检测，防止用户操作太快全部B档退出

    private boolean scrollingGear;

    private Map<Integer, Bitmap> bitmapMap = new ConcurrentHashMap<>();

    public HobCookerSettingFragment() {
        mPresenter = new HobCookerSettingPresenter(this);
    }

    private int mMaxTime=0;
    private int bleBatteryMaxUsableMinutes;
    private boolean bleBatteryCharging;

    @SuppressLint("ValidFragment")
    public HobCookerSettingFragment(CookerSettingData data) {
        String powerLimitSwitchStatus = SettingPreferencesUtil.getPowerLimitSwitchStatus(getActivity());
        int totalPower = PowerConstant.POWER_LIMIT_TOTAL_LIMIT_DEFAULT;
        if (powerLimitSwitchStatus.equals(CataSettingConstant.POWER_LIMIT_SWITCH_STATUS_OPEN)) {
            totalPower = SettingPreferencesUtil.getPowerLimitLevel(getActivity());
        }

        mPowerLimitManager = PowerLimitManager.getIstance(CookerConstant.COOKER_TYPE_CURRENT_TYPE,totalPower);
        setSettingInitData(data);
        mPresenter = new HobCookerSettingPresenter(this);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initFont();
        tvPrecise.setText(R.string.tfthobmodule_title_precise);
        tvFast.setText(R.string.tfthobmodule_title_fast);
    }

    public void setSettingInitData(CookerSettingData data) {
       // LogUtil.d("cookID----->" + data.getCookerID());
        //LogUtil.d("Enter:: setSettingInitData----->" + data.getFireValue());
        LogUtil.d("Enter:: setSettingInitData----->" + data.getTimerHourValue());
        LogUtil.d("Enter:: setSettingInitData----->" + data.getTimerRemainHourValue());
        initcanUseTempSensor(data.getCookerID());
        mCookerSettingData = data;

        settingMode = mCookerSettingData.getCookerSettingMode();
        if (settingMode != CookerSettingData.SETTING_MODE_TIMER && settingMode != CookerSettingData.SETTING_MODE_FIRE_GEAR) {
            /*if (canUseTempSensor) tempSettingMode = settingMode;
            else tempSettingMode = CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR_WITHOUT_SENSOR;*/
            tempSettingMode = settingMode;
        }
        if (settingMode == CookerSettingData.SETTING_MODE_TIMER) {
            if (mCookerSettingData.getTempMode() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {
                lastSettingMode = tempSettingMode;
            } else {
                lastSettingMode = CookerSettingData.SETTING_MODE_FIRE_GEAR;
            }
        }
        if (settingMode == CookerSettingData.SETTING_MODE_FIRE_GEAR) {
            /*if (canUseTempSensor) tempSettingMode = CookerSettingData.SETTING_MODE_TEMP_TYPE_CHOOSEN;
            else tempSettingMode = CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR_WITHOUT_SENSOR;*/
            tempSettingMode = CookerSettingData.SETTING_MODE_TEMP_TYPE_CHOOSEN;
        }
        //LogUtil.d("lastsettingmode-------->" + lastSettingMode);
        LogUtil.d("Enter:: setSettingInitData------->" + settingMode);

    }

    private void setAnim(ImageView view) {
        if (alphaAnim != null) {
            cancelAnim();
        }

        showMasks(view, true);

        alphaAnim = ObjectAnimator.ofInt(
                view,
                ANIM_ATTRIBUTE,
                ALPHA_MAX,
                ALPHA_MIN);
        alphaAnim.setDuration(ANIM_DURATION);
        alphaAnim.setEvaluator(evaluator);
        alphaAnim.setRepeatCount(ValueAnimator.INFINITE);
        alphaAnim.setRepeatMode(ValueAnimator.REVERSE);

        alphaAnim.start();
    }

    private void cancelAnim() {
        showMasks(null, false);

        if (alphaAnim != null) {
            alphaAnim.cancel();
            alphaAnim = null;
        }
    }

    private void showMasks(ImageView view, boolean visible) {
        int visibility = visible ? View.VISIBLE : View.INVISIBLE;
        if (ivAMask != null) {
            ivAMask.setVisibility(View.INVISIBLE);
        }
        if (ivBMask != null) {
            ivBMask.setVisibility(View.INVISIBLE);
        }
        if (ivCMask != null) {
            ivCMask.setVisibility(View.INVISIBLE);
        }
        if (ivAMask90 != null) {
            ivAMask90.setVisibility(View.INVISIBLE);
        }
        if (ivBMask90 != null) {
            ivBMask90.setVisibility(View.INVISIBLE);
        }
        if (ivABMask90 != null) {
            ivABMask90.setVisibility(View.INVISIBLE);
        }
        if (ivCMask90 != null) {
            ivCMask90.setVisibility(View.INVISIBLE);
        }
        if (ivEMask90 != null) {
            ivEMask90.setVisibility(View.INVISIBLE);
        }
        if (ivFMask90 != null) {
            ivFMask90.setVisibility(View.INVISIBLE);
        }
        if (ivEFMask90 != null) {
            ivEFMask90.setVisibility(View.INVISIBLE);
        }
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    private int getBackgroundResource(
            boolean a, boolean a0,
            boolean b, boolean b0,
            boolean c, boolean c0) {
        if (a && b && c) {
            return R.mipmap.cooker_setting_confirm_all;
        } else if (a && b) {
            return c0? R.mipmap.cooker_setting_confirm_small_both_c_red: R.mipmap.cooker_setting_confirm_small_both;
        } else if (b && c) {
            return a0 ? R.mipmap.cooker_setting_confirm_small_up_big_a_red : R.mipmap.cooker_setting_confirm_small_up_big;
        } else if (a && c) {
            return b0 ? R.mipmap.cooker_setting_confirm_small_down_big_b_red : R.mipmap.cooker_setting_confirm_small_down_big;
        } else if (a) {
            if (b0 && c0) {
                return R.mipmap.cooker_setting_confirm_small_down_bc_red;
            } else if (b0) {
                return R.mipmap.cooker_setting_confirm_small_down_b_red;
            } else if (c0) {
                return R.mipmap.cooker_setting_confirm_small_down_c_red;
            } else {
                return R.mipmap.cooker_setting_confirm_small_down;
            }
        } else if (b) {
            if (a0 && c0) {
                return R.mipmap.cooker_setting_confirm_small_up_ac_red;
            } else if (a0) {
                return R.mipmap.cooker_setting_confirm_small_up_a_red;
            } else if (c0) {
                return R.mipmap.cooker_setting_confirm_small_up_c_red;
            } else {
                return R.mipmap.cooker_setting_confirm_small_up;
            }
        } else if (c) {
            if (a0 && b0) {
                return R.mipmap.cooker_setting_confirm_big_ab_red;
            } else if (a0) {
                return R.mipmap.cooker_setting_confirm_big_a_red;
            } else if (b0) {
                return R.mipmap.cooker_setting_confirm_big_b_red;
            } else {
                return R.mipmap.cooker_setting_confirm_big;
            }
        } else {
            // 这个分支一定不会执行，随便返回一个即可
            return R.mipmap.cooker_setting_confirm_all;
        }
    }

    private void updateCookerIndicatorUI(int cookerID) {

        CookerDataTableDao dao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
        CookerDataTable cookerDataA = dao.queryBuilder().where(
                CookerDataTableDao.Properties.CookerID.eq(
                        TFTHobConstant.COOKER_TYPE_A_COOKER)).build().unique();
        CookerDataTable cookerDataB = dao.queryBuilder().where(
                CookerDataTableDao.Properties.CookerID.eq(
                        TFTHobConstant.COOKER_TYPE_B_COOKER)).build().unique();
        CookerDataTable cookerDataC = dao.queryBuilder().where(
                CookerDataTableDao.Properties.CookerID.eq(
                        TFTHobConstant.COOKER_TYPE_C_COOKER)).build().unique();
        boolean poweredOnA = false;
        boolean highTempA = false;
        boolean poweredOnB = false;
        boolean highTempB = false;
        boolean poweredOnC = false;
        boolean highTempC = false;
        if (cookerDataA != null) {
            poweredOnA = cookerDataA.getPowerOnFlag();
            highTempA = cookerDataA.getHighTempFlag();
        }
        if (cookerDataB != null) {
            poweredOnB = cookerDataB.getPowerOnFlag();
            highTempB = cookerDataB.getHighTempFlag();
        }
        if (cookerDataC != null) {
            poweredOnC = cookerDataC.getPowerOnFlag();
            highTempC = cookerDataC.getHighTempFlag();
        }

        switch (cookerID) {
            case TFTHobConstant.COOKER_TYPE_C_COOKER:
                //ibCookerSettingConfirm.setImageDrawable(getResources().getDrawable(R.mipmap.ic_big_cooker_setting_confirm));
                if (ProductManager.PRODUCT_TYPE == ProductManager.Haier) {
                    ibCookerSettingConfirm.setImageBitmap(getBitmap(R.mipmap.ic_big_cooker_setting_confirm_haier));
                } else {
                    ibCookerSettingConfirm.setImageBitmap(getBitmap(getBackgroundResource(
                            poweredOnA,
                            highTempA,
                            poweredOnB,
                            highTempB,
                            true,
                            false)));
                    setAnim(ivCMask);
                }

                break;
            case TFTHobConstant.COOKER_TYPE_B_COOKER:
                //ibCookerSettingConfirm.setImageDrawable(getResources().getDrawable(R.drawable.btn_small_up_cooker_setting_confirm));
                if (ProductManager.PRODUCT_TYPE == ProductManager.Haier) {
                    ibCookerSettingConfirm.setImageBitmap(getBitmap(R.mipmap.ic_small_up_cooker_confirm_haier));
                } else {
                    ibCookerSettingConfirm.setImageBitmap(getBitmap(getBackgroundResource(
                            poweredOnA,
                            highTempA,
                            true,
                            false,
                            poweredOnC,
                            highTempC)));
                    setAnim(ivBMask);
                }

                break;
            case TFTHobConstant.COOKER_TYPE_A_COOKER:
                if (ProductManager.PRODUCT_TYPE == ProductManager.Haier) {
                    ibCookerSettingConfirm.setImageBitmap(getBitmap(R.mipmap.ic_small_down_cooker_confirm_haier));
                } else {
                    ibCookerSettingConfirm.setImageBitmap(getBitmap(getBackgroundResource(
                            true,
                            false,
                            poweredOnB,
                            highTempB,
                            poweredOnC,
                            highTempC)));
                    setAnim(ivAMask);
                }
                //ibCookerSettingConfirm.setImageDrawable(getResources().getDrawable(R.drawable.btn_small_down_cooker_setting_confirm));

                break;
            default:
                if (ProductManager.PRODUCT_TYPE == ProductManager.Haier) {
                    ibCookerSettingConfirm.setImageBitmap(getBitmap(R.mipmap.ic_small_down_cooker_confirm_haier));
                } else {
                    ibCookerSettingConfirm.setImageBitmap(getBitmap(R.mipmap.cooker_setting_confirm_all));
                }
                break;
        }
    }

    private void updateCookerIndicatorUI80(int cookerID) {
        CookerDataTableDao dao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
        CookerDataTable cookerDataA = dao.queryBuilder().where(
                CookerDataTableDao.Properties.CookerID.eq(
                        TFTHobConstant.COOKER_TYPE_A_COOKER)).build().unique();
        CookerDataTable cookerDataB = dao.queryBuilder().where(
                CookerDataTableDao.Properties.CookerID.eq(
                        TFTHobConstant.COOKER_TYPE_B_COOKER)).build().unique();
        CookerDataTable cookerDataAB = dao.queryBuilder().where(
                CookerDataTableDao.Properties.CookerID.eq(
                        TFTHobConstant.COOKER_TYPE_AB_COOKER)).build().unique();
        CookerDataTable cookerDataC = dao.queryBuilder().where(
                CookerDataTableDao.Properties.CookerID.eq(
                        TFTHobConstant.COOKER_TYPE_C_COOKER)).build().unique();
        CookerDataTable cookerDataD = dao.queryBuilder().where(
                CookerDataTableDao.Properties.CookerID.eq(
                        TFTHobConstant.COOKER_TYPE_D_COOKER)).build().unique();

        boolean poweredOnA = false;
        boolean highTempA = false;
        boolean poweredOnB = false;
        boolean highTempB = false;
        boolean poweredOnAB = false;
        boolean highTempAB = false;
        boolean poweredOnC = false;
        boolean highTempC = false;
        boolean poweredOnD = false;
        boolean highTempD = false;
        if (cookerDataA != null) {
            poweredOnA = cookerDataA.getPowerOnFlag();
            highTempA = cookerDataA.getHighTempFlag();
        }
        if (cookerDataB != null) {
            poweredOnB = cookerDataB.getPowerOnFlag();
            highTempB = cookerDataB.getHighTempFlag();
        }
        if (cookerDataAB != null) {
            poweredOnAB = cookerDataAB.getPowerOnFlag();
            highTempAB = cookerDataAB.getHighTempFlag();
        }
        if (cookerDataC != null) {
            poweredOnC = cookerDataC.getPowerOnFlag();
            highTempC = cookerDataC.getHighTempFlag();
        }
        if (cookerDataD != null) {
            poweredOnD = cookerDataD.getPowerOnFlag();
            highTempD = cookerDataD.getHighTempFlag();
        }

        h8siv.updateUI(cookerID,
                poweredOnA, highTempA,
                poweredOnB, highTempB,
                poweredOnAB, highTempAB,
                poweredOnC, highTempC,
                poweredOnD, highTempD);

        switch (cookerID) {
            case TFTHobConstant.COOKER_TYPE_A_COOKER:
                setAnim(ivAMask80);
                break;
            case TFTHobConstant.COOKER_TYPE_B_COOKER:
                setAnim(ivBMask80);
                break;
            case TFTHobConstant.COOKER_TYPE_AB_COOKER:
                setAnim(ivABMask80);
                break;
            case TFTHobConstant.COOKER_TYPE_C_COOKER:
                setAnim(ivCMask80);
                break;
            case TFTHobConstant.COOKER_TYPE_D_COOKER:
                setAnim(ivDMask80);
                break;
        }
    }

    private void updateCookerIndicatorUI90(int cookerID) {

        CookerDataTableDao dao = CataTFTHobApplication.getDaoSession().getCookerDataTableDao();
        CookerDataTable cookerDataA = dao.queryBuilder().where(
                CookerDataTableDao.Properties.CookerID.eq(
                        TFTHobConstant.COOKER_TYPE_A_COOKER)).build().unique();
        CookerDataTable cookerDataB = dao.queryBuilder().where(
                CookerDataTableDao.Properties.CookerID.eq(
                        TFTHobConstant.COOKER_TYPE_B_COOKER)).build().unique();
        CookerDataTable cookerDataAB = dao.queryBuilder().where(
                CookerDataTableDao.Properties.CookerID.eq(
                        TFTHobConstant.COOKER_TYPE_AB_COOKER)).build().unique();
        CookerDataTable cookerDataC = dao.queryBuilder().where(
                CookerDataTableDao.Properties.CookerID.eq(
                        TFTHobConstant.COOKER_TYPE_C_COOKER)).build().unique();
        CookerDataTable cookerDataE = dao.queryBuilder().where(
                CookerDataTableDao.Properties.CookerID.eq(
                        TFTHobConstant.COOKER_TYPE_E_COOKER)).build().unique();
        CookerDataTable cookerDataF = dao.queryBuilder().where(
                CookerDataTableDao.Properties.CookerID.eq(
                        TFTHobConstant.COOKER_TYPE_F_COOKER)).build().unique();
        CookerDataTable cookerDataEF = dao.queryBuilder().where(
                CookerDataTableDao.Properties.CookerID.eq(
                        TFTHobConstant.COOKER_TYPE_EF_COOKER)).build().unique();

        boolean poweredOnA = false;
        boolean highTempA = false;
        boolean poweredOnB = false;
        boolean highTempB = false;
        boolean poweredOnAB = false;
        boolean highTempAB = false;
        boolean poweredOnC = false;
        boolean highTempC = false;
        boolean poweredOnE = false;
        boolean highTempE = false;
        boolean poweredOnF = false;
        boolean highTempF = false;
        boolean poweredOnEF = false;
        boolean highTempEF = false;
        if (cookerDataA != null) {
            poweredOnA = cookerDataA.getPowerOnFlag();
            highTempA = cookerDataA.getHighTempFlag();
        }
        if (cookerDataB != null) {
            poweredOnB = cookerDataB.getPowerOnFlag();
            highTempB = cookerDataB.getHighTempFlag();
        }
        if (cookerDataAB != null) {
            poweredOnAB = cookerDataAB.getPowerOnFlag();
            highTempAB = cookerDataAB.getHighTempFlag();
        }
        if (cookerDataC != null) {
            poweredOnC = cookerDataC.getPowerOnFlag();
            highTempC = cookerDataC.getHighTempFlag();
        }
        if (cookerDataE != null) {
            poweredOnE = cookerDataE.getPowerOnFlag();
            highTempE = cookerDataE.getHighTempFlag();
        }
        if (cookerDataF != null) {
            poweredOnF = cookerDataF.getPowerOnFlag();
            highTempF = cookerDataF.getHighTempFlag();
        }
        if (cookerDataEF != null) {
            poweredOnEF = cookerDataEF.getPowerOnFlag();
            highTempEF = cookerDataEF.getHighTempFlag();
        }

        h9siv.updateUI(
                cookerID,
                poweredOnA, highTempA,
                poweredOnB, highTempB,
                poweredOnAB, highTempAB,
                poweredOnC, highTempC,
                poweredOnE, highTempE,
                poweredOnF, highTempF,
                poweredOnEF, highTempEF);
        switch (cookerID) {
            case TFTHobConstant.COOKER_TYPE_A_COOKER:
                setAnim(ivAMask90);
                break;
            case TFTHobConstant.COOKER_TYPE_B_COOKER:
                setAnim(ivBMask90);
                break;
            case TFTHobConstant.COOKER_TYPE_AB_COOKER:
                setAnim(ivABMask90);
                break;
            case TFTHobConstant.COOKER_TYPE_C_COOKER:
                setAnim(ivCMask90);
                break;
            case TFTHobConstant.COOKER_TYPE_E_COOKER:
                setAnim(ivEMask90);
                break;
            case TFTHobConstant.COOKER_TYPE_F_COOKER:
                setAnim(ivFMask90);
                break;
            case TFTHobConstant.COOKER_TYPE_EF_COOKER:
                setAnim(ivEFMask90);
                break;
        }
    }

    private void initcanUseTempSensor(int cookerID) {
        int status = SettingDbHelper.getTemperatureSensorStatus();
        if (status == -1) canUseTempSensor = true;
        else if (status == cookerID) canUseTempSensor = true;
        else if (status != cookerID) canUseTempSensor = false;
        else canUseTempSensor = false;
    }

    @Override
    public int initLyaout() {
        return R.layout.tfthobmodule_fragment_hob_cooker_setting;
    }

    @Override
    public void initListener() {

    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        if (TFTHobConfiguration.TFT_HOB_TYPE == TFTHobConfiguration.TFT_HOB_TYPE_60) {
            ibCookerSettingConfirm.setVisibility(View.VISIBLE);
            h9siv.setVisibility(View.INVISIBLE);
            h8siv.setVisibility(View.INVISIBLE);
        } else if (TFTHobConfiguration.TFT_HOB_TYPE == TFTHobConfiguration.TFT_HOB_TYPE_80) {
            ibCookerSettingConfirm.setVisibility(View.INVISIBLE);
            h9siv.setVisibility(View.INVISIBLE);
            h8siv.setVisibility(View.VISIBLE);
        } else if (TFTHobConfiguration.TFT_HOB_TYPE == TFTHobConfiguration.TFT_HOB_TYPE_90) {
            ibCookerSettingConfirm.setVisibility(View.INVISIBLE);
            h9siv.setVisibility(View.VISIBLE);
            h8siv.setVisibility(View.INVISIBLE);
        }

        ivCookerSettingOperateArea.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return processOperateAreaTouchEvent(view, motionEvent);
            }
        });
        //init();
        initFont();
        handler = new MessageHandler(this);
    }

    private boolean processOperateAreaTouchEvent(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            resetIdleStart();
            if (ViewUtils.isShade(view)) {
                return false;
            }
            float touchX = event.getX();
            float touchY = event.getY();
            float centerX = view.getWidth() / 2;
            float centerY = view.getHeight() / 2;
            // LogUtil.d(touchX + "------" + touchY + "---------" + centerX + "----------" + centerY);
            if (settingMode == CookerSettingData.SETTING_MODE_TEMP_TYPE_CHOOSEN) {//温控模式选择：普通，精准控温
                if (touchY > centerY) {
                    view.playSoundEffect(SoundEffectConstants.CLICK);
                    tempSettingMode = CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR;
                    setCookerSettingMode(tempSettingMode);
                    LogUtil.d("--------down--------------");
                } else if (touchY < centerY) {
                    if (isTempSensorEnable()) {
                        view.playSoundEffect(SoundEffectConstants.CLICK);
                        tempSettingMode = CookerSettingData.SETTING_MODE_PRECISE_TEMP_GEAR;
                        setCookerSettingMode(tempSettingMode);
                    }
                }
            } else if (settingMode == CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR) {
                if (isOutOfCircle(centerX, centerY, touchX, touchY)) {
                    if (isTempSensorEnable()) {
                        if (touchX < centerX && touchY > centerY) {

                            view.playSoundEffect(SoundEffectConstants.CLICK);
                            String tempGear = preciseTemps[npvFastTempGear.getValue()];
                            tempGear = tempGear.substring(0, tempGear.length() - 1);
                            mCookerSettingData.setTempValue(Integer.valueOf(tempGear));
                            int tempIdentifyResourceID = CookerGearUtil.getTempIdentifyResourceID(Integer.valueOf(tempGear));
                            mCookerSettingData.setTempIdentifyDrawableResourceID(tempIdentifyResourceID);

                            tempSettingMode = CookerSettingData.SETTING_MODE_PRECISE_TEMP_GEAR;
                            setCookerSettingMode(tempSettingMode);
                            LogUtil.d("------------fast------------");
                        }
                    }
                } else {
                    view.playSoundEffect(SoundEffectConstants.CLICK);
                    doConfirm(true);
                }
            } else if (settingMode == CookerSettingData.SETTING_MODE_PRECISE_TEMP_GEAR) {
                if (isOutOfCircle(centerX, centerY, touchX, touchY)) {
                    if (touchX < centerX && touchY > centerY) {

                        view.playSoundEffect(SoundEffectConstants.CLICK);
                        String tempGear = TFTHobConstant.COOKER_FAST_TEMP_LIST[npvFastTempGear.getValue()];
                        tempGear = tempGear.substring(0, tempGear.length() - 1);
                        mCookerSettingData.setTempValue(Integer.valueOf(tempGear));
                        //int tempIdentifyResourceID = CookerGearUtil.getTempIdentifyResourceID(Integer.valueOf(tempGear));
                        //mCookerSettingData.setTempIdentifyDrawableResourceID(tempIdentifyResourceID);
                        //精确控温，工作时不需要温度值和温度标识切换显示，只需显示温度值
                        mCookerSettingData.setTempIdentifyDrawableResourceID(-1);

                        tempSettingMode = CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR;
                        setCookerSettingMode(tempSettingMode);
                        LogUtil.d("------------precise------------");
                    }
                } else {
                    view.playSoundEffect(SoundEffectConstants.CLICK);
                    doConfirm(true);
                }
            } else {
                view.playSoundEffect(SoundEffectConstants.CLICK);
                doConfirm(true);
            }
        }

        return true;
    }

    private double calDistance(float x1, float y1, float x2, float y2) {
        double x = Math.abs(x1 - x2);
        double y = Math.abs(y1 - y2);
        return Math.sqrt(x * x + y * y);
    }

    private boolean isOutOfCircle(float x1, float y1, float x2, float y2) {
        Drawable drawable = ivCookerSettingOperateArea.getDrawable();
        if (drawable == null) {
            return false;
        }

        Rect drawableBounds = drawable.getBounds();
        int radius = drawableBounds.width() / 2;
        return calDistance(x1, y1, x2, y2) > radius;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI();

        //initWheel();
    }

    private void init() {
        // 获取温度值
        int tempValue = mCookerSettingData.getTempValue();
        int tempMode = mCookerSettingData.getTempMode();

        initFireGearView();
        initFastTempGearView();
        initPreciseTempGearView();
        initTimerView();
        setCookerSettingMode(settingMode);

        // 根据温度值显示合适的温度示意图片
        if (tempMode == CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR) {
            mCookerSettingData.setTempIdentifyDrawableResourceID(
                    CookerGearUtil.getTempIdentifyResourceID(tempValue));
        }
        updateTempIndentifyUI();
    }

    private void initFont() {
        ivBoost.setTypeface(GlobalVars.getInstance().getDefaultFontBold());
        tvTempIdentification.setTypeface(GlobalVars.getInstance().getDefaultFontRegular());
        tvPrecise.setTypeface(GlobalVars.getInstance().getDefaultFontRegular());
        tvFast.setTypeface(GlobalVars.getInstance().getDefaultFontRegular());
        tvFastOrPrecise.setTypeface(GlobalVars.getInstance().getDefaultFontRegular());

        if (GlobalVars.getInstance().getCurrentLocale().toString().toLowerCase().equals("ru")) {
            tvPrecise.setTextSize(52.0f);
        } else {
            tvPrecise.setTextSize(60.0f);
        }
    }

    private void updateUI() {
        switch (settingMode) {
            case CookerSettingData.SETTING_MODE_FIRE_GEAR:
                if (ProductManager.PRODUCT_TYPE == ProductManager.Haier) {
                    ivCookerSettingOperateArea.setImageBitmap(getBitmap(R.mipmap.bg_cooker_setting_fire_gear_mode_haier));
                } else {
                    ivCookerSettingOperateArea.setImageBitmap(getBitmap(R.mipmap.bg_cooker_setting_fire_gear_mode));
                }

                break;
        }
    }

    private void initWheel() {
        //mainWheelLeft.setData(Arrays.asList(TFTHobConstant.COOKER_FIRE_GEAR_LIST));
     /*   mainWheelLeft.setCurved(true);
        mainWheelLeft.setItemTextColor(Color.GRAY);
        mainWheelLeft.setSelectedItemTextColor(Color.WHITE);
        mainWheelLeft.setIndicator(false);
        mainWheelLeft.setIndicatorSize(150);
        mainWheelLeft.setItemSpace(200);*/

    }

    private void initFireGearView() {
        npvFireGear.setDisplayedValues(TFTHobConstant.COOKER_FIRE_GEAR_LIST);
        npvFireGear.setOnValueChangedListener(this);
        npvFireGear.setOnScrollListener(this);
        npvFireGear.setMaxValue(TFTHobConstant.COOKER_FIRE_GEAR_LIST.length - 1);
        npvFireGear.setMinValue(0);
        if (mCookerSettingData.getFireValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {
            int realValue = checkPowerLimit(mCookerSettingData.getFireValue());
            npvFireGear.setValue(CookerGearUtil.getFireGearIndex(String.valueOf(realValue)));

        } else {
            int realValue = checkPowerLimit(TFTHobConstant.COOKER_DEFAULT_FIRE_GEAR);
            npvFireGear.setValue(CookerGearUtil.getFireGearIndex(String.valueOf(realValue)));
        }

        npvFireGear.setFriction(10 * ViewConfiguration.getScrollFriction());
        npvFireGear.setContentTextTypeface(GlobalVars.getInstance().getDefaultFontBold());
        npvFireGear.setHintTextTypeface(GlobalVars.getInstance().getDefaultFontBold());
        npvFireGear.setItemScrollListener(new NumberPickerView.ItemScrollListener() {
            @Override
            public void OnItemScroll(float itemHeight, float totalScrollY) {
                EventBus.getDefault().post(new SoundEvent(
                        SoundEvent.SOUND_ACTION_PLAY,
                        SoundUtil.SOUND_ID_SCROLL,
                        SoundEvent.SOUND_TYPE_SCROLL));
            }
        });
    }

    private void initFastTempGearView() {
        npvFastTempGear.setDisplayedValues(TFTHobConstant.COOKER_FAST_TEMP_LIST);
        npvFastTempGear.setOnValueChangedListener(this);
        npvFastTempGear.setOnScrollListener(this);
        npvFastTempGear.setMaxValue(TFTHobConstant.COOKER_FAST_TEMP_LIST.length - 1);
        npvFastTempGear.setMinValue(0);

        int tempValue = mCookerSettingData.getTempValue();
        int tempMode = mCookerSettingData.getTempMode();
        if (tempValue != CookerSettingData.COOKER_SETTING_INVALID_VALUE
                && tempMode == CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR) {
            int tempIndex = CookerGearUtil.getFastTempGearIndex(
                    String.valueOf(tempValue));
            npvFastTempGear.setValue(tempIndex);
        } else {
            npvFastTempGear.setValue(TFTHobConstant.COOKER_DEFAULT_FAST_TEMP_GEAR);
        }


        npvFastTempGear.setFriction(10 * ViewConfiguration.getScrollFriction());
        npvFastTempGear.setContentTextTypeface(GlobalVars.getInstance().getDefaultFontBold());
        npvFastTempGear.setHintTextTypeface(GlobalVars.getInstance().getDefaultFontBold());
        npvFastTempGear.setItemScrollListener(new NumberPickerView.ItemScrollListener() {
            @Override
            public void OnItemScroll(float itemHeight, float totalScrollY) {
                EventBus.getDefault().post(new SoundEvent(
                        SoundEvent.SOUND_ACTION_PLAY,
                        SoundUtil.SOUND_ID_SCROLL,
                        SoundEvent.SOUND_TYPE_SCROLL));
            }
        });
    }

    private void initPreciseTempGearView() {
        for (int i = TFTHobConstant.COOKER_PRECISE_TEMP_MAX; i >= TFTHobConstant.COOKER_PRECISE_TEMP_MIN; i--) {  // 原来是220 ，修改为180 ，更新 2018年9月24日17:21:33
            preciseTemps[TFTHobConstant.COOKER_PRECISE_TEMP_MAX - i] = String.valueOf(i) + "°";
        }
        npvPreciseTempGear.setDisplayedValues(preciseTemps);
        npvPreciseTempGear.setOnValueChangedListener(this);
        npvPreciseTempGear.setOnScrollListener(this);
        npvPreciseTempGear.setMaxValue(preciseTemps.length - 1);
        npvPreciseTempGear.setMinValue(0);

        int tempValue = mCookerSettingData.getTempValue();
        int tempMode = mCookerSettingData.getTempMode();
        if (tempValue != CookerSettingData.COOKER_SETTING_INVALID_VALUE
                && tempMode == CookerSettingData.SETTING_MODE_PRECISE_TEMP_GEAR) {
            int tempIndex = CookerGearUtil.getPreciseTempGearIndex(
                    String.valueOf(mCookerSettingData.getTempValue()));
            npvPreciseTempGear.setValue(tempIndex);
        } else {
            npvPreciseTempGear.setValue(TFTHobConstant.COOKER_DEFAULT_PRECISE_TEMP_GEAR);
        }

        npvPreciseTempGear.setFriction(3 * ViewConfiguration.getScrollFriction());
        npvPreciseTempGear.setContentTextTypeface(GlobalVars.getInstance().getDefaultFontBold());
        npvPreciseTempGear.setHintTextTypeface(GlobalVars.getInstance().getDefaultFontBold());
        npvPreciseTempGear.setItemScrollListener(new NumberPickerView.ItemScrollListener() {
            @Override
            public void OnItemScroll(float itemHeight, float totalScrollY) {
                EventBus.getDefault().post(new SoundEvent(
                        SoundEvent.SOUND_ACTION_PLAY,
                        SoundUtil.SOUND_ID_SCROLL,
                        SoundEvent.SOUND_TYPE_SCROLL));
            }
        });
    }

    private void initTimerView() {
        switchMinutesRange(59);
        setInitMinuteValue();

        switchHoursRange(CookerSettingData.getMaxTimerHours(
                mCookerSettingData.getTempMode(),
                mCookerSettingData.getFireValue(),
                mCookerSettingData.getTempValue()));
        setInitHourValue();

        Typeface typeface = GlobalVars.getInstance().getDefaultFontBold();
        npvTimerMinute.setContentTextTypeface(typeface);
        npvTimerMinute.setHintTextTypeface(typeface);
        npvTimerHour.setContentTextTypeface(typeface);
        npvTimerHour.setHintTextTypeface(typeface);
        tfthobmoduleTimerSymbol.setTypeface(typeface);
    }

    @Override
    public void doFinish() {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setPresenter(HobCookerSettingContract.Presenter presenter) {

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
        registerEventBus();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (mRootView == null) {
            return;
        }
        isCheckingPower = false;
        scrollingGear = false;
        Logger.getInstance().i("onHiddenChanged(" + hidden + "): CookerID=" + mCookerSettingData.getCookerID());
        if (!hidden) {
            GlobalVars.getInstance().setConfiguringCooker(true);
            init();
            updateCookerConfirmUI();
            resetIdleStart();
            checkingIdleThread = new CheckingIdleThread();
            checkingIdleThread.start();
        } else {
            ToastDialog.dimissDialog();
            GlobalVars.getInstance().setConfiguringCooker(false);
            cancelAnim();
            if (handler.hasMessages(MSG_IDLE)) {
                handler.removeMessages(MSG_IDLE);
            }
            if (handler.hasMessages(MSG_UPDATE_COOKERS_INDICATOR)) {
                handler.removeMessages(MSG_UPDATE_COOKERS_INDICATOR);
            }
            try {
                if (checkingIdleThread != null && checkingIdleThread.isAlive()) {
                    checkingIdleThread.setForceToStop(true);
                    checkingIdleThread.join(2000);
                    checkingIdleThread = null;
                }
            } catch (InterruptedException e) {
                LogUtil.e(e.getMessage());
            }

        }
    }

    private synchronized void resetIdleStart() {
        timestampIdleStart = SystemClock.elapsedRealtime();
    }

    private void updateCookerConfirmUI() {
        if (TFTHobConfiguration.TFT_HOB_TYPE == TFTHobConfiguration.TFT_HOB_TYPE_60) {
            updateCookerIndicatorUI(mCookerSettingData.getCookerID());

        } else if (TFTHobConfiguration.TFT_HOB_TYPE == TFTHobConfiguration.TFT_HOB_TYPE_80) {
            updateCookerIndicatorUI80(mCookerSettingData.getCookerID());

        } else if (TFTHobConfiguration.TFT_HOB_TYPE == TFTHobConfiguration.TFT_HOB_TYPE_90) {
            updateCookerIndicatorUI90(mCookerSettingData.getCookerID());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.stop();
        unregisterEventBus();
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BleBatteryEvent event) {


        if (event.getBatteryViewStatus() == BleBatteryEvent.BATTERY_VIEW_STATUS_SHOW) {//bt connect
            if (settingMode == CookerSettingData.SETTING_MODE_TEMP_TYPE_CHOOSEN) {
                setCookerSettingMode(CookerSettingData.SETTING_MODE_TEMP_TYPE_CHOOSEN);
            }else if (settingMode == CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR) {
                setCookerSettingMode(CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR);
            }else if (settingMode == CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR_WITHOUT_SENSOR) {
                setCookerSettingMode(CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR_WITHOUT_SENSOR);
            }else if (settingMode == CookerSettingData.SETTING_MODE_PRECISE_TEMP_GEAR) {
                setCookerSettingMode(CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR);
            }
        }else if (event.getBatteryViewStatus() == BleBatteryEvent.BATTERY_VIEW_STATUS_HIDE) {//bt lost

            if (settingMode == CookerSettingData.SETTING_MODE_TEMP_TYPE_CHOOSEN) {
                setCookerSettingMode(CookerSettingData.SETTING_MODE_TEMP_TYPE_CHOOSEN);
            }else if (settingMode == CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR) {
                setCookerSettingMode(CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR);
            }else if (settingMode == CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR_WITHOUT_SENSOR) {
                setCookerSettingMode(CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR_WITHOUT_SENSOR);
            }else if (settingMode == CookerSettingData.SETTING_MODE_PRECISE_TEMP_GEAR) {
                setCookerSettingMode(CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR);
            }else if (settingMode == CookerSettingData.SETTING_MODE_TIMER) {
                mCookerSettingData.setTempMode(CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR);
            }
        } else if (event.getBatteryViewStatus() == BleBatteryEvent.BATTERY_VIEW_STATUS_UPDATE_LEVEL) {
            bleBatteryMaxUsableMinutes = event.getMaxUsableMinutes();
        } else if (event.getBatteryViewStatus() == BleBatteryEvent.BATTERY_VIEW_STATUS_UPDATE_CHARGE_STATE) {
            bleBatteryCharging = event.getChargeStatus() > 0;
        }
    }

    private void showInfoIcon(int mode){
        if(mode==CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR){
            ivInfo.setVisibility(View.VISIBLE);
        }else {
            ivInfo.setVisibility(View.INVISIBLE);
        }
    }

    private void setCookerSettingMode(int mode) {

        Logger.getInstance().i("setCookerSettingMode(" + CookerSettingData.getCookerSettingModeStr(mode) + "): CookerID=" + mCookerSettingData.getCookerID());

        //settingMode = mode;
        tvPrecise.setVisibility(View.INVISIBLE);
        tvFast.setVisibility(View.INVISIBLE);
        tvFastOrPrecise.setVisibility(View.INVISIBLE);
        switch (mode) {
            case CookerSettingData.SETTING_MODE_FIRE_GEAR:
                ibCookerSettingMode.setImageBitmap(getBitmap(R.mipmap.ic_cooker_setting_temp));
                if (ProductManager.PRODUCT_TYPE == ProductManager.Haier) {
                    ivCookerSettingOperateArea.setImageBitmap(getBitmap(R.mipmap.bg_cooker_setting_fire_gear_mode_haier));
                } else {
                    ivCookerSettingOperateArea.setImageBitmap(getBitmap(R.mipmap.bg_cooker_setting_fire_gear_mode));
                }

                break;
            case CookerSettingData.SETTING_MODE_TEMP_TYPE_CHOOSEN:
                ibCookerSettingMode.setImageBitmap(getBitmap(R.mipmap.ic_cooker_setting_fire));
                if (ProductManager.PRODUCT_TYPE == ProductManager.Haier) {
                    ivCookerSettingOperateArea.setImageBitmap(getBitmap(R.mipmap.bg_cooker_setting_temp_mode_fast_precise_haier));
                } else {
                    //ivCookerSettingOperateArea.setImageBitmap(getBitmap(R.mipmap.bg_cooker_setting_temp_mode_fast_precise));
                    tvPrecise.setVisibility(View.VISIBLE);
                    tvFast.setVisibility(View.VISIBLE);
                    if (isTempSensorEnable()) {
                        ivCookerSettingOperateArea.setImageBitmap(getBitmap(R.mipmap.bg_cooker_setting_temp_mode_fast_precise));
                    } else {
                        ivCookerSettingOperateArea.setImageBitmap(getBitmap(R.mipmap.bg_cooker_setting_fast_precise_mode_disable));
                        EventBus.getDefault().post(new NoExternalSensorDetectedEvent(NoExternalSensorDetectedEvent.SOURCE_HOB_COOKER_SETTING_FRAGMENT));
                        setIdleCheckingPaused(true);
                    }
                }

                npvFireGear.setVisibility(View.INVISIBLE);

                break;
            case CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR:
                setFastTempGearValue();
                tvFastOrPrecise.setVisibility(View.VISIBLE);
                tvFastOrPrecise.setText(R.string.tfthobmodule_title_fast);
                ibCookerSettingMode.setImageBitmap(getBitmap(R.mipmap.ic_cooker_setting_fire));
                if (ProductManager.PRODUCT_TYPE == ProductManager.Haier) {
                    ivCookerSettingOperateArea.setImageBitmap(getBitmap(R.mipmap.bg_cooker_setting_fast_precise_mode_haier));
                } else {
                    ivCookerSettingOperateArea.setImageBitmap(getBitmap(R.mipmap.bg_cooker_setting_fast_precise_mode));

                }

                npvFireGear.setVisibility(View.INVISIBLE);

                LogUtil.d("Fast value---->" + npvFastTempGear.getValue());

                String fastTempStr = TFTHobConstant.COOKER_FAST_TEMP_LIST[npvFastTempGear.getValue()];
                fastTempStr = fastTempStr.substring(0, fastTempStr.length() - 1);


                mCookerSettingData.setTempIdentifyDrawableResourceID(CookerGearUtil.getTempIdentifyResourceID(Integer.valueOf(fastTempStr)));

                break;
            case CookerSettingData.SETTING_MODE_PRECISE_TEMP_GEAR:
                setPreciseTempGearValue();
                tvFastOrPrecise.setVisibility(View.VISIBLE);
                tvFastOrPrecise.setText(R.string.tfthobmodule_title_precise);
                ibCookerSettingMode.setImageBitmap(getBitmap(R.mipmap.ic_cooker_setting_fire));
                if (ProductManager.PRODUCT_TYPE == ProductManager.Haier) {
                    ivCookerSettingOperateArea.setImageBitmap(getBitmap(R.mipmap.bg_cooker_setting_fast_temp_mode_haier));
                } else {
                    ivCookerSettingOperateArea.setImageBitmap(getBitmap(R.mipmap.bg_cooker_setting_fast_temp_mode));
                }

                npvFireGear.setVisibility(View.INVISIBLE);



                String preciseTempStr = preciseTemps[npvPreciseTempGear.getValue()];
                preciseTempStr = preciseTempStr.substring(0, preciseTempStr.length() - 1);


                //mCookerSettingData.setTempIdentifyDrawableResourceID(CookerGearUtil.getTempIdentifyResourceID(Integer.valueOf(preciseTempStr)));
               //精确控温，工作时不需要温度值和温度标识切换显示，只需显示温度值
                mCookerSettingData.setTempIdentifyDrawableResourceID(-1);


                break;
            case CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR_WITHOUT_SENSOR:
                ibCookerSettingMode.setImageBitmap(getBitmap(R.mipmap.ic_cooker_setting_fire));
                if (ProductManager.PRODUCT_TYPE == ProductManager.Haier) {
                    ivCookerSettingOperateArea.setImageBitmap(getBitmap(R.mipmap.bg_cooker_setting_fast_temp_withoutsensor_haier));
                } else {
                    ivCookerSettingOperateArea.setImageBitmap(getBitmap(R.mipmap.bg_cooker_setting_fast_temp_withoutsensor));
                }

                npvFireGear.setVisibility(View.INVISIBLE);


                String fastTempWithoutSensorStr = TFTHobConstant.COOKER_FAST_TEMP_LIST[npvFastTempGear.getValue()];
                fastTempWithoutSensorStr = fastTempWithoutSensorStr.substring(0, fastTempWithoutSensorStr.length() - 1);

                mCookerSettingData.setTempIdentifyDrawableResourceID(CookerGearUtil.getTempIdentifyResourceID(Integer.valueOf(fastTempWithoutSensorStr)));
                break;
            case CookerSettingData.SETTING_MODE_TIMER:
                if (lastSettingMode == CookerSettingData.SETTING_MODE_FIRE_GEAR) {
                    ibCookerSettingMode.setImageBitmap(getBitmap(R.mipmap.ic_cooker_setting_fire));
                } else if (lastSettingMode == CookerSettingData.SETTING_MODE_TEMP_TYPE_CHOOSEN ||
                        lastSettingMode == CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR ||
                        lastSettingMode == CookerSettingData.SETTING_MODE_PRECISE_TEMP_GEAR) {
                    ibCookerSettingMode.setImageBitmap(getBitmap(R.mipmap.ic_cooker_setting_temp));
                }
                if (ProductManager.PRODUCT_TYPE == ProductManager.Haier) {
                    ivCookerSettingOperateArea.setImageBitmap(getBitmap(R.mipmap.bg_cooker_setting_timer_mode_haier));
                } else {
                    ivCookerSettingOperateArea.setImageBitmap(getBitmap(R.mipmap.bg_cooker_setting_timer_mode));
                }

                switchHoursRange(CookerSettingData.getMaxTimerHours(
                        mCookerSettingData.getTempMode(),
                        mCookerSettingData.getFireValue(),
                        mCookerSettingData.getTempValue()));
                setInitMinuteValue();
                setInitHourValue();
                npvFireGear.setVisibility(View.INVISIBLE);
                break;
        }
        lastSettingMode = settingMode;
        settingMode = mode;
        updateOperateAreaView();
        showInfoIcon(mode);
    }

    private void updateCookerSettingData() {
        // if (mCookerSettingData.getCookerSettingMode() == settingMode) return;
        mCookerSettingData.setCookerSettingMode(settingMode);
        switch (settingMode) {
            case CookerSettingData.SETTING_MODE_FIRE_GEAR:
                mCookerSettingData.setTempMode(CookerSettingData.COOKER_SETTING_INVALID_VALUE);
                mCookerSettingData.setTempValue(CookerSettingData.COOKER_SETTING_INVALID_VALUE);

                LogUtil.d("Enter:: npvFireGear.getValue()------->" + npvFireGear.getValue());
                String fireValueStr = TFTHobConstant.COOKER_FIRE_GEAR_LIST[npvFireGear.getValue()];
                int fireValue = fireValueStr.equals("B") ? Integer.valueOf(10) : Integer.valueOf(fireValueStr);
                mCookerSettingData.setFireValue(fireValue);
                break;
            case CookerSettingData.SETTING_MODE_TEMP_TYPE_CHOOSEN:
                break;
            case CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR:
                mCookerSettingData.setTempMode(CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR);
                mCookerSettingData.setFireValue(CookerSettingData.COOKER_SETTING_INVALID_VALUE);

                //String fastTempStr = TFTHobConstant.COOKER_FAST_TEMP_LIST[TFTHobConstant.COOKER_DEFAULT_FAST_TEMP_GEAR];
                String fastTempStr = TFTHobConstant.COOKER_FAST_TEMP_LIST[npvFastTempGear.getValue()];
                fastTempStr = fastTempStr.substring(0, fastTempStr.length() - 1);
                mCookerSettingData.setTempValue(Integer.valueOf(fastTempStr));
                LogUtil.d("the init fast temp gear is " + fastTempStr);
                break;
            case CookerSettingData.SETTING_MODE_PRECISE_TEMP_GEAR:
                mCookerSettingData.setTempMode(CookerSettingData.SETTING_MODE_PRECISE_TEMP_GEAR);
                mCookerSettingData.setFireValue(CookerSettingData.COOKER_SETTING_INVALID_VALUE);

                //String preciseTempStr = preciseTemps[TFTHobConstant.COOKER_DEFAULT_FAST_TEMP_GEAR];
                String preciseTempStr = preciseTemps[npvPreciseTempGear.getValue()];
                preciseTempStr = preciseTempStr.substring(0, preciseTempStr.length() - 1);
                mCookerSettingData.setTempValue(Integer.valueOf(preciseTempStr));

                break;
            case CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR_WITHOUT_SENSOR:
                mCookerSettingData.setTempMode(CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR_WITHOUT_SENSOR);
                mCookerSettingData.setFireValue(CookerSettingData.COOKER_SETTING_INVALID_VALUE);

                String fastTempWithoutSensorStr = TFTHobConstant.COOKER_FAST_TEMP_LIST[npvFastTempGear.getValue()];
                fastTempWithoutSensorStr = fastTempWithoutSensorStr.substring(0, fastTempWithoutSensorStr.length() - 1);
                mCookerSettingData.setTempValue(Integer.valueOf(fastTempWithoutSensorStr));
                break;
            case CookerSettingData.SETTING_MODE_TIMER:
                String hourStr = hours[npvTimerHour.getValue()];
                String mintueStr = minutes[npvTimerMinute.getValue()];
                int temperatureValue=mCookerSettingData.getTempValue();
                int powerValue=mCookerSettingData.getFireValue();
                int total_time =0;
                int hour ,min;
                hour =Integer.valueOf(hourStr);
                min= Integer.valueOf(mintueStr);
                total_time=hour *60+min;

                mMaxTime = CookerSettingData.getMaxTimerMinutes(
                        mCookerSettingData.getTempMode(),
                        mCookerSettingData.getFireValue(),
                        mCookerSettingData.getTempValue());

                if(total_time>mMaxTime){
                    hour=mMaxTime/60;
                    min=mMaxTime%60;
                }
                LogUtil.d("liang the mMaxTime is "+ mMaxTime);
                mCookerSettingData.setTimer(hour, min);
                break;
        }

    }

    private void setFastTempGearValue() {

        if (mCookerSettingData.getTempMode() == CookerSettingData.SETTING_MODE_PRECISE_TEMP_GEAR) {
            int preciseValue = TFTHobConstant.COOKER_PRECISE_TEMP_MAX - npvPreciseTempGear.getValue();

            int fastValue = (int) Math.floor((float) preciseValue / (float) TFTHobConstant.COOKER_FAST_TEMP_STEP)
                    * TFTHobConstant.COOKER_FAST_TEMP_STEP;
            if (fastValue > TFTHobConstant.COOKER_FAST_TEMP_MAX) {
                fastValue = TFTHobConstant.COOKER_FAST_TEMP_MAX;
            } else if (fastValue < TFTHobConstant.COOKER_FAST_TEMP_MIN) {
                fastValue = TFTHobConstant.COOKER_FAST_TEMP_MIN;
            }
            fastValue = (TFTHobConstant.COOKER_FAST_TEMP_MAX - fastValue)
                    / TFTHobConstant.COOKER_FAST_TEMP_STEP;
            npvFastTempGear.stopScrolling();
            npvFastTempGear.setValue(fastValue);
        }
    }

    private void setPreciseTempGearValue() {
        if (mCookerSettingData.getTempMode() == CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR) {
            int fastValue = TFTHobConstant.COOKER_FAST_TEMP_VALUE_LIST[npvFastTempGear.getValue()];
            fastValue = TFTHobConstant.COOKER_PRECISE_TEMP_MAX - fastValue;
            npvPreciseTempGear.stopScrolling();
            npvPreciseTempGear.setValue(fastValue);
        }
    }

    private void updateOperateAreaView() {
        switch (settingMode) {
            case CookerSettingData.SETTING_MODE_FIRE_GEAR:
                npvFireGear.setVisibility(View.VISIBLE);
                npvFastTempGear.setVisibility(View.INVISIBLE);
                npvPreciseTempGear.setVisibility(View.INVISIBLE);
                llTimer.setVisibility(View.INVISIBLE);
                ivTimerDisplay.setVisibility(View.INVISIBLE);
                ibTimerCancel.setVisibility(View.INVISIBLE);
                llTempIdentification.setVisibility(View.INVISIBLE);
                ivBoost.setVisibility(View.VISIBLE);
                break;
            case CookerSettingData.SETTING_MODE_TEMP_TYPE_CHOOSEN:
                npvFireGear.setVisibility(View.INVISIBLE);
                npvFastTempGear.setVisibility(View.INVISIBLE);
                npvPreciseTempGear.setVisibility(View.INVISIBLE);
                llTimer.setVisibility(View.INVISIBLE);
                ivTimerDisplay.setVisibility(View.INVISIBLE);
                ibTimerCancel.setVisibility(View.INVISIBLE);
                llTempIdentification.setVisibility(View.INVISIBLE);
                ivBoost.setVisibility(View.INVISIBLE);
                break;
            case CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR:
                npvFireGear.setVisibility(View.INVISIBLE);
                npvFastTempGear.setVisibility(View.VISIBLE);
                npvPreciseTempGear.setVisibility(View.INVISIBLE);
                llTimer.setVisibility(View.INVISIBLE);
                ivTimerDisplay.setVisibility(View.INVISIBLE);
                ibTimerCancel.setVisibility(View.INVISIBLE);
                llTempIdentification.setVisibility(View.VISIBLE);
                ivBoost.setVisibility(View.INVISIBLE);
                break;
            case CookerSettingData.SETTING_MODE_PRECISE_TEMP_GEAR:
                npvFireGear.setVisibility(View.INVISIBLE);
                npvFastTempGear.setVisibility(View.INVISIBLE);
                npvPreciseTempGear.setVisibility(View.VISIBLE);
                llTimer.setVisibility(View.INVISIBLE);
                ivTimerDisplay.setVisibility(View.INVISIBLE);
                ibTimerCancel.setVisibility(View.INVISIBLE);
                llTempIdentification.setVisibility(View.VISIBLE);
                ivBoost.setVisibility(View.INVISIBLE);
                break;
            case CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR_WITHOUT_SENSOR:
                npvFireGear.setVisibility(View.INVISIBLE);
                npvFastTempGear.setVisibility(View.VISIBLE);
                npvPreciseTempGear.setVisibility(View.INVISIBLE);
                llTimer.setVisibility(View.INVISIBLE);
                ivTimerDisplay.setVisibility(View.INVISIBLE);
                ibTimerCancel.setVisibility(View.INVISIBLE);
                llTempIdentification.setVisibility(View.VISIBLE);
                ivBoost.setVisibility(View.INVISIBLE);
                break;
            case CookerSettingData.SETTING_MODE_TIMER:
                npvFireGear.setVisibility(View.INVISIBLE);
                npvFastTempGear.setVisibility(View.INVISIBLE);
                npvPreciseTempGear.setVisibility(View.INVISIBLE);
                llTimer.setVisibility(View.VISIBLE);
                ivTimerDisplay.setVisibility(View.VISIBLE);
                ibTimerCancel.setVisibility(View.VISIBLE);
                llTempIdentification.setVisibility(View.INVISIBLE);
                ivBoost.setVisibility(View.INVISIBLE);
                break;

        }
        updateTempIndentifyUI();
    }

    private void updateTempIndentifyUI() {


        if (settingMode == CookerSettingData.SETTING_MODE_FIRE_GEAR || settingMode == CookerSettingData.SETTING_MODE_TEMP_TYPE_CHOOSEN || settingMode == CookerSettingData.SETTING_MODE_TIMER)
            return;

        if (mCookerSettingData.getTempIdentifyDrawableResourceID() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {
            ivTempIdentification.setImageBitmap(getBitmap(mCookerSettingData.getTempIdentifyDrawableResourceID()));
            switch (mCookerSettingData.getTempIdentifyDrawableResourceID()) {
                case R.mipmap.temp_identification_boiling:
                    tvTempIdentification.setText(R.string.tfthobmodule_title_boiling);
                    break;
                case R.mipmap.temp_identification_simmering:
                    tvTempIdentification.setText(R.string.tfthobmodule_title_simering);
                    break;
                case R.mipmap.temp_identification_slow_cock:
                    tvTempIdentification.setText(R.string.tfthobmodule_title_slow_cook);
                    break;
                case R.mipmap.temp_identification_keep_warm:
                    tvTempIdentification.setText(R.string.tfthobmodule_title_keep_warm);
                    break;
                case R.mipmap.temp_identification_melting:
                    tvTempIdentification.setText(R.string.tfthobmodule_title_melting);
                    break;
            }
        } else {
            ivTempIdentification.setImageDrawable(null);
            tvTempIdentification.setText(null);
        }
    }

    //, R.id.ib_cooker_setting_confirm
    @OnClick({
            R.id.ib_cooker_setting_mode,
            R.id.ib_cooker_setting_timer,
            R.id.ib_timer_cancel,
            R.id.iv_boost,
            R.id.fl_cooker_setting_confirm,
            R.id.iv_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_cooker_setting_mode:
                //CataTFTHobApplication.playClickSound();
                updateCookerSettingData();
                isCheckingPower = false;
                if (settingMode == CookerSettingData.SETTING_MODE_FIRE_GEAR) {
                    setCookerSettingMode(tempSettingMode);
                    handleTimerBeyondLimit(mCookerSettingData.getFireValue());
                } else if (settingMode == CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR
                        || settingMode == CookerSettingData.SETTING_MODE_PRECISE_TEMP_GEAR
                        || settingMode == CookerSettingData.SETTING_MODE_TEMP_TYPE_CHOOSEN
                        || settingMode == CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR_WITHOUT_SENSOR) {
                    setCookerSettingMode(CookerSettingData.SETTING_MODE_FIRE_GEAR);
                    if (settingMode != CookerSettingData.SETTING_MODE_TEMP_TYPE_CHOOSEN) {
                        handleTimerBeyondLimit(-1);
                    }

                } else if (settingMode == CookerSettingData.SETTING_MODE_TIMER) {
                    if (mCookerSettingData.getTempMode() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {
                        setCookerSettingMode(mCookerSettingData.getTempMode());
                    } else {
                        setCookerSettingMode(CookerSettingData.SETTING_MODE_FIRE_GEAR);
                    }
                    //setCookerSettingMode(lastSettingMode);

                }

                resetIdleStart();
                break;
            case R.id.ib_cooker_setting_timer:
                if (isCheckingPower || settingMode == CookerSettingData.SETTING_MODE_TIMER) return;
                updateCookerSettingData();
                setCookerSettingMode(CookerSettingData.SETTING_MODE_TIMER);
                resetIdleStart();

                break;
            case R.id.ib_cooker_setting_confirm:
            case R.id.fl_cooker_setting_confirm:
                doConfirm(true);
                break;
            case R.id.ib_timer_cancel:
                doTimerCancel();
                resetIdleStart();
                break;
            case R.id.iv_boost:
                doBoost();
                resetIdleStart();
                break;
            case R.id.iv_info:
                ToastDialog.showDialog(
                        getActivity(),
                        R.string.tfthobmodule_hob_setting_fragment_toast_content,
                        1050,
                        ivInfo,
                        ToastDialog.ANCHOR_DIRECTION_BELOW_END,
                        -90,
                        0,
                        CataSettingConstant.TOAST_SHOW_DURATION < DURATION_IDLE  ? CataSettingConstant.TOAST_SHOW_DURATION : DURATION_IDLE);
                break;
        }
    }

    private void doBoost() {
        // LogUtil.d("Enter: doBoost");
        npvFireGear.smoothScrollToValue(TFTHobConstant.COOKER_BOOST_FIRE_GEAR);
        // updateCookerSettingData();
        // mCookerSettingData.setFireValue(10);
    }

    private void doTimerCancel() {
        mCookerSettingData.setTimerHourValue(CookerSettingData.COOKER_SETTING_INVALID_VALUE);
        mCookerSettingData.setTimerMinuteValue(CookerSettingData.COOKER_SETTING_INVALID_VALUE);
        mCookerSettingData.setTimerSecondValue(CookerSettingData.COOKER_SETTING_INVALID_VALUE);
        if (mCookerSettingData.getTempMode() == CookerSettingData.SETTING_MODE_PRECISE_TEMP_GEAR)
            SettingDbHelper.saveTemperatureSensorStatus(mCookerSettingData.getCookerID());
        else {
            if (SettingDbHelper.getTemperatureSensorStatus() == mCookerSettingData.getCookerID())
                SettingDbHelper.saveTemperatureSensorStatus(-1);
        }
        GlobalVars.getInstance().setConfiguringCooker(false);
        ((OnHobCookerSettingFragmentListener) mListener).onHobCookerSettingFragmentFinish(
                mCookerSettingData,
                FINISH_TYPE_SAVE);
        LogUtil.d("cookerSettingData---->" + mCookerSettingData.toString());
    }

    private void doConfirm(boolean saveData) {
        if (isCheckingPower) {
            Logger.getInstance().w("doConfirm ignored! isCheckingPower");
            return;
        }

        if (!isVisible()) {
            Logger.getInstance().w("doConfirm ignored! !isVisible()");
            return;
        }

        if (!GlobalVars.getInstance().isPowerOffEventHandled()) {
            // 在关机过程中，如果恰好触发了此处，则直接忽略
            Logger.getInstance().w("doConfirm ignored! !isPowerOffEventHandled()");
            return;
        }

        Logger.getInstance().i("doConfirm(" + saveData + "): mCookerSettingData=[" + mCookerSettingData.toStringShort() + "]");
        if (saveData) {
            updateCookerSettingData();
            updateTempSensorStatus();
            if (mCookerSettingData.getTimerHourValue() == 0 && mCookerSettingData.getTimerMinuteValue() == 0) {
                mCookerSettingData.setTimerHourValue(CookerSettingData.COOKER_SETTING_INVALID_VALUE);
                mCookerSettingData.setTimerMinuteValue(CookerSettingData.COOKER_SETTING_INVALID_VALUE);
                mCookerSettingData.setTimerSecondValue(CookerSettingData.COOKER_SETTING_INVALID_VALUE);
            }
        }

        GlobalVars.getInstance().setConfiguringCooker(false);
        ((OnHobCookerSettingFragmentListener) mListener).onHobCookerSettingFragmentFinish(
                mCookerSettingData,
                saveData ? FINISH_TYPE_SAVE : FINISH_TYPE_CANCEL);
        LogUtil.d("cookerSettingData---->" + mCookerSettingData.toString());
        LogUtil.d("settingMode------>" + settingMode);

        if (isTempSensorEnable()) {
            if (!bleBatteryCharging && mCookerSettingData.getTimerHourValue() * 60 + mCookerSettingData.getTimerMinuteValue() > bleBatteryMaxUsableMinutes) {
                ShowErrorEvent event = new ShowErrorEvent(ShowErrorEvent.ERROR_R1E);
                event.setwParam(mCookerSettingData.getCookerID());
                EventBus.getDefault().post(event);
            }
        }
    }

    private void updateTempSensorStatus() {
        if (mCookerSettingData.getTempMode() == CookerSettingData.SETTING_MODE_PRECISE_TEMP_GEAR)
            SettingDbHelper.saveTemperatureSensorStatus(mCookerSettingData.getCookerID());
        else {
            if (SettingDbHelper.getTemperatureSensorStatus() == mCookerSettingData.getCookerID()) {
                SettingDbHelper.saveTemperatureSensorStatus(-1);
            }

        }

    }

    private void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_IDLE:
                doConfirm(true);
                break;
            case MSG_UPDATE_COOKERS_INDICATOR:
                updateCookerConfirmUI();
                break;
        }
    }

    private void switchMinutesRange(int maxMinute) {
        String format = "%02d";
        minutes = new String[maxMinute + 1];
        for (int i = maxMinute; i >= 0; i--) {
            minutes[maxMinute - i] = String.format(Locale.ENGLISH, format, i);
        }

        npvTimerMinute.setOnValueChangedListener(this);
        npvTimerMinute.setOnScrollListener(this);
        npvTimerMinute.setFriction(5 * ViewConfiguration.get(getActivity()).getScrollFriction());

        npvTimerMinute.setValues(0, minutes.length - 1, minutes);
        npvTimerMinute.setMaxValue(minutes.length - 1);
        npvTimerMinute.setMinValue(0);

        npvTimerMinute.setItemScrollListener(new NumberPickerView.ItemScrollListener() {
            @Override
            public void OnItemScroll(float itemHeight, float totalScrollY) {
                EventBus.getDefault().post(new SoundEvent(
                        SoundEvent.SOUND_ACTION_PLAY,
                        SoundUtil.SOUND_ID_SCROLL,
                        SoundEvent.SOUND_TYPE_SCROLL));
            }
        });
    }

    private void switchHoursRange(int maxHour) {
        String hourFormat = "%d";

        if (maxHour == 1) {
            hours = new String[] {"1", "0", "1", "0", "1", "0", "1", "0"};
        } else if (maxHour == 2) {
            hours = new String[] {"2", "1", "0", "2", "1", "0", "2", "1", "0"};
        } else if (maxHour == 3) {
            hours = new String[] {"3", "2", "1", "0", "3", "2", "1", "0"};
        } else {
            hours = new String[maxHour + 1];
            for (int i = maxHour; i >= 0; i--) {
                hours[maxHour - i] = String.format(Locale.ENGLISH, hourFormat, i);
            }
        }

        int padding = mContext.getResources().getDimensionPixelSize(R.dimen.tfthobmodule_padding_normal);
        int paddingTop = tfthobmoduleTimerSymbol.getPaddingTop();
        int paddingBottom = tfthobmoduleTimerSymbol.getPaddingBottom();
        if (maxHour < 10) {
            // 通过扩大左边的 Padding 值来使其位置向右稍移
            npvTimerHour.setPadding(70, 0, 66, 0);
            tfthobmoduleTimerSymbol.setPadding(padding,paddingTop,padding,paddingBottom);
        } else {
            npvTimerHour.setPadding(50, 0, 36, 0);
            tfthobmoduleTimerSymbol.setPadding(0,paddingTop,0,paddingBottom);
        }

        npvTimerHour.setOnValueChangedListener(this);
        npvTimerHour.setOnScrollListener(this);
        npvTimerHour.setFriction(5 * ViewConfiguration.getScrollFriction());

        npvTimerHour.setValues(0, hours.length - 1, hours);
        npvTimerHour.setMaxValue(hours.length - 1);
        npvTimerHour.setMinValue(0);

        npvTimerHour.setItemScrollListener(new NumberPickerView.ItemScrollListener() {
            @Override
            public void OnItemScroll(float itemHeight, float totalScrollY) {
                EventBus.getDefault().post(new SoundEvent(
                        SoundEvent.SOUND_ACTION_PLAY,
                        SoundUtil.SOUND_ID_SCROLL,
                        SoundEvent.SOUND_TYPE_SCROLL));
            }
        });
    }

    private void setInitHourValue() {
        int hour = mCookerSettingData.getTimerRemainHourValue();
        if (hour > hours.length - 1) {
            hour = hours.length - 1;
        }
        if (hour != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {
            int idx = getHourIndex(
                    hours,
                    String.format(Locale.ENGLISH, "%d", hour));
            if (idx != -1) {
                npvTimerHour.setValue(idx);
            } else {
                npvTimerHour.setValue(0);
            }
        } else {
            npvTimerHour.setValue(hours.length - 1);
        }
    }

    private void setInitMinuteValue() {
        if (mCookerSettingData.getTimerRemainMinuteValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {
            int idx = getMinuteIndex(
                    minutes,
                    String.format(
                            Locale.ENGLISH,
                            "%02d",
                            mCookerSettingData.getTimerRemainMinuteValue()));
            if (idx != -1) {
                npvTimerMinute.setValue(idx);
            } else {
                npvTimerMinute.setValue(0);
            }
        } else {
            npvTimerMinute.setValue(minutes.length - 11);
        }
    }

    private int getHourIndex(String[] hours ,String value) {
        for (int i = 0; i < hours.length ; i++) {
            if (hours[i].equals(value)) {
                return i;
            }
        }

        return -1;
    }

    private int getMinuteIndex(String[] mintues ,String value) {
        for (int i = 0; i < mintues.length ; i++) {
            if (mintues[i].equals(value)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {
        if (picker == npvFireGear) {
            int fireGear = newVal == 0 ? 10 : Integer.valueOf(TFTHobConstant.COOKER_FIRE_GEAR_LIST[newVal]);
            int realValue = checkPowerLimit(fireGear);
            if (realValue == fireGear) {
                mCookerSettingData.setFireValue(fireGear);
            }else {

                mCookerSettingData.setFireValue(realValue);
                npvFireGear.smoothScrollToValue(CookerGearUtil.getFireGearIndex(String.valueOf(realValue)));
            }

            handleTimerBeyondLimit(fireGear);

            //LogUtil.d("TotalPower--->" + mPowerLimitManager.getCurrentTotalPower() + "---Left--->" + mPowerLimitManager.getCurrentTotalSingleLeftPower() + "----Right---->" + mPowerLimitManager.getCurrentTotalSingleRightPower());
           // LogUtil.d("TotalPower final---->" + mPowerLimitManager.getTotalPower());

            //mCookerSettingData.setFireValue(fireGear);

           /* if (mCookerSettingData.getTempMode() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {
                tempSettingMode = mCookerSettingData.getTempMode();
                mCookerSettingData.setTempMode(CookerSettingData.COOKER_SETTING_INVALID_VALUE);
                mCookerSettingData.setTempValue(CookerSettingData.COOKER_SETTING_INVALID_VALUE);
            }*/

        } else if (picker == npvFastTempGear) {

            String tempStr = TFTHobConstant.COOKER_FAST_TEMP_LIST[newVal];
            tempStr = tempStr.substring(0, tempStr.length() - 1);
            int tempGear = Integer.valueOf(tempStr);
            mCookerSettingData.setTempValue(tempGear);
            LogUtil.d("the setting tmpgear is " + tempGear);

            int tempIdentifyResourceID = CookerGearUtil.getTempIdentifyResourceID(tempGear);
            mCookerSettingData.setTempIdentifyDrawableResourceID(tempIdentifyResourceID);

            updateTempIndentifyUI();

            handleTimerBeyondLimit(-1);
          /*  if (mCookerSettingData.getFireValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {
                mCookerSettingData.setFireValue(CookerSettingData.COOKER_SETTING_INVALID_VALUE);
            }*/

        } else if (picker == npvPreciseTempGear) {

            String tempStr = preciseTemps[newVal];
            tempStr = tempStr.substring(0, tempStr.length() - 1);
            int tempGear = Integer.valueOf(tempStr);
            mCookerSettingData.setTempValue(tempGear);
            int tempIdentifyResourceID = CookerGearUtil.getTempIdentifyResourceID(tempGear);
            //mCookerSettingData.setTempIdentifyDrawableResourceID(tempIdentifyResourceID);
            //精确控温，工作时不需要温度值和温度标识切换显示，只需显示温度值
            mCookerSettingData.setTempIdentifyDrawableResourceID(-1);
            updateTempIndentifyUI();

            handleTimerBeyondLimit(-1);

           /* if (mCookerSettingData.getFireValue() != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {
                mCookerSettingData.setFireValue(CookerSettingData.COOKER_SETTING_INVALID_VALUE);
            }*/

        } else if (picker == npvTimerMinute) {

            mCookerSettingData.setTimerMinuteValue(Integer.valueOf(minutes[npvTimerMinute.getValue()]));

            int fireValue = mCookerSettingData.getFireValue();
            //int hourValue = mCookerSettingData.getTimerHourValue();
            int hourValue = Integer.valueOf(hours[npvTimerHour.getValue()]);
            int minuteValue = mCookerSettingData.getTimerMinuteValue();

            mCookerSettingData.setTimerRemainMinuteValue(minuteValue);

            if (fireValue >= 9) {
                if (hourValue == 1) {
                    if (minutes.length != 31) {
                        switchMinutesRange(30);
                        if (minuteValue > 30) {
                            minuteValue = 30;
                            mCookerSettingData.setTimerMinuteValue(minuteValue);
                        }
                        if (minuteValue != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {
                            npvTimerMinute.setValue(30 - minuteValue);
                        } else {
                            npvTimerMinute.setValue(minutes.length - 11);
                        }
                    }
                }
            } else if (minuteValue > 0) {
                if (hourValue == CookerSettingData.getMaxTimerHours(
                        mCookerSettingData.getTempMode(),
                        mCookerSettingData.getFireValue(),
                        mCookerSettingData.getTempValue())) {
                    npvTimerHour.smoothScrollToValue(1);
                }
            }
        } else if (picker == npvTimerHour) {
            mCookerSettingData.setTimerHourValue(Integer.valueOf(hours[npvTimerHour.getValue()]));

            int fireValue = mCookerSettingData.getFireValue();
            int hourValue = mCookerSettingData.getTimerHourValue();
           // int minuteValue = mCookerSettingData.getTimerMinuteValue();
            int minuteValue = Integer.valueOf(minutes[npvTimerMinute.getValue()]);

            mCookerSettingData.setTimerRemainHourValue(hourValue);

            if (fireValue >= 9) {
                if (hourValue == 1) {
                    if (minutes.length != 31) {
                        switchMinutesRange(30);
                        if (minuteValue > 30) {
                            minuteValue = 30;
                            mCookerSettingData.setTimerMinuteValue(minuteValue);
                        }
                        if (minuteValue != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {
                            npvTimerMinute.setValue(30 - minuteValue);
                        } else {
                            npvTimerMinute.setValue(minutes.length - 11);
                        }

                    }
                } else {
                    if (minutes.length != 60) {
                        switchMinutesRange(59);
                        if (minuteValue != CookerSettingData.COOKER_SETTING_INVALID_VALUE) {
                            npvTimerMinute.setValue(59 - minuteValue);
                        } else {
                            npvTimerMinute.setValue(minutes.length - 11);
                        }
                    }
                }
            } else if (hourValue == CookerSettingData.getMaxTimerHours(
                    mCookerSettingData.getTempMode(),
                    mCookerSettingData.getFireValue(),
                    mCookerSettingData.getTempValue())) {
                npvTimerMinute.smoothScrollToValue(minutes.length - 1);
            }
        }
        resetIdleStart();

    }

    private int checkPowerLimit(int value) {
        isCheckingPower = true;
        CookerData cookerData = new CookerData(mCookerSettingData.getCookerID(), InductionCookerProtocol.PROTOCOL_COOKER_WORK_MODE_FIRE_GEAR);
        int returnPowerValue = mPowerLimitManager.checkPower(value,cookerData);
        LogUtil.d("Enter:: checkPowerLimit---->" + returnPowerValue);
        if (returnPowerValue == value) isCheckingPower = false;
        else isCheckingPower = true;
        return returnPowerValue;

    }

    public boolean isIdleCheckingPaused() {
        return idleCheckingPaused;
    }

    public void setIdleCheckingPaused(boolean idleCheckingPaused) {
        this.idleCheckingPaused = idleCheckingPaused;
    }

    private boolean isTempSensorEnable() {
        int sensorStatus = SettingDbHelper.getTemperatureSensorStatus();

        return sensorStatus == -1 || sensorStatus == mCookerSettingData.getCookerID();//-1表示温度传感器空闲
    }

    private void handleTimerBeyondLimit(int fireGear) {
        if (timerBeyondLimit(fireGear)) {
            mCookerSettingData.setTimerHourValue(0);
            mCookerSettingData.setTimerMinuteValue(0);
            mCookerSettingData.setTimerRemainHourValue(-1);
            mCookerSettingData.setTimerRemainMinuteValue(-1);
        }
    }
    private boolean timerBeyondLimit(int fireGear) {
        int hour = mCookerSettingData.getTimerHourValue();
        int minute = mCookerSettingData.getTimerMinuteValue();
        int minutes = hour * 60 + minute;

        if (fireGear > 8) {
            return minutes > 90;
        } else {
            return minutes > CookerSettingData.getMaxTimerHours(
                    mCookerSettingData.getTempMode(),
                    mCookerSettingData.getFireValue(),
                    mCookerSettingData.getTempValue()) * 60;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder1 = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder1.unbind();
        unbindDrawables(getView());
        recycleBitmaps();
        System.gc();
    }

    @OnClick(R.id.ib_cooker_setting_menu)
    public void onClick() {
        GlobalVars.getInstance().setConfiguringCooker(false);
        ((OnHobCookerSettingFragmentListener) mListener).onShowHobRecipesFragment();
        SettingDbHelper.saveRecipesCookerID(mCookerSettingData.getCookerID());
        resetIdleStart();
    }

    @Override
    public void onScrollStateChange(NumberPickerView view, int scrollState) {

        scrollingGear = scrollState != NumberPickerView.OnScrollListener.SCROLL_STATE_IDLE;
        if (view == npvFireGear && scrollState == NumberPickerView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            isCheckingPower = true;
        }
        resetIdleStart();
        if (scrollState == NumberPickerView.OnScrollListener.SCROLL_STATE_CLICK) {
            onValueChange(view, view.getValue(), view.getValue());
            doConfirm(true);
        }
    }

    public interface OnHobCookerSettingFragmentListener extends OnFragmentListener {
        void onHobCookerSettingFragmentFinish(CookerSettingData data, int finishType);

        void onShowHobRecipesFragment();

    }

    private Bitmap getBitmap(int source) {
        if (bitmapMap.containsKey(source)) {
            return bitmapMap.get(source);
        }

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), source);
        bitmapMap.put(source, bitmap);
        return bitmap;
    }
    private void recycleBitmaps() {
        for (int r: bitmapMap.keySet()) {
            bitmapMap.remove(r).recycle();
        }
    }

    private class CheckingIdleThread extends Thread {

        private boolean forceToStop = false;

        @Override
        public void run() {
            try {
                long lastTimeChecked = SystemClock.elapsedRealtime();
                while (!forceToStop) {
                    if (isIdleCheckingPaused()) {
                        resetIdleStart();
                    }
                    if (GlobalVars.getInstance().isShowingPauseHobDialog()) {
                        // 暂停状态下不进行计时
                        resetIdleStart();
                    }
                    if (scrollingGear || isCheckingPower) {
                        // 操作滚动控件或者处于检测功率时不进行计时
                        resetIdleStart();
                    }
                    if (SystemClock.elapsedRealtime() - timestampIdleStart >= DURATION_IDLE) {
                        // 用户已经进入本界面，并且已经操作过
                        handler.sendEmptyMessage(MSG_IDLE);
                        break;
                    } else if (SystemClock.elapsedRealtime() - lastTimeChecked >= DURATION_CHECK) {
                        handler.sendEmptyMessage(MSG_UPDATE_COOKERS_INDICATOR);
                        lastTimeChecked = SystemClock.elapsedRealtime();
                    }
                    Thread.sleep(DURATION_SLEEP);
                }
            } catch (InterruptedException e) {
                LogUtil.e(e.getMessage());
            }
        }

        public void setForceToStop(boolean forceToStop) {
            this.forceToStop = forceToStop;
        }
    }

    public static class MessageHandler extends Handler {
        private final WeakReference<HobCookerSettingFragment> master;

        private MessageHandler(HobCookerSettingFragment master) {
            this.master = new WeakReference<>(master);
        }

        @Override
        public void handleMessage(Message msg) {
            this.master.get().handleMessage(msg);
        }
    }
}
