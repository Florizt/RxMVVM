package com.rx.rxmvvmlib.util;

import com.dgrlucky.log.LogX;


/**
 * Created by wuwei on 2017/6/3.
 */

/**
 * log工具类
 */
public class LogUtil {
    private static boolean debug;

    public static void setDebug(boolean debug) {
        LogUtil.debug = debug;
    }

    public static void d(Object object) {
        if (debug) {
            LogX.d(object);
        }
    }

    public static void d(String message, Object... args) {
        if (debug) {
            LogX.d(message, args);
        }
    }

    public static void i(Object object) {
        if (debug) {
            LogX.i(object);
        }
    }

    public static void i(String message, Object... args) {
        if (debug) {
            LogX.i(message, args);
        }
    }

    public static void w(Object object) {
        if (debug) {
            LogX.w(object);
        }
    }

    public static void w(String message, Object... args) {
        if (debug) {
            LogX.w(message, args);
        }
    }

    public static void e(Object object) {
        if (debug) {
            LogX.e(object);
        }
    }

    public static void e(String message, Object... args) {
        if (debug) {
            LogX.e(message, args);
        }
    }
}
