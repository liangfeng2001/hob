package com.ekek.settingmodule.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.settingmodule.R;
import com.ekek.settingmodule.R2;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.database.SettingPreferencesUtil;
import com.ekek.settingmodule.events.TestCookwareSettingFragmentContentChanged;
import com.ekek.settingmodule.model.GetTestCookwareResult;
import com.ekek.settingmodule.model.SendOrderForFirstSwitchOn;
import com.ekek.settingmodule.model.SendOrderToTestCookwareFragment;
import com.ekek.settingmodule.model.SettingDoBack;
import com.ekek.settingmodule.model.SettingTestCookDataEvent;
import com.ekek.settingmodule.model.WhichBannerWillBeShow;
import com.ekek.viewmodule.common.CataAlertDialog;
import com.ekek.viewmodule.common.FocusedTextView;
import com.ekek.viewmodule.contract.HobCircleCookerContract;
import com.ekek.viewmodule.contract.HobRectangleCookerContract;
import com.ekek.viewmodule.hob.Hob60BigCircleCooker;
import com.ekek.viewmodule.hob.Hob60SmallCircleCooker;
import com.ekek.viewmodule.hob.Hob80BigCircleCooker;
import com.ekek.viewmodule.hob.Hob80RectangleCookerNew;
import com.ekek.viewmodule.hob.Hob80SmallCircleCooker;
import com.ekek.viewmodule.hob.Hob90CircleCooker;
import com.ekek.viewmodule.product.ProductManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TestCookwareSettingFragment extends BaseFragment {
    @BindView(R2.id.tv_start_new_test)
    TextView tvStartNewTest;
    @BindView(R2.id.tv_title)
    FocusedTextView tvTitle;
    Unbinder unbinder;
    @BindView(R2.id.tv_content)
    TextView tvContent;
    @BindView(R2.id.iv_next_button)
    ImageView ivNextButton;
    @BindView(R2.id.iv_compatible_image)
    ImageView ivCompatibleImage;
    @BindView(R2.id.tv_show_test_result)
    TextView tvShowTestResult;
    @BindView(R2.id.tv_testing)
    TextView tvTesting;
    @BindView(R2.id.hbcc_c)
    Hob60BigCircleCooker hbccC;
    @BindView(R2.id.hbcc_b)
    Hob60SmallCircleCooker hbccB;
    @BindView(R2.id.hbcc_a)
    Hob60SmallCircleCooker hbccA;
    @BindView(R2.id.iv_test_cookware_1)
    ImageView ivTestCookware1;

    public static final int TFT_HOB_TYPE_60 = 0;
    public static final int TFT_HOB_TYPE_80 = 1;
    public static final int TFT_HOB_TYPE_90 = 2;

    private static final int power_on = 1;
    private static final int power_off = 0;
    private static final int cookerID_A = 1;
    private static final int cookerID_B = 2;
    private static final int cookerID_C = 3;

    private static final int Handle_Test_Cookware_Result = 1;
    private static final int HANDLE_RECOVER_CONNECTION_WITH_HOOD=0;

    private static final int Timer_Length = 11;  // 11 秒
    @BindView(R2.id.hrc90_a)
    Hob80RectangleCookerNew hrc90A;
    @BindView(R2.id.hbc90_b)
    Hob90CircleCooker hbc90B;
    @BindView(R2.id.hrc90_c)
    Hob80RectangleCookerNew hrc90C;
    @BindView(R2.id.hrc80_a)
    Hob80RectangleCookerNew hrc80A;
    @BindView(R2.id.hbc80_b)
    Hob80BigCircleCooker hbc80B;
    @BindView(R2.id.hbc80_c)
    Hob80SmallCircleCooker hbc80C;

    private int Current_CookerID = 0;
    private float mScaleZone = 0.85f;
    private Typeface typeface;

    private boolean mIs_No_Pan_Result = false; // 默认是无锅


    private boolean mTest_Cookware_Is_Working = false;

    private Disposable mDisposable;//定时器
    private int mTimer_Number = 0;

    private int mTheDotIndex = 0;
    private String mConnectionWithHood="";
    private Map<Integer, Bitmap> bitmapMap = new HashMap<>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLE_RECOVER_CONNECTION_WITH_HOOD:
                    SettingPreferencesUtil.saveStablishConnectionSwitchStatus(getActivity(), mConnectionWithHood);
                    GlobalVars.getInstance().setHandlePowerOffAllCooker(true);
                    break;








            }
        }
    };


    @Override
    public int initLyaout() {
        return R.layout.settingmodule_fragment_setting_test_cookware;
    }

    @Override
    public void initListener() {

    }

    @Override
    public void onStart() {
        super.onStart();
        registerEventBus();

    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        ResetData();
        SetFont();
        showHobTypeUI(GlobalVars.getInstance().getHobType());
        SetUI();
        ShowUI("E130");
        // registerEventBus();
        //   LogUtil.d("liang show testcookware~~~");
        HobTypeInit(GlobalVars.getInstance().getHobType());


    }

    private void SetUI() {
        if (ProductManager.PRODUCT_TYPE == ProductManager.Haier) {
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_test_cookware_haier);
            drawable.setBounds(0, 0, 53, 53);
            tvTitle.setCompoundDrawables(drawable, null, null, null);
        }
        hbccC.setScaleX(mScaleZone);
        hbccC.setScaleY(mScaleZone);

        hbccA.setScaleX(mScaleZone);
        hbccA.setScaleY(mScaleZone);

        hbccB.setScaleX(mScaleZone);
        hbccB.setScaleY(mScaleZone);
    }

    private void ShowUI(String uiName) {
        switch (uiName) {
            case "E130":
                tvStartNewTest.setVisibility(View.VISIBLE);

                tvContent.setVisibility(View.INVISIBLE);
                ivNextButton.setVisibility(View.INVISIBLE);

               /* hbccA.setVisibility(View.INVISIBLE);
                hbccB.setVisibility(View.INVISIBLE);
                hbccC.setVisibility(View.INVISIBLE);*/
                hideHobTypeUI();

                ivCompatibleImage.setVisibility(View.INVISIBLE);
                tvShowTestResult.setVisibility(View.INVISIBLE);
                tvTesting.setVisibility(View.INVISIBLE);
                break;
            case "E1301":

                tvStartNewTest.setVisibility(View.INVISIBLE);

                tvContent.setVisibility(View.VISIBLE);
                ivNextButton.setVisibility(View.VISIBLE);

              /*  hbccA.setVisibility(View.INVISIBLE);
                hbccB.setVisibility(View.INVISIBLE);
                hbccC.setVisibility(View.INVISIBLE);*/
                hideHobTypeUI();

                ivCompatibleImage.setVisibility(View.INVISIBLE);
                tvShowTestResult.setVisibility(View.INVISIBLE);
                tvTesting.setVisibility(View.INVISIBLE);

                break;
            case "E1302":
                tvStartNewTest.setVisibility(View.INVISIBLE);

                tvContent.setVisibility(View.INVISIBLE);
                ivNextButton.setVisibility(View.INVISIBLE);

           /*     hbccA.setVisibility(View.VISIBLE);
                hbccB.setVisibility(View.VISIBLE);
                hbccC.setVisibility(View.VISIBLE);*/
                showHobTypeUI(GlobalVars.getInstance().getHobType());

                ivCompatibleImage.setVisibility(View.INVISIBLE);
                tvShowTestResult.setVisibility(View.INVISIBLE);
                tvTesting.setVisibility(View.INVISIBLE);

                // tvTitle.setText("Select the Cooking Zone");
                tvTitle.setText(getResources().getString(R.string.settingmodule_fragment_test_cookware_title_select_the_cooking_zone));

                break;
            case "E1303":  // E1305  实际 是
                tvStartNewTest.setVisibility(View.INVISIBLE);

                tvContent.setVisibility(View.INVISIBLE);
                ivNextButton.setVisibility(View.INVISIBLE);

             /*   hbccA.setVisibility(View.INVISIBLE);
                hbccB.setVisibility(View.INVISIBLE);
                hbccC.setVisibility(View.INVISIBLE);*/
                hideHobTypeUI();

                ivCompatibleImage.setVisibility(View.INVISIBLE);
                tvShowTestResult.setVisibility(View.INVISIBLE);
                //  tvTesting.setVisibility(View.VISIBLE);
                tvTitle.setText(getResources().getString(R.string.settingmodule_setting_title_test_cookware));
                GlobalVars.getInstance().setHandlePowerOffAllCooker(false); // 主界面，不处理所有炉头关闭的操作，即出现定时圈圈
                mConnectionWithHood= SettingPreferencesUtil.getStablishConnectionSwitchStatus(getActivity());
                SettingPreferencesUtil.saveStablishConnectionSwitchStatus(getActivity(), CataSettingConstant.STABLISH_CONNECTION_SWITCH_STATUS_CLOSE);
                break;
            case "E1304":  // E1303 E1304 实际是
                tvStartNewTest.setVisibility(View.INVISIBLE);

                tvContent.setVisibility(View.INVISIBLE);
                ivNextButton.setVisibility(View.INVISIBLE);

             /*   hbccA.setVisibility(View.INVISIBLE);
                hbccB.setVisibility(View.INVISIBLE);
                hbccC.setVisibility(View.INVISIBLE);*/
                hideHobTypeUI();

                ivCompatibleImage.setVisibility(View.VISIBLE);
                tvShowTestResult.setVisibility(View.VISIBLE);
                tvTesting.setVisibility(View.INVISIBLE);
                tvTitle.setText(getResources().getString(R.string.settingmodule_setting_title_test_cookware));
                break;
        }

        switch (GlobalVars.getInstance().getCurrentLocale().toString().toLowerCase()) {
            case "da":
                ivNextButton.setImageBitmap(getBitmap(R.mipmap.test_cookware_next_da));
                break;
            case "de":
                ivNextButton.setImageBitmap(getBitmap(R.mipmap.test_cookware_next_de));
                break;
            case "el":
                ivNextButton.setImageBitmap(getBitmap(R.mipmap.test_cookware_next_el));
                break;
            case "es":
                ivNextButton.setImageBitmap(getBitmap(R.mipmap.test_cookware_next_es));
                break;
            case "fi":
                ivNextButton.setImageBitmap(getBitmap(R.mipmap.test_cookware_next_fi));
                break;
            case "fr":
                ivNextButton.setImageBitmap(getBitmap(R.mipmap.test_cookware_next_fr));
                break;
            case "hu":
                ivNextButton.setImageBitmap(getBitmap(R.mipmap.test_cookware_next_hu));
                break;
            case "it":
                ivNextButton.setImageBitmap(getBitmap(R.mipmap.test_cookware_next_it));
                break;
            case "ko":
                ivNextButton.setImageBitmap(getBitmap(R.mipmap.test_cookware_next_ko));
                break;
            case "nl":
                ivNextButton.setImageBitmap(getBitmap(R.mipmap.test_cookware_next_nl));
                break;
            case "no":
                ivNextButton.setImageBitmap(getBitmap(R.mipmap.test_cookware_next_no));
                break;
            case "pl":
                ivNextButton.setImageBitmap(getBitmap(R.mipmap.test_cookware_next_pl));
                break;
            case "pt":
                ivNextButton.setImageBitmap(getBitmap(R.mipmap.test_cookware_next_pt));
                break;
            case "ru":
                ivNextButton.setImageBitmap(getBitmap(R.mipmap.test_cookware_next_ru));
                break;
            case "sv":
                ivNextButton.setImageBitmap(getBitmap(R.mipmap.test_cookware_next_sv));
                break;
            case "vi":
                ivNextButton.setImageBitmap(getBitmap(R.mipmap.test_cookware_next_vi));
                break;
            case "zh":
                ivNextButton.setImageBitmap(getBitmap(R.mipmap.test_cookware_next_zh));
                break;
            default:
                ivNextButton.setImageBitmap(getBitmap(R.mipmap.test_cookware_next));
                break;
        }

        EventBus.getDefault().post(new TestCookwareSettingFragmentContentChanged(uiName));
    }


    @OnClick({R2.id.tv_title, R2.id.iv_next_button, R2.id.tv_start_new_test, R2.id.iv_compatible_image})
    public void onClick(View view) {
        if ((view.getId() == R.id.tv_title)) {
          //  EventBus.getDefault().post(new SettingDoBack(CataSettingConstant.do_back));
            if (mTest_Cookware_Is_Working) {
                EventBus.getDefault().post(new SettingTestCookDataEvent(power_off, Current_CookerID));  // 关掉 测试的 炉头
                mTest_Cookware_Is_Working = false;
                SettingPreferencesUtil.saveStablishConnectionSwitchStatus(getActivity(), mConnectionWithHood);
                GlobalVars.getInstance().setHandlePowerOffAllCooker(true);
              //  handler.sendEmptyMessageDelayed(HANDLE_RECOVER_CONNECTION_WITH_HOOD, 1000);

                LogUtil.d("liang hidden the testcookware fragment~~~~~~~");
            }

            doStopTimerCountDown();
            ResetData();
            EventBus.getDefault().post(new WhichBannerWillBeShow(CataSettingConstant.START_A_NEW_TEST));
            EventBus.getDefault().post(new SettingDoBack(CataSettingConstant.do_back));
            unregisterEventBus();
        } else if (view.getId() == R.id.iv_next_button) {
            EventBus.getDefault().post(new WhichBannerWillBeShow(CataSettingConstant.SELECT_THE_COOKING_ZONE));
            ShowUI("E1302");
            doStartTimerCountDown();
        } else if (view.getId() == R.id.tv_start_new_test) {
            EventBus.getDefault().post(new WhichBannerWillBeShow(CataSettingConstant.PLACE_THE_COOKER));
            ShowUI("E1301");

        } else if (view.getId() == R.id.iv_compatible_image) {
            EventBus.getDefault().post(new WhichBannerWillBeShow(CataSettingConstant.START_A_NEW_TEST));
            ShowUI("E130");
        }
    }


    private void SetFont() {

        typeface = GlobalVars.getInstance().getDefaultFontRegular();
        tvTitle.setTypeface(typeface);
        tvContent.setTypeface(typeface);
        tvShowTestResult.setTypeface(typeface);
        tvTesting.setTypeface(typeface);
        tvStartNewTest.setTypeface(typeface);
    }

    private Bitmap getBitmap(int source) {
        if (bitmapMap.containsKey(source)) {
            return bitmapMap.get(source);
        }

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), source);
        bitmapMap.put(source, bitmap);
        return bitmap;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GetTestCookwareResult event) {
        if (event.getOrder() == 1) {
            mIs_No_Pan_Result = true;
        } else if (event.getOrder() == 0) {
            mIs_No_Pan_Result = false;
        }
        LogUtil.d("liang get test cookware result~~~  " + mIs_No_Pan_Result);
    }

    private void ShowTheTestCookwareResult() {
        EventBus.getDefault().post(new SettingTestCookDataEvent(power_off, Current_CookerID));
        ShowUI("E1304");
        if (mIs_No_Pan_Result) {
            tvShowTestResult.setText(getResources().getString(R.string.settingmodule_fragment_test_cookware_result_is_compatible));
            ivCompatibleImage.setImageBitmap(getBitmap(R.mipmap.test_cookware_ok));
            EventBus.getDefault().post(new WhichBannerWillBeShow(CataSettingConstant.COOKWARE_IS_COMPATIBLE));
        } else {
            tvShowTestResult.setText(getResources().getString(R.string.settingmodule_fragment_test_cookware_result_is_not_compatible));
            ivCompatibleImage.setImageBitmap(getBitmap(R.mipmap.test_cookware_not));
            EventBus.getDefault().post(new WhichBannerWillBeShow(CataSettingConstant.COOKWARE_IS_NOT_COMPATIBLE));
        }

        //EventBus.getDefault().post(new SettingTestCookDataEvent(power_off, Current_CookerID));
        mTest_Cookware_Is_Working = false;
     //   mConnectionWithHood= SettingPreferencesUtil.getStablishConnectionSwitchStatus(getActivity());
      //  SettingPreferencesUtil.saveStablishConnectionSwitchStatus(getActivity(), mConnectionWithHood);
        handler.sendEmptyMessageDelayed(HANDLE_RECOVER_CONNECTION_WITH_HOOD, 1000);
    }

    private void ResetData() {
        mTest_Cookware_Is_Working = false;
        mTimer_Number = 0;
        mIs_No_Pan_Result = false;
        ivTestCookware1.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
           /* if(mTest_Cookware_Is_Working){
                EventBus.getDefault().post(new SettingTestCookDataEvent(power_off,Current_CookerID));
                mTest_Cookware_Is_Working=false;
                LogUtil.d("liang hidden the testcookware fragment~~~~~~~");
            }*/
            unregisterEventBus();
        } else {
           /* ShowUI("E130");
            LogUtil.d("liang show the testcookware fragment~~~~~~~");*/
            registerEventBus();
        }

    }

    private void changeTheDotImage(int index) {

        switch (index) {
            case 1:
            case 4:
            case 7:
            case 10:
                ivTestCookware1.setImageBitmap(getBitmap(R.mipmap.test_cookware_1_new));
                ivTestCookware1.setVisibility(View.VISIBLE);
                break;
            case 2:
            case 5:
            case 8:
            case 11:
                ivTestCookware1.setImageBitmap(getBitmap(R.mipmap.test_cookware_2_new));

                break;
            case 3:
            case 6:
            case 9:
            case 12:
                ivTestCookware1.setImageBitmap(getBitmap(R.mipmap.test_cookware_3_new));
                break;
        }
    }

    private void doStartTimerCountDown() {//定时器倒计时
        //  doStopTimerCountDown();
        mDisposable = Observable
                //.interval(0,1, TimeUnit.SECONDS)
                .interval(1, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        LogUtil.d("Enter:: countdown----->" + aLong);
                        //LogUtil.d("hour--->" + remainHourValue + "----minute--->" + remainMinuteValue + "---->" + remainSecondValue);

                        if (mTest_Cookware_Is_Working) {
                            mTimer_Number++;
                            EventBus.getDefault().post(new SendOrderForFirstSwitchOn(CataSettingConstant.FlashTheTimeOfTouchUI));
                            changeTheDotImage(mTimer_Number);
                            if (mTimer_Number >= Timer_Length) {
                                ShowTheTestCookwareResult();
                                doStopTimerCountDown();
                                ResetData();
                                //mTimer_Number=0;
                            }

                        }


                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribe();
    }

    private void doStopTimerCountDown() {
        if (mDisposable != null) {
            if (!mDisposable.isDisposed()) {
                mDisposable.dispose();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SendOrderToTestCookwareFragment event) {
        if (event.getOrder() == CataSettingConstant.OrderForTestCookwareFragment) {
            if (mTest_Cookware_Is_Working) {
                EventBus.getDefault().post(new SettingTestCookDataEvent(power_off, Current_CookerID));
                mTest_Cookware_Is_Working = false;

          //      LogUtil.d("liang hidden the testcookware fragment~~~~~~~");
            }
            unregisterEventBus();
            doStopTimerCountDown();
            ResetData();
        }
    }

    private void hideHobTypeUI(){
        hbccA.setVisibility(View.INVISIBLE);
        hbccB.setVisibility(View.INVISIBLE);
        hbccC.setVisibility(View.INVISIBLE);


        hrc80A.setVisibility(View.INVISIBLE);
        hbc80B.setVisibility(View.INVISIBLE);
        hbc80C.setVisibility(View.INVISIBLE);

        hrc90A.setVisibility(View.INVISIBLE);
        hbc90B.setVisibility(View.INVISIBLE);
        hrc90C.setVisibility(View.INVISIBLE);
    }

    private void HobTypeInit(int type){
        switch (type){
            case TFT_HOB_TYPE_60:
                hbccC.setOnHobCircleCookerListener(new HobCircleCookerContract.OnHobCircleCookerListener() {
                    @Override
                    public void onCookerPowerOff() {
                        //   EventBus.getDefault().post(new SettingTestCookDataEvent(power_off,cookerID_C));
                    }

                    @Override
                    public void onCookerPowerOn() {
                        if (mTest_Cookware_Is_Working) {

                        } else {
                            ShowUI("E1303");
                            EventBus.getDefault().post(new SettingTestCookDataEvent(power_on, cookerID_C));

                            //   doStartTimerCountDown();
                            Current_CookerID = cookerID_C;
                            mTest_Cookware_Is_Working = true;
                        }

                    }

                    @Override
                    public void onSetGear() {

                    }

                    @Override
                    public void onSetTimer() {

                    }

                    @Override
                    public void onReadyToCook() {

                    }

                    @Override
                    public void onRequestAddTenMinute() {

                    }

                    @Override
                    public void onRequestKeepWarm() {

                    }
                });

                hbccA.setOnHobCircleCookerListener(new HobCircleCookerContract.OnHobCircleCookerListener() {
                    @Override
                    public void onCookerPowerOff() {
                        //  EventBus.getDefault().post(new SettingTestCookDataEvent(power_off,cookerID_A));
                    }

                    @Override
                    public void onCookerPowerOn() {
                        if (mTest_Cookware_Is_Working) {

                        } else {
                            ShowUI("E1303");
                            EventBus.getDefault().post(new SettingTestCookDataEvent(power_on, cookerID_A));

                            //  doStartTimerCountDown();
                            Current_CookerID = cookerID_A;
                            mTest_Cookware_Is_Working = true;
                        }

                    }

                    @Override
                    public void onSetGear() {

                    }

                    @Override
                    public void onSetTimer() {

                    }

                    @Override
                    public void onReadyToCook() {

                    }

                    @Override
                    public void onRequestAddTenMinute() {

                    }

                    @Override
                    public void onRequestKeepWarm() {

                    }
                });

                hbccB.setOnHobCircleCookerListener(new HobCircleCookerContract.OnHobCircleCookerListener() {
                    @Override
                    public void onCookerPowerOff() {
                        //  EventBus.getDefault().post(new SettingTestCookDataEvent(power_off,cookerID_B));
                    }

                    @Override
                    public void onCookerPowerOn() {
                        if (mTest_Cookware_Is_Working) {

                        } else {
                            ShowUI("E1303");
                            EventBus.getDefault().post(new SettingTestCookDataEvent(power_on, cookerID_B));

                            //  doStartTimerCountDown();
                            Current_CookerID = cookerID_B;
                            mTest_Cookware_Is_Working = true;
                        }
                    }

                    @Override
                    public void onSetGear() {

                    }

                    @Override
                    public void onSetTimer() {

                    }

                    @Override
                    public void onReadyToCook() {

                    }

                    @Override
                    public void onRequestAddTenMinute() {

                    }

                    @Override
                    public void onRequestKeepWarm() {

                    }
                });

                break;
            case TFT_HOB_TYPE_80:
                hbc80B.setOnHobCircleCookerListener(new HobCircleCookerContract.OnHobCircleCookerListener() {
                    @Override
                    public void onCookerPowerOff() {

                     //   Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PAUSE, SoundUtil.SOUND_ID_ALARM, " + soundType + ")");
                    }

                    @Override
                    public void onCookerPowerOn() {

                        if(mTest_Cookware_Is_Working) {

                        }else {
                            ShowUI("E1303");
                            EventBus.getDefault().post(new SettingTestCookDataEvent(power_on, hbc80B.getCookerID()));

                            //  doStartTimerCountDown();
                            Current_CookerID = hbc80B.getCookerID();
                            mTest_Cookware_Is_Working = true;
                        }

                    }


                    @Override
                    public void onSetGear() {

                    }

                    @Override
                    public void onSetTimer() {

                    }

                    @Override
                    public void onReadyToCook() {

                    }

                    @Override
                    public void onRequestAddTenMinute() {

                    }

                    @Override
                    public void onRequestKeepWarm() {

                    }
                });

                hbc80C.setOnHobCircleCookerListener(new HobCircleCookerContract.OnHobCircleCookerListener() {
                    @Override
                    public void onCookerPowerOff() {

                       // Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PAUSE, SoundUtil.SOUND_ID_ALARM, " + soundType + ")");
                    }

                    @Override
                    public void onCookerPowerOn() {

                        if(mTest_Cookware_Is_Working) {

                        }else {
                            ShowUI("E1303");
                            EventBus.getDefault().post(new SettingTestCookDataEvent(power_on, hbc80C.getCookerID()));
                            //  doStartTimerCountDown();
                            Current_CookerID = hbc80C.getCookerID();
                            mTest_Cookware_Is_Working = true;
                        }

                    }

                    @Override
                    public void onSetGear() {

                    }

                    @Override
                    public void onSetTimer() {

                    }

                    @Override
                    public void onReadyToCook() {

                    }

                    @Override
                    public void onRequestAddTenMinute() {

                    }

                    @Override
                    public void onRequestKeepWarm() {

                    }
                });

                hrc80A.setOnHobRectangleCookerListener(new HobRectangleCookerContract.OnHobRectangleCookerListener() {
                    @Override
                    public void onCookerPowerOff(int cookerID) {

                       // Logger.getInstance().d("new SoundEvent(SoundEvent.SOUND_ACTION_PAUSE, SoundUtil.SOUND_ID_ALARM, " + soundType + ")");
                    }

                    @Override
                    public void onCookerPowerOn(int cookerID) {

                        if(mTest_Cookware_Is_Working) {

                        }else {
                            ShowUI("E1303");
                            EventBus.getDefault().post(new SettingTestCookDataEvent(power_on, cookerID));

                            //  doStartTimerCountDown();
                            Current_CookerID = cookerID;
                            mTest_Cookware_Is_Working = true;
                        }

                    }

                    @Override
                    public void onSetGear(int cookerID) {

                    }

                    @Override
                    public void onSetTimer(int cookerID) {

                    }

                    @Override
                    public void onReadyToCook(int cookerID) {

                    }

                    @Override
                    public void onRequestAddTenMinute(int cookerID) {

                    }

                    @Override
                    public void onRequestKeepWarm(int cookerID) {

                    }
                });


                break;
            case TFT_HOB_TYPE_90:
                hbc90B.setOnHobCircleCookerListener(new HobCircleCookerContract.OnHobCircleCookerListener() {
                    @Override
                    public void onCookerPowerOff() {

                    }

                    @Override
                    public void onCookerPowerOn() {
                       if(mTest_Cookware_Is_Working) {

                       }else {
                           ShowUI("E1303");
                           EventBus.getDefault().post(new SettingTestCookDataEvent(power_on, hbc90B.getCookerID()));

                           //  doStartTimerCountDown();
                           Current_CookerID = hbc90B.getCookerID();
                           mTest_Cookware_Is_Working = true;
                       }

                    }

                    @Override
                    public void onSetGear() {

                    }

                    @Override
                    public void onSetTimer() {

                    }

                    @Override
                    public void onReadyToCook() {

                    }

                    @Override
                    public void onRequestAddTenMinute() {

                    }

                    @Override
                    public void onRequestKeepWarm() {

                    }
                });

                hrc90A.setOnHobRectangleCookerListener(new HobRectangleCookerContract.OnHobRectangleCookerListener() {
                    @Override
                    public void onCookerPowerOff(int cookerID) {

                    }

                    @Override
                    public void onCookerPowerOn(int cookerID) {
                        if(mTest_Cookware_Is_Working) {

                        }else {
                            ShowUI("E1303");
                            EventBus.getDefault().post(new SettingTestCookDataEvent(power_on, cookerID));

                            //  doStartTimerCountDown();
                            Current_CookerID = cookerID;
                            mTest_Cookware_Is_Working = true;
                        }
                    }

                    @Override
                    public void onSetGear(int cookerID) {

                    }

                    @Override
                    public void onSetTimer(int cookerID) {

                    }

                    @Override
                    public void onReadyToCook(int cookerID) {

                    }

                    @Override
                    public void onRequestAddTenMinute(int cookerID) {

                    }

                    @Override
                    public void onRequestKeepWarm(int cookerID) {

                    }
                });

                hrc90C.setOnHobRectangleCookerListener(new HobRectangleCookerContract.OnHobRectangleCookerListener() {
                    @Override
                    public void onCookerPowerOff(int cookerID) {


                    }

                    @Override
                    public void onCookerPowerOn(int cookerID) {
                        if(mTest_Cookware_Is_Working) {

                        }else {
                            ShowUI("E1303");
                            EventBus.getDefault().post(new SettingTestCookDataEvent(power_on, cookerID));

                            //  doStartTimerCountDown();
                            Current_CookerID = cookerID;
                            mTest_Cookware_Is_Working = true;
                        }
                    }

                    @Override
                    public void onSetGear(int cookerID) {

                    }

                    @Override
                    public void onSetTimer(int cookerID) {

                    }

                    @Override
                    public void onReadyToCook(int cookerID) {

                    }

                    @Override
                    public void onRequestAddTenMinute(int cookerID) {

                    }

                    @Override
                    public void onRequestKeepWarm(int cookerID) {

                    }
                });
                break;
        }
    }

    private void showHobTypeUI(int type) {
        switch (type) {
            case TFT_HOB_TYPE_60:

                hbccA.setVisibility(View.VISIBLE);
                hbccB.setVisibility(View.VISIBLE);
                hbccC.setVisibility(View.VISIBLE);

                hbccC.setScaleX(mScaleZone);
                hbccC.setScaleY(mScaleZone);

                hbccA.setScaleX(mScaleZone);
                hbccA.setScaleY(mScaleZone);

                hbccB.setScaleX(mScaleZone);
                hbccB.setScaleY(mScaleZone);


                hrc80A.setVisibility(View.INVISIBLE);
                hbc80B.setVisibility(View.INVISIBLE);
                hbc80C.setVisibility(View.INVISIBLE);

                hrc90A.setVisibility(View.INVISIBLE);
                hbc90B.setVisibility(View.INVISIBLE);
                hrc90C.setVisibility(View.INVISIBLE);

                break;
            case TFT_HOB_TYPE_80:
                hbccA.setVisibility(View.INVISIBLE);
                hbccB.setVisibility(View.INVISIBLE);
                hbccC.setVisibility(View.INVISIBLE);


                hrc80A.setVisibility(View.VISIBLE);
                hbc80B.setVisibility(View.VISIBLE);
                hbc80C.setVisibility(View.VISIBLE);

                hrc80A.setScaleX(mScaleZone);
                hrc80A.setScaleY(mScaleZone);

              /*  hbc80B.setScaleX(mScaleZone);
                hbc80B.setScaleY(mScaleZone);

                hbc80C.setScaleX(mScaleZone);
                hbc80C.setScaleY(mScaleZone);*/

                hrc90A.setVisibility(View.INVISIBLE);
                hbc90B.setVisibility(View.INVISIBLE);
                hrc90C.setVisibility(View.INVISIBLE);
                break;
            case TFT_HOB_TYPE_90:

                hbccA.setVisibility(View.INVISIBLE);
                hbccB.setVisibility(View.INVISIBLE);
                hbccC.setVisibility(View.INVISIBLE);

                hrc80A.setVisibility(View.INVISIBLE);
                hbc80B.setVisibility(View.INVISIBLE);
                hbc80C.setVisibility(View.INVISIBLE);

                hrc90A.setVisibility(View.VISIBLE);
                hbc90B.setVisibility(View.VISIBLE);
                hrc90C.setVisibility(View.VISIBLE);

                hrc90A.setScaleX(mScaleZone);
                hrc90A.setScaleY(mScaleZone);

                hbc90B.setScaleX(mScaleZone);
                hbc90B.setScaleY(mScaleZone);

                hrc90C.setScaleX(mScaleZone);
                hrc90C.setScaleY(mScaleZone);
                break;

        }

    }


}
