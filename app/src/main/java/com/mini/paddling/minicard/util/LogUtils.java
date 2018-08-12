package com.mini.paddling.minicard.util;

import android.util.Log;

public class LogUtils {
    public static void v(String tag, Throwable throwable) {
        Log.v(tag, throwable.toString()  + " " + (throwable.getMessage() != null ? throwable.getMessage() : ""));
    }
    public static void d(String tag, Throwable throwable) {
        Log.d(tag, throwable.toString()  + " " + (throwable.getMessage() != null ? throwable.getMessage() : ""));
    }
    public static void i(String tag, Throwable throwable) {
        Log.i(tag, throwable.toString()  + " " + (throwable.getMessage() != null ? throwable.getMessage() : ""));
    }
    public static void w(String tag, Throwable throwable) {
        Log.w(tag, throwable.toString()  + " " + (throwable.getMessage() != null ? throwable.getMessage() : ""));
    }
    public static void e(String tag, Throwable throwable) {
        Log.e(tag, throwable.toString()  + " " + (throwable.getMessage() != null ? throwable.getMessage() : ""));
    }
}
