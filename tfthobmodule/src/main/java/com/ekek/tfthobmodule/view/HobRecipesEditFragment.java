package com.ekek.tfthobmodule.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.commonmodule.utils.KeyboardUtil;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.dialog.ToastDialog;
import com.ekek.tfthobmodule.CataTFTHobApplication;
import com.ekek.tfthobmodule.R;
import com.ekek.tfthobmodule.constants.TFTHobConstant;
import com.ekek.tfthobmodule.entity.MyRecipesTable;
import com.ekek.tfthobmodule.event.SoundEvent;
import com.ekek.tfthobmodule.utils.SoundUtil;
import com.ekek.viewmodule.common.NumberPickerView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HobRecipesEditFragment extends BaseFragment implements NumberPickerView.OnValueChangeListener {

    // Widgets
    Unbinder unbinder;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.ll_edit_name)
    LinearLayout llEditName;
    @BindView(R.id.et_decription)
    EditText etDecription;
    @BindView(R.id.iv_cooker_setting_operate_area)
    ImageView ivCookerSettingOperateArea;
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
    @BindView(R.id.ic_info)
    ImageView icInfo;
    Unbinder unbinder1;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_up_toast)
    TextView tvUpToast;
    @BindView(R.id.tv_ok)
    TextView tvOk;
    @BindView(R.id.iv_information)
    ImageView ivInformation;
    @BindView(R.id.tv_dot)
    TextView tvDot;
    @BindView(R.id.tv_precise)
    TextView tvPrecise;
    @BindView(R.id.tv_fast)
    TextView tvFast;
    @BindView(R.id.fl_temp)
    FrameLayout flTemp;


    public static final int RECIPES_EDIT_TYPE_NAME = 0;
    public static final int RECIPES_EDIT_TYPE_TEMPERATURE = 1;
    public static final int RECIPES_EDIT_TYPE_TIME = 2;
    public static final int RECIPES_EDIT_TYPE_DESCRIPTION = 3;
    public static final int RECIPES_EDIT_TYPE_CHOOSE_TEMPERATURE_MODE = 4;

    private int type = RECIPES_EDIT_TYPE_NAME;
    private MyRecipesTable initData;
    private String[] hours = new String[24];
    private String[] minutes = new String[60];
    private boolean fastTempMode = true;
    private String[] preciseTemps = new String[TFTHobConstant.COOKER_PRECISE_TEMP_LEN];
    private Map<Integer, Bitmap> bitmapMap = new HashMap<>();

    @Override
    public int initLyaout() {
        return R.layout.tfthobmodule_fragment_hob_recipes_edit;
    }

    @Override
    public void initListener() {

    }

    public void setType(int type) {
        this.type = type;
    }

    public void setInitData(MyRecipesTable initData) {
        this.initData = initData;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            init();
        }else {
            hideInputMethod();
            //LogUtil.d("liang hidden ");
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        initTexts();
    }

    private void init() {
        etName.setImeOptions(EditorInfo.IME_ACTION_NONE);
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                LogUtil.d("liang text edit 1");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //LogUtil.d("liang text edit 2");
            }

            @Override
            public void afterTextChanged(Editable s) {
                //LogUtil.d("liang text edit 3 etName");
                CataTFTHobApplication.getInstance().updateLatestTouchTime();
            }
        });

        etDecription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //LogUtil.d("liang text edit 1");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //LogUtil.d("liang text edit 2");
            }

            @Override
            public void afterTextChanged(Editable s) {
                //LogUtil.d("liang text edit 3 etDecription");
                CataTFTHobApplication.getInstance().updateLatestTouchTime();
            }
        });


        tvPrecise.setVisibility(View.INVISIBLE);
        tvFast.setVisibility(View.INVISIBLE);
        flTemp.setVisibility(View.INVISIBLE);
        switch (type) {
            case RECIPES_EDIT_TYPE_NAME:  // 名称
                llEditName.setVisibility(View.VISIBLE);
                etName.setText(initData.getName());
                KeyboardUtil.openKeyBoard(getActivity(), etName);
                etDecription.setVisibility(View.INVISIBLE);
                npvFastTempGear.setVisibility(View.INVISIBLE);
                npvPreciseTempGear.setVisibility(View.INVISIBLE);
                llTimer.setVisibility(View.INVISIBLE);
                ivTimerDisplay.setVisibility(View.INVISIBLE);
                ivCookerSettingOperateArea.setVisibility(View.INVISIBLE);
                //  tvTitle.setText(R.string.tfthobmodule_fragment_my_recipes_edit_title_name);
                tvTitle.setVisibility(View.GONE);
                tvBack.setText(R.string.tfthobmodule_fragment_my_recipes_edit_title_name);
                ivInformation.setVisibility(View.VISIBLE);
                icInfo.setVisibility(View.INVISIBLE);
                tvOk.setVisibility(View.VISIBLE);
                break;
            case RECIPES_EDIT_TYPE_CHOOSE_TEMPERATURE_MODE:
                llEditName.setVisibility(View.INVISIBLE);
                etDecription.setVisibility(View.INVISIBLE);
                npvFastTempGear.setVisibility(View.INVISIBLE);
                npvPreciseTempGear.setVisibility(View.INVISIBLE);
                llTimer.setVisibility(View.INVISIBLE);
                ivTimerDisplay.setVisibility(View.INVISIBLE);
                ivCookerSettingOperateArea.setImageBitmap(getBitmap(R.mipmap.bg_cooker_setting_temp_mode_fast_precise));
                ivCookerSettingOperateArea.setVisibility(View.VISIBLE);
                tvPrecise.setVisibility(View.VISIBLE);
                tvFast.setVisibility(View.VISIBLE);
                flTemp.setVisibility(View.VISIBLE);
                ivCookerSettingOperateArea.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            float touchX = event.getX();
                            float touchY = event.getY();
                            float centerX = view.getWidth() / 2;
                            float centerY = view.getHeight() / 2;
                            if (type == RECIPES_EDIT_TYPE_CHOOSE_TEMPERATURE_MODE) {

                                view.playSoundEffect(SoundEffectConstants.CLICK);
                                if (touchY > centerY) {
                                    setType(RECIPES_EDIT_TYPE_TEMPERATURE);
                                    fastTempMode = true;
                                    init();
                                } else if (touchY < centerY) {
                                    setType(RECIPES_EDIT_TYPE_TEMPERATURE);
                                    fastTempMode = false;
                                    init();
                                }
                            } if (type == RECIPES_EDIT_TYPE_TEMPERATURE) {

                                if (isOutOfCircle(centerX, centerY, touchX, touchY)) {
                                    if (touchX < centerX && touchY > centerY) {
                                        view.playSoundEffect(SoundEffectConstants.CLICK);
                                        if (fastTempMode) {
                                            String tempGear = TFTHobConstant.COOKER_FAST_TEMP_LIST[npvFastTempGear.getValue()];
                                            tempGear = tempGear.substring(0, tempGear.length() - 1);
                                            initData.setTempValue(Integer.valueOf(tempGear));
                                        } else {
                                            String preciseTempStr = preciseTemps[npvPreciseTempGear.getValue()];
                                            preciseTempStr = preciseTempStr.substring(0, preciseTempStr.length() - 1);
                                            initData.setTempValue(Integer.valueOf(preciseTempStr));
                                        }
                                        fastTempMode = !fastTempMode;
                                        init();
                                    }
                                }
                            }
                        }
                        return true;
                    }
                });
                tvTitle.setVisibility(View.GONE);
                tvBack.setText(R.string.tfthobmodule_title_back);
                ivInformation.setVisibility(View.INVISIBLE);
                icInfo.setVisibility(View.INVISIBLE);
                tvOk.setVisibility(View.INVISIBLE);
                break;
            case RECIPES_EDIT_TYPE_TEMPERATURE:  // 温度
                initFastTempGearView();
                initPreciseTempGearView();
                llEditName.setVisibility(View.INVISIBLE);
                etDecription.setVisibility(View.INVISIBLE);

                int temperature = initData.getTempValue();
                if (fastTempMode) {
                    npvFastTempGear.setVisibility(View.VISIBLE);
                    npvPreciseTempGear.setVisibility(View.INVISIBLE);
                    ivCookerSettingOperateArea.setImageBitmap(getBitmap(R.mipmap.bg_cooker_setting_fast_precise_mode));
                    if (temperature <= 0) {
                        npvFastTempGear.setValue(TFTHobConstant.COOKER_DEFAULT_FAST_TEMP_GEAR);
                    } else {
                        int fastValue = (int) Math.floor((float) temperature / (float) TFTHobConstant.COOKER_FAST_TEMP_STEP)
                                * TFTHobConstant.COOKER_FAST_TEMP_STEP;
                        if (fastValue > TFTHobConstant.COOKER_FAST_TEMP_MAX) {
                            fastValue = TFTHobConstant.COOKER_FAST_TEMP_MAX;
                        } else if (fastValue < TFTHobConstant.COOKER_FAST_TEMP_MIN) {
                            fastValue = TFTHobConstant.COOKER_FAST_TEMP_MIN;
                        }
                        fastValue = (TFTHobConstant.COOKER_FAST_TEMP_MAX - fastValue)
                                / TFTHobConstant.COOKER_FAST_TEMP_STEP;
                        npvFastTempGear.setValue(fastValue);
                    }
                } else {
                    npvFastTempGear.setVisibility(View.INVISIBLE);
                    npvPreciseTempGear.setVisibility(View.VISIBLE);
                    ivCookerSettingOperateArea.setImageBitmap(getBitmap(R.mipmap.bg_cooker_setting_fast_temp_mode));
                    if (temperature <= 0) {
                        npvPreciseTempGear.setValue(TFTHobConstant.COOKER_DEFAULT_PRECISE_TEMP_GEAR);
                    } else {
                        int preciseValue = TFTHobConstant.COOKER_PRECISE_TEMP_MAX - temperature;
                        npvPreciseTempGear.setValue(preciseValue);
                    }
                }

                llTimer.setVisibility(View.INVISIBLE);
                ivTimerDisplay.setVisibility(View.INVISIBLE);
                ivCookerSettingOperateArea.setVisibility(View.VISIBLE);
                tvTitle.setText(R.string.tfthobmodule_fragment_my_recipes_edit_title_power);
                tvTitle.setVisibility(View.GONE);// 更新 2018年9月23日14:25:52
                tvBack.setText(R.string.tfthobmodule_title_back);
                ivInformation.setVisibility(View.INVISIBLE);
                icInfo.setVisibility(View.VISIBLE);
                tvOk.setVisibility(View.VISIBLE);
                break;
            case RECIPES_EDIT_TYPE_TIME:   // 定时
                llEditName.setVisibility(View.INVISIBLE);
                etDecription.setVisibility(View.INVISIBLE);
                npvFastTempGear.setVisibility(View.INVISIBLE);
                npvPreciseTempGear.setVisibility(View.INVISIBLE);
                llTimer.setVisibility(View.VISIBLE);
                ivTimerDisplay.setVisibility(View.VISIBLE);
                initTimerView();
                if (initData.getHourValue() != 0 || initData.getMinuteValue() != 0) {
                    npvTimerHour.setValue(23 - initData.getHourValue());
                    npvTimerMinute.setValue(59 - initData.getMinuteValue());
                }

                ivCookerSettingOperateArea.setImageBitmap(getBitmap(R.mipmap.bg_cooker_setting_timer_mode));
                ivCookerSettingOperateArea.setVisibility(View.VISIBLE);
                tvTitle.setText(R.string.tfthobmodule_fragment_my_recipes_edit_title_time);
                tvTitle.setVisibility(View.GONE); // 更新 2018年9月23日14:25:52
                tvBack.setText(R.string.tfthobmodule_title_back);
                ivInformation.setVisibility(View.INVISIBLE);
                icInfo.setVisibility(View.VISIBLE);
                tvOk.setVisibility(View.VISIBLE);
                break;
            case RECIPES_EDIT_TYPE_DESCRIPTION: // 描述

                llEditName.setVisibility(View.INVISIBLE);
                etDecription.setVisibility(View.VISIBLE);
                etDecription.setText(initData.getDecription());
                KeyboardUtil.openKeyBoard(getActivity(), etDecription);
                npvFastTempGear.setVisibility(View.INVISIBLE);
                npvPreciseTempGear.setVisibility(View.INVISIBLE);
                llTimer.setVisibility(View.INVISIBLE);
                ivTimerDisplay.setVisibility(View.INVISIBLE);
                //  tvTitle.setText(R.string.tfthobmodule_fragment_my_recipes_edit_title_description);
                tvTitle.setVisibility(View.GONE);
                tvBack.setText(R.string.tfthobmodule_fragment_my_recipes_edit_title_description);
                ivCookerSettingOperateArea.setVisibility(View.INVISIBLE);
                ivInformation.setVisibility(View.VISIBLE);
                icInfo.setVisibility(View.INVISIBLE);
                tvOk.setVisibility(View.VISIBLE);


                break;
        }
        initTexts();
    }

    private void initTexts() {
        Typeface typeface = GlobalVars.getInstance().getDefaultFontBold();
        tvBack.setTypeface(typeface);
        tvDot.setTypeface(typeface);

        typeface = GlobalVars.getInstance().getDefaultFontRegular();
        tvOk.setTypeface(typeface);
        tvOk.setText(R.string.viewmodule_c31_c33_c34_ok);
        tvPrecise.setTypeface(typeface);
        tvPrecise.setText(R.string.tfthobmodule_title_precise);
        tvFast.setTypeface(typeface);
        tvFast.setText(R.string.tfthobmodule_title_fast);

        if (GlobalVars.getInstance().getCurrentLocale().toString().toLowerCase().equals("ru")) {
            tvPrecise.setTextSize(52.0f);
        } else {
            tvPrecise.setTextSize(60.0f);
        }
    }

    private void initFastTempGearView() {
        npvFastTempGear.setDisplayedValues(TFTHobConstant.COOKER_FAST_TEMP_LIST);
        npvFastTempGear.setOnValueChangedListener(this);
        npvFastTempGear.setMaxValue(TFTHobConstant.COOKER_FAST_TEMP_LIST.length - 1);
        npvFastTempGear.setMinValue(0);
        npvFastTempGear.setValue(TFTHobConstant.COOKER_DEFAULT_FAST_TEMP_GEAR);

        npvFastTempGear.setFriction(10 * ViewConfiguration.get(getActivity()).getScrollFriction());
        Typeface typeface = GlobalVars.getInstance().getDefaultFontBold();
        npvFastTempGear.setContentTextTypeface(typeface);
        npvFastTempGear.setHintTextTypeface(typeface);
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
        npvPreciseTempGear.setMaxValue(preciseTemps.length - 1);
        npvPreciseTempGear.setMinValue(0);
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

    private void initTimerView() {

        for (int i = 23; i >= 0; i--) {

            hours[23 - i] = String.format("%d", i);
        }


        for (int i = 59; i >= 0; i--) {
            minutes[59 - i] = String.format("%02d", i);
        }

        npvTimerHour.setDisplayedValues(hours);
        npvTimerHour.setOnValueChangedListener(this);
        //npvFireGear.setOnScrollListener(this);
        npvTimerHour.setMaxValue(hours.length - 1);
        npvTimerHour.setMinValue(0);

        npvTimerHour.setValue(22);

        npvTimerHour.setFriction(5 * ViewConfiguration.get(getActivity()).getScrollFriction());
        npvTimerHour.setItemScrollListener(new NumberPickerView.ItemScrollListener() {
            @Override
            public void OnItemScroll(float itemHeight, float totalScrollY) {
                EventBus.getDefault().post(new SoundEvent(
                        SoundEvent.SOUND_ACTION_PLAY,
                        SoundUtil.SOUND_ID_SCROLL,
                        SoundEvent.SOUND_TYPE_SCROLL));
            }
        });

        npvTimerMinute.setDisplayedValues(minutes);
        npvTimerMinute.setOnValueChangedListener(this);
        //npvFireGear.setOnScrollListener(this);
        npvTimerMinute.setMaxValue(minutes.length - 1);
        npvTimerMinute.setMinValue(0);
        npvTimerMinute.setValue(59);//59

        npvTimerMinute.setFriction(5 * ViewConfiguration.get(getActivity()).getScrollFriction());
        npvTimerMinute.setItemScrollListener(new NumberPickerView.ItemScrollListener() {
            @Override
            public void OnItemScroll(float itemHeight, float totalScrollY) {
                EventBus.getDefault().post(new SoundEvent(
                        SoundEvent.SOUND_ACTION_PLAY,
                        SoundUtil.SOUND_ID_SCROLL,
                        SoundEvent.SOUND_TYPE_SCROLL));
            }
        });


        Typeface typeface = GlobalVars.getInstance().getDefaultFontBold();
        npvTimerMinute.setContentTextTypeface(typeface);
        npvTimerMinute.setHintTextTypeface(typeface);

        npvTimerHour.setContentTextTypeface(typeface);
        npvTimerHour.setHintTextTypeface(typeface);

    }


    @OnClick({R.id.ic_info, R.id.iv_information, R.id.tv_ok, R.id.tv_back})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_back:
                doBack();
                hideInputMethod();
                break;
            case R.id.tv_ok:
                doConfirm();
                hideInputMethod();
                break;
            case R.id.iv_information:
            case R.id.ic_info:
                switch (type) {
                    case RECIPES_EDIT_TYPE_NAME: // 名称
                        ToastDialog.showDialog(
                                getActivity(),
                                R.string.tfthobmodule_hob_recipes_edit_fragment_toast_content_name,
                                ToastDialog.WIDTH_LONG,
                                Gravity.TOP | Gravity.END,
                                35,
                                145,
                                CataSettingConstant.TOAST_SHOW_DURATION);
                        break;
                    case RECIPES_EDIT_TYPE_TEMPERATURE: // 温度
                        ToastDialog.showDialog(
                                getActivity(),
                                R.string.tfthobmodule_hob_recipes_edit_fragment_toast_content_temp,
                                ToastDialog.WIDTH_LONG,
                                Gravity.BOTTOM | Gravity.END,
                                160,
                                20,
                                CataSettingConstant.TOAST_SHOW_DURATION);
                        break;
                    case RECIPES_EDIT_TYPE_TIME:  // 定时
                        ToastDialog.showDialog(
                                getActivity(),
                                R.string.tfthobmodule_hob_recipes_edit_fragment_toast_content_time,
                                ToastDialog.WIDTH_LONG,
                                Gravity.BOTTOM | Gravity.END,
                                150,
                                20,
                                CataSettingConstant.TOAST_SHOW_DURATION);
                        break;
                    case RECIPES_EDIT_TYPE_DESCRIPTION: // 描述
                        ToastDialog.showDialog(
                                getActivity(),
                                R.string.tfthobmodule_hob_recipes_edit_fragment_toast_content_desc,
                                ToastDialog.WIDTH_LONG,
                                Gravity.TOP | Gravity.END,
                                35,
                                145,
                                CataSettingConstant.TOAST_SHOW_DURATION,
                                new ToastDialog.OnToastDialogListener() {
                                    @Override
                                    public void onToastDialogDismiss() {
                                        KeyboardUtil.openKeyBoard(getActivity(), etDecription);
                                    }
                                }
                        );
                        break;
                }
                break;
        }

    }

    private void doConfirm() {
        switch (type) {
            case RECIPES_EDIT_TYPE_NAME:
                ((OnHobRecipesEditFragmentListener) mListener).onHobRecipesEditFragmentFinish(
                        type,
                        etName.getText().toString().replace('\n',' ').trim());
                break;
            case RECIPES_EDIT_TYPE_TEMPERATURE:
                String tempStr = TFTHobConstant.COOKER_FAST_TEMP_LIST[npvFastTempGear.getValue()];
                if (!fastTempMode) {
                    tempStr = preciseTemps[npvPreciseTempGear.getValue()];
                }
                ((OnHobRecipesEditFragmentListener) mListener).onHobRecipesEditFragmentFinish(type, tempStr);
                break;
            case RECIPES_EDIT_TYPE_TIME:
                String hourStr = hours[npvTimerHour.getValue()];
                String mintueStr = minutes[npvTimerMinute.getValue()];
                if (hourStr.equals("0") && mintueStr.equals("00")) {
                    ((OnHobRecipesEditFragmentListener) mListener).onHobRecipesEditFragmentFinish(type, "");

                } else {
                    ((OnHobRecipesEditFragmentListener) mListener).onHobRecipesEditFragmentFinish(type, hourStr + ":" + mintueStr);

                }
                break;
            case RECIPES_EDIT_TYPE_DESCRIPTION:
                ((OnHobRecipesEditFragmentListener) mListener).onHobRecipesEditFragmentFinish(type, etDecription.getText().toString());
                break;
            case RECIPES_EDIT_TYPE_CHOOSE_TEMPERATURE_MODE:
                ((OnHobRecipesEditFragmentListener) mListener).onHobRecipesEditFragmentFinish(type, "");
                break;
        }
    }

    private void doBack() {
        ((OnHobRecipesEditFragmentListener) mListener).onHobRecipesEditFragmentCancel();
    }



    @Override
    public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {

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
    }


    public interface OnHobRecipesEditFragmentListener extends OnFragmentListener {
        void onHobRecipesEditFragmentCancel();
        void onHobRecipesEditFragmentFinish(int type, String content);
    }

    private void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
        if (isOpen&&getView()!=null) {
            imm.showSoftInput(getView(), InputMethodManager.SHOW_FORCED);

            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0); //强制隐藏键盘
        }

    }
    private Bitmap getBitmap(int source) {
        if (bitmapMap.containsKey(source)) {
            return bitmapMap.get(source);
        }

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), source);
        bitmapMap.put(source, bitmap);
        return bitmap;
    }
}
