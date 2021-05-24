package com.rx.rxmvvmlib.util;

import android.os.Build;
import android.text.TextUtils;

/**
 * Created by wuwei
 * 2019/12/7
 * 佛祖保佑       永无BUG
 */
public class RomUtil {

    public static String getMobileType() {
        return Build.MANUFACTURER;
    }

    public static boolean isXiaomi() {
        return getMobileType().equals("Xiaomi");
    }

    public static boolean isHuawei() {
        return getMobileType().equals("HUAWEI");
    }

    public static boolean isVivo() {
        return getMobileType().equals("vivo");
    }

    public static boolean isOppo() {
        return getMobileType().equals("OPPO");
    }

    public static boolean isSamsung() {
        return getMobileType().equals("samsung");
    }

    public static boolean isMeizu() {
        return getMobileType().equals("Meizu");
    }

    public static boolean isLetv() {
        return getMobileType().equals("Letv");
    }

    public static boolean is360() {
        return getMobileType().equals("ulong");
    }

    public static double getOppoVer(String ver) {
        if (TextUtils.isEmpty(ver)) {
            return 0;
        }
        String pro = ver.replaceAll("V", "");
        if (TextUtils.isEmpty(pro)) {
            return 0;
        } else if (pro.length() == 1) {
            return Double.valueOf(pro);
        } else if (pro.length() == 2) {
            if (!pro.contains(".")) {
                return Double.valueOf(pro);
            } else {
                return Double.valueOf(pro.substring(0, 1));
            }
        } else {
            return Double.valueOf(pro.substring(0, 3));
        }
    }
}
