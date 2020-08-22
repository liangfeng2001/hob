package com.ekek.tfthobmodule.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.hardwaremodule.utils.DataParseUtil;

import java.util.List;
import java.util.UUID;

public class BTManager {
    private static final String TAG = "BTManager";
    private static final String TEMP_NAME = "Sensor Slave";
    private UUID UUID_SERVICE = UUID.fromString("66c926b2-189b-4a02-97e7-38ae7adce96d");
    private UUID UUID_MEDIUM_CHARACTER = UUID.fromString("97df461c-ae66-4284-8510-4c0b992dac72");
    private UUID UUID_READ_LEVEL = UUID.fromString("a8d3059e-9f5a-41c9-a865-f9247af44594");
    private UUID UUID_BATTERY_STATUS = UUID.fromString("d1250404-d897-4f21-b7d5-1367e0faeeb8");
    private UUID UUID_BATTERY_CHARGE_STATUS = UUID.fromString("41ca39ce-91b3-41e9-9542-d58a71ac7b9e");

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

    private static final int HANDLER_SET_NOTIFY_BATTERY_CHARGE_STATUS = 0;
    private static final int HANDLER_SET_NOTIFY_TEMP = 1;
    private static final int HANDLER_READ_TEMP = 2;
    private static final int HANDLER_READ_BATTERY_LEVEL = 3;

    private static final long PERIOD_READ_TEMP_BUSY = 1 * 1000;//工作时温度读取周期为1m
    private static final long PERIOD_READ_BATTERY_LEVEL = 5 * 1000;//工作时电量读取周期为5m

    private static final int READ_BATTERY_LEVEL_PROGRESS_STEP_ONE = 0;
    private static final int READ_BATTERY_LEVEL_PROGRESS_STEP_TWO = 1;
    private static final int READ_BATTERY_LEVEL_PROGRESS_STEP_THREE = 2;
    private static final int READ_BATTERY_LEVEL_PROGRESS_STEP_FOUR = 3;

    private BluetoothDevice mBluetoothDevice;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    private Context mContext;
    private boolean isBatteryLevelNotify = false;
    private boolean isChargeStatusNotify = false;
    private boolean isTempNotify = false;
    private int btMode = BLUETOOTH_OPTIONS_DISABLE;
    private int readBatteryLevelProgress = READ_BATTERY_LEVEL_PROGRESS_STEP_ONE;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_SET_NOTIFY_BATTERY_CHARGE_STATUS:
                    enableChargeStatusNotify();

                    break;
                case HANDLER_SET_NOTIFY_TEMP:
                    enableTempNotify();

                    break;
                case HANDLER_READ_TEMP:
                    doWrite(new byte[]{2});

                    break;



            }
        }
    };


    public BTManager(Context context) {

        mContext = context;
        initBT();

    }

    private void initBT() {
        android.bluetooth.BluetoothManager bluetoothManager=(BluetoothManager)mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter= bluetoothManager.getAdapter();

        if (bluetoothAdapter != null) {  //有蓝牙功能
            if (!bluetoothAdapter.isEnabled()) {  //蓝牙未开启

                bluetoothAdapter.enable();

            } else {
                if (btMode == BLUETOOTH_OPTIONS_TEMPERTURE_SENSOR)startScan();
                else if (btMode == BLUETOOTH_OPTIONS_SMART_PHONE);
            }
        } else {  //无蓝牙功能

        }
    }

    private void startScan(){
        bluetoothAdapter.stopLeScan(leScanCallback);
        bluetoothAdapter.startLeScan(leScanCallback);
    }

    private void stopScan(){
        bluetoothAdapter.stopLeScan(leScanCallback);
    }

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scandata) {
            LogUtil.d("device name ---->" + device.getName() + "----rssi---->" + rssi);
            if (device.getName() != null && device.getName().equals(TEMP_NAME)) {
                mBluetoothDevice = device;
                stopScan();
                mBluetoothGatt = device.connectGatt(mContext, false, mGattCallback);

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
                    handler.sendEmptyMessage(HANDLER_SET_NOTIFY_BATTERY_CHARGE_STATUS);
                    handler.sendEmptyMessageDelayed(HANDLER_SET_NOTIFY_TEMP,500);

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
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            byte[] data = characteristic.getValue();
            String uuid = characteristic.getUuid().toString();
            Log.e(TAG, "onCharacteristicChanged---UUID--->" + uuid);
            Log.e(TAG, "onCharacteristicChanged------>" + DataParseUtil.bytesToHexString(data,data.length));
            if (uuid.equals(UUID_MEDIUM_CHARACTER.toString())) {
                Log.e(TAG, "Temp value----->" + updateTempValue(data));
            }else if (uuid.equals(UUID_BATTERY_STATUS.toString())) {
                Log.e(TAG, "Battery value----->" + updateBatterValue(data));
            }else if (uuid.equals(UUID_BATTERY_CHARGE_STATUS.toString())) {
                Log.e(TAG, "Battery charge status----->" + ((data[0] == 0x00)?false:true));
            }


        }
    };


    private void doFirstConnect(BluetoothGatt gatt) {
        //往蓝牙数据通道的写入数据
        BluetoothGattService service = gatt.getService(UUID_SERVICE);
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID_READ_LEVEL);
        characteristic.setValue(new byte[]{0});
        gatt.writeCharacteristic(characteristic);
    }

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

    private void enableBatteryStatusNotify() {
        //if (!isBatteryLevelNotify) return;
        BluetoothGattCharacteristic ch44 = mBluetoothGatt.getService(UUID_SERVICE).getCharacteristic(UUID_BATTERY_STATUS);
        boolean result = enableNotification(mBluetoothGatt,ch44.getService().getUuid(),ch44.getUuid());
        //boolean result = enableIndications(ch44,UUID_BATTERY_STATUS);
        LogUtil.d("Enter:: enableBatteryStatusNotify----result---->" + result);
        if (result) isBatteryLevelNotify = true;
        else isBatteryLevelNotify = false;

    }

    private void disableBatteryStatusNotify() {
        //if (isBatteryLevelNotify) return;
        BluetoothGattCharacteristic ch44 = mBluetoothGatt.getService(UUID_SERVICE).getCharacteristic(UUID_BATTERY_STATUS);
        boolean result = disableNotification(mBluetoothGatt,ch44.getService().getUuid(),ch44.getUuid());
        if (result) isBatteryLevelNotify = false;
        else isBatteryLevelNotify = true;

    }

    private void enableChargeStatusNotify() {
        //if (!isChargeStatusNotify) return;
        BluetoothGattCharacteristic ch44 = mBluetoothGatt.getService(UUID_SERVICE).getCharacteristic(UUID_BATTERY_CHARGE_STATUS);
        boolean result = enableNotification(mBluetoothGatt,ch44.getService().getUuid(),ch44.getUuid());
        if (result) isChargeStatusNotify = true;
        else isChargeStatusNotify = false;

    }

    private void disableChargeStatusNotify() {
        //if (isChargeStatusNotify) return;
        BluetoothGattCharacteristic ch44 = mBluetoothGatt.getService(UUID_SERVICE).getCharacteristic(UUID_BATTERY_CHARGE_STATUS);
        boolean result = disableNotification(mBluetoothGatt,ch44.getService().getUuid(),ch44.getUuid());
        if (result) isChargeStatusNotify = false;
        else isChargeStatusNotify = true;

    }


    private void enableTempNotify() {
        // if (!isTempNotify) return;
        BluetoothGattCharacteristic ch44 = mBluetoothGatt.getService(UUID_SERVICE).getCharacteristic(UUID_MEDIUM_CHARACTER);
        boolean result = enableNotification(mBluetoothGatt,ch44.getService().getUuid(),ch44.getUuid());
        if (result) isTempNotify = true;
        else isTempNotify = false;

    }

    private void disableTempNotify() {
        //if (isTempNotify) return;
        BluetoothGattCharacteristic ch44 = mBluetoothGatt.getService(UUID_SERVICE).getCharacteristic(UUID_MEDIUM_CHARACTER);
        boolean result = disableNotification(mBluetoothGatt,ch44.getService().getUuid(),ch44.getUuid());
        if (result) isTempNotify = false;
        else isTempNotify = true;

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
                LogUtil.d("disableNotification---->" + success);
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



    private void doWrite(byte[] data) {
        BluetoothGattService service = mBluetoothGatt.getService(UUID_SERVICE);
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID_READ_LEVEL);
        characteristic.setValue(data);
        mBluetoothGatt.writeCharacteristic(characteristic);
    }

    private void doReadBatteryLevel() {
        BluetoothGattService service = mBluetoothGatt.getService(UUID_SERVICE);
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID_BATTERY_STATUS);
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    private void processReadBatteryLevel() {
        switch (readBatteryLevelProgress) {
            case READ_BATTERY_LEVEL_PROGRESS_STEP_ONE:
                doWrite(new byte[]{0x01});
                readBatteryLevelProgress = READ_BATTERY_LEVEL_PROGRESS_STEP_TWO;
                break;
            case READ_BATTERY_LEVEL_PROGRESS_STEP_TWO:
                doWrite(new byte[]{0x01});
                readBatteryLevelProgress = READ_BATTERY_LEVEL_PROGRESS_STEP_THREE;
                break;
            case READ_BATTERY_LEVEL_PROGRESS_STEP_THREE:
                doWrite(new byte[]{0x01});
                readBatteryLevelProgress = READ_BATTERY_LEVEL_PROGRESS_STEP_FOUR;
                break;
            case READ_BATTERY_LEVEL_PROGRESS_STEP_FOUR:
                doReadBatteryLevel();
                break;
        }
    }


    public interface OnBTManagerListener {
        void onTempSensorReady();

        void onTempSensorStop();

        void onTempSensorTempUpdate(int tempValue);

        void onTempSensorBatteryUpdate(int chargeStatus, int batteryLevel);
    }
}
