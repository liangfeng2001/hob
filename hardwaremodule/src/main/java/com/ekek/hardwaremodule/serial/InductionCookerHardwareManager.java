package com.ekek.hardwaremodule.serial;

import android.content.Context;

import com.ekek.commonmodule.utils.Logger;
import com.ekek.hardwaremodule.constants.CookerConstant;
import com.ekek.hardwaremodule.entity.AllCookerData;
import com.ekek.hardwaremodule.entity.CookerData;
import com.ekek.hardwaremodule.entity.CookerDataProcessRequest;
import com.ekek.hardwaremodule.entity.CookerHardwareResponse;
import com.ekek.hardwaremodule.entity.FTFDataModel;
import com.ekek.hardwaremodule.power.PowerConstant;
import com.ekek.hardwaremodule.power.PowerLimitManager;
import com.ekek.hardwaremodule.protocol.InductionCookerProtocol;
import com.ekek.hardwaremodule.utils.DataParseUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Samhung on 2017/12/28.
 */

public class InductionCookerHardwareManager implements InductionCookerProtocol.OnInductionCookerProtocolListener, HardwareManager.OnHardwareManagerListener {
    private static volatile InductionCookerHardwareManager instance;
    private HardwareManager mHardwareManager;
    private InductionCookerProtocol mInductionCookerProtocol;
    private Context mContext;
    private Builder builder;
    private OnInductionCookerHardwareManagerListener mListener;
    private CommandSendThread mCommandSendThread;
    private Queue<CookerDataProcessRequest> requestQueue = new LinkedList<CookerDataProcessRequest>();
    private PowerLimitManager mPowerLimitManager;
    private FTFDataModel lastSuccessModel;


    public static Builder init(Context context){
        return new Builder(context);
    }


    private InductionCookerHardwareManager(Context context , Builder builder , OnInductionCookerHardwareManagerListener listener) {
        this.mContext = context;
        this.builder = builder;
        this.mListener = listener;
        initInductionCookerManager();
    }


    private static InductionCookerHardwareManager getIstance(Context context , Builder builder , OnInductionCookerHardwareManagerListener listener) {
        if (instance == null) {
            synchronized (InductionCookerHardwareManager.class) {
                if (instance == null) {
                    instance = new InductionCookerHardwareManager(context,builder,listener);
                }
            }
        }else {
            instance.mContext = context;
            instance.builder = builder;
            instance.mListener = listener;
            instance.reInitInductionCookerManager();

        }
        return instance;
    }

    private void initInductionCookerManager() {
        mInductionCookerProtocol = InductionCookerProtocol.getIstance(this);
        mPowerLimitManager = PowerLimitManager.getIstance(builder.cookerType,builder.totalPower);
        mHardwareManager = SerialManager.getIstance(builder.serialPortPath,builder.serialBaudrate,this);
        if (mCommandSendThread == null) {
            mCommandSendThread = new CommandSendThread();
            mCommandSendThread.start();
        }else {
            mCommandSendThread.resumeThread();
        }

    }

    private void reInitInductionCookerManager() {
        mInductionCookerProtocol = InductionCookerProtocol.getIstance(this);
        mPowerLimitManager = PowerLimitManager.getIstance(builder.cookerType,builder.totalPower);
        mHardwareManager = SerialManager.getIstance(builder.serialPortPath,builder.serialBaudrate,this);
        if (mCommandSendThread == null) {
            mCommandSendThread = new CommandSendThread();
            mCommandSendThread.start();
        }else {
            mCommandSendThread.resumeThread();
        }

    }

    @Override
    public void onHardwareReceived(byte[] data, int size) {
        for (int i = 0;i < size; i++) {
            processHardwareData(data[i]);
        }

    }


    @Override
    public void onHardwareResponse(int action, int code) {

    }

    @Override
    public void onHardwareError(int action, int error) {

    }

    private int processIndex = 0;
    private CookerDataFrame mCookerDataFrame;
    private void processHardwareData(byte data) {
        switch (processIndex) {
            case 0://T
                if (data == InductionCookerProtocol.PROTOCOL_RECEIVE_HEAD_FIRST_BYTE) {
                    processIndex++;
                }
                break;
            case 1:
                if (data == InductionCookerProtocol.PROTOCOL_RECEIVE_HEAD_SECOND_BYTE) {
                    processIndex++;
                }
                break;
            case 2:
                if (data == InductionCookerProtocol.PROTOCOL_RECEIVE_HEAD_THIRD_BYTE) {
                    processIndex++;
                }
                break;
            case 3:
                if (data == InductionCookerProtocol.PROTOCOL_RECEIVE_HEAD_FOURTH_BYTE) {
                    processIndex++;

                    mCookerDataFrame = new CookerDataFrame();
                    mCookerDataFrame.dataFrameList.add(InductionCookerProtocol.PROTOCOL_RECEIVE_HEAD_FIRST_BYTE);
                    mCookerDataFrame.dataFrameList.add(InductionCookerProtocol.PROTOCOL_RECEIVE_HEAD_SECOND_BYTE);
                    mCookerDataFrame.dataFrameList.add(InductionCookerProtocol.PROTOCOL_RECEIVE_HEAD_THIRD_BYTE);
                    mCookerDataFrame.dataFrameList.add(InductionCookerProtocol.PROTOCOL_RECEIVE_HEAD_FOURTH_BYTE);
                }
                break;
            case 4:
                mCookerDataFrame.dataFrameList.add(data);
                if (mCookerDataFrame.dataFrameList.size() == InductionCookerProtocol.PROTOCOL_RECEIVE_DATA_SIZE) {
                    processIndex = 0;
                    requestProtocolProcess();
                }
                break;

        }


    }

    private void requestProtocolProcess() {
        int size = mCookerDataFrame.dataFrameList.size();
        byte[] data = new byte[size];
        for (int i = 0; i < size; i++) {
            data[i] = mCookerDataFrame.dataFrameList.get(i);
        }

        CookerDataProcessRequest request = new CookerDataProcessRequest();
        //request.setSn(TimeUtil.getTimeStamp());
        request.setCommandData(data);
        if (mInductionCookerProtocol == null) {

        }
        mInductionCookerProtocol.requestProcessData(request);
        mCookerDataFrame.recyle();
    }

    private class CookerDataFrame {
        public List<Byte> dataFrameList;
        public CookerDataFrame() {
            dataFrameList = new ArrayList<>();
        }

        public void recyle() {
            dataFrameList.clear();
            dataFrameList = null;
        }

    }


    private class CommandSendThread extends Thread {
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
                while (requestQueue.size() > 0 ) {
                    if (!isSupend) {

                        CookerDataProcessRequest request = getRequestAndRemove();
                        if (request != null) {
                            mHardwareManager.send(request.getCommandData());
                            Logger.getInstance().d("Original sent: " + DataParseUtil.bytesToHexString(request.getCommandData(),request.getCommandData().length));
                        }
                    }
                }
            }
        }
    }

    private CookerDataProcessRequest getRequestAndRemove() {
        synchronized(requestQueue) {
            CookerDataProcessRequest request = requestQueue.poll();
            return request;
        }
    }


    private void addRequsetInQueue(CookerDataProcessRequest request) {
        synchronized(requestQueue) {
            requestQueue.offer(request);
        }
    }


    public void sendCookerData(FTFDataModel model) {
        CookerDataProcessRequest request = new CookerDataProcessRequest();
        request.setCommandData(mInductionCookerProtocol.getCookerSendCommandData(model));
        addRequsetInQueue(request);
    }

    public int requestSendCookerData(AllCookerData data) {
        if (mPowerLimitManager.updateCookerPower(data) == 0) {//success
            //data = changeCookerDataForPowerMatch(data);
            FTFDataModel model = new FTFDataModel(
                    (byte)data.getaMode(),(byte)data.getaFireValue(),(byte)data.getaTempValue(),
                    (byte)data.getbMode(),(byte)data.getbFireValue(),(byte)data.getbTempValue(),
                    (byte)data.getcMode(),(byte)data.getcFireValue(),(byte)data.getcTempValue(),
                    (byte)data.getdMode(),(byte)data.getdFireValue(),(byte)data.getdTempValue(),
                    (byte)data.geteMode(),(byte)data.geteFireValue(),(byte)data.geteTempValue(),
                    (byte)data.getfMode(),(byte)data.getfFireValue(),(byte)data.getfTempValue(),
                    (byte)data.getBluetoothTempValue(),(byte)data.getHoodValue(),(byte)data.getLightValue()

            );
            sendCookerData(model);
            return CookerConstant.REQUEST_SEND_COOKER_DATA_RESULT_SUCCESS;

        }else {
            return CookerConstant.REQUEST_SEND_COOKER_DATA_RESULT_FAIL; //fail
        }


    }

    private AllCookerData changeCookerDataForPowerMatch(AllCookerData data) {
        if (builder.cookerType == CookerConstant.COOKER_TYPE_CATA_60) {//B C
            if (data.getbFireValue() == 0x0a && (data.getcFireValue() >= 0x01 && data.getcFireValue() <= 0x07 )) {
                data.setbFireValue(0x09);
            } else if (data.getcFireValue() >= 0x09 && (data.getbFireValue() >= 0x01 && data.getbFireValue() <= 0x07 )) {
                data.setcFireValue(0x08);
            }

        }

        return data;
    }

    /**
     * 请求发送demo模式的数据，根据realData 进行功率限制，如果没超出功率，则发送demoData 到单片机，并返回发送成功，即0；如果超出功率，不发送任何数据，并且返回发送失败，即1
     * @param realData : 炉头真实设置数据，用于总功率计算并限制功率，如没超出功率限制，则返回发送成功，即是0，反之，则返回1
     * @param demoData : demo 数据，期望发给单片机的数据
     * @param needPowerLimit : 是否需要限制功率，如true：需要功率限制，则会根据realData计算总功率；如false：不需要功率限制，直接发送demoData到单片机，并返回发送成功。
     * @return 返回数据结果，如0表示发送成功，如1发送失败
     * @see CookerConstant
     */
    public int requestSendCookerDataForDemoMode(AllCookerData realData , AllCookerData demoData, boolean needPowerLimit) {
        if (needPowerLimit) {
            if (mPowerLimitManager.updateCookerPower(realData) == 0) {//success
                FTFDataModel model = new FTFDataModel(
                        (byte)demoData.getaMode(),(byte)demoData.getaFireValue(),(byte)demoData.getaTempValue(),
                        (byte)demoData.getbMode(),(byte)demoData.getbFireValue(),(byte)demoData.getbTempValue(),
                        (byte)demoData.getcMode(),(byte)demoData.getcFireValue(),(byte)demoData.getcTempValue(),
                        (byte)demoData.getdMode(),(byte)demoData.getdFireValue(),(byte)demoData.getdTempValue(),
                        (byte)demoData.geteMode(),(byte)demoData.geteFireValue(),(byte)demoData.geteTempValue(),
                        (byte)demoData.getfMode(),(byte)demoData.getfFireValue(),(byte)demoData.getfTempValue(),
                        (byte)demoData.getBluetoothTempValue(),(byte)demoData.getHoodValue(),(byte)demoData.getLightValue()

                );
                sendCookerData(model);
                lastSuccessModel = model;
                return CookerConstant.REQUEST_SEND_COOKER_DATA_RESULT_SUCCESS;

            }else {
                if (lastSuccessModel != null) sendCookerData(lastSuccessModel);
                return CookerConstant.REQUEST_SEND_COOKER_DATA_RESULT_FAIL; //fail
            }
        }else {
            FTFDataModel model = new FTFDataModel(
                    (byte)demoData.getaMode(),(byte)demoData.getaFireValue(),(byte)demoData.getaTempValue(),
                    (byte)demoData.getbMode(),(byte)demoData.getbFireValue(),(byte)demoData.getbTempValue(),
                    (byte)demoData.getcMode(),(byte)demoData.getcFireValue(),(byte)demoData.getcTempValue(),
                    (byte)demoData.getdMode(),(byte)demoData.getdFireValue(),(byte)demoData.getdTempValue(),
                    (byte)demoData.geteMode(),(byte)demoData.geteFireValue(),(byte)demoData.geteTempValue(),
                    (byte)demoData.getfMode(),(byte)demoData.getfFireValue(),(byte)demoData.getfTempValue(),
                    (byte)demoData.getBluetoothTempValue(),(byte)demoData.getHoodValue(),(byte)demoData.getLightValue()

            );
            sendCookerData(model);
            return CookerConstant.REQUEST_SEND_COOKER_DATA_RESULT_SUCCESS;


        }


    }

    /**
     * 请求发送demo模式数据，根据realData 进行功率限制，如果没超出功率，则发送档位值为0 到单片机，并返回发送成功，即0；如果超出功率，不发送任何数据，并且返回发送失败，即1
     * @param realData : 炉头真实设置数据，用于总功率计算并限制功率，如没超出功率限制，则返回发送成功，即是0，反之，则返回1
     * @param needPowerLimit : 是否需要限制功率，如true：需要功率限制，则会根据realData计算总功率；如false：不需要功率限制，直接发送档位值为0的数据到单片机，并返回发送成功。
     * @return 返回数据结果，如0表示发送成功，如1发送失败
     * @see CookerConstant
     */
    public int requestSendCookerDataForDemoMode(AllCookerData realData , boolean needPowerLimit) {
        if (needPowerLimit) {
            if (mPowerLimitManager.updateCookerPower(realData) == 0) {//success
                FTFDataModel model = new FTFDataModel(
                        (byte)realData.getaMode(),(byte)0,(byte)realData.getaTempValue(),
                        (byte)realData.getbMode(),(byte)0,(byte)realData.getbTempValue(),
                        (byte)realData.getcMode(),(byte)0,(byte)realData.getcTempValue(),
                        (byte)realData.getdMode(),(byte)0,(byte)realData.getdTempValue(),
                        (byte)realData.geteMode(),(byte)0,(byte)realData.geteTempValue(),
                        (byte)realData.getfMode(),(byte)0,(byte)realData.getfTempValue(),
                        (byte)realData.getBluetoothTempValue(),(byte)realData.getHoodValue(),(byte)realData.getLightValue()

                );
                sendCookerData(model);
                return CookerConstant.REQUEST_SEND_COOKER_DATA_RESULT_SUCCESS;

            }else {
                return CookerConstant.REQUEST_SEND_COOKER_DATA_RESULT_FAIL; //fail
            }
        }else {
            FTFDataModel model = new FTFDataModel(
                    (byte)realData.getaMode(),(byte)0,(byte)realData.getaTempValue(),
                    (byte)realData.getbMode(),(byte)0,(byte)realData.getbTempValue(),
                    (byte)realData.getcMode(),(byte)0,(byte)realData.getcTempValue(),
                    (byte)realData.getdMode(),(byte)0,(byte)realData.getdTempValue(),
                    (byte)realData.geteMode(),(byte)0,(byte)realData.geteTempValue(),
                    (byte)realData.getfMode(),(byte)0,(byte)realData.getfTempValue(),
                    (byte)realData.getBluetoothTempValue(),(byte)realData.getHoodValue(),(byte)realData.getLightValue()

            );
            sendCookerData(model);
            return CookerConstant.REQUEST_SEND_COOKER_DATA_RESULT_SUCCESS;

        }


    }

    public int requestGetFinalTotalPower() {
        return mPowerLimitManager.getTotalPower();
    }

    public boolean requestSetTotalPower(int power) {
        return mPowerLimitManager.setTotalPower(power);
    }

    public int requestCheckPower(int value, CookerData... data) {

        return mPowerLimitManager.checkPower(value,data);
    }

    public int requestGetCurrentTotalPower() {
        return mPowerLimitManager.getCurrentTotalPower();
    }

    public int requestGetCurrentTotalSingleLeftPower() {
        return mPowerLimitManager.getCurrentTotalSingleLeftPower();
    }

    public int requestGetCurrentTotalSingleRightPower() {
        return mPowerLimitManager.getCurrentTotalSingleRightPower();
    }


    public static final class Builder {
        private static final int DEFAULT_BAUDRATE = 4800;
        private static final String DEFAULT_SERIAL_PORT_PATH = "/dev/ttyS0";//ttyS0
        private int cookerType = CookerConstant.COOKER_TYPE_CURRENT_TYPE;
        private String serialPortPath;
        private Context mContext;
        private int serialBaudrate;
        private int totalPower = PowerConstant.POWER_LIMIT_TOTAL_LIMIT_DEFAULT;
        private OnInductionCookerHardwareManagerListener listener;
        private boolean debugStatus = false;
        public Builder(Context context) {
            this.mContext = context;
        }

        /**
         * 设置串口Rfid设备通讯串口参数，其中包括串口号和波特率，如串口为ttyS1,波特率为115200，则传入serialPortPath = "/dev/ttyS1" , serialBaudrate = 115200
         * 如设置串口参数则使用串口端口通讯，如果不设置串口参数则默认使用USB端口进行通讯
         *
         * @param serialPortPath    串口号
         * @param serialBaudrate    串口波特率
         * @return  this builder
         */
        public Builder setSerailPortParameter(String serialPortPath , int serialBaudrate) {
            this.serialPortPath = serialPortPath;
            this.serialBaudrate = serialBaudrate;
            return this;
        }

        public Builder setSerailPortParameter() {
            this.serialPortPath = DEFAULT_SERIAL_PORT_PATH;
            this.serialBaudrate = DEFAULT_BAUDRATE;
            return this;
        }



        public Builder setCookerHardwareManagerListener(OnInductionCookerHardwareManagerListener listener) {
            this.listener = listener;
            return this;
        }

        /**
         * 设置炉头类型，如KSO60
         *
         * @param type 炉头类型,如KSO60 : CookerConstant.COOKER_TYPE_KSO_60
         * @return the cooker type
         * @see com.ekek.hardwaremodule.constants.CookerConstant
         *
         */
        public Builder setCookerType(int type) {
            this.cookerType = type;
            return this;
        }

        public Builder setTotalPower(int power) {
            this.totalPower  = power;
            return this;
        }

        public Builder setDebug(boolean status) {
            this.debugStatus = status;
            return this;
        }

        public InductionCookerHardwareManager build() {
            return getIstance(mContext , this , listener);
        }
    }

    public void recyle() {
        if (mCommandSendThread != null) {
            mCommandSendThread.supendThread();
        }
        mInductionCookerProtocol.recyle();
        mHardwareManager.closeHardware();

    }


    //Induction cooker protocol
    @Override
    public void onInductionCookerProtocolDataReady(CookerHardwareResponse response) {
        mListener.onCookerHardwareResponse(response);
    }

    public static abstract interface OnInductionCookerHardwareManagerListener {
        public abstract void onCookerHardwareResponse(CookerHardwareResponse response);
    }

}
