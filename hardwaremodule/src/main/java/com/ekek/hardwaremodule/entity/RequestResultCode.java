package com.ekek.hardwaremodule.entity;

/**
 * 请求Rfid模块操作结果代码
 *
 * Created by Samhung on 2017/9/3.
 */

public class RequestResultCode {
    /**
     * 请求Rfid模块操作失败代码
     *
     */
    public static final int REQUEST_RESULT_CODE_REQUEST_FAIL = 0;
    /**
     * 请求Rfid模块操作成功代码
     *
     */
    public static final int REQUEST_RESULT_CODE_REQUEST_SUCCESS = 1;

    /**
     * Brd Rfid 模块启动成功
     *
     */
    public static final int REQUEST_RESULT_CODE_BRD_RFID_DEVICE_OPEN_SUCCESS = 2;

    /**
     * Brd Rfid 模块关闭成功
     *
     */
    public static final int REQUEST_RESULT_CODE_BRD_RFID_DEVICE_CLOSE_SUCCESS = 3;
}
