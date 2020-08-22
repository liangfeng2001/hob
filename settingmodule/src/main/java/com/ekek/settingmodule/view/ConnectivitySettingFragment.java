package com.ekek.settingmodule.view;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.commonmodule.utils.Logger;
import com.ekek.settingmodule.R;
import com.ekek.settingmodule.R2;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.database.SettingPreferencesUtil;
import com.ekek.settingmodule.events.ConnectivitySettingEvent;
import com.ekek.settingmodule.events.EKEKSocketEvent;
import com.ekek.settingmodule.model.SettingDoBack;
import com.ekek.viewmodule.common.BluetoothHelper;
import com.ekek.viewmodule.common.NoTextSwitch;
import com.ekek.viewmodule.product.ProductManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ConnectivitySettingFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener{
    private static final int HANDLER_CHECK_BLUETOOTH_STATE = 0;
    private static final long CHECK_BLUETOOTH_STATE_DELAY = 1000;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.sw_bluetooth)
    NoTextSwitch swBluetooth;
    Unbinder unbinder;
    @BindView(R2.id.title_bluetooth)
    TextView titleBluetooth;
    @BindView(R2.id.ct_smart_phone)
    CheckedTextView ctSmartPhone;
    @BindView(R2.id.ct_temperature_sensor)
    CheckedTextView ctTemperatureSensor;
    @BindView(R2.id.tv_bluetooth_address)
    TextView tvBluetoothAddress;

    private Typeface typeface;
    private BluetoothAdapter bluetoothAdapter;
    private int currentState;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
            setBluetoothStateInt(state);
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_CHECK_BLUETOOTH_STATE:
                    if (bluetoothAdapter == null) bluetoothAdapter = getBluetoothAdapter();
                    if (bluetoothAdapter != null) {
                        setBluetoothStateInt(bluetoothAdapter.getState());
                    }
                    handler.sendEmptyMessageDelayed(HANDLER_CHECK_BLUETOOTH_STATE,CHECK_BLUETOOTH_STATE_DELAY);
                    break;

            }
        }
    };

    @Override
    public int initLyaout() {
        return R.layout.settingmodule_fragment_connectivity_setting;
    }

    @Override
    public void initListener() {

    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        SetFont();
        SetUI();
      //  init();
    }

    @Override
    public void onStart() {
        super.onStart();
        init();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (handler.hasMessages(HANDLER_CHECK_BLUETOOTH_STATE)) handler.removeMessages(HANDLER_CHECK_BLUETOOTH_STATE);
        getActivity().unregisterReceiver(mReceiver);
    }

    private void registerReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_OFF");
        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_ON");
        context.registerReceiver(mReceiver, intentFilter);

    }

    private void SetUI() {
        if (ProductManager.PRODUCT_TYPE == ProductManager.Haier) {
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_connectivity_haier);
            drawable.setBounds(0, 0, 53, 53);
            tvTitle.setCompoundDrawables(drawable, null, null, null);
        }
    }

    private void SetFont() {
        typeface = GlobalVars.getInstance().getDefaultFontRegular();
        tvTitle.setTypeface(typeface);
        titleBluetooth.setTypeface(typeface);
        ctSmartPhone.setTypeface(typeface);
        ctTemperatureSensor.setTypeface(typeface);
        tvBluetoothAddress.setTypeface(typeface);
    }

    private void init() {
        initBT();

        boolean bluethoothSwitchStatus = SettingPreferencesUtil.getBluetoothSwitchStatus(getActivity()).equals(CataSettingConstant.BLUETOOTH_SWITCH_STATUS_OPEN) ? true : false;

        boolean bluetoothStyle = SettingPreferencesUtil.getBluetoothStyle(getContext() ).equals(CataSettingConstant.BLUETOOTH_SENSOR_STYLE)? true :false;

        if (bluethoothSwitchStatus) {
            swBluetooth.setChecked(true);
            ctSmartPhone.setVisibility(View.VISIBLE);
            ctTemperatureSensor.setVisibility(View.VISIBLE);
        } else {
            swBluetooth.setChecked(false);
            ctSmartPhone.setVisibility(View.INVISIBLE);
            ctTemperatureSensor.setVisibility(View.INVISIBLE);
        }

        if(bluetoothStyle){
            ctSmartPhone.setChecked(false );
            ctTemperatureSensor.setChecked(true );
        }else{
            ctSmartPhone.setChecked(true );
            ctTemperatureSensor.setChecked(false );
        }

        if (bluethoothSwitchStatus && !bluetoothStyle) {
            tvBluetoothAddress.setVisibility(View.VISIBLE);
            GlobalVars.getInstance().setWaitingToUpdate(true);
        } else {
            tvBluetoothAddress.setVisibility(View.INVISIBLE);
            GlobalVars.getInstance().setWaitingToUpdate(false);
        }

        swBluetooth.setOnCheckedChangeListener(this);
        tvBluetoothAddress.setText(BluetoothHelper.getInstance(mContext).getBluetoothAddress());
    }

    private void initBT() {
        registerReceiver(getActivity());
        android.bluetooth.BluetoothManager bluetoothManager = (android.bluetooth.BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
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

    @OnClick({R2.id.tv_title,R2.id.ct_temperature_sensor,R2.id.ct_smart_phone})
    public void onClick(View view ) {
        int id=view.getId();
        if(id==R.id.tv_title){
            EventBus.getDefault().post(new SettingDoBack(CataSettingConstant.do_back));
        }else if(id==R.id.ct_temperature_sensor){
            ctTemperatureSensor.setChecked(true);
            SettingPreferencesUtil.saveBluetoothStyle(getActivity(),CataSettingConstant.BLUETOOTH_SENSOR_STYLE);
            ctSmartPhone.setChecked(false);
            tvBluetoothAddress.setVisibility(View.INVISIBLE);
            GlobalVars.getInstance().setWaitingToUpdate(false);
            notifyBluetoothOptionsChanged(ConnectivitySettingEvent.BLUETOOTH_OPTIONS_TEMPERTURE_SENSOR);
        }else if(id==R.id.ct_smart_phone){
            ctSmartPhone.setChecked(true);
            tvBluetoothAddress.setVisibility(View.VISIBLE);
            ctTemperatureSensor.setChecked(false);
            GlobalVars.getInstance().setWaitingToUpdate(true);
            SettingPreferencesUtil.saveBluetoothStyle(getActivity(),CataSettingConstant.BLUETOOTH_PHONE_STYLE);
            notifyBluetoothOptionsChanged(ConnectivitySettingEvent.BLUETOOTH_OPTIONS_SMART_PHONE);
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //LogUtil.d("bt state---->" + bluetoothAdapter.getState());
        if(isChecked){
            swBluetooth.setEnabled(false);
            swBluetooth.setOnCheckedChangeListener(null);
            //swBluetooth.setChecked(true);
            Logger.getInstance().i("Bluetooth is turning on");
            SettingPreferencesUtil.saveBluetoothSwitchStatus(getActivity(), CataSettingConstant.BLUETOOTH_SWITCH_STATUS_OPEN);
            LogUtil.d("Enter:: ble open");
            setBTEnable(true);
        }else {
            swBluetooth.setEnabled(false);
            swBluetooth.setOnCheckedChangeListener(null);
            //swBluetooth.setChecked(false);
            Logger.getInstance().i("Bluetooth is turning off");
            SettingPreferencesUtil.saveBluetoothSwitchStatus(getActivity(), CataSettingConstant.BLUETOOTH_SWITCH_STATUS_CLOSE);

            setBTEnable(false);
        }
    }

    private void setBTEnable(boolean isEnable) {
        /*if (isEnable) bluetoothAdapter.enable();
        else bluetoothAdapter.disable();*/

        if (bluetoothAdapter == null) bluetoothAdapter = getBluetoothAdapter();
        if (bluetoothAdapter != null) {
            boolean success = isEnable
                    ? bluetoothAdapter.enable()
                    : bluetoothAdapter.disable();

            if (success) {
                setBluetoothStateInt(isEnable
                        ? BluetoothAdapter.STATE_TURNING_ON
                        : BluetoothAdapter.STATE_TURNING_OFF);
            }
            //handler.sendEmptyMessage(HANDLER_CHECK_BLUETOOTH_STATE);
            handler.sendEmptyMessageDelayed(HANDLER_CHECK_BLUETOOTH_STATE,1500);
        }



    }

    synchronized void setBluetoothStateInt(int state) {
        LogUtil.e("bluetooth ::---->" + state);
        currentState = state;
        switch (state) {
            case BluetoothAdapter.STATE_TURNING_ON:
                LogUtil.d("bluetooth :: STATE_TURNING_ON");

                break;
            case BluetoothAdapter.STATE_ON:
                LogUtil.d("bluetooth :: STATE_ON");
                handler.removeMessages(HANDLER_CHECK_BLUETOOTH_STATE);
                SettingPreferencesUtil.saveBluetoothSwitchStatus(getActivity(), CataSettingConstant.BLUETOOTH_SWITCH_STATUS_OPEN);
                if (ctSmartPhone != null) ctSmartPhone.setVisibility(View.VISIBLE);
                if (ctTemperatureSensor != null) {
                    ctTemperatureSensor.setVisibility(View.VISIBLE);
                    if (ctTemperatureSensor.isChecked()) {//连接温度计
                        tvBluetoothAddress.setVisibility(View.INVISIBLE);
                        GlobalVars.getInstance().setWaitingToUpdate(false);
                        //notifyBluetoothOptionsChanged(ConnectivitySettingEvent.BLUETOOTH_OPTIONS_TEMPERTURE_SENSOR);
                    }else {//连接手机
                        tvBluetoothAddress.setVisibility(View.VISIBLE);
                        GlobalVars.getInstance().setWaitingToUpdate(true);
                        //notifyBluetoothOptionsChanged(ConnectivitySettingEvent.BLUETOOTH_OPTIONS_SMART_PHONE);
                    }
                }


                swBluetooth.setChecked(true);
                swBluetooth.setEnabled(true);
                swBluetooth.setOnCheckedChangeListener(ConnectivitySettingFragment.this);
                break;
            case BluetoothAdapter.STATE_TURNING_OFF:
                LogUtil.d("bluetooth :: STATE_TURNING_OFF");

                break;
            case BluetoothAdapter.STATE_OFF:
                LogUtil.d("bluetooth :: STATE_OFF");
                handler.removeMessages(HANDLER_CHECK_BLUETOOTH_STATE);
                SettingPreferencesUtil.saveBluetoothSwitchStatus(getActivity(), CataSettingConstant.BLUETOOTH_SWITCH_STATUS_CLOSE);
                if (ctSmartPhone != null) ctSmartPhone.setVisibility(View.INVISIBLE);
                if (ctTemperatureSensor != null) ctTemperatureSensor.setVisibility(View.INVISIBLE);
                if (tvBluetoothAddress != null) tvBluetoothAddress.setVisibility(View.INVISIBLE);
                GlobalVars.getInstance().setWaitingToUpdate(false);
                //notifyBluetoothOptionsChanged(ConnectivitySettingEvent.BLUETOOTH_OPTIONS_DISABLE);

                //清除系统蓝牙配置数据，避免bluetooth share crash
                List<String> commandList = new ArrayList<>();
                commandList.add("rm /data/misc/bluedroid/bt_config.xml");
                commandList.add("rm /data/misc/bluedroid/bt_config.old");
                EventBus.getDefault().post(new EKEKSocketEvent(commandList));

                swBluetooth.setChecked(false);
                swBluetooth.setEnabled(true);
                swBluetooth.setOnCheckedChangeListener(ConnectivitySettingFragment.this);
                break;
        }
    }

    private BluetoothAdapter getBluetoothAdapter() {
        final android.bluetooth.BluetoothManager bluetoothManager = (android.bluetooth.BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        return bluetoothManager.getAdapter();
    }


    private void notifyBluetoothOptionsChanged(int mode) {
        LogUtil.d("Enter:: notifyBluetoothOptionsChanged---mode--->" + mode);
        EventBus.getDefault().post(new ConnectivitySettingEvent(mode));
    }



}
