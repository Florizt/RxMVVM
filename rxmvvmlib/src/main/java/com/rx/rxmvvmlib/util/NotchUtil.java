package com.rx.rxmvvmlib.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.View;
import android.view.WindowInsets;

import java.lang.reflect.Method;

/**
 * Created by wuwei
 * 2018/7/27
 * 佛祖保佑       永无BUG
 */
public class NotchUtil {

    /**
     * 是否是小米刘海屏
     *
     * @return
     */
    public static boolean hasNotchAtXiaomi() {
        if (getInt("ro.miui.notch", 0) == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否是华为刘海屏
     *
     * @param context
     * @return
     */
    public static boolean hasNotchAtHuawei(Context context) {
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class HwNotchSizeUtil = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            return (boolean) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            Log.e("Notch", "hasNotchAtHuawei ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("Notch", "hasNotchAtHuawei NoSuchMethodException");
        } catch (Exception e) {
            Log.e("Notch", "hasNotchAtHuawei Exception");
        }
        return false;
    }

    /**
     * 华为获取刘海尺寸：width、height
     * int[0]值为刘海宽度 int[1]值为刘海高度
     *
     * @param context
     * @return
     */
    public static int[] getNotchSizeAtHuawei(Context context) {
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("getNotchSize");
            return (int[]) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            Log.e("Notch", "getNotchSizeAtHuawei ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("Notch", "getNotchSizeAtHuawei NoSuchMethodException");
        } catch (Exception e) {
            Log.e("Notch", "getNotchSizeAtHuawei Exception");
        }
        return new int[]{0, 0};
    }

    /**
     * 是否是vivo刘海屏
     *
     * @param context
     * @return
     */
    public static boolean hasNotchAtVivo(Context context) {
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class FtFeature = classLoader.loadClass("android.util.FtFeature");
            Method method = FtFeature.getMethod("isFeatureSupport", int.class);
            return (boolean) method.invoke(FtFeature, 0x00000020);
        } catch (ClassNotFoundException e) {
            Log.e("Notch", "hasNotchAtVivo ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("Notch", "hasNotchAtVivo NoSuchMethodException");
        } catch (Exception e) {
            Log.e("Notch", "hasNotchAtVivo Exception");
        }
        return false;
    }

    /**
     * 是否是oppo刘海屏
     *
     * @param context
     * @return
     */
    public static boolean hasNotchAtOPPO(Context context) {
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    /**
     * Android P 刘海屏判断
     *
     * @param activity
     * @return
     */
    public static DisplayCutout isAndroidP(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        if (decorView != null && android.os.Build.VERSION.SDK_INT >= 28) {
            WindowInsets windowInsets = decorView.getRootWindowInsets();
            if (windowInsets != null)
                return windowInsets.getDisplayCutout();
        }
        return null;
    }

    /**
     * 判断是否是刘海屏
     *
     * @return
     */
    public static boolean hasNotchScreen(Activity activity) {
        if (hasNotchAtXiaomi() || hasNotchAtHuawei(activity) || hasNotchAtOPPO(activity)
                || hasNotchAtVivo(activity) || isAndroidP(activity) != null) { //TODO 各种品牌
            return true;
        }

        return false;
    }

    private static int getInt(final String key, final int def) {
        try {
            Method intMethod = Class.forName("android.os.SystemProperties")
                    .getMethod("getInt", String.class, int.class);
            return (Integer) intMethod.invoke(null, key, def);
        } catch (Exception e) {
            return def;
        }
    }
}
