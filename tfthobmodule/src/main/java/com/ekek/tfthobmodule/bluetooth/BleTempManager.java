package com.ekek.tfthobmodule.bluetooth;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.hardwaremodule.utils.DataParseUtil;
import com.inuker.bluetooth.library.Code;
import com.inuker.bluetooth.library.beacon.Beacon;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.inuker.bluetooth.library.utils.BluetoothLog;

import java.util.Objects;
import java.util.UUID;

public class BleTempManager {
    private UUID UUID_SERVICE = UUID.fromString("66c926b2-189b-4a02-97e7-38ae7adce96d");
    private UUID UUID_MEDIUM_CHARACTER = UUID.fromString("97df461c-ae66-4284-8510-4c0b992dac72");
    private UUID UUID_READ_LEVEL = UUID.fromString("a8d3059e-9f5a-41c9-a865-f9247af44594");
    private UUID UUID_BATTERY_STATUS = UUID.fromString("d1250404-d897-4f21-b7d5-1367e0faeeb8");
    private static final String TEMP_NAME = "Sensor Slave";
    private static final int BLE_CONNECT_STATUS_UNCONNECT = 0;
    private static final int BLE_CONNECT_STATUS_CONNECTTING = 1;
    private static final int BLE_CONNECT_STATUS_CONNECTTED = 2;
    private static final int BLE_CONNECT_STATUS_SEARCHING = 3;
    private static final int WORK_PROGRESS_START_HANDSHAKE_SEND_1 = 0;
    private static final int WORK_PROGRESS_START_HANDSHAKE_SEND_2 = 1;
    private static final int WORK_PROGRESS_START_HANDSHAKE_SEND_3 = 2;
    private static final int WORK_PROGRESS_NORMAL_SEND_1 = 3;
    private static final int WORK_PROGRESS_NORMAL_SEND_2 = 4;
    private static final int WORK_PROGRESS_NORMAL_SEND_3 = 5;
    private static final int WORK_PROGRESS_NORMAL_SEND_DISCONECT = 6;
    private static final int WORK_PROGRESS_NORMAL_DO_READ_TEMP = 7;
    private static final int WORK_PROGRESS_NORMAL_DO_READ_BATTERY = 8;
    private static final int HANDLER_UPDATE_UI = 0;
    private static final int HANDLER_START_WORK_FLOW = 1;
    private static final int HANDLER_START_DISCOVERY = 2;
    private static final int START_WORK_FLOW_DELAY_TIME = 100 * 1;//1000
    private static final int START_DISCOVERY_DELAY_TIME = 1000 * 1;
    private static final int DISCOVERY_COUNT = 3;
    private int connectStatus = BLE_CONNECT_STATUS_UNCONNECT;
    private int workProgress = WORK_PROGRESS_START_HANDSHAKE_SEND_1;
    // Stops scanning after 20 seconds.
    private static final long SCAN_PERIOD = 10000 * 6;
    private int handShakeCounter = 0;
    private String macStr;
    private byte cmd = 0x01;
    private int tempValue = 1000;
    private int batteryValue = 1000;
    private Context context;
    private BluetoothLeScanner scanner;
    private OnBleTempManagerListener listener;
    private BluetoothAdapter bluetoothAdapter;
    private boolean isManualDisconnect = false;
    private int discoveryCnt = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
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
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    LogUtil.d("Enter:: samhung---------ACTION_FOUND");
                    //BluetoothDevice mBluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    LogUtil.d("ble device----->" + device.getName() + "\n" + device.getAddress());

                    if (device.getName().equals(TEMP_NAME) && connectStatus == BLE_CONNECT_STATUS_SEARCHING) {
                        //ClientManager.getClient().stopSearch();
                        LogUtil.d("Enter:: do connect");
                        connectStatus = BLE_CONNECT_STATUS_CONNECTTING;
                        if (bluetoothAdapter.isDiscovering()) {
                            bluetoothAdapter.cancelDiscovery();
                        }
                        discoveryCnt = 0;
                        // updateStatusUI();
                        macStr = device.getAddress();
                        doConnect(macStr);

                    }

                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED:
                    LogUtil.d("Enter:: samhung---------ACTION_ACL_DISCONNECT_REQUESTED");
                    break;
                /*case BluetoothAdapter.STATE_OFF:

                    break;*/
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    LogUtil.d("Enter:: samhung---------ACTION_DISCOVERY_FINISHED----->" + isManualDisconnect);
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
                                if (listener != null) listener.onTempSensorStop();
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
                    LogUtil.d("Enter:: samhung---------ACTION_DISCOVERY_STARTED");
                    break;
            }
        }
    };

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

            }
        }
    };

    public BleTempManager(Context context , OnBleTempManagerListener listener) {
        this.context = context;
        this.listener = listener;
        registerReceiver(context);
        checkBluetooth();
    }

    private void registerReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);

        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);

        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_OFF");
        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_ON");
        context.registerReceiver(mReceiver,intentFilter);
    }


    private void checkPermission(Context mContext) {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(mContext), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 申请定位授权
           /* ActivityCompat.requestPermissions((Activity) Objects.requireNonNull(context),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);*/
            ActivityCompat.requestPermissions((Activity) Objects.requireNonNull(mContext),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
           // doSearch();

            doDiscovery();

            //doScanLeDevice(true);

            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                scanner.stopScan(scanCallback);
                scanner.startScan(scanCallback);
            }
*/
            //7.0
          /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //scanLeDevice(true);
                doScanLeDevice(true);
            }*/
        }

    }

    private void doDiscovery() {
        connectStatus = BLE_CONNECT_STATUS_SEARCHING;
        // If we're already discovering, stop it
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        bluetoothAdapter.startDiscovery();

    }

    private void doSearch() {
        connectStatus = BLE_CONNECT_STATUS_SEARCHING;
        //updateStatusUI();
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(3000, 20)   // 先扫BLE设备3次，每次3s
                //.searchBluetoothClassicDevice(5000) // 再扫经典蓝牙5s
                //.searchBluetoothLeDevice(2000)      // 再扫BLE设备2s
                .build();
        ClientManager.getClient().search(request, new SearchResponse() {
            @Override
            public void onSearchStarted() {

            }

            @Override
            public void onDeviceFounded(SearchResult device) {
                Beacon beacon = new Beacon(device.scanRecord);
                BluetoothLog.v(String.format("beacon for %s\n%s", device.getAddress(), beacon.toString()));
                LogUtil.d("device name ---->" + device.getName() + "----rssi---->" + device.rssi);
                if (device.getName().equals(TEMP_NAME) && connectStatus == BLE_CONNECT_STATUS_SEARCHING) {
                    ClientManager.getClient().stopSearch();
                    connectStatus = BLE_CONNECT_STATUS_CONNECTTING;
                   // updateStatusUI();
                    macStr = device.getAddress();
                    doConnect(macStr);

                }

            }

            @Override
            public void onSearchStopped() {
               /* if (connectStatus == BLE_CONNECT_STATUS_CONNECTTING || connectStatus == BLE_CONNECT_STATUS_CONNECTTED) {

                }else {
                    connectStatus = BLE_CONNECT_STATUS_UNCONNECT;
                    updateStatusUI();
                }*/

                if (connectStatus == BLE_CONNECT_STATUS_SEARCHING || connectStatus == BLE_CONNECT_STATUS_UNCONNECT) {
                    connectStatus = BLE_CONNECT_STATUS_UNCONNECT;
                    if (listener != null) listener.onTempSensorStop();
                    //updateStatusUI();
                }

                LogUtil.d("Enter:: ble onSearchStopped");

            }

            @Override
            public void onSearchCanceled() {
                LogUtil.d("Enter:: ble onSearchCanceled");

            }

        });
    }

    private void doConnect(String mac) {
        LogUtil.d("Enter:: doConnect");
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
                    doWrite(UUID_SERVICE,UUID_READ_LEVEL,getCmdData());
                    //updateStatusUI();
                }else {
                    if (connectStatus == BLE_CONNECT_STATUS_SEARCHING || connectStatus == BLE_CONNECT_STATUS_UNCONNECT || connectStatus == BLE_CONNECT_STATUS_CONNECTTING) {
                        connectStatus = BLE_CONNECT_STATUS_UNCONNECT;
                        //updateStatusUI();
                    }
                }
                LogUtil.d("connect code----->" + code);

            }

        });
    }

    private void checkBluetooth() {
        //final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();  //获取蓝牙适配器
        final BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            scanner = bluetoothAdapter.getBluetoothLeScanner();
        }
        if (bluetoothAdapter != null) {  //有蓝牙功能
            if (!bluetoothAdapter.isEnabled()) {  //蓝牙未开启
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        bluetoothAdapter.enable();  //开启蓝牙（还有一种方法开启，我就不说了，自己查去）
                    }
                }).start();
            } else {
               /* if (!bluetoothAdapter.isDiscovering()) {  //如果没有在扫描设备
                    //bluetoothAdapter.startDiscovery();//扫描附近蓝牙设备，然后做接下来的操作，比如扫描附近蓝牙等
                } else {
                    //IApplication.showToast("正在扫描");  //弹出Toast提示
                }*/
            }
        } else {  //无蓝牙功能
            //IApplication.showToast("当前设备未找到蓝牙功能");  //弹出Toast提示
        }
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
                    }else {
                        doNormalWorkFlow();
                    }
                }
            }
        });


    }

    private void doNormalWorkFlow() {
        LogUtil.d("Enter:: samhung doNormalWorkFlow---workProgress---->" + workProgress);
        switch (workProgress) {
            case WORK_PROGRESS_START_HANDSHAKE_SEND_1:
                workProgress = WORK_PROGRESS_START_HANDSHAKE_SEND_2;
                cmd = 0x02;
                doWrite(UUID_SERVICE,UUID_READ_LEVEL,getCmdData());
                break;
            case WORK_PROGRESS_START_HANDSHAKE_SEND_2:
                workProgress = WORK_PROGRESS_START_HANDSHAKE_SEND_3;
                cmd = 0x03;
                doWrite(UUID_SERVICE,UUID_READ_LEVEL,getCmdData());
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
                }else {
                    workProgress = WORK_PROGRESS_START_HANDSHAKE_SEND_1;
                    cmd = 0x01;
                    doWrite(UUID_SERVICE,UUID_READ_LEVEL,getCmdData());
                }


                break;
            case WORK_PROGRESS_NORMAL_SEND_1:
                workProgress = WORK_PROGRESS_NORMAL_SEND_2;
                cmd = 0x02;
                doWrite(UUID_SERVICE,UUID_READ_LEVEL,getCmdData());
                break;
            case WORK_PROGRESS_NORMAL_SEND_2:
                workProgress = WORK_PROGRESS_NORMAL_SEND_3;
                cmd = 0x02;
                doWrite(UUID_SERVICE,UUID_READ_LEVEL,getCmdData());
                break;
            case WORK_PROGRESS_NORMAL_SEND_3:
                workProgress = WORK_PROGRESS_NORMAL_DO_READ_TEMP;
                doRead();
                break;
            case WORK_PROGRESS_NORMAL_DO_READ_TEMP:
                workProgress = WORK_PROGRESS_NORMAL_DO_READ_BATTERY;
                doRead();
                break;
            case WORK_PROGRESS_NORMAL_DO_READ_BATTERY:
                workProgress = WORK_PROGRESS_NORMAL_SEND_1;
                handler.sendEmptyMessageDelayed(HANDLER_START_WORK_FLOW,START_WORK_FLOW_DELAY_TIME);
                break;
            case WORK_PROGRESS_NORMAL_SEND_DISCONECT:
                workProgress = WORK_PROGRESS_START_HANDSHAKE_SEND_1;
                cmd = 0x04;
                doWrite(UUID_SERVICE,UUID_READ_LEVEL,getCmdData());

                if (isManualDisconnect) {
                    if (bluetoothAdapter.isDiscovering()) {
                        bluetoothAdapter.cancelDiscovery();
                    }
                }
                break;
        }
    }

    private void doRead() {
        if(workProgress == WORK_PROGRESS_NORMAL_DO_READ_TEMP) {
            doRead(UUID_SERVICE,UUID_MEDIUM_CHARACTER);
        }else if (workProgress == WORK_PROGRESS_NORMAL_DO_READ_BATTERY){
            doRead(UUID_SERVICE,UUID_BATTERY_STATUS);
        }
    }

    private void doRead(UUID service, final UUID character) {
        ClientManager.getClient().read(macStr, service, character, new BleReadResponse() {
            @Override
            public void onResponse(int code, byte[] data) {
                //LogUtil.d("samhung read--code------->" + code);
                if (code == Code.REQUEST_SUCCESS) {
                    if(workProgress == WORK_PROGRESS_NORMAL_DO_READ_TEMP) {
                        LogUtil.d("temp---->" + DataParseUtil.bytesToHexString(data,data.length));
                        //LogUtil.d("temp---->" + (data[0] & 0xff));
                        updateTempValue(data);
                        if (listener != null) listener.onTempSensorTempUpdate(tempValue);
                        handler.sendEmptyMessage(HANDLER_UPDATE_UI);
                        doNormalWorkFlow();
                    }else if (workProgress == WORK_PROGRESS_NORMAL_DO_READ_BATTERY){
                        LogUtil.d("battery---->" + DataParseUtil.bytesToHexString(data,data.length));
                        updateBatterValue(data);
                        if (listener != null) listener.onTempSensorBatteryUpdate(batteryValue,batteryValue);
                        handler.sendEmptyMessage(HANDLER_UPDATE_UI);
                        doNormalWorkFlow();
                    }
                }else {
                    if(workProgress == WORK_PROGRESS_NORMAL_DO_READ_TEMP) {
                        doRead(UUID_SERVICE,UUID_MEDIUM_CHARACTER);
                    }else if (workProgress == WORK_PROGRESS_NORMAL_DO_READ_BATTERY){
                        doRead(UUID_SERVICE,UUID_READ_LEVEL);
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

    private void doUnconnect() {
        workProgress = WORK_PROGRESS_NORMAL_SEND_DISCONECT;
        doNormalWorkFlow();

    }

    private void doDisconnect() {
        LogUtil.d("samhungsamhungsamhung-----connectStatus----->" + connectStatus);
        LogUtil.d("Enter:: samhung doDisconnect------------1");
        if (connectStatus != BLE_CONNECT_STATUS_UNCONNECT) {
            if (!isManualDisconnect) {
                if (listener != null) listener.onTempSensorStop();
            }else {
                LogUtil.d("Enter:: samhung doDisconnect------------2");
                //isManualDisconnect = false;
                if (bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();
                }else {
                    isManualDisconnect = false;
                }

            }


        }



        workProgress = WORK_PROGRESS_START_HANDSHAKE_SEND_1;
        connectStatus = BLE_CONNECT_STATUS_UNCONNECT;
        handShakeCounter = 0;
        macStr = "";
        handler.sendEmptyMessage(HANDLER_UPDATE_UI);
       // if (listener != null) listener.onTempSensorStop();
        //showToast(getString(R.string.msg_unconnect_success));
        //updateStatusUI();
    }

    private byte[] getCmdData() {
        byte[] data = {cmd};
        return data;
    }

 /*   ScanCallback scanCallback = new ScanCallback() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            BluetoothDevice device = result.getDevice();
            int rssi = result.getRssi();//获取rssi
            //这里写你自己的逻辑
            LogUtil.d("result" + result.getDevice().getName());
            if (device.getName() != null) {
                if (device.getName().equals(TEMP_NAME) && connectStatus == BLE_CONNECT_STATUS_SEARCHING) {
                    //ClientManager.getClient().stopSearch();
                    scanLeDevice(false);
                    connectStatus = BLE_CONNECT_STATUS_CONNECTTING;
                    // updateStatusUI();
                    macStr = device.getAddress();
                    doConnect(macStr);

                }
            }

        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            LogUtil.d("fail----->" + errorCode);
            if (connectStatus == BLE_CONNECT_STATUS_SEARCHING || connectStatus == BLE_CONNECT_STATUS_UNCONNECT) {
                connectStatus = BLE_CONNECT_STATUS_UNCONNECT;

            }
        }
    };*/

    private void doScanLeDevice(final boolean enable) {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                scanLeDevice(enable);
            }
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            connectStatus = BLE_CONNECT_STATUS_SEARCHING;
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //mScanning = false;
                   // mBluetoothAdapter.stopLeScan(mLeScanCallback);

                    //scanner.stopScan(scanCallback);//7.0
                    if (connectStatus == BLE_CONNECT_STATUS_SEARCHING || connectStatus == BLE_CONNECT_STATUS_UNCONNECT) {
                        connectStatus = BLE_CONNECT_STATUS_UNCONNECT;
                        if (listener != null) listener.onTempSensorStop();

                    }


                }
            }, SCAN_PERIOD);


            //mScanning = true;
           // mBluetoothAdapter.startLeScan(mLeScanCallback);

            //scanner.stopScan(scanCallback);//7.0
            //scanner.startScan(scanCallback);//7.0
        } else {
            //mScanning = false;

            //scanner.stopScan(scanCallback);//7.0
            if (connectStatus == BLE_CONNECT_STATUS_SEARCHING || connectStatus == BLE_CONNECT_STATUS_UNCONNECT) {
                connectStatus = BLE_CONNECT_STATUS_UNCONNECT;

            }
        }

    }

    public void findAndConnectSensorSlave(Context context) {
        if (connectStatus == BLE_CONNECT_STATUS_UNCONNECT) {
            checkPermission(context);
        }


    }

    public void stopSensorSlaveWork() {
        isManualDisconnect = true;
        doUnconnect();
    }


    public void recyle() {
        context.unregisterReceiver(mReceiver);
    }

    public interface OnBleTempManagerListener {
        void onTempSensorReady();
        void onTempSensorStop();
        void onTempSensorTempUpdate(int tempValue);
        void onTempSensorBatteryUpdate(int chargeStatus,int batteryLevel);
    }

}
