package com.ekek.hardwaremodule.serial;


import com.ekek.hardwaremodule.entity.RequestAction;
import com.ekek.hardwaremodule.entity.RequestResultCode;
import com.ekek.hardwaremodule.provider.SerialPort;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Samhung on 2017/2/8.
 */

public class SerialManager extends HardwareManager {
    private static volatile SerialManager instance;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private SerialPort mSerialPort;
    private SerialReadThread mSerialReadThread ;
    private String mDevPath;
    private int mBaudrate,mFlags;
    private SerialManager(String devPath, int baudrate , OnHardwareManagerListener listener) {
        this.listener = listener;
        initSerialPort(devPath , baudrate);
    }

    private void initSerialPort(String devPath, int baudrate) {
        int flags = 0;//O_RDWR ,read and write
        if ( (devPath.length() == 0) || (baudrate == -1)) {
            listener.onHardwareError(RequestAction.REQUEST_ACTION_BRD_RFID_OPEN_DEVICE,SerialError.SERIAL_ERROR_INVALID_PARAMETER);

            return;
        }
        mDevPath = devPath;
        mBaudrate = baudrate;
        mFlags = flags;
        try {
            File deviceFile = new File(devPath);
            if (!deviceFile.exists()) {
                listener.onHardwareError(RequestAction.REQUEST_ACTION_BRD_RFID_OPEN_DEVICE,SerialError.SERIAL_ERROR_SERIAL_PORT_NO_EXIST);

                return;
            }
            mSerialPort = new SerialPort(deviceFile , baudrate , flags);
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();


                if (mSerialReadThread == null) {
                    mSerialReadThread = new SerialReadThread();
                    //mSerialReadThread.setPriority(Thread.MAX_PRIORITY);
                    mSerialReadThread.start();
                }else {
                    mSerialReadThread.resumeThread();
                }

         /*   if (mSerialReadThread == null) {
                LogUtil.d("---------2222-----------2222---------");
                mSerialReadThread = new SerialReadThread();
                //mSerialReadThread.setPriority(Thread.MAX_PRIORITY);
                mSerialReadThread.resumeThread();
                mSerialReadThread.start();
            }else {
                LogUtil.d("---------111111-----------1111---------");
                mSerialReadThread.resumeThread();
            }*/

            listener.onHardwareResponse(RequestAction.REQUEST_ACTION_BRD_RFID_OPEN_DEVICE, RequestResultCode.REQUEST_RESULT_CODE_BRD_RFID_DEVICE_OPEN_SUCCESS);
        } catch (SecurityException e) {
            listener.onHardwareError(RequestAction.REQUEST_ACTION_BRD_RFID_OPEN_DEVICE,SerialError.SERIAL_ERROR_SERIAL_NO_PERMISSION);

            e.printStackTrace();
        }catch (IOException e) {
            listener.onHardwareError(RequestAction.REQUEST_ACTION_BRD_RFID_OPEN_DEVICE,SerialError.SERIAL_ERROR_IO_ERROR);

            e.printStackTrace();
        }
    }

    public static SerialManager getIstance(String devPath, int baudrate , OnHardwareManagerListener listener) {
        if (instance == null) {
            synchronized (SerialManager.class) {
                if (instance == null) {
                    instance = new SerialManager(devPath , baudrate ,listener);
                }
            }
        }else {
            instance.listener = listener;
            instance.initSerialPort(devPath, baudrate);
        }
        return instance;
    }


    @Override
    public void send(byte[] data) {
        //LogUtil.d("Enter:: serial send");
        if (mOutputStream != null) {
            try {
                mOutputStream.write(data);
                //LogUtil.d("Enter:: send data -------------serial");
            } catch (IOException e) {
                listener.onHardwareError(RequestAction.REQUEST_ACTION_BRD_RFID_HARDWARE_RESPONSE,SerialError.SERIAL_ERROR_IO_ERROR);
                e.printStackTrace();
            }
        }else {
            //LogUtil.d("serial send mOutputStream == null");
        }
    }

    @Override
    public void pauseHardware() {
        if (mSerialReadThread != null)mSerialReadThread.supendThread();
    }

    @Override
    public void resumeHardware() {
        if (mSerialReadThread != null)mSerialReadThread.resumeThread();
    }

    @Override
    public void closeHardware() {
        closeSerial();
        listener.onHardwareResponse(RequestAction.REQUEST_ACTION_BRD_RFID_CLOSE_DEVICE, RequestResultCode.REQUEST_RESULT_CODE_BRD_RFID_DEVICE_CLOSE_SUCCESS);
    }

    @Override
    public void setOnHardwareManagerListener(OnHardwareManagerListener listener) {
        this.listener = listener;
    }


    private void closeSerial() {
        mSerialPort.close();
        if (mSerialReadThread != null) {
            mSerialReadThread.supendThread();
        }

       /* try {
            if (mInputStream != null) {
                mInputStream.close();

            }
            if (mOutputStream != null) {
                mOutputStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    private void startSerial() {
        initSerialPort(mDevPath,mBaudrate);
    }


    private void startSerial(String devPath , int baudrate) {
        initSerialPort(devPath , baudrate);
    }

    private class SerialReadThread extends Thread {
        private boolean isSupend = false;
        public void resumeThread() {
            isSupend = false;
        }

        public void supendThread() {
            isSupend = true;
        }

        @Override
        public void run() {
            super.run();
            while(!isInterrupted()) {
                if (!isSupend) {
                    int size;
                    try {
                        byte[] buffer = new byte[32];
                        if (mInputStream == null || listener == null) {
                            continue;
                        }
                        size = mInputStream.read(buffer);
                        //LogUtil.d("serial data--->" + DataParseUtil.bytesToHexString(buffer,size));
                       // LogUtil.d("serial---size->" + size + "---isSupend--->" + isSupend + "id---->" + mSerialReadThread.getId());
                        if (size > 0 && !isSupend) {
                            listener.onHardwareReceived(buffer, size);
                        }else {
                            size = 0;
                            buffer = null;
                            continue;
                        }
                        size = 0;
                        buffer = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                }

            }


        }
    }


/*    public static abstract interface OnSerialManagerListener {
        public abstract void onSerialReceived(final byte[] buffer, final int size);
        public abstract void onSerialError(int error);
    }*/

}
