package com.ekek.hardwaremodule.protocol;


import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.commonmodule.utils.Logger;
import com.ekek.hardwaremodule.entity.CookerDataProcessRequest;
import com.ekek.hardwaremodule.entity.CookerHardwareResponse;
import com.ekek.hardwaremodule.entity.CookerMessage;
import com.ekek.hardwaremodule.entity.FTFDataModel;
import com.ekek.hardwaremodule.event.PowerSwitchStateChangedEvent;
import com.ekek.hardwaremodule.utils.DataParseUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Samhung on 2018/1/9.
 */

public class InductionCookerProtocol {
    public static final byte PROTOCOL_RECEIVE_HEAD_FIRST_BYTE = 0x54;
    public static final byte PROTOCOL_RECEIVE_HEAD_SECOND_BYTE = 0x58;
    public static final byte PROTOCOL_RECEIVE_HEAD_THIRD_BYTE = 0x54;
    public static final byte PROTOCOL_RECEIVE_HEAD_FOURTH_BYTE = 0x3A;
    public static final byte PROTOCOL_RECEIVE_END_SECOND_LAST_BYTE = 0x0D;
    public static final byte PROTOCOL_RECEIVE_END_LAST_BYTE = 0x0A;
    public static final int PROTOCOL_RECEIVE_DATA_SIZE = 27;//17:老协议长度, 21: 协议v2 长度 , 27: 协议v3 长度

    public static final byte PROTOCOL_SEND_HEAD_FIRST_BYTE = 0x52;
    public static final byte PROTOCOL_SEND_HEAD_SECOND_BYTE = 0x58;
    public static final byte PROTOCOL_SEND_HEAD_THIRD_BYTE = 0x54;
    public static final byte PROTOCOL_SEND_HEAD_FOURTH_BYTE = 0x3A;
    public static final byte PROTOCOL_SEND_END_SECOND_LAST_BYTE = 0x0D;
    public static final byte PROTOCOL_SEND_END_LAST_BYTE = 0x0A;
    public static final int PROTOCOL_SEND_DATA_SIZE = 29;//21:老协议长度，29：协议v2长度

    public static final int PROTOCOL_COOKER_WORK_MODE_POWER_OFF = 0x00;
    public static final int PROTOCOL_COOKER_WORK_MODE_FIRE_GEAR = 0x01;
    public static final int PROTOCOL_COOKER_WORK_MODE_FAST_TEMPERATURE = 0x02;
    public static final int PROTOCOL_COOKER_WORK_MODE_PRECISE_TEMPERATURE = 0x03;
    public static final int PROTOCOL_COOKER_WORK_MODE_SETTING = 0x04;
    public static final int PROTOCOL_COOKER_WORK_MODE_UNION = 0x05;

    public static final int PROTOCOL_STATE_POWER_SWITCH_00 = 0x00;
    public static final int PROTOCOL_STATE_POWER_SWITCH_11 = 0x11;
    public static final int PROTOCOL_STATE_POWER_SWITCH_ON = 0x55;
    public static final int PROTOCOL_STATE_POWER_SWITCH_88 = 0x88;
    public static final int PROTOCOL_STATE_POWER_SWITCH_99 = 0x99;
    public static final int PROTOCOL_STATE_POWER_SWITCH_OFF = 0xAA;
    public static final int PROTOCOL_STATE_POWER_SWITCH_OFF_2 = 0xBB;
    public static final int PROTOCOL_FLAG_COOKER_HIGH_TEMPERATURE = 0x08;
    public static final int PROTOCOL_FLAG_COOKER_HAVE_COOKER_0X01 = 0x01;
    public static final int PROTOCOL_FLAG_COOKER_HAVE_COOKER_0X02 = 0x02;
    public static final int PROTOCOL_FLAG_COOKER_HAVE_COOKER_0X03 = 0x03;
    public static final int PROTOCOL_FLAG_COOKER_PANEL_HIGH_TEMPERATURE = 0x20;
    private static volatile InductionCookerProtocol instance;
    private OnInductionCookerProtocolListener mListener;
    private Queue<CookerDataProcessRequest> requestQueue = new LinkedList<CookerDataProcessRequest>();
    private InductionCookerDataProcessThread mInductionCookerDataProcessThread;

    private InductionCookerProtocol(OnInductionCookerProtocolListener listener) {
        this.mListener = listener;
        if (mInductionCookerDataProcessThread == null) {
            mInductionCookerDataProcessThread = new InductionCookerDataProcessThread();
            mInductionCookerDataProcessThread.start();
        }else {
            mInductionCookerDataProcessThread.resumeThread();
        }

    }

    public static InductionCookerProtocol getIstance(OnInductionCookerProtocolListener listener) {
        if (instance == null) {
            synchronized (InductionCookerProtocol.class) {
                if (instance == null) {
                    instance = new InductionCookerProtocol(listener);
                }
            }
        }else {

            instance.mListener = listener;
            instance.initProtocol();

        }
        return instance;
    }

    public void requestProcessData(CookerDataProcessRequest request) {
        synchronized (requestQueue) {
            requestQueue.offer(request);
        }

    }

    private void initProtocol() {
        if (mInductionCookerDataProcessThread == null) {
            mInductionCookerDataProcessThread = new InductionCookerDataProcessThread();
            mInductionCookerDataProcessThread.start();
        }else {
            mInductionCookerDataProcessThread.resumeThread();
        }
    }

    private class InductionCookerDataProcessThread extends Thread {
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
                while (requestQueue.size() > 0) {
                    if (!isSupend) {
                        //LogUtil.d("Enter:: protocol process data");
                        CookerDataProcessRequest request = getRequestAndRemove();
                        processSerailDataFrame(request);
                        request.recyle();
                    }
                }
            }
        }
    }

    private void processSerailDataFrame(CookerDataProcessRequest request) {
        byte[] data = request.getCommandData();
        boolean checksumResult = doChecksum(data);
        CookerHardwareResponse response = new CookerHardwareResponse();
        response.setChecksumResult(checksumResult);
        int powerSwitchStateByte = data[4] & 0xFF;
        int oldPowerSwitchState = GlobalVars.getInstance().getPowerSwitchState();
        int newPowerSwitchState = CookerHardwareResponse.POWER_SWITCH_UNKNOWN;

        switch (powerSwitchStateByte) {
            case PROTOCOL_STATE_POWER_SWITCH_00:
                newPowerSwitchState = CookerHardwareResponse.POWER_SWITCH_00;
                break;
            case PROTOCOL_STATE_POWER_SWITCH_11:
                newPowerSwitchState = CookerHardwareResponse.POWER_SWITCH_11;
                break;
            case PROTOCOL_STATE_POWER_SWITCH_ON:
                newPowerSwitchState = CookerHardwareResponse.POWER_SWITCH_ON;
                break;
            case PROTOCOL_STATE_POWER_SWITCH_88:
                newPowerSwitchState = CookerHardwareResponse.POWER_SWITCH_88;
                break;
            case PROTOCOL_STATE_POWER_SWITCH_99:
                newPowerSwitchState = CookerHardwareResponse.POWER_SWITCH_99;
                break;
            case PROTOCOL_STATE_POWER_SWITCH_OFF:
                newPowerSwitchState = CookerHardwareResponse.POWER_SWITCH_OFF;
                break;
            case PROTOCOL_STATE_POWER_SWITCH_OFF_2:
                newPowerSwitchState = CookerHardwareResponse.POWER_SWITCH_OFF_2;
                break;
        }

        response.setPowerSwitchState(newPowerSwitchState);
        if (newPowerSwitchState != oldPowerSwitchState) {
            EventBus.getDefault().post(new PowerSwitchStateChangedEvent(
                    oldPowerSwitchState,
                    newPowerSwitchState));
            Logger.getInstance().i("new PowerSwitchStateChangedEvent(" + getPowerSwitchStateString(oldPowerSwitchState) + ", " + getPowerSwitchStateString(newPowerSwitchState) + ")");
            GlobalVars.getInstance().setPowerSwitchState(newPowerSwitchState);
        }

        response.setRawData(data);
        for (int i = 0; i < 6;i++) {
//            LogUtil.d("Enter:: samhung processSerailDataFrame");
            //CookerMessage cookerMessage = new CookerMessage(data[i * 2 + 5] & 0xff ,data[i * 2 + 6] & 0xff);
            CookerMessage cookerMessage = new CookerMessage(data[i * 3 + 5] & 0xff ,data[i * 3 + 6] & 0xff,data[i * 3 + 7] & 0xff);
            if (i == 0)response.setaCookerMessage(cookerMessage);
            if (i == 1)response.setbCookerMessage(cookerMessage);
            if (i == 2)response.setcCookerMessage(cookerMessage);
            if (i == 3)response.setdCookerMessage(cookerMessage);
            if (i == 4)response.seteCookerMessage(cookerMessage);
            if (i == 5)response.setfCookerMessage(cookerMessage);
        }
        //CookerMessage aCookerMessage = new CookerMessage(data[5] & 0xff ,data[6] & 0xff);
        mListener.onInductionCookerProtocolDataReady(response);

    }

    private String getPowerSwitchStateString(int state) {
        switch (state) {
            case CookerHardwareResponse.POWER_SWITCH_UNKNOWN:
                return "POWER_SWITCH_UNKNOWN";
            case CookerHardwareResponse.POWER_SWITCH_ON:
                return "POWER_SWITCH_ON";
            case CookerHardwareResponse.POWER_SWITCH_OFF:
                return "POWER_SWITCH_OFF";
            case CookerHardwareResponse.POWER_SWITCH_OFF_2:
                return "POWER_SWITCH_OFF_2";
            case CookerHardwareResponse.POWER_SWITCH_00:
                return "POWER_SWITCH_00";
            case CookerHardwareResponse.POWER_SWITCH_11:
                return "POWER_SWITCH_11";
            case CookerHardwareResponse.POWER_SWITCH_88:
                return "POWER_SWITCH_88";
            case CookerHardwareResponse.POWER_SWITCH_99:
                return "POWER_SWITCH_99";
        }
        return "POWER_SWITCH_UNKNOWN";
    }

    private boolean doChecksum(byte[] data) {
        boolean result = false;
        int crc = 0x00;
        for (int i = 4 ; i < data.length - 6;i++) {
            crc = crc + (data[i] & 0xff);
        }
        byte high = (byte) (crc >> 8);
        byte low = (byte) (crc & 0xff);
        if (high == data[23] && low == data[24]) result = true;

        return result;
    }

    private CookerDataProcessRequest getRequestAndRemove() {
        synchronized (requestQueue) {
            CookerDataProcessRequest request = requestQueue.poll();
            return request;
        }
    }



    public byte[] getCookerSendCommandData(FTFDataModel model) {
        byte[] data = new byte[PROTOCOL_SEND_DATA_SIZE];
        data[0] = PROTOCOL_SEND_HEAD_FIRST_BYTE;
        data[1] = PROTOCOL_SEND_HEAD_SECOND_BYTE;
        data[2] = PROTOCOL_SEND_HEAD_THIRD_BYTE;
        data[3] = PROTOCOL_SEND_HEAD_FOURTH_BYTE;

        data[4] = model.getaMode();
        data[5] = model.getaFireGear();
        data[6] = model.getaTempGear();

        data[7] = model.getbMode();
        data[8] = model.getbFireGear();
        data[9] = model.getbTempGear();

        data[10] = model.getcMode();
        data[11] = model.getcFireGear();
        data[12] = model.getcTempGear();

        data[13] = model.getdMode();
        data[14] = model.getdFireGear();
        data[15] = model.getdTempGear();

        data[16] = model.geteMode();
        data[17] = model.geteFireGear();
        data[18] = model.geteTempGear();

        data[19] = model.getfMode();
        data[20] = model.getfFireGear();
        data[21] = model.getfTempGear();



        data[22] = model.getTempSensorValue();//temp sensor
        data[23] = model.getFanGear();//fan gear
       if(GlobalVars.getInstance().isPause()) {
           data[23]=(byte)(data[23]&0xf0);
        //   LogUtil.d("liang is pause");
       }else {
     //      LogUtil.d("liang is not  pause");
       }
        data[24] = model.getLightGear();//light gear

        int crc = getFtfDataCrc(data);
        data[25] = (byte) (crc >> 8);//crc data high
        data[26] = (byte) (crc & 0xff);//crc data low

        data[27] = PROTOCOL_SEND_END_SECOND_LAST_BYTE;
        data[28] = PROTOCOL_SEND_END_LAST_BYTE;


       /* data[13] = 0x00;//D cooker
        data[14] = 0x00;
        data[15] = 0x00;

        data[16] = 0x00;//bluetooth temp data

        int crc = getFtfDataCrc(data);
        data[17] = (byte) (crc >> 8);//crc data high
        data[18] = (byte) (crc & 0xff);//crc data low


        data[19] = PROTOCOL_SEND_END_SECOND_LAST_BYTE;
        data[20] = PROTOCOL_SEND_END_LAST_BYTE;*/

        String rawData = DataParseUtil.bytesToHexString(data,data.length);
        LogUtil.d("rawdata  send---->" + rawData);
        return data;

    }

    private int getFtfDataCrc(byte[] data) {
        int crc = 0x00;
        for (int i = 4 ; i < 18;i++) {
            crc = crc + (data[i] & 0xff);
        }
        return crc;
    }



    public void recyle() {
        if (mInductionCookerDataProcessThread != null) {
            mInductionCookerDataProcessThread.supendThread();
        }
    }

    public static abstract interface OnInductionCookerProtocolListener {
        public abstract void onInductionCookerProtocolDataReady(CookerHardwareResponse response);
    }
}
