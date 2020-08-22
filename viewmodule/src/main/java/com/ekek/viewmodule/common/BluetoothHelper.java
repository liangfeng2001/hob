package com.ekek.viewmodule.common;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Environment;

import com.ekek.commonmodule.GlobalCons;
import com.ekek.commonmodule.utils.ContextUtils;
import com.ekek.commonmodule.utils.Logger;
import com.ekek.commonmodule.utils.NativeUtil;
import com.ekek.viewmodule.events.BluetoothEvent;
import com.ekek.viewmodule.product.ProductManager;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class BluetoothHelper {

    // Singleton
    private static BluetoothHelper instance;
    public static BluetoothHelper getInstance(Context context) {
        if (instance == null) {
            instance = new BluetoothHelper(context);
        }
        return instance;
    }

    // Constants
    private static final String BT_NAME = "CATA";
    private static final int CHANNEL_BT_UPGRADE = 29;
    private static final byte[] EKEK_TAG = new byte[] {0x45, 0x4B, 0x65, 0x6B, 0x42, 0x54};
    private static final int CHECK_CODE_SEED = 0x5555;
    private static final int STRUCTURE_VERSION = 1;
    private static final int ORG_KSO = 0;
    private static final int ORG_CATA = 1;
    private static final int ORG_HAIER = 2;
    private static final int COMMAND_FAILED = 0x00;
    private static final int COMMAND_OK = 0x01;

    // Fields
    private Context context;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private String apkFileName;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
            switch (state) {
                case BluetoothAdapter.STATE_TURNING_ON:
                    Logger.getInstance().i("BluetoothAdapter.STATE_TURNING_ON");
                    break;
                case BluetoothAdapter.STATE_ON:
                    Logger.getInstance().i("BluetoothAdapter.STATE_ON");
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    Logger.getInstance().i("BluetoothAdapter.STATE_TURNING_OFF");
                    break;
                case BluetoothAdapter.STATE_OFF:
                    Logger.getInstance().i("BluetoothAdapter.STATE_OFF");
                    break;
            }

            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            switch (action){
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    Logger.getInstance().i("BluetoothDevice.ACTION_ACL_CONNECTED");
                    onActionAclConnected(device);
                    break;
                case BluetoothDevice.ACTION_PAIRING_REQUEST:
                    Logger.getInstance().i("BluetoothDevice.ACTION_PAIRING_REQUEST");
                    onActionPairingRequest(device);
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    Logger.getInstance().i("BluetoothDevice.ACTION_ACL_DISCONNECTED");
                    onActionAclDisconnected(device);
                    break;
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    Logger.getInstance().i("BluetoothAdapter.ACTION_STATE_CHANGED");
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    Logger.getInstance().i("BluetoothDevice.ACTION_FOUND");
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED:
                    Logger.getInstance().i("BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED");
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    Logger.getInstance().i("BluetoothAdapter.ACTION_DISCOVERY_FINISHED");
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    Logger.getInstance().i("BluetoothAdapter.ACTION_DISCOVERY_STARTED");
                    break;
            }
        }
    };

    // Constructors
    public BluetoothHelper(Context context) {
        this.context = context;
        registerReceiver(context);
    }


    // Public functions
    public void setDiscoverableTimeout(int timeout) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            try {
                if (!adapter.isEnabled()) {
                    adapter.enable();
                }
                Method setDiscoverableTimeout = BluetoothAdapter.class.getMethod(
                        "setDiscoverableTimeout",
                        int.class);
                setDiscoverableTimeout.setAccessible(true);
                Method setScanMode =BluetoothAdapter.class.getMethod(
                        "setScanMode",
                        int.class,
                        int.class);
                setScanMode.setAccessible(true);

                setDiscoverableTimeout.invoke(adapter, timeout);
                setScanMode.invoke(adapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE,timeout);
            } catch (Exception e) {
                Logger.getInstance().e(e);
            }
        }

    }
    public void closeDiscoverableTimeout() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            try {
                Method setDiscoverableTimeout = BluetoothAdapter.class.getMethod(
                        "setDiscoverableTimeout",
                        int.class);
                setDiscoverableTimeout.setAccessible(true);
                Method setScanMode =BluetoothAdapter.class.getMethod(
                        "setScanMode",
                        int.class,
                        int.class);
                setScanMode.setAccessible(true);

                setDiscoverableTimeout.invoke(adapter, 1);
                setScanMode.invoke(adapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE,1);
            } catch (Exception e) {
                Logger.getInstance().e(e);
            }
        }

    }
    public String getBluetoothAddress() {
        return bluetoothAdapter.getAddress();
    }

    // Private functions
    private void registerReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_OFF");
        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_ON");
        context.registerReceiver(receiver, intentFilter);
    }
    private void onActionAclConnected(BluetoothDevice device) {
        Logger.getInstance().i("onActionAclConnected(" + device.getAddress() + ") BondState=" + device.getBondState());
        try {
            Method listenMethod = bluetoothAdapter.getClass().getMethod(
                    "listenUsingRfcommOn",
                    new Class[]{int.class});
            BluetoothServerSocket serverSocket = ( BluetoothServerSocket) listenMethod.invoke(
                    bluetoothAdapter,
                    new Object[]{CHANNEL_BT_UPGRADE});
            if (serverSocket != null) {
                ManipulateBluetoothThread thread = new ManipulateBluetoothThread(serverSocket);
                thread.start();
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            Logger.getInstance().e(e);
        }
    }
    private void onActionPairingRequest(BluetoothDevice device) {
        Logger.getInstance().i("onActionPairingRequest(" + device.getAddress() + ")");
    }
    private void onActionAclDisconnected(BluetoothDevice device) {
        Logger.getInstance().i("onActionAclDisconnected(" + device.getAddress() + ")");
    }
    private void interactWithBluetooth(BluetoothSocket socket) throws IOException {
        OutputStream os = socket.getOutputStream();
        sendBasicInfo(os);
        InputStream is = socket.getInputStream();
        if (is.read() != COMMAND_OK) {
            EventBus.getDefault().post(new BluetoothEvent(BluetoothEvent.ORDER_FAILED));
            return;
        }

        if (!receiveApkFile(is, os)) {
        }
    }
    private void sendBasicInfo(OutputStream os) throws IOException {

        // send flag and structure version
        os.write(EKEK_TAG);
        os.write(STRUCTURE_VERSION);

        // send major and minor classification
        os.write(ORG_CATA);
        os.write(ProductManager.PRODUCT_TYPE);

        // send model info
        String model = ProductManager.getModelInfo();
        os.write(model.length());
        os.write(model.getBytes());

        // send software version
        String appVersion = ContextUtils.getVersionName(context);
        os.write(appVersion.length());
        os.write(appVersion.getBytes());

        // send core version
        String sysVersion = GlobalCons.VERSION_DISPLAY_UNKNOWN;
        if (GlobalCons.BRAND_EKEK.equals(Build.BRAND)) {
            sysVersion = Build.DISPLAY;
        }
        os.write(sysVersion.length());
        os.write(sysVersion.getBytes());
    }
    private boolean receiveApkFile(InputStream is, OutputStream os) throws IOException {
        String apkVersion = readString(is);
        int totalSize = readInt(is);
        EventBus.getDefault().post(new BluetoothEvent(
                BluetoothEvent.ORDER_START,
                totalSize,
                0,
                apkVersion));

        int receivedSize = 0;
        int receivedPercent = 0;
        int calcCheckCode = CHECK_CODE_SEED;

        OutputStream fileOS = getOutputFileStream(apkVersion);
        while (true) {
            int len = readInt(is);
            if (len <= 0) {
                os.write(COMMAND_OK);
                break;
            }

            byte[] buffer = new byte[len];
            int count = 0;
            int receivedAlready = receivedSize;
            while (count < len) {
                count += is.read(buffer, count, len - count);
                receivedSize = receivedAlready + count;
                int percent = (int)(receivedSize * 100.0 / totalSize);
                if (percent > receivedPercent) {
                    receivedPercent = percent;
                    EventBus.getDefault().post(new BluetoothEvent(
                            BluetoothEvent.ORDER_REPORT_PROGRESS,
                            percent));
                }
            }

            int checkCode = readInt(is);
            calcCheckCode = genCheckCode(calcCheckCode, buffer);
            Logger.getInstance().d("" + len + " " + checkCode + " " + calcCheckCode + " " + Arrays.toString(buffer));
            if (checkCode != calcCheckCode) {
                os.write(COMMAND_FAILED);
                return false;
            } else {
                fileOS.write(buffer);
                os.write(COMMAND_OK);
            }

        }

        fileOS.flush();
        fileOS.close();
        EventBus.getDefault().post(new BluetoothEvent(BluetoothEvent.ORDER_FINISHED, apkFileName));
        return true;
    }
    private String readString(InputStream is) throws IOException {
        int len = is.read();
        byte[] data = new byte[len];
        if (is.read(data) != len) {
            return null;
        }
        return new String(data);
    }
    private int readInt(InputStream is) throws IOException {
        byte[] data = new byte[4];
        if (is.read(data) != 4) {
            return -1;
        }

        return NativeUtil.bytes2Int(data);
    }
    private int genCheckCode(int result, byte[] buffer) {
        for (int i = 0; i < buffer.length; i++) {
            int digits = (i % 4) * 2;
            result = (result ^ (buffer[i] << digits));
        }
        return result;
    }
    private OutputStream getOutputFileStream(String apkVersion) throws IOException {
        apkFileName = getOutputFileName(apkVersion);
        File outputFile = new File(apkFileName);
        if (!outputFile.createNewFile()) {
            Logger.getInstance().e("createNewFile failed: " + apkFileName);
        }
        OutputStream fileOs = new FileOutputStream(outputFile);
        return fileOs;
    }
    private String getOutputFileName(String apkVersion) {
        File dir = Environment.getExternalStorageDirectory();
        File downloadDir = new File(dir, "Download");
        if (!downloadDir.exists()) {
            if (!downloadDir.mkdirs()) {
                Logger.getInstance().e("Failed to create " + downloadDir.getAbsolutePath());
            }
        }

        File eKekDir = new File(downloadDir, "EKek");
        if (!eKekDir.exists()) {
            if (!eKekDir.mkdirs()) {
                Logger.getInstance().e("Failed to create " + eKekDir.getAbsolutePath());
            }
        }

        File[] files = eKekDir.listFiles();
        for (File file: files) {
            if (file.isFile()) {
                if (!file.delete()) {
                    Logger.getInstance().e("Failed to delete " + file.getAbsolutePath());
                }
            }
        }

        String fileName = eKekDir.getAbsolutePath();
        if (fileName.endsWith("/")) {
            fileName += apkVersion + ".apk";
        } else {
            fileName += "/" + apkVersion + ".apk";
        }
        return fileName;
    }

    // Classes
    private class ManipulateBluetoothThread extends Thread {

        BluetoothServerSocket serverSocket;

        ManipulateBluetoothThread(BluetoothServerSocket serverSocket) {

            this.serverSocket = serverSocket;
        }

        @Override
        public void run() {
            BluetoothSocket socket;
            try {
                socket = serverSocket.accept();
                if (socket != null) {
                    interactWithBluetooth(socket);
                }
                serverSocket.close();
            } catch (IOException e) {
                Logger.getInstance().e(e);
            }
        }
    }
}