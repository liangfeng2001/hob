package com.ekek.tfthobmodule.bluetooth;

import com.ekek.tfthobmodule.CataTFTHobApplication;
import com.inuker.bluetooth.library.BluetoothClient;

public class ClientManager {

    private static BluetoothClient mClient;

    public static BluetoothClient getClient() {
        if (mClient == null) {
            synchronized (ClientManager.class) {
                if (mClient == null) {
                    mClient = new BluetoothClient(CataTFTHobApplication.getInstance());
                }
            }
        }
        return mClient;
    }
}
