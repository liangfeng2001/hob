package com.ekek.tfthobmodule.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.hardwaremodule.utils.DataParseUtil;

import java.util.List;
import java.util.UUID;

public class CataBluetoothManager {
    private static final String TEMP_NAME = "Sensor Slave";
    private static final String TAG = "CataBluetoothManager";
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

    private UUID UUID_SERVICE = UUID.fromString("66c926b2-189b-4a02-97e7-38ae7adce96d");
    private UUID UUID_MEDIUM_CHARACTER = UUID.fromString("97df461c-ae66-4284-8510-4c0b992dac72");
    private UUID UUID_READ_LEVEL = UUID.fromString("a8d3059e-9f5a-41c9-a865-f9247af44594");
    private UUID UUID_BATTERY_STATUS = UUID.fromString("d1250404-d897-4f21-b7d5-1367e0faeeb8");
    private UUID UUID_BATTERY_CHARGE_STATUS = UUID.fromString("41ca39ce-91b3-41e9-9542-d58a71ac7b9e");
    private static final int HANDLER_SET_NOTIFY_BATTERY = 0;
    private static final int HANDLER_SET_NOTIFY_TEMP = 1;
    private static final int HANDLER_SET_NOTIFY_BATTERY_CHARGE = 2;
    private static final int HANDLER_REQUEST_READ_TEMP = 3;
    private static final int HANDLER_REQUEST_READ_BATTERY_LEVEL = 4;
    private static final int READ_TEMP_DELAY = 1000 * 1;//1000
    private static final int READ_BATTERY_LEVEL_DELAY = 1000 * 2;//1000
    public final static int BATTERY_CHARGE_STATUS_CHARGING = 0;
    public final static int BATTERY_CHARGE_STATUS_NO_CHARGE = 1;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    private OnCataBluetoothManagerListener listener;
    private Context context;
    private int batteryLevel = 20;
    private int btMode = BLUETOOTH_OPTIONS_DISABLE;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_SET_NOTIFY_BATTERY:

                    BluetoothGattCharacteristic ch1 = mBluetoothGatt.getService(UUID_SERVICE).getCharacteristic(UUID_BATTERY_STATUS);
                    boolean enbaleBatteryLevel = enableNotification(mBluetoothGatt,ch1.getService().getUuid(),ch1.getUuid());
                    LogUtil.d("samhung enbaleBatteryLevel ---- > " + enbaleBatteryLevel);
                    if (enbaleBatteryLevel) startReadBattery();
                    break;
                case HANDLER_SET_NOTIFY_TEMP:
                    BluetoothGattCharacteristic ch2 = mBluetoothGatt.getService(UUID_SERVICE).getCharacteristic(UUID_MEDIUM_CHARACTER);
                    boolean result = enableNotification(mBluetoothGatt,ch2.getService().getUuid(),ch2.getUuid());
                    if (result) {
                        startReadTemp();
                        listener.onTempSensorReady();
                    }
                    break;
                case HANDLER_SET_NOTIFY_BATTERY_CHARGE:
                    BluetoothGattCharacteristic ch4 = mBluetoothGatt.getService(UUID_SERVICE).getCharacteristic(UUID_BATTERY_CHARGE_STATUS);
                    boolean enableResult = enableNotification(mBluetoothGatt,ch4.getService().getUuid(),ch4.getUuid());
                    LogUtil.d("samhung enableResult ---- > " + enableResult);
                    if (enableResult) {
                        startReadBatteryChargeStatus();
                        handler.sendEmptyMessageDelayed(HANDLER_REQUEST_READ_BATTERY_LEVEL,READ_BATTERY_LEVEL_DELAY);
                    }
                    break;
                case HANDLER_REQUEST_READ_TEMP:
                    doWrite(new byte[]{2});
                    startReadTemp();
                    break;
                case HANDLER_REQUEST_READ_BATTERY_LEVEL:
                    BluetoothGattCharacteristic ch5 = mBluetoothGatt.getService(UUID_SERVICE).getCharacteristic(UUID_BATTERY_CHARGE_STATUS);
                    boolean disableResult = disableNotification(mBluetoothGatt,ch5.getService().getUuid(),ch5.getUuid());
                    LogUtil.d("samhung disableResult ---- > " + disableResult);
                    if (disableResult) {
                        Message msg1= new Message();
                        msg1.what = HANDLER_SET_NOTIFY_BATTERY;
                        msg1.obj = UUID_BATTERY_STATUS;
                        handler.sendMessageDelayed(msg1,0);
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
                    break;
                case BluetoothAdapter.STATE_ON:
                    LogUtil.d("bluetooth :: STATE_ON");
                    if (btMode == BLUETOOTH_OPTIONS_TEMPERTURE_SENSOR) startScanTempSensor();
                    else if (btMode == BLUETOOTH_OPTIONS_SMART_PHONE) stopScanTempSensor();
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    LogUtil.d("bluetooth :: STATE_TURNING_OFF");
                    break;
                case BluetoothAdapter.STATE_OFF:
                    LogUtil.d("bluetooth :: STATE_OFF");
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
                    //LogUtil.d("Enter:: samhung---------ACTION_ACL_DISCONNECTED");
                    break;
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    //LogUtil.d("Enter:: samhung---------ACTION_STATE_CHANGED");
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    //LogUtil.d("Enter:: samhung---------ACTION_FOUND");
                    //LogUtil.d("ble device----->" + device.getName() + "\n" + device.getAddress());

                 /*   if (device.getName().equals(TEMP_NAME) && connectStatus == BLE_CONNECT_STATUS_SEARCHING) {
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

                    }*/

                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED:
                    //LogUtil.d("Enter:: samhung---------ACTION_ACL_DISCONNECT_REQUESTED");
                    break;
                /*case BluetoothAdapter.STATE_OFF:

                    break;*/
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    //LogUtil.d("Enter:: samhung---------ACTION_DISCOVERY_FINISHED----->" + isManualDisconnect);
                /*    if (isManualDisconnect) {
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
                    }*/



                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:

                    break;
            }
        }
    };

    private void doDisconnect() {
        if (listener != null) listener.onTempSensorStop();
    }


    private void startReadTemp() {
        handler.sendEmptyMessageDelayed(HANDLER_REQUEST_READ_TEMP,READ_TEMP_DELAY);

    }


    private void startReadBatteryChargeStatus() {
        doWrite(new byte[]{1});
    }


    private void startReadBattery() {
        doWrite(new byte[]{1});
    }

    public CataBluetoothManager(Context context, int mode ,OnCataBluetoothManagerListener listener) {
        this.context = context;
        this.listener = listener;
        this.btMode = mode;
        registerReceiver(context);
        checkBluetooth();
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

    private void checkBluetooth() {

        android.bluetooth.BluetoothManager bluetoothManager = (android.bluetooth.BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter != null) {  //有蓝牙功能
            if (!bluetoothAdapter.isEnabled()) {  //蓝牙未开启
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        bluetoothAdapter.enable();  //开启蓝牙（还有一种方法开启，我就不说了，自己查去）
                    }
                }).start();
            } else {
                if (btMode == BLUETOOTH_OPTIONS_TEMPERTURE_SENSOR) startScanTempSensor();
                else if (btMode == BLUETOOTH_OPTIONS_SMART_PHONE);
            }
        } else {  //无蓝牙功能
            //IApplication.showToast("当前设备未找到蓝牙功能");  //弹出Toast提示
        }
    }

    public void startScanTempSensor() {
        bluetoothAdapter.stopLeScan(leScanCallback);
        bluetoothAdapter.startLeScan(leScanCallback);

    }

    public void stopScanTempSensor() {
        bluetoothAdapter.stopLeScan(leScanCallback);

    }

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scandata) {
            LogUtil.d("device name ---->" + device.getName() + "----rssi---->" + rssi);
            if (device.getName() != null && device.getName().equals(TEMP_NAME)) {
                stopScanTempSensor();
                mBluetoothGatt = device.connectGatt(context, false, mGattCallback);

            }



        }
    };

    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        //连接状态改变的回调
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                            int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                // 连接成功后启动服务发现
                Log.e("AAAAAAAA", "启动服务发现:" + mBluetoothGatt.discoverServices());


            }
        }

        //发现服务的回调
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e(TAG, "成功发现服务");

                doFirstConnect(mBluetoothGatt);

            }else{
                Log.e(TAG, "服务发现失败，错误码为:" + status);
            }
        }

        //写操作的回调
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                byte[] writeValue = characteristic.getValue();
                Log.e(TAG, "写入成功" + DataParseUtil.bytesToHexString(writeValue,writeValue.length));
                if (writeValue[0] == 0x00) {
           /*         Message msg1= new Message();
                    msg1.what = HANDLER_SET_NOTIFY_BATTERY;
                    msg1.obj = UUID_BATTERY_STATUS;
                    handler.sendMessageDelayed(msg1,0);*/

                    Message msg2= new Message();
                    msg2.what = HANDLER_SET_NOTIFY_BATTERY_CHARGE;
                    msg2.obj = UUID_MEDIUM_CHARACTER;
                    handler.sendMessageDelayed(msg2,0);

                    Message msg3= new Message();
                    msg3.what = HANDLER_SET_NOTIFY_TEMP;
                    msg3.obj = UUID_BATTERY_CHARGE_STATUS;
                    handler.sendMessageDelayed(msg3,500);

                }


            }else {
                Log.e(TAG, "写入失败" +characteristic.getValue());
            }
        }

        //读操作的回调
        public void onCharacteristicRead(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e(TAG, "读取成功" +characteristic.getValue());
                byte[] data = characteristic.getValue();
                String uuid = characteristic.getUuid().toString();
                Log.e(TAG, "onCharacteristicRead---UUID--->" + uuid);
                Log.e(TAG, "onCharacteristicRead------>" + DataParseUtil.bytesToHexString(data,data.length));
                if (uuid.equals(UUID_MEDIUM_CHARACTER.toString())) {
                    Log.e(TAG, "Temp value----->" + updateTempValue(data));
                }else if (uuid.equals(UUID_BATTERY_STATUS.toString())) {
                    Log.e(TAG, "Battery value----->" + updateBatterValue(data));
                }else if (uuid.equals(UUID_BATTERY_CHARGE_STATUS.toString())) {
                    Log.e(TAG, "Battery charge status----->" + ((data[0] == 0x00)?false:true));
                }
            }
        }

        //数据返回的回调（此处接收BLE设备返回数据）
        public void onCharacteristicChanged(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic) {
            byte[] data = characteristic.getValue();
            String uuid = characteristic.getUuid().toString();
            Log.e(TAG, "onCharacteristicChanged---UUID--->" + uuid);
            Log.e(TAG, "onCharacteristicChanged------>" + DataParseUtil.bytesToHexString(data,data.length));
            if (uuid.equals(UUID_MEDIUM_CHARACTER.toString())) {
                //Log.e(TAG, "Temp value----->" + updateTempValue(data));
                listener.onTempSensorTempUpdate(updateTempValue(data));
            }else if (uuid.equals(UUID_BATTERY_STATUS.toString())) {
                Log.e(TAG, "Battery value----->" + updateBatterValue(data));
                batteryLevel = updateBatterValue(data);
                Message msg3= new Message();
                msg3.what = HANDLER_SET_NOTIFY_TEMP;
                msg3.obj = UUID_BATTERY_CHARGE_STATUS;
                handler.sendMessageDelayed(msg3,0);
            }else if (uuid.equals(UUID_BATTERY_CHARGE_STATUS.toString())) {
                //Log.e(TAG, "Battery charge status----->" + ((data[0] == 0x00)?false:true));
                int chargeStatus = BATTERY_CHARGE_STATUS_NO_CHARGE;
                if (data[0] == 0x00) {
                    chargeStatus = BATTERY_CHARGE_STATUS_NO_CHARGE;
                }else {
                    chargeStatus = BATTERY_CHARGE_STATUS_CHARGING;
                }
                listener.onTempSensorBatteryUpdate(chargeStatus,batteryLevel);
            }


        }
    };

    private int updateTempValue(byte[] data) {
        int tempValue = 0;
        if (data.length == 2) {//c800
            tempValue = (((data[1] & 0xff) << 8) | (data[0] & 0xff)) / 10;

        }
        return tempValue;
    }

    private int updateBatterValue(byte[] data) {
        int batteryValue = 0;
        if (data.length == 1) {
            batteryValue = data[0] & 0xff;
        }
        return batteryValue;
    }

    private void doFirstConnect(BluetoothGatt gatt) {
        //往蓝牙数据通道的写入数据
        BluetoothGattService service = gatt.getService(UUID_SERVICE);
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID_READ_LEVEL);
        characteristic.setValue(new byte[]{0});
        gatt.writeCharacteristic(characteristic);
    }



    private void doWrite(byte[] data) {
        BluetoothGattService service = mBluetoothGatt.getService(UUID_SERVICE);
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID_READ_LEVEL);
        characteristic.setValue(data);
        mBluetoothGatt.writeCharacteristic(characteristic);
    }

    public boolean enableNotification(BluetoothGatt gatt, UUID serviceUUID, UUID characteristicUUID) {
        boolean success = false;
        BluetoothGattService service = gatt.getService(serviceUUID);
        if (service != null) {
            BluetoothGattCharacteristic characteristic = findNotifyCharacteristic(service, characteristicUUID);
            if (characteristic != null) {
                success = gatt.setCharacteristicNotification(characteristic, true);
                LogUtil.d("enableNotification---->" + success);
                if (success) {

                    // 来源：http://stackoverflow.com/questions/38045294/oncharacteristicchanged-not-called-with-ble
                    for(BluetoothGattDescriptor dp: characteristic.getDescriptors()){
                        if (dp != null) {
                            if ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0) {
                                dp.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                LogUtil.d("dp value--->ENABLE_NOTIFICATION_VALUE");
                            } else if ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_INDICATE) != 0) {
                                dp.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                                LogUtil.d("dp value--->ENABLE_INDICATION_VALUE");
                            }
                            gatt.writeDescriptor(dp);
//                            LogUtil.d("gatt write dp");

                        }
                    }
                }
            }
        }
        return success;
    }

    public boolean disableNotification(BluetoothGatt gatt, UUID serviceUUID, UUID characteristicUUID) {
        boolean success = false;
        BluetoothGattService service = gatt.getService(serviceUUID);
        if (service != null) {
            BluetoothGattCharacteristic characteristic = findNotifyCharacteristic(service, characteristicUUID);
            if (characteristic != null) {
                success = gatt.setCharacteristicNotification(characteristic, false);
                LogUtil.d("enableNotification---->" + success);
                if (success) {

                    // 来源：http://stackoverflow.com/questions/38045294/oncharacteristicchanged-not-called-with-ble
                    for(BluetoothGattDescriptor dp: characteristic.getDescriptors()){
                        if (dp != null) {
                            if ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0) {
                                dp.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                LogUtil.d("dp value--->ENABLE_NOTIFICATION_VALUE");
                            } else if ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_INDICATE) != 0) {
                                dp.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                                LogUtil.d("dp value--->ENABLE_INDICATION_VALUE");
                            }
                            gatt.writeDescriptor(dp);
//                            LogUtil.d("gatt write dp");

                        }
                    }
                }
            }
        }
        return success;
    }

    private BluetoothGattCharacteristic findNotifyCharacteristic(BluetoothGattService service, UUID characteristicUUID) {
        BluetoothGattCharacteristic characteristic = null;
        List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
        for (BluetoothGattCharacteristic c : characteristics) {
            if ((c.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0
                    && characteristicUUID.equals(c.getUuid())) {
                characteristic = c;
                break;
            }
        }
        if (characteristic != null)
            return characteristic;
        for (BluetoothGattCharacteristic c : characteristics) {
            if ((c.getProperties() & BluetoothGattCharacteristic.PROPERTY_INDICATE) != 0
                    && characteristicUUID.equals(c.getUuid())) {
                characteristic = c;
                break;
            }
        }
        return characteristic;
    }

    public void setBTWorkMode(int mode) {
        LogUtil.d("Enter:: setBTWorkMode---->" + mode);
        switch (mode) {
            case BLUETOOTH_OPTIONS_DISABLE:
                doUnconnect();
                bluetoothAdapter.disable();

                break;
            case BLUETOOTH_OPTIONS_TEMPERTURE_SENSOR:
                if (!bluetoothAdapter.isEnabled()) checkBluetooth();
                startScanTempSensor();
                break;
            case BLUETOOTH_OPTIONS_SMART_PHONE:
                if (!bluetoothAdapter.isEnabled()) checkBluetooth();
                stopScanTempSensor();
                break;
        }
        btMode = mode;
    }

    private void doUnconnect() {
        doWrite(new byte[]{4});

    }

    public void recyle() {
        context.unregisterReceiver(mReceiver);
    }


    public interface OnCataBluetoothManagerListener {
        void onTempSensorReady();

        void onTempSensorStop();

        void onTempSensorTempUpdate(int tempValue);

        void onTempSensorBatteryUpdate(int chargeStatus, int batteryLevel);
    }
}
