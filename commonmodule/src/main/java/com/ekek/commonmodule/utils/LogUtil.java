package com.ekek.commonmodule.utils;

import android.util.Log;


/**
 * Created by Samhung on 2016/9/10.
 */
public class LogUtil {
    static String className;//类名
    static String methodName;//方法名
    static int lineNumber;//行数
    static String TAG = "EKEKTAG";


    public static boolean isDebuggable() {
        //return BuildConfig.DEBUG;
        return true;
    }

    private static String createLog(String log ) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(methodName);
        buffer.append("(").append(className).append(":").append(lineNumber).append(")");
        buffer.append(log);
        return buffer.toString();
    }
    private static String createLog(
            String log,
            String methodName,
            String className,
            int lineNumber) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(methodName);
        buffer.append("(").append(className).append(":").append(lineNumber).append(")");
        buffer.append(log);
        return buffer.toString();
    }

    private static void getMethodNames(StackTraceElement[] sElements){
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }


    public static void e(String message){
        if (!isDebuggable())
            return;

        // Throwable instance must be created before any methods
        getMethodNames(new Throwable().getStackTrace());
        Log.e(className, createLog(message));
    }


    public static void i(String message){
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.i(className, createLog(message));
    }

    public static void d(String message){
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.d(className, createLog(message));
    }

    public static void d(String message, boolean includeStack) {
        if (!isDebuggable())
            return;
        Log.d(TAG, message);
        if (includeStack) {
            StackTraceElement[] elements = new Throwable().getStackTrace();
            Log.d(TAG, "-------- Stack Trace ------------------------------------------");
            for (int i = 1; i < elements.length; i++) {
                String cName = elements[i].getFileName();
                String mName = elements[i].getMethodName();
                int lineNum = elements[i].getLineNumber();
                Log.d(TAG, createLog("", mName, cName, lineNum));
            }
            Log.d(TAG, "-------- Stack Trace ------------------------------------------");
        }
    }


    public static void v(String message){
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.v(className, createLog(message));
    }

    public static void w(String message){
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.w(className, createLog(message));
    }

    public static void wtf(String message){
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.wtf(className, createLog(message));
    }

}
