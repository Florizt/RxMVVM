package com.rx.rxmvvmlib.util;

import com.dgrlucky.log.LogX;
import com.rx.rxmvvmlib.RxMVVMInit;


/**
 * Created by wuwei on 2017/6/3.
 */

/**
 * log工具类
 */
public class LogUtil {

    public static void d(Object object) {
        if (RxMVVMInit.config.debugEnable) {
            LogX.d(object);
        }
    }

    public static void d(String message, Object... args) {
        if (RxMVVMInit.config.debugEnable) {
            LogX.d(message, args);
        }
    }

    public static void i(Object object) {
        if (RxMVVMInit.config.debugEnable) {
            LogX.i(object);
        }
    }

    public static void i(String message, Object... args) {
        if (RxMVVMInit.config.debugEnable) {
            LogX.i(message, args);
        }
    }

    public static void w(Object object) {
        if (RxMVVMInit.config.debugEnable) {
            LogX.w(object);
        }
    }

    public static void w(String message, Object... args) {
        if (RxMVVMInit.config.debugEnable) {
            LogX.w(message, args);
        }
    }

    public static void e(Object object) {
        if (RxMVVMInit.config.debugEnable) {
            LogX.e(object);
        }
    }

    public static void e(String message, Object... args) {
        if (RxMVVMInit.config.debugEnable) {
            LogX.e(message, args);
        }
    }
}
