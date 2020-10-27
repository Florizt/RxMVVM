package com.rx.rxmvvmlib.util;

import android.util.Log;

import com.rx.rxmvvmlib.BuildConfig;
import com.rx.rxmvvmlib.RxMVVMInitializer;


/**
 * Created by wuwei on 2017/6/3.
 */

/**
 * log工具类
 */
public class LogUtil {

    public static void d(String tag, String message) {
        if (RxMVVMInitializer.getInstance().getAppConfig().isDebugEnable()) {
            Log.d(tag, message);
        }
    }


    public static void i(String tag, String message) {
        if (RxMVVMInitializer.getInstance().getAppConfig().isDebugEnable()) {
            Log.i(tag, message);
        }
    }


    public static void w(String tag, String message) {
        if (RxMVVMInitializer.getInstance().getAppConfig().isDebugEnable()) {
            Log.w(tag, message);
        }
    }


    public static void e(String tag, String message) {
        if (RxMVVMInitializer.getInstance().getAppConfig().isDebugEnable()) {
            Log.e(tag, message);
        }
    }
}
