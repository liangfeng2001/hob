package com.ekek.hardwaremodule.entity;

/**
 * Rfid模块请求操作
 *
 * Created by Samhung on 2017/9/3.
 */

public class RequestAction {

    public static final int REQUEST_ACTION_NO_OPERATION = -1;

    public static final int REQUEST_ACTION_OPERATE_MODULE = 0;

    /*****************************************************************************************/
    /*                                                                                       */
    /*                                   14443A                                              */
    /*                                                                                       */
    /*****************************************************************************************/
    /**
     * 请求认证14443A协议卡秘钥
     *
     * */
    public static final int REQUEST_ACTION_REQUEST_PICC_AUTH_KEY = 1;
    /**
     * 请求激活14443A协议卡
     *
     * */
    public static final int REQUEST_ACTION_REQUEST_PICC_ACTIVATE = 2;
    /**
     * 请求带参数激活14443A协议卡秘钥
     *
     * */
    public static final int REQUEST_ACTION_REQUEST_PICC_ACTIVATE_WITH_PARAMETER = 3;
    /**
     * 请求读取14443A协议卡的块数据
     *
     * */
    public static final int REQUEST_ACTION_REQUEST_PICC_READ_A = 4;
    /**
     * 请求写入数据到14443A协议卡的数据块
     *
     * */
    public static final int REQUEST_ACTION_REQUEST_PICC_WRITE_A = 5;

    /**
     * 请求写入数据到Mifare Ultralight卡的数据块
     *
     * */
    public static final int REQUEST_ACTION_REQUEST_PICC_WRITE_UL = 6;

    /**
     * 请求初始化电子钱包
     *
     * */
    public static final int REQUEST_ACTION_REQUEST_PICC_INIT_VL = 7;

    /**
     * 请求操作电子钱包
     *
     * */
    public static final int REQUEST_ACTION_REQUEST_PICC_OPERATE_VL = 8;

    /**
     * 请求备份电子钱包值
     *
     * */
    public static final int REQUEST_ACTION_REQUEST_PICC_BAK_A = 9;

    /**
     * 请求读取电子钱包值
     *
     * */
    public static final int REQUEST_ACTION_REQUEST_PICC_READ_VALUE = 10;

    /**
     * 请求卡片复位
     *
     * */
    public static final int REQUEST_ACTION_REQUEST_PICC_RESET = 11;

    /**
     * 请求应答，使CPU卡进入ISO14443-4模式，返回卡片的ATS数据
     *
     * */
    public static final int REQUEST_ACTION_REQUEST_PICC_ATS = 12;

    /**
     * 请求发送APDU指令，给14443-4模式下的CPU卡发送APDU指令
     *
     * */
    public static final int REQUEST_ACTION_REQUEST_PICC_TPCL = 13;

    /**
     * 请求激活ISO14443B卡
     *
     * */
    public static final int REQUEST_ACTION_REQUEST_PICC_ACTIVATE_B = 14;

    /**
     * 请求ISO14443B卡数据交互
     *
     * */
    public static final int REQUEST_ACTION_REQUEST_PICC_TRANSFER = 15;

    /*****************************************************************************************/
    /*                                                                                       */
    /*                                   15693                                               */
    /*                                                                                       */
    /*****************************************************************************************/
    /**
     * 请求寻找15693协议的卡
     *
     * */
    public static final int REQUEST_ACTION_REQUEST_ISO15693_INVENTORY = 101;
    /**
     * 请求读取15693协议的卡的块数据
     *
     * */
    public static final int REQUEST_ACTION_REQUEST_ISO15693_READ_BLOCK = 102;
    /**
     * 请求写入数据到15693协议的卡的数据块
     *
     * */
    public static final int REQUEST_ACTION_REQUEST_ISO15693_WRITE_BLOCK = 103;
    /**
     * 请求锁定15693协议卡的数据块
     *
     * */
    public static final int REQUEST_ACTION_REQUEST_ISO15693_LOCK_BLOCK = 104;
    /**
     * 请求写入15693协议卡的AFI
     *
     * */
    public static final int REQUEST_ACTION_REQUEST_ISO15693_WRITE_AFI = 105;
    /**
     * 请求锁定15693协议卡的AFI
     *
     * */
    public static final int REQUEST_ACTION_REQUEST_ISO15693_LOCK_AFI = 106;
    /**
     * 请求写入15693协议卡的DSFID
     *
     * */
    public static final int REQUEST_ACTION_REQUEST_ISO15693_WRITE_DSFID = 107;
    /**
     * 请求锁定15693协议卡的DSFID
     *
     * */
    public static final int REQUEST_ACTION_REQUEST_ISO15693_LOCK_DSFID = 108;
    /**
     * 请求获取15693协议卡信息
     *
     * */
    public static final int REQUEST_ACTION_REQUEST_ISO15693_GET_SYS_INFOR = 109;

    /*****************************************************************************************/
    /*                                                                                       */
    /*                                   Rfid module                                         */
    /*                                                                                       */
    /*****************************************************************************************/

    /**
     * Brd Rfid模块响应
     *
     * */
    public static final int REQUEST_ACTION_BRD_RFID_HARDWARE_RESPONSE = 1000;
    /**
     * 请求初始化Rfid模块
     *
     * */
    public static final int REQUEST_ACTION_REQUEST_INITIALIZE_HARDWARE = 1001;
    /**
     * 请求控制Rfid模块蜂鸣器响
     *
     * */
    public static final int REQUEST_ACTION_REQUEST_MODULE_SET_BUZZER = 1002;
    /**
     * 请求设置Rfid模块的波特率
     *
     * */
    public static final int REQUEST_ACTION_REQUEST_MODULE_SET_BAUDRATE = 1003;
    /**
     * 请求控制Rfid模块的LED闪烁
     *
     * */
    public static final int REQUEST_ACTION_REQUEST_MODULE_SET_LED = 1004;
    /**
     * 请求Rfid模块的进入休眠
     *
     * */
    public static final int REQUEST_ACTION_REQUEST_MODULE_SET_HALT = 1005;
    /**
     * 请求Rfid模块的从休眠恢复进入工作状态
     *
     * */
    public static final int REQUEST_ACTION_REQUEST_MODULE_SET_WAKE = 1006;

    /**
     * 请求打开设备
     *
     * */
    public static final int REQUEST_ACTION_BRD_RFID_OPEN_DEVICE = 1007;

    /**
     * 请求关闭设备
     *
     * */
    public static final int REQUEST_ACTION_BRD_RFID_CLOSE_DEVICE = 1008;



}
