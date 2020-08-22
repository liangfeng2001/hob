package com.ekek.hardwaremodule.serial;

/**
 * Created by Samhung on 2017/2/21.
 */

public class SerialError {
    public static final int SERIAL_ERROR_INVALID_PARAMETER = 0;
    public static final int SERIAL_ERROR_SERIAL_NO_PERMISSION = 1;
    public static final int SERIAL_ERROR_IO_ERROR = 2;
    public static final int SERIAL_ERROR_SERIAL_PORT_NO_EXIST = 3;



    private static final String SERIAL_ERROR_WRONG_CONFIGURATION_MESSAGE = "Invalid parameter";
    private static final String SERIAL_ERROR_SERIAL_NO_PERMISSION_MESSAGE = "Serial no permission";
    private static final String SERIAL_ERROR_IO_ERROR_MESSAGE = "IO error";
    private static final String SERIAL_ERROR_UNKNOWN_ERROR = "Unknown error";
    private static final String SERIAL_ERROR_SERIAL_PORT_NO_EXIST_MESSAGE = "Serail port doesn't exist";


    public static String getErrorMessage(int error) {
        switch (error) {
            case SERIAL_ERROR_INVALID_PARAMETER:
                return SERIAL_ERROR_WRONG_CONFIGURATION_MESSAGE;
            case SERIAL_ERROR_SERIAL_NO_PERMISSION:
                return SERIAL_ERROR_SERIAL_NO_PERMISSION_MESSAGE;
            case SERIAL_ERROR_IO_ERROR:
                return SERIAL_ERROR_IO_ERROR_MESSAGE;
            case SERIAL_ERROR_SERIAL_PORT_NO_EXIST:
                return SERIAL_ERROR_SERIAL_PORT_NO_EXIST_MESSAGE;
            default:
                return SERIAL_ERROR_UNKNOWN_ERROR;
        }
    }
}
