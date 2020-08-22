package com.ekek.tfthobmodule.bluetooth;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.commonmodule.utils.Logger;
import com.ekek.hardwaremodule.utils.DataParseUtil;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.database.SettingPreferencesUtil;
import com.ekek.tfthobmodule.constants.TFTHobConstant;
import com.ekek.viewmodule.common.BluetoothHelper;
import com.inuker.bluetooth.library.Code;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;

import java.util.Objects;
import java.util.UUID;

public class BluetoothManager {
    //Bluetooth options
    /**
     * 关闭蓝牙
     * */
    public static final int BLUETOOTH_OPTIONS_DISABLE= 0;
    /**
     * 打开蓝牙温度计连接模式
     * */
    public static final int BLUETOOTH_OPTIONS_TEMPERTURE_SENSOR = 1;
    /**
     * 打开连接手机模式
     * */
    public static final int BLUETOOTH_OPTIONS_SMART_PHONE = 2;

    public static final int BLUETOOTH_OPTIONS_ENABLE= 3;

    private UUID UUID_SERVICE = UUID.fromString("66c926b2-189b-4a02-97e7-38ae7adce96d");
    private UUID UUID_MEDIUM_CHARACTER = UUID.fromString("97df461c-ae66-4284-8510-4c0b992dac72");
    private UUID UUID_READ_LEVEL = UUID.fromString("a8d3059e-9f5a-41c9-a865-f9247af44594");
    private UUID UUID_BATTERY_STATUS = UUID.fromString("d1250404-d897-4f21-b7d5-1367e0faeeb8");
    private UUID UUID_BATTERY_STATUS_CHARGING = UUID.fromString("41ca39ce-91b3-41e9-9542-d58a71ac7b9e");

    private static final String TEMP_NAME = "Sensor Slave";
    public static final int BLE_CONNECT_STATUS_UNCONNECT = 0;
    public static final int BLE_CONNECT_STATUS_CONNECTTING = 1;
    public static final int BLE_CONNECT_STATUS_CONNECTTED = 2;
    public static final int BLE_CONNECT_STATUS_SEARCHING = 3;
    private static final int WORK_PROGRESS_START_HANDSHAKE_SEND_1 = 0;
    private static final int WORK_PROGRESS_START_HANDSHAKE_SEND_2 = 1;
    private static final int WORK_PROGRESS_START_HANDSHAKE_SEND_3 = 2;
    private static final int WORK_PROGRESS_NORMAL_SEND_1 = 3;
    private static final int WORK_PROGRESS_NORMAL_SEND_2 = 4;
    private static final int WORK_PROGRESS_NORMAL_SEND_3 = 5;
    private static final int WORK_PROGRESS_NORMAL_SEND_4 = 51;
    private static final int WORK_PROGRESS_NORMAL_SEND_DISCONECT = 6;
    private static final int WORK_PROGRESS_NORMAL_SEND_DISCONECT_MANUAL = 61;
    private static final int WORK_PROGRESS_NORMAL_DO_READ_TEMP = 7;
    private static final int WORK_PROGRESS_NORMAL_DO_READ_BATTERY = 8;
    private static final int WORK_PROGRESS_NORMAL_DO_READ_BATTERY_CHARGING_STATUS = 9;
    private static final int HANDLER_UPDATE_UI = 0;
    private static final int HANDLER_START_WORK_FLOW = 1;
    private static final int HANDLER_START_DISCOVERY = 2;
    private static final int HANDLER_DO_LE_SCAN_CALLBACK = 3;

    public static final int BLUETOOTH_TEMP_SENSOR_STOP_TYPE_PASSIVE = 0;//蓝牙温度计被动断开
    public static final int BLUETOOTH_TEMP_SENSOR_STOP_TYPE_INITIATIVE = 1;//蓝牙温度计主动断开

    private static final int START_WORK_FLOW_DELAY_TIME = 1000 * 1;//1000
    private static final int START_DISCOVERY_DELAY_TIME = 1000 * 1;
    private static final int DISCOVERY_COUNT = 3;
    private int connectStatus = BLE_CONNECT_STATUS_UNCONNECT;
    private int workProgress = WORK_PROGRESS_START_HANDSHAKE_SEND_1;

    private static long BLUETOOTH_WORK_FLOW_BUSY_DELAY_TIME = 1000;
    private static long BLUETOOTH_READ_BATTERY_DELAY_TIME = 1000;

    private int handShakeCounter = 0;
    private String macStr;
    private byte cmd = 0x01;
    private int tempValue = 1000;
    private int batteryValue = 1000;
    private int batteryChargeState;
    private Context context;

    private BluetoothAdapter bluetoothAdapter;
    private boolean isManualDisconnect = false;
    private int discoveryCnt = 0;
    private int btMode;
    private OnBluetoothManagerListener listener;
    private boolean leScanStarted = false;
    private BluetoothHelper bluetoothHelper;

    private boolean bluetoothIsTurningOn;
    private boolean bluetoothIsTurningOff;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_UPDATE_UI:
                    //updateMsgUI();
                    break;
                case HANDLER_START_WORK_FLOW:
                    LogUtil.d("Enter:: samhung  HANDLER_START_WORK_FLOW");
                    workProgress = WORK_PROGRESS_NORMAL_SEND_1;
                    cmd = 0x01;
                    doWrite(UUID_SERVICE,UUID_READ_LEVEL,getCmdData());
                    break;
                case HANDLER_START_DISCOVERY:
                    doDiscovery();
                    break;
                case HANDLER_DO_LE_SCAN_CALLBACK:
                    BluetoothDevice device = (BluetoothDevice) msg.obj;
                    int rssi = msg.arg1;
                    String deviceName = device.getName();
                    LogUtil.d("device name ---->" + device.getName() + "----rssi---->" + rssi + "----connectStatus---->" + connectStatus);
                    if (deviceName != null && deviceName.equals(TEMP_NAME) && connectStatus != BLE_CONNECT_STATUS_CONNECTTED && connectStatus != BLE_CONNECT_STATUS_CONNECTTING ) {
                        connectStatus = BLE_CONNECT_STATUS_CONNECTTING;
                        stopScanTempSensor(false);
                        discoveryCnt = 0;
                        macStr = device.getAddress();
                        doConnect(macStr);

                    }
                    break;

            }
        }
    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
            switch (state) {
                case BluetoothAdapter.STATE_TURNING_ON:
                    LogUtil.d("bluetooth :: STATE_TURNING_ON");
                    bluetoothIsTurningOn = true;
                    bluetoothIsTurningOff = false;
                    break;
                case BluetoothAdapter.STATE_ON:
                    LogUtil.d("bluetooth :: STATE_ON");
                    bluetoothIsTurningOn = false;
                    bluetoothIsTurningOff = false;
                    syncBTMode();
                    if (btMode == BLUETOOTH_OPTIONS_TEMPERTURE_SENSOR) startScanTempSensor();
                    else if (btMode == BLUETOOTH_OPTIONS_SMART_PHONE) stopScanTempSensor(true);
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    LogUtil.d("bluetooth :: STATE_TURNING_OFF");
                    bluetoothIsTurningOn = false;
                    bluetoothIsTurningOff = true;
                    break;
                case BluetoothAdapter.STATE_OFF:
                    LogUtil.d("bluetooth :: STATE_OFF");
                    bluetoothIsTurningOn = false;
                    bluetoothIsTurningOff = false;
                    syncBTMode();
                    stopScanTempSensor(false);
                    break;
            }


            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            switch (action){
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    LogUtil.d("Enter:: samhung---------ACTION_ACL_CONNECTED");
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    //ClientManager.getClient().closeBluetooth();
                    doDisconnect();
                    LogUtil.d("Enter:: samhung---------ACTION_ACL_DISCONNECTED");
                    break;
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    LogUtil.d("Enter:: samhung---------ACTION_STATE_CHANGED");

                  /*  if(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                            == BluetoothAdapter.STATE_OFF){

                    } else if(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                            == BluetoothAdapter.STATE_ON){
                        initBluetooth();
                    }*/



                    break;
                case BluetoothDevice.ACTION_FOUND:


                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED:
                    //LogUtil.d("Enter:: samhung---------ACTION_ACL_DISCONNECT_REQUESTED");
                    break;
                /*case BluetoothAdapter.STATE_OFF:

                    break;*/
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    //LogUtil.d("Enter:: samhung---------ACTION_DISCOVERY_FINISHED----->" + isManualDisconnect);
                    if (isManualDisconnect) {
                        connectStatus = BLE_CONNECT_STATUS_UNCONNECT;
                        isManualDisconnect = false;
                        discoveryCnt = 0;
                    }else {
                        discoveryCnt++;
                        if (connectStatus == BLE_CONNECT_STATUS_SEARCHING || connectStatus == BLE_CONNECT_STATUS_UNCONNECT) {
                            if (discoveryCnt == DISCOVERY_COUNT) {
                                discoveryCnt = 0;
                                connectStatus = BLE_CONNECT_STATUS_UNCONNECT;
                                if (listener != null) listener.onTempSensorStop(BLUETOOTH_TEMP_SENSOR_STOP_TYPE_PASSIVE);
                            }else {
                                handler.sendEmptyMessageDelayed(HANDLER_START_DISCOVERY,START_DISCOVERY_DELAY_TIME);

                            }

                            //updateStatusUI();
                        }else {
                            discoveryCnt = 0;
                        }
                    }



                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:

                    break;
            }
        }
    };


    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scandata) {
            Message msg = new Message();
            msg.obj = device;
            msg.arg1 = rssi;
            msg.what = HANDLER_DO_LE_SCAN_CALLBACK;
            handler.sendMessage(msg);

        }
    };



        public BluetoothManager(Context context, int mode ,OnBluetoothManagerListener listener) {
            this.context = context;
            this.listener = listener;
            this.btMode = mode;
            this.bluetoothHelper = BluetoothHelper.getInstance(context);
            registerReceiver(context);
            //checkBluetooth();
        }

        private void registerReceiver(Context context) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
            intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
            intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);

            //intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
            //intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            //intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);


            intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_OFF");
            intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_ON");
            context.registerReceiver(mReceiver, intentFilter);
        }

        private void initBluetooth() {
            final android.bluetooth.BluetoothManager bluetoothManager = (android.bluetooth.BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            bluetoothAdapter = bluetoothManager.getAdapter();
        }

        private void checkBluetooth() {
            //final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();  //获取蓝牙适配器
            final android.bluetooth.BluetoothManager bluetoothManager = (android.bluetooth.BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            bluetoothAdapter = bluetoothManager.getAdapter();


            if (bluetoothAdapter != null) {  //有蓝牙功能
                if (!bluetoothAdapter.isEnabled()) {  //蓝牙未开启
                   /* new Thread(new Runnable() {
                        @Override
                        public void run() {
                            bluetoothAdapter.enable();  //开启蓝牙（还有一种方法开启，我就不说了，自己查去）
                        }
                    }).start();*/

                    bluetoothAdapter.enable();

                } else {
                    if (btMode == BLUETOOTH_OPTIONS_TEMPERTURE_SENSOR)startScanTempSensor();
                    else if (btMode == BLUETOOTH_OPTIONS_SMART_PHONE);
                }
            } else {  //无蓝牙功能
                //IApplication.showToast("当前设备未找到蓝牙功能");  //弹出Toast提示
            }
        }

        public void setBTWorkMode(int mode) {
            LogUtil.d("Enter:: setBTWorkMode---->" + mode);
            switch (mode) {
                case BLUETOOTH_OPTIONS_DISABLE:
                    stopScanTempSensor(false);
                    if (connectStatus == BLE_CONNECT_STATUS_CONNECTTED) doUnconnect();
                  /*  new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (bluetoothAdapter.isEnabled()) bluetoothAdapter.disable();
                        }
                    }).start();*/
                  if (bluetoothAdapter != null) bluetoothAdapter.disable();
                  else {
                      bluetoothAdapter = getBluetoothAdapter();
                      if (bluetoothAdapter != null) bluetoothAdapter.disable();
                  }

                    break;
                case BLUETOOTH_OPTIONS_ENABLE:
                    //int btMode = TFTHobConstant.BLUETOOTH_OPTIONS_DISABLE;
                    if (SettingPreferencesUtil.getBluetoothSwitchStatus(context).equals(CataSettingConstant.BLUETOOTH_SWITCH_STATUS_OPEN)) {
                        if (SettingPreferencesUtil.getBluetoothStyle(context).equals(CataSettingConstant.BLUETOOTH_SENSOR_STYLE)) {
                            btMode = TFTHobConstant.BLUETOOTH_OPTIONS_TEMPERTURE_SENSOR;
                        }else if (SettingPreferencesUtil.getBluetoothStyle(context).equals(CataSettingConstant.BLUETOOTH_PHONE_STYLE)) {
                            btMode = TFTHobConstant.BLUETOOTH_OPTIONS_SMART_PHONE;
                        }else {
                            //防止getBluetoothStyle取到的值是空的
                            btMode = TFTHobConstant.BLUETOOTH_OPTIONS_TEMPERTURE_SENSOR;
                        }
                    }else if (SettingPreferencesUtil.getBluetoothSwitchStatus(context).equals(CataSettingConstant.BLUETOOTH_SWITCH_STATUS_CLOSE)) {
                        btMode = TFTHobConstant.BLUETOOTH_OPTIONS_DISABLE;
                    }


                    checkBluetooth();
                    break;
                case BLUETOOTH_OPTIONS_TEMPERTURE_SENSOR:
                    if (bluetoothAdapter != null) {
                        if (!bluetoothAdapter.isEnabled()) checkBluetooth();
                    }else {
                        bluetoothAdapter = getBluetoothAdapter();
                        if (bluetoothAdapter != null) {
                            if (!bluetoothAdapter.isEnabled()) checkBluetooth();
                        }
                    }


                    startScanTempSensor();
                    break;
                case BLUETOOTH_OPTIONS_SMART_PHONE:
                    if (bluetoothAdapter != null) {
                        if (!bluetoothAdapter.isEnabled()) checkBluetooth();
                    }else {
                        bluetoothAdapter = getBluetoothAdapter();
                        if (bluetoothAdapter != null) {
                            if (!bluetoothAdapter.isEnabled()) checkBluetooth();
                        }
                    }
                    stopScanTempSensor(true);
                    break;
            }
            btMode = mode;
        }

        private void syncBTMode() {
            if (CataSettingConstant.BLUETOOTH_SWITCH_STATUS_OPEN.equals(SettingPreferencesUtil.getBluetoothSwitchStatus(context))) {
                boolean bluetoothStyle = SettingPreferencesUtil.getBluetoothStyle(context).equals(CataSettingConstant.BLUETOOTH_SENSOR_STYLE) ? true : false;
                if (bluetoothStyle) {
                    LogUtil.d("Enter:: syncBTMode---->temp");
                    btMode = BLUETOOTH_OPTIONS_TEMPERTURE_SENSOR;
                } else {
                    btMode = BLUETOOTH_OPTIONS_SMART_PHONE;
                    LogUtil.d("Enter:: syncBTMode---->phone");
                }
            } else {
                btMode = BLUETOOTH_OPTIONS_DISABLE;
            }
        }

        private void doConnect(String mac) {
            LogUtil.d("Enter:: doConnect");
            Logger.getInstance().i("doConnect(" + mac + ")");
            BleConnectOptions options = new BleConnectOptions.Builder()
                    .setConnectRetry(3)   // 连接如果失败重试3次
                    .setConnectTimeout(30000)   // 连接超时30s
                    .setServiceDiscoverRetry(3)  // 发现服务如果失败重试3次
                    .setServiceDiscoverTimeout(20000)  // 发现服务超时20s
                    .build();

            ClientManager.getClient().connect(mac, options, new BleConnectResponse() {
                @Override
                public void onResponse(int code, BleGattProfile data) {
                    if (code == Code.REQUEST_SUCCESS) {
                        connectStatus = BLE_CONNECT_STATUS_CONNECTTED;
                        //showToast(getString(R.string.msg_connect_success));
                        workProgress = WORK_PROGRESS_START_HANDSHAKE_SEND_1;
                        cmd = 0x01;
                        doWrite(UUID_SERVICE, UUID_READ_LEVEL, getCmdData());
                        //updateStatusUI();
                    } else {
                        if (connectStatus == BLE_CONNECT_STATUS_SEARCHING || connectStatus == BLE_CONNECT_STATUS_UNCONNECT || connectStatus == BLE_CONNECT_STATUS_CONNECTTING) {
                            connectStatus = BLE_CONNECT_STATUS_UNCONNECT;
                            //updateStatusUI();
                            startScanTempSensor();
                        }
                    }
                    LogUtil.d("connect code----->" + code);

                }

            });
        }

        private void doWrite(UUID service, final UUID character, byte[] value) {
            //if ("00002a24-0000-1000-8000-00805f9b34fb".equals(character.toString())) {
            ClientManager.getClient().write(macStr, service, character, value, new BleWriteResponse() {
                @Override
                public void onResponse(int code) {

                    if (code == Code.REQUEST_SUCCESS) {

                        if (workProgress == WORK_PROGRESS_NORMAL_SEND_DISCONECT) {
                            LogUtil.d("samhung write success----->WORK_PROGRESS_NORMAL_SEND_DISCONECT");
                            workProgress = WORK_PROGRESS_START_HANDSHAKE_SEND_1;
                            connectStatus = BLE_CONNECT_STATUS_UNCONNECT;

                            handShakeCounter = 0;
                            macStr = "";
                            handler.sendEmptyMessage(HANDLER_UPDATE_UI);

                            //showToast(getString(R.string.msg_unconnect_success));
                        } else {
                            doNormalWorkFlow();
                        }
                    }else {
                        if (workProgress == WORK_PROGRESS_NORMAL_SEND_DISCONECT) {
                            LogUtil.d("samhung write fail----->WORK_PROGRESS_NORMAL_SEND_DISCONECT");
                        }

                    }
                }
            });


        }

        private void doDiscovery() {
        connectStatus = BLE_CONNECT_STATUS_SEARCHING;
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }
        }else {
            bluetoothAdapter = getBluetoothAdapter();
            if (bluetoothAdapter != null) {
                if (bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();
                }
            }else return;
        }


        // Request discover from BluetoothAdapter
        bluetoothAdapter.startDiscovery();

    }

        private void doNormalWorkFlow() {
            LogUtil.d("Enter:: samhung doNormalWorkFlow---workProgress---->" + workProgress);
            switch (workProgress) {
                case WORK_PROGRESS_START_HANDSHAKE_SEND_1:
                    workProgress = WORK_PROGRESS_START_HANDSHAKE_SEND_2;
                    cmd = 0x02;
                    doWrite(UUID_SERVICE, UUID_READ_LEVEL, getCmdData());
                    break;
                case WORK_PROGRESS_START_HANDSHAKE_SEND_2:
                    workProgress = WORK_PROGRESS_START_HANDSHAKE_SEND_3;
                    cmd = 0x03;
                    doWrite(UUID_SERVICE, UUID_READ_LEVEL, getCmdData());
                    break;
                case WORK_PROGRESS_START_HANDSHAKE_SEND_3:
                /*workProgress = WORK_PROGRESS_NORMAL_SEND_1;
                cmd = 0x01;
                doWrite(UUID_SERVICE,UUID_READ_LEVEL,getCmdData());*/
                    handShakeCounter++;
                    if (handShakeCounter == 3) {
                        if (listener != null) listener.onTempSensorReady();
                        workProgress = WORK_PROGRESS_NORMAL_DO_READ_TEMP;
                        doRead();
                    } else {
                        workProgress = WORK_PROGRESS_START_HANDSHAKE_SEND_1;
                        cmd = 0x01;
                        doWrite(UUID_SERVICE, UUID_READ_LEVEL, getCmdData());
                    }


                    break;
                case WORK_PROGRESS_NORMAL_SEND_1:
                    workProgress = WORK_PROGRESS_NORMAL_SEND_2;
                    cmd = 0x02;
                    doWrite(UUID_SERVICE, UUID_READ_LEVEL, getCmdData());
                    break;
                case WORK_PROGRESS_NORMAL_SEND_2:
                    workProgress = WORK_PROGRESS_NORMAL_SEND_3;
                    cmd = 0x02;
                    doWrite(UUID_SERVICE, UUID_READ_LEVEL, getCmdData());
                    break;
                case WORK_PROGRESS_NORMAL_SEND_3:
                    workProgress = WORK_PROGRESS_NORMAL_SEND_4;
                    cmd = 0x02;
                    doWrite(UUID_SERVICE, UUID_READ_LEVEL, getCmdData());
                    break;
                case WORK_PROGRESS_NORMAL_SEND_4:
                    workProgress = WORK_PROGRESS_NORMAL_DO_READ_TEMP;
                    doRead();
                    break;
                case WORK_PROGRESS_NORMAL_DO_READ_TEMP:
                    workProgress = WORK_PROGRESS_NORMAL_DO_READ_BATTERY;
                    doRead();
                    break;
                case WORK_PROGRESS_NORMAL_DO_READ_BATTERY:
                    workProgress = WORK_PROGRESS_NORMAL_DO_READ_BATTERY_CHARGING_STATUS;
                    doRead();
                    break;
                case WORK_PROGRESS_NORMAL_DO_READ_BATTERY_CHARGING_STATUS:
                    workProgress = WORK_PROGRESS_NORMAL_SEND_1;
                    handler.sendEmptyMessageDelayed(HANDLER_START_WORK_FLOW, START_WORK_FLOW_DELAY_TIME);
                    break;
                case WORK_PROGRESS_NORMAL_SEND_DISCONECT:
                    workProgress = WORK_PROGRESS_START_HANDSHAKE_SEND_1;
                    cmd = 0x04;
                    doWrite(UUID_SERVICE, UUID_READ_LEVEL, getCmdData());
                    if (listener != null) {
                        listener.onTempSensorStop(BLUETOOTH_TEMP_SENSOR_STOP_TYPE_PASSIVE);
                    }
                    LogUtil.d("Enter:: WORK_PROGRESS_NORMAL_SEND_DISCONECT");
                    /*if (isManualDisconnect) {
                        if (bluetoothAdapter.isDiscovering()) {
                            bluetoothAdapter.cancelDiscovery();
                        }
                    }*/

                    if (btMode == BLUETOOTH_OPTIONS_TEMPERTURE_SENSOR)startScanTempSensor();
                    else if (btMode == BLUETOOTH_OPTIONS_SMART_PHONE);
                    break;
                case WORK_PROGRESS_NORMAL_SEND_DISCONECT_MANUAL:
                    workProgress = WORK_PROGRESS_START_HANDSHAKE_SEND_1;
                    cmd = 0x04;
                    doWrite(UUID_SERVICE, UUID_READ_LEVEL, getCmdData());
                    connectStatus = BLE_CONNECT_STATUS_UNCONNECT;
                    handShakeCounter = 0;
                    macStr = "";
                    if (listener != null) {
                        listener.onTempSensorStop(BLUETOOTH_TEMP_SENSOR_STOP_TYPE_INITIATIVE);
                    }
                    break;
            }
        }

        private void doRead() {
            if (workProgress == WORK_PROGRESS_NORMAL_DO_READ_TEMP) {
                doRead(UUID_SERVICE, UUID_MEDIUM_CHARACTER);
            } else if (workProgress == WORK_PROGRESS_NORMAL_DO_READ_BATTERY) {
                doRead(UUID_SERVICE, UUID_BATTERY_STATUS);
            } else if (workProgress == WORK_PROGRESS_NORMAL_DO_READ_BATTERY_CHARGING_STATUS) {
                doRead(UUID_SERVICE, UUID_BATTERY_STATUS_CHARGING);
            }
        }

        private void doRead(UUID service, final UUID character) {
            ClientManager.getClient().read(macStr, service, character, new BleReadResponse() {
                @Override
                public void onResponse(int code, byte[] data) {
                    //LogUtil.d("samhung read--code------->" + code);
                    if (code == Code.REQUEST_SUCCESS) {
                        if (workProgress == WORK_PROGRESS_NORMAL_DO_READ_TEMP) {
                            LogUtil.d("temp---->" + DataParseUtil.bytesToHexString(data, data.length));
                            //LogUtil.d("temp---->" + (data[0] & 0xff));
                            updateTempValue(data);
                            if (listener != null) listener.onTempSensorTempUpdate(tempValue);
                            handler.sendEmptyMessage(HANDLER_UPDATE_UI);
                            doNormalWorkFlow();
                        } else if (workProgress == WORK_PROGRESS_NORMAL_DO_READ_BATTERY) {
                            LogUtil.d("battery---->" + DataParseUtil.bytesToHexString(data, data.length));
                            updateBatterValue(data);
                            if (listener != null)
                                listener.onTempSensorBatteryUpdate(batteryValue, batteryValue);
                            handler.sendEmptyMessage(HANDLER_UPDATE_UI);
                            doNormalWorkFlow();
                        } else if (workProgress == WORK_PROGRESS_NORMAL_DO_READ_BATTERY_CHARGING_STATUS) {
                            LogUtil.d("battery charge---->" + DataParseUtil.bytesToHexString(data, data.length));
                            updateBatteryChargeStateValue(data);
                            if (listener != null)
                                listener.onTempSensorBatteryChargeStateUpdate(batteryChargeState);
                            handler.sendEmptyMessage(HANDLER_UPDATE_UI);
                            doNormalWorkFlow();
                        }
                    } else {
                        if (workProgress == WORK_PROGRESS_NORMAL_DO_READ_TEMP) {
                            doRead(UUID_SERVICE, UUID_MEDIUM_CHARACTER);
                        } else if (workProgress == WORK_PROGRESS_NORMAL_DO_READ_BATTERY) {
                            doRead(UUID_SERVICE, UUID_READ_LEVEL);
                        } else if (workProgress == WORK_PROGRESS_NORMAL_DO_READ_BATTERY_CHARGING_STATUS) {
                            doRead(UUID_SERVICE, UUID_BATTERY_STATUS_CHARGING);
                        }
                    }
                }
            });
        }

        private void updateTempValue(byte[] data) {
            if (data.length == 2) {
                tempValue = (((data[1] & 0xff) << 8) | (data[0] & 0xff)) / 10;

            }
        }

        private void updateBatterValue(byte[] data) {
            if (data.length == 1) {
                batteryValue = data[0] & 0xff;
            }
        }

        private void updateBatteryChargeStateValue(byte[] data) {
            if (data.length == 1) {
                batteryChargeState = data[0] & 0xff;
            }
        }

        private void doUnconnect() {
            workProgress = WORK_PROGRESS_NORMAL_SEND_DISCONECT;
            doNormalWorkFlow();

        }

    private void doUnconnectManual() {
        workProgress = WORK_PROGRESS_NORMAL_SEND_DISCONECT_MANUAL;
        doNormalWorkFlow();

    }

        private void doDisconnect() {
            if (connectStatus != BLE_CONNECT_STATUS_UNCONNECT) {
                if (!isManualDisconnect) {
                    if (listener != null) listener.onTempSensorStop(BLUETOOTH_TEMP_SENSOR_STOP_TYPE_PASSIVE);
                } else {
                    if (listener != null) listener.onTempSensorStop(BLUETOOTH_TEMP_SENSOR_STOP_TYPE_PASSIVE);
                   /* if (bluetoothAdapter.isDiscovering()) {
                        bluetoothAdapter.cancelDiscovery();
                    } else {
                        isManualDisconnect = false;
                    }*/
                }

                if (!bluetoothIsTurningOff) {
                    if (btMode == BLUETOOTH_OPTIONS_TEMPERTURE_SENSOR) {
                        startScanTempSensor();
                    } else if (btMode == BLUETOOTH_OPTIONS_SMART_PHONE) {

                    }
                }
            }


            workProgress = WORK_PROGRESS_START_HANDSHAKE_SEND_1;
            connectStatus = BLE_CONNECT_STATUS_UNCONNECT;
            handShakeCounter = 0;
            macStr = "";
            handler.sendEmptyMessage(HANDLER_UPDATE_UI);

        }

        private byte[] getCmdData() {
            byte[] data = {cmd};
            return data;
        }

        private void checkPermission(Context mContext) {
            if (ContextCompat.checkSelfPermission(Objects.requireNonNull(mContext), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) Objects.requireNonNull(mContext),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            } else {

                //doDiscovery();

                //doScanLeDevice(true);

            }

        }

        public void startScanTempSensor() {
            Logger.getInstance().i("startScanTempSensor()");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (bluetoothAdapter == null) bluetoothAdapter = getBluetoothAdapter();
                    if (bluetoothAdapter != null) {
                        connectStatus = BLE_CONNECT_STATUS_SEARCHING;
                        bluetoothHelper.closeDiscoverableTimeout();
                        if (isLeScanStarted()) {
                            bluetoothAdapter.stopLeScan(leScanCallback);
                            setLeScanStarted(false);
                        }
                        bluetoothAdapter.startLeScan(leScanCallback);
                        setLeScanStarted(true);
                    }

                }
            }).start();
        }

        public void stopScanTempSensor(final boolean visibleAlways) {
            Logger.getInstance().i("stopScanTempSensor(" + visibleAlways + ")");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (bluetoothAdapter == null) bluetoothAdapter = getBluetoothAdapter();
                    if (bluetoothAdapter != null) {
                        if (isLeScanStarted()) {
                            bluetoothAdapter.stopLeScan(leScanCallback);
                            setLeScanStarted(false);
                            if (visibleAlways) {
                                bluetoothHelper.setDiscoverableTimeout(300 * 1000);
                            } else {
                                bluetoothHelper.closeDiscoverableTimeout();
                            }
                        }
                    }

                }
            }).start();


        }

        private BluetoothAdapter getBluetoothAdapter() {
            final android.bluetooth.BluetoothManager bluetoothManager = (android.bluetooth.BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            return bluetoothManager.getAdapter();
        }



        public void findAndConnectSensorSlave(Context context) {
            LogUtil.d("Enter:: findAndConnectSensorSlave---status---->" + connectStatus);
            if (connectStatus == BLE_CONNECT_STATUS_UNCONNECT) {
                checkBluetooth();
            }


        }

        public void stopSensorSlaveWork() {
            LogUtil.d("Enter:: stopSensorSlaveWork");
            isManualDisconnect = true;
            doUnconnectManual();
            stopScanTempSensor(false);
        }

        public int getTempSensorConnectStatus() {
            return connectStatus;
        }


        public void recyle() {
            context.unregisterReceiver(mReceiver);
        }

    public synchronized boolean isLeScanStarted() {
        return leScanStarted;
    }

    public synchronized void setLeScanStarted(boolean leScanStarted) {
        this.leScanStarted = leScanStarted;
    }

    public interface OnBluetoothManagerListener {
            void onTempSensorReady();

            void onTempSensorStop(int stopType);

            void onTempSensorTempUpdate(int tempValue);

            void onTempSensorBatteryUpdate(int chargeStatus, int batteryLevel);

            void onTempSensorBatteryChargeStateUpdate(int chartState);
        }

}
