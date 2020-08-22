package com.ekek.hardwaremodule.utils;

import java.util.List;

/**
 * Created by Samhung on 2017/2/21.
 */

public class DataParseUtil {
    public static String bytesToHexString(byte src[], int length)
    {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0 || length <= 0)
            return null;
        for (int i = 0; i < length && i < src.length; i++)
        {
            int v = src[i] & 0xff;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2)
                stringBuilder.append(0);
            stringBuilder.append((new StringBuilder(String.valueOf(hv))).append(" ").toString());
        }

        return stringBuilder.toString().toUpperCase();
    }

    public static byte[] ByteListToByteArray(List lstBytes)
    {
        byte packet[] = new byte[lstBytes.size()];
        for (int i = 0; i < packet.length; i++)
            packet[i] = ((Byte)lstBytes.get(i)).byteValue();

        return packet;
    }

    public static String intsToHexString(int src[], int length)
    {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0 || length <= 0)
            return null;
        for (int i = 0; i < length && i < src.length; i++)
        {
            int v = src[i];
            String hv = Integer.toHexString(v);
            if (hv.length() < 2)
                stringBuilder.append(0);
            stringBuilder.append((new StringBuilder(String.valueOf(hv))).append(" ").toString());
        }

        return stringBuilder.toString().toUpperCase();
    }

    public static byte calculateLrc(byte[] buf){
        byte lrc = 0x00;
        for (int i = 0; i < buf.length - 1; i++) {
            lrc = (byte) (lrc ^ buf[i]);
        }
        return lrc;
    }

    public static byte[] intArrayToByteArray(int[] array) {
        byte[] data = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            data[i] = (byte) array[i];
        }
        return data;
    }

    public static int[] byteArrayToIntArray(byte[] array) {
        int[] data = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            data[i] = array[i] & 0xff;
        }
        return data;
    }
}
